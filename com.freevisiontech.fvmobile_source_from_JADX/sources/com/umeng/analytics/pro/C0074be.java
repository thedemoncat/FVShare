package com.umeng.analytics.pro;

import android.content.Context;
import android.text.TextUtils;
import com.umeng.analytics.pro.C0037af;

/* renamed from: com.umeng.analytics.pro.be */
/* compiled from: ABTest */
public class C0074be implements C0066ay {

    /* renamed from: i */
    private static C0074be f292i = null;

    /* renamed from: a */
    private boolean f293a = false;

    /* renamed from: b */
    private int f294b = -1;

    /* renamed from: c */
    private int f295c = -1;

    /* renamed from: d */
    private int f296d = -1;

    /* renamed from: e */
    private float f297e = 0.0f;

    /* renamed from: f */
    private float f298f = 0.0f;

    /* renamed from: g */
    private String f299g = null;

    /* renamed from: h */
    private Context f300h = null;

    /* renamed from: a */
    public static synchronized C0074be m336a(Context context) {
        C0074be beVar;
        synchronized (C0074be.class) {
            if (f292i == null) {
                C0037af.C0038a b = C0037af.m144a(context).mo140b();
                f292i = new C0074be(context, b.mo159f((String) null), b.mo156d(0));
            }
            beVar = f292i;
        }
        return beVar;
    }

    private C0074be(Context context, String str, int i) {
        this.f300h = context;
        mo231a(str, i);
    }

    /* renamed from: b */
    private float m338b(String str, int i) {
        int i2 = i * 2;
        if (str == null) {
            return 0.0f;
        }
        return ((float) Integer.valueOf(str.substring(i2, i2 + 5), 16).intValue()) / 1048576.0f;
    }

    /* renamed from: a */
    public void mo231a(String str, int i) {
        this.f295c = i;
        String a = C0031aa.m112a(this.f300h);
        if (TextUtils.isEmpty(a) || TextUtils.isEmpty(str)) {
            this.f293a = false;
            return;
        }
        try {
            this.f297e = m338b(a, 12);
            this.f298f = m338b(a, 6);
            if (str.startsWith("SIG7")) {
                m339b(str);
            } else if (str.startsWith("FIXED")) {
                m340c(str);
            }
        } catch (Exception e) {
            this.f293a = false;
            C0138bw.m851e("v:" + str, (Throwable) e);
        }
    }

    /* renamed from: a */
    public static boolean m337a(String str) {
        int parseInt;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String[] split = str.split("\\|");
        if (split.length != 6) {
            return false;
        }
        if (split[0].startsWith("SIG7") && split[1].split(",").length == split[5].split(",").length) {
            return true;
        }
        if (!split[0].startsWith("FIXED") || split[5].split(",").length < (parseInt = Integer.parseInt(split[1])) || parseInt < 1) {
            return false;
        }
        return true;
    }

    /* renamed from: b */
    private void m339b(String str) {
        float f;
        if (str != null) {
            String[] split = str.split("\\|");
            if (split[2].equals("SIG13")) {
                f = Float.valueOf(split[3]).floatValue();
            } else {
                f = 0.0f;
            }
            if (this.f297e > f) {
                this.f293a = false;
                return;
            }
            float[] fArr = null;
            if (split[0].equals("SIG7")) {
                String[] split2 = split[1].split(",");
                float[] fArr2 = new float[split2.length];
                for (int i = 0; i < split2.length; i++) {
                    fArr2[i] = Float.valueOf(split2[i]).floatValue();
                }
                fArr = fArr2;
            }
            int[] iArr = null;
            if (split[4].equals("RPT")) {
                this.f299g = "RPT";
                String[] split3 = split[5].split(",");
                int[] iArr2 = new int[split3.length];
                for (int i2 = 0; i2 < split3.length; i2++) {
                    iArr2[i2] = Integer.valueOf(split3[i2]).intValue();
                }
                iArr = iArr2;
            } else if (split[4].equals("DOM")) {
                this.f293a = true;
                this.f299g = "DOM";
                try {
                    String[] split4 = split[5].split(",");
                    iArr = new int[split4.length];
                    for (int i3 = 0; i3 < split4.length; i3++) {
                        iArr[i3] = Integer.valueOf(split4[i3]).intValue();
                    }
                } catch (Exception e) {
                }
            }
            float f2 = 0.0f;
            int i4 = 0;
            while (true) {
                if (i4 >= fArr.length) {
                    i4 = -1;
                    break;
                }
                f2 += fArr[i4];
                if (this.f298f < f2) {
                    break;
                }
                i4++;
            }
            if (i4 != -1) {
                this.f293a = true;
                this.f296d = i4 + 1;
                if (iArr != null) {
                    this.f294b = iArr[i4];
                    return;
                }
                return;
            }
            this.f293a = false;
        }
    }

    /* renamed from: c */
    private void m340c(String str) {
        int i;
        if (str != null) {
            String[] split = str.split("\\|");
            float f = 0.0f;
            if (split[2].equals("SIG13")) {
                f = Float.valueOf(split[3]).floatValue();
            }
            if (this.f297e > f) {
                this.f293a = false;
                return;
            }
            if (split[0].equals("FIXED")) {
                i = Integer.valueOf(split[1]).intValue();
            } else {
                i = -1;
            }
            int[] iArr = null;
            if (split[4].equals("RPT")) {
                this.f299g = "RPT";
                String[] split2 = split[5].split(",");
                int[] iArr2 = new int[split2.length];
                for (int i2 = 0; i2 < split2.length; i2++) {
                    iArr2[i2] = Integer.valueOf(split2[i2]).intValue();
                }
                iArr = iArr2;
            } else if (split[4].equals("DOM")) {
                this.f299g = "DOM";
                this.f293a = true;
                try {
                    String[] split3 = split[5].split(",");
                    iArr = new int[split3.length];
                    for (int i3 = 0; i3 < split3.length; i3++) {
                        iArr[i3] = Integer.valueOf(split3[i3]).intValue();
                    }
                } catch (Exception e) {
                }
            }
            if (i != -1) {
                this.f293a = true;
                this.f296d = i;
                if (iArr != null) {
                    this.f294b = iArr[i - 1];
                    return;
                }
                return;
            }
            this.f293a = false;
        }
    }

    /* renamed from: a */
    public boolean mo232a() {
        return this.f293a;
    }

    /* renamed from: b */
    public int mo233b() {
        return this.f294b;
    }

    /* renamed from: c */
    public int mo234c() {
        return this.f295c;
    }

    /* renamed from: d */
    public int mo235d() {
        return this.f296d;
    }

    /* renamed from: e */
    public String mo236e() {
        if (!this.f293a) {
            return "error";
        }
        return String.valueOf(this.f296d);
    }

    /* renamed from: f */
    public String mo237f() {
        return this.f299g;
    }

    /* renamed from: a */
    public void mo177a(C0037af.C0038a aVar) {
        mo231a(aVar.mo159f((String) null), aVar.mo156d(0));
    }

    public String toString() {
        return " p13:" + this.f297e + " p07:" + this.f298f + " policy:" + this.f294b + " interval:" + this.f295c;
    }
}
