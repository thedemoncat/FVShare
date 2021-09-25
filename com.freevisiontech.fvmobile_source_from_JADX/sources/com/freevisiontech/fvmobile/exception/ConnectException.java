package com.freevisiontech.fvmobile.exception;

import android.bluetooth.BluetoothGatt;
import com.freevisiontech.fvmobile.common.BleExceptionCode;

public class ConnectException extends BleException {
    private BluetoothGatt bluetoothGatt;
    private int gattStatus;

    public ConnectException(BluetoothGatt bluetoothGatt2, int gattStatus2) {
        super(BleExceptionCode.CONNECT_ERR, "Connect Exception Occurred! ");
        this.bluetoothGatt = bluetoothGatt2;
        this.gattStatus = gattStatus2;
    }

    public int getGattStatus() {
        return this.gattStatus;
    }

    public ConnectException setGattStatus(int gattStatus2) {
        this.gattStatus = gattStatus2;
        return this;
    }

    public BluetoothGatt getBluetoothGatt() {
        return this.bluetoothGatt;
    }

    public ConnectException setBluetoothGatt(BluetoothGatt bluetoothGatt2) {
        this.bluetoothGatt = bluetoothGatt2;
        return this;
    }

    public String toString() {
        return "ConnectException{gattStatus=" + this.gattStatus + ", bluetoothGatt=" + this.bluetoothGatt + "} " + super.toString();
    }
}
