package com.jayam.impactapp.webacceslayer;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.jayam.impactapp.businesslayer.GetDataBL;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.ServiceURLs;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.NetworkUtility;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.StringUtils;
import com.jayam.impactapp.utils.UtilClass;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by administrator_pc on 19-02-2020.
 */

public class ADVRegularScheduleService extends Service {

    public String TOTAL_AMT_UPLOADED_ADV,TOTAL_ACCOUNTS_ADV;

    RegularDemandsBLTemp demandsBLTemp;
    private GetDataBL getDataBL;
    private RegularDemandsBL regularDemandsBL;
    private ArrayList<RegularDemandsDO> alRegularDemands;
    private Transaction_OD_BL transaction_OD_BL;
    private ODDemandsBL odDemands;
    private TrnsactionsBL trnsactionsBL;
    private ArrayList<ODDemandsDO> alOdDemandsDOs;
    private ArrayList<AdvaceDemandDO> alAdvanceDemands;

    private IntialParametrsBL intialParametrsBL;
    private AdvanceDemandBL advdemadsbl;

    AdvanceDemandBL advanceBL;
    public String printerAddress = null;
    public String ReceiptNo = null;
    public String TxnCode = null;
    public String macid = null;
    ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    IntialParametrsDO intialParametrsDO;

    String userID="";
    int advcount=0;
    String MLAID="";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        advanceBL=new AdvanceDemandBL();
        advdemadsbl = new AdvanceDemandBL();
        intialParametrsBL = new IntialParametrsBL();
        trnsactionsBL=new TrnsactionsBL();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                userID = SharedPrefUtils.getKeyValue(ADVRegularScheduleService.this, AppConstants.pref_name, AppConstants.username);
                alAdvanceDemands = advanceBL.SelectAllFlagwise(ADVRegularScheduleService.this);
                alIntialParametrsDOs = intialParametrsBL.SelectAll(ADVRegularScheduleService.this);
                macid = SharedPrefUtils.getKeyValue(ADVRegularScheduleService.this, AppConstants.pref_name, AppConstants.macid);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(alIntialParametrsDOs!=null)
                {
                    try
                    {
                        intialParametrsDO=alIntialParametrsDOs.get(0);
                    }catch (IndexOutOfBoundsException in)
                    {
                        in.printStackTrace();
                    }
                }
                if(intialParametrsDO!=null)
                {
                    printerAddress = intialParametrsDO.BTPrinterAddress;
                    ReceiptNo = intialParametrsDO.LastTransactionID;
                    TxnCode = intialParametrsDO.LastTransactionCode;
                }
                if(alAdvanceDemands!=null&&alAdvanceDemands.size()>0)
                {
                    advcount=alAdvanceDemands.size();
                    try
                    {
                        MLAID=alAdvanceDemands.get(0).MLAI_ID;

                        TOTAL_AMT_UPLOADED_ADV=advdemadsbl.getTOTALDemandAmountForAdvGroupid(MLAID,ADVRegularScheduleService.this);
                        TOTAL_ACCOUNTS_ADV=advdemadsbl.getTOTALAdvAccounts(ADVRegularScheduleService.this);
                    }catch (IndexOutOfBoundsException in)
                    {
                        in.printStackTrace();
                    }
                }
                Log.v("","MLAID"+MLAID);
                if (advcount > 0) {
                    String Rcollection="";
                    Rcollection =StringUtils.getSoapStringForAdvance(ADVRegularScheduleService.this, alAdvanceDemands);
                    if(Rcollection!=null&&!Rcollection.isEmpty())
                    {
                        if (NetworkUtility.isNetworkConnectionAvailable(ADVRegularScheduleService.this))
                        {
                            trnsactionsBL.UpdateAdvCollection(MLAID,"P", UtilClass.UploadCurrentDate(),ADVRegularScheduleService.this);
                            new ADVRegularScheduleService.UploadAsync().execute( Rcollection);
                        }

                    }

                }



            }
        }, 3000, 12000);// 5 Minutes


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int getCount(ArrayList<RegularDemandsDO> alRegularDemands) {
        int count = 0;
        for (int i = 0; i < alRegularDemands.size(); i++) {
            if (alRegularDemands.get(i).Confirmed != null
                    && alRegularDemands.get(i).Confirmed.equalsIgnoreCase("CONFIRMED")) {
                count = count + 1;
            }
        }
        return count;
    }

    class UploadAsync extends AsyncTask<String, Void, ArrayList<String>> {
        ArrayList<String> counts = null;
        String countvar="";
        String data="";
        protected ArrayList<String> doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            data=arg0[0];
            String url= ServiceURLs.mailURl+"AdvCollectionUploads.aspx";
            Log.v("safal","Final URL"+ url);
            ArrayList<NameValuePair> namevaluepair=new ArrayList<NameValuePair>();
            namevaluepair.add(new BasicNameValuePair("AdvCollstring", data));
            namevaluepair.add(new BasicNameValuePair("uid", userID));
            namevaluepair.add(new BasicNameValuePair("MACID", macid));
            namevaluepair.add(new BasicNameValuePair("BTAddress", printerAddress));
            namevaluepair.add(new BasicNameValuePair("MaxReceiptNo", ReceiptNo));
            namevaluepair.add(new BasicNameValuePair("MaxTxnCode", TxnCode));
            namevaluepair.add(new BasicNameValuePair("MLAID", MLAID));

            String s = url;
            s = s.replaceAll(" ", "%20");
            Log.v("safal","Final data"+ s);
            Log.v("safal","Final data"+ data);

            try {
                int timeoutConnection = 5000;
                int timeoutSocket = 15000;
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpPost httppost = new HttpPost(s);
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepair));
                HttpResponse hres = httpClient.execute(httppost);
                countvar = EntityUtils.toString(hres.getEntity());
                counts = new ArrayList<String>();
                Log.v("", "upload json counts"+countvar);
                JSONObject js;
                try {

                    Log.v("", "upload json string"+s);
                    js = new JSONObject(countvar);
                    counts.add(js.getString("MLAID"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }
            return counts;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                if(!result.isEmpty())
                {
                    String Message="";
                    try
                    {
                        Message	= result.get(0);
                    }catch (IndexOutOfBoundsException in)
                    {
                        in.printStackTrace();
                    }

                        if(Message!=null&&!Message.isEmpty())
                        {
                            trnsactionsBL.InsertServerCollectionUploadData("ADVCollectiion",TOTAL_ACCOUNTS_ADV,TOTAL_AMT_UPLOADED_ADV,data,ADVRegularScheduleService.this);
                            advanceBL.TruncatetabelGroupId(Message,ADVRegularScheduleService.this);
                            advanceBL.TruncatetabeGroupId(Message,ADVRegularScheduleService.this);
                        }


                }
            }

        }


    }


//    public String prepareMemberString(ArrayList<RegularDemandsDO> alArrayListm) {
//        Log.v("","alArrayListmprepa"+alArrayListm.size());
//        JsonArray memberarray= new JsonArray();
//        JsonObject finalOutput = new JsonObject();
//        if (alArrayListm.size() > 0) {
//
//            for (RegularDemandsDO newclient : alArrayListm) {
//                JsonObject memberobject= new JsonObject();
//                memberobject.addProperty("ProductType",newclient.ProductType);
//                memberobject.addProperty("Village",newclient.previllageName);
//                memberobject.addProperty("VillageID",newclient.previllageid);
//                memberobject.addProperty("CenterID",newclient.precenterid);
//                memberobject.addProperty("Center",newclient.precenterNmae);
//                memberobject.addProperty("GroupType",newclient.GropupType);
//                memberobject.addProperty("Name",newclient.name);
//                memberobject.addProperty("DateofBirth",newclient.dob);
//                memberobject.addProperty("Age",newclient.age);
//                memberobject.addProperty("Gender",newclient.gender);
//                memberarray.add(memberobject);
//            }
//            finalOutput.add("SamarthLProposalMember_Upload",memberarray);
//        }
//        return finalOutput.toString();
//    }

    public String GetDate()
    {
        Date endsd = new Date();
        String dateString = "";
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateString = sdfr.format(endsd);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  dateString;
    }
    public String GetCurrentdateNano()
    {
        Date endsd = new Date();
        String dateString = "";
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        try {
            dateString = sdfr.format(endsd);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  dateString;
    }

    public String GetCurrentdateNanoSeconds()
    {
        Date endsd = new Date();
        String dateString = "";
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyyMMddHHmmssSSSSSSSSS");
        try {
            dateString = sdfr.format(endsd);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  dateString;
    }
    public String GetCurrentdate()
    {
        Date endsd = new Date();
        String dateString = "";
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            dateString = sdfr.format(endsd);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  dateString;
    }
}