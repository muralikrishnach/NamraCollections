package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

import com.jayam.impactapp.adapters.Group_members_Adapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.StringUtils;


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
import android.widget.ListView;
import android.widget.Toast;

public class Group_Members extends Base {

    private LinearLayout llGroups;
    private ListView lvGroups;
    private Button btnNext;
    private Group_members_Adapter reportsMembers_Adapter;
    private String groupcode;
    private TrnsactionsBL trnsactionsBL;
    private ArrayList<RegularDemandsDO> alArrayList;
    int count = 0;
    String type;
    public boolean isphotCaptured = false;
    File sdImageMainDirectory;
    RegularDemandsBL bl;

    @Override
    public void initialize() {
	// TODO Auto-generated method stub
	groupcode = getIntent().getExtras().getString("groupcode");
	type = getIntent().getExtras().getString("type");
	trnsactionsBL = new TrnsactionsBL();
	intializeControlles();
	bl = new RegularDemandsBL();
	RegularDemandsBLTemp regularDemandsBL = new RegularDemandsBLTemp();
	alArrayList = regularDemandsBL.SelectAll(groupcode, "Groups",Group_Members.this);
	File root = new File(
		Environment.getExternalStorageDirectory() + File.separator + AppConstants.FolderName + File.separator);
	if (!root.exists()) {
	    root.mkdirs();
	}
	final String LastTranScode = StringUtils.getTransactionCode_G(alArrayList.get(0),Group_Members.this);
	sdImageMainDirectory = new File(root, LastTranScode + ".JPEG");
	reportsMembers_Adapter = new Group_members_Adapter(Group_Members.this, alArrayList);
	lvGroups.setAdapter(reportsMembers_Adapter);
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
		Intent i = new Intent(Group_Members.this, loginActivity.class);
		startActivity(i);

		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	btnNext.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		IntialParametrsBL inpbl = new IntialParametrsBL();
		ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(Group_Members.this);
		if (Integer.parseInt(bl.getCompletedCount(groupcode, "group",Group_Members.this)) == 0) {
		    if (alin.get(0).MeetingTime.equalsIgnoreCase("1")) {
			Intent intent = new Intent(Group_Members.this, MeetingTime.class);
			intent.putExtra("groupnumber", groupcode);
			startActivityForResult(intent, 1234);
		    } else if (alin.get(0).GroupPhoto.equalsIgnoreCase("1") && !isphotCaptured) {
			Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			startActivityForResult(cameraIntent, 111);
		    } else {
			Intent confmScreen = new Intent(Group_Members.this, ConfirmationScreen.class);
			confmScreen.putExtra("groupnumber", groupcode);
			startActivityForResult(confmScreen, 123);
		    }
		} else {
		    showAlertDailog("please take feedback for all members");
		}
	    }
	});
    }

    public void intializeControlles() {
	llGroups = (LinearLayout) inflater.inflate(R.layout.centers, null);
	lvGroups = (ListView) llGroups.findViewById(R.id.lvCenters);
	btnNext = (Button) llGroups.findViewById(R.id.btnNext);
	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llGroups, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
	if (type.equalsIgnoreCase("group")) {
	    btnNext.setVisibility(View.VISIBLE);
	}
        ivLogout.setVisibility(View.GONE);
	tvHeader.setText("Members");
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
	} else if (resultCode == 123456) {
	    RegularDemandsBLTemp regularDemandsBL = new RegularDemandsBLTemp();
	    alArrayList = regularDemandsBL.SelectAll(groupcode, "Groups",Group_Members.this);
	    reportsMembers_Adapter.refresh(alArrayList);
	    count++;
	    if (!type.equals("group") && Integer.parseInt(bl.getCompletedCount(groupcode, "group",Group_Members.this)) == 0) {
		Toast.makeText(Group_Members.this, "press back to select another group", Toast.LENGTH_LONG).show();
	    }
	} else if (requestCode == 111 && resultCode == RESULT_OK) {
	    // Toast.makeText(getApplicationContext(), data.toString(),
	    // Toast.LENGTH_LONG).show();
	    showAlertDailog("Photo Captured successfully", "Ok", new CustomDailoglistner() {

		@Override
		public void onPossitiveButtonClick(DialogInterface dialog) {
		    // TODO Auto-generated method stub
		    isphotCaptured = true;
		    Intent confm = new Intent(Group_Members.this, ConfirmationScreen.class);
		    confm.putExtra("groupnumber", groupcode);
		    startActivityForResult(confm, 123);
		}

		@Override
		public void onNegativeButtonClick(DialogInterface dialog) {
		    // TODO Auto-generated method stub

		}
	    });
	}
    }

    @Override
    public void onBackPressed() {
	// TODO Auto-generated method stub
	// if(!type.equalsIgnoreCase("group")){
	// if(count==alArrayList.size() || count>alArrayList.size()){
	// finish();
	// super.onBackPressed();
	// }
	// else
	// Toast.makeText(Group_Members.this, "please take feedback for another
	// members also", Toast.LENGTH_LONG).show();
	// }else{
	// super.onBackPressed();
	// }
	super.onBackPressed();
    }
}
