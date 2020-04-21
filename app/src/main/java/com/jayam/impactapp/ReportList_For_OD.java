package com.jayam.impactapp;

import com.jayam.impactapp.common.AppConstants;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportList_For_OD extends Base
{
	private LinearLayout llReportList;
	private TextView tvCollections, tvSummary;
	
	@Override
	public void initialize()
	{
		intializeControlles();
		
		tvCollections.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(ReportList_For_OD.this, CollectionReports_Centers_OD.class);
				startActivityForResult(intent,1234);
			}
		});
		
		tvSummary.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(ReportList_For_OD.this, SummaryReport_OD.class);
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
		
//		ivLogout.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Intent i = new Intent(ReportList_For_OD.this,loginActivity.class);
//				startActivity(i);
//				//setResult(AppConstants.RESULTCODE_LOGOUT);
//			//	finish();
//			}
//		});
	}
	
	public void intializeControlles()
	{
		llReportList		=	(LinearLayout)inflater.inflate(R.layout.reportslist, null);
		
		tvCollections		=	(TextView)llReportList.findViewById(R.id.tvCollections);
		tvSummary		=	(TextView)llReportList.findViewById(R.id.tvSummary);
		
		tvCollections.setText("Collection Reports");
		tvSummary.setText("Summary Report");
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llReportList, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
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
