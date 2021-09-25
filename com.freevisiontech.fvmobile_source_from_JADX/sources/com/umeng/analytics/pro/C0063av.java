package com.umeng.analytics.pro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.C0015a;
import java.net.URLEncoder;

/* renamed from: com.umeng.analytics.pro.av */
/* compiled from: NetworkHelper */
public class C0063av {

    /* renamed from: a */
    private String f245a;

    /* renamed from: b */
    private String f246b = "10.0.0.172";

    /* renamed from: c */
    private int f247c = 80;

    /* renamed from: d */
    private Context f248d;

    /* renamed from: e */
    private C0062au f249e;

    public C0063av(Context context) {
        this.f248d = context;
        this.f245a = m273a(context);
    }

    /* renamed from: a */
    public void mo200a(C0062au auVar) {
        this.f249e = auVar;
    }

    /* renamed from: a */
    private void m274a() {
        String d = C0037af.m144a(this.f248d).mo140b().mo157d("");
        String c = C0037af.m144a(this.f248d).mo140b().mo155c("");
        if (!TextUtils.isEmpty(d)) {
            C0015a.f19f = C0133br.m757b(d);
        }
        if (!TextUtils.isEmpty(c)) {
            C0015a.f20g = C0133br.m757b(c);
        }
        C0015a.f21h = new String[]{C0015a.f19f, C0015a.f20g};
        int b = C0074be.m336a(this.f248d).mo233b();
        if (b == -1) {
            return;
        }
        if (b == 0) {
            C0015a.f21h = new String[]{C0015a.f19f, C0015a.f20g};
        } else if (b == 1) {
            C0015a.f21h = new String[]{C0015a.f20g, C0015a.f19f};
        }
    }

    /* renamed from: a */
    public byte[] mo201a(byte[] bArr) {
        byte[] bArr2 = null;
        m274a();
        int i = 0;
        while (true) {
            if (i >= C0015a.f21h.length) {
                break;
            }
            bArr2 = m275a(bArr, C0015a.f21h[i]);
            if (bArr2 == null) {
                if (this.f249e != null) {
                    this.f249e.mo199d();
                }
                i++;
            } else if (this.f249e != null) {
                this.f249e.mo198c();
            }
        }
        return bArr2;
    }

    /* renamed from: b */
    private boolean m276b() {
        String extraInfo;
        if (this.f248d.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", this.f248d.getPackageName()) != 0) {
            return false;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.f248d.getSystemService("connectivity");
            if (!C0135bt.m778a(this.f248d, "android.permission.ACCESS_NETWORK_STATE")) {
                return false;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (!(activeNetworkInfo == null || activeNetworkInfo.getType() == 1 || (extraInfo = activeNetworkInfo.getExtraInfo()) == null || (!extraInfo.equals("cmwap") && !extraInfo.equals("3gwap") && !extraInfo.equals("uniwap")))) {
                return true;
            }
            return false;
        } catch (Throwable th) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x00fc  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x010c  */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] m275a(byte[] r9, java.lang.String r10) {
        /*
            r8 = this;
            r4 = 0
            r1 = 0
            r3 = 1
            com.umeng.analytics.pro.au r0 = r8.f249e     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            if (r0 == 0) goto L_0x000c
            com.umeng.analytics.pro.au r0 = r8.f249e     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            r0.mo196a()     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
        L_0x000c:
            boolean r0 = r8.m276b()     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            if (r0 == 0) goto L_0x00e0
            java.net.Proxy r0 = new java.net.Proxy     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            java.net.Proxy$Type r2 = java.net.Proxy.Type.HTTP     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            java.net.InetSocketAddress r5 = new java.net.InetSocketAddress     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            java.lang.String r6 = r8.f246b     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            int r7 = r8.f247c     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            r5.<init>(r6, r7)     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            r0.<init>(r2, r5)     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            java.net.URL r2 = new java.net.URL     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            r2.<init>(r10)     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            java.net.URLConnection r0 = r2.openConnection(r0)     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            r2 = r0
        L_0x002e:
            java.lang.String r0 = "X-Umeng-UTC"
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00f3 }
            java.lang.String r5 = java.lang.String.valueOf(r6)     // Catch:{ Throwable -> 0x00f3 }
            r2.setRequestProperty(r0, r5)     // Catch:{ Throwable -> 0x00f3 }
            java.lang.String r0 = "X-Umeng-Sdk"
            java.lang.String r5 = r8.f245a     // Catch:{ Throwable -> 0x00f3 }
            r2.setRequestProperty(r0, r5)     // Catch:{ Throwable -> 0x00f3 }
            java.lang.String r0 = "Msg-Type"
            java.lang.String r5 = "envelope/json"
            r2.setRequestProperty(r0, r5)     // Catch:{ Throwable -> 0x00f3 }
            java.lang.String r0 = "Content-Type"
            java.lang.String r5 = "envelope/json"
            r2.setRequestProperty(r0, r5)     // Catch:{ Throwable -> 0x00f3 }
            r0 = 10000(0x2710, float:1.4013E-41)
            r2.setConnectTimeout(r0)     // Catch:{ Throwable -> 0x00f3 }
            r0 = 30000(0x7530, float:4.2039E-41)
            r2.setReadTimeout(r0)     // Catch:{ Throwable -> 0x00f3 }
            java.lang.String r0 = "POST"
            r2.setRequestMethod(r0)     // Catch:{ Throwable -> 0x00f3 }
            r0 = 1
            r2.setDoOutput(r0)     // Catch:{ Throwable -> 0x00f3 }
            r0 = 1
            r2.setDoInput(r0)     // Catch:{ Throwable -> 0x00f3 }
            r0 = 0
            r2.setUseCaches(r0)     // Catch:{ Throwable -> 0x00f3 }
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x00f3 }
            r5 = 8
            if (r0 >= r5) goto L_0x0081
            java.lang.String r0 = "http.keepAlive"
            java.lang.String r5 = "false"
            java.lang.System.setProperty(r0, r5)     // Catch:{ Throwable -> 0x00f3 }
        L_0x0081:
            java.io.OutputStream r0 = r2.getOutputStream()     // Catch:{ Throwable -> 0x00f3 }
            r0.write(r9)     // Catch:{ Throwable -> 0x00f3 }
            r0.flush()     // Catch:{ Throwable -> 0x00f3 }
            r0.close()     // Catch:{ Throwable -> 0x00f3 }
            com.umeng.analytics.pro.au r0 = r8.f249e     // Catch:{ Throwable -> 0x00f3 }
            if (r0 == 0) goto L_0x0097
            com.umeng.analytics.pro.au r0 = r8.f249e     // Catch:{ Throwable -> 0x00f3 }
            r0.mo197b()     // Catch:{ Throwable -> 0x00f3 }
        L_0x0097:
            int r5 = r2.getResponseCode()     // Catch:{ Throwable -> 0x00f3 }
            java.lang.String r0 = "Content-Type"
            java.lang.String r0 = r2.getHeaderField(r0)     // Catch:{ Throwable -> 0x00f3 }
            boolean r6 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x00f3 }
            if (r6 != 0) goto L_0x0115
            java.lang.String r6 = "application/thrift"
            boolean r0 = r0.equalsIgnoreCase(r6)     // Catch:{ Throwable -> 0x00f3 }
            if (r0 == 0) goto L_0x0115
            r0 = r3
        L_0x00b2:
            r3 = 200(0xc8, float:2.8E-43)
            if (r5 != r3) goto L_0x0101
            if (r0 == 0) goto L_0x0101
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00f3 }
            r0.<init>()     // Catch:{ Throwable -> 0x00f3 }
            java.lang.String r3 = "Send message to "
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ Throwable -> 0x00f3 }
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ Throwable -> 0x00f3 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x00f3 }
            com.umeng.analytics.pro.C0138bw.m837c((java.lang.String) r0)     // Catch:{ Throwable -> 0x00f3 }
            java.io.InputStream r3 = r2.getInputStream()     // Catch:{ Throwable -> 0x00f3 }
            byte[] r0 = com.umeng.analytics.pro.C0136bu.m820b((java.io.InputStream) r3)     // Catch:{ all -> 0x00ee }
            com.umeng.analytics.pro.C0136bu.m821c(r3)     // Catch:{ Throwable -> 0x00f3 }
            if (r2 == 0) goto L_0x00df
            r2.disconnect()
        L_0x00df:
            return r0
        L_0x00e0:
            java.net.URL r0 = new java.net.URL     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            r0.<init>(r10)     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x0112, all -> 0x0108 }
            r2 = r0
            goto L_0x002e
        L_0x00ee:
            r0 = move-exception
            com.umeng.analytics.pro.C0136bu.m821c(r3)     // Catch:{ Throwable -> 0x00f3 }
            throw r0     // Catch:{ Throwable -> 0x00f3 }
        L_0x00f3:
            r0 = move-exception
        L_0x00f4:
            java.lang.String r3 = "IOException,Failed to send message."
            com.umeng.analytics.pro.C0138bw.m851e((java.lang.String) r3, (java.lang.Throwable) r0)     // Catch:{ all -> 0x0110 }
            if (r2 == 0) goto L_0x00ff
            r2.disconnect()
        L_0x00ff:
            r0 = r1
            goto L_0x00df
        L_0x0101:
            if (r2 == 0) goto L_0x0106
            r2.disconnect()
        L_0x0106:
            r0 = r1
            goto L_0x00df
        L_0x0108:
            r0 = move-exception
            r2 = r1
        L_0x010a:
            if (r2 == 0) goto L_0x010f
            r2.disconnect()
        L_0x010f:
            throw r0
        L_0x0110:
            r0 = move-exception
            goto L_0x010a
        L_0x0112:
            r0 = move-exception
            r2 = r1
            goto L_0x00f4
        L_0x0115:
            r0 = r4
            goto L_0x00b2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0063av.m275a(byte[], java.lang.String):byte[]");
    }

    /* renamed from: a */
    private String m273a(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Android");
        stringBuffer.append("/");
        stringBuffer.append(C0132bq.f497a);
        stringBuffer.append(" ");
        try {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(C0135bt.m806v(context));
            stringBuffer2.append("/");
            stringBuffer2.append(C0135bt.m781b(context));
            stringBuffer2.append(" ");
            stringBuffer2.append(Build.MODEL);
            stringBuffer2.append("/");
            stringBuffer2.append(Build.VERSION.RELEASE);
            stringBuffer2.append(" ");
            stringBuffer2.append(C0136bu.m813a(AnalyticsConfig.getAppkey(context)));
            stringBuffer.append(URLEncoder.encode(stringBuffer2.toString(), "UTF-8"));
        } catch (Throwable th) {
        }
        return stringBuffer.toString();
    }
}
