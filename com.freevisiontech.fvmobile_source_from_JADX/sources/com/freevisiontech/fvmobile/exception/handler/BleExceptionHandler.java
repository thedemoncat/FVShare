package com.freevisiontech.fvmobile.exception.handler;

import com.freevisiontech.fvmobile.exception.BleException;
import com.freevisiontech.fvmobile.exception.ConnectException;
import com.freevisiontech.fvmobile.exception.GattException;
import com.freevisiontech.fvmobile.exception.InitiatedException;
import com.freevisiontech.fvmobile.exception.OtherException;
import com.freevisiontech.fvmobile.exception.TimeoutException;

public abstract class BleExceptionHandler {
    /* access modifiers changed from: protected */
    public abstract void onConnectException(ConnectException connectException);

    /* access modifiers changed from: protected */
    public abstract void onGattException(GattException gattException);

    /* access modifiers changed from: protected */
    public abstract void onInitiatedException(InitiatedException initiatedException);

    /* access modifiers changed from: protected */
    public abstract void onOtherException(OtherException otherException);

    /* access modifiers changed from: protected */
    public abstract void onTimeoutException(TimeoutException timeoutException);

    public BleExceptionHandler handleException(BleException exception) {
        if (exception != null) {
            if (exception instanceof ConnectException) {
                onConnectException((ConnectException) exception);
            } else if (exception instanceof GattException) {
                onGattException((GattException) exception);
            } else if (exception instanceof TimeoutException) {
                onTimeoutException((TimeoutException) exception);
            } else if (exception instanceof InitiatedException) {
                onInitiatedException((InitiatedException) exception);
            } else {
                onOtherException((OtherException) exception);
            }
        }
        return this;
    }
}
