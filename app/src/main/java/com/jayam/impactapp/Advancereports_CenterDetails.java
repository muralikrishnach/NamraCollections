package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.PrintUtils;
import com.jayam.impactapp.utils.StringUtils;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Advancereports_CenterDetails extends Base implements PrintListner
{
	private LinearLayout llAck;
	private TextView tvCenterName,tvDemandDate,tvTotalOSAmt,tvTotalPreclosureIntrest,tvNoofMembers,tvCollectedAmount,tvAmounttobeCollected;
	private Button btnGroupMenu, btnPrint;
	private String CenterName,CenterCode;
	private AdvanceDemandBL trnsactionsBL;
	private ArrayList<AdvaceDemandDO> alArrayList;
	private float OSAmt=0,PreInt=0,CollAmt=0,AmtTobeColl=0;
	private IntialParametrsBL intialParametrsBL;
	private boolean isPrintTaken = false;
	private int DUPCOUNT;
	AdvanceDemandBL advancedemandBL;
	
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	
	@Override
	public void initialize()
	{
		CenterName		=	getIntent().getExtras().getString("CenterName");
		CenterCode		=	getIntent().getExtras().getString("CenterCode");

		intializeControlles();
		advancedemandBL = new AdvanceDemandBL();
		trnsactionsBL = new AdvanceDemandBL();
		intialParametrsBL = new  IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(Advancereports_CenterDetails.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		String Print=intialParametrsDO.PrintValidate;
		if(Print.equals("0"))
		{
			btnPrint.setVisibility(View.GONE);
		}
		alArrayList = trnsactionsBL.SelectReportsData(CenterCode,"Center",Advancereports_CenterDetails.this);
		
		tvCenterName.setText(""+CenterName);
		tvDemandDate.setText(""+alArrayList.get(0).DemandDate);
		tvNoofMembers.setText(""+alArrayList.size());
		for(AdvaceDemandDO Obj : alArrayList)
		{
			OSAmt=OSAmt+Float.valueOf(Obj.OS);
			PreInt=PreInt+Float.valueOf(Obj.OSAmt);
			CollAmt=CollAmt+Float.valueOf(Obj.previousAmt);
			AmtTobeColl=AmtTobeColl+Float.valueOf(Obj.OSAmt)-Float.valueOf(Obj.previousAmt);
		}

		tvTotalOSAmt.setText(""+OSAmt);
		tvTotalPreclosureIntrest.setText(""+PreInt);
		tvCollectedAmount.setText(""+CollAmt);
		tvAmounttobeCollected.setText(""+AmtTobeColl);
		
		
		btnGroupMenu.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Advancereports_CenterDetails.this, Advancereports_GroupDetails.class);
			
				intent.putExtra("CenterCode", CenterCode);
				startActivityForResult(intent, 1234);
			}
		});
		
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int DupNo=Integer.valueOf(trnsactionsBL.MinDupNo(CenterCode,"Center",Advancereports_CenterDetails.this));
				if(DupNo<3)
				{
					BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
					if (bluetooth.isEnabled()) 
					{
						PrintUtils printUtils = new PrintUtils(Advancereports_CenterDetails.this, Advancereports_CenterDetails.this);
						printUtils.print();
					}
					else
					{
						showAlertDailog("Please Switch On Mobile Bluetooth");
						return;
					}
				}
				else
				{
					showAlertDailog("Maximum No Of Prints are completed for this Center");
					return;
				}
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llAck						=	(LinearLayout)inflater.inflate(R.layout.advancereports_centerdetails, null);
		tvCenterName				=	(TextView)llAck.findViewById(R.id.tvRCenterName);
		tvDemandDate				=	(TextView)llAck.findViewById(R.id.tvDemandDate);
		tvTotalOSAmt				=	(TextView)llAck.findViewById(R.id.tvTotalOSAmt);
		tvTotalPreclosureIntrest	=	(TextView)llAck.findViewById(R.id.tvTotalPreclosureIntrest);
		tvNoofMembers				=	(TextView)llAck.findViewById(R.id.tvNoofMembers);
		tvCollectedAmount			=	(TextView)llAck.findViewById(R.id.tvCollectedAmount);
		tvAmounttobeCollected		=	(TextView)llAck.findViewById(R.id.tvAmounttobeCollected);
		btnGroupMenu				=	(Button)llAck.findViewById(R.id.btnGroupMenu);
		btnPrint					=	(Button)llAck.findViewById(R.id.btnPrint);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Center Summary");
	}
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
	@Override
	public PrintDetailsDO getprintObject() 
	{
		PrintValues printValues = new PrintValues();
		AdvanceDemandBL trnsactionsBL1 = new AdvanceDemandBL();
		ArrayList<AdvaceDemandDO> alDistincttxncode = trnsactionsBL1.SelectDistinctTransactioncodeFromCenter(CenterCode,"Center",Advancereports_CenterDetails.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		if(intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1"))
		{
			for(int i =0 ; i < alDistincttxncode.size(); i++ )
			{
			AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
			ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllTransactions(alDistincttxncode.get(i).TransactionCode,Advancereports_CenterDetails.this);
			for(int j=0 ; j <alArrayList.size(); j++ )
			{
				AdvaceDemandDO obj = alArrayList.get(j);
				AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAllCollectedData(obj.MLAI_ID, "memeber",Advancereports_CenterDetails.this).get(0);
				DUPCOUNT=  Integer.valueOf(trnsactionsBL.getDupNoforMember(obj.MLAI_ID,alDistincttxncode.get(i).TransactionCode,Advancereports_CenterDetails.this));
				String recnum=StringUtils.getRecieptNumberForAdvance(obj,Advancereports_CenterDetails.this);
				if(DUPCOUNT<3)
				{
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
					DUPCOUNT=DUPCOUNT+1;
					printValues.add("Duplicate TXN Acknowledgment "+DUPCOUNT,"true");
					printValues.add("----------------------------","true");
					printValues.add(" ","false");
					printValues.add("R.No:"+recnum,"false");
					printValues.add("Date:"+obj.DemandDate,"false");
					printValues.add(" ","false");
					printValues.add("Center:"+advanceDemandsDO.CenterName,"false");
					printValues.add("Group:"+advanceDemandsDO.MGI_Name,"false");
					printValues.add("Member:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
					printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
					printValues.add("Os Amt:"+advanceDemandsDO.OSAmt,"false");
					printValues.add("Advance collection:"+obj.previousAmt,"false");
					printValues.add(" ","false");
					printValues.add(intialParametrsDO.ReceiptFooter,"true");
					trnsactionsBL.updateDupNO(String.valueOf(DUPCOUNT),obj.MLAI_ID,alDistincttxncode.get(i).TransactionCode,Advancereports_CenterDetails.this);
				}
			}
		  }
		}
		else
		{
			for(int i =0 ; i < alDistincttxncode.size(); i++ )
			{
			float totDemand= 0, collectedAmt = 0;
//			ArrayList<AdvaceDemandDO> alDos = trnsactionsBL1.SelectDistinctGroupsFromCenter(String.valueOf(alDistincttxncode.get(i).TransactionCode));
			ArrayList<AdvaceDemandDO> alDos = trnsactionsBL1.SelectDistinctGroupsFromCenter(CenterCode,Advancereports_CenterDetails.this);
			int DupNo=Integer.valueOf(trnsactionsBL.SelectMinDupNo(alDistincttxncode.get(i).TransactionCode,CenterCode,"Center",Advancereports_CenterDetails.this));
			if(DupNo<3){
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
			DupNo=DupNo+1;
			printValues.add("Duplicate TXN Acknowledgment "+DupNo,"true");
			printValues.add("----------------------------","true");
			printValues.add(" ","false");
			for(int j =0 ; j < alDos.size(); j++ )
			{
				AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
				ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(alDistincttxncode.get(i).TransactionCode,alDos.get(j).MGI_Code,Advancereports_CenterDetails.this);
				if(alArrayList.size()>0)
				{
					String recnum=StringUtils.getRecieptNumberForAdvance(alArrayList.get(0),Advancereports_CenterDetails.this);
					float GroupCollectedAmt= 0, GroupDemandAmt = 0;
					if(j==0)
					{
						printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
						printValues.add("Center R.No:"+recnum,"false");
						printValues.add(" ","false");
					}
					for(int k=0 ; k <alArrayList.size(); k++ )
					{
						AdvaceDemandDO obj = alArrayList.get(k);
						AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",Advancereports_CenterDetails.this).get(0);
						if(k==0){
							if(j==0){
								printValues.add("Center:"+advanceDemandsDO.CenterName,"false");
							}
						printValues.add("Group:"+advanceDemandsDO.MGI_Name,"false");
						}
						printValues.add(" ","false");
						printValues.add("Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
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
			printValues.add("Total Center Demand:"+totDemand,"false");
			printValues.add("Total Center Coll. Amt:"+collectedAmt,"false");
			printValues.add(" ","false");
			printValues.add(intialParametrsDO.ReceiptFooter,"true");
			printValues.add(" ","false");
			printValues.add(" ","false");
			printValues.add(" ","false");
			trnsactionsBL.updateDupNoingroup(CenterCode,alDistincttxncode.get(i).TransactionCode,"Center",Advancereports_CenterDetails.this);
			}
		}
		}
		isPrintTaken = true;
		return printValues.getDetailObj();
	}

}
