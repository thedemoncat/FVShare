package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.umeng.analytics.C0015a;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.m */
/* compiled from: UMCCAggregatedManager */
public class C0236m {

    /* renamed from: i */
    private static final int f771i = 48;

    /* renamed from: j */
    private static final int f772j = 49;
    /* access modifiers changed from: private */

    /* renamed from: k */
    public static Context f773k;
    /* access modifiers changed from: private */

    /* renamed from: a */
    public C0230h f774a;
    /* access modifiers changed from: private */

    /* renamed from: b */
    public C0256o f775b;
    /* access modifiers changed from: private */

    /* renamed from: c */
    public C0259p f776c;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public boolean f777d;

    /* renamed from: e */
    private boolean f778e;

    /* renamed from: f */
    private long f779f;

    /* renamed from: g */
    private final String f780g;

    /* renamed from: h */
    private final String f781h;

    /* renamed from: l */
    private List<String> f782l;
    /* access modifiers changed from: private */

    /* renamed from: m */
    public C0251a f783m;

    /* renamed from: n */
    private final Thread f784n;

    /* renamed from: com.umeng.analytics.pro.m$b */
    /* compiled from: UMCCAggregatedManager */
    private static class C0252b {
        /* access modifiers changed from: private */

        /* renamed from: a */
        public static final C0236m f802a = new C0236m();

        private C0252b() {
        }
    }

    /* renamed from: com.umeng.analytics.pro.m$a */
    /* compiled from: UMCCAggregatedManager */
    private static class C0251a extends Handler {

        /* renamed from: a */
        private final WeakReference<C0236m> f801a;

        public C0251a(C0236m mVar) {
            this.f801a = new WeakReference<>(mVar);
        }

        public void handleMessage(Message message) {
            if (this.f801a != null) {
                switch (message.what) {
                    case 48:
                        sendEmptyMessageDelayed(48, C0260q.m1380c(System.currentTimeMillis()));
                        C0236m.m1296a(C0236m.f773k).m1319p();
                        return;
                    case 49:
                        sendEmptyMessageDelayed(49, C0260q.m1381d(System.currentTimeMillis()));
                        C0236m.m1296a(C0236m.f773k).m1318o();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* renamed from: a */
    public boolean mo700a() {
        return this.f777d;
    }

    private C0236m() {
        this.f774a = null;
        this.f775b = null;
        this.f776c = null;
        this.f777d = false;
        this.f778e = false;
        this.f779f = 0;
        this.f780g = "main_fest_mode";
        this.f781h = "main_fest_timestamp";
        this.f782l = new ArrayList();
        this.f783m = null;
        this.f784n = new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                if (C0236m.this.f783m == null) {
                    C0251a unused = C0236m.this.f783m = new C0251a(C0236m.this);
                }
                C0236m.this.m1307h();
            }
        });
        if (f773k != null) {
            if (this.f774a == null) {
                this.f774a = new C0230h();
            }
            if (this.f775b == null) {
                this.f775b = C0256o.m1355a(f773k);
            }
            if (this.f776c == null) {
                this.f776c = new C0259p();
            }
        }
        this.f784n.start();
    }

    /* access modifiers changed from: private */
    /* renamed from: h */
    public void m1307h() {
        long currentTimeMillis = System.currentTimeMillis();
        this.f783m.sendEmptyMessageDelayed(48, C0260q.m1380c(currentTimeMillis));
        this.f783m.sendEmptyMessageDelayed(49, C0260q.m1381d(currentTimeMillis));
    }

    /* renamed from: a */
    public static final C0236m m1296a(Context context) {
        f773k = context;
        return C0252b.f802a;
    }

    /* renamed from: a */
    public void mo698a(final C0228f fVar) {
        if (!this.f777d) {
            C0139bx.m859b(new C0151bz() {
                /* renamed from: a */
                public void mo87a() {
                    try {
                        C0236m.this.f775b.mo717a((C0228f) new C0228f() {
                            /* renamed from: a */
                            public void mo195a(Object obj, boolean z) {
                                if (obj instanceof Map) {
                                    C0236m.this.f774a.mo660a((Map<List<String>, C0232i>) (Map) obj);
                                } else if ((obj instanceof String) || (obj instanceof Boolean)) {
                                }
                                boolean unused = C0236m.this.f777d = true;
                            }
                        });
                        C0236m.this.m1315l();
                        C0236m.this.m1320q();
                        fVar.mo195a("success", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /* renamed from: a */
    public void mo699a(final C0228f fVar, Map<List<String>, C0235l> map) {
        C0235l lVar = (C0235l) map.values().toArray()[0];
        List<String> a = lVar.mo692a();
        if (this.f782l.size() > 0 && this.f782l.contains(C0196d.m1144a(a))) {
            this.f774a.mo657a((C0228f) new C0228f() {
                /* renamed from: a */
                public void mo195a(Object obj, boolean z) {
                    if (obj instanceof C0230h) {
                        C0230h unused = C0236m.this.f774a = (C0230h) obj;
                    }
                    fVar.mo195a("success", false);
                }
            }, lVar);
        } else if (this.f778e) {
            m1298a(lVar, a);
        } else if (m1310i()) {
            String a2 = C0196d.m1144a(a);
            if (!this.f782l.contains(a2)) {
                this.f782l.add(a2);
            }
            this.f774a.mo659a((C0228f) new C0228f() {
                /* renamed from: a */
                public void mo195a(Object obj, boolean z) {
                    C0230h unused = C0236m.this.f774a = (C0230h) obj;
                }
            }, a, lVar);
        } else {
            m1298a(lVar, a);
            m1312j();
        }
    }

    /* renamed from: a */
    private void m1298a(C0235l lVar, List<String> list) {
        this.f774a.mo658a(new C0228f() {
            /* renamed from: a */
            public void mo195a(Object obj, boolean z) {
                if (obj instanceof C0230h) {
                    C0230h unused = C0236m.this.f774a = (C0230h) obj;
                } else if (obj instanceof Boolean) {
                    C0236m.this.m1317n();
                }
            }
        }, lVar, list, this.f782l);
    }

    /* renamed from: i */
    private boolean m1310i() {
        if (this.f782l.size() < C0253n.m1345a().mo715d()) {
            return true;
        }
        return false;
    }

    /* renamed from: j */
    private void m1312j() {
        SharedPreferences a = C0067az.m285a(f773k);
        if (!a.getBoolean("main_fest_mode", false)) {
            this.f778e = true;
            SharedPreferences.Editor edit = a.edit();
            edit.putBoolean("main_fest_mode", true);
            edit.putLong("main_fest_timestamp", System.currentTimeMillis());
            edit.commit();
        }
    }

    /* renamed from: k */
    private void m1313k() {
        SharedPreferences.Editor edit = C0067az.m285a(f773k).edit();
        edit.putBoolean("main_fest_mode", false);
        edit.putLong("main_fest_timestamp", 0);
        edit.commit();
        this.f778e = false;
    }

    /* access modifiers changed from: private */
    /* renamed from: l */
    public void m1315l() {
        SharedPreferences a = C0067az.m285a(f773k);
        this.f778e = a.getBoolean("main_fest_mode", false);
        this.f779f = a.getLong("main_fest_timestamp", 0);
    }

    /* renamed from: b */
    public JSONObject mo701b() {
        JSONObject a = this.f775b.mo716a();
        JSONObject jSONObject = new JSONObject();
        if (a == null || a.length() <= 0) {
            return null;
        }
        for (String next : this.f782l) {
            if (a.has(next)) {
                try {
                    jSONObject.put(next, a.opt(next));
                } catch (Exception e) {
                }
            }
        }
        return jSONObject;
    }

    /* renamed from: c */
    public JSONObject mo703c() {
        if (this.f776c.mo726a().size() > 0) {
            this.f775b.mo724b(new C0228f() {
                /* renamed from: a */
                public void mo195a(Object obj, boolean z) {
                    if (obj instanceof String) {
                        C0236m.this.f776c.mo731b();
                    }
                }
            }, this.f776c.mo726a());
        }
        return this.f775b.mo723b(new C0228f());
    }

    /* renamed from: b */
    public void mo702b(C0228f fVar) {
        boolean z = false;
        if (this.f778e) {
            if (this.f779f == 0) {
                m1315l();
            }
            z = C0260q.m1378a(System.currentTimeMillis(), this.f779f);
        }
        if (!z) {
            m1313k();
            this.f782l.clear();
        }
        this.f776c.mo731b();
        this.f775b.mo721a((C0228f) new C0228f() {
            /* renamed from: a */
            public void mo195a(Object obj, boolean z) {
                if (obj.equals("success")) {
                    C0236m.this.m1316m();
                }
            }
        }, z);
    }

    /* access modifiers changed from: private */
    /* renamed from: m */
    public void m1316m() {
        for (Map.Entry<List<String>, C0232i> key : this.f774a.mo654a().entrySet()) {
            List list = (List) key.getKey();
            if (!this.f782l.contains(list)) {
                this.f782l.add(C0196d.m1144a((List<String>) list));
            }
        }
        if (this.f782l.size() > 0) {
            this.f775b.mo719a(new C0228f(), this.f782l);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: n */
    public void m1317n() {
        this.f776c.mo727a(new C0228f() {
            /* renamed from: a */
            public void mo195a(Object obj, boolean z) {
                C0259p unused = C0236m.this.f776c = (C0259p) obj;
            }
        }, C0015a.f34u);
    }

    /* renamed from: a */
    public void mo697a(long j, long j2, String str) {
        this.f775b.mo718a(new C0228f() {
            /* renamed from: a */
            public void mo195a(Object obj, boolean z) {
                if (obj.equals("success")) {
                }
            }
        }, str, j, j2);
    }

    /* access modifiers changed from: private */
    /* renamed from: o */
    public void m1318o() {
        try {
            if (this.f774a.mo654a().size() > 0) {
                this.f775b.mo725c(new C0228f() {
                    /* renamed from: a */
                    public void mo195a(Object obj, boolean z) {
                        if (obj instanceof String) {
                            C0236m.this.f774a.mo665d();
                        }
                    }
                }, this.f774a.mo654a());
            }
            if (this.f776c.mo726a().size() > 0) {
                this.f775b.mo724b(new C0228f() {
                    /* renamed from: a */
                    public void mo195a(Object obj, boolean z) {
                        if (obj instanceof String) {
                            C0236m.this.f776c.mo731b();
                        }
                    }
                }, this.f776c.mo726a());
            }
            if (this.f782l.size() > 0) {
                this.f775b.mo719a(new C0228f(), this.f782l);
            }
        } catch (Throwable th) {
            C0138bw.m831b("converyMemoryToDataTable happen error: " + th.toString());
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: p */
    public void m1319p() {
        try {
            if (this.f774a.mo654a().size() > 0) {
                this.f775b.mo720a((C0228f) new C0228f() {
                    /* renamed from: a */
                    public void mo195a(Object obj, boolean z) {
                    }
                }, this.f774a.mo654a());
            }
            if (this.f776c.mo726a().size() > 0) {
                this.f775b.mo724b(new C0228f() {
                    /* renamed from: a */
                    public void mo195a(Object obj, boolean z) {
                        if (obj instanceof String) {
                            C0236m.this.f776c.mo731b();
                        }
                    }
                }, this.f776c.mo726a());
            }
            if (this.f782l.size() > 0) {
                this.f775b.mo719a(new C0228f(), this.f782l);
            }
        } catch (Throwable th) {
            C0138bw.m831b("convertMemoryToCacheTable happen error: " + th.toString());
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: q */
    public void m1320q() {
        List<String> b = this.f775b.mo722b();
        if (b != null) {
            this.f782l = b;
        }
    }

    /* renamed from: d */
    public void mo704d() {
        m1319p();
    }

    /* renamed from: e */
    public void mo705e() {
        m1319p();
    }

    /* renamed from: f */
    public void mo706f() {
        m1319p();
    }
}
