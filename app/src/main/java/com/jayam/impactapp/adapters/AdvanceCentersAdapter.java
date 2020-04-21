package com.jayam.impactapp.adapters;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.api.GoogleApiClient;
import com.jayam.LocationUtil.PermissionUtils;
import com.jayam.impactapp.AdvCenterDetails;
import com.jayam.impactapp.AdvCenters;
import com.jayam.impactapp.AdvanceGroups;
import com.jayam.impactapp.DisplayRoute;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.utils.DialogUtils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AdvanceCentersAdapter extends GenericAdapter {
	private Location mLastLocation;
	boolean isPermissionGranted;

	private final static int PLAY_SERVICES_REQUEST = 1000;
	private final static int REQUEST_CHECK_SETTINGS = 2000;
	PermissionUtils permissionUtils;
	ArrayList<String> permissions = new ArrayList<>();
	private GoogleApiClient mGoogleApiClient;

	public String langitude, latitude;
    private String type;
	public	String dlatitude, dlangitude;
    public AdvanceCentersAdapter(Context context, List<? extends BaseDO> listItems, String type,String latitude,String langitude) {
	super(context, listItems);
	this.type = type;
		this.langitude=langitude;
		this.latitude=latitude;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	AdvaceDemandDO advaceDemandDO = (AdvaceDemandDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
		ImageView Location = (ImageView) convertView.findViewById(R.id.location);

		TextView demandamount = (TextView) convertView.findViewById(R.id.tvdmamt);
		TextView maetingdate = (TextView) convertView.findViewById(R.id.tvmdate);
		demandamount.setVisibility(View.GONE);

		maetingdate.setText( advaceDemandDO.DemandDate);

	tvCenterName.setText("" + advaceDemandDO.CenterName);



		dlangitude=advaceDemandDO.LongitudeCenter;
		dlatitude=advaceDemandDO.LatitudeCenter;
		Log.v("","dlangitude"+dlangitude);
		Log.v("","dlatitude"+dlatitude);

	Log.e("position", "" + position);
	convertView.setTag(advaceDemandDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (type.equalsIgnoreCase("center")) {
		    AdvaceDemandDO advaceDemandDO = (AdvaceDemandDO) v.getTag();
		    Intent intent = new Intent(mContext, AdvCenterDetails.class);
		    intent.putExtra("CenterCode", advaceDemandDO.MCI_Code);
		    ((Activity) (mContext)).startActivityForResult(intent, 0);
		} else {
		    AdvaceDemandDO advaceDemandDO = (AdvaceDemandDO) v.getTag();
		    Intent intent = new Intent(mContext, AdvanceGroups.class);
		    intent.putExtra("CenterCode", advaceDemandDO.MCI_Code);
		    ((Activity) (mContext)).startActivityForResult(intent, 0);
		}

	    }
	});

		Location.setTag(advaceDemandDO);
		Location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mLastLocation!=null)
				{
					latitude = mLastLocation.getLatitude()+"";
					langitude = mLastLocation.getLongitude()+"";
				}
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
	return convertView;
    }

}
