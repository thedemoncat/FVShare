package com.freevisiontech.fvmobile.utility;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.math.BigDecimal;

public class DataCleanManager {
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals("mounted")) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize((double) cacheSize);
    }

    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals("mounted")) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static long getFolderSize(File file) throws Exception {
        long length;
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    length = getFolderSize(fileList[i]);
                } else {
                    length = fileList[i].length();
                }
                size += length;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024.0d;
        if (kiloByte < 1.0d) {
            return "0KB";
        }
        double megaByte = kiloByte / 1024.0d;
        if (megaByte < 1.0d) {
            return new BigDecimal(Double.toString(kiloByte)).setScale(2, 4).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024.0d;
        if (gigaByte < 1.0d) {
            return new BigDecimal(Double.toString(megaByte)).setScale(2, 4).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024.0d;
        if (teraBytes < 1.0d) {
            return new BigDecimal(Double.toString(gigaByte)).setScale(2, 4).toPlainString() + "GB";
        }
        return new BigDecimal(teraBytes).setScale(2, 4).toPlainString() + "TB";
    }
}
