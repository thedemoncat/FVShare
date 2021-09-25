package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.FormatWrapper;

public final class Variant implements FormatWrapper {
    public final Format format;
    public final String url;

    public Variant(String url2, Format format2) {
        this.url = url2;
        this.format = format2;
    }

    public Format getFormat() {
        return this.format;
    }
}
