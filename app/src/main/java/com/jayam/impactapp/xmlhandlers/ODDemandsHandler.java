package com.jayam.impactapp.xmlhandlers;

import android.content.Context;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.objects.ODDemandsDO;

public class ODDemandsHandler extends BaseHandler
{
	public Context context;
	private StringBuffer appendString;
	private Boolean currentElement = false;
	private ArrayList<ODDemandsDO> alDemandsDOs;
	private ODDemandsDO odDemandsDO;


	public ODDemandsHandler( Context context)
	{
		this.context=context;
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;
		appendString = new StringBuffer();
		
		if(localName.equalsIgnoreCase("NewDataSet"))
		{
			alDemandsDOs = new ArrayList<ODDemandsDO>();
		}
		else if(localName.equalsIgnoreCase("ODDemands"))
		{
			odDemandsDO = new ODDemandsDO();
		}
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		currentElement = false;
		
		if(localName.equalsIgnoreCase("MCI_Name"))
		{
			odDemandsDO.MCI_Name = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MCI_Code"))
		{
			odDemandsDO.MCI_Code = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MGI_Name"))
		{
			odDemandsDO.MGI_Name = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MGI_Code"))
		{
			odDemandsDO.MGI_Code = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MMI_Name"))
		{
			odDemandsDO.MMI_Name = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MMI_Code"))
		{
			odDemandsDO.MMI_Code = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MLAI_ID"))
		{
			odDemandsDO.MLAI_ID = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("ODAmt"))
		{
			odDemandsDO.ODAmt = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("DemandDate"))
		{
			odDemandsDO.DemandDate = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("OSAmt"))
		{
			if(appendString.toString() == null)
			{
				odDemandsDO.OSAmt = "0";
			}
			else
			{
				odDemandsDO.OSAmt = appendString.toString();
			}
			
		}

		else if(localName.equalsIgnoreCase("SittingOrder"))
		{
			odDemandsDO.SittingOrder = appendString.toString();
		}


		else if(localName.equalsIgnoreCase("primaryid"))
		{
			odDemandsDO.AAdharNo = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MMI_Phone"))
		{
			odDemandsDO.MobileNo = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("CollectionMode")) {
			odDemandsDO.BranchPaymode = appendString.toString();
		}

		else if(localName.equalsIgnoreCase("ODDemands"))
		{
			alDemandsDOs.add(odDemandsDO);
		}

		else if (localName.equalsIgnoreCase("ProductName")) {
			odDemandsDO.ProductName = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("MobileNumber")) {
			odDemandsDO.MobileNo = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("LatituteCenter")) {
			odDemandsDO.LatitudeCenter = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("LongituteCenter")) {
			odDemandsDO.LongitudeCenter = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("LatituteGroup")) {
			odDemandsDO.LatitudeGroup = appendString.toString();
		}

		else if (localName.equalsIgnoreCase("LongituteGroup")) {
			odDemandsDO.LongitudeGroup = appendString.toString();
		}

		else if (localName.equalsIgnoreCase("LatituteMember")) {
			odDemandsDO.LatitudeMember = appendString.toString();
		}

		else if (localName.equalsIgnoreCase("LongituteMember")) {
			odDemandsDO.LongitudeMember = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("NewDataSet"))
		{
			ODDemandsBL odDemandsBL = new ODDemandsBL();
			odDemandsBL.InsertArrayList(alDemandsDOs,this.context);
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
		return alDemandsDOs;
	}

	@Override
	public boolean getExecutionStatus()
	{
		if(alDemandsDOs != null && alDemandsDOs.size() > 0)
		return true;
		else return false;
	}

	@Override
	public String getErrorMessage()
	{
		return "error";
	}

}
