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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Advancereports_MemberDetails extends Base implements PrintListner
{
	private LinearLayout llAck;
	private TextView tvRCenterName,tvRGroupName,tvRMemberName,tvDemandDate,tvTotalOSAmt,tvTotalPreclosureIntrest,tvCollectedAmount,tvAmounttobeCollected;
	private Button  btnPrint,btnMenu;
	private String MLAIID;
	private AdvanceDemandBL trnsactionsBL;
	private ArrayList<AdvaceDemandDO> alArrayList;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    private IntialParametrsBL intialParametrsBL;
	private boolean isPrintTaken = false;
	
	AdvanceDemandBL advancedemandBL;
	
	private int DUPCOUNT;
	
	@Override
	public void initialize()
	{
		MLAIID			=	getIntent().getExtras().getString("MLAIID");
		intializeControlles();
		trnsactionsBL = new AdvanceDemandBL();
		advancedemandBL= new AdvanceDemandBL();
		alArrayList = trnsactionsBL.SelectReportsData(MLAIID,"Memeber",Advancereports_MemberDetails.this);
		intialParametrsBL = new  IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(Advancereports_MemberDetails.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		String Print=intialParametrsDO.PrintValidate;
		if(Print.equals("0"))
		{
			btnPrint.setVisibility(View.GONE);
		}
		tvRCenterName.setText(""+alArrayList.get(0).CenterName);
		tvRGroupName.setText(""+alArrayList.get(0).MGI_Name);
		tvRMemberName.setText(""+alArrayList.get(0).MMI_Name);
		tvDemandDate.setText(""+alArrayList.get(0).DemandDate);
		tvTotalOSAmt.setText(""+alArrayList.get(0).OS);
		tvTotalPreclosureIntrest.setText(""+alArrayList.get(0).OSAmt);
		tvCollectedAmount.setText(""+alArrayList.get(0).previousAmt);
		float TobeColl=Float.valueOf(alArrayList.get(0).OSAmt)-Float.valueOf(alArrayList.get(0).previousAmt);
		tvAmounttobeCollected.setText(""+TobeColl);

		
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
//				if(intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1"))
//				{
					String txncode = advancedemandBL.getTxncode(MLAIID,Advancereports_MemberDetails.this);
					String dupno=advancedemandBL.getDupNoforMember(MLAIID,txncode,Advancereports_MemberDetails.this);
					int dupCount=Integer.valueOf(dupno);
					if(dupCount<3)
					{
						BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
						if (bluetooth.isEnabled()) 
						{
							PrintUtils printUtils = new PrintUtils(Advancereports_MemberDetails.this, Advancereports_MemberDetails.this);
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
						showAlertDailog("Max No Of Prints Are Taken For This Member");
						return;	
					}
//				}
//				else
//				{
//					showAlertDailog("You are not authorised to use this print");
//					return;
//				}
			}
		});
		btnMenu.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setResult(AppConstants.RESULTCODE_HOME);
				finish();

			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llAck						=	(LinearLayout)inflater.inflate(R.layout.advancrreport_memberdetails, null);
		tvRCenterName				=	(TextView)llAck.findViewById(R.id.tvRCenterName);
		tvRGroupName				=	(TextView)llAck.findViewById(R.id.tvRGroupName);
		tvRMemberName				=	(TextView)llAck.findViewById(R.id.tvRMemberName);
		tvDemandDate				=	(TextView)llAck.findViewById(R.id.tvDemandDate);
		tvTotalOSAmt				=	(TextView)llAck.findViewById(R.id.tvTotalOSAmt);
		tvTotalPreclosureIntrest	=	(TextView)llAck.findViewById(R.id.tvTotalPreclosureIntrest);
		tvCollectedAmount			=	(TextView)llAck.findViewById(R.id.tvCollectedAmount);
		tvAmounttobeCollected		=	(TextView)llAck.findViewById(R.id.tvAmounttobeCollected);
		btnPrint					=	(Button)llAck.findViewById(R.id.btnPrint);
		btnMenu						=	(Button)llAck.findViewById(R.id.btnMenu);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Member Summary");
	}

	@Override
	public PrintDetailsDO getprintObject() 
	{
		PrintValues printValues = new PrintValues();
		AdvanceDemandBL trnsactionsBL1 = new AdvanceDemandBL();
		ArrayList<AdvaceDemandDO> alDistincttxncode = trnsactionsBL1.SelectDistinctTransactioncodeFromCenter(MLAIID,"Member",Advancereports_MemberDetails.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		
			for(int i =0 ; i < alDistincttxncode.size(); i++ )
			{
			AdvanceDemandBL trnsactionsBL = new AdvanceDemandBL();
			ArrayList<AdvaceDemandDO> alArrayList = trnsactionsBL.SelectAllTransactions(alDistincttxncode.get(i).TransactionCode,Advancereports_MemberDetails.this);
			for(int j=0 ; j <alArrayList.size(); j++ )
			{
				AdvaceDemandDO obj = alArrayList.get(j);
				AdvaceDemandDO advanceDemandsDO = advancedemandBL.SelectAll(obj.MLAI_ID, "memeber",Advancereports_MemberDetails.this).get(0);
				String dupno=trnsactionsBL1.getDupNoforMember(obj.MLAI_ID,alDistincttxncode.get(i).TransactionCode,Advancereports_MemberDetails.this);
				String recnum=StringUtils.getRecieptNumberForAdvance(obj,Advancereports_MemberDetails.this);
				DUPCOUNT=  Integer.valueOf(dupno);
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
					trnsactionsBL.updateDupNO(String.valueOf(DUPCOUNT),obj.MLAI_ID,alDistincttxncode.get(i).TransactionCode,Advancereports_MemberDetails.this);
				}
				
			}
		  }
		
		isPrintTaken = true;
		return printValues.getDetailObj();
	}

}
