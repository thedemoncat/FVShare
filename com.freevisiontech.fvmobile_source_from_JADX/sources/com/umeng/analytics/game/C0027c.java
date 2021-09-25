package com.umeng.analytics.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.umeng.analytics.C0016b;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.C0025b;
import com.umeng.analytics.pro.C0067az;
import com.umeng.analytics.pro.C0137bv;
import com.umeng.analytics.pro.C0138bw;
import com.umeng.analytics.pro.C0139bx;
import com.umeng.analytics.pro.C0151bz;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/* renamed from: com.umeng.analytics.game.c */
/* compiled from: InternalGameAgent */
class C0027c implements C0137bv {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public C0016b f84a = MobclickAgent.getAgent();
    /* access modifiers changed from: private */

    /* renamed from: b */
    public C0025b f85b = null;

    /* renamed from: c */
    private final int f86c = 100;

    /* renamed from: d */
    private final int f87d = 1;

    /* renamed from: e */
    private final int f88e = 0;

    /* renamed from: f */
    private final int f89f = -1;

    /* renamed from: g */
    private final int f90g = 1;

    /* renamed from: h */
    private final String f91h = "level";

    /* renamed from: i */
    private final String f92i = "pay";

    /* renamed from: j */
    private final String f93j = "buy";

    /* renamed from: k */
    private final String f94k = "use";

    /* renamed from: l */
    private final String f95l = "bonus";

    /* renamed from: m */
    private final String f96m = "item";

    /* renamed from: n */
    private final String f97n = "cash";

    /* renamed from: o */
    private final String f98o = "coin";

    /* renamed from: p */
    private final String f99p = "source";

    /* renamed from: q */
    private final String f100q = "amount";

    /* renamed from: r */
    private final String f101r = "user_level";

    /* renamed from: s */
    private final String f102s = "bonus_source";

    /* renamed from: t */
    private final String f103t = "level";

    /* renamed from: u */
    private final String f104u = "status";

    /* renamed from: v */
    private final String f105v = "duration";

    /* renamed from: w */
    private final String f106w = "curtype";

    /* renamed from: x */
    private final String f107x = "orderid";

    /* renamed from: y */
    private final String f108y = "UMGameAgent.init(Context) should be called before any game api";
    /* access modifiers changed from: private */

    /* renamed from: z */
    public Context f109z;

    public C0027c() {
        C0024a.f71a = true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo104a(Context context) {
        if (context == null) {
            C0138bw.m849e("Context is null, can't init GameAgent");
            return;
        }
        this.f109z = context.getApplicationContext();
        this.f84a.mo65a((C0137bv) this);
        this.f85b = new C0025b(this.f109z);
        this.f84a.mo53a(context, 1);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo108a(boolean z) {
        C0138bw.m831b(String.format("Trace sleep time : %b", new Object[]{Boolean.valueOf(z)}));
        C0024a.f71a = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo105a(String str) {
        this.f85b.f73b = str;
        SharedPreferences a = C0067az.m285a(this.f109z);
        if (a != null) {
            SharedPreferences.Editor edit = a.edit();
            edit.putString("userlevel", str);
            edit.commit();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public void mo110b(final String str) {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        this.f85b.f72a = str;
        C0139bx.m857a(new C0151bz() {
            /* renamed from: a */
            public void mo87a() {
                C0027c.this.f85b.mo88a(str);
                HashMap hashMap = new HashMap();
                hashMap.put("level", str);
                hashMap.put("status", 0);
                if (C0027c.this.f85b.f73b != null) {
                    hashMap.put("user_level", C0027c.this.f85b.f73b);
                }
                C0027c.this.f84a.mo58a(C0027c.this.f109z, "level", (HashMap<String, Object>) hashMap);
            }
        });
    }

    /* renamed from: a */
    private void m74a(final String str, final int i) {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
        } else {
            C0139bx.m857a(new C0151bz() {
                /* renamed from: a */
                public void mo87a() {
                    C0025b.C0026a b = C0027c.this.f85b.mo90b(str);
                    if (b != null) {
                        long e = b.mo97e();
                        if (e <= 0) {
                            C0138bw.m831b("level duration is 0");
                            return;
                        }
                        HashMap hashMap = new HashMap();
                        hashMap.put("level", str);
                        hashMap.put("status", Integer.valueOf(i));
                        hashMap.put("duration", Long.valueOf(e));
                        if (C0027c.this.f85b.f73b != null) {
                            hashMap.put("user_level", C0027c.this.f85b.f73b);
                        }
                        C0027c.this.f84a.mo58a(C0027c.this.f109z, "level", (HashMap<String, Object>) hashMap);
                        return;
                    }
                    C0138bw.m843d(String.format("finishLevel(or failLevel) called before startLevel", new Object[0]));
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: c */
    public void mo112c(String str) {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
        } else {
            m74a(str, 1);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: d */
    public void mo113d(String str) {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
        } else {
            m74a(str, -1);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo100a(double d, double d2, int i) {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("cash", Long.valueOf((long) (d * 100.0d)));
        hashMap.put("coin", Long.valueOf((long) (d2 * 100.0d)));
        hashMap.put("source", Integer.valueOf(i));
        if (this.f85b.f73b != null) {
            hashMap.put("user_level", this.f85b.f73b);
        }
        if (this.f85b.f72a != null) {
            hashMap.put("level", this.f85b.f72a);
        }
        this.f84a.mo58a(this.f109z, "pay", (HashMap<String, Object>) hashMap);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo103a(double d, String str, int i, double d2, int i2) {
        mo100a(d, d2 * ((double) i), i2);
        mo106a(str, i, d2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo106a(String str, int i, double d) {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("item", str);
        hashMap.put("amount", Integer.valueOf(i));
        hashMap.put("coin", Long.valueOf((long) (((double) i) * d * 100.0d)));
        if (this.f85b.f73b != null) {
            hashMap.put("user_level", this.f85b.f73b);
        }
        if (this.f85b.f72a != null) {
            hashMap.put("level", this.f85b.f72a);
        }
        this.f84a.mo58a(this.f109z, "buy", (HashMap<String, Object>) hashMap);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public void mo111b(String str, int i, double d) {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("item", str);
        hashMap.put("amount", Integer.valueOf(i));
        hashMap.put("coin", Long.valueOf((long) (((double) i) * d * 100.0d)));
        if (this.f85b.f73b != null) {
            hashMap.put("user_level", this.f85b.f73b);
        }
        if (this.f85b.f72a != null) {
            hashMap.put("level", this.f85b.f72a);
        }
        this.f84a.mo58a(this.f109z, "use", (HashMap<String, Object>) hashMap);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo101a(double d, int i) {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("coin", Long.valueOf((long) (100.0d * d)));
        hashMap.put("bonus_source", Integer.valueOf(i));
        if (this.f85b.f73b != null) {
            hashMap.put("user_level", this.f85b.f73b);
        }
        if (this.f85b.f72a != null) {
            hashMap.put("level", this.f85b.f72a);
        }
        this.f84a.mo58a(this.f109z, "bonus", (HashMap<String, Object>) hashMap);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo107a(String str, int i, double d, int i2) {
        mo101a(((double) i) * d, i2);
        mo106a(str, i, d);
    }

    /* renamed from: a */
    public void mo99a() {
        C0138bw.m831b("App resume from background");
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
        } else if (C0024a.f71a) {
            this.f85b.mo91b();
        }
    }

    /* renamed from: b */
    public void mo109b() {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
        } else if (C0024a.f71a) {
            this.f85b.mo89a();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo102a(double d, String str, double d2, int i, String str2) {
        if (this.f109z == null) {
            C0138bw.m849e("UMGameAgent.init(Context) should be called before any game api");
        } else if (d >= 0.0d && d2 >= 0.0d) {
            HashMap hashMap = new HashMap();
            if (!TextUtils.isEmpty(str) && str.length() > 0 && str.length() <= 3) {
                hashMap.put("curtype", str);
            }
            if (!TextUtils.isEmpty(str2)) {
                try {
                    int length = str2.getBytes("UTF-8").length;
                    if (length > 0 && length <= 1024) {
                        hashMap.put("orderid", str2);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            hashMap.put("cash", Long.valueOf((long) (d * 100.0d)));
            hashMap.put("coin", Long.valueOf((long) (d2 * 100.0d)));
            hashMap.put("source", Integer.valueOf(i));
            if (this.f85b.f73b != null) {
                hashMap.put("user_level", this.f85b.f73b);
            }
            if (this.f85b.f72a != null) {
                hashMap.put("level", this.f85b.f72a);
            }
            this.f84a.mo58a(this.f109z, "pay", (HashMap<String, Object>) hashMap);
        }
    }
}
