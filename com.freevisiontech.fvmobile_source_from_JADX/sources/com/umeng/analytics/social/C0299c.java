package com.umeng.analytics.social;

/* renamed from: com.umeng.analytics.social.c */
/* compiled from: UMResult */
public class C0299c {

    /* renamed from: a */
    private int f991a = -1;

    /* renamed from: b */
    private String f992b = "";

    /* renamed from: c */
    private String f993c = "";

    /* renamed from: d */
    private Exception f994d = null;

    public C0299c(int i) {
        this.f991a = i;
    }

    public C0299c(int i, Exception exc) {
        this.f991a = i;
        this.f994d = exc;
    }

    /* renamed from: a */
    public Exception mo785a() {
        return this.f994d;
    }

    /* renamed from: b */
    public int mo788b() {
        return this.f991a;
    }

    /* renamed from: a */
    public void mo786a(int i) {
        this.f991a = i;
    }

    /* renamed from: c */
    public String mo790c() {
        return this.f992b;
    }

    /* renamed from: a */
    public void mo787a(String str) {
        this.f992b = str;
    }

    /* renamed from: d */
    public String mo791d() {
        return this.f993c;
    }

    /* renamed from: b */
    public void mo789b(String str) {
        this.f993c = str;
    }

    public String toString() {
        return "status=" + this.f991a + "\r\n" + "msg:  " + this.f992b + "\r\n" + "data:  " + this.f993c;
    }
}
