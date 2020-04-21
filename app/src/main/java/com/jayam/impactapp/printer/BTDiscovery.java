package com.jayam.impactapp.printer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Pattern;


import com.jayam.impactapp.R;
import com.prowesspride.api.Setup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class BTDiscovery extends Activity implements OnClickListener {

    /** CONST: device type bluetooth 2.1 */
    public static final int DEVICE_TYPE_BREDR = 0x01;
    /** CONST: device type bluetooth 4.0 ble */
    public static final int DEVICE_TYPE_BLE = 0x02;
    /** CONST: device type bluetooth double mode */
    public static final int DEVICE_TYPE_DUMO = 0x03;
    public final static String EXTRA_DEVICE_TYPE = "android.bluetooth.device.extra.DEVICE_TYPE";
    /** Discovery is Finished */
    private boolean _bdiscoveryFinished;
    private BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
    /** bluetooth List View */
    private ListView mlvList = null;
    /**
     * Storage for the found bluetooth devices format:<MAC, <Key,Val>><br>
     * Key=[RSSI/NAME/COD(class of device)/BOND/UUID]
     */
    private Hashtable<String, Hashtable<String, String>> mhtFDS = null;
    /**
     * (List of storage arrays for display) dynamic array of objects / **
     * ListView's
     */
    private ArrayList<HashMap<String, Object>> malListItem = null;
    /** SimpleAdapter object (list display container objects) */
    Context context = this;
    private SimpleAdapter msaListItemAdapter = null;
    /**
     * Scan for Bluetooth devices. (broadcast listener)
     */

    private BroadcastReceiver _foundReceiver = new BroadcastReceiver() {
	@Override
	public void onReceive(Context context, Intent intent) {
	    /* bluetooth device profiles */
	    Hashtable<String, String> htDeviceInfo = new Hashtable<String, String>();
	    Log.d(getString(R.string.app_name), ">>Scan for Bluetooth devices");
	    /* get the search results */
	    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	    /* create found device profiles to htDeviceInfo */
	    Bundle b = intent.getExtras();
	    htDeviceInfo.put("RSSI", String.valueOf(b.get(BluetoothDevice.EXTRA_RSSI)));
	    if (null == device.getName()) {
		htDeviceInfo.put("NAME", "Null");
	    } else {
		htDeviceInfo.put("NAME", device.getName());
	    }
	    htDeviceInfo.put("COD", String.valueOf(b.get(BluetoothDevice.EXTRA_CLASS)));
	    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
		htDeviceInfo.put("BOND", getString(R.string.actDiscovery_bond_bonded));
	    } else {
		htDeviceInfo.put("BOND", getString(R.string.actDiscovery_bond_nothing));
	    }
	    String sDeviceType = String.valueOf(b.get(EXTRA_DEVICE_TYPE));
	    if (!sDeviceType.equals("null")) {
		htDeviceInfo.put("DEVICE_TYPE", sDeviceType);
	    } else {
		htDeviceInfo.put("DEVICE_TYPE", "-1");
	    }
	    /* adding scan to the device profiles */
	    mhtFDS.put(device.getAddress(), htDeviceInfo);

	    /* Refresh show list */
	    showDevices();
	}
    };

    /**
     * Bluetooth scanning is finished processing.(broadcast listener)
     */
    private BroadcastReceiver _finshedReceiver = new BroadcastReceiver() {
	@Override
	public void onReceive(Context context, Intent intent) {
	    Log.d(getString(R.string.app_name), ">>Bluetooth scanning is finished");
	    _bdiscoveryFinished = true; // set scan is finished
	    unregisterReceiver(_foundReceiver);
	    unregisterReceiver(_finshedReceiver);
	    if (null != mhtFDS && mhtFDS.size() > 0) {
		Toast.makeText(BTDiscovery.this, getString(R.string.actDiscovery_msg_select_device), Toast.LENGTH_SHORT)
			.show();
	    } else {
		Toast.makeText(BTDiscovery.this, getString(R.string.actDiscovery_msg_not_find_device),
			Toast.LENGTH_LONG).show();
	    }
	}
    };

    Button scan_btn, close_btn;
    static Setup impressSetUp = new Setup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	impressSetUp = new Setup();
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	if (mBT.isDiscovering()) {
	    mBT.cancelDiscovery();
	}
    }

    private void startSearch() {
	_bdiscoveryFinished = false;
	if (null == mhtFDS) {
	    this.mhtFDS = new Hashtable<String, Hashtable<String, String>>();
	} else {
	    this.mhtFDS.clear();
	}
	/* Register Receiver */
	IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	registerReceiver(_finshedReceiver, discoveryFilter);
	IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	registerReceiver(_foundReceiver, foundFilter);
	mBT.startDiscovery();// start scan
	this.showDevices(); // the first scan clear show list
    }

    private String toDeviceTypeString(String sDeviceTypeId) {
	Pattern pt = Pattern.compile("^[-\\+]?[\\d]+$");
	if (pt.matcher(sDeviceTypeId).matches()) {
	    switch (Integer.valueOf(sDeviceTypeId)) {
	    case DEVICE_TYPE_BREDR:
		return getString(R.string.device_type_bredr);
	    case DEVICE_TYPE_BLE:
		return getString(R.string.device_type_ble);
	    case DEVICE_TYPE_DUMO:
		return getString(R.string.device_type_ble);
	    default:
		return getString(R.string.device_type_bredr);
	    }
	} else {
	    return sDeviceTypeId;
	}
    }

    /* Show devices list */
    protected void showDevices() {

    }

    private class scanDeviceTask extends AsyncTask<String, String, Integer> {
	/** Constants: Bluetooth is not turned on */
	private static final int RET_BLUETOOTH_NOT_START = 0x0001;
	/** Constant: the device search complete */
	private static final int RET_SCAN_DEVICE_FINISHED = 0x0002;
	/** Wait a Bluetooth device starts the maximum time (in S) */
	private static final int miWATI_TIME = 10;
	/** Every thread sleep time (in ms) */
	private static final int miSLEEP_TIME = 150;
	/** Process waits prompt box */
	private ProgressDialog mpd = null;

	@Override
	public void onPreExecute() {
	    this.mpd = new ProgressDialog(BTDiscovery.this);
	    this.mpd.setMessage(getString(R.string.actDiscovery_msg_scaning_device));
	    this.mpd.setCancelable(true);
	    this.mpd.setCanceledOnTouchOutside(true);
	    this.mpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
		    _bdiscoveryFinished = true;
		}
	    });
	    this.mpd.show();

	    startSearch();
	}

	@Override
	protected Integer doInBackground(String... params) {
	    if (!mBT.isEnabled()) {
		return RET_BLUETOOTH_NOT_START;
	    }
	    int iWait = miWATI_TIME * 1000;
	    // Wait miSLEEP_TIME seconds, start the Bluetooth device before you
	    // start scanning
	    while (iWait > 0) {
		if (_bdiscoveryFinished) {
		    return RET_SCAN_DEVICE_FINISHED;
		} else {
		    iWait -= miSLEEP_TIME;
		}
		SystemClock.sleep(miSLEEP_TIME);
		;
	    }
	    return RET_SCAN_DEVICE_FINISHED;
	}

	@Override
	public void onProgressUpdate(String... progress) {
	}

	@Override
	public void onPostExecute(Integer result) {
	    if (this.mpd.isShowing()) {
		this.mpd.dismiss();
	    }

	    if (mBT.isDiscovering()) {
		mBT.cancelDiscovery();
	    }

	    if (RET_SCAN_DEVICE_FINISHED == result) {

	    } else if (RET_BLUETOOTH_NOT_START == result) {
		Toast.makeText(BTDiscovery.this, getString(R.string.actDiscovery_msg_bluetooth_not_start),
			Toast.LENGTH_SHORT).show();
	    }
	}
    }

    @Override
    public void onClick(View v) {

    }

    // Exit confirmation dialog box
    public void dlgExit() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    dlgExit();
	}
	return super.onKeyDown(keyCode, event);
    }

}
