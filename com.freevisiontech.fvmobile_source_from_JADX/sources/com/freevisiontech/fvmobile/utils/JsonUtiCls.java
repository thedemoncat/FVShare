package com.freevisiontech.fvmobile.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonUtiCls {
    public static Map<String, String> Json2StringMap(String jsonStr) {
        Map<String, JsonElement> map1 = Json2ElementsMap(jsonStr);
        if (map1 == null) {
            return null;
        }
        Map<String, String> map2 = new HashMap<>();
        try {
            for (Map.Entry<String, JsonElement> entry : map1.entrySet()) {
                map2.put(entry.getKey(), entry.getValue().getAsString());
            }
            return map2;
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, JsonElement> Json2ElementsMap(String jsonStr) {
        try {
            return (Map) new GsonBuilder().create().fromJson(jsonStr, new TypeToken<Map<String, JsonElement>>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }

    public static String toJson(Object obj) {
        return new GsonBuilder().create().toJson(obj);
    }

    public static <T> T fromJson(String str, Type type) {
        return new GsonBuilder().create().fromJson(str, type);
    }

    public static <T> T fromJson(String str, Class<T> type) {
        return new GsonBuilder().create().fromJson(str, type);
    }
}
