package com.google.android.exoplayer.util;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.upstream.UriDataSource;
import com.google.android.exoplayer.upstream.UriLoadable;
import java.io.IOException;
import java.util.concurrent.CancellationException;

public class ManifestFetcher<T> implements Loader.Callback {
    private long currentLoadStartTimestamp;
    private UriLoadable<T> currentLoadable;
    private int enabledCount;
    private final Handler eventHandler;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    private ManifestIOException loadException;
    private int loadExceptionCount;
    private long loadExceptionTimestamp;
    private Loader loader;
    private volatile T manifest;
    private volatile long manifestLoadCompleteTimestamp;
    private volatile long manifestLoadStartTimestamp;
    volatile String manifestUri;
    private final UriLoadable.Parser<T> parser;
    private final UriDataSource uriDataSource;

    public interface EventListener {
        void onManifestError(IOException iOException);

        void onManifestRefreshStarted();

        void onManifestRefreshed();
    }

    public interface ManifestCallback<T> {
        void onSingleManifest(T t);

        void onSingleManifestError(IOException iOException);
    }

    public interface RedirectingManifest {
        String getNextManifestUri();
    }

    public static final class ManifestIOException extends IOException {
        public ManifestIOException(Throwable cause) {
            super(cause);
        }
    }

    public ManifestFetcher(String manifestUri2, UriDataSource uriDataSource2, UriLoadable.Parser<T> parser2) {
        this(manifestUri2, uriDataSource2, parser2, (Handler) null, (EventListener) null);
    }

    public ManifestFetcher(String manifestUri2, UriDataSource uriDataSource2, UriLoadable.Parser<T> parser2, Handler eventHandler2, EventListener eventListener2) {
        this.parser = parser2;
        this.manifestUri = manifestUri2;
        this.uriDataSource = uriDataSource2;
        this.eventHandler = eventHandler2;
        this.eventListener = eventListener2;
    }

    public void updateManifestUri(String manifestUri2) {
        this.manifestUri = manifestUri2;
    }

    public void singleLoad(Looper callbackLooper, ManifestCallback<T> callback) {
        new SingleFetchHelper(new UriLoadable(this.manifestUri, this.uriDataSource, this.parser), callbackLooper, callback).startLoading();
    }

    public T getManifest() {
        return this.manifest;
    }

    public long getManifestLoadStartTimestamp() {
        return this.manifestLoadStartTimestamp;
    }

    public long getManifestLoadCompleteTimestamp() {
        return this.manifestLoadCompleteTimestamp;
    }

    public void maybeThrowError() throws ManifestIOException {
        if (this.loadException != null && this.loadExceptionCount > 1) {
            throw this.loadException;
        }
    }

    public void enable() {
        int i = this.enabledCount;
        this.enabledCount = i + 1;
        if (i == 0) {
            this.loadExceptionCount = 0;
            this.loadException = null;
        }
    }

    public void disable() {
        int i = this.enabledCount - 1;
        this.enabledCount = i;
        if (i == 0 && this.loader != null) {
            this.loader.release();
            this.loader = null;
        }
    }

    public void requestRefresh() {
        if (this.loadException == null || SystemClock.elapsedRealtime() >= this.loadExceptionTimestamp + getRetryDelayMillis((long) this.loadExceptionCount)) {
            if (this.loader == null) {
                this.loader = new Loader("manifestLoader");
            }
            if (!this.loader.isLoading()) {
                this.currentLoadable = new UriLoadable<>(this.manifestUri, this.uriDataSource, this.parser);
                this.currentLoadStartTimestamp = SystemClock.elapsedRealtime();
                this.loader.startLoading(this.currentLoadable, this);
                notifyManifestRefreshStarted();
            }
        }
    }

    public void onLoadCompleted(Loader.Loadable loadable) {
        if (this.currentLoadable == loadable) {
            this.manifest = this.currentLoadable.getResult();
            this.manifestLoadStartTimestamp = this.currentLoadStartTimestamp;
            this.manifestLoadCompleteTimestamp = SystemClock.elapsedRealtime();
            this.loadExceptionCount = 0;
            this.loadException = null;
            if (this.manifest instanceof RedirectingManifest) {
                String nextLocation = ((RedirectingManifest) this.manifest).getNextManifestUri();
                if (!TextUtils.isEmpty(nextLocation)) {
                    this.manifestUri = nextLocation;
                }
            }
            notifyManifestRefreshed();
        }
    }

    public void onLoadCanceled(Loader.Loadable loadable) {
    }

    public void onLoadError(Loader.Loadable loadable, IOException exception) {
        if (this.currentLoadable == loadable) {
            this.loadExceptionCount++;
            this.loadExceptionTimestamp = SystemClock.elapsedRealtime();
            this.loadException = new ManifestIOException(exception);
            notifyManifestError(this.loadException);
        }
    }

    /* access modifiers changed from: package-private */
    public void onSingleFetchCompleted(T result, long loadStartTimestamp) {
        this.manifest = result;
        this.manifestLoadStartTimestamp = loadStartTimestamp;
        this.manifestLoadCompleteTimestamp = SystemClock.elapsedRealtime();
    }

    private long getRetryDelayMillis(long errorCount) {
        return Math.min((errorCount - 1) * 1000, 5000);
    }

    private void notifyManifestRefreshStarted() {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ManifestFetcher.this.eventListener.onManifestRefreshStarted();
                }
            });
        }
    }

    private void notifyManifestRefreshed() {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ManifestFetcher.this.eventListener.onManifestRefreshed();
                }
            });
        }
    }

    private void notifyManifestError(final IOException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ManifestFetcher.this.eventListener.onManifestError(e);
                }
            });
        }
    }

    private class SingleFetchHelper implements Loader.Callback {
        private final Looper callbackLooper;
        private long loadStartTimestamp;
        private final UriLoadable<T> singleUseLoadable;
        private final Loader singleUseLoader = new Loader("manifestLoader:single");
        private final ManifestCallback<T> wrappedCallback;

        public SingleFetchHelper(UriLoadable<T> singleUseLoadable2, Looper callbackLooper2, ManifestCallback<T> wrappedCallback2) {
            this.singleUseLoadable = singleUseLoadable2;
            this.callbackLooper = callbackLooper2;
            this.wrappedCallback = wrappedCallback2;
        }

        public void startLoading() {
            this.loadStartTimestamp = SystemClock.elapsedRealtime();
            this.singleUseLoader.startLoading(this.callbackLooper, this.singleUseLoadable, this);
        }

        public void onLoadCompleted(Loader.Loadable loadable) {
            try {
                T result = this.singleUseLoadable.getResult();
                ManifestFetcher.this.onSingleFetchCompleted(result, this.loadStartTimestamp);
                this.wrappedCallback.onSingleManifest(result);
            } finally {
                releaseLoader();
            }
        }

        public void onLoadCanceled(Loader.Loadable loadable) {
            try {
                this.wrappedCallback.onSingleManifestError(new ManifestIOException(new CancellationException()));
            } finally {
                releaseLoader();
            }
        }

        public void onLoadError(Loader.Loadable loadable, IOException exception) {
            try {
                this.wrappedCallback.onSingleManifestError(exception);
            } finally {
                releaseLoader();
            }
        }

        private void releaseLoader() {
            this.singleUseLoader.release();
        }
    }
}
