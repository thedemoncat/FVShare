package org.mp4parser.aspectj.runtime.reflect;

import org.mp4parser.aspectj.lang.reflect.MemberSignature;

abstract class MemberSignatureImpl extends SignatureImpl implements MemberSignature {
    MemberSignatureImpl(int modifiers, String name, Class declaringType) {
        super(modifiers, name, declaringType);
    }

    public MemberSignatureImpl(String stringRep) {
        super(stringRep);
    }
}
