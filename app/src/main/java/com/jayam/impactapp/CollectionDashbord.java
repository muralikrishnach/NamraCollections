package com.jayam.impactapp;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;

import java.util.ArrayList;

/**
 * Created by administrator_pc on 19-01-2020.
 */

public class CollectionDashbord extends Base {
    int Rtotalmembers=0;
    private AdvanceDemandBL advdemadsbl;
    private ArrayList<RegularDemandsDO> alRegularDemandssave;
    ArrayList<ODDemandsDO> oddemands;
    ArrayList<ODDemandsDO> oddemandssave;

    ArrayList<AdvaceDemandDO> AdvaceDemandDO;
    ArrayList<AdvaceDemandDO> advaceDemandsave;
    private RegularDemandsBL regularDemandsBL;
    ArrayList<BaseDO> Rcollection;
    ArrayList<BaseDO> ODcollection;
    ArrayList<BaseDO> Advcollection;




    String rgrecordssavemembers = "";
    String rgrecordssaveamount = "";
    String odrecordsavemembers = "";
    String odrecordsaveamount = "";
    String advrecordsavemembers = "";
    String advrecordsaveamount = "";

//    ArrayList<MasterDataDo> Rcollection;
//    ArrayList<MasterDataDo> ODcollection;
//    ArrayList<MasterDataDo> Advcollection;

    TextView  textView11, textView12, textView13,
            textView21, textView22, textView23, textView31, textView32, textView33,textView41, textView42, textView43;
    private LinearLayout llDashboard;
    private TrnsactionsBL trnsactionsBL;
    ODDemandsBL odDemandsBL;
    Transaction_OD_BL transaction_od_BL;

    int rsavecount;
    int odsavecount;
    int advsavecount;
    int totaluplaorcount = 0;

    @Override
    public void initialize() {
        intializeControlles();
    }

    private void intializeControlles() {

        trnsactionsBL = new TrnsactionsBL();
        regularDemandsBL = new RegularDemandsBL();
        odDemandsBL = new ODDemandsBL();
        transaction_od_BL = new Transaction_OD_BL();
        advdemadsbl = new AdvanceDemandBL();
        llDashboard = (LinearLayout) inflater.inflate(R.layout.collectiondashboard, null);
        llBaseMiddle_lv.setVisibility(View.VISIBLE);
        llBaseMiddle_lv.addView(llDashboard, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        tvHeader.setText("Collection Dashboard");



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

        ArrayList<RegularDemandsDO> alregulardemnads = regularDemandsBL.SelectAll(CollectionDashbord.this);
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
       // Log.v("onDataretrieved", "Success" + totalAmountTobeColleted);

        alRegularDemandssave = trnsactionsBL.SelectAll(CollectionDashbord.this);
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
        //Log.v("onDataretrieved", "Success" + totalAmountTobeColleted);
        Rtotalmembers=0;
        Rcollection = trnsactionsBL.offlineSavecountCollectionRG("RCollectiion",CollectionDashbord.this);

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


        oddemands = odDemandsBL.SelectAll(CollectionDashbord.this);
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


        oddemandssave = transaction_od_BL.SelectAll(CollectionDashbord.this);
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


            ODcollection = trnsactionsBL.offlineSavecountCollectionOD("ODCollectiion",CollectionDashbord.this);
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


            AdvaceDemandDO = advdemadsbl.SelectAllAdvanceDemands(CollectionDashbord.this);
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


            advaceDemandsave = advdemadsbl.SelectAll(CollectionDashbord.this);
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


                Advcollection = trnsactionsBL.offlineSavecountCollectionADV("ADVCollectiion",CollectionDashbord.this);
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





                textView11.setText(totalDemands + "/" + totalAmountTobeColleted);
                textView12.setText(totalDemandssave + "/" + totalAmountTobeColletedsave);
                 textView13.setText(Rtotalmembers + "/" + totalAmountTobeColletedupload);


                textView21.setText(totalDemandsod + "/" + totalAmountTobeColletedod);
                textView22.setText(totalDemandssaveod + "/" + totalAmountTobeColletedsaveOD);
                textView23.setText(totalmembers + "/" + totalAmountTobeColleteduploadOD);


                textView31.setText(advtotalDemand + "/" + totalAmountTobeColletedadv);
                textView32.setText(totalDemandssaveAdv + "/" + totalAmountTobeColletedsaveAdv);
                textView33.setText(adtotalmembers + "/" + totalAmountTobeColleteduploadadv);




            }
        }





