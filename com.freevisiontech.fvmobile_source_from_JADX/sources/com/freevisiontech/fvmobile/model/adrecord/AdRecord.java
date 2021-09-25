package com.freevisiontech.fvmobile.model.adrecord;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

public class AdRecord implements Parcelable {
    public static final int BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_COMPLETE = 7;
    public static final int BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_MORE_AVAILABLE = 6;
    public static final int BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_COMPLETE = 3;
    public static final int BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_MORE_AVAILABLE = 2;
    public static final int BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_COMPLETE = 5;
    public static final int BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_MORE_AVAILABLE = 4;
    public static final int BLE_GAP_AD_TYPE_3D_INFORMATION_DATA = 61;
    public static final int BLE_GAP_AD_TYPE_ADVERTISING_INTERVAL = 26;
    public static final int BLE_GAP_AD_TYPE_APPEARANCE = 25;
    public static final int BLE_GAP_AD_TYPE_CLASS_OF_DEVICE = 13;
    public static final int BLE_GAP_AD_TYPE_COMPLETE_LOCAL_NAME = 9;
    public static final int BLE_GAP_AD_TYPE_FLAGS = 1;
    public static final int BLE_GAP_AD_TYPE_LE_BLUETOOTH_DEVICE_ADDRESS = 27;
    public static final int BLE_GAP_AD_TYPE_LE_ROLE = 28;
    public static final int BLE_GAP_AD_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    public static final int BLE_GAP_AD_TYPE_PUBLIC_TARGET_ADDRESS = 23;
    public static final int BLE_GAP_AD_TYPE_RANDOM_TARGET_ADDRESS = 24;
    public static final int BLE_GAP_AD_TYPE_SECURITY_MANAGER_OOB_FLAGS = 17;
    public static final int BLE_GAP_AD_TYPE_SECURITY_MANAGER_TK_VALUE = 16;
    public static final int BLE_GAP_AD_TYPE_SERVICE_DATA = 22;
    public static final int BLE_GAP_AD_TYPE_SERVICE_DATA_128BIT_UUID = 33;
    public static final int BLE_GAP_AD_TYPE_SERVICE_DATA_32BIT_UUID = 32;
    public static final int BLE_GAP_AD_TYPE_SHORT_LOCAL_NAME = 8;
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_HASH_C = 14;
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_HASH_C256 = 29;
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_RANDOMIZER_R = 15;
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_RANDOMIZER_R256 = 30;
    public static final int BLE_GAP_AD_TYPE_SLAVE_CONNECTION_INTERVAL_RANGE = 18;
    public static final int BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_128BIT = 21;
    public static final int BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_16BIT = 20;
    public static final int BLE_GAP_AD_TYPE_TX_POWER_LEVEL = 10;
    public static final Parcelable.Creator<AdRecord> CREATOR = new Parcelable.Creator<AdRecord>() {
        public AdRecord createFromParcel(Parcel in) {
            return new AdRecord(in);
        }

        public AdRecord[] newArray(int size) {
            return new AdRecord[size];
        }
    };
    private static final String PARCEL_RECORD_DATA = "record_data";
    private static final String PARCEL_RECORD_LENGTH = "record_length";
    private static final String PARCEL_RECORD_TYPE = "record_type";
    private final byte[] mData;
    private final int mLength;
    private final int mType;

    public AdRecord(int length, int type, byte[] data) {
        this.mLength = length;
        this.mType = type;
        this.mData = data;
    }

    public AdRecord(Parcel in) {
        Bundle b = in.readBundle(getClass().getClassLoader());
        this.mLength = b.getInt(PARCEL_RECORD_LENGTH);
        this.mType = b.getInt(PARCEL_RECORD_TYPE);
        this.mData = b.getByteArray(PARCEL_RECORD_DATA);
    }

    public int describeContents() {
        return 0;
    }

    public byte[] getData() {
        return this.mData;
    }

    public String getHumanReadableType() {
        return getHumanReadableAdType(this.mType);
    }

    public int getLength() {
        return this.mLength;
    }

    public int getType() {
        return this.mType;
    }

    public String toString() {
        return "AdRecord [mLength=" + this.mLength + ", mType=" + this.mType + ", mData=" + Arrays.toString(this.mData) + ", getHumanReadableType()=" + getHumanReadableType() + "]";
    }

    public void writeToParcel(Parcel parcel, int arg1) {
        Bundle b = new Bundle(getClass().getClassLoader());
        b.putInt(PARCEL_RECORD_LENGTH, this.mLength);
        b.putInt(PARCEL_RECORD_TYPE, this.mType);
        b.putByteArray(PARCEL_RECORD_DATA, this.mData);
        parcel.writeBundle(b);
    }

    private static String getHumanReadableAdType(int type) {
        switch (type) {
            case 1:
                return "Flags for discoverAbility.";
            case 2:
                return "Partial list of 16 bit service UUIDs.";
            case 3:
                return "Complete list of 16 bit service UUIDs.";
            case 4:
                return "Partial list of 32 bit service UUIDs.";
            case 5:
                return "Complete list of 32 bit service UUIDs.";
            case 6:
                return "Partial list of 128 bit service UUIDs.";
            case 7:
                return "Complete list of 128 bit service UUIDs.";
            case 8:
                return "Short local device name.";
            case 9:
                return "Complete local device name.";
            case 10:
                return "Transmit power level.";
            case 13:
                return "Class of device.";
            case 14:
                return "Simple Pairing Hash C.";
            case 15:
                return "Simple Pairing Randomizer R.";
            case 16:
                return "Security Manager TK Value.";
            case 17:
                return "Security Manager Out Of Band Flags.";
            case 18:
                return "Slave Connection Interval Range.";
            case 20:
                return "List of 16-bit Service Solicitation UUIDs.";
            case 21:
                return "List of 128-bit Service Solicitation UUIDs.";
            case 22:
                return "Service Data - 16-bit UUID.";
            case 23:
                return "Public Target Address.";
            case 24:
                return "Random Target Address.";
            case 25:
                return "Appearance.";
            case 26:
                return "Advertising Interval.";
            case 27:
                return "LE Bluetooth Device Address.";
            case 28:
                return "LE Role.";
            case 29:
                return "Simple Pairing Hash C-256.";
            case 30:
                return "Simple Pairing Randomizer R-256.";
            case 32:
                return "Service Data - 32-bit UUID.";
            case 33:
                return "Service Data - 128-bit UUID.";
            case 61:
                return "3D Information Data.";
            case 255:
                return "Manufacturer Specific Data.";
            default:
                return "Unknown AdRecord Structure: " + type;
        }
    }
}
