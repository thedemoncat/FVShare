package com.freevisiontech.fvmobile.callback.scan;

import android.text.TextUtils;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;
import com.freevisiontech.fvmobile.model.BluetoothLeDeviceStore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PeriodFilterScanCallback extends PeriodScanCallback {
    protected BluetoothLeDeviceStore bluetoothLeDeviceStore = new BluetoothLeDeviceStore();
    protected String filterName;
    protected int filterRssi;
    protected Matcher matcher;
    protected Pattern pattern = Pattern.compile("^[\\x00-\\xff]*$");

    public abstract void onDeviceFound(BluetoothLeDeviceStore bluetoothLeDeviceStore2);

    public PeriodFilterScanCallback setFilterName(String filterName2) {
        this.filterName = filterName2;
        if (!TextUtils.isEmpty(this.filterName)) {
            this.pattern = Pattern.compile(this.filterName);
        }
        this.bluetoothLeDeviceStore.clear();
        return this;
    }

    public PeriodFilterScanCallback setFilterRssi(int filterRssi2) {
        this.filterRssi = filterRssi2;
        this.bluetoothLeDeviceStore.clear();
        return this;
    }

    public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
        String tempName = bluetoothLeDevice.getName();
        int tempRssi = bluetoothLeDevice.getRssi();
        if (!TextUtils.isEmpty(tempName)) {
            addDevice(bluetoothLeDevice, tempName, tempRssi);
        } else {
            addDevice(bluetoothLeDevice, "", tempRssi);
        }
        onDeviceFound(this.bluetoothLeDeviceStore);
    }

    private void addDevice(BluetoothLeDevice bluetoothLeDevice, String tempName, int tempRssi) {
        this.matcher = this.pattern.matcher(tempName);
        if (this.filterRssi < 0) {
            if (this.matcher.matches() && tempRssi >= this.filterRssi) {
                this.bluetoothLeDeviceStore.addDevice(bluetoothLeDevice);
            } else if (this.matcher.matches() && tempRssi < this.filterRssi) {
                this.bluetoothLeDeviceStore.removeDevice(bluetoothLeDevice);
            }
        } else if (this.matcher.matches()) {
            this.bluetoothLeDeviceStore.addDevice(bluetoothLeDevice);
        }
    }
}
