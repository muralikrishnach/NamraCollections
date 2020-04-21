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

public class AdvanceReport_GroupsSummary extends Base implements PrintListner
{
	private LinearLayout llAck;
	private TextView tvReportGroupName,tvRDemandDate, tvRTotalOSAmt, tvRTotalPreclosureIntrest, tvRNoofMembers, tvRCollectedAmount, tvRAmounttobeCollected;
	private Button btnGroupMembers, btnPrint;
	private AdvanceDemandBL advancedemandBL;
	private ArrayList<AdvaceDemandDO> alArrayList;
	private String groupCode;
	String groupcode;
	AdvaceDemandDO advacedemandDO;
	private float totalRegularDemand, totalODAmount, TOTAl;

	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	private IntialParametrsBL intialParametrsBL;
	private float OSAmt=0,PreInt=0,CollAmt=0,AmtTobeColl=0;
	private boolean isPrintTaken = false;
	private int DUPCOUNT;

	@Override
	public void initialize()
	{
		groupcode	=	getIntent().getExtras().getString("groupcode");
		intializeControlles();
		advancedemandBL = new AdvanceDemandBL();
		intialParametrsBL = new  IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(AdvanceReport_GroupsSummary.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		String Print=intialParametrsDO.PrintValidate;
		if(Print.equals("0"))
		{
			btnPrint.setVisibility(View.GONE);
		}
		alArrayList = advancedemandBL.SelectReportsData(groupcode,"Group",AdvanceReport_GroupsSummary.this);
		tvReportGroupName.setText(""+alArrayList.get(0).MGI_Name);
		tvRDemandDate.setText(""+alArrayList.get(0).DemandDate);
		tvRNoofMembers.setText(""+alArrayList.size());

		for(AdvaceDemandDO Obj : alArrayList)
		{
			OSAmt=OSAmt+Float.valueOf(Obj.OS);
			PreInt=PreInt+Float.valueOf(Obj.OSAmt);
			CollAmt=CollAmt+Float.valueOf(Obj.previousAmt);
			AmtTobeColl=AmtTobeColl+Float.valueOf(Obj.OSAmt)-Float.valueOf(Obj.previousAmt);
		}
		tvRTotalOSAmt.setText(""+OSAmt);
		tvRTotalPreclosureIntrest.setText(""+PreInt);
		tvRCollectedAmount.setText(""+CollAmt);		
		tvRAmounttobeCollected.setText(""+AmtTobeColl);
		
		groupCode=alArrayList.get(0).MGI_Code;
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int DupNo=Integer.valueOf(advancedemandBL.MinDupNo(groupcode,"Group",AdvanceReport_GroupsSummary.this));
				if(DupNo<3)
				{
					BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
					if (bluetooth.isEnabled()) 
					{
						PrintUtils printUtils = new PrintUtils(AdvanceReport_GroupsSummary.this, AdvanceReport_GroupsSummary.this);
						printUtils.print();
					}
					else
					{
						showAlertDailog("Please Switch On Mobile Bluetooth");
						return;
					}
				}else
				{
					showAlertDailog("Maximum No Of Prints are completed for this Group");
					return;
				}
			}
		});
		
		btnGroupMembers.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent  intent = new Intent(AdvanceReport_GroupsSummary.this, AdvanceReports_Members.class);
				intent.putExtra("groupcode", groupCode);
				startActivityForResult(intent,1234);
			}
		});
		
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llAck						=	(LinearLayout)inflater.inflate(R.layout.advancereports_centerdetails, null);
		tvReportGroupName			=	(TextView)llAck.findViewById(R.id.tvRCenterName);
		tvRDemandDate				=	(TextView)llAck.findViewById(R.id.tvDemandDate);
		tvRTotalOSAmt				=	(TextView)llAck.findViewById(R.id.tvTotalOSAmt);
		tvRTotalPreclosureIntrest	=	(TextView)llAck.findViewById(R.id.tvTotalPreclosureIntrest);
		tvRNoofMembers				=	(TextView)llAck.findViewById(R.id.tvNoofMembers);
		tvRCollectedAmount			=	(TextView)llAck.findViewById(R.id.tvCollectedAmount);
		tvRAmounttobeCollected		=	(TextView)llAck.findViewById(R.id.tvAmounttobeCollected);
		btnGroupMembers				=	(Button)llAck.findViewById(R.id.btnGroupMenu);
		btnPrint					=	(Button)llAck.findViewById(R.id.btnPrint);
		
		btnGroupMembers.setText("Members");
		TextView CTGName			=	(TextView)llAck.findViewById(R.id.ADVCenterName);
		CTGName.setText("Group Name");
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Group Summary");
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
		ArrayList<AdvaceDemandDO> alDistincttxncode = trnsactionsBL1.SelectDistinctTransactioncodeFromCenter(groupcode,"Group",AdvanceReport_GroupsSummary.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		if(intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1"))
		{
			for(int i =0 ; i < alDistincttxncode.size(); i++ )
			{
			AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
			ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllTransactions(alDistincttxncode.get(i).TransactionCode,AdvanceReport_GroupsSummary.this);
			for(int j=0 ; j <alArrayList.size(); j++ )
			{
				AdvaceDemandDO obj = alArrayList.get(j);
				AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",AdvanceReport_GroupsSummary.this).get(0);
				String recnum=StringUtils.getRecieptNumberForAdvance(obj,AdvanceReport_GroupsSummary.this);
				DUPCOUNT=  Integer.valueOf(trnsactionsBL.getDupNoforMember(obj.MLAI_ID,alDistincttxncode.get(i).TransactionCode,AdvanceReport_GroupsSummary.this));
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
					trnsactionsBL.updateDupNO(String.valueOf(DUPCOUNT),obj.MLAI_ID,alDistincttxncode.get(i).TransactionCode,AdvanceReport_GroupsSummary.this);
				}
			}
		  }
		}
		else
		{
			for(int i =0 ; i < alDistincttxncode.size(); i++ )
			{
			float totaldemand=0,totalcollection=0;
			AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
			ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllGroupTransactions(alDistincttxncode.get(i).TransactionCode,groupcode,AdvanceReport_GroupsSummary.this);
			int DupNo=Integer.valueOf(trnsactionsBL.SelectMinDupNo(alDistincttxncode.get(i).TransactionCode,groupcode,"Group",AdvanceReport_GroupsSummary.this));
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
			printValues.add("------------------------------","true");
			printValues.add(" ","false");
			printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
			printValues.add("Group R.No:"+alDistincttxncode.get(i).TransactionCode,"false");
			printValues.add("Center:"+alArrayList.get(0).CenterName,"false");
			printValues.add("Group:"+alArrayList.get(0).MGI_Name,"false");
			
			for(int j=0 ; j <alArrayList.size(); j++ )
			{
				AdvaceDemandDO obj = alArrayList.get(j);
				AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",AdvanceReport_GroupsSummary.this).get(0);
				printValues.add(" ","false");
				printValues.add("Name:"+obj.MMI_Name+"("+obj.MMI_Code+")","false");
				printValues.add("collection Amt:"+obj.previousAmt,"false");
				totaldemand=totaldemand+Float.valueOf(advanceDemandsDO.OSAmt);
				totalcollection=totalcollection+Float.valueOf(obj.previousAmt);
			}
			printValues.add(" ","false");
			printValues.add("Total Group Demand :"+totaldemand,"false");
			printValues.add("Total Collected amount :"+totalcollection,"false");
			printValues.add(" ","false");
			printValues.add(intialParametrsDO.ReceiptFooter,"true");
			printValues.add(" ","false");
			printValues.add(" ","false");
			trnsactionsBL.updateDupNoingroup(groupcode,alDistincttxncode.get(i).TransactionCode,"Group",AdvanceReport_GroupsSummary.this);
			}
			}
		}
		isPrintTaken = true;
		return printValues.getDetailObj();
	}

}
