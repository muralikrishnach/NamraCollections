package com.jayam.impactapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.jayam.impactapp.businesslayer.GetDataBL;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.utils.NetworkUtility;


import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdvanceDemands extends Base implements DataListner
{
	private LinearLayout llAdvacedemand;
	private GetDataBL getDataBL, checkDemands;

	private TextView tvToatalDemands;
	private IntialParametrsBL initialBL;
	private ArrayList<IntialParametrsDO> alArrayList;
	ArrayList<AdvaceDemandDO> oddemands;
	private String terminalId;
	private String dateNow;
    String printerAddress=null;
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		
		intializeControlles();
		
		getDataBL = new GetDataBL(AdvanceDemands.this, AdvanceDemands.this);
		initialBL = new IntialParametrsBL();
		alArrayList = initialBL.SelectAll(AdvanceDemands.this);
		terminalId = alArrayList.get(0).TerminalID;
		printerAddress = alArrayList.get(0).BTPrinterAddress;
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter=  new SimpleDateFormat("dd-MM-yyyy");
		 dateNow = formatter.format(currentDate.getTime());
		
		if(NetworkUtility.isNetworkConnectionAvailable(AdvanceDemands.this))
		{
			ShowLoader();	
			getDataBL.getAdvanceDemands(terminalId, dateNow);
		}
		else
		{
			showAlertDailog(getResources().getString(R.string.nonetwork));
		}
		
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
				Intent i = new Intent(AdvanceDemands.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llAdvacedemand					=	(LinearLayout)inflater.inflate(R.layout.advancedemands, null);
		tvToatalDemands					=	(TextView)llAdvacedemand.findViewById(R.id.tvToatalRecords);
		
		llBaseMiddle.addView(llAdvacedemand, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		tvHeader.setText("Advance Downloaded ");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDataretrieved(Object objs) 
	{

		// TODO Auto-generated method stub
		oddemands =  (ArrayList<AdvaceDemandDO>) objs;
		int totalDemands = oddemands.size();
		Log.e("onDataretrieved", "Success"+totalDemands);
		tvToatalDemands.setText(""+totalDemands);
		
		checkDemands = new GetDataBL(AdvanceDemands.this, new DataListner()
		{
			@Override
			public void onDataretrieved(Object objs) 
			{
				Log.e("onDataretrieved", "verify="+objs);
				showAlertDailog("Advance Demand Downloaded Successfully", "OK", null, new CustomDailoglistner() {
					
					@Override
					public void onPossitiveButtonClick(DialogInterface dialog) 
					{
						dialog.dismiss();
						finish();
					}
					
					@Override
					public void onNegativeButtonClick(DialogInterface dialog) 
					{
						// TODO Auto-generated method stub
						
					}
				});
			}
			
			@Override
			public void onDataretrievalFailed(String errorMessage)
			{
				Log.e("onDataretrievalFailed", "verify"+errorMessage);
				showAlertDailog("Advance Demand Sync Failed,Pls try again later", "OK", null, new CustomDailoglistner() {
					
					@Override
					public void onPossitiveButtonClick(DialogInterface dialog) 
					{
						dialog.dismiss();
						finish();
						AdvanceDemandBL demandsBL = new AdvanceDemandBL();
						demandsBL.Truncatetabel(AdvanceDemands.this);
					}
					
					@Override
					public void onNegativeButtonClick(DialogInterface dialog) 
					{
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		
//		checkDemands.checkOverDues(terminalId,dateNow, ""+oddemands.size());
		checkDemands.verifyAdvanceDownloads(terminalId,dateNow, ""+oddemands.size(),printerAddress);
		HideLoader();
		Log.e("onDataretrieved", "Success");
	}

	@Override
	public void onDataretrievalFailed(String errorMessage)
	{
		HideLoader();
		Log.e("onDataretrievalFailed", "failed");
		showAlertDailog("No Advance Details For the User", "OK", null, new CustomDailoglistner() {
			
			@Override
			public void onPossitiveButtonClick(DialogInterface dialog) 
			{
				dialog.dismiss();
				finish();
			}
			
			@Override
			public void onNegativeButtonClick(DialogInterface dialog) 
			{
				// TODO Auto-generated method stub
				
			}
		});
	}

}
