package com.jayam.impactapp.webacceslayer;

import android.util.Log;

import com.jayam.impactapp.R;
import com.jayam.impactapp.common.MfimoApp;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;

public class SimpleHttpClient {
	/**
	 * The time it takes for our client to timeout
	 */
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds

	/**
	 * Single instance of our HttpClient
	 */
	private static HttpClient mHttpClient;

	/**
	 * Get our single instance of our HttpClient object.
	 *
	 * @return an HttpClient object with connection parameters set
	 */
	public static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			// sets up parameters
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "utf-8");
			params.setBooleanParameter("http.protocol.expect-continue", false);
			// registers schemes for both http and https
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", newSslSocketFactory(), 443));
			ClientConnectionManager manager = new ThreadSafeClientConnManager(params, registry);
			mHttpClient = new DefaultHttpClient(manager, params);
		}
		return mHttpClient;
	}

	private static SSLSocketFactory newSslSocketFactory() {
		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			InputStream in = MfimoApp.getAppContext().getResources().openRawResource(R.raw.keystore);
			try {
				// Keystore password comes in place of 222222
				trusted.load(in, "222222".toCharArray());
			} finally {
				in.close();
			}
	    /*
	     * If you use STRICT_HOSTNAME_VERIFIER, the the host name in the URL
	     * should match with the host name in the server certificate. In
	     * this application it is 192.168.1.3
	     * 
	     * If you do not want to check the host name and simply want to
	     * connect to the URL, then use ALLOW_ALL_HOSTNAME_VERIFIER
	     * 
	     */
			HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			// HostnameVerifier hostnameVerifier =
			// org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
			SSLSocketFactory socketFactory = new SSLSocketFactory(trusted);
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			return socketFactory;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AssertionError(e);
		}
	}

	/**
	 * Performs an HTTP Post request to the specified url with the specified
	 * parameters.
	 *
	 * @param url            The web address to post the request to
	 * @param postParameters The parameters to send via the request
	 * @return The result of the request
	 * @throws Exception
	 */
	public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
			request.setEntity(formEntity);
			Log.d("yesb", url);
			for (int i = 0; i < postParameters.size(); i++) {
				Log.d("yesb", postParameters.get(i).getName() + ": " + postParameters.get(i).getValue());
			}
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String result = sb.toString();
			Log.d("yesb", "" + result.length());
			Log.d("yesb", "" + result);
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Performs an HTTP GET request to the specified url.
	 *
	 * @param url The web address to post the request to
	 * @return The result of the request
	 * @throws Exception
	 */
	public static String executeHttpGet(String url) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			// HttpParams params = client.getParams();
			// HttpConnectionParams.setConnectionTimeout(params, 3000);
			// HttpConnectionParams.setSoTimeout(params, 3000);
			HttpGet request = new HttpGet();
			url = url.replace(" ", "%20");
			url = url.replace("+", "%2B");
			Log.d("yesb", url);
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String result = sb.toString();
			Log.d("yesb", "" + result.length());
			return result;
		} finally {
			Log.d("yesb", "finally");
			if (in != null) {
				try {
					Log.d("yesb", "finally try");
					in.close();
				} catch (IOException e) {
					Log.d("yesb", "finally try catch");
					e.printStackTrace();
					return null;
				}
			}
		}
	}

	public static String executemasterHttpGet(String url) throws Exception {
		BufferedReader in = null;
		try {

			HttpClient client = new DefaultHttpClient();
			;
			//  HttpClient client = getHttpClient();
			// HttpParams params = client.getParams();
			// HttpConnectionParams.setConnectionTimeout(params, 3000);
			// HttpConnectionParams.setSoTimeout(params, 3000);
			HttpGet request = new HttpGet();
			url = url.replace(" ", "%20");
			url = url.replace("+", "%2B");
			Log.d("yesb", url);
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String result = sb.toString();
			Log.d("yesb", "" + result.length());
			return result;
		} finally {
			Log.d("yesb", "finally");
			if (in != null) {
				try {
					Log.d("yesb", "finally try");
					in.close();
				} catch (IOException e) {
					Log.d("yesb", "finally try catch");
					e.printStackTrace();
					return null;
				}
			}
		}
	}


	public static String executemasterHttpGetTime(String url) throws Exception {
		BufferedReader in = null;
		try {


			HttpParams httpParameters = new BasicHttpParams();
			int timeoutSocket = 6000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient client = new DefaultHttpClient(httpParameters);
			;
			HttpGet request = new HttpGet();
			url = url.replace(" ", "%20");
			url = url.replace("+", "%2B");
			Log.d("yesb", url);
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String result = sb.toString();
			Log.d("yesb", "" + result.length());
			return result;
		} catch (ConnectTimeoutException e) {
			//Here Connection TimeOut excepion
			e.printStackTrace();
			return null;

		} finally {
			Log.d("yesb", "finally");
			if (in != null) {
				try {
					Log.d("yesb", "finally try");
					in.close();
				} catch (IOException e) {
					Log.d("yesb", "finally try catch");
					e.printStackTrace();
					return null;
				}
			}
		}


	}


}
