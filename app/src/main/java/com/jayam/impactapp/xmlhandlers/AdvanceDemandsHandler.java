package com.jayam.impactapp.xmlhandlers;

import android.content.Context;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;


public class AdvanceDemandsHandler extends BaseHandler
{
	public Context context;
	private StringBuffer appendString;
	private Boolean currentElement = false;
	private ArrayList<AdvaceDemandDO> alDemandsDOs;
	private AdvaceDemandDO advDemandsDO;

	public AdvanceDemandsHandler( Context context)
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
			alDemandsDOs = new ArrayList<AdvaceDemandDO>();
		}
		else if(localName.equalsIgnoreCase("ADVDemands"))
		{
			advDemandsDO = new AdvaceDemandDO();
		}
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		currentElement = false;
		
		if(localName.equalsIgnoreCase("CenterName"))
		{
			advDemandsDO.CenterName = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MCI_Code"))
		{
			advDemandsDO.MCI_Code = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MGI_Name"))
		{
			advDemandsDO.MGI_Name = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MGI_Code"))
		{
			advDemandsDO.MGI_Code = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MMI_Name"))
		{
			advDemandsDO.MMI_Name = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MMI_Code"))
		{
			advDemandsDO.MMI_Code = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MLAI_ID"))
		{
			advDemandsDO.MLAI_ID = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("OS"))
		{
			advDemandsDO.OS = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("OSAmt"))
		{
			advDemandsDO.OSAmt = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("DemandDate"))
		{
			advDemandsDO.DemandDate = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("SO"))
		{
			advDemandsDO.SO = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("RNo"))
		{
			advDemandsDO.RNo = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("CenterCnt"))
		{
			advDemandsDO.CenterCnt = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("OSInt"))
		{
			advDemandsDO.OSInt = appendString.toString();
		}


		else if(localName.equalsIgnoreCase("primaryid"))
		{
			advDemandsDO.AAdharNo = appendString.toString();
		}

		else if(localName.equalsIgnoreCase("MMI_Phone"))
		{
			advDemandsDO.MobileNo = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("CollectionMode")) {
			advDemandsDO.BranchPaymode = appendString.toString();
		}

		else if (localName.equalsIgnoreCase("ProductName")) {
			advDemandsDO.ProductName = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("MobileNumber")) {
			advDemandsDO.MobileNo = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("LatituteCenter")) {
			advDemandsDO.LatitudeCenter = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("LongituteCenter")) {
			advDemandsDO.LongitudeCenter = appendString.toString();
		}
		else if (localName.equalsIgnoreCase("LatituteGroup")) {
			advDemandsDO.LatitudeGroup = appendString.toString();
		}

		else if (localName.equalsIgnoreCase("LongituteGroup")) {
			advDemandsDO.LongitudeGroup = appendString.toString();
		}

		else if (localName.equalsIgnoreCase("LatituteMember")) {
			advDemandsDO.LatitudeMember = appendString.toString();
		}

		else if (localName.equalsIgnoreCase("LongituteMember")) {
			advDemandsDO.LongitudeMember = appendString.toString();
		}


		else if(localName.equalsIgnoreCase("ADVDemands"))
		{
			alDemandsDOs.add(advDemandsDO);
		}
		else if(localName.equalsIgnoreCase("NewDataSet"))
		{
			AdvanceDemandBL advDemandsBL = new AdvanceDemandBL();
			advDemandsBL.InsertArrayList(alDemandsDOs,this.context);
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
