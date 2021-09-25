package com.umeng.analytics.pro;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.umeng.analytics.C0015a;
import com.umeng.analytics.pro.C0196d;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* renamed from: com.umeng.analytics.pro.a */
/* compiled from: CCSQLManager */
public class C0030a {
    /* renamed from: a */
    public static boolean m102a(SQLiteDatabase sQLiteDatabase, String str) {
        try {
            sQLiteDatabase.execSQL("drop table if exists " + str);
            return true;
        } catch (SQLException e) {
            C0138bw.m849e("delete table faild!");
            e.printStackTrace();
            return false;
        }
    }

    /* renamed from: b */
    public static boolean m107b(SQLiteDatabase sQLiteDatabase, String str) {
        try {
            if (m108c(sQLiteDatabase, str) >= 0) {
                sQLiteDatabase.execSQL("delete from " + str);
            }
            return true;
        } catch (SQLException e) {
            C0138bw.m849e("cleanTableData faild!" + e.toString());
            return false;
        }
    }

    /* renamed from: c */
    public static int m108c(SQLiteDatabase sQLiteDatabase, String str) {
        Cursor cursor = null;
        int i = 0;
        try {
            cursor = sQLiteDatabase.rawQuery("select * from " + str, (String[]) null);
            i = cursor.getCount();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            C0138bw.m849e("count error " + e.toString());
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return i;
    }

    /* renamed from: a */
    public static boolean m103a(SQLiteDatabase sQLiteDatabase, Collection<C0232i> collection) {
        try {
            sQLiteDatabase.beginTransaction();
            if (m108c(sQLiteDatabase, C0196d.C0197a.f663b) > 0) {
                m107b(sQLiteDatabase, C0196d.C0197a.f663b);
            }
            for (C0232i a : collection) {
                sQLiteDatabase.insert(C0196d.C0197a.f663b, (String) null, m94a(a));
            }
            sQLiteDatabase.setTransactionSuccessful();
            sQLiteDatabase.endTransaction();
            return true;
        } catch (SQLException e) {
            C0138bw.m849e("insert to Aggregated cache table faild!");
            sQLiteDatabase.endTransaction();
            return false;
        } catch (Throwable th) {
            sQLiteDatabase.endTransaction();
            throw th;
        }
    }

    /* renamed from: a */
    public static boolean m104a(C0228f fVar, SQLiteDatabase sQLiteDatabase, Collection<C0232i> collection) {
        try {
            sQLiteDatabase.beginTransaction();
            for (C0232i a : collection) {
                sQLiteDatabase.insert(C0196d.C0197a.f662a, (String) null, m94a(a));
            }
            sQLiteDatabase.setTransactionSuccessful();
            m107b(sQLiteDatabase, C0196d.C0197a.f663b);
            fVar.mo195a("success", false);
            sQLiteDatabase.endTransaction();
            return true;
        } catch (SQLException e) {
            C0138bw.m849e("insert to Aggregated cache table faild!");
            sQLiteDatabase.endTransaction();
            return false;
        } catch (Throwable th) {
            sQLiteDatabase.endTransaction();
            throw th;
        }
    }

    /* renamed from: a */
    public static boolean m101a(SQLiteDatabase sQLiteDatabase, C0228f fVar) {
        try {
            sQLiteDatabase.beginTransaction();
            if (m108c(sQLiteDatabase, C0196d.C0197a.f663b) <= 0) {
                fVar.mo195a("faild", false);
                return false;
            }
            sQLiteDatabase.execSQL("insert into aggregated(key, count, value, totalTimestamp, timeWindowNum, label) select key, count, value, totalTimestamp, timeWindowNum, label from aggregated_cache");
            sQLiteDatabase.setTransactionSuccessful();
            m107b(sQLiteDatabase, C0196d.C0197a.f663b);
            fVar.mo195a("success", false);
            sQLiteDatabase.endTransaction();
            return true;
        } catch (SQLException e) {
            fVar.mo195a(false, false);
            C0138bw.m849e("cacheToAggregatedTable happen " + e.toString());
            return false;
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }

    /* renamed from: a */
    private static ContentValues m94a(C0232i iVar) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", iVar.mo666a());
        contentValues.put("label", iVar.mo676c());
        contentValues.put("count", Long.valueOf(iVar.mo681g()));
        contentValues.put("value", Long.valueOf(iVar.mo680f()));
        contentValues.put(C0196d.C0197a.C0198a.f665b, Long.valueOf(iVar.mo679e()));
        contentValues.put(C0196d.C0197a.C0198a.f669f, iVar.mo682h());
        return contentValues;
    }

    /* renamed from: b */
    public static boolean m106b(SQLiteDatabase sQLiteDatabase, C0228f fVar) {
        Cursor cursor = null;
        try {
            HashMap hashMap = new HashMap();
            cursor = sQLiteDatabase.rawQuery("select * from aggregated_cache", (String[]) null);
            while (cursor.moveToNext()) {
                C0232i iVar = new C0232i();
                iVar.mo671a(C0196d.m1146b(cursor.getString(cursor.getColumnIndex("key"))));
                iVar.mo675b(C0196d.m1146b(cursor.getString(cursor.getColumnIndex("label"))));
                iVar.mo677c((long) cursor.getInt(cursor.getColumnIndex("count")));
                iVar.mo673b((long) cursor.getInt(cursor.getColumnIndex("value")));
                iVar.mo674b(cursor.getString(cursor.getColumnIndex(C0196d.C0197a.C0198a.f669f)));
                iVar.mo667a(Long.parseLong(cursor.getString(cursor.getColumnIndex(C0196d.C0197a.C0198a.f665b))));
                hashMap.put(C0196d.m1146b(cursor.getString(cursor.getColumnIndex("key"))), iVar);
            }
            if (hashMap.size() > 0) {
                fVar.mo195a(hashMap, false);
            } else {
                fVar.mo195a("faild", false);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            fVar.mo195a(false, false);
            C0138bw.m849e("cacheToMemory happen " + e.toString());
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return false;
    }

    /* renamed from: a */
    public static void m99a(SQLiteDatabase sQLiteDatabase, boolean z, C0228f fVar) {
        m107b(sQLiteDatabase, C0196d.C0203c.f679a);
        m107b(sQLiteDatabase, C0196d.C0197a.f662a);
        if (!z) {
            m107b(sQLiteDatabase, C0196d.C0200b.f676a);
            fVar.mo195a("success", false);
        }
    }

    /* renamed from: a */
    public static void m97a(SQLiteDatabase sQLiteDatabase, String str, long j, long j2) {
        try {
            int c = m108c(sQLiteDatabase, C0196d.C0203c.f679a);
            int c2 = C0253n.m1345a().mo714c();
            if (c < c2) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("key", str);
                contentValues.put(C0196d.C0203c.C0204a.f681b, Long.valueOf(j2));
                contentValues.put("count", Long.valueOf(j));
                sQLiteDatabase.insert(C0196d.C0203c.f679a, (String) null, contentValues);
            } else if (c == c2) {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("key", C0015a.f37x);
                contentValues2.put(C0196d.C0203c.C0204a.f681b, Long.valueOf(System.currentTimeMillis()));
                contentValues2.put("count", 1);
                sQLiteDatabase.insert(C0196d.C0203c.f679a, (String) null, contentValues2);
            } else {
                m110d(sQLiteDatabase, C0015a.f37x);
            }
        } catch (SQLException e) {
        }
    }

    /* renamed from: d */
    private static void m110d(SQLiteDatabase sQLiteDatabase, String str) {
        try {
            sQLiteDatabase.beginTransaction();
            sQLiteDatabase.execSQL("update system set count=count+1 where key like '" + str + "'");
            sQLiteDatabase.setTransactionSuccessful();
            if (sQLiteDatabase != null) {
                sQLiteDatabase.endTransaction();
            }
        } catch (SQLException e) {
            if (sQLiteDatabase != null) {
                sQLiteDatabase.endTransaction();
            }
        } catch (Throwable th) {
            if (sQLiteDatabase != null) {
                sQLiteDatabase.endTransaction();
            }
            throw th;
        }
    }

    /* renamed from: a */
    public static void m100a(C0228f fVar, SQLiteDatabase sQLiteDatabase, List<String> list) {
        try {
            sQLiteDatabase.beginTransaction();
            if (m108c(sQLiteDatabase, C0196d.C0200b.f676a) > 0) {
                m107b(sQLiteDatabase, C0196d.C0200b.f676a);
            }
            for (String put : list) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(C0196d.C0200b.C0201a.f677a, put);
                sQLiteDatabase.insert(C0196d.C0200b.f676a, (String) null, contentValues);
            }
            sQLiteDatabase.setTransactionSuccessful();
            fVar.mo195a("success", false);
        } catch (SQLException e) {
            C0138bw.m849e("insertToLimitCKTable error " + e.toString());
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }

    /* renamed from: a */
    public static void m98a(SQLiteDatabase sQLiteDatabase, Map<String, C0234k> map, C0228f fVar) {
        int i = 0;
        Cursor cursor = null;
        try {
            C0234k kVar = map.get(C0015a.f34u);
            if (kVar != null) {
                cursor = sQLiteDatabase.rawQuery("select * from " + "system where key=\"__ag_of\"", (String[]) null);
                cursor.moveToFirst();
                long j = 0;
                while (!cursor.isAfterLast()) {
                    if (cursor.getCount() > 0) {
                        i = cursor.getInt(cursor.getColumnIndex("count"));
                        j = cursor.getLong(cursor.getColumnIndex(C0196d.C0203c.C0204a.f681b));
                        sQLiteDatabase.execSQL("delete from " + "system where key=\"__ag_of\"");
                    }
                    cursor.moveToNext();
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("key", kVar.mo689c());
                contentValues.put("count", Long.valueOf(i == 0 ? kVar.mo691e() : ((long) i) + kVar.mo691e()));
                if (j == 0) {
                    j = kVar.mo690d();
                }
                contentValues.put(C0196d.C0203c.C0204a.f681b, Long.valueOf(j));
                sQLiteDatabase.insert(C0196d.C0203c.f679a, (String) null, contentValues);
                fVar.mo195a("success", false);
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            C0138bw.m849e("save to system table error " + e.toString());
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v11, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v13, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v14, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v20, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v21, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v22, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v23, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v24, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x006d  */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String m95a(android.database.sqlite.SQLiteDatabase r6) {
        /*
            r1 = 0
            r6.beginTransaction()     // Catch:{ SQLException -> 0x0040, all -> 0x0069 }
            java.lang.String r0 = "aggregated_cache"
            int r0 = m108c(r6, r0)     // Catch:{ SQLException -> 0x0040, all -> 0x0069 }
            if (r0 > 0) goto L_0x001b
            r0 = 0
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ SQLException -> 0x0040, all -> 0x0069 }
            if (r1 == 0) goto L_0x0017
            r1.close()
        L_0x0017:
            r6.endTransaction()
        L_0x001a:
            return r0
        L_0x001b:
            java.lang.String r0 = "select * from aggregated_cache"
            r2 = 0
            android.database.Cursor r2 = r6.rawQuery(r0, r2)     // Catch:{ SQLException -> 0x0040, all -> 0x0069 }
            boolean r0 = r2.moveToLast()     // Catch:{ SQLException -> 0x0076 }
            if (r0 == 0) goto L_0x007d
            java.lang.String r0 = "timeWindowNum"
            int r0 = r2.getColumnIndex(r0)     // Catch:{ SQLException -> 0x0076 }
            java.lang.String r0 = r2.getString(r0)     // Catch:{ SQLException -> 0x0076 }
        L_0x0034:
            r6.setTransactionSuccessful()     // Catch:{ SQLException -> 0x007b }
            if (r2 == 0) goto L_0x003c
            r2.close()
        L_0x003c:
            r6.endTransaction()
            goto L_0x001a
        L_0x0040:
            r0 = move-exception
            r2 = r1
            r5 = r0
            r0 = r1
            r1 = r5
        L_0x0045:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0074 }
            r3.<init>()     // Catch:{ all -> 0x0074 }
            java.lang.String r4 = "queryLastTimeWindowNumFromCache error "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x0074 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0074 }
            java.lang.StringBuilder r1 = r3.append(r1)     // Catch:{ all -> 0x0074 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0074 }
            com.umeng.analytics.pro.C0138bw.m849e((java.lang.String) r1)     // Catch:{ all -> 0x0074 }
            if (r2 == 0) goto L_0x0065
            r2.close()
        L_0x0065:
            r6.endTransaction()
            goto L_0x001a
        L_0x0069:
            r0 = move-exception
            r2 = r1
        L_0x006b:
            if (r2 == 0) goto L_0x0070
            r2.close()
        L_0x0070:
            r6.endTransaction()
            throw r0
        L_0x0074:
            r0 = move-exception
            goto L_0x006b
        L_0x0076:
            r0 = move-exception
            r5 = r0
            r0 = r1
            r1 = r5
            goto L_0x0045
        L_0x007b:
            r1 = move-exception
            goto L_0x0045
        L_0x007d:
            r0 = r1
            goto L_0x0034
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0030a.m95a(android.database.sqlite.SQLiteDatabase):java.lang.String");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: android.database.Cursor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: org.json.JSONObject} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00c6  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00db  */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.json.JSONObject m105b(android.database.sqlite.SQLiteDatabase r10) {
        /*
            r1 = 0
            java.lang.String r0 = "aggregated"
            int r0 = m108c(r10, r0)     // Catch:{ SQLException -> 0x00e1, all -> 0x00d7 }
            if (r0 <= 0) goto L_0x00d1
            java.lang.String r0 = "select * from aggregated"
            r2 = 0
            android.database.Cursor r2 = r10.rawQuery(r0, r2)     // Catch:{ SQLException -> 0x00e1, all -> 0x00d7 }
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ SQLException -> 0x00a8 }
            r0.<init>()     // Catch:{ SQLException -> 0x00a8 }
        L_0x0017:
            boolean r3 = r2.moveToNext()     // Catch:{ SQLException -> 0x00a8 }
            if (r3 == 0) goto L_0x00cb
            java.lang.String r3 = "key"
            int r3 = r2.getColumnIndex(r3)     // Catch:{ Exception -> 0x009f }
            java.lang.String r4 = r2.getString(r3)     // Catch:{ Exception -> 0x009f }
            boolean r3 = r0.has(r4)     // Catch:{ Exception -> 0x009f }
            if (r3 == 0) goto L_0x00a2
            org.json.JSONArray r3 = r0.getJSONArray(r4)     // Catch:{ Exception -> 0x009f }
            r0.remove(r4)     // Catch:{ Exception -> 0x009f }
        L_0x0035:
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Exception -> 0x009f }
            r5.<init>()     // Catch:{ Exception -> 0x009f }
            java.lang.String r6 = "v_sum"
            java.lang.String r7 = "value"
            int r7 = r2.getColumnIndex(r7)     // Catch:{ Exception -> 0x009f }
            long r8 = r2.getLong(r7)     // Catch:{ Exception -> 0x009f }
            r5.put(r6, r8)     // Catch:{ Exception -> 0x009f }
            java.lang.String r6 = "ts_sum"
            java.lang.String r7 = "totalTimestamp"
            int r7 = r2.getColumnIndex(r7)     // Catch:{ Exception -> 0x009f }
            long r8 = r2.getLong(r7)     // Catch:{ Exception -> 0x009f }
            r5.put(r6, r8)     // Catch:{ Exception -> 0x009f }
            java.lang.String r6 = "tw_num"
            java.lang.String r7 = "timeWindowNum"
            int r7 = r2.getColumnIndex(r7)     // Catch:{ Exception -> 0x009f }
            java.lang.String r7 = r2.getString(r7)     // Catch:{ Exception -> 0x009f }
            int r7 = java.lang.Integer.parseInt(r7)     // Catch:{ Exception -> 0x009f }
            r5.put(r6, r7)     // Catch:{ Exception -> 0x009f }
            java.lang.String r6 = "count"
            java.lang.String r7 = "count"
            int r7 = r2.getColumnIndex(r7)     // Catch:{ Exception -> 0x009f }
            int r7 = r2.getInt(r7)     // Catch:{ Exception -> 0x009f }
            r5.put(r6, r7)     // Catch:{ Exception -> 0x009f }
            java.lang.String r6 = "labels"
            java.lang.String r7 = "label"
            int r7 = r2.getColumnIndex(r7)     // Catch:{ Exception -> 0x009f }
            java.lang.String r7 = r2.getString(r7)     // Catch:{ Exception -> 0x009f }
            org.json.JSONArray r7 = com.umeng.analytics.pro.C0196d.m1145a((java.lang.String) r7)     // Catch:{ Exception -> 0x009f }
            r5.put(r6, r7)     // Catch:{ Exception -> 0x009f }
            r3.put(r5)     // Catch:{ Exception -> 0x009f }
            r0.put(r4, r3)     // Catch:{ Exception -> 0x009f }
            goto L_0x0017
        L_0x009f:
            r3 = move-exception
            goto L_0x0017
        L_0x00a2:
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ Exception -> 0x009f }
            r3.<init>()     // Catch:{ Exception -> 0x009f }
            goto L_0x0035
        L_0x00a8:
            r0 = move-exception
        L_0x00a9:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00df }
            r3.<init>()     // Catch:{ all -> 0x00df }
            java.lang.String r4 = "readAllAggregatedDataForUpload error "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x00df }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00df }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ all -> 0x00df }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00df }
            com.umeng.analytics.pro.C0138bw.m849e((java.lang.String) r0)     // Catch:{ all -> 0x00df }
            if (r2 == 0) goto L_0x00c9
            r2.close()
        L_0x00c9:
            r0 = r1
        L_0x00ca:
            return r0
        L_0x00cb:
            if (r2 == 0) goto L_0x00ca
            r2.close()
            goto L_0x00ca
        L_0x00d1:
            if (r1 == 0) goto L_0x00c9
            r1.close()
            goto L_0x00c9
        L_0x00d7:
            r0 = move-exception
            r2 = r1
        L_0x00d9:
            if (r2 == 0) goto L_0x00de
            r2.close()
        L_0x00de:
            throw r0
        L_0x00df:
            r0 = move-exception
            goto L_0x00d9
        L_0x00e1:
            r0 = move-exception
            r2 = r1
            goto L_0x00a9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0030a.m105b(android.database.sqlite.SQLiteDatabase):org.json.JSONObject");
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a3  */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.json.JSONObject m96a(com.umeng.analytics.pro.C0228f r11, android.database.sqlite.SQLiteDatabase r12) {
        /*
            r1 = 0
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ SQLException -> 0x00ad, all -> 0x009f }
            r2.<init>()     // Catch:{ SQLException -> 0x00ad, all -> 0x009f }
            java.lang.String r0 = "system"
            int r0 = m108c(r12, r0)     // Catch:{ SQLException -> 0x00ad, all -> 0x009f }
            if (r0 <= 0) goto L_0x0097
            java.lang.String r0 = "select * from system"
            r3 = 0
            android.database.Cursor r0 = r12.rawQuery(r0, r3)     // Catch:{ SQLException -> 0x00ad, all -> 0x009f }
        L_0x0017:
            boolean r3 = r0.moveToNext()     // Catch:{ SQLException -> 0x006b, all -> 0x00a7 }
            if (r3 == 0) goto L_0x0098
            java.lang.String r3 = "key"
            int r3 = r0.getColumnIndex(r3)     // Catch:{ Exception -> 0x0063 }
            java.lang.String r4 = r0.getString(r3)     // Catch:{ Exception -> 0x0063 }
            boolean r3 = r2.has(r4)     // Catch:{ Exception -> 0x0063 }
            if (r3 == 0) goto L_0x0065
            org.json.JSONArray r3 = r2.getJSONArray(r4)     // Catch:{ Exception -> 0x0063 }
            r2.remove(r4)     // Catch:{ Exception -> 0x0063 }
        L_0x0035:
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Exception -> 0x0063 }
            r5.<init>()     // Catch:{ Exception -> 0x0063 }
            java.lang.String r6 = "value"
            java.lang.String r7 = "count"
            int r7 = r0.getColumnIndex(r7)     // Catch:{ Exception -> 0x0063 }
            int r7 = r0.getInt(r7)     // Catch:{ Exception -> 0x0063 }
            r5.put(r6, r7)     // Catch:{ Exception -> 0x0063 }
            java.lang.String r6 = "ts"
            java.lang.String r7 = "timeStamp"
            int r7 = r0.getColumnIndex(r7)     // Catch:{ Exception -> 0x0063 }
            long r8 = r0.getLong(r7)     // Catch:{ Exception -> 0x0063 }
            r5.put(r6, r8)     // Catch:{ Exception -> 0x0063 }
            r3.put(r5)     // Catch:{ Exception -> 0x0063 }
            r2.put(r4, r3)     // Catch:{ Exception -> 0x0063 }
            goto L_0x0017
        L_0x0063:
            r3 = move-exception
            goto L_0x0017
        L_0x0065:
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ Exception -> 0x0063 }
            r3.<init>()     // Catch:{ Exception -> 0x0063 }
            goto L_0x0035
        L_0x006b:
            r2 = move-exception
            r10 = r2
            r2 = r0
            r0 = r10
        L_0x006f:
            java.lang.String r3 = "faild"
            r4 = 0
            r11.mo195a(r3, r4)     // Catch:{ all -> 0x00ab }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ab }
            r3.<init>()     // Catch:{ all -> 0x00ab }
            java.lang.String r4 = "readAllSystemDataForUpload error "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x00ab }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00ab }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ all -> 0x00ab }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00ab }
            com.umeng.analytics.pro.C0138bw.m849e((java.lang.String) r0)     // Catch:{ all -> 0x00ab }
            if (r2 == 0) goto L_0x0096
            r2.close()
        L_0x0096:
            return r1
        L_0x0097:
            r0 = r1
        L_0x0098:
            if (r0 == 0) goto L_0x009d
            r0.close()
        L_0x009d:
            r1 = r2
            goto L_0x0096
        L_0x009f:
            r0 = move-exception
            r2 = r1
        L_0x00a1:
            if (r2 == 0) goto L_0x00a6
            r2.close()
        L_0x00a6:
            throw r0
        L_0x00a7:
            r1 = move-exception
            r2 = r0
            r0 = r1
            goto L_0x00a1
        L_0x00ab:
            r0 = move-exception
            goto L_0x00a1
        L_0x00ad:
            r0 = move-exception
            r2 = r1
            goto L_0x006f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0030a.m96a(com.umeng.analytics.pro.f, android.database.sqlite.SQLiteDatabase):org.json.JSONObject");
    }

    /* JADX WARNING: type inference failed for: r0v4, types: [java.util.List<java.lang.String>] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005f  */
    /* renamed from: c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<java.lang.String> m109c(android.database.sqlite.SQLiteDatabase r5) {
        /*
            r1 = 0
            java.lang.String r0 = "limitedck"
            int r0 = m108c(r5, r0)     // Catch:{ SQLException -> 0x0065, all -> 0x005b }
            if (r0 <= 0) goto L_0x0055
            java.lang.String r0 = "select * from limitedck"
            r2 = 0
            android.database.Cursor r2 = r5.rawQuery(r0, r2)     // Catch:{ SQLException -> 0x0065, all -> 0x005b }
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ SQLException -> 0x002c }
            r0.<init>()     // Catch:{ SQLException -> 0x002c }
        L_0x0017:
            boolean r3 = r2.moveToNext()     // Catch:{ SQLException -> 0x002c }
            if (r3 == 0) goto L_0x004f
            java.lang.String r3 = "ck"
            int r3 = r2.getColumnIndex(r3)     // Catch:{ SQLException -> 0x002c }
            java.lang.String r3 = r2.getString(r3)     // Catch:{ SQLException -> 0x002c }
            r0.add(r3)     // Catch:{ SQLException -> 0x002c }
            goto L_0x0017
        L_0x002c:
            r0 = move-exception
        L_0x002d:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0063 }
            r3.<init>()     // Catch:{ all -> 0x0063 }
            java.lang.String r4 = "loadLimitCKFromDB error "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x0063 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0063 }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ all -> 0x0063 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0063 }
            com.umeng.analytics.pro.C0138bw.m849e((java.lang.String) r0)     // Catch:{ all -> 0x0063 }
            if (r2 == 0) goto L_0x004d
            r2.close()
        L_0x004d:
            r0 = r1
        L_0x004e:
            return r0
        L_0x004f:
            if (r2 == 0) goto L_0x004e
            r2.close()
            goto L_0x004e
        L_0x0055:
            if (r1 == 0) goto L_0x004d
            r1.close()
            goto L_0x004d
        L_0x005b:
            r0 = move-exception
            r2 = r1
        L_0x005d:
            if (r2 == 0) goto L_0x0062
            r2.close()
        L_0x0062:
            throw r0
        L_0x0063:
            r0 = move-exception
            goto L_0x005d
        L_0x0065:
            r0 = move-exception
            r2 = r1
            goto L_0x002d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0030a.m109c(android.database.sqlite.SQLiteDatabase):java.util.List");
    }
}
