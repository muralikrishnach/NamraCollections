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
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

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
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.DialogUtils;
import com.jayam.impactapp.utils.ImageCompress;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.StringUtils;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LateMemeberDetails extends Base implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback, PermissionUtils.PermissionResultCallback {
    File sdImageMainDirectory;
    LinearLayout mphotolayout;
    private TrnsactionsBL trnsactionsBL;
    String Latecheck="";
    CheckBox latecheck;
    String Ttype="";
    RadioButton cashwithdrae,balancewnq;
    RadioGroup trantype;
    LinearLayout Translayout,Latelayout;
    String Responseda="";
    LinearLayout Aadhralayout;
    public	String dlatitude, dlangitude;
    ImageView buttoncall,btnlocation,btnCaptureImage;

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


    String[] paymentarr = new String[]{"Cash"};
    double latitude = 0.0;
    double longitude = 0.0;
//	String merchantId = "jayam";
//	String password = "1234";      //suresh

    private LinearLayout llMemeberDetails, llpaidAmt;
    private TextView tvproductname,tvMemebrName, tvMemberCode, tvDemanddate, tvRegDemandAmount, tvODDemandsAmount, tvPaidAmount,tvphotopath,
            tvInstall, tvLoanAccNo, tvNxtPayDate, paymentmode;
    private EditText etAmountTobeCollected, aadhaarEt, mobileEt,imagepath;
    public LinearLayout llNextInsdate;
    private Button btnSave,btnmobileno;
    private IntialParametrsBL intialParametrsBL;
    private ArrayList<IntialParametrsDO> altialParametrsDO;
    private RegularDemandsDO regularDemandsDO_Temp;
    private RegularDemandsDO regularDemandsDO_Original;
    private double totalamount;
    private double finalamount;
    private double rdamount;
    public String RgAmount="";
    private String MLAI_ID;
    private String TxnType;
    private RegularDemandsBLTemp regularDemandsBLTemp;
    private RegularDemandsBL regularDemandsBL;
    private float Savedamt = 0;
    private float RegularDemnand = 0;
    private float CollectedAmt = 0;
    private float ODAmt = 0;
    private IntialParametrsDO intialParametrsDO;

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
    String LastTranScode="";
    String FileName="";

    //	String merchantId = "Dillipa";
//	String password = "1234";
    @Override
    public void initialize() {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        trnsactionsBL=new TrnsactionsBL();
        Latecheck="No";
        UserId = SharedPrefUtils.getKeyValue(LateMemeberDetails.this, AppConstants.pref_name, AppConstants.username);
        mContext = LateMemeberDetails.this;
        permissionUtils = new PermissionUtils(LateMemeberDetails.this);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add("android.permission.CAMERA");
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

        regularDemandsBL = new RegularDemandsBL();
        intialParametrsBL = new IntialParametrsBL();
        altialParametrsDO = intialParametrsBL.SelectAll(LateMemeberDetails.this);
        intialParametrsDO = altialParametrsDO.get(0);

        if(intialParametrsDO!=null)
        {
            LUserID=intialParametrsDO.UserID;
            Branchname=intialParametrsDO.BranchName;
            BranchID=intialParametrsDO.Code;
        }

        regularDemandsDO_Original = regularDemandsBL.SelectAllTransactionLate(MLAI_ID,LateMemeberDetails.this);
        if(regularDemandsDO_Original!=null)
        {
            MemberId=regularDemandsDO_Original.MLAI_ID;
            dlangitude=regularDemandsDO_Original.LongitudeMember;
            dlatitude=regularDemandsDO_Original.LatitudeMember;
            LastTranScode = StringUtils.getTransactionCode_CLate(regularDemandsDO_Original,LateMemeberDetails.this);
            Log.v("","LastTranScode"+LastTranScode);
           // FileName=LastTranScode+MemberId+".JPEG";
            FileName=LastTranScode+".JPEG";
            Log.v("","FileName"+FileName);
        }
        File root = new File(Environment.getExternalStorageDirectory() + File.separator + AppConstants.FolderName + File.separator);
        if (!root.exists()) {
            root.mkdirs();
        }
        sdImageMainDirectory = new File(root, LastTranScode + ".JPEG");
        intializeControlles();
        llpaidAmt.setVisibility(View.GONE);
        Latelayout.setVisibility(View.GONE);

        if(regularDemandsDO_Original!=null)
        {
            tvMemebrName.setText("" + regularDemandsDO_Original.MemberName);
            tvMemberCode.setText("" + regularDemandsDO_Original.MemberCode);
            tvODDemandsAmount.setText("" + regularDemandsDO_Original.ODAmount);
            tvInstall.setText("" + regularDemandsDO_Original.InstallNo);
            tvLoanAccNo.setText("" + regularDemandsDO_Original.MLAI_ID);
            tvNxtPayDate.setText("" + regularDemandsDO_Original.NextRepayDate);
            aadhaarEt.setText("" + regularDemandsDO_Original.AAdharNo);
            mobileEt.setText("" + regularDemandsDO_Original.MobileNo);
            tvproductname.setText(regularDemandsDO_Original.ProductName);
            tvDemanddate.setText("" + regularDemandsDO_Original.DemandDate);

            String colleamt=regularDemandsDO_Original.collectedAmount;
            String odamt=regularDemandsDO_Original.ODAmount;
            RgAmount=regularDemandsDO_Original.DemandTotal;

            Log.v("","RgAmount"+RgAmount);

            Log.v("","colleamt"+colleamt);
            Log.v("","odamt--"+odamt);

            try
            {
                if(RgAmount!=null)
                {
                    rdamount=Double.parseDouble(RgAmount);
                }
            }catch (NumberFormatException n)
            {
                n.printStackTrace();
            }
            double camount=0;
            double overdamt=0;
            try
            {
                if(colleamt!=null)
                {
                    camount=Double.parseDouble(colleamt);
                }
            }catch (NumberFormatException n)
            {
                n.printStackTrace();
            }

            try
            {
                if(odamt!=null)
                {
                    overdamt=Double.parseDouble(odamt);
                }
            }catch (NumberFormatException n)
            {
                n.printStackTrace();
            }

            totalamount =camount+overdamt;

            Log.v("","totalamount"+totalamount);

            finalamount=rdamount-camount;
            Log.v("","finalamount"+finalamount);

            tvRegDemandAmount.setText("" + finalamount);

        }


        btnSave.setOnClickListener(new View.OnClickListener() {
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

                    if (amount >= 0 && amount <= finalamount)
                    {
                        savePayment(amount,AAdahr,Mobileno);
                    }
                    else {
                        showAlertDailog("Coll Amount Cannot be greater than Total Demand.");
                    }
                }

            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(AppConstants.RESULTCODE_HOME);
                finish();
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LateMemeberDetails.this, loginActivity.class);
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
        Latelayout = (LinearLayout) llMemeberDetails.findViewById(R.id.latelayout);
        trantype = (RadioGroup) llMemeberDetails.findViewById(R.id.groupradio);

        etAmountTobeCollected = (EditText) llMemeberDetails.findViewById(R.id.etAmountTobeCollected);
        tvPaidAmount = (TextView) llMemeberDetails.findViewById(R.id.tvPaidAmount);
        tvNxtPayDate = (TextView) llMemeberDetails.findViewById(R.id.tvNextPayDate);
        llpaidAmt = (LinearLayout) llMemeberDetails.findViewById(R.id.llpaidAmt);
        buttoncall=(ImageView)  llMemeberDetails.findViewById(R.id.btncall);
        btnlocation=(ImageView)  llMemeberDetails.findViewById(R.id.btnlocation);
        btnSave = (Button) llMemeberDetails.findViewById(R.id.btnSave);
        btnmobileno= (Button) llMemeberDetails.findViewById(R.id.btnmobile);
        mphotolayout = (LinearLayout) llMemeberDetails.findViewById(R.id.photoayout);
        tvInstall = (TextView) llMemeberDetails.findViewById(R.id.tvInstall);

        tvproductname = (TextView) llMemeberDetails.findViewById(R.id.tvproductname);
        tvphotopath = (TextView) llMemeberDetails.findViewById(R.id.photoedit);

        btnCaptureImage=(ImageView)  llMemeberDetails.findViewById(R.id.imageview);

        mphotolayout = (LinearLayout) llMemeberDetails.findViewById(R.id.photoayout);

        btnmobileno.setVisibility(View.GONE);
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, 999);
            }
        });

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
        llBaseMiddle_lv.addView(llMemeberDetails, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
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
                if(mobileEt.getText().toString().trim().length()==10){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LateMemeberDetails.this);
                    alertDialogBuilder.setMessage("Mobile No Update successfully");
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            regularDemandsBLTemp.updateMobilenoregtemp(regularDemandsDO_Temp.MLAI_ID, mobileEt.getText().toString(),LateMemeberDetails.this);
                            regularDemandsBLTemp.updateMobilenoreg(regularDemandsDO_Temp.MLAI_ID, mobileEt.getText().toString(),LateMemeberDetails.this);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }else{
                    DialogUtils.showAlert(LateMemeberDetails.this,"Please Enter Mobile No it must be 10 digits");

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
                    DialogUtils.showAlert(LateMemeberDetails.this,"Please Enter Mobile No it must be 10 digits");

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
        btnlocation.setOnClickListener(new View.OnClickListener() {
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
        paymentmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogUtils.showSingleChoiceLIstItems(LateMemeberDetails.this,
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
        llNextInsdate.setVisibility(View.GONE);
//        if ((intialParametrsDO.InstRequired).equals("0")) {
//            llNextInsdate.setVisibility(View.GONE);
//        } else {
//            llNextInsdate.setVisibility(View.VISIBLE);
//        }
    }

    public void savePayment(float paymnet,String aadharno,String mobileno) {
        String imagepath= tvphotopath.getText().toString();
        Log.v("","imagepath--"+imagepath);
        if(imagepath!=null&&!imagepath.isEmpty())
        {
            trnsactionsBL.UpdateLateAmount(paymnet+"",MLAI_ID,imagepath,LateMemeberDetails.this);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LateMemeberDetails.this);
            alertDialogBuilder.setMessage("Data Saved Successfully");
            alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    Intent inten=new Intent(LateMemeberDetails.this,NavigationActivity.class);
                    startActivity(inten);
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else
        {
            showAlertDailog("Please Take  Member Photo");
        }



//        //paymnet=1;
//        //String amount="1";
//        String paymentmod = paymentmode.getText().toString();
//        if(paymentmod!=null && paymentmod.trim().equalsIgnoreCase("Cash"))
//        {
//            String enterdAmount = etAmountTobeCollected.getText().toString();
//
//            //float paymnet = Float.valueOf(enterdAmount.trim()).floatValue();
//
//
//            totalamount = Float.valueOf(regularDemandsDO_Temp.updated.trim()).floatValue();
//            float amount_collects = Float.valueOf(SharedPrefUtils
//                    .getKeyValue(LateMemeberDetails.this, AppConstants.memberDetails_pref, AppConstants.amountTobeCollected)
//                    .trim()).floatValue();
//
//            if (totalamount != paymnet) {
//                KeyValue keyValue_amountToBeCollected = new KeyValue(AppConstants.amountTobeCollected, (amount_collects - paymnet) + "");
//                SharedPrefUtils.setValue(LateMemeberDetails.this, AppConstants.memberDetails_pref, keyValue_amountToBeCollected);
//                KeyValue keyValue_memeberCode = new KeyValue(regularDemandsDO_Temp.MemberCode, (paymnet) + "");
//                SharedPrefUtils.setValue(LateMemeberDetails.this, AppConstants.memberDetails_pref, keyValue_memeberCode);
//            }
//
//            RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
//            regularDemandsBLTemp.updateregularDemandForMemeber(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet), String.valueOf(paymnet).toString());
//            regularDemandsBL.updateLate(regularDemandsDO_Temp.MLAI_ID, Latecheck);
//            regularDemandsBLTemp.updateregulartempLate(regularDemandsDO_Temp.MLAI_ID, Latecheck);
//
//
//            regularDemandsBL.updateSavedAmt(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet).toString());
//            regularDemandsBL.updateCashless(regularDemandsDO_Temp.MLAI_ID,"","",paymentmod,TXN_ID,"","","");
//            regularDemandsBLTemp.updateCashlessMemeberTemp(regularDemandsDO_Temp.MLAI_ID, "","",paymentmod,TXN_ID,"","","");
//
//            regularDemandsBLTemp.updateMobilenoregtemp(regularDemandsDO_Temp.MLAI_ID, mobileEt.getText().toString());
//            regularDemandsBLTemp.updateMobilenoreg(regularDemandsDO_Temp.MLAI_ID, mobileEt.getText().toString());
//
//            if (((paymnet + CollectedAmt) < Float.valueOf(regularDemandsDO_Temp.DemandTotal)) && ODAmt == 0) {
//
//                Intent i = new Intent(LateMemeberDetails.this, Ftodreasons.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("MLAID", "" + regularDemandsDO_Temp.MLAI_ID);
//                bundle.putString("txntype", "" + TxnType);
//                i.putExtras(bundle);
//                startActivityForResult(i, 111);
//
//            } else {
//                Intent intent1 = new Intent(this, Paymentconfiramtion.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("paymnet", "" + paymnet);
//                bundle.putString("MemberName", "" + regularDemandsDO_Temp.MemberName);
//                bundle.putString("MemberCode", "" + regularDemandsDO_Temp.MemberCode);
//                bundle.putString("DemandDate", "" + regularDemandsDO_Temp.DemandDate);
//                bundle.putString("txntype", "" + TxnType);
//                intent1.putExtras(bundle);
//                startActivityForResult(intent1, 0);
//            }


   //     }





//        else
//        {
//
//            if(paymentmod != null)
//            {
//                String amount="";
//                try
//                {
//                    amount = Float.toString(paymnet);
//                }catch (NumberFormatException n)
//                {
//                    n.printStackTrace();
//                }
//                Date endsd = new Date();
//                SimpleDateFormat sdfr = new SimpleDateFormat("ddMMyyyyHHmmss");
//                try {
//                    TXN_ID = MemberId+UserId+sdfr.format(endsd);
//
//                } catch (Exception ex) {
//                    System.out.println(ex);
//                }
//
//
//                if (paymentmod != null && paymentmod.trim().equalsIgnoreCase("AEPS"))
//                {
//                    AepsSdkConstants.transactionType=Ttype;
//                    AepsSdkConstants.transactionAmount = amount;
//                    AepsSdkConstants.paramA = AppConstants.paramA;
//                    AepsSdkConstants.paramB =  AppConstants.paramB;
//                    AepsSdkConstants.paramC =MemberId;
//                    AepsSdkConstants.encryptedData =  AppConstants.encryptedData;
//                    AepsSdkConstants.loginID =  AppConstants.loginID;
//
//                    Intent intent = new Intent(LateMemeberDetails.this, AEPSHomeActivity.class);
//                    startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
//                }
//
//
//                else if (paymentmod.trim().equalsIgnoreCase("pg"))
//                {
//                    Intent intent = new Intent(LateMemeberDetails.this, AEPSHomeActivity.class);
//                    startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
//                }
//                else if (paymentmod.trim().equalsIgnoreCase("upi"))
//                {
//                    Intent intent = new Intent(LateMemeberDetails.this, AEPSHomeActivity.class);
//                    startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
//                }
//            }
//        }

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
            datetime= UtilsClass.GetCurrentdateTime();
            timev= UtilsClass.GetCurrentdateNanoTime();
            Responseda=data.getStringExtra(AepsSdkConstants.responseData);
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LateMemeberDetails.this);
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
                            .getKeyValue(LateMemeberDetails.this, AppConstants.memberDetails_pref, AppConstants.amountTobeCollected)
                            .trim()).floatValue();

                    if (totalamount != paymnet) {
                        KeyValue keyValue_amountToBeCollected = new KeyValue(AppConstants.amountTobeCollected, (amount_collects - paymnet) + "");
                        SharedPrefUtils.setValue(LateMemeberDetails.this, AppConstants.memberDetails_pref, keyValue_amountToBeCollected);
                        KeyValue keyValue_memeberCode = new KeyValue(regularDemandsDO_Temp.MemberCode, (paymnet) + "");
                        SharedPrefUtils.setValue(LateMemeberDetails.this, AppConstants.memberDetails_pref, keyValue_memeberCode);
                    }

                    RegularDemandsBLTemp regularDemandsBLTemp = new RegularDemandsBLTemp();
                    regularDemandsBLTemp.updateregularDemandForMemeber(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet), String.valueOf(paymnet).toString(),LateMemeberDetails.this);
                    regularDemandsBL.updateLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,LateMemeberDetails.this);
                    regularDemandsBLTemp.updateregulartempLate(regularDemandsDO_Temp.MLAI_ID, Latecheck,LateMemeberDetails.this);
                    regularDemandsBL.updateSavedAmt(regularDemandsDO_Temp.MLAI_ID, String.valueOf(paymnet).toString(),LateMemeberDetails.this);
                    regularDemandsBL.updateCashless(regularDemandsDO_Temp.MLAI_ID,referenceNo,statusDesc,paymentmod,TXN_ID,BankName,timev,datetime,LateMemeberDetails.this);
                    regularDemandsBLTemp.updateCashlessMemeberTemp(regularDemandsDO_Temp.MLAI_ID, referenceNo,statusDesc,paymentmod,TXN_ID,BankName,timev,datetime,LateMemeberDetails.this);


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LateMemeberDetails.this);
                    alertDialogBuilder.setMessage(statusDesc);
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (((paymnet + CollectedAmt) < Float.valueOf(regularDemandsDO_Temp.DemandTotal)) && ODAmt == 0) {

                                Intent i = new Intent(LateMemeberDetails.this, Ftodreasons.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("MLAID", "" + regularDemandsDO_Temp.MLAI_ID);
                                bundle.putString("txntype", "" + TxnType);
                                i.putExtras(bundle);
                                startActivityForResult(i, 111);

                            } else {
                                Intent intent1 = new Intent(LateMemeberDetails.this, Paymentconfiramtion.class);
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




        else if (requestCode == 999 && resultCode == RESULT_OK) {

            Log.v("","FileName"+FileName);

            ImageCompress bmp= new ImageCompress();
            File  root = new File(Environment.getExternalStorageDirectory()+ File.separator + AppConstants.FolderName + File.separator);
            String image=   bmp.compressImage(root,FileName);
            String length=bmp.getFileSize(sdImageMainDirectory);
            Log.v("","length"+length);
            String repdata="";
            double val=0;
            if(length!=null&&!length.isEmpty())
            {
                if(length.contains("MiB"))
                {
                    System.out.println("if"+length);
                    repdata=length.replaceAll("MiB", "");
                    try
                    {
                        val=Double.parseDouble(repdata);
                    }catch (NumberFormatException n)
                    {
                        n.printStackTrace();
                    }
                    if(val>1)
                    {
                        DialogUtils.showAlert(LateMemeberDetails.this,"Image Size Exceed 1 MB Please take once Again");

                        new File(sdImageMainDirectory, FileName).delete();
                    }
                    else
                    {
                        showAlertDailog("Photo Captured successfully", "OK", new CustomDailoglistner() {

                            public void onPossitiveButtonClick(DialogInterface dialog) {
                                tvphotopath.setText(FileName);
                                dialog.dismiss();

                            }


                            public void onNegativeButtonClick(DialogInterface dialog) {

                            }
                        });
                    }

                }
                else if(length.contains("KiB"))
                {

                    showAlertDailog("Photo Captured successfully", "OK", new CustomDailoglistner() {
                        @Override
                        public void onPossitiveButtonClick(DialogInterface dialog) {
                            tvphotopath.setText(FileName);
                            dialog.dismiss();

                        }

                        @Override
                        public void onNegativeButtonClick(DialogInterface dialog) {

                        }
                    });
                }


                else
                {
                    DialogUtils.showAlert(LateMemeberDetails.this,"Image is Not Compressed");
                    new File(sdImageMainDirectory, FileName).delete();
                }
            }
            else
            {
                DialogUtils.showAlert(LateMemeberDetails.this,"Image is Not Compressed");
                new File(sdImageMainDirectory, FileName).delete();
            }





            // StoreImage(MeetingTime_Centerwise.this, Uri.parse(data.toURI()),
            // sdImageMainDirectory);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        regularDemandsDO_Original = regularDemandsBL.SelectAllTransactionLate(MLAI_ID,LateMemeberDetails.this);
        // totalamount=
        // Float.valueOf(regularDemandsDO_Temp.updated.trim()).floatValue();
        Log.e("totalamout1111", String.valueOf(totalamount));
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
                            status.startResolutionForResult(LateMemeberDetails.this, REQUEST_CHECK_SETTINGS);

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



    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    private void onCallBtnClick(String no) {

        if (Build.VERSION.SDK_INT < 23) {
            phoneCall(no);
        } else {

            if (ActivityCompat.checkSelfPermission(LateMemeberDetails.this,
                    android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall(no);
            } else {
                final String[] PERMISSIONS_STORAGE = {android.Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(LateMemeberDetails.this, PERMISSIONS_STORAGE, 9);
            }
        }
    }




    private void phoneCall(String phone) {


        String d = "tel:" + phone;
        Log.v("Make call", "" + d);

        if (ActivityCompat.checkSelfPermission(LateMemeberDetails.this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(d));
            startActivity(callIntent);
        } else {
            Toast.makeText(LateMemeberDetails.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }
}

