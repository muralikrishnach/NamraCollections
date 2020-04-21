package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.CollectionReport_MemberDetails_OD;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.ODDemandsDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class CollectionReportFor_MemberDetails_OD_Adapter extends GenericAdapter {

    public CollectionReportFor_MemberDetails_OD_Adapter(Context context, List<? extends BaseDO> listItems) {
	super(context, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ODDemandsDO odDemandsDO = (ODDemandsDO) getList().get(position);
	if (convertView == null) {
	    convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	}
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);

	tvCenterName.setText("" + odDemandsDO.MMI_Name + "-" + odDemandsDO.MLAI_ID + "-" + odDemandsDO.collectedAmt);

	convertView.setTag(odDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		ODDemandsDO obj = (ODDemandsDO) v.getTag();
		Intent intent = new Intent(mContext, CollectionReport_MemberDetails_OD.class);
		intent.putExtra("mlai_id", obj.MLAI_ID);
		((Activity) (mContext)).startActivityForResult(intent, 1234);
	    }
	});
	return convertView;
    }

}
