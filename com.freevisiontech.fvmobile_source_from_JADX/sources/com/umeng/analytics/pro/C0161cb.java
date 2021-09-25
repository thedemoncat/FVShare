package com.umeng.analytics.pro;

/* renamed from: com.umeng.analytics.pro.cb */
/* compiled from: EncodingUtils */
public class C0161cb {
    /* renamed from: a */
    public static final void m922a(int i, byte[] bArr) {
        m923a(i, bArr, 0);
    }

    /* renamed from: a */
    public static final void m923a(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) ((i >> 24) & 255);
        bArr[i2 + 1] = (byte) ((i >> 16) & 255);
        bArr[i2 + 2] = (byte) ((i >> 8) & 255);
        bArr[i2 + 3] = (byte) (i & 255);
    }

    /* renamed from: a */
    public static final int m918a(byte[] bArr) {
        return m919a(bArr, 0);
    }

    /* renamed from: a */
    public static final int m919a(byte[] bArr, int i) {
        return ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << C0217dk.f724n) | ((bArr[i + 2] & 255) << 8) | (bArr[i + 3] & 255);
    }

    /* renamed from: a */
    public static final boolean m924a(byte b, int i) {
        return m925a((int) b, i);
    }

    /* renamed from: a */
    public static final boolean m927a(short s, int i) {
        return m925a((int) s, i);
    }

    /* renamed from: a */
    public static final boolean m925a(int i, int i2) {
        return ((1 << i2) & i) != 0;
    }

    /* renamed from: a */
    public static final boolean m926a(long j, int i) {
        return ((1 << i) & j) != 0;
    }

    /* renamed from: b */
    public static final byte m928b(byte b, int i) {
        return (byte) m929b((int) b, i);
    }

    /* renamed from: b */
    public static final short m931b(short s, int i) {
        return (short) m929b((int) s, i);
    }

    /* renamed from: b */
    public static final int m929b(int i, int i2) {
        return ((1 << i2) ^ -1) & i;
    }

    /* renamed from: b */
    public static final long m930b(long j, int i) {
        return ((1 << i) ^ -1) & j;
    }

    /* renamed from: a */
    public static final byte m916a(byte b, int i, boolean z) {
        return (byte) m917a((int) b, i, z);
    }

    /* renamed from: a */
    public static final short m921a(short s, int i, boolean z) {
        return (short) m917a((int) s, i, z);
    }

    /* renamed from: a */
    public static final int m917a(int i, int i2, boolean z) {
        if (z) {
            return (1 << i2) | i;
        }
        return m929b(i, i2);
    }

    /* renamed from: a */
    public static final long m920a(long j, int i, boolean z) {
        if (z) {
            return (1 << i) | j;
        }
        return m930b(j, i);
    }
}
