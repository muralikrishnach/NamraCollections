package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportList extends Base
{
	private LinearLayout llReportList;
	private TextView tvCollections, tvSummary;
	private TrnsactionsBL trnsactionsBL;
	private ArrayList<RegularDemandsDO> arrayListTransactions;
	@Override
	public void initialize()
	{
		intializeControlles();
		
		tvCollections.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				trnsactionsBL	 = new TrnsactionsBL();
				arrayListTransactions	= trnsactionsBL.SelectAllCenters_CollectedAmt(ReportList.this);
				int k=arrayListTransactions.size();
				if(k==0){
					showAlertDailog("No Collections Records available");  
					return;
				}else{
				Intent intent = new Intent(ReportList.this, CollectionReport.class);
				startActivityForResult(intent,1234);
				}
			}
		});
		
		tvSummary.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(ReportList.this, SummaryReports_Regular.class);
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
				Intent i = new Intent(ReportList.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
	}
	
	public void intializeControlles()
	{
		llReportList		=	(LinearLayout)inflater.inflate(R.layout.reportslist, null);
		
		tvCollections		=	(TextView)llReportList.findViewById(R.id.tvCollections);
		tvSummary		=	(TextView)llReportList.findViewById(R.id.tvSummary);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llReportList, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
		tvHeader.setText("Report List");
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
