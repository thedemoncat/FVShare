package com.lzy.okgo.exception;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.HttpUtils;

public class HttpException extends RuntimeException {
    private static final long serialVersionUID = 8773734741709178425L;
    private int code;
    private String message;
    private transient Response<?> response;

    public HttpException(String message2) {
        super(message2);
    }

    public HttpException(Response<?> response2) {
        super(getMessage(response2));
        this.code = response2.code();
        this.message = response2.message();
        this.response = response2;
    }

    private static String getMessage(Response<?> response2) {
        HttpUtils.checkNotNull(response2, "response == null");
        return "HTTP " + response2.code() + " " + response2.message();
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public Response<?> response() {
        return this.response;
    }

    public static HttpException NET_ERROR() {
        return new HttpException("network error! http response code is 404 or 5xx!");
    }

    public static HttpException COMMON(String message2) {
        return new HttpException(message2);
    }
}
