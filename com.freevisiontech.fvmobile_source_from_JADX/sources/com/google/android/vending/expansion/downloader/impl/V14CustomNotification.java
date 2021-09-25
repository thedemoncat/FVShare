package com.google.android.vending.expansion.downloader.impl;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import com.android.vending.expansion.downloader.C0788R;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.impl.DownloadNotification;

public class V14CustomNotification implements DownloadNotification.ICustomNotification {
    long mCurrentKB = -1;
    int mIcon;
    PendingIntent mPendingIntent;
    CharSequence mTicker;
    long mTimeRemaining;
    CharSequence mTitle;
    long mTotalKB = -1;

    public void setIcon(int icon) {
        this.mIcon = icon;
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
    }

    public void setTotalBytes(long totalBytes) {
        this.mTotalKB = totalBytes;
    }

    public void setCurrentBytes(long currentBytes) {
        this.mCurrentKB = currentBytes;
    }

    /* access modifiers changed from: package-private */
    public void setProgress(Notification.Builder builder) {
    }

    public Notification.Builder updateNotification(Context c) {
        Notification.Builder builder = new Notification.Builder(c);
        builder.setContentTitle(this.mTitle);
        if (this.mTotalKB <= 0 || -1 == this.mCurrentKB) {
            builder.setProgress(0, 0, true);
        } else {
            builder.setProgress((int) (this.mTotalKB >> 8), (int) (this.mCurrentKB >> 8), false);
        }
        builder.setContentText(Helpers.getDownloadProgressString(this.mCurrentKB, this.mTotalKB));
        builder.setContentInfo(c.getString(C0788R.string.time_remaining_notification, new Object[]{Helpers.getTimeRemaining(this.mTimeRemaining)}));
        if (this.mIcon != 0) {
            builder.setSmallIcon(this.mIcon);
        } else {
            builder.setSmallIcon(17301633);
        }
        builder.setOngoing(true);
        builder.setTicker(this.mTicker);
        builder.setContentIntent(this.mPendingIntent);
        builder.setOnlyAlertOnce(true);
        return builder;
    }

    public void setPendingIntent(PendingIntent contentIntent) {
        this.mPendingIntent = contentIntent;
    }

    public void setTicker(CharSequence ticker) {
        this.mTicker = ticker;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.mTimeRemaining = timeRemaining;
    }
}
