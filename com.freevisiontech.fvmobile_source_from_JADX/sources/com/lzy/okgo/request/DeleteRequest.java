package com.lzy.okgo.request;

import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.utils.HttpUtils;
import com.lzy.okgo.utils.OkLogger;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DeleteRequest<T> extends BodyRequest<T, DeleteRequest<T>> {
    public DeleteRequest(String url) {
        super(url);
    }

    public HttpMethod getMethod() {
        return HttpMethod.DELETE;
    }

    public Request generateRequest(RequestBody requestBody) {
        try {
            this.headers.put(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.printStackTrace(e);
        }
        Request.Builder requestBuilder = HttpUtils.appendHeaders(this.headers);
        if (this.isSpliceUrl) {
            this.url = HttpUtils.createUrlFromParams(this.baseUrl, this.params.urlParamsMap);
        }
        return requestBuilder.delete(requestBody).url(this.url).tag(this.tag).build();
    }
}
