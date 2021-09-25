package p004pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Locale;
import p004pl.droidsonroids.gif.annotations.Beta;

/* renamed from: pl.droidsonroids.gif.GifAnimationMetaData */
public class GifAnimationMetaData implements Serializable, Parcelable {
    public static final Parcelable.Creator<GifAnimationMetaData> CREATOR = new Parcelable.Creator<GifAnimationMetaData>() {
        public GifAnimationMetaData createFromParcel(Parcel source) {
            return new GifAnimationMetaData(source);
        }

        public GifAnimationMetaData[] newArray(int size) {
            return new GifAnimationMetaData[size];
        }
    };
    private static final long serialVersionUID = 5692363926580237325L;
    private final int mDuration;
    private final int mHeight;
    private final int mImageCount;
    private final int mLoopCount;
    private final long mMetadataBytesCount;
    private final long mPixelsBytesCount;
    private final int mWidth;

    public GifAnimationMetaData(@NonNull Resources res, @RawRes @DrawableRes int id) throws Resources.NotFoundException, IOException {
        this(res.openRawResourceFd(id));
    }

    public GifAnimationMetaData(@NonNull AssetManager assets, @NonNull String assetName) throws IOException {
        this(assets.openFd(assetName));
    }

    public GifAnimationMetaData(@NonNull String filePath) throws IOException {
        this(new GifInfoHandle(filePath));
    }

    public GifAnimationMetaData(@NonNull File file) throws IOException {
        this(file.getPath());
    }

    public GifAnimationMetaData(@NonNull InputStream stream) throws IOException {
        this(new GifInfoHandle(stream));
    }

    public GifAnimationMetaData(@NonNull AssetFileDescriptor afd) throws IOException {
        this(new GifInfoHandle(afd));
    }

    public GifAnimationMetaData(@NonNull FileDescriptor fd) throws IOException {
        this(new GifInfoHandle(fd));
    }

    public GifAnimationMetaData(@NonNull byte[] bytes) throws IOException {
        this(new GifInfoHandle(bytes));
    }

    public GifAnimationMetaData(@NonNull ByteBuffer buffer) throws IOException {
        this(new GifInfoHandle(buffer));
    }

    public GifAnimationMetaData(@Nullable ContentResolver resolver, @NonNull Uri uri) throws IOException {
        this(GifInfoHandle.openUri(resolver, uri));
    }

    private GifAnimationMetaData(GifInfoHandle gifInfoHandle) {
        this.mLoopCount = gifInfoHandle.getLoopCount();
        this.mDuration = gifInfoHandle.getDuration();
        this.mWidth = gifInfoHandle.getWidth();
        this.mHeight = gifInfoHandle.getHeight();
        this.mImageCount = gifInfoHandle.getNumberOfFrames();
        this.mMetadataBytesCount = gifInfoHandle.getMetadataByteCount();
        this.mPixelsBytesCount = gifInfoHandle.getAllocationByteCount();
        gifInfoHandle.recycle();
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getNumberOfFrames() {
        return this.mImageCount;
    }

    public int getLoopCount() {
        return this.mLoopCount;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public boolean isAnimated() {
        return this.mImageCount > 1 && this.mDuration > 0;
    }

    public long getAllocationByteCount() {
        return this.mPixelsBytesCount;
    }

    @Beta
    public long getDrawableAllocationByteCount(@Nullable GifDrawable oldDrawable, @IntRange(from = 1, mo8779to = 65535) int sampleSize) {
        long bufferSize;
        if (sampleSize < 1 || sampleSize > 65535) {
            throw new IllegalStateException("Sample size " + sampleSize + " out of range <1, " + 65535 + ">");
        }
        int sampleSizeFactor = sampleSize * sampleSize;
        if (oldDrawable == null || oldDrawable.mBuffer.isRecycled()) {
            bufferSize = (long) (((this.mWidth * this.mHeight) * 4) / sampleSizeFactor);
        } else if (Build.VERSION.SDK_INT >= 19) {
            bufferSize = (long) oldDrawable.mBuffer.getAllocationByteCount();
        } else {
            bufferSize = (long) oldDrawable.getFrameByteCount();
        }
        return (this.mPixelsBytesCount / ((long) sampleSizeFactor)) + bufferSize;
    }

    public long getMetadataAllocationByteCount() {
        return this.mMetadataBytesCount;
    }

    public String toString() {
        String suffix = String.format(Locale.ENGLISH, "GIF: size: %dx%d, frames: %d, loops: %s, duration: %d", new Object[]{Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight), Integer.valueOf(this.mImageCount), this.mLoopCount == 0 ? "Infinity" : Integer.toString(this.mLoopCount), Integer.valueOf(this.mDuration)});
        return isAnimated() ? "Animated " + suffix : suffix;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mLoopCount);
        dest.writeInt(this.mDuration);
        dest.writeInt(this.mHeight);
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mImageCount);
        dest.writeLong(this.mMetadataBytesCount);
        dest.writeLong(this.mPixelsBytesCount);
    }

    private GifAnimationMetaData(Parcel in) {
        this.mLoopCount = in.readInt();
        this.mDuration = in.readInt();
        this.mHeight = in.readInt();
        this.mWidth = in.readInt();
        this.mImageCount = in.readInt();
        this.mMetadataBytesCount = in.readLong();
        this.mPixelsBytesCount = in.readLong();
    }
}
