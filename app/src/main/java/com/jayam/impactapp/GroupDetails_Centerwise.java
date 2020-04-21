package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.SharedPrefUtils;


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

public class GroupDetails_Centerwise extends Base {
    private LinearLayout llCenterDetails;
    private TextView tvCollectionDate, tvCenterCode, tvCenterName, tvNumberOfAccounts, tvRegularDemandsAmount,
	    tvODDemandAmount, tvAmountTobeCollected;
    private TextView tvCenterCode_label, tvCenterName_label;
    private String groupnumber;
    private Button etEdit, btnNext;
    private IntialParametrsBL intialParametrsBL;
    private ArrayList<IntialParametrsDO> altialParametrsDO;
    private ArrayList<RegularDemandsDO> alregularDemandsBL;
    private RegularDemandsBL regularDemandsBL;
    private Button btnNoPayment;

    @Override
    public void initialize() {
	groupnumber = getIntent().getExtras().getString("groupnumber");

	intializeControlles();

	intialParametrsBL = new IntialParametrsBL();
	altialParametrsDO = intialParametrsBL.SelectAll(GroupDetails_Centerwise.this);

	regularDemandsBL = new RegularDemandsBL();
	alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",GroupDetails_Centerwise.this);

	tvCollectionDate.setText("" + alregularDemandsBL.get(0).DemandDate);

	tvCenterCode.setText("" + alregularDemandsBL.get(0).GNo);

	tvCenterName.setText("" + alregularDemandsBL.get(0).GroupName);

	tvNumberOfAccounts.setText("" + alregularDemandsBL.size());

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
	tvAmountTobeCollected.setText("" + (totalamout));

	KeyValue keyValue_amountToBeCollected = new KeyValue(AppConstants.amountTobeCollected,
		(regularDemandAmount + odDemandAmount) + "");

	SharedPrefUtils.setValue(GroupDetails_Centerwise.this, AppConstants.memberDetails_pref,
		keyValue_amountToBeCollected);

	etEdit.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(GroupDetails_Centerwise.this, GroupMembers.class);
		intent.putExtra("groupnumber", groupnumber);
		intent.putExtra("txntype", "Center");

		startActivityForResult(intent, 0);
	    }
	});

	// btnNext.setOnClickListener(new OnClickListener()
	// {
	// @Override
	// public void onClick(View v)
	// {
	//
	// if(!hasDone(alregularDemandsBL))
	// {
	// IntialParametrsDO intialParametrsDO = altialParametrsDO.get(0);
	// if(intialParametrsDO.MemberAttendance.equalsIgnoreCase("1") ||
	// intialParametrsDO.GLI.equalsIgnoreCase("1") ||
	// intialParametrsDO.Lateness.equalsIgnoreCase("1"))
	// {
	// Intent intent = new Intent(GroupDetails_Centerwise.this,
	// Attendense.class);
	// intent.putExtra("groupnumber", groupnumber);
	// intent.putExtra("att", ""+intialParametrsDO.MemberAttendance);
	// intent.putExtra("gli", ""+intialParametrsDO.GLI);
	// intent.putExtra("lat", ""+intialParametrsDO.Lateness);
	// intent.putExtra("meetingtime", ""+intialParametrsDO.MeetingTime);
	// intent.putExtra("groupphoto", ""+intialParametrsDO.GroupPhoto);
	// overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
	// startActivityForResult(intent,0);
	// }
	// else if(intialParametrsDO.MemberAttendance.equalsIgnoreCase("1"))
	// {
	//
	// }
	// }
	// else
	// {
	// showAlertDailog("You have already Submitted this Group Details");
	// }
	//
	//
	// }
	// });

	ivHome.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		setResult(AppConstants.RESULTCODE_HOME);
		finish();
	    }
	});

//	ivLogout.setOnClickListener(new OnClickListener() {
//	    @Override
//	    public void onClick(View v) {
//		Intent i = new Intent(GroupDetails_Centerwise.this, loginActivity.class);
//		startActivity(i);
//		// setResult(AppConstants.RESULTCODE_LOGOUT);
//		// finish();
//	    }
//	});

	/**
	 * check for no paymnet
	 * 
	 * if the group available in trasaction table then disbale the nopaymnet
	 * option
	 */

	btnNoPayment.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
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

				NoPayment_Centerwise centerwise = new NoPayment_Centerwise(alregularDemandsBL,
					new Nopaymnetlistner_Centerwise() {

				    @Override
				    public void nopaymnetCompleted() {
					HideLoader();
					showAlertDailog("Group OD marking successful.", "OK", null,
						new CustomDailoglistner() {
					    @Override
					    public void onPossitiveButtonClick(DialogInterface dialog) {
						dialog.dismiss();
						finish();
					    }

					    @Override
					    public void onNegativeButtonClick(DialogInterface dialog) {

					    }
					});
				    }
				}, GroupDetails_Centerwise.this, groupnumber);
				centerwise.start();
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
	llCenterDetails = (LinearLayout) inflater.inflate(R.layout.centerdetails, null);
	tvCollectionDate = (TextView) llCenterDetails.findViewById(R.id.tvCollectionDate);
	tvCenterCode = (TextView) llCenterDetails.findViewById(R.id.tvCenterCode);
	tvCenterName = (TextView) llCenterDetails.findViewById(R.id.tvCenterName);
	tvNumberOfAccounts = (TextView) llCenterDetails.findViewById(R.id.tvNumberOfAccounts);
	tvRegularDemandsAmount = (TextView) llCenterDetails.findViewById(R.id.tvRegularDemandsAmount);
	tvODDemandAmount = (TextView) llCenterDetails.findViewById(R.id.tvODDemandAmount);
	tvAmountTobeCollected = (TextView) llCenterDetails.findViewById(R.id.tvAmountTobeCollected);
	tvCenterCode_label = (TextView) llCenterDetails.findViewById(R.id.tvCenterCode_label);
	tvCenterName_label = (TextView) llCenterDetails.findViewById(R.id.tvCenterName_label);
	etEdit = (Button) llCenterDetails.findViewById(R.id.etEdit);
	btnNext = (Button) llCenterDetails.findViewById(R.id.btnNext);
	btnNoPayment = (Button) llCenterDetails.findViewById(R.id.btnNoPayment);
	String URL = SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
	if (URL.equals("Yes")) {

	    tvCenterCode_label.setText("SHG Code");
	    tvCenterName_label.setText("SHG Name");
	} else {

	    tvCenterCode_label.setText("Group Code");
	    tvCenterName_label.setText("Group Name");
	}

	svBase.setVisibility(View.GONE);
	btnNext.setVisibility(View.GONE);
	btnNoPayment.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llCenterDetails, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	tvHeader.setText("Group Details");
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
	} else if (resultCode == AppConstants.RESULTCODE_CENTERDETAILS) {
	    setResult(resultCode);
	    finish();
	}
    }

    @Override
    protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	float totalamout = 0;
	alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",GroupDetails_Centerwise.this);
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

class NoPayment_Centerwise extends Thread {
    private ArrayList<RegularDemandsDO> alArrayList;
    private Nopaymnetlistner_Centerwise nopaymnetlistner;
    private Context context;
    private TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
    private String groupNumber;

    public NoPayment_Centerwise(ArrayList<RegularDemandsDO> alArrayList, Nopaymnetlistner_Centerwise nopaymnetlistner,
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
	((Activity) context).runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
		nopaymnetlistner.nopaymnetCompleted();
	    }
	});
    }
}

interface Nopaymnetlistner_Centerwise {
    public abstract void nopaymnetCompleted();
}
