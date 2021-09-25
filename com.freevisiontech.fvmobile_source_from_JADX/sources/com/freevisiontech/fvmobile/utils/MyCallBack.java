package com.freevisiontech.fvmobile.utils;

import org.xutils.common.Callback;

public class MyCallBack<ResultType> implements Callback.CommonCallback<ResultType> {
    public void onSuccess(ResultType resulttype) {
    }

    public void onError(Throwable ex, boolean isOnCallback) {
    }

    public void onCancelled(Callback.CancelledException cex) {
    }

    public void onFinished() {
    }
}
