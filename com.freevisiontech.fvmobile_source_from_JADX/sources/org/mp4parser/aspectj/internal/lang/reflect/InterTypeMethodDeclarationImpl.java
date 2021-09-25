package org.mp4parser.aspectj.internal.lang.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.mp4parser.aspectj.lang.reflect.AjType;
import org.mp4parser.aspectj.lang.reflect.AjTypeSystem;
import org.mp4parser.aspectj.lang.reflect.InterTypeMethodDeclaration;

public class InterTypeMethodDeclarationImpl extends InterTypeDeclarationImpl implements InterTypeMethodDeclaration {
    private Method baseMethod;
    private AjType<?>[] exceptionTypes;
    private Type[] genericParameterTypes;
    private Type genericReturnType;
    private String name;
    private int parameterAdjustmentFactor;
    private AjType<?>[] parameterTypes;
    private AjType<?> returnType;

    public InterTypeMethodDeclarationImpl(AjType<?> decType, String target, int mods, String name2, Method itdInterMethod) {
        super(decType, target, mods);
        this.parameterAdjustmentFactor = 1;
        this.name = name2;
        this.baseMethod = itdInterMethod;
    }

    public InterTypeMethodDeclarationImpl(AjType<?> decType, AjType<?> targetType, Method base, int modifiers) {
        super(decType, targetType, modifiers);
        this.parameterAdjustmentFactor = 1;
        this.parameterAdjustmentFactor = 0;
        this.name = base.getName();
        this.baseMethod = base;
    }

    public String getName() {
        return this.name;
    }

    public AjType<?> getReturnType() {
        return AjTypeSystem.getAjType(this.baseMethod.getReturnType());
    }

    public Type getGenericReturnType() {
        Type gRet = this.baseMethod.getGenericReturnType();
        if (gRet instanceof Class) {
            return AjTypeSystem.getAjType((Class) gRet);
        }
        return gRet;
    }

    public AjType<?>[] getParameterTypes() {
        Class<?>[] baseTypes = this.baseMethod.getParameterTypes();
        AjType<?>[] ret = new AjType[(baseTypes.length - this.parameterAdjustmentFactor)];
        for (int i = this.parameterAdjustmentFactor; i < baseTypes.length; i++) {
            ret[i - this.parameterAdjustmentFactor] = AjTypeSystem.getAjType(baseTypes[i]);
        }
        return ret;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: org.mp4parser.aspectj.lang.reflect.AjType[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.reflect.Type[] getGenericParameterTypes() {
        /*
            r5 = this;
            java.lang.reflect.Method r3 = r5.baseMethod
            java.lang.reflect.Type[] r0 = r3.getGenericParameterTypes()
            int r3 = r0.length
            int r4 = r5.parameterAdjustmentFactor
            int r3 = r3 - r4
            org.mp4parser.aspectj.lang.reflect.AjType[] r2 = new org.mp4parser.aspectj.lang.reflect.AjType[r3]
            int r1 = r5.parameterAdjustmentFactor
        L_0x000e:
            int r3 = r0.length
            if (r1 >= r3) goto L_0x0031
            r3 = r0[r1]
            boolean r3 = r3 instanceof java.lang.Class
            if (r3 == 0) goto L_0x0028
            int r3 = r5.parameterAdjustmentFactor
            int r4 = r1 - r3
            r3 = r0[r1]
            java.lang.Class r3 = (java.lang.Class) r3
            org.mp4parser.aspectj.lang.reflect.AjType r3 = org.mp4parser.aspectj.lang.reflect.AjTypeSystem.getAjType(r3)
            r2[r4] = r3
        L_0x0025:
            int r1 = r1 + 1
            goto L_0x000e
        L_0x0028:
            int r3 = r5.parameterAdjustmentFactor
            int r3 = r1 - r3
            r4 = r0[r1]
            r2[r3] = r4
            goto L_0x0025
        L_0x0031:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mp4parser.aspectj.internal.lang.reflect.InterTypeMethodDeclarationImpl.getGenericParameterTypes():java.lang.reflect.Type[]");
    }

    public TypeVariable<Method>[] getTypeParameters() {
        return this.baseMethod.getTypeParameters();
    }

    public AjType<?>[] getExceptionTypes() {
        Class<?>[] baseTypes = this.baseMethod.getExceptionTypes();
        AjType<?>[] ret = new AjType[baseTypes.length];
        for (int i = 0; i < baseTypes.length; i++) {
            ret[i] = AjTypeSystem.getAjType(baseTypes[i]);
        }
        return ret;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(Modifier.toString(getModifiers()));
        sb.append(" ");
        sb.append(getReturnType().toString());
        sb.append(" ");
        sb.append(this.targetTypeName);
        sb.append(".");
        sb.append(getName());
        sb.append("(");
        AjType<?>[] pTypes = getParameterTypes();
        for (int i = 0; i < pTypes.length - 1; i++) {
            sb.append(pTypes[i].toString());
            sb.append(", ");
        }
        if (pTypes.length > 0) {
            sb.append(pTypes[pTypes.length - 1].toString());
        }
        sb.append(")");
        return sb.toString();
    }
}
