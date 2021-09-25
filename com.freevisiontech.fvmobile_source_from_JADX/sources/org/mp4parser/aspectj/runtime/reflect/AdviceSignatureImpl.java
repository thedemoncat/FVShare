package org.mp4parser.aspectj.runtime.reflect;

import java.lang.reflect.Method;
import org.mp4parser.aspectj.lang.reflect.AdviceSignature;

class AdviceSignatureImpl extends CodeSignatureImpl implements AdviceSignature {
    private Method adviceMethod = null;
    Class returnType;

    AdviceSignatureImpl(int modifiers, String name, Class declaringType, Class[] parameterTypes, String[] parameterNames, Class[] exceptionTypes, Class returnType2) {
        super(modifiers, name, declaringType, parameterTypes, parameterNames, exceptionTypes);
        this.returnType = returnType2;
    }

    AdviceSignatureImpl(String stringRep) {
        super(stringRep);
    }

    public Class getReturnType() {
        if (this.returnType == null) {
            this.returnType = extractType(6);
        }
        return this.returnType;
    }

    /* access modifiers changed from: protected */
    public String createToString(StringMaker sm) {
        StringBuffer buf = new StringBuffer();
        if (sm.includeArgs) {
            buf.append(sm.makeTypeName(getReturnType()));
        }
        if (sm.includeArgs) {
            buf.append(" ");
        }
        buf.append(sm.makePrimaryTypeName(getDeclaringType(), getDeclaringTypeName()));
        buf.append(".");
        buf.append(toAdviceName(getName()));
        sm.addSignature(buf, getParameterTypes());
        sm.addThrows(buf, getExceptionTypes());
        return buf.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x0018  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String toAdviceName(java.lang.String r5) {
        /*
            r4 = this;
            r2 = 36
            int r2 = r5.indexOf(r2)
            r3 = -1
            if (r2 != r3) goto L_0x000a
        L_0x0009:
            return r5
        L_0x000a:
            java.util.StringTokenizer r0 = new java.util.StringTokenizer
            java.lang.String r2 = "$"
            r0.<init>(r5, r2)
        L_0x0012:
            boolean r2 = r0.hasMoreTokens()
            if (r2 == 0) goto L_0x0009
            java.lang.String r1 = r0.nextToken()
            java.lang.String r2 = "before"
            boolean r2 = r1.startsWith(r2)
            if (r2 != 0) goto L_0x0037
            java.lang.String r2 = "after"
            boolean r2 = r1.startsWith(r2)
            if (r2 != 0) goto L_0x0037
            java.lang.String r2 = "around"
            boolean r2 = r1.startsWith(r2)
            if (r2 == 0) goto L_0x0012
        L_0x0037:
            r5 = r1
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mp4parser.aspectj.runtime.reflect.AdviceSignatureImpl.toAdviceName(java.lang.String):java.lang.String");
    }

    public Method getAdvice() {
        if (this.adviceMethod == null) {
            try {
                this.adviceMethod = getDeclaringType().getDeclaredMethod(getName(), getParameterTypes());
            } catch (Exception e) {
            }
        }
        return this.adviceMethod;
    }
}
