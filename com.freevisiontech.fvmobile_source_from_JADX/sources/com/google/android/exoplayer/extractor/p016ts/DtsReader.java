package com.google.android.exoplayer.extractor.p016ts;

import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.DtsUtil;
import com.google.android.exoplayer.util.ParsableByteArray;

/* renamed from: com.google.android.exoplayer.extractor.ts.DtsReader */
final class DtsReader extends ElementaryStreamReader {
    private static final int HEADER_SIZE = 15;
    private static final int STATE_FINDING_SYNC = 0;
    private static final int STATE_READING_HEADER = 1;
    private static final int STATE_READING_SAMPLE = 2;
    private static final int SYNC_VALUE = 2147385345;
    private static final int SYNC_VALUE_SIZE = 4;
    private int bytesRead;
    private final ParsableByteArray headerScratchBytes = new ParsableByteArray(new byte[15]);
    private MediaFormat mediaFormat;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private int syncBytes;
    private long timeUs;

    public DtsReader(TrackOutput output) {
        super(output);
        this.headerScratchBytes.data[0] = Byte.MAX_VALUE;
        this.headerScratchBytes.data[1] = -2;
        this.headerScratchBytes.data[2] = Byte.MIN_VALUE;
        this.headerScratchBytes.data[3] = 1;
        this.state = 0;
    }

    public void seek() {
        this.state = 0;
        this.bytesRead = 0;
        this.syncBytes = 0;
    }

    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        this.timeUs = pesTimeUs;
    }

    public void consume(ParsableByteArray data) {
        while (data.bytesLeft() > 0) {
            switch (this.state) {
                case 0:
                    if (!skipToNextSync(data)) {
                        break;
                    } else {
                        this.bytesRead = 4;
                        this.state = 1;
                        break;
                    }
                case 1:
                    if (!continueRead(data, this.headerScratchBytes.data, 15)) {
                        break;
                    } else {
                        parseHeader();
                        this.headerScratchBytes.setPosition(0);
                        this.output.sampleData(this.headerScratchBytes, 15);
                        this.state = 2;
                        break;
                    }
                case 2:
                    int bytesToRead = Math.min(data.bytesLeft(), this.sampleSize - this.bytesRead);
                    this.output.sampleData(data, bytesToRead);
                    this.bytesRead += bytesToRead;
                    if (this.bytesRead != this.sampleSize) {
                        break;
                    } else {
                        this.output.sampleMetadata(this.timeUs, 1, this.sampleSize, 0, (byte[]) null);
                        this.timeUs += this.sampleDurationUs;
                        this.state = 0;
                        break;
                    }
            }
        }
    }

    public void packetFinished() {
    }

    private boolean continueRead(ParsableByteArray source, byte[] target, int targetLength) {
        int bytesToRead = Math.min(source.bytesLeft(), targetLength - this.bytesRead);
        source.readBytes(target, this.bytesRead, bytesToRead);
        this.bytesRead += bytesToRead;
        return this.bytesRead == targetLength;
    }

    private boolean skipToNextSync(ParsableByteArray pesBuffer) {
        while (pesBuffer.bytesLeft() > 0) {
            this.syncBytes <<= 8;
            this.syncBytes |= pesBuffer.readUnsignedByte();
            if (this.syncBytes == SYNC_VALUE) {
                this.syncBytes = 0;
                return true;
            }
        }
        return false;
    }

    private void parseHeader() {
        byte[] frameData = this.headerScratchBytes.data;
        if (this.mediaFormat == null) {
            this.mediaFormat = DtsUtil.parseDtsFormat(frameData, (String) null, -1, (String) null);
            this.output.format(this.mediaFormat);
        }
        this.sampleSize = DtsUtil.getDtsFrameSize(frameData);
        this.sampleDurationUs = (long) ((int) ((C1907C.MICROS_PER_SECOND * ((long) DtsUtil.parseDtsAudioSampleCount(frameData))) / ((long) this.mediaFormat.sampleRate)));
    }
}
