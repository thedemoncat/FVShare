package com.google.android.vending.expansion.downloader;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;
import com.android.vending.expansion.downloader.C0788R;
import com.umeng.analytics.C0015a;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Helpers {
    private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern.compile("attachment;\\s*filename\\s*=\\s*\"([^\"]*)\"");
    private static final Logger LOG = LoggerFactory.getLogger("Helpers");
    public static Random sRandom = new Random(SystemClock.uptimeMillis());

    private Helpers() {
    }

    static String parseContentDisposition(String contentDisposition) {
        try {
            Matcher m = CONTENT_DISPOSITION_PATTERN.matcher(contentDisposition);
            if (m.find()) {
                return m.group(1);
            }
        } catch (IllegalStateException e) {
        }
        return null;
    }

    public static File getFilesystemRoot(String path) {
        File cache = Environment.getDownloadCacheDirectory();
        if (path.startsWith(cache.getPath())) {
            return cache;
        }
        File external = Environment.getExternalStorageDirectory();
        if (path.startsWith(external.getPath())) {
            return external;
        }
        throw new IllegalArgumentException("Cannot determine filesystem root for " + path);
    }

    public static boolean isExternalMediaMounted() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return false;
        }
        return true;
    }

    public static long getAvailableBytes(File root) {
        StatFs stat = new StatFs(root.getPath());
        return ((long) stat.getBlockSize()) * (((long) stat.getAvailableBlocks()) - 4);
    }

    public static boolean isFilenameValid(String filename) {
        String filename2 = filename.replaceFirst("/+", "/");
        return filename2.startsWith(Environment.getDownloadCacheDirectory().toString()) || filename2.startsWith(Environment.getExternalStorageDirectory().toString());
    }

    static void deleteFile(String path) {
        try {
            new File(path).delete();
        } catch (Exception e) {
            LOG.warn("file: '" + path + "' couldn't be deleted", (Throwable) e);
        }
    }

    public static String getDownloadProgressString(long overallProgress, long overallTotal) {
        if (overallTotal == 0) {
            return "";
        }
        return String.format("%.2f", new Object[]{Float.valueOf(((float) overallProgress) / 1048576.0f)}) + "MB /" + String.format("%.2f", new Object[]{Float.valueOf(((float) overallTotal) / 1048576.0f)}) + "MB";
    }

    public static String getDownloadProgressStringNotification(long overallProgress, long overallTotal) {
        if (overallTotal == 0) {
            return "";
        }
        return getDownloadProgressString(overallProgress, overallTotal) + " (" + getDownloadProgressPercent(overallProgress, overallTotal) + ")";
    }

    public static String getDownloadProgressPercent(long overallProgress, long overallTotal) {
        if (overallTotal == 0) {
            return "";
        }
        return Long.toString((100 * overallProgress) / overallTotal) + "%";
    }

    public static String getSpeedString(float bytesPerMillisecond) {
        return String.format("%.2f", new Object[]{Float.valueOf((1000.0f * bytesPerMillisecond) / 1024.0f)});
    }

    public static String getTimeRemaining(long durationInMilliseconds) {
        SimpleDateFormat sdf;
        if (durationInMilliseconds > C0015a.f23j) {
            sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        }
        return sdf.format(new Date(durationInMilliseconds - ((long) TimeZone.getDefault().getRawOffset())));
    }

    public static String getExpansionAPKFileName(Context c, boolean mainFile, int versionCode) {
        return (mainFile ? "main." : "patch.") + versionCode + "." + c.getPackageName() + ".obb";
    }

    public static String generateSaveFileName(Context c, String fileName) {
        return getSaveFilePath(c) + File.separator + fileName;
    }

    public static String getSaveFilePath(Context c) {
        return Environment.getExternalStorageDirectory().toString() + (Build.VERSION.SDK_INT >= 23 ? Constants.EXP_PATH_API23 : Constants.EXP_PATH) + c.getPackageName();
    }

    public static boolean doesFileExist(Context c, String fileName, long fileSize, boolean deleteFileOnMismatch) {
        File fileForNewFile = new File(generateSaveFileName(c, fileName));
        Log.e("fileForNewFile path: ", generateSaveFileName(c, fileName));
        Log.e("fileForNewFile length: ", String.valueOf(fileForNewFile.length()));
        if (fileForNewFile.exists()) {
            if (fileForNewFile.length() == fileSize) {
                return true;
            }
            if (deleteFileOnMismatch) {
                fileForNewFile.delete();
            }
        }
        return false;
    }

    public static int getDownloaderStringResourceIDFromState(int state) {
        switch (state) {
            case 1:
                return C0788R.string.state_idle;
            case 2:
                return C0788R.string.state_fetching_url;
            case 3:
                return C0788R.string.state_connecting;
            case 4:
                return C0788R.string.state_downloading;
            case 5:
                return C0788R.string.state_completed;
            case 6:
                return C0788R.string.state_paused_network_unavailable;
            case 7:
                return C0788R.string.state_paused_by_request;
            case 8:
                return C0788R.string.state_paused_wifi_disabled;
            case 9:
                return C0788R.string.state_paused_wifi_unavailable;
            case 10:
                return C0788R.string.state_paused_wifi_disabled;
            case 11:
                return C0788R.string.state_paused_wifi_unavailable;
            case 12:
                return C0788R.string.state_paused_roaming;
            case 13:
                return C0788R.string.state_paused_network_setup_failure;
            case 14:
                return C0788R.string.state_paused_sdcard_unavailable;
            case 15:
                return C0788R.string.state_failed_unlicensed;
            case 16:
                return C0788R.string.state_failed_fetching_url;
            case 17:
                return C0788R.string.state_failed_sdcard_full;
            case 18:
                return C0788R.string.state_failed_cancelled;
            default:
                return C0788R.string.state_unknown;
        }
    }
}
