package com.jayam.impactapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;


import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.ServiceURLs;
import com.jayam.impactapp.database.IntialParametrsBL;
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


public class MemberViewCycle extends Base {


    String Loanproductid="";
    private ArrayList<BaseDO> alArrayList_Loanproduct;
    String borrowersearch="";

    LinearLayout groupallotment,llscroll;
    Button saving,submit;
    AutoCompleteTextView groupname,village;

    EditText borrower;
    EditText loanproduct;


    ArrayList<RepaymentDetails> alcgtDo;
    LayoutInflater ll;
    NetworkConnectivity ncty;
    IntialParametrsBL kycbl;
    String Username="";
    String Loanid="";
    String MemberName="";
    RelativeLayout llGroupAuth;
    RecyclerView lvGroups;
//    public LinearLayout llBaseMiddle, llBaseMiddle_lv,vila;



     EditText searchBox;

    CustomAdapterView adapter;

    String LoanIdva="";

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

        if(this.getIntent().getExtras()!=null && this.getIntent().getExtras().containsKey("LoanId"))
        {
            LoanIdva=getIntent().getExtras().getString("LoanId");
        }

        kycbl = new IntialParametrsBL();
        Username = SharedPrefUtils.getKeyValue(MemberViewCycle.this, AppConstants.pref_name, AppConstants.username, "");
        alArrayList_Loanproduct = kycbl.getLoanProduct(MemberViewCycle.this);

        ncty = new NetworkConnectivity(MemberViewCycle.this);



        alArrayList_Loanproduct = kycbl.getLoanProduct(MemberViewCycle.this);


        initializeControls();


//        loanproduct.setTag(-1);
//        loanproduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//
//                int checkedItem = (Integer) v.getTag();
//                final String names[] = new String[alArrayList_Loanproduct.size()];
//
//                for (int i = 0; i < alArrayList_Loanproduct.size(); i++) {
//                    names[i] = alArrayList_Loanproduct.get(i).name;
//                }
//                // Arrays.sort(names);
//                DialogUtils.showSingleChoiceLIstItems(MemberViewCycle.this,
//                        names, (Integer) v.getTag(),
//                        "Please select PaymentMode",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                v.setTag(which);
//                                ((TextView) v).setText(names[which]);
//                                Loanproductid = alArrayList_Loanproduct.get(which).id;
//
//                                Log.v("", "Loanproductid" + Loanproductid);
//
//                            }
//                        });
//            }
//        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub



//                if(Loanproductid!=null&&!Loanproductid.isEmpty())
//
//                {
                if(Successfully())
                {
                    Loanid=searchBox.getText().toString().trim();
                   // MemberName=loanproduct.getText().toString().trim();
                    if(NetworkUtility.isNetworkConnectionAvailable(MemberViewCycle.this))
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

    public void initializeControls(){


        llGroupAuth  = (RelativeLayout)inflater.inflate(R.layout.memberrecycle, null);
        lvGroups = (RecyclerView) llGroupAuth.findViewById(recyclerView);
        loanproduct    =  (EditText)llGroupAuth.findViewById(R.id.payment);
        searchBox = (EditText) llGroupAuth.findViewById(R.id.AutocenterCrocpv);
        submit = (Button)llGroupAuth.findViewById(R.id.btnSave);

        llBaseMiddle.addView(llGroupAuth, ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);

        showHomeIcons();

        ivLogout.setVisibility(View.GONE);
        tvHeader.setText("Repayment Details");

        searchBox.setText(LoanIdva);

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

    public class CenterAsync extends AsyncTask<String, Void, MasterDataDo> {
        MasterDataDo mastreData = null;

        @Override
        protected MasterDataDo doInBackground(String... arg0) {
            JsonSynch nc = new JsonSynch();
            String s = null;
            try {


                // Loanid=469661
                // Loanid=SHG

                String url  = ServiceURLs.mailURl+"LoanRepaymentView.aspx?UserName="+Username+"&LoanID="+Loanid;


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

                mbl.deleteBorrowersearch(MemberViewCycle.this);
                mbl.Repaymentsave(result,MemberViewCycle.this);
                if(!MemberViewCycle.this.isFinishing()) {
                    HideLoader();


                    alcgtDo = kycbl.SelectAllRepayment(MemberViewCycle.this);
                    Log.v("","alcgtDo"+alcgtDo.size());

                    if(alcgtDo!=null&&alcgtDo.size()>0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewCycle.this);
                        builder.setMessage("Repayment Synch Data Download Sucessfully")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        adapter = new CustomAdapterView(MemberViewCycle.this,alcgtDo);
                                        lvGroups.setHasFixedSize(true);
                                        lvGroups.setLayoutManager(new LinearLayoutManager(MemberViewCycle.this));
                                        lvGroups.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewCycle.this);
                        builder.setMessage("No Data Found")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }




                }

            } else {
                if(!MemberViewCycle.this.isFinishing()) {
                    showAlertDailog("");
                }
            }
        }
    }

    public   class CustomAdapterView extends RecyclerView.Adapter<CustomAdapterView.MyViewHolder>  {

        Context mContext;
        String memberid="";
        private ArrayList<RepaymentDetails> LoanMemberData;
        CustomItemClickListener listener;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView memberid, membername,Spousename,AAmount,Eamount,ACkid,CBsattus,Villagename;

            public MyViewHolder(View view) {
                super(view);
                memberid = (TextView) view.findViewById(R.id.memberid);
                membername = (TextView) view.findViewById(R.id.membername);
                Spousename = (TextView) view.findViewById(R.id.Spousename);
                AAmount = (TextView) view.findViewById(R.id.AAmount);
                Eamount = (TextView) view.findViewById(R.id.Eamount);





            }
        }


        public CustomAdapterView(Context mContext, ArrayList<RepaymentDetails> LoanMemberData ) {

            this.LoanMemberData = LoanMemberData;
            this.mContext=mContext;

//            Log.v("","this.LoanMemberDatasizr"+this.LoanMemberData.size());
        }




        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.v("","oncreateview");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_itemmemberrec, parent, false);
            final MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder

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
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            {
                RepaymentDetails movie = this.LoanMemberData.get(i);


                       Log.v("","paiddate"+movie.PaidDate);

                myViewHolder.memberid.setText(movie.sno);
                myViewHolder.membername.setText(movie.PaidDate);
                myViewHolder.Spousename.setText(movie.Principle);
                myViewHolder.AAmount.setText(movie.Interest);
                myViewHolder.Eamount.setText(movie.Total);


            }

        }

        @Override
        public int getItemCount() {
            return LoanMemberData.size();
        }




    }
    private boolean Successfully() {
        if (searchBox.getText().toString().trim().length() == 0) {
            return UtilClass.showAlert(MemberViewCycle.this, "Please Enter Loan No");
        }

//        if (loanproduct.getText().toString().trim().length() == 0) {
//            return UtilClass.showAlert(MemberViewCycle.this, "Please Enter Member Name");
//        }



        return true;
    }
}
