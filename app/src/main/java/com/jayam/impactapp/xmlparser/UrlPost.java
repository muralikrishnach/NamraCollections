package com.jayam.impactapp.xmlparser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.util.Log;

public class UrlPost
{
	private HttpURLConnection connection;
	private InputStream inStream;
	private static final int TIMEOUT_CONNECT_MILLIS = 4*60000;
	private static final int TIMEOUT_READ_MILLIS = TIMEOUT_CONNECT_MILLIS - 5000;
	
	public InputStream soapPost1(String xmlString,URL url,String soapUrl) throws Exception
	{
		try
		{
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
			connection.setReadTimeout(TIMEOUT_READ_MILLIS);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			connection.setRequestProperty("Content-Length",""+xmlString.length() );
			connection.setRequestProperty("SOAPAction", soapUrl);

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(xmlString);
			outputStream.flush();
			
			inStream = (InputStream) connection.getInputStream();
		}
		catch(Exception e)
		{
			throw e;
		}
		return inStream;
	}
	//InputStream inStream;
	public InputStream soapPost(String xmlString,URL url,String soapUrl) throws Exception, SocketTimeoutException,ParserConfigurationException, SAXException, FactoryConfigurationError, IOException 
	{
		//url = Tools.Replace(url, "%20", " ");
		Log.e("xmlString", ""+xmlString);
        Log.e("Url", ""+url.toString());
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		try
        {
//			URL url = new URL(feedUrl);
	    	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			connection.setRequestProperty("Content-Length",""+xmlString.length());
			connection.setRequestProperty("SOAPAction", soapUrl);
			connection.setReadTimeout(TIMEOUT_READ_MILLIS);
			connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(xmlString);
			outputStream.flush();
			
			inStream = (InputStream) connection.getInputStream();
		}
		catch(Exception e)
		{
//			Log.d("exception in url post", "exception in url post");
			e.printStackTrace();
			throw e;
		}
		return inStream;
}
	
	
	public InputStream jsonPost(URL url) throws Exception, ParserConfigurationException, SAXException, FactoryConfigurationError, IOException 
	{
          Log.e("Url", ""+url.toString());
		try
        {
	    	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		
		

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			
			outputStream.flush();
			
			inStream = (InputStream) connection.getInputStream();
		}
		catch(Exception e)
		{
			throw e;
		}
		return inStream;
	}
}

