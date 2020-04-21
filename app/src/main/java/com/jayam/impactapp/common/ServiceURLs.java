package com.jayam.impactapp.common;

public class ServiceURLs {


//   public static String mailURl="http://219.90.67.117/Ampl_Uat_Live_mFIMO/";
//    public static String mailURl="http://219.90.67.117/mfimolive/";   //live

//    public static String mailURl="http://219.90.67.117/Ampl_Uat_Live/";
//public static String mailURl="http://219.90.67.117/SPPFM_OFC/Tabscreens/";
//    http://219.90.67.117/Ampl_Uat_Live_mFIMO/validatelogin.aspx?UId=fyyfyf&PWD=tyfyf&MACID=4480EB800c9B&Version=3.1&AFlag=A
   // public static String mailURl="http://219.90.67.117/mfimouat/";//prepode
   //  public static String mailURl="https://afpluat.com/mfimouat/";//prepode

    //public static String mailURl="https://afpllive.com/AMPL_TrainingColl/";//Training
  //  public static String mailURl="https://www.afpluat.com/AMPL_TrainingColl/";//Training UAt


//    public static String mailURl="http://219.90.67.210/mfimoLive/";//Training

//  public static String mailURl="http://219.90.67.225/mfimolive/";//live
//    public static String mailURl="https://afpllive.com/Ampl_Trainingcoll/";//live

//    public static String mailURl="http://219.90.67.117/Pre_Prod/";//live
//    public static String mailURl="https://afpllive.com/pre_prod/";//live

//    public static String mailURl="https://afpllive.com/AMPL_Training/";

   // public static String mailURl="http://219.90.67.210/ampl_live/";

 //  public static String mailURl="https://www.afpluat.com/Afpluatlive_MFIMO_PPFM_COLL/";//prepode
  //  public static String mailURl="https://afpllive.com/Afpllive_MFIMO_PPFM_COLL/";//Live

   /* public static String DomainName= "www.afpluat.com";
    public static String mailURl="https://www.afpluat.com/Afpl_Pre_Prod_Coll_Test/";//prepode
    public static String Image_URL="https://afpluat.com/Afpl_Pre_Prod_Fileuploads/api/";//prepode*/

//    public static String mailURl = "http://efimo2.jayamsolutions.com/Namra_prelivecollections/";
//    public static String Image_URL = "http://efimo2.jayamsolutions.com/Namra_preliveimageUpload/api/";
//    public static String DomainName = "http://efimo2.jayamsolutions.com";

    public static String mailURl = "http://172.20.221.5/Namra_prelivecollections/";
    public static String Image_URL = "http://172.20.221.5/Namra_preliveimageUpload/api/";
    public static String DomainName = "http://172.20.221.5";


    public static String loginURL = mailURl + "validatelogin.aspx?UId=%s&PWD=%s&MACID=%s&Version=4.4";
    public static String intialParametersUrl = mailURl + "getparameters.aspx?UId=%s";
    public static String getDates = mailURl + "getdates.aspx?UId=%s";
    public static String newDemands = mailURl + "RegularDemands.aspx?UId=%s&DT=%s";
    public static String newLUCDemands = mailURl + "LUCDemands.aspx?UId=%s&DT=%s";
    public static String advDemands = mailURl + "ADVDemands.aspx?UId=%s&DT=%s";
    public static String odDemandsUrl = mailURl + "oddemands.aspx?UId=%s&DT=%s";
    public static String npsDemands = mailURl + "NPSforAndroid.aspx?UId=%s";
    public static String uploadservice = mailURl + "MFIMOServ.asmx";
    public static String changePasswordUrl = mailURl + "changepassword.aspx?UId=%s&OPWD=%s&NPWD=%s";

    public static String downloadVerify = mailURl + "CheckDemands.aspx?UID=%s&DT=%s&COUNT=%s&MACID=%s&BTAddress=%s";
    public static String advdownloadVerify = mailURl + "CheckAdvances.aspx?UID=%s&DT=%s&COUNT=%s&MACID=%s&BTAddress=%s";

    public static String npsdownloadVerify = mailURl + "CheckNPSCount.aspx?UID=%s&COUNT=%s&MACID=%s&BTAddress=%s";

    public static String loginVerify = mailURl + "CheckLogin.aspx?UID=%s&MACID=%s&BTAddress=%s";
    public static String ftodreasonsUrl = mailURl + "FTODReasons.aspx";

    public static String checkOverdues = mailURl + "checkoverdues.aspx?UId=%s&DT=%s&COUNT=%S";

    public static String getloginUrl(String username, String password, String deviceId) {
	return String.format(loginURL, username, password, deviceId);
    }

    public static String getIntialParametersUrl(String userID) {
	return String.format(intialParametersUrl, userID);
    }

    public static String getDatesUrl(String userid) {
	return String.format(getDates, userid);
    }

    public static String getNewDesandsUrl(String userid, String date) {
	return String.format(newDemands, userid, date);
    }

    public static String getNewLUCDesandsUrl(String userid, String date) {
	return String.format(newLUCDemands, userid, date);
    }

    public static String getNewAdvanceUrl(String terminalid, String date) {
	return String.format(advDemands, terminalid, date);
    }

    public static String getNPSUrl(String terminalid) {
	return String.format(npsDemands, terminalid);
    }

    public static String getODDemandsUrl(String terminalid, String date) {
	return String.format(odDemandsUrl, terminalid, date);
    }

    public static String getchangePasswordUrl(String UserId, String OldPassword, String NewPassword) {
	return String.format(changePasswordUrl, UserId, OldPassword, NewPassword);
    }

    public static String getDowmloadVerifyUrl(String uid, String date, String count, String MACID, String PAddress) {
	return String.format(downloadVerify, uid, date, count, MACID, PAddress);
    }

    public static String getloginVerifyUrl(String uid, String MACID, String PAddress) {
	return String.format(loginVerify, uid, MACID, PAddress);
    }

    public static String getAdvDowmloadVerifyUrl(String uid, String date, String count, String MACID, String PAddress) {
	return String.format(advdownloadVerify, uid, date, count, MACID, PAddress);
    }

    public static String getNpsDowmloadVerifyUrl(String uid, String date, String count, String MACID, String PAddress) {
	return String.format(npsdownloadVerify, uid, count, MACID, PAddress);
    }

    public static String getcheckOverDuesUrl(String uid, String date, String count) {
	return String.format(checkOverdues, uid, date, count);
    }

    public static String getFtodreasonsUrl() {
	return String.format(ftodreasonsUrl);
    }
}
