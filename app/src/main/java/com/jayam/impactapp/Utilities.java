package com.jayam.impactapp;

import com.jayam.impactapp.common.AppConstants;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class Utilities extends Base
{

	LinearLayout llUtilities;
	Button btnChangePassword,btnBackupDetails,btnShowDetails,btnAppVersion;
	@Override
	public void initialize() 
	{
		llUtilities = (LinearLayout)getLayoutInflater().inflate(R.layout.utilities, null);
		
		initializeControls();
		
		
		btnChangePassword.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Utilities.this,ChangePassword.class);
				startActivityForResult(intent,12345);
			}
		});
		
		btnBackupDetails.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showAlertDailog("Name of the record store is backupDB Number of records in this record store are 51 Size of record store is 4968 Available size for the record store 519397.");
			}
		});
		
		btnShowDetails.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showAlertDailog("Name of the store is demandsDB Number of recodes in this recod store are 51 Size of record store is 8568 Available size for the recod store 515807.");
			}
		});
		
		btnAppVersion.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showAlertDailog("APP Version 2.9.2");
			}
		});
		
		
		llBaseMiddle.addView(llUtilities,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Utilities");
		
		
		ivHome.setOnClickListener(new  OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		ivLogout.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Utilities.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
	}
	private void initializeControls() 
	{
		btnChangePassword = (Button)llUtilities.findViewById(R.id.btnChangePassword);
		btnBackupDetails = (Button)llUtilities.findViewById(R.id.btnBackupDetails);
		btnShowDetails = (Button)llUtilities.findViewById(R.id.btnShowDetails);
		btnAppVersion = (Button)llUtilities.findViewById(R.id.btnAppVersion);
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
