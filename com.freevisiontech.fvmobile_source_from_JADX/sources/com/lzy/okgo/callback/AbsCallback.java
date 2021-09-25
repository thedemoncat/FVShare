package com.lzy.okgo.callback;

import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.Request;
import com.lzy.okgo.utils.OkLogger;

public abstract class AbsCallback<T> implements Callback<T> {
    public void onStart(Request<T, ? extends Request> request) {
    }

    public void onCacheSuccess(Response<T> response) {
    }

    public void onError(Response<T> response) {
        OkLogger.printStackTrace(response.getException());
    }

    public void onFinish() {
    }

    public void uploadProgress(Progress progress) {
    }

    public void downloadProgress(Progress progress) {
    }
}
