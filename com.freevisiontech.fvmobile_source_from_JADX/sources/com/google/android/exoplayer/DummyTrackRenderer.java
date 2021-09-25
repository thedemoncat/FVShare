package com.google.android.exoplayer;

public final class DummyTrackRenderer extends TrackRenderer {
    /* access modifiers changed from: protected */
    public boolean doPrepare(long positionUs) throws ExoPlaybackException {
        return true;
    }

    /* access modifiers changed from: protected */
    public int getTrackCount() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public MediaFormat getFormat(int track) {
        throw new IllegalStateException();
    }

    /* access modifiers changed from: protected */
    public boolean isEnded() {
        throw new IllegalStateException();
    }

    /* access modifiers changed from: protected */
    public boolean isReady() {
        throw new IllegalStateException();
    }

    /* access modifiers changed from: protected */
    public void seekTo(long positionUs) {
        throw new IllegalStateException();
    }

    /* access modifiers changed from: protected */
    public void doSomeWork(long positionUs, long elapsedRealtimeUs) {
        throw new IllegalStateException();
    }

    /* access modifiers changed from: protected */
    public void maybeThrowError() {
        throw new IllegalStateException();
    }

    /* access modifiers changed from: protected */
    public long getDurationUs() {
        throw new IllegalStateException();
    }

    /* access modifiers changed from: protected */
    public long getBufferedPositionUs() {
        throw new IllegalStateException();
    }
}
