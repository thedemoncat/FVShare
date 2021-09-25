package com.lzy.okgo.model;

import okhttp3.Call;
import okhttp3.Headers;

public final class Response<T> {
    private T body;
    private boolean isFromCache;
    private Call rawCall;
    private okhttp3.Response rawResponse;
    private Throwable throwable;

    public static <T> Response<T> success(boolean isFromCache2, T body2, Call rawCall2, okhttp3.Response rawResponse2) {
        Response<T> response = new Response<>();
        response.setFromCache(isFromCache2);
        response.setBody(body2);
        response.setRawCall(rawCall2);
        response.setRawResponse(rawResponse2);
        return response;
    }

    public static <T> Response<T> error(boolean isFromCache2, Call rawCall2, okhttp3.Response rawResponse2, Throwable throwable2) {
        Response<T> response = new Response<>();
        response.setFromCache(isFromCache2);
        response.setRawCall(rawCall2);
        response.setRawResponse(rawResponse2);
        response.setException(throwable2);
        return response;
    }

    public int code() {
        if (this.rawResponse == null) {
            return -1;
        }
        return this.rawResponse.code();
    }

    public String message() {
        if (this.rawResponse == null) {
            return null;
        }
        return this.rawResponse.message();
    }

    public Headers headers() {
        if (this.rawResponse == null) {
            return null;
        }
        return this.rawResponse.headers();
    }

    public boolean isSuccessful() {
        return this.throwable == null;
    }

    public void setBody(T body2) {
        this.body = body2;
    }

    public T body() {
        return this.body;
    }

    public Throwable getException() {
        return this.throwable;
    }

    public void setException(Throwable exception) {
        this.throwable = exception;
    }

    public Call getRawCall() {
        return this.rawCall;
    }

    public void setRawCall(Call rawCall2) {
        this.rawCall = rawCall2;
    }

    public okhttp3.Response getRawResponse() {
        return this.rawResponse;
    }

    public void setRawResponse(okhttp3.Response rawResponse2) {
        this.rawResponse = rawResponse2;
    }

    public boolean isFromCache() {
        return this.isFromCache;
    }

    public void setFromCache(boolean fromCache) {
        this.isFromCache = fromCache;
    }
}
