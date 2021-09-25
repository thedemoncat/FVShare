package com.umeng.analytics.pro;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* renamed from: com.umeng.analytics.pro.am */
/* compiled from: UTDIdTracker */
public class C0045am extends C0282y {

    /* renamed from: a */
    private static final String f180a = "utdid";

    /* renamed from: b */
    private static final String f181b = "android.permission.WRITE_EXTERNAL_STORAGE";

    /* renamed from: c */
    private static final Pattern f182c = Pattern.compile("UTDID\">([^<]+)");

    /* renamed from: d */
    private Context f183d;

    public C0045am(Context context) {
        super(f180a);
        this.f183d = context;
    }

    /* renamed from: f */
    public String mo122f() {
        try {
            return (String) Class.forName("com.ut.device.UTDevice").getMethod("getUtdid", new Class[]{Context.class}).invoke((Object) null, new Object[]{this.f183d});
        } catch (Exception e) {
            return m190g();
        }
    }

    /* renamed from: g */
    private String m190g() {
        FileInputStream fileInputStream;
        File h = m191h();
        if (h == null || !h.exists()) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(h);
            String b = m189b(C0136bu.m812a((InputStream) fileInputStream));
            C0136bu.m821c(fileInputStream);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } catch (Throwable th) {
            C0136bu.m821c(fileInputStream);
            throw th;
        }
    }

    /* renamed from: b */
    private String m189b(String str) {
        if (str == null) {
            return null;
        }
        Matcher matcher = f182c.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /* renamed from: h */
    private File m191h() {
        if (!C0135bt.m778a(this.f183d, f181b) || !Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        try {
            return new File(Environment.getExternalStorageDirectory().getCanonicalPath(), ".UTSystemConfig/Global/Alvin2.xml");
        } catch (Exception e) {
            return null;
        }
    }
}
