package com.jayam.impactapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.ServiceURLs;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.MasterDataDo;
import com.jayam.impactapp.objects.RepaymentDetails;
import com.jayam.impactapp.utils.NetworkUtility;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.UtilClass;
import com.jayam.impactapp.webacceslayer.NetworkConnectivity;
import com.jayam.impactapp.webacceslayer.SimpleHttpClient;
import com.jayam.impactapp.xmlhandlers.JsonSynch;

import java.util.ArrayList;

import static com.jayam.impactapp.R.id.recyclerView;

public class TransactionHistory extends Baselogin {

    String dayfilter,paymethod="";
    String Loanproductid="";
    private ArrayList<BaseDO> alArrayList_Loanproduct;
    String borrowersearch="";
    LinearLayout groupallotment,llscroll;
    Button saving,submit;
    AutoCompleteTextView groupname,village;
    EditText borrower;
    EditText loanproduct;

    LayoutInflater ll;
    NetworkConnectivity ncty;
    IntialParametrsBL kycbl;
    String Username="";
    String Loanid="";
    String MemberName="";
    RelativeLayout llGroupAuth;
    RecyclerView lvGroups;
    EditText searchBox;
    TransactionHistory.CustomAdapterView adapter;
    private AdvanceDemandBL advdemadsbl;
    TextView  ttoday,tyesterday,tselectdate,taeps,tmatm,tpgtype,tupi;
    AdvanceDemandBL advanceBL;
    private ArrayList<AdvaceDemandDO> alAdvanceDemands;
    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        advanceBL = new AdvanceDemandBL();
        alAdvanceDemands = advanceBL.SelectAllFlagwise(TransactionHistory.this);
        kycbl = new IntialParametrsBL();
        Username = SharedPrefUtils.getKeyValue(TransactionHistory.this, AppConstants.pref_name, AppConstants.username, "");
        initializeControls();




    }

    public void initializeControls(){


        llGroupAuth  = (RelativeLayout)inflater.inflate(R.layout.transactionhistory, null);
        lvGroups = (RecyclerView) llGroupAuth.findViewById(recyclerView);

        ttoday = (TextView) llGroupAuth.findViewById(R.id.today);
        tyesterday = (TextView) llGroupAuth.findViewById(R.id.yeday);
        tselectdate = (TextView) llGroupAuth.findViewById(R.id.sdat);
        taeps = (TextView) llGroupAuth.findViewById(R.id.aep);
        tmatm = (TextView) llGroupAuth.findViewById(R.id.mat);
        tpgtype = (TextView) llGroupAuth.findViewById(R.id.pgt);
        tupi = (TextView) llGroupAuth.findViewById(R.id.upi);

         TextView  today,yesterday,selectdate,aeps,matm,pgtype,upi;

        llBaseMiddle.addView(llGroupAuth, ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);

        showHomeIcons();

        ivLogout.setVisibility(View.GONE);
        tvHeader.setText(getResources().getString(R.string.thistory));
        adapter = new TransactionHistory.CustomAdapterView(TransactionHistory.this,alAdvanceDemands);
        lvGroups.setHasFixedSize(true);
        lvGroups.setLayoutManager(new LinearLayoutManager(TransactionHistory.this));
        lvGroups.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ttoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayfilter="";

                ttoday.setBackgroundColor(AppConstants.Colorcodeb);
                ttoday.setTextColor(AppConstants.Colorcodet);


                tyesterday.setBackgroundColor(AppConstants.Colorcodet);
                tyesterday.setTextColor(AppConstants.Colorcodew);

                tselectdate.setBackgroundColor(AppConstants.Colorcodet);
                tselectdate.setTextColor(AppConstants.Colorcodew);




            }
        });

        tyesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 dayfilter="";
                tyesterday.setBackgroundColor(AppConstants.Colorcodeb);
                tyesterday.setTextColor(AppConstants.Colorcodet);

                ttoday.setBackgroundColor(AppConstants.Colorcodet);
                ttoday.setTextColor(AppConstants.Colorcodew);

                tselectdate.setBackgroundColor(AppConstants.Colorcodet);
                tselectdate.setTextColor(AppConstants.Colorcodew);


            }
        });


        tselectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayfilter="";
                tselectdate.setBackgroundColor(AppConstants.Colorcodeb);
                tselectdate.setTextColor(AppConstants.Colorcodet);

                ttoday.setBackgroundColor(AppConstants.Colorcodet);
                ttoday.setTextColor(AppConstants.Colorcodew);


                tyesterday.setBackgroundColor(AppConstants.Colorcodet);
                tyesterday.setTextColor(AppConstants.Colorcodew);

            }
        });









        taeps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymethod="";
                taeps.setBackgroundColor(AppConstants.Colorcodeb);
                taeps.setTextColor(AppConstants.Colorcodet);


                tmatm.setBackgroundColor(AppConstants.Colorcodet);
                tmatm.setTextColor(AppConstants.Colorcodew);

                tpgtype.setBackgroundColor(AppConstants.Colorcodet);
                tpgtype.setTextColor(AppConstants.Colorcodew);

                tupi.setBackgroundColor(AppConstants.Colorcodet);
                tupi.setTextColor(AppConstants.Colorcodew);


            }
        });

        tmatm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymethod="";
                tmatm.setBackgroundColor(AppConstants.Colorcodeb);
                tmatm.setTextColor(AppConstants.Colorcodet);


                tpgtype.setBackgroundColor(AppConstants.Colorcodet);
                tpgtype.setTextColor(AppConstants.Colorcodew);

                tupi.setBackgroundColor(AppConstants.Colorcodet);
                tupi.setTextColor(AppConstants.Colorcodew);

                taeps.setBackgroundColor(AppConstants.Colorcodet);
                taeps.setTextColor(AppConstants.Colorcodew);


            }
        });


        tpgtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymethod="";
                tpgtype.setBackgroundColor(AppConstants.Colorcodeb);
                tpgtype.setTextColor(AppConstants.Colorcodet);


                tmatm.setBackgroundColor(AppConstants.Colorcodet);
                tmatm.setTextColor(AppConstants.Colorcodew);

                tupi.setBackgroundColor(AppConstants.Colorcodet);
                tupi.setTextColor(AppConstants.Colorcodew);

                taeps.setBackgroundColor(AppConstants.Colorcodet);
                taeps.setTextColor(AppConstants.Colorcodew);


            }
        });

        tupi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymethod="";
                tupi.setBackgroundColor(AppConstants.Colorcodeb);
                tupi.setTextColor(AppConstants.Colorcodet);



                tmatm.setBackgroundColor(AppConstants.Colorcodet);
                tmatm.setTextColor(AppConstants.Colorcodew);

                tpgtype.setBackgroundColor(AppConstants.Colorcodet);
                tpgtype.setTextColor(AppConstants.Colorcodew);

                taeps.setBackgroundColor(AppConstants.Colorcodet);
                taeps.setTextColor(AppConstants.Colorcodew);

            }
        });



        ivHome.setOnClickListener(new  View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setResult(AppConstants.RESULTCODE_HOME);
                finish();
            }
        });

    }



    public   class CustomAdapterView extends RecyclerView.Adapter<TransactionHistory.CustomAdapterView.MyViewHolder>  {

        Context mContext;
        String memberid="";
        private ArrayList<AdvaceDemandDO> LoanMemberData;
        CustomItemClickListener listener;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView date, amount;
            ImageView status;

            public MyViewHolder(View view) {
                super(view);
                date = (TextView) view.findViewById(R.id.tv_date);
                status = (ImageView) view.findViewById(R.id.iv_status);
                amount = (TextView) view.findViewById(R.id.tv_amt);






            }
        }


        public CustomAdapterView(Context mContext, ArrayList<AdvaceDemandDO> LoanMemberData ) {

            this.LoanMemberData = LoanMemberData;
            this.mContext=mContext;

//            Log.v("","this.LoanMemberDatasizr"+this.LoanMemberData.size());
        }




        @Override
        public CustomAdapterView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.v("","oncreateview");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactionmembers, parent, false);
            final CustomAdapterView.MyViewHolder vh = new CustomAdapterView.MyViewHolder(v); // pass the view to View Holder

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener!=null){
                        listener.onItemClick(v, vh.getPosition());
                    }

                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(CustomAdapterView.MyViewHolder myViewHolder, int i) {
            {
                AdvaceDemandDO movie = this.LoanMemberData.get(i);



               String staus=movie.Status;
               if(staus!=null)
               {
                   if(staus.equalsIgnoreCase("0"))
                   {
                       if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                           myViewHolder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.cancle_icon));
                       else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
                           myViewHolder.status.setBackground(getResources().getDrawable(R.drawable.cancle_icon));
                       else
                           myViewHolder.status.setBackground(ContextCompat.getDrawable(TransactionHistory.this, R.drawable.cancle_icon));
                   }
                   else   if(staus.equalsIgnoreCase("-1"))
                   {
                       if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                           myViewHolder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_icon));
                       else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
                           myViewHolder.status.setBackground(getResources().getDrawable(R.drawable.right_icon));
                       else
                           myViewHolder.status.setBackground(ContextCompat.getDrawable(TransactionHistory.this, R.drawable.right_icon));

                   }
               }
               else
               {
                   if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                       myViewHolder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.upi_pending));
                   else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
                       myViewHolder.status.setBackground(getResources().getDrawable(R.drawable.upi_pending));
                   else
                       myViewHolder.status.setBackground(ContextCompat.getDrawable(TransactionHistory.this, R.drawable.upi_pending));
               }
               Log.v("","movie.TransactionTime"+movie.TransactionTime);
                Log.v("","movie.CollectedAmt"+movie.previousAmt);
                myViewHolder.date.setText(movie.TransactionTime);
                myViewHolder.amount.setText(movie.previousAmt);


            }

        }

        @Override
        public int getItemCount() {
            return LoanMemberData.size();
        }




    }
    private boolean Successfully() {
//        if (searchBox.getText().toString().trim().length() == 0) {
//            return UtilClass.showAlert(TransactionHistory.this, "Please Enter Loan No");
//        }
//
//        if (loanproduct.getText().toString().trim().length() == 0) {
//            return UtilClass.showAlert(TransactionHistory.this, "Please Enter Member Name");
//        }



        return true;
    }
}
