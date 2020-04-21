package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.utils.PrintUtils;
import com.jayam.impactapp.utils.StringUtils;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectionReport_MemberDetails_OD extends Base implements PrintListner
{
	private LinearLayout llAck;
	private TextView tvCenterName, tvGroupName, tvMemberName, tvMemberCode, tvloanAc, tvODDemand, tvODCollectedAmt, tvAmtCollected;
	private String MLAI_ID;
	private Transaction_OD_BL transaction_OD_BL;
	private ArrayList<ODDemandsDO> alOdDemandsDOs;
	private ODDemandsBL odDemandsBL;
	private ODDemandsDO odDemandsDO;
	private String ODAmount ;
	private float AmountTobeCollected;
	private Button btnPrint, btnMenu;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;;
	@Override
	public void initialize() 
	{
		//mlai_id
		MLAI_ID		=	getIntent().getExtras().getString("mlai_id");
		intializeControlles();
		
		IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(CollectionReport_MemberDetails_OD.this);
		
		
		transaction_OD_BL 	=	 new Transaction_OD_BL();
		odDemandsBL			=	 new ODDemandsBL();
		ODAmount 			=	 odDemandsBL.getODAmt(MLAI_ID,CollectionReport_MemberDetails_OD.this);
		
		
		alOdDemandsDOs		=	 transaction_OD_BL.GetMemberDetails(MLAI_ID,CollectionReport_MemberDetails_OD.this);
		odDemandsDO 		=	 alOdDemandsDOs.get(0);
		
		tvCenterName.setText(""+odDemandsDO.MCI_Name);
		tvGroupName.setText(""+odDemandsDO.MGI_Name);
		tvMemberName.setText(""+odDemandsDO.MMI_Name);
		tvMemberCode.setText(""+odDemandsDO.MMI_Code);
		tvloanAc.setText(""+odDemandsDO.MLAI_ID);
		tvODDemand.setText(""+ODAmount);
		tvODCollectedAmt.setText(""+odDemandsDO.collectedAmt);
		
		AmountTobeCollected = Float.valueOf(ODAmount).floatValue() - Float.valueOf(odDemandsDO.collectedAmt).floatValue();
		tvAmtCollected.setText(""+AmountTobeCollected);
		
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
				Intent i = new Intent( CollectionReport_MemberDetails_OD.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
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
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				PrintUtils printUtils = new PrintUtils(CollectionReport_MemberDetails_OD.this, CollectionReport_MemberDetails_OD.this);
				printUtils.print();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llAck				=	(LinearLayout)inflater.inflate(R.layout.collection_reports_memberdetails_od, null);
		tvCenterName		=	(TextView)llAck.findViewById(R.id.tvCenterName);
		tvGroupName			=	(TextView)llAck.findViewById(R.id.tvGroupName);
		tvMemberName		=	(TextView)llAck.findViewById(R.id.tvMemberName);
		tvMemberCode		=	(TextView)llAck.findViewById(R.id.tvMemberCode);
		tvloanAc			=	(TextView)llAck.findViewById(R.id.tvloanAc);
		tvODDemand			=	(TextView)llAck.findViewById(R.id.tvODDemand);
		tvODCollectedAmt	=	(TextView)llAck.findViewById(R.id.tvODCollectedAmt);
		tvAmtCollected		=	(TextView)llAck.findViewById(R.id.tvAmtCollected);
		
		btnPrint			=	(Button)llAck.findViewById(R.id.btnPrint);
		btnMenu			=	(Button)llAck.findViewById(R.id.btnMenu);
		
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Member Details");
	}

	@Override
	public PrintDetailsDO getprintObject()
	{
		PrintValues printValues = new PrintValues();
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		String header=intialParametrsDO.ReceiptHeader;
//		Log.d("mfimo", header);
        String[] head = header.split("@");
        String head1=head[0];
        String head2=head[1];
        String head3=head[2];
//        Log.d("mfimo", reciptNum);
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
		printValues.add("                            ","");
		printValues.add("Duplicate TXN Acknowledgement","true");
		printValues.add("---------------------------","true");
		printValues.add(" ","false");
		printValues.add("Center :"+odDemandsDO.MCI_Name,"false");
		printValues.add("Group :"+odDemandsDO.MGI_Name,"false");
		printValues.add("Member :"+odDemandsDO.MMI_Name+"("+odDemandsDO.MMI_Code+")","false");
		printValues.add("Loan A/C No :"+odDemandsDO.MLAI_ID,"false");
		printValues.add("OD Amt :"+ODAmount,"false");
		printValues.add("Collected Amt :"+""+Float.valueOf(odDemandsDO.collectedAmt).floatValue(),"false");
		printValues.add("Amt To be Collected :"+AmountTobeCollected,"false");
		printValues.add("                            ","");
		printValues.add(""+intialParametrsDO.ReceiptFooter,"true");
		printValues.add("                            ","");
		printValues.add("                            ","");
		printValues.add("                            ","");
		printValues.add("                            ","");
		return printValues.getDetailObj();
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
}
