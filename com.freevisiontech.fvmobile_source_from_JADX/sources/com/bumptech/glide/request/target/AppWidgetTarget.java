package com.bumptech.glide.request.target;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.bumptech.glide.request.animation.GlideAnimation;

public class AppWidgetTarget extends SimpleTarget<Bitmap> {
    private final ComponentName componentName;
    private final Context context;
    private final RemoteViews remoteViews;
    private final int viewId;
    private final int[] widgetIds;

    public AppWidgetTarget(Context context2, RemoteViews remoteViews2, int viewId2, int width, int height, int... widgetIds2) {
        super(width, height);
        if (context2 == null) {
            throw new NullPointerException("Context can not be null!");
        } else if (widgetIds2 == null) {
            throw new NullPointerException("WidgetIds can not be null!");
        } else if (widgetIds2.length == 0) {
            throw new IllegalArgumentException("WidgetIds must have length > 0");
        } else if (remoteViews2 == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        } else {
            this.context = context2;
            this.remoteViews = remoteViews2;
            this.viewId = viewId2;
            this.widgetIds = widgetIds2;
            this.componentName = null;
        }
    }

    public AppWidgetTarget(Context context2, RemoteViews remoteViews2, int viewId2, int... widgetIds2) {
        this(context2, remoteViews2, viewId2, Integer.MIN_VALUE, Integer.MIN_VALUE, widgetIds2);
    }

    public AppWidgetTarget(Context context2, RemoteViews remoteViews2, int viewId2, int width, int height, ComponentName componentName2) {
        super(width, height);
        if (context2 == null) {
            throw new NullPointerException("Context can not be null!");
        } else if (componentName2 == null) {
            throw new NullPointerException("ComponentName can not be null!");
        } else if (remoteViews2 == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        } else {
            this.context = context2;
            this.remoteViews = remoteViews2;
            this.viewId = viewId2;
            this.componentName = componentName2;
            this.widgetIds = null;
        }
    }

    public AppWidgetTarget(Context context2, RemoteViews remoteViews2, int viewId2, ComponentName componentName2) {
        this(context2, remoteViews2, viewId2, Integer.MIN_VALUE, Integer.MIN_VALUE, componentName2);
    }

    private void update() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.context);
        if (this.componentName != null) {
            appWidgetManager.updateAppWidget(this.componentName, this.remoteViews);
        } else {
            appWidgetManager.updateAppWidget(this.widgetIds, this.remoteViews);
        }
    }

    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        this.remoteViews.setImageViewBitmap(this.viewId, resource);
        update();
    }
}
