package org.xutils.common.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ParameterizedTypeUtil {
    private ParameterizedTypeUtil() {
    }

    public static Type getParameterizedType(Type ownerType, Class<?> declaredClass, int paramIndex) {
        Class<?> clazz;
        Type[] ats = null;
        TypeVariable<?>[] tps = null;
        if (ownerType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) ownerType;
            clazz = (Class) pt.getRawType();
            ats = pt.getActualTypeArguments();
            tps = clazz.getTypeParameters();
        } else {
            clazz = (Class) ownerType;
        }
        if (declaredClass != clazz) {
            Type[] types = clazz.getGenericInterfaces();
            if (types != null) {
                for (Type t : types) {
                    if ((t instanceof ParameterizedType) && declaredClass.isAssignableFrom((Class) ((ParameterizedType) t).getRawType())) {
                        try {
                            return getTrueType(getParameterizedType(t, declaredClass, paramIndex), tps, ats);
                        } catch (Throwable th) {
                        }
                    }
                }
            }
            Class<? super Object> superclass = clazz.getSuperclass();
            if (superclass != null && declaredClass.isAssignableFrom(superclass)) {
                return getTrueType(getParameterizedType(clazz.getGenericSuperclass(), declaredClass, paramIndex), tps, ats);
            }
            throw new IllegalArgumentException("FindGenericType:" + ownerType + ", declaredClass: " + declaredClass + ", index: " + paramIndex);
        } else if (ats != null) {
            return ats[paramIndex];
        } else {
            return Object.class;
        }
    }

    private static Type getTrueType(Type type, TypeVariable<?>[] typeVariables, Type[] actualTypes) {
        if (type instanceof TypeVariable) {
            TypeVariable<?> tv = (TypeVariable) type;
            String name = tv.getName();
            if (actualTypes == null) {
                return tv;
            }
            for (int i = 0; i < typeVariables.length; i++) {
                if (name.equals(typeVariables[i].getName())) {
                    return actualTypes[i];
                }
            }
            return tv;
        }
        if (type instanceof GenericArrayType) {
            Type ct = ((GenericArrayType) type).getGenericComponentType();
            if (ct instanceof Class) {
                return Array.newInstance((Class) ct, 0).getClass();
            }
        }
        return type;
    }
}
