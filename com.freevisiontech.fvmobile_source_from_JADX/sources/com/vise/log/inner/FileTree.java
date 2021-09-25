package com.vise.log.inner;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTree extends Tree {
    private static final String FILE_NAME_ASSERT = "assert_";
    private static final String FILE_NAME_DEBUG = "debug_";
    private static final String FILE_NAME_ERROR = "error_";
    private static final String FILE_NAME_INFO = "info_";
    private static final String FILE_NAME_SUFFIX = ".log";
    private static final String FILE_NAME_VERBOSE = "verbose_";
    private static final String FILE_NAME_WARN = "warn_";
    private static final String PATH = (Environment.getExternalStorageDirectory().getPath() + File.separator);
    private Context mContext;
    private String mDirectory;
    private boolean mIsPrintPhoneInfo = false;
    private File mLogFile;

    public FileTree(Context mContext2, String mDirectory2) {
        this.mContext = mContext2;
        this.mDirectory = mDirectory2;
    }

    /* access modifiers changed from: protected */
    public void log(int type, String tag, String message) {
        saveMessageToSDCard(type, tag, message);
    }

    private void saveMessageToSDCard(int type, String tag, String message) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            System.out.print("sdcard unmounted, skip dump exception");
            return;
        }
        File dir = new File(PATH + this.mDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        String timeHour = new SimpleDateFormat("yyyy-MM-dd HH").format(new Date(System.currentTimeMillis()));
        String timeMinute = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis()));
        String timeSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String fileName = FILE_NAME_VERBOSE;
        switch (type) {
            case 2:
                fileName = FILE_NAME_VERBOSE + timeMinute;
                break;
            case 3:
                fileName = FILE_NAME_DEBUG + timeMinute;
                break;
            case 4:
                fileName = FILE_NAME_INFO + timeHour;
                break;
            case 5:
                fileName = FILE_NAME_WARN + timeDay;
                break;
            case 6:
                fileName = FILE_NAME_ERROR + timeSecond;
                break;
            case 7:
                fileName = FILE_NAME_ASSERT + timeDay;
                break;
        }
        this.mLogFile = new File(PATH + this.mDirectory + File.separator + fileName + FILE_NAME_SUFFIX);
        try {
            if (!this.mLogFile.exists()) {
                this.mLogFile.createNewFile();
                this.mIsPrintPhoneInfo = true;
            } else {
                this.mIsPrintPhoneInfo = false;
            }
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(this.mLogFile, true)));
            if (!this.mIsPrintPhoneInfo) {
                printWriter.println();
                printWriter.println();
            }
            printWriter.println(timeSecond);
            if (this.mIsPrintPhoneInfo) {
                printPhoneInfo(printWriter);
                printWriter.println();
            }
            printWriter.print(tag + "\t" + message);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPhoneInfo(PrintWriter printWriter) {
        try {
            PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 1);
            printWriter.print("App Version:");
            printWriter.print(packageInfo.versionName);
            printWriter.print('_');
            printWriter.println(packageInfo.versionCode);
            printWriter.print("OS Version:");
            printWriter.print(Build.VERSION.RELEASE);
            printWriter.print('_');
            printWriter.println(Build.VERSION.SDK_INT);
            printWriter.print("Vendor:");
            printWriter.println(Build.MANUFACTURER);
            printWriter.print("Model:");
            printWriter.println(Build.MODEL);
            printWriter.print("CPU ABI:");
            printWriter.println(Build.CPU_ABI);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
