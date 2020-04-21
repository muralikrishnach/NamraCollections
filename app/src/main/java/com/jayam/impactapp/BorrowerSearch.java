package com.jayam.impactapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.ServiceURLs;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.MasterDataDo;
import com.jayam.impactapp.objects.RepaymentDetails;
import com.jayam.impactapp.utils.DialogUtils;
import com.jayam.impactapp.utils.NetworkUtility;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.UtilClass;
import com.jayam.impactapp.webacceslayer.NetworkConnectivity;
import com.jayam.impactapp.webacceslayer.SimpleHttpClient;
import com.jayam.impactapp.xmlhandlers.JsonSynch;


import java.util.ArrayList;


/**
 * Created by administrator_pc on 20-01-2020.
 */

public class BorrowerSearch extends Base  {
    String Loanproductid="";
    private ArrayList<BaseDO> alArrayList_Loanproduct;
    String borrowersearch="";

    LinearLayout groupallotment,llscroll;
    Button saving,submit;
    AutoCompleteTextView groupname,village;

    EditText borrower;
    TextView loanproduct;

    String groupidv="";

    ArrayList<RepaymentDetails> alcgtDo;
    LayoutInflater ll;
    NetworkConnectivity ncty;
    IntialParametrsBL kycbl;

    String Username="";

    String Loanid="";

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        kycbl = new IntialParametrsBL();
        Username = SharedPrefUtils.getKeyValue(BorrowerSearch.this, AppConstants.pref_name, AppConstants.username, "");
        alArrayList_Loanproduct = kycbl.getLoanProduct(BorrowerSearch.this);

        ncty = new NetworkConnectivity(BorrowerSearch.this);
        initializeControlles();
        ll = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//		setResult(AppConstants.RESULTCODE_HOME);

                Intent i = new Intent(BorrowerSearch.this, NavigationActivity.class);
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Loanid=borrower.getText().toString();

//                if(Loanproductid!=null&&!Loanproductid.isEmpty())
//
//                {
                    if(Successfully())
                    {
                        if(NetworkUtility.isNetworkConnectionAvailable(BorrowerSearch.this))
                        {
                            CenterAsync upload=new CenterAsync();
                            upload.execute();
                        }
                        else
                        {
                            showAlertDailog(getResources().getString(R.string.nonetwork));
                        }
                    }

//                }
//                else
//                {
//                    showAlertDailog("Please Select Loan Product");
//                }



            }
        });

    }
    public class CenterAsync extends AsyncTask<String, Void, MasterDataDo> {
        MasterDataDo mastreData = null;

        @Override
        protected MasterDataDo doInBackground(String... arg0) {
            JsonSynch nc = new JsonSynch();
            String s = null;
            try {


               // Loanid=469661
                // Loanid=SHG

                String url  = ServiceURLs.mailURl+"LoanRepaymentView.aspx?UserName="+Username+"&LoanID="+Loanid+"&ProductName="+Loanproductid;


                Log.v("", "urlexistinggroup" + url);
                s = SimpleHttpClient.executemasterHttpGet(url);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (s != null) {
                mastreData = nc.getBorrower(s);
            }
            return mastreData;
        }

        @Override
        protected void onPostExecute(MasterDataDo result) {

            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                IntialParametrsBL mbl = new IntialParametrsBL();

                mbl.deleteBorrowersearch(BorrowerSearch.this);
                mbl.Repaymentsave(result,BorrowerSearch.this);
                if(!BorrowerSearch.this.isFinishing()) {
                    HideLoader();

                    AlertDialog.Builder builder = new AlertDialog.Builder(BorrowerSearch.this);

                    builder.setMessage("Repayment Synch Data Download Sucessfully")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    creatingViews();
                                }
                            });


                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually

                    alert.show();



                }

            } else {
                if(!BorrowerSearch.this.isFinishing()) {
                    showAlertDailog("");
                }
            }
        }
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		creatingViews();
    }


    public void initializeControlles()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        BorrowerSearch.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        groupallotment		=	(LinearLayout)inflater.inflate(R.layout.borrowersearch, null);
        borrower    =  (EditText)groupallotment.findViewById(R.id.loanid);
        loanproduct    =  (TextView)groupallotment.findViewById(R.id.payment);
        llscroll  =  (LinearLayout)groupallotment.findViewById(R.id.llvill);
        submit = (Button)groupallotment.findViewById(R.id.btnSave);
        showHomeIcons();
        ivLogout.setVisibility(View.GONE);
        llBaseMiddle.addView(groupallotment, ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        tvHeader.setText("BorrowerSearch");


        loanproduct.setTag(-1);
        loanproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                int checkedItem = (Integer) v.getTag();
                final String names[] = new String[alArrayList_Loanproduct.size()];

                for (int i = 0; i < alArrayList_Loanproduct.size(); i++) {
                    names[i] = alArrayList_Loanproduct.get(i).name;
                }
                // Arrays.sort(names);
                DialogUtils.showSingleChoiceLIstItems(BorrowerSearch.this,
                        names, (Integer) v.getTag(),
                        "Please select PaymentMode",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                v.setTag(which);
                                ((TextView) v).setText(names[which]);
                                Loanproductid = alArrayList_Loanproduct.get(which).id;

                                Log.v("", "Loanproductid" + Loanproductid);

                            }
                        });
            }
        });



    }

    private void creatingViews() {
        // TODO Auto-generated method stub





        llscroll.removeAllViews();
        alcgtDo = kycbl.SelectAllRepayment(BorrowerSearch.this);
        if(alcgtDo.size()>0)
        {
            for(int i=0;i<alcgtDo.size();i++){

                final RepaymentDetails adkyc = alcgtDo.get(i);
                final View vv  =  ll.inflate(R.layout.borrowerloangrid, null);


                TextView  Sno =  (TextView)vv.findViewById(R.id.snum);
                TextView  Paiddate   =  (TextView)vv.findViewById(R.id.padidate);
                TextView  Principal   =  (TextView)vv.findViewById(R.id.principal);
                TextView  Intrest   =  (TextView)vv.findViewById(R.id.intrest);
                TextView  Total   =  (TextView)vv.findViewById(R.id.total);



                Sno.setText(adkyc.sno);
                Paiddate.setText(adkyc.PaidDate);
                Principal.setText(adkyc.Principle);
                Intrest.setText(adkyc.Interest);
                Total.setText(adkyc.Total);


                llscroll.addView(vv);
            }



        }


    }

    private boolean Successfully() {
        if (borrower.getText().toString().trim().length() == 0) {
            return UtilClass.showAlert(BorrowerSearch.this, "Please Enter Loan No");
        }

        if (loanproduct.getText().toString().trim().length() == 0) {
            return UtilClass.showAlert(BorrowerSearch.this, "Please Select Product");
        }



        return true;
    }

}
