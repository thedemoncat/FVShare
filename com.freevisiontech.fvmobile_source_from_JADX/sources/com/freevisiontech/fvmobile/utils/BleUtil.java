package com.freevisiontech.fvmobile.utils;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import com.vise.log.ViseLog;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

public class BleUtil {
    public static void enableBluetooth(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), requestCode);
    }

    public static boolean isSupportBle(Context context) {
        if (context == null || !context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le") || ((BluetoothManager) context.getSystemService("bluetooth")).getAdapter() == null) {
            return false;
        }
        return true;
    }

    public static boolean isBleEnable(Context context) {
        if (!isSupportBle(context)) {
            return false;
        }
        return ((BluetoothManager) context.getSystemService("bluetooth")).getAdapter().isEnabled();
    }

    public static void printServices(BluetoothGatt gatt) {
        if (gatt != null) {
            for (BluetoothGattService service : gatt.getServices()) {
                ViseLog.m1468i("service: " + service.getUuid());
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    ViseLog.m1464d("  characteristic: " + characteristic.getUuid() + " value: " + Arrays.toString(characteristic.getValue()));
                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                        ViseLog.m1470v("        descriptor: " + descriptor.getUuid() + " value: " + Arrays.toString(descriptor.getValue()));
                    }
                }
            }
        }
    }

    public static boolean refreshDeviceCache(BluetoothGatt gatt) {
        try {
            Method refresh = BluetoothGatt.class.getMethod("refresh", new Class[0]);
            if (refresh != null) {
                boolean success = ((Boolean) refresh.invoke(gatt, new Object[0])).booleanValue();
                ViseLog.m1468i("Refreshing result: " + success);
                return success;
            }
        } catch (Exception e) {
            ViseLog.m1466e("An exception occured while refreshing device" + e);
        }
        return false;
    }

    public static void closeBluetoothGatt(BluetoothGatt gatt) {
        if (gatt != null) {
            gatt.disconnect();
            refreshDeviceCache(gatt);
            gatt.close();
        }
    }

    public static BluetoothGattService getService(BluetoothGatt gatt, String serviceUUID) {
        return gatt.getService(UUID.fromString(serviceUUID));
    }

    public static BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String charactUUID) {
        if (service != null) {
            return service.getCharacteristic(UUID.fromString(charactUUID));
        }
        return null;
    }

    public static BluetoothGattCharacteristic getCharacteristic(BluetoothGatt gatt, String serviceUUID, String charactUUID) {
        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUUID));
        if (service != null) {
            return service.getCharacteristic(UUID.fromString(charactUUID));
        }
        return null;
    }
}
