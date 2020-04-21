package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.FtodreasonsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FtodGroupWiseConfirmation extends Base {
    private LinearLayout llFTODCONFIRMATION;
    private EditText etremarksonOD, etdemisedate;
    private TextView tvdate, tvremarks;
    private Button btnftodconfirm;
    String ID, GroupCode;
    String Remarks = "0";
    String paymnet, MemberName, MemberCode, DemandDate;
    private ArrayList<RegularDemandsDO> alregularDemandsBL;
    private RegularDemandsBL regularDemandsBL;

    @Override
    public void initialize() {

	ID = getIntent().getExtras().getString("ReasonID");
	GroupCode = getIntent().getExtras().getString("Code");
	Log.e("GroupCode", GroupCode);

	intializeControlles();
	etdemisedate.setVisibility(View.GONE);
	tvdate.setVisibility(View.GONE);

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
		Intent i = new Intent(FtodGroupWiseConfirmation.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	btnftodconfirm.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		showAlertDailog("OD Reason captured. Do you want to continue?", "Yes", "No", new CustomDailoglistner() {

		    @Override
		    public void onPossitiveButtonClick(DialogInterface dialog) {
			dialog.dismiss();
			Remarks = etremarksonOD.getText().toString();
			if (Remarks.equals("")) {
			    Remarks = "0";
			}

			showAlertDailog("Are you sure that you want to mark these accounts as OD Accounts?", "OK",
				"Cancel", new CustomDailoglistner() {

			    @Override
			    public void onPossitiveButtonClick(DialogInterface dialog) {
				dialog.dismiss();
				// ask for second confirmation

				showAlertDailog("Are you sure to mark OD?", "OK", "cancel", new CustomDailoglistner() {
				    @Override
				    public void onPossitiveButtonClick(DialogInterface dialog) {
					dialog.dismiss();

					ShowLoader();

					FtodreasonsBL FTODBL = new FtodreasonsBL();
					FTODBL.updateFTODReasons(GroupCode, ID, Remarks, "null", "Group",FtodGroupWiseConfirmation.this);
					regularDemandsBL = new RegularDemandsBL();
					alregularDemandsBL = regularDemandsBL.SelectAll(GroupCode, "Groups",FtodGroupWiseConfirmation.this);
					NoPayment_center noPayment = new NoPayment_center(alregularDemandsBL,
						new Nopaymnetlistner_center() {
					    @Override
					    public void nopaymnetCompleted() {
						HideLoader();
						showAlertDailog("Group OD Marked Successfully.", "OK",
							new CustomDailoglistner() {
						    @Override
						    public void onPossitiveButtonClick(DialogInterface dialog) {
							dialog.dismiss();
							setResult(AppConstants.RESULTCODE_HOME);
							finish();
						    }

						    @Override
						    public void onNegativeButtonClick(DialogInterface dialog) {

						    }
						});
					    }
					}, FtodGroupWiseConfirmation.this, GroupCode);

					noPayment.start();
				    }

				    @Override
				    public void onNegativeButtonClick(DialogInterface dialog) {
					dialog.dismiss();
				    }
				});
			    }

			    @Override
			    public void onNegativeButtonClick(DialogInterface dialog) {
				dialog.dismiss();
			    }
			});

		    }

		    @Override
		    public void onNegativeButtonClick(DialogInterface dialog) {
			dialog.dismiss();
		    }
		});
	    }
	});

    }

    public void intializeControlles() {
	llFTODCONFIRMATION = (LinearLayout) inflater.inflate(R.layout.ftodconfirmation, null);
	etremarksonOD = (EditText) llFTODCONFIRMATION.findViewById(R.id.etremarksonOD);
	etdemisedate = (EditText) llFTODCONFIRMATION.findViewById(R.id.etdemisedate);
	tvdate = (TextView) llFTODCONFIRMATION.findViewById(R.id.tvdate);
	tvremarks = (TextView) llFTODCONFIRMATION.findViewById(R.id.tvremarks);
	btnftodconfirm = (Button) llFTODCONFIRMATION.findViewById(R.id.btnftodconfirm);

	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llFTODCONFIRMATION, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
	tvHeader.setText("Reasons");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode == AppConstants.RESULTCODE_GROPMEMBERS) {
	    setResult(AppConstants.RESULTCODE_GROPMEMBERS);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_GROPDETAILS) {
	    setResult(AppConstants.RESULTCODE_GROPDETAILS);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_LOGOUT) {
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_HOME) {
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_CENTERDETAILS) {
	    setResult(resultCode);
	    finish();
	}

    }

    class NoPayment_center extends Thread {
	private ArrayList<RegularDemandsDO> alArrayList;
	private Nopaymnetlistner_center nopaymnetlistner;
	private Context context;
	private TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
	private String groupNumber;

	public NoPayment_center(ArrayList<RegularDemandsDO> alArrayList, Nopaymnetlistner_center nopaymnetlistner,
		Context context, String groupNumber) {
	    this.alArrayList = alArrayList;
	    this.nopaymnetlistner = nopaymnetlistner;
	    this.context = context;
	    this.groupNumber = groupNumber;
	}

	@Override
	public void run() {
	    super.run();

	    trnsactionsBL.InsertNoPaymnet(alArrayList,this.context);
	    RegularDemandsBL demandsBL = new RegularDemandsBL();
	    demandsBL.updateNoPaymnetStatus(groupNumber,this.context);
	    String CNO = demandsBL.SelectCenterNumber(groupNumber,this.context).get(0).CNo;
	    Log.d("mfimo", trnsactionsBL.getTransactionCount(CNO,this.context));
	    if (Integer.parseInt(trnsactionsBL.getTransactionCount(CNO,this.context)) > 0) {
		trnsactionsBL.updatePIC(CNO, alArrayList.get(0).probInCenter,this.context);

		((Activity) context).runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			nopaymnetlistner.nopaymnetCompleted();
		    }
		});
	    }
	}
    }
}
