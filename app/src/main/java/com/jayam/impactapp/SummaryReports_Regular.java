package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SummaryReports_Regular extends Base implements PrintListner
{
	private LinearLayout llReportListOD;
	private TextView tvSummary, tvDetaildSummary;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	private TrnsactionsBL trnsactionsBL;
	private ArrayList<RegularDemandsDO> arrayListTransactions;
	@Override
	public void initialize()
	{
		intializeControlles();
		IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(SummaryReports_Regular.this);
		tvSummary.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				trnsactionsBL	 = new TrnsactionsBL();
				arrayListTransactions	= trnsactionsBL.SelectAllCenters_CollectedAmt(SummaryReports_Regular.this);
				int i=arrayListTransactions.size();
				if(i==0){
					showAlertDailog("No Summary Records available");  
					return;
				}else{
				Intent intent = new Intent(SummaryReports_Regular.this, ReportSummaryForRegular.class);
				startActivityForResult(intent, 1234);
			
				}
				}
		});
		
		tvDetaildSummary.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				trnsactionsBL	 = new TrnsactionsBL();
				arrayListTransactions	= trnsactionsBL.SelectAllCenters_CollectedAmt(SummaryReports_Regular.this);
				int j=arrayListTransactions.size();
				if(j==0){
					showAlertDailog("No DetailedSummary Records available");  
					return;
				}else{
				Intent intent = new Intent(SummaryReports_Regular.this, ReportDetailSummaryForRegular.class);
				startActivityForResult(intent, 1234);
			}
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
		
//		ivLogout.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Intent i = new Intent(SummaryReports_Regular.this,loginActivity.class);
//				startActivity(i);
//				//setResult(AppConstants.RESULTCODE_LOGOUT);
//				//finish();
//			}
//		});
	}
	

	public void intializeControlles()
	{
		llReportListOD		=	(LinearLayout)inflater.inflate(R.layout.summaryreports_od, null);
		
		tvSummary		=	(TextView)llReportListOD.findViewById(R.id.tvSummary);
		tvDetaildSummary		=	(TextView)llReportListOD.findViewById(R.id.tvDetaildSummary);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llReportListOD, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Summary Reports");
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
	public PrintDetailsDO getprintObject() 
	{
		PrintValues printValues = new PrintValues();
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		return printValues.getDetailObj();
	}
	
	


}
