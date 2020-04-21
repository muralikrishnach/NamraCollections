package com.jayam.impactapp;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayam.impactapp.businesslayer.GetDataBL;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.FtodreasonsBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.DialogUtils;
import com.jayam.impactapp.utils.PrintUtils;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.webacceslayer.ADVRegularScheduleService;
import com.jayam.impactapp.webacceslayer.LateSchedilerService;
import com.jayam.impactapp.webacceslayer.ODRegularScheduleService;
import com.jayam.impactapp.webacceslayer.RegularScheduleService;

import java.util.ArrayList;

import static com.jayam.impactapp.utils.DialogUtils.dismissProgress;
import static com.jayam.impactapp.utils.DialogUtils.showProgressManuval;

/**
 * Created by administrator_pc on 27-02-2020.
 */

public class NavigationActivity extends AppCompatActivity implements DataListner {
    int Rtotalmembers=0;
    private AdvanceDemandBL advdemadsbl;
    private ArrayList<RegularDemandsDO> alRegularDemandssave;
    ArrayList<ODDemandsDO> oddemands;
    ArrayList<ODDemandsDO> oddemandssave;
    private ArrayList<RegularDemandsDO> alRegularDemandssaveLate;

    ArrayList<com.jayam.impactapp.objects.AdvaceDemandDO> AdvaceDemandDO;
    ArrayList<AdvaceDemandDO> advaceDemandsave;
    private RegularDemandsBL regularDemandsBL;
    ArrayList<BaseDO> Rcollection;
    ArrayList<BaseDO> Lcollection;
    ArrayList<BaseDO> ODcollection;
    ArrayList<BaseDO> Advcollection;
    TextView textView11, textView12, textView13,
            textView21, textView22, textView23, textView31, textView32, textView33, textView41, textView42, textView43,fcoName, branchName;
    private LinearLayout llDashboard;
    private TrnsactionsBL trnsactionsBL;
    ODDemandsBL odDemandsBL;
    Transaction_OD_BL transaction_od_BL;

    int rsavecount;
    int odsavecount;
    int advsavecount;
    int lsavecount;

    private Button btnLastreceiptprint;
    private GetDataBL getDataBL, Loginevent, FTODevent;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    Transaction_OD_BL tobl;
    private String status;
    IntialParametrsBL intialParametrsBL;
    private ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    IntialParametrsDO intialParametrsDO=null;
    String print="";
    String TerminalID = "";
    String printerAddress = "";
    String printerValidation = "";
    Menu nav_Menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy);



        tobl=new Transaction_OD_BL();
        intialParametrsBL = new IntialParametrsBL();
        trnsactionsBL = new TrnsactionsBL();
        regularDemandsBL = new RegularDemandsBL();
        odDemandsBL = new ODDemandsBL();
        transaction_od_BL = new Transaction_OD_BL();
        advdemadsbl = new AdvanceDemandBL();

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>AFPL Collections</font>"));


        textView11 = (TextView) findViewById(R.id.textView11);
        textView12 = (TextView) findViewById(R.id.textView12);
        textView13 = (TextView) findViewById(R.id.textView13);
        textView21 = (TextView) findViewById(R.id.textView21);
        textView22 = (TextView) findViewById(R.id.textView22);
        textView23 = (TextView) findViewById(R.id.textView23);
        textView31 = (TextView) findViewById(R.id.textView31);
        textView32 = (TextView) findViewById(R.id.textView32);
        textView33 = (TextView) findViewById(R.id.textView33);

        textView41 = (TextView) findViewById(R.id.textView41);
        textView42 = (TextView) findViewById(R.id.textView42);
        textView43 = (TextView) findViewById(R.id.textView43);

        fcoName = (TextView) findViewById(R.id.fconame);
        branchName = (TextView) findViewById(R.id.branchname);



//        textView41 = (TextView) findViewById(R.id.textView41);
//        textView42 = (TextView) findViewById(R.id.textView42);
//        textView43 = (TextView) findViewById(R.id.textView43);

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);

         nav_Menu = nv.getMenu();


        if (this.getIntent().getSerializableExtra("status") != null) {
            try {
                status = getIntent().getExtras().getString("status");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        alIntialParametrsDOs = intialParametrsBL.SelectAll(NavigationActivity.this);

        if(alIntialParametrsDOs!=null&&alIntialParametrsDOs.size()>0)

        {

            Log.v("","initif");
            if(alIntialParametrsDOs!=null)
            {
                try
                {
                    intialParametrsDO = alIntialParametrsDOs.get(0);
                }catch (IndexOutOfBoundsException in)
                {
                    in.printStackTrace();
                }
                if(intialParametrsDO!=null)
                {
                    String branchname = intialParametrsDO.BranchName;
                Log.v("","branchname"+branchname);
                branchName.setText(branchname);
                    print = intialParametrsDO.PrintValidate;
                }
                if (print.equals("1")) {

                    nav_Menu.findItem(R.id.btnLastreceiptprint).setVisible(true);
                } else {
                    nav_Menu.findItem(R.id.btnLastreceiptprint).setVisible(false);
                }


            }
        }
        else
        {
            Log.v("","initelse");
            getDataBL = new GetDataBL(NavigationActivity.this, NavigationActivity.this);
            getDataBL.getIntialParameters();
        }

//        if (status!=null&&status.equalsIgnoreCase("online")) {
//            getDataBL = new GetDataBL(NavigationActivity.this, NavigationActivity.this);
//            //showProgressManuval(NavigationActivity.this,"Image Syncing Please wait");
//
//
//        } else {
//
//            alIntialParametrsDOs = intialParametrsBL.SelectAll();
//            if(alIntialParametrsDOs!=null)
//            {
//                try
//                {
//                    intialParametrsDO = alIntialParametrsDOs.get(0);
//                }catch (IndexOutOfBoundsException in)
//                {
//                    in.printStackTrace();
//                }
//                if(intialParametrsDO!=null)
//                {
//                    print = intialParametrsDO.PrintValidate;
//                }
//            }
//        }



        Intent inte=new Intent(NavigationActivity.this, RegularScheduleService.class);
        startService(inte);
        Intent odreg=new Intent(NavigationActivity.this, ODRegularScheduleService.class);
        startService(odreg);
        Intent latesch=new Intent(NavigationActivity.this, LateSchedilerService.class);
        startService(latesch);
        Intent advsc=new Intent(NavigationActivity.this, ADVRegularScheduleService.class);
        startService(advsc);


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.btnTransaction:
                        startActivityForResult(new Intent(NavigationActivity.this, Transactions.class), 123);
                        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                        break;
                    case R.id.btnrepay:
                        Intent inten=new Intent(NavigationActivity.this,MemberViewCycle.class);
                        startActivity(inten);
                        break;
                    case R.id.btndataSync:
                        startActivityForResult(new Intent(NavigationActivity.this, NewDataSync.class), 123);
                        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                        break;
                    case R.id.btnReports:
                        startActivityForResult(new Intent(NavigationActivity.this, Reports.class), 123);
                        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                        break;
                    case R.id.btnLastreceiptprint:
                        startActivityForResult(new Intent(NavigationActivity.this, LastReceiptPrintMenu.class), 123);
                        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                        break;
                    case R.id.btntransactionhistory:
                        startActivityForResult(new Intent(NavigationActivity.this, TransactionHistoryMemberwise.class), 123);
                        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                        break;
                    case R.id.btnlogout:

                        alRegularDemandssave = trnsactionsBL.SelectAll(NavigationActivity.this);
                        int totalDemandssave = 0;
                        if (alRegularDemandssave != null) {
                            totalDemandssave = alRegularDemandssave.size();
                        }
                        oddemandssave = transaction_od_BL.SelectAll(NavigationActivity.this);
                        int totalDemandssaveod = 0;
                        if (oddemandssave != null) {
                            totalDemandssaveod = oddemandssave.size();
                        }
                        advaceDemandsave = advdemadsbl.SelectAll(NavigationActivity.this);
                        int totalDemandssaveAdv = 0;
                        if (advaceDemandsave != null) {
                            totalDemandssaveAdv = advaceDemandsave.size();
                        }

                        if(totalDemandssave==0&&totalDemandssaveod==0&&totalDemandssaveAdv==0)
                        {
                            tobl.TruncatetabelServerUploadData(NavigationActivity.this);
                            Intent i = new Intent(NavigationActivity.this, loginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            break;
                        }
                        else
                        {
                            DialogUtils.showAlert(NavigationActivity.this,"Please Upload Pending Records Before Logout");

                        }


                    default:
                        return true;
                }


                return true;

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        String userID = SharedPrefUtils.getKeyValue(NavigationActivity.this, AppConstants.pref_name, AppConstants.username);
        fcoName.setText(userID);
//        alIntialParametrsDOs = intialParametrsBL.SelectAll();
//        if(alIntialParametrsDOs!=null)
//        {
//            try
//            {
//                intialParametrsDO = alIntialParametrsDOs.get(0);
//            }catch (IndexOutOfBoundsException in)
//            {
//                in.printStackTrace();
//            }
//            if(intialParametrsDO!=null)
//            {
//                String branchname = intialParametrsDO.BranchName;
//                Log.v("","branchname"+branchname);
//                branchName.setText(branchname);
//            }
//        }
       dashboarditems();
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataretrieved(Object objs) {
        IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
        intialParametrsBL.Delete(null,NavigationActivity.this);
        intialParametrsBL.Insert((IntialParametrsDO) objs,NavigationActivity.this);
        Log.e("onDataretrieved", "onDataretrieved");
        if(!(NavigationActivity.this.isFinishing())) {
            dismissProgress();
        }
        Loginevent = new GetDataBL(NavigationActivity.this, new DataListner() {
            @Override
            public void onDataretrieved(Object objs) {

            }

            @Override
            public void onDataretrievalFailed(String errorMessage) {

            }
        });

        alIntialParametrsDOs = intialParametrsBL.SelectAll(NavigationActivity.this);

        if(alIntialParametrsDOs!=null)
        {
            try
            {
                intialParametrsDO = alIntialParametrsDOs.get(0);
            }catch (IndexOutOfBoundsException in)
            {
                in.printStackTrace();
            }
            if(intialParametrsDO!=null)
            {
                if(intialParametrsDO!=null)
                {
                    String branchname = intialParametrsDO.BranchName;
                    Log.v("","branchname"+branchname);
                    branchName.setText(branchname);
                    print = intialParametrsDO.PrintValidate;
                }
                print = intialParametrsDO.PrintValidate;

            }
        }
         TerminalID = intialParametrsDO.TerminalID;
         printerAddress = intialParametrsDO.BTPrinterAddress;
         print = intialParametrsDO.PrintValidate;
         printerValidation = intialParametrsDO.ValidatePrinter;
        Log.d("mfimo", "printerValidation" + printerValidation);

        if (print.equals("1")) {

            nav_Menu.findItem(R.id.btnLastreceiptprint).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.btnLastreceiptprint).setVisible(false);
        }

        if (printerValidation.equals("1")) {
            BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
            if (bluetooth!=null&&bluetooth.isEnabled()) {
                String PAdd = printerAddress;
                // Toast.makeText(getApplicationContext(), PAdd.toString(),
                // Toast.LENGTH_LONG).show();
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

                Log.v("", "printerAddress"+printerAddress);
                Log.v("", "PrinterAddress"+PrinterAddress);
                PrintUtils printUtils = new PrintUtils(NavigationActivity.this);
                String con = printUtils.getConnection(PrinterAddress);
                Log.v("con", String.valueOf(con));
                if (con.equals("Not Connected")) {
                    AlertDialog.Builder  builder = new AlertDialog.Builder(NavigationActivity.this);
                    builder.setMessage("PLEASE, SWITCH ON THE PRINTER TO CONTINUE");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).create().show();

                    return;

                }
            } else {
                 AlertDialog.Builder  builder = new AlertDialog.Builder(NavigationActivity.this);
                builder.setMessage("Please Switch On Mobile Bluetooth");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create().show();

                return;
            }
        }
        Loginevent.loginEvent(TerminalID, printerAddress);
        FTODevent = new GetDataBL(NavigationActivity.this, new DataListner() {
            @Override
            public void onDataretrieved(Object objs) {

            }

            @Override
            public void onDataretrievalFailed(String errorMessage) {
                AlertDialog.Builder  builder = new AlertDialog.Builder(NavigationActivity.this);
                builder.setMessage("There are no FTOD Reasons to Download");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create().show();


            }
        });
        FtodreasonsBL ftodreasonsBL = new FtodreasonsBL();
        ftodreasonsBL.Delete(null,NavigationActivity.this);
        // Log.d("mfimo", "ftod reasons start");
        FTODevent.getFTODreasons();
    }

    @Override
    public void onDataretrievalFailed(String errorMessage) {
        Log.e("onDataretrievalFailed", "onDataretrievalFailed");
        if(!(NavigationActivity.this.isFinishing())) {
            dismissProgress();
        }
        AlertDialog.Builder  builder = new AlertDialog.Builder(NavigationActivity.this);
        builder.setMessage("Please Try Again.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).create().show();

    }


    private void dashboarditems() {


        ArrayList<RegularDemandsDO> alregulardemnads = regularDemandsBL.SelectAll(NavigationActivity.this);
        int totalDemands = alregulardemnads.size();
        float totalAmountTobeColleted = 0;
        for (int i = 0; i < totalDemands; i++) {
            RegularDemandsDO demandsDO = alregulardemnads.get(i);
            float dtotal = 0;
            float odamt = 0;
            if (demandsDO != null) {
                if (demandsDO.DemandTotal != null) {
                    try {
                        dtotal = Float.valueOf(demandsDO.DemandTotal.trim()).floatValue();
                    } catch (NumberFormatException n) {
                        n.printStackTrace();
                    }
                }
                if (demandsDO.ODAmount != null) {
                    try {
                        odamt = Float.valueOf(demandsDO.ODAmount.trim()).floatValue();
                    } catch (NumberFormatException n) {
                        n.printStackTrace();
                    }
                }
            }
            totalAmountTobeColleted = totalAmountTobeColleted + dtotal + odamt;
        }
        alRegularDemandssave = trnsactionsBL.SelectAll(NavigationActivity.this);
        int totalDemandssave = 0;
        if (alRegularDemandssave != null) {
            totalDemandssave = alRegularDemandssave.size();
        }
        float totalAmountTobeColletedsave = 0;
        for (int i = 0; i < totalDemandssave; i++) {
            RegularDemandsDO demandsDO = alRegularDemandssave.get(i);
            float coolectedamt = 0;
            if (demandsDO != null) {
                if (demandsDO.collectedAmount != null) {
                    try {
                        coolectedamt = Float.valueOf(demandsDO.collectedAmount.trim()).floatValue();
                    } catch (NumberFormatException n) {
                        n.printStackTrace();
                    }
                }
            }
            totalAmountTobeColletedsave = totalAmountTobeColletedsave + coolectedamt;
        }
        Rtotalmembers=0;
        Rcollection = trnsactionsBL.offlineSavecountCollectionRG("RCollectiion",NavigationActivity.this);

        float totalAmountTobeColletedupload = 0;
        if (Rcollection != null) {
            rsavecount = Rcollection.size();
//            for (int i = 0; i < Rcollection; i++) {
            for (int i = 0; i < Rcollection.size(); i++) {
                BaseDO demandsDO = Rcollection.get(i);
                float uploadamt = 0;

                if (demandsDO != null) {
                    if (demandsDO.TotalUpload != null) {
                        try {
                            uploadamt = Float.valueOf(demandsDO.TotalUpload.trim()).floatValue();
                            Log.v("","rguploadamt"+uploadamt);
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }

                    totalAmountTobeColletedupload = totalAmountTobeColletedupload + uploadamt;
                    Log.v("","totalAmountTobeColletedupload"+totalAmountTobeColletedupload);



                    Log.v("memberstotal", "outloop" + demandsDO.UploadMembers);

                    int members1=0;
                    if (demandsDO.UploadMembers!= null) {
                        try {
                            members1 = Integer.parseInt(demandsDO.UploadMembers);
                            Log.v("memberstotal", "memberstotal" + members1);
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }
                    Rtotalmembers=Rtotalmembers+members1;

                }

                Log.v("Rtotalmembers", "Rtotalmembers" + Rtotalmembers);
            }
        }
        oddemands = odDemandsBL.SelectAll(NavigationActivity.this);
        int totalDemandsod = 0;
        if (oddemands != null) {
            totalDemandsod = oddemands.size();
        }
        // Log.v("onDataretrieved", "totalDemandsod" + totalDemandsod);
        float totalAmountTobeColletedod = 0;

        for (int i = 0; i < totalDemandsod; i++) {
            ODDemandsDO demandsDO = oddemands.get(i);
            if (demandsDO != null) {
                if (demandsDO.ODAmt != null) {
                    totalAmountTobeColletedod = totalAmountTobeColletedod + (Float.valueOf(demandsDO.ODAmt.trim()).floatValue());
                }
            }
        }


        oddemandssave = transaction_od_BL.SelectAll(NavigationActivity.this);
        int totalDemandssaveod = 0;

        if (oddemandssave != null) {
            totalDemandssaveod = oddemandssave.size();
        }

        float totalAmountTobeColletedsaveOD = 0;
        for (int i = 0; i < totalDemandssaveod; i++) {
            ODDemandsDO demandsDO = oddemandssave.get(i);
            float coolectedamt = 0;
            if (demandsDO != null) {
                if (demandsDO.collectedAmt != null) {
                    try {
                        coolectedamt = Float.valueOf(demandsDO.collectedAmt.trim()).floatValue();
                    } catch (NumberFormatException n) {
                        n.printStackTrace();
                    }
                }
            }
            totalAmountTobeColletedsaveOD = totalAmountTobeColletedsaveOD + coolectedamt;
        }


        ODcollection = trnsactionsBL.offlineSavecountCollectionOD("ODCollectiion",NavigationActivity.this);
        float totalAmountTobeColleteduploadOD = 0;

        int members=0;
        int totalmembers=0;
        if (ODcollection != null) {
            odsavecount = ODcollection.size();
            for (int j = 0; j < ODcollection.size(); j++) {
                BaseDO demandsOD = ODcollection.get(j);
                float oduploadamt = 0;

                if (demandsOD != null) {

                    if (demandsOD.TotalUpload != null) {
                        try {
                            oduploadamt = Float.valueOf(demandsOD.TotalUpload.trim()).floatValue();
                            Log.v("totalAmountTobeColleted", "oduploadamt" + oduploadamt);
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }
                    totalAmountTobeColleteduploadOD = totalAmountTobeColleteduploadOD + oduploadamt;
                    if (demandsOD.UploadMembers != null) {
                        try {
                            members = Integer.parseInt(demandsOD.UploadMembers);
                            Log.v("totalAmountTobeColleted", "members" + members);
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }
                    totalmembers=totalmembers+members;

                }

            }
        }


        AdvaceDemandDO = advdemadsbl.SelectAllAdvanceDemands(NavigationActivity.this);
        int advtotalDemand = 0;

        if (AdvaceDemandDO != null) {
            advtotalDemand = AdvaceDemandDO.size();
        }
        //   Log.v("onDataretrieved", "advtotalDemand" + advtotalDemand);
        float totalAmountTobeColletedadv = 0;

        for (int k = 0; k < advtotalDemand; k++) {
            AdvaceDemandDO advaceDemandDO = AdvaceDemandDO.get(k);
            if (advaceDemandDO != null) {
                if (advaceDemandDO.CollectedAmt != null) {
                    totalAmountTobeColletedadv = totalAmountTobeColletedadv + (Float.valueOf(advaceDemandDO.CollectedAmt.trim()).floatValue());
                }
            }
        }


        advaceDemandsave = advdemadsbl.SelectAll(NavigationActivity.this);
        int totalDemandssaveAdv = 0;

        if (advaceDemandsave != null) {
            totalDemandssaveAdv = advaceDemandsave.size();
        }

        float totalAmountTobeColletedsaveAdv = 0;
        for (int m = 0; m < totalDemandssaveAdv; m++) {
            AdvaceDemandDO advaceDemandDO = advaceDemandsave.get(m);
            float coolectedamtadv = 0;
            if (advaceDemandDO != null) {
                if (advaceDemandDO.previousAmt != null) {
                    try {
                        coolectedamtadv = Float.valueOf(advaceDemandDO.previousAmt.trim()).floatValue();
                    } catch (NumberFormatException n) {
                        n.printStackTrace();
                    }
                }
            }
            totalAmountTobeColletedsaveAdv = totalAmountTobeColletedsaveAdv + coolectedamtadv;
        }


        Advcollection = trnsactionsBL.offlineSavecountCollectionADV("ADVCollectiion",NavigationActivity.this);
        float totalAmountTobeColleteduploadadv = 0;
        int adtotalmembers=0;
        int admembers=0;
        if (Advcollection != null) {
            advsavecount = Advcollection.size();

            for (int r = 0; r <  Advcollection.size(); r++) {
                BaseDO demandsOD = Advcollection.get(r);
                float advuploadamt = 0;

                if (demandsOD != null) {
                    if (demandsOD.TotalUpload != null) {
                        try {
                            advuploadamt = Float.valueOf(demandsOD.TotalUpload.trim()).floatValue();
                            Log.v("totalAmountTobeColleted", "advuploadamt" + advuploadamt);
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }
                }
                totalAmountTobeColleteduploadadv = totalAmountTobeColleteduploadadv + advuploadamt;
                if (demandsOD.UploadMembers != null) {
                    try {
                        admembers = Integer.parseInt(demandsOD.UploadMembers);
                        Log.v("totalAmountTobeColleted", "members" + members);
                    } catch (NumberFormatException n) {
                        n.printStackTrace();
                    }
                }
                adtotalmembers=adtotalmembers+admembers;
            }
        }





        //////


        ArrayList<RegularDemandsDO> alregulardemnadslate = regularDemandsBL.SelectAllTransactionLatetotal(NavigationActivity.this);
        float totalAmountTobeColletedlate = 0;

        int totalDemandslate=0;

        if(alregulardemnadslate!=null)
        {
             totalDemandslate = alregulardemnadslate.size();
            for (int i = 0; i < totalDemandslate; i++) {
                RegularDemandsDO demandsDO = alregulardemnadslate.get(i);
                float dtotal = 0;
                float odamt = 0;
                float colamt = 0;
                if (demandsDO != null) {
                    if (demandsDO.DemandTotal != null) {
                        try {
                            dtotal = Float.valueOf(demandsDO.DemandTotal.trim()).floatValue();
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }
                    if (demandsDO.ODAmount != null) {
                        try {
                            odamt = Float.valueOf(demandsDO.ODAmount.trim()).floatValue();
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }
                    if (demandsDO.collectedAmount != null) {
                        try {
                            colamt = Float.valueOf(demandsDO.collectedAmount.trim()).floatValue();
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }
                }
                totalAmountTobeColletedlate = totalAmountTobeColletedlate + dtotal + odamt- colamt;
                Log.v("","totalAmountTobeColletedlate"+totalAmountTobeColletedlate);
            }
        }


        ArrayList<RegularDemandsDO> alregulardemnadslatesave = regularDemandsBL.SelectAllTransactionLatetotalSave(NavigationActivity.this);

        float totalAmountTobeColletedsaveLate = 0;
        int totalDemandslatesave=0;

        if(alregulardemnadslatesave!=null)
        {
            totalDemandslatesave = alregulardemnadslatesave.size();
            for (int i = 0; i < totalDemandslatesave; i++) {
                RegularDemandsDO demandsDO = alregulardemnadslatesave.get(i);
                float dtotal = 0;
                float odamt = 0;
                float colamt = 0;
                if (demandsDO != null) {

                    if (demandsDO.collectedAmount != null) {
                        try {
                            colamt = Float.valueOf(demandsDO.LateCollection.trim()).floatValue();
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }
                }

                totalAmountTobeColletedsaveLate = totalAmountTobeColletedsaveLate + colamt;
                Log.v("","totalAmountTobeColletedsaveLate"+totalAmountTobeColletedsaveLate);
            }
        }





        Lcollection = trnsactionsBL.offlineSavecountCollectionLate("LCollectiion",NavigationActivity.this);
        float totalAmountTobeColleteduploadLate = 0;
        if (Lcollection != null) {
            lsavecount = Lcollection.size();
            for (int i = 0; i < lsavecount; i++) {
                BaseDO demandsDO = Lcollection.get(i);
                float uploadamt = 0;

                if (demandsDO != null) {
                    if (demandsDO.TotalUpload != null) {
                        try {
                            uploadamt = Float.valueOf(demandsDO.TotalUpload.trim()).floatValue();
                            Log.v("","lateupload"+uploadamt);
                        } catch (NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }
                }
                totalAmountTobeColleteduploadLate = totalAmountTobeColleteduploadLate + uploadamt;
                Log.v("","totalAmountTobeColleteduploadLate"+totalAmountTobeColletedupload);
            }

        }





        textView11.setText(totalDemands + "/" + totalAmountTobeColleted);
        textView12.setText(totalDemandssave + "/" + totalAmountTobeColletedsave);
        textView13.setText(Rtotalmembers + "/" + totalAmountTobeColletedupload);


        textView21.setText(totalDemandsod + "/" + totalAmountTobeColletedod);
        textView22.setText(totalDemandssaveod + "/" + totalAmountTobeColletedsaveOD);
        textView23.setText(totalmembers + "/" + totalAmountTobeColleteduploadOD);


        textView31.setText(advtotalDemand + "/" + totalAmountTobeColletedadv);
        textView32.setText(totalDemandssaveAdv + "/" + totalAmountTobeColletedsaveAdv);
        textView33.setText(adtotalmembers + "/" + totalAmountTobeColleteduploadadv);



        textView41.setText(totalDemandslate + "/" + totalAmountTobeColletedlate);
        textView42.setText(totalDemandslatesave + "/" + totalAmountTobeColletedsaveLate);
        textView43.setText(lsavecount + "/" + totalAmountTobeColleteduploadLate);




    }
}