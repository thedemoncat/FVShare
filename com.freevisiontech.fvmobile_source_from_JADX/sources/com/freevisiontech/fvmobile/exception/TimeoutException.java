package com.freevisiontech.fvmobile.exception;

import com.freevisiontech.fvmobile.common.BleExceptionCode;

public class TimeoutException extends BleException {
    public TimeoutException() {
        super(BleExceptionCode.TIMEOUT, "Timeout Exception Occurred! ");
    }
}
