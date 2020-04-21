package com.jayam.impactapp;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.utils.SharedPrefUtils;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Paymentconfiramtion extends Base
{
	private LinearLayout llPaymentConfirmation;
	private TextView tvMemebrName, tvMemebrCode, tvdemandDate , tvCollectedAmount;
	private Button btnViewgroupdetails, btnViewGroupMemebrs;
	String paymnet,MemberName,MemberCode,DemandDate;
	private String TxnType;
	@Override
	public void initialize()
	{

		Bundle bundle 		= 	getIntent().getExtras();
		paymnet				=	bundle.getString("paymnet");
		MemberName			=	bundle.getString("MemberName");
		MemberCode			=	bundle.getString("MemberCode");
		DemandDate			=	bundle.getString("DemandDate");
		TxnType				= 	bundle.getString("txntype");
		intialControlles();		
		tvMemebrName.setText(""+MemberName);
		tvMemebrCode.setText(""+MemberCode);
		tvdemandDate.setText(""+DemandDate);
		tvCollectedAmount.setText(""+paymnet);
		
		btnViewGroupMemebrs.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				setResult(AppConstants.RESULTCODE_GROPMEMBERS);
				finish();
			}
		});
		
		btnViewgroupdetails.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(TxnType.equals("Center")){
					setResult(AppConstants.RESULTCODE_CENTERDETAILS);
					finish();
					
			
				}
				else
				{
					
					setResult(AppConstants.RESULTCODE_GROPDETAILS);
					finish();
				}
				
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
				Intent i = new Intent(Paymentconfiramtion.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
	}
	
	public void intialControlles()
	{
		llPaymentConfirmation	=	(LinearLayout)inflater.inflate(R.layout.paymentconfirmation, null);
		tvMemebrName			=	(TextView)llPaymentConfirmation.findViewById(R.id.tvMemebrName);
		tvMemebrCode			=	(TextView)llPaymentConfirmation.findViewById(R.id.tvMemebrCode);
		tvdemandDate			=	(TextView)llPaymentConfirmation.findViewById(R.id.tvdemandDate);
		tvCollectedAmount		=	(TextView)llPaymentConfirmation.findViewById(R.id.tvCollectedAmount);
		
		btnViewgroupdetails		=	(Button)llPaymentConfirmation.findViewById(R.id.btnViewgroupdetails);
		btnViewGroupMemebrs		=	(Button)llPaymentConfirmation.findViewById(R.id.btnViewGroupMemebrs);
		
		if(TxnType.equals("Center"))
		{
			btnViewgroupdetails.setText("View Center Details");
			String URL=SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
			  if(URL.equals("Yes")){
				btnViewGroupMemebrs.setText("View SHG Members");
			  }
		}
		else
		{
			String URL=SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
			  if(URL.equals("Yes")){
				btnViewgroupdetails.setText("View SHG Details");
				btnViewGroupMemebrs.setText("View SHG Members");
			  }
		}
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		llBaseMiddle.addView(llPaymentConfirmation, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		tvHeader.setText("Payment Confirmation");
		
	}

	@Override
	public void onBackPressed() {
		// Do Here what ever you want do on back press;
	}

}
