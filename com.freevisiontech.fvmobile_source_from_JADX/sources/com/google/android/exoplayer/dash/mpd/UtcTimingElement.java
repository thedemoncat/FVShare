package com.google.android.exoplayer.dash.mpd;

public final class UtcTimingElement {
    public final String schemeIdUri;
    public final String value;

    public UtcTimingElement(String schemeIdUri2, String value2) {
        this.schemeIdUri = schemeIdUri2;
        this.value = value2;
    }

    public String toString() {
        return this.schemeIdUri + ", " + this.value;
    }
}
