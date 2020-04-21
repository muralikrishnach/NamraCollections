package com.jayam.impactapp.adapters;

import java.util.ArrayList;
import java.util.List;

import com.jayam.impactapp.R;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AttendenseAdapter extends GenericAdapter {
    private boolean selectAll = true;
    private String att, gli, lat;
    private int attendense;

    public AttendenseAdapter(Context context, List<? extends BaseDO> listItems) {
	super(context, listItems);
    }

    public AttendenseAdapter(Context context, List<? extends BaseDO> listItems, String att, String gli, String lat) {
	this(context, listItems);
	this.att = att;
	this.gli = gli;
	this.lat = lat;
	this.attendense = getList().size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	final RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
	convertView = getLayoutInflater().inflate(R.layout.attendence_cell, null);
	TextView tvmemebrName = (TextView) convertView.findViewById(R.id.tvmemebrName);
	final CheckBox cbMember = (CheckBox) convertView.findViewById(R.id.cbMember);
	CheckBox cbMember_gli = (CheckBox) convertView.findViewById(R.id.cbMember_gli);
	final CheckBox cbMember_lateness = (CheckBox) convertView.findViewById(R.id.cbMember_lateness);
	tvmemebrName.setText("" + regularDemandsDO.MemberName + " (" + regularDemandsDO.MemberCode + ")");
	if (!regularDemandsDO.FTODID.equals("1")) {
	    regularDemandsDO.Attendance = "1";
	} else {
	    regularDemandsDO.Attendance = "0";
	}
	regularDemandsDO.GLI = "0";
	regularDemandsDO.Lateness = "0";
	if (att.equalsIgnoreCase("1")) {
	    cbMember.setVisibility(View.VISIBLE);
	    ((LinearLayout) cbMember.getParent()).setVisibility(View.VISIBLE);
	} else {
	    cbMember.setVisibility(View.GONE);
	    ((LinearLayout) cbMember.getParent()).setVisibility(View.GONE);
	}
	if (regularDemandsDO.attended == true && !regularDemandsDO.FTODID.equals("1")) {
	    cbMember.setChecked(true);
	} else {
	    cbMember.setChecked(false);
	}
	if (gli.equalsIgnoreCase("1")) {
	    cbMember_gli.setVisibility(View.VISIBLE);
	    ((LinearLayout) cbMember_gli.getParent()).setVisibility(View.VISIBLE);
	} else {
	    cbMember_gli.setVisibility(View.GONE);
	    ((LinearLayout) cbMember_gli.getParent()).setVisibility(View.GONE);
	}
	if(regularDemandsDO.gli)
		cbMember_gli.setChecked(true);
	else
		cbMember_gli.setChecked(false);
	if (lat.equalsIgnoreCase("1")) {
	    cbMember_lateness.setVisibility(View.VISIBLE);
	    ((LinearLayout) cbMember_lateness.getParent()).setVisibility(View.VISIBLE);
	} else {
	    cbMember_lateness.setVisibility(View.GONE);
	    ((LinearLayout) cbMember_lateness.getParent()).setVisibility(View.GONE);
	}
	Log.d("mfimo", regularDemandsDO.Lateness);
	if(regularDemandsDO.late)
		cbMember_lateness.setChecked(true);
	else
		cbMember_lateness.setChecked(false);
	cbMember.setTag(regularDemandsDO);
	cbMember.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		RegularDemandsDO regularDemandsDO = (RegularDemandsDO) buttonView.getTag();
		if (!(regularDemandsDO.FTODID.equals("1"))) {
		    if (isChecked) {
			cbMember_lateness.setClickable(true);
			attendense = attendense + 1;
			regularDemandsDO.Attendance = "1";
			regularDemandsDO.attended = true;
		    } else {
			cbMember_lateness.setChecked(false);
			cbMember_lateness.setClickable(false);
			attendense = attendense - 1;
			regularDemandsDO.Attendance = "0";
			regularDemandsDO.attended = false;
		    }
		} else {
		    cbMember.setChecked(false);
		    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		    builder.setTitle("Attendance should not capture for death marking Member(s)");
		    regularDemandsDO.Attendance = "0";
		    regularDemandsDO.attended = false;
		    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			}
		    });
		    builder.show();
		}
	    }
	});
	cbMember_gli.setTag(regularDemandsDO);
	cbMember_gli.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		RegularDemandsDO regularDemandsDO = (RegularDemandsDO) buttonView.getTag();
		if (isChecked) {
		    regularDemandsDO.GLI = "1";
			regularDemandsDO.gli=true;
		} else {
		    regularDemandsDO.GLI = "0";
			regularDemandsDO.gli=false;
		}

	    }
	});
	cbMember_lateness.setTag(regularDemandsDO);
	cbMember_lateness.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		RegularDemandsDO regularDemandsDO = (RegularDemandsDO) buttonView.getTag();
		if (isChecked) {
		    regularDemandsDO.Lateness = "1";
		    regularDemandsDO.late=true;
		} else {
		    regularDemandsDO.Lateness = "0";
		    regularDemandsDO.late=false;
		}

	    }
	});
	return convertView;
    }

    public void selectAll() {
	this.selectAll = true;
	this.notifyDataSetChanged();
    }

    public void deSelectAll() {
	this.selectAll = false;
	this.notifyDataSetChanged();
    }

    public int getSelectedItems() {
	return attendense;
    }

    public ArrayList<RegularDemandsDO> getUpDatedList() {

	return (ArrayList<RegularDemandsDO>) getList();
    }

}
