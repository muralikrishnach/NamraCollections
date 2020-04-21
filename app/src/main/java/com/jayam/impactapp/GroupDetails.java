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

public class GroupDetails extends Base {

    private LinearLayout llCenterDetails;
    private TextView tvCollectionDate,tvProductname, tvCenterCode, tvCenterName, tvNumberOfAccounts, tvRegularDemandsAmount,
	    tvODDemandAmount, tvAmountTobeCollected;
    private TextView tvCenterCode_label, tvCenterName_label;
    private String groupnumber;
    private Button etEdit, btnNext;
    private IntialParametrsBL intialParametrsBL;
    private ArrayList<IntialParametrsDO> altialParametrsDO;
    private ArrayList<RegularDemandsDO> alregularDemandsBL;
    private RegularDemandsBL regularDemandsBL;
    private Button btnNoPayment;
    private float AmountSaved = 0;

    @Override
    public void initialize() {
	groupnumber = getIntent().getExtras().getString("groupnumber");

	intializeControlles();

	intialParametrsBL = new IntialParametrsBL();
	altialParametrsDO = intialParametrsBL.SelectAll(GroupDetails.this);

	regularDemandsBL = new RegularDemandsBL();
	alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",GroupDetails.this);

	tvCollectionDate.setText("" + alregularDemandsBL.get(0).DemandDate);

	tvCenterCode.setText("" + alregularDemandsBL.get(0).GNo);
	tvCenterName.setText("" + alregularDemandsBL.get(0).GroupName);
	tvNumberOfAccounts.setText("" + alregularDemandsBL.size());
		tvProductname.setText("" + alregularDemandsBL.get(0).ProductName);
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

	SharedPrefUtils.setValue(GroupDetails.this, AppConstants.memberDetails_pref, keyValue_amountToBeCollected);

	etEdit.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(GroupDetails.this, GroupMembers.class);
		intent.putExtra("groupnumber", groupnumber);
		intent.putExtra("txntype", "Group");
		startActivityForResult(intent, 0);
	    }
	});

	btnNext.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
		String MCount = trnsactionsBL.getGroupCount(groupnumber, "Group",GroupDetails.this);
		Log.e("MCOUNT:-", MCount);
		int MC = Integer.parseInt(MCount);
		if (MC >= 2) {
		    showAlertDailog("Two Transactions have completed for this group");
		    return;
		}
		if (!hasDone(alregularDemandsBL) || AmountSaved > 0) {
		    IntialParametrsDO intialParametrsDO = altialParametrsDO.get(0);
		    if (intialParametrsDO.MemberAttendance.equalsIgnoreCase("1")
			    || intialParametrsDO.GLI.equalsIgnoreCase("1")
			    || intialParametrsDO.Lateness.equalsIgnoreCase("1")) {
			Intent intent = new Intent(GroupDetails.this, Attendense.class);
			intent.putExtra("groupnumber", groupnumber);
			intent.putExtra("att", "" + intialParametrsDO.MemberAttendance);
			intent.putExtra("gli", "" + intialParametrsDO.GLI);
			intent.putExtra("lat", "" + intialParametrsDO.Lateness);
			intent.putExtra("meetingtime", "" + intialParametrsDO.MeetingTime);
			intent.putExtra("groupphoto", "" + intialParametrsDO.GroupPhoto);
			if (getIntent().hasExtra("position")) {
			    Log.d("mfimo", "group details");
			    intent.putExtra("position", "last");
			}
			overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
			startActivityForResult(intent, 0);
		    } else if (intialParametrsDO.MemberAttendance.equalsIgnoreCase("1")) {

		    }
		} else {
		    showAlertDailog("You have already Submitted this Group Details");
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
		Intent i = new Intent(GroupDetails.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	/**
	 * check for no paymnet
	 * 
	 * if the group available in trasaction table then disbale the nopaymnet
	 * option
	 */

	TrnsactionsBL bl = new TrnsactionsBL();
	String count = bl.checkGroupAvailbleIntransactiontable(groupnumber,GroupDetails.this);

	if (Integer.parseInt(count) == 0) {
	    btnNoPayment.setVisibility(View.VISIBLE);
	} else {
	    btnNoPayment.setVisibility(View.GONE);
	}

	btnNoPayment.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		String ODMembers = regularDemandsBL.getODMembers(groupnumber, "Group",GroupDetails.this);
		if (ODMembers.equals("1")) {
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
				    NoPayment noPayment = new NoPayment(alregularDemandsBL, new Nopaymnetlistner() {
					@Override
					public void nopaymnetCompleted() {
					    HideLoader();
					    showAlertDailog("Group OD marking successful.", "OK", null,
						    new CustomDailoglistner() {
						@Override
						public void onPossitiveButtonClick(DialogInterface dialog) {
						    dialog.dismiss();
						    if (getIntent().hasExtra("position")) {
						    	Log.d("mfimo", "last group");
						    	Intent i = new Intent(GroupDetails.this, ProbInCenterGW.class);
							    i.putExtra("groupnumber", groupnumber);
								i.putExtra("position", "last");
								i.putExtra("from", "ftod");
							    startActivityForResult(i, 12345);
						    }else{
						    	setResult(AppConstants.RESULTCODE_HOME);
							    finish();
						    }
						}

						@Override
						public void onNegativeButtonClick(DialogInterface dialog) {

						}
					    });
					}
				    }, GroupDetails.this, groupnumber);
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
		    ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(GroupDetails.this);
		    if (alin.get(0).rmelUser.equalsIgnoreCase("N") && getIntent().hasExtra("position")) {
			Intent i = new Intent(GroupDetails.this, ProbInCenterGW.class);
			i.putExtra("groupnumber", groupnumber);
			i.putExtra("Type", "Group");
			i.putExtra("from", "ftodforward");
			startActivityForResult(i, 111);
		    } else {
			Intent i = new Intent(GroupDetails.this, Ftodreasonscenterwise.class);
			i.putExtra("CCode", groupnumber);
			i.putExtra("Type", "Group");
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
	tvCenterCode_label = (TextView) llCenterDetails.findViewById(R.id.tvCenterCode_label);
	tvCenterName_label = (TextView) llCenterDetails.findViewById(R.id.tvCenterName_label);
	etEdit = (Button) llCenterDetails.findViewById(R.id.etEdit);
	btnNext = (Button) llCenterDetails.findViewById(R.id.btnNext);
	btnNoPayment = (Button) llCenterDetails.findViewById(R.id.btnNoPayment);

	String URL = SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
	if (URL.equals("Yes")) {

	    tvCenterCode_label.setText("SHG Code");
	    tvCenterName_label.setText("SHG Name");
	    tvHeader.setText("SHG Details");
	} else {

	    tvCenterCode_label.setText("Group Code");
	    tvCenterName_label.setText("Group Name");
	    tvHeader.setText("Group Details");
	}

	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llCenterDetails, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	// tvHeader.setText("Group Details");
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
	alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",GroupDetails.this);
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

class NoPayment extends Thread {
    private ArrayList<RegularDemandsDO> alArrayList;
    private Nopaymnetlistner nopaymnetlistner;
    private Context context;
    private TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
    private String groupNumber;

    public NoPayment(ArrayList<RegularDemandsDO> alArrayList, Nopaymnetlistner nopaymnetlistner, Context context,
	    String groupNumber) {
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

interface Nopaymnetlistner {
    public abstract void nopaymnetCompleted();
}
