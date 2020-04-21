package com.jayam.impactapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.ServiceURLs;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.MasterDataDo;
import com.jayam.impactapp.utils.NetworkUtility;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.webacceslayer.SimpleHttpClient;
import com.jayam.impactapp.xmlhandlers.JsonSynch;

/**
 * Created by administrator_pc on 18-02-2020.
 */

public class ProductSynch extends Base {
    String Username="";
    @Override
    public void initialize() {

        showHomeIcons();
        ivLogout.setVisibility(View.GONE);
        tvHeader.setText("Product Synch");
        Username = SharedPrefUtils.getKeyValue(ProductSynch.this, AppConstants.pref_name, AppConstants.username, "");
        if (NetworkUtility.isNetworkConnectionAvailable(ProductSynch.this)) {
            ShowLoader();
            new LoanProductSynch().execute();

        } else {
            showAlertDailog(getResources().getString(R.string.nonetwork));
        }
    }
    public class LoanProductSynch extends AsyncTask<String, Void, MasterDataDo> {
        MasterDataDo mastreData = null;
        @Override
        protected MasterDataDo doInBackground(String... arg0) {
            JsonSynch nc = new JsonSynch();
            String s = null;
            try {
                String url  = ServiceURLs.mailURl+"LoanProductData.aspx?Username=" + Username;
                Log.v("", "loanmaster" + url);
                s = SimpleHttpClient.executemasterHttpGet(url);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (s != null) {
                mastreData = nc.getLoanproduct(s);
            }
            return mastreData;
        }

        @Override
        protected void onPostExecute(MasterDataDo result) {

            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                IntialParametrsBL mbl = new IntialParametrsBL();

                mbl.TruncatetabelProduct(ProductSynch.this);
                mbl.Loanproducttablesave(result,ProductSynch.this);
                if(!ProductSynch.this.isFinishing()) {
                    HideLoader();


                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductSynch.this);

                    builder.setMessage("LoanProduct Synch Data Download Sucessfully")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();

                                }
                            });



                    AlertDialog alert = builder.create();


                    alert.show();


                }

            } else {
                if(!ProductSynch.this.isFinishing()) {
                    showAlertDailog("");
                }
            }
        }
    }
}
