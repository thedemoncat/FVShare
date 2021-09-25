package org.mp4parser.aspectj.lang.reflect;

public interface DeclarePrecedence {
    AjType getDeclaringType();

    TypePattern[] getPrecedenceOrder();
}
