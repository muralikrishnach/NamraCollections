package com.jayam.impactapp;

import android.app.ActionBar;
import android.content.Context;
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
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.RegularDemandsDO;

import java.util.ArrayList;

import static com.jayam.impactapp.R.id.recyclerView;

public class TransactionHistoryMemberwise extends Baselogin {
    LinearLayout llGroupAuth;
    RecyclerView lvGroups;
    AutoCompleteTextView Centername, groupName;
    CustomAdapterView adapter;
    private TrnsactionsBL trnsactionsBL;
    private ArrayList<RegularDemandsDO> alRegularDemands;
    @Override
    public void initialize() {
        trnsactionsBL = new TrnsactionsBL();
        alRegularDemands = trnsactionsBL.SelectAllTransaction(TransactionHistoryMemberwise.this);
        initializeControls();
    }

    private void initializeControls() {

        llGroupAuth = (LinearLayout) inflater.inflate(R.layout.transactionhistorymember, null);
        lvGroups = (RecyclerView) llGroupAuth.findViewById(recyclerView);
        Centername = (AutoCompleteTextView) llGroupAuth.findViewById(R.id.payment);
        groupName = (AutoCompleteTextView) llGroupAuth.findViewById(R.id.AutocenterCrocpv);

        adapter = new CustomAdapterView(TransactionHistoryMemberwise.this, alRegularDemands);
        lvGroups.setHasFixedSize(true);
        lvGroups.setLayoutManager(new LinearLayoutManager(TransactionHistoryMemberwise.this));
        lvGroups.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        llBaseMiddle.addView(llGroupAuth, ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);

        showHomeIcons();

        ivLogout.setVisibility(View.GONE);
        tvHeader.setText("Payment Status");


        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(AppConstants.RESULTCODE_HOME);
                finish();
            }
        });


    }

    public class CustomAdapterView extends RecyclerView.Adapter<CustomAdapterView.MyViewHolder> {

        Context mContext;
        String memberid = "";
        private ArrayList<RegularDemandsDO> LoanMemberData;
        CustomItemClickListener listener;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView membername, amount,status;


            public MyViewHolder(View view) {
                super(view);
                membername = (TextView) view.findViewById(R.id.tv_date);
                status = (TextView) view.findViewById(R.id.iv_status);
                amount = (TextView) view.findViewById(R.id.tv_amt);


            }
        }


        public CustomAdapterView(Context mContext, ArrayList<RegularDemandsDO> LoanMemberData) {

            this.LoanMemberData = LoanMemberData;
            this.mContext = mContext;

//            Log.v("","this.LoanMemberDatasizr"+this.LoanMemberData.size());
        }


        @Override
        public CustomAdapterView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.v("", "oncreateview");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memberstatustransaction, parent, false);
            final CustomAdapterView.MyViewHolder vh = new CustomAdapterView.MyViewHolder(v); // pass the view to View Holder

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        listener.onItemClick(v, vh.getPosition());
                    }

                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(CustomAdapterView.MyViewHolder myViewHolder, int i) {
            {
                RegularDemandsDO movie = this.LoanMemberData.get(i);


                String staus = movie.Status;

                Log.v("", "regstatus" + staus);
                if (staus != null) {

                } else
                {
                    myViewHolder.status.setText("Pending");
                }




                myViewHolder.membername.setText(movie.MemberName);

                myViewHolder.amount.setText(movie.collectedAmount);


            }

        }

        @Override
        public int getItemCount() {
            return LoanMemberData.size();
        }

    }
}
