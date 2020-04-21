package com.jayam.impactapp;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.utils.SharedPrefUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class Administration extends Base {
    private LinearLayout llAdmin;
    Button btnODData, btnLUCData, btnResetParameter;

    @Override
    public void initialize() {
	initializeControlles();
	btnResetParameter.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (SharedPrefUtils.getKeyValue(Administration.this, AppConstants.pref_name, AppConstants.username)
			.equalsIgnoreCase("admin")
			|| SharedPrefUtils
				.getKeyValue(Administration.this, AppConstants.pref_name, AppConstants.username)
				.contains("admin")) {
		    Intent intent = new Intent(Administration.this, ResetParameters.class);
		    startActivity(intent);
		} else {
		    showAlertDailog("You are not authorized.");
		}
	    }
	});
	ivHome.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		finish();
	    }
	});

	ivLogout.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(Administration.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();

	    }
	});

	btnODData.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Transaction_OD_BL transaction_OD_BL = new Transaction_OD_BL();
		int count = Integer.parseInt(transaction_OD_BL.getrowCount(Administration.this));
		if (count == 0) {
		    showAlertDailog("Are you Sure to delete the OD Data?", "OK", "Cancel", new CustomDailoglistner() {
			@Override
			public void onPossitiveButtonClick(DialogInterface dialog) {
			    ODDemandsBL demandsBL = new ODDemandsBL();
			    demandsBL.Truncatetabel(Administration.this);
			    ;
			    dialog.dismiss();
			    ;

			    showAlertDailog("OD Data Successfully Purged.");
			}

			@Override
			public void onNegativeButtonClick(DialogInterface dialog) {
			    dialog.dismiss();
			}
		    });

		} else {
		    showAlertDailog("Please Upload OD Transactions before Purge.");
		}
	    }
	});


    }

    public void initializeControlles() {
	llAdmin = (LinearLayout) inflater.inflate(R.layout.administration, null);
	btnODData = (Button) llAdmin.findViewById(R.id.btnODData);
	btnLUCData = (Button) llAdmin.findViewById(R.id.btnLUCData);
	btnResetParameter = (Button) llAdmin.findViewById(R.id.btnResetParameter);
	llBaseMiddle.addView(llAdmin, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	tvHeader.setText(getResources().getString(R.string.administration));
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
    }

}
