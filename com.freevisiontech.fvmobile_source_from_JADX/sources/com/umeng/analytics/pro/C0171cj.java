package com.umeng.analytics.pro;

import java.lang.reflect.InvocationTargetException;

/* renamed from: com.umeng.analytics.pro.cj */
/* compiled from: TEnumHelper */
public class C0171cj {
    /* renamed from: a */
    public static C0170ci m984a(Class<? extends C0170ci> cls, int i) {
        try {
            return (C0170ci) cls.getMethod("findByValue", new Class[]{Integer.TYPE}).invoke((Object) null, new Object[]{Integer.valueOf(i)});
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e2) {
            return null;
        } catch (InvocationTargetException e3) {
            return null;
        }
    }
}
