package com.umeng.analytics.pro;

import android.content.Context;
import android.text.TextUtils;
import com.umeng.analytics.C0015a;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/* renamed from: com.umeng.analytics.pro.af */
/* compiled from: ImprintHandler */
public class C0037af {

    /* renamed from: a */
    private static final String f145a = ".imprint";

    /* renamed from: b */
    private static final byte[] f146b = "pbl0".getBytes();

    /* renamed from: f */
    private static C0037af f147f;

    /* renamed from: c */
    private C0066ay f148c;

    /* renamed from: d */
    private C0038a f149d = new C0038a();

    /* renamed from: e */
    private C0099bl f150e = null;

    /* renamed from: g */
    private Context f151g;

    C0037af(Context context) {
        this.f151g = context;
    }

    /* renamed from: a */
    public static synchronized C0037af m144a(Context context) {
        C0037af afVar;
        synchronized (C0037af.class) {
            if (f147f == null) {
                f147f = new C0037af(context);
                f147f.mo142c();
            }
            afVar = f147f;
        }
        return afVar;
    }

    /* renamed from: a */
    public void mo138a(C0066ay ayVar) {
        this.f148c = ayVar;
    }

    /* renamed from: a */
    public String mo137a(C0099bl blVar) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : new TreeMap(blVar.mo354d()).entrySet()) {
            sb.append((String) entry.getKey());
            if (((C0106bm) entry.getValue()).mo381e()) {
                sb.append(((C0106bm) entry.getValue()).mo378c());
            }
            sb.append(((C0106bm) entry.getValue()).mo382f());
            sb.append(((C0106bm) entry.getValue()).mo385i());
        }
        sb.append(blVar.f398b);
        return C0136bu.m813a(sb.toString()).toLowerCase(Locale.US);
    }

    /* renamed from: c */
    private boolean m147c(C0099bl blVar) {
        if (!blVar.mo360j().equals(mo137a(blVar))) {
            return false;
        }
        for (C0106bm next : blVar.mo354d().values()) {
            byte[] a = C0133br.m755a(next.mo385i());
            byte[] a2 = mo139a(next);
            int i = 0;
            while (true) {
                if (i < 4) {
                    if (a[i] != a2[i]) {
                        return false;
                    }
                    i++;
                }
            }
        }
        return true;
    }

    /* renamed from: a */
    public byte[] mo139a(C0106bm bmVar) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.order((ByteOrder) null);
        allocate.putLong(bmVar.mo382f());
        byte[] array = allocate.array();
        byte[] bArr = f146b;
        byte[] bArr2 = new byte[4];
        for (int i = 0; i < 4; i++) {
            bArr2[i] = (byte) (array[i] ^ bArr[i]);
        }
        return bArr2;
    }

    /* renamed from: b */
    public void mo141b(C0099bl blVar) {
        C0099bl a;
        String str = null;
        if (blVar != null && m147c(blVar)) {
            boolean z = false;
            synchronized (this) {
                C0099bl blVar2 = this.f150e;
                String j = blVar2 == null ? null : blVar2.mo360j();
                if (blVar2 == null) {
                    a = m148d(blVar);
                } else {
                    a = m145a(blVar2, blVar);
                }
                this.f150e = a;
                if (a != null) {
                    str = a.mo360j();
                }
                if (!m146a(j, str)) {
                    z = true;
                }
            }
            if (this.f150e != null && z) {
                this.f149d.mo148a(this.f150e);
                if (this.f148c != null) {
                    this.f148c.mo177a(this.f149d);
                }
            }
        }
    }

    /* renamed from: a */
    private boolean m146a(String str, String str2) {
        if (str != null) {
            return str.equals(str2);
        }
        if (str2 != null) {
            return false;
        }
        return true;
    }

    /* renamed from: a */
    private C0099bl m145a(C0099bl blVar, C0099bl blVar2) {
        if (blVar2 != null) {
            Map<String, C0106bm> d = blVar.mo354d();
            for (Map.Entry next : blVar2.mo354d().entrySet()) {
                if (((C0106bm) next.getValue()).mo381e()) {
                    d.put(next.getKey(), next.getValue());
                } else {
                    d.remove(next.getKey());
                }
            }
            blVar.mo345a(blVar2.mo357g());
            blVar.mo346a(mo137a(blVar));
        }
        return blVar;
    }

    /* renamed from: d */
    private C0099bl m148d(C0099bl blVar) {
        Map<String, C0106bm> d = blVar.mo354d();
        ArrayList<String> arrayList = new ArrayList<>(d.size() / 2);
        for (Map.Entry next : d.entrySet()) {
            if (!((C0106bm) next.getValue()).mo381e()) {
                arrayList.add(next.getKey());
            }
        }
        for (String remove : arrayList) {
            d.remove(remove);
        }
        return blVar;
    }

    /* renamed from: a */
    public synchronized C0099bl mo136a() {
        return this.f150e;
    }

    /* renamed from: b */
    public C0038a mo140b() {
        return this.f149d;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: byte[]} */
    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0028 A[SYNTHETIC, Splitter:B:8:0x0028] */
    /* renamed from: c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void mo142c() {
        /*
            r4 = this;
            r2 = 0
            java.io.File r0 = new java.io.File
            android.content.Context r1 = r4.f151g
            java.io.File r1 = r1.getFilesDir()
            java.lang.String r3 = ".imprint"
            r0.<init>(r1, r3)
            boolean r0 = r0.exists()
            if (r0 != 0) goto L_0x0016
        L_0x0015:
            return
        L_0x0016:
            android.content.Context r0 = r4.f151g     // Catch:{ Exception -> 0x0042, all -> 0x004b }
            java.lang.String r1 = ".imprint"
            java.io.FileInputStream r1 = r0.openFileInput(r1)     // Catch:{ Exception -> 0x0042, all -> 0x004b }
            byte[] r2 = com.umeng.analytics.pro.C0136bu.m820b((java.io.InputStream) r1)     // Catch:{ Exception -> 0x0053 }
            com.umeng.analytics.pro.C0136bu.m821c(r1)
        L_0x0026:
            if (r2 == 0) goto L_0x0015
            com.umeng.analytics.pro.bl r0 = new com.umeng.analytics.pro.bl     // Catch:{ Exception -> 0x003d }
            r0.<init>()     // Catch:{ Exception -> 0x003d }
            com.umeng.analytics.pro.ch r1 = new com.umeng.analytics.pro.ch     // Catch:{ Exception -> 0x003d }
            r1.<init>()     // Catch:{ Exception -> 0x003d }
            r1.mo533a((com.umeng.analytics.pro.C0164ce) r0, (byte[]) r2)     // Catch:{ Exception -> 0x003d }
            r4.f150e = r0     // Catch:{ Exception -> 0x003d }
            com.umeng.analytics.pro.af$a r1 = r4.f149d     // Catch:{ Exception -> 0x003d }
            r1.mo148a((com.umeng.analytics.pro.C0099bl) r0)     // Catch:{ Exception -> 0x003d }
            goto L_0x0015
        L_0x003d:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0015
        L_0x0042:
            r0 = move-exception
            r1 = r2
        L_0x0044:
            r0.printStackTrace()     // Catch:{ all -> 0x0050 }
            com.umeng.analytics.pro.C0136bu.m821c(r1)
            goto L_0x0026
        L_0x004b:
            r0 = move-exception
        L_0x004c:
            com.umeng.analytics.pro.C0136bu.m821c(r2)
            throw r0
        L_0x0050:
            r0 = move-exception
            r2 = r1
            goto L_0x004c
        L_0x0053:
            r0 = move-exception
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0037af.mo142c():void");
    }

    /* renamed from: d */
    public void mo143d() {
        if (this.f150e != null) {
            try {
                C0136bu.m816a(new File(this.f151g.getFilesDir(), f145a), new C0175cn().mo544a(this.f150e));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: e */
    public boolean mo144e() {
        return new File(this.f151g.getFilesDir(), f145a).delete();
    }

    /* renamed from: com.umeng.analytics.pro.af$a */
    /* compiled from: ImprintHandler */
    public static class C0038a {

        /* renamed from: a */
        private int f152a = -1;

        /* renamed from: b */
        private int f153b = -1;

        /* renamed from: c */
        private int f154c = -1;

        /* renamed from: d */
        private int f155d = -1;

        /* renamed from: e */
        private int f156e = -1;

        /* renamed from: f */
        private String f157f = null;

        /* renamed from: g */
        private int f158g = -1;

        /* renamed from: h */
        private String f159h = null;

        /* renamed from: i */
        private int f160i = -1;

        /* renamed from: j */
        private int f161j = -1;

        /* renamed from: k */
        private String f162k = null;

        /* renamed from: l */
        private String f163l = null;

        /* renamed from: m */
        private String f164m = null;

        /* renamed from: n */
        private String f165n = null;

        /* renamed from: o */
        private String f166o = null;

        C0038a() {
        }

        C0038a(C0099bl blVar) {
            mo148a(blVar);
        }

        /* renamed from: a */
        public void mo148a(C0099bl blVar) {
            if (blVar != null) {
                this.f152a = m158a(blVar, "defcon");
                this.f153b = m158a(blVar, C0281x.f932an);
                this.f154c = m158a(blVar, "codex");
                this.f155d = m158a(blVar, "report_policy");
                this.f156e = m158a(blVar, "report_interval");
                this.f157f = m159b(blVar, "client_test");
                this.f158g = m158a(blVar, "test_report_interval");
                this.f159h = m159b(blVar, "umid");
                this.f160i = m158a(blVar, "integrated_test");
                this.f161j = m158a(blVar, "latent_hours");
                this.f162k = m159b(blVar, C0281x.f881G);
                this.f163l = m159b(blVar, "domain_p");
                this.f164m = m159b(blVar, "domain_s");
                this.f165n = m159b(blVar, C0281x.f891Q);
                this.f166o = m159b(blVar, "track_list");
            }
        }

        /* renamed from: a */
        public String mo147a(String str) {
            if (this.f165n != null) {
                return this.f165n;
            }
            return str;
        }

        /* renamed from: b */
        public String mo152b(String str) {
            if (this.f166o != null) {
                return this.f166o;
            }
            return str;
        }

        /* renamed from: c */
        public String mo155c(String str) {
            if (this.f164m != null) {
                return this.f164m;
            }
            return str;
        }

        /* renamed from: d */
        public String mo157d(String str) {
            if (this.f163l != null) {
                return this.f163l;
            }
            return str;
        }

        /* renamed from: e */
        public String mo158e(String str) {
            if (this.f162k != null) {
                return this.f162k;
            }
            return str;
        }

        /* renamed from: a */
        public int mo145a(int i) {
            return (this.f152a != -1 && this.f152a <= 3 && this.f152a >= 0) ? this.f152a : i;
        }

        /* renamed from: b */
        public int mo151b(int i) {
            return (this.f153b != -1 && this.f153b >= 0 && this.f153b <= 1800) ? this.f153b * 1000 : i;
        }

        /* renamed from: c */
        public int mo154c(int i) {
            if (this.f154c == 0 || this.f154c == 1 || this.f154c == -1) {
                return this.f154c;
            }
            return i;
        }

        /* renamed from: a */
        public int[] mo150a(int i, int i2) {
            if (this.f155d == -1 || !C0140by.m861a(this.f155d)) {
                return new int[]{i, i2};
            }
            if (this.f156e == -1 || this.f156e < 90 || this.f156e > 86400) {
                this.f156e = 90;
            }
            return new int[]{this.f155d, this.f156e * 1000};
        }

        /* renamed from: f */
        public String mo159f(String str) {
            return (this.f157f == null || !C0074be.m337a(this.f157f)) ? str : this.f157f;
        }

        /* renamed from: d */
        public int mo156d(int i) {
            return (this.f158g == -1 || this.f158g < 90 || this.f158g > 86400) ? i : this.f158g * 1000;
        }

        /* renamed from: a */
        public boolean mo149a() {
            return this.f158g != -1;
        }

        /* renamed from: g */
        public String mo160g(String str) {
            return this.f159h;
        }

        /* renamed from: b */
        public boolean mo153b() {
            return this.f160i == 1;
        }

        /* renamed from: a */
        public long mo146a(long j) {
            return (this.f161j != -1 && this.f161j >= 48) ? C0015a.f23j * ((long) this.f161j) : j;
        }

        /* renamed from: a */
        private int m158a(C0099bl blVar, String str) {
            if (blVar != null) {
                try {
                    if (blVar.mo356f()) {
                        C0106bm bmVar = blVar.mo354d().get(str);
                        if (bmVar == null || TextUtils.isEmpty(bmVar.mo378c())) {
                            return -1;
                        }
                        try {
                            return Integer.parseInt(bmVar.mo378c().trim());
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return -1;
                }
            }
            return -1;
        }

        /* renamed from: b */
        private String m159b(C0099bl blVar, String str) {
            String str2;
            C0106bm bmVar;
            if (blVar == null) {
                return null;
            }
            try {
                if (!blVar.mo356f() || (bmVar = blVar.mo354d().get(str)) == null || TextUtils.isEmpty(bmVar.mo378c())) {
                    return null;
                }
                str2 = bmVar.mo378c();
                return str2;
            } catch (Exception e) {
                e.printStackTrace();
                str2 = null;
            }
        }
    }
}
