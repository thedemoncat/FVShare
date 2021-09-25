package com.freevisiontech.fvmobile.callback.data;

import android.bluetooth.BluetoothGattCharacteristic;

public interface ICharacteristicCallback extends IBleCallback {
    void onSuccess(BluetoothGattCharacteristic bluetoothGattCharacteristic);
}
