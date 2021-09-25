package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;

final class XingSeeker implements Mp3Extractor.Seeker {
    private final long durationUs;
    private final long firstFramePosition;
    private final int headerSize;
    private final long inputLength;
    private final long sizeBytes;
    private final long[] tableOfContents;

    public static XingSeeker create(MpegAudioHeader mpegAudioHeader, ParsableByteArray frame, long position, long inputLength2) {
        int frameCount;
        int samplesPerFrame = mpegAudioHeader.samplesPerFrame;
        int sampleRate = mpegAudioHeader.sampleRate;
        long firstFramePosition2 = position + ((long) mpegAudioHeader.frameSize);
        int flags = frame.readInt();
        if ((flags & 1) != 1 || (frameCount = frame.readUnsignedIntToInt()) == 0) {
            return null;
        }
        long durationUs2 = Util.scaleLargeTimestamp((long) frameCount, ((long) samplesPerFrame) * C1907C.MICROS_PER_SECOND, (long) sampleRate);
        if ((flags & 6) != 6) {
            return new XingSeeker(firstFramePosition2, durationUs2, inputLength2);
        }
        long sizeBytes2 = (long) frame.readUnsignedIntToInt();
        frame.skipBytes(1);
        long[] tableOfContents2 = new long[99];
        for (int i = 0; i < 99; i++) {
            tableOfContents2[i] = (long) frame.readUnsignedByte();
        }
        return new XingSeeker(firstFramePosition2, durationUs2, inputLength2, tableOfContents2, sizeBytes2, mpegAudioHeader.frameSize);
    }

    private XingSeeker(long firstFramePosition2, long durationUs2, long inputLength2) {
        this(firstFramePosition2, durationUs2, inputLength2, (long[]) null, 0, 0);
    }

    private XingSeeker(long firstFramePosition2, long durationUs2, long inputLength2, long[] tableOfContents2, long sizeBytes2, int headerSize2) {
        this.firstFramePosition = firstFramePosition2;
        this.durationUs = durationUs2;
        this.inputLength = inputLength2;
        this.tableOfContents = tableOfContents2;
        this.sizeBytes = sizeBytes2;
        this.headerSize = headerSize2;
    }

    public boolean isSeekable() {
        return this.tableOfContents != null;
    }

    public long getPosition(long timeUs) {
        float fa;
        float fb;
        float fx;
        if (!isSeekable()) {
            return this.firstFramePosition;
        }
        float percent = (((float) timeUs) * 100.0f) / ((float) this.durationUs);
        if (percent <= 0.0f) {
            fx = 0.0f;
        } else if (percent >= 100.0f) {
            fx = 256.0f;
        } else {
            int a = (int) percent;
            if (a == 0) {
                fa = 0.0f;
            } else {
                fa = (float) this.tableOfContents[a - 1];
            }
            if (a < 99) {
                fb = (float) this.tableOfContents[a];
            } else {
                fb = 256.0f;
            }
            fx = fa + ((fb - fa) * (percent - ((float) a)));
        }
        return Math.min(Math.round(0.00390625d * ((double) fx) * ((double) this.sizeBytes)) + this.firstFramePosition, this.inputLength != -1 ? this.inputLength - 1 : ((this.firstFramePosition - ((long) this.headerSize)) + this.sizeBytes) - 1);
    }

    public long getTimeUs(long position) {
        if (!isSeekable() || position < this.firstFramePosition) {
            return 0;
        }
        double offsetByte = (256.0d * ((double) (position - this.firstFramePosition))) / ((double) this.sizeBytes);
        int previousTocPosition = Util.binarySearchFloor(this.tableOfContents, (long) offsetByte, true, false) + 1;
        long previousTime = getTimeUsForTocPosition(previousTocPosition);
        long previousByte = previousTocPosition == 0 ? 0 : this.tableOfContents[previousTocPosition - 1];
        long nextByte = previousTocPosition == 99 ? 256 : this.tableOfContents[previousTocPosition];
        return previousTime + (nextByte == previousByte ? 0 : (long) ((((double) (getTimeUsForTocPosition(previousTocPosition + 1) - previousTime)) * (offsetByte - ((double) previousByte))) / ((double) (nextByte - previousByte))));
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    private long getTimeUsForTocPosition(int tocPosition) {
        return (this.durationUs * ((long) tocPosition)) / 100;
    }
}
