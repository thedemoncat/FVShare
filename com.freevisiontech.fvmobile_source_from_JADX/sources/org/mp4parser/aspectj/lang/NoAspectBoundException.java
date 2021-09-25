package org.mp4parser.aspectj.lang;

public class NoAspectBoundException extends RuntimeException {
    Throwable cause;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NoAspectBoundException(String aspectName, Throwable inner) {
        super(inner != null ? new StringBuffer().append("Exception while initializing ").append(aspectName).append(": ").append(inner).toString() : aspectName);
        this.cause = inner;
    }

    public NoAspectBoundException() {
    }

    public Throwable getCause() {
        return this.cause;
    }
}
