package com.umeng.analytics.pro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.umeng.analytics.pro.i */
/* compiled from: UMCCAggregatedObject */
public class C0232i implements Serializable {

    /* renamed from: a */
    private static final long f744a = 1;

    /* renamed from: b */
    private List<String> f745b = new ArrayList();

    /* renamed from: c */
    private List<String> f746c = new ArrayList();

    /* renamed from: d */
    private long f747d = 0;

    /* renamed from: e */
    private long f748e = 0;

    /* renamed from: f */
    private long f749f = 0;

    /* renamed from: g */
    private String f750g = null;

    public C0232i() {
    }

    public C0232i(List<String> list, long j, long j2, long j3, List<String> list2, String str) {
        this.f745b = list;
        this.f746c = list2;
        this.f747d = j;
        this.f748e = j2;
        this.f749f = j3;
        this.f750g = str;
    }

    /* renamed from: a */
    public void mo670a(String str) {
        try {
            if (this.f746c.size() < C0253n.m1345a().mo711b()) {
                this.f746c.add(str);
            } else {
                this.f746c.remove(this.f746c.get(0));
                this.f746c.add(str);
            }
            if (this.f746c.size() > C0253n.m1345a().mo711b()) {
                for (int i = 0; i < this.f746c.size() - C0253n.m1345a().mo711b(); i++) {
                    this.f746c.remove(this.f746c.get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: a */
    public void mo668a(C0228f fVar, C0235l lVar) {
        mo670a(lVar.mo693b());
        this.f749f++;
        this.f748e += lVar.mo694c();
        this.f747d += lVar.mo695d();
        fVar.mo195a(this, false);
    }

    /* renamed from: a */
    public void mo669a(C0235l lVar) {
        this.f749f = 1;
        this.f745b = lVar.mo692a();
        mo670a(lVar.mo693b());
        this.f748e = lVar.mo694c();
        this.f747d = System.currentTimeMillis();
        this.f750g = C0260q.m1377a(System.currentTimeMillis());
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[key: ").append(this.f745b).append("] [label: ").append(this.f746c).append("][ totalTimeStamp").append(this.f750g).append("][ value").append(this.f748e).append("][ count").append(this.f749f).append("][ timeWindowNum").append(this.f750g).append("]");
        return stringBuffer.toString();
    }

    /* renamed from: a */
    public String mo666a() {
        return C0196d.m1144a(this.f745b);
    }

    /* renamed from: b */
    public List<String> mo672b() {
        return this.f745b;
    }

    /* renamed from: c */
    public String mo676c() {
        return C0196d.m1144a(this.f746c);
    }

    /* renamed from: d */
    public List<String> mo678d() {
        return this.f746c;
    }

    /* renamed from: e */
    public long mo679e() {
        return this.f747d;
    }

    /* renamed from: f */
    public long mo680f() {
        return this.f748e;
    }

    /* renamed from: g */
    public long mo681g() {
        return this.f749f;
    }

    /* renamed from: h */
    public String mo682h() {
        return this.f750g;
    }

    /* renamed from: a */
    public void mo671a(List<String> list) {
        this.f745b = list;
    }

    /* renamed from: b */
    public void mo675b(List<String> list) {
        this.f746c = list;
    }

    /* renamed from: a */
    public void mo667a(long j) {
        this.f747d = j;
    }

    /* renamed from: b */
    public void mo673b(long j) {
        this.f748e = j;
    }

    /* renamed from: c */
    public void mo677c(long j) {
        this.f749f = j;
    }

    /* renamed from: b */
    public void mo674b(String str) {
        this.f750g = str;
    }
}
