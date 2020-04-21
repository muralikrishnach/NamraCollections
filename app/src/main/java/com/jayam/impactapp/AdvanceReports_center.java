package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.AdvanceReports_center_Adapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AdvanceReports_center extends Base
{
	private LinearLayout llCenters;
	private ListView lvCenters;
	private AdvanceReports_center_Adapter advcenterAdapter;
	private ArrayList<AdvaceDemandDO> advanceDemandsDO;
	AdvanceDemandBL advanceDemandsBL;
	String centername = null;
	@Override
	public void initialize() 
	{
		intializeControlles();
		
		advanceDemandsBL = new AdvanceDemandBL();
		advanceDemandsDO = advanceDemandsBL.SelectReports_Centers(AdvanceReports_center.this);
		
		advcenterAdapter = new  AdvanceReports_center_Adapter(AdvanceReports_center.this, null,advanceDemandsDO);
		lvCenters.setAdapter(advcenterAdapter);
		ivHome.setOnClickListener(new  OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				setResult(AppConstants.RESULTCODE_HOME);
				finish();
			}
		});
		
//		ivLogout.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Intent i = new Intent(AdvanceReports_center.this,loginActivity.class);
//				startActivity(i);
//				//setResult(AppConstants.RESULTCODE_LOGOUT);
//				//finish();
//			}
//		});
		
		
		
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llCenters		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvCenters		=	(ListView)llCenters.findViewById(R.id.lvCenters);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llCenters, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
		tvHeader.setText("Centers");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == AppConstants.RESULTCODE_LOGOUT)
		{
			setResult(resultCode);
			finish();
		}
		else if(resultCode == AppConstants.RESULTCODE_HOME)
		{
			setResult(resultCode);
			finish();
		}
			
	}
	
	@Override
	protected void onResume()
	{
		
		super.onResume();
		advanceDemandsDO = advanceDemandsBL.SelectReports_Centers(AdvanceReports_center.this);
//		advanceDemandsBL.updateCollectedAmt();
		advcenterAdapter.refresh(advanceDemandsDO);
	}
	
	

}
