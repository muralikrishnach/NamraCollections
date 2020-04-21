package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.PrintUtils;
import com.jayam.impactapp.utils.StringUtils;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectionReportSummary_Groups extends Base implements PrintListner
{
	private LinearLayout llAck;
	private TextView tvCenterName,tvGroupName, tvDate, tvRegularDemand, tvODAmt, tvTotalDemands, tvNOofMembers ,tvCollectedAmount, tvAmttobeCollected;
	private Button btnMenu, btnPrint;
	private String CNO;
	private TrnsactionsBL trnsactionsBL;
	private RegularDemandsBL regularDemandsBL;
	private ArrayList<RegularDemandsDO> alArrayList;
	private ArrayList<RegularDemandsDO> alArrayListTransactions;
	private String CollectedAmt;
	private float totalDemAND;
	private int totalAttendense;
	private String groupcode;
	private float totalRegularDemand, totalODAmount, TOTAl;
	private String collectedAmt;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	private boolean isPrintTaken = false;
	private int DUPCOUNT;
	RegularDemandsDO obj;
	@Override
	public void initialize()
	{
		groupcode	=	getIntent().getExtras().getString("groupcode");
		
		intializeControlles();
		
		IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(CollectionReportSummary_Groups.this);
		
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		String Print=intialParametrsDO.PrintValidate;
		if(Print.equals("0"))
		{
			btnPrint.setVisibility(View.GONE);
		}
		regularDemandsBL = new RegularDemandsBL();
		alArrayList = regularDemandsBL.SelectAll(groupcode, "Groups",CollectionReportSummary_Groups.this);
		
		
		trnsactionsBL = new TrnsactionsBL();
		collectedAmt = trnsactionsBL.getCollectedAmt(groupcode,CollectionReportSummary_Groups.this);
		
		btnMenu.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent  intent = new Intent(CollectionReportSummary_Groups.this, Collections_Reports_members.class);
				intent.putExtra("groupcode",groupcode);
				startActivityForResult(intent,1234);
			}
		});
		
		obj = alArrayList.get(0);
		tvCenterName.setText(""+obj.CName);
		tvGroupName.setText(""+obj.GroupName);
		tvDate.setText(""+obj.DemandDate);
		
		for(RegularDemandsDO regularDemandsDO : alArrayList)
		{
			totalRegularDemand = totalRegularDemand + Float.valueOf(regularDemandsDO.DemandTotal).floatValue() ;
			totalODAmount		=	totalODAmount +  Float.valueOf(regularDemandsDO.ODAmount).floatValue();
		}
		
		TOTAl = totalRegularDemand + totalODAmount;
		tvRegularDemand.setText(""+totalRegularDemand);
		tvODAmt.setText(""+totalODAmount);
		tvTotalDemands.setText(""+TOTAl);
		
		tvCollectedAmount.setText(""+Float.valueOf(collectedAmt));
		float banlanceAmt=TOTAl-Float.valueOf(collectedAmt).floatValue();
		if(banlanceAmt<=0)
		{
			banlanceAmt=0;	
		}else{
			
		}
		tvAmttobeCollected.setText(""+banlanceAmt);
		tvNOofMembers.setText(""+alArrayList.size());
		
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				int DupNo=Integer.valueOf(trnsactionsBL.MinDupNo(groupcode,"Group",CollectionReportSummary_Groups.this));
				if(DupNo<3)
				{
					BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
					if (bluetooth.isEnabled()) 
					{
						PrintUtils printUtils = new PrintUtils(CollectionReportSummary_Groups.this, CollectionReportSummary_Groups.this);
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
					showAlertDailog("Maximum No Of Prints are completed for this Group");
					return;
					
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
				Intent i = new Intent(CollectionReportSummary_Groups.this,loginActivity.class);
				startActivity(i);
			//	setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
	}
	
	public void intializeControlles()
	{
		llAck		=	(LinearLayout)inflater.inflate(R.layout.collectionreport_summary_groups, null);
		
		tvCenterName		=	(TextView)llAck.findViewById(R.id.tvCenterName);
		
		tvGroupName			=	(TextView)llAck.findViewById(R.id.tvGroupName);
		tvDate				=	(TextView)llAck.findViewById(R.id.tvDate);
		
		tvRegularDemand		=	(TextView)llAck.findViewById(R.id.tvRegularDemand);
		tvODAmt				=	(TextView)llAck.findViewById(R.id.tvODAmt);
		
		tvTotalDemands		=	(TextView)llAck.findViewById(R.id.tvTotalDemands);
		tvNOofMembers		=	(TextView)llAck.findViewById(R.id.tvNOofMembers);
		
		tvCollectedAmount	=	(TextView)llAck.findViewById(R.id.tvCollectedAmount);
		tvAmttobeCollected	=	(TextView)llAck.findViewById(R.id.tvAmttobeCollected);
		
		btnMenu				=	(Button)llAck.findViewById(R.id.btnMenu);
		btnPrint			=	(Button)llAck.findViewById(R.id.btnPrint);
		btnMenu.setText("Members");
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
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
		TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
		ArrayList<RegularDemandsDO> alDos = trnsactionsBL.SelectDistinctTransactioncodeFromCenter(groupcode,"Group",CollectionReportSummary_Groups.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		if(intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1"))
		{
			for(int i =0 ; i < alDos.size(); i++ )
			{
				TrnsactionsBL trnsactionsBL1 = new TrnsactionsBL();
				ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL1.SelectAll(alDos.get(i).TransactionCode,"","Group",CollectionReportSummary_Groups.this);
				
				for(int j=0 ; j <alArrayList.size(); j++ )
				{
					RegularDemandsDO obj = alArrayList.get(i);
					RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
					RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",CollectionReportSummary_Groups.this).get(0);
					String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,CollectionReportSummary_Groups.this);
					DUPCOUNT=  Integer.valueOf(trnsactionsBL.getDupNoforMember(obj.MLAI_ID,alDos.get(i).TransactionCode,CollectionReportSummary_Groups.this));
					String recnum=StringUtils.getRecieptNumber(obj,CollectionReportSummary_Groups.this);
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
						printValues.add("Duplicate TXN Acknowledgment"+DUPCOUNT,"true");
						printValues.add("---------------------------","true");
						printValues.add(" ","false");
						printValues.add("R.No:"+recnum,"false");
						printValues.add("Date:"+obj.DemandDate,"false");
						printValues.add(" ","false");
						printValues.add("Center:"+obj.CName,"false");
						printValues.add("Group:"+obj.GroupName,"false");
						printValues.add("Member:"+obj.MemberName+"("+obj.MemberCode+")","false");
						printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
						if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
						{
							printValues.add("Installment No:"+regularDemandsDO.InstallNo,"false");
						}
						printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
						float DmdTot=Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount);
						printValues.add("Demand:"+DmdTot,"false");
						printValues.add("Collection Amt:"+obj.collectedAmount,"false");
						float OSAmt=Float.valueOf(obj.OSAmt)-Float.valueOf(CollAmt);
						Log.d("mfimo", "act os amt:"+obj.OSAmt+" Collection amt:"+CollAmt);
						if(OSAmt<0)
							printValues.add("Curr OS(P+I):"+0.0,"false");
						else
							printValues.add("Curr OS(P+I):"+OSAmt,"false");
						printValues.add(" ","false");
						printValues.add(intialParametrsDO.ReceiptFooter,"true");
						trnsactionsBL.updateDupNO(String.valueOf(DUPCOUNT),obj.MLAI_ID,alDos.get(i).TransactionCode,CollectionReportSummary_Groups.this);
					}
				}
			}
		}
		else
		{
			for(int i =0 ; i < alDos.size(); i++ )
			{
				float totaldemand=0,totalcollection=0;
				ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL.SelectAll(alDos.get(i).TransactionCode,groupcode,"Center",CollectionReportSummary_Groups.this);
				int DupNo=Integer.valueOf(trnsactionsBL.SelectMinDupNo(alDos.get(i).TransactionCode,groupcode,"Group",CollectionReportSummary_Groups.this));
				String recnum=StringUtils.getRecieptNumber(alArrayList.get(0),CollectionReportSummary_Groups.this);
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
				printValues.add("Date:"+alArrayList.get(0).DemandDate,"false");
				printValues.add("Receipt Number:"+recnum,"false");
				printValues.add(" ","false");
				printValues.add("Center:"+alArrayList.get(0).CName,"false");
				printValues.add("Group:"+alArrayList.get(0).GroupName,"false");
				
				for(int k=0 ; k <alArrayList.size(); k++ )
				{
					RegularDemandsDO obj = alArrayList.get(k);
					RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
					RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",CollectionReportSummary_Groups.this).get(0);
					String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,CollectionReportSummary_Groups.this);
					printValues.add(" ","false");
					printValues.add("Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
					printValues.add("Loan A/C No:"+obj.MLAI_ID,"false");
					if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
					{
//						printValues.add("Installment No :"+regularDemandsDO.InstallNo,"false");
						printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
					}
					printValues.add("Collection Amt:"+obj.collectedAmount,"false");
					float OSAmt=Float.valueOf(obj.OSAmt)-Float.valueOf(CollAmt);
					Log.d("mfimo", "act os amt:"+obj.OSAmt+" Collection amt:"+CollAmt);
					if(OSAmt<0)
						printValues.add("Curr OS(P+I):"+0.0,"false");
					else
						printValues.add("Curr OS(P+I):"+OSAmt,"false");
					totaldemand=totaldemand+Float.valueOf(regularDemandsDO.DemandTotal)+Float.valueOf(regularDemandsDO.ODAmount);
					totalcollection=totalcollection+Float.valueOf(obj.collectedAmount);
				}
				printValues.add(" ","false");
				printValues.add("Total Group Demand :"+totaldemand,"false");
				printValues.add("Total Collected amount :"+totalcollection,"false");
				printValues.add(" ","false");
				printValues.add(intialParametrsDO.ReceiptFooter,"true");
				printValues.add(" ","false");
				printValues.add(" ","false");
				printValues.add(" ","false");
				printValues.add(" ","false");
				trnsactionsBL.updateDupNoingroup(groupcode,alDos.get(i).TransactionCode,"Group",CollectionReportSummary_Groups.this);
				}
			}
			
		}
		
		isPrintTaken = true;
		return printValues.getDetailObj();
	}

}
