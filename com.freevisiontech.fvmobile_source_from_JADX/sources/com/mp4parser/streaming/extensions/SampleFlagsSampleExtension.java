package com.mp4parser.streaming.extensions;

import com.mp4parser.streaming.SampleExtension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SampleFlagsSampleExtension implements SampleExtension {
    public static Map<Long, SampleFlagsSampleExtension> pool = Collections.synchronizedMap(new HashMap());
    private byte isLeading;
    private int sampleDegradationPriority;
    private byte sampleDependsOn;
    private byte sampleHasRedundancy;
    private byte sampleIsDependedOn;
    private boolean sampleIsNonSyncSample;
    private byte samplePaddingValue;

    public static SampleFlagsSampleExtension create(byte isLeading2, byte sampleDependsOn2, byte sampleIsDependedOn2, byte sampleHasRedundancy2, byte samplePaddingValue2, boolean sampleIsNonSyncSample2, int sampleDegradationPriority2) {
        long key = ((long) ((sampleDependsOn2 << 2) + isLeading2 + (sampleIsDependedOn2 << 4) + (sampleHasRedundancy2 << 6))) + ((long) (samplePaddingValue2 << 8)) + ((long) (sampleDegradationPriority2 << 11)) + ((long) ((sampleIsNonSyncSample2 ? 1 : 0) << 27));
        SampleFlagsSampleExtension c = pool.get(Long.valueOf(key));
        if (c != null) {
            return c;
        }
        SampleFlagsSampleExtension c2 = new SampleFlagsSampleExtension();
        c2.isLeading = isLeading2;
        c2.sampleDependsOn = sampleDependsOn2;
        c2.sampleIsDependedOn = sampleIsDependedOn2;
        c2.sampleHasRedundancy = sampleHasRedundancy2;
        c2.samplePaddingValue = samplePaddingValue2;
        c2.sampleIsNonSyncSample = sampleIsNonSyncSample2;
        c2.sampleDegradationPriority = sampleDegradationPriority2;
        pool.put(Long.valueOf(key), c2);
        return c2;
    }

    public byte getIsLeading() {
        return this.isLeading;
    }

    public void setIsLeading(byte isLeading2) {
        this.isLeading = isLeading2;
    }

    public byte getSampleDependsOn() {
        return this.sampleDependsOn;
    }

    public void setSampleDependsOn(byte sampleDependsOn2) {
        this.sampleDependsOn = sampleDependsOn2;
    }

    public byte getSampleIsDependedOn() {
        return this.sampleIsDependedOn;
    }

    public void setSampleIsDependedOn(byte sampleIsDependedOn2) {
        this.sampleIsDependedOn = sampleIsDependedOn2;
    }

    public byte getSampleHasRedundancy() {
        return this.sampleHasRedundancy;
    }

    public void setSampleHasRedundancy(byte sampleHasRedundancy2) {
        this.sampleHasRedundancy = sampleHasRedundancy2;
    }

    public byte getSamplePaddingValue() {
        return this.samplePaddingValue;
    }

    public void setSamplePaddingValue(byte samplePaddingValue2) {
        this.samplePaddingValue = samplePaddingValue2;
    }

    public boolean isSampleIsNonSyncSample() {
        return this.sampleIsNonSyncSample;
    }

    public boolean isSyncSample() {
        return !this.sampleIsNonSyncSample;
    }

    public void setSampleIsNonSyncSample(boolean sampleIsNonSyncSample2) {
        this.sampleIsNonSyncSample = sampleIsNonSyncSample2;
    }

    public int getSampleDegradationPriority() {
        return this.sampleDegradationPriority;
    }

    public void setSampleDegradationPriority(int sampleDegradationPriority2) {
        this.sampleDegradationPriority = sampleDegradationPriority2;
    }
}
