package com.umeng.analytics.pro;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* renamed from: com.umeng.analytics.pro.cw */
/* compiled from: TBinaryProtocol */
public class C0189cw extends C0209dd {

    /* renamed from: a */
    protected static final int f603a = -65536;

    /* renamed from: b */
    protected static final int f604b = -2147418112;

    /* renamed from: h */
    private static final C0214di f605h = new C0214di();

    /* renamed from: c */
    protected boolean f606c;

    /* renamed from: d */
    protected boolean f607d;

    /* renamed from: e */
    protected int f608e;

    /* renamed from: f */
    protected boolean f609f;

    /* renamed from: i */
    private byte[] f610i;

    /* renamed from: j */
    private byte[] f611j;

    /* renamed from: k */
    private byte[] f612k;

    /* renamed from: l */
    private byte[] f613l;

    /* renamed from: m */
    private byte[] f614m;

    /* renamed from: n */
    private byte[] f615n;

    /* renamed from: o */
    private byte[] f616o;

    /* renamed from: p */
    private byte[] f617p;

    /* renamed from: com.umeng.analytics.pro.cw$a */
    /* compiled from: TBinaryProtocol */
    public static class C0190a implements C0211df {

        /* renamed from: a */
        protected boolean f618a;

        /* renamed from: b */
        protected boolean f619b;

        /* renamed from: c */
        protected int f620c;

        public C0190a() {
            this(false, true);
        }

        public C0190a(boolean z, boolean z2) {
            this(z, z2, 0);
        }

        public C0190a(boolean z, boolean z2, int i) {
            this.f618a = false;
            this.f619b = true;
            this.f618a = z;
            this.f619b = z2;
            this.f620c = i;
        }

        /* renamed from: a */
        public C0209dd mo619a(C0224dr drVar) {
            C0189cw cwVar = new C0189cw(drVar, this.f618a, this.f619b);
            if (this.f620c != 0) {
                cwVar.mo594c(this.f620c);
            }
            return cwVar;
        }
    }

    public C0189cw(C0224dr drVar) {
        this(drVar, false, true);
    }

    public C0189cw(C0224dr drVar, boolean z, boolean z2) {
        super(drVar);
        this.f606c = false;
        this.f607d = true;
        this.f609f = false;
        this.f610i = new byte[1];
        this.f611j = new byte[2];
        this.f612k = new byte[4];
        this.f613l = new byte[8];
        this.f614m = new byte[1];
        this.f615n = new byte[2];
        this.f616o = new byte[4];
        this.f617p = new byte[8];
        this.f606c = z;
        this.f607d = z2;
    }

    /* renamed from: a */
    public void mo584a(C0207db dbVar) throws C0172ck {
        if (this.f607d) {
            mo579a((int) f604b | dbVar.f692b);
            mo587a(dbVar.f691a);
            mo579a(dbVar.f693c);
            return;
        }
        mo587a(dbVar.f691a);
        mo577a(dbVar.f692b);
        mo579a(dbVar.f693c);
    }

    /* renamed from: a */
    public void mo576a() {
    }

    /* renamed from: a */
    public void mo586a(C0214di diVar) {
    }

    /* renamed from: b */
    public void mo592b() {
    }

    /* renamed from: a */
    public void mo581a(C0194cy cyVar) throws C0172ck {
        mo577a(cyVar.f652b);
        mo589a(cyVar.f653c);
    }

    /* renamed from: c */
    public void mo593c() {
    }

    /* renamed from: d */
    public void mo595d() throws C0172ck {
        mo577a((byte) 0);
    }

    /* renamed from: a */
    public void mo583a(C0206da daVar) throws C0172ck {
        mo577a(daVar.f688a);
        mo577a(daVar.f689b);
        mo579a(daVar.f690c);
    }

    /* renamed from: e */
    public void mo597e() {
    }

    /* renamed from: a */
    public void mo582a(C0195cz czVar) throws C0172ck {
        mo577a(czVar.f654a);
        mo579a(czVar.f655b);
    }

    /* renamed from: f */
    public void mo598f() {
    }

    /* renamed from: a */
    public void mo585a(C0213dh dhVar) throws C0172ck {
        mo577a(dhVar.f708a);
        mo579a(dhVar.f709b);
    }

    /* renamed from: g */
    public void mo599g() {
    }

    /* renamed from: a */
    public void mo590a(boolean z) throws C0172ck {
        mo577a(z ? (byte) 1 : 0);
    }

    /* renamed from: a */
    public void mo577a(byte b) throws C0172ck {
        this.f610i[0] = b;
        this.f698g.mo635b(this.f610i, 0, 1);
    }

    /* renamed from: a */
    public void mo589a(short s) throws C0172ck {
        this.f611j[0] = (byte) ((s >> 8) & 255);
        this.f611j[1] = (byte) (s & 255);
        this.f698g.mo635b(this.f611j, 0, 2);
    }

    /* renamed from: a */
    public void mo579a(int i) throws C0172ck {
        this.f612k[0] = (byte) ((i >> 24) & 255);
        this.f612k[1] = (byte) ((i >> 16) & 255);
        this.f612k[2] = (byte) ((i >> 8) & 255);
        this.f612k[3] = (byte) (i & 255);
        this.f698g.mo635b(this.f612k, 0, 4);
    }

    /* renamed from: a */
    public void mo580a(long j) throws C0172ck {
        this.f613l[0] = (byte) ((int) ((j >> 56) & 255));
        this.f613l[1] = (byte) ((int) ((j >> 48) & 255));
        this.f613l[2] = (byte) ((int) ((j >> 40) & 255));
        this.f613l[3] = (byte) ((int) ((j >> 32) & 255));
        this.f613l[4] = (byte) ((int) ((j >> 24) & 255));
        this.f613l[5] = (byte) ((int) ((j >> 16) & 255));
        this.f613l[6] = (byte) ((int) ((j >> 8) & 255));
        this.f613l[7] = (byte) ((int) (255 & j));
        this.f698g.mo635b(this.f613l, 0, 8);
    }

    /* renamed from: a */
    public void mo578a(double d) throws C0172ck {
        mo580a(Double.doubleToLongBits(d));
    }

    /* renamed from: a */
    public void mo587a(String str) throws C0172ck {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            mo579a(bytes.length);
            this.f698g.mo635b(bytes, 0, bytes.length);
        } catch (UnsupportedEncodingException e) {
            throw new C0172ck("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    /* renamed from: a */
    public void mo588a(ByteBuffer byteBuffer) throws C0172ck {
        int limit = byteBuffer.limit() - byteBuffer.position();
        mo579a(limit);
        this.f698g.mo635b(byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), limit);
    }

    /* renamed from: h */
    public C0207db mo600h() throws C0172ck {
        int w = mo615w();
        if (w < 0) {
            if ((-65536 & w) == f604b) {
                return new C0207db(mo618z(), (byte) (w & 255), mo615w());
            }
            throw new C0210de(4, "Bad version in readMessageBegin");
        } else if (!this.f606c) {
            return new C0207db(mo591b(w), mo613u(), mo615w());
        } else {
            throw new C0210de(4, "Missing version in readMessageBegin, old client?");
        }
    }

    /* renamed from: i */
    public void mo601i() {
    }

    /* renamed from: j */
    public C0214di mo602j() {
        return f605h;
    }

    /* renamed from: k */
    public void mo603k() {
    }

    /* renamed from: l */
    public C0194cy mo604l() throws C0172ck {
        byte u = mo613u();
        return new C0194cy("", u, u == 0 ? 0 : mo614v());
    }

    /* renamed from: m */
    public void mo605m() {
    }

    /* renamed from: n */
    public C0206da mo606n() throws C0172ck {
        return new C0206da(mo613u(), mo613u(), mo615w());
    }

    /* renamed from: o */
    public void mo607o() {
    }

    /* renamed from: p */
    public C0195cz mo608p() throws C0172ck {
        return new C0195cz(mo613u(), mo615w());
    }

    /* renamed from: q */
    public void mo609q() {
    }

    /* renamed from: r */
    public C0213dh mo610r() throws C0172ck {
        return new C0213dh(mo613u(), mo615w());
    }

    /* renamed from: s */
    public void mo611s() {
    }

    /* renamed from: t */
    public boolean mo612t() throws C0172ck {
        return mo613u() == 1;
    }

    /* renamed from: u */
    public byte mo613u() throws C0172ck {
        if (this.f698g.mo644h() >= 1) {
            byte b = this.f698g.mo642f()[this.f698g.mo643g()];
            this.f698g.mo638a(1);
            return b;
        }
        m1033a(this.f614m, 0, 1);
        return this.f614m[0];
    }

    /* renamed from: v */
    public short mo614v() throws C0172ck {
        int i = 0;
        byte[] bArr = this.f615n;
        if (this.f698g.mo644h() >= 2) {
            bArr = this.f698g.mo642f();
            i = this.f698g.mo643g();
            this.f698g.mo638a(2);
        } else {
            m1033a(this.f615n, 0, 2);
        }
        return (short) ((bArr[i + 1] & 255) | ((bArr[i] & 255) << 8));
    }

    /* renamed from: w */
    public int mo615w() throws C0172ck {
        int i = 0;
        byte[] bArr = this.f616o;
        if (this.f698g.mo644h() >= 4) {
            bArr = this.f698g.mo642f();
            i = this.f698g.mo643g();
            this.f698g.mo638a(4);
        } else {
            m1033a(this.f616o, 0, 4);
        }
        return (bArr[i + 3] & 255) | ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << C0217dk.f724n) | ((bArr[i + 2] & 255) << 8);
    }

    /* renamed from: x */
    public long mo616x() throws C0172ck {
        int i = 0;
        byte[] bArr = this.f617p;
        if (this.f698g.mo644h() >= 8) {
            bArr = this.f698g.mo642f();
            i = this.f698g.mo643g();
            this.f698g.mo638a(8);
        } else {
            m1033a(this.f617p, 0, 8);
        }
        return ((long) (bArr[i + 7] & 255)) | (((long) (bArr[i] & 255)) << 56) | (((long) (bArr[i + 1] & 255)) << 48) | (((long) (bArr[i + 2] & 255)) << 40) | (((long) (bArr[i + 3] & 255)) << 32) | (((long) (bArr[i + 4] & 255)) << 24) | (((long) (bArr[i + 5] & 255)) << 16) | (((long) (bArr[i + 6] & 255)) << 8);
    }

    /* renamed from: y */
    public double mo617y() throws C0172ck {
        return Double.longBitsToDouble(mo616x());
    }

    /* renamed from: z */
    public String mo618z() throws C0172ck {
        int w = mo615w();
        if (this.f698g.mo644h() < w) {
            return mo591b(w);
        }
        try {
            String str = new String(this.f698g.mo642f(), this.f698g.mo643g(), w, "UTF-8");
            this.f698g.mo638a(w);
            return str;
        } catch (UnsupportedEncodingException e) {
            throw new C0172ck("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    /* renamed from: b */
    public String mo591b(int i) throws C0172ck {
        try {
            mo596d(i);
            byte[] bArr = new byte[i];
            this.f698g.mo646d(bArr, 0, i);
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new C0172ck("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    /* renamed from: A */
    public ByteBuffer mo575A() throws C0172ck {
        int w = mo615w();
        mo596d(w);
        if (this.f698g.mo644h() >= w) {
            ByteBuffer wrap = ByteBuffer.wrap(this.f698g.mo642f(), this.f698g.mo643g(), w);
            this.f698g.mo638a(w);
            return wrap;
        }
        byte[] bArr = new byte[w];
        this.f698g.mo646d(bArr, 0, w);
        return ByteBuffer.wrap(bArr);
    }

    /* renamed from: a */
    private int m1033a(byte[] bArr, int i, int i2) throws C0172ck {
        mo596d(i2);
        return this.f698g.mo646d(bArr, i, i2);
    }

    /* renamed from: c */
    public void mo594c(int i) {
        this.f608e = i;
        this.f609f = true;
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public void mo596d(int i) throws C0172ck {
        if (i < 0) {
            throw new C0210de("Negative length: " + i);
        } else if (this.f609f) {
            this.f608e -= i;
            if (this.f608e < 0) {
                throw new C0210de("Message length exceeded: " + i);
            }
        }
    }
}
