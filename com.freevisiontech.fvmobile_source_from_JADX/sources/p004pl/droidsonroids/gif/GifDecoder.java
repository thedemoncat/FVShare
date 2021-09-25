package p004pl.droidsonroids.gif;

import android.graphics.Bitmap;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.IOException;

/* renamed from: pl.droidsonroids.gif.GifDecoder */
public class GifDecoder {
    private final GifInfoHandle mGifInfoHandle;

    public GifDecoder(@NonNull InputSource inputSource) throws IOException {
        this(inputSource, (GifOptions) null);
    }

    public GifDecoder(@NonNull InputSource inputSource, @Nullable GifOptions options) throws IOException {
        this.mGifInfoHandle = inputSource.open();
        if (options != null) {
            this.mGifInfoHandle.setOptions(options.inSampleSize, options.inIsOpaque);
        }
    }

    public String getComment() {
        return this.mGifInfoHandle.getComment();
    }

    public int getLoopCount() {
        return this.mGifInfoHandle.getLoopCount();
    }

    public long getSourceLength() {
        return this.mGifInfoHandle.getSourceLength();
    }

    public void seekToTime(@IntRange(from = 0, mo8779to = 2147483647L) int position, @NonNull Bitmap buffer) {
        checkBuffer(buffer);
        this.mGifInfoHandle.seekToTime(position, buffer);
    }

    public void seekToFrame(@IntRange(from = 0, mo8779to = 2147483647L) int frameIndex, @NonNull Bitmap buffer) {
        checkBuffer(buffer);
        this.mGifInfoHandle.seekToFrame(frameIndex, buffer);
    }

    public long getAllocationByteCount() {
        return this.mGifInfoHandle.getAllocationByteCount();
    }

    public int getFrameDuration(@IntRange(from = 0) int index) {
        return this.mGifInfoHandle.getFrameDuration(index);
    }

    public int getDuration() {
        return this.mGifInfoHandle.getDuration();
    }

    public int getWidth() {
        return this.mGifInfoHandle.getWidth();
    }

    public int getHeight() {
        return this.mGifInfoHandle.getHeight();
    }

    public int getNumberOfFrames() {
        return this.mGifInfoHandle.getNumberOfFrames();
    }

    public boolean isAnimated() {
        return this.mGifInfoHandle.getNumberOfFrames() > 1 && getDuration() > 0;
    }

    public void recycle() {
        this.mGifInfoHandle.recycle();
    }

    private void checkBuffer(Bitmap buffer) {
        if (buffer.isRecycled()) {
            throw new IllegalArgumentException("Bitmap is recycled");
        } else if (buffer.getWidth() < this.mGifInfoHandle.getWidth() || buffer.getHeight() < this.mGifInfoHandle.getHeight()) {
            throw new IllegalArgumentException("Bitmap ia too small, size must be greater than or equal to GIF size");
        }
    }
}
