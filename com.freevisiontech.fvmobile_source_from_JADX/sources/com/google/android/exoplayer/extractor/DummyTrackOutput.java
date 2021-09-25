package com.google.android.exoplayer.extractor;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;

public class DummyTrackOutput implements TrackOutput {
    public void format(MediaFormat format) {
    }

    public int sampleData(ExtractorInput input, int length, boolean allowEndOfInput) throws IOException, InterruptedException {
        return input.skip(length);
    }

    public void sampleData(ParsableByteArray data, int length) {
        data.skipBytes(length);
    }

    public void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
    }
}
