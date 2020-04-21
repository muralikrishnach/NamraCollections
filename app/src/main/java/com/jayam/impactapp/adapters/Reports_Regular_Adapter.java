package com.jayam.impactapp.adapters;

import java.util.ArrayList;

import com.jayam.impactapp.R;
import com.jayam.impactapp.ReportList;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Reports_Regular_Adapter extends BaseAdapter {
    private ArrayList<String> alArrayList;
    private Context context;

    public Reports_Regular_Adapter(Context context, ArrayList<String> alArrayList) {
	this.context = context;
	this.alArrayList = alArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	String stringDate = alArrayList.get(position);
	convertView = LayoutInflater.from(context).inflate(R.layout.regularcollection_cell, null);
	TextView tvTvdates = (TextView) convertView.findViewById(R.id.tvdates);
	tvTvdates.setText(stringDate);

	convertView.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(context, ReportList.class);
		((Activity) (context)).startActivityForResult(intent, 0);
	    }
	});
	return convertView;
    }

    @Override
    public int getCount() {
	if (alArrayList != null && alArrayList.size() > 0) {
	    return alArrayList.size();
	} else {
	    return 0;
	}

    }

    @Override
    public Object getItem(int arg0) {
	return arg0;
    }

    @Override
    public long getItemId(int arg0) {
	return arg0;
    }

}
