package com.jayam.impactapp.xmlhandlers;

import android.content.Context;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.objects.RegularDemandsDO;

public class RegularDemandsHandler extends BaseHandler {
	public Context context;
    private StringBuffer appendString;
    private Boolean currentElement = false;
    private ArrayList<RegularDemandsDO> alRegularDemandsDOs;
    private RegularDemandsDO regularDemandsDO;
    RegularDemandsBL regularDemandsBL;
    RegularDemandsBLTemp regularDemandsBLTemp;

	public RegularDemandsHandler( Context context)
	{
		this.context=context;
	}

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	currentElement = true;
	appendString = new StringBuffer();
	regularDemandsBL = new RegularDemandsBL();
	regularDemandsBLTemp = new RegularDemandsBLTemp();
	if (localName.equalsIgnoreCase("NewDataSet")) {
	    alRegularDemandsDOs = new ArrayList<RegularDemandsDO>();
	} else if (localName.equalsIgnoreCase("RegDemands")) {
	    regularDemandsDO = new RegularDemandsDO();
	}
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
	currentElement = false;
	if (localName.equalsIgnoreCase("CNo")) {
	    regularDemandsDO.CNo = appendString.toString();
	} else if (localName.equalsIgnoreCase("CName")) {
	    regularDemandsDO.CName = appendString.toString();
	} else if (localName.equalsIgnoreCase("CNo")) {
	    regularDemandsDO.CNo = appendString.toString();
	} else if (localName.equalsIgnoreCase("GNo")) {
	    regularDemandsDO.GNo = appendString.toString();
	} else if (localName.equalsIgnoreCase("EII_EMP_ID")) {
	    regularDemandsDO.EII_EMP_ID = appendString.toString();
	} else if (localName.equalsIgnoreCase("GroupName")) {
	    regularDemandsDO.GroupName = appendString.toString();
	} else if (localName.equalsIgnoreCase("MemberCode")) {
	    regularDemandsDO.MemberCode = appendString.toString();
	} else if (localName.equalsIgnoreCase("MemberName")) {
	    regularDemandsDO.MemberName = appendString.toString();
	} else if (localName.equalsIgnoreCase("DemandDate")) {
	    regularDemandsDO.DemandDate = appendString.toString();
	} else if (localName.equalsIgnoreCase("MLAI_ID")) {
	    regularDemandsDO.MLAI_ID = appendString.toString();
	} else if (localName.equalsIgnoreCase("OSAmt")) {
	    regularDemandsDO.OSAmt = appendString.toString();
	} else if (localName.equalsIgnoreCase("DemandTotal")) {
	    regularDemandsDO.DemandTotal = appendString.toString();
	} else if (localName.equalsIgnoreCase("ODAmount")) {
	    regularDemandsDO.ODAmount = appendString.toString();
	} else if (localName.equalsIgnoreCase("Attendance")) {
	    regularDemandsDO.Attendance = appendString.toString();
	} else if (localName.equalsIgnoreCase("GLI")) {
	    regularDemandsDO.GLI = appendString.toString();
	} else if (localName.equalsIgnoreCase("Lateness")) {
	    regularDemandsDO.Lateness = appendString.toString();
	} else if (localName.equalsIgnoreCase("InstallNo")) {
	    regularDemandsDO.InstallNo = appendString.toString();
	} else if (localName.equalsIgnoreCase("SittingOrder")) {
	    regularDemandsDO.SittingOrder = appendString.toString();
	} else if (localName.equalsIgnoreCase("SO")) {
	    regularDemandsDO.SO = appendString.toString();
	} else if (localName.equalsIgnoreCase("NextRepayDate")) {
	    regularDemandsDO.NextRepayDate = appendString.toString();
	}
	else if (localName.equalsIgnoreCase("Renew")) {
	    regularDemandsDO.Renew = appendString.toString();
	}

	else if (localName.equalsIgnoreCase("MMI_Phone")) {
		regularDemandsDO.MobileNo = appendString.toString();
	}
	else if (localName.equalsIgnoreCase("primaryid")) {
		regularDemandsDO.AAdharNo = appendString.toString();
	}

	else if (localName.equalsIgnoreCase("CollectionMode")) {
		regularDemandsDO.BranchPaymode = appendString.toString();
	}

	else if (localName.equalsIgnoreCase("ProductName")) {
		regularDemandsDO.ProductName = appendString.toString();
	}
	else if (localName.equalsIgnoreCase("MobileNumber")) {
		regularDemandsDO.MobileNo = appendString.toString();
	}
	else if (localName.equalsIgnoreCase("LatituteCenter")) {
		regularDemandsDO.LatitudeCenter = appendString.toString();
	}
	else if (localName.equalsIgnoreCase("LongituteCenter")) {
		regularDemandsDO.LongitudeCenter = appendString.toString();
	}
	else if (localName.equalsIgnoreCase("LatituteGroup")) {
		regularDemandsDO.LatitudeGroup = appendString.toString();
	}

	else if (localName.equalsIgnoreCase("LongituteGroup")) {
		regularDemandsDO.LongitudeGroup = appendString.toString();
	}

	else if (localName.equalsIgnoreCase("LatituteMember")) {
		regularDemandsDO.LatitudeMember = appendString.toString();
	}

	else if (localName.equalsIgnoreCase("LongituteMember")) {
		regularDemandsDO.LongitudeMember = appendString.toString();
	}

	else if (localName.equalsIgnoreCase("CenterDemandAmt")) {
		regularDemandsDO.CenterAmt = appendString.toString();
	}
	else if (localName.equalsIgnoreCase("CenterMeetingDate")) {
		regularDemandsDO.CenterMeeting = appendString.toString();
	}
	else if (localName.equalsIgnoreCase("GroupDemandAmt")) {
		regularDemandsDO.GroupAmt = appendString.toString();
	}else if (localName.equalsIgnoreCase("GroupMeetingTime")) {
		regularDemandsDO.GroupMeeting = appendString.toString();
	}


	else if (localName.equalsIgnoreCase("RegDemands")) {
	    if (regularDemandsDO != null && regularDemandsDO.MLAI_ID != null) {
		regularDemandsBL.Insert(regularDemandsDO,this.context);
		regularDemandsBLTemp.Insert(regularDemandsDO,this.context);
		alRegularDemandsDOs.add(regularDemandsDO);
	    }

	}
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
	if (currentElement) {
	    appendString.append(ch, start, length);
	}
    }

    @Override
    public Object getData() {
	return alRegularDemandsDOs;
    }

    @Override
    public boolean getExecutionStatus() {
	if (alRegularDemandsDOs != null && alRegularDemandsDOs.size() > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public String getErrorMessage() {
	return "Already Downloaded";
    }

}
