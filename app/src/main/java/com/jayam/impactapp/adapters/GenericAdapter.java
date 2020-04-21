package com.jayam.impactapp.adapters;

import java.util.List;

import com.jayam.impactapp.objects.BaseDO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class GenericAdapter extends BaseAdapter {
    private List<? extends BaseDO> mList;
    private View mEmptyView;
    protected Context mContext;
    private LayoutInflater mLayoutInflater;

    public GenericAdapter(Context context, List<? extends BaseDO> listItems) {
	mList = listItems;
	mContext = context;
	mEmptyView = new View(context);

	mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
	if (mList != null) {
	    return mList.size();
	}

	return 0;
    }

    @Override
    public Object getItem(int position) {
	return position;
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	return mEmptyView;
    }

    public void refresh(List<? extends BaseDO> list) {
	mList = list;
	notifyDataSetChanged();
    }

    public LayoutInflater getLayoutInflater() {
	return mLayoutInflater;
    }

    public List<? extends BaseDO> getList() {
	return mList;
    }

}
