package com.umeng.analytics.pro;

/* renamed from: com.umeng.analytics.pro.cy */
/* compiled from: TField */
public class C0194cy {

    /* renamed from: a */
    public final String f651a;

    /* renamed from: b */
    public final byte f652b;

    /* renamed from: c */
    public final short f653c;

    public C0194cy() {
        this("", (byte) 0, 0);
    }

    public C0194cy(String str, byte b, short s) {
        this.f651a = str;
        this.f652b = b;
        this.f653c = s;
    }

    public String toString() {
        return "<TField name:'" + this.f651a + "' type:" + this.f652b + " field-id:" + this.f653c + ">";
    }

    /* renamed from: a */
    public boolean mo622a(C0194cy cyVar) {
        return this.f652b == cyVar.f652b && this.f653c == cyVar.f653c;
    }
}
