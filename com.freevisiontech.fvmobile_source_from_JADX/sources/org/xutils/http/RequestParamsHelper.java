package org.xutils.http;

import android.os.Parcelable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

final class RequestParamsHelper {
    private static final ClassLoader BOOT_CL = String.class.getClassLoader();

    interface ParseKVListener {
        void onParseKV(String str, Object obj);
    }

    private RequestParamsHelper() {
    }

    static void parseKV(Object entity, Class<?> type, ParseKVListener listener) {
        ClassLoader cl;
        if (entity != null && type != null && type != RequestParams.class && type != Object.class && (cl = type.getClassLoader()) != null && cl != BOOT_CL) {
            Field[] fields = type.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if (!Modifier.isTransient(field.getModifiers()) && field.getType() != Parcelable.Creator.class) {
                        field.setAccessible(true);
                        try {
                            String name = field.getName();
                            Object value = field.get(entity);
                            if (value != null) {
                                listener.onParseKV(name, value);
                            }
                        } catch (IllegalAccessException ex) {
                            LogUtil.m1565e(ex.getMessage(), ex);
                        }
                    }
                }
            }
            parseKV(entity, type.getSuperclass(), listener);
        }
    }

    static Object parseJSONObject(Object value) throws JSONException {
        if (value == null) {
            return null;
        }
        Object obj = value;
        Class<?> cls = value.getClass();
        if (cls.isArray()) {
            JSONArray array = new JSONArray();
            int len = Array.getLength(value);
            for (int i = 0; i < len; i++) {
                array.put(parseJSONObject(Array.get(value, i)));
            }
            return array;
        } else if (value instanceof List) {
            JSONArray array2 = new JSONArray();
            for (Object item : (List) value) {
                array2.put(parseJSONObject(item));
            }
            return array2;
        } else if (value instanceof Map) {
            JSONObject jo = new JSONObject();
            for (Map.Entry<?, ?> entry : ((Map) value).entrySet()) {
                Object k = entry.getKey();
                Object v = entry.getValue();
                if (!(k == null || v == null)) {
                    jo.put(String.valueOf(k), parseJSONObject(v));
                }
            }
            return jo;
        } else {
            ClassLoader cl = cls.getClassLoader();
            if (cl == null || cl == BOOT_CL) {
                return obj;
            }
            final JSONObject jo2 = new JSONObject();
            parseKV(value, cls, new ParseKVListener() {
                public void onParseKV(String name, Object value) {
                    try {
                        jo2.put(name, RequestParamsHelper.parseJSONObject(value));
                    } catch (JSONException ex) {
                        throw new IllegalArgumentException("parse RequestParams to json failed", ex);
                    }
                }
            });
            return jo2;
        }
    }
}
