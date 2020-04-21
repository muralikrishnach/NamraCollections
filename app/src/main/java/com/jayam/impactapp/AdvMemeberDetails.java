package com.jayam.impactapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.JsonObject;

import com.jayam.LocationUtil.PermissionUtils;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.utils.DialogUtils;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.UtilsClass;
import com.matm.matmsdk.aepsmodule.AEPSHomeActivity;
import com.matm.matmsdk.aepsmodule.utils.AepsSdkConstants;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class AdvMemeberDetails extends Base implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback,PermissionUtils.PermissionResultCallback {
	RadioButton cashwithdrae,balancewnq;
	RadioGroup trantype;
	LinearLayout Translayout;
	public	String dlatitude, dlangitude;
	ImageView buttoncall,btnlocation;

	private IntialParametrsBL intialParametrsBL;
	MediaPlayer mPlayer;
	String Responseda="";
	String Ttype="";

//
//	String merchantId = "Dillipa";
//	String password = "Ampl1234";
LinearLayout Aadhralayout;


	String UserId,MemberId,LUserID,BranchID,Branchname="";

	private final static int PLAY_SERVICES_REQUEST = 1000;
	private final static int REQUEST_CHECK_SETTINGS = 2000;

	private Location mLastLocation;

	// Google client to interact with Google API

	private GoogleApiClient mGoogleApiClient;
	boolean isPermissionGranted;
	ArrayList<String> permissions = new ArrayList<>();
	private static final String TAG = MemeberDetails.class.getSimpleName();
	PermissionUtils permissionUtils;
	String[] paymentarr = new String[]{"Cash", "AEPS", "matm", "pg", "upi"};
	public static final int RequestPermissionCode = 1;
	double latitude = 0.0;
	double longitude = 0.0;
	private String collectedAmt;
//	String merchantId = "jayam";
//	String password = "1234";
	private static final int CODE = 1;

	public static final String SUPER_MERCHANT_ID = "235"; // will be given by FingPay send that only from App to SDK
	public static String TXN_ID = "";
	public String imei = "";
	private LinearLayout llMemeberDetails;
	private TextView tvproductname,tvMemebrName, tvMemberCode, tvDemanddate, tvLoanAccNo, tvOSAmount, tvIntOSAmount, tvPreIntAmount, tvAmountColl, paymentmode;
	private EditText etAmountTobeCollected, aadhaarEt, mobileEt;
	private Button btnSave;
	private AdvaceDemandDO advanceDemandsDO;
	private String MLAI_ID;
	private AdvanceDemandBL advanceDemandsBL;
	private float PreAmt, OutStaAmt, IntOs;
	String Flag;
	private ArrayList<AdvaceDemandDO> alAdvanceDemands;
	private Context mContext;
	GPSTrackevalue gps;
	String merchantId = "Ampl";
	String password = "1234";
//String merchantId = "Dillipa";
//	String password = "1234";
	private ArrayList<IntialParametrsDO> altialParametrsDO;
	private IntialParametrsDO intialParametrsDO;
	@Override
	public void initialize() {

		intialParametrsBL = new IntialParametrsBL();
		altialParametrsDO = intialParametrsBL.SelectAll(AdvMemeberDetails.this);

		try
		{
			intialParametrsDO = altialParametrsDO.get(0);

		}catch (IndexOutOfBoundsException in)
		{
			in.printStackTrace();
		}

		if(intialParametrsDO!=null)
		{
			LUserID=intialParametrsDO.UserID;
			Branchname=intialParametrsDO.BranchName;
			BranchID=intialParametrsDO.Code;
		}

		UserId = SharedPrefUtils.getKeyValue(AdvMemeberDetails.this, AppConstants.pref_name, AppConstants.username);
		mContext = AdvMemeberDetails.this;

		permissionUtils = new PermissionUtils(AdvMemeberDetails.this);

		permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
		permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

		permissionUtils.check_permission(permissions, "Need GPS permission for getting your location", 1);


		TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		imei = telephonyManager.getDeviceId();


		Bundle bundle = getIntent().getExtras();

		MLAI_ID = bundle.getString("mlaid");

		MemberId=MLAI_ID;

		advanceDemandsBL = new AdvanceDemandBL();
		alAdvanceDemands = advanceDemandsBL.SelectAll(MLAI_ID, "memeber",AdvMemeberDetails.this);
		advanceDemandsDO = alAdvanceDemands.get(0);
		intializeControlles();
		tvMemebrName.setText("" + advanceDemandsDO.MMI_Name);
		tvMemberCode.setText("" + advanceDemandsDO.MMI_Code);
		tvLoanAccNo.setText("" + advanceDemandsDO.MLAI_ID);
		tvDemanddate.setText("" + advanceDemandsDO.DemandDate);

		tvOSAmount.setText("" + advanceDemandsDO.OS);

		aadhaarEt.setText("" + advanceDemandsDO.AAdharNo);
		mobileEt.setText("" + advanceDemandsDO.MobileNo);
		float OS = 0;
		float OSInt = 0;
		float OSIntfinal = 0;

		float previous = 0;
		float collAmt = 0;
		OS = Float.valueOf(advanceDemandsDO.OSAmt.trim()).floatValue();
		OSInt = Float.valueOf(advanceDemandsDO.OSAmt.trim()).floatValue();
		OSIntfinal = OS - OSInt;
		String preAmt = advanceDemandsDO.previousAmt;
		if (preAmt == null) {

			previous = 0;
		} else {

			previous = Float.valueOf(advanceDemandsDO.previousAmt.trim()).floatValue();
		}
		String CollectedAmt = advanceDemandsDO.CollectedAmt;
		if (CollectedAmt == null) {

			collAmt = 0;
			etAmountTobeCollected.setText("" + "0.0");
		} else {

			collAmt = Float.valueOf(advanceDemandsDO.CollectedAmt.trim()).floatValue();
			etAmountTobeCollected.setText("" + collAmt);
		}


		PreAmt = previous;
		OutStaAmt = Float.valueOf(advanceDemandsDO.OSAmt);
		IntOs = Float.valueOf(advanceDemandsDO.OSInt);

		tvOSAmount.setText("" + OS);
		tvIntOSAmount.setText("" + advanceDemandsDO.OSInt);
		tvPreIntAmount.setText("" + OSIntfinal);
		tvAmountColl.setText("" + previous);
		tvproductname.setText(advanceDemandsDO.ProductName);
		dlangitude=advanceDemandsDO.LongitudeMember;
		dlatitude=advanceDemandsDO.LatitudeMember;



		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String enterdAmount = etAmountTobeCollected.getText().toString();
				String payment = paymentmode.getText().toString();


				if(mLastLocation!=null) {
					latitude = mLastLocation.getLatitude();
					longitude = mLastLocation.getLongitude();
				}
				Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


				String AAdahr = aadhaarEt.getText().toString();
				String Mobileno = mobileEt.getText().toString();
				float EntAmt = Float.valueOf(enterdAmount);
				float TotColl = PreAmt + EntAmt;
				float TotOSColl = OutStaAmt + IntOs;

				if (payment.length() == 0) {
					showAlertDailog("Please Select PaymentMode.");
				} else if (enterdAmount.equalsIgnoreCase("")) {
					showAlertDailog("Please Enter Amount.");
				}
				float amount = Float.valueOf(enterdAmount.trim()).floatValue();
				if (amount == .0 || amount == .00) {
					showAlertDailog("Please Enter Valid Amount.");
				} else if (amount == 0.0 || amount <= 0) {
					showAlertDailog("Please Enter Amount Greater than Zero");
				} else if (amount < 1.0) {
					showAlertDailog("Please Enter Amount Greater than One Rupee");
				} else if (TotColl > OutStaAmt) {
					showAlertDailog("Coll. Amt. cannot be more than Prin. OS + Pre.Int");
				} else if (TotColl > TotOSColl) {
					showAlertDailog("Coll. Amt. cannot be more than Prin. OS + Int OS");
				}
				else if (payment!=null && payment.trim().equalsIgnoreCase("CashLess") &&AAdahr.length()<12) {
					showAlertDailog("Please Enter AAdhar No and it must be 12 digits.");
				}


				else if (payment!=null && payment.trim().equalsIgnoreCase("CashLess") && Mobileno.length()<10) {
					showAlertDailog("Please Enter Mobile No and it must be 10 digits.");
				}
				else if (!Mobileno.isEmpty()&&Mobileno!=null&&Mobileno.length()>0 && Mobileno.length()<10) {
					showAlertDailog("Please Enter Mobile No and it must be 10 digits.");
				}
				else {
					Flag = "OA";
					if (TotColl > TotOSColl || TotColl == OutStaAmt || TotColl >= OutStaAmt) {
						Flag = "P";
					} else {
						Flag = "OA";
					}
					savePayment(amount, Flag, AAdahr, Mobileno);
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
				Intent i = new Intent(AdvMemeberDetails.this, loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void intializeControlles() {
		llMemeberDetails = (LinearLayout) inflater.inflate(R.layout.advancememberdetails, null);
		tvMemebrName = (TextView) llMemeberDetails.findViewById(R.id.tvAdvMemebrName);
		tvMemberCode = (TextView) llMemeberDetails.findViewById(R.id.tvAdvMemberCode);
		tvLoanAccNo = (TextView) llMemeberDetails.findViewById(R.id.tvAdvLoanAccNo);
		tvDemanddate = (TextView) llMemeberDetails.findViewById(R.id.tvAdvDemanddate);
		tvOSAmount = (TextView) llMemeberDetails.findViewById(R.id.tvAdvOSAmount);
		tvIntOSAmount = (TextView) llMemeberDetails.findViewById(R.id.tvAdvIntOSAmount);
		tvPreIntAmount = (TextView) llMemeberDetails.findViewById(R.id.tvAdvPreIntAmount);
		tvAmountColl = (TextView) llMemeberDetails.findViewById(R.id.tvAdvAmountColl);
		aadhaarEt = (EditText) llMemeberDetails.findViewById(R.id.et_aadhaar);
		mobileEt = (EditText) llMemeberDetails.findViewById(R.id.et_mobile);
		etAmountTobeCollected = (EditText) llMemeberDetails.findViewById(R.id.etAdvAmountTobeCollected);
		btnSave = (Button) llMemeberDetails.findViewById(R.id.btnSave);
		paymentmode = (TextView) llMemeberDetails.findViewById(R.id.payment);
		tvproductname = (TextView) llMemeberDetails.findViewById(R.id.tvproductname);
		buttoncall=(ImageView)  llMemeberDetails.findViewById(R.id.btncall);
		btnlocation=(ImageView)  llMemeberDetails.findViewById(R.id.btnlocation);
		Aadhralayout = (LinearLayout) llMemeberDetails.findViewById(R.id.aadharlayout);
		Translayout = (LinearLayout) llMemeberDetails.findViewById(R.id.translayout);
		trantype = (RadioGroup) llMemeberDetails.findViewById(R.id.groupradio);




		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		llBaseMiddle_lv.addView(llMemeberDetails, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		tvHeader.setText("Member Details");
		Aadhralayout.setVisibility(View.GONE);
		trantype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
				switch (checkedId)
				{
					case R.id.cashrad:
						Ttype = AepsSdkConstants.cashWithdrawal;
						break;

					case R.id.balence:
						Ttype = AepsSdkConstants.balanceEnquiry;
						break;

				}

			}
		});


		buttoncall.setTag(-1);
		buttoncall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub


				if(mobileEt.getText().toString().trim().length()==10){

					onCallBtnClick(mobileEt.getText().toString().trim());

				}else{
					DialogUtils.showAlert(AdvMemeberDetails.this,"Please Enter Mobile No it must be 10 digits");

				}
			}
		});


		btnlocation.setTag(-1);
		btnlocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {



				if(mLastLocation!=null)
				{
					latitude = mLastLocation.getLatitude();
					longitude = mLastLocation.getLongitude();
				}
				Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();




				String slangitude="";
				String slatitude="";

				slatitude=latitude+"";
				slangitude=longitude+"";



				if(slangitude!=null && !slangitude.isEmpty() && slatitude!=null && !slangitude.isEmpty()) {

					if(slangitude!=null && !slangitude.isEmpty())
					{
						slangitude=slangitude.trim();
					}

					if(slatitude!=null && !slatitude.isEmpty())
					{
						slatitude=slatitude.trim();
					}
					Intent intent = new Intent(mContext, DisplayRoute.class);
					intent.putExtra("Lognitude", slangitude);
					intent.putExtra("Latitude", slatitude);
					intent.putExtra("DESLognitude", dlangitude);
					intent.putExtra("DESLatitude", dlatitude);
					((Activity) (mContext)).startActivity(intent);
				}
				else
				{
					DialogUtils.showAlert(mContext,"Latitude and Longitude not capture wait");
				}

			}
		});


		String Branchpay="";
		if(advanceDemandsDO!=null)
		{
			Branchpay=advanceDemandsDO.BranchPaymode;
		}
		paymentmode.setEnabled(true);

//		if(Branchpay!=null)
//		{
//			if (Branchpay.equalsIgnoreCase("0")) {
//				paymentmode.setText("Cash");
//				paymentmode.setEnabled(false);
//			}
//			else if (Branchpay.equalsIgnoreCase("1")) {
//				paymentmode.setEnabled(true);
//			}
//		}


		if (checkPlayServices()) {

			// Building the GoogleApi client
			buildGoogleApiClient();
		}

		paymentmode.setTag(-1);
		paymentmode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				DialogUtils.showSingleChoiceLIstItems(AdvMemeberDetails.this,
						paymentarr, (Integer) v.getTag(),
						"Please select PaymentMode",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								v.setTag(which);
								((TextView) v).setText(paymentarr[which]);
								String type=paymentmode.getText().toString().trim();

								if(type!=null&&type.equalsIgnoreCase("Cash"))
								{
									Aadhralayout.setVisibility(View.GONE);
									Translayout.setVisibility(View.GONE);
								}
								else
								{
									//Aadhralayout.setVisibility(View.VISIBLE);
									Translayout.setVisibility(View.VISIBLE);
								}

							}
						});
			}
		});
	}


	public void savePayment(float paymnet, String Flag, String aadharno, String mobileno) {
		String paymentmod = paymentmode.getText().toString();
		String enterdAmount = etAmountTobeCollected.getText().toString();
		String AAdahr = aadhaarEt.getText().toString();
		String Mobileno = mobileEt.getText().toString();

		if (paymentmod.length()==0) {
			showAlertDailog("Please Select PaymentMode.");
		}

//		else if (paymentmod!=null && paymentmod.trim().equalsIgnoreCase("CashLess") &&AAdahr.length()<12) {
//			showAlertDailog("Please Enter AAdhar No and it must be 12 digits.");
//		}


		else if (paymentmod!=null && paymentmod.trim().equalsIgnoreCase("CashLess") && Mobileno.length()<10) {
			showAlertDailog("Please Enter Mobile No and it must be 10 digits.");
		}
		else if (paymentmod!=null && paymentmod.trim().equalsIgnoreCase("CashLess") &&  trantype.getCheckedRadioButtonId()==-1) {
			showAlertDailog("Please Select Transaction Type.");
		}
    else
		{
			if (paymentmod != null && paymentmod.trim().equalsIgnoreCase("Cash")) {

			//	Toast.makeText("",""+)

				String timev="";
				String datetime="";
				datetime=UtilsClass.GetCurrentdateTime();
				timev=UtilsClass.GetCurrentdateNanoTime();

				Log.v("","advmember"+collectedAmt);


				advanceDemandsBL.updateSavedAmtFING(advanceDemandsDO.MLAI_ID, String.valueOf(paymnet).toString(), Flag, "", "", paymentmod,"","",timev,datetime,AdvMemeberDetails.this);

				Intent intent = new Intent(this, AdvancePaymentconfiramtion.class);
				Bundle bundle = new Bundle();
				bundle.putString("MemberName", "" + advanceDemandsDO.MMI_Name);
				bundle.putString("MemberCode", "" + advanceDemandsDO.MMI_Code);
				bundle.putString("DemandDate", "" + advanceDemandsDO.DemandDate);
				bundle.putString("paymnet", ""+paymnet);
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);

			} else {


				if(paymentmod != null)
				{
					collectedAmt = etAmountTobeCollected.getText().toString();
					Date endsd = new Date();
					SimpleDateFormat sdfr = new SimpleDateFormat("ddMMyyyyHHmmss");
					try {
						TXN_ID = MemberId+UserId+sdfr.format(endsd);

					} catch (Exception ex) {
						System.out.println(ex);
					}
					Log.d("", "TXN_IDFIRST" + TXN_ID);
					AepsSdkConstants.transactionType=Ttype;
					AepsSdkConstants.transactionAmount =collectedAmt;
					AepsSdkConstants.paramA = AppConstants.paramA;
					AepsSdkConstants.paramB =  AppConstants.paramB;
					AepsSdkConstants.paramC = advanceDemandsDO.MLAI_ID;
					AepsSdkConstants.encryptedData =  AppConstants.encryptedData;
					AepsSdkConstants.loginID =  AppConstants.loginID;


					if (paymentmod != null && paymentmod.trim().equalsIgnoreCase("AEPS"))
					{
						Intent intent = new Intent(AdvMemeberDetails.this, AEPSHomeActivity.class);
						startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
					}
					else if (paymentmod.trim().equalsIgnoreCase("matm"))
					{
						Intent intent = new Intent(AdvMemeberDetails.this, AEPSHomeActivity.class);
						startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
					}

					else if (paymentmod.trim().equalsIgnoreCase("pg"))
					{
						Intent intent = new Intent(AdvMemeberDetails.this, AEPSHomeActivity.class);
						startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
					}

					else if (paymentmod.trim().equalsIgnoreCase("upi"))
					{
						Intent intent = new Intent(AdvMemeberDetails.this, AEPSHomeActivity.class);
						startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
					}
				}
			}

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
		else if (resultCode == RESULT_OK && requestCode == AepsSdkConstants.REQUEST_CODE) {
			Responseda="";

			String timev="";
			String datetime="";
			datetime=UtilsClass.GetCurrentdateTime();
			timev=UtilsClass.GetCurrentdateNanoTime();



			Responseda=data.getStringExtra(AepsSdkConstants.responseData);
			UtilsClass.writeToFile(Responseda,timev);
			String referenceNo = " ";
			String status = " ";
			String apiComment = " ";
			String statusDesc = " ";
			String transactionType = " ";
			String transactionAmount = " ";
			String BankName="";
			try {
				JSONObject jobj = new JSONObject(Responseda);
				referenceNo=jobj.getString("referenceNo");
				status=jobj.getString("status");
				apiComment=jobj.getString("apiComment");
				statusDesc=jobj.getString("statusDesc");
				transactionType=jobj.getString("transactionType");
				BankName=jobj.getString("bankName");
				if(transactionType!=null&&!transactionType.isEmpty()&&transactionType.equalsIgnoreCase("Cash Withdrawal"))
				{
					transactionAmount=jobj.getString("transactionAmount");
				}
			} catch (Exception t) {
				t.printStackTrace();
			}

			if(status!=null && !status.isEmpty()&&transactionType!=null&&!transactionType.isEmpty()&&transactionType.equalsIgnoreCase("Cash Withdrawal"))
			{
				if(!status.trim().equalsIgnoreCase("0"))
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdvMemeberDetails.this);
					alertDialogBuilder.setMessage(statusDesc);
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}

				else
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdvMemeberDetails.this);
					alertDialogBuilder.setMessage(statusDesc);
					final String finalReferenceNo = referenceNo;
					final String finalStatusDesc = statusDesc;
					final String finalBankName = BankName;
					final String finalTimev = timev;
					final String finalDatetime = datetime;
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							String paymentmod = paymentmode.getText().toString();
							advanceDemandsBL.updateSavedAmtFING(advanceDemandsDO.MLAI_ID, collectedAmt, Flag, finalReferenceNo, finalStatusDesc, paymentmod,TXN_ID, finalBankName, finalTimev, finalDatetime,AdvMemeberDetails.this);
							Intent intent = new Intent(AdvMemeberDetails.this, AdvancePaymentconfiramtion.class);
							Bundle bundle = new Bundle();
							bundle.putString("MemberName", "" + advanceDemandsDO.MMI_Name);
							bundle.putString("MemberCode", "" + advanceDemandsDO.MMI_Code);
							bundle.putString("DemandDate", "" + advanceDemandsDO.DemandDate);
							bundle.putString("paymnet", collectedAmt);
							intent.putExtra("TXNID", TXN_ID);
							intent.putExtras(bundle);
							startActivityForResult(intent, 0);
						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();

				}
			}


		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();

	}

	/**
	 * Google api callback methods
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());
	}

	@Override
	public void onConnected(Bundle arg0) {

		// Once connected with google api, get the location
		getLocation();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}


	// Permission check functions


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		// redirects to utils
		permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

	}


	public void PermissionGranted(int request_code) {
		Log.i("PERMISSION", "GRANTED");
		isPermissionGranted = true;
	}


	public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
		Log.i("PERMISSION PARTIALLY", "GRANTED");
	}


	public void PermissionDenied(int request_code) {
		Log.i("PERMISSION", "DENIED");
	}


	public void NeverAskAgain(int request_code) {
		Log.i("PERMISSION", "NEVER ASK AGAIN");
	}

	public void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}


	private boolean checkPlayServices() {

		GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

		int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

		if (resultCode != ConnectionResult.SUCCESS) {
			if (googleApiAvailability.isUserResolvableError(resultCode)) {
				googleApiAvailability.getErrorDialog(this, resultCode,
						PLAY_SERVICES_REQUEST).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"This device is not supported.", Toast.LENGTH_LONG)
						.show();
				finish();
			}
			return false;
		}
		return true;
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();

		mGoogleApiClient.connect();

		LocationRequest mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(mLocationRequest);

		PendingResult<LocationSettingsResult> result =
				LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(LocationSettingsResult locationSettingsResult) {

				final Status status = locationSettingsResult.getStatus();

				switch (status.getStatusCode()) {
					case LocationSettingsStatusCodes.SUCCESS:
						// All location settings are satisfied. The client can initialize location requests here
						getLocation();
						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						try {
							// Show the dialog by calling startResolutionForResult(),
							// and check the result in onActivityResult().
							status.startResolutionForResult(AdvMemeberDetails.this, REQUEST_CHECK_SETTINGS);

						} catch (IntentSender.SendIntentException e) {
							// Ignore the error.
						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						break;
				}
			}
		});


	}

	private void getLocation() {

		if (isPermissionGranted) {

			try {
				mLastLocation = LocationServices.FusedLocationApi
						.getLastLocation(mGoogleApiClient);
			} catch (SecurityException e) {
				e.printStackTrace();
			}

		}
	}


	class UploadGroupDetails extends AsyncTask<Void, Void, String> {
		StringBuffer Groupstring = new StringBuffer();
		String finalmesage="";
		@Override
		protected void onPreExecute() {
			ShowLoader();
		}

		@Override
		protected String doInBackground(Void... arg0) {

			String URL="https://fingpayap.tapits.in/fpaepsweb/api/auth/merchantInfo/statusCheckV2/secure/aadhaarPay";

			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			HttpResponse response;

			JSONObject jsonobj = new JSONObject();
			try {
				jsonobj.put("merchantTranId", TXN_ID);

			} catch (JSONException e) {
				e.printStackTrace();
			}


			try {


				HttpPost post = new HttpPost(URL);
				StringEntity se = new StringEntity( jsonobj.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				post.setEntity(se);
				response = client.execute(post);

				/*Checking response */
				if(response!=null){
					finalmesage = EntityUtils.toString(response.getEntity());
				}

				Log.v("","finalmesage"+finalmesage);

				Log.d("","finalmesage"+finalmesage);

				System.out.println("finalmesage"+finalmesage);



			} catch (Exception e) {
				e.printStackTrace();
			}

			return finalmesage;
		}

		@Override
		protected void onPostExecute(String result) {
			HideLoader();

			Log.v("","result2"+result);
			JSONObject jpb=null;
			JSONObject resp=null;
			JSONArray jarray=null;
			String response="";
			String bankRrn="";

			String transactionStatusMessage="";
			try {
				jpb= new JSONObject(result);
				if(jpb!=null)
				{
					response=jpb.getString("apiStatusMessage");
					jarray=jpb.getJSONArray("data");
					if(jarray!=null)
					{
						for(int i=0;i<jarray.length();i++)
						{
							resp=jarray.getJSONObject(i);
							if(resp!=null)
							{
								bankRrn=resp.getString("bankRRN");
								transactionStatusMessage=resp.getString("transactionStatusMessage");

							}
						}
					}

				}


				Log.v("","bankRrn2"+bankRrn);
				Log.v("","response2"+response);
				Log.v("","transactionStatusMessage"+transactionStatusMessage);


				if(transactionStatusMessage!=null &&transactionStatusMessage.trim().equalsIgnoreCase("Success"))

				{
					ivHome.setVisibility(View.GONE);
					ivLogout.setVisibility(View.GONE);
					try {
						mPlayer = MediaPlayer.create(AdvMemeberDetails.this, R.raw.success);
						mPlayer.setVolume(100, 100);
						mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mediaPlayer) {
								releasePlayer();

							}


						});
						mPlayer.start();

					} catch (Exception e) {
					}
					String paymentmod = paymentmode.getText().toString();
					advanceDemandsBL.updateSavedAmtFING(advanceDemandsDO.MLAI_ID, collectedAmt, Flag, bankRrn, transactionStatusMessage, paymentmod,TXN_ID,"","","",AdvMemeberDetails.this);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdvMemeberDetails.this);
					alertDialogBuilder.setMessage(transactionStatusMessage);
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							Intent intent = new Intent(AdvMemeberDetails.this, AdvancePaymentconfiramtion.class);
							Bundle bundle = new Bundle();
							bundle.putString("MemberName", "" + advanceDemandsDO.MMI_Name);
							bundle.putString("MemberCode", "" + advanceDemandsDO.MMI_Code);
							bundle.putString("DemandDate", "" + advanceDemandsDO.DemandDate);
							bundle.putString("paymnet", collectedAmt);
							intent.putExtra("TXNID", TXN_ID);
							intent.putExtras(bundle);
							startActivityForResult(intent, 0);
						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}

				else
				{
					ivHome.setVisibility(View.GONE);
					ivLogout.setVisibility(View.GONE);

					try {
						mPlayer = MediaPlayer.create(AdvMemeberDetails.this, R.raw.failure);
						mPlayer.setVolume(100, 100);
						mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mediaPlayer) {
								releasePlayer();

							}


						});
						mPlayer.start();
					} catch (Exception e) {
					}
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdvMemeberDetails.this);
					alertDialogBuilder.setMessage(transactionStatusMessage);
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}


			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}


	private void releasePlayer() {
		try {
			if (mPlayer != null) {
				mPlayer.stop();
				mPlayer.release();
				mPlayer = null;
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onBackPressed() {
		// Do Here what ever you want do on back press;
	}

	private void onCallBtnClick(String no) {

		if (Build.VERSION.SDK_INT < 23) {
			phoneCall(no);
		} else {

			if (ActivityCompat.checkSelfPermission(AdvMemeberDetails.this,
					android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

				phoneCall(no);
			} else {
				final String[] PERMISSIONS_STORAGE = {android.Manifest.permission.CALL_PHONE};
				//Asking request Permissions
				ActivityCompat.requestPermissions(AdvMemeberDetails.this, PERMISSIONS_STORAGE, 9);
			}
		}
	}


	private void phoneCall(String phone) {


		String d = "tel:" + phone;
		Log.v("Make call", "" + d);

		if (ActivityCompat.checkSelfPermission(AdvMemeberDetails.this,
				android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse(d));
			startActivity(callIntent);
		} else {
			Toast.makeText(AdvMemeberDetails.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
		}
	}
}