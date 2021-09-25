package com.freevisiontech.fvmobile.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothLeDeviceStore {
    private final Map<String, BluetoothLeDevice> mDeviceMap = new HashMap();

    public void addDevice(BluetoothLeDevice device) {
        if (device != null) {
            if (this.mDeviceMap.containsKey(device.getAddress())) {
                this.mDeviceMap.get(device.getAddress()).updateRssiReading(device.getTimestamp(), device.getRssi());
            } else {
                this.mDeviceMap.put(device.getAddress(), device);
            }
        }
    }

    public void removeDevice(BluetoothLeDevice device) {
        if (device != null && this.mDeviceMap.containsKey(device.getAddress())) {
            this.mDeviceMap.remove(device.getAddress());
        }
    }

    public void clear() {
        this.mDeviceMap.clear();
    }

    public Map<String, BluetoothLeDevice> getDeviceMap() {
        return this.mDeviceMap;
    }

    public List<BluetoothLeDevice> getDeviceList() {
        List<BluetoothLeDevice> methodResult = new ArrayList<>(this.mDeviceMap.values());
        Collections.sort(methodResult, new Comparator<BluetoothLeDevice>() {
            public int compare(BluetoothLeDevice arg0, BluetoothLeDevice arg1) {
                return arg0.getAddress().compareToIgnoreCase(arg1.getAddress());
            }
        });
        return methodResult;
    }
}
