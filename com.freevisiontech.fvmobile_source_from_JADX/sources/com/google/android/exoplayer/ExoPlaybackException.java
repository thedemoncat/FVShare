package com.google.android.exoplayer;

public final class ExoPlaybackException extends Exception {
    public final boolean caughtAtTopLevel;

    public ExoPlaybackException(String message) {
        super(message);
        this.caughtAtTopLevel = false;
    }

    public ExoPlaybackException(Throwable cause) {
        super(cause);
        this.caughtAtTopLevel = false;
    }

    public ExoPlaybackException(String message, Throwable cause) {
        super(message, cause);
        this.caughtAtTopLevel = false;
    }

    ExoPlaybackException(Throwable cause, boolean caughtAtTopLevel2) {
        super(cause);
        this.caughtAtTopLevel = caughtAtTopLevel2;
    }
}
