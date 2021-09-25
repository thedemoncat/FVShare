package com.lzy.okgo.adapter;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.Request;

public interface Call<T> {
    void cancel();

    Call<T> clone();

    Response<T> execute() throws Exception;

    void execute(Callback<T> callback);

    Request getRequest();

    boolean isCanceled();

    boolean isExecuted();
}
