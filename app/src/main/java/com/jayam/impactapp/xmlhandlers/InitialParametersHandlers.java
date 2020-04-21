package com.jayam.impactapp.xmlhandlers;

import com.jayam.impactapp.objects.IntialParametrsDO;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class InitialParametersHandlers extends BaseHandler
{
	private StringBuffer appendString;
	private Boolean currentElement = false;
	private IntialParametrsDO intialParametrsDO;
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;
		appendString = new StringBuffer();
		
		if(localName.equalsIgnoreCase("User"))
		{
			intialParametrsDO = new IntialParametrsDO();
		}
	}
	

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		currentElement = false;
		if(localName.equalsIgnoreCase("WorkMode"))
		{
			intialParametrsDO.WorkMode = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("TerminalID"))
		{
			intialParametrsDO.TerminalID = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MACID"))
		{
			intialParametrsDO.MACID = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("ReceiptHeader"))
		{
			intialParametrsDO.ReceiptHeader = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("ReceiptFooter"))
		{
			intialParametrsDO.ReceiptFooter = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("PrinterBTAddress"))
		{
			intialParametrsDO.BTPrinterAddress = appendString.toString();
					
		}
		else if(localName.equalsIgnoreCase("AgentCopy"))
		{
			intialParametrsDO.AgentCopy = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("DaysOffSet"))
		{
			intialParametrsDO.DaysOffSet = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MeetingTime"))
		{
			intialParametrsDO.MeetingTime = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("TimeOut"))
		{
			intialParametrsDO.TimeOut = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("ServerUrl"))
		{
			intialParametrsDO.ServerUrl = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MaxTransactions"))
		{
			intialParametrsDO.MaxTransactions = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MaxAmount"))
		{
			intialParametrsDO.MaxAmount = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("Code"))
		{
			intialParametrsDO.Code = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("UserName"))
		{
			intialParametrsDO.UserName = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("LastTransactionID"))
		{
			intialParametrsDO.LastTransactionID = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("PartialPayment"))
		{
			intialParametrsDO.PartialPayment = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("AdvPayment"))
		{
			intialParametrsDO.AdvPayment = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("AdvDemandDwds"))
		{
			intialParametrsDO.AdvDemandDwds = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("MemberAttendance"))
		{
			intialParametrsDO.MemberAttendance = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("IndividualReceipts"))
		{
			intialParametrsDO.IndividualReceipts = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("GLI"))
		{
			intialParametrsDO.GLI = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("Lateness"))
		{
			intialParametrsDO.Lateness = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("GroupPhoto"))
		{
			intialParametrsDO.GroupPhoto = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("TxnCode"))
		{
			intialParametrsDO.LastTransactionCode = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("PrintType"))
		{
			intialParametrsDO.PrintType = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("InstRequired"))
		{
			intialParametrsDO.InstRequired = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("PhotoNos"))
		{
			intialParametrsDO.PhotoNos = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("ValidatePrinter"))
		{
			intialParametrsDO.ValidatePrinter = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("PrintValidate"))
		{
			intialParametrsDO.PrintValidate = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("LogoPrinting"))
		{
			intialParametrsDO.LogoPrinting = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("QOM"))
		{
			intialParametrsDO.qom = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("Problems_in_center"))
		{
			intialParametrsDO.probInCenter= appendString.toString();
		}
		else if(localName.equalsIgnoreCase("GroupDiscipline"))
		{
			intialParametrsDO.groupDiscipline = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("CollExperience"))
		{
			intialParametrsDO.collExp = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("CollExpinRMEL"))
		{
			intialParametrsDO.collExpRMEL = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("CollectionPlace"))
		{
			intialParametrsDO.collPlace = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("RepaymentMadeby"))
		{
			intialParametrsDO.repaymentMadeBy = appendString.toString();
		}


		else if(localName.equalsIgnoreCase("RMELUser"))
		{
			intialParametrsDO.rmelUser = appendString.toString();
		}

		else if(localName.equalsIgnoreCase("RMELUser"))
		{
			intialParametrsDO.rmelUser = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("UserId"))
		{
			intialParametrsDO.UserID = appendString.toString();
		}
		else if(localName.equalsIgnoreCase("branchName"))
		{
			intialParametrsDO.BranchName = appendString.toString();
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
		return intialParametrsDO;
	}

	@Override
	public boolean getExecutionStatus()
	{
		if(intialParametrsDO == null)
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
