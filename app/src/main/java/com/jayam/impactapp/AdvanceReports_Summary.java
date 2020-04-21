package com.jayam.impactapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.common.PrintValues;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.PrintDetailsDO;
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

public class AdvanceReports_Summary extends Base implements PrintListner
{
	private LinearLayout llAck;
	private TextView tvRCenterName,tvDemandDate,tvTotalOSAmt,tvTotalPreclosureIntrest,tvNoofMembers,tvCollectedAmount,tvAmounttobeCollected;
	private Button btnGroupMenu, btnPrint;
	private AdvanceDemandBL trnsactionsBL;
	private ArrayList<AdvaceDemandDO> alArrayList;
	private float OSAmt=0,PreInt=0,CollAmt=0,AmtTobeColl=0;
	private String dateNow;
	ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	private IntialParametrsBL intialParametrsBL;
	int Count=0;
	String uName;
	
	@Override
	public void initialize()
	{
		uName = SharedPrefUtils.getKeyValue(AdvanceReports_Summary.this, AppConstants.pref_name, AppConstants.username);
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter=  new SimpleDateFormat("dd-MM-yyyy");
		 dateNow = formatter.format(currentDate.getTime());
		intializeControlles();
		trnsactionsBL = new AdvanceDemandBL();
		alArrayList = trnsactionsBL.SelectReportsData("","Summary",AdvanceReports_Summary.this);
		intialParametrsBL = new  IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(AdvanceReports_Summary.this);
		tvRCenterName.setText(""+dateNow);
		tvDemandDate.setText(""+uName);
		for(AdvaceDemandDO Obj : alArrayList)
		{
			OSAmt=OSAmt+Float.valueOf(Obj.OS);
			PreInt=PreInt+Float.valueOf(Obj.OSAmt);
			float previous = 0;
			String preAmt=Obj.previousAmt;
			if(preAmt == null)
			{
				previous=0;
			}
			else
			{
				Count=Count+1;
				previous=Float.valueOf(Obj.previousAmt.trim()).floatValue();
			}
			CollAmt=CollAmt+previous;
			AmtTobeColl=AmtTobeColl+Float.valueOf(Obj.OSAmt)-previous;
		}
		tvNoofMembers.setText(""+Count);
		tvTotalOSAmt.setText(""+OSAmt);
		tvTotalPreclosureIntrest.setText(""+PreInt);
		tvCollectedAmount.setText(""+CollAmt);
		tvAmounttobeCollected.setText(""+AmtTobeColl);
		btnPrint.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				PrintUtils printUtils = new PrintUtils(AdvanceReports_Summary.this, AdvanceReports_Summary.this);
				printUtils.print();
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
				Intent i = new Intent(AdvanceReports_Summary.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
		
		btnGroupMenu.setOnClickListener(new OnClickListener()
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
		llAck						=	(LinearLayout)inflater.inflate(R.layout.advancereports_centerdetails, null);
		tvRCenterName				=	(TextView)llAck.findViewById(R.id.tvRCenterName);
		tvDemandDate				=	(TextView)llAck.findViewById(R.id.tvDemandDate);
		tvTotalOSAmt				=	(TextView)llAck.findViewById(R.id.tvTotalOSAmt);
		tvTotalPreclosureIntrest	=	(TextView)llAck.findViewById(R.id.tvTotalPreclosureIntrest);
		tvNoofMembers				=	(TextView)llAck.findViewById(R.id.tvNoofMembers);
		tvCollectedAmount			=	(TextView)llAck.findViewById(R.id.tvCollectedAmount);
		tvAmounttobeCollected		=	(TextView)llAck.findViewById(R.id.tvAmounttobeCollected);
		btnGroupMenu				=	(Button)llAck.findViewById(R.id.btnGroupMenu);
		btnPrint					=	(Button)llAck.findViewById(R.id.btnPrint);
//		btnGroupMenu.setVisibility(View.GONE);
		btnGroupMenu.setText("Main Menu");
		TextView CTGName			=	(TextView)llAck.findViewById(R.id.ADVCenterName);
		CTGName.setText("Date");
		TextView AgentName			=	(TextView)llAck.findViewById(R.id.agentName);
		AgentName.setText("Agent Name");
		TextView CollRec			=	(TextView)llAck.findViewById(R.id.CollectedRecords);
		CollRec.setText("Collection A/C's");
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAck, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Advance Summary Report");
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
		printValues.add("Advance TXN Acknowledgement","true");
		printValues.add("---------------------------","true");
		printValues.add(" ","false");
		printValues.add("Date: "+dateNow,"false");
		printValues.add("Agent Name: "+uName,"false");
		printValues.add("Total OS Amt: "+OSAmt,"false");
		printValues.add("Total PreclosureInterest : "+PreInt,"false");
		printValues.add("collection A/C's : "+Count,"false");
		printValues.add("Collected Amount : "+CollAmt,"false");
		printValues.add("Amount to be Collected: "+AmtTobeColl,"false");
		printValues.add(" ","false");
		printValues.add(intialParametrsDO.ReceiptFooter,"true");
		printValues.add(" ","false");
		printValues.add(" ","false");
		return printValues.getDetailObj();
	}

}
