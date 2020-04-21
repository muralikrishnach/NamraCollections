package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

import com.jayam.impactapp.RepaymentMadeBy.UpdateRepay;
import com.jayam.impactapp.RepaymentMadeBy.Updatelistner;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RenewGroupWise extends Base{

	private LinearLayout llAttendense;
	private Button btnNext;
	private RadioGroup rgGroup;
	private String groupnumber,centernumber;
	private Intent ii;
	public boolean isphotCaptured = false;
	File sdImageMainDirectory;
	ArrayList<RegularDemandsDO> alregularDemandsBL;
	String problem="S";
	String LastTranScode;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		intializeControlles();
		ii=getIntent();
		File root = new File(Environment.getExternalStorageDirectory()+ File.separator + AppConstants.FolderName + File.separator);
		if(!root.exists())
			root.mkdirs();
		groupnumber	=	getIntent().getExtras().getString("groupnumber");
		final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",RenewGroupWise.this);
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
		final String LastTranScode= StringUtils.getTransactionCode_C(alregularDemandsBL.get(0),RenewGroupWise.this);
        sdImageMainDirectory = new File(root, LastTranScode+".JPEG");
        
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
        
		ivHome.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				setResult(AppConstants.RESULTCODE_HOME);
				finish();
			}
		});
		
//		ivLogout.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Intent i = new Intent(RenewGroupWise.this,loginActivity.class);
//				startActivity(i);
//			}
//		});
		btnNext.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				UpdateRenew updateQom = new UpdateRenew(RenewGroupWise.this, new Updatelistner()
				{
					@Override
					public void onUpdate()
					{
						HideLoader();
						IntialParametrsBL inpbl=new IntialParametrsBL();
						ArrayList<IntialParametrsDO> alin=inpbl.SelectAll(RenewGroupWise.this);
						if(alin.get(0).MeetingTime.equalsIgnoreCase("1"))
						{
							Intent intent	=	new Intent(RenewGroupWise.this, MeetingTime.class);
							intent.putExtra("groupnumber", groupnumber);
							startActivityForResult(intent,1234);
						}
						else if(alin.get(0).GroupPhoto.equalsIgnoreCase("1") &&  !isphotCaptured)
						{
							Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
							Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				            startActivityForResult(cameraIntent, 111);
						}
						else
						{
							Intent confmScreen = new Intent(RenewGroupWise.this, ConfirmationScreen.class);
							confmScreen.putExtra("groupnumber", groupnumber);
							startActivityForResult(confmScreen, 123);
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
		llAttendense	=	(LinearLayout) inflater.inflate(R.layout.renew_activity, null);
		rgGroup			=	(RadioGroup) llAttendense.findViewById(R.id.rgGroup);
		btnNext			=	(Button) llAttendense.findViewById(R.id.btnNext);
		svBase.setVisibility(View.INVISIBLE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Renew");
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
		else if(requestCode == 111 && resultCode==RESULT_OK)
		{
			//Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_LONG).show();
			showAlertDailog("Photo Captured successfully", "Ok", new CustomDailoglistner() {
				
				@Override
				public void onPossitiveButtonClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					isphotCaptured=true;
						Intent confmScreen = new Intent(RenewGroupWise.this, ConfirmationScreen.class);
						confmScreen.putExtra("groupnumber", groupnumber);
						startActivityForResult(confmScreen, 123);
				}
				
				@Override
				public void onNegativeButtonClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					
				}
			});
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
			regularDemandsBL.updateRenew(groupnumber, problem, "Group",RenewGroupWise.this);
			regularDemandsBLTemp.updateRenew(groupnumber, problem, "Group",RenewGroupWise.this);
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
