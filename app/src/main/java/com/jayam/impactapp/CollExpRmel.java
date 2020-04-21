package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

import com.jayam.impactapp.CollExperianceGW.UpdateCollExp;
import com.jayam.impactapp.CollExperianceGW.Updatelistner;
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

public class CollExpRmel extends Base {

	private LinearLayout llAttendense;
	private Button btnNext;
	private RadioGroup rgGroup;
	private String memcode,memname;
	public boolean isphotCaptured = false;
	File sdImageMainDirectory;
	ArrayList<RegularDemandsDO> alregularDemandsBL;
	String problem="S";
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		memcode=getIntent().getExtras().getString("memcode");
		memname=getIntent().getExtras().getString("memname");
		intializeControlles();
		IntialParametrsBL inpbl=new IntialParametrsBL();
		ArrayList<IntialParametrsDO> alin=inpbl.SelectAll(CollExpRmel.this);
		final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectAll(memcode, "memeber",CollExpRmel.this);
//		Log.d("mfimo", alregularDemandsBL.get(0).collExpRMEL);
		if(alregularDemandsBL.get(0).collExpRMEL!=null && alregularDemandsBL.get(0).collExpRMEL.equals("O")){
			rgGroup.check(R.id.rgbtonline);
			problem="O";
		}
		else if(alregularDemandsBL.get(0).collExpRMEL!=null && alregularDemandsBL.get(0).collExpRMEL.equals("D")){
			rgGroup.check(R.id.rgbtdelayed);
			problem="D";
		}
		else if(alregularDemandsBL.get(0).collExpRMEL!=null && alregularDemandsBL.get(0).collExpRMEL.equals("N")){
			rgGroup.check(R.id.rgbtnotcoll);
			problem="N";
		}else if(alregularDemandsBL.get(0).collExpRMEL!=null && alregularDemandsBL.get(0).collExpRMEL.equals("U")){
			rgGroup.check(R.id.rgbtmemunava);
			problem="U";
		}
		if(alin.get(0).collPlace.equalsIgnoreCase("1") || alin.get(0).collExp.equalsIgnoreCase("1") || alin.get(0).repaymentMadeBy.equalsIgnoreCase("1"))
			btnNext.setText("Next");
		else
			btnNext.setText("Save");
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg1){
					case R.id.rgbtonline:
						problem="O";
						break;
					case R.id.rgbtdelayed:
						problem="D";
						break;
					case R.id.rgbtnotcoll:
						problem="N";
						break;
					case R.id.rgbtmemunava:
						problem="U";
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
				Intent i = new Intent(CollExpRmel.this,loginActivity.class);
				startActivity(i);
			//	setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				UpdateCollExp updateQom = new UpdateCollExp(CollExpRmel.this, new Updatelistner()
				{
					@Override
					public void onUpdate()
					{
						HideLoader();
						if(btnNext.getText().toString().equalsIgnoreCase("Next")){
							IntialParametrsBL inpbl=new IntialParametrsBL();
							ArrayList<IntialParametrsDO> alin=inpbl.SelectAll(CollExpRmel.this);
							if(alin.get(0).collPlace.equalsIgnoreCase("1")){
								Intent intent	=	new Intent(CollExpRmel.this, CollectionPlace.class);
								intent.putExtra("memcode", memcode);
								intent.putExtra("memname", memname);
								startActivityForResult(intent, 123456);
							}else if(alin.get(0).collExp.equalsIgnoreCase("1")){
								Intent intent	=	new Intent(CollExpRmel.this, CollExperianceMW.class);
								intent.putExtra("memcode", memcode);
								intent.putExtra("memname", memname);
								startActivityForResult(intent, 123456);
							}else if(alin.get(0).repaymentMadeBy.equalsIgnoreCase("1")){
								Intent intent	=	new Intent(CollExpRmel.this, RepaymentMadeBy.class);
								intent.putExtra("memcode", memcode);
								intent.putExtra("memname", memname);
								startActivityForResult(intent, 123456);
							}
						}else{
							setResult(123456);
							finish();
						}
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
		llAttendense	=	(LinearLayout)inflater.inflate(R.layout.collexprmel, null);
		btnNext			=	(Button)llAttendense.findViewById(R.id.btnNext);
		rgGroup			=	(RadioGroup)llAttendense.findViewById(R.id.rggroup);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Collection Experience RMEL");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 123456)
		{
			Log.d("mfimo", "exp finish");
			setResult(resultCode);
			finish();
		}else
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
	class UpdateCollExp extends Thread
	{
		Context context ;
		Updatelistner updatelistner;
		public UpdateCollExp(Context context , Updatelistner updatelistner)
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
			regularDemandsBL.updateCollExpR(memcode, problem, "Member",CollExpRmel.this);
			regularDemandsBLTemp.updateCollExpR(memcode, problem, "Member",CollExpRmel.this);
			if(btnNext.getText().toString().equalsIgnoreCase("Save")){
				regularDemandsBL.updatesavefb(memcode,"member",CollExpRmel.this);
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
