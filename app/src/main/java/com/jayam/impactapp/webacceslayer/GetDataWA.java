package com.jayam.impactapp.webacceslayer;

import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.common.ServiceMethods;

import android.content.Context;
import android.util.Log;

public class GetDataWA extends BaseWA {
    public GetDataWA(Context context, DataListner listner, ServiceMethods MethodName) {
	super(context, listner, MethodName);
    }

    public void validateuserlogin(String url) {
	LoginService loginseervice = new LoginService(url);
	loginseervice.start();
    }

    public void getIntialParameters(String url) {
	startDownload(url);
    }

    public void getDates(String url) {
	String result = getDemandDates(url);
    }

    public void getNewDemands(String url) {
	startDownload(url);
    }

    public void getNewLUCDemands(String url) {
	startDownload(url);
    }

    public void getAdvDemands(String url) {
	startDownload(url);
    }

    public void getNPSDemands(String url) {
	startDownload(url);
    }

    public void getODDemands(String url) {
	startDownload(url);
    }

    public void uploadData(String uid, String soapString, String MACID, String PAddress, String ReceiptNo,
	    String TxnCode, String UFlag,String GroupId) {
	Log.e("uploadData", "called-GetDataWA");
	UploadService uploadSerice = new UploadService(uid, soapString, MACID, PAddress, ReceiptNo, TxnCode, UFlag,GroupId);
	uploadSerice.start();
    }

    public void uploadlucData(String uid, String soapString, String MACID, String PAddress) {
	Log.e("uploadData", "called-GetDataWA");
	UploadLUCService uploadSerice = new UploadLUCService(uid, soapString, MACID, PAddress);
	uploadSerice.start();
    }

    public void changePassword(String url) {
	ChangePasswordService changePasswordService = new ChangePasswordService(url);
	changePasswordService.start();
    }

    public void checkDownload(String url) {
	DownlaodChek downlaodChek = new DownlaodChek(url);
	downlaodChek.start();
    }

    public void uploadImages(String gname, String data, String uid) {
	UploadImageService imageService = new UploadImageService(uid, data, gname);
	imageService.start();
    }

    public void getFTODREASONS(String url) {
	startDownload(url);
    }
}
