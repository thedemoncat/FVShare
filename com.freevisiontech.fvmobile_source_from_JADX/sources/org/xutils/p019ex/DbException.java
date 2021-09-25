package org.xutils.p019ex;

/* renamed from: org.xutils.ex.DbException */
public class DbException extends BaseException {
    private static final long serialVersionUID = 1;

    public DbException() {
    }

    public DbException(String detailMessage) {
        super(detailMessage);
    }

    public DbException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DbException(Throwable throwable) {
        super(throwable);
    }
}
