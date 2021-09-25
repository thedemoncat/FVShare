package com.google.android.exoplayer.extractor.wav;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.MimeTypes;
import java.io.IOException;
import java.util.List;

public final class WavExtractor implements Extractor, SeekMap {
    private static final int MAX_INPUT_SIZE = 32768;
    private int bytesPerFrame;
    private ExtractorOutput extractorOutput;
    private int pendingBytes;
    private TrackOutput trackOutput;
    private WavHeader wavHeader;

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        return WavHeaderReader.peek(input) != null;
    }

    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
        this.trackOutput = output.track(0);
        this.wavHeader = null;
        output.endTracks();
    }

    public void seek() {
        this.pendingBytes = 0;
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        if (this.wavHeader == null) {
            this.wavHeader = WavHeaderReader.peek(input);
            if (this.wavHeader == null) {
                throw new ParserException("Error initializing WavHeader. Did you sniff first?");
            }
            this.bytesPerFrame = this.wavHeader.getBytesPerFrame();
        }
        if (!this.wavHeader.hasDataBounds()) {
            WavHeaderReader.skipToData(input, this.wavHeader);
            this.trackOutput.format(MediaFormat.createAudioFormat((String) null, MimeTypes.AUDIO_RAW, this.wavHeader.getBitrate(), 32768, this.wavHeader.getDurationUs(), this.wavHeader.getNumChannels(), this.wavHeader.getSampleRateHz(), (List<byte[]>) null, (String) null, this.wavHeader.getEncoding()));
            this.extractorOutput.seekMap(this);
        }
        int bytesAppended = this.trackOutput.sampleData(input, 32768 - this.pendingBytes, true);
        if (bytesAppended != -1) {
            this.pendingBytes += bytesAppended;
        }
        int frameBytes = (this.pendingBytes / this.bytesPerFrame) * this.bytesPerFrame;
        if (frameBytes > 0) {
            long sampleStartPosition = input.getPosition() - ((long) this.pendingBytes);
            this.pendingBytes -= frameBytes;
            this.trackOutput.sampleMetadata(this.wavHeader.getTimeUs(sampleStartPosition), 1, frameBytes, this.pendingBytes, (byte[]) null);
        }
        if (bytesAppended == -1) {
            return -1;
        }
        return 0;
    }

    public boolean isSeekable() {
        return true;
    }

    public long getPosition(long timeUs) {
        return this.wavHeader.getPosition(timeUs);
    }
}
