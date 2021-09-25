package com.google.android.exoplayer.chunk;

import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.MediaFormatHolder;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.DefaultTrackOutput;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChunkSampleSource implements SampleSource, SampleSource.SampleSourceReader, Loader.Callback {
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private static final long NO_RESET_PENDING = Long.MIN_VALUE;
    private static final int STATE_ENABLED = 3;
    private static final int STATE_IDLE = 0;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_PREPARED = 2;
    private final int bufferSizeContribution;
    private final ChunkSource chunkSource;
    private long currentLoadStartTimeMs;
    private IOException currentLoadableException;
    private int currentLoadableExceptionCount;
    private long currentLoadableExceptionTimestamp;
    private final ChunkOperationHolder currentLoadableHolder;
    private DrmInitData downstreamDrmInitData;
    private Format downstreamFormat;
    private MediaFormat downstreamMediaFormat;
    private long downstreamPositionUs;
    private int enabledTrackCount;
    private final Handler eventHandler;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    /* access modifiers changed from: private */
    public final int eventSourceId;
    private long lastPerformedBufferOperation;
    private long lastSeekPositionUs;
    private final LoadControl loadControl;
    private Loader loader;
    private boolean loadingFinished;
    private final LinkedList<BaseMediaChunk> mediaChunks;
    private final int minLoadableRetryCount;
    private boolean pendingDiscontinuity;
    private long pendingResetPositionUs;
    private final List<BaseMediaChunk> readOnlyMediaChunks;
    protected final DefaultTrackOutput sampleQueue;
    private int state;

    public interface EventListener extends BaseChunkSampleSourceEventListener {
    }

    public ChunkSampleSource(ChunkSource chunkSource2, LoadControl loadControl2, int bufferSizeContribution2) {
        this(chunkSource2, loadControl2, bufferSizeContribution2, (Handler) null, (EventListener) null, 0);
    }

    public ChunkSampleSource(ChunkSource chunkSource2, LoadControl loadControl2, int bufferSizeContribution2, Handler eventHandler2, EventListener eventListener2, int eventSourceId2) {
        this(chunkSource2, loadControl2, bufferSizeContribution2, eventHandler2, eventListener2, eventSourceId2, 3);
    }

    public ChunkSampleSource(ChunkSource chunkSource2, LoadControl loadControl2, int bufferSizeContribution2, Handler eventHandler2, EventListener eventListener2, int eventSourceId2, int minLoadableRetryCount2) {
        this.chunkSource = chunkSource2;
        this.loadControl = loadControl2;
        this.bufferSizeContribution = bufferSizeContribution2;
        this.eventHandler = eventHandler2;
        this.eventListener = eventListener2;
        this.eventSourceId = eventSourceId2;
        this.minLoadableRetryCount = minLoadableRetryCount2;
        this.currentLoadableHolder = new ChunkOperationHolder();
        this.mediaChunks = new LinkedList<>();
        this.readOnlyMediaChunks = Collections.unmodifiableList(this.mediaChunks);
        this.sampleQueue = new DefaultTrackOutput(loadControl2.getAllocator());
        this.state = 0;
        this.pendingResetPositionUs = Long.MIN_VALUE;
    }

    public SampleSource.SampleSourceReader register() {
        Assertions.checkState(this.state == 0);
        this.state = 1;
        return this;
    }

    public boolean prepare(long positionUs) {
        Assertions.checkState(this.state == 1 || this.state == 2);
        if (this.state == 2) {
            return true;
        }
        if (!this.chunkSource.prepare()) {
            return false;
        }
        if (this.chunkSource.getTrackCount() > 0) {
            this.loader = new Loader("Loader:" + this.chunkSource.getFormat(0).mimeType);
        }
        this.state = 2;
        return true;
    }

    public int getTrackCount() {
        Assertions.checkState(this.state == 2 || this.state == 3);
        return this.chunkSource.getTrackCount();
    }

    public MediaFormat getFormat(int track) {
        Assertions.checkState(this.state == 2 || this.state == 3);
        return this.chunkSource.getFormat(track);
    }

    public void enable(int track, long positionUs) {
        boolean z = true;
        Assertions.checkState(this.state == 2);
        int i = this.enabledTrackCount;
        this.enabledTrackCount = i + 1;
        if (i != 0) {
            z = false;
        }
        Assertions.checkState(z);
        this.state = 3;
        this.chunkSource.enable(track);
        this.loadControl.register(this, this.bufferSizeContribution);
        this.downstreamFormat = null;
        this.downstreamMediaFormat = null;
        this.downstreamDrmInitData = null;
        this.downstreamPositionUs = positionUs;
        this.lastSeekPositionUs = positionUs;
        this.pendingDiscontinuity = false;
        restartFrom(positionUs);
    }

    public void disable(int track) {
        boolean isLoading;
        boolean z = true;
        Assertions.checkState(this.state == 3);
        int i = this.enabledTrackCount - 1;
        this.enabledTrackCount = i;
        if (i != 0) {
            z = false;
        }
        Assertions.checkState(z);
        this.state = 2;
        try {
            this.chunkSource.disable(this.mediaChunks);
            if (!isLoading) {
                this.sampleQueue.clear();
                this.mediaChunks.clear();
                clearCurrentLoadable();
                this.loadControl.trimAllocator();
            }
        } finally {
            this.loadControl.unregister(this);
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
            } else {
                this.sampleQueue.clear();
                this.mediaChunks.clear();
                clearCurrentLoadable();
                this.loadControl.trimAllocator();
            }
        }
    }

    public boolean continueBuffering(int track, long positionUs) {
        boolean z;
        if (this.state == 3) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        this.downstreamPositionUs = positionUs;
        this.chunkSource.continueBuffering(positionUs);
        updateLoadControl();
        if (this.loadingFinished || !this.sampleQueue.isEmpty()) {
            return true;
        }
        return false;
    }

    public long readDiscontinuity(int track) {
        if (!this.pendingDiscontinuity) {
            return Long.MIN_VALUE;
        }
        this.pendingDiscontinuity = false;
        return this.lastSeekPositionUs;
    }

    public int readData(int track, long positionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder) {
        Assertions.checkState(this.state == 3);
        this.downstreamPositionUs = positionUs;
        if (this.pendingDiscontinuity || isPendingReset()) {
            return -2;
        }
        boolean haveSamples = !this.sampleQueue.isEmpty();
        BaseMediaChunk currentChunk = this.mediaChunks.getFirst();
        while (haveSamples && this.mediaChunks.size() > 1 && this.mediaChunks.get(1).getFirstSampleIndex() <= this.sampleQueue.getReadIndex()) {
            this.mediaChunks.removeFirst();
            currentChunk = this.mediaChunks.getFirst();
        }
        Format format = currentChunk.format;
        if (!format.equals(this.downstreamFormat)) {
            notifyDownstreamFormatChanged(format, currentChunk.trigger, currentChunk.startTimeUs);
        }
        this.downstreamFormat = format;
        if (haveSamples || currentChunk.isMediaFormatFinal) {
            MediaFormat mediaFormat = currentChunk.getMediaFormat();
            DrmInitData drmInitData = currentChunk.getDrmInitData();
            if (!mediaFormat.equals(this.downstreamMediaFormat) || !Util.areEqual(this.downstreamDrmInitData, drmInitData)) {
                formatHolder.format = mediaFormat;
                formatHolder.drmInitData = drmInitData;
                this.downstreamMediaFormat = mediaFormat;
                this.downstreamDrmInitData = drmInitData;
                return -4;
            }
            this.downstreamMediaFormat = mediaFormat;
            this.downstreamDrmInitData = drmInitData;
        }
        if (!haveSamples) {
            if (this.loadingFinished) {
                return -1;
            }
            return -2;
        } else if (!this.sampleQueue.getSample(sampleHolder)) {
            return -2;
        } else {
            boolean decodeOnly = sampleHolder.timeUs < this.lastSeekPositionUs;
            sampleHolder.flags = (decodeOnly ? C1907C.SAMPLE_FLAG_DECODE_ONLY : 0) | sampleHolder.flags;
            onSampleRead(currentChunk, sampleHolder);
            return -3;
        }
    }

    public void seekToUs(long positionUs) {
        boolean z;
        boolean seekInsideBuffer;
        boolean haveSamples;
        if (this.state == 3) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        long currentPositionUs = isPendingReset() ? this.pendingResetPositionUs : this.downstreamPositionUs;
        this.downstreamPositionUs = positionUs;
        this.lastSeekPositionUs = positionUs;
        if (currentPositionUs != positionUs) {
            if (isPendingReset() || !this.sampleQueue.skipToKeyframeBefore(positionUs)) {
                seekInsideBuffer = false;
            } else {
                seekInsideBuffer = true;
            }
            if (seekInsideBuffer) {
                if (!this.sampleQueue.isEmpty()) {
                    haveSamples = true;
                } else {
                    haveSamples = false;
                }
                while (haveSamples && this.mediaChunks.size() > 1 && this.mediaChunks.get(1).getFirstSampleIndex() <= this.sampleQueue.getReadIndex()) {
                    this.mediaChunks.removeFirst();
                }
            } else {
                restartFrom(positionUs);
            }
            this.pendingDiscontinuity = true;
        }
    }

    public void maybeThrowError() throws IOException {
        if (this.currentLoadableException != null && this.currentLoadableExceptionCount > this.minLoadableRetryCount) {
            throw this.currentLoadableException;
        } else if (this.currentLoadableHolder.chunk == null) {
            this.chunkSource.maybeThrowError();
        }
    }

    public long getBufferedPositionUs() {
        Assertions.checkState(this.state == 3);
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        if (this.loadingFinished) {
            return -3;
        }
        long largestParsedTimestampUs = this.sampleQueue.getLargestParsedTimestampUs();
        return largestParsedTimestampUs == Long.MIN_VALUE ? this.downstreamPositionUs : largestParsedTimestampUs;
    }

    public void release() {
        Assertions.checkState(this.state != 3);
        if (this.loader != null) {
            this.loader.release();
            this.loader = null;
        }
        this.state = 0;
    }

    public void onLoadCompleted(Loader.Loadable loadable) {
        long now = SystemClock.elapsedRealtime();
        long loadDurationMs = now - this.currentLoadStartTimeMs;
        Chunk currentLoadable = this.currentLoadableHolder.chunk;
        this.chunkSource.onChunkLoadCompleted(currentLoadable);
        if (isMediaChunk(currentLoadable)) {
            BaseMediaChunk mediaChunk = (BaseMediaChunk) currentLoadable;
            notifyLoadCompleted(currentLoadable.bytesLoaded(), mediaChunk.type, mediaChunk.trigger, mediaChunk.format, mediaChunk.startTimeUs, mediaChunk.endTimeUs, now, loadDurationMs);
        } else {
            notifyLoadCompleted(currentLoadable.bytesLoaded(), currentLoadable.type, currentLoadable.trigger, currentLoadable.format, -1, -1, now, loadDurationMs);
        }
        clearCurrentLoadable();
        updateLoadControl();
    }

    public void onLoadCanceled(Loader.Loadable loadable) {
        notifyLoadCanceled(this.currentLoadableHolder.chunk.bytesLoaded());
        clearCurrentLoadable();
        if (this.state == 3) {
            restartFrom(this.pendingResetPositionUs);
            return;
        }
        this.sampleQueue.clear();
        this.mediaChunks.clear();
        clearCurrentLoadable();
        this.loadControl.trimAllocator();
    }

    public void onLoadError(Loader.Loadable loadable, IOException e) {
        this.currentLoadableException = e;
        this.currentLoadableExceptionCount++;
        this.currentLoadableExceptionTimestamp = SystemClock.elapsedRealtime();
        notifyLoadError(e);
        this.chunkSource.onChunkLoadError(this.currentLoadableHolder.chunk, e);
        updateLoadControl();
    }

    /* access modifiers changed from: protected */
    public void onSampleRead(MediaChunk mediaChunk, SampleHolder sampleHolder) {
    }

    private void restartFrom(long positionUs) {
        this.pendingResetPositionUs = positionUs;
        this.loadingFinished = false;
        if (this.loader.isLoading()) {
            this.loader.cancelLoading();
            return;
        }
        this.sampleQueue.clear();
        this.mediaChunks.clear();
        clearCurrentLoadable();
        updateLoadControl();
    }

    private void clearCurrentLoadable() {
        this.currentLoadableHolder.chunk = null;
        clearCurrentLoadableException();
    }

    private void clearCurrentLoadableException() {
        this.currentLoadableException = null;
        this.currentLoadableExceptionCount = 0;
    }

    private void updateLoadControl() {
        boolean isBackedOff;
        boolean loadingOrBackedOff;
        long now = SystemClock.elapsedRealtime();
        long nextLoadPositionUs = getNextLoadPositionUs();
        if (this.currentLoadableException != null) {
            isBackedOff = true;
        } else {
            isBackedOff = false;
        }
        if (this.loader.isLoading() || isBackedOff) {
            loadingOrBackedOff = true;
        } else {
            loadingOrBackedOff = false;
        }
        if (!loadingOrBackedOff && ((this.currentLoadableHolder.chunk == null && nextLoadPositionUs != -1) || now - this.lastPerformedBufferOperation > 2000)) {
            this.lastPerformedBufferOperation = now;
            doChunkOperation();
            boolean chunksDiscarded = discardUpstreamMediaChunks(this.currentLoadableHolder.queueSize);
            if (this.currentLoadableHolder.chunk == null) {
                nextLoadPositionUs = -1;
            } else if (chunksDiscarded) {
                nextLoadPositionUs = getNextLoadPositionUs();
            }
        }
        boolean nextLoader = this.loadControl.update(this, this.downstreamPositionUs, nextLoadPositionUs, loadingOrBackedOff);
        if (isBackedOff) {
            if (now - this.currentLoadableExceptionTimestamp >= getRetryDelayMillis((long) this.currentLoadableExceptionCount)) {
                resumeFromBackOff();
            }
        } else if (!this.loader.isLoading() && nextLoader) {
            maybeStartLoading();
        }
    }

    private long getNextLoadPositionUs() {
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        if (this.loadingFinished) {
            return -1;
        }
        return this.mediaChunks.getLast().endTimeUs;
    }

    private void resumeFromBackOff() {
        this.currentLoadableException = null;
        Chunk backedOffChunk = this.currentLoadableHolder.chunk;
        if (!isMediaChunk(backedOffChunk)) {
            doChunkOperation();
            discardUpstreamMediaChunks(this.currentLoadableHolder.queueSize);
            if (this.currentLoadableHolder.chunk == backedOffChunk) {
                this.loader.startLoading(backedOffChunk, this);
                return;
            }
            notifyLoadCanceled(backedOffChunk.bytesLoaded());
            maybeStartLoading();
        } else if (backedOffChunk == this.mediaChunks.getFirst()) {
            this.loader.startLoading(backedOffChunk, this);
        } else {
            BaseMediaChunk removedChunk = this.mediaChunks.removeLast();
            Assertions.checkState(backedOffChunk == removedChunk);
            doChunkOperation();
            this.mediaChunks.add(removedChunk);
            if (this.currentLoadableHolder.chunk == backedOffChunk) {
                this.loader.startLoading(backedOffChunk, this);
                return;
            }
            notifyLoadCanceled(backedOffChunk.bytesLoaded());
            discardUpstreamMediaChunks(this.currentLoadableHolder.queueSize);
            clearCurrentLoadableException();
            maybeStartLoading();
        }
    }

    private void maybeStartLoading() {
        Chunk currentLoadable = this.currentLoadableHolder.chunk;
        if (currentLoadable != null) {
            this.currentLoadStartTimeMs = SystemClock.elapsedRealtime();
            if (isMediaChunk(currentLoadable)) {
                BaseMediaChunk mediaChunk = (BaseMediaChunk) currentLoadable;
                mediaChunk.init(this.sampleQueue);
                this.mediaChunks.add(mediaChunk);
                if (isPendingReset()) {
                    this.pendingResetPositionUs = Long.MIN_VALUE;
                }
                notifyLoadStarted(mediaChunk.dataSpec.length, mediaChunk.type, mediaChunk.trigger, mediaChunk.format, mediaChunk.startTimeUs, mediaChunk.endTimeUs);
            } else {
                notifyLoadStarted(currentLoadable.dataSpec.length, currentLoadable.type, currentLoadable.trigger, currentLoadable.format, -1, -1);
            }
            this.loader.startLoading(currentLoadable, this);
        }
    }

    private void doChunkOperation() {
        this.currentLoadableHolder.endOfStream = false;
        this.currentLoadableHolder.queueSize = this.readOnlyMediaChunks.size();
        this.chunkSource.getChunkOperation(this.readOnlyMediaChunks, this.pendingResetPositionUs != Long.MIN_VALUE ? this.pendingResetPositionUs : this.downstreamPositionUs, this.currentLoadableHolder);
        this.loadingFinished = this.currentLoadableHolder.endOfStream;
    }

    private boolean discardUpstreamMediaChunks(int queueLength) {
        if (this.mediaChunks.size() <= queueLength) {
            return false;
        }
        long startTimeUs = 0;
        long endTimeUs = this.mediaChunks.getLast().endTimeUs;
        BaseMediaChunk removed = null;
        while (this.mediaChunks.size() > queueLength) {
            removed = this.mediaChunks.removeLast();
            startTimeUs = removed.startTimeUs;
            this.loadingFinished = false;
        }
        this.sampleQueue.discardUpstreamSamples(removed.getFirstSampleIndex());
        notifyUpstreamDiscarded(startTimeUs, endTimeUs);
        return true;
    }

    private boolean isMediaChunk(Chunk chunk) {
        return chunk instanceof BaseMediaChunk;
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != Long.MIN_VALUE;
    }

    private long getRetryDelayMillis(long errorCount) {
        return Math.min((errorCount - 1) * 1000, 5000);
    }

    /* access modifiers changed from: protected */
    public final long usToMs(long timeUs) {
        return timeUs / 1000;
    }

    private void notifyLoadStarted(long length, int type, int trigger, Format format, long mediaStartTimeUs, long mediaEndTimeUs) {
        if (this.eventHandler != null && this.eventListener != null) {
            final long j = length;
            final int i = type;
            final int i2 = trigger;
            final Format format2 = format;
            final long j2 = mediaStartTimeUs;
            final long j3 = mediaEndTimeUs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ChunkSampleSource.this.eventListener.onLoadStarted(ChunkSampleSource.this.eventSourceId, j, i, i2, format2, ChunkSampleSource.this.usToMs(j2), ChunkSampleSource.this.usToMs(j3));
                }
            });
        }
    }

    private void notifyLoadCompleted(long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeUs, long mediaEndTimeUs, long elapsedRealtimeMs, long loadDurationMs) {
        if (this.eventHandler != null && this.eventListener != null) {
            final long j = bytesLoaded;
            final int i = type;
            final int i2 = trigger;
            final Format format2 = format;
            final long j2 = mediaStartTimeUs;
            final long j3 = mediaEndTimeUs;
            final long j4 = elapsedRealtimeMs;
            final long j5 = loadDurationMs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ChunkSampleSource.this.eventListener.onLoadCompleted(ChunkSampleSource.this.eventSourceId, j, i, i2, format2, ChunkSampleSource.this.usToMs(j2), ChunkSampleSource.this.usToMs(j3), j4, j5);
                }
            });
        }
    }

    private void notifyLoadCanceled(final long bytesLoaded) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ChunkSampleSource.this.eventListener.onLoadCanceled(ChunkSampleSource.this.eventSourceId, bytesLoaded);
                }
            });
        }
    }

    private void notifyLoadError(final IOException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ChunkSampleSource.this.eventListener.onLoadError(ChunkSampleSource.this.eventSourceId, e);
                }
            });
        }
    }

    private void notifyUpstreamDiscarded(long mediaStartTimeUs, long mediaEndTimeUs) {
        if (this.eventHandler != null && this.eventListener != null) {
            final long j = mediaStartTimeUs;
            final long j2 = mediaEndTimeUs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ChunkSampleSource.this.eventListener.onUpstreamDiscarded(ChunkSampleSource.this.eventSourceId, ChunkSampleSource.this.usToMs(j), ChunkSampleSource.this.usToMs(j2));
                }
            });
        }
    }

    private void notifyDownstreamFormatChanged(Format format, int trigger, long positionUs) {
        if (this.eventHandler != null && this.eventListener != null) {
            final Format format2 = format;
            final int i = trigger;
            final long j = positionUs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ChunkSampleSource.this.eventListener.onDownstreamFormatChanged(ChunkSampleSource.this.eventSourceId, format2, i, ChunkSampleSource.this.usToMs(j));
                }
            });
        }
    }
}
