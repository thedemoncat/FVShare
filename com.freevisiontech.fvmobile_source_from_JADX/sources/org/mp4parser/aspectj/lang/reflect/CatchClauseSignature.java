package org.mp4parser.aspectj.lang.reflect;

import org.mp4parser.aspectj.lang.Signature;

public interface CatchClauseSignature extends Signature {
    String getParameterName();

    Class getParameterType();
}
