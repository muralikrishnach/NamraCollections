package com.jayam.impactapp.adapters;

import java.util.ArrayList;
import java.util.List;

import com.jayam.impactapp.CollExperianceCW;
import com.jayam.impactapp.GroupDisciplineCW;
import com.jayam.impactapp.Group_Members;
import com.jayam.impactapp.QomCenterWise;
import com.jayam.impactapp.R;
import com.jayam.impactapp.RenewCenterWise;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupsAdapter_Qom extends GenericAdapter {

    protected ArrayList<RegularDemandsDO> alregularDemandsBL;
    protected boolean isRenew;

    public GroupsAdapter_Qom(Context context, List<? extends BaseDO> listItems) {
	super(context, listItems);
	// TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
	TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);
	ImageView imgConfirmed = (ImageView) convertView.findViewById(R.id.imgConfirmed);
	RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	ArrayList<RegularDemandsDO> alregularDemandsBL1 = regularDemandsBL.SelectAll(regularDemandsDO.GNo, "Groups",mContext);
	// int count=0;
	// for(int i=0;i<alregularDemandsBL1.size();i++){
	// if(alregularDemandsBL1.get(i).Attendance.equalsIgnoreCase("1")){
	// count++;
	// }
	// }
	// Log.d("mfimo", "count"+alregularDemandsBL1.size()+"in
	// adapter"+regularDemandsBL.getCompletedCount(regularDemandsDO.GNo,
	// "group",
	// alregularDemandsBL1.get(0).Renew.equalsIgnoreCase("Y")?true:false));
	if (Integer.parseInt(regularDemandsBL.getCompletedCount(regularDemandsDO.GNo, "group",mContext)) == 0) {
	    imgConfirmed.setVisibility(View.VISIBLE);
	} else {
	    imgConfirmed.setVisibility(View.GONE);
	}
	// if(position > 0)
	// {
	// if(!regularDemandsDO.CNo.equalsIgnoreCase(((RegularDemandsDO)
	// getList().get(position-1)).CNo))
	// {
	// tvCenterName.setText(""+regularDemandsDO.CName);
	// }
	// }
	// else if(position == 0)
	// {
	tvCenterName.setText("" + regularDemandsDO.GroupName);
	// }

	Log.e("position", "" + position);
	convertView.setTag(regularDemandsDO);
	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// RegularDemandsDO regularDemandsDO = (RegularDemandsDO)
		// v.getTag();
		// Intent intent = new Intent(mContext, GroupDetails.class);
		// intent.putExtra("groupnumber", regularDemandsDO.GNo);
		// ((Activity)(mContext)).startActivityForResult(intent, 0);

		// show popup with all groupmembers
		// groupnumber

		/*
		 * String groupID = ((RegularDemandsDO) v.getTag()).GNo;
		 * RegularDemandsBL demandsBL = new RegularDemandsBL();
		 * ArrayList<RegularDemandsDO> alRegularDemandsDOs =
		 * demandsBL.SelectAll(groupID, "Groups");
		 * 
		 * showMembers(alRegularDemandsDOs);
		 */
		String gno = ((RegularDemandsDO) v.getTag()).GNo;
		RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		alregularDemandsBL = regularDemandsBL.SelectAll(gno, "Groups",mContext);
		for (int i = 0; i < alregularDemandsBL.size(); i++) {
		    if (alregularDemandsBL.get(i).Renew.equals("Y")) {
			isRenew = true;
			i = alregularDemandsBL.size();
		    }
		}
		IntialParametrsBL inpbl = new IntialParametrsBL();
		ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(mContext);
		if (alin.get(0).rmelUser.equalsIgnoreCase("N")) {
		    if (alin.get(0).qom.equalsIgnoreCase("Y")) {
			Intent intent = new Intent(mContext, QomCenterWise.class);
			intent.putExtra("groupnumber", ((RegularDemandsDO) v.getTag()).GNo);
			((Activity) (mContext)).startActivityForResult(intent, 12345);
		    } else if (alin.get(0).groupDiscipline.equalsIgnoreCase("1")) {
			Intent intent = new Intent(mContext, GroupDisciplineCW.class);
			intent.putExtra("groupnumber", ((RegularDemandsDO) v.getTag()).GNo);
			((Activity) (mContext)).startActivityForResult(intent, 12345);
		    } else if (alin.get(0).collExp.equalsIgnoreCase("1")) {
			Intent intent = new Intent(mContext, CollExperianceCW.class);
			intent.putExtra("groupnumber", ((RegularDemandsDO) v.getTag()).GNo);
			((Activity) (mContext)).startActivityForResult(intent, 12345);
		    } else if (isRenew) {
			Intent intent = new Intent(mContext, RenewCenterWise.class);
			intent.putExtra("groupnumber", ((RegularDemandsDO) v.getTag()).GNo);
			((Activity) (mContext)).startActivityForResult(intent, 12345);
		    }
		} else {
		    Intent intent = new Intent(mContext, Group_Members.class);
		    intent.putExtra("groupcode", ((RegularDemandsDO) v.getTag()).GNo);
		    intent.putExtra("type", "center");
		    ((Activity) (mContext)).startActivityForResult(intent, 12345);
		}
	    }
	});
	return convertView;
    }
}
