package com.freevisiontech.fvmobile.callback.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import com.freevisiontech.fvmobile.common.StateScan;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class PeriodMacScanCallback extends PeriodScanCallback {
    private AtomicBoolean hasFound = new AtomicBoolean(false);
    private String mac;

    public PeriodMacScanCallback(String mac2) {
        this.mac = mac2;
        if (mac2 == null) {
            throw new IllegalArgumentException("start scan, mac can not be null!");
        }
    }

    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (!this.hasFound.get() && device != null && device.getAddress() != null && this.mac.equalsIgnoreCase(device.getAddress().trim())) {
            this.hasFound.set(true);
            if (this.viseBluetooth != null) {
                this.viseBluetooth.stopLeScan((BluetoothAdapter.LeScanCallback) this);
                this.viseBluetooth.setScanState(StateScan.SCAN_SUCCESS);
            }
            onDeviceFound(new BluetoothLeDevice(device, rssi, scanRecord, System.currentTimeMillis()));
        }
    }
}
