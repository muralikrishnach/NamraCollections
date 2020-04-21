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

public class ConfirmationScreen_Centerwise extends Base implements UpdatereciprnUmbers_Centerwise {
    private TextView tvCenterName, tvgroupname, tvCollectedAmts, tvAttendense, tvMeetingStartTime, tvMeetingEndTime;
    private String centernuber, attendesne, attendense_total;
    private Button btnConfirm;
    private float collectedAmt;
    private ArrayList<RegularDemandsDO> vecRegularDemands;
    private TrnsactionsBL trnsactionsBL;
    private LinearLayout llGroup;
    int LastTxn;
    String latitude, langitude;
    //GpsTracker gps;
    
    GPSTrackevalue gps;

    @Override
    public void initialize() {
	intializeControlles();
	trnsactionsBL = new TrnsactionsBL();
	centernuber = getIntent().getExtras().getString("centernuber");

	Log.e("groupnumber", "" + centernuber);

	RegularDemandsBLTemp bl = new RegularDemandsBLTemp();
	String CenterName = bl.getCenterName_Centerwise(centernuber,ConfirmationScreen_Centerwise.this);
	// String groupname = bl.getGroupName(groupNumber)
	attendesne = bl.getAttandense_Centerwise(centernuber,ConfirmationScreen_Centerwise.this);
	attendense_total = bl.getTotalMemebes_Centerwise(centernuber,ConfirmationScreen_Centerwise.this);
	vecRegularDemands = bl.SelectAll(centernuber, "CNo",ConfirmationScreen_Centerwise.this);

	for (int i = 0; i < vecRegularDemands.size(); i++) {
	    collectedAmt = collectedAmt + (Float.valueOf(vecRegularDemands.get(i).updated.trim()).floatValue());

	}
	// String collectdAmount = collectedAmt;
	// String attendesne =
	// SharedPrefUtils.getKeyValue(ConfirmationScreen_Centerwise.this,
	// AppConstants.memberDetails_pref, AppConstants.attendense);

	String meeting_start = SharedPrefUtils.getKeyValue(ConfirmationScreen_Centerwise.this,
		AppConstants.memberDetails_pref, AppConstants.meetingStartTime);
	String meeting_end = SharedPrefUtils.getKeyValue(ConfirmationScreen_Centerwise.this,
		AppConstants.memberDetails_pref, AppConstants.meetingEnd);
	// String attendense_total =
	// SharedPrefUtils.getKeyValue(ConfirmationScreen_Centerwise.this,
	// AppConstants.memberDetails_pref, AppConstants.attendense_total);

	tvCenterName.setText("" + CenterName);
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
		Intent i = new Intent(ConfirmationScreen_Centerwise.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	btnConfirm.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	//	gps = new GpsTracker(ConfirmationScreen_Centerwise.this);
		
		gps = new GPSTrackevalue(ConfirmationScreen_Centerwise.this);
		
		
		
		Log.v("", "gps.canGetLocation()" + gps.canGetLocation());
		if (gps.canGetLocation()) {
		    latitude = gps.getLatitude() + "";
		    langitude = gps.getLongitude() + "";
		    
		    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + langitude, Toast.LENGTH_LONG).show();
		    showAlertDailog("Are you sure to submit the Group Detail and sync to server?", "Yes", "No",
			    new CustomDailoglistner() {
			@Override
			public void onPossitiveButtonClick(DialogInterface dialog) {
			    dialog.dismiss();
			    ShowLoader();
			    UpdatereciptNumbers updatereciptNumbers = new UpdatereciptNumbers(vecRegularDemands,
				    ConfirmationScreen_Centerwise.this);
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

	View vgroup = llConfirmationScreen.findViewById(R.id.vgroup);
	llGroup = (LinearLayout) llConfirmationScreen.findViewById(R.id.llGroup);
	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llConfirmationScreen, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	tvHeader.setText("Confirmation");
	llGroup.setVisibility(View.GONE);
	vgroup.setVisibility(View.GONE);
    }

    class UpdatereciptNumbers extends Thread {
	private ArrayList<RegularDemandsDO> alArrayList;
	private UpdatereciprnUmbers_Centerwise listner;

	public UpdatereciptNumbers(ArrayList<RegularDemandsDO> alArrayList, UpdatereciprnUmbers_Centerwise listner) {
	    this.alArrayList = alArrayList;
	    this.listner = listner;
	}

	@Override
	public void run() {
	    super.run();

	    for (int i = 0; i < alArrayList.size(); i++) {

		// IntialParametrsBL intialParametrsBL = new
		// IntialParametrsBL();
		//
		// String[] id =
		// StringUtils.getRecieptNumber(alArrayList.get(i)).split("-");
		// intialParametrsBL.updateReceiptNumber(String.valueOf(Integer.parseInt(id[id.length-1])+1).toString());
		//
		RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		regularDemandsBL.updateReciptNumbers(alArrayList.get(i).MLAI_ID,
			StringUtils.getRecieptNumber(alArrayList.get(i),ConfirmationScreen_Centerwise.this),ConfirmationScreen_Centerwise.this);
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
		regularDemandsBL.update(vecRegularDemands,ConfirmationScreen_Centerwise.this);

		vecRegularDemands = regularDemandsBL.SelectAll(centernuber, "CNo",ConfirmationScreen_Centerwise.this);
		String CollAmt1;

		updateTransactionTable(vecRegularDemands);

		final IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		final String LastTranScode = StringUtils.getTransactionCode_C(vecRegularDemands.get(0),ConfirmationScreen_Centerwise.this);
		int LastTaxn = Integer.parseInt(intialParametrsBL.SelectAll(ConfirmationScreen_Centerwise.this).get(0).LastTransactionCode);
		LastTaxn = LastTaxn + 1;
		for (RegularDemandsDO obj : vecRegularDemands) {
		    regularDemandsTemp = b2.SelectMemberDetails_MALIID(obj.MLAI_ID,ConfirmationScreen_Centerwise.this);
		    float TotDem = (Float.valueOf(regularDemandsTemp.DemandTotal).floatValue()
			    + Float.valueOf(regularDemandsTemp.ODAmount).floatValue());
		    float Update = Float.valueOf(regularDemandsTemp.updated).floatValue();
		    String CollAmt = trnsactionsBL.getMemberCollAmt(obj.MLAI_ID,ConfirmationScreen_Centerwise.this);
		    if (CollAmt == null) {
			CollAmt1 = "0";
		    } else {
			CollAmt1 = CollAmt;
		    }
		    TotalCollectedAmt = Update + Float.valueOf(CollAmt1).floatValue();
		    balance = TotDem - TotalCollectedAmt;

		    String[] id = StringUtils.getRecieptNumber(obj,ConfirmationScreen_Centerwise.this).split("-");
		    String CollType = "R";
		    String MemCount = trnsactionsBL.getMemberCount(obj.MLAI_ID,ConfirmationScreen_Centerwise.this);

		    if (MemCount.equals("0")) {

			CollType = "R";
		    } else {
			if (TotalCollectedAmt == TotDem) {
			    trnsactionsBL.updateFTODReasons(obj.MLAI_ID,ConfirmationScreen_Centerwise.this);
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
			regularDemandsBL.updateCollectedAmt(obj.MLAI_ID, TotalCollectedAmt,ConfirmationScreen_Centerwise.this);
		    } else {
			regularDemandsBL.updateCollectedAmt(obj.MLAI_ID, TotDem,ConfirmationScreen_Centerwise.this);
		    }
		    float AmntC = Float.valueOf(obj.collectedAmount).floatValue();
		    Log.e("AmtC", String.valueOf(AmntC));
		    if (MemCount.equals("0")) {
			Log.e("Inserted", "saved");
			intialParametrsBL.updateReceiptNumber(String.valueOf(Integer.parseInt(id[id.length - 1]) + 1).toString(),ConfirmationScreen_Centerwise.this);
			trnsactionsBL.Insert(obj, StringUtils.getRecieptNumber(obj,ConfirmationScreen_Centerwise.this), LastTranScode, CollType,ConfirmationScreen_Centerwise.this);
				String Latecollection="";
				if(obj!=null)
				{
					Latecollection=obj.LateCollection;
				}
				Log.v("","Latecollection67"+Latecollection);
				if(Latecollection!=null&&!Latecollection.isEmpty()&&Latecollection.trim().equalsIgnoreCase("Yes"))
				{
					trnsactionsBL.InsertLate(obj, StringUtils.getRecieptNumber(obj,ConfirmationScreen_Centerwise.this), LastTranScode, CollType,ConfirmationScreen_Centerwise.this);
				}
		    }
		    else if (AmntC > 0) {
			Log.e("Inserted", "notsaved");
			intialParametrsBL.updateReceiptNumber(
				String.valueOf(Integer.parseInt(id[id.length - 1]) + 1).toString(),ConfirmationScreen_Centerwise.this);
			trnsactionsBL.Insert(obj, StringUtils.getRecieptNumber(obj,ConfirmationScreen_Centerwise.this), LastTranScode, CollType,ConfirmationScreen_Centerwise.this);
				String Latecollection="";
				if(obj!=null)
				{
					Latecollection=obj.LateCollection;
				}
				Log.v("","Latecollection67"+Latecollection);
				if(Latecollection!=null&&!Latecollection.isEmpty()&&Latecollection.trim().equalsIgnoreCase("Yes"))
				{
					trnsactionsBL.InsertLate(obj, StringUtils.getRecieptNumber(obj,ConfirmationScreen_Centerwise.this), LastTranScode, CollType,ConfirmationScreen_Centerwise.this);
				}
		    }
		    regularDemandsBL.updateSavedAmt(obj.MLAI_ID, "0",ConfirmationScreen_Centerwise.this);
		    b2.updateAmt(obj.MLAI_ID, balance,ConfirmationScreen_Centerwise.this);

		}
		trnsactionsBL.Insertlanglat(langitude, latitude, centernuber, "Center",ConfirmationScreen_Centerwise.this);
		intialParametrsBL.updateLastTransctionCode(LastTaxn + "",ConfirmationScreen_Centerwise.this);
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			HideLoader();

			Intent intent = new Intent(ConfirmationScreen_Centerwise.this, TransactionACK_Centerwise.class);
			intent.putExtra("Center", centernuber);
			intent.putExtra("LastTranScode", LastTranScode);
			startActivityForResult(intent, 1234);
		    }
		});
	    }
	}).start();

    }

    public void updateTransactionTable(ArrayList<RegularDemandsDO> alRegularDemandsDOs) {
	for (RegularDemandsDO regularDemandsDO : alRegularDemandsDOs) {
	    // float totalAmt, collectedAmt;

	    // totalAmt =
	    // Float.valueOf(regularDemandsDO.DemandTotal).floatValue() +
	    // Float.valueOf(regularDemandsDO.ODAmount).floatValue();
	    // if( regularDemandsDO.collectedAmount != null)
	    // collectedAmt =
	    // Float.valueOf(regularDemandsDO.collectedAmount).floatValue();
	    // else
	    // collectedAmt = 0;

	    // if(totalAmt - collectedAmt == 0)
	    // {
	    // if(regularDemandsDO.RecieptGenerated == null)
	    // {
	    // trnsactionsBL.Insert(regularDemandsDO);
	    // RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	    // regularDemandsBL.updateTransactionStatus(regularDemandsDO);
	    //
	    // }
	    // }
	    // else
	    // {
	    // trnsactionsBL.Insert(regularDemandsDO);
	    // }

	    Log.e("savedamt", "" + regularDemandsDO.savedAmt);

	}

    }

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

interface UpdatereciprnUmbers_Centerwise {
    public abstract void onUpdateCompleted();

    public void transactionAdded();
}
