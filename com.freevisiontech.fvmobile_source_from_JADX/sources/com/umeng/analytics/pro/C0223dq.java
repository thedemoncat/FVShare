package com.umeng.analytics.pro;

/* renamed from: com.umeng.analytics.pro.dq */
/* compiled from: TMemoryInputTransport */
public final class C0223dq extends C0224dr {

    /* renamed from: a */
    private byte[] f727a;

    /* renamed from: b */
    private int f728b;

    /* renamed from: c */
    private int f729c;

    public C0223dq() {
    }

    public C0223dq(byte[] bArr) {
        mo639a(bArr);
    }

    public C0223dq(byte[] bArr, int i, int i2) {
        mo640c(bArr, i, i2);
    }

    /* renamed from: a */
    public void mo639a(byte[] bArr) {
        mo640c(bArr, 0, bArr.length);
    }

    /* renamed from: c */
    public void mo640c(byte[] bArr, int i, int i2) {
        this.f727a = bArr;
        this.f728b = i;
        this.f729c = i + i2;
    }

    /* renamed from: e */
    public void mo641e() {
        this.f727a = null;
    }

    /* renamed from: c */
    public void mo636c() {
    }

    /* renamed from: a */
    public boolean mo633a() {
        return true;
    }

    /* renamed from: b */
    public void mo634b() throws C0225ds {
    }

    /* renamed from: a */
    public int mo632a(byte[] bArr, int i, int i2) throws C0225ds {
        int h = mo644h();
        if (i2 > h) {
            i2 = h;
        }
        if (i2 > 0) {
            System.arraycopy(this.f727a, this.f728b, bArr, i, i2);
            mo638a(i2);
        }
        return i2;
    }

    /* renamed from: b */
    public void mo635b(byte[] bArr, int i, int i2) throws C0225ds {
        throw new UnsupportedOperationException("No writing allowed!");
    }

    /* renamed from: f */
    public byte[] mo642f() {
        return this.f727a;
    }

    /* renamed from: g */
    public int mo643g() {
        return this.f728b;
    }

    /* renamed from: h */
    public int mo644h() {
        return this.f729c - this.f728b;
    }

    /* renamed from: a */
    public void mo638a(int i) {
        this.f728b += i;
    }
}
