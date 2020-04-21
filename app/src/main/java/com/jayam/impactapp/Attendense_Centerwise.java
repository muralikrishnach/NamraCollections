package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.AttendenseAdapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
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
import android.widget.ListView;
import android.widget.TextView;

public class Attendense_Centerwise extends Base {
    private LinearLayout llAttendense;
    private AttendenseAdapter attendenseAdapter;
    private ListView lvAttendense;
    // private CheckBox cbSelectAll;
    private String groupnumber;
    private Button btnNext;
    private String att, gli, lat;// ,meetingtime, groupphoto;
    private TextView tvAttendance, tvGli, tvLate;
    public boolean isphotCaptured = false;

    @Override
    public void initialize() {
	intializeControlles();
	groupnumber = getIntent().getExtras().getString("groupnumber");
	RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	final ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",Attendense_Centerwise.this);

	att = getIntent().getExtras().getString("att");
	gli = getIntent().getExtras().getString("gli");
	lat = getIntent().getExtras().getString("lat");
	// meetingtime = getIntent().getExtras().getString("meetingtime");
	// groupphoto = getIntent().getExtras().getString("groupphoto");
	Log.e("att", "" + att);
	Log.e("gli", "" + gli);
	Log.e("lat", "" + lat);
	Log.e("aSize", "" + alregularDemandsBL.size());
	attendenseAdapter = new AttendenseAdapter(Attendense_Centerwise.this, alregularDemandsBL, att, gli, lat);
	lvAttendense.setAdapter(attendenseAdapter);

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
		Intent i = new Intent(Attendense_Centerwise.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	btnNext.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {

		ShowLoader();
		UpdateAttendense_C attendense_C = new UpdateAttendense_C(Attendense_Centerwise.this,
			new Updatelistner_C() {

		    @Override
		    public void onUpdate() {
			HideLoader();
			finish();
		    }
		});
		attendense_C.start();

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

	btnNext.setText("Save");
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
	} else if (requestCode == 111) {
	    isphotCaptured = true;
	}
    }

    class UpdateAttendense_C extends Thread {
	Context context;
	Updatelistner_C updatelistner;

	public UpdateAttendense_C(Context context, Updatelistner_C updatelistner) {
	    this.context = context;
	    this.updatelistner = updatelistner;
	}

	@Override
	public void run() {
	    super.run();

	    RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();

	    regularDemandsBLTemp.updateAttendense(attendenseAdapter.getUpDatedList(),Attendense_Centerwise.this);
	    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();

	    regularDemandsBL.updateAttendense(attendenseAdapter.getUpDatedList(),Attendense_Centerwise.this);
	    ((Activity) context).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    updatelistner.onUpdate();
		}
	    });

	}
    }

    interface Updatelistner_C {
	public abstract void onUpdate();
    }

}
