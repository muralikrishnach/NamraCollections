package com.jayam.impactapp.xmlparser;

import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class BaseParser extends DefaultHandler
{
	public String sb2String(StringBuffer sb)
	{
		if(sb == null)
			return "";
		try
		{
			return sb.toString().trim();
		}
		catch(Exception e)
		{
	   		Log.e(this.getClass().getName(), "sb2String exception:"+e.getMessage() );
		}
		return null;
	}
	
	public int sb2Int(StringBuffer sb)
	{
		if(sb == null)
			return 0;
		try
		{
			return Integer.parseInt(sb.toString().trim());
		}
		catch(Exception e)
		{
	   		Log.e(this.getClass().getName(), "sb2Int exception:"+e.getMessage() );
		}
		return 0;
	}
}
