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
import com.jayam.impactapp.utils.SharedPrefUtils;
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

public class CollectionReportSummary extends Base implements PrintListner
{
	private LinearLayout llAck;
	private TextView tvDate,tvCenterName,tvTotalDemand,tvCollection,tvNoofmem,tvTotalRegDemand,tvTotalODAmt,tvBalanceAmt,tvSavingsAmt;
	private Button btnMenu, btnPrint;
	private String CNO;
	private TrnsactionsBL trnsactionsBL;
	private RegularDemandsBL regularDemandsBL;
	private ArrayList<RegularDemandsDO> alArrayList;
	private ArrayList<RegularDemandsDO> alArrayListRegularDemand;
	private String CollectedAmt;
	private float totalDemAND;
	private float totalRegDem;
	private boolean isPrintTaken = false;
	private float totalODDem;
	private int DUPCOUNT;
	private int totalAttendense;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	String URL="No";
	
	@Override
	public void initialize()
	{
		CNO				=	getIntent().getExtras().getString("CNO");
		CollectedAmt 	= getIntent().getExtras().getString("CollectedAmt");
		intializeControlles();
		URL=SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
		IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(CollectionReportSummary.this);
		
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		String Print=intialParametrsDO.PrintValidate;
		if(Print.equals("0"))
		{
			btnPrint.setVisibility(View.GONE);
		}
		trnsactionsBL = new TrnsactionsBL();
		regularDemandsBL = new RegularDemandsBL();
		
		alArrayList = trnsactionsBL.SelectAllGroups(CNO,CollectionReportSummary.this);
		
		alArrayListRegularDemand = regularDemandsBL.SelectAll(CNO, "CNo",CollectionReportSummary.this);
		
//		if(alArrayList != null && alArrayList.size() > 2)
//			Collections.sort(alArrayList, new RecieptComparator());
		tvDate.setText(""+alArrayList.get(0).DemandDate);
		tvCenterName.setText(""+alArrayList.get(0).CName);
		
		tvCollection.setText(""+CollectedAmt);
		tvNoofmem.setText(""+alArrayListRegularDemand.size());
		
		for(RegularDemandsDO Obj : alArrayListRegularDemand)
		{
			totalRegDem =totalRegDem+ Float.valueOf(Obj.DemandTotal).floatValue();
			totalODDem  =totalODDem+ Float.valueOf(Obj.ODAmount).floatValue();
			totalDemAND = Float.valueOf(Obj.DemandTotal).floatValue() + Float.valueOf(Obj.ODAmount).floatValue()+totalDemAND;
		}
		tvTotalRegDemand.setText(""+totalRegDem);
		tvTotalODAmt.setText(""+totalODDem);
		tvTotalDemand.setText(""+totalDemAND);
		
		float BalAmt=0;
		BalAmt=totalDemAND-Float.valueOf(CollectedAmt).floatValue();
		if(BalAmt<=0)
		{
			BalAmt=0;
		}
		else{
			
		}
		tvBalanceAmt.setText(""+BalAmt);
		btnMenu.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(CollectionReportSummary.this, CollectionReport_Groups.class);
				intent.putExtra("CNO", CNO);
				startActivityForResult(intent, 1234);
			}
		});
		
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int DupNo=Integer.valueOf(trnsactionsBL.MinDupNo(CNO,"Center",CollectionReportSummary.this));
				if(DupNo<3)
				{
					BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
					if (bluetooth.isEnabled()) 
					{
					PrintUtils printUtils = new PrintUtils(CollectionReportSummary.this, CollectionReportSummary.this);
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
				Intent i = new Intent(CollectionReportSummary.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
	}
	
	
	
	public void intializeControlles()
	{
		llAck		=	(LinearLayout)inflater.inflate(R.layout.collectionreport_summary, null);
		

		tvDate				=	(TextView)llAck.findViewById(R.id.tvDate);
		tvCenterName		=	(TextView)llAck.findViewById(R.id.tvCenterName);
		tvTotalDemand		=	(TextView)llAck.findViewById(R.id.tvTotalDemand);
		tvTotalRegDemand	=	(TextView)llAck.findViewById(R.id.tvTotalRegDemand);
		tvTotalODAmt		=	(TextView)llAck.findViewById(R.id.tvTotalODDemand);
		tvCollection		=	(TextView)llAck.findViewById(R.id.tvCollection);
		tvNoofmem			=	(TextView)llAck.findViewById(R.id.tvNOOFMem);
		tvBalanceAmt		=	(TextView)llAck.findViewById(R.id.tvBalAmt);
		btnMenu				=	(Button)llAck.findViewById(R.id.btnMenu);
		btnPrint			=	(Button)llAck.findViewById(R.id.btnPrint);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		if(URL.equals("Yes"))
		{
			btnMenu.setText("SHGS");
		}
		tvHeader.setText("Center Summary");
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
	@Override
	public PrintDetailsDO getprintObject() 
	{
//		PrintDetailsDO detailsDO = new PrintDetailsDO();
		PrintValues printValues = new PrintValues();
		TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
		ArrayList<RegularDemandsDO> alDos = trnsactionsBL.SelectDistinctTransactioncodeFromCenter(CNO,"Center",CollectionReportSummary.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		if(intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1"))
		{
			for(int i =0 ; i < alDos.size(); i++ )
			{
				TrnsactionsBL trnsactionsBL1 = new TrnsactionsBL();
				ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL1.SelectAll(alDos.get(i).TransactionCode,"","Group",CollectionReportSummary.this);
				for(int j=0 ; j <alArrayList.size(); j++ )
				{
					RegularDemandsDO obj = alArrayList.get(i);
					RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
					RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",CollectionReportSummary.this).get(0);
					String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,CollectionReportSummary.this);
					DUPCOUNT=  Integer.valueOf(trnsactionsBL.getDupNoforMember(obj.MLAI_ID,alDos.get(i).TransactionCode,CollectionReportSummary.this));
					String recnum=StringUtils.getRecieptNumber(obj,CollectionReportSummary.this);
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
						printValues.add("---------------------------","true");
						printValues.add(" ","false");
						printValues.add("R.No:"+recnum,"false");
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
								printValues.add("Meeting No:"+obj.InstallNo,"false");
								printValues.add("Next Meeting Date:"+regularDemandsDO.NextRepayDate,"false");
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
						}
						
						
						printValues.add(" ","false");
						printValues.add(intialParametrsDO.ReceiptFooter,"true");
						trnsactionsBL.updateDupNO(String.valueOf(DUPCOUNT),obj.MLAI_ID,alDos.get(i).TransactionCode,CollectionReportSummary.this);
					}
				}
			}
		}
		else
		{
			for(int i =0 ; i < alDos.size(); i++ )
			{
				float totDemand= 0, collectedAmt = 0;
				ArrayList<RegularDemandsDO> alGroupcode = trnsactionsBL.SelectDistinctGroupsCodefromtxn(String.valueOf(alDos.get(i).TransactionCode),CollectionReportSummary.this);
				int DupNo=Integer.valueOf(trnsactionsBL.SelectMinDupNo(alDos.get(i).TransactionCode,CNO,"Center",CollectionReportSummary.this));
				if(DupNo<3)
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
					DupNo=DupNo+1;
					printValues.add("Duplicate TXN Acknowledgment "+DupNo,"true");
					printValues.add("----------------------------","true");
					printValues.add(" ","false");
					for(int k =0 ; k < alGroupcode.size(); k++ )
					{
						float GroupCollectedAmt= 0, GroupDemandAmt = 0;
						TrnsactionsBL trnsactionsBL2 = new TrnsactionsBL();
						ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL2.SelectAll(alDos.get(i).TransactionCode,alGroupcode.get(k).GNo,"Center",CollectionReportSummary.this);
						String recnum=StringUtils.getRecieptNumber(alArrayList.get(0),CollectionReportSummary.this);
						printValues.add("Date : "+alArrayList.get(0).DemandDate,"false");
//						printValues.add("R.N0:"+alDos.get(i).TransactionCode,"false");
						printValues.add("R.N0:"+recnum,"false");
						printValues.add(" ","false");
						printValues.add("Center:"+alArrayList.get(0).CName,"false");
						printValues.add("Group:"+alArrayList.get(0).GroupName,"false");
						for(int j=0 ; j <alArrayList.size(); j++ )
						{
							RegularDemandsDO obj = alArrayList.get(j);
							RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
							RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",CollectionReportSummary.this).get(0);
							printValues.add(" ","false");
							printValues.add("Name:"+obj.MemberName+"("+obj.MemberCode+")","false");
							printValues.add("Loan A/C No:"+regularDemandsDO.MLAI_ID,"false");
							if(intialParametrsDO.InstRequired.equalsIgnoreCase("1"))
							{
								//printValues.add("InstallMent No:"+regularDemandsDO.InstallNo,"false");
								printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
							}
							String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,CollectionReportSummary.this);
							printValues.add("Collection:"+obj.collectedAmount,"false");
							float OSAmt=Float.valueOf(obj.OSAmt)-Float.valueOf(CollAmt);
							Log.d("mfimo", "act os amt:"+obj.OSAmt+" Collection amt:"+CollAmt);
							if(OSAmt<0)
								printValues.add("Curr OS(P+I):"+0.0,"false");
							else
								printValues.add("Curr OS(P+I):"+OSAmt,"false");
							GroupCollectedAmt = GroupCollectedAmt+Float.parseFloat(obj.collectedAmount);
							GroupDemandAmt= GroupDemandAmt+(Float.parseFloat(regularDemandsDO.DemandTotal)+Float.parseFloat(regularDemandsDO.ODAmount));
						}
						totDemand=totDemand+GroupCollectedAmt;
						collectedAmt=collectedAmt+GroupDemandAmt;
						printValues.add(" ","false");
						printValues.add("Group Demand Amt:"+GroupDemandAmt,"false");
						printValues.add("Group Collection Amt:"+GroupCollectedAmt,"false");
						printValues.add(" ","false");
					}
					printValues.add("Total Center Demand:"+totDemand,"false");
					printValues.add("Total Center Coll. Amt:"+collectedAmt,"false");
					printValues.add(" ","false");
					printValues.add(intialParametrsDO.ReceiptFooter,"true");
					printValues.add(" ","false");
					printValues.add(" ","false");
					printValues.add(" ","false");
					printValues.add(" ","false");
					trnsactionsBL.updateDupNoingroup(CNO,alDos.get(i).TransactionCode,"Center",CollectionReportSummary.this);
				}
			}
		}
		isPrintTaken = true;
		return printValues.getDetailObj();
	}

}
