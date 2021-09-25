package com.freevisiontech.fvmobile.common;

public enum State {
    CONNECT_PROCESS(4),
    CONNECT_SUCCESS(5),
    CONNECT_FAILURE(6),
    CONNECT_TIMEOUT(7),
    DISCONNECT(8);
    
    private int code;

    private State(int code2) {
        this.code = code2;
    }

    public int getCode() {
        return this.code;
    }
}
