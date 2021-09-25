package p004pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/* renamed from: pl.droidsonroids.gif.InputSource */
public abstract class InputSource {
    /* access modifiers changed from: package-private */
    public abstract GifInfoHandle open() throws IOException;

    private InputSource() {
    }

    /* access modifiers changed from: package-private */
    public final GifDrawable build(GifDrawable oldDrawable, ScheduledThreadPoolExecutor executor, boolean isRenderingAlwaysEnabled, GifOptions options) throws IOException {
        GifInfoHandle handle = open();
        handle.setOptions(options.inSampleSize, options.inIsOpaque);
        return new GifDrawable(handle, oldDrawable, executor, isRenderingAlwaysEnabled);
    }

    /* renamed from: pl.droidsonroids.gif.InputSource$DirectByteBufferSource */
    public static final class DirectByteBufferSource extends InputSource {
        private final ByteBuffer byteBuffer;

        public DirectByteBufferSource(@NonNull ByteBuffer byteBuffer2) {
            super();
            this.byteBuffer = byteBuffer2;
        }

        /* access modifiers changed from: package-private */
        public GifInfoHandle open() throws GifIOException {
            return new GifInfoHandle(this.byteBuffer);
        }
    }

    /* renamed from: pl.droidsonroids.gif.InputSource$ByteArraySource */
    public static final class ByteArraySource extends InputSource {
        private final byte[] bytes;

        public ByteArraySource(@NonNull byte[] bytes2) {
            super();
            this.bytes = bytes2;
        }

        /* access modifiers changed from: package-private */
        public GifInfoHandle open() throws GifIOException {
            return new GifInfoHandle(this.bytes);
        }
    }

    /* renamed from: pl.droidsonroids.gif.InputSource$FileSource */
    public static final class FileSource extends InputSource {
        private final String mPath;

        public FileSource(@NonNull File file) {
            super();
            this.mPath = file.getPath();
        }

        public FileSource(@NonNull String filePath) {
            super();
            this.mPath = filePath;
        }

        /* access modifiers changed from: package-private */
        public GifInfoHandle open() throws GifIOException {
            return new GifInfoHandle(this.mPath);
        }
    }

    /* renamed from: pl.droidsonroids.gif.InputSource$UriSource */
    public static final class UriSource extends InputSource {
        private final ContentResolver mContentResolver;
        private final Uri mUri;

        public UriSource(@Nullable ContentResolver contentResolver, @NonNull Uri uri) {
            super();
            this.mContentResolver = contentResolver;
            this.mUri = uri;
        }

        /* access modifiers changed from: package-private */
        public GifInfoHandle open() throws IOException {
            return GifInfoHandle.openUri(this.mContentResolver, this.mUri);
        }
    }

    /* renamed from: pl.droidsonroids.gif.InputSource$AssetSource */
    public static final class AssetSource extends InputSource {
        private final AssetManager mAssetManager;
        private final String mAssetName;

        public AssetSource(@NonNull AssetManager assetManager, @NonNull String assetName) {
            super();
            this.mAssetManager = assetManager;
            this.mAssetName = assetName;
        }

        /* access modifiers changed from: package-private */
        public GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mAssetManager.openFd(this.mAssetName));
        }
    }

    /* renamed from: pl.droidsonroids.gif.InputSource$FileDescriptorSource */
    public static final class FileDescriptorSource extends InputSource {
        private final FileDescriptor mFd;

        public FileDescriptorSource(@NonNull FileDescriptor fileDescriptor) {
            super();
            this.mFd = fileDescriptor;
        }

        /* access modifiers changed from: package-private */
        public GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mFd);
        }
    }

    /* renamed from: pl.droidsonroids.gif.InputSource$InputStreamSource */
    public static final class InputStreamSource extends InputSource {
        private final InputStream inputStream;

        public InputStreamSource(@NonNull InputStream inputStream2) {
            super();
            this.inputStream = inputStream2;
        }

        /* access modifiers changed from: package-private */
        public GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.inputStream);
        }
    }

    /* renamed from: pl.droidsonroids.gif.InputSource$ResourcesSource */
    public static class ResourcesSource extends InputSource {
        private final int mResourceId;
        private final Resources mResources;

        public ResourcesSource(@NonNull Resources resources, @RawRes @DrawableRes int resourceId) {
            super();
            this.mResources = resources;
            this.mResourceId = resourceId;
        }

        /* access modifiers changed from: package-private */
        public GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mResources.openRawResourceFd(this.mResourceId));
        }
    }

    /* renamed from: pl.droidsonroids.gif.InputSource$AssetFileDescriptorSource */
    public static class AssetFileDescriptorSource extends InputSource {
        private final AssetFileDescriptor mAssetFileDescriptor;

        public AssetFileDescriptorSource(@NonNull AssetFileDescriptor assetFileDescriptor) {
            super();
            this.mAssetFileDescriptor = assetFileDescriptor;
        }

        /* access modifiers changed from: package-private */
        public GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mAssetFileDescriptor);
        }
    }
}
