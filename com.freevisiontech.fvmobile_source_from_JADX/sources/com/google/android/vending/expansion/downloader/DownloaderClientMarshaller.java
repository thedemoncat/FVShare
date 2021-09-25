package com.google.android.vending.expansion.downloader;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.google.android.vending.expansion.downloader.impl.DownloaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloaderClientMarshaller {
    public static final int DOWNLOAD_REQUIRED = 2;
    private static final Logger LOG = LoggerFactory.getLogger("DownloaderClientMarshaller");
    public static final int LVL_CHECK_REQUIRED = 1;
    public static final int MSG_ONDOWNLOADPROGRESS = 11;
    public static final int MSG_ONDOWNLOADSTATE_CHANGED = 10;
    public static final int MSG_ONSERVICECONNECTED = 12;
    public static final int NO_DOWNLOAD_REQUIRED = 0;
    public static final String PARAM_MESSENGER = "EMH";
    public static final String PARAM_NEW_STATE = "newState";
    public static final String PARAM_PROGRESS = "progress";

    private static class Proxy implements IDownloaderClient {
        private Messenger mServiceMessenger;

        public void onDownloadStateChanged(int newState) {
            Bundle params = new Bundle(1);
            params.putInt(DownloaderClientMarshaller.PARAM_NEW_STATE, newState);
            send(10, params);
        }

        public void onDownloadProgress(DownloadProgressInfo progress) {
            Bundle params = new Bundle(1);
            params.putParcelable("progress", progress);
            send(11, params);
        }

        private void send(int method, Bundle params) {
            Message m = Message.obtain((Handler) null, method);
            m.setData(params);
            try {
                this.mServiceMessenger.send(m);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public Proxy(Messenger msg) {
            this.mServiceMessenger = msg;
        }

        public void onServiceConnected(Messenger m) {
        }
    }

    private static class Stub implements IStub {
        private boolean mBound;
        private ServiceConnection mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                Messenger unused = Stub.this.mServiceMessenger = new Messenger(service);
                Stub.this.mItf.onServiceConnected(Stub.this.mServiceMessenger);
            }

            public void onServiceDisconnected(ComponentName className) {
                Messenger unused = Stub.this.mServiceMessenger = null;
            }
        };
        /* access modifiers changed from: private */
        public Context mContext;
        private Class<?> mDownloaderServiceClass;
        /* access modifiers changed from: private */
        public IDownloaderClient mItf = null;
        final Messenger mMessenger = new Messenger(new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 10:
                        Stub.this.mItf.onDownloadStateChanged(msg.getData().getInt(DownloaderClientMarshaller.PARAM_NEW_STATE));
                        return;
                    case 11:
                        Bundle bun = msg.getData();
                        if (Stub.this.mContext != null) {
                            bun.setClassLoader(Stub.this.mContext.getClassLoader());
                            Stub.this.mItf.onDownloadProgress((DownloadProgressInfo) msg.getData().getParcelable("progress"));
                            return;
                        }
                        return;
                    case 12:
                        Stub.this.mItf.onServiceConnected((Messenger) msg.getData().getParcelable("EMH"));
                        return;
                    default:
                        return;
                }
            }
        });
        /* access modifiers changed from: private */
        public Messenger mServiceMessenger;

        public Stub(IDownloaderClient itf, Class<?> downloaderService) {
            this.mItf = itf;
            this.mDownloaderServiceClass = downloaderService;
        }

        public void connect(Context c) {
            this.mContext = c;
            Intent bindIntent = new Intent(c, this.mDownloaderServiceClass);
            bindIntent.putExtra("EMH", this.mMessenger);
            if (c.bindService(bindIntent, this.mConnection, 1)) {
                this.mBound = true;
            }
        }

        public void disconnect(Context c) {
            if (this.mBound) {
                c.unbindService(this.mConnection);
                this.mBound = false;
            }
            this.mContext = null;
        }

        public Messenger getMessenger() {
            return this.mMessenger;
        }
    }

    public static IDownloaderClient CreateProxy(Messenger msg) {
        return new Proxy(msg);
    }

    public static IStub CreateStub(IDownloaderClient itf, Class<?> downloaderService) {
        return new Stub(itf, downloaderService);
    }

    public static int startDownloadServiceIfRequired(Context context, PendingIntent notificationClient, Class<?> serviceClass) throws PackageManager.NameNotFoundException {
        return DownloaderService.startDownloadServiceIfRequired(context, notificationClient, serviceClass);
    }

    public static int startDownloadServiceIfRequired(Context context, Intent notificationClient, Class<?> serviceClass) throws PackageManager.NameNotFoundException {
        return DownloaderService.startDownloadServiceIfRequired(context, notificationClient, serviceClass);
    }
}
