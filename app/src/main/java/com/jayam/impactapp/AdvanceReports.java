package com.jayam.impactapp;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.AdvanceDemandBL;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class AdvanceReports extends Base
{
	private LinearLayout llTranscations;
	private Button btnCollectionReport, btnSummaryReport;
	@Override
	public void initialize()
	{
		intializeControlles();
		
		btnCollectionReport.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AdvanceDemandBL regularDemandsBL = new AdvanceDemandBL();
				 String count = regularDemandsBL.SelectCountcenters(AdvanceReports.this);
				if(Integer.parseInt(count) == 0)
				{
					showAlertDailog("No Collection Data available");   
				}
				else
				{
					Intent intent = new Intent(AdvanceReports.this, AdvanceReports_center.class);
					startActivityForResult(intent, 12345);
				}
			}
		});
		
		btnSummaryReport.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new  Intent(AdvanceReports.this, AdvanceReports_Summary.class);
				startActivityForResult(intent,1234);
			}
		});
		
		
		
		ivHome.setOnClickListener(new  OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				setResult(AppConstants.RESULTCODE_HOME);
				finish();
			}
		});
		
		ivLogout.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(AdvanceReports.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llTranscations 			= 	(LinearLayout)inflater.inflate(R.layout.advancereports, null);
		btnCollectionReport 	= 	(Button)llTranscations.findViewById(R.id.btnCollectionReport);
		btnSummaryReport		=	(Button)llTranscations.findViewById(R.id.btnSummaryReport);
	
		
		llBaseMiddle.addView(llTranscations, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		tvHeader.setText("ReportsList");
		showHomeIcons();
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

}
