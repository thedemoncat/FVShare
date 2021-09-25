package com.umeng.analytics.social;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* renamed from: com.umeng.analytics.social.b */
/* compiled from: UMNetwork */
public abstract class C0298b {
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v13, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v15, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v20, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x011b  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x014f  */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static java.lang.String m1433a(java.lang.String r8) {
        /*
            r1 = 0
            java.util.Random r0 = new java.util.Random
            r0.<init>()
            r2 = 1000(0x3e8, float:1.401E-42)
            int r4 = r0.nextInt(r2)
            java.lang.String r0 = "line.separator"
            java.lang.String r5 = java.lang.System.getProperty(r0)     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            int r0 = r8.length()     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            r2 = 1
            if (r0 > r2) goto L_0x0038
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            r0.<init>()     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            java.lang.String r2 = ":  Invalid baseUrl."
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            com.umeng.analytics.pro.C0138bw.m849e((java.lang.String) r0)     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            if (r1 == 0) goto L_0x0036
            r1.disconnect()
        L_0x0036:
            r0 = r1
        L_0x0037:
            return r0
        L_0x0038:
            java.net.URL r0 = new java.net.URL     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            r0.<init>(r8)     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x0158, all -> 0x014c }
            r2 = 10000(0x2710, float:1.4013E-41)
            r0.setConnectTimeout(r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r2 = 20000(0x4e20, float:2.8026E-41)
            r0.setReadTimeout(r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r2 = "GET"
            r0.setRequestMethod(r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r2 = android.os.Build.VERSION.SDK     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r3 = 8
            if (r2 >= r3) goto L_0x0066
            java.lang.String r2 = "http.keepAlive"
            java.lang.String r3 = "false"
            java.lang.System.setProperty(r2, r3)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
        L_0x0066:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r2.<init>()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r3 = ": GET_URL: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r2 = r2.append(r8)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            com.umeng.analytics.pro.C0138bw.m837c((java.lang.String) r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            int r2 = r0.getResponseCode()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r3 = 200(0xc8, float:2.8E-43)
            if (r2 != r3) goto L_0x0129
            java.io.InputStream r3 = r0.getInputStream()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r2 = "Content-Encoding"
            java.lang.String r2 = r0.getHeaderField(r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            boolean r6 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            if (r6 != 0) goto L_0x00ec
            java.lang.String r6 = "gzip"
            boolean r6 = r2.equalsIgnoreCase(r6)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            if (r6 == 0) goto L_0x00ec
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r2.<init>()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r6 = "  Use GZIPInputStream get data...."
            java.lang.StringBuilder r2 = r2.append(r6)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            com.umeng.analytics.pro.C0138bw.m837c((java.lang.String) r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.util.zip.GZIPInputStream r2 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
        L_0x00bf:
            java.lang.String r2 = m1432a((java.io.InputStream) r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r3.<init>()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r4 = ":  response: "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r3 = r3.append(r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            com.umeng.analytics.pro.C0138bw.m837c((java.lang.String) r3)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            if (r2 != 0) goto L_0x0121
            if (r0 == 0) goto L_0x00e9
            r0.disconnect()
        L_0x00e9:
            r0 = r1
            goto L_0x0037
        L_0x00ec:
            boolean r6 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            if (r6 != 0) goto L_0x015b
            java.lang.String r6 = "deflate"
            boolean r2 = r2.equalsIgnoreCase(r6)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            if (r2 == 0) goto L_0x015b
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r2.<init>()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r6 = "  Use InflaterInputStream get data...."
            java.lang.StringBuilder r2 = r2.append(r6)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            com.umeng.analytics.pro.C0138bw.m837c((java.lang.String) r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.util.zip.InflaterInputStream r2 = new java.util.zip.InflaterInputStream     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            goto L_0x00bf
        L_0x0118:
            r2 = move-exception
        L_0x0119:
            if (r0 == 0) goto L_0x011e
            r0.disconnect()
        L_0x011e:
            r0 = r1
            goto L_0x0037
        L_0x0121:
            if (r0 == 0) goto L_0x0126
            r0.disconnect()
        L_0x0126:
            r0 = r2
            goto L_0x0037
        L_0x0129:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            r2.<init>()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r3 = ":  Failed to get message."
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.StringBuilder r2 = r2.append(r8)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            com.umeng.analytics.pro.C0138bw.m837c((java.lang.String) r2)     // Catch:{ Exception -> 0x0118, all -> 0x0153 }
            if (r0 == 0) goto L_0x0149
            r0.disconnect()
        L_0x0149:
            r0 = r1
            goto L_0x0037
        L_0x014c:
            r0 = move-exception
        L_0x014d:
            if (r1 == 0) goto L_0x0152
            r1.disconnect()
        L_0x0152:
            throw r0
        L_0x0153:
            r1 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
            goto L_0x014d
        L_0x0158:
            r0 = move-exception
            r0 = r1
            goto L_0x0119
        L_0x015b:
            r2 = r3
            goto L_0x00bf
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.social.C0298b.m1433a(java.lang.String):java.lang.String");
    }

    /* renamed from: a */
    private static String m1432a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8192);
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine + "\n");
                } else {
                    try {
                        inputStream.close();
                        return sb.toString();
                    } catch (IOException e) {
                        return null;
                    }
                }
            } catch (IOException e2) {
                try {
                    inputStream.close();
                    return null;
                } catch (IOException e3) {
                    return null;
                }
            } catch (Throwable th) {
                try {
                    inputStream.close();
                    throw th;
                } catch (IOException e4) {
                    return null;
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0146  */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static java.lang.String m1434a(java.lang.String r8, java.lang.String r9) {
        /*
            r1 = 0
            java.util.Random r0 = new java.util.Random
            r0.<init>()
            r2 = 1000(0x3e8, float:1.401E-42)
            int r4 = r0.nextInt(r2)
            java.lang.String r0 = "line.separator"
            java.lang.String r5 = java.lang.System.getProperty(r0)     // Catch:{ Exception -> 0x013a, all -> 0x0143 }
            java.net.URL r0 = new java.net.URL     // Catch:{ Exception -> 0x013a, all -> 0x0143 }
            r0.<init>(r8)     // Catch:{ Exception -> 0x013a, all -> 0x0143 }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Exception -> 0x013a, all -> 0x0143 }
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x013a, all -> 0x0143 }
            r2 = 10000(0x2710, float:1.4013E-41)
            r0.setConnectTimeout(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2 = 20000(0x4e20, float:2.8026E-41)
            r0.setReadTimeout(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2 = 1
            r0.setDoOutput(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2 = 1
            r0.setDoInput(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2 = 0
            r0.setUseCaches(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r2 = "POST"
            r0.setRequestMethod(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2.<init>()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r3 = ": POST_URL: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r2 = r2.append(r8)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            com.umeng.analytics.pro.C0138bw.m837c((java.lang.String) r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r2 = android.os.Build.VERSION.SDK     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r3 = 8
            if (r2 >= r3) goto L_0x0068
            java.lang.String r2 = "http.keepAlive"
            java.lang.String r3 = "false"
            java.lang.System.setProperty(r2, r3)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
        L_0x0068:
            boolean r2 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            if (r2 != 0) goto L_0x00be
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2.<init>()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r3 = ": POST_BODY: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r2 = r2.append(r9)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            com.umeng.analytics.pro.C0138bw.m837c((java.lang.String) r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2.<init>()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r3.<init>()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r6 = "data="
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r3 = r3.append(r9)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2.add(r3)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.io.OutputStream r3 = r0.getOutputStream()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r2 = java.net.URLEncoder.encode(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            byte[] r2 = r2.getBytes()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r3.write(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r3.flush()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r3.close()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
        L_0x00be:
            int r2 = r0.getResponseCode()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r3 = 200(0xc8, float:2.8E-43)
            if (r2 != r3) goto L_0x0118
            java.io.InputStream r3 = r0.getInputStream()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r2 = "Content-Encoding"
            java.lang.String r2 = r0.getHeaderField(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            boolean r6 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            if (r6 != 0) goto L_0x0151
            java.lang.String r6 = "gzip"
            boolean r2 = r2.equalsIgnoreCase(r6)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            if (r2 == 0) goto L_0x0151
            java.util.zip.InflaterInputStream r2 = new java.util.zip.InflaterInputStream     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2.<init>(r3)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
        L_0x00e5:
            java.lang.String r2 = m1432a((java.io.InputStream) r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r3.<init>()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r4 = ":  response: "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r3 = r3.append(r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            com.umeng.analytics.pro.C0138bw.m837c((java.lang.String) r3)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            if (r2 != 0) goto L_0x0111
            if (r0 == 0) goto L_0x010f
            r0.disconnect()
        L_0x010f:
            r0 = r1
        L_0x0110:
            return r0
        L_0x0111:
            if (r0 == 0) goto L_0x0116
            r0.disconnect()
        L_0x0116:
            r0 = r2
            goto L_0x0110
        L_0x0118:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            r2.<init>()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r3 = ":  Failed to send message."
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.StringBuilder r2 = r2.append(r8)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            com.umeng.analytics.pro.C0138bw.m849e((java.lang.String) r2)     // Catch:{ Exception -> 0x014f, all -> 0x014a }
            if (r0 == 0) goto L_0x0138
            r0.disconnect()
        L_0x0138:
            r0 = r1
            goto L_0x0110
        L_0x013a:
            r0 = move-exception
            r0 = r1
        L_0x013c:
            if (r0 == 0) goto L_0x0141
            r0.disconnect()
        L_0x0141:
            r0 = r1
            goto L_0x0110
        L_0x0143:
            r0 = move-exception
        L_0x0144:
            if (r1 == 0) goto L_0x0149
            r1.disconnect()
        L_0x0149:
            throw r0
        L_0x014a:
            r1 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
            goto L_0x0144
        L_0x014f:
            r2 = move-exception
            goto L_0x013c
        L_0x0151:
            r2 = r3
            goto L_0x00e5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.social.C0298b.m1434a(java.lang.String, java.lang.String):java.lang.String");
    }
}
