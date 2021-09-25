package com.freevisiontech.fvmobile.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;

public class DownloadExpansionAlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            DownloaderClientMarshaller.startDownloadServiceIfRequired(context, intent, (Class<?>) DownloadExpansionService.class);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
