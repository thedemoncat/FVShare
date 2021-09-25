package com.google.android.exoplayer;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.util.Assertions;
import java.io.IOException;
import java.util.Arrays;

public final class SingleSampleSource implements SampleSource, SampleSource.SampleSourceReader, Loader.Callback, Loader.Loadable {
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private static final int INITIAL_SAMPLE_SIZE = 1;
    private static final int STATE_END_OF_STREAM = 2;
    private static final int STATE_SEND_FORMAT = 0;
    private static final int STATE_SEND_SAMPLE = 1;
    private IOException currentLoadableException;
    private int currentLoadableExceptionCount;
    private long currentLoadableExceptionTimestamp;
    private final DataSource dataSource;
    private final Handler eventHandler;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    /* access modifiers changed from: private */
    public final int eventSourceId;
    private final MediaFormat format;
    private Loader loader;
    private boolean loadingFinished;
    private final int minLoadableRetryCount;
    private long pendingDiscontinuityPositionUs;
    private byte[] sampleData;
    private int sampleSize;
    private int state;
    private final Uri uri;

    public interface EventListener {
        void onLoadError(int i, IOException iOException);
    }

    public SingleSampleSource(Uri uri2, DataSource dataSource2, MediaFormat format2) {
        this(uri2, dataSource2, format2, 3);
    }

    public SingleSampleSource(Uri uri2, DataSource dataSource2, MediaFormat format2, int minLoadableRetryCount2) {
        this(uri2, dataSource2, format2, minLoadableRetryCount2, (Handler) null, (EventListener) null, 0);
    }

    public SingleSampleSource(Uri uri2, DataSource dataSource2, MediaFormat format2, int minLoadableRetryCount2, Handler eventHandler2, EventListener eventListener2, int eventSourceId2) {
        this.uri = uri2;
        this.dataSource = dataSource2;
        this.format = format2;
        this.minLoadableRetryCount = minLoadableRetryCount2;
        this.eventHandler = eventHandler2;
        this.eventListener = eventListener2;
        this.eventSourceId = eventSourceId2;
        this.sampleData = new byte[1];
    }

    public SampleSource.SampleSourceReader register() {
        return this;
    }

    public boolean prepare(long positionUs) {
        if (this.loader != null) {
            return true;
        }
        this.loader = new Loader("Loader:" + this.format.mimeType);
        return true;
    }

    public int getTrackCount() {
        return 1;
    }

    public MediaFormat getFormat(int track) {
        return this.format;
    }

    public void enable(int track, long positionUs) {
        this.state = 0;
        this.pendingDiscontinuityPositionUs = Long.MIN_VALUE;
        clearCurrentLoadableException();
        maybeStartLoading();
    }

    public boolean continueBuffering(int track, long positionUs) {
        maybeStartLoading();
        return this.loadingFinished;
    }

    public void maybeThrowError() throws IOException {
        if (this.currentLoadableException != null && this.currentLoadableExceptionCount > this.minLoadableRetryCount) {
            throw this.currentLoadableException;
        }
    }

    public long readDiscontinuity(int track) {
        long discontinuityPositionUs = this.pendingDiscontinuityPositionUs;
        this.pendingDiscontinuityPositionUs = Long.MIN_VALUE;
        return discontinuityPositionUs;
    }

    public int readData(int track, long positionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder) {
        boolean z;
        if (this.state == 2) {
            return -1;
        }
        if (this.state == 0) {
            formatHolder.format = this.format;
            this.state = 1;
            return -4;
        }
        if (this.state == 1) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!this.loadingFinished) {
            return -2;
        }
        sampleHolder.timeUs = 0;
        sampleHolder.size = this.sampleSize;
        sampleHolder.flags = 1;
        sampleHolder.ensureSpaceForWrite(sampleHolder.size);
        sampleHolder.data.put(this.sampleData, 0, this.sampleSize);
        this.state = 2;
        return -3;
    }

    public void seekToUs(long positionUs) {
        if (this.state == 2) {
            this.pendingDiscontinuityPositionUs = positionUs;
            this.state = 1;
        }
    }

    public long getBufferedPositionUs() {
        return this.loadingFinished ? -3 : 0;
    }

    public void disable(int track) {
        this.state = 2;
    }

    public void release() {
        if (this.loader != null) {
            this.loader.release();
            this.loader = null;
        }
    }

    private void maybeStartLoading() {
        if (!this.loadingFinished && this.state != 2 && !this.loader.isLoading()) {
            if (this.currentLoadableException != null) {
                if (SystemClock.elapsedRealtime() - this.currentLoadableExceptionTimestamp >= getRetryDelayMillis((long) this.currentLoadableExceptionCount)) {
                    this.currentLoadableException = null;
                } else {
                    return;
                }
            }
            this.loader.startLoading(this, this);
        }
    }

    private void clearCurrentLoadableException() {
        this.currentLoadableException = null;
        this.currentLoadableExceptionCount = 0;
    }

    private long getRetryDelayMillis(long errorCount) {
        return Math.min((errorCount - 1) * 1000, 5000);
    }

    public void onLoadCompleted(Loader.Loadable loadable) {
        this.loadingFinished = true;
        clearCurrentLoadableException();
    }

    public void onLoadCanceled(Loader.Loadable loadable) {
    }

    public void onLoadError(Loader.Loadable loadable, IOException e) {
        this.currentLoadableException = e;
        this.currentLoadableExceptionCount++;
        this.currentLoadableExceptionTimestamp = SystemClock.elapsedRealtime();
        notifyLoadError(e);
        maybeStartLoading();
    }

    public void cancelLoad() {
    }

    public boolean isLoadCanceled() {
        return false;
    }

    public void load() throws IOException, InterruptedException {
        this.sampleSize = 0;
        try {
            this.dataSource.open(new DataSpec(this.uri));
            int result = 0;
            while (result != -1) {
                this.sampleSize += result;
                if (this.sampleSize == this.sampleData.length) {
                    this.sampleData = Arrays.copyOf(this.sampleData, this.sampleData.length * 2);
                }
                result = this.dataSource.read(this.sampleData, this.sampleSize, this.sampleData.length - this.sampleSize);
            }
        } finally {
            this.dataSource.close();
        }
    }

    private void notifyLoadError(final IOException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    SingleSampleSource.this.eventListener.onLoadError(SingleSampleSource.this.eventSourceId, e);
                }
            });
        }
    }
}
