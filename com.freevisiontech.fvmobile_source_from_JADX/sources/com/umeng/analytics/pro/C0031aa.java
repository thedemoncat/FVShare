package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;
import com.p007ny.ijk.upplayer.BuildConfig;
import com.umeng.analytics.AnalyticsConfig;
import java.io.File;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.aa */
/* compiled from: Envelope */
public class C0031aa {

    /* renamed from: a */
    private final byte[] f115a = {0, 0, 0, 0, 0, 0, 0, 0};

    /* renamed from: b */
    private final int f116b = 1;

    /* renamed from: c */
    private final int f117c = 0;

    /* renamed from: d */
    private String f118d = BuildConfig.VERSION_NAME;

    /* renamed from: e */
    private String f119e = null;

    /* renamed from: f */
    private byte[] f120f = null;

    /* renamed from: g */
    private byte[] f121g = null;

    /* renamed from: h */
    private byte[] f122h = null;

    /* renamed from: i */
    private int f123i = 0;

    /* renamed from: j */
    private int f124j = 0;

    /* renamed from: k */
    private int f125k = 0;

    /* renamed from: l */
    private byte[] f126l = null;

    /* renamed from: m */
    private byte[] f127m = null;

    /* renamed from: n */
    private boolean f128n = false;

    private C0031aa(byte[] bArr, String str, byte[] bArr2) throws Exception {
        if (bArr == null || bArr.length == 0) {
            throw new Exception("entity is null or empty");
        }
        this.f119e = str;
        this.f125k = bArr.length;
        this.f126l = C0134bs.m764a(bArr);
        this.f124j = (int) (System.currentTimeMillis() / 1000);
        this.f127m = bArr2;
    }

    /* renamed from: a */
    public static String m112a(Context context) {
        SharedPreferences a = C0067az.m285a(context);
        if (a == null) {
            return null;
        }
        return a.getString("signature", (String) null);
    }

    /* renamed from: a */
    public void mo116a(String str) {
        this.f120f = C0133br.m755a(str);
    }

    /* renamed from: a */
    public String mo114a() {
        return C0133br.m753a(this.f120f);
    }

    /* renamed from: a */
    public void mo115a(int i) {
        this.f123i = i;
    }

    /* renamed from: a */
    public void mo117a(boolean z) {
        this.f128n = z;
    }

    /* renamed from: a */
    public static C0031aa m111a(Context context, String str, byte[] bArr) {
        try {
            String q = C0135bt.m801q(context);
            String c = C0135bt.m784c(context);
            SharedPreferences a = C0067az.m285a(context);
            String string = a.getString("signature", (String) null);
            int i = a.getInt("serial", 1);
            C0031aa aaVar = new C0031aa(bArr, str, (c + q).getBytes());
            aaVar.mo116a(string);
            aaVar.mo115a(i);
            aaVar.mo118b();
            a.edit().putInt("serial", i + 1).putString("signature", aaVar.mo114a()).commit();
            aaVar.mo119b(context);
            return aaVar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: b */
    public static C0031aa m114b(Context context, String str, byte[] bArr) {
        try {
            String q = C0135bt.m801q(context);
            String c = C0135bt.m784c(context);
            SharedPreferences a = C0067az.m285a(context);
            String string = a.getString("signature", (String) null);
            int i = a.getInt("serial", 1);
            C0031aa aaVar = new C0031aa(bArr, str, (c + q).getBytes());
            aaVar.mo117a(true);
            aaVar.mo116a(string);
            aaVar.mo115a(i);
            aaVar.mo118b();
            a.edit().putInt("serial", i + 1).putString("signature", aaVar.mo114a()).commit();
            aaVar.mo119b(context);
            return aaVar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: b */
    public void mo118b() {
        if (this.f120f == null) {
            this.f120f = m115d();
        }
        if (this.f128n) {
            byte[] bArr = new byte[16];
            try {
                System.arraycopy(this.f120f, 1, bArr, 0, 16);
                this.f126l = C0133br.m756a(this.f126l, bArr);
            } catch (Exception e) {
            }
        }
        this.f121g = m113a(this.f120f, this.f124j);
        this.f122h = m116e();
    }

    /* renamed from: a */
    private byte[] m113a(byte[] bArr, int i) {
        byte[] b = C0133br.m758b(this.f127m);
        byte[] b2 = C0133br.m758b(this.f126l);
        int length = b.length;
        byte[] bArr2 = new byte[(length * 2)];
        for (int i2 = 0; i2 < length; i2++) {
            bArr2[i2 * 2] = b2[i2];
            bArr2[(i2 * 2) + 1] = b[i2];
        }
        for (int i3 = 0; i3 < 2; i3++) {
            bArr2[i3] = bArr[i3];
            bArr2[(bArr2.length - i3) - 1] = bArr[(bArr.length - i3) - 1];
        }
        byte[] bArr3 = {(byte) (i & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 16) & 255), (byte) (i >>> 24)};
        for (int i4 = 0; i4 < bArr2.length; i4++) {
            bArr2[i4] = (byte) (bArr2[i4] ^ bArr3[i4 % 4]);
        }
        return bArr2;
    }

    /* renamed from: d */
    private byte[] m115d() {
        return m113a(this.f115a, (int) (System.currentTimeMillis() / 1000));
    }

    /* renamed from: e */
    private byte[] m116e() {
        return C0133br.m758b((C0133br.m753a(this.f120f) + this.f123i + this.f124j + this.f125k + C0133br.m753a(this.f121g)).getBytes());
    }

    /* renamed from: c */
    public byte[] mo120c() {
        C0120bo boVar = new C0120bo();
        boVar.mo432a(this.f118d);
        boVar.mo436b(this.f119e);
        boVar.mo439c(C0133br.m753a(this.f120f));
        boVar.mo431a(this.f123i);
        boVar.mo438c(this.f124j);
        boVar.mo442d(this.f125k);
        boVar.mo434a(this.f126l);
        boVar.mo446e(this.f128n ? 1 : 0);
        boVar.mo443d(C0133br.m753a(this.f121g));
        boVar.mo447e(C0133br.m753a(this.f122h));
        try {
            return new C0175cn().mo544a(boVar);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: b */
    public void mo119b(Context context) {
        String str = this.f119e;
        String g = C0037af.m144a(context).mo140b().mo160g((String) null);
        String a = C0133br.m753a(this.f120f);
        byte[] bArr = new byte[16];
        System.arraycopy(this.f120f, 2, bArr, 0, 16);
        String a2 = C0133br.m753a(C0133br.m758b(bArr));
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(C0281x.f901a, str);
            if (g != null) {
                jSONObject.put("umid", g);
            }
            jSONObject.put("signature", a);
            jSONObject.put("checksum", a2);
            File file = new File(context.getFilesDir(), ".umeng");
            if (!file.exists()) {
                file.mkdir();
            }
            C0136bu.m815a(new File(file, "exchangeIdentity.json"), jSONObject.toString());
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(C0281x.f901a, str);
            jSONObject2.put(C0281x.f945b, AnalyticsConfig.getChannel(context));
            if (g != null) {
                jSONObject2.put("umid", C0136bu.m819b(g));
            }
            C0136bu.m815a(new File(context.getFilesDir(), "exid.dat"), jSONObject2.toString());
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    public String toString() {
        int i = 1;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("version : %s\n", new Object[]{this.f118d}));
        sb.append(String.format("address : %s\n", new Object[]{this.f119e}));
        sb.append(String.format("signature : %s\n", new Object[]{C0133br.m753a(this.f120f)}));
        sb.append(String.format("serial : %s\n", new Object[]{Integer.valueOf(this.f123i)}));
        sb.append(String.format("timestamp : %d\n", new Object[]{Integer.valueOf(this.f124j)}));
        sb.append(String.format("length : %d\n", new Object[]{Integer.valueOf(this.f125k)}));
        sb.append(String.format("guid : %s\n", new Object[]{C0133br.m753a(this.f121g)}));
        sb.append(String.format("checksum : %s ", new Object[]{C0133br.m753a(this.f122h)}));
        Object[] objArr = new Object[1];
        if (!this.f128n) {
            i = 0;
        }
        objArr[0] = Integer.valueOf(i);
        sb.append(String.format("codex : %d", objArr));
        return sb.toString();
    }
}
