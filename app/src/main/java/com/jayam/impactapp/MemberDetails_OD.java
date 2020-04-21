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
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
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
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.ODDemandsDO;
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

import java.security.MessageDigest;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




public class MemberDetails_OD extends Base  implements ConnectionCallbacks,OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback,PermissionUtils.PermissionResultCallback
{
	String Ttype="";
	LinearLayout Translayout;
	RadioButton cashwithdrae,balancewnq;
	RadioGroup trantype;
	public	String dlatitude, dlangitude;
	ImageView buttoncall,btnlocation;
	String bankRrn="";
	String transactionStatusMessage="";
	private IntialParametrsBL intialParametrsBL;

	MediaPlayer mPlayer;
	LinearLayout Aadhralayout;
	String UserId,MemberId,LUserID,BranchID,Branchname,LoanID="";

	String MerchantHistoryID="10243";
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;

    private Location mLastLocation;
	String Responseda="";
    // Google client to interact with Google API

    private GoogleApiClient mGoogleApiClient;
    boolean isPermissionGranted;
    ArrayList<String> permissions=new ArrayList<>();
	private static final String TAG = MemberDetails_OD.class.getSimpleName();
    PermissionUtils permissionUtils;

	String[] paymentarr = new String[]{"Cash", "AEPS", "matm", "pg", "upi"};
	public static final int RequestPermissionCode = 1;
	double latitude=0.0;
	double longitude=0.0;
//	String merchantId = "jayam";
//	String password = "1234";
	private static final int CODE = 1;

	public static final String SUPER_MERCHANT_ID = "235"; // will be given by FingPay send that only from App to SDK
	public static  String TXN_ID = "";
	public String imei = "";
	private TextView tvproductname, tvCenters,tvGroups,tvMemberName, tvLoanacNum, tvOSAmt,paymentmode;
	private EditText etAmountTobeCollected,aadhaarEt, mobileEt;
	private LinearLayout llMemberDetails;
	private Button btnSave;
	private String collectedAmt;
	private Transaction_OD_BL transaction_OD_BL;
	private float amount_paid_previously;
	ODDemandsDO demandsDO=null;
	private Context mContext;
	GPSTrackevalue gps;
//	String merchantId = "Dillipa";
//	String password = "1234";
	String merchantId = "Ampl";
	String password = "1234";

	private ArrayList<IntialParametrsDO> altialParametrsDO;
	private IntialParametrsDO intialParametrsDO;
	@Override
	public void initialize()
	{
		intialParametrsBL = new IntialParametrsBL();
		altialParametrsDO = intialParametrsBL.SelectAll(MemberDetails_OD.this);


		if(altialParametrsDO!=null)
		{
			try
			{
				intialParametrsDO = altialParametrsDO.get(0);

			}catch (IndexOutOfBoundsException in)
			{
				in.printStackTrace();
			}
		}



		if(intialParametrsDO!=null)
		{
			LUserID=intialParametrsDO.UserID;
			Branchname=intialParametrsDO.BranchName;
			BranchID=intialParametrsDO.Code;

			Log.v("","LUserIDin"+LUserID);

		}
		UserId = SharedPrefUtils.getKeyValue(MemberDetails_OD.this, AppConstants.pref_name, AppConstants.username);
		mContext = MemberDetails_OD.this;
        permissionUtils=new PermissionUtils(MemberDetails_OD.this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions,"Need GPS permission for getting your location",1);




		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// write code to get permission from user to READ_PHONE_STATE to get imei
		} else {
			TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();

		}
		String memberCode = getIntent().getExtras().getString("memberCode");
		ODDemandsBL odDemandsBL = new ODDemandsBL();

		Log.v("","memberCode"+memberCode);



		ArrayList<ODDemandsDO> alist=odDemandsBL.SelectAllMemberDetails(memberCode,MemberDetails_OD.this);

		if(alist!=null)
		{
			try
			{
				demandsDO = alist.get(0);
			}catch (IndexOutOfBoundsException in)
			{
				in.printStackTrace();
			}
		}
		intializeControlles();
		if(demandsDO!=null)
		{
			MemberId=demandsDO.MLAI_ID;
			tvCenters.setText(""+demandsDO.MCI_Name);
			tvGroups.setText(""+demandsDO.MGI_Name);
			tvMemberName.setText(""+demandsDO.MMI_Name+"("+demandsDO.MMI_Code+")");
			tvLoanacNum.setText(""+demandsDO.MLAI_ID);
			tvOSAmt.setText(""+demandsDO.OSAmt);
			etAmountTobeCollected.setText(""+demandsDO.ODAmt);
			aadhaarEt.setText(""+demandsDO.AAdharNo);
			mobileEt.setText(""+demandsDO.MobileNo);
			tvproductname.setText(demandsDO.ProductName);
			dlangitude=demandsDO.LongitudeMember;
			dlatitude=demandsDO.LatitudeMember;
			transaction_OD_BL = new Transaction_OD_BL();
			ArrayList<String> alArrayList = transaction_OD_BL.SelectAll(demandsDO.MLAI_ID,MemberDetails_OD.this);
			for(String str : alArrayList)
			{
				amount_paid_previously = Float.valueOf(str).floatValue()+amount_paid_previously;
			}
			Log.e("amount_paid_previously", ""+amount_paid_previously);
		}



		btnSave.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{



				if(mLastLocation!=null)
				{
					latitude = mLastLocation.getLatitude();
					longitude = mLastLocation.getLongitude();
				}


				Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


				String AAdahr = aadhaarEt.getText().toString();
				String Mobileno = mobileEt.getText().toString();

				String payment = paymentmode.getText().toString();


				if (payment.length()==0) {
					showAlertDailog("Please Select PaymentMode.");
				}

//				else if (payment!=null && payment.trim().equalsIgnoreCase("CashLess") &&AAdahr.length()<12) {
//					showAlertDailog("Please Enter AAdhar No and it must be 12 digits.");
//				}


				else if (payment!=null && payment.trim().equalsIgnoreCase("CashLess") && Mobileno.length()<10) {
					showAlertDailog("Please Enter Mobile No and it must be 10 digits.");
				}
				else if (!Mobileno.isEmpty()&&Mobileno!=null&&Mobileno.length()>0 && Mobileno.length()<10) {
					showAlertDailog("Please Enter Mobile No and it must be 10 digits.");
				}
				else if (payment!=null && payment.trim().equalsIgnoreCase("CashLess") &&  trantype.getCheckedRadioButtonId()==-1) {
					showAlertDailog("Please Select Transaction Type.");
				}
				else
				{

					collectedAmt = etAmountTobeCollected.getText().toString();
					float collcamt=0;

					float CollAmt=0;
					float ODAmt=0;


					try
					{
						collcamt = Float.valueOf(collectedAmt).floatValue();
					}catch (NumberFormatException n)
					{
						n.printStackTrace();
					}


					try
					{
						CollAmt=collcamt+amount_paid_previously;
					}catch (NumberFormatException n)
					{
						n.printStackTrace();
					}


					try
					{
						ODAmt=Float.valueOf(demandsDO.ODAmt).floatValue();
					}catch (NumberFormatException n)
					{
						n.printStackTrace();
					}


					if(CollAmt <= ODAmt)
					{



						if(payment!=null && payment.trim().equalsIgnoreCase("Cash"))
						{
							collectedAmt = etAmountTobeCollected.getText().toString();
							Intent intent = new Intent(MemberDetails_OD.this, TransactionACK_OD.class);
							intent.putExtra("obj", demandsDO);
							intent.putExtra("amount", collectedAmt);
							intent.putExtra("BANKRRN", "");
							intent.putExtra("Response", "");
							intent.putExtra("PaymentMode", payment);
							intent.putExtra("TXNID", TXN_ID);
							intent.putExtra("MobileNo", mobileEt.getText().toString());
							startActivityForResult(intent,1234);
						}
						else
						{
							if(payment != null)
							{
								collectedAmt = etAmountTobeCollected.getText().toString();
								String amount="";
								String aadharno = aadhaarEt.getText().toString();
								Log.v("","AAdahrsa"+aadharno);
								Log.v("","SUPER_MERCHANT_ID"+SUPER_MERCHANT_ID);
								Log.v("","LUserID"+LUserID);
								Toast.makeText(MemberDetails_OD.this,"AAdharno"+aadharno,Toast.LENGTH_LONG).show();
								Date endsd = new Date();
								SimpleDateFormat sdfr = new SimpleDateFormat("ddMMyyyyHHmmss");
								try {
									TXN_ID = MemberId+UserId+sdfr.format(endsd);
								} catch (Exception ex) {
									System.out.println(ex);
								}
								AepsSdkConstants.transactionType=Ttype;
								AepsSdkConstants.transactionAmount = amount;
								AepsSdkConstants.paramA = AppConstants.paramA;
								AepsSdkConstants.paramB =  AppConstants.paramB;
								AepsSdkConstants.paramC =MemberId;
								AepsSdkConstants.encryptedData =  AppConstants.encryptedData;
								AepsSdkConstants.loginID =  AppConstants.loginID;

								if (payment != null && payment.trim().equalsIgnoreCase("AEPS"))
								{
									Intent intent = new Intent(MemberDetails_OD.this, AEPSHomeActivity.class);
									startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
								}
								else if (payment.trim().equalsIgnoreCase("matm"))
								{
									Intent intent = new Intent(MemberDetails_OD.this, AEPSHomeActivity.class);
									startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
								}
								else if (payment.trim().equalsIgnoreCase("pg"))
								{
									Intent intent = new Intent(MemberDetails_OD.this, AEPSHomeActivity.class);
									startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
								}
								else if (payment.trim().equalsIgnoreCase("upi"))
								{
									Intent intent = new Intent(MemberDetails_OD.this, AEPSHomeActivity.class);
									startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
								}
							}

						}


					}
					else
					{
//					// not allow payment
						showAlertDailog("OD Collection cannot be greater than OD Demand");
					}

				}

			}
		});


		ivHome.setOnClickListener(new  OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setResult(AppConstants.RESULTCODE_HOME);
				finish();
			}
		});

		ivLogout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(MemberDetails_OD.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llMemberDetails		= 	(LinearLayout) inflater.inflate(R.layout.memberdetails_od, null);
		tvCenters			=	(TextView)llMemberDetails.findViewById(R.id.tvCenterName);
		tvGroups			=	(TextView)llMemberDetails.findViewById(R.id.tvGroupName);
		tvMemberName		=	(TextView)llMemberDetails.findViewById(R.id.tvMemberName);
		tvLoanacNum			=	(TextView)llMemberDetails.findViewById(R.id.tvLoanacNum);
		tvOSAmt				=	(TextView)llMemberDetails.findViewById(R.id.tvOSAmt);
		btnSave				=	(Button)llMemberDetails.findViewById(R.id.btnSave);
		aadhaarEt = (EditText) llMemberDetails.findViewById(R.id.et_aadhaar);
		mobileEt = (EditText) llMemberDetails.findViewById(R.id.et_mobile);
		etAmountTobeCollected		=	(EditText)llMemberDetails.findViewById(R.id.etAmountTobeCollected);

		paymentmode = (TextView)llMemberDetails. findViewById(R.id.payment);

		buttoncall=(ImageView)  llMemberDetails.findViewById(R.id.btncall);
		btnlocation=(ImageView)  llMemberDetails.findViewById(R.id.btnlocation);
		tvproductname = (TextView) llMemberDetails.findViewById(R.id.tvproductname);
		Aadhralayout = (LinearLayout) llMemberDetails.findViewById(R.id.aadharlayout);
		Translayout = (LinearLayout) llMemberDetails.findViewById(R.id.translayout);
		trantype = (RadioGroup) llMemberDetails.findViewById(R.id.groupradio);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llMemberDetails, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Member Details");

		Aadhralayout.setVisibility(View.GONE);
		trantype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checked) {
				switch (checked)
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
					DialogUtils.showAlert(MemberDetails_OD.this,"Please Enter Mobile No it must be 10 digits");

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



				if(demandsDO!=null)
				{
					Branchpay=demandsDO.BranchPaymode;

					LoanID=demandsDO.MLAI_ID;
				}


		if(Branchpay!=null)
		{
			if (Branchpay.equalsIgnoreCase("0")) {

				paymentmode.setText("Cash");
				paymentmode.setEnabled(false);

			}

			else if (Branchpay.equalsIgnoreCase("1")) {

				paymentmode.setEnabled(true);
			}



		}
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
		paymentmode.setTag(-1);

		paymentmode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				DialogUtils.showSingleChoiceLIstItems(MemberDetails_OD.this,
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == AppConstants.RESULTCODE_LOGOUT)
		{
			setResult(resultCode);
			finish();
		}
		else if(resultCode == AppConstants.RESULTCODE_HOME)
		{
			setResult(resultCode);
			finish();
		}
		else if (resultCode == RESULT_OK && requestCode == AepsSdkConstants.REQUEST_CODE) {
			Responseda="";

			String timev="";
			String datetime="";
			datetime=UtilsClass.GetCurrentdateTime();
			timev= UtilsClass.GetCurrentdateNanoTime();
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
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberDetails_OD.this);
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
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberDetails_OD.this);
					alertDialogBuilder.setMessage(statusDesc);
					final String finalStatusDesc = statusDesc;
					final String finalReferenceNo = referenceNo;
					final String finalBankName = BankName;
					final String finalTimev = timev;
					final String finalDatetime = datetime;
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							String payment = paymentmode.getText().toString();
							Intent intent = new Intent(MemberDetails_OD.this, TransactionACK_OD.class);
							intent.putExtra("obj", demandsDO);
							intent.putExtra("amount", collectedAmt);
							intent.putExtra("BANKRRN", finalReferenceNo);
							intent.putExtra("Response", finalStatusDesc);
							intent.putExtra("PaymentMode", payment);
							intent.putExtra("MobileNo", mobileEt.getText().toString());
							intent.putExtra("TXNID", TXN_ID);
							intent.putExtra("BankName", finalBankName);
							intent.putExtra("TransactionTime", finalTimev);
							intent.putExtra("datetime", finalDatetime);
							startActivityForResult(intent,1234);

						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();

				}
			}
		}
			//respTv.setText("");
		}

	private boolean checkAndRequestPermissions() {

		int readphone = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
		int permissionSendMessage = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
		int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
		int locationPermission1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int wifi = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE);


		List<String> listPermissionsNeeded = new ArrayList();

		if (readphone != 0) {
			listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
		}

		if (locationPermission1 != 0) {
			listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}

		if (locationPermission != 0) {
			listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
		}
		if (permissionSendMessage != 0) {
			listPermissionsNeeded.add("android.permission.CAMERA");
		}
		if(wifi!=0){
			listPermissionsNeeded.add(android.Manifest.permission.ACCESS_WIFI_STATE);
		}

		if (listPermissionsNeeded.isEmpty()) {
			return true;
		}
		ActivityCompat.requestPermissions(this, (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), RequestPermissionCode);
		return false;
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
                            status.startResolutionForResult(MemberDetails_OD.this, REQUEST_CHECK_SETTINGS);

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
			 bankRrn="";
			 transactionStatusMessage="";
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
				Log.v("","transactionStatusMessage"+transactionStatusMessage);


				if(transactionStatusMessage!=null &&transactionStatusMessage.trim().equalsIgnoreCase("Success"))

				{
					try {
						mPlayer = MediaPlayer.create(MemberDetails_OD.this, R.raw.success);
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



					ivHome.setVisibility(View.GONE);
					ivLogout.setVisibility(View.GONE);

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberDetails_OD.this);
					alertDialogBuilder.setMessage(transactionStatusMessage);
					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							String payment = paymentmode.getText().toString();
							Intent intent = new Intent(MemberDetails_OD.this, TransactionACK_OD.class);
							intent.putExtra("obj", demandsDO);
							intent.putExtra("amount", collectedAmt);
							intent.putExtra("BANKRRN", bankRrn);
							intent.putExtra("Response", transactionStatusMessage);
							intent.putExtra("PaymentMode", payment);
							intent.putExtra("TXNID", TXN_ID);
							intent.putExtra("MobileNo", mobileEt.getText().toString());
							startActivityForResult(intent,1234);

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
						mPlayer = MediaPlayer.create(MemberDetails_OD.this, R.raw.failure);

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
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberDetails_OD.this);
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
	private void onCallBtnClick(String no) {

		if (Build.VERSION.SDK_INT < 23) {
			phoneCall(no);
		} else {

			if (ActivityCompat.checkSelfPermission(MemberDetails_OD.this,
					android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

				phoneCall(no);
			} else {
				final String[] PERMISSIONS_STORAGE = {android.Manifest.permission.CALL_PHONE};
				//Asking request Permissions
				ActivityCompat.requestPermissions(MemberDetails_OD.this, PERMISSIONS_STORAGE, 9);
			}
		}
	}


	private void phoneCall(String phone) {


		String d = "tel:" + phone;
		Log.v("Make call", "" + d);

		if (ActivityCompat.checkSelfPermission(MemberDetails_OD.this,
				android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse(d));
			startActivity(callIntent);
		} else {
			Toast.makeText(MemberDetails_OD.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
		}
	}
}
