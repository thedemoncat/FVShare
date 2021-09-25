package com.google.android.exoplayer.util;

import com.google.android.exoplayer.C1907C;

public final class FlacStreamInfo {
    public final int bitsPerSample;
    public final int channels;
    public final int maxBlockSize;
    public final int maxFrameSize;
    public final int minBlockSize;
    public final int minFrameSize;
    public final int sampleRate;
    public final long totalSamples;

    public FlacStreamInfo(byte[] data, int offset) {
        ParsableBitArray scratch = new ParsableBitArray(data);
        scratch.setPosition(offset * 8);
        this.minBlockSize = scratch.readBits(16);
        this.maxBlockSize = scratch.readBits(16);
        this.minFrameSize = scratch.readBits(24);
        this.maxFrameSize = scratch.readBits(24);
        this.sampleRate = scratch.readBits(20);
        this.channels = scratch.readBits(3) + 1;
        this.bitsPerSample = scratch.readBits(5) + 1;
        this.totalSamples = (long) scratch.readBits(36);
    }

    public FlacStreamInfo(int minBlockSize2, int maxBlockSize2, int minFrameSize2, int maxFrameSize2, int sampleRate2, int channels2, int bitsPerSample2, long totalSamples2) {
        this.minBlockSize = minBlockSize2;
        this.maxBlockSize = maxBlockSize2;
        this.minFrameSize = minFrameSize2;
        this.maxFrameSize = maxFrameSize2;
        this.sampleRate = sampleRate2;
        this.channels = channels2;
        this.bitsPerSample = bitsPerSample2;
        this.totalSamples = totalSamples2;
    }

    public int maxDecodedFrameSize() {
        return this.maxBlockSize * this.channels * 2;
    }

    public int bitRate() {
        return this.bitsPerSample * this.sampleRate;
    }

    public long durationUs() {
        return (this.totalSamples * C1907C.MICROS_PER_SECOND) / ((long) this.sampleRate);
    }
}
