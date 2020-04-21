package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;


import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ResetParameters extends Base implements DataListner
{
	LinearLayout llResetParameters;
	EditText txtworkmode,txtterminalid,txtmac,txtheader,txtfooter,txtBTAddress,txtoffset,txttimeout,txturl,txtmaxtxns,txtmaxamt,txtusername,txttxnid,txtadvdemands;
	CheckBox chklateness,chkgli,chkindividual,chkattn,chkadv,chkpartial,chkmtime,chkacopy;
	Button btnSubmit;
	@Override
	public void initialize() 
	{
		initiliazeControls();
		onCheckButton();
		
		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				final ArrayList<IntialParametrsDO> arrayList = new IntialParametrsBL().SelectAll(ResetParameters.this);
				runOnUiThread(new Runnable()
				{
					@Override
					public void run() 
					{
						if( arrayList.get(0)!= null)
						{
							IntialParametrsDO objParametrsDO = arrayList.get(0);
							
							
							txtworkmode.setText(objParametrsDO.WorkMode);
							txtterminalid.setText(objParametrsDO.TerminalID);
							txtmac.setText(objParametrsDO.MACID);
							txtheader.setText(objParametrsDO.ReceiptHeader);
							txtfooter.setText(objParametrsDO.ReceiptFooter);
							txtBTAddress.setText(objParametrsDO.BTPrinterAddress);
							txtoffset .setText(objParametrsDO.DaysOffSet);
							txttimeout.setText(objParametrsDO.TimeOut);
							txturl.setText(objParametrsDO.ServerUrl);
							txtmaxtxns.setText(objParametrsDO.MaxTransactions);
							txtmaxamt.setText(objParametrsDO.MaxAmount);
							txtusername.setText(objParametrsDO.UserName);
							txttxnid .setText(objParametrsDO.LastTransactionID);
							txtadvdemands .setText(objParametrsDO.AdvDemandDwds);
							
							chklateness.setChecked(false);
							chkgli .setChecked(false);
							chkindividual.setChecked(false);
							chkattn.setChecked(false);
							chkadv .setChecked(false);
							chkpartial .setChecked(false);
							chkmtime.setChecked(false);
							chkacopy.setChecked(false);
							
							chklateness.setTag(false);
							chkgli .setTag(false);
							chkindividual.setTag(false);
							chkattn.setTag(false);
							chkadv .setTag(false);
							chkpartial .setTag(false);
							chkmtime.setTag(false);
							chkacopy.setTag(false);
							
							if(objParametrsDO.Lateness.equalsIgnoreCase("1"))
							{
								chklateness.setChecked(true);
								chklateness.setTag(true);
							}
							if(objParametrsDO.GLI.equalsIgnoreCase("1"))
							{
								chkgli.setChecked(true);
								chkgli .setTag(true);
							}
							if(objParametrsDO.IndividualReceipts.equalsIgnoreCase("1"))
							{
								chkindividual.setChecked(true);
								chkindividual.setTag(true);
							}
							if(objParametrsDO.MemberAttendance.equalsIgnoreCase("1"))
							{
								chkattn.setChecked(true);
								chkattn.setTag(true);
							}
							if(objParametrsDO.AdvPayment.equalsIgnoreCase("1"))
							{
								chkadv.setChecked(true);
								chkadv.setTag(true);
							}
							if(objParametrsDO.PartialPayment.equalsIgnoreCase("1"))
							{
								chkpartial.setChecked(true);
								chkpartial .setTag(true);
							}
							if(objParametrsDO.MeetingTime.equalsIgnoreCase("1"))
							{
								chkmtime.setChecked(true);
								chkmtime.setTag(true);
							}
							if(objParametrsDO.AgentCopy.equalsIgnoreCase("1"))
							{
								chkacopy.setChecked(true);
								chkacopy.setTag(true);
							}
							
						}
					}
				});
			
			}
		}).start();
		
		
		btnSubmit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				IntialParametrsDO objParametrsDO = new IntialParametrsDO();
				
				objParametrsDO.WorkMode = txtworkmode.getText().toString();
				objParametrsDO.TerminalID = txtterminalid.getText().toString();
				objParametrsDO.MACID = txtmac.getText().toString();
				objParametrsDO.ReceiptHeader = txtheader.getText().toString();
				objParametrsDO.ReceiptFooter = txtfooter.getText().toString();
				objParametrsDO.BTPrinterAddress = txtBTAddress.getText().toString();
				objParametrsDO.DaysOffSet = txtoffset .getText().toString();
				objParametrsDO.TimeOut = txttimeout.getText().toString();
				objParametrsDO.ServerUrl = txturl.getText().toString();
				objParametrsDO.MaxTransactions = txtmaxtxns.getText().toString();
				objParametrsDO.MaxAmount = txtmaxamt.getText().toString();
				objParametrsDO.UserName = txtusername.getText().toString();
				objParametrsDO.LastTransactionID = txttxnid .getText().toString();
				objParametrsDO.AdvDemandDwds = txtadvdemands .getText().toString();
				
				
				objParametrsDO.Lateness = "0";
				objParametrsDO.GLI = "0";
				objParametrsDO.IndividualReceipts = "0";
				objParametrsDO.MemberAttendance = "0";
				objParametrsDO.AdvPayment = "0";
				objParametrsDO.PartialPayment = "0";
				objParametrsDO.MeetingTime = "0";
				objParametrsDO.AgentCopy = "0";
				
				if(chklateness.getTag().toString().equalsIgnoreCase("tue"))
				{
					objParametrsDO.Lateness = "1";
				}
				if(chkgli.getTag().toString().equalsIgnoreCase("tue"))
				{
					objParametrsDO.GLI = "1";
				}
				if(chkindividual.getTag().toString().equalsIgnoreCase("tue"))
				{
					objParametrsDO.IndividualReceipts = "1";
				}
				if(chkattn.getTag().toString().equalsIgnoreCase("tue"))
				{
					objParametrsDO.MemberAttendance = "1";
				}
				if(chkadv.getTag().toString().equalsIgnoreCase("tue"))
				{
					objParametrsDO.AdvPayment = "1";
				}
				if(chkpartial.getTag().toString().equalsIgnoreCase("tue"))
				{
					objParametrsDO.PartialPayment = "1";
				}
				if(chkmtime.getTag().toString().equalsIgnoreCase("tue"))
				{
					objParametrsDO.MeetingTime = "1";
				}
				
				if(chkacopy.getTag().toString().equalsIgnoreCase("tue"))
				{
					objParametrsDO.AgentCopy = "1";
				}
			}
		});
		
		
		
		llBaseMiddle.addView(llResetParameters, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT ));
	}
	private void initiliazeControls() 
	{
		llResetParameters = (LinearLayout)getLayoutInflater().inflate(R.layout.resetparameters, null);
		
		txtworkmode 	= (EditText)llResetParameters.findViewById(R.id.txtworkmode);
		txtterminalid 	= (EditText)llResetParameters.findViewById(R.id.txtterminalid);
		txtmac 			= (EditText)llResetParameters.findViewById(R.id.txtmac);
		txtheader		= (EditText)llResetParameters.findViewById(R.id.txtheader);
		txtfooter 		= (EditText)llResetParameters.findViewById(R.id.txtfooter);
		txtBTAddress	= (EditText)llResetParameters.findViewById(R.id.txtBTAddress);
		txtoffset 		= (EditText)llResetParameters.findViewById(R.id.txtoffset);
		txttimeout 		= (EditText)llResetParameters.findViewById(R.id.txttimeout);
		txturl 			= (EditText)llResetParameters.findViewById(R.id.txturl);
		txtmaxtxns 		= (EditText)llResetParameters.findViewById(R.id.txtmaxtxns);
		txtmaxamt 		= (EditText)llResetParameters.findViewById(R.id.txtmaxamt);
		txtusername 	= (EditText)llResetParameters.findViewById(R.id.txtusername);
		txttxnid 		= (EditText)llResetParameters.findViewById(R.id.txttxnid);
		txtadvdemands 	= (EditText)llResetParameters.findViewById(R.id.txtadvdemands);
		
		chklateness 	= (CheckBox)llResetParameters.findViewById(R.id.chklateness);
		chkgli 			= (CheckBox)llResetParameters.findViewById(R.id.chkgli);
		chkindividual 	= (CheckBox)llResetParameters.findViewById(R.id.chkindividual);
		chkattn 		= (CheckBox)llResetParameters.findViewById(R.id.chkattn);
		chkadv 			= (CheckBox)llResetParameters.findViewById(R.id.chkadv);
		chkpartial 		= (CheckBox)llResetParameters.findViewById(R.id.chkpartial);
		chkmtime 		= (CheckBox)llResetParameters.findViewById(R.id.chkmtime);
		chkacopy		= (CheckBox)llResetParameters.findViewById(R.id.chkacopy);
		
		btnSubmit 		= (Button)llResetParameters.findViewById(R.id.btnSubmit);
		btnSubmit.setVisibility(View.GONE);
		tvHeader.setText("Reset parameters");
	}
	
	public void onCheckButton()
	{
		chklateness.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				chklateness.setTag(isChecked);
			}
		});
		
		chkgli.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				chkgli.setTag(isChecked);
			}
		});
		
		chkindividual.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				chkindividual.setTag(isChecked);
			}
		});

		chkattn .setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				chkattn.setTag(isChecked);
			}
		});

		chkadv .setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				chkadv.setTag(isChecked);
			}
		});

		chkpartial.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				chkpartial.setTag(isChecked);
			}
		});

		chkmtime .setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				chkmtime.setTag(isChecked);
			}
		});

		chkacopy.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				chkacopy.setTag(isChecked);
			}
		});
	}
	
	@Override
	public void onDataretrieved(Object objs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDataretrievalFailed(String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	

}
