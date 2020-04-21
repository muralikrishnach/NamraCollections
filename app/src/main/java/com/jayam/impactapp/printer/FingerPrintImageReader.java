package com.jayam.impactapp.printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import android.os.Environment;
import android.util.Log;

public class FingerPrintImageReader extends Thread{
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static byte[] SAGEM_INIT = { /*(byte) 0x32, (byte) 0x02, (byte) 0x61,
		(byte) 0x00, */(byte) 0x21, (byte) 0x08, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x01,
		(byte) 0x00, (byte) 0x01, /*(byte) 0xB5, (byte) 0x84, (byte) 0x1B,
		(byte) 0x03*/ };
	public static final byte[] LOOKUP1 = { (byte) 0x00, (byte) 0x21,
		(byte) 0x42, (byte) 0x63, (byte) 0x84, (byte) 0xA5, (byte) 0xC6,
		(byte) 0xE7, (byte) 0x08, (byte) 0x29, (byte) 0x4A, (byte) 0x6B,
		(byte) 0x8C, (byte) 0xAD, (byte) 0xCE, (byte) 0xEF, (byte) 0x31,
		(byte) 0x10, (byte) 0x73, (byte) 0x52, (byte) 0xB5, (byte) 0x94,
		(byte) 0xF7, (byte) 0xD6, (byte) 0x39, (byte) 0x18, (byte) 0x7B,
		(byte) 0x5A, (byte) 0xBD, (byte) 0x9C, (byte) 0xFF, (byte) 0xDE,
		(byte) 0x62, (byte) 0x43, (byte) 0x20, (byte) 0x01, (byte) 0xE6,
		(byte) 0xC7, (byte) 0xA4, (byte) 0x85, (byte) 0x6A, (byte) 0x4B,
		(byte) 0x28, (byte) 0x09, (byte) 0xEE, (byte) 0xCF, (byte) 0xAC,
		(byte) 0x8D, (byte) 0x53, (byte) 0x72, (byte) 0x11, (byte) 0x30,
		(byte) 0xD7, (byte) 0xF6, (byte) 0x95, (byte) 0xB4, (byte) 0x5B,
		(byte) 0x7A, (byte) 0x19, (byte) 0x38, (byte) 0xDF, (byte) 0xFE,
		(byte) 0x9D, (byte) 0xBC, (byte) 0xC4, (byte) 0xE5, (byte) 0x86,
		(byte) 0xA7, (byte) 0x40, (byte) 0x61, (byte) 0x02, (byte) 0x23,
		(byte) 0xCC, (byte) 0xED, (byte) 0x8E, (byte) 0xAF, (byte) 0x48,
		(byte) 0x69, (byte) 0x0A, (byte) 0x2B, (byte) 0xF5, (byte) 0xD4,
		(byte) 0xB7, (byte) 0x96, (byte) 0x71, (byte) 0x50, (byte) 0x33,
		(byte) 0x12, (byte) 0xFD, (byte) 0xDC, (byte) 0xBF, (byte) 0x9E,
		(byte) 0x79, (byte) 0x58, (byte) 0x3B, (byte) 0x1A, (byte) 0xA6,
		(byte) 0x87, (byte) 0xE4, (byte) 0xC5, (byte) 0x22, (byte) 0x03,
		(byte) 0x60, (byte) 0x41, (byte) 0xAE, (byte) 0x8F, (byte) 0xEC,
		(byte) 0xCD, (byte) 0x2A, (byte) 0x0B, (byte) 0x68, (byte) 0x49,
		(byte) 0x97, (byte) 0xB6, (byte) 0xD5, (byte) 0xF4, (byte) 0x13,
		(byte) 0x32, (byte) 0x51, (byte) 0x70, (byte) 0x9F, (byte) 0xBE,
		(byte) 0xDD, (byte) 0xFC, (byte) 0x1B, (byte) 0x3A, (byte) 0x59,
		(byte) 0x78, (byte) 0x88, (byte) 0xA9, (byte) 0xCA, (byte) 0xEB,
		(byte) 0x0C, (byte) 0x2D, (byte) 0x4E, (byte) 0x6F, (byte) 0x80,
		(byte) 0xA1, (byte) 0xC2, (byte) 0xE3, (byte) 0x04, (byte) 0x25,
		(byte) 0x46, (byte) 0x67, (byte) 0xB9, (byte) 0x98, (byte) 0xFB,
		(byte) 0xDA, (byte) 0x3D, (byte) 0x1C, (byte) 0x7F, (byte) 0x5E,
		(byte) 0xB1, (byte) 0x90, (byte) 0xF3, (byte) 0xD2, (byte) 0x35,
		(byte) 0x14, (byte) 0x77, (byte) 0x56, (byte) 0xEA, (byte) 0xCB,
		(byte) 0xA8, (byte) 0x89, (byte) 0x6E, (byte) 0x4F, (byte) 0x2C,
		(byte) 0x0D, (byte) 0xE2, (byte) 0xC3, (byte) 0xA0, (byte) 0x81,
		(byte) 0x66, (byte) 0x47, (byte) 0x24, (byte) 0x05, (byte) 0xDB,
		(byte) 0xFA, (byte) 0x99, (byte) 0xB8, (byte) 0x5F, (byte) 0x7E,
		(byte) 0x1D, (byte) 0x3C, (byte) 0xD3, (byte) 0xF2, (byte) 0x91,
		(byte) 0xB0, (byte) 0x57, (byte) 0x76, (byte) 0x15, (byte) 0x34,
		(byte) 0x4C, (byte) 0x6D, (byte) 0x0E, (byte) 0x2F, (byte) 0xC8,
		(byte) 0xE9, (byte) 0x8A, (byte) 0xAB, (byte) 0x44, (byte) 0x65,
		(byte) 0x06, (byte) 0x27, (byte) 0xC0, (byte) 0xE1, (byte) 0x82,
		(byte) 0xA3, (byte) 0x7D, (byte) 0x5C, (byte) 0x3F, (byte) 0x1E,
		(byte) 0xF9, (byte) 0xD8, (byte) 0xBB, (byte) 0x9A, (byte) 0x75,
		(byte) 0x54, (byte) 0x37, (byte) 0x16, (byte) 0xF1, (byte) 0xD0,
		(byte) 0xB3, (byte) 0x92, (byte) 0x2E, (byte) 0x0F, (byte) 0x6C,
		(byte) 0x4D, (byte) 0xAA, (byte) 0x8B, (byte) 0xE8, (byte) 0xC9,
		(byte) 0x26, (byte) 0x07, (byte) 0x64, (byte) 0x45, (byte) 0xA2,
		(byte) 0x83, (byte) 0xE0, (byte) 0xC1, (byte) 0x1F, (byte) 0x3E,
		(byte) 0x5D, (byte) 0x7C, (byte) 0x9B, (byte) 0xBA, (byte) 0xD9,
		(byte) 0xF8, (byte) 0x17, (byte) 0x36, (byte) 0x55, (byte) 0x74,
		(byte) 0x93, (byte) 0xB2, (byte) 0xD1, (byte) 0xF0 };

	/*---   Index table number  2 ---------------------------------------*/
	public static final byte[] LOOKUP2 = { (byte) 0x00, (byte) 0x10,
		(byte) 0x20, (byte) 0x30, (byte) 0x40, (byte) 0x50, (byte) 0x60,
		(byte) 0x70, (byte) 0x81, (byte) 0x91, (byte) 0xA1, (byte) 0xB1,
		(byte) 0xC1, (byte) 0xD1, (byte) 0xE1, (byte) 0xF1, (byte) 0x12,
		(byte) 0x02, (byte) 0x32, (byte) 0x22, (byte) 0x52, (byte) 0x42,
		(byte) 0x72, (byte) 0x62, (byte) 0x93, (byte) 0x83, (byte) 0xB3,
		(byte) 0xA3, (byte) 0xD3, (byte) 0xC3, (byte) 0xF3, (byte) 0xE3,
		(byte) 0x24, (byte) 0x34, (byte) 0x04, (byte) 0x14, (byte) 0x64,
		(byte) 0x74, (byte) 0x44, (byte) 0x54, (byte) 0xA5, (byte) 0xB5,
		(byte) 0x85, (byte) 0x95, (byte) 0xE5, (byte) 0xF5, (byte) 0xC5,
		(byte) 0xD5, (byte) 0x36, (byte) 0x26, (byte) 0x16, (byte) 0x06,
		(byte) 0x76, (byte) 0x66, (byte) 0x56, (byte) 0x46, (byte) 0xB7,
		(byte) 0xA7, (byte) 0x97, (byte) 0x87, (byte) 0xF7, (byte) 0xE7,
		(byte) 0xD7, (byte) 0xC7, (byte) 0x48, (byte) 0x58, (byte) 0x68,
		(byte) 0x78, (byte) 0x08, (byte) 0x18, (byte) 0x28, (byte) 0x38,
		(byte) 0xC9, (byte) 0xD9, (byte) 0xE9, (byte) 0xF9, (byte) 0x89,
		(byte) 0x99, (byte) 0xA9, (byte) 0xB9, (byte) 0x5A, (byte) 0x4A,
		(byte) 0x7A, (byte) 0x6A, (byte) 0x1A, (byte) 0x0A, (byte) 0x3A,
		(byte) 0x2A, (byte) 0xDB, (byte) 0xCB, (byte) 0xFB, (byte) 0xEB,
		(byte) 0x9B, (byte) 0x8B, (byte) 0xBB, (byte) 0xAB, (byte) 0x6C,
		(byte) 0x7C, (byte) 0x4C, (byte) 0x5C, (byte) 0x2C, (byte) 0x3C,
		(byte) 0x0C, (byte) 0x1C, (byte) 0xED, (byte) 0xFD, (byte) 0xCD,
		(byte) 0xDD, (byte) 0xAD, (byte) 0xBD, (byte) 0x8D, (byte) 0x9D,
		(byte) 0x7E, (byte) 0x6E, (byte) 0x5E, (byte) 0x4E, (byte) 0x3E,
		(byte) 0x2E, (byte) 0x1E, (byte) 0x0E, (byte) 0xFF, (byte) 0xEF,
		(byte) 0xDF, (byte) 0xCF, (byte) 0xBF, (byte) 0xAF, (byte) 0x9F,
		(byte) 0x8F, (byte) 0x91, (byte) 0x81, (byte) 0xB1, (byte) 0xA1,
		(byte) 0xD1, (byte) 0xC1, (byte) 0xF1, (byte) 0xE1, (byte) 0x10,
		(byte) 0x00, (byte) 0x30, (byte) 0x20, (byte) 0x50, (byte) 0x40,
		(byte) 0x70, (byte) 0x60, (byte) 0x83, (byte) 0x93, (byte) 0xA3,
		(byte) 0xB3, (byte) 0xC3, (byte) 0xD3, (byte) 0xE3, (byte) 0xF3,
		(byte) 0x02, (byte) 0x12, (byte) 0x22, (byte) 0x32, (byte) 0x42,
		(byte) 0x52, (byte) 0x62, (byte) 0x72, (byte) 0xB5, (byte) 0xA5,
		(byte) 0x95, (byte) 0x85, (byte) 0xF5, (byte) 0xE5, (byte) 0xD5,
		(byte) 0xC5, (byte) 0x34, (byte) 0x24, (byte) 0x14, (byte) 0x04,
		(byte) 0x74, (byte) 0x64, (byte) 0x54, (byte) 0x44, (byte) 0xA7,
		(byte) 0xB7, (byte) 0x87, (byte) 0x97, (byte) 0xE7, (byte) 0xF7,
		(byte) 0xC7, (byte) 0xD7, (byte) 0x26, (byte) 0x36, (byte) 0x06,
		(byte) 0x16, (byte) 0x66, (byte) 0x76, (byte) 0x46, (byte) 0x56,
		(byte) 0xD9, (byte) 0xC9, (byte) 0xF9, (byte) 0xE9, (byte) 0x99,
		(byte) 0x89, (byte) 0xB9, (byte) 0xA9, (byte) 0x58, (byte) 0x48,
		(byte) 0x78, (byte) 0x68, (byte) 0x18, (byte) 0x08, (byte) 0x38,
		(byte) 0x28, (byte) 0xCB, (byte) 0xDB, (byte) 0xEB, (byte) 0xFB,
		(byte) 0x8B, (byte) 0x9B, (byte) 0xAB, (byte) 0xBB, (byte) 0x4A,
		(byte) 0x5A, (byte) 0x6A, (byte) 0x7A, (byte) 0x0A, (byte) 0x1A,
		(byte) 0x2A, (byte) 0x3A, (byte) 0xFD, (byte) 0xED, (byte) 0xDD,
		(byte) 0xCD, (byte) 0xBD, (byte) 0xAD, (byte) 0x9D, (byte) 0x8D,
		(byte) 0x7C, (byte) 0x6C, (byte) 0x5C, (byte) 0x4C, (byte) 0x3C,
		(byte) 0x2C, (byte) 0x1C, (byte) 0x0C, (byte) 0xEF, (byte) 0xFF,
		(byte) 0xCF, (byte) 0xDF, (byte) 0xAF, (byte) 0xBF, (byte) 0x8F,
		(byte) 0x9F, (byte) 0x6E, (byte) 0x7E, (byte) 0x4E, (byte) 0x5E,
		(byte) 0x2E, (byte) 0x3E, (byte) 0x0E, (byte) 0x1E };
	public static byte RC =0x00;
	public static final byte STX = (byte) 0x02; // 0x6E;
	public static final byte ETX = (byte) 0x03; // 0x05;
	public static final byte DLE = (byte) 0x1B;
	public static final byte ACK = (byte) 0xE2;
	public static final byte NACK = (byte) 0xE4;

	public static final byte PACKET_ID = (byte) 0x61;
	public static int TIMES=0;
	public static int TIMESCOUNT=0;
	public static final byte FIRST_DATA_PACKET_ID = (byte) 0xC1;
	public static final byte INTER_DATA_PACKET_ID = (byte) 0x81;
	public static final byte LAST_DATA_PACKET_ID = (byte) 0xA1;
	public static final byte SINGLE_DATA_PACKET_ID = (byte) 0xE1;
	public static final byte ID_ASYNCHRONOUS_MESSAGE = (byte) 0x71; // Signals
	public static int ER_COUNT=0;
	public static final byte MESSAGE_CODE_QUALITY_CMD = (byte) 0x40;
	public static final byte MESSAGE_COMMAND_CMD = (byte) 0X01;
	public static final byte MESSAGE_IMAGE_CMD = (byte) 0X02;
	public static final byte MESSAGE_ENROLL_CMD = (byte) 0X04;
	public static final byte MESSAGE_DETECT_QUALITY_CMD = (byte) 0X80;
	public static int pquality;
	private int totalSize =2;
	public static Vector fpData =new Vector(1);
	public static byte[] fpTemplate = null;
	// Async messages
	public static final int MORPHO_MOVE_NO_FINGER = 0; // No finger is
	// detected.

	public static final int MORPHO_MOVE_FINGER_UP = 1; // User must move his
	// finger up

	public static final int MORPHO_MOVE_FINGER_DOWN = 2; // User must move
	// his finger down

	public static final int MORPHO_MOVE_FINGER_LEFT = 3; // User must move
	// his finger to the
	// left.

	public static final int MORPHO_MOVE_FINGER_RIGHT = 4; // User must move
	// his finger to the
	// right.

	public static final int MORPHO_PRESS_FINGER_HARDER = 5; // User must press
	// his finger
	// harder.

	public static final int MORPHO_LATENT = 6; // Finger was in the same place
	// as a previous
	// acquisition.User must move
	// his finger.

	public static final int MORPHO_REMOVE_FINGER = 7; // User must remove his
	// finger.

	public static final int MORPHO_FINGER_OK = 8; // The finger acquisition
	// was correctly completed

	public static final byte ID_EXPORT_IMAGE = (byte) 0x3D;
	public static final byte ID_COMPRESSION_NULL = (byte) 0x2C;
	// Req Status

	public static final byte ILVERR_ERROR = (byte) 0xFF; // An error occurred

	public static final byte FP_ID = 0x32;
	private static final String TAG = "Finger Print Image";
	//////////////////////////////////////////////////////////////////////////////////
	byte [] imageFPData=null;
	InputStream mmInStream;
	OutputStream mmOutStream;

	public FingerPrintImageReader(InputStream in, OutputStream out){
		byte[] data = null;
		mmInStream=in;
		mmOutStream=out;
		try {
			
			getFpImage();
			//getFPTemplate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public byte[] getFPTemplate() throws Exception{
		byte[] command = getTemplateCommand(RC);
		System.out.println("Data"+HexString.bufferToHex(command));
		sendFPPackets((new byte[][]{command}), RC);
		return imageFPData;
	}
	public byte[] getTemplateCommand(byte RC) {
		return getCommandPacket(SAGEM_INIT, RC);
	}
	public void createFPImage(byte[] data, int time){
		String state = Environment.getExternalStorageState();
		OutputStream fOut = null;
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			String path = Environment.getExternalStorageDirectory().toString();
			File folder=new File(path+"/fpimage/");
			if(!folder.exists()){folder.mkdir();}
			File file = new File(folder, time+"-test.png");
			try {
				if(file.exists()){
					file.delete();
					file.createNewFile();
				}
				else
					file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fOut = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(fOut!=null)
					fOut.write(data);
				else
					System.out.println("Outputstream is null");
				fOut.flush();
				fOut.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			Log.d(TAG, "We can only read media");
		} else {
			// Something else is wrong. It may be one of many other states, but all we need
			//  to know is we can neither read nor write
			Log.d(TAG, "Media is not mounted on the device");

		}
		//FPReset();
	}
	public byte[] getEnrollCommand(byte RC) {
		return (getCommandPacket(SagemFP.captureImageISO() ,RC));
	}
	public byte[] getCommandPacket(byte[] data ,byte RC){
		byte[] unStuffedData = new byte[3+data.length]; //RC+DATA+CRC1+CRC2
		unStuffedData[0] =RC;
		System.arraycopy(data, 0, unStuffedData, 1, data.length);
		byte[] CRC= calculateCRC(data);
		unStuffedData[unStuffedData.length-2] = CRC[0];
		unStuffedData[unStuffedData.length-1] = CRC[1];
		byte[] stuffedData = stuffing(unStuffedData);

		byte[] command = new byte[2+stuffedData.length+2];//STX PACID [RC DATA CRC] IDE ETX
		command[0]=STX;
		command[1]=PACKET_ID;
		System.arraycopy(stuffedData, 0, command, 2, stuffedData.length);
		command[command.length-2]=DLE;
		command[command.length-1]=ETX;
		return command;
	}
	public byte[] calculateCRC(byte[] data) {
		byte[] crc = new byte[2];
		short index = 0;
		for (int i = 0; i < data.length; i++) {
			index = (short) ((crc[1] ^ data[i]) & 0xff);
			crc[1] = (byte) (LOOKUP2[index] ^ crc[0]);
			crc[0] = (byte) (LOOKUP1[index]);
		}
		return crc;
	}
	public byte[] stuffing(byte[] data) {
		byte[] stuData = new byte[getStuffedDataLen(data)];
		int len = 0;

		try {
			for (int i = 0; i < data.length; i++) {
				switch (data[i]) {
				case 0x11:
					stuData[len] = 0x1B;
					len = len + 1;
					stuData[len] = 0x12;
					len = len + 1;
					break;

				case 0x13:
					stuData[len] = 0x1B;
					len = len + 1;
					stuData[len] = 0x14;
					len = len + 1;

					break;
				case 0x1B:
					stuData[len] = 0x1B;
					len = len + 1;
					stuData[len] = 0x1B;
					len = len + 1;
					break;
				default:
					stuData[len] = data[i];
					len = len + 1;
					break;
				}
			}
		} catch (Exception e) {
		}
		return stuData;
	}

	public byte[] unStuff(byte[] data) {
		ByteBuffer unStuffedData = new ByteBuffer();
		try {
			for (int i = 0; i < data.length; i++) {
				switch (data[i]) {
				case 0x1B:
					switch (data[i + 1]) {
					case 0x12:
						unStuffedData.appendByte((byte) 0x11);
						break;
					case 0x14:
						unStuffedData.appendByte((byte) 0x13);
						break;
					case 0x1B:
						unStuffedData.appendByte((byte) 0x1B);
						break;
					default:
						break;
					}
					i++;
					break;
				default:
					unStuffedData.appendByte(data[i]);
					break;
				}
			}
		} catch (Exception e) {
		}
		return unStuffedData.getBuffer();
	}
	public int getStuffedDataLen(byte[] data) {
		int length = data.length;
		for (int i = 0; i < data.length; i++) {
			switch (data[i]) {
			case 0x11:
			case 0x13:
			case 0x1B:
				length++;
				break;
			default:
				break;
			}
		}
		return length;
	}
	private boolean getACKrNACK(byte RC) throws Exception {
		byte[] res = receiveACKrNACK();

		if (res.length < 3)
			return false;
		byte[] rxdRC = new byte[res.length - 2];
		System.arraycopy(res, 2, rxdRC, 0, rxdRC.length);
		if (res[1] == ACK && (unStuff(rxdRC)[0] == RC)) {
			RC++;
			return true;
		} else
			return false;
	}
	public byte[] receiveACKrNACK() throws Exception {

		ByteBuffer buffer = new ByteBuffer();
		int count = 0;

		byte rd;
		while (true) {
			rd = (byte) mmInStream.read();
			if (rd ==(byte)0x02) {
				buffer.appendByte(rd);
				count++;
				break;
			}
		}

		while (true) {
			rd = (byte) mmInStream.read();
			buffer.appendByte(rd);
			count++;
			if (count == 3) {
				if (rd == (byte) 0x1B) {
					buffer.appendByte((byte) mmInStream.read());
				}
				break;
			}
		}
		byte[] res = buffer.getBuffer();
		if (res.length==0)
			return null;
		//MFIEnrollment.appendLog("<<<---"+ HexString.bufferToHex(res));
		return res;
	}
	public void getFpImage() throws Exception{
		//byte[] command = FPUtil.appendIdentifier(FP_ID, getEnrollCommand(RC));
		byte[] command =getEnrollCommand(RC);
		sendFPPackets(new byte[][]{command}, RC);
	}
	private void sendFPPackets(byte[][] packets ,byte RC) throws Exception{

		for (int i = 0; i < packets.length; i++) {
			send(packets[i]);

			if(getACKrNACK(RC)){
				runFingerPrint();
			}else{
			}
		}
		//return res;
	}
	public void runFingerPrint(){
		System.out.println("Started run method to fetch fimger image data");
		byte[] res= null;
		try {
			res=getResponse();
			imageFPData=res;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void send(byte[] data) throws Exception {

		try {
			mmOutStream.write(data);
			mmOutStream.flush();
			Thread.sleep(1000);
		}
		catch(Exception e){}

	}
	public  byte[] receive(byte STX ,byte ETX) throws Exception
	{

		ByteBuffer buffer = new ByteBuffer();
		byte[] res = null;
		byte rd = 0x00;
		try{
			while (true) {
				rd = (byte) mmInStream.read();
				System.out.println("Value of the rd->"+rd);
				if (rd == STX) {
					buffer.appendByte(rd);
					break;
				}
			}

			byte prev = 0x00;
			byte pPrev =0x00;
			byte DLE =(byte)0x1B;

			while (true) {
				//Thread.sleep(500);
				pPrev = prev;
				prev = rd ;
				rd =(byte) mmInStream.read();
				buffer.appendByte(rd);

				if (rd == ETX && (prev == DLE)) {
					if (pPrev != DLE) {
						break;
					} else {
						byte[] temp = buffer.getBuffer();
						int i = temp.length - 4;
						int DLECNT = 0;

						while (true) {
							if (temp[i] == DLE) {
								DLECNT++;
							} else {
								break;
							}
							i--;
						}
						if (DLECNT % 2 != 0) {
							break;
						}
					}
				}
				//Thread.sleep(100);
			}	
		}catch(Exception ex){
			res = buffer.getBuffer();
			//ex.printStackTrace();
			return res;
		}

		String data = HexString.bufferToHex(buffer.getBuffer());

		//  BluetoothSearch.append("In Receive:"+data);
		//  Display.getDisplay(thisMidlet).setCurrent(BluetoothSearch);

		res = buffer.getBuffer();
		if(res == null || res.length==0)
			throw new RuntimeException("Not able to receive the response Len of res:"+res.length);
		return res;

	}
	public void sendFpACK(byte RC) throws Exception{
		byte[] ackCommand = getACKCommand(RC);
		send(ackCommand);
	}
	public byte[] getACKCommand(byte RC) throws Exception{
		byte[] rc1 = new byte[10];
		rc1[0] = RC;
		byte[] stufedRC = stuffing(rc1);
		byte[] ACK = new byte[2+stufedRC.length];
		ACK[0] = STX;
		ACK[1] = 0x62;
		System.arraycopy(stufedRC, 0, ACK,2 , stufedRC.length);
		return ACK;
	}
	public void sendFpNACK(byte RC) throws Exception{
		byte[] nackCommand = getNACK(RC);
		send(nackCommand);
	}
	public byte[] getNACK(byte RC) throws Exception{
		byte []rc1 = new byte[2];
		rc1[0] = RC;
		byte[] stufedRC = stuffing(rc1);
		byte[] NACK = new byte[2+stufedRC.length];
		NACK[0] = STX;
		NACK[1] = 0x64;
		System.arraycopy(stufedRC, 0, NACK,2 , stufedRC.length);
		return NACK;
	}

	public boolean hasNextPacket(byte packetID) {
		boolean hasNextDataPack = true;
		if (packetID == LAST_DATA_PACKET_ID
				|| packetID == SINGLE_DATA_PACKET_ID) {
			hasNextDataPack = false;
		}
		return hasNextDataPack;
	}
	public byte[] getResponse() throws Exception{ //It works only for receiveTemplate response and receive Image with template
		boolean hasNextPacket = false;
		ByteBuffer buffer = new ByteBuffer();
		int packet =0;
		ER_COUNT =0;
		try {
			do{
				byte[] response = receive(STX,ETX); //STX PACID [RC DATA CRC1 CRC2 ]DLE ETX  //
				if(response == null || response.length==0){ // DATA (ASYNC MSG) =  FUNCID LEN(2 LsbMsb) STATUS COMMADID DATA
					throw new RuntimeException("Not able to receive Sagem Response");
				}
				//throw new RuntimeException("Not able to receive Sagem Response");
				byte pac_ID =response[1];
				byte [] dataForUnstuff = new byte[response.length - 4]; //removing  the STX PAC_ID.. DLE ETX (4 bytes)
				System.arraycopy(response, 2, dataForUnstuff, 0, dataForUnstuff.length);
				response = null;
				byte [] unStuffed = unStuff(dataForUnstuff); //Unstuffed Data  [RC DATA CRC1 CRC2 ]
				byte[] temp = new byte[unStuffed.length - 3];
				System.arraycopy(unStuffed, 1, temp, 0, temp.length);
				byte[] crc =calculateCRC(temp);
				if((crc[0]== unStuffed[unStuffed.length-2]) && (crc[1]== unStuffed[unStuffed.length-1]) ){
					sendFpACK(unStuffed[0]);//RC = unstuffed[0];
					ER_COUNT =0;


				} else {
					if (ER_COUNT >= 3) {
						sendFpACK(unStuffed[0]);
						ER_COUNT = 0;

					} else {
						sendFpNACK(unStuffed[0]);

						ER_COUNT++;
					}

					continue;
				}
				byte functionID = unStuffed[1];//DATA = FUNCID LEN2 LEN1 DATA(VARY by FUN-ID)
				hasNextPacket = hasNextPacket(pac_ID) || (functionID==ID_ASYNCHRONOUS_MESSAGE);
				System.out.println("Has Next Packet "+hasNextPacket);
				if(functionID == ID_ASYNCHRONOUS_MESSAGE){//FUNCTID =0X71
					String message="";

					switch(unStuffed[5]){
					case MESSAGE_COMMAND_CMD:
						if(unStuffed[8]==8)
						{
						}
						message = getCommandMessage(unStuffed[8]);
						break;
					case MESSAGE_CODE_QUALITY_CMD:
						int quality = ((char) ((byte)unStuffed[8] * 100 / 120)& 0xFF);
						message = "Image Quality :"+ quality ;

						break;
					case MESSAGE_DETECT_QUALITY_CMD:
						pquality =((char) ((byte)unStuffed[8] * 100 / 120)& 0xFF);

						message = "Presence Quality :"+pquality ;
						break;
					case MESSAGE_IMAGE_CMD:
						break;
					case MESSAGE_ENROLL_CMD:
						break;
					default:
						break;
					}

				}
				else //Syncronous Message
				{

					byte [] data =new byte[unStuffed.length - 3]; //removing RC and CRC1 CRC2
					System.arraycopy(unStuffed, 1, data, 0, data.length);//DATA with FUNCID
					System.out.println("Packet ID is coming ->"+pac_ID+" is equals "+FIRST_DATA_PACKET_ID);
					if(pac_ID != FIRST_DATA_PACKET_ID){
						packet++;
						buffer.appendBytes(data);
						data =null;

					}
					else{
						packet =1;

						buffer.appendBytes(processPacket(data));

					}
				}

			}while(hasNextPacket);

		} catch (Exception e) {
			throw e;
		}
		/*			FPReset();*/
		//createFPImage(buffer.getBuffer(),5);
		String St = HexString.bufferToHex(buffer.getBuffer());
		System.out.println("Read byte data"+St);
		return buffer.getBuffer();
	}
	public String getCommandMessage(byte messageByte) {
		String message = "";
		switch ((int) messageByte) {
		case MORPHO_FINGER_OK:
			message = "Finger acquisition success";
			break;
		case MORPHO_MOVE_FINGER_UP:
			message = "Move finger up";
			break;
		case MORPHO_MOVE_FINGER_LEFT:
			message = "Move finger left";
			break;
		case MORPHO_MOVE_FINGER_DOWN:
			message = "Move finger down";
			break;
		case MORPHO_MOVE_FINGER_RIGHT:
			message = "Move finger right";
			break;
		case MORPHO_PRESS_FINGER_HARDER:
			message = "Press finger harder";
			break;
		case MORPHO_LATENT:
			message = "Please move the finger";
			break;
		case MORPHO_MOVE_NO_FINGER:
			message = "No finger is detected";
			break;
		case MORPHO_REMOVE_FINGER:
			message = "User must remove his finger";
			break;
		}
		return message;
	}
	private byte[] processPacket(byte[] data) {
		TIMESCOUNT++;

		byte[] imgData = null;
		try {
			byte[] len = new byte[2];
			len[0] = data[2];
			len[1] = data[1];
			totalSize = Integer.parseInt(HexString.bufferToHex(len), 16);
		} catch (Exception e) {
		}

		int offset = (((HexString.bufferToHex(data, 0, 20))
				.indexOf("FFFFFFFF02")) + 10) / 2;
		fpTemplate = new byte[getLen(data, offset, false)];
		offset += 2;
		System.arraycopy(data, offset, fpTemplate, 0, fpTemplate.length);
		fpData.addElement(fpTemplate);
		offset += fpTemplate.length;
		//FP TEMPLATE DISPLAY
		//  BluetoothSearch.append("FP Template:");
		//  for(int j=0;j<fpTemplate.length;j++)
		//     BluetoothSearch.append(""+(byte)fpTemplate[j]);
		//  Display.getDisplay(thisMidlet).setCurrent(BluetoothSearch);

		if ((data[offset + 7] == 0x00) && (data[offset + 8] == (byte) 0x0A)) {
			offset += 7;
		} else {
			offset += 3;
		}
		byte[] imgHeader = new byte[12];
		System.arraycopy(data, offset, imgHeader, 0, imgHeader.length);

		imgData = new byte[data.length - offset];
		System.arraycopy(data, offset, imgData, 0, imgData.length);
		//RAW IMAGE DISPLAY
		//  BluetoothSearch.append("Image Data:");
		//for(int j=0;j<imgData.length;j++)
		//.append(""+(byte)imgData[j]);
		//  Display.getDisplay(thisMidlet).setCurrent(BluetoothSearch);
		//createFPImage(imgData,TIMESCOUNT);
		return imgData;
	}
	public int getLen(byte[] data, int pos, boolean isMSBLSB) {

		byte[] len = new byte[2];

		if (isMSBLSB) {
			len[0] = data[pos];
			len[1] = data[pos + 1];
		} else {
			len[0] = data[pos + 1];
			len[1] = data[pos];
		}
		return Integer.parseInt(HexString.bufferToHex(len), 16);

	}
}
