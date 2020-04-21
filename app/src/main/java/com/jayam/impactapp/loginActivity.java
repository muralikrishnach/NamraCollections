package com.jayam.impactapp;

import com.jayam.impactapp.businesslayer.GetDataBL;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.DatabaseHelper;
import com.jayam.impactapp.database.FtodreasonsBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.utils.NetworkUtility;
import com.jayam.impactapp.utils.SharedPrefUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class loginActivity extends Baselogin implements DataListner
{
	double latitude=0.0;
	double longitude=0.0;
	public static final int RequestPermissionCode = 1;
	 /** Called when the activity is first created. */
	private LinearLayout llLogin;
	private EditText etuserId, etPassword;
	private Button btnLogin;
	private GetDataBL getDataBL;
	private Context mContext;
	GPSTrackevalue gps;

	TrnsactionsBL tbl;
	Transaction_OD_BL tobl;
//	private RegularDemandsBL bl = new RegularDemandsBL();
	@Override
	public void initialize() 
	{
		mContext = loginActivity.this;
		tbl=new TrnsactionsBL();
		tobl=new Transaction_OD_BL();
		checkAndRequestPermissions();

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




		if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(loginActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

		} else {
			Toast.makeText(mContext, "You need have granted permission", Toast.LENGTH_SHORT).show();
			gps = new GPSTrackevalue(loginActivity.this);

			// Check if GPS enabled
			if (gps.canGetLocation()) {

				latitude = gps.getLatitude();
				longitude = gps.getLongitude();

				// \n is for new line
				Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
			} else {
				// Can't get location.
				// GPS or network is not enabled.
				// Ask user to enable GPS/network in settings.
				gps.showSettingsAlert();
			}
		}
		intializeControlles();
		@SuppressWarnings("static-access")
		TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(loginActivity.this.TELEPHONY_SERVICE);
		
		//pvGGApupkcWjIuDsrcnj1A4/UX6ahJLfK8vPVC8oDVk=
		//final String DeviceId =  telephonyManager.getDeviceId();
		
		final String DeviceId =  "pvGGApupkcWjIuDsrcnj1A4/UX6ahJLfK8vPVC8oDVk=";
//		
		Toast.makeText(getApplicationContext(), DeviceId, Toast.LENGTH_LONG).show();
		
		
		//B8:51:47:66:06:00
		//BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
		
		
		//String address = mBluetoothAdapter.getAddress();    
		
		String address ="B8:51:47:66:06:00";    
		Log.d("mfimo", address);
//		
		String isIntialdownload = SharedPrefUtils.getKeyValue(loginActivity.this, AppConstants.pref_name, AppConstants.intialDownload, "false");
		createDataBase();
		if(isIntialdownload.equalsIgnoreCase("false"))
		{
			KeyValue value = new KeyValue(AppConstants.intialDownload, "true");
			SharedPrefUtils.setValue(loginActivity.this, AppConstants.pref_name, value);
		}
		
		getDataBL = new GetDataBL(loginActivity.this, loginActivity.this);
		
		tvHeader.setText(getResources().getString(R.string.login));


		/*IntialParametrsBL inpbl=new IntialParametrsBL();
		FtodreasonsBL ftodreasonsBL = new FtodreasonsBL();
		inpbl.Delete(null,loginActivity.this);
		ftodreasonsBL.Delete(null,loginActivity.this);
		RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
        regularDemandsBL.Truncatetabel(loginActivity.this);
		RegularDemandsBLTemp temp = new RegularDemandsBLTemp();
		temp.Truncatetabel(loginActivity.this);
		regularDemandsBL.Truncatetabel(loginActivity.this);
		tbl.Truncatetabel(loginActivity.this);
		tbl.TruncatetabelLateTransaction(loginActivity.this);
		regularDemandsBL.TruncatetabelOD(loginActivity.this);
		regularDemandsBL.ODDemandsSAVE(loginActivity.this);
		regularDemandsBL.TruncatetabelAdvance(loginActivity.this);
		regularDemandsBL.TruncatetabelAdvanceSave(loginActivity.this);
		tobl.TruncatetabelServerUploadData(loginActivity.this);*/




			btnLogin.setOnClickListener(new  OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					if(etuserId.getText().toString().equalsIgnoreCase(""))
					{
						showAlertDailog(getResources().getString(R.string.enteruserid));
					}
					else if(etPassword.getText().toString().equalsIgnoreCase(""))
					{
						showAlertDailog(getResources().getString(R.string.enterpassword));
					}
					else if(NetworkUtility.isNetworkConnectionAvailable(loginActivity.this))
					{
						ShowLoader();
						String address;

				String addressv="44:80:EB:80:0c:9B"; //= "123456Aa";
					getDataBL.validateuserlogin(etuserId.getText().toString(), etPassword.getText().toString(), DeviceId,addressv);
					KeyValue keyValue_printeraddress = new KeyValue(AppConstants.macid, addressv.replaceAll(":",""));
					SharedPrefUtils.setValue(loginActivity.this, AppConstants.pref_name, keyValue_printeraddress);
						getDataBL.validateuserlogin(etuserId.getText().toString(), etPassword.getText().toString(), DeviceId, addressv);
					}
					
					
					
					
					else
					{
						showAlertDailog(getResources().getString(R.string.nonetwork));
					}
				}
			});
		//}
	}
	public void onBackPressed() {
		   Intent intent = new Intent(Intent.ACTION_MAIN);
		   intent.addCategory(Intent.CATEGORY_HOME);
		   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   startActivity(intent);
		 }
	@SuppressWarnings("deprecation")
	private void intializeControlles()
	{
		llLogin		=	(LinearLayout)inflater.inflate(R.layout.login, null);
		etuserId	=	(EditText)llLogin.findViewById(R.id.etuserId);
		etPassword	=	(EditText)llLogin.findViewById(R.id.etPassword);
		btnLogin	=	(Button)llLogin.findViewById(R.id.btnLogin);
		llBaseMiddle.addView(llLogin, LayoutParams.FILL_PARENT,  LayoutParams.FILL_PARENT);
		
		etuserId.setFocusable(true);
		hidehomeIcons();
		
	}

	public void createDataBase()
    {
    	//DatabaseHelper dbHelper = new DatabaseHelper(this);
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(loginActivity.this);

		try
		{
			databaseHelper.createDataBase();
			//DatabaseHelper.newopenDataBase();
		}
		catch (Exception e)
		{							
			Log.e("Database Error MSG", e+"");
		}
    }
	@Override
	public void onDataretrieved(Object objs)
	{
		HideLoader();
		Log.e("status", ""+((String)objs));
		if(((String)objs).toString().equalsIgnoreCase("0"))
		{
			KeyValue keyValue_username = new KeyValue(AppConstants.username, etuserId.getText().toString());
			KeyValue keyValue_password = new KeyValue(AppConstants.password, etPassword.getText().toString());
			
			SharedPrefUtils.setValue(loginActivity.this, AppConstants.pref_name, keyValue_username);
			SharedPrefUtils.setValue(loginActivity.this, AppConstants.pref_name, keyValue_password);
			
			//Intent dashboard = new Intent(loginActivity.this, DashBoard.class);
			Intent dashboard = new Intent(loginActivity.this, NavigationActivity.class);
			overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
			dashboard.putExtra("status", "online");
			startActivity(dashboard);
		}
		else if(((String)objs).toString().equalsIgnoreCase("1"))
		{
			showAlertDailog("Invalid UserName or Password.");
		}
		else if(((String)objs).toString().equalsIgnoreCase("2"))
		{
			showAlertDailog("Server Busy.. Please Try Again Later.");
		}
		else if(((String)objs).toString().equalsIgnoreCase("3"))
		{
			showAlertDailog("New Version Available please Download...");
		}
		else if(((String)objs).toString().equalsIgnoreCase("4"))
		{
			showAlertDailog("Mobile MACID mismatch, Contact Administrator");
		} 
		else
		{
			showAlertDailog("Login Failed. Please Try Again Later.");	
		}
		
	}

	@Override
	public void onDataretrievalFailed(String errorMessage) 
	{
		HideLoader();
		if(errorMessage.equalsIgnoreCase("1"))
		{
			showAlertDailog("Invalid UserName or Password");
			return;
		}
		else if(errorMessage.equalsIgnoreCase("2"))
		{
			showAlertDailog("Server Busy.. Please Try Again Later.");
			return;
		}
		if(errorMessage.equalsIgnoreCase("3"))
		{
			showAlertDailog("New Version Available please Download...");
			return;
		}
		else if(errorMessage.equalsIgnoreCase("4"))
		{
			showAlertDailog("Mobile MACID mismatch, Contact Administrator.");
			return;
		}
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		etPassword.setText("");

		String count_regularDemands = new RegularDemandsBL().getrowCount(loginActivity.this);
		String count_advanceDemands = new AdvanceDemandBL().getrowCount(loginActivity.this);
		String count_ODDemands = new Transaction_OD_BL().getrowCount(loginActivity.this);




		if((count_regularDemands != null && Integer.parseInt(count_regularDemands) > 0) || (count_advanceDemands != null && Integer.parseInt(count_advanceDemands) > 0) ||  (count_ODDemands != null && Integer.parseInt(count_ODDemands) > 0))
//		if((count_regularDemands != null && Integer.parseInt(count_regularDemands) > 0) || (count_advanceDemands != null && Integer.parseInt(count_advanceDemands) > 0) || (count_npsDemands != null && Integer.parseInt(count_npsDemands) > 0))
		{
			String uName = SharedPrefUtils.getKeyValue(loginActivity.this, AppConstants.pref_name, AppConstants.username);
			etuserId.setText(""+uName);
			etuserId.setEnabled(false);

			etPassword.requestFocus();
		}
		else
		{
			etuserId.setText("");
			etPassword.setText("");
			etuserId.setEnabled(true);
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