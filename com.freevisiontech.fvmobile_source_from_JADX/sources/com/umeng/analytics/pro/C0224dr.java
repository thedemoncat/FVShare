package com.umeng.analytics.pro;

/* renamed from: com.umeng.analytics.pro.dr */
/* compiled from: TTransport */
public abstract class C0224dr {
    /* renamed from: a */
    public abstract int mo632a(byte[] bArr, int i, int i2) throws C0225ds;

    /* renamed from: a */
    public abstract boolean mo633a();

    /* renamed from: b */
    public abstract void mo634b() throws C0225ds;

    /* renamed from: b */
    public abstract void mo635b(byte[] bArr, int i, int i2) throws C0225ds;

    /* renamed from: c */
    public abstract void mo636c();

    /* renamed from: i */
    public boolean mo647i() {
        return mo633a();
    }

    /* renamed from: d */
    public int mo646d(byte[] bArr, int i, int i2) throws C0225ds {
        int i3 = 0;
        while (i3 < i2) {
            int a = mo632a(bArr, i + i3, i2 - i3);
            if (a <= 0) {
                throw new C0225ds("Cannot read. Remote side has closed. Tried to read " + i2 + " bytes, but only got " + i3 + " bytes. (This is often indicative of an internal error on the server side. Please check your server logs.)");
            }
            i3 += a;
        }
        return i3;
    }

    /* renamed from: b */
    public void mo645b(byte[] bArr) throws C0225ds {
        mo635b(bArr, 0, bArr.length);
    }

    /* renamed from: d */
    public void mo637d() throws C0225ds {
    }

    /* renamed from: f */
    public byte[] mo642f() {
        return null;
    }

    /* renamed from: g */
    public int mo643g() {
        return 0;
    }

    /* renamed from: h */
    public int mo644h() {
        return -1;
    }

    /* renamed from: a */
    public void mo638a(int i) {
    }
}
