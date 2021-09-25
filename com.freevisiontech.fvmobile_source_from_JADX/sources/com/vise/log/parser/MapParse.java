package com.vise.log.parser;

import com.vise.log.common.LogConvert;
import java.util.Map;

public class MapParse implements Parser<Map> {
    public Class<Map> parseClassType() {
        return Map.class;
    }

    public String parseString(Map map) {
        String msg = map.getClass().getName() + " [" + LINE_SEPARATOR;
        for (Object key : map.keySet()) {
            String itemString = "%s -> %s" + LINE_SEPARATOR;
            Object value = map.get(key);
            if (value != null) {
                if (value instanceof String) {
                    value = "\"" + value + "\"";
                } else if (value instanceof Character) {
                    value = "'" + value + "'";
                }
            }
            msg = msg + String.format(itemString, new Object[]{LogConvert.objectToString(key), LogConvert.objectToString(value)});
        }
        return msg + "]";
    }
}
