package com.google.android.exoplayer.extractor.mp4;

import com.google.android.exoplayer.util.Util;

final class FixedSampleSizeRechunker {
    private static final int MAX_SAMPLE_SIZE = 8192;

    FixedSampleSizeRechunker() {
    }

    public static final class Results {
        public final int[] flags;
        public final int maximumSize;
        public final long[] offsets;
        public final int[] sizes;
        public final long[] timestamps;

        public Results(long[] offsets2, int[] sizes2, int maximumSize2, long[] timestamps2, int[] flags2) {
            this.offsets = offsets2;
            this.sizes = sizes2;
            this.maximumSize = maximumSize2;
            this.timestamps = timestamps2;
            this.flags = flags2;
        }
    }

    public static Results rechunk(int fixedSampleSize, long[] chunkOffsets, int[] chunkSampleCounts, long timestampDeltaInTimeUnits) {
        int maxSampleCount = 8192 / fixedSampleSize;
        int rechunkedSampleCount = 0;
        for (int chunkSampleCount : chunkSampleCounts) {
            rechunkedSampleCount += Util.ceilDivide(chunkSampleCount, maxSampleCount);
        }
        long[] offsets = new long[rechunkedSampleCount];
        int[] sizes = new int[rechunkedSampleCount];
        int maximumSize = 0;
        long[] timestamps = new long[rechunkedSampleCount];
        int[] flags = new int[rechunkedSampleCount];
        int originalSampleIndex = 0;
        int newSampleIndex = 0;
        for (int chunkIndex = 0; chunkIndex < chunkSampleCounts.length; chunkIndex++) {
            int chunkSamplesRemaining = chunkSampleCounts[chunkIndex];
            long sampleOffset = chunkOffsets[chunkIndex];
            while (chunkSamplesRemaining > 0) {
                int bufferSampleCount = Math.min(maxSampleCount, chunkSamplesRemaining);
                offsets[newSampleIndex] = sampleOffset;
                sizes[newSampleIndex] = fixedSampleSize * bufferSampleCount;
                maximumSize = Math.max(maximumSize, sizes[newSampleIndex]);
                timestamps[newSampleIndex] = ((long) originalSampleIndex) * timestampDeltaInTimeUnits;
                flags[newSampleIndex] = 1;
                sampleOffset += (long) sizes[newSampleIndex];
                originalSampleIndex += bufferSampleCount;
                chunkSamplesRemaining -= bufferSampleCount;
                newSampleIndex++;
            }
        }
        return new Results(offsets, sizes, maximumSize, timestamps, flags);
    }
}
