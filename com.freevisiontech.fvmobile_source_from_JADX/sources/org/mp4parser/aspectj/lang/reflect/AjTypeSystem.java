package org.mp4parser.aspectj.lang.reflect;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import org.mp4parser.aspectj.internal.lang.reflect.AjTypeImpl;

public class AjTypeSystem {
    private static Map<Class, WeakReference<AjType>> ajTypes = Collections.synchronizedMap(new WeakHashMap());

    public static <T> AjType<T> getAjType(Class<T> fromClass) {
        WeakReference<AjType> weakRefToAjType = ajTypes.get(fromClass);
        if (weakRefToAjType != null) {
            AjType<T> theAjType = (AjType) weakRefToAjType.get();
            if (theAjType != null) {
                return theAjType;
            }
            AjType<T> theAjType2 = new AjTypeImpl<>(fromClass);
            ajTypes.put(fromClass, new WeakReference(theAjType2));
            return theAjType2;
        }
        AjType<T> theAjType3 = new AjTypeImpl<>(fromClass);
        ajTypes.put(fromClass, new WeakReference(theAjType3));
        return theAjType3;
    }
}
