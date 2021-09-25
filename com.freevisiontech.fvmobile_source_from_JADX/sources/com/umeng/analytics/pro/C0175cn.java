package com.umeng.analytics.pro;

import com.umeng.analytics.pro.C0191cx;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/* renamed from: com.umeng.analytics.pro.cn */
/* compiled from: TSerializer */
public class C0175cn {

    /* renamed from: a */
    private final ByteArrayOutputStream f583a;

    /* renamed from: b */
    private final C0222dp f584b;

    /* renamed from: c */
    private C0209dd f585c;

    public C0175cn() {
        this(new C0191cx.C0192a());
    }

    public C0175cn(C0211df dfVar) {
        this.f583a = new ByteArrayOutputStream();
        this.f584b = new C0222dp((OutputStream) this.f583a);
        this.f585c = dfVar.mo619a(this.f584b);
    }

    /* renamed from: a */
    public byte[] mo544a(C0164ce ceVar) throws C0172ck {
        this.f583a.reset();
        ceVar.mo258b(this.f585c);
        return this.f583a.toByteArray();
    }

    /* renamed from: a */
    public String mo543a(C0164ce ceVar, String str) throws C0172ck {
        try {
            return new String(mo544a(ceVar), str);
        } catch (UnsupportedEncodingException e) {
            throw new C0172ck("JVM DOES NOT SUPPORT ENCODING: " + str);
        }
    }

    /* renamed from: b */
    public String mo545b(C0164ce ceVar) throws C0172ck {
        return new String(mo544a(ceVar));
    }
}
