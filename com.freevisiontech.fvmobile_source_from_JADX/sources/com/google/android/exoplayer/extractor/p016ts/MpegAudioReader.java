package com.google.android.exoplayer.extractor.p016ts;

import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.List;

/* renamed from: com.google.android.exoplayer.extractor.ts.MpegAudioReader */
final class MpegAudioReader extends ElementaryStreamReader {
    private static final int HEADER_SIZE = 4;
    private static final int STATE_FINDING_HEADER = 0;
    private static final int STATE_READING_FRAME = 2;
    private static final int STATE_READING_HEADER = 1;
    private int frameBytesRead;
    private long frameDurationUs;
    private int frameSize;
    private boolean hasOutputFormat;
    private final MpegAudioHeader header;
    private final ParsableByteArray headerScratch = new ParsableByteArray(4);
    private boolean lastByteWasFF;
    private int state = 0;
    private long timeUs;

    public MpegAudioReader(TrackOutput output) {
        super(output);
        this.headerScratch.data[0] = -1;
        this.header = new MpegAudioHeader();
    }

    public void seek() {
        this.state = 0;
        this.frameBytesRead = 0;
        this.lastByteWasFF = false;
    }

    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        this.timeUs = pesTimeUs;
    }

    public void consume(ParsableByteArray data) {
        while (data.bytesLeft() > 0) {
            switch (this.state) {
                case 0:
                    findHeader(data);
                    break;
                case 1:
                    readHeaderRemainder(data);
                    break;
                case 2:
                    readFrameRemainder(data);
                    break;
            }
        }
    }

    public void packetFinished() {
    }

    private void findHeader(ParsableByteArray source) {
        boolean found;
        byte[] data = source.data;
        int startOffset = source.getPosition();
        int endOffset = source.limit();
        for (int i = startOffset; i < endOffset; i++) {
            boolean byteIsFF = (data[i] & 255) == 255;
            if (!this.lastByteWasFF || (data[i] & 224) != 224) {
                found = false;
            } else {
                found = true;
            }
            this.lastByteWasFF = byteIsFF;
            if (found) {
                source.setPosition(i + 1);
                this.lastByteWasFF = false;
                this.headerScratch.data[1] = data[i];
                this.frameBytesRead = 2;
                this.state = 1;
                return;
            }
        }
        source.setPosition(endOffset);
    }

    private void readHeaderRemainder(ParsableByteArray source) {
        int bytesToRead = Math.min(source.bytesLeft(), 4 - this.frameBytesRead);
        source.readBytes(this.headerScratch.data, this.frameBytesRead, bytesToRead);
        this.frameBytesRead += bytesToRead;
        if (this.frameBytesRead >= 4) {
            this.headerScratch.setPosition(0);
            if (!MpegAudioHeader.populateHeader(this.headerScratch.readInt(), this.header)) {
                this.frameBytesRead = 0;
                this.state = 1;
                return;
            }
            this.frameSize = this.header.frameSize;
            if (!this.hasOutputFormat) {
                this.frameDurationUs = (C1907C.MICROS_PER_SECOND * ((long) this.header.samplesPerFrame)) / ((long) this.header.sampleRate);
                this.output.format(MediaFormat.createAudioFormat((String) null, this.header.mimeType, -1, 4096, -1, this.header.channels, this.header.sampleRate, (List<byte[]>) null, (String) null));
                this.hasOutputFormat = true;
            }
            this.headerScratch.setPosition(0);
            this.output.sampleData(this.headerScratch, 4);
            this.state = 2;
        }
    }

    private void readFrameRemainder(ParsableByteArray source) {
        int bytesToRead = Math.min(source.bytesLeft(), this.frameSize - this.frameBytesRead);
        this.output.sampleData(source, bytesToRead);
        this.frameBytesRead += bytesToRead;
        if (this.frameBytesRead >= this.frameSize) {
            this.output.sampleMetadata(this.timeUs, 1, this.frameSize, 0, (byte[]) null);
            this.timeUs += this.frameDurationUs;
            this.frameBytesRead = 0;
            this.state = 0;
        }
    }
}
