package com.umeng.analytics.pro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.o */
/* compiled from: UMCCStorageManager */
public class C0256o {

    /* renamed from: a */
    private static Context f804a;

    /* renamed from: com.umeng.analytics.pro.o$a */
    /* compiled from: UMCCStorageManager */
    private static final class C0258a {
        /* access modifiers changed from: private */

        /* renamed from: a */
        public static final C0256o f805a = new C0256o();

        private C0258a() {
        }
    }

    private C0256o() {
        if (f804a != null) {
        }
    }

    /* renamed from: a */
    public static C0256o m1355a(Context context) {
        f804a = context;
        return C0258a.f805a;
    }

    /* renamed from: a */
    public void mo717a(C0228f fVar) {
        try {
            SQLiteDatabase a = C0068b.m287a(f804a).mo202a();
            String a2 = C0030a.m95a(a);
            String a3 = C0260q.m1377a(System.currentTimeMillis());
            if (a2.equals("0")) {
                fVar.mo195a("faild", false);
                return;
            }
            if (!a2.equals(a3)) {
                C0030a.m101a(a, fVar);
            } else {
                C0030a.m106b(a, fVar);
            }
            C0068b.m287a(f804a).mo204c();
        } catch (Exception e) {
            fVar.mo195a(false, false);
            C0138bw.m849e("load agg data error");
        } finally {
            C0068b.m287a(f804a).mo204c();
        }
    }

    /* renamed from: a */
    public void mo720a(C0228f fVar, Map<List<String>, C0232i> map) {
        try {
            C0030a.m103a(C0068b.m287a(f804a).mo203b(), map.values());
            fVar.mo195a("success", false);
        } catch (Exception e) {
            C0138bw.m849e("save agg data error");
        } finally {
            C0068b.m287a(f804a).mo204c();
        }
    }

    /* JADX INFO: finally extract failed */
    /* renamed from: a */
    public JSONObject mo716a() {
        try {
            JSONObject b = C0030a.m105b(C0068b.m287a(f804a).mo202a());
            C0068b.m287a(f804a).mo204c();
            return b;
        } catch (Exception e) {
            C0138bw.m849e("upload agg date error");
            C0068b.m287a(f804a).mo204c();
            return null;
        } catch (Throwable th) {
            C0068b.m287a(f804a).mo204c();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    /* renamed from: b */
    public JSONObject mo723b(C0228f fVar) {
        try {
            JSONObject a = C0030a.m96a(fVar, C0068b.m287a(f804a).mo202a());
            C0068b.m287a(f804a).mo204c();
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            C0068b.m287a(f804a).mo204c();
            return null;
        } catch (Throwable th) {
            C0068b.m287a(f804a).mo204c();
            throw th;
        }
    }

    /* renamed from: a */
    public void mo721a(C0228f fVar, boolean z) {
        try {
            C0030a.m99a(C0068b.m287a(f804a).mo203b(), z, fVar);
        } catch (Exception e) {
            C0138bw.m849e("notifyUploadSuccess error");
        } finally {
            C0068b.m287a(f804a).mo204c();
        }
    }

    /* renamed from: a */
    public void mo718a(C0228f fVar, String str, long j, long j2) {
        try {
            C0030a.m97a(C0068b.m287a(f804a).mo203b(), str, j, j2);
            fVar.mo195a("success", false);
        } catch (Exception e) {
            C0138bw.m849e("package size to big or envelopeOverflowPackageCount exception");
        } finally {
            C0068b.m287a(f804a).mo204c();
        }
    }

    /* renamed from: a */
    public void mo719a(C0228f fVar, List<String> list) {
        try {
            C0030a.m100a(fVar, C0068b.m287a(f804a).mo203b(), list);
        } catch (Exception e) {
            C0138bw.m849e("saveToLimitCKTable exception");
        } finally {
            C0068b.m287a(f804a).mo204c();
        }
    }

    /* renamed from: b */
    public void mo724b(C0228f fVar, Map<String, C0234k> map) {
        try {
            C0030a.m98a(C0068b.m287a(f804a).mo203b(), map, fVar);
        } catch (Exception e) {
            C0138bw.m849e("arrgetated system buffer exception");
        } finally {
            C0068b.m287a(f804a).mo204c();
        }
    }

    /* JADX INFO: finally extract failed */
    /* renamed from: b */
    public List<String> mo722b() {
        try {
            List<String> c = C0030a.m109c(C0068b.m287a(f804a).mo202a());
            C0068b.m287a(f804a).mo204c();
            return c;
        } catch (Exception e) {
            C0138bw.m849e("loadCKToMemory exception");
            C0068b.m287a(f804a).mo204c();
            return null;
        } catch (Throwable th) {
            C0068b.m287a(f804a).mo204c();
            throw th;
        }
    }

    /* renamed from: c */
    public void mo725c(C0228f fVar, Map<List<String>, C0232i> map) {
        try {
            C0030a.m104a(fVar, C0068b.m287a(f804a).mo203b(), map.values());
        } catch (Exception e) {
            C0138bw.m849e("cacheToData error");
        } finally {
            C0068b.m287a(f804a).mo204c();
        }
    }
}
