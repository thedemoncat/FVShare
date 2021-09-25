package com.google.android.exoplayer.text.ttml;

final class TtmlRegion {
    public final float line;
    public final int lineType;
    public final float position;
    public final float width;

    public TtmlRegion() {
        this(Float.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
    }

    public TtmlRegion(float position2, float line2, int lineType2, float width2) {
        this.position = position2;
        this.line = line2;
        this.lineType = lineType2;
        this.width = width2;
    }
}
