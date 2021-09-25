package com.umeng.analytics.pro;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.umeng.analytics.pro.p */
/* compiled from: UMCCSystemBufferManager */
public class C0259p implements Serializable {

    /* renamed from: a */
    private static final long f806a = 1;

    /* renamed from: b */
    private Map<String, C0234k> f807b = new HashMap();

    /* renamed from: a */
    public void mo727a(C0228f fVar, String str) {
        if (this.f807b.containsKey(str)) {
            m1369c(str);
        } else {
            m1368b(str);
        }
        fVar.mo195a(this, false);
    }

    /* renamed from: a */
    public Map<String, C0234k> mo726a() {
        return this.f807b;
    }

    /* renamed from: b */
    public void mo731b() {
        this.f807b.clear();
    }

    /* renamed from: a */
    public void mo729a(Map<String, C0234k> map) {
        this.f807b = map;
    }

    /* renamed from: a */
    public boolean mo730a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (Map.Entry<String, C0234k> key : this.f807b.entrySet()) {
            if (((String) key.getKey()).equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: b */
    private void m1368b(String str) {
        this.f807b.put(str, new C0234k(str, System.currentTimeMillis(), 1));
    }

    /* renamed from: c */
    private void m1369c(String str) {
        this.f807b.put(str, this.f807b.get(str).mo684a());
    }

    /* renamed from: a */
    public void mo728a(C0234k kVar) {
        if (mo730a(kVar.mo689c())) {
            m1367b(kVar);
        } else {
            this.f807b.put(kVar.mo689c(), kVar);
        }
    }

    /* renamed from: b */
    private void m1367b(C0234k kVar) {
        this.f807b.put(kVar.mo689c(), this.f807b.get(kVar.mo689c()).mo685a(kVar));
    }
}
