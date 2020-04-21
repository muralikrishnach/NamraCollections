package com.jayam.impactapp.businesslayer;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.common.ServiceMethods;
import com.jayam.impactapp.common.ServiceURLs;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.webacceslayer.GetDataWA;

import android.content.Context;
import android.util.Log;

public class GetDataBL extends BaseBL {

    public GetDataBL(Context context, DataListner listner) {
	super(context, listner);
    }

    public void validateuserlogin(String username, String password, String deviceId, String MACID) {
	String macid = MACID.replaceAll(":", "");
	String url = ServiceURLs.getloginUrl(username, password, macid);
	
	Log.v("", " GEt URL"+url);
	new GetDataWA(context, listner, ServiceMethods.LOGIN).validateuserlogin(url);
    }

    public void getIntialParameters() {
	String userID = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.username);
	String url = ServiceURLs.getIntialParametersUrl(userID);

	new GetDataWA(context, listner, ServiceMethods.INTIALPARAMS).getIntialParameters(url);
    }

    public void getDates() {
	String userID = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.username);
	String url = ServiceURLs.getDatesUrl(userID);
	new GetDataWA(context, listner, ServiceMethods.GETDATES).getDates(url);
    }

    public void getNewDemands(String userId, String date) {
	String url = ServiceURLs.getNewDesandsUrl(userId, date);
	new GetDataWA(context, listner, ServiceMethods.REGULARDEMANDS).getNewDemands(url);
    }

    public void getNewLUCDemands(String userId, String date) {
	String url = ServiceURLs.getNewLUCDesandsUrl(userId, date);
	new GetDataWA(context, listner, ServiceMethods.LUCDEMANDS).getNewDemands(url);
    }

    public void getAdvanceDemands(String terminalID, String date) {
	String url = ServiceURLs.getNewAdvanceUrl(terminalID, date);
	new GetDataWA(context, listner, ServiceMethods.ADVANCEDEMANDS).getAdvDemands(url);
    }

    public void getNPSDemands(String terminalID) {
	String url = ServiceURLs.getNPSUrl(terminalID);
	new GetDataWA(context, listner, ServiceMethods.NPSDEMANDS).getNPSDemands(url);
    }

    public void uploadData(String soapString, String MACID, String PAddress, String ReceiptNo, String TxnCode, String UFlag,String GroupId) {
	Log.e("uploadData", "called");
	String userID = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.username);
	new GetDataWA(context, listner, ServiceMethods.UPLOADDATA).uploadData(userID, soapString, MACID, PAddress, ReceiptNo, TxnCode, UFlag,GroupId);
    }

    public void uploadLucData(String soapString, String MACID, String PAddress) { }

    public void uploadImage(String userID, String data, String gName) {
	new GetDataWA(context, listner, ServiceMethods.UPLOADDATA).uploadImages(gName, data, userID);
    }

    public void getODDemands(String terminalID, String date) {
	String url = ServiceURLs.getODDemandsUrl(terminalID, date);
	new GetDataWA(context, listner, ServiceMethods.ODDEMANDS).getODDemands(url);
    }

    public void changePassword(String UserId, String OldPassword, String NewPassword) {
	String url = ServiceURLs.getchangePasswordUrl(UserId, OldPassword, NewPassword);
	new GetDataWA(context, listner, ServiceMethods.CHANGEPASSWORD).changePassword(url);
    }

    public void verifyDownloads(String date, String count, String PAddress) {
	String userID = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.username);
	String macid = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.macid);
	// String url = ServiceURLs.getDowmloadVerifyUrl(userID,date, count);
	String url = ServiceURLs.getDowmloadVerifyUrl(userID, date, count, macid, PAddress);
	new GetDataWA(context, listner, ServiceMethods.VERIFYDOWNLOAD).checkDownload(url);
    }

    public void verifyAdvanceDownloads(String userID, String date, String count, String PAddress) {
	// String userID = SharedPrefUtils.getKeyValue(context,
	// AppConstants.pref_name, AppConstants.username);
	String macid = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.macid);

	String url = ServiceURLs.getAdvDowmloadVerifyUrl(userID, date, count, macid, PAddress);
	new GetDataWA(context, listner, ServiceMethods.VERIFYDOWNLOAD).checkDownload(url);
    }

    public void verifyNPSDownloads(String userID, String date, String count, String PAddress) {
	// String userID = SharedPrefUtils.getKeyValue(context,
	// AppConstants.pref_name, AppConstants.username);
	String macid = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.macid);
	String url = ServiceURLs.getNpsDowmloadVerifyUrl(userID, date, count, macid, PAddress);
	new GetDataWA(context, listner, ServiceMethods.VERIFYDOWNLOAD).checkDownload(url);
    }

    public void checkOverDues(String terminlaID, String date, String count) {
	String url = ServiceURLs.getcheckOverDuesUrl(terminlaID, date, count);
	new GetDataWA(context, listner, ServiceMethods.VERIFYDOWNLOAD).checkDownload(url);
    }

    public void loginEvent(String userID, String PAddress) {
	Log.d("mfimo", userID + "" + PAddress);
	String macid = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.macid);
	String url = ServiceURLs.getloginVerifyUrl(userID, macid, PAddress);
	new GetDataWA(context, listner, ServiceMethods.VERIFYDOWNLOAD).checkDownload(url);
    }

    public void getFTODreasons() {
	Log.d("mfimo", "ftod reasons");
	String url = ServiceURLs.getFtodreasonsUrl();
	new GetDataWA(context, listner, ServiceMethods.FTODREASONS).getFTODREASONS(url);
    }

}
