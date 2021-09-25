package org.mp4parser.aspectj.lang;

import org.mp4parser.aspectj.runtime.internal.AroundClosure;

public interface ProceedingJoinPoint extends JoinPoint {
    Object proceed() throws Throwable;

    Object proceed(Object[] objArr) throws Throwable;

    void set$AroundClosure(AroundClosure aroundClosure);
}
