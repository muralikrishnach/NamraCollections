package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.NewDemands;
import com.jayam.impactapp.R;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.DemandDateDO;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DemandDatesAdapter extends GenericAdapter {
    private RegularDemandsBL regularDemandsBL;

    public DemandDatesAdapter(Context context, List<? extends BaseDO> listItems) {
	super(context, listItems);
	regularDemandsBL = new RegularDemandsBL();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	DemandDateDO date = (DemandDateDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.demanddates_cell, null);
	TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
	ImageView ivStatus = (ImageView) convertView.findViewById(R.id.ivStatus);

	if (!regularDemandsBL.isAvailable(date.date,mContext)) {
	    ivStatus.setImageResource(R.drawable.notdownloaded);
	} else {
	    ivStatus.setImageResource(R.drawable.downloaded);
	}
	tvDate.setText(date.date);
	convertView.setTag(date);

	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		DemandDateDO date = (DemandDateDO) v.getTag();

		if (!regularDemandsBL.isAvailable(date.date,mContext)) {
		    Intent intent = new Intent(mContext, NewDemands.class);
		    intent.putExtra("Date", date.date);
		    ((Activity) mContext).startActivityForResult(intent, 123);
		} else {
		    showAlertDailog("Demands are already downloaded");
		}

	    }
	});
	return convertView;
    }

    public void showAlertDailog(String Message) {
	AlertDialog.Builder builder;
	builder = new AlertDialog.Builder(mContext);
	builder.setMessage(Message);
	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	    }
	}).create().show();
    }
}
