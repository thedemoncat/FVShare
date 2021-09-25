package com.freevisiontech.fvmobile.model;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.freevisiontech.fvmobile.common.BluetoothServiceType;
import com.freevisiontech.fvmobile.model.adrecord.AdRecordStore;
import com.freevisiontech.fvmobile.model.resolver.BluetoothClassResolver;
import com.freevisiontech.fvmobile.utils.BleAdRecordUtil;
import com.freevisiontech.fvmobile.utils.HexUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BluetoothLeDevice implements Parcelable {
    public static final Parcelable.Creator<BluetoothLeDevice> CREATOR = new Parcelable.Creator<BluetoothLeDevice>() {
        public BluetoothLeDevice createFromParcel(Parcel in) {
            return new BluetoothLeDevice(in);
        }

        public BluetoothLeDevice[] newArray(int size) {
            return new BluetoothLeDevice[size];
        }
    };
    private static final long LOG_INVALIDATION_THRESHOLD = 10000;
    protected static final int MAX_RSSI_LOG_SIZE = 10;
    private static final String PARCEL_EXTRA_BLUETOOTH_DEVICE = "bluetooth_device";
    private static final String PARCEL_EXTRA_CURRENT_RSSI = "current_rssi";
    private static final String PARCEL_EXTRA_CURRENT_TIMESTAMP = "current_timestamp";
    private static final String PARCEL_EXTRA_DEVICE_RSSI_LOG = "device_rssi_log";
    private static final String PARCEL_EXTRA_DEVICE_SCANRECORD = "device_scanrecord";
    private static final String PARCEL_EXTRA_DEVICE_SCANRECORD_STORE = "device_scanrecord_store";
    private static final String PARCEL_EXTRA_FIRST_RSSI = "device_first_rssi";
    private static final String PARCEL_EXTRA_FIRST_TIMESTAMP = "first_timestamp";
    private boolean isConnected = false;
    private int mCurrentRssi;
    private long mCurrentTimestamp;
    private final BluetoothDevice mDevice;
    private final int mFirstRssi;
    private final long mFirstTimestamp;
    private final AdRecordStore mRecordStore;
    private final Map<Long, Integer> mRssiLog;
    private final byte[] mScanRecord;
    private transient Set<BluetoothServiceType> mServiceSet;

    public BluetoothLeDevice(BluetoothDevice device, int rssi, byte[] scanRecord, long timestamp) {
        this.mDevice = device;
        this.mFirstRssi = rssi;
        this.mFirstTimestamp = timestamp;
        this.mRecordStore = new AdRecordStore(BleAdRecordUtil.parseScanRecordAsSparseArray(scanRecord));
        this.mScanRecord = scanRecord;
        this.mRssiLog = new LinkedHashMap(10);
        updateRssiReading(timestamp, rssi);
    }

    public BluetoothLeDevice(BluetoothLeDevice device) {
        this.mCurrentRssi = device.getRssi();
        this.mCurrentTimestamp = device.getTimestamp();
        this.mDevice = device.getDevice();
        this.mFirstRssi = device.getFirstRssi();
        this.mFirstTimestamp = device.getFirstTimestamp();
        this.mRecordStore = new AdRecordStore(BleAdRecordUtil.parseScanRecordAsSparseArray(device.getScanRecord()));
        this.mRssiLog = device.getRssiLog();
        this.mScanRecord = device.getScanRecord();
    }

    protected BluetoothLeDevice(Parcel in) {
        Bundle b = in.readBundle(getClass().getClassLoader());
        this.mCurrentRssi = b.getInt(PARCEL_EXTRA_CURRENT_RSSI, 0);
        this.mCurrentTimestamp = b.getLong(PARCEL_EXTRA_CURRENT_TIMESTAMP, 0);
        this.mDevice = (BluetoothDevice) b.getParcelable(PARCEL_EXTRA_BLUETOOTH_DEVICE);
        this.mFirstRssi = b.getInt(PARCEL_EXTRA_FIRST_RSSI, 0);
        this.mFirstTimestamp = b.getLong(PARCEL_EXTRA_FIRST_TIMESTAMP, 0);
        this.mRecordStore = (AdRecordStore) b.getParcelable(PARCEL_EXTRA_DEVICE_SCANRECORD_STORE);
        this.mRssiLog = (Map) b.getSerializable(PARCEL_EXTRA_DEVICE_RSSI_LOG);
        this.mScanRecord = b.getByteArray(PARCEL_EXTRA_DEVICE_SCANRECORD);
    }

    private void addToRssiLog(long timestamp, int rssiReading) {
        synchronized (this.mRssiLog) {
            if (timestamp - this.mCurrentTimestamp > LOG_INVALIDATION_THRESHOLD) {
                this.mRssiLog.clear();
            }
            this.mCurrentRssi = rssiReading;
            this.mCurrentTimestamp = timestamp;
            this.mRssiLog.put(Long.valueOf(timestamp), Integer.valueOf(rssiReading));
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BluetoothLeDevice other = (BluetoothLeDevice) obj;
        if (this.mCurrentRssi != other.mCurrentRssi) {
            return false;
        }
        if (this.mCurrentTimestamp != other.mCurrentTimestamp) {
            return false;
        }
        if (this.mDevice == null) {
            if (other.mDevice != null) {
                return false;
            }
        } else if (!this.mDevice.equals(other.mDevice)) {
            return false;
        }
        if (this.mFirstRssi != other.mFirstRssi) {
            return false;
        }
        if (this.mFirstTimestamp != other.mFirstTimestamp) {
            return false;
        }
        if (this.mRecordStore == null) {
            if (other.mRecordStore != null) {
                return false;
            }
        } else if (!this.mRecordStore.equals(other.mRecordStore)) {
            return false;
        }
        if (this.mRssiLog == null) {
            if (other.mRssiLog != null) {
                return false;
            }
        } else if (!this.mRssiLog.equals(other.mRssiLog)) {
            return false;
        }
        if (!Arrays.equals(this.mScanRecord, other.mScanRecord)) {
            return false;
        }
        return true;
    }

    public AdRecordStore getAdRecordStore() {
        return this.mRecordStore;
    }

    public String getAddress() {
        return this.mDevice.getAddress();
    }

    public String getBluetoothDeviceBondState() {
        return resolveBondingState(this.mDevice.getBondState());
    }

    public String getBluetoothDeviceClassName() {
        return BluetoothClassResolver.resolveDeviceClass(this.mDevice.getBluetoothClass().getDeviceClass());
    }

    public Set<BluetoothServiceType> getBluetoothDeviceKnownSupportedServices() {
        if (this.mServiceSet == null) {
            synchronized (this) {
                if (this.mServiceSet == null) {
                    Set<BluetoothServiceType> serviceSet = new HashSet<>();
                    for (BluetoothServiceType service : BluetoothServiceType.values()) {
                        if (this.mDevice.getBluetoothClass().hasService(service.getCode())) {
                            serviceSet.add(service);
                        }
                    }
                    this.mServiceSet = Collections.unmodifiableSet(serviceSet);
                }
            }
        }
        return this.mServiceSet;
    }

    public String getBluetoothDeviceMajorClassName() {
        return BluetoothClassResolver.resolveMajorDeviceClass(this.mDevice.getBluetoothClass().getMajorDeviceClass());
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public int getFirstRssi() {
        return this.mFirstRssi;
    }

    public long getFirstTimestamp() {
        return this.mFirstTimestamp;
    }

    public String getName() {
        return this.mDevice.getName();
    }

    public int getRssi() {
        return this.mCurrentRssi;
    }

    /* access modifiers changed from: protected */
    public Map<Long, Integer> getRssiLog() {
        Map<Long, Integer> map;
        synchronized (this.mRssiLog) {
            map = this.mRssiLog;
        }
        return map;
    }

    public double getRunningAverageRssi() {
        int sum = 0;
        int count = 0;
        synchronized (this.mRssiLog) {
            for (Long aLong : this.mRssiLog.keySet()) {
                count++;
                sum += this.mRssiLog.get(aLong).intValue();
            }
        }
        if (count > 0) {
            return (double) (sum / count);
        }
        return 0.0d;
    }

    public byte[] getScanRecord() {
        return this.mScanRecord;
    }

    public long getTimestamp() {
        return this.mCurrentTimestamp;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (((((((((((this.mCurrentRssi + 31) * 31) + ((int) (this.mCurrentTimestamp ^ (this.mCurrentTimestamp >>> 32)))) * 31) + (this.mDevice == null ? 0 : this.mDevice.hashCode())) * 31) + this.mFirstRssi) * 31) + ((int) (this.mFirstTimestamp ^ (this.mFirstTimestamp >>> 32)))) * 31) + (this.mRecordStore == null ? 0 : this.mRecordStore.hashCode())) * 31;
        if (this.mRssiLog != null) {
            i = this.mRssiLog.hashCode();
        }
        return ((hashCode + i) * 31) + Arrays.hashCode(this.mScanRecord);
    }

    public String toString() {
        return "BluetoothLeDevice [mDevice=" + this.mDevice + ", mRssi=" + this.mFirstRssi + ", mScanRecord=" + HexUtil.encodeHexStr(this.mScanRecord) + ", mRecordStore=" + this.mRecordStore + ", getBluetoothDeviceBondState()=" + getBluetoothDeviceBondState() + ", getBluetoothDeviceClassName()=" + getBluetoothDeviceClassName() + "]";
    }

    public void updateRssiReading(long timestamp, int rssiReading) {
        addToRssiLog(timestamp, rssiReading);
    }

    public void writeToParcel(Parcel parcel, int arg1) {
        Bundle b = new Bundle(getClass().getClassLoader());
        b.putByteArray(PARCEL_EXTRA_DEVICE_SCANRECORD, this.mScanRecord);
        b.putInt(PARCEL_EXTRA_FIRST_RSSI, this.mFirstRssi);
        b.putInt(PARCEL_EXTRA_CURRENT_RSSI, this.mCurrentRssi);
        b.putLong(PARCEL_EXTRA_FIRST_TIMESTAMP, this.mFirstTimestamp);
        b.putLong(PARCEL_EXTRA_CURRENT_TIMESTAMP, this.mCurrentTimestamp);
        b.putParcelable(PARCEL_EXTRA_BLUETOOTH_DEVICE, this.mDevice);
        b.putParcelable(PARCEL_EXTRA_DEVICE_SCANRECORD_STORE, this.mRecordStore);
        b.putSerializable(PARCEL_EXTRA_DEVICE_RSSI_LOG, (Serializable) this.mRssiLog);
        parcel.writeBundle(b);
    }

    private static String resolveBondingState(int bondState) {
        switch (bondState) {
            case 10:
                return "UnBonded";
            case 11:
                return "Pairing";
            case 12:
                return "Paired";
            default:
                return "Unknown";
        }
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void setConnected(boolean connected) {
        this.isConnected = connected;
    }
}
