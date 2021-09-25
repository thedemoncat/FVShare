package com.umeng.analytics.pro;

/* renamed from: com.umeng.analytics.pro.cc */
/* compiled from: ShortStack */
public class C0162cc {

    /* renamed from: a */
    private short[] f561a;

    /* renamed from: b */
    private int f562b = -1;

    public C0162cc(int i) {
        this.f561a = new short[i];
    }

    /* renamed from: a */
    public short mo520a() {
        short[] sArr = this.f561a;
        int i = this.f562b;
        this.f562b = i - 1;
        return sArr[i];
    }

    /* renamed from: a */
    public void mo521a(short s) {
        if (this.f561a.length == this.f562b + 1) {
            m932d();
        }
        short[] sArr = this.f561a;
        int i = this.f562b + 1;
        this.f562b = i;
        sArr[i] = s;
    }

    /* renamed from: d */
    private void m932d() {
        short[] sArr = new short[(this.f561a.length * 2)];
        System.arraycopy(this.f561a, 0, sArr, 0, this.f561a.length);
        this.f561a = sArr;
    }

    /* renamed from: b */
    public short mo522b() {
        return this.f561a[this.f562b];
    }

    /* renamed from: c */
    public void mo523c() {
        this.f562b = -1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<ShortStack vector:[");
        for (int i = 0; i < this.f561a.length; i++) {
            if (i != 0) {
                sb.append(" ");
            }
            if (i == this.f562b) {
                sb.append(">>");
            }
            sb.append(this.f561a[i]);
            if (i == this.f562b) {
                sb.append("<<");
            }
        }
        sb.append("]>");
        return sb.toString();
    }
}
