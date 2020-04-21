package com.jayam.impactapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;

import com.jayam.impactapp.common.AppConstants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class BitMapUtils 
{
	public String getImage(String fileName)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = 8; 
	    
	    File root = new File(Environment.getExternalStorageDirectory()+ File.separator + AppConstants.FolderName + File.separator);
	    
	    
	    Bitmap receipt = BitmapFactory.decodeFile(root.getAbsolutePath()+"/"+fileName,options);
	    
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    receipt.compress(Bitmap.CompressFormat.JPEG, 90, stream);
	    byte[] receiptbyte = stream.toByteArray();   
	    String hexstring = toHex(receiptbyte);  
	    return hexstring;
	}
    
	public static String toHex(byte[] bytes)
	{
	    BigInteger bi = new BigInteger(1, bytes); 
	    return String.format("%0" + (bytes.length << 1) + "X", bi);
	}
}
