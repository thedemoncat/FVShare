package com.freevisiontech.fvmobile.utils;

import android.app.Activity;
import android.content.Context;
import android.os.storage.StorageManager;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SDCardUtil {
    public static boolean getSdCardBoolean(Activity activity) {
        StorageManager mStorageManager = (StorageManager) activity.getSystemService("storage");
        try {
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method getPath = storageVolumeClazz.getMethod("getPath", new Class[0]);
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable", new Class[0]);
            Object result = getVolumeList.invoke(mStorageManager, new Object[0]);
            Method getVolumeState = mStorageManager.getClass().getMethod("getVolumeState", new Class[]{String.class});
            int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                boolean removable = ((Boolean) isRemovable.invoke(storageVolumeElement, new Object[0])).booleanValue();
                String state = (String) getVolumeState.invoke(mStorageManager, new Object[]{(String) getPath.invoke(storageVolumeElement, new Object[0])});
                if (removable && "mounted".equals(state)) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        }
        return false;
    }

    private static String[] getVolumePaths(Context context) {
        try {
            StorageManager mStorageManager = (StorageManager) context.getSystemService("storage");
            return (String[]) mStorageManager.getClass().getMethod("getVolumePaths", new Class[0]).invoke(mStorageManager, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkMounted(Context context, String mountPoint) {
        if (mountPoint == null) {
            return false;
        }
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        try {
            return "mounted".equals((String) storageManager.getClass().getMethod("getVolumeState", new Class[]{String.class}).invoke(storageManager, new Object[]{mountPoint}));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getExternalSDCardPath(Context context) {
        String[] paths = getVolumePaths(context);
        String path = null;
        for (int i = 0; i < paths.length; i++) {
            if (checkMounted(context, paths[i])) {
                path = paths[i];
            }
        }
        return path;
    }
}
