package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayam.impactapp.adapters.GroupsAdapter_Attendense;
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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class Groups_For_attendense extends Base {
	public static final int RequestPermissionCode = 1;
    private LinearLayout llGroups;
    private ListView lvGroups;
    private GroupsAdapter_Attendense groupsAdapter;
    private String centernuber, centername, collectedamount;
    private ArrayList<RegularDemandsDO> alRegularDemandsDOs;
    private RegularDemandsBL regularDemandsBL;
    private Button btnNext;
    private ArrayList<IntialParametrsDO> altialParametrsDO;
    // private ArrayList<RegularDemandsDO> alregularDemandsBL;
    private IntialParametrsBL intialParametrsBL;
    private String meetingtime, groupphoto;
    UpdateAttandense updateAttandense;
    File sdImageMainDirectory;
    private ArrayList<RegularDemandsDO> alregularDemandsBL;
    private boolean isRenew, isRmel = false;
	String  latitude="";
	String longitude="";
	GPSTrackevalue gps;
	Context mContext;

    @Override
    public void initialize() {

		mContext=Groups_For_attendense.this;
	centernuber = getIntent().getExtras().getString("centernuber");
	centername = getIntent().getExtras().getString("tvCenterName");
	collectedamount = getIntent().getExtras().getString("tvAmountTobeCollected");

		checkAndRequestPermissions();

		gps=new GPSTrackevalue(Groups_For_attendense.this);

		if (Build.VERSION.SDK_INT >= 24) {
			try {
				StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(Groups_For_attendense.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

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
	regularDemandsBL = new RegularDemandsBL();
	alregularDemandsBL = regularDemandsBL.SelectAll(centernuber, "CNo",Groups_For_attendense.this);
	for (int i = 0; i < alregularDemandsBL.size(); i++) {
	    if (alregularDemandsBL.get(i).Renew.equals("Y")) {
		isRenew = true;
		i = alregularDemandsBL.size();
	    }
	}
	ShowLoader();
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		regularDemandsBL = new RegularDemandsBL();
		alRegularDemandsDOs = regularDemandsBL.SelectGroups(centernuber,Groups_For_attendense.this);



		intialParametrsBL = new IntialParametrsBL();
		altialParametrsDO = intialParametrsBL.SelectAll(Groups_For_attendense.this);

		meetingtime = altialParametrsDO.get(0).MeetingTime;
		groupphoto = altialParametrsDO.get(0).GroupPhoto;
		;
		groupsAdapter = new GroupsAdapter_Attendense(Groups_For_attendense.this, alRegularDemandsDOs, altialParametrsDO.get(0),latitude,longitude);
		/*
		 * String[] mStringArray = new
		 * String[alRegularDemandsDOs.size()]; mStringArray
		 * =alRegularDemandsDOs.toArray(mStringArray); for(int
		 * i=0;i<=mStringArray.length;i++) { String gropname =
		 * mStringArray[0]; Toast.makeText(getApplicationContext(),
		 * gropname, Toast.LENGTH_LONG).show(); }
		 */
		File root = new File(Environment.getExternalStorageDirectory() + File.separator
			+ AppConstants.FolderName + File.separator);
		if (!root.exists()) {
		    root.mkdirs();
		}

		// RegularDemandsBL regularDemandsB = new RegularDemandsBL();
		// ArrayList<RegularDemandsDO> vecRegularDemands =
		// regularDemandsB.SelectAll(groupnumber, "Groups");
		final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		final ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectAll(centernuber, "CNo",Groups_For_attendense.this);
		final String LastTranScode = StringUtils.getTransactionCode_C(alregularDemandsBL.get(0),Groups_For_attendense.this);
		Log.d("mfimo", LastTranScode);
		sdImageMainDirectory = new File(root, LastTranScode + ".JPEG");
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			updateAttandense = new UpdateAttandense(Groups_For_attendense.this, new AttandenseListner() {
			    @Override
			    public void onUpdate() {
				HideLoader();

			    }
			});

			updateAttandense.start();
			lvGroups.setAdapter(groupsAdapter);
			lvGroups.setOnItemSelectedListener(new OnItemSelectedListener() {

			    @Override
			    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				String s = parent.getContext().toString();
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_LONG).show();

			    }

			    @Override
			    public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			    }

			});
		    }
		});
	    }
	}).start();

	ivHome.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		setResult(AppConstants.RESULTCODE_HOME);
		finish();
	    }
	});

	ivLogout.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(Groups_For_attendense.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	btnNext.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		IntialParametrsBL inpbl = new IntialParametrsBL();
		ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(Groups_For_attendense.this);
		if (alin.get(0).rmelUser.equalsIgnoreCase("N")) {
		    if (alin.get(0).probInCenter.equalsIgnoreCase("1")) {
			Intent intent = new Intent(Groups_For_attendense.this, ProbInCenterCW.class);
			intent.putExtra("centernumber", centernuber);
			startActivityForResult(intent, 1234);
		    } else if (alin.get(0).groupDiscipline.equalsIgnoreCase("1")
			    || alin.get(0).collExp.equalsIgnoreCase("1") || isRenew) {
			Intent intent = new Intent(Groups_For_attendense.this, QomCenters.class);
			intent.putExtra("centernumber", centernuber);
			startActivityForResult(intent, 1234);
		    } else if (meetingtime.equalsIgnoreCase("1")) {
			Intent intent = new Intent(Groups_For_attendense.this, MeetingTime_Centerwise.class);
			intent.putExtra("centernuber", centernuber);
			intent.putExtra("groupphoto", groupphoto);
			startActivityForResult(intent, 1234);
		    } else if (groupphoto.equalsIgnoreCase("1")) {
			Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			startActivityForResult(cameraIntent, 111);
		    } else {
			Intent confmScreen = new Intent(Groups_For_attendense.this,
				ConfirmationScreen_Centerwise.class);
			confmScreen.putExtra("centernuber", centernuber);
			startActivityForResult(confmScreen, 123);
		    }
		} else {// reml
		    if (alin.get(0).collPlace.equalsIgnoreCase("1") || alin.get(0).collExpRMEL.equalsIgnoreCase("1")
			    || alin.get(0).repaymentMadeBy.equalsIgnoreCase("1")) {
			Intent intent = new Intent(Groups_For_attendense.this, QomCenters.class);
			intent.putExtra("centernumber", centernuber);
			startActivityForResult(intent, 1234);
		    } else if (meetingtime.equalsIgnoreCase("1")) {
			Intent intent = new Intent(Groups_For_attendense.this, MeetingTime_Centerwise.class);
			intent.putExtra("centernuber", centernuber);
			intent.putExtra("groupphoto", groupphoto);
			startActivityForResult(intent, 1234);
		    } else if (groupphoto.equalsIgnoreCase("1")) {
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, 111);
		    } else {
			Intent confmScreen = new Intent(Groups_For_attendense.this,
				ConfirmationScreen_Centerwise.class);
			confmScreen.putExtra("centernuber", centernuber);
			startActivityForResult(confmScreen, 123);
		    }
		}
	    }
	});
    }

    public void intializeControlles() {
	llGroups = (LinearLayout) inflater.inflate(R.layout.centers_attendance, null);
	lvGroups = (ListView) llGroups.findViewById(R.id.lvCenters);
	btnNext = (Button) llGroups.findViewById(R.id.btnNext);
	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llGroups, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	tvHeader.setText("Capture Attendance/GLI/Lateness");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode == AppConstants.RESULTCODE_LOGOUT) {
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_HOME) {
	    setResult(resultCode);
	    finish();
	} else if (requestCode == 111 && resultCode == RESULT_OK) {
	    if (groupphoto.equalsIgnoreCase("1")) {
		showAlertDailog("Photo Captured successfully", "OK", new CustomDailoglistner() {

		    @Override
		    public void onPossitiveButtonClick(DialogInterface dialog) {
			Intent confm = new Intent(Groups_For_attendense.this, ConfirmationScreen_Centerwise.class);
			confm.putExtra("centernuber", centernuber);
			// confm.putExtra("centername", centername);
			// confm.putExtra("collectedamount", collectedamount);
			startActivityForResult(confm, 123);
		    }

		    @Override
		    public void onNegativeButtonClick(DialogInterface dialog) {

		    }
		});
	    } else if (groupphoto.equalsIgnoreCase("2")) {
		showAlertDailog("No Centers Available for transactions.", "OK", new CustomDailoglistner() {

		    @Override
		    public void onPossitiveButtonClick(DialogInterface dialog) {
			Intent groupforcenter = new Intent(Groups_For_attendense.this, Groups_For_attendense.class);
			groupforcenter.putExtra("centernuber", centernuber);
			startActivityForResult(groupforcenter, 1234);
		    }

		    @Override
		    public void onNegativeButtonClick(DialogInterface dialog) {

		    }
		});
	    }
	}
    }

    @Override
    protected void onResume() {
	super.onResume();
	// alRegularDemandsDOs = regularDemandsBL.SelectGroups(centernuber);
	// groupsAdapter.refresh(alRegularDemandsDOs);
    }

    class UpdateAttandense extends Thread {
	Context context;
	AttandenseListner attandenseListner;

	public UpdateAttandense(Context context, AttandenseListner attandenseListner) {
	    this.context = context;
	    this.attandenseListner = attandenseListner;
	}

	@Override
	public void run() {
	    super.run();
	    RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();

	    regularDemandsBLTemp.updateAttendense_Centerwise(centernuber,this.context);

	    regularDemandsBL.updateAttendense_Centerwise(centernuber,this.context);

	    ((Activity) context).runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    attandenseListner.onUpdate();
		}
	    });
	}
    }

    interface AttandenseListner {
	public abstract void onUpdate();
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
