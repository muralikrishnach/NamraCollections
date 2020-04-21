package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

import com.jayam.impactapp.adapters.AttendenseAdapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.SharedPrefUtils;
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
import android.widget.ListView;
import android.widget.TextView;

public class Attendense extends Base {
    private LinearLayout llAttendense;
    private AttendenseAdapter attendenseAdapter;
    private ListView lvAttendense;
    // private CheckBox cbSelectAll;
    private String groupnumber;
    private Button btnNext;
    private String att, gli, lat, meetingtime, groupphoto;
    private TextView tvAttendance, tvGli, tvLate;
    public boolean isphotCaptured = false;
    File sdImageMainDirectory;

    @Override
    public void initialize() {
	intializeControlles();
	groupnumber = getIntent().getExtras().getString("groupnumber");
	final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	final ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",Attendense.this);

	att = getIntent().getExtras().getString("att");
	gli = getIntent().getExtras().getString("gli");
	lat = getIntent().getExtras().getString("lat");
	meetingtime = getIntent().getExtras().getString("meetingtime");
	groupphoto = getIntent().getExtras().getString("groupphoto");

	Log.e("att", "" + att);
	Log.e("gli", "" + gli);
	Log.e("lat", "" + lat);
	Log.e("aSize", "" + alregularDemandsBL.size());
	attendenseAdapter = new AttendenseAdapter(Attendense.this, alregularDemandsBL, att, gli, lat);
	lvAttendense.setAdapter(attendenseAdapter);

	File root = new File(
		Environment.getExternalStorageDirectory() + File.separator + AppConstants.FolderName + File.separator);
	if (!root.exists()) {
	    root.mkdirs();
	}

	// RegularDemandsBL regularDemandsB = new RegularDemandsBL();
	// ArrayList<RegularDemandsDO> vecRegularDemands =
	// regularDemandsB.SelectAll(groupnumber, "Groups");
	final String LastTranScode = StringUtils.getTransactionCode_G(alregularDemandsBL.get(0),Attendense.this);
	sdImageMainDirectory = new File(root, LastTranScode + ".JPEG");
	// Toast.makeText(getApplicationContext(), "hi",
	// Toast.LENGTH_LONG).show();

	if (att.equalsIgnoreCase("1")) {
	    tvAttendance.setVisibility(View.VISIBLE);
	} else {
	    tvAttendance.setVisibility(View.GONE);
	}

	if (gli.equalsIgnoreCase("1")) {
	    tvGli.setVisibility(View.VISIBLE);
	} else {
	    tvGli.setVisibility(View.GONE);
	}

	if (lat.equalsIgnoreCase("1")) {
	    tvLate.setVisibility(View.VISIBLE);
	} else {
	    tvLate.setVisibility(View.GONE);
	}

	/*
	 * if(isphotCaptured==true) // edited hari { Intent confmScrn = new
	 * Intent(Attendense.this, ConfirmationScreen.class);
	 * confmScrn.putExtra("groupnumber", groupnumber);
	 * startActivityForResult(confmScrn, 123); }
	 */
	/*
	 * cbSelectAll.setTag("true");
	 * 
	 * cbSelectAll.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { String tag =
	 * v.getTag().toString();
	 * 
	 * if(tag.equalsIgnoreCase("true")) { v.setTag("false");
	 * attendenseAdapter.deSelectAll(); } else { v.setTag("true");
	 * attendenseAdapter.selectAll(); } } });
	 */

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
		Intent i = new Intent(Attendense.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	btnNext.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {

		ShowLoader();
		UpdateAttendense updateAttendense = new UpdateAttendense(Attendense.this, new Updatelistner() {
		    @Override
		    public void onUpdate() {
			HideLoader();
			IntialParametrsBL inpbl = new IntialParametrsBL();
			ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(Attendense.this);
			if (alin.get(0).rmelUser.equalsIgnoreCase("N")) {
			    if (alin.get(0).probInCenter.equalsIgnoreCase("1") && getIntent().hasExtra("position")) {
				Intent intent = new Intent(Attendense.this, ProbInCenterGW.class);
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 1234);
			    } else if (alin.get(0).qom.equalsIgnoreCase("Y")) {
				Intent intent = new Intent(Attendense.this, QomGroupWise.class);
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 1234);
			    } else if (alin.get(0).groupDiscipline.equalsIgnoreCase("1")) {
				Intent intent = new Intent(Attendense.this, GroupDisciplineGW.class);
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 1234);
			    } else if (alin.get(0).collExp.equalsIgnoreCase("1")) {
				Intent intent = new Intent(Attendense.this, CollExperianceGW.class);
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 1234);
			    } else if (meetingtime.equalsIgnoreCase("1")) {
				Intent intent = new Intent(Attendense.this, MeetingTime.class);
				intent.putExtra("groupphoto", groupphoto);
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 1234);
			    } else if (groupphoto.equalsIgnoreCase("1") && !isphotCaptured) {
				Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(cameraIntent, 111);
			    } else {
				Intent confmScreen = new Intent(Attendense.this, ConfirmationScreen.class);
				confmScreen.putExtra("groupnumber", groupnumber);
				startActivityForResult(confmScreen, 123);
			    }
			    KeyValue keyValue = new KeyValue(AppConstants.attendense,
				    attendenseAdapter.getSelectedItems() + "");
			    SharedPrefUtils.setValue(Attendense.this, AppConstants.memberDetails_pref, keyValue);
			    KeyValue keyValue_total = new KeyValue(AppConstants.attendense_total,
				    alregularDemandsBL.size() + "");
			    SharedPrefUtils.setValue(Attendense.this, AppConstants.memberDetails_pref, keyValue_total);
			} else {
			    if (alin.get(0).qom.equalsIgnoreCase("Y") || alin.get(0).collExpRMEL.equalsIgnoreCase("1")
				    || alin.get(0).collPlace.equalsIgnoreCase("1")
				    || alin.get(0).repaymentMadeBy.equalsIgnoreCase("1")) {
				Intent confmScreen = new Intent(Attendense.this, Group_Members.class);
				confmScreen.putExtra("groupcode", groupnumber);
				confmScreen.putExtra("type", "group");
				startActivityForResult(confmScreen, 123);
			    } else {
				if (meetingtime.equalsIgnoreCase("1")) {
				    Intent intent = new Intent(Attendense.this, MeetingTime.class);
				    intent.putExtra("groupphoto", groupphoto);
				    intent.putExtra("groupnumber", groupnumber);
				    startActivityForResult(intent, 1234);
				} else if (groupphoto.equalsIgnoreCase("1") && !isphotCaptured) {
				    Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
				    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				    startActivityForResult(cameraIntent, 111);
				} else {
				    Intent confmScreen = new Intent(Attendense.this, ConfirmationScreen.class);
				    confmScreen.putExtra("groupnumber", groupnumber);
				    startActivityForResult(confmScreen, 123);
				}
			    }
			    KeyValue keyValue = new KeyValue(AppConstants.attendense,
				    attendenseAdapter.getSelectedItems() + "");
			    Log.d("mfimo att", attendenseAdapter.getSelectedItems() + "");
			    SharedPrefUtils.setValue(Attendense.this, AppConstants.memberDetails_pref, keyValue);
			    KeyValue keyValue_total = new KeyValue(AppConstants.attendense_total,
				    alregularDemandsBL.size() + "");
			    Log.d("mfimo att tot", alregularDemandsBL.size() + "");
			    SharedPrefUtils.setValue(Attendense.this, AppConstants.memberDetails_pref, keyValue_total);
			}
		    }
		});
		updateAttendense.start();

	    }
	});
    }

    @SuppressWarnings("deprecation")
    public void intializeControlles() {
	llAttendense = (LinearLayout) inflater.inflate(R.layout.attendense, null);
	lvAttendense = (ListView) llAttendense.findViewById(R.id.lvAttendense);
	// cbSelectAll = (CheckBox)llAttendense.findViewById(R.id.cbSelectAll);
	btnNext = (Button) llAttendense.findViewById(R.id.btnNext);
	tvAttendance = (TextView) llAttendense.findViewById(R.id.tvAttendance);
	tvGli = (TextView) llAttendense.findViewById(R.id.tvGli);
	tvLate = (TextView) llAttendense.findViewById(R.id.tvLate);

	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	tvHeader.setText("Attendance / GLI / Lateness");
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
	    // Toast.makeText(getApplicationContext(), data.toString(),
	    // Toast.LENGTH_LONG).show();
	    showAlertDailog("Photo Captured successfully", "Ok", new CustomDailoglistner() {

		@Override
		public void onPossitiveButtonClick(DialogInterface dialog) {
		    // TODO Auto-generated method stub
		    isphotCaptured = true;
		    Intent confm = new Intent(Attendense.this, ConfirmationScreen.class);
		    confm.putExtra("groupnumber", groupnumber);
		    startActivityForResult(confm, 123);
		}

		@Override
		public void onNegativeButtonClick(DialogInterface dialog) {
		    // TODO Auto-generated method stub

		}
	    });
	}
    }

    class UpdateAttendense extends Thread {
	Context context;
	Updatelistner updatelistner;

	public UpdateAttendense(Context context, Updatelistner updatelistner) {
	    this.context = context;
	    this.updatelistner = updatelistner;
	}

	@Override
	public void run() {
	    super.run();

	    RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();

	    regularDemandsBLTemp.updateAttendense(attendenseAdapter.getUpDatedList(),Attendense.this);
	    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();

	    regularDemandsBL.updateAttendense(attendenseAdapter.getUpDatedList(),Attendense.this);
	    ((Activity) context).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    updatelistner.onUpdate();
		}
	    });

	}
    }

    interface Updatelistner {
	public abstract void onUpdate();
    }
}
