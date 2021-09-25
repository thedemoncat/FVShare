package com.freevisiontech.fvmobile.callback.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import com.freevisiontech.fvmobile.common.StateScan;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class PeriodNameScanCallback extends PeriodScanCallback {
    private AtomicBoolean hasFound = new AtomicBoolean(false);
    private String name;

    public PeriodNameScanCallback(String name2) {
        this.name = name2;
        if (name2 == null) {
            throw new IllegalArgumentException("start scan, name can not be null!");
        }
    }

    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (!this.hasFound.get() && device != null && device.getName() != null && this.name.equalsIgnoreCase(device.getName().trim())) {
            this.hasFound.set(true);
            if (this.viseBluetooth != null) {
                this.viseBluetooth.stopLeScan((BluetoothAdapter.LeScanCallback) this);
                this.viseBluetooth.setScanState(StateScan.SCAN_SUCCESS);
            }
            onDeviceFound(new BluetoothLeDevice(device, rssi, scanRecord, System.currentTimeMillis()));
        }
    }
}
