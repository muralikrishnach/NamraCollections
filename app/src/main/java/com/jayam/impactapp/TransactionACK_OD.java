package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.LastRecieptBL;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.utils.PrintUtils;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.StringUtils;


import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TransactionACK_OD extends Base implements PrintListner {
    private LinearLayout llAck;
    private TextView tvrecptNumber, tvrecptNumberTo, tvDate, tvCenterName, tvgroupname, tvmembername, tvTotalDemand,
	    tvCollection, tvAttendense, tvMeetingStart, tvMeetingEndTime;
    private Button btnMenu, btnPrint;
    private String amount,BANKRRN,Response,PaymentMode,TXNID,MobileNo;
    String Print;
    ODDemandsDO obj;
    ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    private String RecptNumber;
    private boolean isPrintTaken = false;
    LastRecieptBL recptBL;
    Transaction_OD_BL transaction_OD_BL;

    String BankName="";
	String TransactionTime="";
	String datetime="";



    @Override
    public void initialize() {
	recptBL = new LastRecieptBL();

	obj = (ODDemandsDO) getIntent().getExtras().get("obj");
	amount = getIntent().getExtras().getString("amount");

		BANKRRN = getIntent().getExtras().getString("BANKRRN");
		Response = getIntent().getExtras().getString("Response");
		PaymentMode = getIntent().getExtras().getString("PaymentMode");
		TXNID = getIntent().getExtras().getString("TXNID");
		MobileNo = getIntent().getExtras().getString("MobileNo");

		BankName = getIntent().getExtras().getString("BankName");
		TransactionTime = getIntent().getExtras().getString("TransactionTime");
		datetime = getIntent().getExtras().getString("datetime");





	intializeControlles();

	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	alIntialParametrsDOs = intialParametrsBL.SelectAll(TransactionACK_OD.this);
	String[] id = StringUtils.getRecieptNumberForOD(obj,TransactionACK_OD.this).split("-");
	intialParametrsBL.updateReceiptNumber(String.valueOf(Integer.parseInt(id[id.length - 1]) + 1).toString(),TransactionACK_OD.this);
	tvrecptNumber.setText("" + StringUtils.getRecieptNumberForOD(obj,TransactionACK_OD.this));
	tvDate.setText("" + obj.DemandDate);
	tvCenterName.setText("" + obj.MCI_Name);
	tvgroupname.setText("" + obj.MGI_Name);
	tvTotalDemand.setText("" + obj.ODAmt);
	tvCollection.setText("" + amount);
	tvmembername.setText("" + obj.MMI_Name);

	transaction_OD_BL = new Transaction_OD_BL();
	RecptNumber = StringUtils.getRecieptNumberForOD(obj,TransactionACK_OD.this);
	transaction_OD_BL.Insert(RecptNumber, amount, obj,BANKRRN,Response,PaymentMode,TXNID,MobileNo,BankName,TransactionTime,datetime,TransactionACK_OD.this);
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
				recptBL.insert(RecptNumber, "N", "O",TransactionACK_OD.this);
				transaction_OD_BL.updatePrintFlag(RecptNumber, "Y",TransactionACK_OD.this);
				runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
					HideLoader();
					KeyValue keyValue_transtype = new KeyValue(AppConstants.TransactioType, "od");
					SharedPrefUtils.setValue(TransactionACK_OD.this, AppConstants.pref_name,
						keyValue_transtype);
					KeyValue keyValue_transcode = new KeyValue(AppConstants.TransactionCode,
						RecptNumber);
					SharedPrefUtils.setValue(TransactionACK_OD.this, AppConstants.pref_name,
						keyValue_transcode);
					PrintUtils printUtils = new PrintUtils(TransactionACK_OD.this,
						TransactionACK_OD.this);
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
			    recptBL.updateLastReceiptPrintFlag(RecptNumber, "Y",TransactionACK_OD.this);
			    transaction_OD_BL.updatePrintFlag(RecptNumber, "Y",TransactionACK_OD.this);
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
				    recptBL.updateLastReceiptPrintFlag(RecptNumber, "N",TransactionACK_OD.this);
				    transaction_OD_BL.updatePrintFlag(RecptNumber, "N",TransactionACK_OD.this);
				    dialog.dismiss();
				    setResult(AppConstants.RESULTCODE_HOME);
				    finish();
				}

				@Override
				public void onNegativeButtonClick(DialogInterface dialog) {
				    recptBL.updateLastReceiptPrintFlag(RecptNumber, "Y",TransactionACK_OD.this);
				    transaction_OD_BL.updatePrintFlag(RecptNumber, "N",TransactionACK_OD.this);
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

	btnMenu.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// if(Print.equals("1"))
		// {
		if (!isPrintTaken) {
		    showAlertDailog("Receipt is not printed. Are you sure want to exit the Menu ?", "OK", "Cancel",
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
				    recptBL.insert(RecptNumber, "N", "O",TransactionACK_OD.this);
				    transaction_OD_BL.updatePrintFlag(RecptNumber, "N",TransactionACK_OD.this);
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
			    recptBL.updateLastReceiptPrintFlag(RecptNumber, "Y",TransactionACK_OD.this);
			    transaction_OD_BL.updatePrintFlag(RecptNumber, "Y",TransactionACK_OD.this);
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
				    recptBL.updateLastReceiptPrintFlag(RecptNumber, "N",TransactionACK_OD.this);
				    transaction_OD_BL.updatePrintFlag(RecptNumber, "N",TransactionACK_OD.this);
				    dialog.dismiss();
				    setResult(AppConstants.RESULTCODE_HOME);
				    finish();
				}

				@Override
				public void onNegativeButtonClick(DialogInterface dialog) {
				    recptBL.updateLastReceiptPrintFlag(RecptNumber, "Y",TransactionACK_OD.this);
				    transaction_OD_BL.updatePrintFlag(RecptNumber, "N",TransactionACK_OD.this);
				    dialog.dismiss();
				    setResult(AppConstants.RESULTCODE_HOME);
				    finish();
				}
			    });
			}
		    });
		}
	    }
	    /*
	     * else { setResult(AppConstants.RESULTCODE_HOME); finish(); }
	     */
	    // }
	});

//	ivHome.setOnClickListener(new OnClickListener() {
//	    @Override
//	    public void onClick(View v) {
//		setResult(AppConstants.RESULTCODE_HOME);
//		finish();
//	    }
//	});
//
//	ivLogout.setOnClickListener(new OnClickListener() {
//	    @Override
//	    public void onClick(View v) {
//		setResult(AppConstants.RESULTCODE_LOGOUT);
//		finish();
//	    }
//	});


    }

    @SuppressWarnings("deprecation")
    public void intializeControlles() {
	llAck = (LinearLayout) inflater.inflate(R.layout.transactionack_od, null);

	tvrecptNumber = (TextView) llAck.findViewById(R.id.tvrecptNumber);

	tvDate = (TextView) llAck.findViewById(R.id.tvDate);
	tvCenterName = (TextView) llAck.findViewById(R.id.tvCenterName);
	tvgroupname = (TextView) llAck.findViewById(R.id.tvgroupname);
	tvTotalDemand = (TextView) llAck.findViewById(R.id.tvTotalDemand);
	tvCollection = (TextView) llAck.findViewById(R.id.tvCollection);
	tvmembername = (TextView) llAck.findViewById(R.id.tvmembername);
	btnMenu = (Button) llAck.findViewById(R.id.btnMenu);
	btnPrint = (Button) llAck.findViewById(R.id.btnPrint);

	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	hidehomeIcons();
	tvHeader.setText("Transaction Acknowledgement");

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
	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
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
	printValues.add("OD Transaction Acknowledgement", "true");
	printValues.add("---------------------------", "true");
	if (intialParametrsDO.AgentCopy.equalsIgnoreCase("1")) {
	    printValues.add("Customer Copy", "true");
	}
	printValues.add(" ", "false");
	printValues.add("Date:" + obj.DemandDate, "false");
	printValues.add("Receipt No:" + RecptNumber, "false");
	printValues.add("Center Name:" + obj.MCI_Name, "false");
	printValues.add("Group Name:" + obj.MGI_Name, "false");
	printValues.add("Member Name:" + obj.MMI_Name + "(" + obj.MMI_Code + ")", "false");
	printValues.add("Loan A/C No:" + obj.MLAI_ID, "false");
	printValues.add("Total Demand:" + obj.ODAmt, "false");
	printValues.add("collection:" + amount, "false");
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
	    printValues.add("Transaction Acknowledgement", "true");
	    printValues.add("---------------------------", "true");
	    printValues.add("Agent Copy", "true");
	    printValues.add("Date:" + obj.DemandDate, "false");
	    printValues.add("R.No:" + RecptNumber, "false");
	    printValues.add("Center Name:" + obj.MCI_Name, "false");
	    printValues.add("Group Name:" + obj.MGI_Name, "false");
	    printValues.add("Member Name:" + obj.MMI_Name + "(" + obj.MMI_Code + ")", "false");
	    printValues.add("Total Demand:" + obj.ODAmt, "false");
	    printValues.add("collection:" + amount, "false");
	    printValues.add(" ", "false");
	    printValues.add(intialParametrsDO.ReceiptFooter, "true");
	    printValues.add(" ", "false");
	    printValues.add(" ", "false");

	}
	printValues.add(" ", "false");
	isPrintTaken = true;
	return printValues.getDetailObj();
	// return null;
    }
	@Override
	public void onBackPressed() {
		// Do Here what ever you want do on back press;
	}

}
