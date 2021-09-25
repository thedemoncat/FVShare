package com.umeng.analytics.pro;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.as */
/* compiled from: EventTracker */
public class C0058as {

    /* renamed from: a */
    private static final String f233a = "fs_lc_tl";

    /* renamed from: b */
    private final int f234b = 128;

    /* renamed from: c */
    private final int f235c = 256;

    /* renamed from: d */
    private final int f236d = 10;
    /* access modifiers changed from: private */

    /* renamed from: e */
    public Context f237e;

    /* renamed from: f */
    private C0053aq f238f = null;

    /* renamed from: g */
    private C0050ap f239g = null;

    /* renamed from: h */
    private JSONObject f240h = null;

    /* renamed from: i */
    private C0053aq f241i;

    public C0058as(Context context) {
        if (context == null) {
            try {
                C0138bw.m849e("Context is null, can't track event");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.f241i = C0053aq.m237b(context);
        this.f237e = context;
        this.f238f = C0053aq.m237b(this.f237e);
        this.f239g = this.f238f.mo185a(this.f237e);
        if (this.f240h == null) {
            m251a(this.f237e);
        }
    }

    /* renamed from: a */
    public void mo192a(String str, Map<String, Object> map, long j) {
        try {
            if (m254a(str) && m257b(map)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("id", str);
                jSONObject.put("ts", System.currentTimeMillis());
                if (j > 0) {
                    jSONObject.put(C0281x.f915aN, j);
                }
                jSONObject.put("__t", C0277w.f860a);
                Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                for (int i = 0; i < 10 && it.hasNext(); i++) {
                    Map.Entry next = it.next();
                    if (!C0281x.f918aQ.equals(next.getKey()) && !C0281x.f916aO.equals(next.getKey()) && !C0281x.f915aN.equals(next.getKey()) && !"id".equals(next.getKey()) && !"ts".equals(next.getKey())) {
                        Object value = next.getValue();
                        if ((value instanceof String) || (value instanceof Integer) || (value instanceof Long)) {
                            jSONObject.put((String) next.getKey(), value);
                        }
                    }
                }
                jSONObject.put("_umpname", C0048ao.f191a);
                if (!TextUtils.isEmpty(C0071bb.m311g(this.f237e))) {
                    jSONObject.put("__i", C0071bb.m311g(this.f237e));
                }
                this.f241i.mo178a((Object) jSONObject);
            }
        } catch (Throwable th) {
        }
    }

    /* renamed from: a */
    public void mo190a(String str, String str2, long j, int i) {
        try {
            if (m254a(str) && m256b(str2)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("id", str);
                jSONObject.put("ts", System.currentTimeMillis());
                if (j > 0) {
                    jSONObject.put(C0281x.f915aN, j);
                }
                jSONObject.put("__t", C0277w.f860a);
                if (str2 == null) {
                    str2 = "";
                }
                jSONObject.put(str, str2);
                if (!TextUtils.isEmpty(C0071bb.m311g(this.f237e))) {
                    jSONObject.put("__i", C0071bb.m311g(this.f237e));
                }
                jSONObject.put("_umpname", C0048ao.f191a);
                this.f241i.mo178a((Object) jSONObject);
            }
        } catch (Throwable th) {
        }
    }

    /* renamed from: a */
    public void mo191a(String str, Map<String, Object> map) {
    }

    /* renamed from: b */
    public void mo194b(String str, Map<String, Object> map) {
        try {
            if (m254a(str)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("id", str);
                jSONObject.put("ts", System.currentTimeMillis());
                jSONObject.put(C0281x.f915aN, 0);
                jSONObject.put("__t", C0277w.f861b);
                Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                for (int i = 0; i < 10 && it.hasNext(); i++) {
                    Map.Entry next = it.next();
                    if (!C0281x.f918aQ.equals(next.getKey()) && !C0281x.f916aO.equals(next.getKey()) && !C0281x.f915aN.equals(next.getKey()) && !"id".equals(next.getKey()) && !"ts".equals(next.getKey())) {
                        Object value = next.getValue();
                        if ((value instanceof String) || (value instanceof Integer) || (value instanceof Long)) {
                            jSONObject.put((String) next.getKey(), value);
                        }
                    }
                }
                if (!TextUtils.isEmpty(C0071bb.m311g(this.f237e))) {
                    jSONObject.put("__i", C0071bb.m311g(this.f237e));
                }
                this.f241i.mo178a((Object) jSONObject);
            }
        } catch (Throwable th) {
        }
    }

    /* renamed from: a */
    public boolean mo193a(List<String> list, int i, String str) {
        String str2;
        try {
            C0253n a = C0253n.m1345a();
            if (list == null) {
                C0138bw.m849e("cklist is null!");
            } else if (list.size() <= 0) {
                C0138bw.m849e("the KeyList is null!");
                return false;
            } else {
                ArrayList arrayList = new ArrayList(list);
                if (!a.mo712b((String) arrayList.get(0))) {
                    C0138bw.m849e("Primary key Invalid!");
                    return false;
                }
                if (arrayList.size() > 8) {
                    arrayList.clear();
                    arrayList.add((String) arrayList.get(0));
                    arrayList.add("__cc");
                    arrayList.add("illegal");
                } else if (!a.mo710a((List<String>) arrayList)) {
                    arrayList.clear();
                    arrayList.add((String) arrayList.get(0));
                    arrayList.add("__cc");
                    arrayList.add("illegal");
                } else if (!a.mo713b((List<String>) arrayList)) {
                    arrayList.clear();
                    arrayList.add((String) arrayList.get(0));
                    arrayList.add("__cc");
                    arrayList.add("illegal");
                } else {
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        String str3 = (String) arrayList.get(i2);
                        if (str3.length() > 16) {
                            arrayList.remove(i2);
                            arrayList.add(i2, str3.substring(0, 16));
                        }
                    }
                }
                if (!a.mo709a(str)) {
                    C0138bw.m849e("label  Invalid!");
                    str2 = "__illegal";
                } else {
                    str2 = str;
                }
                final HashMap hashMap = new HashMap();
                hashMap.put(arrayList, new C0235l(arrayList, (long) i, str2, System.currentTimeMillis()));
                C0139bx.m859b(new C0151bz() {
                    /* renamed from: a */
                    public void mo87a() {
                        C0236m.m1296a(C0058as.this.f237e).mo699a((C0228f) new C0228f() {
                            /* renamed from: a */
                            public void mo195a(Object obj, boolean z) {
                                if (obj.equals("success")) {
                                }
                            }
                        }, (Map<List<String>, C0235l>) hashMap);
                    }
                });
            }
        } catch (Exception e) {
        }
        return true;
    }

    /* renamed from: a */
    private void m251a(Context context) {
        try {
            String string = C0067az.m285a(context).getString(f233a, (String) null);
            if (!TextUtils.isEmpty(string)) {
                this.f240h = new JSONObject(string);
            }
            m250a();
        } catch (Exception e) {
        }
    }

    /* renamed from: a */
    private void m250a() {
        int i = 0;
        try {
            if (!TextUtils.isEmpty(this.f239g.f201a)) {
                String[] split = this.f239g.f201a.split("!");
                JSONObject jSONObject = new JSONObject();
                if (this.f240h != null) {
                    for (String a : split) {
                        String a2 = C0136bu.m814a(a, 128);
                        if (this.f240h.has(a2)) {
                            jSONObject.put(a2, this.f240h.get(a2));
                        }
                    }
                }
                this.f240h = new JSONObject();
                if (split.length >= 10) {
                    while (i < 10) {
                        m252a(split[i], jSONObject);
                        i++;
                    }
                } else {
                    while (i < split.length) {
                        m252a(split[i], jSONObject);
                        i++;
                    }
                }
                m255b(this.f237e);
                this.f239g.f201a = null;
            }
        } catch (Exception e) {
        }
    }

    /* renamed from: a */
    private void m252a(String str, JSONObject jSONObject) throws JSONException {
        String a = C0136bu.m814a(str, 128);
        if (jSONObject.has(a)) {
            m253a(a, ((Boolean) jSONObject.get(a)).booleanValue());
        } else {
            m253a(a, false);
        }
    }

    /* renamed from: a */
    private void m253a(String str, boolean z) {
        try {
            if (!C0281x.f918aQ.equals(str) && !C0281x.f916aO.equals(str) && !C0281x.f915aN.equals(str) && !"id".equals(str) && !"ts".equals(str) && !this.f240h.has(str)) {
                this.f240h.put(str, z);
            }
        } catch (Exception e) {
        }
    }

    /* renamed from: b */
    private void m255b(Context context) {
        try {
            if (this.f240h != null) {
                C0067az.m285a(this.f237e).edit().putString(f233a, this.f240h.toString()).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: a */
    public void mo189a(Context context, List<String> list) {
        try {
            if (this.f237e == null && context != null) {
                this.f237e = context;
            }
        } catch (Exception e) {
        }
    }

    /* renamed from: a */
    private JSONObject m249a(Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject();
        try {
            for (Map.Entry next : map.entrySet()) {
                try {
                    String str = (String) next.getKey();
                    if (str != null) {
                        String a = C0136bu.m814a(str, 128);
                        Object value = next.getValue();
                        if (value != null) {
                            if (value.getClass().isArray()) {
                                if (value instanceof int[]) {
                                    int[] iArr = (int[]) value;
                                    ArrayList arrayList = new ArrayList();
                                    for (int valueOf : iArr) {
                                        arrayList.add(Integer.valueOf(valueOf));
                                    }
                                    jSONObject.put(a, arrayList);
                                } else if (value instanceof double[]) {
                                    double[] dArr = (double[]) value;
                                    ArrayList arrayList2 = new ArrayList();
                                    for (double valueOf2 : dArr) {
                                        arrayList2.add(Double.valueOf(valueOf2));
                                    }
                                    jSONObject.put(a, arrayList2);
                                } else if (value instanceof long[]) {
                                    long[] jArr = (long[]) value;
                                    ArrayList arrayList3 = new ArrayList();
                                    for (long valueOf3 : jArr) {
                                        arrayList3.add(Long.valueOf(valueOf3));
                                    }
                                    jSONObject.put(a, arrayList3);
                                } else if (value instanceof float[]) {
                                    float[] fArr = (float[]) value;
                                    ArrayList arrayList4 = new ArrayList();
                                    for (float valueOf4 : fArr) {
                                        arrayList4.add(Float.valueOf(valueOf4));
                                    }
                                    jSONObject.put(a, arrayList4);
                                } else if (value instanceof boolean[]) {
                                    boolean[] zArr = (boolean[]) value;
                                    ArrayList arrayList5 = new ArrayList();
                                    for (boolean valueOf5 : zArr) {
                                        arrayList5.add(Boolean.valueOf(valueOf5));
                                    }
                                    jSONObject.put(a, arrayList5);
                                } else if (value instanceof byte[]) {
                                    byte[] bArr = (byte[]) value;
                                    ArrayList arrayList6 = new ArrayList();
                                    for (byte valueOf6 : bArr) {
                                        arrayList6.add(Byte.valueOf(valueOf6));
                                    }
                                    jSONObject.put(a, arrayList6);
                                } else if (value instanceof short[]) {
                                    short[] sArr = (short[]) value;
                                    ArrayList arrayList7 = new ArrayList();
                                    for (short valueOf7 : sArr) {
                                        arrayList7.add(Short.valueOf(valueOf7));
                                    }
                                    jSONObject.put(a, arrayList7);
                                } else if (value instanceof char[]) {
                                    char[] cArr = (char[]) value;
                                    ArrayList arrayList8 = new ArrayList();
                                    for (char valueOf8 : cArr) {
                                        arrayList8.add(Character.valueOf(valueOf8));
                                    }
                                    jSONObject.put(a, arrayList8);
                                } else {
                                    jSONObject.put(a, new ArrayList(Arrays.asList((Object[]) value)));
                                }
                            } else if (value instanceof String) {
                                jSONObject.put(a, C0136bu.m814a(value.toString(), 256));
                            } else {
                                jSONObject.put(a, value);
                            }
                        }
                    }
                } catch (Exception e) {
                    C0138bw.m853e((Throwable) e);
                }
            }
        } catch (Exception e2) {
        }
        return jSONObject;
    }

    /* renamed from: a */
    private boolean m254a(String str) {
        if (str != null) {
            try {
                int length = str.trim().getBytes().length;
                if (length > 0 && length <= 128) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        C0138bw.m849e("Event id is empty or too long in tracking Event");
        return false;
    }

    /* renamed from: b */
    private boolean m256b(String str) {
        if (str == null) {
            return true;
        }
        try {
            if (str.trim().getBytes().length <= 256) {
                return true;
            }
            C0138bw.m849e("Event label or value is empty or too long in tracking Event");
            return false;
        } catch (Exception e) {
        }
    }

    /* renamed from: b */
    private boolean m257b(Map<String, Object> map) {
        if (map != null) {
            try {
                if (!map.isEmpty()) {
                    for (Map.Entry next : map.entrySet()) {
                        if (!m254a((String) next.getKey())) {
                            return false;
                        }
                        if (next.getValue() == null) {
                            return false;
                        }
                        if ((next.getValue() instanceof String) && !m256b(next.getValue().toString())) {
                            return false;
                        }
                    }
                    return true;
                }
            } catch (Exception e) {
            }
        }
        C0138bw.m849e("map is null or empty in onEvent");
        return false;
    }
}
