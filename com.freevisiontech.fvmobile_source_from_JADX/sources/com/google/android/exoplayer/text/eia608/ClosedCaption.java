package com.google.android.exoplayer.text.eia608;

abstract class ClosedCaption {
    public static final int TYPE_CTRL = 0;
    public static final int TYPE_TEXT = 1;
    public final int type;

    protected ClosedCaption(int type2) {
        this.type = type2;
    }
}
