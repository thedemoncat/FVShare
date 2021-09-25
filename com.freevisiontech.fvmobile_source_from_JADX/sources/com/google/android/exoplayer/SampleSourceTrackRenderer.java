package com.google.android.exoplayer;

import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.SampleSource;
import java.io.IOException;
import java.util.Arrays;

public abstract class SampleSourceTrackRenderer extends TrackRenderer {
    private long durationUs;
    private SampleSource.SampleSourceReader enabledSource;
    private int enabledSourceTrackIndex;
    private int[] handledSourceIndices;
    private int[] handledSourceTrackIndices;
    private final SampleSource.SampleSourceReader[] sources;

    /* access modifiers changed from: protected */
    public abstract void doSomeWork(long j, long j2, boolean z) throws ExoPlaybackException;

    /* access modifiers changed from: protected */
    public abstract boolean handlesTrack(MediaFormat mediaFormat) throws MediaCodecUtil.DecoderQueryException;

    /* access modifiers changed from: protected */
    public abstract void onDiscontinuity(long j) throws ExoPlaybackException;

    public SampleSourceTrackRenderer(SampleSource... sources2) {
        this.sources = new SampleSource.SampleSourceReader[sources2.length];
        for (int i = 0; i < sources2.length; i++) {
            this.sources[i] = sources2[i].register();
        }
    }

    /* access modifiers changed from: protected */
    public final boolean doPrepare(long positionUs) throws ExoPlaybackException {
        boolean allSourcesPrepared = true;
        for (SampleSource.SampleSourceReader prepare : this.sources) {
            allSourcesPrepared &= prepare.prepare(positionUs);
        }
        if (!allSourcesPrepared) {
            return false;
        }
        int totalSourceTrackCount = 0;
        for (SampleSource.SampleSourceReader trackCount : this.sources) {
            totalSourceTrackCount += trackCount.getTrackCount();
        }
        long durationUs2 = 0;
        int handledTrackCount = 0;
        int[] handledSourceIndices2 = new int[totalSourceTrackCount];
        int[] handledTrackIndices = new int[totalSourceTrackCount];
        int sourceCount = this.sources.length;
        for (int sourceIndex = 0; sourceIndex < sourceCount; sourceIndex++) {
            SampleSource.SampleSourceReader source = this.sources[sourceIndex];
            int sourceTrackCount = source.getTrackCount();
            int trackIndex = 0;
            while (trackIndex < sourceTrackCount) {
                MediaFormat format = source.getFormat(trackIndex);
                try {
                    if (handlesTrack(format)) {
                        handledSourceIndices2[handledTrackCount] = sourceIndex;
                        handledTrackIndices[handledTrackCount] = trackIndex;
                        handledTrackCount++;
                        if (durationUs2 != -1) {
                            long trackDurationUs = format.durationUs;
                            if (trackDurationUs == -1) {
                                durationUs2 = -1;
                            } else if (trackDurationUs != -2) {
                                durationUs2 = Math.max(durationUs2, trackDurationUs);
                            }
                        }
                    }
                    trackIndex++;
                } catch (MediaCodecUtil.DecoderQueryException e) {
                    throw new ExoPlaybackException((Throwable) e);
                }
            }
        }
        this.durationUs = durationUs2;
        this.handledSourceIndices = Arrays.copyOf(handledSourceIndices2, handledTrackCount);
        this.handledSourceTrackIndices = Arrays.copyOf(handledTrackIndices, handledTrackCount);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onEnabled(int track, long positionUs, boolean joining) throws ExoPlaybackException {
        long positionUs2 = shiftInputPosition(positionUs);
        this.enabledSource = this.sources[this.handledSourceIndices[track]];
        this.enabledSourceTrackIndex = this.handledSourceTrackIndices[track];
        this.enabledSource.enable(this.enabledSourceTrackIndex, positionUs2);
        onDiscontinuity(positionUs2);
    }

    /* access modifiers changed from: protected */
    public final void seekTo(long positionUs) throws ExoPlaybackException {
        long positionUs2 = shiftInputPosition(positionUs);
        this.enabledSource.seekToUs(positionUs2);
        checkForDiscontinuity(positionUs2);
    }

    /* access modifiers changed from: protected */
    public final void doSomeWork(long positionUs, long elapsedRealtimeUs) throws ExoPlaybackException {
        long positionUs2 = shiftInputPosition(positionUs);
        doSomeWork(checkForDiscontinuity(positionUs2), elapsedRealtimeUs, this.enabledSource.continueBuffering(this.enabledSourceTrackIndex, positionUs2));
    }

    /* access modifiers changed from: protected */
    public long getBufferedPositionUs() {
        return this.enabledSource.getBufferedPositionUs();
    }

    /* access modifiers changed from: protected */
    public long getDurationUs() {
        return this.durationUs;
    }

    /* access modifiers changed from: protected */
    public void maybeThrowError() throws ExoPlaybackException {
        if (this.enabledSource != null) {
            maybeThrowError(this.enabledSource);
            return;
        }
        for (SampleSource.SampleSourceReader maybeThrowError : this.sources) {
            maybeThrowError(maybeThrowError);
        }
    }

    /* access modifiers changed from: protected */
    public void onDisabled() throws ExoPlaybackException {
        this.enabledSource.disable(this.enabledSourceTrackIndex);
        this.enabledSource = null;
    }

    /* access modifiers changed from: protected */
    public void onReleased() throws ExoPlaybackException {
        for (SampleSource.SampleSourceReader release : this.sources) {
            release.release();
        }
    }

    /* access modifiers changed from: protected */
    public final int getTrackCount() {
        return this.handledSourceTrackIndices.length;
    }

    /* access modifiers changed from: protected */
    public final MediaFormat getFormat(int track) {
        return this.sources[this.handledSourceIndices[track]].getFormat(this.handledSourceTrackIndices[track]);
    }

    /* access modifiers changed from: protected */
    public long shiftInputPosition(long positionUs) {
        return positionUs;
    }

    /* access modifiers changed from: protected */
    public final int readSource(long positionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder) {
        return this.enabledSource.readData(this.enabledSourceTrackIndex, positionUs, formatHolder, sampleHolder);
    }

    private long checkForDiscontinuity(long positionUs) throws ExoPlaybackException {
        long discontinuityPositionUs = this.enabledSource.readDiscontinuity(this.enabledSourceTrackIndex);
        if (discontinuityPositionUs == Long.MIN_VALUE) {
            return positionUs;
        }
        onDiscontinuity(discontinuityPositionUs);
        return discontinuityPositionUs;
    }

    private void maybeThrowError(SampleSource.SampleSourceReader source) throws ExoPlaybackException {
        try {
            source.maybeThrowError();
        } catch (IOException e) {
            throw new ExoPlaybackException((Throwable) e);
        }
    }
}
