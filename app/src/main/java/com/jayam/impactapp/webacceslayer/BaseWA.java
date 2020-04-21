package com.jayam.impactapp.webacceslayer;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.common.ServiceMethods;
import com.jayam.impactapp.common.ServiceURLs;
import com.jayam.impactapp.objects.DemandDateDO;
import com.jayam.impactapp.xmlhandlers.AdvanceDemandsHandler;
import com.jayam.impactapp.xmlhandlers.BaseHandler;
import com.jayam.impactapp.xmlhandlers.FtodreasonsHandler;
import com.jayam.impactapp.xmlhandlers.InitialParametersHandlers;
import com.jayam.impactapp.xmlhandlers.LoginHandler;
import com.jayam.impactapp.xmlhandlers.ODDemandsHandler;
import com.jayam.impactapp.xmlhandlers.RegularDemandsHandler;
import com.jayam.impactapp.xmlparser.ParserManager;
import com.jayam.impactapp.xmlparser.UrlPost;
import com.jayam.impactapp.xmlparser.XMLParser;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public abstract class BaseWA {
    public Context context;
    public DataListner listner;
    public String url;
    public ServiceMethods MethodName;
    public BaseHandler handler;
    public String result = "";

    public BaseWA(Context context, DataListner listner, ServiceMethods MethodName) {
	this.context = context;
	this.listner = listner;
	this.MethodName = MethodName;
	this.handler = getHandler(MethodName);
    }

    public void startDownload(String url) {
	this.url = url;
	HttpSimulator httpSimulator = new HttpSimulator();
	httpSimulator.start();
    }

    private class HttpSimulator extends Thread {
	private HttpSimulator() {
	}

	@Override
	public void run() {
	    super.run();

	    XMLParser parser = ParserManager.getParser(ParserManager.ParserType.ANDROID_SAX);
	    try {
		parser.parse(url, handler, context);

		((Activity) context).runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			if (handler != null) {
			    if (handler.getExecutionStatus()) {
				// Log.e("dataretrived", "from server");
				// Log.d("mfimo", handler.getData().toString());
				listner.onDataretrieved(handler.getData());
			    } else {
				Log.e("dataretried", "from server fails");
				listner.onDataretrievalFailed(handler.getErrorMessage());
			    }
			} else {
			    listner.onDataretrievalFailed(handler.getErrorMessage());
			    Log.e("dataretried", "from server fails");
			}
		    }
		});

	    } catch (Exception e) {
		Log.e("Ecxeption", "" + e.toString());
	    }
	}
    }

    public BaseHandler getHandler(ServiceMethods method) {
	switch (method) {
	case LOGIN:
	    return new LoginHandler();
	case INTIALPARAMS:
	    return new InitialParametersHandlers();
	case REGULARDEMANDS:
	    return new RegularDemandsHandler(context);
	case ODDEMANDS:
	    return new ODDemandsHandler(context);
	case ADVANCEDEMANDS:
	    return new AdvanceDemandsHandler(context);

	case FTODREASONS:
	    return new FtodreasonsHandler(context);


	default:
	    return null;
	}
    }

    public class LoginService extends Thread {
	String urlstring;

	public LoginService(String urlstring) {
	    this.urlstring = urlstring;
	}

	@Override
	public void run() {
	    super.run();
	    getLoginStatus(urlstring);
	}
    }

    public class DownlaodChek extends Thread {
	String urlstring;

	public DownlaodChek(String urlstring) {
	    this.urlstring = urlstring;
	}

	@Override
	public void run() {
	    super.run();
	    verifyDownloads(urlstring);
	}
    }

    public class UploadService extends Thread {
	String uid;
	String soapString;
	String MACID;
	String PrinterAddress;
	String ReceiptNum;
	String TxnCode;
	String UFlag;
		String GroupId;

	public UploadService(String uid, String soapString, String macid, String PAddress, String ReceiptNo,
		String Txncode, String UFlag,String GroupId) {
	    this.uid = uid;
	    this.soapString = soapString;
	    this.MACID = macid;
	    this.PrinterAddress = PAddress;
	    this.ReceiptNum = ReceiptNo;
	    this.TxnCode = Txncode;
	    this.UFlag = UFlag;
		this.GroupId = GroupId;
	}

	@Override
	public void run() {
	    super.run();
	    upLoadSoapString(uid, soapString, MACID, PrinterAddress, ReceiptNum, TxnCode, UFlag,GroupId);
	}
    }

    public class UploadLUCService extends Thread {
	String uid;
	String soapString;
	String MACID;
	String PrinterAddress;

	public UploadLUCService(String uid, String soapString, String macid, String PAddress) {
	    this.uid = uid;
	    this.soapString = soapString;
	    this.MACID = macid;
	    this.PrinterAddress = PAddress;
	}

	@Override
	public void run() {
	    super.run();
	    upLoadLUCSoapString(uid, soapString, MACID, PrinterAddress);
	}
    }

    public class UploadImageService extends Thread {
	String uid;

	String data, groupName;

	public UploadImageService(String uid, String data, String groupName) {
	    this.uid = uid;
	    this.data = data;
	    this.groupName = groupName;
	}

	@Override
	public void run() {
	    super.run();
	    UploadImage(groupName, data, uid);
	}
    }

    public String getLoginStatus(String urlstring) {
	URL url = null;
	try {

	    url = new URL(urlstring);

	    Log.e("BASE WAurl", url + "");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setConnectTimeout(20000);
	    InputStream input = connection.getInputStream();
	    InputStreamReader reader = new InputStreamReader(input);
	    StringBuilder buf = new StringBuilder();
	    char[] cbuf = new char[2048];
	    int num;
	    while (-1 != (num = reader.read(cbuf))) {
		buf.append(cbuf, 0, num);
	    }
	    final String result = buf.toString();

	    ((Activity) context).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    if (result.equalsIgnoreCase("0")) {
			listner.onDataretrieved("0");
		    } else {
			listner.onDataretrievalFailed(result);
		    }

		}
	    });

	    input.close();
	    return result;

	} catch (final MalformedURLException e) {
	    Log.e("MalformedURLException", "while parsing loginservice");
	    ((Activity) (context)).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    listner.onDataretrievalFailed("Invalid URL Address");
		}
	    });

	    return "false";
	} catch (final IOException e) {
	    Log.e("IOException", "while parsing login service" + e.toString());

	    ((Activity) (context)).runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    listner.onDataretrievalFailed("Invalid URL Address");
		}

	    });

	    return "false";
	}

    }

    public String verifyDownloads(String urlstring) {
	URL url = null;
	try {

	    url = new URL(urlstring);

	    Log.e("url", url + "");

	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	    connection.setConnectTimeout(20000);
	    InputStream input = connection.getInputStream();
	    InputStreamReader reader = new InputStreamReader(input);
	    StringBuilder buf = new StringBuilder();
	    char[] cbuf = new char[2048];
	    int num;
	    while (-1 != (num = reader.read(cbuf))) {
		buf.append(cbuf, 0, num);
	    }
	    final String result = buf.toString();

	    ((Activity) context).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    if (result.equalsIgnoreCase("true") || result.equalsIgnoreCase("0")) {
			// Log.d("mfimo", result);
			listner.onDataretrieved(result);
		    } else {
			listner.onDataretrievalFailed(result);
		    }

		}
	    });

	    input.close();
	    return result;

	} catch (final MalformedURLException e) {
	    Log.e("MalformedURLException", "while parsing loginservice");
	    ((Activity) (context)).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    listner.onDataretrievalFailed(e.toString());
		}
	    });

	    return "false";
	} catch (final IOException e) {
	    Log.e("IOException", "while parsing login service" + e.toString());

	    ((Activity) (context)).runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    listner.onDataretrievalFailed(e.toString());
		}

	    });

	    return "false";
	}

    }

    public String getDemandDates(String str) {
	URL urlDates = null;
	try {

	    urlDates = new URL(str);

	    Log.e("url", url + "");
	    HttpURLConnection connection = (HttpURLConnection) urlDates.openConnection();
	    final InputStream input = connection.getInputStream();

	    InputStreamReader reader = new InputStreamReader(input);
	    StringBuilder buf = new StringBuilder();
	    char[] cbuf = new char[2048];
	    int num;
	    while (-1 != (num = reader.read(cbuf))) {
		buf.append(cbuf, 0, num);
	    }
	    final String result = buf.toString();
	    ((Activity) context).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    if (result != null && !result.equalsIgnoreCase("No Demand Dates")) {
			ArrayList<DemandDateDO> alDateDOs = new ArrayList<DemandDateDO>();
			if (result.contains("@")) {
			    String[] dates = result.split("@");
			    for (int i = 0; i < dates.length; i++) {
				DemandDateDO obj = new DemandDateDO();
				obj.date = dates[i];
				alDateDOs.add(obj);
			    }
			} else {
			    DemandDateDO obj = new DemandDateDO();
			    obj.date = result;
			    alDateDOs.add(obj);
			}
			listner.onDataretrieved(alDateDOs);
		    } else {
			listner.onDataretrievalFailed(result);

			try {
			    input.close();
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		    }
		}
	    });

	    return result;
	} catch (MalformedURLException e) {
	    Log.e("MalformedURLException", "while parsing loginservice");
	    return "Exception";
	} catch (IOException e) {
	    Log.e("IOException", "while parsing login service" + e.toString());
	    return "Exception";
	}

    }

    public void upLoadSoapString(String uid, String soapString, String MACID, String PAddress, String ReceiptNo,
	    String Txncode, String UFlag,String GroupId) {
	Log.e("uploadData", "called-service");
	
	String SOAP_ACTION_URL = "http://tempuri.org/";
	String SOAP_XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
		+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
		+ "<soap:Body>";
	String SOAP_XML_FOOTER = "</soap:Body></soap:Envelope>";

	String xmlStirng = SOAP_XML_HEADER + "<GetUploadedData xmlns=\"http://tempuri.org/\">" + "<uid>" + uid
		+ "</uid>" + "<str>" + soapString + "</str>" + "<MACID>" + MACID + "</MACID>" + "<BTAddress>" + PAddress
		+ "</BTAddress>" + "<MaxReceiptNo>" + ReceiptNo + "</MaxReceiptNo>" + "<MaxTxnCode>" + Txncode
		+ "</MaxTxnCode>" + "<UFlag>" + UFlag + "</UFlag>" + "<GroupID>" + GroupId + "</GroupID>" + "</GetUploadedData>" + SOAP_XML_FOOTER;

	InputStream inputStream;
	UrlPost urlPost = new UrlPost();
	try {
	    inputStream = urlPost.soapPost(xmlStirng, new URL(ServiceURLs.uploadservice),
		    SOAP_ACTION_URL + "GetUploadedData");
	    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	    factory.setNamespaceAware(true);
	    XmlPullParser xpp = factory.newPullParser();

	    xpp.setInput(inputStream, "UTF-8");
	    int eventType = xpp.getEventType();
	    while (eventType != XmlPullParser.END_DOCUMENT) {
		if (eventType == XmlPullParser.TEXT) {
		    result = xpp.getText();
		    Log.v("Result data = ", "" + result);
		    break;
		}
		eventType = xpp.next();
	    }

	    ((Activity) context).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    if (!result.equalsIgnoreCase("")) {
			listner.onDataretrieved(result);
		    } else {
			listner.onDataretrievalFailed(result);
		    }

		}
	    });

	} catch (Exception e) {
	    Log.e("Exception", "while uploading the data" + e.toString());
	}

    }

    public void upLoadLUCSoapString(String uid, String soapString, String MACID, String PAddress) {
	Log.e("uploadData", "called-service");
	String SOAP_ACTION_URL = "http://tempuri.org/";
	String SOAP_XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
		+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
		+ "<soap:Body>";
	String SOAP_XML_FOOTER = "</soap:Body></soap:Envelope>";

	String xmlStirng = SOAP_XML_HEADER + "<GetLUCUploaded xmlns=\"http://tempuri.org/\">" + "<uid>" + uid + "</uid>"
		+ "<str>" + soapString + "</str>" + "<MACID>" + MACID + "</MACID>" + "<BTAddress>" + PAddress
		+ "</BTAddress></GetLUCUploaded>" + SOAP_XML_FOOTER;

	InputStream inputStream;
	UrlPost urlPost = new UrlPost();
	try {
	    inputStream = urlPost.soapPost(xmlStirng, new URL(ServiceURLs.uploadservice),
		    SOAP_ACTION_URL + "GetLUCUploaded");
	    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	    factory.setNamespaceAware(true);
	    XmlPullParser xpp = factory.newPullParser();
	    xpp.setInput(inputStream, "UTF-8");
	    int eventType = xpp.getEventType();
	    while (eventType != XmlPullParser.END_DOCUMENT) {
		if (eventType == XmlPullParser.TEXT) {
		    result = xpp.getText();
		    Log.e("Result = ", "" + result);
		    break;
		}
		eventType = xpp.next();
	    }

	    ((Activity) context).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    if (!result.equalsIgnoreCase("")) {
			listner.onDataretrieved(result);
		    } else {
			listner.onDataretrievalFailed(result);
		    }

		}
	    });

	} catch (Exception e) {
	    Log.e("Exception", "while uploading the data" + e.toString());
	}

    }

    public void UploadImage(String groupNumber, String data, String user) {
	String SOAP_ACTION_URL = "http://tempuri.org/";
	String SOAP_XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
		+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
		+ "<soap:Body>";
	String SOAP_XML_FOOTER = "</soap:Body></soap:Envelope>";

	String xmlStirng = SOAP_XML_HEADER + "<GetGroupPhoto xmlns=\"http://tempuri.org/\">" + "<user>" + user
		+ "</user>" + "<data>" + data + "</data>" + "<GroupCode>" + groupNumber + "</GroupCode>"
		+ "</GetGroupPhoto>" + SOAP_XML_FOOTER;
	try {
	    WriteinFile(xmlStirng);
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	InputStream inputStream;

	UrlPost urlPost = new UrlPost();
	try {
	    inputStream = urlPost.soapPost(xmlStirng, new URL(ServiceURLs.uploadservice),
		    SOAP_ACTION_URL + "GetGroupPhoto");
	    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	    factory.setNamespaceAware(true);
	    XmlPullParser xpp = factory.newPullParser();

	    xpp.setInput(inputStream, "UTF-8");
	    int eventType = xpp.getEventType();
	    while (eventType != XmlPullParser.END_DOCUMENT) {
		if (eventType == XmlPullParser.TEXT) {
		    result = xpp.getText();
		    Log.e("Result = ", "" + result);
		    break;
		}
		eventType = xpp.next();
	    }

	    ((Activity) context).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    if (!result.equalsIgnoreCase("")) {
			listner.onDataretrieved(result);
		    } else {
			listner.onDataretrievalFailed(result);
		    }

		}
	    });

	} catch (Exception e) {
	    Log.e("Exception", "while uploading the data" + e.toString());
	}

    }

    public void WriteinFile(String str) throws IOException {
	FileOutputStream fos = new FileOutputStream("/sdcard/Response.xml");
	BufferedOutputStream bos = new BufferedOutputStream(fos);
	bos.write(str.getBytes());
	bos.close();
	fos.close();

    }

    public void getChangePasswordStatus(String urlstring) {
	URL url = null;
	try {

	    url = new URL(urlstring);

	    Log.e("url", url + "");

	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	    connection.setConnectTimeout(20000);
	    InputStream input = connection.getInputStream();
	    InputStreamReader reader = new InputStreamReader(input);
	    StringBuilder buf = new StringBuilder();
	    char[] cbuf = new char[2048];
	    int num;
	    while (-1 != (num = reader.read(cbuf))) {
		buf.append(cbuf, 0, num);
	    }
	    final String result = buf.toString();

	    ((Activity) context).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    if (!result.equalsIgnoreCase("Invalid Username or Password")) {
			listner.onDataretrieved(result);
		    } else {
			listner.onDataretrievalFailed(result);
		    }

		}
	    });

	    input.close();

	} catch (final MalformedURLException e) {
	    Log.e("MalformedURLException", "while parsing changePassword");
	    ((Activity) (context)).runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    listner.onDataretrievalFailed(e.toString());
		}
	    });

	} catch (final IOException e) {
	    Log.e("IOException", "while parsing changePassword service" + e.toString());

	    ((Activity) (context)).runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    listner.onDataretrievalFailed(e.toString());
		}

	    });

	}

    }

    public class ChangePasswordService extends Thread {
	String urlstring;

	public ChangePasswordService(String urlstring) {
	    this.urlstring = urlstring;
	}

	@Override
	public void run() {
	    super.run();
	    getChangePasswordStatus(urlstring);
	}
    }
}
