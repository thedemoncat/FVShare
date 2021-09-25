package com.freevisiontech.fvmobile.exception;

import com.freevisiontech.fvmobile.common.BleExceptionCode;
import java.io.Serializable;

public class BleException implements Serializable {
    private BleExceptionCode code;
    private String description;

    public BleException(BleExceptionCode code2, String description2) {
        this.code = code2;
        this.description = description2;
    }

    public BleExceptionCode getCode() {
        return this.code;
    }

    public BleException setCode(BleExceptionCode code2) {
        this.code = code2;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public BleException setDescription(String description2) {
        this.description = description2;
        return this;
    }

    public String toString() {
        return "BleException{code=" + this.code + ", description='" + this.description + '\'' + '}';
    }
}
