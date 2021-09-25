package com.freevisiontech.fvmobile.exception;

import com.freevisiontech.fvmobile.common.BleExceptionCode;

public class OtherException extends BleException {
    public OtherException(String description) {
        super(BleExceptionCode.OTHER_ERR, description);
    }
}
