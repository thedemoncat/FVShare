package com.google.android.exoplayer.util.extensions;

import com.google.android.exoplayer.SampleHolder;

public class InputBuffer extends Buffer {
    public final SampleHolder sampleHolder = new SampleHolder(2);

    public void reset() {
        super.reset();
        this.sampleHolder.clearData();
    }
}
