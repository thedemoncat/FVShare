package com.vise.log.parser;

import android.os.Bundle;
import com.vise.log.common.LogConvert;

public class BundleParse implements Parser<Bundle> {
    public Class<Bundle> parseClassType() {
        return Bundle.class;
    }

    public String parseString(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(bundle.getClass().getName() + " [" + LINE_SEPARATOR);
        for (String key : bundle.keySet()) {
            builder.append(String.format("'%s' => %s " + LINE_SEPARATOR, new Object[]{key, LogConvert.objectToString(bundle.get(key))}));
        }
        builder.append("]");
        return builder.toString();
    }
}
