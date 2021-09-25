package com.umeng.analytics.pro;

import java.util.BitSet;

/* renamed from: com.umeng.analytics.pro.dj */
/* compiled from: TTupleProtocol */
public final class C0215dj extends C0191cx {

    /* renamed from: com.umeng.analytics.pro.dj$a */
    /* compiled from: TTupleProtocol */
    public static class C0216a implements C0211df {
        /* renamed from: a */
        public C0209dd mo619a(C0224dr drVar) {
            return new C0215dj(drVar);
        }
    }

    public C0215dj(C0224dr drVar) {
        super(drVar);
    }

    /* renamed from: D */
    public Class<? extends C0218dl> mo628D() {
        return C0221do.class;
    }

    /* renamed from: a */
    public void mo630a(BitSet bitSet, int i) throws C0172ck {
        for (byte a : m1200b(bitSet, i)) {
            mo577a(a);
        }
    }

    /* renamed from: b */
    public BitSet mo631b(int i) throws C0172ck {
        int ceil = (int) Math.ceil(((double) i) / 8.0d);
        byte[] bArr = new byte[ceil];
        for (int i2 = 0; i2 < ceil; i2++) {
            bArr[i2] = mo613u();
        }
        return m1199a(bArr);
    }

    /* renamed from: a */
    public static BitSet m1199a(byte[] bArr) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < bArr.length * 8; i++) {
            if ((bArr[(bArr.length - (i / 8)) - 1] & (1 << (i % 8))) > 0) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    /* renamed from: b */
    public static byte[] m1200b(BitSet bitSet, int i) {
        byte[] bArr = new byte[((int) Math.ceil(((double) i) / 8.0d))];
        for (int i2 = 0; i2 < bitSet.length(); i2++) {
            if (bitSet.get(i2)) {
                int length = (bArr.length - (i2 / 8)) - 1;
                bArr[length] = (byte) (bArr[length] | (1 << (i2 % 8)));
            }
        }
        return bArr;
    }
}
