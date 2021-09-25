package com.lzy.okgo.request;

import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.utils.HttpUtils;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GetRequest<T> extends Request<T, GetRequest<T>> {
    public GetRequest(String url) {
        super(url);
    }

    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    public RequestBody generateRequestBody() {
        return null;
    }

    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = HttpUtils.appendHeaders(this.headers);
        this.url = HttpUtils.createUrlFromParams(this.baseUrl, this.params.urlParamsMap);
        return requestBuilder.get().url(this.url).tag(this.tag).build();
    }
}
