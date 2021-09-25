package com.freevisiontech.fvmobile.utils;

import android.util.SparseArray;
import com.freevisiontech.fvmobile.model.adrecord.AdRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BleAdRecordUtil {
    private BleAdRecordUtil() {
    }

    public static String getRecordDataAsString(AdRecord nameRecord) {
        if (nameRecord == null) {
            return "";
        }
        return new String(nameRecord.getData());
    }

    public static byte[] getServiceData(AdRecord serviceData) {
        if (serviceData == null || serviceData.getType() != 22) {
            return null;
        }
        byte[] raw = serviceData.getData();
        return Arrays.copyOfRange(raw, 2, raw.length);
    }

    public static int getServiceDataUuid(AdRecord serviceData) {
        if (serviceData == null || serviceData.getType() != 22) {
            return -1;
        }
        byte[] raw = serviceData.getData();
        return ((raw[1] & 255) << 8) + (raw[0] & 255);
    }

    public static List<AdRecord> parseScanRecordAsList(byte[] scanRecord) {
        List<AdRecord> records = new ArrayList<>();
        int index = 0;
        while (true) {
            if (index >= scanRecord.length) {
                break;
            }
            int index2 = index + 1;
            byte length = scanRecord[index];
            if (length == 0) {
                int i = index2;
                break;
            }
            int type = BleConvertUtil.getIntFromByte(scanRecord[index2]);
            if (type == 0) {
                int i2 = index2;
                break;
            }
            records.add(new AdRecord(length, type, Arrays.copyOfRange(scanRecord, index2 + 1, index2 + length)));
            index = index2 + length;
        }
        return Collections.unmodifiableList(records);
    }

    public static Map<Integer, AdRecord> parseScanRecordAsMap(byte[] scanRecord) {
        Map<Integer, AdRecord> records = new HashMap<>();
        int index = 0;
        while (true) {
            if (index >= scanRecord.length) {
                break;
            }
            int index2 = index + 1;
            byte length = scanRecord[index];
            if (length == 0) {
                int i = index2;
                break;
            }
            int type = BleConvertUtil.getIntFromByte(scanRecord[index2]);
            if (type == 0) {
                int i2 = index2;
                break;
            }
            records.put(Integer.valueOf(type), new AdRecord(length, type, Arrays.copyOfRange(scanRecord, index2 + 1, index2 + length)));
            index = index2 + length;
        }
        return Collections.unmodifiableMap(records);
    }

    public static SparseArray<AdRecord> parseScanRecordAsSparseArray(byte[] scanRecord) {
        SparseArray<AdRecord> records = new SparseArray<>();
        int index = 0;
        while (true) {
            if (index >= scanRecord.length) {
                break;
            }
            int index2 = index + 1;
            byte length = scanRecord[index];
            if (length == 0) {
                int i = index2;
                break;
            }
            int type = BleConvertUtil.getIntFromByte(scanRecord[index2]);
            if (type == 0) {
                int i2 = index2;
                break;
            }
            records.put(type, new AdRecord(length, type, Arrays.copyOfRange(scanRecord, index2 + 1, index2 + length)));
            index = index2 + length;
        }
        return records;
    }
}
