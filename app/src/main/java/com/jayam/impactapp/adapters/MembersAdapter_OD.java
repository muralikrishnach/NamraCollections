package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.MemberDetails_OD;
import com.jayam.impactapp.R;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.ODDemandsDO;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MembersAdapter_OD extends GenericAdapter {

    public MembersAdapter_OD(Context context, List<? extends BaseDO> listItems) {
	super(context, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ODDemandsDO odDemandsDO = (ODDemandsDO) getList().get(position);

	if (convertView == null) {
	    convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	}
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
		TextView demandamount = (TextView) convertView.findViewById(R.id.tvdmamt);
		TextView maetingdate = (TextView) convertView.findViewById(R.id.tvmdate);
		ImageView Location = (ImageView) convertView.findViewById(R.id.location);
	tvCenterName.setText("" + odDemandsDO.MMI_Name);

		maetingdate.setText( odDemandsDO.DemandDate);
		demandamount.setVisibility(View.GONE);
		Location.setVisibility(View.GONE);
	convertView.setTag(odDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		ODDemandsDO obj = (ODDemandsDO) v.getTag();
		ODDemandsBL bl = new ODDemandsBL();
		String MCount = bl.SelectMemberCount(obj.MLAI_ID,mContext);
		Log.e("MCOUNT_OD:-", MCount);
		int MC = Integer.parseInt(MCount);
		if (MC > 0) {
		    AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
		    alertbox.setMessage("More than One Transaction cannot be allowed");
		    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			    arg0.dismiss();
			}
		    });
		    alertbox.show();
		} else {

			String cashCount = bl.SelectCashODrCountOD(obj.MLAI_ID,mContext);
			Log.e("MCOUNT_OD:-", MCount);


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

				Intent intent = new Intent(mContext, MemberDetails_OD.class);
				intent.putExtra("memberCode", obj.MMI_Code);
				((Activity) (mContext)).startActivityForResult(intent, 1234);
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
	    }
	});
	return convertView;
    }

}
