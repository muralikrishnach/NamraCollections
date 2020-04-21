package com.jayam.impactapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.Branch;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.cbsearchgrid;
import com.jayam.impactapp.utils.NetworkUtility;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.webacceslayer.NetworkConnectivity;
import com.jayam.impactapp.webacceslayer.SimpleHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class AdvanceCashLessPending extends Base {
    ODDemandsDO cbmdao;

    Date finaledate;
    LinearLayout llclientinfo;
    String device_id, userID = "";
    RegularDemandsBL mdb;
    private NetworkConnectivity ncty;
    private AutoCompleteTextView center, group,member;


    private TextView dropselect;
    String[] drop_select = new String[]{"Center No", "Group Name", "Borrower", "Voter ID", "Aadhar ID"};
    ArrayList<Branch> branch;
    String BranchId = "";
    String branchName = "";
    ArrayList<ODDemandsDO> centerlist;
    ArrayList<String> centercheck = new ArrayList<String>();
    HashMap<String, AdvaceDemandDO> hmcentermap;
    private ArrayAdapter<String> centeradapter;
    AdvaceDemandDO centerdao;
    String centerid, groupid,memberid;
    String centerbyid = "";
    String groupbyid = "";
    Date finalsdate;
    ArrayList<AdvaceDemandDO> centerlistdata,grouplist,memberlistdata,membergrid;




    HashMap<String, AdvaceDemandDO> hmgroupmap;
    HashMap<String, AdvaceDemandDO> hmmembermap;

    ArrayList<String> groupcheck = new ArrayList<String>();
    ArrayList<String> membercheck = new ArrayList<String>();

    private ArrayAdapter<String> groupadapter;

    private ArrayAdapter<String> memberadapter;

    AdvaceDemandDO groupdao;
    AdvaceDemandDO memberdao;
    LinearLayout firstgrid, secndgrid;
    RelativeLayout loandetailsgrid;
    String reportlink = "";
    Date starts;
    Date ends;
    long secondcheck;
    long mintues;
    String Formnumber = "";
    private ConcurrentHashMap memberlist;
    private Object memberloanlist;

    @Override
    public void initialize() {
        userID = SharedPrefUtils.getKeyValue(AdvanceCashLessPending.this, AppConstants.pref_name, AppConstants.username);
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return;
            }
            device_id = tm.getDeviceId();

            Log.v("", "device_idmember" + device_id);
        }catch (Exception e ){
            e.printStackTrace();
        }
        Log.v("","userID"+userID);
        mdb = new RegularDemandsBL();


        initializecontrols();

    }



    private void oncreatingViews() {

        firstgrid.removeAllViews();
        Log.v("", "membergridSize" + membergrid.size());
        for (int i = 0; i < membergrid.size(); i++) {
            final AdvaceDemandDO adkyc = (AdvaceDemandDO) membergrid.get(i);
            View vv = getLayoutInflater().inflate(R.layout.firstcbsearchgrid, null);
            LinearLayout layout = (LinearLayout) vv.findViewById(R.id.llfirstgrid);
            final TextView Clientname = (TextView) vv.findViewById(R.id.clintnme);
            final TextView Aadhar = (TextView) vv.findViewById(R.id.aadhrid);
            final TextView Village = (TextView) vv.findViewById(R.id.village);
            final TextView Centerno = (TextView) vv.findViewById(R.id.centerno);
            final TextView Group = (TextView) vv.findViewById(R.id.group);
            final Button CBsummary = (Button) vv.findViewById(R.id.cbsummary);
            String clientid=adkyc.MMI_Code;
            String TXNID=adkyc.TransactionID;
            String clientname=adkyc.MMI_Name;
            Clientname.setText(clientname);


            String Status=adkyc.Status;

            CBsummary.setText(Status);
            CBsummary.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(final View arg0) {
                    // TODO Auto-generated method stub

                    hideKeyboard();


                    Log.v("","Formnumber"+Formnumber);
                    if (NetworkUtility.isNetworkConnectionAvailable(AdvanceCashLessPending.this))
                    {

                    }
                    else
                    {
                        HideLoader();
                        showAlertDailog("Connection Lost! Please Check your Internet Connection.");
                    }


                }
            });





            firstgrid.addView(vv);

        }


    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initializecontrols() {
        DisplayMetrics metrics = new DisplayMetrics();
        AdvanceCashLessPending.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);

        if (diagonalInches>=6.5){
            // 6.5inch device or bigger
            llclientinfo = (LinearLayout) inflater.inflate(R.layout.activity_regular_cb_check_tab, null);

        }else {
            // smaller device
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            llclientinfo = (LinearLayout) inflater.inflate(R.layout.activity_regular_cb_check, null);

        }


        secndgrid= (LinearLayout) llclientinfo.findViewById(R.id.llaprisal);
        firstgrid = (LinearLayout) llclientinfo.findViewById(R.id.llfirstgridscroll);
        center = (AutoCompleteTextView) llclientinfo.findViewById(R.id.autoselect);
        group = (AutoCompleteTextView) llclientinfo.findViewById(R.id.autoselecttwo);
        member = (AutoCompleteTextView) llclientinfo.findViewById(R.id.automember);
        loandetailsgrid = (RelativeLayout) llclientinfo.findViewById(R.id.llloandetails);



        svBase.setVisibility(View.GONE);
        llBaseMiddle_lv.setVisibility(View.VISIBLE);
        llBaseMiddle_lv.addView(llclientinfo, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        tvHeader.setText("CB Status Check");
        showHomeIcons();
        ivHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setResult(AppConstants.RESULTCODE_HOME);
                finish();
            }
        });



        centerlistdata=mdb.SelectAllAdvanceCashlesspending(AdvanceCashLessPending.this);

        if (centerlistdata != null && !centerlistdata.isEmpty()) {
            String[] vistleadvalues = new String[centerlistdata.size()];
            hmcentermap = new HashMap<String, AdvaceDemandDO>();
            for (int i = 0; i < centerlistdata.size(); i++) {
                AdvaceDemandDO vlead = centerlistdata.get(i);
                String value=vlead.CenterName;
                Log.v("","value"+value);
                vistleadvalues[i] = vlead.CenterName;
                centercheck.add(vlead.CenterName);
                hmcentermap.put(vlead.MCI_Code, vlead);
                hmcentermap.put(vlead.CenterName, vlead);
            }


            if (vistleadvalues.length > 0) {
                Log.v("","vistleadvalueslength"+vistleadvalues.length);
                centeradapter = new ArrayAdapter<String>(AdvanceCashLessPending.this, R.layout.mylistdata, vistleadvalues);
                center.setAdapter(centeradapter);

            }


        }


        center.setThreshold(-1);
        center.setTag(0);
        center.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (centeradapter != null && !centeradapter.isEmpty())
                {
                    centerdao = hmcentermap.get(centeradapter.getItem(position));
                    if(centerdao!=null)
                    {
                        String gropname = centerdao.CenterName;
                        Log.v("","centername"+gropname);
                        centerid = centerdao.MCI_Code;
                        Log.v("","centerid"+centerid);
                        center.setText(gropname);


                        grouplist=mdb.SelectAllGroupCashlesspendingadvance(centerid,AdvanceCashLessPending.this);
                        Log.v("","grouplistsize"+grouplist.size());

                        if (grouplist != null && !grouplist.isEmpty()) {
                            String[] vistleadvalues = new String[grouplist.size()];
                            hmgroupmap = new HashMap<String, AdvaceDemandDO>();
                            for (int i = 0; i < grouplist.size(); i++) {
                                AdvaceDemandDO vlead = grouplist.get(i);
                                String value=vlead.MGI_Name;
                                Log.v("","value"+value);
                                vistleadvalues[i] = vlead.MGI_Name;
                                groupcheck.add(vlead.MGI_Name);
                                hmgroupmap.put(vlead.MGI_Code, vlead);
                                hmgroupmap.put(vlead.MGI_Name, vlead);
                            }


                            if (vistleadvalues.length > 0) {
                                Log.v("","vistleadvalueslength"+vistleadvalues.length);
                                groupadapter = new ArrayAdapter<String>(AdvanceCashLessPending.this, R.layout.mylistdata, vistleadvalues);
                                group.setAdapter(groupadapter);

                            }
                        }
                    }


                }


            }
        });
        center.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (centercheck.size() == 0 || centercheck.indexOf(center.getText().toString()) == -1) {
                        center.setText("");
                        center.setError("Invalid Name.");
                    }

                    ;
                }
            }
        });







        group.setThreshold(-1);
        group.setTag(0);
        group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (groupadapter != null && !groupadapter.isEmpty())
                {
                    groupdao = hmgroupmap.get(groupadapter.getItem(position));
                    if(groupdao!=null)
                    {
                        String gropname = groupdao.MGI_Name;
                        Log.v("","centername"+gropname);
                        groupid = groupdao.MGI_Code;
                        Log.v("","groupid"+groupid);
                        group.setText(gropname);


                        memberlistdata=mdb.SelectAllMemberCashlesspendingadvanceMEMBER(groupid,AdvanceCashLessPending.this);
                        Log.v("","memberlistdata"+memberlistdata.size());

                        if (memberlistdata != null && !memberlistdata.isEmpty()) {
                            String[] vistleadvalues = new String[memberlistdata.size()];
                            hmmembermap = new HashMap<String, AdvaceDemandDO>();
                            for (int i = 0; i < memberlistdata.size(); i++) {
                                AdvaceDemandDO vlead = memberlistdata.get(i);
                                String value=vlead.MMI_Name;
                                Log.v("","value"+value);
                                vistleadvalues[i] = vlead.MMI_Name;
                                membercheck.add(vlead.MMI_Name);
                                hmmembermap.put(vlead.MMI_Code, vlead);
                                hmmembermap.put(vlead.MMI_Name, vlead);
                            }


                            if (vistleadvalues.length > 0) {
                                Log.v("","vistleadvalueslength"+vistleadvalues.length);
                                memberadapter = new ArrayAdapter<String>(AdvanceCashLessPending.this, R.layout.mylistdata, vistleadvalues);
                                member.setAdapter(memberadapter);

                            }
                        }
                    }


                }


            }
        });
        center.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (centercheck.size() == 0 || centercheck.indexOf(center.getText().toString()) == -1) {
                        center.setText("");
                        center.setError("Invalid Name.");
                    }

                    ;
                }
            }
        });



        group.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (groupcheck.size() == 0 || groupcheck.indexOf(group.getText().toString()) == -1) {
                        group.setText("");
                        group.setError("Invalid Name.");
                    }

                    ;
                }
            }
        });






        member.setThreshold(-1);
        member.setTag(0);
        member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (memberadapter != null && !memberadapter.isEmpty())
                {
                    memberdao = hmmembermap.get(memberadapter.getItem(position));
                    if(memberdao!=null)
                    {
                        String gropname = memberdao.MMI_Name;
                        Log.v("","centername"+gropname);
                        memberid = memberdao.MMI_Code;
                        Log.v("","memberid"+memberid);
                        member.setText(gropname);


                        membergrid=mdb.SelectAllMeberdetailsAdvance(memberid,AdvanceCashLessPending.this);
                        Log.v("","memberlistdata"+memberlistdata.size());

                        if (membergrid != null && !membergrid.isEmpty()) {

                            oncreatingViews();
                        }
                    }


                }


            }
        });
        member.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (membercheck.size() == 0 || membercheck.indexOf(member.getText().toString()) == -1) {
                        member.setText("");
                        member.setError("Invalid Name.");
                    }

                    ;
                }
            }
        });





















    }


    class UploadAsync extends AsyncTask<String, Void, ArrayList<cbsearchgrid>> {
        String counts = null;

        @Override
        protected ArrayList<cbsearchgrid> doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            ArrayList<cbsearchgrid> mastreData = null;
//                            NetworkConnection nc = new NetworkConnection();
            String s = null;


            try {


                Log.v("", "arg0[1]" + arg0[1]);
                Log.v("", "arg0[2]" + arg0[2]);
                Log.v("", "arg0[3]" + arg0[3]);


                Log.v("", "URL [0]" + arg0[0]);


                ArrayList<NameValuePair> postVars = new ArrayList<NameValuePair>();

                postVars.add(new BasicNameValuePair("UserName", arg0[1]));
                postVars.add(new BasicNameValuePair("Screen", arg0[2]));
                postVars.add(new BasicNameValuePair("Searchcode", arg0[3]));


                counts = SimpleHttpClient.executeHttpPost(arg0[0], postVars); //
                Log.v("", "counts" + counts);

                if (counts != null) {
//                                    mastreData = nc.cbstatusfirstgrid(counts);
                    Log.v("", "ExisitgmembmastreData" + mastreData);
                }


                Log.v("", "counts" + counts);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//			    if (s != null) {
//				counts = nc.dataUpload(s);
//			    }
            return mastreData;
        }

        @Override
        protected void onPostExecute(ArrayList<cbsearchgrid> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {


                if (!AdvanceCashLessPending.this.isFinishing()) {
                    HideLoader();

                    //showAlertDailogpurpose( "member  is Exist in branch" );


                }

                RegularDemandsBL mbl = new RegularDemandsBL();
                mbl.deletecbsearchfirstchange();
                mbl.GroupcbsearchfirstSave(result);


                memberlist = mdb.getfirstgrid();

                oncreatingViews();


                if (!AdvanceCashLessPending.this.isFinishing()) {
                    HideLoader();


                }


            } else {
                HideLoader();
                showAlertDailog("Connection to Server Lost.");
            }
        }
    }

}











