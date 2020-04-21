package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.AdvMemeberDetails;
import com.jayam.impactapp.DisplayRoute;
import com.jayam.impactapp.R;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.utils.DialogUtils;


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

public class AdvGroupMembersAdapter extends GenericAdapter {
	public String langitude, latitude;
	public	String dlatitude, dlangitude;
    public AdvGroupMembersAdapter(Context context, List<? extends BaseDO> listItems,String latitude,String langitude) {
	super(context, listItems);
		this.langitude=langitude;
		this.latitude=latitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	AdvaceDemandDO advanceDemandsDO = (AdvaceDemandDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
		ImageView Location = (ImageView) convertView.findViewById(R.id.location);
	tvCenterName.setText("" + advanceDemandsDO.MMI_Name);

		TextView demandamount = (TextView) convertView.findViewById(R.id.tvdmamt);
		TextView maetingdate = (TextView) convertView.findViewById(R.id.tvmdate);
		demandamount.setVisibility(View.GONE);

		maetingdate.setText( advanceDemandsDO.DemandDate);
	Log.e("position", "" + position);

		Location.setVisibility(View.INVISIBLE);
		dlangitude=advanceDemandsDO.LatitudeMember;
		dlatitude=advanceDemandsDO.LatitudeMember;
		Log.v("","advdlangitude"+dlangitude);
		Log.v("","advdlatitude"+dlatitude);
		Location.setTag(advanceDemandsDO);
		Location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(langitude!=null && !langitude.isEmpty() && latitude!=null && !latitude.isEmpty()) {

					if(langitude!=null && !langitude.isEmpty())
					{
						langitude=langitude.trim();
					}

					if(latitude!=null && !latitude.isEmpty())
					{
						latitude=latitude.trim();
					}
					Intent intent = new Intent(mContext, DisplayRoute.class);
					intent.putExtra("Lognitude", langitude);
					intent.putExtra("Latitude", latitude);
					intent.putExtra("DESLognitude", dlangitude);
					intent.putExtra("DESLatitude", dlatitude);
					((Activity) (mContext)).startActivity(intent);
				}
				else
				{
					DialogUtils.showAlert(mContext,"Latitude and Longitude not capture wait");
				}

			}
		});

	convertView.setTag(advanceDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		AdvaceDemandDO advanceDemandsDO = (AdvaceDemandDO) v.getTag();
		AdvanceDemandBL advanceDemandsBL = new AdvanceDemandBL();
		String MCount = advanceDemandsBL.SelectMemberCount(advanceDemandsDO.MMI_Code,mContext);
		Log.e("MCOUNT:-", MCount);
		int MC = Integer.parseInt(MCount);
		// Log.e("MC:-",(String)MC);
		if (MC > 0) {
		    AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
		    alertbox.setMessage("More than One Transactions cannot be allowed");
		    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			    arg0.dismiss();
			}
		    });
		    alertbox.show();
		} else {

			//String cashCount = advanceDemandsBL.SelectCASHCountADV(advanceDemandsDO.MMI_Code);
			String cashCount = advanceDemandsBL.SelectCASHCountADVTransaction(advanceDemandsDO.MMI_Code,mContext);
			int Ccount=0;
			Log.e("MCOUNT:-", MCount);
			try
			{
				Ccount = Integer.parseInt(cashCount);
			}catch (NumberFormatException n)
			{
				n.printStackTrace();
			}


			Log.v("Ccount:-",""+ Ccount);

			if (Ccount ==0) {

				Intent intent = new Intent(mContext, AdvMemeberDetails.class);
				Bundle bundle = new Bundle();
				bundle.putString("mlaid", advanceDemandsDO.MLAI_ID);
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

	    }

	});
	return convertView;

    }

}
