package org.xutils.p018db.table;

import android.text.TextUtils;
import com.freevisiontech.fvmobile.common.BleConstant;
import java.util.Date;
import java.util.HashMap;

/* renamed from: org.xutils.db.table.DbModel */
public final class DbModel {
    private HashMap<String, String> dataMap = new HashMap<>();

    public String getString(String columnName) {
        return this.dataMap.get(columnName);
    }

    public int getInt(String columnName) {
        return Integer.valueOf(this.dataMap.get(columnName)).intValue();
    }

    public boolean getBoolean(String columnName) {
        String value = this.dataMap.get(columnName);
        if (value != null) {
            return value.length() == 1 ? BleConstant.SHUTTER.equals(value) : Boolean.valueOf(value).booleanValue();
        }
        return false;
    }

    public double getDouble(String columnName) {
        return Double.valueOf(this.dataMap.get(columnName)).doubleValue();
    }

    public float getFloat(String columnName) {
        return Float.valueOf(this.dataMap.get(columnName)).floatValue();
    }

    public long getLong(String columnName) {
        return Long.valueOf(this.dataMap.get(columnName)).longValue();
    }

    public Date getDate(String columnName) {
        return new Date(Long.valueOf(this.dataMap.get(columnName)).longValue());
    }

    public java.sql.Date getSqlDate(String columnName) {
        return new java.sql.Date(Long.valueOf(this.dataMap.get(columnName)).longValue());
    }

    public void add(String columnName, String valueStr) {
        this.dataMap.put(columnName, valueStr);
    }

    public HashMap<String, String> getDataMap() {
        return this.dataMap;
    }

    public boolean isEmpty(String columnName) {
        return TextUtils.isEmpty(this.dataMap.get(columnName));
    }
}
