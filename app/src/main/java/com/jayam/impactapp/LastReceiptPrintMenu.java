package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.LastRecieptBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.LastReceiptDO;
import com.jayam.impactapp.objects.NPSDemandDO;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.PrintUtils;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.StringUtils;


import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class LastReceiptPrintMenu extends Base  implements PrintListner
{
	private LinearLayout llLastReceiptPrintMenu;
	private Button btnPrint,btnMainmenu;
	LastRecieptBL lastrecieptBL;
	LastReceiptDO LastReceiptDO;
	String LastTXN="0";
	String Print="0";
	String Type="0";
    private ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    AdvanceDemandBL advancedemandBL;
    private ArrayList<AdvaceDemandDO> alArrayList;
    private TrnsactionsBL trnsactionsBL;
    private IntialParametrsBL intialParametrsBL;
	private boolean isPrintTaken = false;
    String TXNTYPE;
    LastRecieptBL recptBL;
    String URL="No";
	private String GroupCode;
	float collectedAmt = 0;
	float RenewalAmt = 0;
	private String CenterCode;
   
	@Override
	public void initialize() 
	{
		intializeControlles();
		URL=SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
		recptBL = new LastRecieptBL();
		intialParametrsBL = new  IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(LastReceiptPrintMenu.this);
		advancedemandBL = new AdvanceDemandBL();
		lastrecieptBL=new LastRecieptBL();
		LastReceiptDO=lastrecieptBL.SelectAll(LastReceiptPrintMenu.this).get(0);
		
//		try{
		
		LastTXN=LastReceiptDO.LastTxnId;
		Print=LastReceiptDO.Print;
		Type=LastReceiptDO.Type;
		
		String TXN = LastTXN;
		String[] TXN1 = TXN.split("-");
		TXNTYPE = TXN1[4].trim();
		
		Log.d("mfimo", "txn id:"+LastTXN+" Print flag:"+Print+" type:"+Type+"txn type:"+TXNTYPE);

//		String aTime1 = TXN1[0]+"_"+time2[0]+"_"+time2[1];
		
//		LastTXN.startsWith("G");

		trnsactionsBL = new TrnsactionsBL();
		
			llBaseMiddle.addView(llLastReceiptPrintMenu, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
				
			btnPrint.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					if(Print.equals("N"))
					{
						if(!isPrintTaken)
						{
							BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
							if (bluetooth.isEnabled()) 
							{
								ShowLoader();
								new Thread(new  Runnable() 
								{
									public void run() 
									{
										runOnUiThread(new Runnable() 
										{
											public void run() 
											{
												HideLoader();
												PrintUtils printUtils = new PrintUtils(LastReceiptPrintMenu.this, LastReceiptPrintMenu.this);
												printUtils.print();
											}
										});
									}
								}).start();
							}
							else
							{
								showAlertDailog("Please Switch On Mobile Bluetooth");
								return;
							}
						}
							else
							{

								showAlertDailog("Receipt Printed Y / N ", "Yes", "No", new CustomDailoglistner() 
								{
									@Override
									public void onPossitiveButtonClick(DialogInterface dialog)
									{
										recptBL.updateLastReceiptPrintFlag(LastTXN, "Y",LastReceiptPrintMenu.this) ;
										dialog.dismiss();
										setResult(AppConstants.RESULTCODE_HOME);
										finish();
									}
									
									@Override
									public void onNegativeButtonClick(DialogInterface dialog)
									{
										dialog.dismiss();
										
										showAlertDailog("WARNING ! Do you want to overwrite printed confirmation, pls. confirm Y / N.", "Yes", "No", new CustomDailoglistner() 
										{
											@Override
											public void onPossitiveButtonClick(DialogInterface dialog)
											{
												recptBL.updateLastReceiptPrintFlag(LastTXN, "N",LastReceiptPrintMenu.this) ;
												dialog.dismiss();
												setResult(AppConstants.RESULTCODE_HOME);
												finish();
											}
											
											@Override
											public void onNegativeButtonClick(DialogInterface dialog)
											{
												recptBL.updateLastReceiptPrintFlag(LastTXN, "Y",LastReceiptPrintMenu.this) ;
												dialog.dismiss();
												setResult(AppConstants.RESULTCODE_HOME);
												finish();
											}
										});
									}
								});
							
							}
						}
							else
							{
								if(Type.equals("R"))
								{
									showAlertDailog("Transaction not available for print");
							        return;
								}
								else if(Type.equals("A"))
								{
									showAlertDailog("Advance Transaction not available for print");
							        return;
								}else if(Type.equals("O"))
								{
									showAlertDailog("OD Transaction not available for print");
							        return;
								}else if(Type.equals("N"))
								{
									showAlertDailog("NPS Transaction not available for print");
							        return;
								}
							}
						}

				
			});
			
			btnMainmenu.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					setResult(AppConstants.RESULTCODE_HOME);
					finish();
				}
			});
			
	}
	
	public void intializeControlles()
	{
//		tvHeader.setText(getResources().getString(R.string.LastReceiptPrintMenu));
		llLastReceiptPrintMenu 		= (LinearLayout)inflater.inflate(R.layout.lastreceiptprint, null);
		
		
		btnPrint	= (Button)llLastReceiptPrintMenu.findViewById(R.id.btnPrint);
		btnMainmenu	= (Button)llLastReceiptPrintMenu.findViewById(R.id.btnMainmenu);
		
		ivLogout.setVisibility(View.GONE);
		ivHome.setVisibility(View.GONE);
		tvHeader.setText(getResources().getString(R.string.lrp));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.e("onActivityResult", "onActivityResult"+resultCode);
		if(resultCode == AppConstants.RESULTCODE_LOGOUT)
		{
			finish();
		}
	}

	@Override
	public PrintDetailsDO getprintObject() {
		PrintValues printValues = new PrintValues();
	if(Type.equals("A"))
	{
		Log.d("mfimo", "advance demand collections");
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		if(intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1"))
		{
			AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
			ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllTransactions(LastTXN,LastReceiptPrintMenu.this);
			for(int i=0 ; i <alArrayList.size(); i++ )
			{
				AdvaceDemandDO obj = alArrayList.get(i);
				AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
				String header=intialParametrsDO.ReceiptHeader;
	            String[] head = header.split("@");
	            String head1=head[0];
	            String head2=head[1];
	            String head3=head[2];
				printValues.add(head1,"true");
				String reciptNum=StringUtils.getRecieptNumberForAdvance(obj,LastReceiptPrintMenu.this);
	            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head2,"true");
	            }
	             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head3,"true");
	            }
				printValues.add(" ","false");
				if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
				{
				printValues.add("Customer Copy","true");	
				}
				printValues.add("Advance TXN Acknowledgment","true");
				printValues.add("--------------------------","true");
				printValues.add(" ","false");
				printValues.add("R.No:"+reciptNum,"false");
				printValues.add("Date:"+obj.DemandDate,"false");
				printValues.add(" ","false");
				if(URL.equals("Yes"))
				{
					printValues.add("Center Name:"+advanceDemandsDO.CenterName,"false");
					printValues.add("SHG Name:"+advanceDemandsDO.MGI_Name,"false");
					printValues.add("Member Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
					printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
					printValues.add("Savings Coll:"+obj.previousAmt,"false");	
				}
				else
				{
					printValues.add("Center:"+advanceDemandsDO.CenterName,"false");
					printValues.add("Group:"+advanceDemandsDO.MGI_Name,"false");
					printValues.add("Member:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
					printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
					printValues.add("Os Amt:"+advanceDemandsDO.OSAmt,"false");
					printValues.add("Advance collection:"+obj.previousAmt,"false");
				}
				
				printValues.add(" ","false");
				printValues.add(intialParametrsDO.ReceiptFooter,"true");
				
			}
			if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
			{
				// print agent copy
				for(int i=0 ; i <alArrayList.size(); i++ )
				{
					AdvaceDemandDO obj = alArrayList.get(i);
					
					AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
					String header=intialParametrsDO.ReceiptHeader;
		            String[] head = header.split("@");
		            String head1=head[0];
		            String head2=head[1];
		            String head3=head[2];
		            String reciptNum=StringUtils.getRecieptNumberForAdvance(obj,LastReceiptPrintMenu.this);
					printValues.add(head1,"true");
					

		            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
		            { }
		            else
		            {
		            	printValues.add(head2,"true");
		            }
		             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
		            { }
		            else
		            {
		            	printValues.add(head3,"true");
		            }
					printValues.add(" ","false");
					printValues.add("Agent Copy","true");	
					printValues.add("Advance TXN Acknowledgment","true");
					printValues.add("--------------------------","true");
					printValues.add(" ","false");
					printValues.add("R.No:"+reciptNum,"false");
					printValues.add("Date:"+obj.DemandDate,"false");
					printValues.add(" ","false");
					if(URL.equals("Yes"))
					{
						printValues.add("Center Name:"+advanceDemandsDO.CenterName,"false");
						printValues.add("SHG Name:"+advanceDemandsDO.MGI_Name,"false");
						printValues.add("Member Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
						printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
						printValues.add("Savings Coll:"+obj.previousAmt,"false");
					}
					else
					{
						printValues.add("Center:"+advanceDemandsDO.CenterName,"false");
						printValues.add("Group:"+advanceDemandsDO.MGI_Name,"false");
						printValues.add("Member:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
						printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
						printValues.add("Os Amt:"+advanceDemandsDO.OSAmt,"false");
						printValues.add("Advance collection:"+obj.previousAmt,"false");
					}
					
					printValues.add(" ","false");
					printValues.add(intialParametrsDO.ReceiptFooter,"true");
					
				}
			}
		}
		else
		{
			if(TXNTYPE.startsWith("G"))
			{
				float totDemand=0,collectedAmt=0;
				AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
				ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllTransactions(LastTXN,LastReceiptPrintMenu.this);
				String header=intialParametrsDO.ReceiptHeader;
	            String[] head = header.split("@");
	            String head1=head[0];
	            String head2=head[1];
	            String head3=head[2];
				printValues.add(head1,"true");
				String reciptNum=StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),LastReceiptPrintMenu.this);
	
	            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head2,"true");
	            }
	             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head3,"true");
	            }
				printValues.add(" ","false");
				if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
				{
				printValues.add("Customer Copy","true");	
				}
				printValues.add("Advance TXN Acknowledgment","true");
				printValues.add("--------------------------","true");
				printValues.add(" ","false");
				printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
				if(URL.equals("Yes"))
				{
				printValues.add("R.No:"+reciptNum,"false");
				printValues.add("Center Name:"+alArrayList.get(0).CenterName,"false");
				printValues.add("SHG Name:"+alArrayList.get(0).MGI_Name,"false");
				
				for(int i=0 ; i <alArrayList.size(); i++ )
				{
					AdvaceDemandDO obj = alArrayList.get(i);
					AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
					printValues.add(" ","false");
					printValues.add("Member Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
					printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
					printValues.add("Savings Coll:"+obj.previousAmt,"false");
					totDemand=totDemand+Float.valueOf(advanceDemandsDO.OSAmt);
					collectedAmt=collectedAmt+Float.valueOf(obj.previousAmt);
				}
				printValues.add(" ","false");
				printValues.add("Total SHG Demand :"+totDemand,"false");
				printValues.add("Total SHG Coll. Amt:"+collectedAmt,"false");
				}
				else
				{
					printValues.add("Group R.No:"+LastTXN,"false");
				printValues.add("Center:"+alArrayList.get(0).CenterName,"false");
				printValues.add("Group:"+alArrayList.get(0).MGI_Name,"false");
				
				for(int i=0 ; i <alArrayList.size(); i++ )
				{
					AdvaceDemandDO obj = alArrayList.get(i);
					AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
					printValues.add(" ","false");
					printValues.add("Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
					printValues.add("collection Amt:"+obj.previousAmt,"false");
					totDemand=totDemand+Float.valueOf(advanceDemandsDO.OSAmt);
					collectedAmt=collectedAmt+Float.valueOf(obj.previousAmt);
				}
				printValues.add(" ","false");
				printValues.add("Total Group Demand :"+totDemand,"false");
				printValues.add("Total Collected amount :"+collectedAmt,"false");
				}
				
				printValues.add(" ","false");
				printValues.add(intialParametrsDO.ReceiptFooter,"true");
				printValues.add(" ","false");
				printValues.add(" ","false");
				
				if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
				{
					// agent Copy
					totDemand=0;
					collectedAmt=0;
					printValues.add(head1,"true");
					if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
		            { }
		            else
		            {
		            	printValues.add(head2,"true");
		            }
		             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
		            { }
		            else
		            {
		            	printValues.add(head3,"true");
		            }
		            String reciptNum2=StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),LastReceiptPrintMenu.this);
					printValues.add(" ","false");
					printValues.add("Agent Copy","true");
					printValues.add(" ","false");
					printValues.add("Advance TXN Acknowledgment","true");
					printValues.add("--------------------------","true");
					printValues.add(" ","false");
					printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
					if(URL.equals("Yes"))
					{
						printValues.add("R.No:"+reciptNum2,"false");
						printValues.add("Center Name:"+alArrayList.get(0).CenterName,"false");
						printValues.add("SHG Name:"+alArrayList.get(0).MGI_Name,"false");
						
						for(int i=0 ; i <alArrayList.size(); i++ )
						{
							AdvaceDemandDO obj = alArrayList.get(i);
							AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
							printValues.add(" ","false");
							printValues.add("Member Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
							printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
							printValues.add("Savings Coll:"+obj.previousAmt,"false");
							totDemand=totDemand+Float.valueOf(advanceDemandsDO.OSAmt);
							collectedAmt=collectedAmt+Float.valueOf(obj.previousAmt);
						}
						
						printValues.add(" ","false");
						printValues.add("Total SHG Demand :"+totDemand,"false");
						printValues.add("Total SHG Coll. Amt:"+collectedAmt,"false");
					}
					else
					{
						printValues.add("Group R.No:"+LastTXN,"false");
						printValues.add("Center:"+alArrayList.get(0).CenterName,"false");
						printValues.add("Group:"+alArrayList.get(0).MGI_Name,"false");
						
						for(int i=0 ; i <alArrayList.size(); i++ )
						{
							AdvaceDemandDO obj = alArrayList.get(i);
							AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
							printValues.add(" ","false");
							printValues.add("Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
							printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
							printValues.add("collection Amt:"+obj.previousAmt,"false");
							totDemand=totDemand+Float.valueOf(advanceDemandsDO.OSAmt);
							collectedAmt=collectedAmt+Float.valueOf(obj.previousAmt);
						}
						
						printValues.add(" ","false");
						printValues.add("Total Group Demand :"+totDemand,"false");
						printValues.add("Total Collected amount :"+collectedAmt,"false");
					}
					
					printValues.add(" ","false");
					printValues.add(intialParametrsDO.ReceiptFooter,"true");
					printValues.add(" ","false");
					printValues.add(" ","false");
					printValues.add(" ","false");
					
				}
			}
			else
			{
				float totDemand=0,collectedAmt=0;
				AdvanceDemandBL trnsactionsBL1 = new AdvanceDemandBL();
				String Groups=trnsactionsBL1.SelectCenterCode(LastTXN,LastReceiptPrintMenu.this);
				ArrayList<AdvaceDemandDO> alDos = trnsactionsBL1.SelectDistinctGroupsFromCenter(Groups,LastReceiptPrintMenu.this);
				String header=intialParametrsDO.ReceiptHeader;
	            String[] head = header.split("@");
	            String head1=head[0];
	            String head2=head[1];
	            String head3=head[2];
//	            String reciptNum=StringUtils.getRecieptNumberForAdvance(alDos.get(0));
				printValues.add(head1,"true");
				

	            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head2,"true");
	            }
	             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head3,"true");
	            }
				printValues.add(" ","false");
				if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
				{
				printValues.add("Customer Copy","true");	
				}
				printValues.add("Advance TXN Acknowledgment","true");
				printValues.add("--------------------------","true");
				printValues.add(" ","false");
				if(URL.equals("Yes"))
				{
					for(int i =0 ; i < alDos.size(); i++ )
					{
						AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
						ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(LastTXN,alDos.get(i).MGI_Code,LastReceiptPrintMenu.this);
						if(alArrayList.size()>0)
						{
							String reciptNum=StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),LastReceiptPrintMenu.this);
							float GroupCollectedAmt= 0, GroupDemandAmt = 0;
							if(i==0)
							{
								printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
								printValues.add("R.No:"+reciptNum,"false");
							}
							for(int k=0 ; k <alArrayList.size(); k++ )
							{
								AdvaceDemandDO obj = alArrayList.get(k);
								AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
								if(k==0){
								printValues.add("Center Name:"+advanceDemandsDO.CenterName,"false");
								printValues.add("SHG Name:"+advanceDemandsDO.MGI_Name,"false");
								}
								printValues.add(" ","false");
								printValues.add("Member Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
								printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
								printValues.add("collection Amt:"+obj.previousAmt,"false");
								GroupDemandAmt=GroupDemandAmt+Float.valueOf(advanceDemandsDO.OSAmt);
								GroupCollectedAmt=GroupCollectedAmt+Float.valueOf(obj.previousAmt);
								totDemand=totDemand+Float.valueOf(advanceDemandsDO.OSAmt);
								collectedAmt=collectedAmt+Float.valueOf(obj.previousAmt);
								
							}
							printValues.add(" ","false");
							printValues.add("SHG Demand Amt:"+GroupDemandAmt,"false");
							printValues.add("SHG collection Amt:"+GroupCollectedAmt,"false");
							printValues.add(" ","false");
						}
					}
				}
				else
				{
					for(int i =0 ; i < alDos.size(); i++ )
					{
						AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
						ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(LastTXN,alDos.get(i).MGI_Code,LastReceiptPrintMenu.this);
						if(alArrayList.size()>0)
						{
							String reciptNum1=StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),LastReceiptPrintMenu.this);
							float GroupCollectedAmt= 0, GroupDemandAmt = 0;
							if(i==0)
							{
								printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
								printValues.add("Center R.No:"+reciptNum1,"false");
							}
							for(int k=0 ; k <alArrayList.size(); k++ )
							{
								AdvaceDemandDO obj = alArrayList.get(k);
								AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
								if(k==0){
								printValues.add("Center:"+advanceDemandsDO.CenterName,"false");
								printValues.add("Group:"+advanceDemandsDO.MGI_Name,"false");
								}
								printValues.add(" ","false");
								printValues.add("Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
								printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
								printValues.add("collection Amt:"+obj.previousAmt,"false");
								GroupDemandAmt=GroupDemandAmt+Float.valueOf(advanceDemandsDO.OSAmt);
								GroupCollectedAmt=GroupCollectedAmt+Float.valueOf(obj.previousAmt);
								totDemand=totDemand+Float.valueOf(advanceDemandsDO.OSAmt);
								collectedAmt=collectedAmt+Float.valueOf(obj.previousAmt);
								
							}
							printValues.add(" ","false");
							printValues.add("Group Demand Amt:"+GroupDemandAmt,"false");
							printValues.add("Group collection Amt:"+GroupCollectedAmt,"false");
							printValues.add(" ","false");
						}
					}
				}
				
				printValues.add(" ","false");
				printValues.add("Total Center Demand:"+totDemand,"false");
				printValues.add("Total Center Coll. Amt:"+collectedAmt,"false");
				printValues.add(" ","false");
				printValues.add(intialParametrsDO.ReceiptFooter,"true");
				printValues.add(" ","false");
				printValues.add(" ","false");
				if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
				{
	            
				printValues.add(head1,"true");
				

	            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head2,"true");
	            }
	             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head3,"true");
	            }
				printValues.add(" ","false");
				
				printValues.add("Agent Copy","true");	
				printValues.add("Advance TXN Acknowledgment","true");
				printValues.add("--------------------------","true");
				printValues.add(" ","false");
				if(URL.equals("Yes"))
				{
					for(int i =0 ; i < alDos.size(); i++ )
					{
						AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
						ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(LastTXN,alDos.get(i).MGI_Code,LastReceiptPrintMenu.this);
						if(alArrayList.size()>0)
						{
							String reciptNum1=StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),LastReceiptPrintMenu.this);
							float GroupCollectedAmt= 0, GroupDemandAmt = 0;
							if(i==0)
							{
								printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
								printValues.add("R.No:"+reciptNum1,"false");
							}
							for(int k=0 ; k <alArrayList.size(); k++ )
							{
								AdvaceDemandDO obj = alArrayList.get(k);
								AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
								if(k==0){
								printValues.add("Center Name:"+advanceDemandsDO.CenterName,"false");
								printValues.add("SHG Name:"+advanceDemandsDO.MGI_Name,"false");
								}
								printValues.add(" ","false");
								printValues.add("Member Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
								printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
								printValues.add("collection Amt:"+obj.previousAmt,"false");
								GroupDemandAmt=GroupDemandAmt+Float.valueOf(advanceDemandsDO.OSAmt);
								GroupCollectedAmt=GroupCollectedAmt+Float.valueOf(obj.previousAmt);
								totDemand=totDemand+Float.valueOf(advanceDemandsDO.OSAmt);
								collectedAmt=collectedAmt+Float.valueOf(obj.previousAmt);
							}
							printValues.add(" ","false");
							printValues.add("SHG Demand Amt:"+GroupDemandAmt,"false");
							printValues.add("SHG collection Amt:"+GroupCollectedAmt,"false");
							printValues.add(" ","false");
						}
					}
				}
				else
				{
					for(int i =0 ; i < alDos.size(); i++ )
					{
						AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
						ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(LastTXN,alDos.get(i).MGI_Code,LastReceiptPrintMenu.this);
						if(alArrayList.size()>0)
						{
							String reciptNum1=StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),LastReceiptPrintMenu.this);
							float GroupCollectedAmt= 0, GroupDemandAmt = 0;
							if(i==0)
							{
								printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
								printValues.add("Center R.No:"+reciptNum1,"false");
							}
							for(int k=0 ; k <alArrayList.size(); k++ )
							{
								AdvaceDemandDO obj = alArrayList.get(k);
								AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
								if(k==0){
								printValues.add("Center:"+advanceDemandsDO.CenterName,"false");
								printValues.add("Group:"+advanceDemandsDO.MGI_Name,"false");
								}
								printValues.add(" ","false");
								printValues.add("Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
								printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
								printValues.add("collection Amt:"+obj.previousAmt,"false");
								GroupDemandAmt=GroupDemandAmt+Float.valueOf(advanceDemandsDO.OSAmt);
								GroupCollectedAmt=GroupCollectedAmt+Float.valueOf(obj.previousAmt);
								totDemand=totDemand+Float.valueOf(advanceDemandsDO.OSAmt);
								collectedAmt=collectedAmt+Float.valueOf(obj.previousAmt);
							}
							printValues.add(" ","false");
							printValues.add("Group Demand Amt:"+GroupDemandAmt,"false");
							printValues.add("Group collection Amt:"+GroupCollectedAmt,"false");
							printValues.add(" ","false");
						}
					}
				}
				
				printValues.add(" ","false");
				printValues.add("Total Center Demand:"+totDemand,"false");
				printValues.add("Total Center Coll. Amt:"+collectedAmt,"false");
				printValues.add(" ","false");
				printValues.add(intialParametrsDO.ReceiptFooter,"true");
				printValues.add(" ","false");
				printValues.add(" ","false");}
			
			}
		}
		

	}
	else if(Type.equals("R"))
	{
		Log.d("mfimo", "regular demand collections");
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		if(intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1"))
		{
			TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
			ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL.SelectAll(LastTXN,"","Group",LastReceiptPrintMenu.this);
			
			for(int i=0 ; i <alArrayList.size(); i++ )
			{
				RegularDemandsDO obj = alArrayList.get(i);
				RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
				RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
				String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,LastReceiptPrintMenu.this);
				String header=intialParametrsDO.ReceiptHeader;
	            String[] head = header.split("@");
	            String head1=head[0];
	            String head2=head[1];
	            String head3=head[2];
	            
				printValues.add(head1,"true");
				String reciptNum=StringUtils.getRecieptNumber(obj,LastReceiptPrintMenu.this);

	            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head2,"true");
	            }
	             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head3,"true");
	            }
				printValues.add(" ","false");
				if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
				{
				printValues.add("Customer Copy","true");	
				}
				printValues.add("Transaction Acknowledgement","true");
				printValues.add("---------------------------","true");
				printValues.add(" ","false");
				printValues.add("R.No:"+reciptNum,"false");
				printValues.add("Date:"+obj.DemandDate,"false");
				printValues.add(" ","false");
				if(URL.equals("Yes"))
				{
					printValues.add("Center Name:"+obj.CName,"false");
					printValues.add("SHG Name:"+obj.GroupName,"false");
					printValues.add("Member Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
					printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
					if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
					{
						printValues.add("Meeting No:"+regularDemandsDO.InstallNo,"false");
//						printValues.add("Next Meeting Date:"+regularDemandsDO.NextRepayDate,"false");
					}
					float DmdTot=Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount);
					printValues.add("Planned Savings:"+DmdTot,"false");
					printValues.add("Savings Collected:"+obj.collectedAmount,"false");
					
				}
				else
				{
					printValues.add("Center:"+obj.CName,"false");
					printValues.add("Group:"+obj.GroupName,"false");
					printValues.add("Member:"+obj.MemberName+"("+obj.MemberCode+")","false");
					printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
					if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
					{
						printValues.add("Installment No:"+regularDemandsDO.InstallNo,"false");
						printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
					}
					float DmdTot=Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount);
					printValues.add("Demand:"+DmdTot,"false");
					printValues.add("collection Amt:"+obj.collectedAmount,"false");
					float OSAmt=Float.valueOf(obj.OSAmt)-Float.valueOf(CollAmt);
					Log.d("mfimo", "act os amt:"+obj.OSAmt+" collection amt:"+CollAmt);
					if(OSAmt<0)
						printValues.add("Curr OS(P+I):"+0.0,"false");
					else
						printValues.add("Curr OS(P+I):"+OSAmt,"false");
				}
				
				printValues.add(" ","false");
				printValues.add(intialParametrsDO.ReceiptFooter,"true");
				printValues.add(" ","false");
				printValues.add(" ","false");
				printValues.add(" ","false");
				
			}
			if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
			{
				// print agent copy
				for(int i=0 ; i <alArrayList.size(); i++ )
				{
					RegularDemandsDO obj = alArrayList.get(i);
					RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
					RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
					String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,LastReceiptPrintMenu.this);
					String header=intialParametrsDO.ReceiptHeader;
		            String[] head = header.split("@");
		            String head1=head[0];
		            String head2=head[1];
		            String head3=head[2];
		            
					printValues.add(head1,"true");
					String reciptNum=StringUtils.getRecieptNumber(obj,LastReceiptPrintMenu.this);

		            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
		            { }
		            else
		            {
		            	printValues.add(head2,"true");
		            }
		             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
		            { }
		            else
		            {
		            	printValues.add(head3,"true");
		            }
					printValues.add(" ","false");
					printValues.add("Agent Copy","true");	
					printValues.add("Transaction Acknowledgement","true");
					printValues.add("---------------------------","true");
					printValues.add(" ","false");
					printValues.add("R.No:"+reciptNum,"false");
					printValues.add("Date:"+obj.DemandDate,"false");
					printValues.add(" ","false");
					if(URL.equals("Yes"))
					{
						printValues.add("Center Name:"+obj.CName,"false");
						printValues.add("SHG Name:"+obj.GroupName,"false");
						printValues.add("Member Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
						printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
						if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
						{
							printValues.add("Meeting No:"+regularDemandsDO.InstallNo,"false");
//							printValues.add("Next Meeting Date:"+regularDemandsDO.NextRepayDate,"false");
						}
						float DmdTot=Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount);
						printValues.add("Planned Savings:"+DmdTot,"false");
						printValues.add("Savings Collected:"+obj.collectedAmount,"false");
						
					}
					else
					{
						printValues.add("Center:"+obj.CName,"false");
						printValues.add("Group:"+obj.GroupName,"false");
						printValues.add("Member:"+obj.MemberName+"("+obj.MemberCode+")","false");
						printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
						if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
						{
							printValues.add("Installment No:"+regularDemandsDO.InstallNo,"false");
							printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
						}
						float DmdTot=Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount);
						printValues.add("Demand:"+DmdTot,"false");
						printValues.add("collection Amt:"+obj.collectedAmount,"false");
						float OSAmt=Float.valueOf(obj.OSAmt)-Float.valueOf(CollAmt);
						Log.d("mfimo", "act os amt:"+obj.OSAmt+" collection amt:"+CollAmt);
						if(OSAmt<0)
							printValues.add("Curr OS(P+I):"+0.0,"false");
						else
							printValues.add("Curr OS(P+I):"+OSAmt,"false");
					}
					printValues.add(" ","false");
					printValues.add(intialParametrsDO.ReceiptFooter,"true");
					printValues.add(" ","false");
					printValues.add(" ","false");
					printValues.add(" ","false");
					
				}
			}
		}
		else
		{
			if(TXNTYPE.startsWith("G"))
			{
				float collectedAmt=0,totDemand=0;
			TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
			ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL.SelectAll(LastTXN,"","Group",LastReceiptPrintMenu.this);
			String header=intialParametrsDO.ReceiptHeader;
            String[] head = header.split("@");
            String head1=head[0];
            String head2=head[1];
            String head3=head[2];
            String reciptNum=StringUtils.getRecieptNumber(alArrayList.get(0),LastReceiptPrintMenu.this);
			printValues.add(head1,"true");
			

            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
            { }
            else
            {
            	printValues.add(head2,"true");
            }
             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
            { }
            else
            {
            	printValues.add(head3,"true");
            }
			
			printValues.add(" ","false");
			if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
			{
			printValues.add("Customer Copy","true");	
			}
			printValues.add("Transaction Acknowledgement","true");
			printValues.add("---------------------------","true");
			printValues.add(" ","false");
			printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
			if(URL.equals("Yes"))
			{
				printValues.add("R.No:"+reciptNum,"false");
				printValues.add("Center Name:"+alArrayList.get(0).CName,"false");
				printValues.add("SHG Name:"+alArrayList.get(0).GroupName,"false");
				
				for(int i=0 ; i <alArrayList.size(); i++ )
				{
					RegularDemandsDO obj = alArrayList.get(i);
					RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
					RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
					printValues.add(" ","false");
					printValues.add("Member Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
					printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
					if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
					{
						printValues.add("Meeting No :"+regularDemandsDO.InstallNo,"false");
					}
					printValues.add("Planned Savings:"+regularDemandsDO.DemandTotal,"false");
					printValues.add("Savings Collected:"+obj.collectedAmount,"false");
					totDemand=totDemand+(Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount));
					collectedAmt=collectedAmt+Float.valueOf(obj.collectedAmount);
				}
				printValues.add(" ","false");
				printValues.add("Total Planned Savings:"+totDemand,"false");
				printValues.add("Collected Savings:"+collectedAmt,"false");
			}
			else
			{
				printValues.add("Group R.No:"+LastTXN,"false");
				printValues.add("Center:"+alArrayList.get(0).CName,"false");
				printValues.add("Group:"+alArrayList.get(0).GroupName,"false");
				
				for(int i=0 ; i <alArrayList.size(); i++ )
				{
					RegularDemandsDO obj = alArrayList.get(i);
					RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
					RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
					String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,LastReceiptPrintMenu.this);
					printValues.add(" ","false");
					printValues.add("Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
					printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
					if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
					{
						printValues.add("Installment No :"+regularDemandsDO.InstallNo,"false");
						printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
					}
					printValues.add("collection Amt:"+obj.collectedAmount,"false");
					float OSAmt=Float.valueOf(obj.OSAmt)-Float.valueOf(CollAmt);
					Log.d("mfimo", "act os amt:"+obj.OSAmt+" collection amt:"+CollAmt);
					if(OSAmt<0)
						printValues.add("Curr OS(P+I):"+0.0,"false");
					else
						printValues.add("Curr OS(P+I):"+OSAmt,"false");
					totDemand=totDemand+(Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount));
					collectedAmt=collectedAmt+Float.valueOf(obj.collectedAmount);
				}
				printValues.add(" ","false");
				printValues.add("Total Group Demand :"+totDemand,"false");
				printValues.add("Total Collected amount :"+collectedAmt,"false");
			}
			
			printValues.add(" ","false");
			printValues.add(intialParametrsDO.ReceiptFooter,"true");
			printValues.add(" ","false");
			printValues.add(" ","false");
			printValues.add(" ","false");
			String reciptNum1=StringUtils.getRecieptNumber(alArrayList.get(0),LastReceiptPrintMenu.this);
			if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
			{
				// agent Copy
				
	            printValues.add(head1,"true");
	            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head2,"true");
	            }
	             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head3,"true");
	            }
				printValues.add(" ","false");
				printValues.add("Agent Copy","true");
				printValues.add(" ","false");
				printValues.add("Transaction Acknowledgement","true");
				printValues.add("---------------------------","true");
				printValues.add(" ","false");
				printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
				if(URL.equals("Yes"))
				{
					printValues.add("R.No:"+reciptNum1,"false");
					printValues.add("Center Name:"+alArrayList.get(0).CName,"false");
					printValues.add("SHG Name:"+alArrayList.get(0).GroupName,"false");
					
					for(int i=0 ; i <alArrayList.size(); i++ )
					{
						RegularDemandsDO obj = alArrayList.get(i);
						RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
						RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
						
						printValues.add(" ","false");
						printValues.add("Member Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
						printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
						if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
						{
							printValues.add("Meeting No :"+regularDemandsDO.InstallNo,"false");
						}
						printValues.add("Planned Savings:"+regularDemandsDO.DemandTotal,"false");
						printValues.add("Savings Collected:"+obj.collectedAmount,"false");
						totDemand=totDemand+(Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount));
						collectedAmt=collectedAmt+Float.valueOf(obj.collectedAmount);
					}
					
					printValues.add(" ","false");
					printValues.add("Total Planned Savings:"+totDemand,"false");
					printValues.add("Collected Savings:"+collectedAmt,"false");
				}
				else
				{
					printValues.add("Group R.No:"+LastTXN,"false");
					printValues.add("Center:"+alArrayList.get(0).CName,"false");
					printValues.add("Group:"+alArrayList.get(0).GroupName,"false");
					
					for(int i=0 ; i <alArrayList.size(); i++ )
					{
						RegularDemandsDO obj = alArrayList.get(i);
						RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
						RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
						String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,LastReceiptPrintMenu.this);
						printValues.add(" ","false");
						printValues.add("Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
						printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
						if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
						{
						printValues.add("Installment No :"+regularDemandsDO.InstallNo,"false");
						printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
						
						}
						printValues.add("collection Amt:"+obj.collectedAmount,"false");
						float OSAmt=Float.valueOf(obj.OSAmt)-Float.valueOf(CollAmt);
						Log.d("mfimo", "act os amt:"+obj.OSAmt+" collection amt:"+CollAmt);
						if(OSAmt<0)
							printValues.add("Curr OS(P+I):"+0.0,"false");
						else
							printValues.add("Curr OS(P+I):"+OSAmt,"false");
						totDemand=totDemand+(Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount));
						collectedAmt=collectedAmt+Float.valueOf(obj.collectedAmount);
					}
					
					printValues.add(" ","false");
					printValues.add("Total Group Demand :"+totDemand,"false");
					printValues.add("Total Collected amount :"+collectedAmt,"false");	
				}
				
				printValues.add(" ","false");
				printValues.add(intialParametrsDO.ReceiptFooter,"true");
				printValues.add(" ","false");
				printValues.add(" ","false");
				printValues.add(" ","false");
				
			}
		}
			else
			{
				float totDemand=0,collectedAmt=0;
				TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
				String CenterCode=trnsactionsBL.SelectCenterCode(LastTXN,LastReceiptPrintMenu.this);
				ArrayList<RegularDemandsDO> alDos = trnsactionsBL.SelectDistinctGroupsFromCenter(CenterCode,LastReceiptPrintMenu.this);
				String header=intialParametrsDO.ReceiptHeader;
	            String[] head = header.split("@");
	            String head1=head[0];
	            String head2=head[1];
	            String head3=head[2];
				printValues.add(head1,"true");
				

	            if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head2,"true");
	            }
	             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head3,"true");
	            }
				printValues.add(" ","false");
				printValues.add("Transaction Acknowledgement","true");
				printValues.add("---------------------------","true");
				printValues.add(" ","false");
				
				if(URL.equals("Yes"))
				{
					String reciptNum=StringUtils.getRecieptNumber(alDos.get(0),LastReceiptPrintMenu.this);
					for(int i =0 ; i < alDos.size(); i++ )
					{
					float GroupCollectedAmt= 0, GroupDemandAmt = 0;
					TrnsactionsBL trnsactionsBL2 = new TrnsactionsBL();
					ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL2.SelectAll(LastTXN,alDos.get(i).GNo,"Center",LastReceiptPrintMenu.this);
					if(i==0){
					printValues.add("Date : "+alArrayList.get(0).DemandDate,"false");
					printValues.add("Receipt Number:"+reciptNum,"false");
					printValues.add(" ","false");
					printValues.add("Center Name:"+alArrayList.get(0).CName,"false");
					}
					printValues.add("SHG Name:"+alArrayList.get(0).GroupName,"false");
					
					for(int k=0 ; k <alArrayList.size(); k++ )
					{
						RegularDemandsDO obj = alArrayList.get(k);
						RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
						RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
						
						printValues.add(" ","false");
						printValues.add("Member Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
						printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
						if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
						{
							printValues.add("Meeting No:"+regularDemandsDO.InstallNo,"false");
						}
						printValues.add("Planned Savings:"+regularDemandsDO.DemandTotal,"false");
						printValues.add("Savings Collected:"+obj.collectedAmount,"false");
								
						GroupCollectedAmt = GroupCollectedAmt+Float.parseFloat(obj.collectedAmount);
						GroupDemandAmt= GroupDemandAmt+(Float.parseFloat(regularDemandsDO.DemandTotal)+Float.parseFloat(regularDemandsDO.ODAmount));
						totDemand=totDemand+(Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount));
						collectedAmt=collectedAmt+Float.valueOf(obj.collectedAmount);
					}
					printValues.add(" ","false");
					printValues.add("Total SHG Planned Savings:"+GroupDemandAmt,"false");
					printValues.add("SHG Collected Savings:"+GroupCollectedAmt,"false");
					printValues.add(" ","false");
					}
					
					printValues.add("Total Planned Savings:"+totDemand,"false");
					printValues.add("Collected Savings:"+collectedAmt,"false");
				}
				else
				{
					for(int i =0 ; i < alDos.size(); i++ )
					{
						float GroupCollectedAmt= 0, GroupDemandAmt = 0;
					TrnsactionsBL trnsactionsBL2 = new TrnsactionsBL();
					ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL2.SelectAll(LastTXN,alDos.get(i).GNo,"Center",LastReceiptPrintMenu.this);
					if(i==0){
					printValues.add("Date : "+alArrayList.get(0).DemandDate,"false");
					printValues.add("Center R.No:"+LastTXN,"false");
					printValues.add(" ","false");
					printValues.add("Center:"+alArrayList.get(0).CName,"false");
					}
					printValues.add("Group:"+alArrayList.get(0).GroupName,"false");
					
					for(int k=0 ; k <alArrayList.size(); k++ )
					{
						RegularDemandsDO obj = alArrayList.get(k);
						RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
						RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
						
						printValues.add(" ","false");
						printValues.add("Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
						printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
						if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
						{
							printValues.add("InstallMent No:"+regularDemandsDO.InstallNo,"false");
							printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
						}
						String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,LastReceiptPrintMenu.this);
//						printValues.add("Demand :"+regularDemandsDO.DemandTotal,"false");
						printValues.add("collection:"+obj.collectedAmount,"false");
						float OSAmt=Float.valueOf(obj.OSAmt)-Float.valueOf(CollAmt);
						Log.d("mfimo", "act os amt:"+obj.OSAmt+" collection amt:"+CollAmt);
						if(OSAmt<0)
							printValues.add("Curr OS(P+I):"+0.0,"false");
						else
							printValues.add("Curr OS(P+I):"+OSAmt,"false");
//						printValues.add("Curr OS(P+I):"+obj.OSAmt,"false");
								
						GroupCollectedAmt = GroupCollectedAmt+Float.parseFloat(obj.collectedAmount);
						GroupDemandAmt= GroupDemandAmt+(Float.parseFloat(regularDemandsDO.DemandTotal)+Float.parseFloat(regularDemandsDO.ODAmount));
						totDemand=totDemand+(Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount));
						collectedAmt=collectedAmt+Float.valueOf(obj.collectedAmount);
					}
					printValues.add(" ","false");
					printValues.add("Group Demand Amt:"+GroupDemandAmt,"false");
					printValues.add("Group collection Amt:"+GroupCollectedAmt,"false");
					printValues.add(" ","false");
					}
					
					printValues.add("Total Center Demand:"+totDemand,"false");
					printValues.add("Total Center Coll. Amt:"+collectedAmt,"false");
				}
				
				printValues.add(" ","false");
				printValues.add(intialParametrsDO.ReceiptFooter,"true");
				printValues.add(" ","false");
				printValues.add(" ","false");
				printValues.add(" ","false");
				printValues.add(" ","false");
				
				if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
				{
					
						printValues.add(head1,"true");
						if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
			            { }
			            else
			            {
			            	printValues.add(head2,"true");
			            }
			             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
			            { }
			            else
			            {
			            	printValues.add(head3,"true");
			            }
						printValues.add(" ","false");
						printValues.add("Agent Copy","false");
						printValues.add("Transaction Acknowledgement","false");
						printValues.add("---------------------------","false");
						printValues.add(" ","false");
						// agent copy
						if(URL.equals("Yes"))
						{
							String reciptNum=StringUtils.getRecieptNumber(alDos.get(0),LastReceiptPrintMenu.this);
							for(int i =0 ; i < alDos.size(); i++ )
							{
							TrnsactionsBL trnsactionsBL2 = new TrnsactionsBL();
							ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL2.SelectAll(LastTXN,alDos.get(i).GNo,"Center",LastReceiptPrintMenu.this);
							
							float GroupCollectedAmt= 0, GroupDemandAmt = 0;
							if(i==0){
							printValues.add("Date : "+alArrayList.get(0).DemandDate,"false");
							printValues.add("Receipt Number:"+reciptNum,"false");
							printValues.add(" ","false");
							printValues.add("Center Name:"+alArrayList.get(0).CName,"false");
							}
							printValues.add("SHG Name :"+alArrayList.get(0).GroupName,"false");
							
							for(int k=0 ; k <alArrayList.size(); k++ )
							{
								RegularDemandsDO obj = alArrayList.get(k);
								RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
								RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
								
								printValues.add(" ","false");
								printValues.add("Member Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
								printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
								if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
								{
									printValues.add("Meeting No :"+regularDemandsDO.InstallNo,"false");
								
								}
								printValues.add("Planned Savings:"+regularDemandsDO.DemandTotal,"false");
								printValues.add("Savings Collected:"+obj.collectedAmount,"false");
								
								GroupCollectedAmt =GroupCollectedAmt +Float.parseFloat(obj.collectedAmount);
								GroupDemandAmt=GroupDemandAmt +(Float.parseFloat(regularDemandsDO.DemandTotal)+Float.parseFloat(regularDemandsDO.ODAmount));
								totDemand=totDemand+(Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount));
								collectedAmt=collectedAmt+Float.valueOf(obj.collectedAmount);
							}
							printValues.add(" ","false");
							printValues.add("Total SHG Planned Savings:"+GroupDemandAmt,"false");
							printValues.add("SHG Collected Savings:"+GroupCollectedAmt,"false");
							printValues.add(" ","false");
							}
							
							printValues.add("Total Planned Savings:"+totDemand,"false");
							printValues.add("Collected Savings:"+collectedAmt,"false");
						}
						else
						{
							String reciptNum=StringUtils.getRecieptNumber(alDos.get(0),LastReceiptPrintMenu.this);
							for(int i =0 ; i < alDos.size(); i++ )
							{
							TrnsactionsBL trnsactionsBL2 = new TrnsactionsBL();
							ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL2.SelectAll(LastTXN,alDos.get(i).GNo,"Center",LastReceiptPrintMenu.this);
							
							float GroupCollectedAmt= 0, GroupDemandAmt = 0;
							if(i==0){
							printValues.add("Date : "+alArrayList.get(0).DemandDate,"false");
							printValues.add("Center R.No:"+reciptNum,"false");
							printValues.add(" ","false");
							printValues.add("Center :"+alArrayList.get(0).CName,"false");
							}
							printValues.add("Group :"+alArrayList.get(0).GroupName,"false");
							
							for(int k=0 ; k <alArrayList.size(); k++ )
							{
								RegularDemandsDO obj = alArrayList.get(k);
								RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
								RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",LastReceiptPrintMenu.this).get(0);
								
								printValues.add(" ","false");
								printValues.add("Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
								printValues.add("Loan A/C No :"+obj.MLAI_ID,"false");
								if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
								{
									printValues.add("InstallMent No :"+regularDemandsDO.InstallNo,"false");
									printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
								}
								String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,LastReceiptPrintMenu.this);
								printValues.add("Demand :"+regularDemandsDO.DemandTotal,"false");
								printValues.add("collection :"+obj.collectedAmount,"false");
								float OSAmt=Float.valueOf(obj.OSAmt)-Float.valueOf(CollAmt);
								Log.d("mfimo", "act os amt:"+obj.OSAmt+" collection amt:"+CollAmt);
								if(OSAmt<0)
									printValues.add("Curr OS(P+I):"+0.0,"false");
								else
									printValues.add("Curr OS(P+I):"+OSAmt,"false");
			//					printValues.add("Curr OS(P+I) :"+obj.OSAmt,"false");
								
								GroupCollectedAmt =GroupCollectedAmt +Float.parseFloat(obj.collectedAmount);
								GroupDemandAmt=GroupDemandAmt +(Float.parseFloat(regularDemandsDO.DemandTotal)+Float.parseFloat(regularDemandsDO.ODAmount));
								totDemand=totDemand+(Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount));
								collectedAmt=collectedAmt+Float.valueOf(obj.collectedAmount);
							}
							printValues.add(" ","false");
							printValues.add("Group Demand Amt :"+GroupDemandAmt,"false");
							printValues.add("Group collection Amt :"+GroupCollectedAmt,"false");
							printValues.add("","false");
							}
							
							printValues.add("Total Center Demand:"+totDemand,"false");
							printValues.add("Total Center Coll. Amt:"+collectedAmt,"false");
						}
						
						printValues.add(" ","false");
						printValues.add(intialParametrsDO.ReceiptFooter,"true");
						printValues.add("","false");
						printValues.add("","false");
						printValues.add("","false");
						printValues.add("","false");
				}
			}
		}
	}else if(Type.equals("O")){
		Log.d("mfimo", "od demand collections");
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
			ODDemandsBL odemands=new ODDemandsBL();
			ArrayList<ODDemandsDO> alArrayList = odemands.SelectAllTransactions(LastTXN,LastReceiptPrintMenu.this);
			ODDemandsDO obj=alArrayList.get(0);
			String header=intialParametrsDO.ReceiptHeader;
	        String[] head = header.split("@");
	        String head1=head[0];
	        String head2=head[1];
	        String head3=head[2];
	        String RecptNumber = StringUtils.getRecieptNumberForOD(obj,LastReceiptPrintMenu.this);
			printValues.add(head1,"true");

	        if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
	        { }
	        else
	        {
	        	printValues.add(head2,"true");
	        }
	         if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
	        { }
	        else
	        {
	        	printValues.add(head3,"true");
	        }
			printValues.add(" ","false");
			printValues.add("OD Transaction Acknowledgement","true");
			printValues.add("---------------------------","true");
			if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1")){
			printValues.add("Customer Copy","true");
			}
			printValues.add(" ","false");
			printValues.add("Date:"+obj.DemandDate,"false");
			printValues.add("Receipt No:"+RecptNumber,"false");
			printValues.add("Center Name:"+obj.MCI_Name,"false");
			printValues.add("Group Name:"+obj.MGI_Name,"false");
			printValues.add("Member Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
			printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
			printValues.add("Total Demand:"+obj.ODAmt,"false");
			printValues.add("collection:"+obj.collectedAmt,"false");
			printValues.add(" ","false");
			printValues.add(intialParametrsDO.ReceiptFooter,"true");
			printValues.add(" ","false");
			printValues.add(" ","false");
			if(intialParametrsDO.AgentCopy.equalsIgnoreCase("1"))
			{
				
				printValues.add(head1,"true");
				if(head2.equals("0")||head2.equals("")||head2.equals("null") || head2.equals("NULL")|| head2.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head2,"true");
	            }
	             if(head3.equals("0")||head3.equals("")||head3.equals("null")|| head3.equals("NULL")|| head3.equals("Null"))
	            { }
	            else
	            {
	            	printValues.add(head3,"true");
	            }
				printValues.add("Transaction Acknowledgement","true");
				printValues.add("---------------------------","true");
				printValues.add("Agent Copy","true");
				printValues.add("Date:"+obj.DemandDate,"false");
				printValues.add("R.No:"+RecptNumber,"false");
				printValues.add("Center Name:"+obj.MCI_Name,"false");
				printValues.add("Group Name:"+obj.MGI_Name,"false");
				printValues.add("Member Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
				printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
				printValues.add("Total Demand:"+obj.ODAmt,"false");
				printValues.add("collection:"+obj.collectedAmt,"false");
				printValues.add(" ","false");
				printValues.add(intialParametrsDO.ReceiptFooter,"true");
				printValues.add(" ","false");
				printValues.add(" ","false");
			printValues.add(" ","false");
		}
	}
		isPrintTaken = true;
		return printValues.getDetailObj();
	}
}
