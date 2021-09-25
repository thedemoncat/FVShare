package com.lzy.okgo.callback;

import com.lzy.okgo.convert.StringConvert;
import okhttp3.Response;

public abstract class StringCallback extends AbsCallback<String> {
    private StringConvert convert = new StringConvert();

    public String convertResponse(Response response) throws Throwable {
        String s = this.convert.convertResponse(response);
        response.close();
        return s;
    }
}
