package com.jayam.impactapp;
import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.utils.StringUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AdvanceConfirmationScreen extends Base implements AdvanceUpdatereciptNumbers
{
	private LinearLayout llConfirmation;
	private TextView tvAdvConCenterName, tvAdvConCollAmt,tvCenterName_label;
	private String groupnumber;
	private Button btnAdvConfirmation;
	private ArrayList<AdvaceDemandDO> vecAdvanceDemands;
	String Name,Amount;
	int LastTaxn ;
	@Override
	public void initialize()
	{
		intialControlles();
		Bundle bundle 	= 	getIntent().getExtras();
		Name			=	bundle.getString("Name");
		Amount			=	bundle.getString("Amount");
		groupnumber		=	bundle.getString("GroupCode");
		
		
		tvAdvConCenterName.setText(""+Name);
		tvAdvConCollAmt.setText(""+Amount);
		
		AdvanceDemandBL bl = new AdvanceDemandBL();
		vecAdvanceDemands  = bl.SelectAllData(groupnumber, "Groups",AdvanceConfirmationScreen.this);
		
		btnAdvConfirmation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAlertDailog("Are you sure to submit the Group Detail and sync to server?", "Yes", "No", new CustomDailoglistner()
				{
					@Override
					public void onPossitiveButtonClick(DialogInterface dialog) 
					{
						dialog.dismiss();
						ShowLoader();
						UpdatereciptNumbers updatereciptNumbers = new  UpdatereciptNumbers(vecAdvanceDemands, AdvanceConfirmationScreen.this);
						updatereciptNumbers.start();
					}
					
					@Override
					public void onNegativeButtonClick(DialogInterface dialog) 
					{
						dialog.dismiss();
					}
				});
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
				Intent i = new Intent(AdvanceConfirmationScreen.this,loginActivity.class);
				startActivity(i);
			//	setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void intialControlles()
	{
		llConfirmation			=	(LinearLayout)inflater.inflate(R.layout.advanceconfirmation, null);
		tvAdvConCenterName		=	(TextView)llConfirmation.findViewById(R.id.tvAdvConCenterName);
		tvAdvConCollAmt			=	(TextView)llConfirmation.findViewById(R.id.tvAdvConCollAmt);
		tvCenterName_label		=	(TextView)llConfirmation.findViewById(R.id.tvCenterName_label);
		btnAdvConfirmation		=	(Button)llConfirmation.findViewById(R.id.btnAdvConfirmation);
		tvCenterName_label.setText("Group Name");		
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		llBaseMiddle.addView(llConfirmation, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		tvHeader.setText("Final Confirmation");
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == AppConstants.RESULTCODE_GROPDETAILS)
		{
			finish();
		}
		else if(resultCode == AppConstants.RESULTCODE_LOGOUT)
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
	class UpdatereciptNumbers extends Thread
	{
		private ArrayList<AdvaceDemandDO> alArrayList;
		private AdvanceUpdatereciptNumbers listner;
		public UpdatereciptNumbers(ArrayList<AdvaceDemandDO> alArrayList, AdvanceUpdatereciptNumbers listner) 
		{
			this.alArrayList = alArrayList;
			this.listner =listner;
		}
		@Override
		public void run()
		{
			super.run();
			
			for(int i=0; i< alArrayList.size(); i++)
			{
				AdvanceDemandBL advanceDemandBL = new AdvanceDemandBL();
				advanceDemandBL.updateReciptNumbers(alArrayList.get(i).MLAI_ID, StringUtils.getRecieptNumberForAdvance(alArrayList.get(i),AdvanceConfirmationScreen.this),AdvanceConfirmationScreen.this);
			}
			
			runOnUiThread(new Runnable() 
			{
				@Override
				public void run()
				{
					listner.onUpdateCompleted();
				}
			});
		}
	}

	@Override
	public void onUpdateCompleted() {
		new Thread(new  Runnable() 
		{
			public void run() 
			{
				AdvanceDemandBL advanceDemandBL = new  AdvanceDemandBL();
				
				vecAdvanceDemands  = advanceDemandBL.SelectAllCollectedData(groupnumber, "Groups",AdvanceConfirmationScreen.this);
				String LastTranScode="";

				if(vecAdvanceDemands!=null)
				{
					final IntialParametrsBL intialParametrsBL = new IntialParametrsBL();

					try
					{
						LastTranScode= StringUtils.getTransactionCodeForAdvance_G(vecAdvanceDemands.get(0),AdvanceConfirmationScreen.this);
						LastTaxn = Integer.parseInt(intialParametrsBL.SelectAll(AdvanceConfirmationScreen.this).get(0).LastTransactionCode);
					}catch (IndexOutOfBoundsException in)
					{
						in.printStackTrace();;
					}

					LastTaxn = LastTaxn+1;

					for(AdvaceDemandDO  obj : vecAdvanceDemands)
					{
						String[] id = StringUtils.getRecieptNumberForAdvance(obj,AdvanceConfirmationScreen.this).split("-");

						float AmntC = Float.valueOf(obj.CollectedAmt);
						if(obj.CollectedAmt != null &&  AmntC > 0)
						{
							Log.e("Inserted", "saved");
							intialParametrsBL.updateReceiptNumber(String.valueOf(Integer.parseInt(id[id.length-1])+1).toString(),AdvanceConfirmationScreen.this);
							advanceDemandBL.Insert(obj, StringUtils.getRecieptNumberForAdvance(obj,AdvanceConfirmationScreen.this),LastTranScode,AdvanceConfirmationScreen.this);
						}
						else if(obj.CollectedAmt == null)
						{
							Log.e("Inserted", "notsaved");
							intialParametrsBL.updateReceiptNumber(String.valueOf(Integer.parseInt(id[id.length-1])+1).toString(),AdvanceConfirmationScreen.this);
							advanceDemandBL.Insert(obj,StringUtils.getRecieptNumberForAdvance(obj,AdvanceConfirmationScreen.this),LastTranScode,AdvanceConfirmationScreen.this);
						}
						advanceDemandBL.updateCollectedAmt(obj.MLAI_ID, obj.CollectedAmt,AdvanceConfirmationScreen.this);
					}

					intialParametrsBL.updateLastTransctionCode(LastTaxn+"",AdvanceConfirmationScreen.this);
					String finalLastTranScode = LastTranScode;
					runOnUiThread(new Runnable() {
						public void run()
						{
							HideLoader();
							Intent intent = new Intent(AdvanceConfirmationScreen.this, AdvTxnack.class);
							intent.putExtra("LastTranScode", finalLastTranScode);
							intent.putExtra("Groups", groupnumber);
							startActivityForResult(intent,1234);
						}
					});

				}
				//updateTransactionTable(vecAdvanceDemands);

			}
		}).start();
		
	}

	@Override
	public void transactionAdded() {
		// TODO Auto-generated method stub
		
	}



}
interface AdvanceUpdatereciptNumbers
{
	public abstract void onUpdateCompleted();
	public void transactionAdded();
}
