package com.google.android.exoplayer;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.exoplayer.ExoPlayer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

final class ExoPlayerImpl implements ExoPlayer {
    private static final String TAG = "ExoPlayerImpl";
    private final Handler eventHandler;
    private final ExoPlayerImplInternal internalPlayer;
    private final CopyOnWriteArraySet<ExoPlayer.Listener> listeners = new CopyOnWriteArraySet<>();
    private int pendingPlayWhenReadyAcks;
    private boolean playWhenReady = false;
    private int playbackState = 1;
    private final int[] selectedTrackIndices;
    private final MediaFormat[][] trackFormats;

    @SuppressLint({"HandlerLeak"})
    public ExoPlayerImpl(int rendererCount, int minBufferMs, int minRebufferMs) {
        Log.i(TAG, "Init 1.5.11");
        this.trackFormats = new MediaFormat[rendererCount][];
        this.selectedTrackIndices = new int[rendererCount];
        this.eventHandler = new Handler() {
            public void handleMessage(Message msg) {
                ExoPlayerImpl.this.handleEvent(msg);
            }
        };
        this.internalPlayer = new ExoPlayerImplInternal(this.eventHandler, this.playWhenReady, this.selectedTrackIndices, minBufferMs, minRebufferMs);
    }

    public Looper getPlaybackLooper() {
        return this.internalPlayer.getPlaybackLooper();
    }

    public void addListener(ExoPlayer.Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ExoPlayer.Listener listener) {
        this.listeners.remove(listener);
    }

    public int getPlaybackState() {
        return this.playbackState;
    }

    public void prepare(TrackRenderer... renderers) {
        Arrays.fill(this.trackFormats, (Object) null);
        this.internalPlayer.prepare(renderers);
    }

    public int getTrackCount(int rendererIndex) {
        if (this.trackFormats[rendererIndex] != null) {
            return this.trackFormats[rendererIndex].length;
        }
        return 0;
    }

    public MediaFormat getTrackFormat(int rendererIndex, int trackIndex) {
        return this.trackFormats[rendererIndex][trackIndex];
    }

    public void setSelectedTrack(int rendererIndex, int trackIndex) {
        if (this.selectedTrackIndices[rendererIndex] != trackIndex) {
            this.selectedTrackIndices[rendererIndex] = trackIndex;
            this.internalPlayer.setRendererSelectedTrack(rendererIndex, trackIndex);
        }
    }

    public int getSelectedTrack(int rendererIndex) {
        return this.selectedTrackIndices[rendererIndex];
    }

    public void setPlayWhenReady(boolean playWhenReady2) {
        if (this.playWhenReady != playWhenReady2) {
            this.playWhenReady = playWhenReady2;
            this.pendingPlayWhenReadyAcks++;
            this.internalPlayer.setPlayWhenReady(playWhenReady2);
            Iterator<ExoPlayer.Listener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().onPlayerStateChanged(playWhenReady2, this.playbackState);
            }
        }
    }

    public boolean getPlayWhenReady() {
        return this.playWhenReady;
    }

    public boolean isPlayWhenReadyCommitted() {
        return this.pendingPlayWhenReadyAcks == 0;
    }

    public void seekTo(long positionMs) {
        this.internalPlayer.seekTo(positionMs);
    }

    public void stop() {
        this.internalPlayer.stop();
    }

    public void release() {
        this.internalPlayer.release();
        this.eventHandler.removeCallbacksAndMessages((Object) null);
    }

    public void sendMessage(ExoPlayer.ExoPlayerComponent target, int messageType, Object message) {
        this.internalPlayer.sendMessage(target, messageType, message);
    }

    public void blockingSendMessage(ExoPlayer.ExoPlayerComponent target, int messageType, Object message) {
        this.internalPlayer.blockingSendMessage(target, messageType, message);
    }

    public long getDuration() {
        return this.internalPlayer.getDuration();
    }

    public long getCurrentPosition() {
        return this.internalPlayer.getCurrentPosition();
    }

    public long getBufferedPosition() {
        return this.internalPlayer.getBufferedPosition();
    }

    public int getBufferedPercentage() {
        long j = 100;
        long bufferedPosition = getBufferedPosition();
        long duration = getDuration();
        if (bufferedPosition == -1 || duration == -1) {
            return 0;
        }
        if (duration != 0) {
            j = (100 * bufferedPosition) / duration;
        }
        return (int) j;
    }

    /* access modifiers changed from: package-private */
    public void handleEvent(Message msg) {
        switch (msg.what) {
            case 1:
                System.arraycopy(msg.obj, 0, this.trackFormats, 0, this.trackFormats.length);
                this.playbackState = msg.arg1;
                Iterator<ExoPlayer.Listener> it = this.listeners.iterator();
                while (it.hasNext()) {
                    it.next().onPlayerStateChanged(this.playWhenReady, this.playbackState);
                }
                return;
            case 2:
                this.playbackState = msg.arg1;
                Iterator<ExoPlayer.Listener> it2 = this.listeners.iterator();
                while (it2.hasNext()) {
                    it2.next().onPlayerStateChanged(this.playWhenReady, this.playbackState);
                }
                return;
            case 3:
                this.pendingPlayWhenReadyAcks--;
                if (this.pendingPlayWhenReadyAcks == 0) {
                    Iterator<ExoPlayer.Listener> it3 = this.listeners.iterator();
                    while (it3.hasNext()) {
                        it3.next().onPlayWhenReadyCommitted();
                    }
                    return;
                }
                return;
            case 4:
                ExoPlaybackException exception = (ExoPlaybackException) msg.obj;
                Iterator<ExoPlayer.Listener> it4 = this.listeners.iterator();
                while (it4.hasNext()) {
                    it4.next().onPlayerError(exception);
                }
                return;
            default:
                return;
        }
    }
}
