package com.umeng.analytics.pro;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* renamed from: com.umeng.analytics.pro.bx */
/* compiled from: QueuedWork */
public class C0139bx {

    /* renamed from: a */
    private static List<WeakReference<ScheduledFuture<?>>> f517a = new ArrayList();

    /* renamed from: b */
    private static ExecutorService f518b = Executors.newSingleThreadExecutor();

    /* renamed from: c */
    private static long f519c = 5;

    /* renamed from: d */
    private static ScheduledExecutorService f520d = Executors.newSingleThreadScheduledExecutor();

    /* renamed from: a */
    public static void m857a(Runnable runnable) {
        if (f518b.isShutdown()) {
            f518b = Executors.newSingleThreadExecutor();
        }
        f518b.execute(runnable);
    }

    /* renamed from: a */
    public static void m856a() {
        try {
            for (WeakReference<ScheduledFuture<?>> weakReference : f517a) {
                ScheduledFuture scheduledFuture = (ScheduledFuture) weakReference.get();
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
            }
            f517a.clear();
            if (!f518b.isShutdown()) {
                f518b.shutdown();
            }
            if (!f520d.isShutdown()) {
                f520d.shutdown();
            }
            f518b.awaitTermination(f519c, TimeUnit.SECONDS);
            f520d.awaitTermination(f519c, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
    }

    /* renamed from: b */
    public static synchronized void m859b(Runnable runnable) {
        synchronized (C0139bx.class) {
            if (f520d.isShutdown()) {
                f520d = Executors.newSingleThreadScheduledExecutor();
            }
            f520d.execute(runnable);
        }
    }

    /* renamed from: a */
    public static synchronized void m858a(Runnable runnable, long j) {
        synchronized (C0139bx.class) {
            if (f520d.isShutdown()) {
                f520d = Executors.newSingleThreadScheduledExecutor();
            }
            f517a.add(new WeakReference(f520d.schedule(runnable, j, TimeUnit.MILLISECONDS)));
        }
    }

    /* renamed from: c */
    public static synchronized void m860c(Runnable runnable) {
        synchronized (C0139bx.class) {
            if (f520d.isShutdown()) {
                f520d = Executors.newSingleThreadScheduledExecutor();
            }
            try {
                f520d.submit(runnable).get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
            }
        }
    }
}
