package org.mp4parser.aspectj.lang.reflect;

public interface InterTypeDeclaration {
    AjType<?> getDeclaringType();

    int getModifiers();

    AjType<?> getTargetType() throws ClassNotFoundException;
}
