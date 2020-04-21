package com.jayam.impactapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.jayam.impactapp.businesslayer.GetDataBL;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.utils.NetworkUtility;


import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ODDemands extends Base implements DataListner 
{
	ArrayList<ODDemandsDO> oddemands;
	private GetDataBL getDataBL, checkDemands;
	private IntialParametrsBL initialBL;
	private LinearLayout llNewdemand;
	private TextView tvToatalDemands, tvAmountTobeCollected;
	private String dateNow;
	private String uid;
	private ArrayList<IntialParametrsDO> alArrayList;
	private String terminalId;
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		
		intializeControlles();
		
		getDataBL = new GetDataBL(ODDemands.this, ODDemands.this);
		initialBL = new IntialParametrsBL();
		alArrayList = initialBL.SelectAll(ODDemands.this);
		terminalId = alArrayList.get(0).TerminalID;
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter=  new SimpleDateFormat("dd-MM-yyyy");
		 dateNow = formatter.format(currentDate.getTime());
		
		if(NetworkUtility.isNetworkConnectionAvailable(ODDemands.this))
		{
			ShowLoader();	
			getDataBL.getODDemands(terminalId, dateNow);
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
				Intent i = new Intent(ODDemands.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
	}
	
	public void intializeControlles()
	{
		llNewdemand					=	(LinearLayout)inflater.inflate(R.layout.newdemanddates, null);
		tvToatalDemands				=	(TextView)llNewdemand.findViewById(R.id.tvToatalDemands);
		tvAmountTobeCollected		=	(TextView)llNewdemand.findViewById(R.id.tvAmountTobeCollected);
		TextView  tvNewDemand		=	(TextView)llNewdemand.findViewById(R.id.tvNewDemand);
		
		tvNewDemand.setText("Total OD Demands");
		
		llBaseMiddle.addView(llNewdemand, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		tvHeader.setText("OD Demands");
	}
	
	@Override
	public void onDataretrieved(Object objs)
	{
		// TODO Auto-generated method stub
		oddemands =  (ArrayList<ODDemandsDO>) objs;
		int totalDemands = oddemands.size();
		Log.e("onDataretrieved", "Success"+totalDemands);
		float totalAmountTobeColleted = 0;
		
		for(int i=0 ; i< totalDemands; i++)
		{
			ODDemandsDO demandsDO = oddemands.get(i);
			totalAmountTobeColleted = totalAmountTobeColleted +(Float.valueOf(demandsDO.ODAmt.trim()).floatValue());
		}
		Log.e("onDataretrieved", "Success"+totalAmountTobeColleted);
		
		tvToatalDemands.setText(""+totalDemands);
		tvAmountTobeCollected.setText(""+totalAmountTobeColleted);
		
		checkDemands = new GetDataBL(ODDemands.this, new DataListner()
		{
			@Override
			public void onDataretrieved(Object objs) 
			{
				Log.e("onDataretrieved", "verify="+objs);

				if(!ODDemands.this.isFinishing())
				{
					showAlertDailog("OD Demand Downloaded Successfully", "OK", null, new CustomDailoglistner() {

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
			
			@Override
			public void onDataretrievalFailed(String errorMessage)
			{

				if(!ODDemands.this.isFinishing())
				{
					showAlertDailog("OD Demand Sync Failed,Pls try again later", "OK", null, new CustomDailoglistner() {

						@Override
						public void onPossitiveButtonClick(DialogInterface dialog)
						{
							dialog.dismiss();
							finish();
							ODDemandsBL demandsBL = new ODDemandsBL();
							demandsBL.Truncatetabel(ODDemands.this);
						}

						@Override
						public void onNegativeButtonClick(DialogInterface dialog)
						{
							// TODO Auto-generated method stub

						}
					});

				}
				Log.e("onDataretrievalFailed", "verify"+errorMessage);

			}
		});
		
		checkDemands.checkOverDues(terminalId,dateNow, ""+oddemands.size());
		HideLoader();
		Log.e("onDataretrieved", "Success");
	}

	@Override
	public void onDataretrievalFailed(String errorMessage)
	{
		// TODO Auto-generated method stub
		HideLoader();
		Log.e("onDataretrievalFailed", "failed");
		
		showAlertDailog("No OD Demands Available.", "OK", null, new CustomDailoglistner() {
			
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
