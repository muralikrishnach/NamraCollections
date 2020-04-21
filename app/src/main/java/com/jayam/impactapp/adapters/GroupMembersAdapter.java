package com.jayam.impactapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayam.impactapp.DisplayRoute;
import com.jayam.impactapp.MemberViewCycle;
import com.jayam.impactapp.MemeberDetails;
import com.jayam.impactapp.R;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.objects.RepaymentDetails;
import com.jayam.impactapp.utils.DialogUtils;

import java.util.List;

public class GroupMembersAdapter extends GenericAdapter {
    private String type;

    public GroupMembersAdapter(Context context, List<? extends BaseDO> listItems, String TxnType) {
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



		repayment.setOnClickListener(new OnClickListener() {
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
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Log.e("Txntype:---", type);
		RegularDemandsDO regularDemandsDO = (RegularDemandsDO) v.getTag();
		TrnsactionsBL trnsactionsBL = new TrnsactionsBL();
//		String MCount = trnsactionsBL.getGroupCount(regularDemandsDO.GNo, "Group");
//		Log.e("MCOUNT:-", MCount);
//		int MC = Integer.parseInt(MCount);
//
//
//		if (MC >= 2) {
//		    AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
//		    alertbox.setMessage("More than Two Transactions cannot be allowed");
//		    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//			    arg0.dismiss();
//			}
//		    });
//		    alertbox.show();
//		} else {
//
//
//
//			String cashCount = trnsactionsBL.getCashCount(regularDemandsDO.MLAI_ID, "Group");
//			Log.v("cashCount:-", cashCount);
//
//			int Ccount=0;
//
//			try
//			{
//				Ccount = Integer.parseInt(cashCount);
//			}catch (NumberFormatException n)
//			{
//				n.printStackTrace();
//			}
//
//
//			Log.v("Ccount:-",""+ Ccount);
//
//			if (Ccount ==0) {
//
//				Intent intent = new Intent(mContext, MemeberDetails.class);
//
//				Bundle bundle = new Bundle();
//				bundle.putString("mlaid", regularDemandsDO.MLAI_ID);
//				bundle.putString("txntype", type);
//				intent.putExtras(bundle);
//				((Activity) (mContext)).startActivityForResult(intent, 0);
//			}
//			else
//			{
//
//				AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
//				alertbox.setMessage("Already Transactions Done cannot be allowed");
//				alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						arg0.dismiss();
//					}
//				});
//				alertbox.show();
//
//			}
//
//
//		}



			//String cashCount = trnsactionsBL.getCashCount(regularDemandsDO.MLAI_ID, "Group");
			String cashCount = trnsactionsBL.getCashCountRegular(regularDemandsDO.MLAI_ID, "Group",mContext);
			Log.v("cashCount:-", cashCount);

			int Ccount=0;

			try
			{
				Ccount = Integer.parseInt(cashCount);
			}catch (NumberFormatException n)
			{
				n.printStackTrace();
			}


			Log.v("Ccount:-",""+ Ccount);

			if (Ccount ==0) {
				Intent intent = new Intent(mContext, MemeberDetails.class);
				Bundle bundle = new Bundle();
				bundle.putString("mlaid", regularDemandsDO.MLAI_ID);
				bundle.putString("txntype", type);
				intent.putExtras(bundle);
				((Activity) (mContext)).startActivityForResult(intent, 0);
			}
			else
			{
				AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
				alertbox.setMessage("Already Transactions Done cannot be allowed");
				alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});
				alertbox.show();

			}











		}
	});
	return convertView;
    }

}
