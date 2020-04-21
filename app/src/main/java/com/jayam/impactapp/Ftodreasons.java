package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.FtodreasonsAdapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.FtodreasonsBL;
import com.jayam.impactapp.objects.FtodreasonsDO;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Ftodreasons extends Base {
    private LinearLayout llftodreasons;
    private ListView lvftodreasons;
    private FtodreasonsAdapter ftodreasonsAdapter;
    private ArrayList<FtodreasonsDO> alFtodreasonsDO;
    FtodreasonsBL ftodreasonsBL;
    String MLAID, TxnType;

    @Override
    public void initialize() {
	Bundle bundle = getIntent().getExtras();
	MLAID = bundle.getString("MLAID");
	TxnType = bundle.getString("txntype");
	intializeControlles();
	ftodreasonsBL = new FtodreasonsBL();
	alFtodreasonsDO = ftodreasonsBL.SelectAll(Ftodreasons.this);
	ftodreasonsAdapter = new FtodreasonsAdapter(Ftodreasons.this, alFtodreasonsDO, MLAID, TxnType);

	lvftodreasons.setAdapter(ftodreasonsAdapter);

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
//		Intent i = new Intent(Ftodreasons.this, loginActivity.class);
//		startActivity(i);
//		// setResult(AppConstants.RESULTCODE_LOGOUT);
//		// finish();
//	    }
//	});

    }

    public void intializeControlles() {
	llftodreasons = (LinearLayout) inflater.inflate(R.layout.ftodreasons, null);
	lvftodreasons = (ListView) llftodreasons.findViewById(R.id.lvftodreasons);

	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llftodreasons, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
	super.onResume();
	alFtodreasonsDO = ftodreasonsBL.SelectAll(Ftodreasons.this);
	// regularDemandsBL.updateSavingsdAmt();
	// regularDemandsBLTemp.updateSavingsAmount();
	// centerAdapter.refresh(alRegularDemandsDOs);
    }

	@Override
	public void onBackPressed() {
		// Do Here what ever you want do on back press;
	}

}
