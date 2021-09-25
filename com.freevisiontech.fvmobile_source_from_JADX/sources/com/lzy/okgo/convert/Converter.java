package com.lzy.okgo.convert;

import okhttp3.Response;

public interface Converter<T> {
    T convertResponse(Response response) throws Throwable;
}
