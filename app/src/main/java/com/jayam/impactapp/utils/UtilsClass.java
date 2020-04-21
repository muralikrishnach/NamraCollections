package com.jayam.impactapp.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtilsClass {

	private static AlertDialog.Builder dialog;

	  public static boolean showAlert(final Context context, String msg) {
	  		dialog = new Builder(context);

	  		dialog.setTitle("");
	  		dialog.setMessage(msg);
	  		dialog.setPositiveButton("OK", new OnClickListener() {

	  			@Override
	  			public void onClick(DialogInterface dialog, int which) {
	  				dialog.dismiss();
	  			}
	  		});
	  		dialog.setCancelable(false);
	  		dialog.show();

	  		return false;
	  	}


	  public static void showSingleChoiceLIstItems(Context context, String[] itemsId, int checkedItem, String title, DialogInterface. OnClickListener listener)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setSingleChoiceItems(itemsId, checkedItem, listener);
			builder.setTitle(title+"");
			builder.show();
		}


	// UNICODE 0x23 = #
	public static final byte[] UNICODE_TEXT = new byte[] {0x23, 0x23, 0x23,
			0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
			0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
			0x23, 0x23, 0x23};

	private static String hexStr = "0123456789ABCDEF";
	private static String[] binaryArray = { "0000", "0001", "0010", "0011",
			"0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
			"1100", "1101", "1110", "1111" };

	public static byte[] decodeBitmap(Bitmap bmp){
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();

		List<String> list = new ArrayList<String>(); //binaryString list
		StringBuffer sb;


		int bitLen = bmpWidth / 8;
		int zeroCount = bmpWidth % 8;

		String zeroStr = "";
		if (zeroCount > 0) {
			bitLen = bmpWidth / 8 + 1;
			for (int i = 0; i < (8 - zeroCount); i++) {
				zeroStr = zeroStr + "0";
			}
		}

		for (int i = 0; i < bmpHeight; i++) {
			sb = new StringBuffer();
			for (int j = 0; j < bmpWidth; j++) {
				int color = bmp.getPixel(j, i);

				int r = (color >> 16) & 0xff;
				int g = (color >> 8) & 0xff;
				int b = color & 0xff;

				// if color close to white，bit='0', else bit='1'
				if (r > 160 && g > 160 && b > 160)
					sb.append("0");
				else
					sb.append("1");
			}
			if (zeroCount > 0) {
				sb.append(zeroStr);
			}
			list.add(sb.toString());
		}

		List<String> bmpHexList = binaryListToHexStringList(list);
		String commandHexString = "1D763000";
		String widthHexString = Integer
				.toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
						: (bmpWidth / 8 + 1));
		if (widthHexString.length() > 2) {
			Log.e("decodeBitmap error", " width is too large");
			return null;
		} else if (widthHexString.length() == 1) {
			widthHexString = "0" + widthHexString;
		}
		widthHexString = widthHexString + "00";

		String heightHexString = Integer.toHexString(bmpHeight);
		if (heightHexString.length() > 2) {
			Log.e("decodeBitmap error", " height is too large");
			return null;
		} else if (heightHexString.length() == 1) {
			heightHexString = "0" + heightHexString;
		}
		heightHexString = heightHexString + "00";

		List<String> commandList = new ArrayList<String>();
		commandList.add(commandHexString+widthHexString+heightHexString);
		commandList.addAll(bmpHexList);

		return hexList2Byte(commandList);
	}

	public static List<String> binaryListToHexStringList(List<String> list) {
		List<String> hexList = new ArrayList<String>();
		for (String binaryStr : list) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < binaryStr.length(); i += 8) {
				String str = binaryStr.substring(i, i + 8);

				String hexString = myBinaryStrToHexString(str);
				sb.append(hexString);
			}
			hexList.add(sb.toString());
		}
		return hexList;

	}

	public static String myBinaryStrToHexString(String binaryStr) {
		String hex = "";
		String f4 = binaryStr.substring(0, 4);
		String b4 = binaryStr.substring(4, 8);
		for (int i = 0; i < binaryArray.length; i++) {
			if (f4.equals(binaryArray[i]))
				hex += hexStr.substring(i, i + 1);
		}
		for (int i = 0; i < binaryArray.length; i++) {
			if (b4.equals(binaryArray[i]))
				hex += hexStr.substring(i, i + 1);
		}

		return hex;
	}

	public static byte[] hexList2Byte(List<String> list) {
		List<byte[]> commandList = new ArrayList<byte[]>();

		for (String hexStr : list) {
			commandList.add(hexStringToBytes(hexStr));
		}
		byte[] bytes = sysCopy(commandList);
		return bytes;
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	public static byte[] sysCopy(List<byte[]> srcArrays) {
		int len = 0;
		for (byte[] srcArray : srcArrays) {
			len += srcArray.length;
		}
		byte[] destArray = new byte[len];
		int destLen = 0;
		for (byte[] srcArray : srcArrays) {
			System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
			destLen += srcArray.length;
		}
		return destArray;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}




	public static void writeToFile(String data,String filename)
	{
		File destination=null;
		File destination1=null;
		try {
			destination1 = new File(Environment.getExternalStorageDirectory() + "/ppfmext");
			if (!destination1.exists()) {
				destination1.mkdirs();
			}
			else
			{
				destination1.delete();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		destination = new File(destination1, filename);
		try
		{
			destination.createNewFile();
			FileOutputStream fOut = new FileOutputStream(destination);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(data);
			myOutWriter.close();
			fOut.flush();
			fOut.close();
		}
		catch (IOException e)
		{
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}

	public static String GetCurrentdateNanoTime()
	{
		Date endsd = new Date();
		String dateString = "";
	//	SimpleDateFormat sdfr = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat sdfr = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		try {
			dateString = sdfr.format(endsd);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return  dateString;
	}

	public static String GetCurrentdateTime()
	{
		Date endsd = new Date();
		String dateString = "";
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateString = sdfr.format(endsd);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return  dateString;
	}
}
