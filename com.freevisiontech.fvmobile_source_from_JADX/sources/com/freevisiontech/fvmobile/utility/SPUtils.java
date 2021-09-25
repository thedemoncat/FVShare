package com.freevisiontech.fvmobile.utility;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SPUtils {
    public static final String FILE_NAME = "share_data";

    public static void put(Context context, String key, Object object) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, 0).edit();
        try {
            if (object instanceof String) {
                editor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, ((Integer) object).intValue());
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, ((Boolean) object).booleanValue());
            } else if (object instanceof Float) {
                editor.putFloat(key, ((Float) object).floatValue());
            } else if (object instanceof Long) {
                editor.putLong(key, ((Long) object).longValue());
            } else {
                editor.putString(key, object.toString());
            }
            SharedPreferencesCompat.apply(editor);
        } catch (Exception e) {
        }
    }

    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        try {
            if (defaultObject instanceof String) {
                return sp.getString(key, (String) defaultObject);
            }
            if (defaultObject instanceof Integer) {
                return Integer.valueOf(sp.getInt(key, ((Integer) defaultObject).intValue()));
            }
            if (defaultObject instanceof Boolean) {
                return Boolean.valueOf(sp.getBoolean(key, ((Boolean) defaultObject).booleanValue()));
            }
            if (defaultObject instanceof Float) {
                return Float.valueOf(sp.getFloat(key, ((Float) defaultObject).floatValue()));
            }
            if (defaultObject instanceof Long) {
                return Long.valueOf(sp.getLong(key, ((Long) defaultObject).longValue()));
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, 0).edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, 0).edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    public static boolean contains(Context context, String key) {
        return context.getSharedPreferences(FILE_NAME, 0).contains(key);
    }

    public static Map<String, ?> getAll(Context context) {
        return context.getSharedPreferences(FILE_NAME, 0).getAll();
    }

    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        private SharedPreferencesCompat() {
        }

        private static Method findApplyMethod() {
            try {
                return SharedPreferences.Editor.class.getMethod("apply", new Class[0]);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor, new Object[0]);
                    return;
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            }
            editor.commit();
        }
    }

    public static <T> void setDataList(Context context, String tag, List<T> datalist) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, 0).edit();
        if (datalist != null && datalist.size() > 0) {
            String strJson = new Gson().toJson((Object) datalist);
            editor.clear();
            editor.putString(tag, strJson);
            editor.commit();
        }
    }

    public static <T> List<T> getDataList(Context context, String tag) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        List<T> datalist = new ArrayList<>();
        String strJson = sp.getString(tag, (String) null);
        if (strJson == null) {
            return datalist;
        }
        return (List) new Gson().fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
    }
}
