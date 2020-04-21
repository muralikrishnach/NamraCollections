package com.jayam.impactapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.PrintListner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.printer.BluetoothChatService;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import mmsl.DeviceUtility.DeviceBluetoothCommunication;
import mmsl.DeviceUtility.DeviceCallBacks;
import mmsl.GetPrintableImage.GetPrintableImage;

public class PrintUtils implements DeviceCallBacks {
    public DeviceBluetoothCommunication bluetoothCommunication = null;
    private PrintListner listner;
    private Context context;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String TOAST = "toast";
    PrintDetailsDO detailsDO;
    BluetoothDevice mmDevice;
    int count = 1;
    int[] prnRasterImg;
    int image_width;
    int image_height;

    public PrintUtils(Context context, PrintListner listner) {
	this.context = context;
	this.listner = listner;
	bluetoothCommunication = new DeviceBluetoothCommunication();
    }

    public PrintUtils(Context context) {
	this.context = context;
	bluetoothCommunication = new DeviceBluetoothCommunication();
    }

    public void print() {
	 BluetoothAdapter mBluetoothAdapter =
	 BluetoothAdapter.getDefaultAdapter();
	 if (!mBluetoothAdapter.isEnabled())
 {
	 Intent enableIntent = new
	Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
 context.startActivity(enableIntent);
	 }
	 else
	 {
	detailsDO = listner.getprintObject();
	connectDevice();
	 }
    }

    @SuppressLint("NewApi")
    private void connectDevice() {
	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDOs = intialParametrsBL.SelectAll(this.context);
	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
	String printType = intialParametrsDO.PrintType;
	// String printType = "5";
	Log.v("mfimo", "Print utils DATA " + printType);
	// B8:51:47:66:06:00 //evolute
	// 00:12:6F:84:B8:C8 //maestro mp
	// 00:12:6F:73:D9:C9 //maestro vb
	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	Set<BluetoothDevice> pairedDevices1 = mBluetoothAdapter.getBondedDevices();
	if (pairedDevices1.size() > 0) {
	    for (BluetoothDevice devices : pairedDevices1) {
	    	Log.v("mfimo", "devices.getAddress()" + devices.getAddress());
		Log.v("mfimo",
			devices.getName() + "asdsd " + devices.getAddress() + "sdafasdf" + devices.getBondState());
	    }
	}


	mChatService = new BluetoothChatService(context, mHandler);
	 String pAddress = SharedPrefUtils.getKeyValue(context,
	 AppConstants.pref_name, AppConstants.printeraddress);
//	String pAddress = "00:12:6F:73:D9:C9";
	Log.v("mfimo", "Print utils" + pAddress);
	Log.v("mfimo", "printType utils" + printType);

	Log.v("mfimo", "Print utils ADDRESSsss  DATA" + pAddress);
	if (printType.equals("4")) {
	    mmDevice = mBluetoothAdapter.getRemoteDevice(pAddress);
	} else if (printType.equals("5")) {
	    mmDevice = mBluetoothAdapter.getRemoteDevice(pAddress);
	    try {
		bluetoothCommunication.StartConnection(mmDevice, this);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	} else {

		Log.v("mfimo", "Print utils Bond sss" + pAddress);
	    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

	    Log.v("mfimo", "Print utils Bond pairedDevices.size" + pairedDevices.size());

	    if (pairedDevices.size() > 0) {
		for (BluetoothDevice devices : pairedDevices) {

			Log.v("mfimo", "printType pAddress sss" + pAddress);
		    if (pAddress.equals("")) {
			Log.v("mfimo if", devices.getName() + "asdsd " + devices.getAddress() + "sdafasdf"
				+ devices.getBondState());
			if (devices.getName().equals("ANTHERMAL")) {
			    mmDevice = devices;
			    break;
			}
		    } else {
			// MP300 is the name of the bluetooth printer device
		    	Log.v("mfimo", "printType else  su" + pAddress);
		    	Log.v("mfimo", "printType devices.getAddress() final else" + devices.getAddress() );

		    	Log.v("mfimo else", devices.getName() + "asdsd " + devices.getAddress() + "sdafasdf"
				+ devices.getBondState());
		    	Log.v("mfimo", "printType devices.getAddress() pAddress" + pAddress );

			if (devices.getAddress().equals(pAddress)) {
			    mmDevice = devices;
			    break;
			}
		    }
		}
	    }
	}


	Log.v("mfimo", "Print utils mmDevice  DATA" + mmDevice);
	// Attempt to connect to the device
	if (mmDevice != null) {
	    // Attempt to connect to the device
	    Toast.makeText(context, "Device connecting " + mmDevice.getAddress(), Toast.LENGTH_SHORT).show();
	    if (!printType.equals("5")) {
		mChatService.connect(mmDevice, detailsDO);
	    }
	} else {
	    Toast.makeText(context, "Your printer is not available", Toast.LENGTH_SHORT).show();
	}
    }

    public String getConnection(String PrintAddr) {
	String status = null;
	// PrintAddr = "B8:51:47:66:06:00";
	Log.v("mfimo", "impactutils" + PrintAddr);
	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDOs = intialParametrsBL.SelectAll(this.context);
	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
	String printType = intialParametrsDO.PrintType;
	Log.v("mfimo", "impactutilstype" + printType);
	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	// if(!printType.equals("5")) {
	// {
	mChatService = new BluetoothChatService(context, mHandler);
	// }
	// Toast.makeText(context, "Device connecting "+PrintAddr,
	// Toast.LENGTH_SHORT).show();
	BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(PrintAddr);
	status = mChatService.connect1(device);
	// }else{
	//// Toast.makeText(context, "Device connecting "+PrintAddr,
	// Toast.LENGTH_SHORT).show();
	// BluetoothDevice device =
	// mBluetoothAdapter.getRemoteDevice(PrintAddr);
	// bluetoothCommunication.StartConnection(device, this);
	// bluetoothCommunication.GetPrinterStatus();
	// // bluetoothCommunication.GetPrinterStatus();
	// }
	return status;
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MESSAGE_WRITE:
		// byte[] writeBuf = (byte[]) msg.obj;
		// construct a string from the buffer
		break;
	    case MESSAGE_TOAST:
		Toast.makeText(context, msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
		break;
	    }
	}
    };

    public void readImagedata3(final PrintDetailsDO detailsDO, String name, String LogoPrinting) {

		Log.v("","LogoPrinting"+LogoPrinting);
		Log.v("","name"+name);

	if (LogoPrinting.equals("1")) {
	    if (name.equals("ffslmobile") || name.equals("mfimouat") || name.equals("mfimouat2")
		    || name.equals("mfimodev")) {
		Log.d("mfimo", "ffsllogo");
		printLogo_Evolute_FFSL();
	    } else if (name.equals("dishamobile") || name.equals("dishamobile2") || name.equals("mfimotest")) {
		printLogo_Evolute_DISHA();
	    } else if (name.equals("lokiblmobile") || name.equals("lokibltest1") || name.equals("lokibltest2")) {
		printLogo_Evolute_LOK();
	    }
	}

	for (int i = 0; i < detailsDO.allabels.size(); i++) {
	    String lable = detailsDO.allabels.get(i);
	    String value = detailsDO.alValues.get(i);

	    try {
		Thread.sleep(100);
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    if (value.equals("true")) {
		PrinterData_Maestro(lable, true);
		bluetoothCommunication.GetPrinterStatus();
	    } else {
		PrinterData_Maestro(lable, false);
		bluetoothCommunication.GetPrinterStatus();
	    }
	}
    }

    private void printLogo_Evolute_FFSL() {
	// TODO Auto-generated method stub
	try {
	    InputStream inputStream = context.getAssets().open("ffsl_logo.bmp");
	    Bitmap bit = BitmapFactory.decodeStream(inputStream);
	    GetPrintableImage getPrintableImage = new GetPrintableImage();
	    prnRasterImg = getPrintableImage.GetPrintableArray(context, bit.getWidth(), bit);
	    image_width = getPrintableImage.getPrintWidth();
	    image_height = getPrintableImage.getPrintHeight();
	    getPrintableImage = null;

	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    // sleep(1000);

	    byte[] CommandImagePrint = new byte[prnRasterImg.length + 5];

	    CommandImagePrint[0] = 0x1B; // Command to for bit image
					 // mode
					 // please refer the previous

	    // document
	    CommandImagePrint[1] = 0x23; // Exc #
	    CommandImagePrint[2] = (byte) image_width; // 8 Vertical
						       // Dots(Heights)
						       // &
						       // Single Width
						       // Mode

	    // selected
	    CommandImagePrint[3] = (byte) (image_height / 256);// f8 // Decimal
							       // 248
							       // since
							       // the
							       // Image
							       // width
							       // is

	    // 248 Pixels as mentioned above
	    CommandImagePrint[4] = (byte) (image_height % 256);

	    for (int i = 0; i < prnRasterImg.length; i++) {
		CommandImagePrint[i + 5] = (byte) (prnRasterImg[i] & 0xFF);
	    }

	    bluetoothCommunication.SendData(CommandImagePrint); // CommandImagePrint.length
	} catch (Exception ioe) {
	    System.out.println("Problems reading from or writing to serial port." + ioe.getMessage());

	}
    }

    private void printLogo_Evolute_DISHA() {
	// TODO Auto-generated method stub
	try {
	    InputStream inputStream = context.getAssets().open("disha_logo.bmp");
	    Bitmap bit = BitmapFactory.decodeStream(inputStream);
	    GetPrintableImage getPrintableImage = new GetPrintableImage();
	    prnRasterImg = getPrintableImage.GetPrintableArray(context, bit.getWidth(), bit);
	    image_width = getPrintableImage.getPrintWidth();
	    image_height = getPrintableImage.getPrintHeight();
	    getPrintableImage = null;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    // sleep(1000);

	    byte[] CommandImagePrint = new byte[prnRasterImg.length + 5];

	    CommandImagePrint[0] = 0x1B; // Command to for bit image
					 // mode
					 // please refer the previous

	    // document
	    CommandImagePrint[1] = 0x23; // Exc #
	    CommandImagePrint[2] = (byte) image_width; // 8 Vertical
						       // Dots(Heights)
						       // &
						       // Single Width
						       // Mode

	    // selected
	    CommandImagePrint[3] = (byte) (image_height / 256);// f8 //
							       // Decimal
							       // 248
							       // since
							       // the
							       // Image
							       // width
							       // is

	    // 248 Pixels as mentioned above
	    CommandImagePrint[4] = (byte) (image_height % 256);

	    for (int i = 0; i < prnRasterImg.length; i++) {
		CommandImagePrint[i + 5] = (byte) (prnRasterImg[i] & 0xFF);
	    }

	    bluetoothCommunication.SendData(CommandImagePrint); // CommandImagePrint.length
	} catch (Exception ioe) {
	    System.out.println("Problems reading from or writing to serial port." + ioe.getMessage());

	}
    }

    private void printLogo_Evolute_LOK() {
	// TODO Auto-generated method stub
	try {
	    InputStream inputStream = context.getAssets().open("lok_ibl_logo.bmp");
	    Bitmap bit = BitmapFactory.decodeStream(inputStream);
	    GetPrintableImage getPrintableImage = new GetPrintableImage();
	    prnRasterImg = getPrintableImage.GetPrintableArray(context, bit.getWidth(), bit);
	    image_width = getPrintableImage.getPrintWidth();
	    image_height = getPrintableImage.getPrintHeight();
	    getPrintableImage = null;

	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    // sleep(1000);

	    byte[] CommandImagePrint = new byte[prnRasterImg.length + 5];

	    CommandImagePrint[0] = 0x1B; // Command to for bit image
					 // mode
					 // please refer the previous

	    // document
	    CommandImagePrint[1] = 0x23; // Exc #
	    CommandImagePrint[2] = (byte) image_width; // 8 Vertical
						       // Dots(Heights)
						       // &
						       // Single Width
						       // Mode

	    // selected
	    CommandImagePrint[3] = (byte) (image_height / 256);// f8 //
							       // Decimal
							       // 248
							       // since
							       // the
							       // Image
							       // width
							       // is

	    // 248 Pixels as mentioned above
	    CommandImagePrint[4] = (byte) (image_height % 256);

	    for (int i = 0; i < prnRasterImg.length; i++) {
		CommandImagePrint[i + 5] = (byte) (prnRasterImg[i] & 0xFF);
	    }

	    bluetoothCommunication.SendData(CommandImagePrint); // CommandImagePrint.length
	} catch (Exception ioe) {
	    System.out.println("Problems reading from or writing to serial port." + ioe.getMessage());

	}
    }

    public int PrinterData_Maestro(String data, boolean mAlign) {
	if (mAlign) {
	    int space = 0;
	    if (data.length() < 42) {
		space = 42 - data.length();
		for (int i = 0; i < space; i++) {
		    if (i < (space / 2)) {
			data = " " + data;
		    } else {
			data = data + " ";
		    }
		}
	    }
	}
	System.out.println(":" + data + ":" + data.length());
	int i = 0, j = 0, col = 0, la = 0;
	char[] tempBuffer = new char[1000];
	int[] dataBuffer = new int[5000];
	byte[] b = new byte[2];
	System.out.println(data);
	data.getChars(0, data.length(), tempBuffer, col);
	// int totDataLen = data.length();
	/////////////////////// PACKET FORMAT /////////////////////
	for (i = 0; la < data.length();) {
	    for (j = 0; j < 42; j++) {
		if (la < data.length()) {
		    dataBuffer[i++] = tempBuffer[la++];
		} else {
		    break;
		}
	    }
	}
	/////////////////////// PACKET FORMAT /////////////////////
	try {
	    // SendPrint_Evloute(dataBuffer, i,3);
	    SendPrint_Maestro(data);
	} catch (IOException ex) {

	}
	return 1;
    }

    public int SendPrint_Maestro(String Data) throws IOException {
	byte FontStyleVal = 0;
	bluetoothCommunication.GetPrinterStatus();
	if (bluetoothCommunication != null) {
	    FontStyleVal &= 0xFC;
	    FontStyleVal |= 0x02;
	    bluetoothCommunication.setPrinterFont(FontStyleVal);
	    bluetoothCommunication.SendData(Data.getBytes());
	    bluetoothCommunication.LineFeed();
	} else {
	    System.out.println("socket is inactive");
	}
	return 1;
    }

    @Override
    public void onConnectComplete() {
	// TODO Auto-generated method stub
	Log.d("mfimo", "onConnectComplete");
	IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
	ArrayList<IntialParametrsDO> alIntialParametrsDOs = intialParametrsBL.SelectAll(this.context);
	IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
	String serverurl = intialParametrsDO.ServerUrl;
	String[] URL = serverurl.split("/");
	String name = URL[URL.length - 1];
	String LogoPrinting = intialParametrsDO.LogoPrinting;
	String printType = intialParametrsDO.PrintType;
	Log.d("mfimo", "blue tooth chat service connected method" + printType);
	Log.d("mfimo", "Print Type: " + printType + "	Name:   " + name + "	Logo: " + LogoPrinting);
	String transtype = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.TransactioType);
	String transcode = SharedPrefUtils.getKeyValue(context, AppConstants.pref_name, AppConstants.TransactionCode);
	if (transtype != null && transtype.equalsIgnoreCase("regular")) {
	    Log.d("mfimo", "type: " + transtype + " code: " + transcode);
	    if (transcode != null) {
		TrnsactionsBL tbl = new TrnsactionsBL();
		tbl.updateActualPrintFlag(transcode, "Y",this.context);
		KeyValue keyValue_transtype = new KeyValue(AppConstants.TransactioType, "");
		SharedPrefUtils.setValue(context, AppConstants.pref_name, keyValue_transtype);
		KeyValue keyValue_transcode = new KeyValue(AppConstants.TransactionCode, "");
		SharedPrefUtils.setValue(context, AppConstants.pref_name, keyValue_transcode);
	    }
	} else if (transtype != null && transtype.equalsIgnoreCase("od")) {
	    Log.d("mfimo", "type: " + transtype + " code: " + transcode);
	    if (transcode != null) {
		Transaction_OD_BL tbl = new Transaction_OD_BL();
		tbl.updateActualPrintFlag(transcode, "Y",this.context);
		KeyValue keyValue_transtype = new KeyValue(AppConstants.TransactioType, "");
		SharedPrefUtils.setValue(context, AppConstants.pref_name, keyValue_transtype);
		KeyValue keyValue_transcode = new KeyValue(AppConstants.TransactionCode, "");
		SharedPrefUtils.setValue(context, AppConstants.pref_name, keyValue_transcode);
	    }
	}
	readImagedata3(detailsDO, name, LogoPrinting);
    }

    @Override
    public void onConnectionFailed() {
	// TODO Auto-generated method stub
	Log.d("mfimo", "onConnectionFailed");
    }

    @Override
    public void onHighHeadTemperature() {
	// TODO Auto-generated method stub
	Log.d("mfimo", "onHighHeadTemperature");
    }

    @Override
    public void onImproperVoltage() {
	// TODO Auto-generated method stub
	Log.d("mfimo", "onImproperVoltage");
    }

    @Override
    public void onLowHeadTemperature() {
	// TODO Auto-generated method stub
	Log.d("mfimo", "onLowHeadTemperature");
    }

    @Override
    public void onOutofPaper() {
	// TODO Auto-generated method stub
	Log.d("mfimo", "onOutofPaper");
    }

    @Override
    public void onPlatenOpen() {
	// TODO Auto-generated method stub
	Log.d("mfimo", "onPlatenOpen");
    }

    @Override
    public void onSuccessfulPrintIndication() {
	// TODO Auto-generated method stub
	Log.d("mfimo", "onSuccessfulPrintIndication");
	try {
	    count++;
	    Log.d("mfimo", count + "" + detailsDO.allabels.size());
	    if (count == detailsDO.allabels.size()) {
		// bluetoothCommunication.StopConnection();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void onCPUSmartCardCommandDataRecieved(byte[] arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onCancelledCommand() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onCardNotSupported() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onCommandNotSupported() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onCommandRecievedWhileAnotherRunning() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onCommandRecievedWhileProcessing() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onCorruptDataRecieved() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onCorruptDataSent() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onCryptographicError() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onErrorOccured() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onErrorOccuredWhileProccess() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onErrorReadingSmartCard() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onErrorWritingSmartCard() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onFalseFingerDetected() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onFingerImageRecieved(byte[] arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onFingerPrintTimeout() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onFingerScanStarted(int arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onFingerTooMoist() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onFingeracquisitioncompeted(String arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onImproveSwipe() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onInternalFPModuleCommunicationerror() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onInvalidCommand() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onLatentFingerHard(String arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onMSRDataRecieved(String arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onMoveFingerDown() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onMoveFingerLeft() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onMoveFingerRight() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onMoveFingerUP() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onNoData() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onNoResponseFromCard() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onNoSmartCardFound() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onOperationNotSupported() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onParameterOutofRange() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onPlaceFinger() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onPressFingerHard() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onRemoveFinger() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onSameFinger() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onSmartCardDataRecieved(byte[] arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onSmartCardPresent() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTemplateRecieved(byte[] arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onVerificationSuccessful(int arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onVerificationfailed() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onWriteToSmartCardSuccessful() {
	// TODO Auto-generated method stub

    }
}
