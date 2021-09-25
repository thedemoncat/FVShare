package com.google.android.exoplayer;

import java.io.IOException;

public final class BehindLiveWindowException extends IOException {
    public BehindLiveWindowException() {
    }

    public BehindLiveWindowException(String message) {
        super(message);
    }
}
