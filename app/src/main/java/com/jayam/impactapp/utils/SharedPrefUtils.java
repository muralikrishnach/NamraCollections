package com.jayam.impactapp.utils;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.jayam.impactapp.objects.KeyValue;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils 
{
	public static final int DATA_TYPE_INT 	 = 101;
	public static final int DATA_TYPE_BOOL 	 = 102;
	public static final int DATA_TYPE_FLOAT  = 103;
	public static final int DATA_TYPE_STRING = 104;
	public static final int DATA_TYPE_LONG 	 = 105;
	
	/**
	 * This method returns Vector<KeyValue> which contains key-value pairs retrieved from SharedPreference
	 * @param ctx
	 * @param prefName
	 */
	public static Vector<KeyValue> getVales(Context ctx,String prefName)
	{
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		Map<String,?> keyMap 	= pref.getAll();
		Set<String> keySet 		=keyMap.keySet();
		
		Vector<KeyValue> vKeyValues = new Vector<KeyValue>();
		
		for(String key:keySet)
		{
			KeyValue value = new KeyValue();
			value.key 	= key;
			
			value.value = pref.getString(key, "");
			
			vKeyValues.add(value);
		}
		
		return vKeyValues;
	}
	/**
	 * This method returns value of a key in String type 
	 * @param ctx
	 * @param prefName
	 * @param key
	 */
	public static String getKeyValue(Context ctx, String prefName, String key)
	{
		String value = "";
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		value = pref.getString(key, "");
		
		return value;
	}
	/**
	 * This method returns value of a key in String type 
	 * @param ctx
	 * @param prefName
	 * @param key
	 * @param defaultValue
	 */
	public static String getKeyValue(Context ctx, String prefName, String key, String defaultValue)
	{
		String value = "";
		
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		value = pref.getString(key, defaultValue);
		
		if(value.equalsIgnoreCase(""))
			value = defaultValue;
		
		return value;
	}
	/**
	 * This method stores Values in SharePreference
	 * @param ctx
	 * @param prefName
	 * @param vecKeyValue
	 */
	public static void setValues(Context ctx,String prefName,Vector<KeyValue> vecKeyValue)
	{
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		for(int i=0;i<vecKeyValue.size();i++)
		{
			KeyValue value = vecKeyValue.get(i);
			switch(value.dataType)
			{
				case DATA_TYPE_INT:
								editor.putInt(value.key, Integer.parseInt(value.value));	
								break;
				case DATA_TYPE_BOOL: 
								editor.putBoolean(value.key, Boolean.parseBoolean(value.value));
								break;
				case DATA_TYPE_FLOAT: 
								editor.putFloat(value.key, Float.parseFloat(value.value));
								break;
				case DATA_TYPE_STRING: 
								editor.putString(value.key,value.value);
								break;
				case DATA_TYPE_LONG: 
								editor.putLong(value.key, Long.parseLong(value.value));
								break;
				default:editor.putString(value.key,value.value);
			}
		}
		
		editor.commit();
	}
	/**
	 * This method Values in SharePreference
	 * @param ctx
	 * @param prefName
	 * @param value
	 */
	public static void setValue(Context ctx,String prefName,KeyValue value)
	{
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		switch(value.dataType)
		{
			case DATA_TYPE_INT:
							editor.putInt(value.key, Integer.parseInt(value.value));	
							break;
			case DATA_TYPE_BOOL: 
							editor.putBoolean(value.key, Boolean.parseBoolean(value.value));
							break;
			case DATA_TYPE_FLOAT: 
							editor.putFloat(value.key, Float.parseFloat(value.value));
							break;
			case DATA_TYPE_STRING: 
							editor.putString(value.key,value.value);
							break;
			case DATA_TYPE_LONG: 
							editor.putLong(value.key, Long.parseLong(value.value));
							break;
			default:editor.putString(value.key,value.value);
		}
		
		editor.commit();
	}
	/**
	 * This method Clears the SharedPreference
	 * @param ctx
	 * @param prefName
	 */
	public static void clearValues(Context ctx,String prefName)
	{
		SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

}
