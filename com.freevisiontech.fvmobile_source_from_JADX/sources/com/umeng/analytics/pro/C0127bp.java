package com.umeng.analytics.pro;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/* renamed from: com.umeng.analytics.pro.bp */
/* compiled from: AdvertisingId */
public class C0127bp {

    /* renamed from: com.umeng.analytics.pro.bp$a */
    /* compiled from: AdvertisingId */
    private static final class C0129a {

        /* renamed from: a */
        private final String f492a;

        /* renamed from: b */
        private final boolean f493b;

        C0129a(String str, boolean z) {
            this.f492a = str;
            this.f493b = z;
        }

        /* access modifiers changed from: private */
        /* renamed from: b */
        public String m746b() {
            return this.f492a;
        }

        /* renamed from: a */
        public boolean mo483a() {
            return this.f493b;
        }
    }

    /* renamed from: a */
    public static String m743a(Context context) {
        try {
            C0129a b = m744b(context);
            if (b == null) {
                return null;
            }
            return b.m746b();
        } catch (Exception e) {
            return null;
        }
    }

    /* renamed from: b */
    private static C0129a m744b(Context context) throws Exception {
        try {
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            C0130b bVar = new C0130b();
            Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
            intent.setPackage("com.google.android.gms");
            if (context.bindService(intent, bVar, 1)) {
                try {
                    C0131c cVar = new C0131c(bVar.mo484a());
                    C0129a aVar = new C0129a(cVar.mo487a(), cVar.mo488a(true));
                    context.unbindService(bVar);
                    return aVar;
                } catch (Exception e) {
                    throw e;
                } catch (Throwable th) {
                    context.unbindService(bVar);
                    throw th;
                }
            } else {
                throw new IOException("Google Play connection failed");
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    /* renamed from: com.umeng.analytics.pro.bp$b */
    /* compiled from: AdvertisingId */
    private static final class C0130b implements ServiceConnection {

        /* renamed from: a */
        boolean f494a;

        /* renamed from: b */
        private final LinkedBlockingQueue<IBinder> f495b;

        private C0130b() {
            this.f494a = false;
            this.f495b = new LinkedBlockingQueue<>(1);
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.f495b.put(iBinder);
            } catch (InterruptedException e) {
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
        }

        /* renamed from: a */
        public IBinder mo484a() throws InterruptedException {
            if (this.f494a) {
                throw new IllegalStateException();
            }
            this.f494a = true;
            return this.f495b.take();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bp$c */
    /* compiled from: AdvertisingId */
    private static final class C0131c implements IInterface {

        /* renamed from: a */
        private IBinder f496a;

        public C0131c(IBinder iBinder) {
            this.f496a = iBinder;
        }

        public IBinder asBinder() {
            return this.f496a;
        }

        /* renamed from: a */
        public String mo487a() throws RemoteException {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.f496a.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                return obtain2.readString();
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        /* renamed from: a */
        public boolean mo488a(boolean z) throws RemoteException {
            boolean z2 = true;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                obtain.writeInt(z ? 1 : 0);
                this.f496a.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() == 0) {
                    z2 = false;
                }
                return z2;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
    }
}
