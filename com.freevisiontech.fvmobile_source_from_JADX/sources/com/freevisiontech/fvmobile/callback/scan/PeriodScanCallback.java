package com.freevisiontech.fvmobile.callback.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.common.StateScan;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;

public abstract class PeriodScanCallback implements BluetoothAdapter.LeScanCallback {
    protected Handler handler = new Handler(Looper.getMainLooper());
    protected boolean isScan = true;
    protected boolean isScanning = false;
    protected int scanTimeout = -1;
    protected ViseBluetooth viseBluetooth;

    public abstract void onDeviceFound(BluetoothLeDevice bluetoothLeDevice);

    public abstract void scanTimeout();

    public ViseBluetooth getViseBluetooth() {
        return this.viseBluetooth;
    }

    public PeriodScanCallback setViseBluetooth(ViseBluetooth viseBluetooth2) {
        this.viseBluetooth = viseBluetooth2;
        return this;
    }

    public PeriodScanCallback setScanTimeout(int scanTimeout2) {
        this.scanTimeout = scanTimeout2;
        return this;
    }

    public PeriodScanCallback setScan(boolean scan) {
        this.isScan = scan;
        return this;
    }

    public boolean isScanning() {
        return this.isScanning;
    }

    public int getScanTimeout() {
        return this.scanTimeout;
    }

    public void scan() {
        if (!this.isScan) {
            this.isScanning = false;
            if (this.viseBluetooth != null) {
                this.viseBluetooth.stopLeScan((BluetoothAdapter.LeScanCallback) this);
            }
        } else if (!this.isScanning) {
            if (this.scanTimeout > 0) {
                this.handler.postDelayed(new Runnable() {
                    public void run() {
                        PeriodScanCallback.this.isScanning = false;
                        if (PeriodScanCallback.this.viseBluetooth != null) {
                            PeriodScanCallback.this.viseBluetooth.setScanState(StateScan.SCAN_TIMEOUT);
                            PeriodScanCallback.this.viseBluetooth.stopLeScan((BluetoothAdapter.LeScanCallback) PeriodScanCallback.this);
                        }
                        PeriodScanCallback.this.scanTimeout();
                    }
                }, (long) this.scanTimeout);
            }
            this.isScanning = true;
            if (this.viseBluetooth != null) {
                this.viseBluetooth.startLeScan((BluetoothAdapter.LeScanCallback) this);
            }
        }
    }

    public PeriodScanCallback removeHandlerMsg() {
        this.handler.removeCallbacksAndMessages((Object) null);
        return this;
    }

    public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
        onDeviceFound(new BluetoothLeDevice(bluetoothDevice, rssi, scanRecord, System.currentTimeMillis()));
    }
}
