package com.google.android.exoplayer.util.extensions;

import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.extensions.InputBuffer;
import com.google.android.exoplayer.util.extensions.OutputBuffer;
import java.lang.Exception;
import java.util.LinkedList;

public abstract class SimpleDecoder<I extends InputBuffer, O extends OutputBuffer, E extends Exception> extends Thread implements Decoder<I, O, E> {
    private int availableInputBufferCount;
    private final I[] availableInputBuffers;
    private int availableOutputBufferCount;
    private final O[] availableOutputBuffers;
    private I dequeuedInputBuffer;
    private E exception;
    private boolean flushed;
    private final Object lock = new Object();
    private final LinkedList<I> queuedInputBuffers = new LinkedList<>();
    private final LinkedList<O> queuedOutputBuffers = new LinkedList<>();
    private boolean released;

    public interface EventListener<E> {
        void onDecoderError(E e);
    }

    /* access modifiers changed from: protected */
    public abstract I createInputBuffer();

    /* access modifiers changed from: protected */
    public abstract O createOutputBuffer();

    /* access modifiers changed from: protected */
    public abstract E decode(I i, O o, boolean z);

    protected SimpleDecoder(I[] inputBuffers, O[] outputBuffers) {
        this.availableInputBuffers = inputBuffers;
        this.availableInputBufferCount = inputBuffers.length;
        for (int i = 0; i < this.availableInputBufferCount; i++) {
            this.availableInputBuffers[i] = createInputBuffer();
        }
        this.availableOutputBuffers = outputBuffers;
        this.availableOutputBufferCount = outputBuffers.length;
        for (int i2 = 0; i2 < this.availableOutputBufferCount; i2++) {
            this.availableOutputBuffers[i2] = createOutputBuffer();
        }
    }

    /* access modifiers changed from: protected */
    public final void setInitialInputBufferSize(int size) {
        Assertions.checkState(this.availableInputBufferCount == this.availableInputBuffers.length);
        for (I i : this.availableInputBuffers) {
            i.sampleHolder.ensureSpaceForWrite(size);
        }
    }

    public final I dequeueInputBuffer() throws Exception {
        I inputBuffer;
        synchronized (this.lock) {
            maybeThrowException();
            Assertions.checkState(this.dequeuedInputBuffer == null);
            if (this.availableInputBufferCount == 0) {
                inputBuffer = null;
            } else {
                I[] iArr = this.availableInputBuffers;
                int i = this.availableInputBufferCount - 1;
                this.availableInputBufferCount = i;
                inputBuffer = iArr[i];
                inputBuffer.reset();
                this.dequeuedInputBuffer = inputBuffer;
            }
        }
        return inputBuffer;
    }

    public final void queueInputBuffer(I inputBuffer) throws Exception {
        synchronized (this.lock) {
            maybeThrowException();
            Assertions.checkArgument(inputBuffer == this.dequeuedInputBuffer);
            this.queuedInputBuffers.addLast(inputBuffer);
            maybeNotifyDecodeLoop();
            this.dequeuedInputBuffer = null;
        }
    }

    public final O dequeueOutputBuffer() throws Exception {
        O o;
        synchronized (this.lock) {
            maybeThrowException();
            if (this.queuedOutputBuffers.isEmpty()) {
                o = null;
            } else {
                o = (OutputBuffer) this.queuedOutputBuffers.removeFirst();
            }
        }
        return o;
    }

    /* access modifiers changed from: protected */
    public void releaseOutputBuffer(O outputBuffer) {
        synchronized (this.lock) {
            O[] oArr = this.availableOutputBuffers;
            int i = this.availableOutputBufferCount;
            this.availableOutputBufferCount = i + 1;
            oArr[i] = outputBuffer;
            maybeNotifyDecodeLoop();
        }
    }

    public final void flush() {
        synchronized (this.lock) {
            this.flushed = true;
            if (this.dequeuedInputBuffer != null) {
                I[] iArr = this.availableInputBuffers;
                int i = this.availableInputBufferCount;
                this.availableInputBufferCount = i + 1;
                iArr[i] = this.dequeuedInputBuffer;
                this.dequeuedInputBuffer = null;
            }
            while (!this.queuedInputBuffers.isEmpty()) {
                I[] iArr2 = this.availableInputBuffers;
                int i2 = this.availableInputBufferCount;
                this.availableInputBufferCount = i2 + 1;
                iArr2[i2] = (InputBuffer) this.queuedInputBuffers.removeFirst();
            }
            while (!this.queuedOutputBuffers.isEmpty()) {
                O[] oArr = this.availableOutputBuffers;
                int i3 = this.availableOutputBufferCount;
                this.availableOutputBufferCount = i3 + 1;
                oArr[i3] = (OutputBuffer) this.queuedOutputBuffers.removeFirst();
            }
        }
    }

    public void release() {
        synchronized (this.lock) {
            this.released = true;
            this.lock.notify();
        }
        try {
            join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void maybeThrowException() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
    }

    private void maybeNotifyDecodeLoop() {
        if (canDecodeBuffer()) {
            this.lock.notify();
        }
    }

    public final void run() {
        do {
            try {
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        } while (decode());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0037, code lost:
        r1.reset();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003e, code lost:
        if (r0.getFlag(1) == false) goto L_0x0068;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0040, code lost:
        r1.setFlag(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0043, code lost:
        r5 = r9.lock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0045, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0048, code lost:
        if (r9.flushed != false) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004f, code lost:
        if (r1.getFlag(2) == false) goto L_0x0083;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0051, code lost:
        r3 = r9.availableOutputBuffers;
        r6 = r9.availableOutputBufferCount;
        r9.availableOutputBufferCount = r6 + 1;
        r3[r6] = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005b, code lost:
        r3 = r9.availableInputBuffers;
        r6 = r9.availableInputBufferCount;
        r9.availableInputBufferCount = r6 + 1;
        r3[r6] = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0065, code lost:
        monitor-exit(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x006c, code lost:
        if (r0.getFlag(2) == false) goto L_0x0071;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x006e, code lost:
        r1.setFlag(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0071, code lost:
        r9.exception = decode(r0, r1, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0079, code lost:
        if (r9.exception == null) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x007b, code lost:
        r4 = r9.lock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x007d, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        r9.queuedOutputBuffers.addLast(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean decode() throws java.lang.InterruptedException {
        /*
            r9 = this;
            r8 = 2
            r4 = 1
            r3 = 0
            java.lang.Object r5 = r9.lock
            monitor-enter(r5)
        L_0x0006:
            boolean r6 = r9.released     // Catch:{ all -> 0x0016 }
            if (r6 != 0) goto L_0x0019
            boolean r6 = r9.canDecodeBuffer()     // Catch:{ all -> 0x0016 }
            if (r6 != 0) goto L_0x0019
            java.lang.Object r6 = r9.lock     // Catch:{ all -> 0x0016 }
            r6.wait()     // Catch:{ all -> 0x0016 }
            goto L_0x0006
        L_0x0016:
            r3 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0016 }
            throw r3
        L_0x0019:
            boolean r6 = r9.released     // Catch:{ all -> 0x0016 }
            if (r6 == 0) goto L_0x001f
            monitor-exit(r5)     // Catch:{ all -> 0x0016 }
        L_0x001e:
            return r3
        L_0x001f:
            java.util.LinkedList<I> r6 = r9.queuedInputBuffers     // Catch:{ all -> 0x0016 }
            java.lang.Object r0 = r6.removeFirst()     // Catch:{ all -> 0x0016 }
            com.google.android.exoplayer.util.extensions.InputBuffer r0 = (com.google.android.exoplayer.util.extensions.InputBuffer) r0     // Catch:{ all -> 0x0016 }
            O[] r6 = r9.availableOutputBuffers     // Catch:{ all -> 0x0016 }
            int r7 = r9.availableOutputBufferCount     // Catch:{ all -> 0x0016 }
            int r7 = r7 + -1
            r9.availableOutputBufferCount = r7     // Catch:{ all -> 0x0016 }
            r1 = r6[r7]     // Catch:{ all -> 0x0016 }
            boolean r2 = r9.flushed     // Catch:{ all -> 0x0016 }
            r6 = 0
            r9.flushed = r6     // Catch:{ all -> 0x0016 }
            monitor-exit(r5)     // Catch:{ all -> 0x0016 }
            r1.reset()
            boolean r5 = r0.getFlag(r4)
            if (r5 == 0) goto L_0x0068
            r1.setFlag(r4)
        L_0x0043:
            java.lang.Object r5 = r9.lock
            monitor-enter(r5)
            boolean r3 = r9.flushed     // Catch:{ all -> 0x0089 }
            if (r3 != 0) goto L_0x0051
            r3 = 2
            boolean r3 = r1.getFlag(r3)     // Catch:{ all -> 0x0089 }
            if (r3 == 0) goto L_0x0083
        L_0x0051:
            O[] r3 = r9.availableOutputBuffers     // Catch:{ all -> 0x0089 }
            int r6 = r9.availableOutputBufferCount     // Catch:{ all -> 0x0089 }
            int r7 = r6 + 1
            r9.availableOutputBufferCount = r7     // Catch:{ all -> 0x0089 }
            r3[r6] = r1     // Catch:{ all -> 0x0089 }
        L_0x005b:
            I[] r3 = r9.availableInputBuffers     // Catch:{ all -> 0x0089 }
            int r6 = r9.availableInputBufferCount     // Catch:{ all -> 0x0089 }
            int r7 = r6 + 1
            r9.availableInputBufferCount = r7     // Catch:{ all -> 0x0089 }
            r3[r6] = r0     // Catch:{ all -> 0x0089 }
            monitor-exit(r5)     // Catch:{ all -> 0x0089 }
            r3 = r4
            goto L_0x001e
        L_0x0068:
            boolean r5 = r0.getFlag(r8)
            if (r5 == 0) goto L_0x0071
            r1.setFlag(r8)
        L_0x0071:
            java.lang.Exception r5 = r9.decode(r0, r1, r2)
            r9.exception = r5
            E r5 = r9.exception
            if (r5 == 0) goto L_0x0043
            java.lang.Object r4 = r9.lock
            monitor-enter(r4)
            monitor-exit(r4)     // Catch:{ all -> 0x0080 }
            goto L_0x001e
        L_0x0080:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0080 }
            throw r3
        L_0x0083:
            java.util.LinkedList<O> r3 = r9.queuedOutputBuffers     // Catch:{ all -> 0x0089 }
            r3.addLast(r1)     // Catch:{ all -> 0x0089 }
            goto L_0x005b
        L_0x0089:
            r3 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0089 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.util.extensions.SimpleDecoder.decode():boolean");
    }

    private boolean canDecodeBuffer() {
        return !this.queuedInputBuffers.isEmpty() && this.availableOutputBufferCount > 0;
    }
}
