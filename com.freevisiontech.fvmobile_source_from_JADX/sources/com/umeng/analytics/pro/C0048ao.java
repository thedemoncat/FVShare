package com.umeng.analytics.pro;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.umeng.analytics.pro.C0277w;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.ao */
/* compiled from: AutoViewPageTracker */
public class C0048ao {

    /* renamed from: a */
    public static String f191a = null;

    /* renamed from: d */
    private static JSONObject f192d = new JSONObject();

    /* renamed from: b */
    Application.ActivityLifecycleCallbacks f193b = new Application.ActivityLifecycleCallbacks() {
        public void onActivityStopped(Activity activity) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityResumed(Activity activity) {
            C0048ao.this.m199b(activity);
        }

        public void onActivityPaused(Activity activity) {
            C0048ao.this.m201c(activity);
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }
    };

    /* renamed from: c */
    private final Map<String, Long> f194c = new HashMap();

    /* renamed from: e */
    private Application f195e = null;

    public C0048ao(Activity activity) {
        if (activity != null) {
            this.f195e = activity.getApplication();
            m196a(activity);
        }
    }

    /* renamed from: a */
    private void m196a(Activity activity) {
        this.f195e.registerActivityLifecycleCallbacks(this.f193b);
        if (f191a == null) {
            m199b(activity);
        }
    }

    /* renamed from: a */
    public void mo165a() {
        if (this.f195e != null) {
            this.f195e.unregisterActivityLifecycleCallbacks(this.f193b);
        }
    }

    /* renamed from: b */
    public void mo166b() {
        m201c((Activity) null);
        mo165a();
    }

    /* renamed from: a */
    public static void m197a(Context context) {
        JSONObject jSONObject = null;
        try {
            synchronized (f192d) {
                if (f192d.length() > 0) {
                    jSONObject = new JSONObject(f192d.toString());
                    f192d = new JSONObject();
                }
            }
            if (jSONObject == null) {
                return;
            }
            if (jSONObject.length() > 0) {
                C0277w.m1400a(context).mo745a(C0071bb.m307a(), jSONObject, C0277w.C0279a.AUTOPAGE);
            }
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: b */
    public void m199b(Activity activity) {
        f191a = activity.getPackageName() + "." + activity.getLocalClassName();
        synchronized (this.f194c) {
            this.f194c.put(f191a, Long.valueOf(System.currentTimeMillis()));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: c */
    public void m201c(Activity activity) {
        long j = 0;
        try {
            synchronized (this.f194c) {
                if (this.f194c.containsKey(f191a)) {
                    j = System.currentTimeMillis() - this.f194c.get(f191a).longValue();
                    this.f194c.remove(f191a);
                }
            }
            synchronized (f192d) {
                try {
                    f192d = new JSONObject();
                    f192d.put(C0281x.f920ab, f191a);
                    f192d.put("duration", j);
                } catch (Throwable th) {
                }
            }
        } catch (Throwable th2) {
        }
    }
}
