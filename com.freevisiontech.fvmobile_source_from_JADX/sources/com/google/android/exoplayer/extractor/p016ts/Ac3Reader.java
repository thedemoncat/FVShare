package com.google.android.exoplayer.extractor.p016ts;

import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.Ac3Util;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;

/* renamed from: com.google.android.exoplayer.extractor.ts.Ac3Reader */
final class Ac3Reader extends ElementaryStreamReader {
    private static final int HEADER_SIZE = 8;
    private static final int STATE_FINDING_SYNC = 0;
    private static final int STATE_READING_HEADER = 1;
    private static final int STATE_READING_SAMPLE = 2;
    private int bytesRead;
    private final ParsableBitArray headerScratchBits = new ParsableBitArray(new byte[8]);
    private final ParsableByteArray headerScratchBytes = new ParsableByteArray(this.headerScratchBits.data);
    private final boolean isEac3;
    private boolean lastByteWas0B;
    private MediaFormat mediaFormat;
    private long sampleDurationUs;
    private int sampleSize;
    private int state = 0;
    private long timeUs;

    public Ac3Reader(TrackOutput output, boolean isEac32) {
        super(output);
        this.isEac3 = isEac32;
    }

    public void seek() {
        this.state = 0;
        this.bytesRead = 0;
        this.lastByteWas0B = false;
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
                        this.state = 1;
                        this.headerScratchBytes.data[0] = 11;
                        this.headerScratchBytes.data[1] = 119;
                        this.bytesRead = 2;
                        break;
                    }
                case 1:
                    if (!continueRead(data, this.headerScratchBytes.data, 8)) {
                        break;
                    } else {
                        parseHeader();
                        this.headerScratchBytes.setPosition(0);
                        this.output.sampleData(this.headerScratchBytes, 8);
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
            if (!this.lastByteWas0B) {
                this.lastByteWas0B = pesBuffer.readUnsignedByte() == 11;
            } else {
                int secondByte = pesBuffer.readUnsignedByte();
                if (secondByte == 119) {
                    this.lastByteWas0B = false;
                    return true;
                }
                this.lastByteWas0B = secondByte == 11;
            }
        }
        return false;
    }

    private void parseHeader() {
        int parseAc3SyncframeSize;
        int audioSamplesPerSyncframe;
        MediaFormat parseAc3SyncframeFormat;
        if (this.mediaFormat == null) {
            if (this.isEac3) {
                parseAc3SyncframeFormat = Ac3Util.parseEac3SyncframeFormat(this.headerScratchBits, (String) null, -1, (String) null);
            } else {
                parseAc3SyncframeFormat = Ac3Util.parseAc3SyncframeFormat(this.headerScratchBits, (String) null, -1, (String) null);
            }
            this.mediaFormat = parseAc3SyncframeFormat;
            this.output.format(this.mediaFormat);
        }
        if (this.isEac3) {
            parseAc3SyncframeSize = Ac3Util.parseEAc3SyncframeSize(this.headerScratchBits.data);
        } else {
            parseAc3SyncframeSize = Ac3Util.parseAc3SyncframeSize(this.headerScratchBits.data);
        }
        this.sampleSize = parseAc3SyncframeSize;
        if (this.isEac3) {
            audioSamplesPerSyncframe = Ac3Util.parseEAc3SyncframeAudioSampleCount(this.headerScratchBits.data);
        } else {
            audioSamplesPerSyncframe = Ac3Util.getAc3SyncframeAudioSampleCount();
        }
        this.sampleDurationUs = (long) ((int) ((C1907C.MICROS_PER_SECOND * ((long) audioSamplesPerSyncframe)) / ((long) this.mediaFormat.sampleRate)));
    }
}
