package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import com.freevisiontech.fvmobile.model.BluetoothLeDevice;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlueToothHistoryUtil {
    public static int findLastDeviceByTypeName(Context mContext, List<BluetoothLeDevice> devices, String ptzTypeName) {
        Gson gson = new Gson();
        String savedDevicesStr = String.valueOf(SPUtils.get(mContext, ptzTypeName + "_" + SharePrefConstant.SAVED_DEVICES, ""));
        if (!"".equals(savedDevicesStr)) {
            List<BleDeviceBean> savedDevices = (List) gson.fromJson(savedDevicesStr, new TypeToken<ArrayList<BleDeviceBean>>() {
            }.getType());
            ViseLog.m1466e("FVMainActivity checkAutoConnect: " + savedDevices);
            for (int i = savedDevices.size() - 1; i >= 0; i--) {
                String lastDeviceName = savedDevices.get(i).getBleName();
                ViseLog.m1466e("FVMainActivity lastDeviceName: " + lastDeviceName);
                for (BluetoothLeDevice currentBleDevice : devices) {
                    ViseLog.m1466e("FVMainActivity currentBleDevice.getName(): " + currentBleDevice.getName());
                    if (currentBleDevice.getName().equals(lastDeviceName)) {
                        int deviceOnline = devices.indexOf(currentBleDevice);
                        ViseLog.m1466e("FVMainActivity onlineDevice: " + devices.get(deviceOnline).getName());
                        return deviceOnline;
                    }
                }
            }
        }
        return -1;
    }

    public static boolean isEmpty(Context mContext, String ptzType) {
        boolean isEmpty = false;
        if ("".equals(String.valueOf(SPUtils.get(mContext, ptzType + "_" + SharePrefConstant.SAVED_DEVICES, "")))) {
            isEmpty = true;
        }
        ViseLog.m1466e("isEmpty ptzType: " + ptzType);
        ViseLog.m1466e("isEmpty: " + isEmpty);
        return isEmpty;
    }

    public static void saveConnectedDevice(Context mContext, BleDeviceBean bleDeviceBean, String ptzType) {
        List<BleDeviceBean> devices;
        long currentTimeMillis = System.currentTimeMillis();
        Gson gson = new Gson();
        String savedDevicesStr = String.valueOf(SPUtils.get(mContext, ptzType + "_" + SharePrefConstant.SAVED_DEVICES, ""));
        if (!isEmpty(mContext, ptzType)) {
            devices = (List) gson.fromJson(savedDevicesStr, new TypeToken<ArrayList<BleDeviceBean>>() {
            }.getType());
        } else {
            devices = new ArrayList<>();
        }
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getBleName().equals(bleDeviceBean.getBleName())) {
                devices.remove(i);
            }
        }
        devices.add(bleDeviceBean);
        SPUtils.put(mContext, ptzType + "_" + SharePrefConstant.SAVED_DEVICES, gson.toJson((Object) devices));
        ViseLog.m1466e("FVMainActivity saved Devices:" + SPUtils.get(mContext, ptzType + "_" + SharePrefConstant.SAVED_DEVICES, ""));
    }

    static class SortByTime implements Comparator {
        SortByTime() {
        }

        public int compare(Object o1, Object o2) {
            if (((BleDeviceBean) o1).getTime() > ((BleDeviceBean) o2).getTime()) {
                return 1;
            }
            return -1;
        }
    }
}
