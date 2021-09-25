package com.google.android.exoplayer.extractor.mp4;

import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;

final class TrackFragment {
    public long auxiliaryDataPosition;
    public long dataPosition;
    public boolean definesEncryptionData;
    public DefaultSampleValues header;
    public int length;
    public long nextFragmentDecodeTime;
    public int[] sampleCompositionTimeOffsetTable;
    public long[] sampleDecodingTimeTable;
    public ParsableByteArray sampleEncryptionData;
    public int sampleEncryptionDataLength;
    public boolean sampleEncryptionDataNeedsFill;
    public boolean[] sampleHasSubsampleEncryptionTable;
    public boolean[] sampleIsSyncFrameTable;
    public int[] sampleSizeTable;
    public TrackEncryptionBox trackEncryptionBox;

    TrackFragment() {
    }

    public void reset() {
        this.length = 0;
        this.nextFragmentDecodeTime = 0;
        this.definesEncryptionData = false;
        this.sampleEncryptionDataNeedsFill = false;
        this.trackEncryptionBox = null;
    }

    public void initTables(int sampleCount) {
        this.length = sampleCount;
        if (this.sampleSizeTable == null || this.sampleSizeTable.length < this.length) {
            int tableSize = (sampleCount * 125) / 100;
            this.sampleSizeTable = new int[tableSize];
            this.sampleCompositionTimeOffsetTable = new int[tableSize];
            this.sampleDecodingTimeTable = new long[tableSize];
            this.sampleIsSyncFrameTable = new boolean[tableSize];
            this.sampleHasSubsampleEncryptionTable = new boolean[tableSize];
        }
    }

    public void initEncryptionData(int length2) {
        if (this.sampleEncryptionData == null || this.sampleEncryptionData.limit() < length2) {
            this.sampleEncryptionData = new ParsableByteArray(length2);
        }
        this.sampleEncryptionDataLength = length2;
        this.definesEncryptionData = true;
        this.sampleEncryptionDataNeedsFill = true;
    }

    public void fillEncryptionData(ExtractorInput input) throws IOException, InterruptedException {
        input.readFully(this.sampleEncryptionData.data, 0, this.sampleEncryptionDataLength);
        this.sampleEncryptionData.setPosition(0);
        this.sampleEncryptionDataNeedsFill = false;
    }

    public void fillEncryptionData(ParsableByteArray source) {
        source.readBytes(this.sampleEncryptionData.data, 0, this.sampleEncryptionDataLength);
        this.sampleEncryptionData.setPosition(0);
        this.sampleEncryptionDataNeedsFill = false;
    }

    public long getSamplePresentationTime(int index) {
        return this.sampleDecodingTimeTable[index] + ((long) this.sampleCompositionTimeOffsetTable[index]);
    }
}
