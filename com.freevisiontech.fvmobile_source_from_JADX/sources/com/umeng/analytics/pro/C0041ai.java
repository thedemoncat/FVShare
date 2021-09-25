package com.umeng.analytics.pro;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;

/* renamed from: com.umeng.analytics.pro.ai */
/* compiled from: OldUMIDTracker */
public class C0041ai extends C0282y {

    /* renamed from: a */
    private static final String f171a = "oldumid";

    /* renamed from: b */
    private Context f172b;

    /* renamed from: c */
    private String f173c = null;

    /* renamed from: d */
    private String f174d = null;

    public C0041ai(Context context) {
        super(f171a);
        this.f172b = context;
    }

    /* renamed from: f */
    public String mo122f() {
        return this.f173c;
    }

    /* renamed from: g */
    public boolean mo161g() {
        return mo162h();
    }

    /* renamed from: h */
    public boolean mo162h() {
        this.f174d = C0037af.m144a(this.f172b).mo140b().mo160g((String) null);
        if (!TextUtils.isEmpty(this.f174d)) {
            this.f174d = C0133br.m760c(this.f174d);
            String b = C0136bu.m818b(new File("/sdcard/Android/data/.um/sysid.dat"));
            String b2 = C0136bu.m818b(new File("/sdcard/Android/obj/.um/sysid.dat"));
            String b3 = C0136bu.m818b(new File("/data/local/tmp/.um/sysid.dat"));
            if (TextUtils.isEmpty(b)) {
                m181l();
            } else if (!this.f174d.equals(b)) {
                this.f173c = b;
                return true;
            }
            if (TextUtils.isEmpty(b2)) {
                m180k();
            } else if (!this.f174d.equals(b2)) {
                this.f173c = b2;
                return true;
            }
            if (TextUtils.isEmpty(b3)) {
                m179j();
            } else if (!this.f174d.equals(b3)) {
                this.f173c = b3;
                return true;
            }
        }
        return false;
    }

    /* renamed from: i */
    public void mo163i() {
        try {
            m181l();
            m180k();
            m179j();
        } catch (Exception e) {
        }
    }

    /* renamed from: j */
    private void m179j() {
        try {
            m178b("/data/local/tmp/.um");
            C0136bu.m815a(new File("/data/local/tmp/.um/sysid.dat"), this.f174d);
        } catch (Throwable th) {
        }
    }

    /* renamed from: k */
    private void m180k() {
        try {
            m178b("/sdcard/Android/obj/.um");
            C0136bu.m815a(new File("/sdcard/Android/obj/.um/sysid.dat"), this.f174d);
        } catch (Throwable th) {
        }
    }

    /* renamed from: l */
    private void m181l() {
        try {
            m178b("/sdcard/Android/data/.um");
            C0136bu.m815a(new File("/sdcard/Android/data/.um/sysid.dat"), this.f174d);
        } catch (Throwable th) {
        }
    }

    /* renamed from: b */
    private void m178b(String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
