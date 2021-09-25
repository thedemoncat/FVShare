package org.xutils.p019ex;

import java.io.IOException;

/* renamed from: org.xutils.ex.BaseException */
public class BaseException extends IOException {
    private static final long serialVersionUID = 1;

    public BaseException() {
    }

    public BaseException(String detailMessage) {
        super(detailMessage);
    }

    public BaseException(String detailMessage, Throwable throwable) {
        super(detailMessage);
        initCause(throwable);
    }

    public BaseException(Throwable throwable) {
        super(throwable.getMessage());
        initCause(throwable);
    }
}
