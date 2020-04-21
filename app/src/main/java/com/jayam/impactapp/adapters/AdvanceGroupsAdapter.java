package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.AdvanceGroupDetails;
import com.jayam.impactapp.DisplayRoute;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.utils.DialogUtils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AdvanceGroupsAdapter extends GenericAdapter {
	public String langitude, latitude;
	public	String dlatitude, dlangitude;
    public AdvanceGroupsAdapter(Context context, List<? extends BaseDO> listItems,String latitude,String langitude) {
	super(context, listItems);

		Log.v("","advlangitudein"+latitude);
		Log.v("","advlatitudein"+langitude);
		this.langitude=langitude;
		this.latitude=latitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	AdvaceDemandDO advanceDemandsDO = (AdvaceDemandDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
	ImageView imgConfirmed = (ImageView) convertView.findViewById(R.id.imgConfirmed);
		ImageView Location = (ImageView) convertView.findViewById(R.id.location);

		TextView demandamount = (TextView) convertView.findViewById(R.id.tvdmamt);
		TextView maetingdate = (TextView) convertView.findViewById(R.id.tvmdate);
		demandamount.setVisibility(View.GONE);
		imgConfirmed.setVisibility(View.GONE);
		maetingdate.setText( advanceDemandsDO.DemandDate);


	tvCenterName.setText("" + advanceDemandsDO.MGI_Name);
		dlangitude=advanceDemandsDO.LongitudeGroup;
		dlatitude=advanceDemandsDO.LatitudeGroup;
		Log.v("","advdlangitude"+dlangitude);
		Log.v("","advdlatitude"+dlatitude);

		Log.v("","advlangitude"+langitude);
		Log.v("","advlatitude"+latitude);

	Log.e("position", "" + position);

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
		AdvaceDemandDO regularDemandsDO = (AdvaceDemandDO) v.getTag();
		Intent intent = new Intent(mContext, AdvanceGroupDetails.class);
		intent.putExtra("groupnumber", regularDemandsDO.MGI_Code);
		((Activity) (mContext)).startActivityForResult(intent, 0);
		//
	    }
	});




		return convertView;
    }

}
