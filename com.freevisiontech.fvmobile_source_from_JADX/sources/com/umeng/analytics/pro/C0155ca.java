package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.umeng.analytics.C0015a;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;

/* renamed from: com.umeng.analytics.pro.ca */
/* compiled from: StoreHelper */
public final class C0155ca {

    /* renamed from: a */
    private static C0155ca f547a = null;
    /* access modifiers changed from: private */

    /* renamed from: b */
    public static Context f548b = null;

    /* renamed from: c */
    private static String f549c = null;

    /* renamed from: e */
    private static final String f550e = "mobclick_agent_user_";

    /* renamed from: f */
    private static final String f551f = "mobclick_agent_header_";

    /* renamed from: g */
    private static final String f552g = "mobclick_agent_cached_";

    /* renamed from: d */
    private C0157a f553d;

    /* renamed from: com.umeng.analytics.pro.ca$b */
    /* compiled from: StoreHelper */
    public interface C0160b {
        /* renamed from: a */
        void mo210a(File file);

        /* renamed from: b */
        boolean mo211b(File file);

        /* renamed from: c */
        void mo212c(File file);
    }

    public C0155ca(Context context) {
        this.f553d = new C0157a(context);
    }

    /* renamed from: a */
    public static synchronized C0155ca m887a(Context context) {
        C0155ca caVar;
        synchronized (C0155ca.class) {
            f548b = context.getApplicationContext();
            f549c = context.getPackageName();
            if (f547a == null) {
                f547a = new C0155ca(context);
            }
            caVar = f547a;
        }
        return caVar;
    }

    /* renamed from: a */
    public void mo500a(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            SharedPreferences.Editor edit = m889k().edit();
            edit.putString("au_p", str);
            edit.putString("au_u", str2);
            edit.commit();
        }
    }

    /* renamed from: a */
    public String[] mo502a() {
        SharedPreferences k = m889k();
        String string = k.getString("au_p", (String) null);
        String string2 = k.getString("au_u", (String) null);
        if (string == null || string2 == null) {
            return null;
        }
        return new String[]{string, string2};
    }

    /* renamed from: b */
    public void mo503b() {
        m889k().edit().remove("au_p").remove("au_u").commit();
    }

    /* renamed from: c */
    public String mo505c() {
        SharedPreferences a = C0067az.m285a(f548b);
        if (a != null) {
            return a.getString(C0281x.f901a, (String) null);
        }
        return null;
    }

    /* renamed from: a */
    public void mo499a(String str) {
        SharedPreferences a = C0067az.m285a(f548b);
        if (a != null) {
            a.edit().putString(C0281x.f901a, str).commit();
        }
    }

    /* renamed from: d */
    public String mo507d() {
        SharedPreferences a = C0067az.m285a(f548b);
        if (a != null) {
            return a.getString(C0281x.f945b, (String) null);
        }
        return null;
    }

    /* renamed from: b */
    public void mo504b(String str) {
        SharedPreferences a = C0067az.m285a(f548b);
        if (a != null) {
            a.edit().putString(C0281x.f945b, str).commit();
        }
    }

    /* renamed from: e */
    public String mo508e() {
        SharedPreferences a = C0067az.m285a(f548b);
        if (a != null) {
            return a.getString("st", (String) null);
        }
        return null;
    }

    /* renamed from: c */
    public void mo506c(String str) {
        SharedPreferences a = C0067az.m285a(f548b);
        if (a != null) {
            a.edit().putString("st", str).commit();
        }
    }

    /* renamed from: a */
    public void mo498a(int i) {
        SharedPreferences a = C0067az.m285a(f548b);
        if (a != null) {
            a.edit().putInt("vt", i).commit();
        }
    }

    /* renamed from: f */
    public int mo509f() {
        SharedPreferences a = C0067az.m285a(f548b);
        if (a != null) {
            return a.getInt("vt", 0);
        }
        return 0;
    }

    /* renamed from: g */
    public void mo510g() {
        f548b.deleteFile(m890l());
        f548b.deleteFile(m891m());
        C0277w.m1400a(f548b).mo743a(true, false);
        C0236m.m1296a(f548b).mo702b((C0228f) new C0228f() {
            /* renamed from: a */
            public void mo195a(Object obj, boolean z) {
                if (obj.equals("success")) {
                }
            }
        });
    }

    /* renamed from: a */
    public void mo501a(byte[] bArr) {
        this.f553d.mo514a(bArr);
    }

    /* renamed from: h */
    public boolean mo511h() {
        return this.f553d.mo515a();
    }

    /* renamed from: i */
    public C0157a mo512i() {
        return this.f553d;
    }

    /* renamed from: k */
    private SharedPreferences m889k() {
        return f548b.getSharedPreferences(f550e + f549c, 0);
    }

    /* renamed from: l */
    private String m890l() {
        return f551f + f549c;
    }

    /* renamed from: m */
    private String m891m() {
        SharedPreferences a = C0067az.m285a(f548b);
        if (a == null) {
            return f552g + f549c + C0135bt.m774a(f548b);
        }
        int i = a.getInt(C0015a.f11B, 0);
        int parseInt = Integer.parseInt(C0135bt.m774a(f548b));
        if (i == 0 || parseInt == i) {
            return f552g + f549c + C0135bt.m774a(f548b);
        }
        return f552g + f549c + i;
    }

    /* renamed from: com.umeng.analytics.pro.ca$a */
    /* compiled from: StoreHelper */
    public static class C0157a {

        /* renamed from: a */
        private final int f555a;

        /* renamed from: b */
        private File f556b;

        /* renamed from: c */
        private FilenameFilter f557c;

        public C0157a(Context context) {
            this(context, ".um");
        }

        public C0157a(Context context, String str) {
            this.f555a = 10;
            this.f557c = new FilenameFilter() {
                public boolean accept(File file, String str) {
                    return str.startsWith("um");
                }
            };
            this.f556b = new File(context.getFilesDir(), str);
            if (!this.f556b.exists() || !this.f556b.isDirectory()) {
                this.f556b.mkdir();
            }
        }

        /* renamed from: a */
        public boolean mo515a() {
            File[] listFiles = this.f556b.listFiles();
            if (listFiles == null || listFiles.length <= 0) {
                return false;
            }
            return true;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0047, code lost:
            r2[r0].delete();
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* renamed from: a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void mo513a(com.umeng.analytics.pro.C0155ca.C0160b r6) {
            /*
                r5 = this;
                r0 = 0
                java.io.File r1 = r5.f556b
                java.io.FilenameFilter r2 = r5.f557c
                java.io.File[] r2 = r1.listFiles(r2)
                if (r2 == 0) goto L_0x0029
                int r1 = r2.length
                r3 = 10
                if (r1 < r3) goto L_0x0029
                java.util.Arrays.sort(r2)
                int r1 = r2.length
                int r3 = r1 + -10
                com.umeng.analytics.pro.ca$a$1 r1 = new com.umeng.analytics.pro.ca$a$1
                r1.<init>(r3)
                com.umeng.analytics.pro.C0139bx.m859b(r1)
                r1 = r0
            L_0x001f:
                if (r1 >= r3) goto L_0x0029
                r4 = r2[r1]
                r4.delete()
                int r1 = r1 + 1
                goto L_0x001f
            L_0x0029:
                if (r2 == 0) goto L_0x0054
                int r1 = r2.length
                if (r1 <= 0) goto L_0x0054
                java.io.File r1 = r5.f556b
                r6.mo210a(r1)
                int r1 = r2.length
            L_0x0034:
                if (r0 >= r1) goto L_0x004f
                r3 = r2[r0]     // Catch:{ Throwable -> 0x0046, all -> 0x004d }
                boolean r3 = r6.mo211b(r3)     // Catch:{ Throwable -> 0x0046, all -> 0x004d }
                if (r3 == 0) goto L_0x0043
                r3 = r2[r0]
                r3.delete()
            L_0x0043:
                int r0 = r0 + 1
                goto L_0x0034
            L_0x0046:
                r3 = move-exception
                r3 = r2[r0]
                r3.delete()
                goto L_0x0043
            L_0x004d:
                r0 = move-exception
                throw r0
            L_0x004f:
                java.io.File r0 = r5.f556b
                r6.mo212c(r0)
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0155ca.C0157a.mo513a(com.umeng.analytics.pro.ca$b):void");
        }

        /* renamed from: a */
        public void mo514a(byte[] bArr) {
            if (bArr != null && bArr.length != 0) {
                try {
                    C0136bu.m816a(new File(this.f556b, String.format(Locale.US, "um_cache_%d.env", new Object[]{Long.valueOf(System.currentTimeMillis())})), bArr);
                } catch (Exception e) {
                }
            }
        }

        /* renamed from: b */
        public void mo516b() {
            File[] listFiles = this.f556b.listFiles(this.f557c);
            if (listFiles != null && listFiles.length > 0) {
                for (File delete : listFiles) {
                    delete.delete();
                }
            }
        }

        /* renamed from: c */
        public int mo517c() {
            File[] listFiles = this.f556b.listFiles(this.f557c);
            if (listFiles == null || listFiles.length <= 0) {
                return 0;
            }
            return listFiles.length;
        }
    }
}
