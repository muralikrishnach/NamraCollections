package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.R;
import com.jayam.impactapp.RenewCenterWise;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupsAdapter_renew extends GenericAdapter {

    public GroupsAdapter_renew(Context context, List<? extends BaseDO> listItems) {
	super(context, listItems);
	// TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
	ImageView imgConfirmed = (ImageView) convertView.findViewById(R.id.imgConfirmed);
	imgConfirmed.setVisibility(View.GONE);

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

		Intent intent = new Intent(mContext, RenewCenterWise.class);
		intent.putExtra("groupnumber", ((RegularDemandsDO) v.getTag()).GNo);
		((Activity) (mContext)).startActivityForResult(intent, 12345);
	    }
	});
	return convertView;
    }
}
