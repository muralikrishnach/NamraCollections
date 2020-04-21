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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QomMemberWise extends Base {

    private LinearLayout llAttendense;
    private Button btnNext;
    TextView tvName;
    EditText etRating;
    private String memcode, memname;
    public boolean isphotCaptured = false;
    File sdImageMainDirectory;
    ArrayList<RegularDemandsDO> alregularDemandsBL;
    String reason;
    boolean exit = true;
    boolean isRenew = false;

    @Override
    public void initialize() {
	// TODO Auto-generated method stub
	memcode = getIntent().getExtras().getString("memcode");
	memname = getIntent().getExtras().getString("memname");
	intializeControlles();
	tvName.setText(memname);
	final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectAll(memcode, "memeber",QomMemberWise.this);
	etRating.setText(alregularDemandsBL.get(0).qom);
	IntialParametrsBL inpbl = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(QomMemberWise.this);
	if (alin.get(0).collExpRMEL.equalsIgnoreCase("1") || alin.get(0).collPlace.equalsIgnoreCase("1")
		|| alin.get(0).collExp.equalsIgnoreCase("1") || alin.get(0).repaymentMadeBy.equalsIgnoreCase("1")) {
	    btnNext.setText("Next");
	} else {
	    btnNext.setText("Save");
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
		Intent i = new Intent(QomMemberWise.this, loginActivity.class);
		startActivity(i);

		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});
	btnNext.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		UpdateQom updateQom = new UpdateQom(QomMemberWise.this, new Updatelistner() {
		    @Override
		    public void onUpdate() {
			HideLoader();
			if (btnNext.getText().toString().equalsIgnoreCase("Next")) {
			    IntialParametrsBL inpbl = new IntialParametrsBL();
			    ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(QomMemberWise.this);
			    if (alin.get(0).collExpRMEL.equalsIgnoreCase("1")) {
				Intent intent = new Intent(QomMemberWise.this, CollExpRmel.class);
				intent.putExtra("memcode", memcode);
				intent.putExtra("memname", memname);
				startActivityForResult(intent, 123456);
			    } else if (alin.get(0).collPlace.equalsIgnoreCase("1")) {
				Intent intent = new Intent(QomMemberWise.this, CollectionPlace.class);
				intent.putExtra("memcode", memcode);
				intent.putExtra("memname", memname);
				startActivityForResult(intent, 123456);
			    } else if (alin.get(0).collExp.equalsIgnoreCase("1")) {
				Intent intent = new Intent(QomMemberWise.this, CollExperianceMW.class);
				intent.putExtra("memcode", memcode);
				intent.putExtra("memname", memname);
				startActivityForResult(intent, 123456);
			    } else if (alin.get(0).repaymentMadeBy.equalsIgnoreCase("1")) {
				Intent intent = new Intent(QomMemberWise.this, RepaymentMadeBy.class);
				intent.putExtra("memcode", memcode);
				intent.putExtra("memname", memname);
				startActivityForResult(intent, 123456);
			    }
			} else {
			    setResult(123456);
			    finish();
			}
		    }
		});
		if (etRating.getText().toString().equals("")) {
		    showAlertDailog("Rating have to give for each member");
		} else if (Integer.parseInt(etRating.getText().toString().trim()) > 10) {
		    showAlertDailog("Rating should between 0 to 10");
		} else {
		    ShowLoader();
		    updateQom.start();
		}
	    }
	});
    }

    private void intializeControlles() {
	// TODO Auto-generated method stub
	llAttendense = (LinearLayout) inflater.inflate(R.layout.qommember, null);
	tvName = (TextView) llAttendense.findViewById(R.id.tvmemname);
	etRating = (EditText) llAttendense.findViewById(R.id.etmemrating);
	btnNext = (Button) llAttendense.findViewById(R.id.btnNext);
	svBase.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
	tvHeader.setText("Member Rating");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	Log.d("mfimo", resultCode + "exp");
	if (resultCode == 123456) {
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

    class UpdateQom extends Thread {
	Context context;
	Updatelistner updatelistner;

	public UpdateQom(Context context, Updatelistner updatelistner) {
	    this.context = context;
	    this.updatelistner = updatelistner;
	}

	@Override
	public void run() {
	    super.run();
	    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	    RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
	    regularDemandsBL.updateQom(etRating.getText().toString().trim(), memcode,QomMemberWise.this);
	    regularDemandsBLTemp.updateQom(etRating.getText().toString().trim(), memcode,QomMemberWise.this);
	    if (btnNext.getText().toString().equalsIgnoreCase("Save")) {
		regularDemandsBL.updatesavefb(memcode, "member",QomMemberWise.this);
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
