package com.jayam.impactapp;

import java.io.File;
import java.util.ArrayList;

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
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QomCenterWise extends Base {

    private LinearLayout llAttendense, llmembers;
    private Button btnNext;
    private String groupnumber;
    public boolean isphotCaptured = false;
    File sdImageMainDirectory;
    ArrayList<RegularDemandsDO> alregularDemandsBL;
    String reason;
    boolean exit = true;
    boolean isRenew = false;

    @Override
    public void initialize() {
	// TODO Auto-generated method stub
	intializeControlles();
	groupnumber = getIntent().getExtras().getString("groupnumber");
	final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",QomCenterWise.this);
	for (int i = 0; i < alregularDemandsBL.size(); i++) {
	    if (alregularDemandsBL.get(i).Renew.equals("Y")) {
		isRenew = true;
		i = alregularDemandsBL.size();
	    }
	}
	IntialParametrsBL inpbl = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(QomCenterWise.this);
	if (alin.get(0).groupDiscipline.equals("1") || alin.get(0).collExp.equals("1") || isRenew) {
	    btnNext.setText("Next");
	} else {
	    btnNext.setText("Save");
	}
	tvHeader.setText(alregularDemandsBL.get(0).GroupName + " Qom");
	File root = new File(
		Environment.getExternalStorageDirectory() + File.separator + AppConstants.FolderName + File.separator);
	if (!root.exists()) {
	    root.mkdirs();
	}
	final String LastTranScode = StringUtils.getTransactionCode_G(alregularDemandsBL.get(0),QomCenterWise.this);
	sdImageMainDirectory = new File(root, LastTranScode + ".JPEG");
	// member view
	for (int i = 0; i < alregularDemandsBL.size(); i++) {
	    if (alregularDemandsBL.get(i).Attendance.equals("1")) {
		View memitem = inflater.inflate(R.layout.memberratingitem, null);
		memitem.setId(i);
		Log.d("mfimo", memitem.getId() + "");
		TextView tvmemname = (TextView) memitem.findViewById(R.id.tvmemname);
		EditText tvmemrat = (EditText) memitem.findViewById(R.id.etmemrating);
		tvmemname.setText(
			alregularDemandsBL.get(i).MemberName + "(" + alregularDemandsBL.get(i).MemberCode + ")");
		tvmemrat.setText(alregularDemandsBL.get(i).qom);
		llmembers.addView(memitem);
	    }
	}

	btnNext.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		UpdateQom updateQom = new UpdateQom(QomCenterWise.this, new Updatelistner() {
		    @Override
		    public void onUpdate() {
			HideLoader();
			if (btnNext.getText().toString().equals("Next")) {
			    IntialParametrsBL inpbl = new IntialParametrsBL();
			    ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(QomCenterWise.this);
			    if (alin.get(0).groupDiscipline.equalsIgnoreCase("1")) {
				Intent intent = new Intent(QomCenterWise.this, GroupDisciplineCW.class);
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 12345);
			    } else if (alin.get(0).collExp.equalsIgnoreCase("1")) {
				Intent intent = new Intent(QomCenterWise.this, CollExperianceGW.class);
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 12345);
			    } else if (isRenew) {
				Intent intent = new Intent(QomCenterWise.this, RenewGroupWise.class);
				intent.putExtra("type", getIntent().getExtras().getString("type"));
				intent.putExtra("groupnumber", groupnumber);
				startActivityForResult(intent, 12345);
			    }
			} else {
			    setResult(12345);
			    finish();
			}
		    }
		});
		for (int i = 0; i < alregularDemandsBL.size(); i++) {
		    if (alregularDemandsBL.get(i).Attendance.equals("1")) {
			View view = llmembers.findViewById(i);
			EditText etmemrating = (EditText) view.findViewById(R.id.etmemrating);
			if (etmemrating.getText().toString().equals("")) {
			    reason = "notext";
			    exit = false;
			    i = alregularDemandsBL.size();
			} else if (Integer.parseInt(etmemrating.getText().toString().trim()) > 10) {
			    reason = "over";
			    exit = false;
			    i = alregularDemandsBL.size();
			}
		    }
		}
		if (exit) {
		    ShowLoader();
		    updateQom.start();
		} else {
		    if (reason.equals("notext")) {
			showAlertDailog("Rating have to give for each member");
			exit = true;
		    } else if (reason.equals("over")) {
			showAlertDailog("Rating should between 0 to 10");
			exit = true;
		    }
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

	ivLogout.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(QomCenterWise.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});
    }

    private void intializeControlles() {
	// TODO Auto-generated method stub
	llAttendense = (LinearLayout) inflater.inflate(R.layout.qom, null);
	llmembers = (LinearLayout) llAttendense.findViewById(R.id.llmembers);
	btnNext = (Button) llAttendense.findViewById(R.id.btnNext);
	svBase.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	tvHeader.setText("Qom Members");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	Log.d("mfimo", resultCode + "qom");
	if (resultCode == 12345) {
	    Log.d("mfimo", "qom finish");
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_LOGOUT) {
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
		    Intent confm = new Intent(QomCenterWise.this, ConfirmationScreen.class);
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
	    RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
	    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	    for (int i = 0; i < alregularDemandsBL.size(); i++) {
		if (alregularDemandsBL.get(i).Attendance.equals("1")) {
		    View v = llmembers.findViewById(i);
		    EditText etmemrating = (EditText) v.findViewById(R.id.etmemrating);
		    regularDemandsBLTemp.updateQom(etmemrating.getText().toString().trim(), alregularDemandsBL.get(i).MLAI_ID,QomCenterWise.this);
		    regularDemandsBL.updateQom(etmemrating.getText().toString().trim(), alregularDemandsBL.get(i).MLAI_ID,QomCenterWise.this);
		}
	    }
	    if (btnNext.getText().toString().equalsIgnoreCase("Save")) {
		regularDemandsBL.updatesavefb(groupnumber, "group",QomCenterWise.this);
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
