package com.umeng.analytics.pro;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;

/* renamed from: com.umeng.analytics.pro.d */
/* compiled from: UMCCDBUtils */
public class C0196d {

    /* renamed from: a */
    public static final String f656a = "/data/data/";

    /* renamed from: b */
    public static final String f657b = "/databases/cc/";

    /* renamed from: c */
    public static final String f658c = "cc.db";

    /* renamed from: d */
    public static final int f659d = 1;

    /* renamed from: e */
    public static final String f660e = "Id";

    /* renamed from: f */
    public static final String f661f = "INTEGER";

    /* renamed from: com.umeng.analytics.pro.d$a */
    /* compiled from: UMCCDBUtils */
    public static class C0197a {

        /* renamed from: a */
        public static final String f662a = "aggregated";

        /* renamed from: b */
        public static final String f663b = "aggregated_cache";

        /* renamed from: com.umeng.analytics.pro.d$a$a */
        /* compiled from: UMCCDBUtils */
        public static class C0198a {

            /* renamed from: a */
            public static final String f664a = "key";

            /* renamed from: b */
            public static final String f665b = "totalTimestamp";

            /* renamed from: c */
            public static final String f666c = "value";

            /* renamed from: d */
            public static final String f667d = "count";

            /* renamed from: e */
            public static final String f668e = "label";

            /* renamed from: f */
            public static final String f669f = "timeWindowNum";
        }

        /* renamed from: com.umeng.analytics.pro.d$a$b */
        /* compiled from: UMCCDBUtils */
        public static class C0199b {

            /* renamed from: a */
            public static final String f670a = "TEXT";

            /* renamed from: b */
            public static final String f671b = "TEXT";

            /* renamed from: c */
            public static final String f672c = "INTEGER";

            /* renamed from: d */
            public static final String f673d = "INTEGER";

            /* renamed from: e */
            public static final String f674e = "TEXT";

            /* renamed from: f */
            public static final String f675f = "TEXT";
        }
    }

    /* renamed from: com.umeng.analytics.pro.d$b */
    /* compiled from: UMCCDBUtils */
    public static class C0200b {

        /* renamed from: a */
        public static final String f676a = "limitedck";

        /* renamed from: com.umeng.analytics.pro.d$b$a */
        /* compiled from: UMCCDBUtils */
        public static class C0201a {

            /* renamed from: a */
            public static final String f677a = "ck";
        }

        /* renamed from: com.umeng.analytics.pro.d$b$b */
        /* compiled from: UMCCDBUtils */
        public static class C0202b {

            /* renamed from: a */
            public static final String f678a = "TEXT";
        }
    }

    /* renamed from: com.umeng.analytics.pro.d$c */
    /* compiled from: UMCCDBUtils */
    public static class C0203c {

        /* renamed from: a */
        public static final String f679a = "system";

        /* renamed from: com.umeng.analytics.pro.d$c$a */
        /* compiled from: UMCCDBUtils */
        public static class C0204a {

            /* renamed from: a */
            public static final String f680a = "key";

            /* renamed from: b */
            public static final String f681b = "timeStamp";

            /* renamed from: c */
            public static final String f682c = "count";

            /* renamed from: d */
            public static final String f683d = "label";
        }

        /* renamed from: com.umeng.analytics.pro.d$c$b */
        /* compiled from: UMCCDBUtils */
        public static class C0205b {

            /* renamed from: a */
            public static final String f684a = "TEXT";

            /* renamed from: b */
            public static final String f685b = "INTEGER";

            /* renamed from: c */
            public static final String f686c = "INTEGER";

            /* renamed from: d */
            public static final String f687d = "TEXT";
        }
    }

    /* renamed from: a */
    public static String m1143a(Context context) {
        return "/data/data/" + context.getPackageName() + f657b;
    }

    /* renamed from: a */
    public static String m1144a(List<String> list) {
        return TextUtils.join("!", list);
    }

    /* renamed from: a */
    public static JSONArray m1145a(String str) {
        String[] split = str.split("!");
        JSONArray jSONArray = new JSONArray();
        for (String put : split) {
            jSONArray.put(put);
        }
        return jSONArray;
    }

    /* renamed from: b */
    public static List<String> m1146b(String str) {
        return new ArrayList(Arrays.asList(str.split("!")));
    }

    /* renamed from: b */
    public static List<String> m1147b(List<String> list) {
        ArrayList arrayList = new ArrayList();
        try {
            for (String next : list) {
                if (Collections.frequency(arrayList, next) < 1) {
                    arrayList.add(next);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
