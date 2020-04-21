package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.DisplayRoute;
import com.jayam.impactapp.GroupDetails_Centerwise;
import com.jayam.impactapp.R;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
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

public class GroupsAdapter_Centerwise extends GenericAdapter {
	public	String dlatitude, dlangitude;
	public String langitude, latitude;
    public GroupsAdapter_Centerwise(Context context, List<? extends BaseDO> listItems,String latitude,String langitude) {
	super(context, listItems);
		this.langitude=langitude;
		this.latitude=latitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
	ImageView imgConfirmed = (ImageView) convertView.findViewById(R.id.imgConfirmed);
		TextView demandamount = (TextView) convertView.findViewById(R.id.tvdmamt);
		TextView maetingdate = (TextView) convertView.findViewById(R.id.tvmdate);


		ImageView Location = (ImageView) convertView.findViewById(R.id.location);

	RegularDemandsBL bl = new RegularDemandsBL();
	String fullpaymnet = bl.CheckForFullpayMent(regularDemandsDO.GNo,mContext);


	if (fullpaymnet != null && fullpaymnet.equalsIgnoreCase("0")) {
	    imgConfirmed.setVisibility(View.VISIBLE);
	} else {
	    imgConfirmed.setVisibility(View.INVISIBLE);
	}

	tvCenterName.setText("" + regularDemandsDO.GroupName + "-" + regularDemandsDO.GNo);

		demandamount.setText( regularDemandsDO.GroupAmt);
		maetingdate.setText( regularDemandsDO.GroupMeeting);
		dlangitude=regularDemandsDO.LongitudeGroup;
		dlatitude=regularDemandsDO.LatitudeGroup;
		Log.v("","dlangitude"+dlangitude);
		Log.v("","dlatitude"+dlatitude);

	Log.e("position", "" + position);

		Location.setTag(regularDemandsDO);
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
	convertView.setTag(regularDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		RegularDemandsDO regularDemandsDO = (RegularDemandsDO) v.getTag();
		Intent intent = new Intent(mContext, GroupDetails_Centerwise.class);
		intent.putExtra("groupnumber", regularDemandsDO.GNo);
		((Activity) (mContext)).startActivityForResult(intent, 0);

	    }
	});
	return convertView;
    }

}
