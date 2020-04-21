package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.Ftodconfirmation;
import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.FtodreasonsDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FtodreasonsAdapter extends GenericAdapter {
    String MLAID, TxnType;

    public FtodreasonsAdapter(Context context, List<? extends BaseDO> listItems, String MCode, String Type) {
	super(context, listItems);
	this.MLAID = MCode;
	this.TxnType = Type;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	FtodreasonsDO regularDemandsDO = (FtodreasonsDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);

		TextView tvamt = (TextView) convertView.findViewById(R.id.tvdmamt);
		TextView tvdate = (TextView) convertView.findViewById(R.id.tvmdate);
		ImageView location = (ImageView) convertView.findViewById(R.id.location);

		tvamt.setVisibility(View.INVISIBLE);
		tvdate.setVisibility(View.INVISIBLE);
		location.setVisibility(View.INVISIBLE);


	tvCenterName.setText("" + regularDemandsDO.FTODReason);

	Log.e("position", "" + position);
	convertView.setTag(regularDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {

		FtodreasonsDO regularDemandsDO = (FtodreasonsDO) v.getTag();
		Intent intent = new Intent(mContext, Ftodconfirmation.class);
		intent.putExtra("ReasonID", regularDemandsDO.ID);
		intent.putExtra("MLAID", MLAID);
		intent.putExtra("TxnType", TxnType);
		((Activity) (mContext)).startActivityForResult(intent, 0);
	    }

	});
	return convertView;
    }

}
