package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;


import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class InitialParameters extends Base{

	private IntialParametrsBL InitialParamsBL;
	private IntialParametrsDO InitialParamsDO;
	
	EditText txtworkmode,txtterminalid,txtmac,txtheader,txtfooter,txtBTAddress,txtoffset,txttimeout,
	txturl,txtmaxtxns,txtmaxamt,txtusername,txttxnid,txtadvdemands;
	
	CheckBox chkacopy,chkmtime,chkpartial,chkadv,chkattn,chkindividual,chkgli,chklateness;
	
	@Override
	public void initialize() {
		intializeControlles();
		//ProgressDialog progressDialog = ProgressDialog.show(this, "", "messagr");
		InitialParamsBL = new IntialParametrsBL();
		ArrayList<IntialParametrsDO> alArrayList = InitialParamsBL.SelectAll(InitialParameters.this);
		InitialParamsDO = alArrayList.get(0);
				
		txtworkmode.setText(InitialParamsDO.WorkMode);
		txtterminalid.setText(InitialParamsDO.TerminalID);
		txtmac.setText(InitialParamsDO.MACID);
		txtheader.setText(InitialParamsDO.ReceiptHeader);
		txtfooter.setText(InitialParamsDO.ReceiptFooter);
		txtBTAddress.setText(InitialParamsDO.BTPrinterAddress);
		
		if(InitialParamsDO.AgentCopy.equalsIgnoreCase("1"))
		{
		  chkacopy.setChecked(true);
		}
		else
		{
			chkacopy.setChecked(false);
		}
		txtoffset.setText(InitialParamsDO.DaysOffSet);
		if(InitialParamsDO.MeetingTime.equalsIgnoreCase("1"))
		{
		   chkmtime.setChecked(true);
		}
		else
		{
		   chkmtime.setChecked(false);
		}
		txttimeout.setText(InitialParamsDO.TimeOut);
		txturl.setText(InitialParamsDO.ServerUrl);
		txtmaxtxns.setText(InitialParamsDO.MaxTransactions);
		txtmaxamt.setText(InitialParamsDO.MaxAmount);
		txtusername.setText(InitialParamsDO.UserName);
		txttxnid.setText(InitialParamsDO.LastTransactionID);
		if(InitialParamsDO.PartialPayment.equalsIgnoreCase("1"))
		{
		    chkpartial.setChecked(true);
		}
		else
		{
			chkpartial.setChecked(false);
		}
		
		if(InitialParamsDO.AdvPayment.equalsIgnoreCase("1"))
		{
		    chkadv.setChecked(true);
		}
		else
		{
			chkadv.setChecked(false);
		}
		txtadvdemands.setText(InitialParamsDO.AdvDemandDwds);
		
		if(InitialParamsDO.MemberAttendance.equalsIgnoreCase("1"))
		{
		    chkattn.setChecked(true);
		}
		else
		{
			chkattn.setChecked(false);
		}
		
		if(InitialParamsDO.IndividualReceipts.equalsIgnoreCase("1"))
		{
		    chkindividual.setChecked(true);
		}
		else
		{
			chkindividual.setChecked(false);
		}
		
		if(InitialParamsDO.GLI.equalsIgnoreCase("1"))
		{
		    chkgli.setChecked(true);
		}
		else
		{
			chkgli.setChecked(false);
		}
		
		if(InitialParamsDO.Lateness.equalsIgnoreCase("1"))
		{
		    chklateness.setChecked(true);
		}
		else
		{
			chklateness.setChecked(false);
		}
		
	}

	
	public void intializeControlles()
	{
		LinearLayout llparameters = (LinearLayout) inflater.inflate(R.layout.resetparameters, null);
		txtworkmode = (EditText)findViewById(R.id.txtworkmode);
		txtterminalid = (EditText)findViewById(R.id.txtterminalid);
		txtmac = (EditText)findViewById(R.id.txtmac);
		txtheader = (EditText)findViewById(R.id.txtheader);
		txtfooter = (EditText)findViewById(R.id.txtfooter);
		txtBTAddress = (EditText)findViewById(R.id.txtBTAddress);
		chkacopy = (CheckBox)findViewById(R.id.chkacopy);
		txtoffset = (EditText)findViewById(R.id.txtoffset);
		chkmtime = (CheckBox)findViewById(R.id.chkmtime);
		txttimeout = (EditText)findViewById(R.id.txttimeout);
		txturl = (EditText)findViewById(R.id.txturl);	
		txtmaxtxns = (EditText)findViewById(R.id.txtmaxtxns);
		txtmaxamt = (EditText)findViewById(R.id.txtmaxamt);
		txtusername = (EditText)findViewById(R.id.txtusername);
		txttxnid = (EditText)findViewById(R.id.txttxnid);
		chkpartial = (CheckBox)findViewById(R.id.chkpartial);
		chkadv = (CheckBox)findViewById(R.id.chkadv);
		txtadvdemands = (EditText)findViewById(R.id.txtadvdemands);
		chkattn = (CheckBox)findViewById(R.id.chkattn);
		chkindividual = (CheckBox)findViewById(R.id.chkindividual);
		chkgli = (CheckBox)findViewById(R.id.chkgli);
		chklateness = (CheckBox)findViewById(R.id.chklateness);
		tvHeader.setText("Initial Parameters");
	}

	
}

