package com.freevisiontech.fvmobile.callback.scan;

import android.annotation.TargetApi;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Handler;
import android.os.Looper;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.common.StateScan;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;
import java.util.List;

@TargetApi(21)
public abstract class PeriodLScanCallback extends ScanCallback {
    protected List<ScanFilter> filters;
    protected Handler handler = new Handler(Looper.getMainLooper());
    protected boolean isScan = true;
    protected boolean isScanning = false;
    protected int scanTimeout = -1;
    protected ScanSettings settings;
    protected ViseBluetooth viseBluetooth;

    public abstract void onDeviceFound(BluetoothLeDevice bluetoothLeDevice);

    public abstract void scanTimeout();

    public ViseBluetooth getViseBluetooth() {
        return this.viseBluetooth;
    }

    public PeriodLScanCallback setViseBluetooth(ViseBluetooth viseBluetooth2) {
        this.viseBluetooth = viseBluetooth2;
        return this;
    }

    public PeriodLScanCallback setScanTimeout(int scanTimeout2) {
        this.scanTimeout = scanTimeout2;
        return this;
    }

    public PeriodLScanCallback setScan(boolean scan) {
        this.isScan = scan;
        return this;
    }

    public PeriodLScanCallback setFilters(List<ScanFilter> filters2) {
        this.filters = filters2;
        return this;
    }

    public PeriodLScanCallback setSettings(ScanSettings settings2) {
        this.settings = settings2;
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
                this.viseBluetooth.stopLeScan((ScanCallback) this);
            }
        } else if (!this.isScanning) {
            if (this.scanTimeout > 0) {
                this.handler.postDelayed(new Runnable() {
                    public void run() {
                        PeriodLScanCallback.this.isScanning = false;
                        if (PeriodLScanCallback.this.viseBluetooth != null) {
                            PeriodLScanCallback.this.viseBluetooth.setScanState(StateScan.SCAN_TIMEOUT);
                            PeriodLScanCallback.this.viseBluetooth.stopLeScan((ScanCallback) PeriodLScanCallback.this);
                        }
                        PeriodLScanCallback.this.scanTimeout();
                    }
                }, (long) this.scanTimeout);
            }
            this.isScanning = true;
            if (this.viseBluetooth == null) {
                return;
            }
            if (this.filters != null) {
                this.viseBluetooth.startLeScan(this.filters, this.settings, this);
            } else {
                this.viseBluetooth.startLeScan((ScanCallback) this);
            }
        }
    }

    public PeriodLScanCallback removeHandlerMsg() {
        this.handler.removeCallbacksAndMessages((Object) null);
        return this;
    }

    public void onScanResult(int callbackType, ScanResult result) {
        if (result != null) {
            onDeviceFound(new BluetoothLeDevice(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes(), System.currentTimeMillis()));
        }
    }
}
