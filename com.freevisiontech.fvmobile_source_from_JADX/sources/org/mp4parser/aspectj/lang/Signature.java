package org.mp4parser.aspectj.lang;

public interface Signature {
    Class getDeclaringType();

    String getDeclaringTypeName();

    int getModifiers();

    String getName();

    String toLongString();

    String toShortString();

    String toString();
}
