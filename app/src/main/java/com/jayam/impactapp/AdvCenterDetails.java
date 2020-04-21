package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdvCenterDetails extends Base
{
	private LinearLayout llCenterDetails;
	private TextView tvCenterName,tvDemandDate, tvTotalOS,tvTotalpreclosureamr, tvMembetCount, tvCollectedAmount, tvAmtTobeCollected;
	private String centercode;
	private Button etEdit, btnNext;
	private ArrayList<AdvaceDemandDO> alAdvancerDemandsBL;
	
	
	@Override
	public void initialize() 
	{
		centercode	= getIntent().getExtras().getString("CenterCode");
		
		AdvanceDemandBL advanceDemandBL = new AdvanceDemandBL();
		alAdvancerDemandsBL = advanceDemandBL.SelectAll(centercode, "CenterCode",AdvCenterDetails.this);
		
		intializeControlles();
		
		tvDemandDate.setText(""+alAdvancerDemandsBL.get(0).DemandDate);
		tvCenterName.setText(""+alAdvancerDemandsBL.get(0).CenterName);
		tvMembetCount.setText(""+alAdvancerDemandsBL.size());
		
		Log.e("centercode", ""+centercode);
		Log.e("alregularDemandsBL", ""+alAdvancerDemandsBL.size());
		float totalos = 0;
		float preclosure = 0;
		float collected = 0;
		float previous = 0;
		for (int i=0; i< alAdvancerDemandsBL.size();i++)
		{
			AdvaceDemandDO obj = alAdvancerDemandsBL.get(i);

			if((obj.OS!=null))
			{
				totalos  = totalos + Float.valueOf(obj.OS.trim()).floatValue();
			}

			if(obj.OSAmt!=null)
			{
				preclosure  = preclosure + Float.valueOf(obj.OSAmt.trim()).floatValue();
			}

			String preAmt=obj.previousAmt;
			if(preAmt == null)
			{
				previous=0;
			}
			else
			{
				if(obj.previousAmt!=null)
				{
					previous=Float.valueOf(obj.previousAmt.trim()).floatValue();
				}

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
				Intent i = new Intent(AdvCenterDetails.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
		
		etEdit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AdvCenterDetails.this, AdvGroups_centerwise.class);
				intent.putExtra("centernuber", centercode);
				startActivityForResult(intent, 1234);
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				AdvanceDemandBL advanceDemandBL = new AdvanceDemandBL();
				String CollAmt = advanceDemandBL.getTOTALCollectedAmount(centercode,"Center",AdvCenterDetails.this);

				if(CollAmt!=null&&!CollAmt.isEmpty())
				{
					if(CollAmt.equals("0.0") || CollAmt.equals("0"))
					{
						Log.e("Equal To Zero",CollAmt);
						showAlertDailog("Total Collection Amount should be greater than Zero");
					}
					else
					{
						Log.e("Not Equal To Zero",CollAmt);
						String CenterName=alAdvancerDemandsBL.get(0).CenterName;
						String CenterCode=alAdvancerDemandsBL.get(0).MCI_Code;
						Intent intent = new Intent(AdvCenterDetails.this, AdvanceConfirmationScreen_Centerwise.class);
						intent.putExtra("Name", CenterName);
						intent.putExtra("Amount", CollAmt);
						intent.putExtra("CenterCode", CenterCode);
						startActivityForResult(intent, 1234);
					}
				}

			}
		});		
	}
	

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
		
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llCenterDetails, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Center Detail");
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




