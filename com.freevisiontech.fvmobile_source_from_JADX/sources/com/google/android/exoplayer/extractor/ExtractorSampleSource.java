package com.google.android.exoplayer.extractor;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.util.SparseArray;
import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.MediaFormatHolder;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ExtractorSampleSource implements SampleSource, SampleSource.SampleSourceReader, ExtractorOutput, Loader.Callback {
    private static final List<Class<? extends Extractor>> DEFAULT_EXTRACTOR_CLASSES = new ArrayList();
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT_LIVE = 6;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT_ON_DEMAND = 3;
    private static final int MIN_RETRY_COUNT_DEFAULT_FOR_MEDIA = -1;
    private static final long NO_RESET_PENDING = Long.MIN_VALUE;
    private final Allocator allocator;
    private IOException currentLoadableException;
    private int currentLoadableExceptionCount;
    private long currentLoadableExceptionTimestamp;
    private final DataSource dataSource;
    private long downstreamPositionUs;
    private volatile DrmInitData drmInitData;
    private int enabledTrackCount;
    private final Handler eventHandler;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    /* access modifiers changed from: private */
    public final int eventSourceId;
    private int extractedSampleCount;
    private int extractedSampleCountAtStartOfLoad;
    /* access modifiers changed from: private */
    public final ExtractorHolder extractorHolder;
    private boolean havePendingNextSampleUs;
    private long lastSeekPositionUs;
    private ExtractingLoadable loadable;
    private Loader loader;
    private boolean loadingFinished;
    private long maxTrackDurationUs;
    private MediaFormat[] mediaFormats;
    private final int minLoadableRetryCount;
    private boolean[] pendingDiscontinuities;
    private boolean[] pendingMediaFormat;
    private long pendingNextSampleUs;
    private long pendingResetPositionUs;
    private boolean prepared;
    private int remainingReleaseCount;
    private final int requestedBufferSize;
    private final SparseArray<InternalTrackOutput> sampleQueues;
    private long sampleTimeOffsetUs;
    private volatile SeekMap seekMap;
    private boolean[] trackEnabledStates;
    private volatile boolean tracksBuilt;
    private final Uri uri;

    public interface EventListener {
        void onLoadError(int i, IOException iOException);
    }

    static /* synthetic */ int access$308(ExtractorSampleSource x0) {
        int i = x0.extractedSampleCount;
        x0.extractedSampleCount = i + 1;
        return i;
    }

    public static final class UnrecognizedInputFormatException extends ParserException {
        public UnrecognizedInputFormatException(Extractor[] extractors) {
            super("None of the available extractors (" + Util.getCommaDelimitedSimpleClassNames(extractors) + ") could read the stream.");
        }
    }

    static {
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.webm.WebmExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.mp4.FragmentedMp4Extractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e2) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.mp4.Mp4Extractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e3) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.mp3.Mp3Extractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e4) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.ts.AdtsExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e5) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.ts.TsExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e6) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.flv.FlvExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e7) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.ogg.OggExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e8) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.ts.PsExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e9) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.wav.WavExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e10) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.ext.flac.FlacExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e11) {
        }
    }

    public ExtractorSampleSource(Uri uri2, DataSource dataSource2, Allocator allocator2, int requestedBufferSize2, Extractor... extractors) {
        this(uri2, dataSource2, allocator2, requestedBufferSize2, -1, extractors);
    }

    public ExtractorSampleSource(Uri uri2, DataSource dataSource2, Allocator allocator2, int requestedBufferSize2, Handler eventHandler2, EventListener eventListener2, int eventSourceId2, Extractor... extractors) {
        this(uri2, dataSource2, allocator2, requestedBufferSize2, -1, eventHandler2, eventListener2, eventSourceId2, extractors);
    }

    public ExtractorSampleSource(Uri uri2, DataSource dataSource2, Allocator allocator2, int requestedBufferSize2, int minLoadableRetryCount2, Extractor... extractors) {
        this(uri2, dataSource2, allocator2, requestedBufferSize2, minLoadableRetryCount2, (Handler) null, (EventListener) null, 0, extractors);
    }

    public ExtractorSampleSource(Uri uri2, DataSource dataSource2, Allocator allocator2, int requestedBufferSize2, int minLoadableRetryCount2, Handler eventHandler2, EventListener eventListener2, int eventSourceId2, Extractor... extractors) {
        this.uri = uri2;
        this.dataSource = dataSource2;
        this.eventListener = eventListener2;
        this.eventHandler = eventHandler2;
        this.eventSourceId = eventSourceId2;
        this.allocator = allocator2;
        this.requestedBufferSize = requestedBufferSize2;
        this.minLoadableRetryCount = minLoadableRetryCount2;
        if (extractors == null || extractors.length == 0) {
            extractors = new Extractor[DEFAULT_EXTRACTOR_CLASSES.size()];
            int i = 0;
            while (i < extractors.length) {
                try {
                    extractors[i] = (Extractor) DEFAULT_EXTRACTOR_CLASSES.get(i).newInstance();
                    i++;
                } catch (InstantiationException e) {
                    throw new IllegalStateException("Unexpected error creating default extractor", e);
                } catch (IllegalAccessException e2) {
                    throw new IllegalStateException("Unexpected error creating default extractor", e2);
                }
            }
        }
        this.extractorHolder = new ExtractorHolder(extractors, this);
        this.sampleQueues = new SparseArray<>();
        this.pendingResetPositionUs = Long.MIN_VALUE;
    }

    public SampleSource.SampleSourceReader register() {
        this.remainingReleaseCount++;
        return this;
    }

    public boolean prepare(long positionUs) {
        if (this.prepared) {
            return true;
        }
        if (this.loader == null) {
            this.loader = new Loader("Loader:ExtractorSampleSource");
        }
        maybeStartLoading();
        if (this.seekMap == null || !this.tracksBuilt || !haveFormatsForAllTracks()) {
            return false;
        }
        int trackCount = this.sampleQueues.size();
        this.trackEnabledStates = new boolean[trackCount];
        this.pendingDiscontinuities = new boolean[trackCount];
        this.pendingMediaFormat = new boolean[trackCount];
        this.mediaFormats = new MediaFormat[trackCount];
        this.maxTrackDurationUs = -1;
        for (int i = 0; i < trackCount; i++) {
            MediaFormat format = this.sampleQueues.valueAt(i).getFormat();
            this.mediaFormats[i] = format;
            if (format.durationUs != -1 && format.durationUs > this.maxTrackDurationUs) {
                this.maxTrackDurationUs = format.durationUs;
            }
        }
        this.prepared = true;
        return true;
    }

    public int getTrackCount() {
        return this.sampleQueues.size();
    }

    public MediaFormat getFormat(int track) {
        Assertions.checkState(this.prepared);
        return this.mediaFormats[track];
    }

    public void enable(int track, long positionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(!this.trackEnabledStates[track]);
        this.enabledTrackCount++;
        this.trackEnabledStates[track] = true;
        this.pendingMediaFormat[track] = true;
        this.pendingDiscontinuities[track] = false;
        if (this.enabledTrackCount == 1) {
            if (!this.seekMap.isSeekable()) {
                positionUs = 0;
            }
            this.downstreamPositionUs = positionUs;
            this.lastSeekPositionUs = positionUs;
            restartFrom(positionUs);
        }
    }

    public void disable(int track) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[track]);
        this.enabledTrackCount--;
        this.trackEnabledStates[track] = false;
        if (this.enabledTrackCount == 0) {
            this.downstreamPositionUs = Long.MIN_VALUE;
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
                return;
            }
            clearState();
            this.allocator.trim(0);
        }
    }

    public boolean continueBuffering(int track, long playbackPositionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[track]);
        this.downstreamPositionUs = playbackPositionUs;
        discardSamplesForDisabledTracks(this.downstreamPositionUs);
        if (this.loadingFinished) {
            return true;
        }
        maybeStartLoading();
        if (isPendingReset()) {
            return false;
        }
        return !this.sampleQueues.valueAt(track).isEmpty();
    }

    public long readDiscontinuity(int track) {
        if (!this.pendingDiscontinuities[track]) {
            return Long.MIN_VALUE;
        }
        this.pendingDiscontinuities[track] = false;
        return this.lastSeekPositionUs;
    }

    public int readData(int track, long playbackPositionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder) {
        boolean decodeOnly;
        int i;
        this.downstreamPositionUs = playbackPositionUs;
        if (this.pendingDiscontinuities[track] || isPendingReset()) {
            return -2;
        }
        InternalTrackOutput sampleQueue = this.sampleQueues.valueAt(track);
        if (this.pendingMediaFormat[track]) {
            formatHolder.format = sampleQueue.getFormat();
            formatHolder.drmInitData = this.drmInitData;
            this.pendingMediaFormat[track] = false;
            return -4;
        } else if (!sampleQueue.getSample(sampleHolder)) {
            return this.loadingFinished ? -1 : -2;
        } else {
            if (sampleHolder.timeUs < this.lastSeekPositionUs) {
                decodeOnly = true;
            } else {
                decodeOnly = false;
            }
            int i2 = sampleHolder.flags;
            if (decodeOnly) {
                i = C1907C.SAMPLE_FLAG_DECODE_ONLY;
            } else {
                i = 0;
            }
            sampleHolder.flags = i | i2;
            if (this.havePendingNextSampleUs) {
                this.sampleTimeOffsetUs = this.pendingNextSampleUs - sampleHolder.timeUs;
                this.havePendingNextSampleUs = false;
            }
            sampleHolder.timeUs += this.sampleTimeOffsetUs;
            return -3;
        }
    }

    public void maybeThrowError() throws IOException {
        int minLoadableRetryCountForMedia;
        if (this.currentLoadableException != null) {
            if (isCurrentLoadableExceptionFatal()) {
                throw this.currentLoadableException;
            }
            if (this.minLoadableRetryCount != -1) {
                minLoadableRetryCountForMedia = this.minLoadableRetryCount;
            } else {
                minLoadableRetryCountForMedia = (this.seekMap == null || this.seekMap.isSeekable()) ? 3 : 6;
            }
            if (this.currentLoadableExceptionCount > minLoadableRetryCountForMedia) {
                throw this.currentLoadableException;
            }
        }
    }

    public void seekToUs(long positionUs) {
        boolean z;
        boolean seekInsideBuffer;
        Assertions.checkState(this.prepared);
        if (this.enabledTrackCount > 0) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!this.seekMap.isSeekable()) {
            positionUs = 0;
        }
        long currentPositionUs = isPendingReset() ? this.pendingResetPositionUs : this.downstreamPositionUs;
        this.downstreamPositionUs = positionUs;
        this.lastSeekPositionUs = positionUs;
        if (currentPositionUs != positionUs) {
            if (!isPendingReset()) {
                seekInsideBuffer = true;
            } else {
                seekInsideBuffer = false;
            }
            int i = 0;
            while (seekInsideBuffer && i < this.sampleQueues.size()) {
                seekInsideBuffer &= this.sampleQueues.valueAt(i).skipToKeyframeBefore(positionUs);
                i++;
            }
            if (!seekInsideBuffer) {
                restartFrom(positionUs);
            }
            for (int i2 = 0; i2 < this.pendingDiscontinuities.length; i2++) {
                this.pendingDiscontinuities[i2] = true;
            }
        }
    }

    public long getBufferedPositionUs() {
        if (this.loadingFinished) {
            return -3;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long largestParsedTimestampUs = Long.MIN_VALUE;
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            largestParsedTimestampUs = Math.max(largestParsedTimestampUs, this.sampleQueues.valueAt(i).getLargestParsedTimestampUs());
        }
        return largestParsedTimestampUs == Long.MIN_VALUE ? this.downstreamPositionUs : largestParsedTimestampUs;
    }

    public void release() {
        Assertions.checkState(this.remainingReleaseCount > 0);
        int i = this.remainingReleaseCount - 1;
        this.remainingReleaseCount = i;
        if (i == 0 && this.loader != null) {
            this.loader.release(new Runnable() {
                public void run() {
                    ExtractorSampleSource.this.extractorHolder.release();
                }
            });
            this.loader = null;
        }
    }

    public void onLoadCompleted(Loader.Loadable loadable2) {
        this.loadingFinished = true;
    }

    public void onLoadCanceled(Loader.Loadable loadable2) {
        if (this.enabledTrackCount > 0) {
            restartFrom(this.pendingResetPositionUs);
            return;
        }
        clearState();
        this.allocator.trim(0);
    }

    public void onLoadError(Loader.Loadable ignored, IOException e) {
        this.currentLoadableException = e;
        this.currentLoadableExceptionCount = this.extractedSampleCount > this.extractedSampleCountAtStartOfLoad ? 1 : this.currentLoadableExceptionCount + 1;
        this.currentLoadableExceptionTimestamp = SystemClock.elapsedRealtime();
        notifyLoadError(e);
        maybeStartLoading();
    }

    public TrackOutput track(int id) {
        InternalTrackOutput sampleQueue = this.sampleQueues.get(id);
        if (sampleQueue != null) {
            return sampleQueue;
        }
        InternalTrackOutput sampleQueue2 = new InternalTrackOutput(this.allocator);
        this.sampleQueues.put(id, sampleQueue2);
        return sampleQueue2;
    }

    public void endTracks() {
        this.tracksBuilt = true;
    }

    public void seekMap(SeekMap seekMap2) {
        this.seekMap = seekMap2;
    }

    public void drmInitData(DrmInitData drmInitData2) {
        this.drmInitData = drmInitData2;
    }

    private void restartFrom(long positionUs) {
        this.pendingResetPositionUs = positionUs;
        this.loadingFinished = false;
        if (this.loader.isLoading()) {
            this.loader.cancelLoading();
            return;
        }
        clearState();
        maybeStartLoading();
    }

    private void maybeStartLoading() {
        boolean z = false;
        if (!this.loadingFinished && !this.loader.isLoading()) {
            if (this.currentLoadableException == null) {
                this.sampleTimeOffsetUs = 0;
                this.havePendingNextSampleUs = false;
                if (!this.prepared) {
                    this.loadable = createLoadableFromStart();
                } else {
                    Assertions.checkState(isPendingReset());
                    if (this.maxTrackDurationUs == -1 || this.pendingResetPositionUs < this.maxTrackDurationUs) {
                        this.loadable = createLoadableFromPositionUs(this.pendingResetPositionUs);
                        this.pendingResetPositionUs = Long.MIN_VALUE;
                    } else {
                        this.loadingFinished = true;
                        this.pendingResetPositionUs = Long.MIN_VALUE;
                        return;
                    }
                }
                this.extractedSampleCountAtStartOfLoad = this.extractedSampleCount;
                this.loader.startLoading(this.loadable, this);
            } else if (!isCurrentLoadableExceptionFatal()) {
                if (this.loadable != null) {
                    z = true;
                }
                Assertions.checkState(z);
                if (SystemClock.elapsedRealtime() - this.currentLoadableExceptionTimestamp >= getRetryDelayMillis((long) this.currentLoadableExceptionCount)) {
                    this.currentLoadableException = null;
                    if (!this.prepared) {
                        for (int i = 0; i < this.sampleQueues.size(); i++) {
                            this.sampleQueues.valueAt(i).clear();
                        }
                        this.loadable = createLoadableFromStart();
                    } else if (!this.seekMap.isSeekable() && this.maxTrackDurationUs == -1) {
                        for (int i2 = 0; i2 < this.sampleQueues.size(); i2++) {
                            this.sampleQueues.valueAt(i2).clear();
                        }
                        this.loadable = createLoadableFromStart();
                        this.pendingNextSampleUs = this.downstreamPositionUs;
                        this.havePendingNextSampleUs = true;
                    }
                    this.extractedSampleCountAtStartOfLoad = this.extractedSampleCount;
                    this.loader.startLoading(this.loadable, this);
                }
            }
        }
    }

    private ExtractingLoadable createLoadableFromStart() {
        return new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this.allocator, this.requestedBufferSize, 0);
    }

    private ExtractingLoadable createLoadableFromPositionUs(long positionUs) {
        return new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this.allocator, this.requestedBufferSize, this.seekMap.getPosition(positionUs));
    }

    private boolean haveFormatsForAllTracks() {
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            if (!this.sampleQueues.valueAt(i).hasFormat()) {
                return false;
            }
        }
        return true;
    }

    private void discardSamplesForDisabledTracks(long timeUs) {
        for (int i = 0; i < this.trackEnabledStates.length; i++) {
            if (!this.trackEnabledStates[i]) {
                this.sampleQueues.valueAt(i).discardUntil(timeUs);
            }
        }
    }

    private void clearState() {
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            this.sampleQueues.valueAt(i).clear();
        }
        this.loadable = null;
        this.currentLoadableException = null;
        this.currentLoadableExceptionCount = 0;
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != Long.MIN_VALUE;
    }

    private boolean isCurrentLoadableExceptionFatal() {
        return this.currentLoadableException instanceof UnrecognizedInputFormatException;
    }

    private long getRetryDelayMillis(long errorCount) {
        return Math.min((errorCount - 1) * 1000, 5000);
    }

    private void notifyLoadError(final IOException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    ExtractorSampleSource.this.eventListener.onLoadError(ExtractorSampleSource.this.eventSourceId, e);
                }
            });
        }
    }

    private class InternalTrackOutput extends DefaultTrackOutput {
        public InternalTrackOutput(Allocator allocator) {
            super(allocator);
        }

        public void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
            super.sampleMetadata(timeUs, flags, size, offset, encryptionKey);
            ExtractorSampleSource.access$308(ExtractorSampleSource.this);
        }
    }

    private static class ExtractingLoadable implements Loader.Loadable {
        private final Allocator allocator;
        private final DataSource dataSource;
        private final ExtractorHolder extractorHolder;
        private volatile boolean loadCanceled;
        private boolean pendingExtractorSeek;
        private final PositionHolder positionHolder = new PositionHolder();
        private final int requestedBufferSize;
        private final Uri uri;

        public ExtractingLoadable(Uri uri2, DataSource dataSource2, ExtractorHolder extractorHolder2, Allocator allocator2, int requestedBufferSize2, long position) {
            this.uri = (Uri) Assertions.checkNotNull(uri2);
            this.dataSource = (DataSource) Assertions.checkNotNull(dataSource2);
            this.extractorHolder = (ExtractorHolder) Assertions.checkNotNull(extractorHolder2);
            this.allocator = (Allocator) Assertions.checkNotNull(allocator2);
            this.requestedBufferSize = requestedBufferSize2;
            this.positionHolder.position = position;
            this.pendingExtractorSeek = true;
        }

        public void cancelLoad() {
            this.loadCanceled = true;
        }

        public boolean isLoadCanceled() {
            return this.loadCanceled;
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x0068  */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x006f  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void load() throws java.io.IOException, java.lang.InterruptedException {
            /*
                r12 = this;
                r9 = 0
            L_0x0001:
                if (r9 != 0) goto L_0x007a
                boolean r1 = r12.loadCanceled
                if (r1 != 0) goto L_0x007a
                r8 = 0
                com.google.android.exoplayer.extractor.PositionHolder r1 = r12.positionHolder     // Catch:{ all -> 0x0063 }
                long r2 = r1.position     // Catch:{ all -> 0x0063 }
                com.google.android.exoplayer.upstream.DataSource r10 = r12.dataSource     // Catch:{ all -> 0x0063 }
                com.google.android.exoplayer.upstream.DataSpec r0 = new com.google.android.exoplayer.upstream.DataSpec     // Catch:{ all -> 0x0063 }
                android.net.Uri r1 = r12.uri     // Catch:{ all -> 0x0063 }
                r4 = -1
                r6 = 0
                r0.<init>(r1, r2, r4, r6)     // Catch:{ all -> 0x0063 }
                long r4 = r10.open(r0)     // Catch:{ all -> 0x0063 }
                r10 = -1
                int r1 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
                if (r1 == 0) goto L_0x0023
                long r4 = r4 + r2
            L_0x0023:
                com.google.android.exoplayer.extractor.DefaultExtractorInput r0 = new com.google.android.exoplayer.extractor.DefaultExtractorInput     // Catch:{ all -> 0x0063 }
                com.google.android.exoplayer.upstream.DataSource r1 = r12.dataSource     // Catch:{ all -> 0x0063 }
                r0.<init>(r1, r2, r4)     // Catch:{ all -> 0x0063 }
                com.google.android.exoplayer.extractor.ExtractorSampleSource$ExtractorHolder r1 = r12.extractorHolder     // Catch:{ all -> 0x007b }
                com.google.android.exoplayer.extractor.Extractor r7 = r1.selectExtractor(r0)     // Catch:{ all -> 0x007b }
                boolean r1 = r12.pendingExtractorSeek     // Catch:{ all -> 0x007b }
                if (r1 == 0) goto L_0x003a
                r7.seek()     // Catch:{ all -> 0x007b }
                r1 = 0
                r12.pendingExtractorSeek = r1     // Catch:{ all -> 0x007b }
            L_0x003a:
                if (r9 != 0) goto L_0x004e
                boolean r1 = r12.loadCanceled     // Catch:{ all -> 0x007b }
                if (r1 != 0) goto L_0x004e
                com.google.android.exoplayer.upstream.Allocator r1 = r12.allocator     // Catch:{ all -> 0x007b }
                int r6 = r12.requestedBufferSize     // Catch:{ all -> 0x007b }
                r1.blockWhileTotalBytesAllocatedExceeds(r6)     // Catch:{ all -> 0x007b }
                com.google.android.exoplayer.extractor.PositionHolder r1 = r12.positionHolder     // Catch:{ all -> 0x007b }
                int r9 = r7.read(r0, r1)     // Catch:{ all -> 0x007b }
                goto L_0x003a
            L_0x004e:
                r1 = 1
                if (r9 != r1) goto L_0x0058
                r9 = 0
            L_0x0052:
                com.google.android.exoplayer.upstream.DataSource r1 = r12.dataSource
                r1.close()
                goto L_0x0001
            L_0x0058:
                if (r0 == 0) goto L_0x0052
                com.google.android.exoplayer.extractor.PositionHolder r1 = r12.positionHolder
                long r10 = r0.getPosition()
                r1.position = r10
                goto L_0x0052
            L_0x0063:
                r1 = move-exception
                r0 = r8
            L_0x0065:
                r6 = 1
                if (r9 != r6) goto L_0x006f
                r9 = 0
            L_0x0069:
                com.google.android.exoplayer.upstream.DataSource r6 = r12.dataSource
                r6.close()
                throw r1
            L_0x006f:
                if (r0 == 0) goto L_0x0069
                com.google.android.exoplayer.extractor.PositionHolder r6 = r12.positionHolder
                long r10 = r0.getPosition()
                r6.position = r10
                goto L_0x0069
            L_0x007a:
                return
            L_0x007b:
                r1 = move-exception
                goto L_0x0065
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.extractor.ExtractorSampleSource.ExtractingLoadable.load():void");
        }
    }

    private static final class ExtractorHolder {
        private Extractor extractor;
        private final ExtractorOutput extractorOutput;
        private final Extractor[] extractors;

        public ExtractorHolder(Extractor[] extractors2, ExtractorOutput extractorOutput2) {
            this.extractors = extractors2;
            this.extractorOutput = extractorOutput2;
        }

        public Extractor selectExtractor(ExtractorInput input) throws UnrecognizedInputFormatException, IOException, InterruptedException {
            if (this.extractor != null) {
                return this.extractor;
            }
            Extractor[] extractorArr = this.extractors;
            int length = extractorArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Extractor extractor2 = extractorArr[i];
                try {
                    if (extractor2.sniff(input)) {
                        this.extractor = extractor2;
                        input.resetPeekPosition();
                        break;
                    }
                    i++;
                } catch (EOFException e) {
                } finally {
                    input.resetPeekPosition();
                }
            }
            if (this.extractor == null) {
                throw new UnrecognizedInputFormatException(this.extractors);
            }
            this.extractor.init(this.extractorOutput);
            return this.extractor;
        }

        public void release() {
            if (this.extractor != null) {
                this.extractor.release();
                this.extractor = null;
            }
        }
    }
}
