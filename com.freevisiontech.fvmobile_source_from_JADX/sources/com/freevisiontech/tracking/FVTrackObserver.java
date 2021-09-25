package com.freevisiontech.tracking;

import org.opencv.core.Rect;

public interface FVTrackObserver {
    void initTrackFailed();

    void trackLost();

    void trackRect(Rect rect);

    void trackStarted();

    void trackStopped();
}
