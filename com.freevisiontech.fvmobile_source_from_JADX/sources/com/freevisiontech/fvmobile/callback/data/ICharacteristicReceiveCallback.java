package com.freevisiontech.fvmobile.callback.data;

import android.bluetooth.BluetoothGattCharacteristic;

public interface ICharacteristicReceiveCallback extends IBleCallback {
    void onSuccess(BluetoothGattCharacteristic bluetoothGattCharacteristic);
}
