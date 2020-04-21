package com.jayam.impactapp.webacceslayer;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.jayam.impactapp.common.ApiClient;
import com.jayam.impactapp.common.ApiInterfaceImage;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.ServiceURLs;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LateSchedilerService extends Service {
    private Stack<String> image_names = new Stack<String>();;
    public static String TOTAL_ACCOUNTS_REG;
    RegularDemandsBLTemp demandsBLTemp;
    private RegularDemandsBL regularDemandsBL;
    private ArrayList<RegularDemandsDO> alRegularDemands;
    private TrnsactionsBL trnsactionsBL;
    private IntialParametrsBL intialParametrsBL;
    AdvanceDemandBL advanceBL;
    public String printerAddress = null;
    public String ReceiptNo = null;
    public String TxnCode = null;
    public String macid = null;
    ArrayList<IntialParametrsDO> alIntialParametrsDOs;
    IntialParametrsDO intialParametrsDO;
    String GroupId="";
    String userID="";
    int regcount=0;
    private File root;
    String TOTAL_AMT_UPLOADED_REG;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    String uid="";
    String  ImageName="";
    @Override
    public void onCreate() {
        regularDemandsBL = new RegularDemandsBL();
        trnsactionsBL = new TrnsactionsBL();
        intialParametrsBL = new IntialParametrsBL();
        demandsBLTemp = new RegularDemandsBLTemp();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                userID = SharedPrefUtils.getKeyValue(LateSchedilerService.this, AppConstants.pref_name, AppConstants.username);
                alRegularDemands = trnsactionsBL.SelectAllFlagwiseLate(LateSchedilerService.this);

                alIntialParametrsDOs = intialParametrsBL.SelectAll(LateSchedilerService.this);
                macid = SharedPrefUtils.getKeyValue(LateSchedilerService.this, AppConstants.pref_name, AppConstants.macid);
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
                    uid = intialParametrsDO.TerminalID;
                }

                if(alRegularDemands!=null&&alRegularDemands.size()>0)
                {
                    regcount=alRegularDemands.size();
                    GroupId="";
                    if(alRegularDemands!=null)
                    {
                        try
                        {
                            GroupId=alRegularDemands.get(0).GNo;
                            TOTAL_ACCOUNTS_REG = regularDemandsBL.getTOTALRegularAccountsGroupIdLate(GroupId,LateSchedilerService.this);
                            TOTAL_AMT_UPLOADED_REG = regularDemandsBL.getTOTALDemandAmountForRegularGroupIdLate(GroupId,LateSchedilerService.this);

                            Log.v("","GroupIdva"+GroupId);
                        }catch (IndexOutOfBoundsException in)
                        {
                            in.printStackTrace();
                        }
                    }
                }


                Log.v("","latecount"+regcount);
                if (regcount > 0) {
                    String Rcollection="";


                    Rcollection = StringUtils.getSoapStringLate(LateSchedilerService.this, alRegularDemands);

                    Log.v("","latecollection"+Rcollection);


                    if(Rcollection!=null&&!Rcollection.isEmpty())
                    {
                        if (NetworkUtility.isNetworkConnectionAvailable(LateSchedilerService.this)) {
                            trnsactionsBL.UpdateRegCollectionLate(GroupId,"P", UtilClass.UploadCurrentDate(),LateSchedilerService.this);
                            new LateSchedilerService.UploadAsync().execute( Rcollection);
                        }

                    }

                }



            }
        }, 3000, 6000);// 5 Minutes


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    class UploadAsync extends AsyncTask<String, Void, ArrayList<String>> {
        ArrayList<String> counts = null;
        String countvar="";
        String data="";
        protected ArrayList<String> doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            data=arg0[0];
            String url= ServiceURLs.mailURl+"RegularCollectionUploads.aspx";
            Log.v("safal","Final URL"+ url);
            ArrayList<NameValuePair> namevaluepair=new ArrayList<NameValuePair>();
            namevaluepair.add(new BasicNameValuePair("RegularCollstring", data));
            namevaluepair.add(new BasicNameValuePair("uid", userID));
            namevaluepair.add(new BasicNameValuePair("MACID", macid));
            namevaluepair.add(new BasicNameValuePair("BTAddress", printerAddress));
            namevaluepair.add(new BasicNameValuePair("MaxReceiptNo", ReceiptNo));
            namevaluepair.add(new BasicNameValuePair("MaxTxnCode", TxnCode));
            namevaluepair.add(new BasicNameValuePair("GroupID", GroupId));

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
                    counts.add(js.getString("GroupID"));
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
                    Log.v("","Message"+Message);

                    if(Message!=null&&!Message.isEmpty())
                    {
                        trnsactionsBL.InsertServerCollectionUploadData("LCollectiion",TOTAL_ACCOUNTS_REG,TOTAL_AMT_UPLOADED_REG,data,LateSchedilerService.this);
                        trnsactionsBL.TruncatetabelGroupIdLate(Message,LateSchedilerService.this);
                    }
                }
            }

        }


    }




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
