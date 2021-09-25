package com.google.android.exoplayer.util;

import android.widget.MediaController;
import com.google.android.exoplayer.ExoPlayer;

public class PlayerControl implements MediaController.MediaPlayerControl {
    private final ExoPlayer exoPlayer;

    public PlayerControl(ExoPlayer exoPlayer2) {
        this.exoPlayer = exoPlayer2;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getAudioSessionId() {
        throw new UnsupportedOperationException();
    }

    public int getBufferPercentage() {
        return this.exoPlayer.getBufferedPercentage();
    }

    public int getCurrentPosition() {
        if (this.exoPlayer.getDuration() == -1) {
            return 0;
        }
        return (int) this.exoPlayer.getCurrentPosition();
    }

    public int getDuration() {
        if (this.exoPlayer.getDuration() == -1) {
            return 0;
        }
        return (int) this.exoPlayer.getDuration();
    }

    public boolean isPlaying() {
        return this.exoPlayer.getPlayWhenReady();
    }

    public void start() {
        this.exoPlayer.setPlayWhenReady(true);
    }

    public void pause() {
        this.exoPlayer.setPlayWhenReady(false);
    }

    public void seekTo(int timeMillis) {
        long seekPosition;
        if (this.exoPlayer.getDuration() == -1) {
            seekPosition = 0;
        } else {
            seekPosition = (long) Math.min(Math.max(0, timeMillis), getDuration());
        }
        this.exoPlayer.seekTo(seekPosition);
    }
}
