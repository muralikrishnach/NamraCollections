package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CollectionPlace extends Base {

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
		IntialParametrsBL inpbl=new IntialParametrsBL();
		ArrayList<IntialParametrsDO> alin=inpbl.SelectAll(CollectionPlace.this);
		final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectAll(memcode, "memeber",CollectionPlace.this);
//		Log.d("mfimo", alregularDemandsBL.get(0).collPlace);
		if(alregularDemandsBL.get(0).collPlace!=null && alregularDemandsBL.get(0).collPlace.equals("C")){
			rgGroup.check(R.id.rgbtcenter);
			problem="C";
		}
		else if(alregularDemandsBL.get(0).collPlace!=null && alregularDemandsBL.get(0).collPlace.equals("D")){
			rgGroup.check(R.id.rgbtdoorstep);
			problem="D";
		}
		else if(alregularDemandsBL.get(0).collPlace!=null && alregularDemandsBL.get(0).collPlace.equals("B")){
			rgGroup.check(R.id.rgbtbranch);
			problem="B";
		}
		else if(alregularDemandsBL.get(0).collPlace!=null && alregularDemandsBL.get(0).collPlace.equals("O")){
			rgGroup.check(R.id.rgbtothers);
			problem="O";
		}
		if(alin.get(0).collExp.equalsIgnoreCase("1") || alin.get(0).repaymentMadeBy.equalsIgnoreCase("1"))
			btnNext.setText("Next");
		else
			btnNext.setText("Save");
        rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg1){
					case R.id.rgbtsel:
						problem="S";
						break;
					case R.id.rgbtcenter:
						problem="C";
						break;
					case R.id.rgbtdoorstep:
						problem="D";
						break;
					case R.id.rgbtbranch:
						problem="B";
						break;
					case R.id.rgbtothers:
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
				Intent i = new Intent(CollectionPlace.this,loginActivity.class);
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
				UpdateCollPlace updateQom = new UpdateCollPlace(CollectionPlace.this, new Updatelistner()
				{
					@Override
					public void onUpdate()
					{
						HideLoader();
						if(btnNext.getText().toString().equalsIgnoreCase("Next")){
							IntialParametrsBL inpbl=new IntialParametrsBL();
							ArrayList<IntialParametrsDO> alin=inpbl.SelectAll(CollectionPlace.this);
							if(alin.get(0).collExp.equalsIgnoreCase("1")){
								Intent intent	=	new Intent(CollectionPlace.this, CollExperianceMW.class);
								intent.putExtra("memcode", memcode);
								intent.putExtra("memname", memname);
								startActivityForResult(intent, 123456);
							}else if(alin.get(0).repaymentMadeBy.equalsIgnoreCase("1")){
								Intent intent	=	new Intent(CollectionPlace.this, RepaymentMadeBy.class);
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
		llAttendense	=	(LinearLayout)inflater.inflate(R.layout.collplace, null);
		btnNext			=	(Button)llAttendense.findViewById(R.id.btnNext);
		rgGroup			=	(RadioGroup)llAttendense.findViewById(R.id.rggroup);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Collection Place");
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
	
	class UpdateCollPlace extends Thread
	{
		Context context ;
		Updatelistner updatelistner;
		public UpdateCollPlace(Context context , Updatelistner updatelistner)
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
			regularDemandsBL.updateCollPlace(memcode, problem, "member",CollectionPlace.this);
			regularDemandsBLTemp.updateCollPlace(memcode, problem, "member",CollectionPlace.this);
			if(btnNext.getText().toString().equalsIgnoreCase("Save")){
				regularDemandsBL.updatesavefb(memcode,"member",CollectionPlace.this);
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
