package com.lzy.okgo.convert;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class StringConvert implements Converter<String> {
    public String convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        return body.string();
    }
}
