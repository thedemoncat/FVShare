package com.umeng.analytics.pro;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.C0015a;
import com.umeng.analytics.MobclickAgent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import javax.microedition.khronos.opengles.GL10;
import org.slf4j.Marker;

/* renamed from: com.umeng.analytics.pro.bt */
/* compiled from: DeviceConfig */
public class C0135bt {

    /* renamed from: a */
    protected static final String f501a = C0135bt.class.getName();

    /* renamed from: b */
    public static final String f502b = "";

    /* renamed from: c */
    public static final String f503c = "2G/3G";

    /* renamed from: d */
    public static final String f504d = "Wi-Fi";

    /* renamed from: e */
    public static final int f505e = 8;

    /* renamed from: f */
    private static final String f506f = "ro.miui.ui.version.name";

    /* renamed from: a */
    public static String m774a(Context context) {
        try {
            return String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /* renamed from: b */
    public static String m781b(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /* renamed from: a */
    public static boolean m778a(Context context, String str) {
        boolean z;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                if (((Integer) Class.forName("android.content.Context").getMethod("checkSelfPermission", new Class[]{String.class}).invoke(context, new Object[]{str})).intValue() == 0) {
                    z = true;
                } else {
                    z = false;
                }
                return z;
            } catch (Throwable th) {
                return false;
            }
        } else if (context.getPackageManager().checkPermission(str, context.getPackageName()) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /* renamed from: a */
    public static String[] m779a(GL10 gl10) {
        try {
            return new String[]{gl10.glGetString(7936), gl10.glGetString(7937)};
        } catch (Throwable th) {
            return new String[0];
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x000c A[Catch:{ Throwable -> 0x0075 }] */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String m780b() {
        /*
            r1 = 0
            r2 = 0
            java.util.Enumeration r3 = java.net.NetworkInterface.getNetworkInterfaces()     // Catch:{ Throwable -> 0x0075 }
        L_0x0006:
            boolean r0 = r3.hasMoreElements()     // Catch:{ Throwable -> 0x0075 }
            if (r0 == 0) goto L_0x0076
            java.lang.Object r0 = r3.nextElement()     // Catch:{ Throwable -> 0x0075 }
            java.net.NetworkInterface r0 = (java.net.NetworkInterface) r0     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r4 = "wlan0"
            java.lang.String r5 = r0.getName()     // Catch:{ Throwable -> 0x0075 }
            boolean r4 = r4.equals(r5)     // Catch:{ Throwable -> 0x0075 }
            if (r4 != 0) goto L_0x002c
            java.lang.String r4 = "eth0"
            java.lang.String r5 = r0.getName()     // Catch:{ Throwable -> 0x0075 }
            boolean r4 = r4.equals(r5)     // Catch:{ Throwable -> 0x0075 }
            if (r4 == 0) goto L_0x0006
        L_0x002c:
            byte[] r3 = r0.getHardwareAddress()     // Catch:{ Throwable -> 0x0075 }
            if (r3 == 0) goto L_0x0035
            int r0 = r3.length     // Catch:{ Throwable -> 0x0075 }
            if (r0 != 0) goto L_0x0037
        L_0x0035:
            r0 = r1
        L_0x0036:
            return r0
        L_0x0037:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0075 }
            r4.<init>()     // Catch:{ Throwable -> 0x0075 }
            int r5 = r3.length     // Catch:{ Throwable -> 0x0075 }
            r0 = r2
        L_0x003e:
            if (r0 >= r5) goto L_0x0059
            byte r2 = r3[r0]     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r6 = "%02X:"
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x0075 }
            r8 = 0
            java.lang.Byte r2 = java.lang.Byte.valueOf(r2)     // Catch:{ Throwable -> 0x0075 }
            r7[r8] = r2     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r2 = java.lang.String.format(r6, r7)     // Catch:{ Throwable -> 0x0075 }
            r4.append(r2)     // Catch:{ Throwable -> 0x0075 }
            int r0 = r0 + 1
            goto L_0x003e
        L_0x0059:
            int r0 = r4.length()     // Catch:{ Throwable -> 0x0075 }
            if (r0 <= 0) goto L_0x0068
            int r0 = r4.length()     // Catch:{ Throwable -> 0x0075 }
            int r0 = r0 + -1
            r4.deleteCharAt(r0)     // Catch:{ Throwable -> 0x0075 }
        L_0x0068:
            java.lang.String r0 = r4.toString()     // Catch:{ Throwable -> 0x0075 }
            java.util.Locale r2 = java.util.Locale.getDefault()     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r0 = r0.toLowerCase(r2)     // Catch:{ Throwable -> 0x0075 }
            goto L_0x0036
        L_0x0075:
            r0 = move-exception
        L_0x0076:
            r0 = r1
            goto L_0x0036
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0135bt.m780b():java.lang.String");
    }

    /* renamed from: c */
    private static String m783c() {
        int i = 0;
        try {
            String[] strArr = {"/sys/class/net/wlan0/address", "/sys/class/net/eth0/address", "/sys/devices/virtual/net/wlan0/address"};
            while (true) {
                int i2 = i;
                if (i2 >= strArr.length) {
                    break;
                }
                try {
                    String a = m775a(strArr[i2]);
                    if (a != null) {
                        return a;
                    }
                    i = i2 + 1;
                } catch (Throwable th) {
                }
            }
        } catch (Throwable th2) {
        }
        return null;
    }

    /* renamed from: a */
    private static String m775a(String str) {
        BufferedReader bufferedReader;
        String str2 = null;
        try {
            FileReader fileReader = new FileReader(str);
            if (fileReader != null) {
                try {
                    bufferedReader = new BufferedReader(fileReader, 1024);
                    try {
                        str2 = bufferedReader.readLine();
                        if (fileReader != null) {
                            try {
                                fileReader.close();
                            } catch (Throwable th) {
                            }
                        }
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (Throwable th2) {
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    bufferedReader = null;
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (Throwable th5) {
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable th6) {
                        }
                    }
                    throw th;
                }
            }
        } catch (Throwable th7) {
        }
        return str2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0047 A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001e  */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String m773a() {
        /*
            r0 = 0
            java.io.FileReader r1 = new java.io.FileReader     // Catch:{ FileNotFoundException -> 0x004b }
            java.lang.String r2 = "/proc/cpuinfo"
            r1.<init>(r2)     // Catch:{ FileNotFoundException -> 0x004b }
            if (r1 == 0) goto L_0x001c
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x002f }
            r3 = 1024(0x400, float:1.435E-42)
            r2.<init>(r1, r3)     // Catch:{ Throwable -> 0x002f }
            java.lang.String r0 = r2.readLine()     // Catch:{ Throwable -> 0x002f }
            r2.close()     // Catch:{ Throwable -> 0x002f }
            r1.close()     // Catch:{ Throwable -> 0x002f }
        L_0x001c:
            if (r0 == 0) goto L_0x0047
            r1 = 58
            int r1 = r0.indexOf(r1)
            int r1 = r1 + 1
            java.lang.String r0 = r0.substring(r1)
            java.lang.String r0 = r0.trim()
        L_0x002e:
            return r0
        L_0x002f:
            r1 = move-exception
            java.lang.String r2 = f501a     // Catch:{ FileNotFoundException -> 0x0039 }
            java.lang.String r3 = "Could not read from file /proc/cpuinfo"
            com.umeng.analytics.pro.C0138bw.m850e((java.lang.String) r2, (java.lang.String) r3, (java.lang.Throwable) r1)     // Catch:{ FileNotFoundException -> 0x0039 }
            goto L_0x001c
        L_0x0039:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x003d:
            java.lang.String r2 = f501a
            java.lang.String r3 = "Could not open file /proc/cpuinfo"
            com.umeng.analytics.pro.C0138bw.m850e((java.lang.String) r2, (java.lang.String) r3, (java.lang.Throwable) r0)
            r0 = r1
            goto L_0x001c
        L_0x0047:
            java.lang.String r0 = ""
            goto L_0x002e
        L_0x004b:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x003d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0135bt.m773a():java.lang.String");
    }

    /* renamed from: c */
    public static String m784c(Context context) {
        if (MobclickAgent.EScenarioType.E_UM_ANALYTICS_OEM.toValue() == AnalyticsConfig.getVerticalType(context) || MobclickAgent.EScenarioType.E_UM_GAME_OEM.toValue() == AnalyticsConfig.getVerticalType(context)) {
            return m770E(context);
        }
        return m769D(context);
    }

    /* renamed from: d */
    public static String m786d(Context context) {
        return C0136bu.m819b(m784c(context));
    }

    /* renamed from: e */
    public static String m787e(Context context) {
        if (m789f(context) == null) {
            return null;
        }
        int i = context.getResources().getConfiguration().mcc;
        int i2 = context.getResources().getConfiguration().mnc;
        if (i == 0) {
            return null;
        }
        String valueOf = String.valueOf(i2);
        if (i2 < 10) {
            valueOf = String.format("%02d", new Object[]{Integer.valueOf(i2)});
        }
        return new StringBuffer().append(String.valueOf(i)).append(valueOf).toString();
    }

    /* renamed from: f */
    public static String m789f(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (m778a(context, "android.permission.READ_PHONE_STATE")) {
            return telephonyManager.getSubscriberId();
        }
        return null;
    }

    /* renamed from: g */
    public static String m791g(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (m778a(context, "android.permission.READ_PHONE_STATE")) {
            return telephonyManager.getNetworkOperator();
        }
        return null;
    }

    /* renamed from: h */
    public static String m792h(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (m778a(context, "android.permission.READ_PHONE_STATE") && telephonyManager != null) {
                return telephonyManager.getNetworkOperatorName();
            }
        } catch (Throwable th) {
        }
        return "";
    }

    /* renamed from: i */
    public static String m793i(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            int i = displayMetrics.widthPixels;
            return String.valueOf(displayMetrics.heightPixels) + Marker.ANY_MARKER + String.valueOf(i);
        } catch (Throwable th) {
            return "";
        }
    }

    /* renamed from: j */
    public static String[] m794j(Context context) {
        String[] strArr = {"", ""};
        try {
            if (!m778a(context, "android.permission.ACCESS_NETWORK_STATE")) {
                strArr[0] = "";
                return strArr;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                strArr[0] = "";
                return strArr;
            }
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
            if (networkInfo == null || networkInfo.getState() != NetworkInfo.State.CONNECTED) {
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
                if (networkInfo2 != null && networkInfo2.getState() == NetworkInfo.State.CONNECTED) {
                    strArr[0] = f503c;
                    strArr[1] = networkInfo2.getSubtypeName();
                    return strArr;
                }
                return strArr;
            }
            strArr[0] = f504d;
            return strArr;
        } catch (Throwable th) {
        }
    }

    /* renamed from: k */
    public static boolean m795k(Context context) {
        return f504d.equals(m794j(context)[0]);
    }

    /* renamed from: l */
    public static boolean m796l(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        try {
            if (!(!m778a(context, "android.permission.ACCESS_NETWORK_STATE") || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null)) {
                return activeNetworkInfo.isConnectedOrConnecting();
            }
        } catch (Throwable th) {
        }
        return false;
    }

    /* renamed from: m */
    public static int m797m(Context context) {
        try {
            Calendar instance = Calendar.getInstance(m767B(context));
            if (instance != null) {
                return instance.getTimeZone().getRawOffset() / 3600000;
            }
        } catch (Throwable th) {
            C0138bw.m838c(f501a, "error in getTimeZone", th);
        }
        return 8;
    }

    /* renamed from: n */
    public static boolean m798n(Context context) {
        String e = C0037af.m144a(context).mo140b().mo158e("");
        if (TextUtils.isEmpty(e)) {
            if (m789f(context) == null) {
                String str = m799o(context)[0];
                if (!TextUtils.isEmpty(str) && str.equalsIgnoreCase("cn")) {
                    return true;
                }
            } else {
                int i = context.getResources().getConfiguration().mcc;
                if (i == 460 || i == 461) {
                    return true;
                }
                if (i == 0) {
                    String str2 = m799o(context)[0];
                    if (!TextUtils.isEmpty(str2) && str2.equalsIgnoreCase("cn")) {
                        return true;
                    }
                }
            }
            return false;
        } else if (e.equals("cn")) {
            return true;
        } else {
            return false;
        }
    }

    /* renamed from: o */
    public static String[] m799o(Context context) {
        String[] strArr = new String[2];
        try {
            Locale B = m767B(context);
            if (B != null) {
                strArr[0] = B.getCountry();
                strArr[1] = B.getLanguage();
            }
            if (TextUtils.isEmpty(strArr[0])) {
                strArr[0] = "Unknown";
            }
            if (TextUtils.isEmpty(strArr[1])) {
                strArr[1] = "Unknown";
            }
        } catch (Throwable th) {
            C0138bw.m850e(f501a, "error in getLocaleInfo", th);
        }
        return strArr;
    }

    /* renamed from: B */
    private static Locale m767B(Context context) {
        Locale locale = null;
        try {
            Configuration configuration = new Configuration();
            configuration.setToDefaults();
            Settings.System.getConfiguration(context.getContentResolver(), configuration);
            if (configuration != null) {
                locale = configuration.locale;
            }
        } catch (Throwable th) {
            C0138bw.m840c(f501a, "fail to read user config locale");
        }
        if (locale == null) {
            return Locale.getDefault();
        }
        return locale;
    }

    /* renamed from: p */
    public static String m800p(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                String string = applicationInfo.metaData.getString("UMENG_APPKEY");
                if (string != null) {
                    return string.trim();
                }
                C0138bw.m840c(f501a, "getAppkey failed. the applicationinfo is null!");
            }
        } catch (Throwable th) {
            C0138bw.m850e(f501a, "Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.", th);
        }
        return null;
    }

    /* renamed from: q */
    public static String m801q(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return m768C(context);
        }
        if (Build.VERSION.SDK_INT == 23) {
            String b = m780b();
            if (!TextUtils.isEmpty(b)) {
                return b;
            }
            if (C0015a.f17d) {
                return m783c();
            }
            return m768C(context);
        }
        String b2 = m780b();
        if (TextUtils.isEmpty(b2)) {
            return m768C(context);
        }
        return b2;
    }

    /* renamed from: C */
    private static String m768C(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (m778a(context, "android.permission.ACCESS_WIFI_STATE")) {
                return wifiManager.getConnectionInfo().getMacAddress();
            }
            return "";
        } catch (Throwable th) {
            return "";
        }
    }

    /* renamed from: r */
    public static int[] m802r(Context context) {
        int i;
        int i2;
        int i3;
        int i4;
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            if ((context.getApplicationInfo().flags & 8192) == 0) {
                i2 = m772a((Object) displayMetrics, "noncompatWidthPixels");
                i = m772a((Object) displayMetrics, "noncompatHeightPixels");
            } else {
                i = -1;
                i2 = -1;
            }
            if (i2 == -1 || i == -1) {
                i3 = displayMetrics.widthPixels;
                i4 = displayMetrics.heightPixels;
            } else {
                i3 = i2;
                i4 = i;
            }
            int[] iArr = new int[2];
            if (i3 > i4) {
                iArr[0] = i4;
                iArr[1] = i3;
                return iArr;
            }
            iArr[0] = i3;
            iArr[1] = i4;
            return iArr;
        } catch (Throwable th) {
            return null;
        }
    }

    /* renamed from: a */
    private static int m772a(Object obj, String str) {
        try {
            Field declaredField = DisplayMetrics.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField.getInt(obj);
        } catch (Throwable th) {
            return -1;
        }
    }

    /* renamed from: s */
    public static String m803s(Context context) {
        Object obj;
        String obj2;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null || applicationInfo.metaData == null || (obj = applicationInfo.metaData.get("UMENG_CHANNEL")) == null || (obj2 = obj.toString()) == null) {
                return "Unknown";
            }
            return obj2;
        } catch (Throwable th) {
            return "Unknown";
        }
    }

    /* renamed from: t */
    public static String m804t(Context context) {
        return context.getPackageName();
    }

    /* renamed from: u */
    public static String m805u(Context context) {
        try {
            return m777a(MessageDigest.getInstance("MD5").digest(((X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(context.getPackageManager().getPackageInfo(m804t(context), 64).signatures[0].toByteArray()))).getEncoded()));
        } catch (Throwable th) {
            return null;
        }
    }

    /* renamed from: a */
    private static String m777a(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            String hexString = Integer.toHexString(bArr[i]);
            int length = hexString.length();
            if (length == 1) {
                hexString = "0" + hexString;
            }
            if (length > 2) {
                hexString = hexString.substring(length - 2, length);
            }
            sb.append(hexString.toUpperCase(Locale.getDefault()));
            if (i < bArr.length - 1) {
                sb.append(':');
            }
        }
        return sb.toString();
    }

    /* renamed from: v */
    public static String m806v(Context context) {
        return context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
    }

    /* renamed from: w */
    public static String m807w(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.loadLabel(context.getPackageManager()).toString();
        } catch (Throwable th) {
            return null;
        }
    }

    /* renamed from: D */
    private static String m769D(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            String F = m771F(context);
            if (!TextUtils.isEmpty(F)) {
                return F;
            }
            String C = m768C(context);
            if (!TextUtils.isEmpty(C)) {
                return C;
            }
            String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (TextUtils.isEmpty(string)) {
                return m785d();
            }
            return string;
        } else if (Build.VERSION.SDK_INT == 23) {
            String F2 = m771F(context);
            if (!TextUtils.isEmpty(F2)) {
                return F2;
            }
            String b = m780b();
            if (TextUtils.isEmpty(b)) {
                if (C0015a.f17d) {
                    b = m783c();
                } else {
                    b = m768C(context);
                }
            }
            if (!TextUtils.isEmpty(b)) {
                return b;
            }
            String string2 = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (TextUtils.isEmpty(string2)) {
                return m785d();
            }
            return string2;
        } else {
            String F3 = m771F(context);
            if (!TextUtils.isEmpty(F3)) {
                return F3;
            }
            String d = m785d();
            if (!TextUtils.isEmpty(d)) {
                return d;
            }
            String string3 = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (!TextUtils.isEmpty(string3)) {
                return string3;
            }
            String b2 = m780b();
            if (TextUtils.isEmpty(b2)) {
                return m768C(context);
            }
            return b2;
        }
    }

    /* renamed from: E */
    private static String m770E(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (!TextUtils.isEmpty(string)) {
                return string;
            }
            String C = m768C(context);
            if (!TextUtils.isEmpty(C)) {
                return C;
            }
            String d = m785d();
            if (TextUtils.isEmpty(d)) {
                return m771F(context);
            }
            return d;
        } else if (Build.VERSION.SDK_INT == 23) {
            String string2 = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (!TextUtils.isEmpty(string2)) {
                return string2;
            }
            String b = m780b();
            if (TextUtils.isEmpty(b)) {
                if (C0015a.f17d) {
                    b = m783c();
                } else {
                    b = m768C(context);
                }
            }
            if (!TextUtils.isEmpty(b)) {
                return b;
            }
            String d2 = m785d();
            if (TextUtils.isEmpty(d2)) {
                return m771F(context);
            }
            return d2;
        } else {
            String string3 = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (!TextUtils.isEmpty(string3)) {
                return string3;
            }
            String d3 = m785d();
            if (!TextUtils.isEmpty(d3)) {
                return d3;
            }
            String F = m771F(context);
            if (!TextUtils.isEmpty(F)) {
                return F;
            }
            String b2 = m780b();
            if (TextUtils.isEmpty(b2)) {
                return m768C(context);
            }
            return b2;
        }
    }

    /* renamed from: F */
    private static String m771F(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager == null) {
            return "";
        }
        try {
            if (!m778a(context, "android.permission.READ_PHONE_STATE")) {
                return "";
            }
            if (Build.VERSION.SDK_INT <= 26) {
                return telephonyManager.getDeviceId();
            }
            Class<?> cls = Class.forName("android.telephony.TelephonyManager");
            Method method = cls.getMethod("getImei", new Class[]{Integer.class});
            String str = (String) method.invoke(telephonyManager, new Object[]{method, 0});
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
            Method method2 = cls.getMethod("getMeid", new Class[]{Integer.class});
            String str2 = (String) method2.invoke(telephonyManager, new Object[]{method2, 0});
            if (TextUtils.isEmpty(str2)) {
                return telephonyManager.getDeviceId();
            }
            return str2;
        } catch (Throwable th) {
            return "";
        }
    }

    /* renamed from: d */
    private static String m785d() {
        if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT < 26) {
            return Build.SERIAL;
        }
        if (Build.VERSION.SDK_INT < 26) {
            return "";
        }
        try {
            Class<?> cls = Class.forName("android.os.Build");
            return (String) cls.getMethod("getSerial", new Class[0]).invoke(cls, new Object[0]);
        } catch (Throwable th) {
            return "";
        }
    }

    /* renamed from: x */
    public static String m808x(Context context) {
        Properties e = m788e();
        try {
            String property = e.getProperty(f506f);
            if (!TextUtils.isEmpty(property)) {
                return "MIUI";
            }
            if (m790f()) {
                return "Flyme";
            }
            if (!TextUtils.isEmpty(m776a(e))) {
                return "YunOS";
            }
            return property;
        } catch (Throwable th) {
            return null;
        }
    }

    /* renamed from: y */
    public static String m809y(Context context) {
        Properties e = m788e();
        try {
            String property = e.getProperty(f506f);
            if (!TextUtils.isEmpty(property)) {
                return property;
            }
            if (m790f()) {
                try {
                    return m782b(e);
                } catch (Throwable th) {
                    return property;
                }
            } else {
                try {
                    return m776a(e);
                } catch (Throwable th2) {
                    return property;
                }
            }
        } catch (Throwable th3) {
            return null;
        }
    }

    /* renamed from: a */
    private static String m776a(Properties properties) {
        String property = properties.getProperty("ro.yunos.version");
        if (!TextUtils.isEmpty(property)) {
            return property;
        }
        return null;
    }

    /* renamed from: b */
    private static String m782b(Properties properties) {
        try {
            String lowerCase = properties.getProperty("ro.build.display.id").toLowerCase(Locale.getDefault());
            if (lowerCase.contains("flyme os")) {
                return lowerCase.split(" ")[2];
            }
        } catch (Throwable th) {
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0024 A[SYNTHETIC, Splitter:B:12:0x0024] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002d A[SYNTHETIC, Splitter:B:17:0x002d] */
    /* renamed from: e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.Properties m788e() {
        /*
            java.util.Properties r2 = new java.util.Properties
            r2.<init>()
            r1 = 0
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0020, all -> 0x002a }
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0020, all -> 0x002a }
            java.io.File r4 = android.os.Environment.getRootDirectory()     // Catch:{ Throwable -> 0x0020, all -> 0x002a }
            java.lang.String r5 = "build.prop"
            r3.<init>(r4, r5)     // Catch:{ Throwable -> 0x0020, all -> 0x002a }
            r0.<init>(r3)     // Catch:{ Throwable -> 0x0020, all -> 0x002a }
            r2.load(r0)     // Catch:{ Throwable -> 0x003a, all -> 0x0035 }
            if (r0 == 0) goto L_0x001f
            r0.close()     // Catch:{ Throwable -> 0x0031 }
        L_0x001f:
            return r2
        L_0x0020:
            r0 = move-exception
            r0 = r1
        L_0x0022:
            if (r0 == 0) goto L_0x001f
            r0.close()     // Catch:{ Throwable -> 0x0028 }
            goto L_0x001f
        L_0x0028:
            r0 = move-exception
            goto L_0x001f
        L_0x002a:
            r0 = move-exception
        L_0x002b:
            if (r1 == 0) goto L_0x0030
            r1.close()     // Catch:{ Throwable -> 0x0033 }
        L_0x0030:
            throw r0
        L_0x0031:
            r0 = move-exception
            goto L_0x001f
        L_0x0033:
            r1 = move-exception
            goto L_0x0030
        L_0x0035:
            r1 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x002b
        L_0x003a:
            r1 = move-exception
            goto L_0x0022
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0135bt.m788e():java.util.Properties");
    }

    /* renamed from: f */
    private static boolean m790f() {
        try {
            Build.class.getMethod("hasSmartBar", new Class[0]);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    /* renamed from: z */
    public static String m810z(Context context) {
        if (context == null) {
            return "Phone";
        }
        if ((context.getResources().getConfiguration().screenLayout & 15) >= 3) {
            return "Tablet";
        }
        return "Phone";
    }

    /* renamed from: A */
    public static String m766A(Context context) {
        String str;
        if (context == null) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager == null || !m778a(context, "android.permission.READ_PHONE_STATE")) {
                str = null;
            } else {
                str = telephonyManager.getDeviceId();
            }
            try {
                if (!TextUtils.isEmpty(str)) {
                    return str;
                }
                String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
                if (!TextUtils.isEmpty(string) || Build.VERSION.SDK_INT < 9) {
                    return string;
                }
                return Build.SERIAL;
            } catch (Throwable th) {
                return str;
            }
        } catch (Throwable th2) {
            return null;
        }
    }
}
