package com.jayam.impactapp.common;

public interface DataListner
{
	public  abstract void onDataretrieved(Object objs);
	public  abstract void onDataretrievalFailed(String errorMessage);
}
