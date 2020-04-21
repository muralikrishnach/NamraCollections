package com.jayam.impactapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayam.impactapp.adapters.GroupsAdapter_OD;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.objects.ODDemandsDO;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class Groups_OD extends Base
{
	public static final int RequestPermissionCode = 1;
	private LinearLayout llCenters;
	private ListView lvCenters;
	private GroupsAdapter_OD adapter_OD;
	String  latitude="";
	String longitude="";
	GPSTrackevalue gps;
	Context mContext;
	@Override
	public void initialize()
	{

		mContext=Groups_OD.this;
		intializeControlles();
		String centercode = getIntent().getExtras().getString("centercode");
		checkAndRequestPermissions();

		gps=new GPSTrackevalue(Groups_OD.this);

		if (Build.VERSION.SDK_INT >= 24) {
			try {
				StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(Groups_OD.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

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
		ODDemandsBL  bl = new ODDemandsBL();
		
		ArrayList<ODDemandsDO> alArrayList = bl.SelectAllGroups(centercode,Groups_OD.this);
		
		adapter_OD = new GroupsAdapter_OD(Groups_OD.this, alArrayList,latitude,longitude);
		lvCenters.setAdapter(adapter_OD);
		
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
				Intent i = new Intent(Groups_OD.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
	}

	public void intializeControlles()
	{
		llCenters		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvCenters		=	(ListView)llCenters.findViewById(R.id.lvCenters);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llCenters, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Groups");
		
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
