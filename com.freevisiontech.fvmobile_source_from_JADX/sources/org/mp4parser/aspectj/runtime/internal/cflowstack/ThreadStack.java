package org.mp4parser.aspectj.runtime.internal.cflowstack;

import java.util.Stack;

public interface ThreadStack {
    Stack getThreadStack();

    void removeThreadStack();
}
