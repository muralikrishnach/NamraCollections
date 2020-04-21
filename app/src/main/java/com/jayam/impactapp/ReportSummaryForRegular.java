package com.jayam.impactapp;

import java.text.DecimalFormat;
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


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportSummaryForRegular extends Base implements PrintListner
{
	private TextView tvcollectionDt, tvAgent, tvtotalodacs, tvtotalodamount, tvdemandacs, tvDemandAmount, 
	tvCollectionacs, tvCollectionAmount,tvUnPaidAcs, tvbaltobereceived, tvattendace,tvRepayment;
	private LinearLayout llReportSummary;
	private Button btnMenu, btnPrint;
	private ArrayList<RegularDemandsDO> alDemandsDOs;
	private float totalODAmount;
	private RegularDemandsBL regularDemandsBL;
	private TrnsactionsBL trnsactionsBL;
	RegularDemandsDO obj ;
	String userID;
	String totalODAcount;
	String totalDemandAmount;
	String countofCollectionAcounts;
	String totalCollectedAmount;
	float balToBereceived;
	String totalAttendense;
	float repaymentPercentage;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	@Override
	public void initialize() 
	{
		intializeControlles();
		
		IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(ReportSummaryForRegular.this);
		
		regularDemandsBL = new RegularDemandsBL();
		alDemandsDOs = regularDemandsBL.SelectAll(ReportSummaryForRegular.this);
		
		trnsactionsBL = new TrnsactionsBL();
		obj = alDemandsDOs.get(0);
		
		tvcollectionDt.setText(""+obj.DemandDate);
		
		//get user name from preferences
		 userID = SharedPrefUtils.getKeyValue(ReportSummaryForRegular.this, AppConstants.pref_name, AppConstants.username);
		
		tvAgent.setText(""+userID);
		
		
		// total noof od accounts
		totalODAcount = regularDemandsBL.getTotalODAcounts(ReportSummaryForRegular.this);
		tvtotalodacs.setText(""+totalODAcount);
		
		//get total OD Amount from table
		
		String totamt = regularDemandsBL.getSumOfODAcounts(ReportSummaryForRegular.this);
		totalODAmount = Float.valueOf(totamt).floatValue();
		tvtotalodamount.setText(""+totalODAmount);
		
		tvdemandacs.setText(""+alDemandsDOs.size());
		
		 totalDemandAmount = regularDemandsBL.getTOTALDemandAmount(ReportSummaryForRegular.this);
		tvDemandAmount.setText(""+Float.valueOf(totalDemandAmount).floatValue());
		
		// get count od collection accounts
		countofCollectionAcounts = trnsactionsBL.getCountofCollectionAccounts(ReportSummaryForRegular.this);
		tvCollectionacs.setText(""+countofCollectionAcounts);
		
		// get total collected amount
		totalCollectedAmount = trnsactionsBL.getTotalCollectionAmount(ReportSummaryForRegular.this);
		tvCollectionAmount.setText(""+Float.valueOf(totalCollectedAmount).floatValue());
		
		tvUnPaidAcs.setText(""+(alDemandsDOs.size()-Integer.parseInt(countofCollectionAcounts) ));
		
		
		balToBereceived = Float.valueOf(totalDemandAmount).floatValue() - Float.valueOf(totalCollectedAmount).floatValue();
		if(balToBereceived<=0)
		{
			balToBereceived=0;	
		}
		else
		{
			
		}
		tvbaltobereceived.setText(""+balToBereceived);
		
		//gettotal attendense
		totalAttendense = trnsactionsBL.getTotalAttendense(ReportSummaryForRegular.this);
		tvattendace.setText(""+totalAttendense + " / "+ alDemandsDOs.size());
		
		
		//caliculate repayment percentage
		
		/**
		 * 	totamt - collected / total amount * 100
		 * 
		 */
		
		float s = (Float.valueOf(totalDemandAmount).floatValue() - Float.valueOf(totalCollectedAmount).floatValue())/ Float.valueOf(totalDemandAmount).floatValue()* 100;
		
		repaymentPercentage=100-s;
		
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		tvRepayment.setText(""+df.format(repaymentPercentage) + " %");
		btnMenu.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setResult(AppConstants.RESULTCODE_HOME);
				finish();
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
				Intent i = new Intent(ReportSummaryForRegular.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
		
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				PrintUtils printUtils = new PrintUtils(ReportSummaryForRegular.this, ReportSummaryForRegular.this);
				printUtils.print();
			}
		});
	}
	
	public void intializeControlles()
	{
		llReportSummary			=	(LinearLayout)inflater.inflate(R.layout.reportsummaryfor_od, null);
		
		tvcollectionDt			=	(TextView)llReportSummary.findViewById(R.id.tvcollectionDt);
		tvAgent					=	(TextView)llReportSummary.findViewById(R.id.tvAgent);
		tvtotalodacs			=	(TextView)llReportSummary.findViewById(R.id.tvtotalodacs);
		tvtotalodamount			=	(TextView)llReportSummary.findViewById(R.id.tvtotalodamount);
		tvdemandacs				=	(TextView)llReportSummary.findViewById(R.id.tvdemandacs);
		tvDemandAmount			=	(TextView)llReportSummary.findViewById(R.id.tvDemandAmount);
		tvCollectionacs			=	(TextView)llReportSummary.findViewById(R.id.tvCollectionacs);
		tvCollectionAmount		=	(TextView)llReportSummary.findViewById(R.id.tvCollectionAmount);
		tvUnPaidAcs				=	(TextView)llReportSummary.findViewById(R.id.tvUnPaidAcs);
		tvbaltobereceived		=	(TextView)llReportSummary.findViewById(R.id.tvbaltobereceived);
		tvattendace				=	(TextView)llReportSummary.findViewById(R.id.tvattendace);
		tvRepayment				=	(TextView)llReportSummary.findViewById(R.id.tvRepayment);
		btnMenu					=	(Button)llReportSummary.findViewById(R.id.btnMenu);
		btnPrint				=	(Button)llReportSummary.findViewById(R.id.btnPrint);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llReportSummary, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Summary Report");
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
		printValues.add("Summary Report","true");
		printValues.add("collection Date :"+obj.DemandDate,"false");
		printValues.add(" ","false");
		printValues.add("Agent :"+userID,"false");
		printValues.add("Total OD A/C's :"+totalODAcount,"false");
		printValues.add("Total OD Amt :"+totalODAmount,"false");
		printValues.add("Total Demand A/c's :"+alDemandsDOs.size(),"false");
		printValues.add("Total Demand Amt :"+Float.valueOf(totalDemandAmount).floatValue(),"false");
		printValues.add("collection A/c's :"+countofCollectionAcounts,"false");
		printValues.add("collection Amount :"+totalCollectedAmount,"false");
		printValues.add("UnPaid A/c's :"+(alDemandsDOs.size()-Integer.parseInt(countofCollectionAcounts) ),"false");
		printValues.add("Bal to be received  :"+balToBereceived,"false");
		printValues.add("Attendance :"+totalAttendense,"false");
		
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		
		printValues.add("Repayment :"+""+df.format(repaymentPercentage) + " %","false");
		
		printValues.add(" ","false");
		printValues.add(intialParametrsDO.ReceiptFooter,"true");
		printValues.add(" ","false");
		printValues.add(" ","false");
		printValues.add(" ","false");
		printValues.add(" ","false");
		return printValues.getDetailObj();
	}


}
