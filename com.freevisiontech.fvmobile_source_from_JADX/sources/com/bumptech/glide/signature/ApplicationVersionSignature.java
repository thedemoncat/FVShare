package com.bumptech.glide.signature;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.bumptech.glide.load.Key;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ApplicationVersionSignature {
    private static final ConcurrentHashMap<String, Key> PACKAGE_NAME_TO_KEY = new ConcurrentHashMap<>();

    public static Key obtain(Context context) {
        String packageName = context.getPackageName();
        Key result = PACKAGE_NAME_TO_KEY.get(packageName);
        if (result != null) {
            return result;
        }
        Key toAdd = obtainVersionSignature(context);
        Key result2 = PACKAGE_NAME_TO_KEY.putIfAbsent(packageName, toAdd);
        if (result2 == null) {
            return toAdd;
        }
        return result2;
    }

    static void reset() {
        PACKAGE_NAME_TO_KEY.clear();
    }

    private static Key obtainVersionSignature(Context context) {
        String versionCode;
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pInfo != null) {
            versionCode = String.valueOf(pInfo.versionCode);
        } else {
            versionCode = UUID.randomUUID().toString();
        }
        return new StringSignature(versionCode);
    }

    private ApplicationVersionSignature() {
    }
}
