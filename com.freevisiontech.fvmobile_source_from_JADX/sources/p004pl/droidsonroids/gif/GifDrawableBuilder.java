package p004pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import p004pl.droidsonroids.gif.InputSource;
import p004pl.droidsonroids.gif.annotations.Beta;

/* renamed from: pl.droidsonroids.gif.GifDrawableBuilder */
public class GifDrawableBuilder {
    private ScheduledThreadPoolExecutor mExecutor;
    private InputSource mInputSource;
    private boolean mIsRenderingTriggeredOnDraw = true;
    private GifDrawable mOldDrawable;
    private GifOptions mOptions = new GifOptions();

    public GifDrawableBuilder sampleSize(@IntRange(from = 1, mo8779to = 65535) int sampleSize) {
        this.mOptions.setInSampleSize(sampleSize);
        return this;
    }

    public GifDrawable build() throws IOException {
        if (this.mInputSource != null) {
            return this.mInputSource.build(this.mOldDrawable, this.mExecutor, this.mIsRenderingTriggeredOnDraw, this.mOptions);
        }
        throw new NullPointerException("Source is not set");
    }

    public GifDrawableBuilder with(GifDrawable drawable) {
        this.mOldDrawable = drawable;
        return this;
    }

    public GifDrawableBuilder threadPoolSize(int threadPoolSize) {
        this.mExecutor = new ScheduledThreadPoolExecutor(threadPoolSize);
        return this;
    }

    public GifDrawableBuilder taskExecutor(ScheduledThreadPoolExecutor executor) {
        this.mExecutor = executor;
        return this;
    }

    public GifDrawableBuilder renderingTriggeredOnDraw(boolean isRenderingTriggeredOnDraw) {
        this.mIsRenderingTriggeredOnDraw = isRenderingTriggeredOnDraw;
        return this;
    }

    public GifDrawableBuilder setRenderingTriggeredOnDraw(boolean isRenderingTriggeredOnDraw) {
        return renderingTriggeredOnDraw(isRenderingTriggeredOnDraw);
    }

    @Beta
    public GifDrawableBuilder options(@Nullable GifOptions options) {
        this.mOptions.setFrom(options);
        return this;
    }

    public GifDrawableBuilder from(InputStream inputStream) {
        this.mInputSource = new InputSource.InputStreamSource(inputStream);
        return this;
    }

    public GifDrawableBuilder from(AssetFileDescriptor assetFileDescriptor) {
        this.mInputSource = new InputSource.AssetFileDescriptorSource(assetFileDescriptor);
        return this;
    }

    public GifDrawableBuilder from(FileDescriptor fileDescriptor) {
        this.mInputSource = new InputSource.FileDescriptorSource(fileDescriptor);
        return this;
    }

    public GifDrawableBuilder from(AssetManager assetManager, String assetName) {
        this.mInputSource = new InputSource.AssetSource(assetManager, assetName);
        return this;
    }

    public GifDrawableBuilder from(ContentResolver contentResolver, Uri uri) {
        this.mInputSource = new InputSource.UriSource(contentResolver, uri);
        return this;
    }

    public GifDrawableBuilder from(File file) {
        this.mInputSource = new InputSource.FileSource(file);
        return this;
    }

    public GifDrawableBuilder from(String filePath) {
        this.mInputSource = new InputSource.FileSource(filePath);
        return this;
    }

    public GifDrawableBuilder from(byte[] bytes) {
        this.mInputSource = new InputSource.ByteArraySource(bytes);
        return this;
    }

    public GifDrawableBuilder from(ByteBuffer byteBuffer) {
        this.mInputSource = new InputSource.DirectByteBufferSource(byteBuffer);
        return this;
    }

    public GifDrawableBuilder from(Resources resources, int resourceId) {
        this.mInputSource = new InputSource.ResourcesSource(resources, resourceId);
        return this;
    }
}
