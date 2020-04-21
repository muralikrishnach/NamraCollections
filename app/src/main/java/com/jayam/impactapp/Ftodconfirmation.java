package com.jayam.impactapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.FtodreasonsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Ftodconfirmation extends Base {
    private LinearLayout llFTODCONFIRMATION;
    private EditText etremarksonOD, etdemisedate;
    private TextView tvdate, tvremarks;
    private String ReasonID, MLAID, TxnType;
    private Button btnftodconfirm;
    static final int DOF = 1111;
    static final int BADOpeningDate = 2;
    private int year;
    private int month;
    private int day;
    String Remarks = "0";
    String Date = "null";
    int cur = 0;
    private RegularDemandsDO regularDemandsDO_Original;
    private RegularDemandsBL regularDemandsBL;
    private ArrayList<RegularDemandsDO> alRegularDemandsDOs_Original;
    String paymnet, MemberName, MemberCode, DemandDate;

    @Override
    public void initialize() {
	ReasonID = getIntent().getExtras().getString("ReasonID");
	MLAID = getIntent().getExtras().getString("MLAID");
	TxnType = getIntent().getExtras().getString("TxnType");

	intializeControlles();
	regularDemandsBL = new RegularDemandsBL();
	alRegularDemandsDOs_Original = regularDemandsBL.SelectAll(MLAID, "memeber",Ftodconfirmation.this);
	regularDemandsDO_Original = alRegularDemandsDOs_Original.get(0);

	paymnet = regularDemandsDO_Original.savedAmt;
	MemberName = regularDemandsDO_Original.MemberName;
	MemberCode = regularDemandsDO_Original.MemberCode;
	DemandDate = regularDemandsDO_Original.DemandDate;

	/*
	 * final Calendar c = Calendar.getInstance(); year =
	 * c.get(Calendar.YEAR); month = c.get(Calendar.MONTH); day =
	 * c.get(Calendar.DAY_OF_MONTH);
	 */
	String[] tokens = DemandDate.split("-");

	for (int i = 0; i < tokens.length; i++) {

	    String dy = tokens[0];
	    String mn = tokens[1];
	    // Toast.makeText(getApplicationContext(), mn,
	    // Toast.LENGTH_LONG).show();
	    String yr = tokens[2];
	    day = Integer.parseInt(dy);
	    month = Integer.parseInt(mn);
	    month = month - 1;
	    year = Integer.parseInt(yr);
	    Log.v("year", "year" + year);
	    Log.v("month", "month" + month);
	    Log.v("day", "day" + day);

	}
	// StringTokenizer tokens = new StringTokenizer(DemandDate, "-");
	// String dy = tokens.nextToken();
	// String tokens2 = tokens.nextToken();
	// Toast.makeText(getApplicationContext(), tokens2,
	// Toast.LENGTH_LONG).show();
	/*
	 * StringTokenizer tokens3 = new StringTokenizer(tokens2, "-"); String
	 * mn = tokens3.nextToken(); String yr = tokens3.nextToken(); day =
	 * Integer.parseInt(dy); month = Integer.parseInt(mn); year =
	 * Integer.parseInt(yr);
	 */

	/*
	 * List<String> list = new ArrayList<String>(); Calendar calendar =
	 * Calendar.getInstance(); int year2 = calendar.get(Calendar.YEAR);
	 * for(int i=2000;i<=year2;i++) { if(i==2000) { list.add("Select");
	 * list.add(Integer.toString(i)); }else{ list.add(Integer.toString(i));
	 * } }
	 */

//	ivHome.setOnClickListener(new OnClickListener() {
//	    @Override
//	    public void onClick(View v) {
//		setResult(AppConstants.RESULTCODE_HOME);
//		finish();
//	    }
//	});
//
//	ivLogout.setOnClickListener(new OnClickListener() {
//	    @Override
//	    public void onClick(View v) {
//		Intent i = new Intent(Ftodconfirmation.this, loginActivity.class);
//		startActivity(i);
//		// setResult(AppConstants.RESULTCODE_LOGOUT);
//		// finish();
//	    }
//	});

	btnftodconfirm.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// Toast.makeText(getApplicationContext(), Date,
		// Toast.LENGTH_LONG).show();
		// if(Date.equals("null") || ReasonID.equals("1") ||
		// ReasonID.equals("2"))
		// {
		// showAlertDailog("Please Enter Demise Date.");
		// return;
		// }

		if (ReasonID.equals("1") || ReasonID.equals("2")) {
		    Date = etdemisedate.getText().toString();
		    if (Date.equals("")) {
			showAlertDailog("Please Enter Demise Date.");
			return;
		    }
		    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		    try {

			String str1 = DemandDate.replaceAll("-", "/");
			java.util.Date date1 = formatter.parse(str1);

			String str2 = Date;
			Log.v("str2", "str2" + str2);
			java.util.Date date2 = formatter.parse(str2);

			// else

			if (date1.compareTo(date2) < 0) {
			    showAlertDailog("Demise Date Should be less than or equal to demand Date.");
			    return;
			}

		    } catch (ParseException e1) {
			e1.printStackTrace();
		    }
		}

		showAlertDailog("OD Reason captured. Do you want to continue?", "Yes", "No", new CustomDailoglistner() {

		    @Override
		    public void onPossitiveButtonClick(DialogInterface dialog) {
			dialog.dismiss();

			Remarks = etremarksonOD.getText().toString();
			if (Remarks.equals("")) {
			    Remarks = "0";

			}
			FtodreasonsBL FTODBL = new FtodreasonsBL();
			FTODBL.updateFTODReasons(MLAID, ReasonID, Remarks, Date, "Member",Ftodconfirmation.this);
			FTODBL.updateFTODReasonsTemp(MLAID, ReasonID, Remarks, Date,Ftodconfirmation.this);
			Intent intent = new Intent(Ftodconfirmation.this, Paymentconfiramtion.class);
			Bundle bundle = new Bundle();
			bundle.putString("paymnet", "" + paymnet);
			bundle.putString("MemberName", "" + MemberName);
			bundle.putString("MemberCode", "" + MemberCode);
			bundle.putString("DemandDate", "" + DemandDate);
			bundle.putString("txntype", "" + TxnType);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);
		    }

		    @Override
		    public void onNegativeButtonClick(DialogInterface dialog) {
			dialog.dismiss();
		    }

		});
	    }

	});
	etdemisedate.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		showDialog(DOF);

	    }

	});
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	switch (id) {
	case DOF:
	    cur = DOF;
	    return new DatePickerDialog(this, pickerListener, year, month, day);
	case BADOpeningDate:
	    cur = BADOpeningDate;
	    // return new DatePickerDialog(this, pickerListener, year,
	    // month,day);

	}
	return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

	// when dialog box is closed, below method will be called.
	@Override
	public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

	    year = selectedYear;
	    month = selectedMonth;
	    day = selectedDay;
	    int months = getNumberOfMonths(year, month);
	    // etSCANoofMonth.setText(Integer.toString(months+1));
	    if (cur == DOF) {
		// Show selected date
		etdemisedate.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/")
			.append(year).append(" "));
	    } else {
		// etBADOpeningdate.setText(new
		// StringBuilder().append(month+1).append("-").append(day).append("-").append(year).append("
		// "));
	    }
	}
    };

    public int getNumberOfMonths(int selectedYear, int selectedMonth) {
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.MONTH, selectedMonth);
	calendar.set(Calendar.YEAR, selectedYear);

	Calendar calendar_current = Calendar.getInstance();
	long diff = calendar_current.getTimeInMillis() - calendar.getTimeInMillis();

	int months = (int) ((diff / (24 * 60 * 60 * 1000)) / 30);

	return months;

    }

    public void intializeControlles() {
	llFTODCONFIRMATION = (LinearLayout) inflater.inflate(R.layout.ftodconfirmation, null);
	etremarksonOD = (EditText) llFTODCONFIRMATION.findViewById(R.id.etremarksonOD);
	etdemisedate = (EditText) llFTODCONFIRMATION.findViewById(R.id.etdemisedate);
	tvdate = (TextView) llFTODCONFIRMATION.findViewById(R.id.tvdate);
	tvremarks = (TextView) llFTODCONFIRMATION.findViewById(R.id.tvremarks);
	btnftodconfirm = (Button) llFTODCONFIRMATION.findViewById(R.id.btnftodconfirm);

	svBase.setVisibility(View.GONE);
	llBaseMiddle_lv.setVisibility(View.VISIBLE);
	llBaseMiddle_lv.addView(llFTODCONFIRMATION, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	tvHeader.setText("Reasons");
	if (ReasonID.equals("1") || ReasonID.equals("2")) {
	    etremarksonOD.setVisibility(View.GONE);
	    tvremarks.setVisibility(View.GONE);
	} else {
	    etdemisedate.setVisibility(View.GONE);
	    tvdate.setVisibility(View.GONE);
	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode == AppConstants.RESULTCODE_GROPMEMBERS) {
	    setResult(AppConstants.RESULTCODE_GROPMEMBERS);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_GROPDETAILS) {
	    setResult(AppConstants.RESULTCODE_GROPDETAILS);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_LOGOUT) {
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_HOME) {
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_CENTERDETAILS) {
	    setResult(resultCode);
	    finish();
	}

    }

	@Override
	public void onBackPressed() {
		// Do Here what ever you want do on back press;
	}
}
