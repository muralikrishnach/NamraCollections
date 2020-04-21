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


import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportDetailSummaryForRegular extends Base implements PrintListner
{
	private TextView tvcollectionDt, tvAgent, tvtotalodacs, tvtotalodamount, tvdemandacs, tvDemandAmount, 
	tvCollectionacs, tvCollectionAmount,tvUnPaidAcs, tvbaltobereceived, tvattendace,tvRepayment, tvODCollectedAcounts,
	tvODCollectedAmount,tvNoOfAdvanceacs, tvAdvanceAmount, tvPartialPaidAcs;
	private LinearLayout llReportSummary;
	private Button btnMenu, btnPrint;
	private ArrayList<RegularDemandsDO> alDemandsDOs;
	private float totalODAmount;
	private RegularDemandsBL regularDemandsBL;
	private TrnsactionsBL trnsactionsBL;
	
	
	private RegularDemandsDO obj;
	private String userID;
	private String totalODAcount;
	private String totamt;
	private String totalDemandAmount;
	private String countofCollectionAcounts;
	private String totalCollectedAmount;
	private float balToBereceived;
	private String totalAttendense;
	private float repaymentPercentage;
	private String odCollectedAcounts;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	@Override
	public void initialize() 
	{
		intializeControlles();
		
		IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(ReportDetailSummaryForRegular.this);
		
		regularDemandsBL = new RegularDemandsBL();
		alDemandsDOs = regularDemandsBL.SelectAll(ReportDetailSummaryForRegular.this);
		
		trnsactionsBL = new TrnsactionsBL();
		obj = alDemandsDOs.get(0);
		
		tvcollectionDt.setText(""+obj.DemandDate);
		
		//get user name from preferences
		userID = SharedPrefUtils.getKeyValue(ReportDetailSummaryForRegular.this, AppConstants.pref_name, AppConstants.username);
		
		tvAgent.setText(""+userID);
		
		
		// total noof od accounts
		totalODAcount = regularDemandsBL.getTotalODAcounts(ReportDetailSummaryForRegular.this);
		tvtotalodacs.setText(""+totalODAcount);
		
		//get total OD Amount from table
		
		totamt = regularDemandsBL.getSumOfODAcounts(ReportDetailSummaryForRegular.this);
		totalODAmount = Float.valueOf(totamt).floatValue();
		tvtotalodamount.setText(""+totalODAmount);
		
		tvdemandacs.setText(""+alDemandsDOs.size());
		
		totalDemandAmount = regularDemandsBL.getTOTALDemandAmount(ReportDetailSummaryForRegular.this);
		tvDemandAmount.setText(""+Float.valueOf(totalDemandAmount).floatValue());
		
		// get count od collection accounts
		countofCollectionAcounts = trnsactionsBL.getCountofCollectionAccounts(ReportDetailSummaryForRegular.this);
		tvCollectionacs.setText(""+countofCollectionAcounts);
		
		// get total collected amount
		totalCollectedAmount = trnsactionsBL.getTotalCollectionAmount(ReportDetailSummaryForRegular.this);
		tvCollectionAmount.setText(""+Float.valueOf(totalCollectedAmount).floatValue());
		
		tvUnPaidAcs.setText(""+(alDemandsDOs.size()-Integer.parseInt(countofCollectionAcounts) ));
		
		
		balToBereceived = Float.valueOf(totalDemandAmount).floatValue() - Float.valueOf(totalCollectedAmount).floatValue();
		tvbaltobereceived.setText(""+balToBereceived);
		
		//gettotal attendense
		totalAttendense = trnsactionsBL.getTotalAttendense(ReportDetailSummaryForRegular.this);
		tvattendace.setText(""+totalAttendense + " / "+ alDemandsDOs.size());
		
		
		//caliculate repayment percentage
		
		/**
		 * 	totamt - collected / total amount * 100
		 * 
		 */
		
		float s = (Float.valueOf(totalDemandAmount).floatValue() - Float.valueOf(totalCollectedAmount).floatValue())/ Float.valueOf(totalDemandAmount).floatValue()* 100;
		
		repaymentPercentage = 100-s;
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		tvRepayment.setText(""+df.format(repaymentPercentage) + " %");
		
		
		// get od collected acounts
		odCollectedAcounts = regularDemandsBL.getNoOfODCollectedAcounts(ReportDetailSummaryForRegular.this);
		tvODCollectedAcounts.setText(""+odCollectedAcounts);
		
		
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				PrintUtils printUtils = new PrintUtils(ReportDetailSummaryForRegular.this, ReportDetailSummaryForRegular.this);
				printUtils.print();
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
	
	public void intializeControlles()
	{
		llReportSummary			=	(LinearLayout)inflater.inflate(R.layout.reports_detail_ummaryfor_od, null);
		
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
		tvODCollectedAcounts	=	(TextView)llReportSummary.findViewById(R.id.tvODCollectedAcounts);
		tvODCollectedAmount		=	(TextView)llReportSummary.findViewById(R.id.tvODCollectedAmount);
		tvNoOfAdvanceacs		=	(TextView)llReportSummary.findViewById(R.id.tvNoOfAdvanceacs);
		tvAdvanceAmount			=	(TextView)llReportSummary.findViewById(R.id.tvAdvanceAmount);
		tvPartialPaidAcs		=	(TextView)llReportSummary.findViewById(R.id.tvPartialPaidAcs);
		
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
		printValues.add("Detailed Summary Report","true");
		printValues.add("collection Date :"+obj.DemandDate,"false");
		printValues.add("Agent :"+userID,"false");
		printValues.add("Total OD A/c's :"+odCollectedAcounts,"false");
		printValues.add("Total OD Amount :"+totalODAcount,"false");
		printValues.add("Total Demand A/c's :",""+alDemandsDOs.size()+"false");
		printValues.add("Total Demand Amount :"+Float.valueOf(totalDemandAmount).floatValue(),"false");
		printValues.add("Regular collection A/C's :"+countofCollectionAcounts,"false");
		printValues.add("Regular collection Amount: "+Float.valueOf(totalCollectedAmount).floatValue(),"false");
		printValues.add("UnPaid A/C's :"+(alDemandsDOs.size()-Integer.parseInt(countofCollectionAcounts) ),"false");
		printValues.add("Bal. to be received :"+balToBereceived,"false");
		printValues.add("Attendance :"+totalAttendense + " / "+ alDemandsDOs.size(),"false");
		
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		
		printValues.add("Repayment % :"+df.format(repaymentPercentage) + " %","false");
		printValues.add("OD Collected A/C's :"+odCollectedAcounts,"false");
		printValues.add("OD Collected Amount :"+0,"false");
		printValues.add("Advance A/C's :"+0,"false");
		printValues.add("Advance Amount :"+0,"false");
		printValues.add("Partial Paid A/C's :"+0,"false");
		printValues.add(" ","false");
		printValues.add(intialParametrsDO.ReceiptFooter,"true");
		printValues.add(" ","false");
		printValues.add(" ","false");
		printValues.add(" ","false");
		printValues.add(" ","false");
		
		return printValues.getDetailObj();
	}
}
