package com.google.android.vending.expansion.downloader.impl;

import android.os.Build;
import com.google.android.vending.expansion.downloader.impl.DownloadNotification;

public class CustomNotificationFactory {
    public static DownloadNotification.ICustomNotification createCustomNotification() {
        if (Build.VERSION.SDK_INT > 13) {
            return new V14CustomNotification();
        }
        throw new RuntimeException();
    }
}
