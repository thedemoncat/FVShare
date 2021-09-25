package com.umeng.analytics.pro;

import com.umeng.analytics.AnalyticsConfig;
import java.lang.Thread;

/* renamed from: com.umeng.analytics.pro.ar */
/* compiled from: CrashHandler */
public class C0057ar implements Thread.UncaughtExceptionHandler {

    /* renamed from: a */
    private Thread.UncaughtExceptionHandler f231a;

    /* renamed from: b */
    private C0065ax f232b;

    public C0057ar() {
        if (Thread.getDefaultUncaughtExceptionHandler() != this) {
            this.f231a = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    /* renamed from: a */
    public void mo187a(C0065ax axVar) {
        this.f232b = axVar;
    }

    public void uncaughtException(Thread thread, Throwable th) {
        m246a(th);
        if (this.f231a != null && this.f231a != Thread.getDefaultUncaughtExceptionHandler()) {
            this.f231a.uncaughtException(thread, th);
        }
    }

    /* renamed from: a */
    private void m246a(Throwable th) {
        if (AnalyticsConfig.CATCH_EXCEPTION) {
            this.f232b.mo68a(th);
        } else {
            this.f232b.mo68a((Throwable) null);
        }
    }
}
