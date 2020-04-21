package com.jayam.impactapp.adapters;

import java.util.ArrayList;
import java.util.List;

import com.jayam.impactapp.CollExpRmel;
import com.jayam.impactapp.CollExperianceMW;
import com.jayam.impactapp.CollectionPlace;
import com.jayam.impactapp.QomMemberWise;
import com.jayam.impactapp.R;
import com.jayam.impactapp.RepaymentMadeBy;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Group_members_Adapter extends GenericAdapter {
    RegularDemandsBL rbl;

    public Group_members_Adapter(Context context, List<? extends BaseDO> listItems) {
	super(context, listItems);
	rbl = new RegularDemandsBL();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
	if (convertView == null) {
	    convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	}
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
	ImageView imgConfirmed = (ImageView) convertView.findViewById(R.id.imgConfirmed);
	RegularDemandsBL bl = new RegularDemandsBL();
	if (Integer.parseInt(bl.getCompletedCount(regularDemandsDO.MLAI_ID, "member",mContext)) == 0) {
	    imgConfirmed.setVisibility(View.VISIBLE);
	} else {
	    imgConfirmed.setVisibility(View.INVISIBLE);
	}
	tvCenterName.setText("" + regularDemandsDO.GroupName);

	tvCenterName.setText("" + regularDemandsDO.MemberName + "- (" + regularDemandsDO.MLAI_ID + ")");
	convertView.setTag(regularDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		IntialParametrsBL inpbl = new IntialParametrsBL();
		ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(mContext);
		RegularDemandsDO obj = (RegularDemandsDO) v.getTag();
		if (alin.get(0).qom.equalsIgnoreCase("Y") && rbl.getMemberAtt(obj.MLAI_ID,mContext).equals("1")) {
		    Intent intent = new Intent(mContext, QomMemberWise.class);
		    intent.putExtra("memcode", obj.MLAI_ID);
		    intent.putExtra("memname", obj.MemberName);
		    ((Activity) (mContext)).startActivityForResult(intent, 1234);
		} else if (alin.get(0).collExpRMEL.equalsIgnoreCase("1")) {
		    Intent intent = new Intent(mContext, CollExpRmel.class);
		    intent.putExtra("memcode", obj.MLAI_ID);
		    intent.putExtra("memname", obj.MemberName);
		    ((Activity) (mContext)).startActivityForResult(intent, 1234);
		} else if (alin.get(0).collPlace.equalsIgnoreCase("1")) {
		    Intent intent = new Intent(mContext, CollectionPlace.class);
		    intent.putExtra("memcode", obj.MLAI_ID);
		    intent.putExtra("memname", obj.MemberName);
		    ((Activity) (mContext)).startActivityForResult(intent, 1234);
		} else if (alin.get(0).collExp.equalsIgnoreCase("1")) {
		    Intent intent = new Intent(mContext, CollExperianceMW.class);
		    intent.putExtra("memcode", obj.MLAI_ID);
		    intent.putExtra("memname", obj.MemberName);
		    ((Activity) (mContext)).startActivityForResult(intent, 1234);
		} else if (alin.get(0).repaymentMadeBy.equalsIgnoreCase("1")) {
		    Intent intent = new Intent(mContext, RepaymentMadeBy.class);
		    intent.putExtra("memcode", obj.MLAI_ID);
		    intent.putExtra("memname", obj.MemberName);
		    ((Activity) (mContext)).startActivityForResult(intent, 123456);
		}
	    }
	});
	return convertView;
    }

}
