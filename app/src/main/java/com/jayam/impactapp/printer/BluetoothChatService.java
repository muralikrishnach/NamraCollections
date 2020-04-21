/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jayam.impactapp.printer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.PrintDetailsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.SharedPrefUtils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for incoming
 * connections, a thread for connecting with a device, and a thread for
 * performing data transmissions when connected.
 */
public class BluetoothChatService {

	// Debugging
	private static final String TAG = "BluetoothConnectivityService";
	private static final boolean D = true;
	// Member fields
	private final BluetoothAdapter mAdapter;
	private ConnectThread mConnectThread;
	private ConnectThread1 mConnectThread1;
	private ConnectedThread mConnectedThread;
	private int mState;
	public final static String UUID_STR = "00001101-0000-1000-8000-00805F9B34FB";
	private boolean mbConectOk = false;
	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming
	// connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing
	// connection
	public static final int STATE_CONNECTED = 3; // now connected to a remote
	private Context mContext; // device
	public static InputStream inputStream = null;
	public static OutputStream outputStream = null;
	BluetoothSocket mmSocket = null;
	BluetoothDevice bluetoothDevice = null;
	Handler mHandler;
	byte[] fpDatabytes = null;
	byte ENROLL_ID = 0x21;
	byte ILV_OK = 0x00;
	byte ILVSTS_OK = 0x00;
	byte ISO_197942 = (byte) 0x6E;
	byte ILVSTS_HIT = 0x01;
	byte ILVSTS_NO_HIT = 0x02;

	private String totAmt;
	private String totAmt_collected;
	private PrintDetailsDO printDetailsDO;

	private ArrayList<IntialParametrsDO> alIntialParametrsDOs;
	private IntialParametrsBL intialParametrsBL;

	/**
	 * Constructor. Prepares a new BluetoothChat session.
	 *
	 * @param context
	 *            The UI Activity Context
	 * @param handler
	 *            A Handler to send messages back to the UI Activity
	 */
	public BluetoothChatService(Context context, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = STATE_NONE;
		mHandler = handler;
		mContext = context;


	}

	/**
	 * Set the current state of the chat connection
	 *
	 * @param state
	 *            An integer defining the current connection state
	 */
	private synchronized void setState(int state) {
		if (D) {
			Log.d(TAG, "setState() " + mState + " -> " + state);
		}
		mState = state;
	}

	/**
	 * Return the current connection state.
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 *
	 * @param device
	 *            The BluetoothDevice to connect
	 */
	public synchronized void connect(BluetoothDevice device, PrintDetailsDO detailsDO) {
		if (D) {
			Log.d(TAG, "connect to: " + device);
		}

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device, detailsDO);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	public synchronized String connect1(BluetoothDevice device) {
		if (D) {
			Log.d(TAG, "connect to: " + device);
		}

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			if (mConnectThread1 != null) {
				mConnectThread1.cancel();
				mConnectThread1 = null;
			}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
		mConnectThread1 = new ConnectThread1(device);
		String status = mConnectThread1.getStatus();
		// setState(STATE_CONNECTING);
		return status;
	}

	/**
	 * Cancel all the thread those are done with their action part
	 *
	 * @param socket
	 *            The BluetoothSocket on which the connection was made
	 * @param device
	 *            The BluetoothDevice that has been connected
	 */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, PrintDetailsDO detailsDO) {
		if (D) {
			Log.d(TAG, "connected");
		}
		mmSocket = socket;
		bluetoothDevice = device;
		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		// Start the thread to manage the connection and perform transmissions
		if (mmSocket == null) {
			System.out.println("Socket is null in the enrollprinter");
		} else {
			System.out.println("Socket is active");
		}
		mConnectedThread = new ConnectedThread(this.mmSocket, this.bluetoothDevice, this);
		intialParametrsBL = new IntialParametrsBL();
		alIntialParametrsDOs = intialParametrsBL.SelectAll(mContext);
		IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
		String printType = intialParametrsDO.PrintType;
		String serverurl = intialParametrsDO.ServerUrl;
		String[] URL = serverurl.split("/");
		String name = URL[URL.length - 1];
		String LogoPrinting = intialParametrsDO.LogoPrinting;
		Log.d("mfimo", "blue tooth chat service connected method" + printType);
		Log.d("mfimo", "Print Type: " + printType + "	Name:   " + name + "	Logo: " + LogoPrinting);
		String transtype = SharedPrefUtils.getKeyValue(mContext, AppConstants.pref_name, AppConstants.TransactioType);
		String transcode = SharedPrefUtils.getKeyValue(mContext, AppConstants.pref_name, AppConstants.TransactionCode);
		Log.d("mfimo", "type: " + transtype + " code: " + transcode);
		if (transtype != null && transtype.equalsIgnoreCase("regular")) {
			Log.d("mfimo", "type: " + transtype + " code: " + transcode);
			if (transcode != null) {
				TrnsactionsBL tbl = new TrnsactionsBL();
				tbl.updateActualPrintFlag(transcode, "Y",mContext);
				KeyValue keyValue_transtype = new KeyValue(AppConstants.TransactioType, "");
				SharedPrefUtils.setValue(mContext, AppConstants.pref_name, keyValue_transtype);
				KeyValue keyValue_transcode = new KeyValue(AppConstants.TransactionCode, "");
				SharedPrefUtils.setValue(mContext, AppConstants.pref_name, keyValue_transcode);
			}
		} else if (transtype != null && transtype.equalsIgnoreCase("od")) {
			Log.d("mfimo", "type: " + transtype + " code: " + transcode);
			if (transcode != null) {
				Transaction_OD_BL tbl = new Transaction_OD_BL();
				tbl.updateActualPrintFlag(transcode, "Y",mContext);
				KeyValue keyValue_transtype = new KeyValue(AppConstants.TransactioType, "");
				SharedPrefUtils.setValue(mContext, AppConstants.pref_name, keyValue_transtype);
				KeyValue keyValue_transcode = new KeyValue(AppConstants.TransactionCode, "");
				SharedPrefUtils.setValue(mContext, AppConstants.pref_name, keyValue_transcode);
			}
		} else if (transtype != null && transtype.equalsIgnoreCase("advance")) {
			Log.d("mfimo", "type: " + transtype + " code: " + transcode);
			if (transcode != null) {
				AdvanceDemandBL tbl = new AdvanceDemandBL();
				tbl.updateActualPrintFlag(transcode, "Y",mContext);
				KeyValue keyValue_transtype = new KeyValue(AppConstants.TransactioType, "");
				SharedPrefUtils.setValue(mContext, AppConstants.pref_name, keyValue_transtype);
				KeyValue keyValue_transcode = new KeyValue(AppConstants.TransactionCode, "");
				SharedPrefUtils.setValue(mContext, AppConstants.pref_name, keyValue_transcode);
			}
		}
		if (printType.equals("1")) {
			mConnectedThread.readImagedata(detailsDO, name, LogoPrinting);
			// mConnectedThread.readImagedata(detailsDO);
		} else if (printType.equals("3")) {




			mConnectedThread.readImagedata1(detailsDO, name, LogoPrinting);



			// mConnectedThread.readImagedata1(detailsDO);
		} else if (printType.equals("4")) {
			mConnectedThread.readImagedata2(detailsDO, name, LogoPrinting);
		}
		setState(STATE_CONNECTED);
	}

	public void enrollFinished() {
		Message msg = mHandler.obtainMessage(BluetoothChatActivity.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(BluetoothChatActivity.TOAST, "Enroll Finished, Start with Printing!");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}
	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 */

	// public void readImage(){mConnectedThread.readImagedata();}
	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		if (D) {
			Log.d(TAG, "stop");
		}
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		// Send a failure message back to the Activity
		Message msg = mHandler.obtainMessage(BluetoothChatActivity.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(BluetoothChatActivity.TOAST, "Unable to connect device");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		// Send a failure message back to the Activity
		Message msg = mHandler.obtainMessage(BluetoothChatActivity.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(BluetoothChatActivity.TOAST, "Device connection was lost");
		msg.setData(bundle);
		mHandler.sendMessage(msg);

	}

	/**
	 * This thread runs while attempting to make an outgoing connection with a
	 * device. It runs straight through; the connection either succeeds or
	 * fails.
	 */
	private class ConnectThread extends Thread {
		// private BluetoothSocket mmSocket;
		private BluetoothDevice mmDevice;
		private String messge;
		private int FontSize;
		private String totAmt;
		private String AmttobeCollected;
		private ArrayList<RegularDemandsDO> alArrayList;
		private PrintDetailsDO detailsDO;

		public ConnectThread(BluetoothDevice device, PrintDetailsDO detailsDO) {


//		
			mmDevice = device;
			this.FontSize = FontSize;
			this.messge = messge;
			this.alArrayList = alArrayList;
			this.detailsDO = detailsDO;
			BluetoothSocket tmp = null;
			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				final UUID uuidComm = UUID.fromString(UUID_STR);
				tmp = device.createInsecureRfcommSocketToServiceRecord(uuidComm);



			} catch (IOException e) {
				Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;

















//	    mmDevice = device;
//	    this.FontSize = FontSize;
//	    this.messge = messge;
//	    this.alArrayList = alArrayList;
//	    this.detailsDO = detailsDO;
//	    BluetoothSocket tmp = null;
//	    // Get a BluetoothSocket for a connection with the
//	    // given BluetoothDevice
//	    try {
//		Method m = null;
//		try {
//		    m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
//		} catch (SecurityException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
//		tmp = (BluetoothSocket) m.invoke(device, 1);
//		// tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//
//	    } catch (IllegalArgumentException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    } catch (IllegalAccessException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    } catch (InvocationTargetException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
//	    mmSocket = tmp;
//	    




		}

		@SuppressLint("NewApi")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
			ArrayList<IntialParametrsDO> alIntialParametrsDOs = intialParametrsBL.SelectAll(mContext);
			IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
			String printType = intialParametrsDO.PrintType;
			Log.d("mfimo", "blue tooth chat service connected thread" + printType);
			if (printType.equals("3")) {
				Log.i(TAG, "BEGIN mConnectThread");
				setName("ConnectThread");

				// Always cancel discovery because it will slow down a
				// connection
				mAdapter.cancelDiscovery();

				// Make a connection to the BluetoothSocket
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					// mmSocket.getRemoteDevice().
					mmSocket.connect();
				} catch (IOException e) {
					connectionFailed();
					e.printStackTrace();
					Log.e(this.toString(), "IOException " + e.getMessage());
					// Close the socket
					try {
						mmSocket.close();
					} catch (IOException e2) {
						Log.e(TAG, "unable to close() socket during connection failure", e2);
					}
					return;
				}

				// Reset the ConnectThread because we're done
				synchronized (BluetoothChatService.this) {
					mConnectThread = null;
				}

				// Start the connected thread
				connected(mmSocket, mmDevice, detailsDO);
			} else {
				Log.e(TAG, ".....create connection  1");
				// If a connection already exists, disconnect
				if (mbConectOk) {
					//  closeConn();
				}
				final BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mmDevice.getAddress());
				final UUID uuidComm = UUID.fromString(UUID_STR);
				try {

					mmSocket = device.createInsecureRfcommSocketToServiceRecord(uuidComm);
					Thread.sleep(2000);
					Log.e(TAG, ">>> Connecting ");
					mmSocket.connect();
					Log.e(TAG, ">>> CONNECTED SUCCESSFULLY");
					Thread.sleep(2000);
					outputStream = mmSocket.getOutputStream();// Get global
					// output stream
					// object
					inputStream = mmSocket.getInputStream(); // Get global
					// streaming input
					// object
					mbConectOk = true; // Device is connected successfully

				} catch (Exception e) {
					try {
						Thread.sleep(2000);
						Log.e(TAG, ">>>>>>           Try 2  ................!");
						mmSocket = device.createInsecureRfcommSocketToServiceRecord(uuidComm);
						Log.e(TAG, " Socket obtained");
						Thread.sleep(2000);
						Log.e(TAG, " Connecting againg ");
						mmSocket.connect();
						Log.e(TAG, " Successful connection 2nd time....... ");
						Thread.sleep(2000);
						outputStream = mmSocket.getOutputStream();// Get global
						// output
						// stream
						// object
						inputStream = mmSocket.getInputStream(); // Get global
						// streaming
						// input object
						mbConectOk = true;
					} catch (IOException e1) {
						Log.e(TAG, " Connection Failed by trying both ways....... ");
						e1.printStackTrace();
						//closeConn();// Disconnect
						Log.e(TAG, " Returning False");

					} catch (Exception ee) {
						Log.e(TAG, " Connection Failed due to other reasons....... ");
						ee.printStackTrace();
						//closeConn();// Disconnect
						Log.e(TAG, " Returning False");

					}
				}
				synchronized (BluetoothChatService.this) {
					mConnectThread = null;
				}
				connected(mmSocket, mmDevice, detailsDO);
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
				Log.e(TAG, "Socket is closed");
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	private class ConnectThread1 extends Thread {
		private BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private String messge;
		private int FontSize;
		private String totAmt;
		private String AmttobeCollected;
		private ArrayList<RegularDemandsDO> alArrayList;
		private PrintDetailsDO detailsDO;
		BluetoothSocket tmp = null;

		public ConnectThread1(BluetoothDevice device) {

			mmDevice = device;
			this.FontSize = FontSize;
			this.messge = messge;
			this.alArrayList = alArrayList;
			this.detailsDO = detailsDO;
			BluetoothSocket tmp = null;
			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				final UUID uuidComm = UUID.fromString(UUID_STR);
				tmp = device.createInsecureRfcommSocketToServiceRecord(uuidComm);



			} catch (IOException e) {
				Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;










//	    mmDevice = device;
//	    this.FontSize = FontSize;
//	    this.messge = messge;
//	    this.alArrayList = alArrayList;
//
//	    // Get a BluetoothSocket for a connection with the
//	    // given BluetoothDevice
//	    try {
//		Method m = null;
//		try {
//		    m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
//		} catch (SecurityException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
//		tmp = (BluetoothSocket) m.invoke(device, 1);
//		// tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//
//	    } catch (IllegalArgumentException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    } catch (IllegalAccessException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    } catch (InvocationTargetException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
//	    mmSocket = tmp;
		}

		@SuppressLint("NewApi")
		public String getStatus() {
			Log.i(TAG, "BEGIN mConnectThread");
			setName("ConnectThread");

			IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
			ArrayList<IntialParametrsDO> alIntialParametrsDOs = intialParametrsBL.SelectAll(mContext);
			IntialParametrsDO intialParametrsDO = alIntialParametrsDOs.get(0);
			String printType = intialParametrsDO.PrintType;
			Log.d("mfimo", "blue tooth chat service connected thread" + printType);
			if (printType.equals("3")) {
				Log.i(TAG, "BEGIN mConnectThread");
				setName("ConnectThread");

				// Always cancel discovery because it will slow down a
				// connection
				mAdapter.cancelDiscovery();

				// Make a connection to the BluetoothSocket
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					// mmSocket.getRemoteDevice().
					mmSocket.connect();
				} catch (IOException e) {
					connectionFailed();
					e.printStackTrace();
					Log.e(this.toString(), "IOException " + e.getMessage());
					// Close the socket
					try {
						mmSocket.close();
					} catch (IOException e2) {
						Log.e(TAG, "unable to close() socket during connection failure", e2);
					}
					String status = "Not Connected";
					return status;
				}

				// Reset the ConnectThread because we're done
				synchronized (BluetoothChatService.this) {
					mConnectThread = null;
				}

				String status = "Connected";
				return status;
			} else {
				Log.e(TAG, ".....create connection  1");
				// If a connection already exists, disconnect
				final BluetoothDevice device = BluetoothAdapter.getDefaultAdapter()
						.getRemoteDevice(mmDevice.getAddress());
				final UUID uuidComm = UUID.fromString(UUID_STR);
				try {

					mmSocket = device.createInsecureRfcommSocketToServiceRecord(uuidComm);
					Thread.sleep(2000);
					Log.e(TAG, ">>> Connecting ");
					mmSocket.connect();
					Log.e(TAG, ">>> CONNECTED SUCCESSFULLY");

				} catch (Exception e) {
					try {
						Thread.sleep(2000);
						Log.e(TAG, ">>>>>>           Try 2  ................!");
						mmSocket = device.createInsecureRfcommSocketToServiceRecord(uuidComm);
						Log.e(TAG, " Socket obtained");
						Thread.sleep(2000);
						Log.e(TAG, " Connecting againg ");
						mmSocket.connect();
						Log.e(TAG, " Successful connection 2nd time....... ");
					} catch (IOException e1) {
						Log.e(TAG, " Connection Failed by trying both ways....... ");
						e1.printStackTrace();
						//closeConn();// Disconnect
						Log.e(TAG, " Returning False");
						String status = "Not Connected";
						return status;
					} catch (Exception ee) {
						Log.e(TAG, " Connection Failed due to other reasons....... ");
						ee.printStackTrace();
						//closeConn();// Disconnect
						Log.e(TAG, " Returning False");
						String status = "Not Connected";
						return status;
					}
				}
				synchronized (BluetoothChatService.this) {
					mConnectThread = null;
				}

				// Reset the ConnectThread because we're done
				synchronized (BluetoothChatService.this) {
					mConnectThread1 = null;
				}

				String status = "Connected";
				return status;
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
				Log.e(TAG, "Socket is closed");
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	public void setvalues(String totAmt, String AmttoBeCollected) {
		this.totAmt = totAmt;
		this.totAmt_collected = AmttoBeCollected;
	}

	public void setDetailsObj(PrintDetailsDO printDetailsDO) {
		this.printDetailsDO = printDetailsDO;
	}

	public PrintDetailsDO getDetailsObj() {
		return printDetailsDO;
	}

	public String gettotAmt() {
		return totAmt;
	}

	public String getAmtTobeCollectd() {
		return totAmt_collected;
	}

	private void closeConn() {
		if (this.mbConectOk) {
			try {
				if (null != this.inputStream) {
					this.inputStream.close();
				}
				if (null != this.outputStream) {
					this.outputStream.close();
				}
				if (null != this.mmSocket) {
					this.mmSocket.close();
				}
				this.mbConectOk = false;// Mark the connection has been closed
			} catch (IOException e) {
				// Any part of the error, will be forced to close socket
				// connection
				this.inputStream = null;
				this.outputStream = null;
				this.mmSocket = null;
				this.mbConectOk = false;// Mark the connection has been closed
			}
		}
		Log.e(TAG, " Closed connection");
	}
}
