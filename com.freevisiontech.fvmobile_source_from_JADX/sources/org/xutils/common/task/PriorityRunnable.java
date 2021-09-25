package org.xutils.common.task;

class PriorityRunnable implements Runnable {
    long SEQ;
    public final Priority priority;
    private final Runnable runnable;

    public PriorityRunnable(Priority priority2, Runnable runnable2) {
        this.priority = priority2 == null ? Priority.DEFAULT : priority2;
        this.runnable = runnable2;
    }

    public final void run() {
        this.runnable.run();
    }
}
