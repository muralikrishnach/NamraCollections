package com.jayam.impactapp.common;

import com.jayam.impactapp.objects.PrintDetailsDO;

public class PrintValues 
{
	PrintDetailsDO detailsDO;
	
	public PrintValues()
	{
		detailsDO = new PrintDetailsDO();
	}
	public void add(String lable, String Value)
	{
		detailsDO.allabels.add(lable);
		detailsDO.alValues.add(Value);
	}
	
	public PrintDetailsDO getDetailObj()
	{
		return detailsDO;
	}
}
