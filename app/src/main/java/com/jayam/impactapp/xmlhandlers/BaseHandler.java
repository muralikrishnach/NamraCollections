package com.jayam.impactapp.xmlhandlers;

import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;


public abstract class BaseHandler extends DefaultHandler 
{
public abstract Object getData();
	
	public abstract boolean getExecutionStatus();
	
	public abstract String getErrorMessage();
	
	public int sb2Int(StringBuffer sb)
	{
		if (sb==null) 
			return 0;
		
		return string2Int(sb.toString());
	}

	public int string2Int(String string)
	{
		int result = 0;
		if (string != null && string.length() > 0)
		{
			try
			{
				result = Integer.parseInt(string);
			}
			catch(Exception e)
			{
		   		Log.e(this.getClass().getName(), "string2Int exception:"+e.getMessage() );
			}
		}
		return result;
	}

}
