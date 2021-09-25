package com.google.android.exoplayer;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.util.Assertions;

public abstract class TrackRenderer implements ExoPlayer.ExoPlayerComponent {
    public static final long END_OF_TRACK_US = -3;
    public static final long MATCH_LONGEST_US = -2;
    protected static final int STATE_ENABLED = 2;
    protected static final int STATE_PREPARED = 1;
    protected static final int STATE_RELEASED = -1;
    protected static final int STATE_STARTED = 3;
    protected static final int STATE_UNPREPARED = 0;
    public static final long UNKNOWN_TIME_US = -1;
    private int state;

    /* access modifiers changed from: protected */
    public abstract boolean doPrepare(long j) throws ExoPlaybackException;

    /* access modifiers changed from: protected */
    public abstract void doSomeWork(long j, long j2) throws ExoPlaybackException;

    /* access modifiers changed from: protected */
    public abstract long getBufferedPositionUs();

    /* access modifiers changed from: protected */
    public abstract long getDurationUs();

    /* access modifiers changed from: protected */
    public abstract MediaFormat getFormat(int i);

    /* access modifiers changed from: protected */
    public abstract int getTrackCount();

    /* access modifiers changed from: protected */
    public abstract boolean isEnded();

    /* access modifiers changed from: protected */
    public abstract boolean isReady();

    /* access modifiers changed from: protected */
    public abstract void maybeThrowError() throws ExoPlaybackException;

    /* access modifiers changed from: protected */
    public abstract void seekTo(long j) throws ExoPlaybackException;

    /* access modifiers changed from: protected */
    public MediaClock getMediaClock() {
        return null;
    }

    /* access modifiers changed from: protected */
    public final int getState() {
        return this.state;
    }

    /* access modifiers changed from: package-private */
    public final int prepare(long positionUs) throws ExoPlaybackException {
        boolean z;
        int i = 1;
        if (this.state == 0) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!doPrepare(positionUs)) {
            i = 0;
        }
        this.state = i;
        return this.state;
    }

    /* access modifiers changed from: package-private */
    public final void enable(int track, long positionUs, boolean joining) throws ExoPlaybackException {
        boolean z = true;
        if (this.state != 1) {
            z = false;
        }
        Assertions.checkState(z);
        this.state = 2;
        onEnabled(track, positionUs, joining);
    }

    /* access modifiers changed from: protected */
    public void onEnabled(int track, long positionUs, boolean joining) throws ExoPlaybackException {
    }

    /* access modifiers changed from: package-private */
    public final void start() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 3;
        onStarted();
    }

    /* access modifiers changed from: protected */
    public void onStarted() throws ExoPlaybackException {
    }

    /* access modifiers changed from: package-private */
    public final void stop() throws ExoPlaybackException {
        Assertions.checkState(this.state == 3);
        this.state = 2;
        onStopped();
    }

    /* access modifiers changed from: protected */
    public void onStopped() throws ExoPlaybackException {
    }

    /* access modifiers changed from: package-private */
    public final void disable() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 1;
        onDisabled();
    }

    /* access modifiers changed from: protected */
    public void onDisabled() throws ExoPlaybackException {
    }

    /* access modifiers changed from: package-private */
    public final void release() throws ExoPlaybackException {
        Assertions.checkState((this.state == 2 || this.state == 3 || this.state == -1) ? false : true);
        this.state = -1;
        onReleased();
    }

    /* access modifiers changed from: protected */
    public void onReleased() throws ExoPlaybackException {
    }

    public void handleMessage(int what, Object object) throws ExoPlaybackException {
    }
}
