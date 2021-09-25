package com.umeng.analytics.pro;

import com.freevisiontech.cameralib.impl.Camera2.Camera2Constants;
import com.umeng.analytics.pro.C0191cx;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* renamed from: com.umeng.analytics.pro.ch */
/* compiled from: TDeserializer */
public class C0169ch {

    /* renamed from: a */
    private final C0209dd f577a;

    /* renamed from: b */
    private final C0223dq f578b;

    public C0169ch() {
        this(new C0191cx.C0192a());
    }

    public C0169ch(C0211df dfVar) {
        this.f578b = new C0223dq();
        this.f577a = dfVar.mo619a(this.f578b);
    }

    /* renamed from: a */
    public void mo533a(C0164ce ceVar, byte[] bArr) throws C0172ck {
        try {
            this.f578b.mo639a(bArr);
            ceVar.mo253a(this.f577a);
        } finally {
            this.f578b.mo641e();
            this.f577a.mo620B();
        }
    }

    /* renamed from: a */
    public void mo532a(C0164ce ceVar, String str, String str2) throws C0172ck {
        try {
            mo533a(ceVar, str.getBytes(str2));
            this.f577a.mo620B();
        } catch (UnsupportedEncodingException e) {
            throw new C0172ck("JVM DOES NOT SUPPORT ENCODING: " + str2);
        } catch (Throwable th) {
            this.f577a.mo620B();
            throw th;
        }
    }

    /* renamed from: a */
    public void mo534a(C0164ce ceVar, byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        try {
            if (m969j(bArr, clVar, clVarArr) != null) {
                ceVar.mo253a(this.f577a);
            }
            this.f578b.mo641e();
            this.f577a.mo620B();
        } catch (Exception e) {
            throw new C0172ck((Throwable) e);
        } catch (Throwable th) {
            this.f578b.mo641e();
            this.f577a.mo620B();
            throw th;
        }
    }

    /* renamed from: a */
    public Boolean mo530a(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        return (Boolean) m968a((byte) 2, bArr, clVar, clVarArr);
    }

    /* renamed from: b */
    public Byte mo535b(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        return (Byte) m968a((byte) 3, bArr, clVar, clVarArr);
    }

    /* renamed from: c */
    public Double mo536c(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        return (Double) m968a((byte) 4, bArr, clVar, clVarArr);
    }

    /* renamed from: d */
    public Short mo537d(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        return (Short) m968a((byte) 6, bArr, clVar, clVarArr);
    }

    /* renamed from: e */
    public Integer mo538e(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        return (Integer) m968a((byte) 8, bArr, clVar, clVarArr);
    }

    /* renamed from: f */
    public Long mo539f(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        return (Long) m968a((byte) 10, bArr, clVar, clVarArr);
    }

    /* renamed from: g */
    public String mo540g(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        return (String) m968a((byte) 11, bArr, clVar, clVarArr);
    }

    /* renamed from: h */
    public ByteBuffer mo541h(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        return (ByteBuffer) m968a((byte) Camera2Constants.JPEG_QUALITY, bArr, clVar, clVarArr);
    }

    /* renamed from: i */
    public Short mo542i(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        try {
            if (m969j(bArr, clVar, clVarArr) != null) {
                this.f577a.mo602j();
                Short valueOf = Short.valueOf(this.f577a.mo604l().f653c);
                this.f578b.mo641e();
                this.f577a.mo620B();
                return valueOf;
            }
            this.f578b.mo641e();
            this.f577a.mo620B();
            return null;
        } catch (Exception e) {
            throw new C0172ck((Throwable) e);
        } catch (Throwable th) {
            this.f578b.mo641e();
            this.f577a.mo620B();
            throw th;
        }
    }

    /* renamed from: a */
    private Object m968a(byte b, byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        try {
            C0194cy j = m969j(bArr, clVar, clVarArr);
            if (j != null) {
                switch (b) {
                    case 2:
                        if (j.f652b == 2) {
                            Boolean valueOf = Boolean.valueOf(this.f577a.mo612t());
                            this.f578b.mo641e();
                            this.f577a.mo620B();
                            return valueOf;
                        }
                        break;
                    case 3:
                        if (j.f652b == 3) {
                            Byte valueOf2 = Byte.valueOf(this.f577a.mo613u());
                            this.f578b.mo641e();
                            this.f577a.mo620B();
                            return valueOf2;
                        }
                        break;
                    case 4:
                        if (j.f652b == 4) {
                            Double valueOf3 = Double.valueOf(this.f577a.mo617y());
                            this.f578b.mo641e();
                            this.f577a.mo620B();
                            return valueOf3;
                        }
                        break;
                    case 6:
                        if (j.f652b == 6) {
                            Short valueOf4 = Short.valueOf(this.f577a.mo614v());
                            this.f578b.mo641e();
                            this.f577a.mo620B();
                            return valueOf4;
                        }
                        break;
                    case 8:
                        if (j.f652b == 8) {
                            Integer valueOf5 = Integer.valueOf(this.f577a.mo615w());
                            this.f578b.mo641e();
                            this.f577a.mo620B();
                            return valueOf5;
                        }
                        break;
                    case 10:
                        if (j.f652b == 10) {
                            Long valueOf6 = Long.valueOf(this.f577a.mo616x());
                            this.f578b.mo641e();
                            this.f577a.mo620B();
                            return valueOf6;
                        }
                        break;
                    case 11:
                        if (j.f652b == 11) {
                            String z = this.f577a.mo618z();
                            this.f578b.mo641e();
                            this.f577a.mo620B();
                            return z;
                        }
                        break;
                    case 100:
                        if (j.f652b == 11) {
                            ByteBuffer A = this.f577a.mo575A();
                            this.f578b.mo641e();
                            this.f577a.mo620B();
                            return A;
                        }
                        break;
                }
            }
            this.f578b.mo641e();
            this.f577a.mo620B();
            return null;
        } catch (Exception e) {
            throw new C0172ck((Throwable) e);
        } catch (Throwable th) {
            this.f578b.mo641e();
            this.f577a.mo620B();
            throw th;
        }
    }

    /* renamed from: j */
    private C0194cy m969j(byte[] bArr, C0173cl clVar, C0173cl... clVarArr) throws C0172ck {
        int i = 0;
        this.f578b.mo639a(bArr);
        C0173cl[] clVarArr2 = new C0173cl[(clVarArr.length + 1)];
        clVarArr2[0] = clVar;
        for (int i2 = 0; i2 < clVarArr.length; i2++) {
            clVarArr2[i2 + 1] = clVarArr[i2];
        }
        this.f577a.mo602j();
        C0194cy cyVar = null;
        while (i < clVarArr2.length) {
            cyVar = this.f577a.mo604l();
            if (cyVar.f652b == 0 || cyVar.f653c > clVarArr2[i].mo287a()) {
                return null;
            }
            if (cyVar.f653c != clVarArr2[i].mo287a()) {
                C0212dg.m1197a(this.f577a, cyVar.f652b);
                this.f577a.mo605m();
            } else {
                i++;
                if (i < clVarArr2.length) {
                    this.f577a.mo602j();
                }
            }
        }
        return cyVar;
    }

    /* renamed from: a */
    public void mo531a(C0164ce ceVar, String str) throws C0172ck {
        mo533a(ceVar, str.getBytes());
    }
}
