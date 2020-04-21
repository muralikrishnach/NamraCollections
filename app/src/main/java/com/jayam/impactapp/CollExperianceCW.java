package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.objects.IntialParametrsDO;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CollExperianceCW extends Base {
    private LinearLayout llAttendense;
    private Button btnNext;
    private RadioGroup rgGroup;
    private String groupnumber, centernumber;
    Intent ii;
    public boolean isphotCaptured = false;
    File sdImageMainDirectory;
    ArrayList<RegularDemandsDO> alregularDemandsBL;
    String problem = "S";
    boolean isRenew = false;

    @Override
    public void initialize() {
	// TODO Auto-generated method stub
	intializeControlles();
	groupnumber = getIntent().getExtras().getString("groupnumber");
	RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",CollExperianceCW.this);
	for (int i = 0; i < alregularDemandsBL.size(); i++) {
	    Log.d("mfimo", alregularDemandsBL.get(i).Renew);
	    if (alregularDemandsBL.get(i).Renew.equals("Y")) {
		isRenew = true;
		i = alregularDemandsBL.size();
	    }
	}
	if (isRenew) {
	    btnNext.setText("Next");
	} else {
	    btnNext.setText("Save");
	}
	if (alregularDemandsBL.get(0).collExp != null && alregularDemandsBL.get(0).collExp.equals("D")) {
	    rgGroup.check(R.id.rgbtdel);
	    problem = "O";
	} else if (alregularDemandsBL.get(0).collExp != null && alregularDemandsBL.get(0).collExp.equals("H")) {
	    rgGroup.check(R.id.rgbthard);
	    problem = "D";
	} else if (alregularDemandsBL.get(0).collExp != null && alregularDemandsBL.get(0).collExp.equals("V")) {
	    rgGroup.check(R.id.rgbtvhard);
	    problem = "N";
	}
	rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.rgbtsel:
		    problem = "S";
		    break;
		case R.id.rgbtdel:
		    problem = "D";
		    break;
		case R.id.rgbthard:
		    problem = "H";
		    break;
		case R.id.rgbtvhard:
		    problem = "V";
		    break;
		}
	    }
	});
	ivHome.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		setResult(AppConstants.RESULTCODE_HOME);
		finish();
	    }
	});
//	ivLogout.setOnClickListener(new OnClickListener() {
//	    @Override
//	    public void onClick(View v) {
//		Intent i = new Intent(CollExperianceCW.this, loginActivity.class);
//		startActivity(i);
//		// setResult(AppConstants.RESULTCODE_LOGOUT);
//		// finish();
//	    }
//	});
	btnNext.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		UpdateCollExp updateQom = new UpdateCollExp(CollExperianceCW.this, new Updatelistner() {
		    @Override
		    public void onUpdate() {
			HideLoader();
			if (btnNext.getText().toString().equals("Next")) {
			    IntialParametrsBL inpbl = new IntialParametrsBL();
			    ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(CollExperianceCW.this);
			    if (isRenew) {
				Intent intent = new Intent(CollExperianceCW.this, RenewCenterWise.class);
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 12345);
			    }
			} else {
			    setResult(12345);
			    finish();
			}
		    }
		});
		if (!problem.equalsIgnoreCase("S")) {
		    ShowLoader();
		    updateQom.start();
		} else {
		    showAlertDailog("Please Select One option");
		}
	    }
	});
    }

    private void intializeControlles() {
	// TODO Auto-generated method stub
	llAttendense = (LinearLayout) inflater.inflate(R.layout.ratecollexp, null);
	btnNext = (Button) llAttendense.findViewById(R.id.btnNext);
	rgGroup = (RadioGroup) llAttendense.findViewById(R.id.rggroup);
	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
	tvHeader.setText("Collection Experience");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	Log.d("mfimo", resultCode + "exp");
	if (resultCode == 12345) {
	    Log.d("mfimo", "exp finish");
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_LOGOUT) {
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_HOME) {
	    setResult(resultCode);
	    finish();
	}
    }

    class UpdateCollExp extends Thread {
	Context context;
	Updatelistner updatelistner;

	public UpdateCollExp(Context context, Updatelistner updatelistner) {
	    this.context = context;
	    this.updatelistner = updatelistner;
	}

	@Override
	public void run() {
	    super.run();
	    RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
	    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	    regularDemandsBL.updateCollExp(groupnumber, problem, "Group",CollExperianceCW.this);
	    regularDemandsBLTemp.updateCollExp(groupnumber, problem, "Group",CollExperianceCW.this);
	    if (btnNext.getText().toString().equalsIgnoreCase("Save")) {
		regularDemandsBL.updatesavefb(groupnumber, "group",CollExperianceCW.this);
	    }
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
