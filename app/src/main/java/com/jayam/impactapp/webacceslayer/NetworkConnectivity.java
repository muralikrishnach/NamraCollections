package com.jayam.impactapp.webacceslayer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectivity {


	Context ct;
	ConnectivityManager cm;
	NetworkInfo networkInfo;
	public NetworkConnectivity(Context ct){
		this.ct=ct;
	}
	
	public boolean haveNetworkConnection() {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	  cm  = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}

	public String networkName(){
		 cm  = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
		 networkInfo = cm.getActiveNetworkInfo();
		 
		 if (networkInfo != null && networkInfo.isConnected()) {
			 if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
				 return "wifi";
			 else 
				 return "mobile";
		}else
			return "not";
	}
	


}
