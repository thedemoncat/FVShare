package com.umeng.analytics.pro;

import android.content.Context;
import com.umeng.analytics.C0015a;

/* renamed from: com.umeng.analytics.pro.by */
/* compiled from: ReportPolicy */
public class C0140by {

    /* renamed from: a */
    public static final int f521a = 0;

    /* renamed from: b */
    public static final int f522b = 1;

    /* renamed from: c */
    static final int f523c = 2;

    /* renamed from: d */
    static final int f524d = 3;

    /* renamed from: e */
    public static final int f525e = 4;

    /* renamed from: f */
    public static final int f526f = 5;

    /* renamed from: g */
    public static final int f527g = 6;

    /* renamed from: h */
    public static final int f528h = 8;

    /* renamed from: a */
    public static boolean m861a(int i) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
                return true;
            default:
                return false;
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$h */
    /* compiled from: ReportPolicy */
    public static class C0148h {
        /* renamed from: a */
        public boolean mo490a(boolean z) {
            return true;
        }

        /* renamed from: a */
        public boolean mo491a() {
            return true;
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$g */
    /* compiled from: ReportPolicy */
    public static class C0147g extends C0148h {
        /* renamed from: a */
        public boolean mo490a(boolean z) {
            return true;
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$d */
    /* compiled from: ReportPolicy */
    public static class C0144d extends C0148h {
        /* renamed from: a */
        public boolean mo490a(boolean z) {
            return z;
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$e */
    /* compiled from: ReportPolicy */
    public static class C0145e extends C0148h {

        /* renamed from: a */
        private static long f535a = 90000;

        /* renamed from: b */
        private static long f536b = C0015a.f22i;

        /* renamed from: c */
        private long f537c;

        /* renamed from: d */
        private C0072bc f538d;

        public C0145e(C0072bc bcVar, long j) {
            this.f538d = bcVar;
            mo492a(j);
        }

        /* renamed from: a */
        public boolean mo490a(boolean z) {
            if (System.currentTimeMillis() - this.f538d.f284c >= this.f537c) {
                return true;
            }
            return false;
        }

        /* renamed from: a */
        public void mo492a(long j) {
            if (j < f535a || j > f536b) {
                this.f537c = f535a;
            } else {
                this.f537c = j;
            }
        }

        /* renamed from: b */
        public long mo493b() {
            return this.f537c;
        }

        /* renamed from: a */
        public static boolean m868a(int i) {
            if (((long) i) < f535a) {
                return false;
            }
            return true;
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$f */
    /* compiled from: ReportPolicy */
    public static class C0146f extends C0148h {

        /* renamed from: a */
        private long f539a = C0015a.f22i;

        /* renamed from: b */
        private C0072bc f540b;

        public C0146f(C0072bc bcVar) {
            this.f540b = bcVar;
        }

        /* renamed from: a */
        public boolean mo490a(boolean z) {
            if (System.currentTimeMillis() - this.f540b.f284c >= this.f539a) {
                return true;
            }
            return false;
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$i */
    /* compiled from: ReportPolicy */
    public static class C0149i extends C0148h {

        /* renamed from: a */
        private Context f541a = null;

        public C0149i(Context context) {
            this.f541a = context;
        }

        /* renamed from: a */
        public boolean mo490a(boolean z) {
            return C0135bt.m795k(this.f541a);
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$b */
    /* compiled from: ReportPolicy */
    public static class C0142b extends C0148h {

        /* renamed from: a */
        private C0075bf f531a;

        /* renamed from: b */
        private C0072bc f532b;

        public C0142b(C0072bc bcVar, C0075bf bfVar) {
            this.f532b = bcVar;
            this.f531a = bfVar;
        }

        /* renamed from: a */
        public boolean mo490a(boolean z) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.f532b.f284c >= this.f531a.mo242b()) {
                return true;
            }
            return false;
        }

        /* renamed from: a */
        public boolean mo491a() {
            return this.f531a.mo244d();
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$c */
    /* compiled from: ReportPolicy */
    public static class C0143c extends C0148h {

        /* renamed from: a */
        private long f533a;

        /* renamed from: b */
        private long f534b = 0;

        public C0143c(int i) {
            this.f533a = (long) i;
            this.f534b = System.currentTimeMillis();
        }

        /* renamed from: a */
        public boolean mo490a(boolean z) {
            if (System.currentTimeMillis() - this.f534b >= this.f533a) {
                return true;
            }
            return false;
        }

        /* renamed from: a */
        public boolean mo491a() {
            return System.currentTimeMillis() - this.f534b < this.f533a;
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$j */
    /* compiled from: ReportPolicy */
    public static class C0150j extends C0148h {

        /* renamed from: a */
        private final long f542a = 10800000;

        /* renamed from: b */
        private C0072bc f543b;

        public C0150j(C0072bc bcVar) {
            this.f543b = bcVar;
        }

        /* renamed from: a */
        public boolean mo490a(boolean z) {
            if (System.currentTimeMillis() - this.f543b.f284c >= 10800000) {
                return true;
            }
            return false;
        }
    }

    /* renamed from: com.umeng.analytics.pro.by$a */
    /* compiled from: ReportPolicy */
    public static class C0141a extends C0148h {

        /* renamed from: a */
        private final long f529a = 15000;

        /* renamed from: b */
        private C0072bc f530b;

        public C0141a(C0072bc bcVar) {
            this.f530b = bcVar;
        }

        /* renamed from: a */
        public boolean mo490a(boolean z) {
            if (System.currentTimeMillis() - this.f530b.f284c >= 15000) {
                return true;
            }
            return false;
        }
    }
}
