package com.umeng.analytics.pro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* renamed from: com.umeng.analytics.pro.h */
/* compiled from: UMCCAggregatedListObject */
public class C0230h implements Serializable {

    /* renamed from: a */
    private static final long f739a = 1;
    /* access modifiers changed from: private */

    /* renamed from: b */
    public Map<List<String>, C0232i> f740b = new HashMap();

    /* renamed from: c */
    private long f741c = 0;

    /* renamed from: a */
    public Map<List<String>, C0232i> mo654a() {
        return this.f740b;
    }

    /* renamed from: a */
    public void mo660a(Map<List<String>, C0232i> map) {
        if (this.f740b.size() <= 0) {
            this.f740b = map;
        } else {
            m1248b(map);
        }
    }

    /* renamed from: b */
    private void m1248b(Map<List<String>, C0232i> map) {
        new ArrayList();
        new ArrayList();
        Iterator<Map.Entry<List<String>, C0232i>> it = this.f740b.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry next = it.next();
            List list = (List) next.getKey();
            Iterator<Map.Entry<List<String>, C0232i>> it2 = this.f740b.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry next2 = it2.next();
                List list2 = (List) next.getKey();
                if (!list.equals(list2)) {
                    this.f740b.put(list2, next2.getValue());
                } else {
                    C0232i iVar = (C0232i) next2.getValue();
                    m1247a((C0232i) next.getValue(), iVar);
                    this.f740b.remove(list);
                    this.f740b.put(list, iVar);
                }
            }
        }
    }

    /* renamed from: a */
    private void m1247a(C0232i iVar, C0232i iVar2) {
        iVar2.mo677c(iVar2.mo681g() + iVar.mo681g());
        iVar2.mo673b(iVar2.mo680f() + iVar.mo680f());
        iVar2.mo667a(iVar2.mo679e() + iVar.mo679e());
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < iVar.mo678d().size()) {
                iVar2.mo670a(iVar.mo678d().get(i2));
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    /* renamed from: b */
    public long mo663b() {
        return this.f741c;
    }

    /* renamed from: a */
    public void mo655a(long j) {
        this.f741c = j;
    }

    /* renamed from: a */
    public void mo657a(final C0228f fVar, C0235l lVar) {
        try {
            if (mo661a((List<?>) lVar.mo692a())) {
                C0232i iVar = this.f740b.get(lVar.mo692a());
                if (iVar != null) {
                    iVar.mo668a(new C0228f() {
                        /* renamed from: a */
                        public void mo195a(Object obj, boolean z) {
                            C0232i iVar = (C0232i) obj;
                            C0230h.this.f740b.remove(iVar.mo666a());
                            C0230h.this.f740b.put(iVar.mo672b(), iVar);
                            fVar.mo195a(this, false);
                        }
                    }, lVar);
                } else {
                    mo659a(fVar, lVar.mo692a(), lVar);
                }
            } else {
                mo659a(fVar, lVar.mo692a(), lVar);
            }
        } catch (Exception e) {
            C0138bw.m849e("aggregated faild!");
        }
    }

    /* renamed from: a */
    public void mo659a(C0228f fVar, List<String> list, C0235l lVar) {
        C0232i iVar = new C0232i();
        iVar.mo669a(lVar);
        this.f740b.put(list, iVar);
        fVar.mo195a(this, false);
    }

    /* renamed from: a */
    public boolean mo661a(List<?> list) {
        if (this.f740b == null || !this.f740b.containsKey(list)) {
            return false;
        }
        return true;
    }

    /* renamed from: a */
    public void mo656a(C0228f fVar) {
        for (List next : this.f740b.keySet()) {
            if (!fVar.mo653a()) {
                fVar.mo195a(this.f740b.get(next), false);
            } else {
                return;
            }
        }
    }

    /* renamed from: c */
    public int mo664c() {
        if (this.f740b != null) {
            return this.f740b.size();
        }
        return 0;
    }

    /* renamed from: d */
    public void mo665d() {
        this.f740b.clear();
    }

    /* renamed from: a */
    public boolean mo662a(List<String> list, List<String> list2) {
        if (list == null || list.size() == 0) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size() - 1; i++) {
            arrayList.add(C0196d.m1146b(list.get(i)));
        }
        if (list == null || list.size() == 0) {
            return false;
        }
        return arrayList.contains(list2);
    }

    /* renamed from: a */
    public void mo658a(C0228f fVar, C0235l lVar, List<String> list, List<String> list2) {
        while (list.size() >= 1) {
            try {
                if (list.size() == 1) {
                    if (!mo662a(list2, list)) {
                        fVar.mo195a(false, false);
                        return;
                    } else {
                        m1246a(fVar, lVar, list);
                        return;
                    }
                } else if (mo662a(list2, list)) {
                    m1246a(fVar, lVar, list);
                    return;
                } else {
                    list.remove(list.size() - 1);
                }
            } catch (Exception e) {
                C0138bw.m849e("overFlowAggregated faild");
                return;
            }
        }
    }

    /* renamed from: a */
    private void m1246a(C0228f fVar, C0235l lVar, List<String> list) {
        if (mo661a((List<?>) list)) {
            mo657a(fVar, lVar);
        } else {
            mo659a(fVar, list, lVar);
        }
    }
}
