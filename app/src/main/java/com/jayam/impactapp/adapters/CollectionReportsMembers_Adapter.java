package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.CollectionReport_MemberSummery;
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

public class CollectionReportsMembers_Adapter extends GenericAdapter {
    public CollectionReportsMembers_Adapter(Context context, List<? extends BaseDO> listItems) {
	super(context, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
	if (convertView == null) {
	    convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	}
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);

	tvCenterName.setText("" + regularDemandsDO.GroupName);

	tvCenterName.setText("" + regularDemandsDO.MemberName + "- (" + regularDemandsDO.collectedAmount + ")");
	convertView.setTag(regularDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		RegularDemandsDO obj = (RegularDemandsDO) v.getTag();
		Intent intent = new Intent(mContext, CollectionReport_MemberSummery.class);
		intent.putExtra("MLAIID", obj.MLAI_ID);
		((Activity) (mContext)).startActivityForResult(intent, 1234);

	    }
	});
	return convertView;
    }

}
