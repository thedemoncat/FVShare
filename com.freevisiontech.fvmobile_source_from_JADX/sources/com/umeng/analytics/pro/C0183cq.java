package com.umeng.analytics.pro;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.umeng.analytics.pro.cq */
/* compiled from: FieldMetaData */
public class C0183cq implements Serializable {

    /* renamed from: d */
    private static Map<Class<? extends C0164ce>, Map<? extends C0173cl, C0183cq>> f590d = new HashMap();

    /* renamed from: a */
    public final String f591a;

    /* renamed from: b */
    public final byte f592b;

    /* renamed from: c */
    public final C0184cr f593c;

    public C0183cq(String str, byte b, C0184cr crVar) {
        this.f591a = str;
        this.f592b = b;
        this.f593c = crVar;
    }

    /* renamed from: a */
    public static void m1027a(Class<? extends C0164ce> cls, Map<? extends C0173cl, C0183cq> map) {
        f590d.put(cls, map);
    }

    /* renamed from: a */
    public static Map<? extends C0173cl, C0183cq> m1026a(Class<? extends C0164ce> cls) {
        if (!f590d.containsKey(cls)) {
            try {
                cls.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("InstantiationException for TBase class: " + cls.getName() + ", message: " + e.getMessage());
            } catch (IllegalAccessException e2) {
                throw new RuntimeException("IllegalAccessException for TBase class: " + cls.getName() + ", message: " + e2.getMessage());
            }
        }
        return f590d.get(cls);
    }
}
