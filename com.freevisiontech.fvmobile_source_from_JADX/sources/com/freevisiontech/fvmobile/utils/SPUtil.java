package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
    private static final String FILE_NAME = "user_info";

    public static void setParam(Context context, String key, Object object) {
        if (!isEmpty(object + "")) {
            String type = object.getClass().getSimpleName();
            SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, 0).edit();
            if ("String".equals(type)) {
                editor.putString(key, (String) object);
            } else if ("Integer".equals(type)) {
                editor.putInt(key, ((Integer) object).intValue());
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, ((Boolean) object).booleanValue());
            } else if ("Float".equals(type)) {
                editor.putFloat(key, ((Float) object).floatValue());
            } else if ("Long".equals(type)) {
                editor.putLong(key, ((Long) object).longValue());
            }
            editor.commit();
        }
    }

    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        }
        if ("Integer".equals(type)) {
            return Integer.valueOf(sp.getInt(key, ((Integer) defaultObject).intValue()));
        }
        if ("Boolean".equals(type)) {
            return Boolean.valueOf(sp.getBoolean(key, ((Boolean) defaultObject).booleanValue()));
        }
        if ("Float".equals(type)) {
            return Float.valueOf(sp.getFloat(key, ((Float) defaultObject).floatValue()));
        }
        if ("Long".equals(type)) {
            return Long.valueOf(sp.getLong(key, ((Long) defaultObject).longValue()));
        }
        return null;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.equals("") || string.equals("null");
    }
}
