package com.umeng.analytics.pro;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* renamed from: com.umeng.analytics.pro.cx */
/* compiled from: TCompactProtocol */
public class C0191cx extends C0209dd {

    /* renamed from: d */
    private static final C0214di f621d = new C0214di("");

    /* renamed from: e */
    private static final C0194cy f622e = new C0194cy("", (byte) 0, 0);

    /* renamed from: f */
    private static final byte[] f623f = new byte[16];

    /* renamed from: h */
    private static final byte f624h = -126;

    /* renamed from: i */
    private static final byte f625i = 1;

    /* renamed from: j */
    private static final byte f626j = 31;

    /* renamed from: k */
    private static final byte f627k = -32;

    /* renamed from: l */
    private static final int f628l = 5;

    /* renamed from: a */
    byte[] f629a;

    /* renamed from: b */
    byte[] f630b;

    /* renamed from: c */
    byte[] f631c;

    /* renamed from: m */
    private C0162cc f632m;

    /* renamed from: n */
    private short f633n;

    /* renamed from: o */
    private C0194cy f634o;

    /* renamed from: p */
    private Boolean f635p;

    /* renamed from: q */
    private final long f636q;

    /* renamed from: r */
    private byte[] f637r;

    static {
        f623f[0] = 0;
        f623f[2] = 1;
        f623f[3] = 3;
        f623f[6] = 4;
        f623f[8] = 5;
        f623f[10] = 6;
        f623f[4] = 7;
        f623f[11] = 8;
        f623f[15] = 9;
        f623f[14] = 10;
        f623f[13] = 11;
        f623f[12] = 12;
    }

    /* renamed from: com.umeng.analytics.pro.cx$a */
    /* compiled from: TCompactProtocol */
    public static class C0192a implements C0211df {

        /* renamed from: a */
        private final long f638a;

        public C0192a() {
            this.f638a = -1;
        }

        public C0192a(int i) {
            this.f638a = (long) i;
        }

        /* renamed from: a */
        public C0209dd mo619a(C0224dr drVar) {
            return new C0191cx(drVar, this.f638a);
        }
    }

    /* renamed from: com.umeng.analytics.pro.cx$b */
    /* compiled from: TCompactProtocol */
    private static class C0193b {

        /* renamed from: a */
        public static final byte f639a = 1;

        /* renamed from: b */
        public static final byte f640b = 2;

        /* renamed from: c */
        public static final byte f641c = 3;

        /* renamed from: d */
        public static final byte f642d = 4;

        /* renamed from: e */
        public static final byte f643e = 5;

        /* renamed from: f */
        public static final byte f644f = 6;

        /* renamed from: g */
        public static final byte f645g = 7;

        /* renamed from: h */
        public static final byte f646h = 8;

        /* renamed from: i */
        public static final byte f647i = 9;

        /* renamed from: j */
        public static final byte f648j = 10;

        /* renamed from: k */
        public static final byte f649k = 11;

        /* renamed from: l */
        public static final byte f650l = 12;

        private C0193b() {
        }
    }

    public C0191cx(C0224dr drVar, long j) {
        super(drVar);
        this.f632m = new C0162cc(15);
        this.f633n = 0;
        this.f634o = null;
        this.f635p = null;
        this.f629a = new byte[5];
        this.f630b = new byte[10];
        this.f637r = new byte[1];
        this.f631c = new byte[1];
        this.f636q = j;
    }

    public C0191cx(C0224dr drVar) {
        this(drVar, -1);
    }

    /* renamed from: B */
    public void mo620B() {
        this.f632m.mo523c();
        this.f633n = 0;
    }

    /* renamed from: a */
    public void mo584a(C0207db dbVar) throws C0172ck {
        m1085b((byte) f624h);
        m1093d(((dbVar.f692b << 5) & -32) | 1);
        mo631b(dbVar.f693c);
        mo587a(dbVar.f691a);
    }

    /* renamed from: a */
    public void mo586a(C0214di diVar) throws C0172ck {
        this.f632m.mo521a(this.f633n);
        this.f633n = 0;
    }

    /* renamed from: b */
    public void mo592b() throws C0172ck {
        this.f633n = this.f632m.mo520a();
    }

    /* renamed from: a */
    public void mo581a(C0194cy cyVar) throws C0172ck {
        if (cyVar.f652b == 2) {
            this.f634o = cyVar;
        } else {
            m1083a(cyVar, (byte) -1);
        }
    }

    /* renamed from: a */
    private void m1083a(C0194cy cyVar, byte b) throws C0172ck {
        if (b == -1) {
            b = m1094e(cyVar.f652b);
        }
        if (cyVar.f653c <= this.f633n || cyVar.f653c - this.f633n > 15) {
            m1085b(b);
            mo589a(cyVar.f653c);
        } else {
            m1093d((int) ((cyVar.f653c - this.f633n) << 4) | b);
        }
        this.f633n = cyVar.f653c;
    }

    /* renamed from: d */
    public void mo595d() throws C0172ck {
        m1085b((byte) 0);
    }

    /* renamed from: a */
    public void mo583a(C0206da daVar) throws C0172ck {
        if (daVar.f690c == 0) {
            m1093d(0);
            return;
        }
        mo631b(daVar.f690c);
        m1093d((int) (m1094e(daVar.f688a) << 4) | m1094e(daVar.f689b));
    }

    /* renamed from: a */
    public void mo582a(C0195cz czVar) throws C0172ck {
        mo621a(czVar.f654a, czVar.f655b);
    }

    /* renamed from: a */
    public void mo585a(C0213dh dhVar) throws C0172ck {
        mo621a(dhVar.f708a, dhVar.f709b);
    }

    /* renamed from: a */
    public void mo590a(boolean z) throws C0172ck {
        byte b = 1;
        if (this.f634o != null) {
            C0194cy cyVar = this.f634o;
            if (!z) {
                b = 2;
            }
            m1083a(cyVar, b);
            this.f634o = null;
            return;
        }
        if (!z) {
            b = 2;
        }
        m1085b(b);
    }

    /* renamed from: a */
    public void mo577a(byte b) throws C0172ck {
        m1085b(b);
    }

    /* renamed from: a */
    public void mo589a(short s) throws C0172ck {
        mo631b(m1088c((int) s));
    }

    /* renamed from: a */
    public void mo579a(int i) throws C0172ck {
        mo631b(m1088c(i));
    }

    /* renamed from: a */
    public void mo580a(long j) throws C0172ck {
        m1087b(m1089c(j));
    }

    /* renamed from: a */
    public void mo578a(double d) throws C0172ck {
        byte[] bArr = {0, 0, 0, 0, 0, 0, 0, 0};
        m1082a(Double.doubleToLongBits(d), bArr, 0);
        this.f698g.mo645b(bArr);
    }

    /* renamed from: a */
    public void mo587a(String str) throws C0172ck {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            m1084a(bytes, 0, bytes.length);
        } catch (UnsupportedEncodingException e) {
            throw new C0172ck("UTF-8 not supported!");
        }
    }

    /* renamed from: a */
    public void mo588a(ByteBuffer byteBuffer) throws C0172ck {
        m1084a(byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), byteBuffer.limit() - byteBuffer.position());
    }

    /* renamed from: a */
    private void m1084a(byte[] bArr, int i, int i2) throws C0172ck {
        mo631b(i2);
        this.f698g.mo635b(bArr, i, i2);
    }

    /* renamed from: a */
    public void mo576a() throws C0172ck {
    }

    /* renamed from: e */
    public void mo597e() throws C0172ck {
    }

    /* renamed from: f */
    public void mo598f() throws C0172ck {
    }

    /* renamed from: g */
    public void mo599g() throws C0172ck {
    }

    /* renamed from: c */
    public void mo593c() throws C0172ck {
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo621a(byte b, int i) throws C0172ck {
        if (i <= 14) {
            m1093d((int) (i << 4) | m1094e(b));
            return;
        }
        m1093d((int) m1094e(b) | 240);
        mo631b(i);
    }

    /* renamed from: b */
    private void mo631b(int i) throws C0172ck {
        int i2 = 0;
        while ((i & -128) != 0) {
            this.f629a[i2] = (byte) ((i & 127) | 128);
            i >>>= 7;
            i2++;
        }
        this.f629a[i2] = (byte) i;
        this.f698g.mo635b(this.f629a, 0, i2 + 1);
    }

    /* renamed from: b */
    private void m1087b(long j) throws C0172ck {
        int i = 0;
        while ((-128 & j) != 0) {
            this.f630b[i] = (byte) ((int) ((127 & j) | 128));
            j >>>= 7;
            i++;
        }
        this.f630b[i] = (byte) ((int) j);
        this.f698g.mo635b(this.f630b, 0, i + 1);
    }

    /* renamed from: c */
    private long m1089c(long j) {
        return (j << 1) ^ (j >> 63);
    }

    /* renamed from: c */
    private int m1088c(int i) {
        return (i << 1) ^ (i >> 31);
    }

    /* renamed from: a */
    private void m1082a(long j, byte[] bArr, int i) {
        bArr[i + 0] = (byte) ((int) (j & 255));
        bArr[i + 1] = (byte) ((int) ((j >> 8) & 255));
        bArr[i + 2] = (byte) ((int) ((j >> 16) & 255));
        bArr[i + 3] = (byte) ((int) ((j >> 24) & 255));
        bArr[i + 4] = (byte) ((int) ((j >> 32) & 255));
        bArr[i + 5] = (byte) ((int) ((j >> 40) & 255));
        bArr[i + 6] = (byte) ((int) ((j >> 48) & 255));
        bArr[i + 7] = (byte) ((int) ((j >> 56) & 255));
    }

    /* renamed from: b */
    private void m1085b(byte b) throws C0172ck {
        this.f637r[0] = b;
        this.f698g.mo645b(this.f637r);
    }

    /* renamed from: d */
    private void m1093d(int i) throws C0172ck {
        m1085b((byte) i);
    }

    /* renamed from: h */
    public C0207db mo600h() throws C0172ck {
        byte u = mo613u();
        if (u != -126) {
            throw new C0210de("Expected protocol id " + Integer.toHexString(-126) + " but got " + Integer.toHexString(u));
        }
        byte u2 = mo613u();
        byte b = (byte) (u2 & 31);
        if (b != 1) {
            throw new C0210de("Expected version 1 but got " + b);
        }
        int E = m1079E();
        return new C0207db(mo618z(), (byte) ((u2 >> 5) & 3), E);
    }

    /* renamed from: j */
    public C0214di mo602j() throws C0172ck {
        this.f632m.mo521a(this.f633n);
        this.f633n = 0;
        return f621d;
    }

    /* renamed from: k */
    public void mo603k() throws C0172ck {
        this.f633n = this.f632m.mo520a();
    }

    /* renamed from: l */
    public C0194cy mo604l() throws C0172ck {
        short s;
        byte u = mo613u();
        if (u == 0) {
            return f622e;
        }
        short s2 = (short) ((u & 240) >> 4);
        if (s2 == 0) {
            s = mo614v();
        } else {
            s = (short) (s2 + this.f633n);
        }
        C0194cy cyVar = new C0194cy("", m1091d((byte) (u & C0217dk.f723m)), s);
        if (m1090c(u)) {
            this.f635p = ((byte) (u & C0217dk.f723m)) == 1 ? Boolean.TRUE : Boolean.FALSE;
        }
        this.f633n = cyVar.f653c;
        return cyVar;
    }

    /* renamed from: n */
    public C0206da mo606n() throws C0172ck {
        int E = m1079E();
        byte u = E == 0 ? 0 : mo613u();
        return new C0206da(m1091d((byte) (u >> 4)), m1091d((byte) (u & C0217dk.f723m)), E);
    }

    /* renamed from: p */
    public C0195cz mo608p() throws C0172ck {
        byte u = mo613u();
        int i = (u >> 4) & 15;
        if (i == 15) {
            i = m1079E();
        }
        return new C0195cz(m1091d(u), i);
    }

    /* renamed from: r */
    public C0213dh mo610r() throws C0172ck {
        return new C0213dh(mo608p());
    }

    /* renamed from: t */
    public boolean mo612t() throws C0172ck {
        if (this.f635p != null) {
            boolean booleanValue = this.f635p.booleanValue();
            this.f635p = null;
            return booleanValue;
        } else if (mo613u() != 1) {
            return false;
        } else {
            return true;
        }
    }

    /* renamed from: u */
    public byte mo613u() throws C0172ck {
        if (this.f698g.mo644h() > 0) {
            byte b = this.f698g.mo642f()[this.f698g.mo643g()];
            this.f698g.mo638a(1);
            return b;
        }
        this.f698g.mo646d(this.f631c, 0, 1);
        return this.f631c[0];
    }

    /* renamed from: v */
    public short mo614v() throws C0172ck {
        return (short) m1097g(m1079E());
    }

    /* renamed from: w */
    public int mo615w() throws C0172ck {
        return m1097g(m1079E());
    }

    /* renamed from: x */
    public long mo616x() throws C0172ck {
        return m1092d(m1080F());
    }

    /* renamed from: y */
    public double mo617y() throws C0172ck {
        byte[] bArr = new byte[8];
        this.f698g.mo646d(bArr, 0, 8);
        return Double.longBitsToDouble(m1081a(bArr));
    }

    /* renamed from: z */
    public String mo618z() throws C0172ck {
        int E = m1079E();
        m1096f(E);
        if (E == 0) {
            return "";
        }
        try {
            if (this.f698g.mo644h() < E) {
                return new String(m1095e(E), "UTF-8");
            }
            String str = new String(this.f698g.mo642f(), this.f698g.mo643g(), E, "UTF-8");
            this.f698g.mo638a(E);
            return str;
        } catch (UnsupportedEncodingException e) {
            throw new C0172ck("UTF-8 not supported!");
        }
    }

    /* renamed from: A */
    public ByteBuffer mo575A() throws C0172ck {
        int E = m1079E();
        m1096f(E);
        if (E == 0) {
            return ByteBuffer.wrap(new byte[0]);
        }
        byte[] bArr = new byte[E];
        this.f698g.mo646d(bArr, 0, E);
        return ByteBuffer.wrap(bArr);
    }

    /* renamed from: e */
    private byte[] m1095e(int i) throws C0172ck {
        if (i == 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[i];
        this.f698g.mo646d(bArr, 0, i);
        return bArr;
    }

    /* renamed from: f */
    private void m1096f(int i) throws C0210de {
        if (i < 0) {
            throw new C0210de("Negative length: " + i);
        } else if (this.f636q != -1 && ((long) i) > this.f636q) {
            throw new C0210de("Length exceeded max allowed: " + i);
        }
    }

    /* renamed from: i */
    public void mo601i() throws C0172ck {
    }

    /* renamed from: m */
    public void mo605m() throws C0172ck {
    }

    /* renamed from: o */
    public void mo607o() throws C0172ck {
    }

    /* renamed from: q */
    public void mo609q() throws C0172ck {
    }

    /* renamed from: s */
    public void mo611s() throws C0172ck {
    }

    /* renamed from: E */
    private int m1079E() throws C0172ck {
        int i = 0;
        if (this.f698g.mo644h() >= 5) {
            byte[] f = this.f698g.mo642f();
            int g = this.f698g.mo643g();
            int i2 = 0;
            int i3 = 0;
            while (true) {
                byte b = f[g + i];
                i3 |= (b & Byte.MAX_VALUE) << i2;
                if ((b & 128) != 128) {
                    this.f698g.mo638a(i + 1);
                    return i3;
                }
                i2 += 7;
                i++;
            }
        } else {
            int i4 = 0;
            while (true) {
                byte u = mo613u();
                i4 |= (u & Byte.MAX_VALUE) << i;
                if ((u & 128) != 128) {
                    return i4;
                }
                i += 7;
            }
        }
    }

    /* renamed from: F */
    private long m1080F() throws C0172ck {
        int i = 0;
        long j = 0;
        if (this.f698g.mo644h() >= 10) {
            byte[] f = this.f698g.mo642f();
            int g = this.f698g.mo643g();
            int i2 = 0;
            while (true) {
                byte b = f[g + i];
                j |= ((long) (b & Byte.MAX_VALUE)) << i2;
                if ((b & 128) != 128) {
                    break;
                }
                i2 += 7;
                i++;
            }
            this.f698g.mo638a(i + 1);
        } else {
            while (true) {
                byte u = mo613u();
                j |= ((long) (u & Byte.MAX_VALUE)) << i;
                if ((u & 128) != 128) {
                    break;
                }
                i += 7;
            }
        }
        return j;
    }

    /* renamed from: g */
    private int m1097g(int i) {
        return (i >>> 1) ^ (-(i & 1));
    }

    /* renamed from: d */
    private long m1092d(long j) {
        return (j >>> 1) ^ (-(1 & j));
    }

    /* renamed from: a */
    private long m1081a(byte[] bArr) {
        return ((((long) bArr[7]) & 255) << 56) | ((((long) bArr[6]) & 255) << 48) | ((((long) bArr[5]) & 255) << 40) | ((((long) bArr[4]) & 255) << 32) | ((((long) bArr[3]) & 255) << 24) | ((((long) bArr[2]) & 255) << 16) | ((((long) bArr[1]) & 255) << 8) | (((long) bArr[0]) & 255);
    }

    /* renamed from: c */
    private boolean m1090c(byte b) {
        byte b2 = b & C0217dk.f723m;
        if (b2 == 1 || b2 == 2) {
            return true;
        }
        return false;
    }

    /* renamed from: d */
    private byte m1091d(byte b) throws C0210de {
        switch ((byte) (b & C0217dk.f723m)) {
            case 0:
                return 0;
            case 1:
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 6;
            case 5:
                return 8;
            case 6:
                return 10;
            case 7:
                return 4;
            case 8:
                return 11;
            case 9:
                return C0217dk.f723m;
            case 10:
                return C0217dk.f722l;
            case 11:
                return C0217dk.f721k;
            case 12:
                return 12;
            default:
                throw new C0210de("don't know what type: " + ((byte) (b & C0217dk.f723m)));
        }
    }

    /* renamed from: e */
    private byte m1094e(byte b) {
        return f623f[b];
    }
}
