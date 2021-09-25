package org.xutils.common.util;

import android.os.Environment;
import android.os.StatFs;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.xutils.C2090x;

public class FileUtil {
    private FileUtil() {
    }

    public static File getCacheDir(String dirName) {
        File result;
        if (existsSdcard().booleanValue()) {
            File cacheDir = C2090x.app().getExternalCacheDir();
            if (cacheDir == null) {
                result = new File(Environment.getExternalStorageDirectory(), "Android/data/" + C2090x.app().getPackageName() + "/cache/" + dirName);
            } else {
                result = new File(cacheDir, dirName);
            }
        } else {
            result = new File(C2090x.app().getCacheDir(), dirName);
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        }
        return null;
    }

    public static boolean isDiskAvailable() {
        return getDiskAvailableSize() > 10485760;
    }

    public static long getDiskAvailableSize() {
        if (!existsSdcard().booleanValue()) {
            return 0;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public static Boolean existsSdcard() {
        return Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
    }

    public static long getFileOrDirSize(File file) {
        if (!file.exists()) {
            return 0;
        }
        if (!file.isDirectory()) {
            return file.length();
        }
        long length = 0;
        File[] list = file.listFiles();
        if (list == null) {
            return 0;
        }
        for (File item : list) {
            length += getFileOrDirSize(item);
        }
        return length;
    }

    public static boolean copy(String fromPath, String toPath) {
        FileOutputStream out;
        boolean result = false;
        File from = new File(fromPath);
        if (!from.exists()) {
            return false;
        }
        File toFile = new File(toPath);
        IOUtil.deleteFileOrDir(toFile);
        File toDir = toFile.getParentFile();
        if (toDir.exists() || toDir.mkdirs()) {
            FileInputStream in = null;
            FileOutputStream out2 = null;
            try {
                FileInputStream in2 = new FileInputStream(from);
                try {
                    out = new FileOutputStream(toFile);
                } catch (Throwable th) {
                    th = th;
                    in = in2;
                    IOUtil.closeQuietly((Closeable) in);
                    IOUtil.closeQuietly((Closeable) out2);
                    throw th;
                }
                try {
                    IOUtil.copy(in2, out);
                    result = true;
                    IOUtil.closeQuietly((Closeable) in2);
                    IOUtil.closeQuietly((Closeable) out);
                } catch (Throwable th2) {
                    th = th2;
                    out2 = out;
                    in = in2;
                    IOUtil.closeQuietly((Closeable) in);
                    IOUtil.closeQuietly((Closeable) out2);
                    throw th;
                }
            } catch (Throwable th3) {
                ex = th3;
                LogUtil.m1563d(ex.getMessage(), ex);
                result = false;
                IOUtil.closeQuietly((Closeable) in);
                IOUtil.closeQuietly((Closeable) out2);
                return result;
            }
        }
        return result;
    }
}
