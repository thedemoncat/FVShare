package com.freevisiontech.fvmobile.callback;

import android.bluetooth.BluetoothGatt;
import com.freevisiontech.fvmobile.exception.BleException;

public interface IConnectCallback {
    void onConnectFailure(BleException bleException);

    void onConnectSuccess(BluetoothGatt bluetoothGatt, int i);

    void onDisconnect();
}
