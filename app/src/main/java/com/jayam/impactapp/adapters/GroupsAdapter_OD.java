package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.DisplayRoute;
import com.jayam.impactapp.Members_OD;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.ODDemandsDO;
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

public class GroupsAdapter_OD extends GenericAdapter {
	public String langitude, latitude;
	public	String dlatitude, dlangitude;
    public GroupsAdapter_OD(Context context, List<? extends BaseDO> listItems,String latitude,String langitude) {
	super(context, listItems);
		this.langitude=langitude;
		this.latitude=latitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ODDemandsDO odDemandsDO = (ODDemandsDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);

		TextView demandamount = (TextView) convertView.findViewById(R.id.tvdmamt);
		TextView maetingdate = (TextView) convertView.findViewById(R.id.tvmdate);

		maetingdate.setText( odDemandsDO.DemandDate);
		demandamount.setVisibility(View.GONE);

	tvCenterName.setText("" + odDemandsDO.MGI_Name);


		ImageView Location = (ImageView) convertView.findViewById(R.id.location);
		dlangitude=odDemandsDO.LongitudeGroup;
		dlatitude=odDemandsDO.LatitudeGroup;
		Log.v("","dlangitude"+dlangitude);
		Log.v("","dlatitude"+dlatitude);
		Location.setTag(odDemandsDO);
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



	convertView.setTag(odDemandsDO);

	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		ODDemandsDO obj = (ODDemandsDO) v.getTag();
		Intent intent = new Intent(mContext, Members_OD.class);
		intent.putExtra("groupcode", obj.MGI_Code);
		((Activity) (mContext)).startActivityForResult(intent, 1234);

	    }
	});
	return convertView;
    }

}
