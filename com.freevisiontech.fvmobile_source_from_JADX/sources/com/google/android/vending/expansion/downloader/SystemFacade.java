package com.google.android.vending.expansion.downloader;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SystemFacade {
    private static final Logger LOG = LoggerFactory.getLogger("SystemFacade");
    private Context mContext;
    private NotificationManager mNotificationManager = ((NotificationManager) this.mContext.getSystemService("notification"));

    public SystemFacade(Context context) {
        this.mContext = context;
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public Integer getActiveNetworkType() {
        ConnectivityManager connectivity = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        if (connectivity == null) {
            LOG.warn("couldn't get connectivity manager");
            return null;
        }
        NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
        if (activeInfo != null) {
            return Integer.valueOf(activeInfo.getType());
        }
        return null;
    }

    public boolean isNetworkRoaming() {
        boolean isMobile;
        boolean isRoaming;
        ConnectivityManager connectivity = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        if (connectivity == null) {
            LOG.warn("couldn't get connectivity manager");
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null || info.getType() != 0) {
            isMobile = false;
        } else {
            isMobile = true;
        }
        TelephonyManager tm = (TelephonyManager) this.mContext.getSystemService("phone");
        if (tm == null) {
            LOG.warn("couldn't get telephony manager");
            return false;
        }
        if (!isMobile || !tm.isNetworkRoaming()) {
            isRoaming = false;
        } else {
            isRoaming = true;
        }
        return isRoaming;
    }

    public Long getMaxBytesOverMobile() {
        return 2147483647L;
    }

    public Long getRecommendedMaxBytesOverMobile() {
        return 2097152L;
    }

    public void sendBroadcast(Intent intent) {
        this.mContext.sendBroadcast(intent);
    }

    public boolean userOwnsPackage(int uid, String packageName) throws PackageManager.NameNotFoundException {
        return this.mContext.getPackageManager().getApplicationInfo(packageName, 0).uid == uid;
    }

    public void postNotification(long id, Notification notification) {
        this.mNotificationManager.notify((int) id, notification);
    }

    public void cancelNotification(long id) {
        this.mNotificationManager.cancel((int) id);
    }

    public void cancelAllNotifications() {
        this.mNotificationManager.cancelAll();
    }

    public void startThread(Thread thread) {
        thread.start();
    }
}
