package com.freevisiontech.fvmobile.utils;

import android.bluetooth.BluetoothGattCharacteristic;
import com.freevisiontech.fvmobile.ViseBluetooth;

public class BleByteUtil {
    private static BluetoothGattCharacteristic mCharacteristic;

    public static void getPTZSingleParameters(byte data) {
        byte[] getVersion = new byte[20];
        getVersion[0] = 58;
        getVersion[1] = data;
        ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
    }

    public static void actPTZSettingChange(byte subcommand) {
        byte[] value = new byte[20];
        value[0] = -91;
        value[1] = subcommand;
        ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, value, System.currentTimeMillis());
    }

    public static void actPTZSettingStateChange(byte subcommand, byte data1) {
        byte[] value = new byte[20];
        value[0] = -91;
        value[1] = subcommand;
        value[2] = data1;
        ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, value, System.currentTimeMillis());
    }

    public static void actPTZStatusChange(byte subcommand) {
        byte[] value = new byte[20];
        value[0] = 102;
        value[1] = subcommand;
        ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, value, System.currentTimeMillis());
    }

    public static void setPTZParameters(byte subCommand, byte data1) {
        if (mCharacteristic != null) {
            byte[] getVersion = new byte[20];
            getVersion[0] = 90;
            getVersion[1] = subCommand;
            getVersion[2] = data1;
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
        }
    }

    public static void setPTZParameters(byte subCommand, byte data1, byte data2) {
        if (mCharacteristic != null) {
            byte[] getVersion = new byte[20];
            getVersion[0] = 90;
            getVersion[1] = subCommand;
            getVersion[2] = data1;
            getVersion[3] = data2;
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
        }
    }

    public static void setPTZParameters(byte subCommand, byte data1, byte data2, byte data3) {
        if (mCharacteristic != null) {
            byte[] getVersion = new byte[20];
            getVersion[0] = 90;
            getVersion[1] = subCommand;
            getVersion[2] = data1;
            getVersion[3] = data2;
            getVersion[4] = data3;
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
        }
    }

    public static void setPTZParameters(byte subCommand, byte[] data) {
        if (mCharacteristic != null) {
            byte[] activateData = new byte[20];
            activateData[0] = 90;
            activateData[1] = subCommand;
            for (int i = 0; i < data.length - 2; i++) {
                activateData[i + 2] = data[i];
            }
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, activateData, System.currentTimeMillis());
        }
    }

    public static void setMoveTimeLapse(byte subCommand, byte data1, byte data2, int data3) {
        if (mCharacteristic != null) {
            byte[] getVersion = new byte[20];
            getVersion[0] = 90;
            getVersion[1] = subCommand;
            getVersion[2] = data1;
            getVersion[3] = data2;
            getVersion[4] = (byte) ((data3 >> 0) & 255);
            getVersion[5] = (byte) ((data3 >> 8) & 255);
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
        }
    }

    public static void setPanoramaStart(byte subCommand, byte data1, byte data2) {
        if (mCharacteristic != null) {
            byte[] getVersion = new byte[20];
            getVersion[0] = 90;
            getVersion[1] = subCommand;
            getVersion[2] = data1;
            getVersion[3] = data2;
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
        }
    }

    public static void setPanoramaCount(byte subCommand, byte data1) {
        if (mCharacteristic != null) {
            byte[] getVersion = new byte[20];
            getVersion[0] = 90;
            getVersion[1] = subCommand;
            getVersion[2] = data1;
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
        }
    }

    public static void ackPTZPanorama(byte[] ackParams) {
        if (mCharacteristic != null) {
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, ackParams, System.currentTimeMillis());
        }
    }

    public static void ackPTZPanorama(byte subCommand, byte data1) {
        if (mCharacteristic != null) {
            byte[] getVersion = new byte[20];
            getVersion[0] = -91;
            getVersion[1] = subCommand;
            getVersion[2] = data1;
        }
    }

    public static void ackPTZMoveTimeLapsePoint(byte subCommand, byte data1, byte data2, byte data3) {
        if (mCharacteristic != null) {
            byte[] getVersion = new byte[20];
            getVersion[0] = -91;
            getVersion[1] = subCommand;
            getVersion[2] = data1;
            getVersion[3] = data2;
            getVersion[4] = data3;
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
        }
    }

    public static void ackPTZMoveTimeLapsePoint(byte subCommand, byte data1, byte data2, byte data3, byte data4) {
        if (mCharacteristic != null) {
            byte[] getVersion = new byte[20];
            getVersion[0] = -91;
            getVersion[1] = subCommand;
            getVersion[2] = data1;
            getVersion[3] = data2;
            getVersion[4] = data3;
            getVersion[5] = data4;
            ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
        }
    }

    public static void sendPtzRockerData(int rockerX, int rockerY, int followX, int followY) {
        byte[] getVersion = new byte[20];
        getVersion[0] = 74;
        getVersion[1] = 1;
        getVersion[2] = (byte) ((rockerX >> 0) & 255);
        getVersion[3] = (byte) ((rockerX >> 8) & 255);
        getVersion[4] = (byte) ((rockerY >> 0) & 255);
        getVersion[5] = (byte) ((rockerY >> 8) & 255);
        getVersion[6] = (byte) ((followX >> 0) & 255);
        getVersion[7] = (byte) ((followX >> 8) & 255);
        getVersion[8] = (byte) ((followY >> 0) & 255);
        getVersion[9] = (byte) ((followY >> 8) & 255);
        ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
    }

    public static void sendPtzFollowData(int xZhou, int yZhou) {
        byte[] getVersion = new byte[20];
        getVersion[0] = 74;
        getVersion[1] = 1;
        getVersion[2] = 0;
        getVersion[3] = 0;
        getVersion[4] = 0;
        getVersion[5] = 0;
        getVersion[6] = (byte) ((xZhou >> 0) & 255);
        getVersion[7] = (byte) ((xZhou >> 8) & 255);
        getVersion[8] = (byte) ((yZhou >> 0) & 255);
        getVersion[9] = (byte) ((yZhou >> 8) & 255);
        ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
    }

    public static void sendMTLFreeStyleData(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        byte[] getVersion = new byte[20];
        getVersion[0] = 90;
        getVersion[1] = 43;
        getVersion[2] = (byte) ((x1 >> 0) & 255);
        getVersion[3] = (byte) ((x1 >> 8) & 255);
        getVersion[4] = (byte) ((y1 >> 0) & 255);
        getVersion[5] = (byte) ((y1 >> 8) & 255);
        getVersion[6] = (byte) ((x2 >> 0) & 255);
        getVersion[7] = (byte) ((x2 >> 8) & 255);
        getVersion[8] = (byte) ((y2 >> 0) & 255);
        getVersion[9] = (byte) ((y2 >> 8) & 255);
        getVersion[10] = (byte) ((x3 >> 0) & 255);
        getVersion[11] = (byte) ((x3 >> 8) & 255);
        getVersion[12] = (byte) ((y3 >> 0) & 255);
        getVersion[13] = (byte) ((y3 >> 8) & 255);
        getVersion[14] = (byte) ((x4 >> 0) & 255);
        getVersion[15] = (byte) ((x4 >> 8) & 255);
        getVersion[16] = (byte) ((y4 >> 0) & 255);
        getVersion[17] = (byte) ((y4 >> 8) & 255);
        ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
    }

    public static void sendMLFreeStyleTotalPoint(byte subCommand, int data1) {
        byte[] getVersion = new byte[20];
        getVersion[0] = 90;
        getVersion[1] = subCommand;
        getVersion[2] = (byte) ((data1 >> 0) & 255);
        getVersion[3] = (byte) ((data1 >> 8) & 255);
        ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
    }

    public static void controlDefaultCamSwitch(byte subCommand, int data1) {
        byte[] getVersion = new byte[20];
        getVersion[0] = 90;
        getVersion[1] = subCommand;
        getVersion[2] = (byte) ((data1 >> 0) & 255);
        getVersion[3] = (byte) ((data1 >> 8) & 255);
        ViseBluetooth.getInstance().writeCharacteristic(mCharacteristic, getVersion, System.currentTimeMillis());
    }

    public static void setWriteCharacteristic(BluetoothGattCharacteristic mWriteCharacteristic) {
        mCharacteristic = mWriteCharacteristic;
    }

    public static BluetoothGattCharacteristic getWriteCharacteristic() {
        return mCharacteristic;
    }
}
