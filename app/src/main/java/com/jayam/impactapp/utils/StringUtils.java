package com.jayam.impactapp.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.LastReceiptDO;
import com.jayam.impactapp.objects.LucDemandsDO;
import com.jayam.impactapp.objects.NPSDemandDO;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;

import android.content.Context;
import android.util.Log;

public class StringUtils {


    public static String getSoapString(Context context, ArrayList<RegularDemandsDO> alRegularDemands) {
		Log.e("getSoapString", "called");
		StringBuffer soapString = new StringBuffer();
		// String userID = SharedPrefUtils.getKeyValue(context,
		// AppConstants.pref_name, AppConstants.username);
		//
		Calendar currentDate = Calendar.getInstance();
		//
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateNow = formatter.format(currentDate.getTime());

		String meeting_startTime = SharedPrefUtils.getKeyValue(context, AppConstants.memberDetails_pref,
				AppConstants.meetingStartTime);
		String meeting_endTime = SharedPrefUtils.getKeyValue(context, AppConstants.memberDetails_pref,
				AppConstants.meetingEnd);

		// IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		// ArrayList<IntialParametrsDO> alIntialParametrsDO =
		// intialParametrsBL.SelectAll();
		// IntialParametrsDO intialObj = alIntialParametrsDO.get(0);
		for (int i = 0; i < alRegularDemands.size(); i++) {
			RegularDemandsDO obj = alRegularDemands.get(i);

			soapString.append(obj.MemberCode);
			soapString.append("$");

			soapString.append(obj.MemberName);
			soapString.append("$");

			soapString.append(obj.DemandDate);
			soapString.append("$");

			soapString.append(obj.MLAI_ID);
			soapString.append("$");

			soapString.append(obj.Attendance);
			soapString.append("$");

			soapString.append(obj.collectedAmount);
			soapString.append("$");

			// reciept number

			soapString.append(obj.ReciptNumber);
			soapString.append("$");

			soapString.append(obj.GLI);
			soapString.append("$");

			soapString.append(obj.Lateness);
			soapString.append("$");

			soapString.append(obj.meetingStartTime);
			soapString.append("$");

			soapString.append(obj.meetingEndTime.substring(1, obj.meetingEndTime.length()));
			soapString.append("$");

			// soapString.append("N");
			soapString.append(obj.Print);
			soapString.append("$");

			soapString.append(obj.TransactionCode);
			soapString.append("$");

			soapString.append("0");
			soapString.append("$");

			soapString.append("0");// Death Marking
			soapString.append("$");

			soapString.append(obj.CollType);
			soapString.append("$");

			// soapString.append("N");
			soapString.append(obj.APFlag);
			soapString.append("$");

			soapString.append(obj.FTODID);
			soapString.append("$");

			soapString.append(obj.FTODReason);
			soapString.append("$");

			soapString.append(obj.DemiseDate);
			soapString.append("$");

			soapString.append(obj.probInCenter);
			soapString.append("$");

			soapString.append(obj.qom);
			soapString.append("$");

			soapString.append(obj.groupDiscipline);
			soapString.append("$");

			soapString.append(obj.collExp);
			soapString.append("$");

			soapString.append(obj.RenewFeed);
			soapString.append("$");

			soapString.append(obj.collExpRMEL);
			soapString.append("$");

			soapString.append(obj.collPlace);
			soapString.append("$");

			soapString.append(obj.repaymentMadeBy);
			soapString.append("$");

			soapString.append(obj.latitude);
			soapString.append("$");

			soapString.append(obj.langitude);

			soapString.append("$");

			soapString.append(obj.TransactionID);

			soapString.append("$");

			soapString.append(obj.Status);
			soapString.append("$");

			soapString.append(obj.PaymentMode);

			soapString.append("$");

			soapString.append(obj.MobileNo);

			soapString.append("$");

			soapString.append(obj.DateTime);
			soapString.append("$");

			soapString.append(obj.GNo);
			soapString.append("$");

			soapString.append(obj.ImageName);
			soapString.append("$");

			soapString.append("Regular");

			soapString.append("@");
		}
		return soapString.toString();
	}



    public static String getSoapStringForOD(Context context, ArrayList<ODDemandsDO> alOdDemands) {
	Log.e("getSoapString", "called");
	StringBuffer soapString = new StringBuffer();

	for (int i = 0; i < alOdDemands.size(); i++) {
	    ODDemandsDO obj = alOdDemands.get(i);

	    soapString.append(obj.MMI_Code);
	    soapString.append("$");

	    soapString.append(obj.MMI_Name);
	    soapString.append("$");

	    soapString.append(obj.DemandDate);
	    soapString.append("$");

	    soapString.append(obj.MLAI_ID);
	    soapString.append("$");

	    soapString.append("0");
	    soapString.append("$");

	    soapString.append(obj.collectedAmt);
	    soapString.append("$");

	    // reciept number
	    soapString.append(obj.recieptNumber);
	    soapString.append("$");

	    // gli
	    soapString.append("0");
	    soapString.append("$");

	    // lateness
	    soapString.append("0");
	    soapString.append("$");

	    // meeting start time
	    soapString.append("0");
	    soapString.append("$");

	    // meeting end time
	    soapString.append("0");
	    soapString.append("$");

	    // printflag Y / N
	    soapString.append(obj.printFlag);
	    // soapString.append("N");
	    soapString.append("$");

	    // TxnCode Default Zero For ODRecords
	    soapString.append("0");
	    soapString.append("$");

	    soapString.append("0");// DupNO
	    soapString.append("$");

	    // DeathMarking
	    soapString.append("0");
	    soapString.append("$");

	    // Coll Type 'OA' are 'P'
	    soapString.append("OA");
	    soapString.append("$");

	    soapString.append(obj.APFlag);

		soapString.append("$");

		soapString.append(obj.TransactionID);
		soapString.append("$");

		soapString.append(obj.Status);
		soapString.append("$");

		soapString.append(obj.PaymentMode);
		soapString.append("$");

		soapString.append(obj.MobileNo);
		soapString.append("$");

		soapString.append(obj.DateTime);
		soapString.append("$");

		soapString.append(obj.MGI_Code);

	    soapString.append("@");
	}
	return soapString.toString();
    }

	public static String getSoapStringLate(Context context, ArrayList<RegularDemandsDO> alRegularDemands) {
		Log.e("getSoapString", "called");
		StringBuffer soapString = new StringBuffer();
		// String userID = SharedPrefUtils.getKeyValue(context,
		// AppConstants.pref_name, AppConstants.username);
		//
		Calendar currentDate = Calendar.getInstance();
		//
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateNow = formatter.format(currentDate.getTime());

		String meeting_startTime = SharedPrefUtils.getKeyValue(context, AppConstants.memberDetails_pref,
				AppConstants.meetingStartTime);
		String meeting_endTime = SharedPrefUtils.getKeyValue(context, AppConstants.memberDetails_pref,
				AppConstants.meetingEnd);

		// IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		// ArrayList<IntialParametrsDO> alIntialParametrsDO =
		// intialParametrsBL.SelectAll();
		// IntialParametrsDO intialObj = alIntialParametrsDO.get(0);
		for (int i = 0; i < alRegularDemands.size(); i++) {
			RegularDemandsDO obj = alRegularDemands.get(i);

			soapString.append(obj.MemberCode);
			soapString.append("$");

			soapString.append(obj.MemberName);
			soapString.append("$");

			soapString.append(obj.DemandDate);
			soapString.append("$");

			soapString.append(obj.MLAI_ID);
			soapString.append("$");

			soapString.append(obj.Attendance);
			soapString.append("$");

			soapString.append(obj.collectedAmount);
			soapString.append("$");

			// reciept number

			soapString.append(obj.ReciptNumber);
			soapString.append("$");

			soapString.append(obj.GLI);
			soapString.append("$");

			soapString.append(obj.Lateness);
			soapString.append("$");

			soapString.append(obj.meetingStartTime);
			soapString.append("$");

			soapString.append(obj.meetingEndTime.substring(1, obj.meetingEndTime.length()));
			soapString.append("$");

			// soapString.append("N");
			soapString.append(obj.Print);
			soapString.append("$");

			soapString.append(obj.TransactionCode);
			soapString.append("$");

			soapString.append("0");
			soapString.append("$");

			soapString.append("0");// Death Marking
			soapString.append("$");

			soapString.append(obj.CollType);
			soapString.append("$");

			// soapString.append("N");
			soapString.append(obj.APFlag);
			soapString.append("$");

			soapString.append(obj.FTODID);
			soapString.append("$");

			soapString.append(obj.FTODReason);
			soapString.append("$");

			soapString.append(obj.DemiseDate);
			soapString.append("$");

			soapString.append(obj.probInCenter);
			soapString.append("$");

			soapString.append(obj.qom);
			soapString.append("$");

			soapString.append(obj.groupDiscipline);
			soapString.append("$");

			soapString.append(obj.collExp);
			soapString.append("$");

			soapString.append(obj.RenewFeed);
			soapString.append("$");

			soapString.append(obj.collExpRMEL);
			soapString.append("$");

			soapString.append(obj.collPlace);
			soapString.append("$");

			soapString.append(obj.repaymentMadeBy);
			soapString.append("$");

			soapString.append(obj.latitude);
			soapString.append("$");

			soapString.append(obj.langitude);

			soapString.append("$");

			soapString.append(obj.TransactionID);

			soapString.append("$");

			soapString.append(obj.Status);
			soapString.append("$");

			soapString.append(obj.PaymentMode);

			soapString.append("$");

			soapString.append(obj.MobileNo);

			soapString.append("$");

			soapString.append(obj.DateTime);
			soapString.append("$");

			soapString.append(obj.GNo);
			soapString.append("$");

			soapString.append(obj.ImageName);
			soapString.append("$");

			soapString.append("Late");


			soapString.append("@");
		}
		return soapString.toString();
	}

    public static String getSoapStringForAdvance(Context context, ArrayList<AdvaceDemandDO> alOdDemands) {
	Log.e("getSoapString", "called");
	StringBuffer soapString = new StringBuffer();

	for (int i = 0; i < alOdDemands.size(); i++) {
	    AdvaceDemandDO obj = alOdDemands.get(i);

	    soapString.append(obj.MMI_Code);// Member Code
	    soapString.append("$");

	    soapString.append(obj.MMI_Name);// Member Name
	    soapString.append("$");

	    soapString.append(obj.DemandDate);// Date
	    soapString.append("$");

	    soapString.append(obj.MLAI_ID);// Loan ID
	    soapString.append("$");

	    soapString.append("0");// Default Zero
	    soapString.append("$");

	    soapString.append(obj.previousAmt);// Collected Amt
	    soapString.append("$");

	    soapString.append(obj.ReceiptNumber);// Receipt Number
	    soapString.append("$");

	    soapString.append("0");// Default Zero
	    soapString.append("$");

	    soapString.append("0");// Default Zero
	    soapString.append("$");

	    soapString.append("0");// Default Zero
	    soapString.append("$");

	    soapString.append("0");// Default Zero
	    soapString.append("$");

	    // printflag Y / N
	    soapString.append(obj.printFlag);
	    soapString.append("$");

	    soapString.append(obj.CenterCnt);// Transaction Code
	    soapString.append("$");

	    soapString.append("0");
	    soapString.append("$");

	    soapString.append("0"); // DeathMarking Default Zero
	    soapString.append("$");

	    soapString.append(obj.MType);// Flag 'OA' are 'P'
	    soapString.append("$");

	    // soapString.append("N");
	    soapString.append(obj.APFlag);
		soapString.append("$");

		soapString.append(obj.TransactionID);// Flag 'OA' are 'P'
		soapString.append("$");

		soapString.append(obj.Status);// Flag 'OA' are 'P'
		soapString.append("$");

		soapString.append(obj.PaymentMode);// Flag 'OA' are 'P'
		soapString.append("$");

		soapString.append(obj.MobileNo);// Flag 'OA' are 'P'
		soapString.append("$");

		soapString.append(obj.DateTime);// Flag 'OA' are 'P'
	    soapString.append("@");
	}
	return soapString.toString();
    }



    public static String getRecieptNumber(RegularDemandsDO obj,Context context) {

	// Calendar currentDate = Calendar.getInstance();

	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDO = intialParametrsBL.SelectAll(context);
		IntialParametrsDO intialObj=null;
	if(alIntialParametrsDO!=null)
	{
		try
		{
			intialObj = alIntialParametrsDO.get(0);
		}catch (IndexOutOfBoundsException in)
		{
			in.printStackTrace();
		}
	}
		StringBuffer soapString = new StringBuffer();
	if(intialObj!=null)
	{
		soapString.append(intialObj.Code);
		soapString.append("-");
		soapString.append(intialObj.TerminalID);
		soapString.append("-");

		String[] date = obj.DemandDate.split("-");
		soapString.append(date[1]);
		soapString.append("-");
		soapString.append(date[2]);
		soapString.append("-");
		soapString.append(getMdifiedID(intialObj.LastTransactionID));
	}


	return soapString.toString();

    }

    public static String getTransactionCode_G(RegularDemandsDO obj,Context context) {

	// Calendar currentDate = Calendar.getInstance();

	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDO = intialParametrsBL.SelectAll(context);
	IntialParametrsDO intialObj = alIntialParametrsDO.get(0);
	StringBuffer soapString = new StringBuffer();
	soapString.append(intialObj.Code);
	soapString.append("-");
	soapString.append(intialObj.TerminalID);
	soapString.append("-");

	String[] date = obj.DemandDate.split("-");
	soapString.append(date[1]);
	soapString.append("-");
	soapString.append(date[2]);
	soapString.append("-");
	soapString.append("G");
	String txnId = getMdifiedID((Integer.parseInt(intialObj.LastTransactionCode) + 1) + "");
	soapString.append(txnId);
	return soapString.toString();

    }

    public static String getTransactionCode_C(RegularDemandsDO obj,Context context) {

	// Calendar currentDate = Calendar.getInstance();

	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDO = intialParametrsBL.SelectAll(context);
	IntialParametrsDO intialObj = alIntialParametrsDO.get(0);
	StringBuffer soapString = new StringBuffer();
	soapString.append(intialObj.Code);
	soapString.append("-");
	soapString.append(intialObj.TerminalID);
	soapString.append("-");

	String[] date = obj.DemandDate.split("-");
	soapString.append(date[1]);
	soapString.append("-");
	soapString.append(date[2]);
	soapString.append("-");
	soapString.append("C");
	String txnId = getMdifiedID((Integer.parseInt(intialObj.LastTransactionCode) + 1) + "");
	soapString.append(txnId);
	return soapString.toString();

    }


	public static String getTransactionCode_CLate(RegularDemandsDO obj,Context context) {

		// Calendar currentDate = Calendar.getInstance();

		IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		ArrayList<IntialParametrsDO> alIntialParametrsDO = intialParametrsBL.SelectAll(context);
		IntialParametrsDO intialObj = alIntialParametrsDO.get(0);
		StringBuffer soapString = new StringBuffer();
		soapString.append(intialObj.Code);
		soapString.append("-");
		soapString.append(intialObj.TerminalID);
		soapString.append("-");

		String[] date = obj.DemandDate.split("-");
		soapString.append(date[1]);
		soapString.append("-");
		soapString.append(date[2]);
		soapString.append("-");
		soapString.append("M");
		String txnId = obj.MLAI_ID;
		soapString.append(txnId);
		return soapString.toString();

	}
    public static String getRecieptNumberForAdvance(AdvaceDemandDO obj,Context context) {

	// Calendar currentDate = Calendar.getInstance();

	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDO = intialParametrsBL.SelectAll(context);
	IntialParametrsDO intialObj = alIntialParametrsDO.get(0);
	StringBuffer soapString = new StringBuffer();
	soapString.append(intialObj.Code);
	soapString.append("-");
	soapString.append(intialObj.TerminalID);
	soapString.append("-");

	String[] date = obj.DemandDate.split("-");
	soapString.append(date[1]);
	soapString.append("-");
	soapString.append(date[2]);
	soapString.append("-");
	soapString.append(getMdifiedID(intialObj.LastTransactionID));
	return soapString.toString();

    }

    public static String getTransactionCodeForAdvance_G(AdvaceDemandDO obj,Context context) {

	// Calendar currentDate = Calendar.getInstance();

	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDO = intialParametrsBL.SelectAll(context);
	IntialParametrsDO intialObj = alIntialParametrsDO.get(0);
	StringBuffer soapString = new StringBuffer();
	soapString.append(intialObj.Code);
	soapString.append("-");
	soapString.append(intialObj.TerminalID);
	soapString.append("-");

	String[] date = obj.DemandDate.split("-");
	soapString.append(date[1]);
	soapString.append("-");
	soapString.append(date[2]);
	soapString.append("-");
	soapString.append("G");
	String txnId = getMdifiedID((Integer.parseInt(intialObj.LastTransactionCode) + 1) + "");
	soapString.append(txnId);
	return soapString.toString();

    }

    public static String getTransactionCodeForAdvance_C(AdvaceDemandDO obj,Context context) {

	// Calendar currentDate = Calendar.getInstance();

	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDO = intialParametrsBL.SelectAll(context);
	IntialParametrsDO intialObj = alIntialParametrsDO.get(0);
	StringBuffer soapString = new StringBuffer();
	soapString.append(intialObj.Code);
	soapString.append("-");
	soapString.append(intialObj.TerminalID);
	soapString.append("-");

	String[] date = obj.DemandDate.split("-");
	soapString.append(date[1]);
	soapString.append("-");
	soapString.append(date[2]);
	soapString.append("-");
	soapString.append("C");
	String txnId = getMdifiedID((Integer.parseInt(intialObj.LastTransactionCode) + 1) + "");
	soapString.append(txnId);
	return soapString.toString();

    }







    public static String getRecieptNumberForOD(ODDemandsDO obj,Context context) {
	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDO = intialParametrsBL.SelectAll(context);
		IntialParametrsDO intialObj=null;
		if(alIntialParametrsDO!=null)
		{
			try
			{
				intialObj = alIntialParametrsDO.get(0);

			}catch (IndexOutOfBoundsException in)
			{
				in.printStackTrace();
			}
		}
		StringBuffer soapString = new StringBuffer();
		if(intialObj!=null)
		{
			soapString.append(intialObj.Code);
			soapString.append("-");
			soapString.append(intialObj.TerminalID);
			soapString.append("-");

			String[] date = obj.DemandDate.split("-");
			soapString.append(date[1]);
			soapString.append("-");
			soapString.append(date[2]);
			soapString.append("-");
			soapString.append(getMdifiedID(intialObj.LastTransactionID));
		}



	return soapString.toString();

    }





    public static String getMdifiedID(String recpNumb) {
	if (recpNumb.length() < 6) {
	    switch (recpNumb.length()) {
	    case 0:

		return recpNumb;
	    case 1:

		return "00000" + recpNumb;
	    case 2:

		return "0000" + recpNumb;
	    case 3:

		return "000" + recpNumb;
	    case 4:

		return "00" + recpNumb;
	    case 5:

		return "0" + recpNumb;

	    default:
		return recpNumb;
	    }
	}
	return recpNumb;
    }
}
