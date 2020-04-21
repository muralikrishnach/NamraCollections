package com.jayam.impactapp;

import com.jayam.impactapp.common.AppConstants;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class AdvGroupsAndCenters extends Base
{
	private LinearLayout llFroupandCenters, llCenters, llGroups;
	@Override
	public void initialize() 
	{
		intializeControlles();

		
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
				Intent i = new Intent(AdvGroupsAndCenters.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
		
		llCenters.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AdvGroupsAndCenters.this, AdvCenters.class);
				intent.putExtra("type", "center");
				AppConstants.ADVANCECENTER="YES";
				startActivityForResult(intent, 0);
			}
		});
		

		llGroups.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AdvGroupsAndCenters.this, AdvCenters.class);
				intent.putExtra("type", "group");
				AppConstants.ADVANCECENTER="NO";
				startActivityForResult(intent, 0);
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llFroupandCenters = (LinearLayout) inflater.inflate(R.layout.groupsandcenters_regular, null);
		
		llCenters			=	(LinearLayout)llFroupandCenters.findViewById(R.id.llCenters);
		llGroups			=	(LinearLayout)llFroupandCenters.findViewById(R.id.llGroups);
		
		llBaseMiddle.addView(llFroupandCenters, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Advance Collections");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == AppConstants.RESULTCODE_LOGOUT)
		{
			setResult( AppConstants.RESULTCODE_LOGOUT);
			finish();
		}
		else if(resultCode == AppConstants.RESULTCODE_HOME)
		{
			setResult(resultCode);
			finish();
		}
			
	}

}
