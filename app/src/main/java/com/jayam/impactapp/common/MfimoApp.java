package com.jayam.impactapp.common;

import android.app.Application;
import android.content.Context;

public class MfimoApp extends Application{

	public static Context ct;
	
	@Override
	public void onCreate() {
		super.onCreate();
		ct=getApplicationContext();
	}
	
	public static Context getAppContext(){
		return ct;
	}
}
