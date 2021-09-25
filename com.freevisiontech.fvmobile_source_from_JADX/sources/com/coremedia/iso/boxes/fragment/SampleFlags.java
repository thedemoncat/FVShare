package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

public class SampleFlags {
    private byte isLeading;
    private byte reserved;
    private int sampleDegradationPriority;
    private byte sampleDependsOn;
    private byte sampleHasRedundancy;
    private byte sampleIsDependedOn;
    private boolean sampleIsDifferenceSample;
    private byte samplePaddingValue;

    public SampleFlags() {
    }

    public SampleFlags(ByteBuffer bb) {
        long a = IsoTypeReader.readUInt32(bb);
        this.reserved = (byte) ((int) ((-268435456 & a) >> 28));
        this.isLeading = (byte) ((int) ((201326592 & a) >> 26));
        this.sampleDependsOn = (byte) ((int) ((50331648 & a) >> 24));
        this.sampleIsDependedOn = (byte) ((int) ((12582912 & a) >> 22));
        this.sampleHasRedundancy = (byte) ((int) ((3145728 & a) >> 20));
        this.samplePaddingValue = (byte) ((int) ((917504 & a) >> 17));
        this.sampleIsDifferenceSample = ((65536 & a) >> 16) > 0;
        this.sampleDegradationPriority = (int) (65535 & a);
    }

    public void getContent(ByteBuffer os) {
        IsoTypeWriter.writeUInt32(os, 0 | ((long) (this.reserved << ClosedCaptionCtrl.MISC_CHAN_2)) | ((long) (this.isLeading << 26)) | ((long) (this.sampleDependsOn << 24)) | ((long) (this.sampleIsDependedOn << 22)) | ((long) (this.sampleHasRedundancy << ClosedCaptionCtrl.MISC_CHAN_1)) | ((long) (this.samplePaddingValue << ClosedCaptionCtrl.MID_ROW_CHAN_1)) | ((long) ((this.sampleIsDifferenceSample ? 1 : 0) << 16)) | ((long) this.sampleDegradationPriority));
    }

    public int getReserved() {
        return this.reserved;
    }

    public void setReserved(int reserved2) {
        this.reserved = (byte) reserved2;
    }

    public byte getIsLeading() {
        return this.isLeading;
    }

    public void setIsLeading(byte isLeading2) {
        this.isLeading = isLeading2;
    }

    public int getSampleDependsOn() {
        return this.sampleDependsOn;
    }

    public void setSampleDependsOn(int sampleDependsOn2) {
        this.sampleDependsOn = (byte) sampleDependsOn2;
    }

    public int getSampleIsDependedOn() {
        return this.sampleIsDependedOn;
    }

    public void setSampleIsDependedOn(int sampleIsDependedOn2) {
        this.sampleIsDependedOn = (byte) sampleIsDependedOn2;
    }

    public int getSampleHasRedundancy() {
        return this.sampleHasRedundancy;
    }

    public void setSampleHasRedundancy(int sampleHasRedundancy2) {
        this.sampleHasRedundancy = (byte) sampleHasRedundancy2;
    }

    public int getSamplePaddingValue() {
        return this.samplePaddingValue;
    }

    public void setSamplePaddingValue(int samplePaddingValue2) {
        this.samplePaddingValue = (byte) samplePaddingValue2;
    }

    public boolean isSampleIsDifferenceSample() {
        return this.sampleIsDifferenceSample;
    }

    public void setSampleIsDifferenceSample(boolean sampleIsDifferenceSample2) {
        this.sampleIsDifferenceSample = sampleIsDifferenceSample2;
    }

    public int getSampleDegradationPriority() {
        return this.sampleDegradationPriority;
    }

    public void setSampleDegradationPriority(int sampleDegradationPriority2) {
        this.sampleDegradationPriority = sampleDegradationPriority2;
    }

    public String toString() {
        return "SampleFlags{reserved=" + this.reserved + ", isLeading=" + this.isLeading + ", depOn=" + this.sampleDependsOn + ", isDepOn=" + this.sampleIsDependedOn + ", hasRedundancy=" + this.sampleHasRedundancy + ", padValue=" + this.samplePaddingValue + ", isDiffSample=" + this.sampleIsDifferenceSample + ", degradPrio=" + this.sampleDegradationPriority + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SampleFlags that = (SampleFlags) o;
        if (this.isLeading != that.isLeading) {
            return false;
        }
        if (this.reserved != that.reserved) {
            return false;
        }
        if (this.sampleDegradationPriority != that.sampleDegradationPriority) {
            return false;
        }
        if (this.sampleDependsOn != that.sampleDependsOn) {
            return false;
        }
        if (this.sampleHasRedundancy != that.sampleHasRedundancy) {
            return false;
        }
        if (this.sampleIsDependedOn != that.sampleIsDependedOn) {
            return false;
        }
        if (this.sampleIsDifferenceSample != that.sampleIsDifferenceSample) {
            return false;
        }
        if (this.samplePaddingValue != that.samplePaddingValue) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((((((((((this.reserved * 31) + this.isLeading) * 31) + this.sampleDependsOn) * 31) + this.sampleIsDependedOn) * 31) + this.sampleHasRedundancy) * 31) + this.samplePaddingValue) * 31) + (this.sampleIsDifferenceSample ? 1 : 0)) * 31) + this.sampleDegradationPriority;
    }
}
