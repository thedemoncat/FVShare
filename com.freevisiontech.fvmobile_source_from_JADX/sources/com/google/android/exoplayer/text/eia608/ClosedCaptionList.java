package com.google.android.exoplayer.text.eia608;

final class ClosedCaptionList implements Comparable<ClosedCaptionList> {
    public final ClosedCaption[] captions;
    public final boolean decodeOnly;
    public final long timeUs;

    public ClosedCaptionList(long timeUs2, boolean decodeOnly2, ClosedCaption[] captions2) {
        this.timeUs = timeUs2;
        this.decodeOnly = decodeOnly2;
        this.captions = captions2;
    }

    public int compareTo(ClosedCaptionList other) {
        long delta = this.timeUs - other.timeUs;
        if (delta == 0) {
            return 0;
        }
        return delta > 0 ? 1 : -1;
    }
}
