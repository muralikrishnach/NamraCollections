package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

import com.jayam.impactapp.adapters.AttendenseAdapter;
import com.jayam.impactapp.adapters.GroupsAdapter_Attendense;
import com.jayam.impactapp.adapters.GroupsAdapter_Qom;
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
import android.widget.TextView;

public class QomCenters extends Base {

	private LinearLayout llGroups;
	private ListView lvGroups;
	private GroupsAdapter_Qom groupsAdapter;
	private String centernuber,centername,collectedamount;
	private Button btnNext;
//	private ArrayList<RegularDemandsDO> alregularDemandsBL;
	private String meetingtime,groupphoto ;
//	int count=0;
	File sdImageMainDirectory;
	public boolean isphotCaptured = false;
	boolean isRenew=false,isNext=false;
	RegularDemandsBL regularDemandsBL;
	ArrayList<RegularDemandsDO> alregularDemandsBL;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		intializeControlles();
		centernuber		=	getIntent().getExtras().getString("centernumber");
		regularDemandsBL = new RegularDemandsBL(); 
		alregularDemandsBL = regularDemandsBL.SelectGroups(centernuber,QomCenters.this);
		String LastTranScode = null;
		for(int i=0;i<alregularDemandsBL.size();i++){
			ArrayList<RegularDemandsDO> alregularDemandsBL1 = regularDemandsBL.SelectAll(alregularDemandsBL.get(i).GNo,"Groups",QomCenters.this);
			for(int j=0;j<alregularDemandsBL1.size();j++){
				if(alregularDemandsBL1.get(j).Renew.equals("Y")){
					isRenew=true;
					j=alregularDemandsBL1.size();
				}
			}
			i=alregularDemandsBL.size();
			LastTranScode= StringUtils.getTransactionCode_C(alregularDemandsBL1.get(0),QomCenters.this);
		}
		for(int i=0;i<alregularDemandsBL.size();i++){
			ArrayList<RegularDemandsDO> alregularDemandsBL1 = regularDemandsBL.SelectAll(alregularDemandsBL.get(i).GNo,"Groups",QomCenters.this);
//			int count=0;
//			for(int j=0;j<alregularDemandsBL1.size();j++){
//				if(alregularDemandsBL1.get(j).Attendance.equalsIgnoreCase("1")){
//					count++;
//				}
//			}
			Log.d("mfimo", "Group"+i+": com count"+regularDemandsBL.getCompletedCount(alregularDemandsBL.get(i).GNo, "group",QomCenters.this)+" act size:"+alregularDemandsBL1.size());
			if(Integer.parseInt(regularDemandsBL.getCompletedCount(alregularDemandsBL.get(i).GNo, "group",QomCenters.this))==0){
				isNext=true;
			}else{
				isNext=false;
			}
		}
		groupsAdapter = new GroupsAdapter_Qom(QomCenters.this, alregularDemandsBL);
		lvGroups.setAdapter(groupsAdapter);
		File root = new File(Environment.getExternalStorageDirectory()+ File.separator + AppConstants.FolderName + File.separator);
		if(!root.exists())
			root.mkdirs();
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
				Intent i = new Intent(QomCenters.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IntialParametrsBL inpbl=new IntialParametrsBL();
				ArrayList<IntialParametrsDO> alin=inpbl.SelectAll(QomCenters.this);
				if(isNext){
					if(alin.get(0).MeetingTime.equalsIgnoreCase("1"))
					{
						Intent intent	=	new Intent(QomCenters.this, MeetingTime_Centerwise.class);
						intent.putExtra("centernuber", centernuber);
						intent.putExtra("groupphoto", groupphoto);
						startActivityForResult(intent,1234);
					}
					else if(alin.get(0).GroupPhoto.equalsIgnoreCase("1") )
					{
						Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			            startActivityForResult(cameraIntent, 111);
					}
					else
					{
						Intent confmScreen = new Intent(QomCenters.this, ConfirmationScreen.class);
						confmScreen.putExtra("centernuber", centernuber);
						startActivityForResult(confmScreen, 123);
					}
				}else{
					showAlertDailog("Capture Feedback for all groups");
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
//		btnNext.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llGroups, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Capture Feedback");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("mfimo", resultCode+"groups");
		for(int i=0;i<alregularDemandsBL.size();i++){
			ArrayList<RegularDemandsDO> alregularDemandsBL1 = regularDemandsBL.SelectAll(alregularDemandsBL.get(i).GNo,"Groups",QomCenters.this);
//			Log.d("mfimo", "Group"+i+": com count"+regularDemandsBL.getCompletedCount(alregularDemandsBL.get(i).GNo, "group", isRenew)+" act size:"+alregularDemandsBL1.size());
//			int count=0;
//			for(int j=0;j<alregularDemandsBL1.size();j++){
//				if(alregularDemandsBL1.get(j).Attendance.equalsIgnoreCase("1")){
//					count++;
//				}
//			}
			Log.d("mfimo", "Group"+i+": com count"+regularDemandsBL.getCompletedCount(alregularDemandsBL.get(i).GNo, "group",QomCenters.this)+" act size:"+alregularDemandsBL1.size());
			if(Integer.parseInt(regularDemandsBL.getCompletedCount(alregularDemandsBL.get(i).GNo, "group",QomCenters.this))==0){
				isNext=true;
			}else{
				isNext=false;
			}
		}
		alregularDemandsBL = regularDemandsBL.SelectGroups(centernuber,QomCenters.this);
		groupsAdapter.refresh(alregularDemandsBL);
		if(resultCode == 12345)
		{
			Log.d("mfimo", "groups finish");
//			count++;
			Log.d("mfimo", requestCode+"asdad"+resultCode);
		}
		else if(resultCode == AppConstants.RESULTCODE_LOGOUT)
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
					Intent confmScreen = new Intent(QomCenters.this, ConfirmationScreen_Centerwise.class);
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
