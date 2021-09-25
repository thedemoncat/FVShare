package com.umeng.analytics.pro;

import java.io.Serializable;

/* renamed from: com.umeng.analytics.pro.cr */
/* compiled from: FieldValueMetaData */
public class C0184cr implements Serializable {

    /* renamed from: a */
    private final boolean f594a;

    /* renamed from: b */
    public final byte f595b;

    /* renamed from: c */
    private final String f596c;

    /* renamed from: d */
    private final boolean f597d;

    public C0184cr(byte b, boolean z) {
        this.f595b = b;
        this.f594a = false;
        this.f596c = null;
        this.f597d = z;
    }

    public C0184cr(byte b) {
        this(b, false);
    }

    public C0184cr(byte b, String str) {
        this.f595b = b;
        this.f594a = true;
        this.f596c = str;
        this.f597d = false;
    }

    /* renamed from: a */
    public boolean mo570a() {
        return this.f594a;
    }

    /* renamed from: b */
    public String mo571b() {
        return this.f596c;
    }

    /* renamed from: c */
    public boolean mo572c() {
        return this.f595b == 12;
    }

    /* renamed from: d */
    public boolean mo573d() {
        return this.f595b == 15 || this.f595b == 13 || this.f595b == 14;
    }

    /* renamed from: e */
    public boolean mo574e() {
        return this.f597d;
    }
}
