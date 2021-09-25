package com.umeng.analytics.pro;

/* renamed from: com.umeng.analytics.pro.db */
/* compiled from: TMessage */
public final class C0207db {

    /* renamed from: a */
    public final String f691a;

    /* renamed from: b */
    public final byte f692b;

    /* renamed from: c */
    public final int f693c;

    public C0207db() {
        this("", (byte) 0, 0);
    }

    public C0207db(String str, byte b, int i) {
        this.f691a = str;
        this.f692b = b;
        this.f693c = i;
    }

    public String toString() {
        return "<TMessage name:'" + this.f691a + "' type: " + this.f692b + " seqid:" + this.f693c + ">";
    }

    public boolean equals(Object obj) {
        if (obj instanceof C0207db) {
            return mo624a((C0207db) obj);
        }
        return false;
    }

    /* renamed from: a */
    public boolean mo624a(C0207db dbVar) {
        return this.f691a.equals(dbVar.f691a) && this.f692b == dbVar.f692b && this.f693c == dbVar.f693c;
    }
}
