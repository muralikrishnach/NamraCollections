package com.jayam.impactapp;

import java.util.ArrayList;


import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.utils.SharedPrefUtils;


import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Transactions extends Baselogin {
    private LinearLayout llTranscations;
    private ImageView btnRegularCollections, btnODCollections, btnADVCollections, btnLUCDemands,btnlatecollection,btnCashless;
    String count;
    private ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    private IntialParametrsBL intialParametrsBL;
    PrintDetailsDO detailsDO;

    @Override
    public void initialize() {
	intializeControlles();
	detailsDO = new PrintDetailsDO();
	intialParametrsBL = new IntialParametrsBL();
	alIntialParametrsDOs = intialParametrsBL.SelectAll(Transactions.this);
	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
	String PAdd = intialParametrsDO.BTPrinterAddress;
	String serverurl = intialParametrsDO.ServerUrl;
	String[] URL = serverurl.split("/");
	String name = URL[URL.length - 1];
	String PrinterAddress = null;
	for (int x = 0; x < PAdd.length(); x++) {
	    char PBT = PAdd.charAt(x);
	    if (x == 2 || x == 4 || x == 6 || x == 8 || x == 10) {
		PrinterAddress = PrinterAddress + ":" + String.valueOf(PBT);
	    } else {
		if (x == 0) {
		    PrinterAddress = String.valueOf(PBT);
		} else {
		    PrinterAddress = PrinterAddress + "" + String.valueOf(PBT);
		}

	    }
	}
	// PrinterAddress="00:04:3E:31:C1:6F";
	detailsDO.printer_address = PrinterAddress;
	Log.d("mfimo", "printer address from server" + PrinterAddress);
	KeyValue keyValue_printeraddress = new KeyValue(AppConstants.printeraddress, PrinterAddress);
	SharedPrefUtils.setValue(Transactions.this, AppConstants.pref_name, keyValue_printeraddress);
	if (name.equals("lokyblmobile") || name.equals("lokybltest1") || name.equals("lokybltest2")
		|| name.equals("mfimotest")) {
	    KeyValue keyValue_Urladdress = new KeyValue(AppConstants.UrlAddress, "Yes");
	    SharedPrefUtils.setValue(Transactions.this, AppConstants.pref_name, keyValue_Urladdress);
	} else {
	    KeyValue keyValue_Urladdress = new KeyValue(AppConstants.UrlAddress, "No");
	    SharedPrefUtils.setValue(Transactions.this, AppConstants.pref_name, keyValue_Urladdress);
	}

	ivHome.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		finish();
	    }
	});

	ivLogout.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(Transactions.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});

	btnRegularCollections.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		new LoadRegularCollections().execute();

	    }
	});

//	btnLUCDemands.setOnClickListener(new OnClickListener() {
//	    @Override
//	    public void onClick(View v) {
//		new LoadLUCCollections().execute();
//
//	    }
//	});

	btnODCollections.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		ODDemandsBL odDemandsBL = new ODDemandsBL();
		String count = odDemandsBL.SelectCount(Transactions.this);
		if (Integer.parseInt(count) == 0) {
		    showAlertDailog("No OD Data available");
		} else {
		    Intent intent = new Intent(Transactions.this, Centers_OD.class);
		    startActivityForResult(intent, 12345);
		}
	    }
	});
	btnADVCollections.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		AdvanceDemandBL regularDemandsBL = new AdvanceDemandBL();
		String count = regularDemandsBL.SelectCount(Transactions.this);
		if (Integer.parseInt(count) == 0) {
		    showAlertDailog("No Advance Data available");
		} else {
		    Intent intent = new Intent(Transactions.this, AdvGroupsAndCenters.class);
		    startActivityForResult(intent, 12345);
		}
	    }
	});


		btnlatecollection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
				String count = regularDemandsBL.SelectCountLate(Transactions.this);
				Log.v("","Latecount"+count);
				if(count!=null)
				{
					try
					{
						if (Integer.parseInt(count) == 0) {
							showAlertDailog("No Late Collection Data available");
						} else {
							Intent intent = new Intent(Transactions.this, LateCenters.class);
							startActivityForResult(intent, 12345);
						}
					}catch (NumberFormatException n)
					{
						n.printStackTrace();
					}
				}


			}
		});

    }

    @SuppressWarnings("deprecation")
    public void intializeControlles() {
	llTranscations = (LinearLayout) inflater.inflate(R.layout.transactions1, null);
	btnRegularCollections = (ImageView) llTranscations.findViewById(R.id.btnRegularCollections);
	btnODCollections = (ImageView) llTranscations.findViewById(R.id.btnODCollections);
	btnADVCollections = (ImageView) llTranscations.findViewById(R.id.btnADVCollections);
		btnlatecollection = (ImageView) llTranscations.findViewById(R.id.btnALateollections);
//	btnLUCDemands = (Button) llTranscations.findViewById(R.id.btnLUCDemands);
//		btnCashless = (Button) llTranscations.findViewById(R.id.btncashless);

	llBaseMiddle.addView(llTranscations, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	tvHeader.setText(getResources().getString(R.string.transactions));
	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
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
	}
    }

    class LoadRegularCollections extends AsyncTask<Void, Void, Void> {

	@Override
	protected void onPreExecute() {
	    ShowLoader();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
	    final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	    count = regularDemandsBL.SelectCount(Transactions.this);
	    return null;
	}

	@Override
	protected void onPostExecute(Void result) {
	    // TODO Auto-generated method stub
	    HideLoader();
	    if (Integer.parseInt(count) == 0) {
		showAlertDailog("No Regular Data available");
	    } else {
		Intent intent = new Intent(Transactions.this, RegularCollections.class);
		startActivityForResult(intent, 12345);
	    }
	}
    }


}
