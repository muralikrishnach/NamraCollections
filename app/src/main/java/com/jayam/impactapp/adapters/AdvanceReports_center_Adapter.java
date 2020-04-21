package com.jayam.impactapp.adapters;

import java.util.ArrayList;
import java.util.List;

import com.jayam.impactapp.Advancereports_CenterDetails;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdvanceReports_center_Adapter extends GenericAdapter {

    private ArrayList<AdvaceDemandDO> alTransations;
    float collctdAmt = 0;
    String CenterName;

    public AdvanceReports_center_Adapter(Context context, List<? extends BaseDO> listItems,
	    ArrayList<AdvaceDemandDO> alTransations) {
	super(context, listItems);
	this.alTransations = alTransations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	AdvaceDemandDO advaceDemandDO = (AdvaceDemandDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
	Log.e("position", "" + position);

	tvCenterName.setText("" + advaceDemandDO.CenterName);

	for (int i = 0; i < alTransations.size(); i++) {
	    String collctdAmt = advaceDemandDO.previousAmt;
	    tvCenterName.setText(advaceDemandDO.CenterName + " - (" + collctdAmt + ")");
	}

	convertView.setTag(advaceDemandDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		AdvaceDemandDO advaceDemandDO = (AdvaceDemandDO) v.getTag();
		{
		    Intent intent = new Intent(mContext, Advancereports_CenterDetails.class);
		    intent.putExtra("CenterName", advaceDemandDO.CenterName);
		    intent.putExtra("CenterCode", advaceDemandDO.MCI_Code);
		    intent.putExtra("CollectedAmt", advaceDemandDO.previousAmt);
		    ((Activity) (mContext)).startActivity(intent);
		}
	    }
	});
	return convertView;
    }

}
