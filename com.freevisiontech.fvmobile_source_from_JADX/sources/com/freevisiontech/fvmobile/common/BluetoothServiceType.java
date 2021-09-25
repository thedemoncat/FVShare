package com.freevisiontech.fvmobile.common;

public enum BluetoothServiceType {
    AUDIO(2097152),
    CAPTURE(524288),
    INFORMATION(8388608),
    LIMITED_DISCOVERABILITY(8192),
    NETWORKING(131072),
    OBJECT_TRANSFER(1048576),
    POSITIONING(65536),
    RENDER(262144),
    TELEPHONY(4194304);
    
    private int code;

    private BluetoothServiceType(int code2) {
        this.code = code2;
    }

    public int getCode() {
        return this.code;
    }
}
