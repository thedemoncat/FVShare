package com.google.android.exoplayer.extractor;

import com.google.android.exoplayer.util.Util;

public final class ChunkIndex implements SeekMap {
    public final long[] durationsUs;
    public final int length;
    public final long[] offsets;
    public final int[] sizes;
    public final long[] timesUs;

    public ChunkIndex(int[] sizes2, long[] offsets2, long[] durationsUs2, long[] timesUs2) {
        this.length = sizes2.length;
        this.sizes = sizes2;
        this.offsets = offsets2;
        this.durationsUs = durationsUs2;
        this.timesUs = timesUs2;
    }

    public int getChunkIndex(long timeUs) {
        return Util.binarySearchFloor(this.timesUs, timeUs, true, true);
    }

    public boolean isSeekable() {
        return true;
    }

    public long getPosition(long timeUs) {
        return this.offsets[getChunkIndex(timeUs)];
    }
}
