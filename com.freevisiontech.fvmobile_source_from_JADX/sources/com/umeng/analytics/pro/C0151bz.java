package com.umeng.analytics.pro;

/* renamed from: com.umeng.analytics.pro.bz */
/* compiled from: SafeRunnable */
public abstract class C0151bz implements Runnable {
    /* renamed from: a */
    public abstract void mo87a();

    public void run() {
        try {
            mo87a();
        } catch (Throwable th) {
            if (th != null) {
                th.printStackTrace();
            }
        }
    }
}
