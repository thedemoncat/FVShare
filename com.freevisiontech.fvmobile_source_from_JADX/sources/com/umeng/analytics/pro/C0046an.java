package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/* renamed from: com.umeng.analytics.pro.an */
/* compiled from: UUIDTracker */
public class C0046an extends C0282y {

    /* renamed from: a */
    private static final String f184a = "uuid";

    /* renamed from: e */
    private static final String f185e = "yosuid";

    /* renamed from: f */
    private static final String f186f = "23346339";

    /* renamed from: b */
    private Context f187b = null;

    /* renamed from: c */
    private String f188c = null;

    /* renamed from: d */
    private String f189d = null;

    public C0046an(Context context) {
        super("uuid");
        this.f187b = context;
        this.f188c = null;
        this.f189d = null;
    }

    /* renamed from: f */
    public String mo122f() {
        SharedPreferences a;
        SharedPreferences.Editor edit;
        try {
            if (!(TextUtils.isEmpty(m193a("ro.yunos.version", "")) || this.f187b == null || (a = C0067az.m285a(this.f187b)) == null)) {
                String string = a.getString(f185e, "");
                if (!TextUtils.isEmpty(string)) {
                    return string;
                }
                this.f189d = m194b(f186f);
                if (!(TextUtils.isEmpty(this.f189d) || this.f187b == null || a == null || (edit = a.edit()) == null)) {
                    edit.putString(f185e, this.f189d).commit();
                }
                return this.f189d;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* JADX WARNING: type inference failed for: r3v0 */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r3v2, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r3v3, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r3v12 */
    /* JADX WARNING: type inference failed for: r3v13 */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x01a4, code lost:
        r1 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x01a5, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x0172, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x0173, code lost:
        r4 = null;
        r6 = r1;
        r1 = r0;
        r0 = r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00e9 A[SYNTHETIC, Splitter:B:31:0x00e9] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00ee A[SYNTHETIC, Splitter:B:34:0x00ee] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00f3 A[SYNTHETIC, Splitter:B:37:0x00f3] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0121 A[SYNTHETIC, Splitter:B:57:0x0121] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0126 A[SYNTHETIC, Splitter:B:60:0x0126] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x012b A[SYNTHETIC, Splitter:B:63:0x012b] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0149 A[SYNTHETIC, Splitter:B:76:0x0149] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x014e A[SYNTHETIC, Splitter:B:79:0x014e] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0153 A[SYNTHETIC, Splitter:B:82:0x0153] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0158  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0172 A[ExcHandler: all (r1v28 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:13:0x00b7] */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String m194b(java.lang.String r8) {
        /*
            r7 = this;
            r3 = 0
            java.lang.String r0 = "ro.yunos.openuuid"
            java.lang.String r1 = ""
            java.lang.String r0 = m193a(r0, r1)
            r7.f189d = r0
            java.lang.String r0 = r7.f189d
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0018
            java.lang.String r0 = r7.f189d
        L_0x0017:
            return r0
        L_0x0018:
            java.lang.String r0 = "ro.aliyun.clouduuid"
            java.lang.String r1 = ""
            java.lang.String r0 = m193a(r0, r1)
            r7.f188c = r0
            java.lang.String r0 = r7.f188c
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0038
            java.lang.String r0 = "ro.sys.aliyun.clouduuid"
            java.lang.String r1 = ""
            java.lang.String r0 = m193a(r0, r1)
            r7.f188c = r0
        L_0x0038:
            java.lang.String r0 = r7.f188c
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x00fb
            java.net.URL r0 = new java.net.URL     // Catch:{ Exception -> 0x0118, all -> 0x0143 }
            java.lang.String r1 = "https://cmnsguider.yunos.com:443/genDeviceToken"
            r0.<init>(r1)     // Catch:{ Exception -> 0x0118, all -> 0x0143 }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Exception -> 0x0118, all -> 0x0143 }
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ Exception -> 0x0118, all -> 0x0143 }
            r1 = 30000(0x7530, float:4.2039E-41)
            r0.setConnectTimeout(r1)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            r1 = 30000(0x7530, float:4.2039E-41)
            r0.setReadTimeout(r1)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.String r1 = "POST"
            r0.setRequestMethod(r1)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            r1 = 1
            r0.setDoInput(r1)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            r1 = 1
            r0.setDoOutput(r1)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            r1 = 0
            r0.setUseCaches(r1)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.String r1 = "Content-Type"
            java.lang.String r2 = "application/x-www-form-urlencoded"
            r0.setRequestProperty(r1, r2)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            com.umeng.analytics.pro.an$1 r1 = new com.umeng.analytics.pro.an$1     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            r1.<init>()     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            r0.setHostnameVerifier(r1)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            r1.<init>()     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.String r2 = "appKey="
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.String r2 = "23338940"
            java.lang.String r4 = "UTF-8"
            java.lang.String r2 = java.net.URLEncoder.encode(r2, r4)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.String r2 = "&uuid="
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.String r2 = "FC1FE84794417B1BEF276234F6FB4E63"
            java.lang.String r4 = "UTF-8"
            java.lang.String r2 = java.net.URLEncoder.encode(r2, r4)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.io.DataOutputStream r5 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            java.io.OutputStream r2 = r0.getOutputStream()     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            r5.<init>(r2)     // Catch:{ Exception -> 0x018f, all -> 0x016b }
            r5.writeBytes(r1)     // Catch:{ Exception -> 0x0195, all -> 0x0172 }
            r5.flush()     // Catch:{ Exception -> 0x0195, all -> 0x0172 }
            int r1 = r0.getResponseCode()     // Catch:{ Exception -> 0x0195, all -> 0x0172 }
            r2 = 200(0xc8, float:2.8E-43)
            if (r1 != r2) goto L_0x01ad
            java.io.InputStream r4 = r0.getInputStream()     // Catch:{ Exception -> 0x01a4, all -> 0x0172 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x01a8, all -> 0x0178 }
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x01a8, all -> 0x0178 }
            r1.<init>(r4)     // Catch:{ Exception -> 0x01a8, all -> 0x0178 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x01a8, all -> 0x0178 }
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x00e2, all -> 0x017d }
            r1.<init>()     // Catch:{ Exception -> 0x00e2, all -> 0x017d }
        L_0x00d8:
            java.lang.String r3 = r2.readLine()     // Catch:{ Exception -> 0x00e2, all -> 0x017d }
            if (r3 == 0) goto L_0x00ff
            r1.append(r3)     // Catch:{ Exception -> 0x00e2, all -> 0x017d }
            goto L_0x00d8
        L_0x00e2:
            r1 = move-exception
            r3 = r4
        L_0x00e4:
            r1.printStackTrace()     // Catch:{ Exception -> 0x019b, all -> 0x0183 }
        L_0x00e7:
            if (r5 == 0) goto L_0x00ec
            r5.close()     // Catch:{ Exception -> 0x0109 }
        L_0x00ec:
            if (r2 == 0) goto L_0x00f1
            r2.close()     // Catch:{ Exception -> 0x010e }
        L_0x00f1:
            if (r3 == 0) goto L_0x00f6
            r3.close()     // Catch:{ Exception -> 0x0113 }
        L_0x00f6:
            if (r0 == 0) goto L_0x00fb
            r0.disconnect()
        L_0x00fb:
            java.lang.String r0 = r7.f189d
            goto L_0x0017
        L_0x00ff:
            if (r1 == 0) goto L_0x0107
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00e2, all -> 0x017d }
            r7.f189d = r1     // Catch:{ Exception -> 0x00e2, all -> 0x017d }
        L_0x0107:
            r3 = r4
            goto L_0x00e7
        L_0x0109:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00ec
        L_0x010e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00f1
        L_0x0113:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00f6
        L_0x0118:
            r0 = move-exception
            r1 = r3
            r2 = r3
            r4 = r3
        L_0x011c:
            r0.printStackTrace()     // Catch:{ all -> 0x018a }
            if (r1 == 0) goto L_0x0124
            r1.close()     // Catch:{ Exception -> 0x0134 }
        L_0x0124:
            if (r3 == 0) goto L_0x0129
            r3.close()     // Catch:{ Exception -> 0x0139 }
        L_0x0129:
            if (r2 == 0) goto L_0x012e
            r2.close()     // Catch:{ Exception -> 0x013e }
        L_0x012e:
            if (r4 == 0) goto L_0x00fb
            r4.disconnect()
            goto L_0x00fb
        L_0x0134:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0124
        L_0x0139:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0129
        L_0x013e:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x012e
        L_0x0143:
            r0 = move-exception
            r5 = r3
            r4 = r3
            r1 = r3
        L_0x0147:
            if (r5 == 0) goto L_0x014c
            r5.close()     // Catch:{ Exception -> 0x015c }
        L_0x014c:
            if (r3 == 0) goto L_0x0151
            r3.close()     // Catch:{ Exception -> 0x0161 }
        L_0x0151:
            if (r4 == 0) goto L_0x0156
            r4.close()     // Catch:{ Exception -> 0x0166 }
        L_0x0156:
            if (r1 == 0) goto L_0x015b
            r1.disconnect()
        L_0x015b:
            throw r0
        L_0x015c:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x014c
        L_0x0161:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0151
        L_0x0166:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0156
        L_0x016b:
            r1 = move-exception
            r5 = r3
            r4 = r3
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x0147
        L_0x0172:
            r1 = move-exception
            r4 = r3
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x0147
        L_0x0178:
            r1 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x0147
        L_0x017d:
            r1 = move-exception
            r3 = r2
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x0147
        L_0x0183:
            r1 = move-exception
            r4 = r3
            r3 = r2
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x0147
        L_0x018a:
            r0 = move-exception
            r5 = r1
            r1 = r4
            r4 = r2
            goto L_0x0147
        L_0x018f:
            r1 = move-exception
            r2 = r3
            r4 = r0
            r0 = r1
            r1 = r3
            goto L_0x011c
        L_0x0195:
            r1 = move-exception
            r2 = r3
            r4 = r0
            r0 = r1
            r1 = r5
            goto L_0x011c
        L_0x019b:
            r1 = move-exception
            r4 = r0
            r0 = r1
            r1 = r5
            r6 = r3
            r3 = r2
            r2 = r6
            goto L_0x011c
        L_0x01a4:
            r1 = move-exception
            r2 = r3
            goto L_0x00e4
        L_0x01a8:
            r1 = move-exception
            r2 = r3
            r3 = r4
            goto L_0x00e4
        L_0x01ad:
            r2 = r3
            goto L_0x00e7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0046an.m194b(java.lang.String):java.lang.String");
    }

    /* renamed from: a */
    public static String m193a(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke((Object) null, new Object[]{str, str2});
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }
}
