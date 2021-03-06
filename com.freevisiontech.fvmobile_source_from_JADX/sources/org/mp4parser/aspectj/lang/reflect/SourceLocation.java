package org.mp4parser.aspectj.lang.reflect;

public interface SourceLocation {
    int getColumn();

    String getFileName();

    int getLine();

    Class getWithinType();
}
