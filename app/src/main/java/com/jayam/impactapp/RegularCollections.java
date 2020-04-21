package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.RegularCollectionsAdapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class RegularCollections extends Base {
    private LinearLayout llRegularCollections;
    private ListView lvRegularCollections;
    private RegularCollectionsAdapter regularCollectionsAdapter;
    private RegularDemandsBL regularDemandsBL;

    @Override
    public void initialize() {
	intializeControlles();
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	regularDemandsBL = new RegularDemandsBL();
	ArrayList<RegularDemandsDO> alRegularDemandsDOs = regularDemandsBL.SelectAll(RegularCollections.this);
	regularCollectionsAdapter.refresh(alRegularDemandsDOs);
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
//		Intent i = new Intent(RegularCollections.this, loginActivity.class);
//		startActivity(i);
//		// setResult(AppConstants.RESULTCODE_LOGOUT);
//		// finish();
//	    }
//	});

    }

    public void intializeControlles() {
	llRegularCollections = (LinearLayout) inflater.inflate(R.layout.regularcollections, null);
	lvRegularCollections = (ListView) llRegularCollections.findViewById(R.id.lvDates);

	regularCollectionsAdapter = new RegularCollectionsAdapter(RegularCollections.this, null);
	lvRegularCollections.setAdapter(regularCollectionsAdapter);
	lvRegularCollections.setDivider(getResources().getDrawable(R.drawable.saparetor));
	llBaseMiddle.addView(llRegularCollections, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

	tvHeader.setText("Regular Collections");
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
