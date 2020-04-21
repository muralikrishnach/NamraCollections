package com.jayam.impactapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtility 
{
	public static boolean isNetworkConnectionAvailable(Context context)
	{
	    boolean isNetworkConnectionAvailable = false;
	    
	    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    
	    if (activeNetworkInfo != null) 
	    {
	    	isNetworkConnectionAvailable = activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
	    }
	    
	    return isNetworkConnectionAvailable;
	}
}
