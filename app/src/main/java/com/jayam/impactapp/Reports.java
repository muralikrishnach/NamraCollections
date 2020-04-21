package com.jayam.impactapp;

import com.jayam.impactapp.common.AppConstants;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Reports extends Baselogin
{
	private LinearLayout llTranscations;

	private ImageView btnRegularCollections, btnODCollections, btnADVCollections, btnLUCDemands,btnCashless;
	@Override
	public void initialize()
	{
		intializeControlles();
		
		btnRegularCollections.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new  Intent(Reports.this, ReportDates_Regular.class);
				startActivityForResult(intent,1234);
			}
		});
		
		btnODCollections.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new  Intent(Reports.this, ReportList_For_OD.class);
				startActivityForResult(intent,1234);
			}
		});
		
		btnADVCollections.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new  Intent(Reports.this, AdvanceReports.class);
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
				Intent i = new Intent(Reports.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
	}
	
	public void intializeControlles()
	{
		llTranscations 			= 	(LinearLayout)inflater.inflate(R.layout.transactions1, null);
		btnRegularCollections 	= 	(ImageView) llTranscations.findViewById(R.id.btnRegularCollections);
		btnODCollections		=	(ImageView)llTranscations.findViewById(R.id.btnODCollections);
		btnADVCollections       =	(ImageView)llTranscations.findViewById(R.id.btnADVCollections);

		llBaseMiddle.addView(llTranscations, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		tvHeader.setText("Reports");
		showHomeIcons();

		ivLogout.setVisibility(View.GONE);
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
