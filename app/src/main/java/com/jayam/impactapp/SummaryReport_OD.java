package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.utils.PrintUtils;
import com.jayam.impactapp.utils.SharedPrefUtils;


import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SummaryReport_OD extends Base implements PrintListner
{
	private LinearLayout llSummaryreport;
	private TextView tvDate, tvAgentName, tvTotalDemandAcs, tvTotalDemandAmount, tvCollectedAcs, tvCollectedAmount, tvUnPaidAcs, tvbalAmt;
	private Transaction_OD_BL transaction_OD_BL;
	private ODDemandsBL odDemandsBL;
	private Button btnPrint, btnMenu;
	private ArrayList<String> alSummaryReportDetails, alSummaryreportDetails_transaction;
	private int unpaidAcounts;
	private  float balAmount;
	private String userID;
	private String totalDemandAcounts, totalDemandAmount, totalCollectedDemandAcounts, totalCollectedAmount,  date ;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	private IntialParametrsBL intialParametrsBL;
	
	@Override
	public void initialize()
	{
		intializeControlles();
		
		odDemandsBL = new ODDemandsBL();
		transaction_OD_BL = new Transaction_OD_BL();
		intialParametrsBL = new  IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(SummaryReport_OD.this);
		// get Details from OdDemands table
		alSummaryReportDetails = odDemandsBL.getDetailsForSummaryReport(SummaryReport_OD.this);
		alSummaryreportDetails_transaction = transaction_OD_BL.getDetailsForSummaryReport(SummaryReport_OD.this);
		if(alSummaryreportDetails_transaction != null && alSummaryreportDetails_transaction.get(0) != null && alSummaryreportDetails_transaction.get(0).equalsIgnoreCase("0"))
		{
			showAlertDailog("No Demands Available.", "OK", null, new CustomDailoglistner() 
			{
				@Override
				public void onPossitiveButtonClick(DialogInterface dialog)
				{
					dialog.dismiss();
					finish();
				}
				
				@Override
				public void onNegativeButtonClick(DialogInterface dialog) 
				{
					
				}
			});
		}
		else
		{
			totalDemandAcounts = alSummaryReportDetails.get(0);
			totalDemandAmount  = alSummaryReportDetails.get(1);
			
			tvTotalDemandAcs.setText(""+totalDemandAcounts);
			tvTotalDemandAmount.setText(""+totalDemandAmount);
			
			// get Details from Transaction Table
			
			
			
			totalCollectedDemandAcounts = alSummaryreportDetails_transaction.get(0);
			totalCollectedAmount = alSummaryreportDetails_transaction.get(1);
			date = alSummaryreportDetails_transaction.get(2);
			tvCollectedAcs.setText(""+totalCollectedDemandAcounts);
			tvCollectedAmount.setText(""+totalCollectedAmount);
			
			// caliculate the unpaid acounts and bal amount
			unpaidAcounts = Integer.parseInt(alSummaryReportDetails.get(0)) - Integer.parseInt(alSummaryreportDetails_transaction.get(0));
			balAmount   = Float.valueOf(alSummaryReportDetails.get(1)).floatValue() - Float.valueOf(alSummaryreportDetails_transaction.get(1)).floatValue();
			
			tvUnPaidAcs.setText(""+unpaidAcounts);
			tvbalAmt.setText(""+balAmount);
			
			// get user id
			userID = SharedPrefUtils.getKeyValue(SummaryReport_OD.this, AppConstants.pref_name, AppConstants.username);
			tvAgentName.setText(""+userID);
			tvDate.setText(date);
	
		}
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				PrintUtils printUtils = new PrintUtils(SummaryReport_OD.this, SummaryReport_OD.this);
				printUtils.print();
			}
		});
	}
	
	public void intializeControlles()
	{
		llSummaryreport				=	(LinearLayout)inflater.inflate(R.layout.collection_reports_summary_od, null);
		tvDate						=	(TextView)llSummaryreport.findViewById(R.id.tvDate);
		tvAgentName					=	(TextView)llSummaryreport.findViewById(R.id.tvAgentName);
		
		tvTotalDemandAcs			=	(TextView)llSummaryreport.findViewById(R.id.tvTotalDemandAcs);
		tvTotalDemandAmount			=	(TextView)llSummaryreport.findViewById(R.id.tvTotalDemandAmount);
		
		tvCollectedAcs				=	(TextView)llSummaryreport.findViewById(R.id.tvCollectedAcs);
		tvCollectedAmount			=	(TextView)llSummaryreport.findViewById(R.id.tvCollectedAmount);
		
		tvUnPaidAcs					=	(TextView)llSummaryreport.findViewById(R.id.tvUnPaidAcs);
		tvbalAmt					=	(TextView)llSummaryreport.findViewById(R.id.tvbalAmt);
		
		btnPrint					=	(Button)llSummaryreport.findViewById(R.id.btnPrint);
		btnMenu						=	(Button)llSummaryreport.findViewById(R.id.btnMenu);
		
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llSummaryreport, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		
		tvHeader.setText("Summary report");
	}

	@Override
	public PrintDetailsDO getprintObject()
	{
		PrintValues printValues = new PrintValues();
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
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
		printValues.add("OD Summary Report","true");
		printValues.add("----------------------------","true");
		printValues.add(" ","false");
		printValues.add("Date: "+date,"false");
		printValues.add("Agent Name :"+userID,"false");
		printValues.add("Total Demand A/C's : "+totalDemandAcounts,"false");
		printValues.add("Total Demand Amount : "+totalDemandAmount,"false");
		printValues.add("Collected A/C's : "+totalCollectedDemandAcounts,"false");
		printValues.add("Collected Amount : "+totalCollectedAmount,"false");
		printValues.add("UnPaid A/C's : "+unpaidAcounts,"false");
		printValues.add("Bal Amt Tobe received : "+balAmount,"false");
		printValues.add(" ","false");
		printValues.add(intialParametrsDO.ReceiptFooter,"true");
		return printValues.getDetailObj();
	}

}
