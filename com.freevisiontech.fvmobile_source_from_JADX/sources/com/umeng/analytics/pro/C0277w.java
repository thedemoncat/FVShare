package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.w */
/* compiled from: UMStoreManager */
public class C0277w {

    /* renamed from: a */
    public static final int f860a = 2049;

    /* renamed from: b */
    public static final int f861b = 2050;

    /* renamed from: c */
    private static Context f862c = null;

    /* renamed from: d */
    private static String f863d = null;

    /* renamed from: e */
    private static final String f864e = "umeng+";

    /* renamed from: f */
    private static final String f865f = "ek__id";

    /* renamed from: g */
    private static final String f866g = "ek_key";

    /* renamed from: h */
    private List<String> f867h;

    /* renamed from: com.umeng.analytics.pro.w$a */
    /* compiled from: UMStoreManager */
    public enum C0279a {
        AUTOPAGE,
        PAGE,
        BEGIN,
        END,
        NEWSESSION
    }

    /* renamed from: com.umeng.analytics.pro.w$b */
    /* compiled from: UMStoreManager */
    private static class C0280b {
        /* access modifiers changed from: private */

        /* renamed from: a */
        public static final C0277w f874a = new C0277w();

        private C0280b() {
        }
    }

    private C0277w() {
        this.f867h = new ArrayList();
        if (f862c != null) {
            m1403b();
            this.f867h.clear();
        }
    }

    /* renamed from: a */
    public static final C0277w m1400a(Context context) {
        f862c = context;
        return C0280b.f874a;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        com.umeng.analytics.pro.C0276v.m1399b(f862c);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x009a, code lost:
        if (r0 != null) goto L_0x009c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r0.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00aa, code lost:
        if (r0 != null) goto L_0x00ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r0.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00b9, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00ba, code lost:
        r6 = r1;
        r1 = r0;
        r0 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        r1.endTransaction();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0094 A[ExcHandler: SQLiteDatabaseCorruptException (e android.database.sqlite.SQLiteDatabaseCorruptException), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a9 A[ExcHandler: Throwable (th java.lang.Throwable), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00bf A[SYNTHETIC, Splitter:B:36:0x00bf] */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void mo742a(org.json.JSONArray r8) {
        /*
            r7 = this;
            r0 = 0
            android.content.Context r1 = f862c     // Catch:{ SQLiteDatabaseCorruptException -> 0x0094, Throwable -> 0x00a9, all -> 0x00b9 }
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0094, Throwable -> 0x00a9, all -> 0x00b9 }
            android.database.sqlite.SQLiteDatabase r0 = r1.mo738a()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0094, Throwable -> 0x00a9, all -> 0x00b9 }
            r0.beginTransaction()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0094, Throwable -> 0x00a9 }
            r1 = 0
        L_0x000f:
            int r2 = r8.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0094, Throwable -> 0x00a9 }
            if (r1 >= r2) goto L_0x0082
            org.json.JSONObject r3 = r8.getJSONObject(r1)     // Catch:{ Exception -> 0x00d9 }
            android.content.ContentValues r4 = new android.content.ContentValues     // Catch:{ Exception -> 0x00d9 }
            r4.<init>()     // Catch:{ Exception -> 0x00d9 }
            java.lang.String r2 = "__i"
            java.lang.String r2 = r3.optString(r2)     // Catch:{ Exception -> 0x00d9 }
            boolean r5 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x00d9 }
            if (r5 == 0) goto L_0x003a
            android.content.Context r2 = f862c     // Catch:{ Exception -> 0x00d9 }
            java.lang.String r2 = com.umeng.analytics.pro.C0071bb.m311g(r2)     // Catch:{ Exception -> 0x00d9 }
            boolean r5 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x00d9 }
            if (r5 == 0) goto L_0x003a
            java.lang.String r2 = ""
        L_0x003a:
            java.lang.String r5 = "__i"
            r4.put(r5, r2)     // Catch:{ Exception -> 0x00d9 }
            java.lang.String r2 = "__e"
            java.lang.String r5 = "id"
            java.lang.String r5 = r3.optString(r5)     // Catch:{ Exception -> 0x00d9 }
            r4.put(r2, r5)     // Catch:{ Exception -> 0x00d9 }
            java.lang.String r2 = "__t"
            java.lang.String r5 = "__t"
            int r5 = r3.optInt(r5)     // Catch:{ Exception -> 0x00d9 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Exception -> 0x00d9 }
            r4.put(r2, r5)     // Catch:{ Exception -> 0x00d9 }
            java.lang.String r2 = "__i"
            r3.remove(r2)     // Catch:{ Exception -> 0x00d9 }
            java.lang.String r2 = "__t"
            r3.remove(r2)     // Catch:{ Exception -> 0x00d9 }
            java.lang.String r2 = "__s"
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x00d9 }
            java.lang.String r3 = r7.mo740a((java.lang.String) r3)     // Catch:{ Exception -> 0x00d9 }
            r4.put(r2, r3)     // Catch:{ Exception -> 0x00d9 }
            java.lang.String r2 = "__et"
            r3 = 0
            r0.insert(r2, r3, r4)     // Catch:{ Exception -> 0x00d9 }
        L_0x007f:
            int r1 = r1 + 1
            goto L_0x000f
        L_0x0082:
            r0.setTransactionSuccessful()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0094, Throwable -> 0x00a9 }
            if (r0 == 0) goto L_0x008a
            r0.endTransaction()     // Catch:{ Throwable -> 0x00cc }
        L_0x008a:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
        L_0x0093:
            return
        L_0x0094:
            r1 = move-exception
            android.content.Context r1 = f862c     // Catch:{ all -> 0x00d4 }
            com.umeng.analytics.pro.C0276v.m1399b((android.content.Context) r1)     // Catch:{ all -> 0x00d4 }
            if (r0 == 0) goto L_0x009f
            r0.endTransaction()     // Catch:{ Throwable -> 0x00ce }
        L_0x009f:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x0093
        L_0x00a9:
            r1 = move-exception
            if (r0 == 0) goto L_0x00af
            r0.endTransaction()     // Catch:{ Throwable -> 0x00d0 }
        L_0x00af:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x0093
        L_0x00b9:
            r1 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
        L_0x00bd:
            if (r1 == 0) goto L_0x00c2
            r1.endTransaction()     // Catch:{ Throwable -> 0x00d2 }
        L_0x00c2:
            android.content.Context r1 = f862c
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)
            r1.mo739b()
            throw r0
        L_0x00cc:
            r0 = move-exception
            goto L_0x008a
        L_0x00ce:
            r0 = move-exception
            goto L_0x009f
        L_0x00d0:
            r0 = move-exception
            goto L_0x00af
        L_0x00d2:
            r1 = move-exception
            goto L_0x00c2
        L_0x00d4:
            r1 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x00bd
        L_0x00d9:
            r2 = move-exception
            goto L_0x007f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0277w.mo742a(org.json.JSONArray):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        com.umeng.analytics.pro.C0276v.m1399b(f862c);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0053, code lost:
        if (r0 != null) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r0.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0063, code lost:
        if (r0 != null) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r0.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r1.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x008d, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x008e, code lost:
        r4 = r1;
        r1 = r0;
        r0 = r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x004d A[ExcHandler: SQLiteDatabaseCorruptException (e android.database.sqlite.SQLiteDatabaseCorruptException), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0062 A[ExcHandler: Throwable (th java.lang.Throwable), Splitter:B:1:0x0001] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0078 A[SYNTHETIC, Splitter:B:27:0x0078] */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean mo744a(java.lang.String r6, java.lang.String r7, int r8) {
        /*
            r5 = this;
            r0 = 0
            android.content.Context r1 = f862c     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062, all -> 0x0072 }
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062, all -> 0x0072 }
            android.database.sqlite.SQLiteDatabase r0 = r1.mo738a()     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062, all -> 0x0072 }
            r0.beginTransaction()     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            android.content.ContentValues r1 = new android.content.ContentValues     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            r1.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            java.lang.String r2 = "__i"
            r1.put(r2, r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            java.lang.String r2 = r5.mo740a((java.lang.String) r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            if (r3 != 0) goto L_0x003a
            java.lang.String r3 = "__a"
            r1.put(r3, r2)     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            java.lang.String r2 = "__t"
            java.lang.Integer r3 = java.lang.Integer.valueOf(r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            r1.put(r2, r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            java.lang.String r2 = "__er"
            r3 = 0
            r0.insert(r2, r3, r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
        L_0x003a:
            r0.setTransactionSuccessful()     // Catch:{ SQLiteDatabaseCorruptException -> 0x004d, Throwable -> 0x0062 }
            if (r0 == 0) goto L_0x0042
            r0.endTransaction()     // Catch:{ Throwable -> 0x0085 }
        L_0x0042:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
        L_0x004b:
            r0 = 0
            return r0
        L_0x004d:
            r1 = move-exception
            android.content.Context r1 = f862c     // Catch:{ all -> 0x008d }
            com.umeng.analytics.pro.C0276v.m1399b((android.content.Context) r1)     // Catch:{ all -> 0x008d }
            if (r0 == 0) goto L_0x0058
            r0.endTransaction()     // Catch:{ Throwable -> 0x0087 }
        L_0x0058:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x004b
        L_0x0062:
            r1 = move-exception
            if (r0 == 0) goto L_0x0068
            r0.endTransaction()     // Catch:{ Throwable -> 0x0089 }
        L_0x0068:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x004b
        L_0x0072:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x0076:
            if (r1 == 0) goto L_0x007b
            r1.endTransaction()     // Catch:{ Throwable -> 0x008b }
        L_0x007b:
            android.content.Context r1 = f862c
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)
            r1.mo739b()
            throw r0
        L_0x0085:
            r0 = move-exception
            goto L_0x0042
        L_0x0087:
            r0 = move-exception
            goto L_0x0058
        L_0x0089:
            r0 = move-exception
            goto L_0x0068
        L_0x008b:
            r1 = move-exception
            goto L_0x007b
        L_0x008d:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x0076
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0277w.mo744a(java.lang.String, java.lang.String, int):boolean");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v1, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: type inference failed for: r1v12 */
    /* JADX WARNING: type inference failed for: r1v49 */
    /* JADX WARNING: type inference failed for: r1v54 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0213  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0218 A[SYNTHETIC, Splitter:B:79:0x0218] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x022a  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x022f A[SYNTHETIC, Splitter:B:87:0x022f] */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean mo745a(java.lang.String r9, org.json.JSONObject r10, com.umeng.analytics.pro.C0277w.C0279a r11) {
        /*
            r8 = this;
            r6 = 0
            r1 = 0
            if (r10 != 0) goto L_0x0005
        L_0x0004:
            return r6
        L_0x0005:
            android.content.Context r0 = f862c     // Catch:{ SQLiteDatabaseCorruptException -> 0x01f2, Throwable -> 0x020e, all -> 0x0226 }
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x01f2, Throwable -> 0x020e, all -> 0x0226 }
            android.database.sqlite.SQLiteDatabase r2 = r0.mo738a()     // Catch:{ SQLiteDatabaseCorruptException -> 0x01f2, Throwable -> 0x020e, all -> 0x0226 }
            r2.beginTransaction()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            com.umeng.analytics.pro.w$a r0 = com.umeng.analytics.pro.C0277w.C0279a.BEGIN     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            if (r11 != r0) goto L_0x0057
            java.lang.String r0 = "__e"
            java.lang.Object r0 = r10.get(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            long r4 = r0.longValue()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            android.content.ContentValues r0 = new android.content.ContentValues     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r0.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "__ii"
            r0.put(r3, r9)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "__e"
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r0.put(r3, r4)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "__sd"
            r4 = 0
            r2.insert(r3, r4, r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r0 = r1
        L_0x0040:
            r2.setTransactionSuccessful()     // Catch:{ SQLiteDatabaseCorruptException -> 0x025b, Throwable -> 0x0255, all -> 0x0247 }
            if (r0 == 0) goto L_0x0048
            r0.close()
        L_0x0048:
            if (r2 == 0) goto L_0x004d
            r2.endTransaction()     // Catch:{ Throwable -> 0x023c }
        L_0x004d:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x0004
        L_0x0057:
            com.umeng.analytics.pro.w$a r0 = com.umeng.analytics.pro.C0277w.C0279a.END     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            if (r11 != r0) goto L_0x00a1
            java.lang.String r0 = "__f"
            java.lang.Object r0 = r10.get(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            long r4 = r0.longValue()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r0.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "update __sd set __f=\""
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "\" where "
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "__ii"
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "=\""
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "\""
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r0 = r0.toString()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r2.execSQL(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r0 = r1
            goto L_0x0040
        L_0x00a1:
            com.umeng.analytics.pro.w$a r0 = com.umeng.analytics.pro.C0277w.C0279a.PAGE     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            if (r11 != r0) goto L_0x00ad
            java.lang.String r0 = "__a"
            r8.m1401a(r9, r10, r2, r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r0 = r1
            goto L_0x0040
        L_0x00ad:
            com.umeng.analytics.pro.w$a r0 = com.umeng.analytics.pro.C0277w.C0279a.AUTOPAGE     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            if (r11 != r0) goto L_0x00b9
            java.lang.String r0 = "__b"
            r8.m1401a(r9, r10, r2, r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r0 = r1
            goto L_0x0040
        L_0x00b9:
            com.umeng.analytics.pro.w$a r0 = com.umeng.analytics.pro.C0277w.C0279a.NEWSESSION     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            if (r11 != r0) goto L_0x026b
            java.lang.String r0 = "__d"
            org.json.JSONObject r0 = r10.getJSONObject(r0)     // Catch:{ Exception -> 0x00ff }
            r4 = r0
        L_0x00c5:
            if (r4 == 0) goto L_0x0267
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r0.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "select __d from __sd where __ii=\""
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r3 = "\""
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            java.lang.String r0 = r0.toString()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            r3 = 0
            android.database.Cursor r0 = r2.rawQuery(r0, r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0257, Throwable -> 0x0252, all -> 0x0245 }
            if (r0 == 0) goto L_0x0264
        L_0x00e9:
            boolean r3 = r0.moveToNext()     // Catch:{ SQLiteDatabaseCorruptException -> 0x025b, Throwable -> 0x0255, all -> 0x0247 }
            if (r3 == 0) goto L_0x0102
            java.lang.String r1 = "__d"
            int r1 = r0.getColumnIndex(r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x025b, Throwable -> 0x0255, all -> 0x0247 }
            java.lang.String r1 = r0.getString(r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x025b, Throwable -> 0x0255, all -> 0x0247 }
            java.lang.String r1 = r8.mo746b((java.lang.String) r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x025b, Throwable -> 0x0255, all -> 0x0247 }
            goto L_0x00e9
        L_0x00ff:
            r0 = move-exception
            r4 = r1
            goto L_0x00c5
        L_0x0102:
            r3 = r1
        L_0x0103:
            if (r4 == 0) goto L_0x015d
            org.json.JSONArray r1 = new org.json.JSONArray     // Catch:{ Exception -> 0x0261 }
            r1.<init>()     // Catch:{ Exception -> 0x0261 }
            boolean r5 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0261 }
            if (r5 != 0) goto L_0x0115
            org.json.JSONArray r1 = new org.json.JSONArray     // Catch:{ Exception -> 0x0261 }
            r1.<init>(r3)     // Catch:{ Exception -> 0x0261 }
        L_0x0115:
            r1.put(r4)     // Catch:{ Exception -> 0x0261 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0261 }
            java.lang.String r1 = r8.mo740a((java.lang.String) r1)     // Catch:{ Exception -> 0x0261 }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0261 }
            if (r3 != 0) goto L_0x015d
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0261 }
            r3.<init>()     // Catch:{ Exception -> 0x0261 }
            java.lang.String r4 = "update  __sd set __d=\""
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0261 }
            java.lang.StringBuilder r1 = r3.append(r1)     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "\" where "
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "__ii"
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "=\""
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x0261 }
            java.lang.StringBuilder r1 = r1.append(r9)     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "\""
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x0261 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0261 }
            r2.execSQL(r1)     // Catch:{ Exception -> 0x0261 }
        L_0x015d:
            java.lang.String r1 = "__c"
            org.json.JSONObject r1 = r10.getJSONObject(r1)     // Catch:{ Exception -> 0x025e }
            if (r1 == 0) goto L_0x01ab
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x025e }
            java.lang.String r1 = r8.mo740a((java.lang.String) r1)     // Catch:{ Exception -> 0x025e }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x025e }
            if (r3 != 0) goto L_0x01ab
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x025e }
            r3.<init>()     // Catch:{ Exception -> 0x025e }
            java.lang.String r4 = "update  __sd set __c=\""
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x025e }
            java.lang.StringBuilder r1 = r3.append(r1)     // Catch:{ Exception -> 0x025e }
            java.lang.String r3 = "\" where "
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x025e }
            java.lang.String r3 = "__ii"
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x025e }
            java.lang.String r3 = "=\""
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x025e }
            java.lang.StringBuilder r1 = r1.append(r9)     // Catch:{ Exception -> 0x025e }
            java.lang.String r3 = "\""
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x025e }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x025e }
            r2.execSQL(r1)     // Catch:{ Exception -> 0x025e }
        L_0x01ab:
            java.lang.String r1 = "__f"
            long r4 = r10.getLong(r1)     // Catch:{ Exception -> 0x01ef }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01ef }
            r1.<init>()     // Catch:{ Exception -> 0x01ef }
            java.lang.String r3 = "update  __sd set __f=\""
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x01ef }
            java.lang.String r3 = java.lang.String.valueOf(r4)     // Catch:{ Exception -> 0x01ef }
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x01ef }
            java.lang.String r3 = "\" where "
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x01ef }
            java.lang.String r3 = "__ii"
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x01ef }
            java.lang.String r3 = "=\""
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x01ef }
            java.lang.StringBuilder r1 = r1.append(r9)     // Catch:{ Exception -> 0x01ef }
            java.lang.String r3 = "\""
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Exception -> 0x01ef }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x01ef }
            r2.execSQL(r1)     // Catch:{ Exception -> 0x01ef }
            goto L_0x0040
        L_0x01ef:
            r1 = move-exception
            goto L_0x0040
        L_0x01f2:
            r0 = move-exception
            r0 = r1
        L_0x01f4:
            android.content.Context r2 = f862c     // Catch:{ all -> 0x024c }
            com.umeng.analytics.pro.C0276v.m1399b((android.content.Context) r2)     // Catch:{ all -> 0x024c }
            if (r0 == 0) goto L_0x01fe
            r0.close()
        L_0x01fe:
            if (r1 == 0) goto L_0x0203
            r1.endTransaction()     // Catch:{ Throwable -> 0x023f }
        L_0x0203:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x0004
        L_0x020e:
            r0 = move-exception
            r0 = r1
            r2 = r1
        L_0x0211:
            if (r0 == 0) goto L_0x0216
            r0.close()
        L_0x0216:
            if (r2 == 0) goto L_0x021b
            r2.endTransaction()     // Catch:{ Throwable -> 0x0241 }
        L_0x021b:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x0004
        L_0x0226:
            r0 = move-exception
            r2 = r1
        L_0x0228:
            if (r1 == 0) goto L_0x022d
            r1.close()
        L_0x022d:
            if (r2 == 0) goto L_0x0232
            r2.endTransaction()     // Catch:{ Throwable -> 0x0243 }
        L_0x0232:
            android.content.Context r1 = f862c
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)
            r1.mo739b()
            throw r0
        L_0x023c:
            r0 = move-exception
            goto L_0x004d
        L_0x023f:
            r0 = move-exception
            goto L_0x0203
        L_0x0241:
            r0 = move-exception
            goto L_0x021b
        L_0x0243:
            r1 = move-exception
            goto L_0x0232
        L_0x0245:
            r0 = move-exception
            goto L_0x0228
        L_0x0247:
            r1 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
            goto L_0x0228
        L_0x024c:
            r2 = move-exception
            r7 = r2
            r2 = r1
            r1 = r0
            r0 = r7
            goto L_0x0228
        L_0x0252:
            r0 = move-exception
            r0 = r1
            goto L_0x0211
        L_0x0255:
            r1 = move-exception
            goto L_0x0211
        L_0x0257:
            r0 = move-exception
            r0 = r1
            r1 = r2
            goto L_0x01f4
        L_0x025b:
            r1 = move-exception
            r1 = r2
            goto L_0x01f4
        L_0x025e:
            r1 = move-exception
            goto L_0x01ab
        L_0x0261:
            r1 = move-exception
            goto L_0x015d
        L_0x0264:
            r3 = r1
            goto L_0x0103
        L_0x0267:
            r3 = r1
            r0 = r1
            goto L_0x0103
        L_0x026b:
            r0 = r1
            goto L_0x0040
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0277w.mo745a(java.lang.String, org.json.JSONObject, com.umeng.analytics.pro.w$a):boolean");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: android.database.Cursor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: android.database.Cursor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v14, resolved type: android.database.Cursor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v17, resolved type: android.database.Cursor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v20, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v21, resolved type: android.database.Cursor} */
    /* JADX WARNING: type inference failed for: r1v20, types: [java.lang.String] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00d2  */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m1401a(java.lang.String r6, org.json.JSONObject r7, android.database.sqlite.SQLiteDatabase r8, java.lang.String r9) throws org.json.JSONException {
        /*
            r5 = this;
            r1 = 0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            r0.<init>()     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.String r2 = "select "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.String r2 = " from "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.String r2 = "__sd"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.String r2 = " where "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.String r2 = "__ii"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.String r2 = "=\""
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.StringBuilder r0 = r0.append(r6)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.String r2 = "\""
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            r2 = 0
            android.database.Cursor r0 = r8.rawQuery(r0, r2)     // Catch:{ Throwable -> 0x00c7, all -> 0x00cf }
            if (r0 == 0) goto L_0x00dd
        L_0x004a:
            boolean r2 = r0.moveToNext()     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            if (r2 == 0) goto L_0x005d
            int r1 = r0.getColumnIndex(r9)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r1 = r0.getString(r1)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r1 = r5.mo746b((java.lang.String) r1)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            goto L_0x004a
        L_0x005d:
            r2 = r1
        L_0x005e:
            org.json.JSONArray r1 = new org.json.JSONArray     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            r1.<init>()     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            if (r3 != 0) goto L_0x006e
            org.json.JSONArray r1 = new org.json.JSONArray     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
        L_0x006e:
            r1.put(r7)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r1 = r5.mo740a((java.lang.String) r1)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            if (r2 != 0) goto L_0x00c1
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            r2.<init>()     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r3 = "update __sd set "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.StringBuilder r2 = r2.append(r9)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r3 = "=\""
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.StringBuilder r1 = r2.append(r1)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r2 = "\" where "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r2 = "__ii"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r2 = "=\""
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.StringBuilder r1 = r1.append(r6)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r2 = "\""
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
            r8.execSQL(r1)     // Catch:{ Throwable -> 0x00db, all -> 0x00d6 }
        L_0x00c1:
            if (r0 == 0) goto L_0x00c6
            r0.close()
        L_0x00c6:
            return
        L_0x00c7:
            r0 = move-exception
            r0 = r1
        L_0x00c9:
            if (r0 == 0) goto L_0x00c6
            r0.close()
            goto L_0x00c6
        L_0x00cf:
            r0 = move-exception
        L_0x00d0:
            if (r1 == 0) goto L_0x00d5
            r1.close()
        L_0x00d5:
            throw r0
        L_0x00d6:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x00d0
        L_0x00db:
            r1 = move-exception
            goto L_0x00c9
        L_0x00dd:
            r2 = r1
            goto L_0x005e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0277w.m1401a(java.lang.String, org.json.JSONObject, android.database.sqlite.SQLiteDatabase, java.lang.String):void");
    }

    /* renamed from: a */
    public JSONObject mo741a() {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        m1405c(jSONObject2);
        m1404b(jSONObject2);
        m1402a(jSONObject2);
        try {
            if (jSONObject2.length() > 0) {
                jSONObject.put("body", jSONObject2);
            }
        } catch (Throwable th) {
        }
        return jSONObject;
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a4 A[SYNTHETIC, Splitter:B:37:0x00a4] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00da A[SYNTHETIC, Splitter:B:51:0x00da] */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m1402a(org.json.JSONObject r12) {
        /*
            r11 = this;
            r0 = 0
            android.content.Context r1 = f862c     // Catch:{ SQLiteDatabaseCorruptException -> 0x01b1, Throwable -> 0x01a8, all -> 0x0194 }
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x01b1, Throwable -> 0x01a8, all -> 0x0194 }
            android.database.sqlite.SQLiteDatabase r2 = r1.mo738a()     // Catch:{ SQLiteDatabaseCorruptException -> 0x01b1, Throwable -> 0x01a8, all -> 0x0194 }
            r2.beginTransaction()     // Catch:{ SQLiteDatabaseCorruptException -> 0x01b5, Throwable -> 0x01ad, all -> 0x019b }
            java.lang.String r1 = "select *  from __et"
            r3 = 0
            android.database.Cursor r1 = r2.rawQuery(r1, r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x01b5, Throwable -> 0x01ad, all -> 0x019b }
            if (r1 == 0) goto L_0x0171
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r4.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r5.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
        L_0x0022:
            boolean r0 = r1.moveToNext()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r0 == 0) goto L_0x00ed
            java.lang.String r0 = "__t"
            int r0 = r1.getColumnIndex(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            int r3 = r1.getInt(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r0 = "__i"
            int r0 = r1.getColumnIndex(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r0 = r1.getString(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r6 = "__s"
            int r6 = r1.getColumnIndex(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r6 = r1.getString(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r7 = ""
            boolean r7 = r7.equals(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r7 == 0) goto L_0x0056
            java.lang.String r0 = com.umeng.analytics.pro.C0071bb.m307a()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
        L_0x0056:
            switch(r3) {
                case 2049: goto L_0x005a;
                case 2050: goto L_0x00b1;
                default: goto L_0x0059;
            }     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
        L_0x0059:
            goto L_0x0022
        L_0x005a:
            boolean r3 = android.text.TextUtils.isEmpty(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r3 != 0) goto L_0x0022
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r3 = r11.mo746b((java.lang.String) r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r7.<init>(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            boolean r3 = r4.has(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r3 == 0) goto L_0x0096
            org.json.JSONArray r3 = r4.optJSONArray(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
        L_0x0073:
            r3.put(r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r4.put(r0, r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            goto L_0x0022
        L_0x007a:
            r0 = move-exception
            r0 = r1
            r1 = r2
        L_0x007d:
            android.content.Context r2 = f862c     // Catch:{ all -> 0x01a1 }
            com.umeng.analytics.pro.C0276v.m1399b((android.content.Context) r2)     // Catch:{ all -> 0x01a1 }
            if (r0 == 0) goto L_0x0087
            r0.close()
        L_0x0087:
            if (r1 == 0) goto L_0x008c
            r1.endTransaction()     // Catch:{ Throwable -> 0x018b }
        L_0x008c:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
        L_0x0095:
            return
        L_0x0096:
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r3.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            goto L_0x0073
        L_0x009c:
            r0 = move-exception
        L_0x009d:
            if (r1 == 0) goto L_0x00a2
            r1.close()
        L_0x00a2:
            if (r2 == 0) goto L_0x00a7
            r2.endTransaction()     // Catch:{ Throwable -> 0x018e }
        L_0x00a7:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x0095
        L_0x00b1:
            boolean r3 = android.text.TextUtils.isEmpty(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r3 != 0) goto L_0x0022
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r3 = r11.mo746b((java.lang.String) r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r7.<init>(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            boolean r3 = r5.has(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r3 == 0) goto L_0x00e7
            org.json.JSONArray r3 = r5.optJSONArray(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
        L_0x00ca:
            r3.put(r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r5.put(r0, r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            goto L_0x0022
        L_0x00d2:
            r0 = move-exception
        L_0x00d3:
            if (r1 == 0) goto L_0x00d8
            r1.close()
        L_0x00d8:
            if (r2 == 0) goto L_0x00dd
            r2.endTransaction()     // Catch:{ Throwable -> 0x0191 }
        L_0x00dd:
            android.content.Context r1 = f862c
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)
            r1.mo739b()
            throw r0
        L_0x00e7:
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r3.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            goto L_0x00ca
        L_0x00ed:
            int r0 = r4.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r0 <= 0) goto L_0x012f
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r3.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.util.Iterator r6 = r4.keys()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
        L_0x00fc:
            boolean r0 = r6.hasNext()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r0 == 0) goto L_0x0123
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r7.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.Object r0 = r6.next()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r8 = r4.optString(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            org.json.JSONArray r9 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r9.<init>(r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r7.put(r0, r9)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            int r0 = r7.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r0 <= 0) goto L_0x00fc
            r3.put(r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            goto L_0x00fc
        L_0x0123:
            int r0 = r3.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r0 <= 0) goto L_0x012f
            java.lang.String r0 = "ekv"
            r12.put(r0, r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
        L_0x012f:
            int r0 = r5.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r0 <= 0) goto L_0x0171
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r3.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.util.Iterator r4 = r5.keys()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
        L_0x013e:
            boolean r0 = r4.hasNext()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r0 == 0) goto L_0x0165
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r6.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.Object r0 = r4.next()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            java.lang.String r7 = r5.optString(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            org.json.JSONArray r8 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r8.<init>(r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            r6.put(r0, r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            int r0 = r6.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r0 <= 0) goto L_0x013e
            r3.put(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            goto L_0x013e
        L_0x0165:
            int r0 = r3.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r0 <= 0) goto L_0x0171
            java.lang.String r0 = "gkv"
            r12.put(r0, r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
        L_0x0171:
            r2.setTransactionSuccessful()     // Catch:{ SQLiteDatabaseCorruptException -> 0x007a, Throwable -> 0x009c, all -> 0x00d2 }
            if (r1 == 0) goto L_0x0179
            r1.close()
        L_0x0179:
            if (r2 == 0) goto L_0x017e
            r2.endTransaction()     // Catch:{ Throwable -> 0x0189 }
        L_0x017e:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x0095
        L_0x0189:
            r0 = move-exception
            goto L_0x017e
        L_0x018b:
            r0 = move-exception
            goto L_0x008c
        L_0x018e:
            r0 = move-exception
            goto L_0x00a7
        L_0x0191:
            r1 = move-exception
            goto L_0x00dd
        L_0x0194:
            r1 = move-exception
            r2 = r0
            r10 = r0
            r0 = r1
            r1 = r10
            goto L_0x00d3
        L_0x019b:
            r1 = move-exception
            r10 = r1
            r1 = r0
            r0 = r10
            goto L_0x00d3
        L_0x01a1:
            r2 = move-exception
            r10 = r2
            r2 = r1
            r1 = r0
            r0 = r10
            goto L_0x00d3
        L_0x01a8:
            r1 = move-exception
            r1 = r0
            r2 = r0
            goto L_0x009d
        L_0x01ad:
            r1 = move-exception
            r1 = r0
            goto L_0x009d
        L_0x01b1:
            r1 = move-exception
            r1 = r0
            goto L_0x007d
        L_0x01b5:
            r1 = move-exception
            r1 = r2
            goto L_0x007d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0277w.m1402a(org.json.JSONObject):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.database.Cursor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v17, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v18, resolved type: android.database.sqlite.SQLiteDatabase} */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00b5, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00b6, code lost:
        r5 = r2;
        r2 = r1;
        r1 = r0;
        r0 = r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0087 A[SYNTHETIC, Splitter:B:38:0x0087] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00a0 A[SYNTHETIC, Splitter:B:46:0x00a0] */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m1404b(org.json.JSONObject r7) {
        /*
            r6 = this;
            r0 = 0
            android.content.Context r1 = f862c     // Catch:{ SQLiteDatabaseCorruptException -> 0x00c3, Throwable -> 0x007e, all -> 0x0094 }
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x00c3, Throwable -> 0x007e, all -> 0x0094 }
            android.database.sqlite.SQLiteDatabase r1 = r1.mo738a()     // Catch:{ SQLiteDatabaseCorruptException -> 0x00c3, Throwable -> 0x007e, all -> 0x0094 }
            r1.beginTransaction()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1, all -> 0x00b5 }
            java.lang.String r2 = "select *  from __er"
            r3 = 0
            android.database.Cursor r0 = r1.rawQuery(r2, r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1, all -> 0x00b5 }
            if (r0 == 0) goto L_0x0067
            org.json.JSONArray r2 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            r2.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
        L_0x001d:
            boolean r3 = r0.moveToNext()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            if (r3 == 0) goto L_0x005b
            java.lang.String r3 = "__a"
            int r3 = r0.getColumnIndex(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            java.lang.String r3 = r0.getString(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            if (r4 != 0) goto L_0x001d
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            java.lang.String r3 = r6.mo746b((java.lang.String) r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            r4.<init>(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            r2.put(r4)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            goto L_0x001d
        L_0x0041:
            r2 = move-exception
        L_0x0042:
            android.content.Context r2 = f862c     // Catch:{ all -> 0x00bb }
            com.umeng.analytics.pro.C0276v.m1399b((android.content.Context) r2)     // Catch:{ all -> 0x00bb }
            if (r0 == 0) goto L_0x004c
            r0.close()
        L_0x004c:
            if (r1 == 0) goto L_0x0051
            r1.endTransaction()     // Catch:{ Throwable -> 0x00af }
        L_0x0051:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
        L_0x005a:
            return
        L_0x005b:
            int r3 = r2.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            if (r3 <= 0) goto L_0x0067
            java.lang.String r3 = "error"
            r7.put(r3, r2)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
        L_0x0067:
            r1.setTransactionSuccessful()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0041, Throwable -> 0x00c1 }
            if (r0 == 0) goto L_0x006f
            r0.close()
        L_0x006f:
            if (r1 == 0) goto L_0x0074
            r1.endTransaction()     // Catch:{ Throwable -> 0x00ad }
        L_0x0074:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x005a
        L_0x007e:
            r1 = move-exception
            r1 = r0
        L_0x0080:
            if (r0 == 0) goto L_0x0085
            r0.close()
        L_0x0085:
            if (r1 == 0) goto L_0x008a
            r1.endTransaction()     // Catch:{ Throwable -> 0x00b1 }
        L_0x008a:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x005a
        L_0x0094:
            r1 = move-exception
            r2 = r0
            r5 = r0
            r0 = r1
            r1 = r5
        L_0x0099:
            if (r1 == 0) goto L_0x009e
            r1.close()
        L_0x009e:
            if (r2 == 0) goto L_0x00a3
            r2.endTransaction()     // Catch:{ Throwable -> 0x00b3 }
        L_0x00a3:
            android.content.Context r1 = f862c
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)
            r1.mo739b()
            throw r0
        L_0x00ad:
            r0 = move-exception
            goto L_0x0074
        L_0x00af:
            r0 = move-exception
            goto L_0x0051
        L_0x00b1:
            r0 = move-exception
            goto L_0x008a
        L_0x00b3:
            r1 = move-exception
            goto L_0x00a3
        L_0x00b5:
            r2 = move-exception
            r5 = r2
            r2 = r1
            r1 = r0
            r0 = r5
            goto L_0x0099
        L_0x00bb:
            r2 = move-exception
            r5 = r2
            r2 = r1
            r1 = r0
            r0 = r5
            goto L_0x0099
        L_0x00c1:
            r2 = move-exception
            goto L_0x0080
        L_0x00c3:
            r1 = move-exception
            r1 = r0
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0277w.m1404b(org.json.JSONObject):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        com.umeng.analytics.pro.C0276v.m1399b(f862c);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x012e, code lost:
        if (r2 != null) goto L_0x0130;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0130, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0133, code lost:
        if (r3 != null) goto L_0x0135;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        r3.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0168, code lost:
        if (r2 != null) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x016a, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x016d, code lost:
        if (r3 != null) goto L_0x016f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        r3.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x017c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x017d, code lost:
        r16 = r4;
        r4 = r3;
        r3 = r2;
        r2 = r16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0185, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:?, code lost:
        r4.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x019f, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x01a0, code lost:
        r16 = r4;
        r4 = r3;
        r3 = r2;
        r2 = r16;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0128 A[ExcHandler: SQLiteDatabaseCorruptException (e android.database.sqlite.SQLiteDatabaseCorruptException), Splitter:B:1:0x0002] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0167 A[ExcHandler: Throwable (th java.lang.Throwable), Splitter:B:1:0x0002] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0185  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x018a A[SYNTHETIC, Splitter:B:63:0x018a] */
    /* renamed from: c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m1405c(org.json.JSONObject r18) {
        /*
            r17 = this;
            r3 = 0
            r2 = 0
            android.content.Context r4 = f862c     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167, all -> 0x017c }
            com.umeng.analytics.pro.u r4 = com.umeng.analytics.pro.C0275u.m1390a(r4)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167, all -> 0x017c }
            android.database.sqlite.SQLiteDatabase r3 = r4.mo738a()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167, all -> 0x017c }
            r3.beginTransaction()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167, all -> 0x019f }
            java.lang.String r4 = "select *  from __sd"
            r5 = 0
            android.database.Cursor r2 = r3.rawQuery(r4, r5)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167, all -> 0x019f }
            if (r2 == 0) goto L_0x0150
            org.json.JSONArray r4 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r4.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r0 = r17
            java.util.List<java.lang.String> r5 = r0.f867h     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r5.clear()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
        L_0x0025:
            boolean r5 = r2.moveToNext()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r5 == 0) goto L_0x0142
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r5.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r6 = "__f"
            int r6 = r2.getColumnIndex(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r6 = r2.getString(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r7 = "__e"
            int r7 = r2.getColumnIndex(r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r7 = r2.getString(r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            boolean r8 = android.text.TextUtils.isEmpty(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r8 != 0) goto L_0x0025
            boolean r8 = android.text.TextUtils.isEmpty(r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r8 != 0) goto L_0x0025
            long r8 = java.lang.Long.parseLong(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            long r10 = java.lang.Long.parseLong(r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            long r8 = r8 - r10
            r10 = 0
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 <= 0) goto L_0x0025
            java.lang.String r8 = "__a"
            int r8 = r2.getColumnIndex(r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r8 = r2.getString(r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r9 = "__b"
            int r9 = r2.getColumnIndex(r9)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r9 = r2.getString(r9)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r10 = "__c"
            int r10 = r2.getColumnIndex(r10)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r10 = r2.getString(r10)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r11 = "__d"
            int r11 = r2.getColumnIndex(r11)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r11 = r2.getString(r11)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r12 = "__ii"
            int r12 = r2.getColumnIndex(r12)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r12 = r2.getString(r12)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r0 = r17
            java.util.List<java.lang.String> r13 = r0.f867h     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r13.add(r12)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r13 = "id"
            r5.put(r13, r12)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r12 = "start_time"
            r5.put(r12, r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r12 = "end_time"
            r5.put(r12, r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            java.lang.String r12 = "duration"
            long r14 = java.lang.Long.parseLong(r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            long r6 = java.lang.Long.parseLong(r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            long r6 = r14 - r6
            r5.put(r12, r6)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            boolean r6 = android.text.TextUtils.isEmpty(r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r6 != 0) goto L_0x00d8
            java.lang.String r6 = "pages"
            org.json.JSONArray r7 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r0 = r17
            java.lang.String r8 = r0.mo746b((java.lang.String) r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r7.<init>(r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r5.put(r6, r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
        L_0x00d8:
            boolean r6 = android.text.TextUtils.isEmpty(r9)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r6 != 0) goto L_0x00ef
            java.lang.String r6 = "autopages"
            org.json.JSONArray r7 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r0 = r17
            java.lang.String r8 = r0.mo746b((java.lang.String) r9)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r7.<init>(r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r5.put(r6, r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
        L_0x00ef:
            boolean r6 = android.text.TextUtils.isEmpty(r10)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r6 != 0) goto L_0x0106
            java.lang.String r6 = "traffic"
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r0 = r17
            java.lang.String r8 = r0.mo746b((java.lang.String) r10)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r7.<init>(r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r5.put(r6, r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
        L_0x0106:
            boolean r6 = android.text.TextUtils.isEmpty(r11)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r6 != 0) goto L_0x011d
            java.lang.String r6 = "locations"
            org.json.JSONArray r7 = new org.json.JSONArray     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r0 = r17
            java.lang.String r8 = r0.mo746b((java.lang.String) r11)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r7.<init>(r8)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            r5.put(r6, r7)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
        L_0x011d:
            int r6 = r5.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r6 <= 0) goto L_0x0025
            r4.put(r5)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            goto L_0x0025
        L_0x0128:
            r4 = move-exception
            android.content.Context r4 = f862c     // Catch:{ all -> 0x01a7 }
            com.umeng.analytics.pro.C0276v.m1399b((android.content.Context) r4)     // Catch:{ all -> 0x01a7 }
            if (r2 == 0) goto L_0x0133
            r2.close()
        L_0x0133:
            if (r3 == 0) goto L_0x0138
            r3.endTransaction()     // Catch:{ Throwable -> 0x0199 }
        L_0x0138:
            android.content.Context r2 = f862c
            com.umeng.analytics.pro.u r2 = com.umeng.analytics.pro.C0275u.m1390a(r2)
            r2.mo739b()
        L_0x0141:
            return
        L_0x0142:
            int r5 = r4.length()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r5 <= 0) goto L_0x0150
            java.lang.String r5 = "sessions"
            r0 = r18
            r0.put(r5, r4)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
        L_0x0150:
            r3.setTransactionSuccessful()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0128, Throwable -> 0x0167 }
            if (r2 == 0) goto L_0x0158
            r2.close()
        L_0x0158:
            if (r3 == 0) goto L_0x015d
            r3.endTransaction()     // Catch:{ Throwable -> 0x0197 }
        L_0x015d:
            android.content.Context r2 = f862c
            com.umeng.analytics.pro.u r2 = com.umeng.analytics.pro.C0275u.m1390a(r2)
            r2.mo739b()
            goto L_0x0141
        L_0x0167:
            r4 = move-exception
            if (r2 == 0) goto L_0x016d
            r2.close()
        L_0x016d:
            if (r3 == 0) goto L_0x0172
            r3.endTransaction()     // Catch:{ Throwable -> 0x019b }
        L_0x0172:
            android.content.Context r2 = f862c
            com.umeng.analytics.pro.u r2 = com.umeng.analytics.pro.C0275u.m1390a(r2)
            r2.mo739b()
            goto L_0x0141
        L_0x017c:
            r4 = move-exception
            r16 = r4
            r4 = r3
            r3 = r2
            r2 = r16
        L_0x0183:
            if (r3 == 0) goto L_0x0188
            r3.close()
        L_0x0188:
            if (r4 == 0) goto L_0x018d
            r4.endTransaction()     // Catch:{ Throwable -> 0x019d }
        L_0x018d:
            android.content.Context r3 = f862c
            com.umeng.analytics.pro.u r3 = com.umeng.analytics.pro.C0275u.m1390a(r3)
            r3.mo739b()
            throw r2
        L_0x0197:
            r2 = move-exception
            goto L_0x015d
        L_0x0199:
            r2 = move-exception
            goto L_0x0138
        L_0x019b:
            r2 = move-exception
            goto L_0x0172
        L_0x019d:
            r3 = move-exception
            goto L_0x018d
        L_0x019f:
            r4 = move-exception
            r16 = r4
            r4 = r3
            r3 = r2
            r2 = r16
            goto L_0x0183
        L_0x01a7:
            r4 = move-exception
            r16 = r4
            r4 = r3
            r3 = r2
            r2 = r16
            goto L_0x0183
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0277w.m1405c(org.json.JSONObject):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0092 A[SYNTHETIC, Splitter:B:32:0x0092] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a5 A[SYNTHETIC, Splitter:B:38:0x00a5] */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void mo743a(boolean r6, boolean r7) {
        /*
            r5 = this;
            r0 = 0
            android.content.Context r1 = f862c     // Catch:{ SQLiteDatabaseCorruptException -> 0x00c4, Throwable -> 0x008e, all -> 0x009f }
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)     // Catch:{ SQLiteDatabaseCorruptException -> 0x00c4, Throwable -> 0x008e, all -> 0x009f }
            android.database.sqlite.SQLiteDatabase r1 = r1.mo738a()     // Catch:{ SQLiteDatabaseCorruptException -> 0x00c4, Throwable -> 0x008e, all -> 0x009f }
            r1.beginTransaction()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            java.lang.String r0 = "delete from __er"
            r1.execSQL(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            java.lang.String r0 = "delete from __et"
            r1.execSQL(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            if (r7 == 0) goto L_0x0036
            if (r6 == 0) goto L_0x0024
            java.lang.String r0 = "delete from __sd"
            r1.execSQL(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
        L_0x0024:
            r1.setTransactionSuccessful()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            if (r1 == 0) goto L_0x002c
            r1.endTransaction()     // Catch:{ Throwable -> 0x00b2 }
        L_0x002c:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
        L_0x0035:
            return
        L_0x0036:
            java.util.List<java.lang.String> r0 = r5.f867h     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            int r0 = r0.size()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            if (r0 <= 0) goto L_0x0072
            r0 = 0
            r2 = r0
        L_0x0040:
            java.util.List<java.lang.String> r0 = r5.f867h     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            int r0 = r0.size()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            if (r2 >= r0) goto L_0x0072
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            r0.<init>()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            java.lang.String r3 = "delete from __sd where __ii=\""
            java.lang.StringBuilder r3 = r0.append(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            java.util.List<java.lang.String> r0 = r5.f867h     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            java.lang.Object r0 = r0.get(r2)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            java.lang.String r3 = "\""
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            java.lang.String r0 = r0.toString()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            r1.execSQL(r0)     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            int r0 = r2 + 1
            r2 = r0
            goto L_0x0040
        L_0x0072:
            java.util.List<java.lang.String> r0 = r5.f867h     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            r0.clear()     // Catch:{ SQLiteDatabaseCorruptException -> 0x0078, Throwable -> 0x00c2, all -> 0x00bb }
            goto L_0x0024
        L_0x0078:
            r0 = move-exception
            r0 = r1
        L_0x007a:
            android.content.Context r1 = f862c     // Catch:{ all -> 0x00bd }
            com.umeng.analytics.pro.C0276v.m1399b((android.content.Context) r1)     // Catch:{ all -> 0x00bd }
            if (r0 == 0) goto L_0x0084
            r0.endTransaction()     // Catch:{ Throwable -> 0x00b5 }
        L_0x0084:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x0035
        L_0x008e:
            r1 = move-exception
            r1 = r0
        L_0x0090:
            if (r1 == 0) goto L_0x0095
            r1.endTransaction()     // Catch:{ Throwable -> 0x00b7 }
        L_0x0095:
            android.content.Context r0 = f862c
            com.umeng.analytics.pro.u r0 = com.umeng.analytics.pro.C0275u.m1390a(r0)
            r0.mo739b()
            goto L_0x0035
        L_0x009f:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x00a3:
            if (r1 == 0) goto L_0x00a8
            r1.endTransaction()     // Catch:{ Throwable -> 0x00b9 }
        L_0x00a8:
            android.content.Context r1 = f862c
            com.umeng.analytics.pro.u r1 = com.umeng.analytics.pro.C0275u.m1390a(r1)
            r1.mo739b()
            throw r0
        L_0x00b2:
            r0 = move-exception
            goto L_0x002c
        L_0x00b5:
            r0 = move-exception
            goto L_0x0084
        L_0x00b7:
            r0 = move-exception
            goto L_0x0095
        L_0x00b9:
            r1 = move-exception
            goto L_0x00a8
        L_0x00bb:
            r0 = move-exception
            goto L_0x00a3
        L_0x00bd:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x00a3
        L_0x00c2:
            r0 = move-exception
            goto L_0x0090
        L_0x00c4:
            r1 = move-exception
            goto L_0x007a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0277w.mo743a(boolean, boolean):void");
    }

    /* renamed from: b */
    private void m1403b() {
        try {
            if (TextUtils.isEmpty(f863d)) {
                SharedPreferences a = C0067az.m285a(f862c);
                String string = a.getString(f865f, (String) null);
                if (TextUtils.isEmpty(string)) {
                    string = C0135bt.m766A(f862c);
                    if (!TextUtils.isEmpty(string)) {
                        a.edit().putString(f865f, string).commit();
                    }
                }
                if (!TextUtils.isEmpty(string)) {
                    String substring = string.substring(1, 9);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < substring.length(); i++) {
                        char charAt = substring.charAt(i);
                        if (!Character.isDigit(charAt)) {
                            sb.append(charAt);
                        } else if (Integer.parseInt(Character.toString(charAt)) == 0) {
                            sb.append(0);
                        } else {
                            sb.append(10 - Integer.parseInt(Character.toString(charAt)));
                        }
                    }
                    f863d = sb.toString();
                }
                if (!TextUtils.isEmpty(f863d)) {
                    f863d += new StringBuilder(f863d).reverse().toString();
                    String string2 = a.getString(f866g, (String) null);
                    if (TextUtils.isEmpty(string2)) {
                        a.edit().putString(f866g, mo740a(f864e)).commit();
                    } else if (!f864e.equals(mo746b(string2))) {
                        mo743a(true, false);
                    }
                }
            }
        } catch (Throwable th) {
        }
    }

    /* renamed from: a */
    public String mo740a(String str) {
        try {
            if (TextUtils.isEmpty(f863d)) {
                return str;
            }
            return Base64.encodeToString(C0133br.m756a(str.getBytes(), f863d.getBytes()), 0);
        } catch (Exception e) {
            return null;
        }
    }

    /* renamed from: b */
    public String mo746b(String str) {
        try {
            if (TextUtils.isEmpty(f863d)) {
                return str;
            }
            return new String(C0133br.m759b(Base64.decode(str.getBytes(), 0), f863d.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }
}
