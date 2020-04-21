package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.businesslayer.GetDataBL;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.NetworkUtility;
import com.jayam.impactapp.utils.SharedPrefUtils;


import android.content.DialogInterface;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewDemands extends Base implements DataListner {
    private LinearLayout llNewdemand;
    private GetDataBL getDataBL, getData_downloadStatus;
    private String demandDate;
    private TextView tvToatalDemands, tvAmountTobeCollected;
    private RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
    private ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    private IntialParametrsBL intialParametrsBL;
    String printerAddress = null;

    @Override
    public void initialize() {
	intializeControlles();
	demandDate = getIntent().getExtras().getString("Date");
	getDataBL = new GetDataBL(NewDemands.this, NewDemands.this);
	intialParametrsBL = new IntialParametrsBL();
	alIntialParametrsDOs = intialParametrsBL.SelectAll(NewDemands.this);
	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
	printerAddress = intialParametrsDO.BTPrinterAddress;
	if (NetworkUtility.isNetworkConnectionAvailable(NewDemands.this)) {
	    String userID = SharedPrefUtils.getKeyValue(NewDemands.this, AppConstants.pref_name, AppConstants.username);
	    if (!regularDemandsBL.isAvailable(demandDate,NewDemands.this)) {
		ShowLoader();
		getDataBL.getNewDemands(userID, demandDate);
	    } else {
		ArrayList<RegularDemandsDO> alregulardemnads = regularDemandsBL.SelectAll(NewDemands.this);
		int totalDemands = alregulardemnads.size();
		float totalAmountTobeColleted = 0;
		for (int i = 0; i < totalDemands; i++) {
		    RegularDemandsDO demandsDO = alregulardemnads.get(i);
		    totalAmountTobeColleted = totalAmountTobeColleted
			    + (Float.valueOf(demandsDO.DemandTotal.trim()).floatValue())
			    + (Float.valueOf(demandsDO.ODAmount.trim()).floatValue());
		}
		Log.e("onDataretrieved", "Success" + totalAmountTobeColleted);
		tvToatalDemands.setText("" + totalDemands);
		tvAmountTobeCollected.setText("" + totalAmountTobeColleted);
	    }

	} else {
	    showAlertDailog(getResources().getString(R.string.nonetwork));
	}
    }

    public void intializeControlles() {
	llNewdemand = (LinearLayout) inflater.inflate(R.layout.newdemanddates, null);
	tvToatalDemands = (TextView) llNewdemand.findViewById(R.id.tvToatalDemands);
	tvAmountTobeCollected = (TextView) llNewdemand.findViewById(R.id.tvAmountTobeCollected);
	llBaseMiddle.addView(llNewdemand, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	tvHeader.setText("New Demands");
    }

    @Override
    public void onDataretrieved(Object objs) {
	ArrayList<RegularDemandsDO> alregulardemnads = (ArrayList<RegularDemandsDO>) objs;
	RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	int totalDemands = alregulardemnads.size();
	float totalAmountTobeColleted = 0;

	Log.e("onDataretrieved", "Success" + totalDemands);
	for (int i = 0; i < totalDemands; i++) {
	    RegularDemandsDO demandsDO = alregulardemnads.get(i);
	    totalAmountTobeColleted = totalAmountTobeColleted
		    + (Float.valueOf(demandsDO.DemandTotal.trim()).floatValue())
		    + (Float.valueOf(demandsDO.ODAmount.trim()).floatValue());
	}
	Log.e("onDataretrieved", "Success" + totalAmountTobeColleted);

	tvToatalDemands.setText("" + totalDemands);
	tvAmountTobeCollected.setText("" + totalAmountTobeColleted);

	HideLoader();
	Log.e("onDataretrieved", "Success");

	getData_downloadStatus = new GetDataBL(NewDemands.this, new DataListner() {
	    @Override
	    public void onDataretrieved(Object objs) {

		showAlertDailog("Demand Downloaded Successfully", "OK", null, new CustomDailoglistner() {

		    @Override
		    public void onPossitiveButtonClick(DialogInterface dialog) {
			dialog.dismiss();
			setResult(AppConstants.RESULTCODE_HOME);
			finish();
		    }

		    @Override
		    public void onNegativeButtonClick(DialogInterface dialog) {
			// TODO Auto-generated method stub

		    }
		});
	    }

	    @Override
	    public void onDataretrievalFailed(String errorMessage) {

		showAlertDailog("Demand Sync Failed,Pls try again later", "OK", null, new CustomDailoglistner() {

		    @Override
		    public void onPossitiveButtonClick(DialogInterface dialog) {
			dialog.dismiss();
			finish();
			final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
			regularDemandsBL.Truncatetabel(NewDemands.this);
		    }

		    @Override
		    public void onNegativeButtonClick(DialogInterface dialog) {
			// TODO Auto-generated method stub

		    }
		});
	    }
	});
	// getData_downloadStatus.verifyDownloads(demandDate,
	// ""+alregulardemnads.size());
	getData_downloadStatus.verifyDownloads(demandDate, "" + alregulardemnads.size(), printerAddress);

    }

    @Override
    protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
    }

    @Override
    public void onDataretrievalFailed(String errorMessage) {
	HideLoader();
	Log.e("onDataretrievalFailed", "failed");
	showAlertDailog(errorMessage, "OK", null, new CustomDailoglistner() {

	    @Override
	    public void onPossitiveButtonClick(DialogInterface dialog) {
		dialog.dismiss();
		finish();
	    }

	    @Override
	    public void onNegativeButtonClick(DialogInterface dialog) {
		// TODO Auto-generated method stub

	    }
	});
    }

}
