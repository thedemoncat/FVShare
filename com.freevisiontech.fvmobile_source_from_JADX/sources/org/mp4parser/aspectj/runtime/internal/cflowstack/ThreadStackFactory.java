package org.mp4parser.aspectj.runtime.internal.cflowstack;

public interface ThreadStackFactory {
    ThreadCounter getNewThreadCounter();

    ThreadStack getNewThreadStack();
}
