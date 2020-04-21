package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

import com.jayam.impactapp.CollectionPlace.UpdateCollPlace;
import com.jayam.impactapp.CollectionPlace.Updatelistner;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.StringUtils;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RepaymentMadeBy extends Base {

	private LinearLayout llAttendense;
	private Button btnNext;
	private RadioGroup rgGroup;
	private String memcode,memname;
	ArrayList<RegularDemandsDO> alregularDemandsBL;
	String problem="S";
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		intializeControlles();
		memcode=getIntent().getExtras().getString("memcode");
		memname=getIntent().getExtras().getString("memname");
		final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectAll(memcode, "memeber",RepaymentMadeBy.this);
//		Log.d("mfimo", alregularDemandsBL.get(0).repaymentMadeBy);
		if(alregularDemandsBL.get(0).repaymentMadeBy!=null && alregularDemandsBL.get(0).repaymentMadeBy.equals("A")){
			rgGroup.check(R.id.rgbtapp);
			problem="A";
		}
		else if(alregularDemandsBL.get(0).repaymentMadeBy!=null && alregularDemandsBL.get(0).repaymentMadeBy.equals("G")){
			rgGroup.check(R.id.rgbtgua);
			problem="G";
		}
		else if(alregularDemandsBL.get(0).repaymentMadeBy!=null && alregularDemandsBL.get(0).repaymentMadeBy.equals("C")){
			rgGroup.check(R.id.rgbtcoapp);
			problem="C";
		}
		else if(alregularDemandsBL.get(0).repaymentMadeBy!=null && alregularDemandsBL.get(0).repaymentMadeBy.equals("O")){
			rgGroup.check(R.id.rgbtot);
			problem="O";
		}
        rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg1){
					case R.id.rgbtsel:
						problem="S";
						break;
					case R.id.rgbtapp:
						problem="A";
						break;
					case R.id.rgbtgua:
						problem="G";
						break;
					case R.id.rgbtcoapp:
						problem="C";
						break;
					case R.id.rgbtot:
						problem="O";
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
				Intent i = new Intent(RepaymentMadeBy.this,loginActivity.class);
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
				UpdateRepay updateQom = new UpdateRepay(RepaymentMadeBy.this, new Updatelistner()
				{
					@Override
					public void onUpdate()
					{
						HideLoader();
						setResult(123456);
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
		llAttendense	=	(LinearLayout)inflater.inflate(R.layout.repaymentmadeby, null);
		btnNext			=	(Button)llAttendense.findViewById(R.id.btnNext);
		rgGroup			=	(RadioGroup)llAttendense.findViewById(R.id.rggroup);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		btnNext.setText("Save");
		tvHeader.setText("Repayment Madeby");
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

	class UpdateRepay extends Thread
	{
		Context context ;
		Updatelistner updatelistner;
		public UpdateRepay(Context context , Updatelistner updatelistner)
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
			regularDemandsBL.updateRepay(memcode, problem, "Member",RepaymentMadeBy.this);
			regularDemandsBLTemp.updateRepay(memcode, problem, "Member",RepaymentMadeBy.this);
			if(btnNext.getText().toString().equalsIgnoreCase("Save")){
				regularDemandsBL.updatesavefb(memcode,"member",RepaymentMadeBy.this);
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
