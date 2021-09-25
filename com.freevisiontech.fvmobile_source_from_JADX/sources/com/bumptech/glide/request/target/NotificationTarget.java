package com.bumptech.glide.request.target;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.bumptech.glide.request.animation.GlideAnimation;

public class NotificationTarget extends SimpleTarget<Bitmap> {
    private final Context context;
    private final Notification notification;
    private final int notificationId;
    private final RemoteViews remoteViews;
    private final int viewId;

    public NotificationTarget(Context context2, RemoteViews remoteViews2, int viewId2, Notification notification2, int notificationId2) {
        this(context2, remoteViews2, viewId2, Integer.MIN_VALUE, Integer.MIN_VALUE, notification2, notificationId2);
    }

    public NotificationTarget(Context context2, RemoteViews remoteViews2, int viewId2, int width, int height, Notification notification2, int notificationId2) {
        super(width, height);
        if (context2 == null) {
            throw new NullPointerException("Context must not be null!");
        } else if (notification2 == null) {
            throw new NullPointerException("Notification object can not be null!");
        } else if (remoteViews2 == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        } else {
            this.context = context2;
            this.viewId = viewId2;
            this.notification = notification2;
            this.notificationId = notificationId2;
            this.remoteViews = remoteViews2;
        }
    }

    private void update() {
        ((NotificationManager) this.context.getSystemService("notification")).notify(this.notificationId, this.notification);
    }

    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        this.remoteViews.setImageViewBitmap(this.viewId, resource);
        update();
    }
}
