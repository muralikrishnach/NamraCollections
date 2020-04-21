package com.jayam.impactapp;

import java.util.ArrayList;
import java.util.Collections;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.common.RecieptComparator;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.LastRecieptBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.printer.BluetoothChatService;
import com.jayam.impactapp.utils.PrintUtils;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.StringUtils;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionACK_Centerwise extends Base implements PrintListner {
    private LinearLayout llAck;
    private TextView tvrecptNumber, tvDate, tvCenterName, tvTotalDemand, tvCollection, tvAttendense, tvMeetingStart,
	    tvMeetingEndTime;
    private ArrayList<RegularDemandsDO> alArrayList, alArrayList_Transactions, alArrayList_CollAndSavings;
    private String Center;
    private TrnsactionsBL trnsactionsBL;
    private Button btnMenu, btnPrint;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String TOAST = "toast";
    private ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    private IntialParametrsBL intialParametrsBL;
    float totAmt = 0;
    float totAmt_Collected = 0;
    float totDemand = 0;
    float collectedAmt = 0;
    private boolean isPrintTaken = false;
    String TranScode;
    LastRecieptBL recptBL;
    String Print;

    @Override
    public void initialize() {
	recptBL = new LastRecieptBL();
	Center = getIntent().getExtras().getString("Center");
	TranScode = getIntent().getExtras().getString("LastTranScode");
	intializeControlles();
	RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	alArrayList = regularDemandsBL.SelectAll(Center, "CNo",TransactionACK_Centerwise.this);

	trnsactionsBL = new TrnsactionsBL();
	String CollAmt = trnsactionsBL.getTotalCollAmt(TranScode,TransactionACK_Centerwise.this);

	tvCollection.setText("" + CollAmt);

	intialParametrsBL = new IntialParametrsBL();
	alIntialParametrsDOs = intialParametrsBL.SelectAll(TransactionACK_Centerwise.this);

	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
	Print = intialParametrsDO.PrintValidate;
	if (Print.equals("0")) {
	    btnPrint.setVisibility(View.GONE);
	}

	tvDate.setText("" + alArrayList.get(0).DemandDate);
	tvCenterName.setText("" + alArrayList.get(0).CName);
	// tvgroupname.setText(""+alArrayList.get(0).GroupName);

	int attendense = 0;
	for (int i = 0; i < alArrayList.size(); i++) {
	    try {
		totDemand = totDemand + Float.valueOf(alArrayList.get(i).DemandTotal).floatValue()
			+ Float.valueOf(alArrayList.get(i).ODAmount).floatValue();
		collectedAmt = collectedAmt + Float.valueOf(alArrayList.get(i).collectedAmount).floatValue();
		if (alArrayList.get(i).Attendance.equalsIgnoreCase("1")) {
		    attendense = attendense + 1;
		}
	    } catch (Exception e) {
		Log.e("Exception", "" + e.toString());
	    }
	}

	tvTotalDemand.setText("" + totDemand);

	// tvCollection.setText(""+collectedAmt);
	// tvCollection.setText(""+CollAmt);
	tvAttendense.setText("" + attendense + " / " + alArrayList.size());

	tvMeetingStart.setText("" + alArrayList.get(0).meetingStartTime);
	tvMeetingEndTime.setText("" + alArrayList.get(0).meetingEndTime);

	if (alArrayList_Transactions != null && alArrayList_Transactions.size() > 2) {
	    Collections.sort(alArrayList_Transactions, new RecieptComparator());
	}

	// tvrecptNumberTo.setText(""+alArrayList_Transactions.get(alArrayList_Transactions.size()-1).ReciptNumber);
	tvrecptNumber.setText("" + TranScode);

	btnMenu.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (Print.equals("1")) {
		    if (!isPrintTaken) {
			showAlertDailog("Receipt is not printed. Are you sure want to exit the Menu ?", "Yes", "No",
				new CustomDailoglistner() {
			    @Override
			    public void onPossitiveButtonClick(DialogInterface dialog) {
				dialog.dismiss();
				ShowLoader();
				new Thread(new Runnable() {
				    @Override
				    public void run() {
					recptBL.delete();
					recptBL.insert(TranScode, "N", "R",TransactionACK_Centerwise.this);
					trnsactionsBL.updatePrintFlag(TranScode, "N",TransactionACK_Centerwise.this);
					runOnUiThread(new Runnable() {
					    @Override
					    public void run() {
						HideLoader();
						setResult(AppConstants.RESULTCODE_HOME);
						finish();
					    }
					});
				    }
				}).start();
			    }

			    @Override
			    public void onNegativeButtonClick(DialogInterface dialog) {
				dialog.dismiss();
			    }
			});
		    } else {
			showAlertDailog("Receipt Printed Y / N ", "Yes", "No", new CustomDailoglistner() {
			    @Override
			    public void onPossitiveButtonClick(DialogInterface dialog) {
				recptBL.updateLastReceiptPrintFlag(TranScode, "Y",TransactionACK_Centerwise.this);
				trnsactionsBL.updatePrintFlag(TranScode, "Y",TransactionACK_Centerwise.this);
				dialog.dismiss();
				setResult(AppConstants.RESULTCODE_HOME);
				finish();
			    }

			    @Override
			    public void onNegativeButtonClick(DialogInterface dialog) {
				dialog.dismiss();

				showAlertDailog(
					"WARNING ! Do you want to overwrite printed confirmation, pls. confirm Y / N.",
					"Yes", "No", new CustomDailoglistner() {
				    @Override
				    public void onPossitiveButtonClick(DialogInterface dialog) {
					recptBL.updateLastReceiptPrintFlag(TranScode, "N",TransactionACK_Centerwise.this);
					trnsactionsBL.updatePrintFlag(TranScode, "N",TransactionACK_Centerwise.this);
					dialog.dismiss();
					setResult(AppConstants.RESULTCODE_HOME);
					finish();
				    }

				    @Override
				    public void onNegativeButtonClick(DialogInterface dialog) {
					recptBL.updateLastReceiptPrintFlag(TranScode, "Y",TransactionACK_Centerwise.this);
					trnsactionsBL.updatePrintFlag(TranScode, "N",TransactionACK_Centerwise.this);
					dialog.dismiss();
					setResult(AppConstants.RESULTCODE_HOME);
					finish();
				    }
				});
			    }
			});
		    }
		} else {
		    setResult(AppConstants.RESULTCODE_HOME);
		    finish();
		}
	    }
	});

	btnPrint.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (!isPrintTaken) {
		    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
		    if (bluetooth.isEnabled()) {
			ShowLoader();
			new Thread(new Runnable() {
			    @Override
			    public void run() {
				recptBL.delete();
				recptBL.insert(TranScode, "N", "R",TransactionACK_Centerwise.this);
				trnsactionsBL.updatePrintFlag(TranScode, "Y",TransactionACK_Centerwise.this);
				runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
					HideLoader();
					KeyValue keyValue_transtype = new KeyValue(AppConstants.TransactioType,
						"regular");
					SharedPrefUtils.setValue(TransactionACK_Centerwise.this, AppConstants.pref_name,
						keyValue_transtype);
					KeyValue keyValue_transcode = new KeyValue(AppConstants.TransactionCode,
						TranScode);
					SharedPrefUtils.setValue(TransactionACK_Centerwise.this, AppConstants.pref_name,
						keyValue_transcode);
					PrintUtils printUtils = new PrintUtils(TransactionACK_Centerwise.this,
						TransactionACK_Centerwise.this);
					printUtils.print();
				    }
				});
			    }
			}).start();
		    } else {
			showAlertDailog("Please Switch On Mobile Bluetooth");
			return;
		    }
		} else {
		    showAlertDailog("Receipt Printed Y / N ", "Yes", "No", new CustomDailoglistner() {
			@Override
			public void onPossitiveButtonClick(DialogInterface dialog) {
			    recptBL.updateLastReceiptPrintFlag(TranScode, "Y",TransactionACK_Centerwise.this);
			    trnsactionsBL.updatePrintFlag(TranScode, "Y",TransactionACK_Centerwise.this);
			    dialog.dismiss();
			    setResult(AppConstants.RESULTCODE_HOME);
			    finish();
			}

			@Override
			public void onNegativeButtonClick(DialogInterface dialog) {
			    dialog.dismiss();
			    showAlertDailog(
				    "WARNING ! Do you want to overwrite printed confirmation, pls. confirm Y / N.",
				    "Yes", "No", new CustomDailoglistner() {
				@Override
				public void onPossitiveButtonClick(DialogInterface dialog) {
				    recptBL.updateLastReceiptPrintFlag(TranScode, "N",TransactionACK_Centerwise.this);
				    trnsactionsBL.updatePrintFlag(TranScode, "N",TransactionACK_Centerwise.this);
				    dialog.dismiss();
				    setResult(AppConstants.RESULTCODE_HOME);
				    finish();
				}

				@Override
				public void onNegativeButtonClick(DialogInterface dialog) {
				    recptBL.updateLastReceiptPrintFlag(TranScode, "Y",TransactionACK_Centerwise.this);
				    trnsactionsBL.updatePrintFlag(TranScode, "N",TransactionACK_Centerwise.this);
				    dialog.dismiss();
				    setResult(AppConstants.RESULTCODE_HOME);
				    finish();
				}
			    });
			}
		    });
		}
	    }
	});

	ivHome.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		setResult(AppConstants.RESULTCODE_HOME);
		finish();
	    }
	});

	ivLogout.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		setResult(AppConstants.RESULTCODE_LOGOUT);
		finish();
	    }
	});

    }

    public void intializeControlles() {
	llAck = (LinearLayout) inflater.inflate(R.layout.transactionack, null);

	tvrecptNumber = (TextView) llAck.findViewById(R.id.tvrecptNumber);

	tvDate = (TextView) llAck.findViewById(R.id.tvDate);
	tvCenterName = (TextView) llAck.findViewById(R.id.tvCenterName);
	LinearLayout llGroup = (LinearLayout) llAck.findViewById(R.id.llGroup);
	tvTotalDemand = (TextView) llAck.findViewById(R.id.tvTotalDemand);
	tvCollection = (TextView) llAck.findViewById(R.id.tvCollection);
	tvAttendense = (TextView) llAck.findViewById(R.id.tvAttendense);
	tvMeetingStart = (TextView) llAck.findViewById(R.id.tvMeetingStart);
	tvMeetingEndTime = (TextView) llAck.findViewById(R.id.tvMeetingEndTime);
	btnMenu = (Button) llAck.findViewById(R.id.btnMenu);
	btnPrint = (Button) llAck.findViewById(R.id.btnPrint);

	svBase.setVisibility(View.GONE);
	llGroup.setVisibility(View.GONE);

	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	hidehomeIcons();
	tvHeader.setText("Transaction Acknowledgement");
    }

    private void connectDevice(String address, String message, int fontSize, ArrayList<RegularDemandsDO> alArrayList) {
	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	mChatService = new BluetoothChatService(this, mHandler);

	mChatService.setvalues(totAmt + "", totAmt_Collected + "");
	// Get the device MAC address
	// String address = data.getExtras()
	// .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
	Toast.makeText(this, "Device connecting " + address, Toast.LENGTH_SHORT).show();
	// Get the BluetoothDevice object
	BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

	// Attempt to connect to the device
	// mChatService.connect(device, message,fontSize,alArrayList);
    }

    private final Handler mHandler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MESSAGE_WRITE:
		// byte[] writeBuf = (byte[]) msg.obj;
		// construct a string from the buffer
		break;
	    case MESSAGE_TOAST:
		Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
		break;
	    }

	}
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.ACTION_DOWN) {
	    return false;
	} else {
	    return true;
	}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	if (resultCode == AppConstants.RESULTCODE_LOGOUT) {
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_HOME) {
	    setResult(resultCode);
	    finish();
	}
    }

    @Override
    public PrintDetailsDO getprintObject() {
	// PrintDetailsDO detailsDO = new PrintDetailsDO();
	PrintValues printValues = new PrintValues();
	TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
	ArrayList<RegularDemandsDO> alDos = trnsactionsBL.SelectDistinctGroupsFromCenter(Center,TransactionACK_Centerwise.this);
	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
	if (intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1")) {
	    for (int i = 0; i < alDos.size(); i++) {
		TrnsactionsBL trnsactionsBL1 = new TrnsactionsBL();
		ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL1.SelectAll(TranScode, alDos.get(i).GNo, "Center",TransactionACK_Centerwise.this);
		for (int j = 0; j < alArrayList.size(); j++) {
		    RegularDemandsDO obj = alArrayList.get(i);
		    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		    RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",TransactionACK_Centerwise.this).get(0);
		    String CollAmt = trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,TransactionACK_Centerwise.this);
		    String header = intialParametrsDO.ReceiptHeader;
		    String[] head = header.split("@");
		    String head1 = head[0];
		    String head2 = head[1];
		    String head3 = head[2];
		    printValues.add(head1, "true");
		    String recnum = StringUtils.getRecieptNumber(obj,TransactionACK_Centerwise.this);
		    if (head2.equals("0") || head2.equals("") || head2.equals("null") || head2.equals("NULL")
			    || head2.equals("Null")) {
		    } else {
			printValues.add(head2, "true");
		    }
		    if (head3.equals("0") || head3.equals("") || head3.equals("null") || head3.equals("NULL")
			    || head3.equals("Null")) {
		    } else {
			printValues.add(head3, "true");
		    }
		    printValues.add(" ", "false");
		    if (intialParametrsDO.AgentCopy.equalsIgnoreCase("1")) {
			printValues.add("Customer Copy", "true");
		    }
		    printValues.add("Transaction Acknowledgement", "true");
		    printValues.add("---------------------------", "true");
		    printValues.add(" ", "false");
		    printValues.add("R.No:" + recnum, "false");
		    printValues.add("Date:" + obj.DemandDate, "false");
		    printValues.add(" ", "false");
		    String URL = SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
		    if (URL.equals("Yes")) {
			printValues.add("Center Name:" + obj.CName, "false");
			printValues.add("SHG Name:" + obj.GroupName, "false");
			printValues.add("Member Name:" + obj.MemberName + "(" + regularDemandsDO.MemberCode + ")",
				"false");
			printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			if (intialParametrsDO.InstRequired.equalsIgnoreCase("1")) {
			    printValues.add("Meeting No:" + regularDemandsDO.InstallNo, "false");
			    // printValues.add("Next Meeting
			    // Date:"+regularDemandsDO.NextRepayDate,"false");
			}
			float DmdTot = Float.valueOf(regularDemandsDO.DemandTotal)
				+ Float.valueOf(regularDemandsDO.ODAmount);
			printValues.add("Planned Savings:" + DmdTot, "false");
			printValues.add("Savings Collected:" + obj.collectedAmount, "false");

		    } else {
			printValues.add("Center:" + obj.CName, "false");
			printValues.add("Group:" + obj.GroupName, "false");
			printValues.add("Member:" + obj.MemberName + "(" + regularDemandsDO.MemberCode + ")", "false");
			printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			if (intialParametrsDO.InstRequired.equalsIgnoreCase("1")) {
			    printValues.add("Installment No:" + regularDemandsDO.InstallNo, "false");
			    printValues.add("Next Installment Date:" + regularDemandsDO.NextRepayDate, "false");
			}

			float DmdTot = Float.valueOf(regularDemandsDO.DemandTotal)
				+ Float.valueOf(regularDemandsDO.ODAmount);
			printValues.add("Demand:" + DmdTot, "false");
			printValues.add("collection Amt:" + obj.collectedAmount, "false");
			float OSAmt = Float.valueOf(obj.OSAmt) - Float.valueOf(CollAmt);
			Log.d("mfimo", "act os amt:" + obj.OSAmt + " collection amt:" + CollAmt);
			if (OSAmt < 0) {
			    printValues.add("Curr OS(P+I):" + 0.0, "false");
			} else {
			    printValues.add("Curr OS(P+I):" + OSAmt, "false");
			}
		    }
		    printValues.add(" ", "false");
		    printValues.add(intialParametrsDO.ReceiptFooter, "true");
		}

	    }

	    if (intialParametrsDO.AgentCopy.equalsIgnoreCase("1")) {
		// agent copy
		for (int i = 0; i < alDos.size(); i++) {

		    // print individual reciepts with header and footer
		    // chek for agent copy if it is 1 print one more copy for
		    // agent with 'Agent' as header

		    TrnsactionsBL trnsactionsBL1 = new TrnsactionsBL();
		    ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL1.SelectAll(TranScode, alDos.get(i).GNo, "Center",TransactionACK_Centerwise.this);
		    for (int j = 0; j < alArrayList.size(); j++) {
			RegularDemandsDO obj = alArrayList.get(i);
			RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
			RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",TransactionACK_Centerwise.this).get(0);
			String CollAmt = trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,TransactionACK_Centerwise.this);
			String header = intialParametrsDO.ReceiptHeader;
			String[] head = header.split("@");
			String head1 = head[0];
			String head2 = head[1];
			String head3 = head[2];
			String recnum = StringUtils.getRecieptNumber(obj,TransactionACK_Centerwise.this);
			printValues.add(head1, "true");

			if (head2.equals("0") || head2.equals("") || head2.equals("null") || head2.equals("NULL")
				|| head2.equals("Null")) {
			} else {
			    printValues.add(head2, "true");
			}
			if (head3.equals("0") || head3.equals("") || head3.equals("null") || head3.equals("NULL")
				|| head3.equals("Null")) {
			} else {
			    printValues.add(head3, "true");
			}
			printValues.add(" ", "false");
			printValues.add("Agent Copy", "true");
			printValues.add("Transaction Acknowledgement", "true");
			printValues.add("---------------------------", "true");
			printValues.add(" ", "false");
			printValues.add("R.No:" + recnum, "false");
			printValues.add("Date:" + obj.DemandDate, "false");
			printValues.add(" ", "false");
			String URL = SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
			if (URL.equals("Yes")) {
			    printValues.add("Center Name:" + obj.CName, "false");
			    printValues.add("SHG Name:" + obj.GroupName, "false");
			    printValues.add("Member Name:" + obj.MemberName + "(" + regularDemandsDO.MemberCode + ")",
				    "false");
			    printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			    if (intialParametrsDO.InstRequired.equalsIgnoreCase("1")) {
				printValues.add("Meeting No:" + regularDemandsDO.InstallNo, "false");
				// printValues.add("Next Meeting
				// Date:"+regularDemandsDO.NextRepayDate,"false");
			    }
			    float DmdTot = Float.valueOf(regularDemandsDO.DemandTotal)
				    + Float.valueOf(regularDemandsDO.ODAmount);
			    printValues.add("Planned Savings:" + DmdTot, "false");
			    printValues.add("Savings Collected:" + obj.collectedAmount, "false");

			} else {
			    printValues.add("Center:" + obj.CName, "false");
			    printValues.add("Group:" + obj.GroupName, "false");
			    printValues.add("Member:" + obj.MemberName + "(" + regularDemandsDO.MemberCode + ")",
				    "false");
			    printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			    if (intialParametrsDO.InstRequired.equalsIgnoreCase("1")) {
				printValues.add("Installment No:" + regularDemandsDO.InstallNo, "false");
				printValues.add("Next Installment Date:" + regularDemandsDO.NextRepayDate, "false");
			    }
			    float DmdTot = Float.valueOf(regularDemandsDO.DemandTotal)
				    + Float.valueOf(regularDemandsDO.ODAmount);
			    printValues.add("Demand:" + DmdTot, "false");
			    printValues.add("collection Amt:" + obj.collectedAmount, "false");
			    float OSAmt = Float.valueOf(obj.OSAmt) - Float.valueOf(CollAmt);
			    Log.d("mfimo", "act os amt:" + obj.OSAmt + " collection amt:" + CollAmt);
			    if (OSAmt < 0) {
				printValues.add("Curr OS(P+I):" + 0.0, "false");
			    } else {
				printValues.add("Curr OS(P+I):" + OSAmt, "false");
			    }
			}
			printValues.add(" ", "false");
			printValues.add(intialParametrsDO.ReceiptFooter, "true");
		    }
		}
	    }
	} else {
	    // print individual reciepts without header and footer
	    // chek for agent copy if it is 1 print one more copy for agent with
	    // 'Agent' as header

	    String URL = SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
	    String header = intialParametrsDO.ReceiptHeader;
	    String[] head = header.split("@");
	    String head1 = head[0];
	    String head2 = head[1];
	    String head3 = head[2];

	    printValues.add(head1, "true");

	    if (head2.equals("0") || head2.equals("") || head2.equals("null") || head2.equals("NULL")
		    || head2.equals("Null")) {
	    } else {
		printValues.add(head2, "true");
	    }
	    if (head3.equals("0") || head3.equals("") || head3.equals("null") || head3.equals("NULL")
		    || head3.equals("Null")) {
	    } else {
		printValues.add(head3, "true");
	    }
	    printValues.add(" ", "false");
	    if (intialParametrsDO.AgentCopy.equalsIgnoreCase("1")) {
		printValues.add("Customer Copy", "true");
	    }
	    printValues.add("Transaction Acknowledgement", "true");
	    printValues.add("---------------------------", "true");
	    printValues.add(" ", "false");

	    if (URL.equals("Yes")) {
		for (int i = 0; i < alDos.size(); i++) {
		    float GroupCollectedAmt = 0, GroupDemandAmt = 0;
		    TrnsactionsBL trnsactionsBL2 = new TrnsactionsBL();
		    ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL2.SelectAll(TranScode, alDos.get(i).GNo, "Center",TransactionACK_Centerwise.this);
		    String recnum = StringUtils.getRecieptNumber(alArrayList.get(0),TransactionACK_Centerwise.this);
		    if (i == 0) {
			printValues.add("Date : " + alArrayList.get(0).DemandDate, "false");
			printValues.add("Receipt Number:" + recnum, "false");
			printValues.add(" ", "false");
			printValues.add("Center Name:" + alArrayList.get(0).CName, "false");
		    }

		    printValues.add("SHG Name:" + alArrayList.get(0).GroupName, "false");

		    for (int k = 0; k < alArrayList.size(); k++) {
			RegularDemandsDO obj = alArrayList.get(k);
			RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
			RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",TransactionACK_Centerwise.this).get(0);

			printValues.add(" ", "false");
			printValues.add("Member Name:" + obj.MemberName + "(" + regularDemandsDO.MemberCode + ")",
				"false");
			printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			if (intialParametrsDO.InstRequired.equalsIgnoreCase("1")) {
			    printValues.add("Meeting No:" + regularDemandsDO.InstallNo, "false");
			}
			printValues.add("Planned Savings:" + regularDemandsDO.DemandTotal, "false");
			printValues.add("Savings Collected:" + obj.collectedAmount, "false");

			GroupCollectedAmt = GroupCollectedAmt + Float.parseFloat(obj.collectedAmount);
			GroupDemandAmt = GroupDemandAmt + (Float.parseFloat(regularDemandsDO.DemandTotal)
				+ Float.parseFloat(regularDemandsDO.ODAmount));
		    }
		    printValues.add(" ", "false");
		    printValues.add("Total SHG Planned Savings:" + GroupDemandAmt, "false");
		    printValues.add("SHG Collected Savings:" + GroupCollectedAmt, "false");
		    printValues.add(" ", "false");
		}

		printValues.add("Total Planned Savings:" + totDemand, "false");
		printValues.add("Collected Savings:" + collectedAmt, "false");
	    } else {
		for (int i = 0; i < alDos.size(); i++) {
		    float GroupCollectedAmt = 0, GroupDemandAmt = 0;
		    TrnsactionsBL trnsactionsBL2 = new TrnsactionsBL();
		    ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL2.SelectAll(TranScode, alDos.get(i).GNo, "Center",TransactionACK_Centerwise.this);
		    String recnum = StringUtils.getRecieptNumber(alArrayList.get(0),TransactionACK_Centerwise.this);
		    if (i == 0) {
			printValues.add("Date : " + alArrayList.get(0).DemandDate, "false");
			printValues.add("Center R.No:" + recnum, "false");
			printValues.add(" ", "false");
			printValues.add("Center:" + alArrayList.get(0).CName, "false");
		    }
		    printValues.add("Group:" + alArrayList.get(0).GroupName, "false");
		    for (int k = 0; k < alArrayList.size(); k++) {
			RegularDemandsDO obj = alArrayList.get(k);
			RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
			RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",TransactionACK_Centerwise.this).get(0);

			printValues.add(" ", "false");
			printValues.add("Name:" + obj.MemberName + "(" + regularDemandsDO.MemberCode + ")", "false");
			printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			if (intialParametrsDO.InstRequired.equalsIgnoreCase("1")) {
			    printValues.add("InstallMent No:" + regularDemandsDO.InstallNo, "false");
			    printValues.add("Next Installment Date:" + regularDemandsDO.NextRepayDate, "false");
			}
			String CollAmt = trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,TransactionACK_Centerwise.this);
			printValues.add("collection:" + obj.collectedAmount, "false");
			float OSAmt = Float.valueOf(obj.OSAmt) - Float.valueOf(CollAmt);
			Log.d("mfimo", "act os amt:" + obj.OSAmt + " collection amt:" + CollAmt);
			if (OSAmt < 0) {
			    printValues.add("Curr OS(P+I):" + 0.0, "false");
			} else {
			    printValues.add("Curr OS(P+I):" + OSAmt, "false");
			}

			GroupCollectedAmt = GroupCollectedAmt + Float.parseFloat(obj.collectedAmount);
			GroupDemandAmt = GroupDemandAmt + (Float.parseFloat(regularDemandsDO.DemandTotal)
				+ Float.parseFloat(regularDemandsDO.ODAmount));
		    }
		    printValues.add(" ", "false");
		    printValues.add("Group Demand Amt:" + GroupDemandAmt, "false");
		    printValues.add("Group collection Amt:" + GroupCollectedAmt, "false");
		    printValues.add(" ", "false");
		}
		printValues.add("Total Center Demand:" + totDemand, "false");
		printValues.add("Total Center Coll. Amt:" + collectedAmt, "false");
	    }

	    printValues.add(" ", "false");
	    printValues.add(intialParametrsDO.ReceiptFooter, "true");
	    printValues.add(" ", "false");
	    printValues.add(" ", "false");
	    printValues.add(" ", "false");
	    printValues.add(" ", "false");

	    if (intialParametrsDO.AgentCopy.equalsIgnoreCase("1")) {

		printValues.add(head1, "true");
		if (head2.equals("0") || head2.equals("") || head2.equals("null") || head2.equals("NULL")
			|| head2.equals("Null")) {
		} else {
		    printValues.add(head2, "true");
		}
		if (head3.equals("0") || head3.equals("") || head3.equals("null") || head3.equals("NULL")
			|| head3.equals("Null")) {
		} else {
		    printValues.add(head3, "true");
		}
		printValues.add(" ", "false");
		printValues.add("Agent Copy", "false");
		printValues.add("Transaction Acknowledgement", "false");
		printValues.add("---------------------------", "false");
		printValues.add(" ", "false");
		// agent copy
		if (URL.equals("Yes")) {
		    for (int i = 0; i < alDos.size(); i++) {
			float GroupCollectedAmt = 0, GroupDemandAmt = 0;
			TrnsactionsBL trnsactionsBL2 = new TrnsactionsBL();
			ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL2.SelectAll(TranScode, alDos.get(i).GNo, "Center",TransactionACK_Centerwise.this);
			if (i == 0) {
			    printValues.add("Date : " + alArrayList.get(0).DemandDate, "false");
			    printValues.add("Receipt Number:" + TranScode, "false");
			    printValues.add(" ", "false");
			    printValues.add("Center Name:" + alArrayList.get(0).CName, "false");
			}

			printValues.add("SHG Name:" + alArrayList.get(0).GroupName, "false");

			for (int k = 0; k < alArrayList.size(); k++) {
			    RegularDemandsDO obj = alArrayList.get(k);
			    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
			    RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",TransactionACK_Centerwise.this)
				    .get(0);

			    printValues.add(" ", "false");
			    printValues.add("Member Name:" + obj.MemberName + "(" + regularDemandsDO.MemberCode + ")",
				    "false");
			    printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			    if (intialParametrsDO.InstRequired.equalsIgnoreCase("1")) {
				printValues.add("Meeting No:" + regularDemandsDO.InstallNo, "false");
			    }
			    printValues.add("Planned Savings:" + regularDemandsDO.DemandTotal, "false");
			    printValues.add("Savings Collected:" + obj.collectedAmount, "false");

			    GroupCollectedAmt = GroupCollectedAmt + Float.parseFloat(obj.collectedAmount);
			    GroupDemandAmt = GroupDemandAmt + (Float.parseFloat(regularDemandsDO.DemandTotal)
				    + Float.parseFloat(regularDemandsDO.ODAmount));
			}
			printValues.add(" ", "false");
			printValues.add("Total SHG Planned Savings:" + GroupDemandAmt, "false");
			printValues.add("SHG Collected Savings:" + GroupCollectedAmt, "false");
			printValues.add(" ", "false");
		    }

		    printValues.add("Total Planned Savings:" + totDemand, "false");
		    printValues.add("Collected Savings:" + collectedAmt, "false");
		} else {
		    for (int i = 0; i < alDos.size(); i++) {
			TrnsactionsBL trnsactionsBL2 = new TrnsactionsBL();
			ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL2.SelectAll(TranScode, alDos.get(i).GNo, "Center",TransactionACK_Centerwise.this);
			String recnum = StringUtils.getRecieptNumber(alArrayList.get(0),TransactionACK_Centerwise.this);
			float GroupCollectedAmt = 0, GroupDemandAmt = 0;
			if (i == 0) {
			    printValues.add("Date : " + alArrayList.get(0).DemandDate, "false");
			    printValues.add("Center R.No:" + recnum, "false");
			    printValues.add(" ", "false");
			    printValues.add("Center :" + alArrayList.get(0).CName, "false");
			}
			printValues.add("Group :" + alArrayList.get(0).GroupName, "false");

			for (int k = 0; k < alArrayList.size(); k++) {
			    RegularDemandsDO obj = alArrayList.get(k);
			    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
			    RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",TransactionACK_Centerwise.this)
				    .get(0);

			    printValues.add(" ", "false");
			    printValues.add("Name:" + obj.MemberName + "(" + regularDemandsDO.MemberCode + ")",
				    "false");
			    printValues.add("Loan A/C No :" + obj.MLAI_ID, "false");
			    if (intialParametrsDO.InstRequired.equalsIgnoreCase("1")) {
				printValues.add("InstallMent No :" + regularDemandsDO.InstallNo, "false");
				printValues.add("Next Installment Date:" + regularDemandsDO.NextRepayDate, "false");
			    }
			    String CollAmt = trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,TransactionACK_Centerwise.this);
			    printValues.add("Demand :" + regularDemandsDO.DemandTotal, "false");
			    printValues.add("collection :" + obj.collectedAmount, "false");
			    float OSAmt = Float.valueOf(obj.OSAmt) - Float.valueOf(CollAmt);
			    Log.d("mfimo", "act os amt:" + obj.OSAmt + " collection amt:" + CollAmt);
			    if (OSAmt < 0) {
				printValues.add("Curr OS(P+I):" + 0.0, "false");
			    } else {
				printValues.add("Curr OS(P+I):" + OSAmt, "false");
			    }

			    GroupCollectedAmt = GroupCollectedAmt + Float.parseFloat(obj.collectedAmount);
			    GroupDemandAmt = GroupDemandAmt + (Float.parseFloat(regularDemandsDO.DemandTotal)
				    + Float.parseFloat(regularDemandsDO.ODAmount));
			}
			printValues.add(" ", "false");
			printValues.add("Group Demand Amt :" + GroupDemandAmt, "false");
			printValues.add("Group collection Amt :" + GroupCollectedAmt, "false");
			printValues.add("", "false");
		    }

		    printValues.add("Total Center Demand:" + totDemand, "false");
		    printValues.add("Total Center Coll. Amt:" + collectedAmt, "false");
		}
		printValues.add(" ", "false");
		printValues.add(intialParametrsDO.ReceiptFooter, "true");
		printValues.add("", "false");
		printValues.add("", "false");
		printValues.add("", "false");
		printValues.add("", "false");
	    }
	}

	isPrintTaken = true;
	return printValues.getDetailObj();
    }
}
