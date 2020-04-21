package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class CenterDetails extends Base {
    private LinearLayout llCenterDetails;
    private TextView tvCollectionDate,tvProductname, tvCenterCode, tvCenterName, tvNumberOfAccounts, tvRegularDemandsAmount,
	    tvODDemandAmount, tvAmountTobeCollected;
    private String centercode, centename;
    private Button etEdit, btnNext;
    private ArrayList<IntialParametrsDO> altialParametrsDO;
    private ArrayList<RegularDemandsDO> alregularDemandsBL;
    private IntialParametrsBL intialParametrsBL;
    private RegularDemandsBL regularDemandsBL;
    private Button btnNoPayment;
    private float AmountSaved = 0;

    @Override
    public void initialize() {
	centercode = getIntent().getExtras().getString("centercode");
	intialParametrsBL = new IntialParametrsBL();
	altialParametrsDO = intialParametrsBL.SelectAll(CenterDetails.this);

	regularDemandsBL = new RegularDemandsBL();
	alregularDemandsBL = regularDemandsBL.SelectAll(centercode, "CNo",CenterDetails.this);

	intializeControlles();
	tvCollectionDate.setText("" + alregularDemandsBL.get(0).DemandDate);
		tvProductname.setText("" + alregularDemandsBL.get(0).ProductName);

	tvCenterCode.setText("" + alregularDemandsBL.get(0).CNo);

	tvCenterName.setText("" + alregularDemandsBL.get(0).CName);
	centename = tvCenterName.getText().toString();
	tvNumberOfAccounts.setText("" + alregularDemandsBL.size());

	Log.e("centercode", "" + centercode);
	Log.e("alregularDemandsBL", "" + alregularDemandsBL.size());
	float regularDemandAmount = 0;
	float odDemandAmount = 0;
	float totalamout = 0;
	for (int i = 0; i < alregularDemandsBL.size(); i++) {
	    RegularDemandsDO obj = alregularDemandsBL.get(i);
	    regularDemandAmount = regularDemandAmount + Float.valueOf(obj.DemandTotal.trim()).floatValue();
	    odDemandAmount = odDemandAmount + Float.valueOf(obj.ODAmount.trim()).floatValue();
	}

	tvRegularDemandsAmount.setText("" + regularDemandAmount);
	tvODDemandAmount.setText("" + odDemandAmount);
	// tvAmountTobeCollected.setText(""+(regularDemandAmount+odDemandAmount));
	tvAmountTobeCollected.setText("" + totalamout);

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
		Intent i = new Intent(CenterDetails.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	etEdit.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(CenterDetails.this, Groups_Centerwise.class);
		intent.putExtra("centernuber", centercode);
		startActivityForResult(intent, 1234);
	    }
	});

	btnNext.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
		String MCount = trnsactionsBL.getGroupCount(centercode, "Center",CenterDetails.this);
		Log.e("MCOUNT:-", MCount);
		int MC = Integer.parseInt(MCount);
		if (MC >= 2) {
		    showAlertDailog("Two Transactions have completed for this Center");
		    return;
		}

		if (!hasDone(alregularDemandsBL) || AmountSaved > 0) {
		    Intent intent = new Intent(CenterDetails.this, Groups_For_attendense.class);
		    overridePendingTransition(R.anim.push_up_out, R.anim.push_up_in);
		    intent.putExtra("centernuber", centercode);
		    intent.putExtra("tvCenterName", centename);
		    intent.putExtra("tvAmountTobeCollected", tvAmountTobeCollected.getText().toString());// tvAmountTobeCollected
		    startActivityForResult(intent, 1234);
		} else {
		    showAlertDailog("You have already Submitted this Center Details");
		}
	    }
	});

	TrnsactionsBL bl = new TrnsactionsBL();
	String count = bl.checkCenterAvailbleIntransactiontable(centercode,CenterDetails.this);

	if (Integer.parseInt(count) == 0) {
	    btnNoPayment.setVisibility(View.VISIBLE);
	} else {
	    btnNoPayment.setVisibility(View.GONE);
	}

	btnNoPayment.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		String ODMembers = regularDemandsBL.getODMembers(centercode, "Center",CenterDetails.this);
		if (!ODMembers.equals("1")) {
		    showAlertDailog("Are you sure that you want to mark these accounts as OD Accounts?", "OK", "Cancel",
			    new CustomDailoglistner() {

			@Override
			public void onPossitiveButtonClick(DialogInterface dialog) {
			    dialog.dismiss();
			    // ask for second confirmation
			    showAlertDailog("Are you sure to mark OD?", "OK", "cancel", new CustomDailoglistner() {
				@Override
				public void onPossitiveButtonClick(DialogInterface dialog) {
				    dialog.dismiss();

				    ShowLoader();

				    NoPayment_center noPayment = new NoPayment_center(alregularDemandsBL,
					    new Nopaymnetlistner_center() {
					@Override
					public void nopaymnetCompleted() {
					    HideLoader();
					    showAlertDailog("Center OD marking successful.", "OK",
						    new CustomDailoglistner() {
						@Override
						public void onPossitiveButtonClick(DialogInterface dialog) {
						    dialog.dismiss();
						    // setResult(AppConstants.RESULTCODE_HOME);
						    // finish();
						    Intent i = new Intent(CenterDetails.this, ProbInCenterCW.class);
						    i.putExtra("centernumber", centercode);
						    i.putExtra("from", "ftod");
						    startActivityForResult(i, 12345);
						}

						@Override
						public void onNegativeButtonClick(DialogInterface dialog) {

						}
					    });
					}
				    }, CenterDetails.this, centercode);

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

		} else {
		    IntialParametrsBL inpbl = new IntialParametrsBL();
		    ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(CenterDetails.this);
		    if (alin.get(0).rmelUser.equalsIgnoreCase("N")) {
			Intent i = new Intent(CenterDetails.this, ProbInCenterCW.class);
			i.putExtra("centernumber", centercode);
			i.putExtra("from", "ftodforward");
			startActivityForResult(i, 111);
		    } else {
			Intent i = new Intent(CenterDetails.this, Ftodreasonscenterwise.class);
			i.putExtra("CCode", centercode);
			i.putExtra("Type", "Center");
			startActivityForResult(i, 111);
		    }
		}

	    }
	});
    }

    public void intializeControlles() {
	llCenterDetails = (LinearLayout) inflater.inflate(R.layout.centerdetails, null);
	tvCollectionDate = (TextView) llCenterDetails.findViewById(R.id.tvCollectionDate);
		tvProductname = (TextView) llCenterDetails.findViewById(R.id.tvproduct);
	tvCenterCode = (TextView) llCenterDetails.findViewById(R.id.tvCenterCode);
	tvCenterName = (TextView) llCenterDetails.findViewById(R.id.tvCenterName);
	tvNumberOfAccounts = (TextView) llCenterDetails.findViewById(R.id.tvNumberOfAccounts);
	tvRegularDemandsAmount = (TextView) llCenterDetails.findViewById(R.id.tvRegularDemandsAmount);
	tvODDemandAmount = (TextView) llCenterDetails.findViewById(R.id.tvODDemandAmount);
	tvAmountTobeCollected = (TextView) llCenterDetails.findViewById(R.id.tvAmountTobeCollected);
	btnNoPayment = (Button) llCenterDetails.findViewById(R.id.btnNoPayment);
	etEdit = (Button) llCenterDetails.findViewById(R.id.etEdit);
	btnNext = (Button) llCenterDetails.findViewById(R.id.btnNext);

	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llCenterDetails, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	tvHeader.setText("Center Detail");

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
    protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	float totalamout = 0;
	alregularDemandsBL = regularDemandsBL.SelectAll(centercode, "CNo",CenterDetails.this);
	for (int i = 0; i < alregularDemandsBL.size(); i++) {
	    try {
		RegularDemandsDO obj = alregularDemandsBL.get(i);
		if (obj.collectedAmount != null) {
		    totalamout = totalamout + Float.valueOf(obj.DemandTotal.trim()).floatValue()
			    + Float.valueOf(obj.ODAmount.trim()).floatValue()
			    - Float.valueOf(obj.collectedAmount.trim()).floatValue();
		} else {
		    totalamout = totalamout + Float.valueOf(obj.DemandTotal.trim()).floatValue()
			    + Float.valueOf(obj.ODAmount.trim()).floatValue();
		}

	    } catch (Exception e) {
		Log.e("Exception", "" + e.toString());
	    }
	    tvAmountTobeCollected.setText("" + (totalamout));
	}
    }

    public boolean hasDone(ArrayList<RegularDemandsDO> alRegularDemandsDOs) {
	boolean hasDone = true;

	for (RegularDemandsDO regularDemandsDO : alRegularDemandsDOs) {
	    float totalAmt, collectedAmt;

	    totalAmt = Float.valueOf(regularDemandsDO.DemandTotal) + Float.valueOf(regularDemandsDO.ODAmount);
	    AmountSaved = AmountSaved + Float.valueOf(regularDemandsDO.savedAmt);
	    if (regularDemandsDO.collectedAmount != null) {
		collectedAmt = Float.valueOf(regularDemandsDO.collectedAmount);
	    } else {
		collectedAmt = 0;
	    }

	    if (totalAmt - collectedAmt == 0) {

	    } else {
		hasDone = false;
		break;
	    }
	}

	return hasDone;
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
	demandsBL.updateNoPaymnetStatus_Center(groupNumber,this.context);

	((Activity) context).runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
		nopaymnetlistner.nopaymnetCompleted();
	    }
	});
    }
}

interface Nopaymnetlistner_center {
    public abstract void nopaymnetCompleted();
}
