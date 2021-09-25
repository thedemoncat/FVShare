package com.freevisiontech.fvmobile.callback.data;

import android.bluetooth.BluetoothGattDescriptor;

public interface IDescriptorCallback extends IBleCallback {
    void onSuccess(BluetoothGattDescriptor bluetoothGattDescriptor);
}
