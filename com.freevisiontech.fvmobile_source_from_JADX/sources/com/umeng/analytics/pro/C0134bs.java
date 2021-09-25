package com.umeng.analytics.pro;

import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* renamed from: com.umeng.analytics.pro.bs */
/* compiled from: DeflaterHelper */
public class C0134bs {

    /* renamed from: a */
    public static int f500a;

    /* renamed from: a */
    public static byte[] m763a(String str, String str2) throws IOException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return m764a(str.getBytes(str2));
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0035  */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] m764a(byte[] r6) throws java.io.IOException {
        /*
            r0 = 0
            r4 = 0
            if (r6 == 0) goto L_0x0007
            int r1 = r6.length
            if (r1 > 0) goto L_0x0008
        L_0x0007:
            return r0
        L_0x0008:
            java.util.zip.Deflater r2 = new java.util.zip.Deflater
            r2.<init>()
            r2.setInput(r6)
            r2.finish()
            r1 = 8192(0x2000, float:1.14794E-41)
            byte[] r3 = new byte[r1]
            f500a = r4
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x0046 }
            r1.<init>()     // Catch:{ all -> 0x0046 }
        L_0x001e:
            boolean r0 = r2.finished()     // Catch:{ all -> 0x0032 }
            if (r0 != 0) goto L_0x0039
            int r0 = r2.deflate(r3)     // Catch:{ all -> 0x0032 }
            int r4 = f500a     // Catch:{ all -> 0x0032 }
            int r4 = r4 + r0
            f500a = r4     // Catch:{ all -> 0x0032 }
            r4 = 0
            r1.write(r3, r4, r0)     // Catch:{ all -> 0x0032 }
            goto L_0x001e
        L_0x0032:
            r0 = move-exception
        L_0x0033:
            if (r1 == 0) goto L_0x0038
            r1.close()
        L_0x0038:
            throw r0
        L_0x0039:
            r2.end()     // Catch:{ all -> 0x0032 }
            if (r1 == 0) goto L_0x0041
            r1.close()
        L_0x0041:
            byte[] r0 = r1.toByteArray()
            goto L_0x0007
        L_0x0046:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x0033
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0134bs.m764a(byte[]):byte[]");
    }

    /* renamed from: a */
    public static String m762a(byte[] bArr, String str) throws UnsupportedEncodingException, DataFormatException {
        byte[] b = m765b(bArr);
        if (b != null) {
            return new String(b, str);
        }
        return null;
    }

    /* renamed from: b */
    public static byte[] m765b(byte[] bArr) throws UnsupportedEncodingException, DataFormatException {
        int i = 0;
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        Inflater inflater = new Inflater();
        inflater.setInput(bArr, 0, bArr.length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr2 = new byte[1024];
        while (!inflater.needsInput()) {
            int inflate = inflater.inflate(bArr2);
            byteArrayOutputStream.write(bArr2, i, inflate);
            i += inflate;
        }
        inflater.end();
        return byteArrayOutputStream.toByteArray();
    }
}
