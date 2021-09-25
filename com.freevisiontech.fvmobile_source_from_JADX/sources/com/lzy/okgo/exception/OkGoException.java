package com.lzy.okgo.exception;

public class OkGoException extends Exception {
    private static final long serialVersionUID = -8641198158155821498L;

    public OkGoException(String detailMessage) {
        super(detailMessage);
    }

    public static OkGoException UNKNOWN() {
        return new OkGoException("unknown exception!");
    }

    public static OkGoException BREAKPOINT_NOT_EXIST() {
        return new OkGoException("breakpoint file does not exist!");
    }

    public static OkGoException BREAKPOINT_EXPIRED() {
        return new OkGoException("breakpoint file has expired!");
    }
}
