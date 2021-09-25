package com.google.android.vending.expansion.downloader.impl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Messenger;
import com.android.vending.expansion.downloader.C0788R;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.IDownloaderClient;

public class DownloadNotification implements IDownloaderClient {
    static final String LOGTAG = "DownloadNotification";
    static final int NOTIFICATION_ID = LOGTAG.hashCode();
    private IDownloaderClient mClientProxy;
    private PendingIntent mContentIntent;
    private final Context mContext;
    private Notification.Builder mCurrentNotificationBuilder;
    private String mCurrentText;
    private String mCurrentTitle;
    final ICustomNotification mCustomNotification;
    private CharSequence mLabel;
    private Notification.Builder mNotificationBuilder;
    private final NotificationManager mNotificationManager;
    private DownloadProgressInfo mProgressInfo;
    private int mState = -1;

    public interface ICustomNotification {
        void setCurrentBytes(long j);

        void setIcon(int i);

        void setPendingIntent(PendingIntent pendingIntent);

        void setTicker(CharSequence charSequence);

        void setTimeRemaining(long j);

        void setTitle(CharSequence charSequence);

        void setTotalBytes(long j);

        Notification.Builder updateNotification(Context context);
    }

    public PendingIntent getClientIntent() {
        return this.mContentIntent;
    }

    public void setClientIntent(PendingIntent mClientIntent) {
        this.mContentIntent = mClientIntent;
    }

    public void resendState() {
        if (this.mClientProxy != null) {
            this.mClientProxy.onDownloadStateChanged(this.mState);
        }
    }

    public void onDownloadStateChanged(int newState) {
        int iconResource;
        int stringDownloadID;
        boolean ongoingEvent;
        boolean z = true;
        boolean completed = false;
        if (this.mClientProxy != null) {
            this.mClientProxy.onDownloadStateChanged(newState);
        }
        if (newState != this.mState) {
            this.mState = newState;
            if (newState != 1 && this.mContentIntent != null) {
                switch (newState) {
                    case 0:
                        iconResource = 17301642;
                        stringDownloadID = C0788R.string.state_unknown;
                        ongoingEvent = false;
                        break;
                    case 2:
                    case 3:
                        iconResource = 17301634;
                        stringDownloadID = Helpers.getDownloaderStringResourceIDFromState(newState);
                        ongoingEvent = true;
                        break;
                    case 4:
                        iconResource = 17301633;
                        stringDownloadID = Helpers.getDownloaderStringResourceIDFromState(newState);
                        ongoingEvent = true;
                        break;
                    case 5:
                        completed = true;
                        break;
                    case 7:
                        break;
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                        iconResource = 17301642;
                        stringDownloadID = Helpers.getDownloaderStringResourceIDFromState(newState);
                        ongoingEvent = false;
                        break;
                    default:
                        iconResource = 17301642;
                        stringDownloadID = Helpers.getDownloaderStringResourceIDFromState(newState);
                        ongoingEvent = true;
                        break;
                }
                iconResource = 17301634;
                stringDownloadID = Helpers.getDownloaderStringResourceIDFromState(newState);
                ongoingEvent = false;
                if (completed) {
                    this.mNotificationManager.cancel(NOTIFICATION_ID);
                    return;
                }
                this.mCurrentText = this.mContext.getString(stringDownloadID);
                this.mCurrentTitle = this.mLabel.toString();
                this.mCurrentNotificationBuilder.setTicker(this.mLabel + ": " + this.mCurrentText);
                this.mCurrentNotificationBuilder.setSmallIcon(iconResource);
                this.mCurrentNotificationBuilder.setContentTitle(this.mCurrentTitle);
                this.mCurrentNotificationBuilder.setContentText(this.mCurrentText);
                this.mCurrentNotificationBuilder.setContentIntent(this.mContentIntent);
                this.mCurrentNotificationBuilder.setOngoing(ongoingEvent);
                Notification.Builder builder = this.mCurrentNotificationBuilder;
                if (ongoingEvent) {
                    z = false;
                }
                builder.setAutoCancel(z);
                this.mNotificationManager.notify(NOTIFICATION_ID, this.mCurrentNotificationBuilder.build());
            }
        }
    }

    public void onDownloadProgress(DownloadProgressInfo progress) {
        this.mProgressInfo = progress;
        if (this.mClientProxy != null) {
            this.mClientProxy.onDownloadProgress(progress);
        }
        if (progress.mOverallTotal <= 0) {
            this.mNotificationBuilder.setTicker(this.mCurrentTitle);
            this.mNotificationBuilder.setSmallIcon(17301633);
            this.mNotificationBuilder.setContentTitle(this.mCurrentTitle);
            this.mNotificationBuilder.setContentText(this.mCurrentText);
            this.mNotificationBuilder.setContentIntent(this.mContentIntent);
            this.mCurrentNotificationBuilder = this.mNotificationBuilder;
        } else {
            this.mCustomNotification.setCurrentBytes(progress.mOverallProgress);
            this.mCustomNotification.setTotalBytes(progress.mOverallTotal);
            this.mCustomNotification.setIcon(17301633);
            this.mCustomNotification.setPendingIntent(this.mContentIntent);
            this.mCustomNotification.setTicker(this.mLabel + ": " + this.mCurrentText);
            this.mCustomNotification.setTitle(this.mLabel);
            this.mCustomNotification.setTimeRemaining(progress.mTimeRemaining);
            this.mCurrentNotificationBuilder = this.mCustomNotification.updateNotification(this.mContext);
        }
        this.mNotificationManager.notify(NOTIFICATION_ID, this.mCurrentNotificationBuilder.build());
    }

    public void setMessenger(Messenger msg) {
        this.mClientProxy = DownloaderClientMarshaller.CreateProxy(msg);
        if (this.mProgressInfo != null) {
            this.mClientProxy.onDownloadProgress(this.mProgressInfo);
        }
        if (this.mState != -1) {
            this.mClientProxy.onDownloadStateChanged(this.mState);
        }
    }

    DownloadNotification(Context ctx, CharSequence applicationLabel) {
        this.mContext = ctx;
        this.mLabel = applicationLabel;
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        this.mCustomNotification = CustomNotificationFactory.createCustomNotification();
        this.mNotificationBuilder = new Notification.Builder(ctx);
        this.mCurrentNotificationBuilder = this.mNotificationBuilder;
    }

    public void onServiceConnected(Messenger m) {
    }
}
