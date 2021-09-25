package com.bumptech.glide.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.bumptech.glide.manager.ConnectivityMonitor;

class DefaultConnectivityMonitor implements ConnectivityMonitor {
    private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean wasConnected = DefaultConnectivityMonitor.this.isConnected;
            boolean unused = DefaultConnectivityMonitor.this.isConnected = DefaultConnectivityMonitor.this.isConnected(context);
            if (wasConnected != DefaultConnectivityMonitor.this.isConnected) {
                DefaultConnectivityMonitor.this.listener.onConnectivityChanged(DefaultConnectivityMonitor.this.isConnected);
            }
        }
    };
    private final Context context;
    /* access modifiers changed from: private */
    public boolean isConnected;
    private boolean isRegistered;
    /* access modifiers changed from: private */
    public final ConnectivityMonitor.ConnectivityListener listener;

    public DefaultConnectivityMonitor(Context context2, ConnectivityMonitor.ConnectivityListener listener2) {
        this.context = context2.getApplicationContext();
        this.listener = listener2;
    }

    private void register() {
        if (!this.isRegistered) {
            this.isConnected = isConnected(this.context);
            this.context.registerReceiver(this.connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.isRegistered = true;
        }
    }

    private void unregister() {
        if (this.isRegistered) {
            this.context.unregisterReceiver(this.connectivityReceiver);
            this.isRegistered = false;
        }
    }

    /* access modifiers changed from: private */
    public boolean isConnected(Context context2) {
        NetworkInfo networkInfo = ((ConnectivityManager) context2.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void onStart() {
        register();
    }

    public void onStop() {
        unregister();
    }

    public void onDestroy() {
    }
}
