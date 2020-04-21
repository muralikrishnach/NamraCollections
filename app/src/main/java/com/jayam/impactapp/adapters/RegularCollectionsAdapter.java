package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.Centers;
import com.jayam.impactapp.GroupsAndCenters;
import com.jayam.impactapp.R;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.SharedPrefUtils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class RegularCollectionsAdapter extends GenericAdapter {
    public RegularCollectionsAdapter(Context context, List<? extends BaseDO> listItems) {
	super(context, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);

	convertView = getLayoutInflater().inflate(R.layout.regularcollection_cell, null);
	TextView tvTvdates = (TextView) convertView.findViewById(R.id.tvdates);
	if (position > 0) {
	    RegularDemandsDO regularDemandsDO_previous = (RegularDemandsDO) getList().get(position);
	    if (!regularDemandsDO.DemandDate.equalsIgnoreCase(regularDemandsDO_previous.DemandDate)) {
		tvTvdates.setText(regularDemandsDO.DemandDate);
	    }
	} else if (position == 0) {
	    tvTvdates.setText(regularDemandsDO.DemandDate);
	}

	convertView.setTag(regularDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		RegularDemandsDO regularDemandsDO = (RegularDemandsDO) v.getTag();
		String URL = SharedPrefUtils.getKeyValue(mContext, AppConstants.pref_name, AppConstants.UrlAddress);
		Log.d("mfimo", URL);
		if (URL.equals("Yes")) {
		    Intent intent = new Intent(mContext, Centers.class);
		    intent.putExtra("type", "group");
		    intent.putExtra("date", regularDemandsDO.DemandDate);
		    ((Activity) (mContext)).startActivityForResult(intent, 0);
		} else {
		    Intent intent = new Intent(mContext, GroupsAndCenters.class);
		    intent.putExtra("date", regularDemandsDO.DemandDate);
		    ((Activity) (mContext)).startActivityForResult(intent, 0);
		}
	    }
	});
	return convertView;
    }

}
