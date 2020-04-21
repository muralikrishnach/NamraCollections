package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.Reports_Regular_Adapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.RegularDemandsBL;


import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ReportDates_Regular extends Base
{
	private LinearLayout llDemandDates;
	private ListView lvDates;
	private RegularDemandsBL regularDemandsBL;
	private Reports_Regular_Adapter reports_Regular_Adapter;
	ArrayList<String> alArrayList;
	@Override
	public void initialize() 
	{
		intializeControlles();
		
		regularDemandsBL = new RegularDemandsBL();
		 
//		ShowLoader();
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
				 alArrayList = regularDemandsBL.SelectAllDemandDates(ReportDates_Regular.this);
//				LastReciept

//				runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						
//						HideLoader();
						reports_Regular_Adapter = new Reports_Regular_Adapter(ReportDates_Regular.this, alArrayList);
						lvDates.setAdapter(reports_Regular_Adapter);
//					}
//				});
//			}
//		}).start();
		
		
		
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
//				Intent i = new Intent(ReportDates_Regular.this,loginActivity.class);
//				startActivity(i);
//				//setResult(AppConstants.RESULTCODE_LOGOUT);
//				//finish();
//			}
//		});
		
		
		if(alArrayList != null && alArrayList.size() == 0)
		{
			showAlertDailog("No Collection Dates Available", "OK", null, new CustomDailoglistner() 
			{
				@Override
				public void onPossitiveButtonClick(DialogInterface dialog)
				{
					dialog.dismiss();
					finish();
				}
				
				@Override
				public void onNegativeButtonClick(DialogInterface dialog) 
				{
					
				}
			});
		}
	}
	
	public void intializeControlles()
	{
		llDemandDates	=	(LinearLayout)inflater.inflate(R.layout.demandates, null);
		lvDates			=	(ListView)llDemandDates.findViewById(R.id.lvDates);
		llBaseMiddle.addView(llDemandDates, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		tvHeader.setText(" Collection  Dates");
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
