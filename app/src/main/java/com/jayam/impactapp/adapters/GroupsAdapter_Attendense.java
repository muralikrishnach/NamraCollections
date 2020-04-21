package com.jayam.impactapp.adapters;

import java.util.ArrayList;
import java.util.List;

import com.jayam.impactapp.Attendense_Centerwise;
import com.jayam.impactapp.DisplayRoute;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.DialogUtils;


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

public class GroupsAdapter_Attendense extends GenericAdapter {
    private IntialParametrsDO intialParametrsDO;
    String isRmel;
	public String langitude, latitude;
	public	String dlatitude, dlangitude;
    public GroupsAdapter_Attendense(Context context, List<? extends BaseDO> listItems,
	    IntialParametrsDO intialParametrsDO,String latitude,String langitude) {
	super(context, listItems);
	this.intialParametrsDO = intialParametrsDO;
		this.langitude=langitude;
		this.latitude=latitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
	ImageView imgConfirmed = (ImageView) convertView.findViewById(R.id.imgConfirmed);
		ImageView Location = (ImageView) convertView.findViewById(R.id.location);

		TextView tvamt = (TextView) convertView.findViewById(R.id.tvdmamt);
		TextView tvdate = (TextView) convertView.findViewById(R.id.tvmdate);
	imgConfirmed.setVisibility(View.INVISIBLE);
		tvamt.setVisibility(View.INVISIBLE);
		tvdate.setVisibility(View.INVISIBLE);
		Location.setVisibility(View.INVISIBLE);

	// if(position > 0)
	// {
	// if(!regularDemandsDO.CNo.equalsIgnoreCase(((RegularDemandsDO)
	// getList().get(position-1)).CNo))
	// {
	// tvCenterName.setText(""+regularDemandsDO.CName);
	// }
	// }
	// else if(position == 0)
	// {
	tvCenterName.setText("" + regularDemandsDO.GroupName);
	// }



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
		// RegularDemandsDO regularDemandsDO = (RegularDemandsDO)
		// v.getTag();
		// Intent intent = new Intent(mContext, GroupDetails.class);
		// intent.putExtra("groupnumber", regularDemandsDO.GNo);
		// ((Activity)(mContext)).startActivityForResult(intent, 0);

		// show popup with all groupmembers
		// groupnumber

		/*
		 * String groupID = ((RegularDemandsDO) v.getTag()).GNo;
		 * RegularDemandsBL demandsBL = new RegularDemandsBL();
		 * ArrayList<RegularDemandsDO> alRegularDemandsDOs =
		 * demandsBL.SelectAll(groupID, "Groups");
		 * 
		 * showMembers(alRegularDemandsDOs);
		 */

		Intent intent = new Intent(mContext, Attendense_Centerwise.class);
		intent.putExtra("groupnumber", ((RegularDemandsDO) v.getTag()).GNo);
		intent.putExtra("att", "" + intialParametrsDO.MemberAttendance);
		intent.putExtra("gli", "" + intialParametrsDO.GLI);
		intent.putExtra("lat", "" + intialParametrsDO.Lateness);
		intent.putExtra("meetingtime", "" + intialParametrsDO.MeetingTime);
		intent.putExtra("groupphoto", "" + intialParametrsDO.GroupPhoto);
		((Activity) (mContext)).startActivityForResult(intent, 12345);
		//
	    }
	});
	return convertView;
    }

    public void showMembers(final ArrayList<RegularDemandsDO> alRegularDemandsDOs) {
	String[] memebers = new String[alRegularDemandsDOs.size()];
	boolean[] checkedItems = new boolean[alRegularDemandsDOs.size()];
	for (int i = 0; i < alRegularDemandsDOs.size(); i++) {
	    memebers[i] = alRegularDemandsDOs.get(i).MemberName;
	    checkedItems[i] = true;
	}
	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	builder.setTitle(alRegularDemandsDOs.get(0).GroupName + " - " + "Attendance");
	builder.setMultiChoiceItems(memebers, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		if (isChecked) {
		    alRegularDemandsDOs.get(which).Attendance = "1";
		} else {
		    alRegularDemandsDOs.get(which).Attendance = "0";
		}
	    }
	});

	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	    }
	});

	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	    }
	});

	builder.show();
    }

}
