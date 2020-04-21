package com.jayam.impactapp.database;

import com.jayam.impactapp.objects.BaseDO;

import java.util.ArrayList;

public abstract class BaseBusinessLayer<T>
{
	public String apstorphe = "'";
	public String sep = ",";
	public abstract boolean Insert(BaseDO object);
	public abstract boolean Update(BaseDO object);
	public abstract boolean Delete(BaseDO object);
	public abstract ArrayList<T> SelectAll();
	
	public String Obj2String(Object obj)
	{
		try
		{
			if(obj == null)
				return "";
			else
				return obj.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	public int Obj2Int(Object obj)
	{
		try
		{
			if(obj == null)
				return 0;
			else
				return Integer.parseInt(obj.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public Double Obj2Double(Object obj)
	{
		try
		{
			if(obj == null)
				return 0d;
			else
				return Double.parseDouble(obj.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0d;
		}
	}
	public boolean Obj2Boolean(Object obj)
	{
		try
		{
			if(obj == null)
				return false;
			else
				return Boolean.parseBoolean(obj.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}


}
