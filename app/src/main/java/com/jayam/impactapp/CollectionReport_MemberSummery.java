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

public class CollectionReport_MemberSummery extends Base implements PrintListner
{
	private LinearLayout llCollectonReport_members;
	private TextView tvMemberName, tvDate, tvCenterName, tvgroupname, tvRegularDemand, tvODAmt, tvTotalDemand, tvCollectedAmt, tvAmttobeCollected;
	private Button btnMenu, btnPrint;
	private String MLAIID;
	
	private TrnsactionsBL trnsactionsBL;
	private RegularDemandsBL regularDemandsBL;
	private ArrayList<RegularDemandsDO> alArrayListRegular, alArrayListTransaction;
	private float collectedAmt;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	RegularDemandsDO regularDemandsDO;
	float totalAmount;
	private int DUPCOUNT;
	@Override
	public void initialize() 
	{
		MLAIID = getIntent().getExtras().getString("MLAIID");
		
		trnsactionsBL = new TrnsactionsBL();
		regularDemandsBL = new RegularDemandsBL();
		
		alArrayListRegular = regularDemandsBL.SelectAll(MLAIID, "memeber",CollectionReport_MemberSummery.this);
		collectedAmt = Float.valueOf(trnsactionsBL.getCollectedAmtForMember(MLAIID,CollectionReport_MemberSummery.this));

		
		intializeControlles();
		
		IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(CollectionReport_MemberSummery.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		String Print=intialParametrsDO.PrintValidate;
		if(Print.equals("0"))
		{
			btnPrint.setVisibility(View.GONE);
		}
		regularDemandsDO = alArrayListRegular.get(0);
		
		tvMemberName.setText(""+regularDemandsDO.MemberName);
		tvDate.setText(""+regularDemandsDO.DemandDate);
		tvCenterName.setText(""+regularDemandsDO.CName);
		tvgroupname.setText(""+regularDemandsDO.GroupName);
		tvRegularDemand.setText(""+regularDemandsDO.DemandTotal);
		tvODAmt.setText(""+regularDemandsDO.ODAmount);
		
		totalAmount = Float.valueOf(regularDemandsDO.DemandTotal).floatValue() + Float.valueOf(regularDemandsDO.ODAmount).floatValue();
		tvTotalDemand.setText(""+totalAmount);
		tvCollectedAmt.setText(""+collectedAmt);
		float banlanceAmt=totalAmount-collectedAmt;
		if(banlanceAmt<=0)
		{
			banlanceAmt=0;	
		}else{
			
		}
		tvAmttobeCollected.setText(""+banlanceAmt);
		
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
				Intent i = new Intent(CollectionReport_MemberSummery .this,loginActivity.class);
				startActivity(i);
			//	setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
		
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
				if(intialParametrsDO.IndividualReceipts.equalsIgnoreCase("1"))
				{
					int DupNo=Integer.valueOf(trnsactionsBL.MinDupNo(MLAIID,"Member",CollectionReport_MemberSummery.this));
					if(DupNo<3)
					{
						BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
						if (bluetooth.isEnabled()) 
						{
							PrintUtils printUtils = new PrintUtils(CollectionReport_MemberSummery.this, CollectionReport_MemberSummery.this);
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
				}
				else
				{
					showAlertDailog("You are not authorised to use this print");
					return;
				}
				
			}
		});
		btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(AppConstants.RESULTCODE_HOME);
				finish();
				
			}
		});
		
	}
	
	public void intializeControlles()
	{
		llCollectonReport_members		=	(LinearLayout)inflater.inflate(R.layout.collection_member_summary, null);
		
		tvMemberName		=	(TextView)llCollectonReport_members.findViewById(R.id.tvMemberName);
		tvDate				=	(TextView)llCollectonReport_members.findViewById(R.id.tvDate);
		
		tvCenterName		=	(TextView)llCollectonReport_members.findViewById(R.id.tvCenterName);
		tvgroupname			=	(TextView)llCollectonReport_members.findViewById(R.id.tvgroupname);
		tvRegularDemand		=	(TextView)llCollectonReport_members.findViewById(R.id.tvRegularDemand);
		tvODAmt				=	(TextView)llCollectonReport_members.findViewById(R.id.tvODAmt);
		tvTotalDemand		=	(TextView)llCollectonReport_members.findViewById(R.id.tvTotalDemand);
		tvCollectedAmt		=	(TextView)llCollectonReport_members.findViewById(R.id.tvCollectedAmt);
		tvAmttobeCollected	=	(TextView)llCollectonReport_members.findViewById(R.id.tvAmttobeCollected);
		
		btnMenu				=	(Button)llCollectonReport_members.findViewById(R.id.btnMenu);
		btnPrint			=	(Button)llCollectonReport_members.findViewById(R.id.btnPrint);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llCollectonReport_members, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Members Summary");
	}

	@Override
	public PrintDetailsDO getprintObject() 
	{
//		PrintDetailsDO detailsDO = new PrintDetailsDO();
		PrintValues printValues = new PrintValues();
		TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
		ArrayList<RegularDemandsDO> alDos = trnsactionsBL.SelectDistinctTransactioncodeFromCenter(MLAIID,"Member",CollectionReport_MemberSummery.this);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		
			for(int i =0 ; i < alDos.size(); i++ )
			{
				TrnsactionsBL trnsactionsBL1 = new TrnsactionsBL();
				ArrayList<RegularDemandsDO> alArrayList = trnsactionsBL1.SelectAll(alDos.get(i).TransactionCode,MLAIID,"Member",CollectionReport_MemberSummery.this);
				DUPCOUNT=  Integer.valueOf(trnsactionsBL.getDupNoforMember(MLAIID,alDos.get(i).TransactionCode,CollectionReport_MemberSummery.this));
				if(DUPCOUNT<3)
				{
					for(int j=0 ; j <alArrayList.size(); j++ )
					{
						RegularDemandsDO obj = alArrayList.get(i);
						RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
						RegularDemandsDO regularDemandsDO = regularDemandsBL.SelectAll(obj.MLAI_ID, "memeber",CollectionReport_MemberSummery.this).get(0);
						String CollAmt=trnsactionsBL.getCollectedAmtForMember(obj.MLAI_ID,CollectionReport_MemberSummery.this);
						String header=intialParametrsDO.ReceiptHeader;
			            String[] head = header.split("@");
			            String head1=head[0];
			            String head2=head[1];
			            String head3=head[2];
			            printValues.add(head1,"true");
			            String recnum=StringUtils.getRecieptNumber(obj,CollectionReport_MemberSummery.this);
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
						printValues.add("-----------------------------","true");
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
							printValues.add("Next Installment Date:"+regularDemandsDO.NextRepayDate,"false");
						}
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
						printValues.add(" ","false");
						printValues.add(" ","false");
						printValues.add(" ","false");
						printValues.add(" ","false");
						trnsactionsBL.updateDupNO(String.valueOf(DUPCOUNT),obj.MLAI_ID,alDos.get(i).TransactionCode,CollectionReport_MemberSummery.this);
					}
				}
				else
				{
					
				}
				
			}
//		isPrintTaken = true;
		return printValues.getDetailObj();
	}

}
