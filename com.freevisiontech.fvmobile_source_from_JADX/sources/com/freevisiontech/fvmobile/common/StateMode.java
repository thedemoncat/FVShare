package com.freevisiontech.fvmobile.common;

public enum StateMode {
    PANORAMA_MODE(1),
    MOVE_TIME_LAPSE_MODE(2),
    FOLLOW_MODE(3),
    OTHER_MODE(4);
    
    private int mode;

    private StateMode(int mode2) {
        this.mode = mode2;
    }

    public int getMode() {
        return this.mode;
    }
}
