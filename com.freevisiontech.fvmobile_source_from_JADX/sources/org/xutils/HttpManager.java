package org.xutils;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

public interface HttpManager {
    <T> Callback.Cancelable get(RequestParams requestParams, Callback.CommonCallback<T> commonCallback);

    <T> T getSync(RequestParams requestParams, Class<T> cls) throws Throwable;

    <T> Callback.Cancelable post(RequestParams requestParams, Callback.CommonCallback<T> commonCallback);

    <T> T postSync(RequestParams requestParams, Class<T> cls) throws Throwable;

    <T> Callback.Cancelable request(HttpMethod httpMethod, RequestParams requestParams, Callback.CommonCallback<T> commonCallback);

    <T> T requestSync(HttpMethod httpMethod, RequestParams requestParams, Class<T> cls) throws Throwable;

    <T> T requestSync(HttpMethod httpMethod, RequestParams requestParams, Callback.TypedCallback<T> typedCallback) throws Throwable;
}
