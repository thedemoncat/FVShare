package com.google.android.exoplayer.extractor.mp4;

import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;

final class TrackSampleTable {
    public static final int NO_SAMPLE = -1;
    public final int[] flags;
    public final int maximumSize;
    public final long[] offsets;
    public final int sampleCount;
    public final int[] sizes;
    public final long[] timestampsUs;

    TrackSampleTable(long[] offsets2, int[] sizes2, int maximumSize2, long[] timestampsUs2, int[] flags2) {
        boolean z;
        boolean z2 = true;
        Assertions.checkArgument(sizes2.length == timestampsUs2.length);
        if (offsets2.length == timestampsUs2.length) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkArgument(z);
        Assertions.checkArgument(flags2.length != timestampsUs2.length ? false : z2);
        this.offsets = offsets2;
        this.sizes = sizes2;
        this.maximumSize = maximumSize2;
        this.timestampsUs = timestampsUs2;
        this.flags = flags2;
        this.sampleCount = offsets2.length;
    }

    public int getIndexOfEarlierOrEqualSynchronizationSample(long timeUs) {
        for (int i = Util.binarySearchFloor(this.timestampsUs, timeUs, true, false); i >= 0; i--) {
            if ((this.flags[i] & 1) != 0) {
                return i;
            }
        }
        return -1;
    }

    public int getIndexOfLaterOrEqualSynchronizationSample(long timeUs) {
        for (int i = Util.binarySearchCeil(this.timestampsUs, timeUs, true, false); i < this.timestampsUs.length; i++) {
            if ((this.flags[i] & 1) != 0) {
                return i;
            }
        }
        return -1;
    }
}
