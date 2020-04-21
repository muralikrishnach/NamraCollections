package com.jayam.impactapp;

import com.jayam.impactapp.common.CustomDailoglistner;

import com.prowesspride.api.Setup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public abstract class Base extends Activity {
    public TextView tvHeader;
    public LinearLayout llBaseMiddle, llBaseMiddle_lv;
    public LayoutInflater inflater;
    public ProgressDialog progressDialog;
    public AlertDialog.Builder builder;
    public ImageView ivHome, ivLogout;
    public ScrollView svBase;
    public boolean setup = false;
    private LinearLayout llLogin;

    public static Setup impressSetUp = null;
    public static Context ct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	try {
	    impressSetUp = new Setup();
	    ct = this;
	    boolean activate = impressSetUp.blActivateLibrary(Base.this, R.raw.licenceffsl);
	    if (activate == true) {
		Log.d("Impress", "Library Activated......");
	    } else if (activate == false) {
		Log.d("Impress", "Library Not Activated...");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	setContentView(R.layout.base);
	inflater = getLayoutInflater();
	llBaseMiddle = (LinearLayout) findViewById(R.id.llBaseMiddle);
	tvHeader = (TextView) findViewById(R.id.tvHeader);
	ivHome = (ImageView) findViewById(R.id.ivHome);
	ivLogout = (ImageView) findViewById(R.id.ivLogout);
	svBase = (ScrollView) findViewById(R.id.svBase);
	llBaseMiddle_lv = (LinearLayout) findViewById(R.id.llBaseMiddle_lv);

	builder = new AlertDialog.Builder(Base.this);
	initialize();
    }

    public abstract void initialize();

    public void ShowLoader() {
	runOnUiThread(runShowLoader);
    }

    Runnable runShowLoader = new Runnable() {
	@Override
	public void run() {
	    try {
		HideLoader();
		progressDialog = ProgressDialog.show(Base.this, "", "Please wait...");
		// progressDialog.setCancelable(false);
		WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();
		lp.dimAmount = 0.0f;
		progressDialog.getWindow().setAttributes(lp);
	    } catch (Exception e) {
		Log.e("BaseActivity", "Error in ShowLoader()" + e.toString());
	    }
	}
    };

    public void HideLoader() {
	runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    /*
		     * if(progressDialog !=null && progressDialog.isShowing())
		     * progressDialog.dismiss();
		     */
		    if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		    }
		} catch (Exception e) {
		    Log.e("HideLoaoder()", e.toString());
		}
	    }
	});
    }

    public void showAlertDailog(String Message) {
	builder = new AlertDialog.Builder(Base.this);
	builder.setMessage(Message);
	builder.setCancelable(false);
	builder.setPositiveButton("Ok", new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();

	    }
	}).create().show();
    }

    public void showAlertDailog1(String Message) {
	builder = new AlertDialog.Builder(Base.this);
	builder.setMessage(Message);
	builder.setCancelable(false);
	builder.setPositiveButton("Ok", new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
		finish();
	    }
	}).create().show();
    }

    public void showAlertDailog(String Message, String postiveMessage, String negativeMessage,
	    final CustomDailoglistner listner) {
	builder = new AlertDialog.Builder(Base.this);
	builder.setMessage(Message);
	builder.setCancelable(false);
	builder.setPositiveButton(postiveMessage, new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		listner.onPossitiveButtonClick(dialog);
	    }
	});

	if (negativeMessage != null) {
	    builder.setNegativeButton(negativeMessage, new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
		    listner.onNegativeButtonClick(dialog);
		}
	    });
	}

	builder.create().show();
    }

    public void showAlertDailog(String Message, String postiveMessage, final CustomDailoglistner listner) {
	builder = new AlertDialog.Builder(Base.this);
	builder.setMessage(Message);
	builder.setCancelable(false);
	builder.setPositiveButton(postiveMessage, new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		listner.onPossitiveButtonClick(dialog);
	    }
	});

	builder.create().show();
    }

    public void showHomeIcons() {
	ivHome.setVisibility(View.VISIBLE);
	ivLogout.setVisibility(View.VISIBLE);
    }

    public void hidehomeIcons() {
	ivHome.setVisibility(View.GONE);
	ivLogout.setVisibility(View.GONE);
    }
}
