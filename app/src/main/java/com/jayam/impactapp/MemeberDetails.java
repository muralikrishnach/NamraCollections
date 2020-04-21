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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import com.jayam.LocationUtil.PermissionUtils;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.DialogUtils;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.UtilsClass;

import com.matm.matmsdk.Bluetooth.BluetoothActivity;
import com.matm.matmsdk.MPOS.PosActivity;
import com.matm.matmsdk.Utils.MATMSDKConstant;
import com.matm.matmsdk.aepsmodule.AEPSHomeActivity;
import com.matm.matmsdk.aepsmodule.utils.AepsSdkConstants;
import com.matm.matmsdk.upitransaction.UPIHomeActivity;


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




public class MemeberDetails extends Base implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback,PermissionUtils.PermissionResultCallback {
	boolean matamcheck=false;

	RadioButton rb_cw, rb_be;
	String Latecheck="No";
	CheckBox latecheck;
	String Ttype="";
	RadioButton cashwithdrae,balancewnq;
	RadioGroup trantype;
	LinearLayout Translayout;
	String Responseda="";
	LinearLayout Aadhralayout;
	LinearLayout mphotolayout;
	public	String dlatitude, dlangitude;
	ImageView buttoncall,btnlocation;

	MediaPlayer mPlayer;
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
	double latitude = 0.0;
	double longitude = 0.0;
//	String merchantId = "jayam";
//	String password = "1234";      //suresh

	private LinearLayout llMemeberDetails, llpaidAmt;
	private TextView tvproductname,tvMemebrName, tvMemberCode, tvDemanddate, tvRegDemandAmount, tvODDemandsAmount, tvPaidAmount,
			tvInstall, tvLoanAccNo, tvNxtPayDate, paymentmode;
	private EditText etAmountTobeCollected, aadhaarEt, mobileEt;
	public LinearLayout llNextInsdate;
	private Button btnSave,btnmobileno;
	private IntialParametrsBL intialParametrsBL;
	private ArrayList<IntialParametrsDO> altialParametrsDO;
	private RegularDemandsDO regularDemandsDO_Temp;
	private RegularDemandsDO regularDemandsDO_Original;
	private float totalamount;
	private String MLAI_ID;
	private String TxnType;
	private RegularDemandsBLTemp regularDemandsBLTemp;
	private RegularDemandsBL regularDemandsBL;
	private float Savedamt = 0;
	private float RegularDemnand = 0;
	private float CollectedAmt = 0;
	private float ODAmt = 0;
	private IntialParametrsDO intialParametrsDO;
	private ArrayList<RegularDemandsDO> alRegularDemandsDOs_Original;
	private Context mContext;
	private TextView respTv, transIdTv;
	public static final int RequestPermissionCode = 1;
	private static final int CODE = 1;

	public static final String SUPER_MERCHANT_ID = "235"; // will be given by FingPay send that only from App to SDK
	public static String TXN_ID = "";
	public String imei = "";
	GPSTrackevalue gps;
	String merchantId = "Ampl";
	String password = "1234";



	//	String merchantId = "Dillipa";
//	String password = "1234";
	@Override
	public void initialize() {
		Latecheck="No";
		UserId = SharedPrefUtils.getKeyValue(MemeberDetails.this, AppConstants.pref_name, AppConstants.username);
		mContext = MemeberDetails.this;
		checkPlayServices();

		permissionUtils = new PermissionUtils(MemeberDetails.this);
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
		TxnType = bundle.getString("txntype");
		regularDemandsBLTemp = new RegularDemandsBLTemp();
		regularDemandsDO_Temp = regularDemandsBLTemp.SelectMemberDetails_MALIID(MLAI_ID,MemeberDetails.this);

		regularDemandsBL = new RegularDemandsBL();
		intialParametrsBL = new IntialParametrsBL();
		altialParametrsDO = intialParametrsBL.SelectAll(MemeberDetails.this);
		intialParametrsDO = altialParametrsDO.get(0);

		if(intialParametrsDO!=null)
		{
			LUserID=intialParametrsDO.UserID;
			Branchname=intialParametrsDO.BranchName;
			BranchID=intialParametrsDO.Code;
		}

		alRegularDemandsDOs_Original = regularDemandsBL.SelectAll(MLAI_ID, "memeber",MemeberDetails.this);
		regularDemandsDO_Original = alRegularDemandsDOs_Original.get(0);


		if(regularDemandsDO_Original!=null)
		{

			MemberId=regularDemandsDO_Original.MLAI_ID;

			dlangitude=regularDemandsDO_Original.LongitudeMember;
			dlatitude=regularDemandsDO_Original.LatitudeMember;
		}



		intializeControlles();

		Log.e("MemberName", "" + regularDemandsDO_Temp.MemberName);
		Log.e("MemberCode", "" + regularDemandsDO_Temp.MemberCode);
		Log.e("DemandTotal", "" + regularDemandsDO_Temp.DemandTotal);
		Log.e("ODAmount", "" + regularDemandsDO_Temp.ODAmount);

		tvMemebrName.setText("" + regularDemandsDO_Temp.MemberName);
		tvMemberCode.setText("" + regularDemandsDO_Temp.MemberCode);
		tvRegDemandAmount.setText("" + regularDemandsDO_Temp.DemandTotal);
		tvODDemandsAmount.setText("" + regularDemandsDO_Temp.ODAmount);
		tvInstall.setText("" + regularDemandsDO_Temp.InstallNo);
		tvLoanAccNo.setText("" + regularDemandsDO_Temp.MLAI_ID);
		tvNxtPayDate.setText("" + regularDemandsDO_Original.NextRepayDate);

		aadhaarEt.setText("" + regularDemandsDO_Original.AAdharNo);
		mobileEt.setText("" + regularDemandsDO_Original.MobileNo);
		tvproductname.setText(regularDemandsDO_Original.ProductName);





		RegularDemnand = Float.valueOf(regularDemandsDO_Temp.DemandTotal);
		ODAmt = Float.valueOf(regularDemandsDO_Temp.ODAmount);

		tvDemanddate.setText("" + regularDemandsDO_Temp.DemandDate);
		if (regularDemandsDO_Temp.savedAmt != null) {
			Savedamt = Float.valueOf(regularDemandsDO_Temp.savedAmt).floatValue();
		}

		totalamount = Float.valueOf(regularDemandsDO_Original.DemandTotal).floatValue()
				+ Float.valueOf(regularDemandsDO_Original.ODAmount).floatValue();

		if (regularDemandsDO_Original.Confirmed != null
				&& regularDemandsDO_Original.Confirmed.equalsIgnoreCase("CONFIRMED")) {
			llpaidAmt.setVisibility(View.VISIBLE);
			tvPaidAmount.setText("" + regularDemandsDO_Original.collectedAmount);
			if (Savedamt > 0) {
				etAmountTobeCollected.setText("" + (Savedamt));

			} else {
				etAmountTobeCollected
						.setText("" + (totalamount - Float.valueOf(regularDemandsDO_Original.collectedAmount)));

			}
			// totalamount = (totalamount -
			// Float.valueOf(regularDemandsDO_Original.collectedAmount));

		} else {
			if (Savedamt > 0) {
				etAmountTobeCollected.setText("" + (Savedamt));
				totalamount = Savedamt;

			} else {
				etAmountTobeCollected.setText("" + (totalamount));
			}
			llpaidAmt.setVisibility(View.GONE);
		}
		/*
		 * String amt = SharedPrefUtils.getKeyValue(MemeberDetails.this,
		 * AppConstants.memberDetails_pref, regularDemandsDO.MemberCode);
		 *
		 * if(amt.equalsIgnoreCase("")) { totalamount =
		 * Float.valueOf(regularDemandsDO.DemandTotal.trim()).floatValue() +
		 * Float.valueOf(regularDemandsDO.ODAmount.trim()).floatValue();
		 * etAmountTobeCollected.setText(""+(totalamount)); } else { totalamount
		 * = Float.valueOf(SharedPrefUtils.getKeyValue(MemeberDetails.this,
		 * AppConstants.memberDetails_pref,
		 * regularDemandsDO.MemberCode).trim()).floatValue() +
		 * Float.valueOf(regularDemandsDO.ODAmount.trim()).floatValue();
		 * etAmountTobeCollected.setText(""+(totalamount)); }
		 */















		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {



				String payment = paymentmode.getText().toString();

				if(mLastLocation!=null)
				{
					latitude = mLastLocation.getLatitude();
					longitude = mLastLocation.getLongitude();
				}
				Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();






				String enterdAmount = etAmountTobeCollected.getText().toString();
				String AAdahr = aadhaarEt.getText().toString();
				String Mobileno = mobileEt.getText().toString();
				if (payment.length()==0) {
					showAlertDailog("Please Select PaymentMode.");
				}

				else if (enterdAmount.equalsIgnoreCase("")) {
					showAlertDailog("Please Enter Amount.");
				}


//			else if (payment!=null && payment.trim().equalsIgnoreCase("CashLess") &&AAdahr.length()<12) {
//				showAlertDailog("Please Enter AAdhar No and it must be 12 digits.");
//			}


				else if (payment!=null && payment.trim().equalsIgnoreCase("CashLess") && Mobileno.length()<10) {
					showAlertDailog("Please Enter Mobile No and it must be 10 digits.");
				}
				else if (!Mobileno.isEmpty()&&Mobileno!=null&&Mobileno.length()>0 && Mobileno.length()<10) {
					showAlertDailog("Please Enter Mobile No and it must be 10 digits.");
				}
				else if (payment!=null && payment.trim().equalsIgnoreCase("CashLess") &&  trantype.getCheckedRadioButtonId()==-1) {
					showAlertDailog("Please Select Transaction Type.");
				}
				else {
					float amount = Float.valueOf(enterdAmount.trim()).floatValue();
					float coltdAmt;

					if (regularDemandsDO_Original.collectedAmount != null) {
						coltdAmt = Float.valueOf(regularDemandsDO_Original.collectedAmount);
					} else {
						coltdAmt = 0;
					}
					CollectedAmt = coltdAmt;
					Log.e("totalamount-------", String.valueOf(totalamount));
					Log.e("coltdAmt-------", String.valueOf(coltdAmt));

					if ((totalamount - coltdAmt) == 0) {
						if (intialParametrsDO.AdvPayment.equalsIgnoreCase("1")) {
							// allow advance payment upto collected amt -
							// outstanding amt

							if (Float
									.valueOf(enterdAmount) > Float.valueOf(regularDemandsDO_Original.OSAmt).floatValue()
									- coltdAmt) {
								showAlertDailog("Payment should not exceed OS Amount.");
							} else {
								savePayment(amount,AAdahr,Mobileno);

							}

						} else {
							showAlertDailog("Zero payment not Allowed.");
						}
					}

					else if (intialParametrsDO.PartialPayment.equalsIgnoreCase("1")) {
						if (amount >= 0 && amount <= totalamount)

						{
							Log.v("","Latecheck--"+Latecheck);
							if(amount >= 0 && amount == totalamount&&Latecheck!=null&&Latecheck.equalsIgnoreCase("Yes"))
							{
								showAlertDailog("Late collection is not possible as Demand amount and collection amount are equal.");
							}
							else
							{
								savePayment(amount,AAdahr,Mobileno);
							}


						} else {
							// showAlertDailog("Payment not Allowed.");
							showAlertDailog("Coll Amount Cannot be greater than Total Demand.");

						}
					}

					else if (amount != Float.valueOf(regularDemandsDO_Original.DemandTotal).floatValue()
							+ Float.valueOf(regularDemandsDO_Original.ODAmount).floatValue()) {
						// exactl amt
						showAlertDailog("Partial Payment not Allowed.");
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
				Intent i = new Intent(MemeberDetails.this, loginActivity.class);
				startActivity(i);
				// setResult(AppConstants.RESULTCODE_LOGOUT);
				// finish();
			}
		});
	}

	public void intializeControlles() {
		llMemeberDetails = (LinearLayout) inflater.inflate(R.layout.memberdetails, null);
		tvMemebrName = (TextView) llMemeberDetails.findViewById(R.id.tvMemebrName);
		tvMemberCode = (TextView) llMemeberDetails.findViewById(R.id.tvMemberCode);
		tvRegDemandAmount = (TextView) llMemeberDetails.findViewById(R.id.tvRegDemandAmount);
		tvDemanddate = (TextView) llMemeberDetails.findViewById(R.id.tvDemanddate);
		tvODDemandsAmount = (TextView) llMemeberDetails.findViewById(R.id.tvODDemandsAmount);
		tvInstall = (TextView) llMemeberDetails.findViewById(R.id.tvInstall);
		tvLoanAccNo = (TextView) llMemeberDetails.findViewById(R.id.tvLoanAccNo);
		paymentmode = (TextView)llMemeberDetails. findViewById(R.id.payment);
		latecheck= (CheckBox) llMemeberDetails. findViewById(R.id.latecheck);
		btnSave = (Button) llMemeberDetails.findViewById(R.id.btnSave);
		aadhaarEt = (EditText) llMemeberDetails.findViewById(R.id.et_aadhaar);
		mobileEt = (EditText) llMemeberDetails.findViewById(R.id.et_mobile);
		llNextInsdate = (LinearLayout) llMemeberDetails.findViewById(R.id.llNextInsdate);
		Aadhralayout = (LinearLayout) llMemeberDetails.findViewById(R.id.aadharlayout);
		Translayout = (LinearLayout) llMemeberDetails.findViewById(R.id.translayout);
		mphotolayout = (LinearLayout) llMemeberDetails.findViewById(R.id.photoayout);
		trantype = (RadioGroup) llMemeberDetails.findViewById(R.id.groupradio);

		rb_cw = (RadioButton) llMemeberDetails.findViewById(R.id.cashrad);
		rb_be = (RadioButton) llMemeberDetails.findViewById(R.id.balnce);


		etAmountTobeCollected = (EditText) llMemeberDetails.findViewById(R.id.etAmountTobeCollected);
		tvPaidAmount = (TextView) llMemeberDetails.findViewById(R.id.tvPaidAmount);
		tvNxtPayDate = (TextView) llMemeberDetails.findViewById(R.id.tvNextPayDate);
		llpaidAmt = (LinearLayout) llMemeberDetails.findViewById(R.id.llpaidAmt);
		buttoncall=(ImageView)  llMemeberDetails.findViewById(R.id.btncall);
		btnlocation=(ImageView)  llMemeberDetails.findViewById(R.id.btnlocation);
		btnSave = (Button) llMemeberDetails.findViewById(R.id.btnSave);
		btnmobileno= (Button) llMemeberDetails.findViewById(R.id.btnmobile);

		tvInstall = (TextView) llMemeberDetails.findViewById(R.id.tvInstall);

		tvproductname = (TextView) llMemeberDetails.findViewById(R.id.tvproductname);

		mphotolayout.setVisibility(View.GONE);



		latecheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked)
				{
					Latecheck="Yes";
				}
				else
				{
					Latecheck="No";
				}



			}
		});

		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		showHomeIcons();
		ivLogout.setVisibility(View.GONE);
		llBaseMiddle_lv.addView(llMemeberDetails, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		tvHeader.setText("Member Details");

		Aadhralayout.setVisibility(View.GONE);
		trantype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId){
					case R.id.cashrad:
						Ttype = AepsSdkConstants.cashWithdrawal;
						break;

					case R.id.balence:
						Ttype = AepsSdkConstants.balanceEnquiry;
						break;
				}
			}
		});

		btnmobileno.setTag(-1);
		btnmobileno.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(MemeberDetails.this, BluetoothActivity.class);
				intent.putExtra("user_id","488");
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
						requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_PRIVILEGED}, 1001);
						Toast.makeText(getApplicationContext(),"Please Grant all the permissions", Toast.LENGTH_LONG).show();
					} else {
						startActivity(intent);
					}
				}else{
					startActivity(intent);

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
					DialogUtils.showAlert(MemeberDetails.this,"Please Enter Mobile No it must be 10 digits");

				}
			}
		});


		paymentmode.setEnabled(true);

//		String Branchpay="";
//
//	if(regularDemandsDO_Original!=null)
//	{
//		Branchpay=regularDemandsDO_Original.BranchPaymode;
//	}
//
//	if(Branchpay!=null)
//	{
//		if (Branchpay.equalsIgnoreCase("0")) {
//
//			paymentmode.setText("Cash");
//			paymentmode.setEnabled(false);
//
//		}
//
//		else if (Branchpay.equalsIgnoreCase("1")) {
//
//			paymentmode.setEnabled(true);
//		}
//
//
//
//	}



		if (checkPlayServices()) {

			// Building the GoogleApi client
			buildGoogleApiClient();
		}

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







		paymentmode.setTag(-1);
		paymentmode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				DialogUtils.showSingleChoiceLIstItems(MemeberDetails.this,
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
		if ((intialParametrsDO.InstRequired).equals("0")) {
			llNextInsdate.setVisibility(View.GONE);
		} else {
			llNextInsdate.setVisibility(View.VISIBLE);
		}
	}

	public void savePayment(float paymnet,String aadharno,String mobileno) {

		//paymnet=1;
		//String amount="1";
		String paymentmod = paymentmode.getText().toString();
		if(paymentmod!=null && paymentmod.trim().equalsIgnoreCase("Cash"))
		{
			String enterdAmount = etAmountTobeCollected.getText().toString();

			//float paymnet = Float.valueOf(enterdAmount.trim()).floatValue();


			totalamount = Float.valueOf(regularDemandsDO_Temp.updated.trim()).floatValue();
			float amount_collects = Float.valueOf(SharedPrefUtils
					.getKeyValue(MemeberDetails.this, AppConstants.memberDetails_pref, AppConstants.amountTobeCollected)
					.trim()).floatValue();

			if (totalamount != paymnet) {
				KeyValue keyValue_amountToBeCollected = new KeyValue(AppConstants.amountTobeCollected, (amount_collects - paymnet) + "");
				SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_amountToBeCollected);
				KeyValue keyValue_memeberCode = new KeyValue(regularDemandsDO_Temp.MemberCode, (paymnet) + "");
				SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_memeberCode);
			}

			RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
			regularDemandsBLTemp.updateregularDemandForMemeber(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet), String.valueOf(paymnet).toString(),MemeberDetails.this);
			regularDemandsBL.updateLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,MemeberDetails.this);
			regularDemandsBLTemp.updateregulartempLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,MemeberDetails.this);


			regularDemandsBL.updateSavedAmt(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet).toString(),MemeberDetails.this);
			regularDemandsBL.updateCashless(regularDemandsDO_Temp.MLAI_ID,"","",paymentmod,TXN_ID,"","","",MemeberDetails.this);
			regularDemandsBLTemp.updateCashlessMemeberTemp(regularDemandsDO_Temp.MLAI_ID, "","",paymentmod,TXN_ID,"","","",MemeberDetails.this);

			regularDemandsBLTemp.updateMobilenoregtemp(regularDemandsDO_Temp.MLAI_ID, mobileEt.getText().toString(),MemeberDetails.this);
			regularDemandsBLTemp.updateMobilenoreg(regularDemandsDO_Temp.MLAI_ID, mobileEt.getText().toString(),MemeberDetails.this);

			if (((paymnet + CollectedAmt) < Float.valueOf(regularDemandsDO_Temp.DemandTotal)) && ODAmt == 0) {

				Intent i = new Intent(MemeberDetails.this, Ftodreasons.class);
				Bundle bundle = new Bundle();
				bundle.putString("MLAID", "" + regularDemandsDO_Temp.MLAI_ID);
				bundle.putString("txntype", "" + TxnType);
				i.putExtras(bundle);
				startActivityForResult(i, 111);

			} else {
				Intent intent1 = new Intent(this, Paymentconfiramtion.class);
				Bundle bundle = new Bundle();
				bundle.putString("paymnet", "" + paymnet);
				bundle.putString("MemberName", "" + regularDemandsDO_Temp.MemberName);
				bundle.putString("MemberCode", "" + regularDemandsDO_Temp.MemberCode);
				bundle.putString("DemandDate", "" + regularDemandsDO_Temp.DemandDate);
				bundle.putString("txntype", "" + TxnType);
				intent1.putExtras(bundle);
				startActivityForResult(intent1, 0);
			}
		}

		else
		{
			if(paymentmod != null)
			{
				int amountentry=0;
				float amountval=0;
				String entryamt = etAmountTobeCollected.getText().toString();

				if(entryamt!=null)
				{
					try {
						amountval=Float.parseFloat(entryamt);
					}catch (NumberFormatException n)
					{
						n.printStackTrace();
					}

					amountentry = (int)amountval;
				}
				Log.v("","amountval--"+amountval);
				Log.v("","amountentry--"+amountentry);

				Date endsd = new Date();
				SimpleDateFormat sdfr = new SimpleDateFormat("ddMMyyyyHHmmss");
				try {
					TXN_ID = MemberId+UserId+sdfr.format(endsd);
				} catch (Exception ex) {
					System.out.println(ex);
				}
				if (paymentmod != null && paymentmod.trim().equalsIgnoreCase("AEPS"))
				{
					AepsSdkConstants.transactionType=Ttype;
					AepsSdkConstants.transactionAmount = amountentry+"";
					AepsSdkConstants.paramA = AppConstants.paramA;
					AepsSdkConstants.paramB =  AppConstants.paramB;
					AepsSdkConstants.paramC =MemberId;
					AepsSdkConstants.encryptedData =  AppConstants.encryptedData;
					AepsSdkConstants.loginID =  AppConstants.loginID;

					Log.v("","AepsSdkConstants.transactionAmount--"+AepsSdkConstants.transactionAmount);

					Intent intent = new Intent(MemeberDetails.this, AEPSHomeActivity.class);
					startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
				}
				else if (paymentmod.trim().equalsIgnoreCase("matm"))
				{
					if(PosActivity.isBlueToothConnected(MemeberDetails.this)){

						MATMSDKConstant.transactionAmount = amountentry+"";
						if(rb_cw.isChecked()){
							MATMSDKConstant.transactionType = MATMSDKConstant.cashWithdrawal;
							MATMSDKConstant.transactionAmount =amountentry+"";

						}if(rb_be.isChecked()){
							MATMSDKConstant.transactionType= MATMSDKConstant.balanceEnquiry;
							MATMSDKConstant.transactionAmount = "0";
						}

						Log.v("","MATMSDKConstant.transactionAmount--"+MATMSDKConstant.transactionAmount);
						matamcheck=true;

						MATMSDKConstant.paramA = AppConstants.paramA;
						MATMSDKConstant.paramB =  AppConstants.paramB;
						MATMSDKConstant.paramC =MemberId;
						MATMSDKConstant.encryptedData =  AppConstants.encryptedData;
						MATMSDKConstant.loginID =  AppConstants.loginID;

						Intent intent = new Intent(MemeberDetails.this, PosActivity.class);
						startActivityForResult(intent, MATMSDKConstant.REQUEST_CODE);
					}else {
						Toast.makeText(MemeberDetails.this, "Please pair the bluetooth device", Toast.LENGTH_SHORT).show();
					}



				}



				else if (paymentmod.trim().equalsIgnoreCase("upi"))
				{
					Intent intent = new Intent(MemeberDetails.this, UPIHomeActivity.class);
					startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
				}

				else if (paymentmod.trim().equalsIgnoreCase("pg"))
				{
					Intent intent = new Intent(MemeberDetails.this, AEPSHomeActivity.class);
					startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
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




		else if (data !=null & resultCode==RESULT_OK && requestCode == AepsSdkConstants.REQUEST_CODE)
		{
			Responseda="";

			String timev="";
			String datetime="";
			datetime=UtilsClass.GetCurrentdateTime();
			timev= UtilsClass.GetCurrentdateNanoTime();
			Responseda=data.getStringExtra(AepsSdkConstants.responseData);

			Log.v("","Responseda"+Responseda);
			UtilsClass.writeToFile(Responseda,timev);
			String referenceNo = " ";
			String status = " ";
			String apiComment = " ";
			String statusDesc = " ";
			String transactionType = " ";
			String transactionAmount = " ";
			String BankName = " ";
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
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemeberDetails.this);
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
					String paymentmod = paymentmode.getText().toString();
					String enterdAmount = etAmountTobeCollected.getText().toString();
					final float paymnet = Float.valueOf(enterdAmount.trim()).floatValue();
					totalamount = Float.valueOf(regularDemandsDO_Temp.updated.trim()).floatValue();
					float amount_collects = Float.valueOf(SharedPrefUtils
							.getKeyValue(MemeberDetails.this, AppConstants.memberDetails_pref, AppConstants.amountTobeCollected)
							.trim()).floatValue();

					if (totalamount != paymnet) {
						KeyValue keyValue_amountToBeCollected = new KeyValue(AppConstants.amountTobeCollected, (amount_collects - paymnet) + "");
						SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_amountToBeCollected);
						KeyValue keyValue_memeberCode = new KeyValue(regularDemandsDO_Temp.MemberCode, (paymnet) + "");
						SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_memeberCode);
					}

					RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
					regularDemandsBLTemp.updateregularDemandForMemeber(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet), String.valueOf(paymnet).toString(),MemeberDetails.this);
					regularDemandsBL.updateLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,MemeberDetails.this);
					regularDemandsBLTemp.updateregulartempLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,MemeberDetails.this);
					regularDemandsBL.updateSavedAmt(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet).toString(),MemeberDetails.this);
					regularDemandsBL.updateCashless(regularDemandsDO_Temp.MLAI_ID,referenceNo,statusDesc,paymentmod,TXN_ID,BankName,timev,datetime,MemeberDetails.this);
					regularDemandsBLTemp.updateCashlessMemeberTemp(regularDemandsDO_Temp.MLAI_ID, referenceNo,statusDesc,paymentmod,TXN_ID,BankName,timev,datetime,MemeberDetails.this);


					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemeberDetails.this);
					alertDialogBuilder.setMessage(statusDesc);
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							if (((paymnet + CollectedAmt) < Float.valueOf(regularDemandsDO_Temp.DemandTotal)) && ODAmt == 0) {

								Intent i = new Intent(MemeberDetails.this, Ftodreasons.class);
								Bundle bundle = new Bundle();
								bundle.putString("MLAID", "" + regularDemandsDO_Temp.MLAI_ID);
								bundle.putString("txntype", "" + TxnType);
								i.putExtras(bundle);
								startActivityForResult(i, 111);

							} else {
								Intent intent1 = new Intent(MemeberDetails.this, Paymentconfiramtion.class);
								Bundle bundle = new Bundle();
								bundle.putString("paymnet", "" + paymnet);
								bundle.putString("MemberName", "" + regularDemandsDO_Temp.MemberName);
								bundle.putString("MemberCode", "" + regularDemandsDO_Temp.MemberCode);
								bundle.putString("DemandDate", "" + regularDemandsDO_Temp.DemandDate);
								bundle.putString("txntype", "" + TxnType);
								intent1.putExtras(bundle);
								startActivityForResult(intent1, 0);
							}

						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();

				}
			}
		}






		else if (data !=null & resultCode==RESULT_OK &&requestCode == MATMSDKConstant.REQUEST_CODE)
		{

			Responseda="";
			String timev="";
			String datetime="";
			datetime=UtilsClass.GetCurrentdateTime();
			timev= UtilsClass.GetCurrentdateNanoTime();
			Responseda=data.getStringExtra(MATMSDKConstant.responseData);

			Log.v("","Responseda"+Responseda);
			UtilsClass.writeToFile(Responseda,timev);
			String referenceNo = " ";
			String status = " ";
			String apiComment = " ";
			String statusDesc = " ";
			String transactionType = " ";
			String transactionAmount = " ";
			String BankName = " ";
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
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemeberDetails.this);
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
					String paymentmod = paymentmode.getText().toString();
					String enterdAmount = etAmountTobeCollected.getText().toString();
					final float paymnet = Float.valueOf(enterdAmount.trim()).floatValue();
					totalamount = Float.valueOf(regularDemandsDO_Temp.updated.trim()).floatValue();
					float amount_collects = Float.valueOf(SharedPrefUtils
							.getKeyValue(MemeberDetails.this, AppConstants.memberDetails_pref, AppConstants.amountTobeCollected)
							.trim()).floatValue();

					if (totalamount != paymnet) {
						KeyValue keyValue_amountToBeCollected = new KeyValue(AppConstants.amountTobeCollected, (amount_collects - paymnet) + "");
						SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_amountToBeCollected);
						KeyValue keyValue_memeberCode = new KeyValue(regularDemandsDO_Temp.MemberCode, (paymnet) + "");
						SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_memeberCode);
					}

					RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
					regularDemandsBLTemp.updateregularDemandForMemeber(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet), String.valueOf(paymnet).toString(),MemeberDetails.this);
					regularDemandsBL.updateLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,MemeberDetails.this);
					regularDemandsBLTemp.updateregulartempLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,MemeberDetails.this);
					regularDemandsBL.updateSavedAmt(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet).toString(),MemeberDetails.this);
					regularDemandsBL.updateCashless(regularDemandsDO_Temp.MLAI_ID,referenceNo,statusDesc,paymentmod,TXN_ID,BankName,timev,datetime,MemeberDetails.this);
					regularDemandsBLTemp.updateCashlessMemeberTemp(regularDemandsDO_Temp.MLAI_ID, referenceNo,statusDesc,paymentmod,TXN_ID,BankName,timev,datetime,MemeberDetails.this);


					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemeberDetails.this);
					alertDialogBuilder.setMessage(statusDesc);
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							if (((paymnet + CollectedAmt) < Float.valueOf(regularDemandsDO_Temp.DemandTotal)) && ODAmt == 0) {

								Intent i = new Intent(MemeberDetails.this, Ftodreasons.class);
								Bundle bundle = new Bundle();
								bundle.putString("MLAID", "" + regularDemandsDO_Temp.MLAI_ID);
								bundle.putString("txntype", "" + TxnType);
								i.putExtras(bundle);
								startActivityForResult(i, 111);

							} else {
								Intent intent1 = new Intent(MemeberDetails.this, Paymentconfiramtion.class);
								Bundle bundle = new Bundle();
								bundle.putString("paymnet", "" + paymnet);
								bundle.putString("MemberName", "" + regularDemandsDO_Temp.MemberName);
								bundle.putString("MemberCode", "" + regularDemandsDO_Temp.MemberCode);
								bundle.putString("DemandDate", "" + regularDemandsDO_Temp.DemandDate);
								bundle.putString("txntype", "" + TxnType);
								intent1.putExtras(bundle);
								startActivityForResult(intent1, 0);
							}

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
		if (matamcheck)
		{

			Responseda="";
			String timev="";
			String datetime="";
			datetime=UtilsClass.GetCurrentdateTime();
			timev= UtilsClass.GetCurrentdateNanoTime();
			Responseda=MATMSDKConstant.responseData;

			Log.v("","Responseda"+Responseda);
			UtilsClass.writeToFile(Responseda,timev);
			String referenceNo = " ";
			String status = " ";
			String apiComment = " ";
			String statusDesc = " ";
			String transactionType = " ";
			String transactionAmount = " ";
			String BankName = " ";
			String TerminalID="";
//			try {
//				JSONObject jobj = new JSONObject(Responseda);
//				referenceNo=jobj.getString("RRN");
//				status=jobj.getString("BalanceEnquiryStatus");
//				apiComment=jobj.getString("apiComment");
//				statusDesc=jobj.getString("statusDesc");
//				transactionType=jobj.getString("transactionType");
//				BankName=jobj.getString("bankName");
//				TerminalID=jobj.getString("TerminalID");
//				if(transactionType!=null&&!transactionType.isEmpty()&&transactionType.equalsIgnoreCase("Cash Withdrawal"))
//				{
//					transactionAmount=jobj.getString("AvailableBalance");
//				}
//			} catch (Exception t) {
//				t.printStackTrace();
//			}


			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemeberDetails.this);
			alertDialogBuilder.setMessage(Responseda);
			alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

//			if(status!=null && !status.isEmpty()&&transactionType!=null&&!transactionType.isEmpty()&&transactionType.equalsIgnoreCase("Cash Withdrawal"))
//			{
//				if(!status.trim().equalsIgnoreCase("0"))
//				{
//					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemeberDetails.this);
//					alertDialogBuilder.setMessage(statusDesc);
//					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int id) {
//							dialog.dismiss();
//						}
//					});
//					AlertDialog alertDialog = alertDialogBuilder.create();
//					alertDialog.show();
//				}
//
//				else
//				{
//					String paymentmod = paymentmode.getText().toString();
//					String enterdAmount = etAmountTobeCollected.getText().toString();
//					final int paymnet = Integer.valueOf(enterdAmount.trim()).intValue();
//					totalamount = Integer.valueOf(regularDemandsDO_Temp.updated.trim()).intValue();
//					int amount_collects = Integer.valueOf(SharedPrefUtils
//							.getKeyValue(MemeberDetails.this, AppConstants.memberDetails_pref, AppConstants.amountTobeCollected)
//							.trim()).intValue();
//
//					if (totalamount != paymnet) {
//						KeyValue keyValue_amountToBeCollected = new KeyValue(AppConstants.amountTobeCollected, (amount_collects - paymnet) + "");
//						SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_amountToBeCollected);
//						KeyValue keyValue_memeberCode = new KeyValue(regularDemandsDO_Temp.MemberCode, (paymnet) + "");
//						SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_memeberCode);
//					}
//
//					RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
//					regularDemandsBLTemp.updateregularDemandForMemeber(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet), String.valueOf(paymnet).toString(),MemeberDetails.this);
//					regularDemandsBL.updateLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,MemeberDetails.this);
//					regularDemandsBLTemp.updateregulartempLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,MemeberDetails.this);
//					regularDemandsBL.updateSavedAmt(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet).toString(),MemeberDetails.this);
//					regularDemandsBL.updateCashless(regularDemandsDO_Temp.MLAI_ID,referenceNo,statusDesc,paymentmod,TXN_ID,BankName,timev,datetime,MemeberDetails.this);
//					regularDemandsBLTemp.updateCashlessMemeberTemp(regularDemandsDO_Temp.MLAI_ID, referenceNo,statusDesc,paymentmod,TXN_ID,BankName,timev,datetime,MemeberDetails.this);
//
//
//					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemeberDetails.this);
//					alertDialogBuilder.setMessage(statusDesc);
//					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int id) {
//
//							if (((paymnet + CollectedAmt) < Integer.valueOf(regularDemandsDO_Temp.DemandTotal)) && ODAmt == 0) {
//
//								Intent i = new Intent(MemeberDetails.this, Ftodreasons.class);
//								Bundle bundle = new Bundle();
//								bundle.putString("MLAID", "" + regularDemandsDO_Temp.MLAI_ID);
//								bundle.putString("txntype", "" + TxnType);
//								i.putExtras(bundle);
//								startActivityForResult(i, 111);
//
//							} else {
//								Intent intent1 = new Intent(MemeberDetails.this, Paymentconfiramtion.class);
//								Bundle bundle = new Bundle();
//								bundle.putString("paymnet", "" + paymnet);
//								bundle.putString("MemberName", "" + regularDemandsDO_Temp.MemberName);
//								bundle.putString("MemberCode", "" + regularDemandsDO_Temp.MemberCode);
//								bundle.putString("DemandDate", "" + regularDemandsDO_Temp.DemandDate);
//								bundle.putString("txntype", "" + TxnType);
//								intent1.putExtras(bundle);
//								startActivityForResult(intent1, 0);
//							}
//
//						}
//					});
//					AlertDialog alertDialog = alertDialogBuilder.create();
//					alertDialog.show();
//
//				}
//			}


		}


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
		permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

	}





	public void PermissionGranted(int request_code) {
		Log.i("PERMISSION","GRANTED");
		isPermissionGranted=true;
	}


	public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
		Log.i("PERMISSION PARTIALLY","GRANTED");
	}


	public void PermissionDenied(int request_code) {
		Log.i("PERMISSION","DENIED");
	}


	public void NeverAskAgain(int request_code) {
		Log.i("PERMISSION","NEVER ASK AGAIN");
	}

	public void showToast(String message)
	{
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}



	private boolean checkPlayServices() {

		GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

		int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

		if (resultCode != ConnectionResult.SUCCESS) {
			if (googleApiAvailability.isUserResolvableError(resultCode)) {
				googleApiAvailability.getErrorDialog(this,resultCode,
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
							status.startResolutionForResult(MemeberDetails.this, REQUEST_CHECK_SETTINGS);

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

			try
			{
				mLastLocation = LocationServices.FusedLocationApi
						.getLastLocation(mGoogleApiClient);
			}
			catch (SecurityException e)
			{
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


				if(transactionStatusMessage!=null &&transactionStatusMessage.trim().equalsIgnoreCase("Success"))

				{


					try {
						mPlayer = MediaPlayer.create(MemeberDetails.this, R.raw.success);

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
					String enterdAmount = etAmountTobeCollected.getText().toString();
					final float paymnet = Float.valueOf(enterdAmount.trim()).floatValue();
					totalamount = Float.valueOf(regularDemandsDO_Temp.updated.trim()).floatValue();
					float amount_collects = Float.valueOf(SharedPrefUtils
							.getKeyValue(MemeberDetails.this, AppConstants.memberDetails_pref, AppConstants.amountTobeCollected)
							.trim()).floatValue();
					if (totalamount != paymnet) {
						KeyValue keyValue_amountToBeCollected = new KeyValue(AppConstants.amountTobeCollected, (amount_collects - paymnet) + "");
						SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_amountToBeCollected);

						KeyValue keyValue_memeberCode = new KeyValue(regularDemandsDO_Temp.MemberCode, (paymnet) + "");
						SharedPrefUtils.setValue(MemeberDetails.this, AppConstants.memberDetails_pref, keyValue_memeberCode);
					}

					RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
					regularDemandsBLTemp.updateregularDemandForMemeber(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet), String.valueOf(paymnet).toString(),MemeberDetails.this);
					//regularDemandsBL.updateSavedAmt(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet).toString());
					//regularDemandsBL.updateCashless(regularDemandsDO_Temp.MLAI_ID,bankRrn,transactionStatusMessage,paymentmod,TXN_ID);
					//regularDemandsBLTemp.updateCashlessMemeberTemp(regularDemandsDO_Temp.MLAI_ID, bankRrn,transactionStatusMessage,paymentmod,TXN_ID);




					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemeberDetails.this);
					alertDialogBuilder.setMessage(transactionStatusMessage);
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							if (((paymnet + CollectedAmt) < Float.valueOf(regularDemandsDO_Temp.DemandTotal)) && ODAmt == 0) {
								Intent i = new Intent(MemeberDetails.this, Ftodreasons.class);
								Bundle bundle = new Bundle();
								bundle.putString("MLAID", "" + regularDemandsDO_Temp.MLAI_ID);
								bundle.putString("txntype", "" + TxnType);
								i.putExtras(bundle);
								startActivityForResult(i, 111);

							} else {
								Intent intent1 = new Intent(MemeberDetails.this, Paymentconfiramtion.class);
								Bundle bundle = new Bundle();
								bundle.putString("paymnet", "" + paymnet);
								bundle.putString("MemberName", "" + regularDemandsDO_Temp.MemberName);
								bundle.putString("MemberCode", "" + regularDemandsDO_Temp.MemberCode);
								bundle.putString("DemandDate", "" + regularDemandsDO_Temp.DemandDate);
								bundle.putString("txntype", "" + TxnType);
								intent1.putExtras(bundle);
								startActivityForResult(intent1, 0);
							}

						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();


				}
				else
				{


					try {
						mPlayer = MediaPlayer.create(MemeberDetails.this, R.raw.failure);

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
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemeberDetails.this);
					alertDialogBuilder.setMessage(transactionStatusMessage);
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {



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

			if (ActivityCompat.checkSelfPermission(MemeberDetails.this,
					android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

				phoneCall(no);
			} else {
				final String[] PERMISSIONS_STORAGE = {android.Manifest.permission.CALL_PHONE};
				//Asking request Permissions
				ActivityCompat.requestPermissions(MemeberDetails.this, PERMISSIONS_STORAGE, 9);
			}
		}
	}


	private void phoneCall(String phone) {


		String d = "tel:" + phone;
		Log.v("Make call", "" + d);

		if (ActivityCompat.checkSelfPermission(MemeberDetails.this,
				android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse(d));
			startActivity(callIntent);
		} else {
			Toast.makeText(MemeberDetails.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
		}
	}
}
