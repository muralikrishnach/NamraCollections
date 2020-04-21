package com.jayam.impactapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayam.impactapp.common.AppConstants;

public class AdvancePaymentconfiramtion extends Base
{
	private LinearLayout llPaymentConfirmation;
	private TextView tvMemebrName, tvMemebrCode, tvdemandDate , tvCollectedAmount;
	private Button btnCenters, btnGroups;
	String paymnet,MemberName,MemberCode,DemandDate,TXNID;
	@Override
	public void initialize()
	{
		intialControlles();
		Bundle bundle	= 	getIntent().getExtras();
		
		MemberName	=	bundle.getString("MemberName");
		MemberCode	=	bundle.getString("MemberCode");
		DemandDate	=	bundle.getString("DemandDate");
		paymnet		=	bundle.getString("paymnet");
		TXNID = getIntent().getExtras().getString("TXNID");

		Log.v("","AdvancePaymentconfiramtionpaymnet"+paymnet);
		tvMemebrName.setText(""+MemberName);
		tvMemebrCode.setText(""+MemberCode);
		tvdemandDate.setText(""+DemandDate);
		tvCollectedAmount.setText(""+paymnet);
		
		btnCenters.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				setResult(AppConstants.RESULTCODE_CENTERDETAILS);
				finish();
			}
		});
		
		btnGroups.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				setResult(AppConstants.RESULTCODE_GROPDETAILS);
				finish();
			}
		});
		
//		ivHome.setOnClickListener(new  OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				setResult(AppConstants.RESULTCODE_HOME);
//				finish();
//			}
//		});
//
//		ivLogout.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				//setResult(AppConstants.RESULTCODE_LOGOUT);
//				//finish();
//				Intent i = new Intent(AdvancePaymentconfiramtion.this,loginActivity.class);
//				startActivity(i);
//			}
//		});
	}
	
	@SuppressWarnings("deprecation")
	public void intialControlles()
	{
		llPaymentConfirmation	=	(LinearLayout)inflater.inflate(R.layout.advancepaymentconfirmation, null);
		tvMemebrName			=	(TextView)llPaymentConfirmation.findViewById(R.id.tvAdvMemebrName);
		tvMemebrCode			=	(TextView)llPaymentConfirmation.findViewById(R.id.tvAdvMemebrCode);
		tvdemandDate			=	(TextView)llPaymentConfirmation.findViewById(R.id.tvAdvdemandDate);
		tvCollectedAmount		=	(TextView)llPaymentConfirmation.findViewById(R.id.tvAdvCollectedAmount);
		btnCenters				=	(Button)llPaymentConfirmation.findViewById(R.id.btnCenters);
		btnGroups				=	(Button)llPaymentConfirmation.findViewById(R.id.btnGroups);
		
		String YESNO			=	AppConstants.ADVANCECENTER;
		Log.e("YESNO:----",YESNO);
		if(YESNO.equals("YES"))
		{
			
		}
		else
		{
			btnCenters.setVisibility(View.GONE);
		}
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		llBaseMiddle.addView(llPaymentConfirmation, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		tvHeader.setText("Member Collection");
		
	}


	@Override
	public void onBackPressed() {
		// Do Here what ever you want do on back press;
	}
}
