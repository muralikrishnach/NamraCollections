package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.DemandDatesAdapter;
import com.jayam.impactapp.businesslayer.GetDataBL;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.objects.DemandDateDO;
import com.jayam.impactapp.utils.NetworkUtility;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DemandDates extends Base implements DataListner {
    private LinearLayout llDemandDates;
    private ListView lvDates;
    private DemandDatesAdapter datesAdapter;
    private GetDataBL dataBL;
    ArrayList<DemandDateDO> aDateDOs;

    @Override
    public void initialize() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);
	intializeControlles();
	datesAdapter = new DemandDatesAdapter(DemandDates.this, new ArrayList<DemandDateDO>());
	lvDates.setAdapter(datesAdapter);
	lvDates.setDivider(getResources().getDrawable(R.drawable.saparetor));
	dataBL = new GetDataBL(DemandDates.this, DemandDates.this);
	if (NetworkUtility.isNetworkConnectionAvailable(DemandDates.this)) {
	    ShowLoader();
	    dataBL.getDates();
	} else {
	    showAlertDailog(getResources().getString(R.string.nonetwork));
	}

    }

    public void intializeControlles() {
	llDemandDates = (LinearLayout) inflater.inflate(R.layout.demandates, null);
	lvDates = (ListView) llDemandDates.findViewById(R.id.lvDates);
	llBaseMiddle.addView(llDemandDates, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	tvHeader.setText("Demand Dates");
    }

    @Override
    public void onDataretrieved(Object objs) {
	HideLoader();
	aDateDOs = (ArrayList<DemandDateDO>) objs;
	datesAdapter.refresh(aDateDOs);
    }

    @Override
    public void onDataretrievalFailed(String errorMessage) {
	HideLoader();

	showAlertDailog("No Demand Dates", "OK", null, new CustomDailoglistner() {

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

    @Override
    protected void onResume() {
	super.onResume();
	datesAdapter.refresh(aDateDOs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode == AppConstants.RESULTCODE_HOME) {
	    setResult(resultCode);
	    finish();
	}
    }
}
