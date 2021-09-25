package p004pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.view.Surface;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* renamed from: pl.droidsonroids.gif.GifInfoHandle */
final class GifInfoHandle {
    private volatile long gifInfoPtr;

    private static native void bindSurface(long j, Surface surface, long[] jArr);

    private static native void free(long j);

    private static native long getAllocationByteCount(long j);

    private static native String getComment(long j);

    private static native int getCurrentFrameIndex(long j);

    private static native int getCurrentLoop(long j);

    private static native int getCurrentPosition(long j);

    private static native int getDuration(long j);

    private static native int getFrameDuration(long j, int i);

    private static native int getHeight(long j);

    private static native int getLoopCount(long j);

    private static native long getMetadataByteCount(long j);

    private static native int getNativeErrorCode(long j);

    private static native int getNumberOfFrames(long j);

    private static native long[] getSavedState(long j);

    private static native long getSourceLength(long j);

    private static native int getWidth(long j);

    private static native void glTexImage2D(long j, int i, int i2);

    private static native void glTexSubImage2D(long j, int i, int i2);

    private static native void initTexImageDescriptor(long j);

    private static native boolean isAnimationCompleted(long j);

    private static native boolean isOpaque(long j);

    static native long openByteArray(byte[] bArr) throws GifIOException;

    static native long openDirectByteBuffer(ByteBuffer byteBuffer) throws GifIOException;

    static native long openFd(FileDescriptor fileDescriptor, long j) throws GifIOException;

    static native long openFile(String str) throws GifIOException;

    static native long openStream(InputStream inputStream) throws GifIOException;

    private static native void postUnbindSurface(long j);

    private static native long renderFrame(long j, Bitmap bitmap);

    private static native boolean reset(long j);

    private static native long restoreRemainder(long j);

    private static native int restoreSavedState(long j, long[] jArr, Bitmap bitmap);

    private static native void saveRemainder(long j);

    private static native void seekToFrame(long j, int i, Bitmap bitmap);

    private static native void seekToFrameGL(long j, int i);

    private static native void seekToTime(long j, int i, Bitmap bitmap);

    private static native void setLoopCount(long j, char c);

    private static native void setOptions(long j, char c, boolean z);

    private static native void setSpeedFactor(long j, float f);

    private static native void startDecoderThread(long j);

    private static native void stopDecoderThread(long j);

    static {
        LibraryLoader.loadLibrary((Context) null);
    }

    GifInfoHandle() {
    }

    GifInfoHandle(FileDescriptor fd) throws GifIOException {
        this.gifInfoPtr = openFd(fd, 0);
    }

    GifInfoHandle(byte[] bytes) throws GifIOException {
        this.gifInfoPtr = openByteArray(bytes);
    }

    GifInfoHandle(ByteBuffer buffer) throws GifIOException {
        this.gifInfoPtr = openDirectByteBuffer(buffer);
    }

    GifInfoHandle(String filePath) throws GifIOException {
        this.gifInfoPtr = openFile(filePath);
    }

    GifInfoHandle(InputStream stream) throws GifIOException {
        if (!stream.markSupported()) {
            throw new IllegalArgumentException("InputStream does not support marking");
        }
        this.gifInfoPtr = openStream(stream);
    }

    GifInfoHandle(AssetFileDescriptor afd) throws IOException {
        try {
            this.gifInfoPtr = openFd(afd.getFileDescriptor(), afd.getStartOffset());
        } finally {
            try {
                afd.close();
            } catch (IOException e) {
            }
        }
    }

    static GifInfoHandle openUri(ContentResolver resolver, Uri uri) throws IOException {
        if ("file".equals(uri.getScheme())) {
            return new GifInfoHandle(uri.getPath());
        }
        return new GifInfoHandle(resolver.openAssetFileDescriptor(uri, "r"));
    }

    /* access modifiers changed from: package-private */
    public synchronized long renderFrame(Bitmap frameBuffer) {
        return renderFrame(this.gifInfoPtr, frameBuffer);
    }

    /* access modifiers changed from: package-private */
    public void bindSurface(Surface surface, long[] savedState) {
        bindSurface(this.gifInfoPtr, surface, savedState);
    }

    /* access modifiers changed from: package-private */
    public synchronized void recycle() {
        free(this.gifInfoPtr);
        this.gifInfoPtr = 0;
    }

    /* access modifiers changed from: package-private */
    public synchronized long restoreRemainder() {
        return restoreRemainder(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean reset() {
        return reset(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized void saveRemainder() {
        saveRemainder(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized String getComment() {
        return getComment(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized int getLoopCount() {
        return getLoopCount(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public void setLoopCount(@IntRange(from = 0, mo8779to = 65535) int loopCount) {
        if (loopCount < 0 || loopCount > 65535) {
            throw new IllegalArgumentException("Loop count of range <0, 65535>");
        }
        synchronized (this) {
            setLoopCount(this.gifInfoPtr, (char) loopCount);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized long getSourceLength() {
        return getSourceLength(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized int getNativeErrorCode() {
        return getNativeErrorCode(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public void setSpeedFactor(@FloatRange(from = 0.0d, fromInclusive = false) float factor) {
        if (factor <= 0.0f || Float.isNaN(factor)) {
            throw new IllegalArgumentException("Speed factor is not positive");
        }
        if (factor < 4.656613E-10f) {
            factor = 4.656613E-10f;
        }
        synchronized (this) {
            setSpeedFactor(this.gifInfoPtr, factor);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized int getDuration() {
        return getDuration(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized int getCurrentPosition() {
        return getCurrentPosition(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized int getCurrentFrameIndex() {
        return getCurrentFrameIndex(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized int getCurrentLoop() {
        return getCurrentLoop(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized void seekToTime(@IntRange(from = 0, mo8779to = 2147483647L) int position, Bitmap buffer) {
        seekToTime(this.gifInfoPtr, position, buffer);
    }

    /* access modifiers changed from: package-private */
    public synchronized void seekToFrame(@IntRange(from = 0, mo8779to = 2147483647L) int frameIndex, Bitmap buffer) {
        seekToFrame(this.gifInfoPtr, frameIndex, buffer);
    }

    /* access modifiers changed from: package-private */
    public synchronized long getAllocationByteCount() {
        return getAllocationByteCount(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized long getMetadataByteCount() {
        return getMetadataByteCount(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isRecycled() {
        return this.gifInfoPtr == 0;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void postUnbindSurface() {
        postUnbindSurface(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isAnimationCompleted() {
        return isAnimationCompleted(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized long[] getSavedState() {
        return getSavedState(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized int restoreSavedState(long[] savedState, Bitmap mBuffer) {
        return restoreSavedState(this.gifInfoPtr, savedState, mBuffer);
    }

    /* access modifiers changed from: package-private */
    public int getFrameDuration(@IntRange(from = 0) int index) {
        int frameDuration;
        synchronized (this) {
            if (index >= 0) {
                if (index < getNumberOfFrames(this.gifInfoPtr)) {
                    frameDuration = getFrameDuration(this.gifInfoPtr, index);
                }
            }
            throw new IndexOutOfBoundsException("Frame index is out of bounds");
        }
        return frameDuration;
    }

    /* access modifiers changed from: package-private */
    public void setOptions(char sampleSize, boolean isOpaque) {
        setOptions(this.gifInfoPtr, sampleSize, isOpaque);
    }

    /* access modifiers changed from: package-private */
    public synchronized int getWidth() {
        return getWidth(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized int getHeight() {
        return getHeight(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized int getNumberOfFrames() {
        return getNumberOfFrames(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isOpaque() {
        return isOpaque(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public void glTexImage2D(int target, int level) {
        glTexImage2D(this.gifInfoPtr, target, level);
    }

    /* access modifiers changed from: package-private */
    public void glTexSubImage2D(int target, int level) {
        glTexSubImage2D(this.gifInfoPtr, target, level);
    }

    /* access modifiers changed from: package-private */
    public void startDecoderThread() {
        startDecoderThread(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public void stopDecoderThread() {
        stopDecoderThread(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public void initTexImageDescriptor() {
        initTexImageDescriptor(this.gifInfoPtr);
    }

    /* access modifiers changed from: package-private */
    public void seekToFrameGL(@IntRange(from = 0) int index) {
        if (index < 0 || index >= getNumberOfFrames(this.gifInfoPtr)) {
            throw new IndexOutOfBoundsException("Frame index is out of bounds");
        }
        seekToFrameGL(this.gifInfoPtr, index);
    }
}
