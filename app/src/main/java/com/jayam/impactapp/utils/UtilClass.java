package com.jayam.impactapp.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class UtilClass {
    private static AlertDialog.Builder dialog;
    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int date = calendar.get(Calendar.DATE);
        StringBuffer dString = new StringBuffer();
        dString.append((date>9)?String.valueOf(date):("0"+date));
        dString.append("-");
        dString.append((month>9)?String.valueOf(month):("0"+month));
        dString.append("-");
        dString.append(year);
        return dString.toString();
    }
    
    public static void setDelay(int millis){
        Date d = new Date();
        long milli = d.getTime();
        while((milli+millis)>(new Date()).getTime()){
            
        }
    }
    public static String getPrintLine(String lineStr,boolean middleAlign){
        if(lineStr.length()>42)
            lineStr = lineStr.substring(0,42);
        else{
            if(middleAlign){
                int leftSpaces = (42-lineStr.length())/2;
                lineStr =  addSpace(leftSpaces)+lineStr;
            }
            int spaceLen = 42-lineStr.length();
            lineStr = lineStr + addSpace(spaceLen);
        }
        return lineStr;
    }
    public static String addSpace(int len) {
        String tmp = "";
        for (int i = 1; i <= len; i++)
        {
                tmp += " ";
        }
        return tmp;
    }
    
    public static String trimDoubleStr(String doubleStr){
        if(doubleStr.indexOf(".")!=-1){
            int index = doubleStr.indexOf(".");
            if(doubleStr.length()>(index+3))
                doubleStr = doubleStr.substring(0, index+3); 
        }
        return doubleStr;   
    }
    public static String RemoveChars(String orgStr,char charc){
        char chars[] = orgStr.toCharArray();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<chars.length;i++){
            if(chars[i] != charc){
                sb.append(chars[i]);
            }            
        }
      return sb.toString();
    }
    public static String GetXMLParameterValue(String xmlString,String parameter){
        int index = xmlString.indexOf(">", xmlString.indexOf(parameter));
        int index1 = xmlString.indexOf("</"+parameter+">");
        
        return xmlString.substring(index+1, index1);
    }
    
    public static void WaitForSomeTime(long millis){
        long time = new Date().getTime();
        while((time+millis)>(new Date()).getTime()){}
    }
    
    public static String stringToHex(String str) {
		char[] chars = str.toCharArray();
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			strBuffer.append(Integer.toHexString((int) chars[i]));
		}
		return strBuffer.toString();
	}
    
    public static final char[] hexChar = {
        '0' , '1' , '2' , '3' ,
        '4' , '5' , '6' , '7' ,
        '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f'};
    
    private static int charToNibble ( char c )
   {
        if ( '0' <= c && c <= '9' )
        {
            return c - '0';
        }
        else if ( 'a' <= c && c <= 'f' )
        {
            return c - 'a' + 0xa;
        }
        else if ( 'A' <= c && c <= 'F' )
        {
            return c - 'A' + 0xa;
        }
        else
        {
            throw new IllegalArgumentException ( "Invalid hex character: " + c );
        }
   }

//    converts from byte array to hexa-decimal string
    public static String BytestoHexStr( byte[] b )
   {
        StringBuffer sb = new StringBuffer( b.length * 2 );
        for ( int i=0; i<b.length; i++ )
        {
            // look up high nibble char
            sb.append( hexChar [( b[i] & 0xf0 ) >>> 4] );

            // look up low nibble char
            sb.append( hexChar [b[i] & 0x0f] );
       }
       return sb.toString();
   }
    
//    converts from hexa-decimal string to byte array 
   public static byte[] HexStrToBytes ( String s )
   {
        
        int stringLength = s.length();
        if ( (stringLength & 0x1) != 0 )
        {
            throw new IllegalArgumentException ("fromHexString requires an even number beof hex characters");
        }
        byte[] b = new byte[stringLength / 2];

        for ( int i=0,j=0; i<stringLength; i+=2,j++ )
        {
            int high = charToNibble( s.charAt ( i ) );
            int low = charToNibble( s.charAt ( i+1 ) );
            b[j] = (byte)( ( high << 4 ) | low );
        }
        return b;
   }


    public static String GetCurrentdateNanoSeconds()
    {
        Date endsd = new Date();
        String dateString = "";
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyyMMddHHmmssSSSSSSSSS");
        try {
            dateString = sdfr.format(endsd);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return  dateString;
    }


    public static String UploadCurrentDate(){
        String Current = "";

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Current = formatter.format(currentDate.getTime());

        return Current;
    }
    public static boolean showAlert(final Context context, String msg) {
        dialog = new AlertDialog.Builder(context);

        dialog.setTitle("");
        dialog.setMessage(msg);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

        return false;
    }
}
