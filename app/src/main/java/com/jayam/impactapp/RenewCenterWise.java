package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

import com.jayam.impactapp.RenewGroupWise.UpdateRenew;
import com.jayam.impactapp.RenewGroupWise.Updatelistner;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.StringUtils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RenewCenterWise extends Base{

	private LinearLayout llAttendense;
	private Button btnNext;
	private String groupnumber;
	private RadioGroup rgGroup;
	File sdImageMainDirectory;
	ArrayList<RegularDemandsDO> alregularDemandsBL;
	String reason;
	String problem="S";
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		intializeControlles();
		groupnumber	=	getIntent().getExtras().getString("groupnumber");
		final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",RenewCenterWise.this);
		if(alregularDemandsBL.get(0).RenewFeed!=null && alregularDemandsBL.get(0).RenewFeed.equals("AY")){
			rgGroup.check(R.id.rgayes);
			problem="AY";
		}
		else if(alregularDemandsBL.get(0).RenewFeed!=null && alregularDemandsBL.get(0).RenewFeed.equals("SY")){
			rgGroup.check(R.id.rgsyes);
			problem="SY";
		}
		else if(alregularDemandsBL.get(0).RenewFeed!=null && alregularDemandsBL.get(0).RenewFeed.equals("AN")){
			rgGroup.check(R.id.rgano);
			problem="AN";
		}
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg1){
					case R.id.rgbtsel:
						problem="S";
						break;
					case R.id.rgayes:
						problem="AY";
						break;
					case R.id.rgsyes:
						problem="SY";
						break;
					case R.id.rgano:
						problem="AN";
						break;
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
				Intent i = new Intent(RenewCenterWise.this,loginActivity.class);
				startActivity(i);
			//	setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				UpdateRenew updateQom = new UpdateRenew(RenewCenterWise.this, new Updatelistner()
				{
					@Override
					public void onUpdate()
					{
						HideLoader();
						setResult(12345);
						finish();
					}
				});
				if(!problem.equalsIgnoreCase("S")){
					ShowLoader();
					updateQom.start();
				}
				else
					showAlertDailog("Please Select One option");
			}
		});
	}
	
	private void intializeControlles() {
		// TODO Auto-generated method stub
		llAttendense	=	(LinearLayout)inflater.inflate(R.layout.renew_activity, null);
		rgGroup			=	(RadioGroup) llAttendense.findViewById(R.id.rgGroup);
		btnNext			=	(Button)llAttendense.findViewById(R.id.btnNext);
		svBase.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
		tvHeader.setText("Renew");
		btnNext.setText("Save");
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
	
	class UpdateRenew extends Thread
	{
		Context context ;
		Updatelistner updatelistner;
		public UpdateRenew(Context context , Updatelistner updatelistner)
		{
			this.context = context;
			this.updatelistner = updatelistner;
		}
		@Override
		public void run()
		{
			super.run();
			RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
			RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
			regularDemandsBL.updateRenew(groupnumber, problem, "Group",RenewCenterWise.this);
			regularDemandsBLTemp.updateRenew(groupnumber, problem, "Group",RenewCenterWise.this);
			if(btnNext.getText().toString().equalsIgnoreCase("Save")){
				regularDemandsBL.updatesavefb(groupnumber,"group",RenewCenterWise.this);
			}
			((Activity)context).runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					updatelistner.onUpdate();
				}
			});
			
		}
	}

	interface Updatelistner 
	{
		public abstract void onUpdate();
	}
}
