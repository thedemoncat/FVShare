package com.google.android.exoplayer.text;

import android.text.Layout;

public class Cue {
    public static final int ANCHOR_TYPE_END = 2;
    public static final int ANCHOR_TYPE_MIDDLE = 1;
    public static final int ANCHOR_TYPE_START = 0;
    public static final float DIMEN_UNSET = Float.MIN_VALUE;
    public static final int LINE_TYPE_FRACTION = 0;
    public static final int LINE_TYPE_NUMBER = 1;
    public static final int TYPE_UNSET = Integer.MIN_VALUE;
    public final float line;
    public final int lineAnchor;
    public final int lineType;
    public final float position;
    public final int positionAnchor;
    public final float size;
    public final CharSequence text;
    public final Layout.Alignment textAlignment;

    public Cue() {
        this((CharSequence) null);
    }

    public Cue(CharSequence text2) {
        this(text2, (Layout.Alignment) null, Float.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
    }

    public Cue(CharSequence text2, Layout.Alignment textAlignment2, float line2, int lineType2, int lineAnchor2, float position2, int positionAnchor2, float size2) {
        this.text = text2;
        this.textAlignment = textAlignment2;
        this.line = line2;
        this.lineType = lineType2;
        this.lineAnchor = lineAnchor2;
        this.position = position2;
        this.positionAnchor = positionAnchor2;
        this.size = size2;
    }
}
