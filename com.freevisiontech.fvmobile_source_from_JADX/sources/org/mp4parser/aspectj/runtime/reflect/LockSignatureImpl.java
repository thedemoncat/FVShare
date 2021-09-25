package org.mp4parser.aspectj.runtime.reflect;

import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.reflect.LockSignature;

class LockSignatureImpl extends SignatureImpl implements LockSignature {
    private Class parameterType;

    LockSignatureImpl(Class c) {
        super(8, JoinPoint.SYNCHRONIZATION_LOCK, c);
        this.parameterType = c;
    }

    LockSignatureImpl(String stringRep) {
        super(stringRep);
    }

    /* access modifiers changed from: protected */
    public String createToString(StringMaker sm) {
        if (this.parameterType == null) {
            this.parameterType = extractType(3);
        }
        return new StringBuffer().append("lock(").append(sm.makeTypeName(this.parameterType)).append(")").toString();
    }

    public Class getParameterType() {
        if (this.parameterType == null) {
            this.parameterType = extractType(3);
        }
        return this.parameterType;
    }
}
