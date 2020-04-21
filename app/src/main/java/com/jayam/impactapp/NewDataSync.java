package com.jayam.impactapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jayam.impactapp.businesslayer.GetDataBL;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.LucDemandsDO;
import com.jayam.impactapp.objects.NPSDemandDO;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.BitMapUtils;
import com.jayam.impactapp.utils.NetworkUtility;
import com.jayam.impactapp.utils.SharedPrefUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by administrator_pc on 29-02-2020.
 */

public class NewDataSync extends Baselogin implements DataListner {
    RegularDemandsBLTemp demandsBLTemp;
    private LinearLayout llDataSync;
    private ImageView btnrepayment,btnUpload, btnlateupdate, btnregupdate,btnodupdate,btnadvupdate,btnDownLoad, btnODDemand, btnLUCDownLoad, btnLUCUpload;
    private ImageView  btnUploadImage, btnAdvDownload
            ;
    private GetDataBL getDataBL;
    private RegularDemandsBL regularDemandsBL;
    private ArrayList<RegularDemandsDO> alRegularDemands;
    private Transaction_OD_BL transaction_OD_BL;
    private ODDemandsBL odDemands;
    private TrnsactionsBL trnsactionsBL;
    private ArrayList<ODDemandsDO> alOdDemandsDOs;
    private ArrayList<AdvaceDemandDO> alAdvanceDemands;
    private ArrayList<NPSDemandDO> alnpsDemands;
    private ArrayList<LucDemandsDO> alLUcDemads;
    private ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    private IntialParametrsBL intialParametrsBL;
    private Stack<String> image_names = new Stack<String>();;
    private File root;
    private AdvanceDemandBL advdemadsbl;

    public boolean isRegular;
    public boolean isAdvance = false;
    public boolean isNPS = false;
    public boolean isOD = false;
    public boolean isLUC = false;
    public String macid = null;
    public String printerAddress = null;
    public String ReceiptNo = null;
    public String TxnCode = null;
    String TOTAL_AMT_UPLOADED_REG;
    public static String TOTAL_ACCOUNTS_REG, TOTAL_ACCOUNTS_NPS, TOTAL_AMT_UPLOADED_NPS, TOTAL_AMT_UPLOADED_ADV;
    public static String TOTAL_AMT_UPLOADED_OD, TOTAL_ACCOUNTS_OD, TOTAL_ACCOUNTS_ADV, TOTAL_ACCOUNTS_LUC;
    AdvanceDemandBL advanceBL;
    public static final int RequestPermissionCode = 1;
    @Override
    public void initialize() {
        intializeControlles();
        checkAndRequestPermissions();
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        advanceBL=new AdvanceDemandBL();

        intialParametrsBL = new IntialParametrsBL();
        alIntialParametrsDOs = intialParametrsBL.SelectAll(NewDataSync.this);
        IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
        printerAddress = intialParametrsDO.BTPrinterAddress;
        ReceiptNo = intialParametrsDO.LastTransactionID;
        TxnCode = intialParametrsDO.LastTransactionCode;
        macid = SharedPrefUtils.getKeyValue(NewDataSync.this, AppConstants.pref_name, AppConstants.macid);
        advdemadsbl = new AdvanceDemandBL();

        getDataBL = new GetDataBL(NewDataSync.this, NewDataSync.this);
        regularDemandsBL = new RegularDemandsBL();

        transaction_OD_BL = new Transaction_OD_BL();
        trnsactionsBL = new TrnsactionsBL();
        odDemands = new ODDemandsBL();
        alOdDemandsDOs = transaction_OD_BL.SelectAllFlagWise(NewDataSync.this);
        demandsBLTemp = new RegularDemandsBLTemp();
        alRegularDemands = trnsactionsBL.SelectAllFlagwise(NewDataSync.this);

        TOTAL_AMT_UPLOADED_REG = regularDemandsBL.getTOTALDemandAmountForRegular(NewDataSync.this);/// c.getInt(c.getColumnIndex("CollectionAmount"))
        /// ;
        /// //regularDemandsBL.getTOTALDemandAmountForRegular();
        TOTAL_ACCOUNTS_REG = regularDemandsBL.getTOTALRegularAccounts(NewDataSync.this);
        TOTAL_AMT_UPLOADED_OD = transaction_OD_BL.getTotalCollectedAmount(NewDataSync.this);
        TOTAL_ACCOUNTS_OD = transaction_OD_BL.getNumberOfAccouts(NewDataSync.this);

        TOTAL_AMT_UPLOADED_ADV = advdemadsbl.getTOTALDemandAmountForAdv(NewDataSync.this);
        TOTAL_ACCOUNTS_ADV = advdemadsbl.getTOTALAdvAccounts(NewDataSync.this);
        root = new File(
                Environment.getExternalStorageDirectory() + File.separator + AppConstants.FolderName + File.separator);

        if (root != null && root.list() != null && root.list().length > 0) {
            String[] files = root.list();

            for (String file : files) {
                image_names.push(file);
            }
        }

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        ivLogout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(NewDataSync.this, loginActivity.class);
//                startActivity(i);
//                // setResult(AppConstants.RESULTCODE_LOGOUT);
//                // finish();
//            }
//        });

        btnDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtility.isNetworkConnectionAvailable(NewDataSync.this)) {
                    Intent intent = new Intent(NewDataSync.this, DemandDates.class);
                    startActivityForResult(intent, 123);
                } else {
                    showAlertDailog("No Network Available.");
                }

            }
        });


        btnlateupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewDataSync.this);
                alertDialogBuilder.setMessage("Late Collection Flag Update successfully");
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        trnsactionsBL.UpdateFlagTimeLate(NewDataSync.this);

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();



            }
        });


        btnregupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewDataSync.this);
                alertDialogBuilder.setMessage("Regular Collection Flag Update successfully");
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        trnsactionsBL.UpdateFlagTimeReg(NewDataSync.this);

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();



            }
        });


        btnodupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewDataSync.this);
                alertDialogBuilder.setMessage("OD Collection Flag Update successfully");
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        trnsactionsBL.UpdateFlagTimeOD(NewDataSync.this);

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();



            }
        });



        btnadvupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewDataSync.this);
                alertDialogBuilder.setMessage("Advance Collection Flag Update successfully");
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        trnsactionsBL.UpdateFlagTimeAdvance(NewDataSync.this);

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();



            }
        });


        btnODDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtility.isNetworkConnectionAvailable(NewDataSync.this)) {
                    Transaction_OD_BL transaction_OD_BL = new Transaction_OD_BL();

                    ArrayList<ODDemandsDO> alArrayList = transaction_OD_BL.SelectAll(NewDataSync.this);

                    if (alArrayList != null && alArrayList.size() > 0) {
                        showAlertDailog("Please Upload the OD Collections.");
                    } else {
                        ODDemandsBL demandsBL = new ODDemandsBL();
                        demandsBL.Truncatetabel(NewDataSync.this);

                        Intent intent = new Intent(NewDataSync.this, ODDemands.class);
                        startActivity(intent);
                    }
                } else {
                    showAlertDailog("No Network Available.");
                }

            }
        });



        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtility.isNetworkConnectionAvailable(NewDataSync.this)) {
                    Transaction_OD_BL od_BL = new Transaction_OD_BL();
                    int ODCount = Integer.parseInt(od_BL.getrowCount(NewDataSync.this));
                    if (ODCount > 0) {
                        showAlertDailog("Please Upload the OD collection records.");
                    } else {
                        AdvanceDemandBL advanceBL = new AdvanceDemandBL();
                        int AdvaceCount = Integer.parseInt(advanceBL.CollectionRecords(NewDataSync.this));
                        if (AdvaceCount > 0) {
                            showAlertDailog("Upload Advance Transactions");
                        }
                        else {

                        }
                    }
                } else {
                    showAlertDailog("No Network Available.");
                }
            }

        });

        btnAdvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (NetworkUtility.isNetworkConnectionAvailable(NewDataSync.this)) {
                    Transaction_OD_BL od_BL = new Transaction_OD_BL();
                    int ODCount = Integer.parseInt(od_BL.getrowCount(NewDataSync.this));
                    if (ODCount > 0) {
                        showAlertDailog("Please Upload the OD collection records.");
                    } else {
                        AdvanceDemandBL advanceBL = new AdvanceDemandBL();
                        int MC = Integer.parseInt(advanceBL.CollectionRecords(NewDataSync.this));
                        if (MC > 0) {
                            showAlertDailog("Upload Advance Transactions");
                        } else {
                            advanceBL.Truncatetabel(NewDataSync.this);
                            Intent intent = new Intent(NewDataSync.this, AdvanceDemands.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    showAlertDailog("No Network Available.");
                }
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void intializeControlles() {

        llDataSync = (LinearLayout) inflater.inflate(R.layout.newdatasynch, null);

        btnDownLoad = (ImageView) llDataSync.findViewById(R.id.btnDownLoad);
        btnODDemand = (ImageView) llDataSync.findViewById(R.id.btnODDemand);
        btnAdvDownload = (ImageView) llDataSync.findViewById(R.id.btnAdvDownload);
        btnUploadImage = (ImageView) llDataSync.findViewById(R.id.btnUploadImage);
        btnregupdate = (ImageView) llDataSync.findViewById(R.id.btnregupdate);
        btnodupdate = (ImageView) llDataSync.findViewById(R.id.btnodupdate);
        btnadvupdate = (ImageView) llDataSync.findViewById(R.id.btnadvupdate);
        btnlateupdate = (ImageView) llDataSync.findViewById(R.id.btncolupdate);




        llBaseMiddle.addView(llDataSync, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        tvHeader.setText(getResources().getString(R.string.datasync));
        showHomeIcons();
        ivLogout.setVisibility(View.GONE);
    }

    @Override
    public void onDataretrieved(Object objs) {

    }

    @Override
    public void onDataretrievalFailed(String errorMessage) {
        HideLoader();
        Log.e("onDataretrieved", "fails");
    }

    public void uploadBitMaps() {

        if (image_names.size() > 0) {
            String imageName = image_names.pop();
            String img = imageName.substring(0, imageName.lastIndexOf("."));
            IntialParametrsBL bl = new IntialParametrsBL();
            String uid = bl.SelectAll(NewDataSync.this).get(0).TerminalID;

            BitMapUtils bitMapUtils = new BitMapUtils();
            String xex = bitMapUtils.getImage(imageName);

            GetDataBL getDataBL = new GetDataBL(NewDataSync.this, new DataListner() {

                @Override
                public void onDataretrieved(Object objs) {
                    uploadBitMaps();
                }

                @Override
                public void onDataretrievalFailed(String errorMessage) {

                }
            });

            getDataBL.uploadImage(uid, xex, img);
        } else {
            if (root.isDirectory()) {
                String[] children = root.list();
                for (int i = 0; i < children.length; i++) {
                    new File(root, children[i]).delete();
                }
            }
            showAlertDailog("Photos uploaded successfully");
            HideLoader();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppConstants.RESULTCODE_HOME) {
            setResult(resultCode);
            finish();
        }
    }

    private boolean checkAndRequestPermissions() {

        int readphone = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        int locationPermission1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

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
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), RequestPermissionCode);
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode /*1*/:
                Map<String, Integer> perms = new HashMap();

                perms.put(android.Manifest.permission.READ_PHONE_STATE, Integer.valueOf(0));
                perms.put("android.permission.CAMERA", Integer.valueOf(0));
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, Integer.valueOf(0));
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Integer.valueOf(0));
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i ++) {
                        perms.put(permissions[i], Integer.valueOf(grantResults[i]));
                    }
                    if (((Integer) perms.get("android.permission.CAMERA")).intValue() == 0 && ((Integer) perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE)).intValue() == 0  && ((Integer) perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)).intValue() == 0&& ((Integer) perms.get(android.Manifest.permission.READ_PHONE_STATE)).intValue() == 0) {
                        Log.v("", "sms & location services permission granted");
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }
}
