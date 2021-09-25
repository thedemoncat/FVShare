package com.vise.log.parser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class IntentParse implements Parser<Intent> {
    @SuppressLint({"UseSparseArrays"})
    private static Map<Integer, String> flagMap = new HashMap();

    static {
        Class cla = Intent.class;
        for (Field field : cla.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().startsWith("FLAG_")) {
                int value = 0;
                try {
                    Object object = field.get(cla);
                    if ((object instanceof Integer) || object.getClass().getSimpleName().equals("int")) {
                        value = ((Integer) object).intValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (flagMap.get(Integer.valueOf(value)) == null) {
                    flagMap.put(Integer.valueOf(value), field.getName());
                }
            }
        }
    }

    public Class<Intent> parseClassType() {
        return Intent.class;
    }

    public String parseString(Intent intent) {
        return ((parseClassType().getSimpleName() + " [" + LINE_SEPARATOR) + String.format("%s = %s" + LINE_SEPARATOR, new Object[]{"Scheme", intent.getScheme()}) + String.format("%s = %s" + LINE_SEPARATOR, new Object[]{"Action", intent.getAction()}) + String.format("%s = %s" + LINE_SEPARATOR, new Object[]{"DataString", intent.getDataString()}) + String.format("%s = %s" + LINE_SEPARATOR, new Object[]{"Type", intent.getType()}) + String.format("%s = %s" + LINE_SEPARATOR, new Object[]{"Package", intent.getPackage()}) + String.format("%s = %s" + LINE_SEPARATOR, new Object[]{"ComponentInfo", intent.getComponent()}) + String.format("%s = %s" + LINE_SEPARATOR, new Object[]{"Flags", getFlags(intent.getFlags())}) + String.format("%s = %s" + LINE_SEPARATOR, new Object[]{"Categories", intent.getCategories()}) + String.format("%s = %s" + LINE_SEPARATOR, new Object[]{"Extras", new BundleParse().parseString(intent.getExtras())})) + "]";
    }

    private String getFlags(int flags) {
        StringBuilder builder = new StringBuilder();
        for (Integer intValue : flagMap.keySet()) {
            int flagKey = intValue.intValue();
            if ((flagKey & flags) == flagKey) {
                builder.append(flagMap.get(Integer.valueOf(flagKey)));
                builder.append(" | ");
            }
        }
        if (TextUtils.isEmpty(builder.toString())) {
            builder.append(flags);
        } else if (builder.indexOf("|") != -1) {
            builder.delete(builder.length() - 2, builder.length());
        }
        return builder.toString();
    }
}
