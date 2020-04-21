package com.jayam.impactapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayam.impactapp.LateMemeberDetails;
import com.jayam.impactapp.MemberViewCycle;
import com.jayam.impactapp.MemeberDetails;
import com.jayam.impactapp.R;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.DialogUtils;

import java.util.List;

public class LateGroupMembersAdapter extends GenericAdapter {
    private String type;

    public LateGroupMembersAdapter(Context context, List<? extends BaseDO> listItems, String TxnType) {
        super(context, listItems);
        this.type = TxnType;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
        convertView = getLayoutInflater().inflate(R.layout.memberwithrepayment, null);
        TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
        ImageView repayment = (ImageView) convertView.findViewById(R.id.repayment);
        tvCenterName.setText("" + regularDemandsDO.MemberName + "-" + regularDemandsDO.MLAI_ID);
        Log.e("position", "" + position);



        repayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String LoanId=regularDemandsDO.MLAI_ID;
                Log.v("","LoanId"+LoanId);
                if(LoanId!=null && !LoanId.isEmpty() ) {


                    Intent intent = new Intent(mContext, MemberViewCycle.class);
                    intent.putExtra("LoanId", LoanId);

                    ((Activity) (mContext)).startActivity(intent);
                }
                else
                {
                    DialogUtils.showAlert(mContext,"Loan Id canot be empty");
                }

            }
        });





        convertView.setTag(regularDemandsDO);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegularDemandsDO regularDemandsDO = (RegularDemandsDO) v.getTag();

                Intent intent = new Intent(mContext, LateMemeberDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("mlaid", regularDemandsDO.MLAI_ID);
                bundle.putString("txntype", type);
                intent.putExtras(bundle);
                ((Activity) (mContext)).startActivityForResult(intent, 0);


            }
        });
        return convertView;
    }

}
