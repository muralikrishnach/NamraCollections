package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.CollectionReportFor_Centers_OD_Adapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.objects.ODDemandsDO;


import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CollectionReports_Centers_OD extends Base
{
	private LinearLayout llCenters;
	private ListView lvCenters;
	private CollectionReportFor_Centers_OD_Adapter adapter;
	private Transaction_OD_BL transaction_OD_BL;
	private ArrayList<ODDemandsDO> alArrayList;
	
	@Override
	public void initialize()
	{
		intializeControlles();
		
		transaction_OD_BL = new Transaction_OD_BL();
		alArrayList = transaction_OD_BL.getAllDistinctCenters(CollectionReports_Centers_OD.this);
		
		adapter = new CollectionReportFor_Centers_OD_Adapter(CollectionReports_Centers_OD.this, alArrayList);
		lvCenters.setAdapter(adapter);
		
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
				Intent i = new Intent(CollectionReports_Centers_OD.this,loginActivity.class);
				startActivity(i);
			//	setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
		
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
		llCenters		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvCenters		=	(ListView)llCenters.findViewById(R.id.lvCenters);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llCenters, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
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


}
