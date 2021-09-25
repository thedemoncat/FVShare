package com.google.android.exoplayer.util;

import android.os.HandlerThread;
import android.os.Process;

public final class PriorityHandlerThread extends HandlerThread {
    private final int priority;

    public PriorityHandlerThread(String name, int priority2) {
        super(name);
        this.priority = priority2;
    }

    public void run() {
        Process.setThreadPriority(this.priority);
        super.run();
    }
}
