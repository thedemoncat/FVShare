package com.freevisiontech.fvmobile.utility;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import com.vise.log.ViseLog;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static String getInternalMemorySize(Context context) {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return Formatter.formatFileSize(context, statFs.getBlockCountLong() * statFs.getBlockSizeLong());
    }

    public static long getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getExternalMemorySize(Context context) {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return Formatter.formatFileSize(context, statFs.getBlockCountLong() * statFs.getBlockSizeLong());
    }

    public static String getAvailableExternalMemorySize(Context context) {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return Formatter.formatFileSize(context, statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
    }

    public static boolean takePhotoMemoryEnough(Context context) {
        if ((getAvailableInternalMemorySize() / 1024) / 1024 > 50) {
            return true;
        }
        return false;
    }

    public static boolean takeVideoMemoryEnough(Context context) {
        if ((getAvailableInternalMemorySize() / 1024) / 1024 > 300) {
            return true;
        }
        return false;
    }

    public static void copy(String filename, byte[] bytes) {
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                FileOutputStream output = new FileOutputStream(filename);
                output.write(bytes);
                ViseLog.m1468i("copy: success，" + filename);
                output.close();
                return;
            }
            ViseLog.m1468i("copy:fail, " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
