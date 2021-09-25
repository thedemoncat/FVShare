package com.umeng.analytics.pro;

import android.text.TextUtils;
import java.util.List;

/* renamed from: com.umeng.analytics.pro.n */
/* compiled from: UMCCAggregatedRestrictionManager */
public class C0253n {

    /* renamed from: com.umeng.analytics.pro.n$a */
    /* compiled from: UMCCAggregatedRestrictionManager */
    private static class C0255a {
        /* access modifiers changed from: private */

        /* renamed from: a */
        public static final C0253n f803a = new C0253n();

        private C0255a() {
        }
    }

    private C0253n() {
    }

    /* renamed from: a */
    public static C0253n m1345a() {
        return C0255a.f803a;
    }

    /* renamed from: a */
    public boolean mo709a(String str) {
        if ("".equals(str)) {
            return true;
        }
        if (str == null) {
            return false;
        }
        if (str.getBytes().length >= 160 || !m1346a(str, 48)) {
            return false;
        }
        return true;
    }

    /* renamed from: b */
    public boolean mo712b(String str) {
        if (!TextUtils.isEmpty(str) && str.length() < 16 && m1346a(str, 48)) {
            return true;
        }
        return false;
    }

    /* renamed from: a */
    public boolean mo710a(List<String> list) {
        if (list == null) {
            return false;
        }
        if (list.size() <= 1) {
            return true;
        }
        for (int i = 1; i < list.size(); i++) {
            if (TextUtils.isEmpty(list.get(i))) {
                return false;
            }
            if (!m1346a(list.get(i), 48)) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: a */
    private boolean m1346a(String str, int i) {
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) < i) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: b */
    public int mo711b() {
        return 8;
    }

    /* renamed from: c */
    public int mo714c() {
        return 128;
    }

    /* renamed from: d */
    public int mo715d() {
        return 512;
    }

    /* renamed from: b */
    public boolean mo713b(List<String> list) {
        if (list == null || list.size() <= 0) {
            return false;
        }
        int i = 0;
        for (String bytes : list) {
            i = bytes.getBytes().length + i;
        }
        if (i < 256) {
            return true;
        }
        return false;
    }
}
