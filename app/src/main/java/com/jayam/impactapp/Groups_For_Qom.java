package com.jayam.impactapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayam.impactapp.Groups_For_attendense.UpdateAttandense;
import com.jayam.impactapp.adapters.GroupsAdapter_Attendense;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class Groups_For_Qom extends Base {

	private LinearLayout llGroups;
	private ListView lvGroups;
	private GroupsAdapter_Attendense groupsAdapter;
	private String centernuber,centername,collectedamount;
	private ArrayList<RegularDemandsDO> alRegularDemandsDOs;
	private RegularDemandsBL  regularDemandsBL;
	private Button btnNext;
	private ArrayList<IntialParametrsDO> altialParametrsDO;
//	private ArrayList<RegularDemandsDO> alregularDemandsBL;
	private IntialParametrsBL intialParametrsBL;
	private String meetingtime,groupphoto ;
	UpdateAttandense updateAttandense;
	GPSTrackevalue gps;
	Context mContext;
	public static final int RequestPermissionCode = 1;
	String  latitude="";
	String longitude="";
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		mContext=Groups_For_Qom.this;
		centernuber		=	getIntent().getExtras().getString("centernuber");
		centername     =	getIntent().getExtras().getString("tvCenterName");
		checkAndRequestPermissions();

		gps=new GPSTrackevalue(Groups_For_Qom.this);

		if (Build.VERSION.SDK_INT >= 24) {
			try {
				StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(Groups_For_Qom.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

		} else {
			Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();

			if (gps.canGetLocation()) {
				latitude = gps.getLatitude()+"";
				longitude = gps.getLongitude()+"";
				Log.v("latitudeCENTERS", "latitudeCENTERS" + latitude);
				Log.v("langitudeCENTERS", "langitudeCENTERS" + longitude);
				Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
			} else {
				gps.showSettingsAlert();
			}
		}
		intializeControlles();
		ShowLoader();
		new Thread(new Runnable() 
		{
			public void run() {
				regularDemandsBL = new RegularDemandsBL();
				alRegularDemandsDOs = regularDemandsBL.SelectGroups(centernuber,Groups_For_Qom.this);
				
				intialParametrsBL = new IntialParametrsBL();
				altialParametrsDO = intialParametrsBL.SelectAll(Groups_For_Qom.this);
				
				meetingtime = altialParametrsDO.get(0).MeetingTime;
				groupphoto = altialParametrsDO.get(0).GroupPhoto;;
				groupsAdapter = new GroupsAdapter_Attendense(Groups_For_Qom.this, alRegularDemandsDOs,altialParametrsDO.get(0),latitude,longitude);
				lvGroups.setAdapter(groupsAdapter);
			}
		}).start();
	}
	
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
		tvHeader.setText("Capture Qom");
	}
	private boolean checkAndRequestPermissions() {

		int readphone = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
		int permissionSendMessage = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
		int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
		int locationPermission1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int wifi = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE);


		List<String> listPermissionsNeeded = new ArrayList();

		if (readphone != 0) {
			listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
		}

		if (locationPermission1 != 0) {
			listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}

		if (locationPermission != 0) {
			listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
		}
		if (permissionSendMessage != 0) {
			listPermissionsNeeded.add("android.permission.CAMERA");
		}
		if(wifi!=0){
			listPermissionsNeeded.add(android.Manifest.permission.ACCESS_WIFI_STATE);
		}

		if (listPermissionsNeeded.isEmpty()) {
			return true;
		}
		ActivityCompat.requestPermissions(this, (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), RequestPermissionCode);
		return false;
	}




	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case RequestPermissionCode /*1*/:
				Map<String, Integer> perms = new HashMap();

				perms.put(android.Manifest.permission.READ_PHONE_STATE, Integer.valueOf(0));
				perms.put("android.permission.CAMERA", Integer.valueOf(0));
				perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, Integer.valueOf(0));
				perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Integer.valueOf(0));
				perms.put(android.Manifest.permission.ACCESS_WIFI_STATE, Integer.valueOf(0));
				if (grantResults.length > 0) {
					for (int i = 0; i < permissions.length; i ++) {
						perms.put(permissions[i], Integer.valueOf(grantResults[i]));
					}
					if (((Integer) perms.get("android.permission.CAMERA")).intValue() == 0 && ((Integer) perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE)).intValue() == 0  && ((Integer) perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)).intValue() == 0&& ((Integer) perms.get(android.Manifest.permission.READ_PHONE_STATE)).intValue() == 0 && ((Integer) perms.get(android.Manifest.permission.ACCESS_WIFI_STATE)).intValue() == 0) {
						Log.v("", "sms & location services permission granted");
						return;
					}
					return;
				}
				return;
			default:
				return;
		}
	}
}
