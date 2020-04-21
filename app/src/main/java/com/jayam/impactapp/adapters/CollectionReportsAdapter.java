package com.jayam.impactapp.adapters;

import java.util.ArrayList;
import java.util.List;

import com.jayam.impactapp.CollectionReportSummary;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class CollectionReportsAdapter extends GenericAdapter {
    private String type;
    private ArrayList<RegularDemandsDO> alTransations;
    float collctdAmt = 0;

    public CollectionReportsAdapter(Context context, List<? extends BaseDO> listItems,
	    ArrayList<RegularDemandsDO> alTransations) {
	super(context, listItems);
	this.alTransations = alTransations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
	Log.e("position", "" + position);

	tvCenterName.setText(regularDemandsDO.CName);
	for (int i = 0; i < alTransations.size(); i++) {
	    if (regularDemandsDO.CNo.equalsIgnoreCase(alTransations.get(i).CNo)) {
		collctdAmt = Float.valueOf(alTransations.get(i).collectedAmount).floatValue();
		tvCenterName.setText(regularDemandsDO.CName + " - (" + collctdAmt + ")");
		regularDemandsDO.collectedAmount = collctdAmt + "";
	    } else {
		// regularDemandsDO.collectedAmount = "0";
	    }
	}

	convertView.setTag(regularDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		RegularDemandsDO regularDemandsDO = (RegularDemandsDO) v.getTag();
		// if(!regularDemandsDO.collectedAmount.equalsIgnoreCase("0.0"))
		{
		    Intent intent = new Intent(mContext, CollectionReportSummary.class);
		    intent.putExtra("CNO", regularDemandsDO.CNo);
		    intent.putExtra("CollectedAmt", regularDemandsDO.collectedAmount);
		    ((Activity) (mContext)).startActivityForResult(intent, 0);
		}
	    }
	});
	return convertView;
    }

}
