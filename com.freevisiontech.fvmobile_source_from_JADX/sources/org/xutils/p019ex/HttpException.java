package org.xutils.p019ex;

import android.text.TextUtils;

/* renamed from: org.xutils.ex.HttpException */
public class HttpException extends BaseException {
    private static final long serialVersionUID = 1;
    private int code;
    private String customMessage;
    private String errorCode;
    private String result;

    public HttpException(int code2, String detailMessage) {
        super(detailMessage);
        this.code = code2;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public void setMessage(String message) {
        this.customMessage = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getErrorCode() {
        return this.errorCode == null ? String.valueOf(this.code) : this.errorCode;
    }

    public void setErrorCode(String errorCode2) {
        this.errorCode = errorCode2;
    }

    public String getMessage() {
        if (!TextUtils.isEmpty(this.customMessage)) {
            return this.customMessage;
        }
        return super.getMessage();
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result2) {
        this.result = result2;
    }

    public String toString() {
        return "errorCode: " + getErrorCode() + ", msg: " + getMessage() + ", result: " + this.result;
    }
}
