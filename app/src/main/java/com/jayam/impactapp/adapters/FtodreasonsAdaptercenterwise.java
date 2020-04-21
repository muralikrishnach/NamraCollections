package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.FtodCenterWiseConfirmation;
import com.jayam.impactapp.FtodGroupWiseConfirmation;
import com.jayam.impactapp.R;
import com.jayam.impactapp.database.FtodreasonsBL;
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

public class FtodreasonsAdaptercenterwise extends GenericAdapter {

    public Context context;
    String CenCode, type;

    public FtodreasonsAdaptercenterwise(Context context, List<? extends BaseDO> listItems, String CCode,
	    String TxnType) {
	super(context, listItems);
	this.CenCode = CCode;
	this.type = TxnType;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	FtodreasonsDO regularDemandsDO = (FtodreasonsDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
	ImageView imgConfirmed = (ImageView) convertView.findViewById(R.id.imgConfirmed);

		TextView tvamt = (TextView) convertView.findViewById(R.id.tvdmamt);
		TextView tvdate = (TextView) convertView.findViewById(R.id.tvmdate);
		ImageView location = (ImageView) convertView.findViewById(R.id.location);

		tvamt.setVisibility(View.INVISIBLE);
		tvdate.setVisibility(View.INVISIBLE);
		location.setVisibility(View.INVISIBLE);
	FtodreasonsBL bl = new FtodreasonsBL();

	tvCenterName.setText("" + regularDemandsDO.FTODReason);

	Log.e("position", "" + position);
	convertView.setTag(regularDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (type.equals("Center")) {
		    FtodreasonsDO regularDemandsDO = (FtodreasonsDO) v.getTag();
		    Intent intent = new Intent(mContext, FtodCenterWiseConfirmation.class);
		    intent.putExtra("ReasonID", regularDemandsDO.ID);
		    intent.putExtra("Code", CenCode);
		    ((Activity) (mContext)).startActivityForResult(intent, 0);
		} else {
		    FtodreasonsDO regularDemandsDO = (FtodreasonsDO) v.getTag();
		    Intent intent = new Intent(mContext, FtodGroupWiseConfirmation.class);
		    intent.putExtra("ReasonID", regularDemandsDO.ID);
		    intent.putExtra("Code", CenCode);
		    ((Activity) (mContext)).startActivityForResult(intent, 0);
		}
	    }

	});
	return convertView;
    }

}
