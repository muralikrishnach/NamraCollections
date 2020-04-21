/**
 * Here this class will parse the given input stream.
 * 
 * Parallely the OARequestLog will be updated with various timezones.
 */

package com.jayam.impactapp.xmlparser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.jayam.impactapp.xmlhandlers.BaseHandler;

import android.content.Context;
import android.util.Log;


public class XMLParser 
{
	InputStream input = null;
	private static final int TIMEOUT_CONNECT_MILLIS = 60000;
	private static final int TIMEOUT_READ_MILLIS = TIMEOUT_CONNECT_MILLIS - 5000;
	UrlPost urlPost=new UrlPost();
	SAXParser parser;
	public void parse(String xmlString, BaseHandler handler, Context context) throws Exception, ParserConfigurationException, SAXException, FactoryConfigurationError, IOException 
	{
		parser = SAXParserFactory.newInstance().newSAXParser();
		try 
        {
			Log.e("xmlString", ""+xmlString);
			URL url = new URL(xmlString);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
			input = connection.getInputStream();
			
			/*InputStreamReader reader = new InputStreamReader(input);
	  		StringBuilder buf = new StringBuilder();
	        char[] cbuf = new char[ 2048 ];
	  	    int num;
	  	    while ( -1 != (num=reader.read( cbuf )))
	  	    {
	  	    	buf.append( cbuf, 0, num );
	  	    }
	  	    String result = buf.toString();
	  	    
	  	    Log.e("RESULT", ""+result);
	  	    */
				parser.parse(input, handler);
	}
    catch(SAXException e)
    {
    	throw(e);
    }
    catch(UnknownHostException e)
    {
    	Log.e("UnknownHostException",""+e.toString()); 
    	throw(e);
    }
    catch(SocketException e)
    {
    	Log.e("SocketException",""+e.toString());
    	throw(e);
    }
    catch(Exception e)
    {
    	
    }
}
	public static void WriteinFile(InputStream is) throws IOException
  {
  BufferedInputStream bis=new BufferedInputStream(is);
  FileOutputStream fos=new FileOutputStream("/sdcard/Response.xml");
  BufferedOutputStream bos=new BufferedOutputStream(fos);
  byte byt[]=new byte[1024];
  int noBytes;
  while((noBytes=bis.read(byt)) != -1)
   bos.write(byt,0,noBytes);
  bos.flush();
  bos.close();
  fos.close();
  bis.close();
  
  }
	
}