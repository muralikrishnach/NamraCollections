package com.jayam.impactapp.adapters;

import java.util.ArrayList;
import java.util.List;

import com.jayam.impactapp.CollectionReportSummary_Groups;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class CollectionReport_Groups_Adapter extends GenericAdapter {
    private ArrayList<RegularDemandsDO> alArrayList;

    public CollectionReport_Groups_Adapter(Context context, List<? extends BaseDO> listItems,
	    ArrayList<RegularDemandsDO> alArrayList) {
	super(context, listItems);
	this.alArrayList = alArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
	if (convertView == null) {
	    convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	}
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);

	tvCenterName.setText("" + regularDemandsDO.GroupName);

	for (RegularDemandsDO obj : alArrayList) {
	    if (regularDemandsDO.GNo.equalsIgnoreCase(obj.GNo)) {
		tvCenterName.setText("" + regularDemandsDO.GroupName + "- (" + obj.collectedAmount + ")");
	    }
	}
	convertView.setTag(regularDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		RegularDemandsDO obj = (RegularDemandsDO) v.getTag();
		Intent intent = new Intent(mContext, CollectionReportSummary_Groups.class);
		intent.putExtra("groupcode", obj.GNo);
		((Activity) (mContext)).startActivityForResult(intent, 1234);

	    }
	});
	return convertView;
    }

}
