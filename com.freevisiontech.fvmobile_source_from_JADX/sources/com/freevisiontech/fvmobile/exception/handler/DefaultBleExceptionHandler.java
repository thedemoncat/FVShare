package com.freevisiontech.fvmobile.exception.handler;

import com.freevisiontech.fvmobile.exception.ConnectException;
import com.freevisiontech.fvmobile.exception.GattException;
import com.freevisiontech.fvmobile.exception.InitiatedException;
import com.freevisiontech.fvmobile.exception.OtherException;
import com.freevisiontech.fvmobile.exception.TimeoutException;
import com.vise.log.ViseLog;

public class DefaultBleExceptionHandler extends BleExceptionHandler {
    /* access modifiers changed from: protected */
    public void onConnectException(ConnectException e) {
        ViseLog.m1466e(e.getDescription());
    }

    /* access modifiers changed from: protected */
    public void onGattException(GattException e) {
        ViseLog.m1466e(e.getDescription());
    }

    /* access modifiers changed from: protected */
    public void onTimeoutException(TimeoutException e) {
        ViseLog.m1466e(e.getDescription());
    }

    /* access modifiers changed from: protected */
    public void onInitiatedException(InitiatedException e) {
        ViseLog.m1466e(e.getDescription());
    }

    /* access modifiers changed from: protected */
    public void onOtherException(OtherException e) {
        ViseLog.m1466e(e.getDescription());
    }
}
