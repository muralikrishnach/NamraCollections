package com.jayam.impactapp.adapters;

import java.util.ArrayList;
import java.util.List;

import com.jayam.impactapp.AdvanceReport_GroupsSummary;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdvanceReport_Groups_Adapter extends GenericAdapter {
    private ArrayList<AdvaceDemandDO> alArrayList;

    public AdvanceReport_Groups_Adapter(Context context, List<? extends BaseDO> listItems,
	    ArrayList<AdvaceDemandDO> alArrayList) {
	super(context, listItems);
	this.alArrayList = alArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	AdvaceDemandDO advanceDemandsDO = (AdvaceDemandDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);

	tvCenterName.setText("" + advanceDemandsDO.MGI_Name);

	for (int i = 0; i < alArrayList.size(); i++) {
	    String collctdAmt = advanceDemandsDO.previousAmt;
	    tvCenterName.setText(advanceDemandsDO.MGI_Name + " - (" + collctdAmt + ")");
	}
	convertView.setTag(advanceDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		AdvaceDemandDO obj = (AdvaceDemandDO) v.getTag();
		Intent intent = new Intent(mContext, AdvanceReport_GroupsSummary.class);
		intent.putExtra("groupname", obj.MGI_Name);
		intent.putExtra("groupcode", obj.MGI_Code);
		// intent.putExtra("MemberCode",obj.MMI_Code);

		((Activity) (mContext)).startActivityForResult(intent, 1234);

	    }
	});
	return convertView;
    }

}
