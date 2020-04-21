package com.jayam.impactapp.xmlhandlers;

import android.content.Context;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jayam.impactapp.database.FtodreasonsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.FtodreasonsDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;

public class FtodreasonsHandler extends BaseHandler
{
	public Context context;
	private StringBuffer appendString;
	private Boolean currentElement = false;
	private ArrayList<FtodreasonsDO> alFTODDOS;
	private FtodreasonsDO ftodreasonsDO;
	FtodreasonsBL ftodreasonsBL;

	public FtodreasonsHandler(Context context)
	{
		this.context=context;
	}

	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;
		appendString = new StringBuffer();
		ftodreasonsBL = new FtodreasonsBL();
			
		if(localName.equalsIgnoreCase("NewDataSet"))
		{
			alFTODDOS		=	new ArrayList<FtodreasonsDO>();
		}
		else if(localName.equalsIgnoreCase("FTODs"))
		{
			ftodreasonsDO = new FtodreasonsDO();
		}
	}
	

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		currentElement = false;
		if(localName.equalsIgnoreCase("ID"))
		{
			ftodreasonsDO.ID = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("FTODReason"))
		{
			ftodreasonsDO.FTODReason = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("FTODs"))
		{
			ftodreasonsBL.Insert(ftodreasonsDO,this.context);
		}
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		if(currentElement)
		{
			appendString.append(ch, start, length);
		}
	}
	@Override
	public Object getData()
	{
		return ftodreasonsDO;
	}

	@Override
	public boolean getExecutionStatus()
	{
		if(ftodreasonsDO == null)
			return false;
		else
			return true;
	}

	@Override
	public String getErrorMessage() 
	{
		return "Error while parsing";
	}

}
