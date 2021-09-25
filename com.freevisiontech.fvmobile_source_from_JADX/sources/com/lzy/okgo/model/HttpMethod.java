package com.lzy.okgo.model;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    PATCH("PATCH"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");
    
    private final String value;

    private HttpMethod(String value2) {
        this.value = value2;
    }

    public String toString() {
        return this.value;
    }

    public boolean hasBody() {
        switch (this) {
            case POST:
            case PUT:
            case PATCH:
            case DELETE:
            case OPTIONS:
                return true;
            default:
                return false;
        }
    }
}
