package com.freevisiontech.fvmobile.exception;

import com.freevisiontech.fvmobile.common.BleExceptionCode;

public class InitiatedException extends BleException {
    public InitiatedException() {
        super(BleExceptionCode.INITIATED_ERR, "Initiated Exception Occurred! ");
    }
}
