package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

import com.jayam.impactapp.adapters.GroupsAdapter_Qom;
import com.jayam.impactapp.adapters.GroupsAdapter_renew;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.StringUtils;


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
import android.widget.ListView;

public class RenewCenters extends Base{

	private LinearLayout llGroups;
	private ListView lvGroups;
	private GroupsAdapter_renew groupsAdapter;
	private String centernuber,centername,collectedamount;
	private Button btnNext;
//	private ArrayList<RegularDemandsDO> alregularDemandsBL;
	private String meetingtime,groupphoto ;
	int count=0;
	File sdImageMainDirectory;
	public boolean isphotCaptured = false;
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		intializeControlles();
		centernuber		=	getIntent().getExtras().getString("centernuber");
		Log.d("mfimo", "center number in renew:"+centernuber);
		RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		final ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectGroups(centernuber,RenewCenters.this);
		groupsAdapter = new GroupsAdapter_renew(RenewCenters.this, alregularDemandsBL);
		lvGroups.setAdapter(groupsAdapter);
		ArrayList<RegularDemandsDO> alregularDemandsBL1 = regularDemandsBL.SelectAll(alregularDemandsBL.get(0).GNo,"Groups",RenewCenters.this);
		File root = new File(Environment.getExternalStorageDirectory()+ File.separator + AppConstants.FolderName + File.separator);
		if(!root.exists())
			root.mkdirs();
		final String LastTranScode= StringUtils.getTransactionCode_C(alregularDemandsBL1.get(0),RenewCenters.this);
	    sdImageMainDirectory = new File(root, LastTranScode+".JPEG");
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
				Intent i = new Intent(RenewCenters.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(count==alregularDemandsBL.size() || count>alregularDemandsBL.size()){
					HideLoader();
					IntialParametrsBL inpbl=new IntialParametrsBL();
					ArrayList<IntialParametrsDO> alin=inpbl.SelectAll(RenewCenters.this);
					if(alin.get(0).MeetingTime.equalsIgnoreCase("1"))
					{
						Intent intent	=	new Intent(RenewCenters.this, MeetingTime_Centerwise.class);
						intent.putExtra("centernuber", centernuber);
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
						Intent confmScreen = new Intent(RenewCenters.this, ConfirmationScreen_Centerwise.class);
						confmScreen.putExtra("centernuber", centernuber);
						startActivityForResult(confmScreen, 123);
					}
				}
			}
		});
	}
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llGroups		=	(LinearLayout)inflater.inflate(R.layout.centers_attendance, null);
		lvGroups		=	(ListView)llGroups.findViewById(R.id.lvCenters);
		btnNext			=	(Button)llGroups.findViewById(R.id.btnNext);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llGroups, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Renew");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		count++;
		Log.d("mfimo", requestCode+"asdad"+resultCode);
		if(resultCode == AppConstants.RESULTCODE_LOGOUT)
		{
			setResult(resultCode);
			finish();
		}
		else if(resultCode == AppConstants.RESULTCODE_HOME)
		{
			setResult(resultCode);
			finish();
		}else if(requestCode == 111 && resultCode==RESULT_OK)
		{
			showAlertDailog("Photo Captured successfully", "OK", new CustomDailoglistner()
			{
				
				@Override
				public void onPossitiveButtonClick(DialogInterface dialog) 
				{
					isphotCaptured=true;
					Intent confmScreen = new Intent(RenewCenters.this, ConfirmationScreen_Centerwise.class);
					confmScreen.putExtra("centernuber", centernuber);
					startActivityForResult(confmScreen,123);
				}

				@Override
				public void onNegativeButtonClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
}
