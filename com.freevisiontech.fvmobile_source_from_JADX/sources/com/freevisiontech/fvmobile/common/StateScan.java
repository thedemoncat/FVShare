package com.freevisiontech.fvmobile.common;

public enum StateScan {
    SCAN_PROCESS(1),
    SCAN_SUCCESS(2),
    SCAN_TIMEOUT(3),
    SCAN_DISSCAN(9);
    
    private int code;

    private StateScan(int code2) {
        this.code = code2;
    }

    public int getCode() {
        return this.code;
    }
}
