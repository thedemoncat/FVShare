package org.mp4parser.aspectj.lang.reflect;

public interface CodeSignature extends MemberSignature {
    Class[] getExceptionTypes();

    String[] getParameterNames();

    Class[] getParameterTypes();
}
