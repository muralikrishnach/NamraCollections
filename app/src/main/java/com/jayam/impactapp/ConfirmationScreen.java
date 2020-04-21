package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.StringUtils;


import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmationScreen extends Base implements UpdatereciprnUmbers {
    private TextView tvCenterName, tvgroupname, tvCollectedAmts, tvAttendense, tvMeetingStartTime, tvMeetingEndTime;
    private String groupnumber, centername, collectedamount, centernuber;
    private Button btnConfirm;
    private float collectedAmt;
    private ArrayList<RegularDemandsDO> vecRegularDemands;
    private TrnsactionsBL trnsactionsBL;
    int LastTaxn;
    String latitude, langitude;
  //  GpsTracker gps;
    GPSTrackevalue gps;

    @Override
    public void initialize() {
	intializeControlles();
	trnsactionsBL = new TrnsactionsBL();
	centernuber = getIntent().getExtras().getString("centernuber");
	// Toast.makeText(getApplicationContext(), centernuber,
	// Toast.LENGTH_LONG).show();
	groupnumber = getIntent().getExtras().getString("groupnumber");
	centername = getIntent().getExtras().getString("centername");
	collectedamount = getIntent().getExtras().getString("collectedamount");
	Log.e("groupnumber", "" + groupnumber);
	RegularDemandsBLTemp bl = new RegularDemandsBLTemp();
	String CenterName = bl.getCenterName(groupnumber,ConfirmationScreen.this);
	String groupname = bl.getGroupName(groupnumber,ConfirmationScreen.this);
	RegularDemandsBL gp = new RegularDemandsBL();
	String gpname = gp.getGroupName(groupnumber,ConfirmationScreen.this);
	vecRegularDemands = bl.SelectAll(groupnumber, "Groups",ConfirmationScreen.this);

	for (int i = 0; i < vecRegularDemands.size(); i++) {
	    collectedAmt = collectedAmt + (Float.valueOf(vecRegularDemands.get(i).updated.trim()).floatValue());
	}
	// String collectdAmount = collectedAmt;
	String attendesne = SharedPrefUtils.getKeyValue(ConfirmationScreen.this, AppConstants.memberDetails_pref,
		AppConstants.attendense);
	String meeting_start = SharedPrefUtils.getKeyValue(ConfirmationScreen.this, AppConstants.memberDetails_pref,
		AppConstants.meetingStartTime);
	String meeting_end = SharedPrefUtils.getKeyValue(ConfirmationScreen.this, AppConstants.memberDetails_pref,
		AppConstants.meetingEnd);
	String attendense_total = SharedPrefUtils.getKeyValue(ConfirmationScreen.this, AppConstants.memberDetails_pref,
		AppConstants.attendense_total);

	/*
	 * if(centernuber!=null) { tvCenterName.setText(""+centername);
	 * tvgroupname.setText(""+gpname);
	 * tvCollectedAmts.setText(""+collectedamount);
	 * tvAttendense.setText(""+attendesne+" / "+attendense_total);
	 * tvMeetingStartTime.setText(""+meeting_start);
	 * tvMeetingEndTime.setText(""+meeting_end); }
	 */

	tvCenterName.setText("" + CenterName);
	tvgroupname.setText("" + groupname);
	tvCollectedAmts.setText("" + collectedAmt);
	tvAttendense.setText("" + attendesne + " / " + attendense_total);
	tvMeetingStartTime.setText("" + meeting_start);
	tvMeetingEndTime.setText("" + meeting_end);

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
		Intent i = new Intent(ConfirmationScreen.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	btnConfirm.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		gps = new GPSTrackevalue(ConfirmationScreen.this);
		Log.d("mfimo", "gps.canGetLocation()" + gps.canGetLocation());
		if (gps.canGetLocation()) {
		    latitude = gps.getLatitude() + "";
		    langitude = gps.getLongitude() + "";
		    Log.d("mfimo", "latitude" + latitude);
		    Log.d("mfimo", "langitude" + langitude);
		    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + langitude, Toast.LENGTH_LONG).show();
		    showAlertDailog("Are you sure to submit the Group Detail and sync to server?", "Yes", "No", new CustomDailoglistner() {
			@Override
			public void onPossitiveButtonClick(DialogInterface dialog) {
			    dialog.dismiss();
			    ShowLoader();
			    UpdatereciptNumbers updatereciptNumbers = new UpdatereciptNumbers(vecRegularDemands,
				    ConfirmationScreen.this);
			    updatereciptNumbers.start();
			}

			@Override
			public void onNegativeButtonClick(DialogInterface dialog) {
			    dialog.dismiss();
			}
		    });
		} else {
		    gps.showSettingsAlert();
		}
	    }
	});
    }

    @SuppressWarnings("deprecation")
    public void intializeControlles() {
	LinearLayout llConfirmationScreen = (LinearLayout) inflater.inflate(R.layout.confirmationscreen, null);
	tvCenterName = (TextView) llConfirmationScreen.findViewById(R.id.tvCenterName);
	tvgroupname = (TextView) llConfirmationScreen.findViewById(R.id.tvgroupname);
	tvCollectedAmts = (TextView) llConfirmationScreen.findViewById(R.id.tvCollectedAmts);
	tvAttendense = (TextView) llConfirmationScreen.findViewById(R.id.tvAttendense);
	tvMeetingStartTime = (TextView) llConfirmationScreen.findViewById(R.id.tvMeetingStartTime);
	tvMeetingEndTime = (TextView) llConfirmationScreen.findViewById(R.id.tvMeetingEndTime);
	btnConfirm = (Button) llConfirmationScreen.findViewById(R.id.btnConfirm);
	TextView GName = (TextView) llConfirmationScreen.findViewById(R.id.tvGName);
	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llConfirmationScreen, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
	String URL = SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
	if (URL.equals("Yes")) {
	    GName.setText("SHG Name");
	}
	tvHeader.setText("Confirmation");
    }

    class UpdatereciptNumbers extends Thread {
	private ArrayList<RegularDemandsDO> alArrayList;
	private UpdatereciprnUmbers listner;

	public UpdatereciptNumbers(ArrayList<RegularDemandsDO> alArrayList, UpdatereciprnUmbers listner) {
	    this.alArrayList = alArrayList;
	    this.listner = listner;
	}

	@Override
	public void run() {
	    super.run();

	    for (int i = 0; i < alArrayList.size(); i++) {
		RegularDemandsBL regularDemandsBL = new RegularDemandsBL();

		regularDemandsBL.updateReciptNumbers(alArrayList.get(i).MLAI_ID, StringUtils.getRecieptNumber(alArrayList.get(i),ConfirmationScreen.this),ConfirmationScreen.this);
	    }

	    runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    listner.onUpdateCompleted();
		}
	    });
	}
    }

    @Override
    public void onUpdateCompleted() {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		float balance = 0;
		float TotalCollectedAmt = 0;

		RegularDemandsBLTemp b2 = new RegularDemandsBLTemp();
		RegularDemandsDO regularDemandsTemp;

		RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		regularDemandsBL.update(vecRegularDemands,ConfirmationScreen.this);

		vecRegularDemands = regularDemandsBL.SelectAll(groupnumber, "Groups",ConfirmationScreen.this);
		String CollAmt1;
		regularDemandsBL.updatesavefb(groupnumber, "group",ConfirmationScreen.this);
		// updateTransactionTable(vecRegularDemands);
		final IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		final String LastTranScode = StringUtils.getTransactionCode_G(vecRegularDemands.get(0),ConfirmationScreen.this);
		LastTaxn = Integer.parseInt(intialParametrsBL.SelectAll(ConfirmationScreen.this).get(0).LastTransactionCode);
		LastTaxn = LastTaxn + 1;
		for (RegularDemandsDO obj : vecRegularDemands) {
		    regularDemandsTemp = b2.SelectMemberDetails_MALIID(obj.MLAI_ID,ConfirmationScreen.this);
		    float TotDem = (Float.valueOf(regularDemandsTemp.DemandTotal).floatValue()
			    + Float.valueOf(regularDemandsTemp.ODAmount).floatValue());
		    float Update = Float.valueOf(regularDemandsTemp.updated).floatValue();
		    String CollAmt = trnsactionsBL.getMemberCollAmt(obj.MLAI_ID,ConfirmationScreen.this);
		    if (CollAmt == null) {
			CollAmt1 = "0";
		    } else {
			CollAmt1 = CollAmt;
		    }
		    TotalCollectedAmt = Update + Float.valueOf(CollAmt1).floatValue();
		    balance = TotDem - TotalCollectedAmt;

		    String[] id = StringUtils.getRecieptNumber(obj,ConfirmationScreen.this).split("-");
		    String CollType = "R";
		    String MemCount = trnsactionsBL.getMemberCount(obj.MLAI_ID,ConfirmationScreen.this);

		    if (MemCount.equals("0")) {

			CollType = "R";
		    } else {
			if (TotalCollectedAmt == TotDem) {
			    trnsactionsBL.updateFTODReasons(obj.MLAI_ID,ConfirmationScreen.this);
			} else {

			}
			float OSAmt = Float.valueOf(obj.OSAmt).floatValue();
			float CollectedAmt = Float.valueOf(obj.collectedAmount).floatValue();
			float PreviousCollAmt = Float.valueOf(CollAmt);
			if (CollectedAmt == (OSAmt - PreviousCollAmt)) {
			    CollType = "P";
			} else {
			    CollType = "OA";
			}
		    }
		    if (TotalCollectedAmt <= TotDem) {
			regularDemandsBL.updateCollectedAmt(obj.MLAI_ID, TotalCollectedAmt,ConfirmationScreen.this);
		    } else {
			regularDemandsBL.updateCollectedAmt(obj.MLAI_ID, TotDem,ConfirmationScreen.this);
		    }
		    float AmntC = Float.valueOf(obj.collectedAmount).floatValue();

		    Log.e("AmtC", String.valueOf(AmntC));
		    if (MemCount.equals("0")) {
			Log.e("Inserted", "saved");
			// String
			// str=id[0]+id[1]+id[2]+id[3]+id[4]+id[5]+(Integer.parseInt(id[id.length-1])+1);
			intialParametrsBL.updateReceiptNumber(String.valueOf(Integer.parseInt(id[id.length - 1]) + 1).toString(),ConfirmationScreen.this);
			trnsactionsBL.Insert(obj, StringUtils.getRecieptNumber(obj,ConfirmationScreen.this), LastTranScode, CollType,ConfirmationScreen.this);
				String Latecollection="";
				if(obj!=null)
				{
					Latecollection=obj.LateCollection;
				}
				Log.v("","Latecollection67"+Latecollection);
				if(Latecollection!=null&&!Latecollection.isEmpty()&&Latecollection.trim().equalsIgnoreCase("Yes"))
				{
					trnsactionsBL.InsertLate(obj, StringUtils.getRecieptNumber(obj,ConfirmationScreen.this), LastTranScode, CollType,ConfirmationScreen.this);
				}

		    } else if (AmntC > 0) {
			Log.e("Inserted", "notsaved");
			intialParametrsBL.updateReceiptNumber(String.valueOf(Integer.parseInt(id[id.length - 1]) + 1).toString(),ConfirmationScreen.this);

			trnsactionsBL.Insert(obj, StringUtils.getRecieptNumber(obj,ConfirmationScreen.this), LastTranScode, CollType,ConfirmationScreen.this);
				String Latecollection="";
				if(obj!=null)
				{
					Latecollection=obj.LateCollection;
				}
				Log.v("","Latecollection67"+Latecollection);
				if(Latecollection!=null&&!Latecollection.isEmpty()&&Latecollection.trim().equalsIgnoreCase("Yes"))
				{
					trnsactionsBL.InsertLate(obj, StringUtils.getRecieptNumber(obj,ConfirmationScreen.this), LastTranScode, CollType,ConfirmationScreen.this);
				}
		    }


		    trnsactionsBL.Insertlanglat(langitude, latitude, groupnumber, "Group",ConfirmationScreen.this);
		    regularDemandsBL.updateSavedAmt(obj.MLAI_ID, "0",ConfirmationScreen.this);
		    b2.updateAmt(obj.MLAI_ID, balance,ConfirmationScreen.this);
		    String CNO = regularDemandsBL.SelectCenterNumber(groupnumber,ConfirmationScreen.this).get(0).CNo;
		    if (Integer.parseInt(trnsactionsBL.getTransactionCount(CNO,ConfirmationScreen.this)) > 0) {
			trnsactionsBL.updatePIC(CNO, obj.probInCenter,ConfirmationScreen.this);
		    }

		}

		intialParametrsBL.updateLastTransctionCode(LastTaxn + "",ConfirmationScreen.this);
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			HideLoader();
			Intent intent = new Intent(ConfirmationScreen.this, TransactionACK.class);
			intent.putExtra("LastTranScode", LastTranScode);
			intent.putExtra("Groups", groupnumber);
			startActivityForResult(intent, 1234);
		    }
		});
	    }
	}).start();
    }

    // public void updateTransactionTable(ArrayList<RegularDemandsDO>
    // alRegularDemandsDOs)
    // {
    // for(RegularDemandsDO regularDemandsDO :alRegularDemandsDOs)
    // {
    //
    // Log.e("savedamt", ""+regularDemandsDO.savedAmt);
    // }
    //
    // }

    @Override
    public void transactionAdded() {

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

}

interface UpdatereciprnUmbers {
    public abstract void onUpdateCompleted();

    public void transactionAdded();
}
