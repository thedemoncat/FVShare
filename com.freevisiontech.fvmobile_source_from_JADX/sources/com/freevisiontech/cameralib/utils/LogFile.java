package com.freevisiontech.cameralib.utils;

import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogFile {
    private static String MYLOGFILEName = "Log.txt";
    private static String MYLOG_PATH_SDCARD_DIR = "/sdcard/";
    private static Boolean MYLOG_SWITCH = true;
    private static char MYLOG_TYPE = 'v';
    private static Boolean MYLOG_WRITE_TO_FILE = true;
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 0;
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /* renamed from: w */
    public static void m1516w(String tag, Object msg) {
        log(tag, msg.toString(), 'w');
    }

    /* renamed from: e */
    public static void m1510e(String tag, Object msg) {
        log(tag, msg.toString(), 'e');
    }

    /* renamed from: d */
    public static void m1508d(String tag, Object msg) {
        log(tag, msg.toString(), 'd');
    }

    /* renamed from: i */
    public static void m1512i(String tag, Object msg) {
        log(tag, msg.toString(), 'i');
    }

    /* renamed from: v */
    public static void m1514v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    /* renamed from: w */
    public static void m1517w(String tag, String text) {
        log(tag, text, 'w');
    }

    /* renamed from: e */
    public static void m1511e(String tag, String text) {
        log(tag, text, 'e');
    }

    /* renamed from: d */
    public static void m1509d(String tag, String text) {
        log(tag, text, 'd');
    }

    /* renamed from: i */
    public static void m1513i(String tag, String text) {
        log(tag, text, 'i');
    }

    /* renamed from: v */
    public static void m1515v(String tag, String text) {
        log(tag, text, 'v');
    }

    private static void log(String tag, String msg, char level) {
        if (MYLOG_SWITCH.booleanValue()) {
            if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.e(tag, msg);
            } else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.w(tag, msg);
            } else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.d(tag, msg);
            } else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.i(tag, msg);
            } else {
                Log.v(tag, msg);
            }
            if (MYLOG_WRITE_TO_FILE.booleanValue()) {
                writeLogtoFile(String.valueOf(level), tag, msg);
            }
        }
    }

    private static void writeLogtoFile(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype + "    " + tag + "    " + text;
        try {
            FileWriter filerWriter = new FileWriter(new File(MYLOG_PATH_SDCARD_DIR, logfile.format(nowtime) + MYLOGFILEName), true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delFile() {
        File file = new File(MYLOG_PATH_SDCARD_DIR, logfile.format(getDateBefore()) + MYLOGFILEName);
        if (file.exists()) {
            file.delete();
        }
    }

    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(5, now.get(5) - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }
}
