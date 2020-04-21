package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.LastRecieptBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.PrintUtils;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.StringUtils;


import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdvTxnack_CenterWise extends Base implements PrintListner {
    private LinearLayout llAck;
    private TextView tvAdvreceiptno, tvAdvTxnDate, tvAdvCenterName, tvAdvCollAmt;
    private ArrayList<AdvaceDemandDO> alArrayList;
    private String Groups;
    // private AdvanceDemandBL trnsactionsBL;
    private String LastTranScode;
    private Button btnadvMenu, btnPrint;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String TOAST = "toast";
    float totAmt = 0;
    float totAmt_Collected = 0;
    float totDemand = 0;
    float collectedAmt = 0;
    int attendense = 0;
    LastRecieptBL recptBL;
    private ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    AdvanceDemandBL advancedemandBL = new AdvanceDemandBL();
    private IntialParametrsBL intialParametrsBL;
    private boolean isPrintTaken = false;
    String Print;

    @Override
    public void initialize() {
	recptBL = new LastRecieptBL();
	Groups = getIntent().getExtras().getString("centernumber");
	LastTranScode = getIntent().getExtras().getString("LastTranScode");
	intializeControlles();
	advancedemandBL = new AdvanceDemandBL();
	alArrayList = advancedemandBL.SelectAll(Groups, "CenterCode",AdvTxnack_CenterWise.this);

	intialParametrsBL = new IntialParametrsBL();
	alIntialParametrsDOs = intialParametrsBL.SelectAll(AdvTxnack_CenterWise.this);
	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
	Print = intialParametrsDO.PrintValidate;
	if (Print.equals("0")) {
	    btnPrint.setVisibility(View.GONE);
	}

	tvAdvTxnDate.setText("" + alArrayList.get(0).DemandDate);
	tvAdvCenterName.setText("" + alArrayList.get(0).CenterName);
	// tvAdvGroupName.setText(""+alArrayList.get(0).MGI_Name);
	tvAdvreceiptno.setText("" + LastTranScode);

	for (int i = 0; i < alArrayList.size(); i++) {
	    try {

		collectedAmt = collectedAmt + Float.valueOf(alArrayList.get(i).CollectedAmt).floatValue();
		totDemand = totDemand + Float.valueOf(alArrayList.get(i).OSAmt).floatValue();

	    }

	    catch (Exception e) {
		Log.e("Exception", "" + e.toString());
	    }
	}
	tvAdvCollAmt.setText("" + collectedAmt);

	btnadvMenu.setOnClickListener(new OnClickListener() {
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
					// update print status Y/N
					recptBL.delete();
					recptBL.insert(LastTranScode, "N", "A",AdvTxnack_CenterWise.this);
					advancedemandBL.updatePrintFlag(LastTranScode, "N",AdvTxnack_CenterWise.this);
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
				recptBL.updateLastReceiptPrintFlag(LastTranScode, "Y",AdvTxnack_CenterWise.this);
				advancedemandBL.updatePrintFlag(LastTranScode, "Y",AdvTxnack_CenterWise.this);
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
					recptBL.updateLastReceiptPrintFlag(LastTranScode, "N",AdvTxnack_CenterWise.this);
					advancedemandBL.updatePrintFlag(LastTranScode, "N",AdvTxnack_CenterWise.this);
					dialog.dismiss();
					setResult(AppConstants.RESULTCODE_HOME);
					finish();
				    }

				    @Override
				    public void onNegativeButtonClick(DialogInterface dialog) {
					recptBL.updateLastReceiptPrintFlag(LastTranScode, "Y",AdvTxnack_CenterWise.this);
					advancedemandBL.updatePrintFlag(LastTranScode, "N",AdvTxnack_CenterWise.this);
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
				recptBL.insert(LastTranScode, "N", "A",AdvTxnack_CenterWise.this);
				advancedemandBL.updatePrintFlag(LastTranScode, "Y",AdvTxnack_CenterWise.this);
				runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
					HideLoader();
					KeyValue keyValue_transtype = new KeyValue(AppConstants.TransactioType,
						"advance");
					SharedPrefUtils.setValue(AdvTxnack_CenterWise.this, AppConstants.pref_name,
						keyValue_transtype);
					KeyValue keyValue_transcode = new KeyValue(AppConstants.TransactionCode,
						LastTranScode);
					SharedPrefUtils.setValue(AdvTxnack_CenterWise.this, AppConstants.pref_name,
						keyValue_transcode);
					PrintUtils printUtils = new PrintUtils(AdvTxnack_CenterWise.this,
						AdvTxnack_CenterWise.this);
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
			    recptBL.updateLastReceiptPrintFlag(LastTranScode, "Y",AdvTxnack_CenterWise.this);
			    advancedemandBL.updatePrintFlag(LastTranScode, "Y",AdvTxnack_CenterWise.this);
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
				    recptBL.updateLastReceiptPrintFlag(LastTranScode, "N",AdvTxnack_CenterWise.this);
				    advancedemandBL.updatePrintFlag(LastTranScode, "N",AdvTxnack_CenterWise.this);
				    dialog.dismiss();
				    setResult(AppConstants.RESULTCODE_HOME);
				    finish();
				}

				@Override
				public void onNegativeButtonClick(DialogInterface dialog) {
				    recptBL.updateLastReceiptPrintFlag(LastTranScode, "Y",AdvTxnack_CenterWise.this);
				    advancedemandBL.updatePrintFlag(LastTranScode, "N",AdvTxnack_CenterWise.this);
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
		Intent i = new Intent(AdvTxnack_CenterWise.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

    }

    @SuppressWarnings("deprecation")
    public void intializeControlles() {
	llAck = (LinearLayout) inflater.inflate(R.layout.advtxnack, null);

	tvAdvreceiptno = (TextView) llAck.findViewById(R.id.tvAdvreceiptno);
	tvAdvTxnDate = (TextView) llAck.findViewById(R.id.tvAdvTxnDate);
	tvAdvCenterName = (TextView) llAck.findViewById(R.id.tvAdvCenterName);
	LinearLayout llGroup = (LinearLayout) llAck.findViewById(R.id.llGroup);
	tvAdvCollAmt = (TextView) llAck.findViewById(R.id.tvAdvCollAmt);
	btnadvMenu = (Button) llAck.findViewById(R.id.btnadvMenu);
	btnPrint = (Button) llAck.findViewById(R.id.btnadvPrint);

	svBase.setVisibility(View.GONE);
	llGroup.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	hidehomeIcons();
	tvHeader.setText("Transaction Acknowledgement");
    }

    public String getFromRecieptNum(ArrayList<RegularDemandsDO> list) {
	String fromReceiptNumber = null;
	String from[] = new String[list.size()];
	for (int i = 0; i < list.size(); i++) {
	    from[i] = list.get(i).ReciptNumber.split("-")[4];
	}

	int smallest = Integer.parseInt(from[0]);

	for (int i = 1; i < from.length; i++) {
	    if (Integer.parseInt(from[i]) < smallest) {
		smallest = Integer.parseInt(from[i]);
		fromReceiptNumber = list.get(i).ReciptNumber;
	    }
	}

	for (int i = 0; i < list.size(); i++) {
	    String rNo = list.get(i).ReciptNumber.split("-")[4];
	    if (Integer.parseInt(rNo) == smallest) {
		fromReceiptNumber = list.get(i).ReciptNumber;
	    }
	}

	return fromReceiptNumber;
    }

    public String getToRecieptNum(ArrayList<RegularDemandsDO> list) {
	String toReceiptNumber = null;
	String from[] = new String[list.size()];
	for (int i = 0; i < list.size(); i++) {
	    from[i] = list.get(i).ReciptNumber.split("-")[4];
	}

	int smallest = Integer.parseInt(from[0]);
	int largetst = Integer.parseInt(from[0]);

	for (int i = 1; i < from.length; i++) {
	    if (Integer.parseInt(from[i]) > largetst) {
		largetst = Integer.parseInt(from[i]);
		toReceiptNumber = list.get(i).ReciptNumber;
	    } else if (Integer.parseInt(from[i]) < smallest) {
		smallest = Integer.parseInt(from[i]);
	    }

	}

	return toReceiptNumber;
    }

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
	PrintValues printValues = new PrintValues();
	AdvanceDemandBL trnsactionsBL1 = new AdvanceDemandBL();
	ArrayList<AdvaceDemandDO> alDos = trnsactionsBL1.SelectDistinctGroupsFromCenter(Groups,AdvTxnack_CenterWise.this);
	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
	if (intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1")) {
	    AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
	    ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllTransactions(LastTranScode,AdvTxnack_CenterWise.this);
	    for (int i = 0; i < alArrayList.size(); i++) {
		AdvaceDemandDO obj = alArrayList.get(i);
		AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",AdvTxnack_CenterWise.this).get(0);
		String header = intialParametrsDO.ReceiptHeader;
		String[] head = header.split("@");
		String head1 = head[0];
		String head2 = head[1];
		String head3 = head[2];
		String receiptNum = StringUtils.getRecieptNumberForAdvance(obj,AdvTxnack_CenterWise.this);
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
		printValues.add("Advance TXN Acknowledgment", "true");
		printValues.add("--------------------------", "true");
		printValues.add(" ", "false");
		printValues.add("R.No:" + receiptNum, "false");
		printValues.add("Date:" + obj.DemandDate, "false");
		printValues.add(" ", "false");
		String URL = SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
		if (URL.equals("Yes")) {
		    printValues.add("Center Name:" + advanceDemandsDO.CenterName, "false");
		    printValues.add("SHG Name:" + advanceDemandsDO.MGI_Name, "false");
		    printValues.add("Member Name:" + obj.MMI_Name + "(" + obj.MMI_Code + ")", "false");
		    printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
		    printValues.add("Savings Coll:" + obj.previousAmt, "false");
		} else {
		    printValues.add("Center:" + advanceDemandsDO.CenterName, "false");
		    printValues.add("Group:" + advanceDemandsDO.MGI_Name, "false");
		    printValues.add("Member:" + obj.MMI_Name + "(" + obj.MMI_Code + ")", "false");
		    printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
		    printValues.add("Os Amt:" + advanceDemandsDO.OSAmt, "false");
		    printValues.add("Advance collection:" + obj.previousAmt, "false");
		}

		printValues.add(" ", "false");
		printValues.add(intialParametrsDO.ReceiptFooter, "true");

	    }
	    if (intialParametrsDO.AgentCopy.equalsIgnoreCase("1")) {
		// print agent copy
		for (int i = 0; i < alArrayList.size(); i++) {
		    AdvaceDemandDO obj = alArrayList.get(i);

		    AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",AdvTxnack_CenterWise.this).get(0);
		    String header = intialParametrsDO.ReceiptHeader;
		    String[] head = header.split("@");
		    String head1 = head[0];
		    String head2 = head[1];
		    String head3 = head[2];
		    String receiptNum = StringUtils.getRecieptNumberForAdvance(obj,AdvTxnack_CenterWise.this);
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
		    printValues.add("Advance TXN Acknowledgment", "true");
		    printValues.add("--------------------------", "true");
		    printValues.add(" ", "false");
		    printValues.add("R.No:" + receiptNum, "false");
		    printValues.add("Date:" + obj.DemandDate, "false");
		    printValues.add(" ", "false");
		    String URL = SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
		    if (URL.equals("Yes")) {
			printValues.add("Center Name:" + advanceDemandsDO.CenterName, "false");
			printValues.add("SHG Name:" + advanceDemandsDO.MGI_Name, "false");
			printValues.add("Member Name:" + obj.MMI_Name + "(" + obj.MMI_Code + ")", "false");
			printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			printValues.add("Savings Coll:" + obj.previousAmt, "false");
		    } else {
			printValues.add("Center:" + advanceDemandsDO.CenterName, "false");
			printValues.add("Group:" + advanceDemandsDO.MGI_Name, "false");
			printValues.add("Member:" + obj.MMI_Name + (obj.MMI_Code), "false");
			printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			printValues.add("Os Amt:" + advanceDemandsDO.OSAmt, "false");
			printValues.add("Advance collection:" + obj.previousAmt, "false");
		    }
		    printValues.add(" ", "false");
		    printValues.add(intialParametrsDO.ReceiptFooter, "true");

		}
	    }
	} else {
	    // print individual reciepts without header and footer
	    // chek for agent copy if it is 1 print one more copy for agent with
	    // 'Agent' as header
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
	    printValues.add("Advance TXN Acknowledgment", "true");
	    printValues.add("--------------------------", "true");
	    printValues.add(" ", "false");
	    String URL = SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
	    if (URL.equals("Yes")) {
		for (int i = 0; i < alDos.size(); i++) {
		    AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
		    ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(LastTranScode,
			    alDos.get(i).MGI_Code,AdvTxnack_CenterWise.this);
		    if (alArrayList.size() > 0) {
			String receiptNum = StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),AdvTxnack_CenterWise.this);
			float GroupCollectedAmt = 0, GroupDemandAmt = 0;
			if (i == 0) {
			    printValues.add("Date:" + alArrayList.get(0).DemandDate, "false");
			    printValues.add("R.No:" + receiptNum, "false");
			}
			for (int k = 0; k < alArrayList.size(); k++) {
			    AdvaceDemandDO obj = alArrayList.get(k);
			    AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",AdvTxnack_CenterWise.this).get(0);
			    if (k == 0) {
				printValues.add(" ", "false");
				printValues.add("Center Name:" + advanceDemandsDO.CenterName, "false");
				printValues.add("SHG Name:" + advanceDemandsDO.MGI_Name, "false");
			    }
			    printValues.add(" ", "false");
			    printValues.add("Member Name:" + obj.MMI_Name + "(" + obj.MMI_Code + ")", "false");
			    printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			    printValues.add("collection Amt:" + obj.previousAmt, "false");
			    GroupDemandAmt = GroupDemandAmt + Float.valueOf(advanceDemandsDO.OSAmt);
			    GroupCollectedAmt = GroupCollectedAmt + Float.valueOf(obj.previousAmt);
			}
			printValues.add(" ", "false");
			printValues.add("SHG Demand Amt:" + GroupDemandAmt, "false");
			printValues.add("SHG collection Amt:" + GroupCollectedAmt, "false");
			printValues.add(" ", "false");
		    }
		}
	    } else {
		for (int i = 0; i < alDos.size(); i++) {
		    AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
		    ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(LastTranScode,
			    alDos.get(i).MGI_Code,AdvTxnack_CenterWise.this);
		    if (alArrayList.size() > 0) {
			String receiptNum = StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),AdvTxnack_CenterWise.this);
			float GroupCollectedAmt = 0, GroupDemandAmt = 0;
			if (i == 0) {
			    printValues.add("Date:" + alArrayList.get(0).DemandDate, "false");
			    printValues.add("Center R.No:" + receiptNum, "false");
			}
			for (int k = 0; k < alArrayList.size(); k++) {
			    AdvaceDemandDO obj = alArrayList.get(k);
			    AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",AdvTxnack_CenterWise.this).get(0);
			    if (k == 0) {
				printValues.add("Center:" + advanceDemandsDO.CenterName, "false");
				printValues.add("Group:" + advanceDemandsDO.MGI_Name, "false");
			    }
			    printValues.add(" ", "false");
			    printValues.add("Member Name:" + obj.MMI_Name + "(" + obj.MMI_Code + ")", "false");
			    printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
			    printValues.add("collection Amt:" + obj.previousAmt, "false");
			    GroupDemandAmt = GroupDemandAmt + Float.valueOf(advanceDemandsDO.OSAmt);
			    GroupCollectedAmt = GroupCollectedAmt + Float.valueOf(obj.previousAmt);
			}
			printValues.add(" ", "false");
			printValues.add("Group Demand Amt:" + GroupDemandAmt, "false");
			printValues.add("Group collection Amt:" + GroupCollectedAmt, "false");
			printValues.add(" ", "false");
		    }
		}
	    }

	    printValues.add("Total Center Demand:" + totDemand, "false");
	    printValues.add("Total Center Coll. Amt:" + collectedAmt, "false");
	    printValues.add(" ", "false");
	    printValues.add(intialParametrsDO.ReceiptFooter, "true");
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

		printValues.add("Agent Copy", "true");
		printValues.add("Advance TXN Acknowledgment", "true");
		printValues.add("--------------------------", "true");
		printValues.add(" ", "false");
		if (URL.equals("Yes")) {
		    for (int i = 0; i < alDos.size(); i++) {
			AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
			ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(LastTranScode,
				alDos.get(i).MGI_Code,AdvTxnack_CenterWise.this);
			if (alArrayList.size() > 0) {
			    String receiptNum = StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),AdvTxnack_CenterWise.this);
			    float GroupCollectedAmt = 0, GroupDemandAmt = 0;
			    if (i == 0) {
				printValues.add("Date:" + alArrayList.get(0).DemandDate, "false");
				printValues.add("R.No:" + receiptNum, "false");
			    }
			    for (int k = 0; k < alArrayList.size(); k++) {
				AdvaceDemandDO obj = alArrayList.get(k);
				AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",AdvTxnack_CenterWise.this)
					.get(0);
				if (k == 0) {
				    printValues.add("Center Name:" + advanceDemandsDO.CenterName, "false");
				    printValues.add("SHG Name:" + advanceDemandsDO.MGI_Name, "false");
				}
				printValues.add(" ", "false");
				printValues.add("Member Name:" + obj.MMI_Name + "(" + obj.MMI_Code + ")", "false");
				printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
				printValues.add("collection Amt:" + obj.previousAmt, "false");
				GroupDemandAmt = GroupDemandAmt + Float.valueOf(advanceDemandsDO.OSAmt);
				GroupCollectedAmt = GroupCollectedAmt + Float.valueOf(obj.previousAmt);
			    }
			    printValues.add(" ", "false");
			    printValues.add("SHG Demand Amt:" + GroupDemandAmt, "false");
			    printValues.add("SHG collection Amt:" + GroupCollectedAmt, "false");
			    printValues.add(" ", "false");
			}
		    }
		} else {
		    for (int i = 0; i < alDos.size(); i++) {
			AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
			ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(LastTranScode,
				alDos.get(i).MGI_Code,AdvTxnack_CenterWise.this);
			if (alArrayList.size() > 0) {
			    String receiptNum = StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),AdvTxnack_CenterWise.this);
			    float GroupCollectedAmt = 0, GroupDemandAmt = 0;
			    if (i == 0) {
				printValues.add("Date:" + alArrayList.get(0).DemandDate, "false");
				printValues.add("Center R.No:" + receiptNum, "false");
			    }
			    for (int k = 0; k < alArrayList.size(); k++) {
				AdvaceDemandDO obj = alArrayList.get(k);
				AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",AdvTxnack_CenterWise.this)
					.get(0);
				if (k == 0) {
				    printValues.add("Center:" + advanceDemandsDO.CenterName, "false");
				    printValues.add("Group:" + advanceDemandsDO.MGI_Name, "false");
				}
				printValues.add(" ", "false");
				printValues.add("Member Name:" + obj.MMI_Name + "(" + obj.MMI_Code + ")", "false");
				printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
				printValues.add("collection Amt:" + obj.previousAmt, "false");
				GroupDemandAmt = GroupDemandAmt + Float.valueOf(advanceDemandsDO.OSAmt);
				GroupCollectedAmt = GroupCollectedAmt + Float.valueOf(obj.previousAmt);
			    }
			    printValues.add(" ", "false");
			    printValues.add("Group Demand Amt:" + GroupDemandAmt, "false");
			    printValues.add("Group collection Amt:" + GroupCollectedAmt, "false");
			    printValues.add(" ", "false");
			}
		    }
		}

		printValues.add(" ", "false");
		printValues.add("Total Center Demand:" + totDemand, "false");
		printValues.add("Total Center Coll. Amt:" + collectedAmt, "false");
		printValues.add(" ", "false");
		printValues.add(intialParametrsDO.ReceiptFooter, "true");
		printValues.add(" ", "false");
		printValues.add(" ", "false");
	    }
	}

	isPrintTaken = true;
	return printValues.getDetailObj();
    }
}
