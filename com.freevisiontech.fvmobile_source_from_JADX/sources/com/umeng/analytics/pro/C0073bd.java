package com.umeng.analytics.pro;

import android.content.Context;
import android.text.TextUtils;
import com.umeng.analytics.pro.C0277w;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.bd */
/* compiled from: ViewPageTracker */
public class C0073bd {

    /* renamed from: b */
    private static JSONObject f290b = new JSONObject();

    /* renamed from: a */
    private final Map<String, Long> f291a = new HashMap();

    /* renamed from: a */
    public void mo229a(String str) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.f291a) {
                this.f291a.put(str, Long.valueOf(System.currentTimeMillis()));
            }
        }
    }

    /* renamed from: b */
    public void mo230b(String str) {
        Long remove;
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.f291a) {
                remove = this.f291a.remove(str);
            }
            if (remove != null) {
                long currentTimeMillis = System.currentTimeMillis() - remove.longValue();
                synchronized (f290b) {
                    try {
                        f290b = new JSONObject();
                        f290b.put(C0281x.f920ab, str);
                        f290b.put("duration", currentTimeMillis);
                    } catch (Throwable th) {
                    }
                }
            }
        }
    }

    /* renamed from: a */
    public void mo228a() {
        long j;
        String str;
        String str2 = null;
        long j2 = 0;
        synchronized (this.f291a) {
            for (Map.Entry next : this.f291a.entrySet()) {
                if (((Long) next.getValue()).longValue() > j2) {
                    long longValue = ((Long) next.getValue()).longValue();
                    str = (String) next.getKey();
                    j = longValue;
                } else {
                    j = j2;
                    str = str2;
                }
                str2 = str;
                j2 = j;
            }
        }
        if (str2 != null) {
            mo230b(str2);
        }
    }

    /* renamed from: a */
    public static void m332a(Context context) {
        JSONObject jSONObject = null;
        try {
            synchronized (f290b) {
                if (f290b.length() > 0) {
                    jSONObject = new JSONObject(f290b.toString());
                    f290b = new JSONObject();
                }
            }
            if (jSONObject == null) {
                return;
            }
            if (jSONObject.length() > 0) {
                C0277w.m1400a(context).mo745a(C0071bb.m307a(), jSONObject, C0277w.C0279a.PAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
