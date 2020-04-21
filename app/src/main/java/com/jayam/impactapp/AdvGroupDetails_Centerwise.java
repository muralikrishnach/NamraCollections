package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdvGroupDetails_Centerwise extends Base
{
	
	private LinearLayout llCenterDetails;
	private TextView tvCenterName,tvDemandDate, tvTotalOS,tvTotalpreclosureamr, tvMembetCount, tvCollectedAmount, tvAmtTobeCollected;
	private TextView tvAdvCenterName_label;
	private String groupnumber;
	private Button etEdit, btnNext;
	private ArrayList<AdvaceDemandDO> alAdvanceDemandsBL;
	private AdvanceDemandBL andanceDemandsBL;
	@Override
	public void initialize()
	{
		groupnumber	=	getIntent().getExtras().getString("groupnumber");
		
		intializeControlles();

		
		andanceDemandsBL = new AdvanceDemandBL();
		alAdvanceDemandsBL = andanceDemandsBL.SelectAll(groupnumber, "Groups",AdvGroupDetails_Centerwise.this);
		
		tvCenterName.setText(""+alAdvanceDemandsBL.get(0).MGI_Name);
		tvDemandDate.setText(""+alAdvanceDemandsBL.get(0).DemandDate);
		tvMembetCount.setText(""+alAdvanceDemandsBL.size());
		
		float totalos = 0;
		float preclosure = 0;
		float collected = 0;
		float previous = 0;
		for (int i=0; i< alAdvanceDemandsBL.size();i++)
		{
			AdvaceDemandDO obj = alAdvanceDemandsBL.get(i);
			totalos  = totalos + Float.valueOf(obj.OS.trim()).floatValue();
			preclosure  = preclosure + Float.valueOf(obj.OSAmt.trim()).floatValue();
			String preAmt=obj.previousAmt;
			if(preAmt == null)
			{
				
				previous=0;
			}
			else
			{
				
				previous=Float.valueOf(obj.previousAmt.trim()).floatValue();
			}
			collected  = collected + previous;
			
		}
		
		tvTotalOS.setText(""+totalos);
		float totalpre=0;
		totalpre=preclosure-totalos;
		tvTotalpreclosureamr.setText(""+totalpre);
		tvCollectedAmount.setText(""+collected);
		float balance=0;
		balance=totalos-collected;
		tvAmtTobeCollected.setText(""+balance);
		
		
		
		
		
		etEdit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AdvGroupDetails_Centerwise.this, AdvGroupMembers.class);
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 0);
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
				Intent i = new Intent(AdvGroupDetails_Centerwise.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
		
	
		
		
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llCenterDetails			=	(LinearLayout)inflater.inflate(R.layout.advancecenterdetails, null);
		tvCenterName 			=	(TextView)llCenterDetails.findViewById(R.id.tvAdvCenterName);
		tvDemandDate 			=	(TextView)llCenterDetails.findViewById(R.id.tvAdvDemandDate);
		tvTotalOS 				=	(TextView)llCenterDetails.findViewById(R.id.tvAdvTotalOSAMT);		
		tvTotalpreclosureamr 	=	(TextView)llCenterDetails.findViewById(R.id.tvTotalpreclosureamt);
		tvMembetCount 			=	(TextView)llCenterDetails.findViewById(R.id.tvMembercount);
		tvCollectedAmount 		=	(TextView)llCenterDetails.findViewById(R.id.tvCollectedAmt);
		tvAmtTobeCollected 		=	(TextView)llCenterDetails.findViewById(R.id.tvAmountTobeCollected);
		etEdit 					=	(Button)llCenterDetails.findViewById(R.id.etEdit);
		btnNext 				=	(Button)llCenterDetails.findViewById(R.id.btnNext);
		tvAdvCenterName_label 	=	(TextView)llCenterDetails.findViewById(R.id.tvAdvCenterName_label);	 
		tvAdvCenterName_label.setText("Group Name");
		btnNext.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llCenterDetails, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Group Detail");
		
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
		else if(resultCode == AppConstants.RESULTCODE_CENTERDETAILS)
		{
			setResult(resultCode);
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}
	
}

