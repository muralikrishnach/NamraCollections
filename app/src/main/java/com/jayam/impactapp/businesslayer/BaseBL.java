package com.jayam.impactapp.businesslayer;

import com.jayam.impactapp.common.DataListner;

import android.content.Context;

public abstract class BaseBL 
{
	public Context context;
	public DataListner listner;
	public BaseBL(Context context, DataListner listner)
	{
		this.context = context;
		this.listner = listner;
	}
}
