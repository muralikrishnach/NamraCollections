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
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ProbInCenterCW extends Base {

    private LinearLayout llAttendense;
    private Button btnNext;
    private RadioGroup rgGroup;
    private String groupnumber, centernumber;
    Intent ii;
    public boolean isphotCaptured = false;
    File sdImageMainDirectory;
    ArrayList<RegularDemandsDO> alregularDemandsBL;
    String problem = "S", from = null;
    boolean isRenew = false;

    @Override
    public void initialize() {
	// TODO Auto-generated method stub
	from = getIntent().getExtras().getString("from");
	centernumber = getIntent().getExtras().getString("centernumber");
	intializeControlles();
	ii = getIntent();
	File root = new File(
		Environment.getExternalStorageDirectory() + File.separator + AppConstants.FolderName + File.separator);
	if (!root.exists()) {
	    root.mkdirs();
	}
	final String LastTranScode;
	RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	alregularDemandsBL = regularDemandsBL.SelectAll(centernumber, "CNo",ProbInCenterCW.this);
	for (int i = 0; i < alregularDemandsBL.size(); i++) {
	    if (alregularDemandsBL.get(i).Renew.equals("Y")) {
		isRenew = true;
		i = alregularDemandsBL.size();
	    }
	}
	if (alregularDemandsBL.get(0).probInCenter != null && alregularDemandsBL.get(0).probInCenter.equals("Y")) {
	    rgGroup.check(R.id.rgbtyes);
	    problem = "Y";
	} else if (alregularDemandsBL.get(0).probInCenter != null
		&& alregularDemandsBL.get(0).probInCenter.equals("N")) {
	    rgGroup.check(R.id.rgbtno);
	    problem = "N";
	}
	LastTranScode = StringUtils.getTransactionCode_C(alregularDemandsBL.get(0),ProbInCenterCW.this);
	sdImageMainDirectory = new File(root, LastTranScode + ".JPEG");
	rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.rgbtsel:
		    problem = "S";
		    break;
		case R.id.rgbtyes:
		    problem = "Y";
		    break;
		case R.id.rgbtno:
		    problem = "N";
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
	ivLogout.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(ProbInCenterCW.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	btnNext.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (from != null && from.equalsIgnoreCase("ftod")) {
		    UpdatePic updateQom = new UpdatePic(ProbInCenterCW.this, new Updatelistner() {
				@Override
				public void onUpdate() {
					setResult(AppConstants.RESULTCODE_HOME);
				    finish();
				}
			    });
			    if (!problem.equals("S")) {
				ShowLoader();
				updateQom.start();
			    } else {
				showAlertDailog("Please Select One option");
			    }
		} else if (from != null && from.equalsIgnoreCase("ftodforward")) {
		    UpdatePic updateQom = new UpdatePic(ProbInCenterCW.this, new Updatelistner() {
			@Override
			public void onUpdate() {
			    Intent i = new Intent(ProbInCenterCW.this, Ftodreasonscenterwise.class);
			    i.putExtra("CCode", centernumber);
			    i.putExtra("Type", "Center");
			    startActivityForResult(i, 111);
			}
		    });
		    if (!problem.equals("S")) {
			ShowLoader();
			updateQom.start();
		    } else {
			showAlertDailog("Please Select One option");
		    }
		} else {
		    UpdatePic updateQom = new UpdatePic(ProbInCenterCW.this, new Updatelistner() {
			@Override
			public void onUpdate() {
			    HideLoader();
			    IntialParametrsBL inpbl = new IntialParametrsBL();
			    ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(ProbInCenterCW.this);
			    if (alin.get(0).groupDiscipline.equalsIgnoreCase("1")
				    || alin.get(0).collExp.equalsIgnoreCase("1") || isRenew) {
				Intent intent = new Intent(ProbInCenterCW.this, QomCenters.class);
				intent.putExtra("centernumber", centernumber);
				startActivityForResult(intent, 1234);
			    } else if (alin.get(0).MeetingTime.equalsIgnoreCase("1")) {
				Intent intent = new Intent(ProbInCenterCW.this, MeetingTime_Centerwise.class);
				intent.putExtra("centernuber", centernumber);
				startActivityForResult(intent, 1234);
			    } else if (alin.get(0).GroupPhoto.equalsIgnoreCase("1") && !isphotCaptured) {
				Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(cameraIntent, 111);
			    } else {
				Intent confmScreen = new Intent(ProbInCenterCW.this,
					ConfirmationScreen_Centerwise.class);
				confmScreen.putExtra("centernuber", centernumber);
				startActivityForResult(confmScreen, 123);
			    }
			}
		    });
		    if (!problem.equals("S")) {
			ShowLoader();
			updateQom.start();
		    } else {
			showAlertDailog("Please Select One option");
		    }
		}
	    }
	});
    }

    private void intializeControlles() {
	// TODO Auto-generated method stub
	llAttendense = (LinearLayout) inflater.inflate(R.layout.probincenter, null);
	rgGroup = (RadioGroup) llAttendense.findViewById(R.id.rgGroup);
	btnNext = (Button) llAttendense.findViewById(R.id.btnNext);
	svBase.setVisibility(View.INVISIBLE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llAttendense, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	tvHeader.setText("MFIMO");
	if (from != null && from.equalsIgnoreCase("ftod")) {
	    btnNext.setText("Save");
	}
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
		    Intent confmScreen = new Intent(ProbInCenterCW.this, ConfirmationScreen_Centerwise.class);
		    confmScreen.putExtra("centernuber", centernumber);
		    startActivityForResult(confmScreen, 123);
		}

		@Override
		public void onNegativeButtonClick(DialogInterface dialog) {
		    // TODO Auto-generated method stub

		}
	    });
	}
    }

    class UpdatePic extends Thread {
	Context context;
	Updatelistner updatelistner;

	public UpdatePic(Context context, Updatelistner updatelistner) {
	    this.context = context;
	    this.updatelistner = updatelistner;
	}

	@Override
	public void run() {
	    super.run();
	    RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
	    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	    regularDemandsBL.updatePic(centernumber, problem, "Center",ProbInCenterCW.this);
	    regularDemandsBLTemp.updatePic(centernumber, problem, "Center",ProbInCenterCW.this);
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
