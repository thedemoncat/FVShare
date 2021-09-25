package com.google.android.exoplayer.dash;

import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.exoplayer.BehindLiveWindowException;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.chunk.Chunk;
import com.google.android.exoplayer.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer.chunk.ChunkOperationHolder;
import com.google.android.exoplayer.chunk.ChunkSource;
import com.google.android.exoplayer.chunk.ContainerMediaChunk;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.chunk.InitializationChunk;
import com.google.android.exoplayer.chunk.MediaChunk;
import com.google.android.exoplayer.chunk.SingleSampleMediaChunk;
import com.google.android.exoplayer.dash.DashTrackSelector;
import com.google.android.exoplayer.dash.mpd.AdaptationSet;
import com.google.android.exoplayer.dash.mpd.ContentProtection;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.Period;
import com.google.android.exoplayer.dash.mpd.RangedUri;
import com.google.android.exoplayer.dash.mpd.Representation;
import com.google.android.exoplayer.dash.mpd.UtcTimingElement;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.ChunkIndex;
import com.google.android.exoplayer.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer.extractor.webm.WebmExtractor;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.Clock;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.SystemClock;
import com.mp4parser.iso14496.part30.WebVTTSampleEntry;
import com.mp4parser.iso14496.part30.XMLSubtitleSampleEntry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DashChunkSource implements ChunkSource, DashTrackSelector.Output {
    private static final String TAG = "DashChunkSource";
    private final FormatEvaluator adaptiveFormatEvaluator;
    private TimeRange availableRange;
    private final long[] availableRangeValues;
    private MediaPresentationDescription currentManifest;
    private final DataSource dataSource;
    private final long elapsedRealtimeOffsetUs;
    private ExposedTrack enabledTrack;
    private final FormatEvaluator.Evaluation evaluation;
    private final Handler eventHandler;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    /* access modifiers changed from: private */
    public final int eventSourceId;
    private IOException fatalError;
    private boolean lastChunkWasInitialization;
    private final boolean live;
    private final long liveEdgeLatencyUs;
    private final ManifestFetcher<MediaPresentationDescription> manifestFetcher;
    private int nextPeriodHolderIndex;
    private final SparseArray<PeriodHolder> periodHolders;
    private boolean prepareCalled;
    private MediaPresentationDescription processedManifest;
    private boolean startAtLiveEdge;
    private final Clock systemClock;
    private final DashTrackSelector trackSelector;
    private final ArrayList<ExposedTrack> tracks;

    public interface EventListener {
        void onAvailableRangeChanged(int i, TimeRange timeRange);
    }

    public static class NoAdaptationSetException extends IOException {
        public NoAdaptationSetException(String message) {
            super(message);
        }
    }

    public DashChunkSource(DashTrackSelector trackSelector2, DataSource dataSource2, FormatEvaluator adaptiveFormatEvaluator2, long durationMs, int adaptationSetType, Representation... representations) {
        this(trackSelector2, dataSource2, adaptiveFormatEvaluator2, durationMs, adaptationSetType, (List<Representation>) Arrays.asList(representations));
    }

    public DashChunkSource(DashTrackSelector trackSelector2, DataSource dataSource2, FormatEvaluator adaptiveFormatEvaluator2, long durationMs, int adaptationSetType, List<Representation> representations) {
        this(buildManifest(durationMs, adaptationSetType, representations), trackSelector2, dataSource2, adaptiveFormatEvaluator2);
    }

    public DashChunkSource(MediaPresentationDescription manifest, DashTrackSelector trackSelector2, DataSource dataSource2, FormatEvaluator adaptiveFormatEvaluator2) {
        this((ManifestFetcher<MediaPresentationDescription>) null, manifest, trackSelector2, dataSource2, adaptiveFormatEvaluator2, new SystemClock(), 0, 0, false, (Handler) null, (EventListener) null, 0);
    }

    public DashChunkSource(ManifestFetcher<MediaPresentationDescription> manifestFetcher2, DashTrackSelector trackSelector2, DataSource dataSource2, FormatEvaluator adaptiveFormatEvaluator2, long liveEdgeLatencyMs, long elapsedRealtimeOffsetMs, Handler eventHandler2, EventListener eventListener2, int eventSourceId2) {
        this(manifestFetcher2, manifestFetcher2.getManifest(), trackSelector2, dataSource2, adaptiveFormatEvaluator2, new SystemClock(), liveEdgeLatencyMs * 1000, elapsedRealtimeOffsetMs * 1000, true, eventHandler2, eventListener2, eventSourceId2);
    }

    public DashChunkSource(ManifestFetcher<MediaPresentationDescription> manifestFetcher2, DashTrackSelector trackSelector2, DataSource dataSource2, FormatEvaluator adaptiveFormatEvaluator2, long liveEdgeLatencyMs, long elapsedRealtimeOffsetMs, boolean startAtLiveEdge2, Handler eventHandler2, EventListener eventListener2, int eventSourceId2) {
        this(manifestFetcher2, manifestFetcher2.getManifest(), trackSelector2, dataSource2, adaptiveFormatEvaluator2, new SystemClock(), liveEdgeLatencyMs * 1000, elapsedRealtimeOffsetMs * 1000, startAtLiveEdge2, eventHandler2, eventListener2, eventSourceId2);
    }

    DashChunkSource(ManifestFetcher<MediaPresentationDescription> manifestFetcher2, MediaPresentationDescription initialManifest, DashTrackSelector trackSelector2, DataSource dataSource2, FormatEvaluator adaptiveFormatEvaluator2, Clock systemClock2, long liveEdgeLatencyUs2, long elapsedRealtimeOffsetUs2, boolean startAtLiveEdge2, Handler eventHandler2, EventListener eventListener2, int eventSourceId2) {
        this.manifestFetcher = manifestFetcher2;
        this.currentManifest = initialManifest;
        this.trackSelector = trackSelector2;
        this.dataSource = dataSource2;
        this.adaptiveFormatEvaluator = adaptiveFormatEvaluator2;
        this.systemClock = systemClock2;
        this.liveEdgeLatencyUs = liveEdgeLatencyUs2;
        this.elapsedRealtimeOffsetUs = elapsedRealtimeOffsetUs2;
        this.startAtLiveEdge = startAtLiveEdge2;
        this.eventHandler = eventHandler2;
        this.eventListener = eventListener2;
        this.eventSourceId = eventSourceId2;
        this.evaluation = new FormatEvaluator.Evaluation();
        this.availableRangeValues = new long[2];
        this.periodHolders = new SparseArray<>();
        this.tracks = new ArrayList<>();
        this.live = initialManifest.dynamic;
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        } else if (this.manifestFetcher != null) {
            this.manifestFetcher.maybeThrowError();
        }
    }

    public boolean prepare() {
        if (!this.prepareCalled) {
            this.prepareCalled = true;
            try {
                this.trackSelector.selectTracks(this.currentManifest, 0, this);
            } catch (IOException e) {
                this.fatalError = e;
            }
        }
        if (this.fatalError == null) {
            return true;
        }
        return false;
    }

    public int getTrackCount() {
        return this.tracks.size();
    }

    public final MediaFormat getFormat(int track) {
        return this.tracks.get(track).trackFormat;
    }

    public void enable(int track) {
        this.enabledTrack = this.tracks.get(track);
        if (this.enabledTrack.isAdaptive()) {
            this.adaptiveFormatEvaluator.enable();
        }
        if (this.manifestFetcher != null) {
            this.manifestFetcher.enable();
            processManifest(this.manifestFetcher.getManifest());
            return;
        }
        processManifest(this.currentManifest);
    }

    public void continueBuffering(long playbackPositionUs) {
        if (this.manifestFetcher != null && this.currentManifest.dynamic && this.fatalError == null) {
            MediaPresentationDescription newManifest = this.manifestFetcher.getManifest();
            if (!(newManifest == null || newManifest == this.processedManifest)) {
                processManifest(newManifest);
                this.processedManifest = newManifest;
            }
            long minUpdatePeriod = this.currentManifest.minUpdatePeriod;
            if (minUpdatePeriod == 0) {
                minUpdatePeriod = 5000;
            }
            if (android.os.SystemClock.elapsedRealtime() > this.manifestFetcher.getManifestLoadStartTimestamp() + minUpdatePeriod) {
                this.manifestFetcher.requestRefresh();
            }
        }
    }

    public final void getChunkOperation(List<? extends MediaChunk> queue, long playbackPositionUs, ChunkOperationHolder out) {
        boolean startingNewPeriod;
        PeriodHolder periodHolder;
        int segmentNum;
        if (this.fatalError != null) {
            out.chunk = null;
            return;
        }
        this.evaluation.queueSize = queue.size();
        if (this.evaluation.format == null || !this.lastChunkWasInitialization) {
            if (this.enabledTrack.isAdaptive()) {
                this.adaptiveFormatEvaluator.evaluate(queue, playbackPositionUs, this.enabledTrack.adaptiveFormats, this.evaluation);
            } else {
                this.evaluation.format = this.enabledTrack.fixedFormat;
                this.evaluation.trigger = 2;
            }
        }
        Format selectedFormat = this.evaluation.format;
        out.queueSize = this.evaluation.queueSize;
        if (selectedFormat == null) {
            out.chunk = null;
        } else if (out.queueSize != queue.size() || out.chunk == null || !out.chunk.format.equals(selectedFormat)) {
            out.chunk = null;
            this.availableRange.getCurrentBoundsUs(this.availableRangeValues);
            if (queue.isEmpty()) {
                if (this.live) {
                    if (playbackPositionUs != 0) {
                        this.startAtLiveEdge = false;
                    }
                    if (this.startAtLiveEdge) {
                        playbackPositionUs = Math.max(this.availableRangeValues[0], this.availableRangeValues[1] - this.liveEdgeLatencyUs);
                    } else {
                        playbackPositionUs = Math.max(Math.min(playbackPositionUs, this.availableRangeValues[1] - 1), this.availableRangeValues[0]);
                    }
                }
                periodHolder = findPeriodHolder(playbackPositionUs);
                startingNewPeriod = true;
            } else {
                if (this.startAtLiveEdge) {
                    this.startAtLiveEdge = false;
                }
                MediaChunk previous = (MediaChunk) queue.get(out.queueSize - 1);
                long nextSegmentStartTimeUs = previous.endTimeUs;
                if (this.live && nextSegmentStartTimeUs < this.availableRangeValues[0]) {
                    this.fatalError = new BehindLiveWindowException();
                    return;
                } else if (!this.currentManifest.dynamic || nextSegmentStartTimeUs < this.availableRangeValues[1]) {
                    PeriodHolder lastPeriodHolder = this.periodHolders.valueAt(this.periodHolders.size() - 1);
                    if (previous.parentId != lastPeriodHolder.localIndex || !lastPeriodHolder.representationHolders.get(previous.format.f1192id).isBeyondLastSegment(previous.getNextChunkIndex())) {
                        startingNewPeriod = false;
                        periodHolder = this.periodHolders.get(previous.parentId);
                        if (periodHolder == null) {
                            periodHolder = this.periodHolders.valueAt(0);
                            startingNewPeriod = true;
                        } else if (!periodHolder.isIndexUnbounded() && periodHolder.representationHolders.get(previous.format.f1192id).isBeyondLastSegment(previous.getNextChunkIndex())) {
                            periodHolder = this.periodHolders.get(previous.parentId + 1);
                            startingNewPeriod = true;
                        }
                    } else if (!this.currentManifest.dynamic) {
                        out.endOfStream = true;
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            RepresentationHolder representationHolder = periodHolder.representationHolders.get(selectedFormat.f1192id);
            Representation selectedRepresentation = representationHolder.representation;
            RangedUri pendingInitializationUri = null;
            RangedUri pendingIndexUri = null;
            MediaFormat mediaFormat = representationHolder.mediaFormat;
            if (mediaFormat == null) {
                pendingInitializationUri = selectedRepresentation.getInitializationUri();
            }
            if (representationHolder.segmentIndex == null) {
                pendingIndexUri = selectedRepresentation.getIndexUri();
            }
            if (pendingInitializationUri == null && pendingIndexUri == null) {
                if (queue.isEmpty()) {
                    segmentNum = representationHolder.getSegmentNum(playbackPositionUs);
                } else if (startingNewPeriod) {
                    segmentNum = representationHolder.getFirstAvailableSegmentNum();
                } else {
                    segmentNum = ((MediaChunk) queue.get(out.queueSize - 1)).getNextChunkIndex();
                }
                Chunk nextMediaChunk = newMediaChunk(periodHolder, representationHolder, this.dataSource, mediaFormat, this.enabledTrack, segmentNum, this.evaluation.trigger, mediaFormat != null);
                this.lastChunkWasInitialization = false;
                out.chunk = nextMediaChunk;
                return;
            }
            Chunk initializationChunk = newInitializationChunk(pendingInitializationUri, pendingIndexUri, selectedRepresentation, representationHolder.extractorWrapper, this.dataSource, periodHolder.localIndex, this.evaluation.trigger);
            this.lastChunkWasInitialization = true;
            out.chunk = initializationChunk;
        }
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof InitializationChunk) {
            InitializationChunk initializationChunk = (InitializationChunk) chunk;
            String formatId = initializationChunk.format.f1192id;
            PeriodHolder periodHolder = this.periodHolders.get(initializationChunk.parentId);
            if (periodHolder != null) {
                RepresentationHolder representationHolder = periodHolder.representationHolders.get(formatId);
                if (initializationChunk.hasFormat()) {
                    representationHolder.mediaFormat = initializationChunk.getFormat();
                }
                if (representationHolder.segmentIndex == null && initializationChunk.hasSeekMap()) {
                    representationHolder.segmentIndex = new DashWrappingSegmentIndex((ChunkIndex) initializationChunk.getSeekMap(), initializationChunk.dataSpec.uri.toString());
                }
                if (periodHolder.drmInitData == null && initializationChunk.hasDrmInitData()) {
                    DrmInitData unused = periodHolder.drmInitData = initializationChunk.getDrmInitData();
                }
            }
        }
    }

    public void onChunkLoadError(Chunk chunk, Exception e) {
    }

    public void disable(List<? extends MediaChunk> list) {
        if (this.enabledTrack.isAdaptive()) {
            this.adaptiveFormatEvaluator.disable();
        }
        if (this.manifestFetcher != null) {
            this.manifestFetcher.disable();
        }
        this.periodHolders.clear();
        this.evaluation.format = null;
        this.availableRange = null;
        this.fatalError = null;
        this.enabledTrack = null;
    }

    public void adaptiveTrack(MediaPresentationDescription manifest, int periodIndex, int adaptationSetIndex, int[] representationIndices) {
        if (this.adaptiveFormatEvaluator == null) {
            Log.w(TAG, "Skipping adaptive track (missing format evaluator)");
            return;
        }
        AdaptationSet adaptationSet = manifest.getPeriod(periodIndex).adaptationSets.get(adaptationSetIndex);
        int maxWidth = 0;
        int maxHeight = 0;
        Format maxHeightRepresentationFormat = null;
        Format[] representationFormats = new Format[representationIndices.length];
        for (int i = 0; i < representationFormats.length; i++) {
            Format format = adaptationSet.representations.get(representationIndices[i]).format;
            if (maxHeightRepresentationFormat == null || format.height > maxHeight) {
                maxHeightRepresentationFormat = format;
            }
            maxWidth = Math.max(maxWidth, format.width);
            maxHeight = Math.max(maxHeight, format.height);
            representationFormats[i] = format;
        }
        Arrays.sort(representationFormats, new Format.DecreasingBandwidthComparator());
        long trackDurationUs = this.live ? -1 : manifest.duration * 1000;
        String mediaMimeType = getMediaMimeType(maxHeightRepresentationFormat);
        if (mediaMimeType == null) {
            Log.w(TAG, "Skipped adaptive track (unknown media mime type)");
            return;
        }
        MediaFormat trackFormat = getTrackFormat(adaptationSet.type, maxHeightRepresentationFormat, mediaMimeType, trackDurationUs);
        if (trackFormat == null) {
            Log.w(TAG, "Skipped adaptive track (unknown media format)");
        } else {
            this.tracks.add(new ExposedTrack(trackFormat.copyAsAdaptive((String) null), adaptationSetIndex, representationFormats, maxWidth, maxHeight));
        }
    }

    public void fixedTrack(MediaPresentationDescription manifest, int periodIndex, int adaptationSetIndex, int representationIndex) {
        AdaptationSet adaptationSet = manifest.getPeriod(periodIndex).adaptationSets.get(adaptationSetIndex);
        Format representationFormat = adaptationSet.representations.get(representationIndex).format;
        String mediaMimeType = getMediaMimeType(representationFormat);
        if (mediaMimeType == null) {
            Log.w(TAG, "Skipped track " + representationFormat.f1192id + " (unknown media mime type)");
            return;
        }
        MediaFormat trackFormat = getTrackFormat(adaptationSet.type, representationFormat, mediaMimeType, manifest.dynamic ? -1 : manifest.duration * 1000);
        if (trackFormat == null) {
            Log.w(TAG, "Skipped track " + representationFormat.f1192id + " (unknown media format)");
        } else {
            this.tracks.add(new ExposedTrack(trackFormat, adaptationSetIndex, representationFormat));
        }
    }

    /* access modifiers changed from: package-private */
    public TimeRange getAvailableRange() {
        return this.availableRange;
    }

    private static MediaPresentationDescription buildManifest(long durationMs, int adaptationSetType, List<Representation> representations) {
        return new MediaPresentationDescription(-1, durationMs, -1, false, -1, -1, (UtcTimingElement) null, (String) null, Collections.singletonList(new Period((String) null, 0, Collections.singletonList(new AdaptationSet(0, adaptationSetType, representations)))));
    }

    private static MediaFormat getTrackFormat(int adaptationSetType, Format format, String mediaMimeType, long durationUs) {
        switch (adaptationSetType) {
            case 0:
                return MediaFormat.createVideoFormat(format.f1192id, mediaMimeType, format.bitrate, -1, durationUs, format.width, format.height, (List<byte[]>) null);
            case 1:
                return MediaFormat.createAudioFormat(format.f1192id, mediaMimeType, format.bitrate, -1, durationUs, format.audioChannels, format.audioSamplingRate, (List<byte[]>) null, format.language);
            case 2:
                return MediaFormat.createTextFormat(format.f1192id, mediaMimeType, format.bitrate, durationUs, format.language);
            default:
                return null;
        }
    }

    private static String getMediaMimeType(Format format) {
        String formatMimeType = format.mimeType;
        if (MimeTypes.isAudio(formatMimeType)) {
            return MimeTypes.getAudioMediaMimeType(format.codecs);
        }
        if (MimeTypes.isVideo(formatMimeType)) {
            return MimeTypes.getVideoMediaMimeType(format.codecs);
        }
        if (mimeTypeIsRawText(formatMimeType)) {
            return formatMimeType;
        }
        if (MimeTypes.APPLICATION_MP4.equals(formatMimeType)) {
            if (XMLSubtitleSampleEntry.TYPE.equals(format.codecs)) {
                return MimeTypes.APPLICATION_TTML;
            }
            if (WebVTTSampleEntry.TYPE.equals(format.codecs)) {
                return MimeTypes.APPLICATION_MP4VTT;
            }
        }
        return null;
    }

    static boolean mimeTypeIsWebm(String mimeType) {
        return mimeType.startsWith(MimeTypes.VIDEO_WEBM) || mimeType.startsWith(MimeTypes.AUDIO_WEBM) || mimeType.startsWith(MimeTypes.APPLICATION_WEBM);
    }

    static boolean mimeTypeIsRawText(String mimeType) {
        return MimeTypes.TEXT_VTT.equals(mimeType) || MimeTypes.APPLICATION_TTML.equals(mimeType);
    }

    private Chunk newInitializationChunk(RangedUri initializationUri, RangedUri indexUri, Representation representation, ChunkExtractorWrapper extractor, DataSource dataSource2, int manifestIndex, int trigger) {
        RangedUri requestUri;
        if (initializationUri != null) {
            requestUri = initializationUri.attemptMerge(indexUri);
            if (requestUri == null) {
                requestUri = initializationUri;
            }
        } else {
            requestUri = indexUri;
        }
        return new InitializationChunk(dataSource2, new DataSpec(requestUri.getUri(), requestUri.start, requestUri.length, representation.getCacheKey()), trigger, representation.format, extractor, manifestIndex);
    }

    /* access modifiers changed from: protected */
    public Chunk newMediaChunk(PeriodHolder periodHolder, RepresentationHolder representationHolder, DataSource dataSource2, MediaFormat mediaFormat, ExposedTrack enabledTrack2, int segmentNum, int trigger, boolean isMediaFormatFinal) {
        Representation representation = representationHolder.representation;
        Format format = representation.format;
        long startTimeUs = representationHolder.getSegmentStartTimeUs(segmentNum);
        long endTimeUs = representationHolder.getSegmentEndTimeUs(segmentNum);
        RangedUri segmentUri = representationHolder.getSegmentUrl(segmentNum);
        DataSpec dataSpec = new DataSpec(segmentUri.getUri(), segmentUri.start, segmentUri.length, representation.getCacheKey());
        long sampleOffsetUs = periodHolder.startTimeUs - representation.presentationTimeOffsetUs;
        if (mimeTypeIsRawText(format.mimeType)) {
            return new SingleSampleMediaChunk(dataSource2, dataSpec, 1, format, startTimeUs, endTimeUs, segmentNum, enabledTrack2.trackFormat, (DrmInitData) null, periodHolder.localIndex);
        }
        return new ContainerMediaChunk(dataSource2, dataSpec, trigger, format, startTimeUs, endTimeUs, segmentNum, sampleOffsetUs, representationHolder.extractorWrapper, mediaFormat, enabledTrack2.adaptiveMaxWidth, enabledTrack2.adaptiveMaxHeight, periodHolder.drmInitData, isMediaFormatFinal, periodHolder.localIndex);
    }

    private long getNowUnixTimeUs() {
        if (this.elapsedRealtimeOffsetUs != 0) {
            return (this.systemClock.elapsedRealtime() * 1000) + this.elapsedRealtimeOffsetUs;
        }
        return System.currentTimeMillis() * 1000;
    }

    private PeriodHolder findPeriodHolder(long positionUs) {
        if (positionUs < this.periodHolders.valueAt(0).getAvailableStartTimeUs()) {
            return this.periodHolders.valueAt(0);
        }
        for (int i = 0; i < this.periodHolders.size() - 1; i++) {
            PeriodHolder periodHolder = this.periodHolders.valueAt(i);
            if (positionUs < periodHolder.getAvailableEndTimeUs()) {
                return periodHolder;
            }
        }
        return this.periodHolders.valueAt(this.periodHolders.size() - 1);
    }

    private void processManifest(MediaPresentationDescription manifest) {
        Period firstPeriod = manifest.getPeriod(0);
        while (this.periodHolders.size() > 0 && this.periodHolders.valueAt(0).startTimeUs < firstPeriod.startMs * 1000) {
            this.periodHolders.remove(this.periodHolders.valueAt(0).localIndex);
        }
        if (this.periodHolders.size() <= manifest.getPeriodCount()) {
            try {
                int periodHolderCount = this.periodHolders.size();
                if (periodHolderCount > 0) {
                    this.periodHolders.valueAt(0).updatePeriod(manifest, 0, this.enabledTrack);
                    if (periodHolderCount > 1) {
                        int lastIndex = periodHolderCount - 1;
                        this.periodHolders.valueAt(lastIndex).updatePeriod(manifest, lastIndex, this.enabledTrack);
                    }
                }
                for (int i = this.periodHolders.size(); i < manifest.getPeriodCount(); i++) {
                    this.periodHolders.put(this.nextPeriodHolderIndex, new PeriodHolder(this.nextPeriodHolderIndex, manifest, i, this.enabledTrack));
                    this.nextPeriodHolderIndex++;
                }
                TimeRange newAvailableRange = getAvailableRange(getNowUnixTimeUs());
                if (this.availableRange == null || !this.availableRange.equals(newAvailableRange)) {
                    this.availableRange = newAvailableRange;
                    notifyAvailableRangeChanged(this.availableRange);
                }
                this.currentManifest = manifest;
            } catch (BehindLiveWindowException e) {
                this.fatalError = e;
            }
        }
    }

    private TimeRange getAvailableRange(long nowUnixTimeUs) {
        long maxEndPositionUs;
        PeriodHolder firstPeriod = this.periodHolders.valueAt(0);
        PeriodHolder lastPeriod = this.periodHolders.valueAt(this.periodHolders.size() - 1);
        if (!this.currentManifest.dynamic || lastPeriod.isIndexExplicit()) {
            return new TimeRange.StaticTimeRange(firstPeriod.getAvailableStartTimeUs(), lastPeriod.getAvailableEndTimeUs());
        }
        long minStartPositionUs = firstPeriod.getAvailableStartTimeUs();
        if (lastPeriod.isIndexUnbounded()) {
            maxEndPositionUs = Long.MAX_VALUE;
        } else {
            maxEndPositionUs = lastPeriod.getAvailableEndTimeUs();
        }
        return new TimeRange.DynamicTimeRange(minStartPositionUs, maxEndPositionUs, (this.systemClock.elapsedRealtime() * 1000) - (nowUnixTimeUs - (this.currentManifest.availabilityStartTime * 1000)), this.currentManifest.timeShiftBufferDepth == -1 ? -1 : this.currentManifest.timeShiftBufferDepth * 1000, this.systemClock);
    }

    private void notifyAvailableRangeChanged(final TimeRange seekRange) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    DashChunkSource.this.eventListener.onAvailableRangeChanged(DashChunkSource.this.eventSourceId, seekRange);
                }
            });
        }
    }

    protected static final class ExposedTrack {
        /* access modifiers changed from: private */
        public final int adaptationSetIndex;
        /* access modifiers changed from: private */
        public final Format[] adaptiveFormats;
        public final int adaptiveMaxHeight;
        public final int adaptiveMaxWidth;
        /* access modifiers changed from: private */
        public final Format fixedFormat;
        public final MediaFormat trackFormat;

        public ExposedTrack(MediaFormat trackFormat2, int adaptationSetIndex2, Format fixedFormat2) {
            this.trackFormat = trackFormat2;
            this.adaptationSetIndex = adaptationSetIndex2;
            this.fixedFormat = fixedFormat2;
            this.adaptiveFormats = null;
            this.adaptiveMaxWidth = -1;
            this.adaptiveMaxHeight = -1;
        }

        public ExposedTrack(MediaFormat trackFormat2, int adaptationSetIndex2, Format[] adaptiveFormats2, int maxWidth, int maxHeight) {
            this.trackFormat = trackFormat2;
            this.adaptationSetIndex = adaptationSetIndex2;
            this.adaptiveFormats = adaptiveFormats2;
            this.adaptiveMaxWidth = maxWidth;
            this.adaptiveMaxHeight = maxHeight;
            this.fixedFormat = null;
        }

        public boolean isAdaptive() {
            return this.adaptiveFormats != null;
        }
    }

    protected static final class RepresentationHolder {
        public final ChunkExtractorWrapper extractorWrapper;
        public MediaFormat mediaFormat;
        public final boolean mimeTypeIsRawText;
        private long periodDurationUs;
        private final long periodStartTimeUs;
        public Representation representation;
        public DashSegmentIndex segmentIndex;
        private int segmentNumShift;

        public RepresentationHolder(long periodStartTimeUs2, long periodDurationUs2, Representation representation2) {
            ChunkExtractorWrapper chunkExtractorWrapper;
            this.periodStartTimeUs = periodStartTimeUs2;
            this.periodDurationUs = periodDurationUs2;
            this.representation = representation2;
            String mimeType = representation2.format.mimeType;
            this.mimeTypeIsRawText = DashChunkSource.mimeTypeIsRawText(mimeType);
            if (this.mimeTypeIsRawText) {
                chunkExtractorWrapper = null;
            } else {
                chunkExtractorWrapper = new ChunkExtractorWrapper(DashChunkSource.mimeTypeIsWebm(mimeType) ? new WebmExtractor() : new FragmentedMp4Extractor());
            }
            this.extractorWrapper = chunkExtractorWrapper;
            this.segmentIndex = representation2.getIndex();
        }

        public void updateRepresentation(long newPeriodDurationUs, Representation newRepresentation) throws BehindLiveWindowException {
            DashSegmentIndex oldIndex = this.representation.getIndex();
            DashSegmentIndex newIndex = newRepresentation.getIndex();
            this.periodDurationUs = newPeriodDurationUs;
            this.representation = newRepresentation;
            if (oldIndex != null) {
                this.segmentIndex = newIndex;
                if (oldIndex.isExplicit()) {
                    int oldIndexLastSegmentNum = oldIndex.getLastSegmentNum(this.periodDurationUs);
                    long oldIndexEndTimeUs = oldIndex.getTimeUs(oldIndexLastSegmentNum) + oldIndex.getDurationUs(oldIndexLastSegmentNum, this.periodDurationUs);
                    int newIndexFirstSegmentNum = newIndex.getFirstSegmentNum();
                    long newIndexStartTimeUs = newIndex.getTimeUs(newIndexFirstSegmentNum);
                    if (oldIndexEndTimeUs == newIndexStartTimeUs) {
                        this.segmentNumShift += (oldIndex.getLastSegmentNum(this.periodDurationUs) + 1) - newIndexFirstSegmentNum;
                    } else if (oldIndexEndTimeUs < newIndexStartTimeUs) {
                        throw new BehindLiveWindowException();
                    } else {
                        this.segmentNumShift += oldIndex.getSegmentNum(newIndexStartTimeUs, this.periodDurationUs) - newIndexFirstSegmentNum;
                    }
                }
            }
        }

        public int getSegmentNum(long positionUs) {
            return this.segmentIndex.getSegmentNum(positionUs - this.periodStartTimeUs, this.periodDurationUs) + this.segmentNumShift;
        }

        public long getSegmentStartTimeUs(int segmentNum) {
            return this.segmentIndex.getTimeUs(segmentNum - this.segmentNumShift) + this.periodStartTimeUs;
        }

        public long getSegmentEndTimeUs(int segmentNum) {
            return getSegmentStartTimeUs(segmentNum) + this.segmentIndex.getDurationUs(segmentNum - this.segmentNumShift, this.periodDurationUs);
        }

        public int getLastSegmentNum() {
            return this.segmentIndex.getLastSegmentNum(this.periodDurationUs);
        }

        public boolean isBeyondLastSegment(int segmentNum) {
            int lastSegmentNum = getLastSegmentNum();
            if (lastSegmentNum != -1 && segmentNum > this.segmentNumShift + lastSegmentNum) {
                return true;
            }
            return false;
        }

        public int getFirstAvailableSegmentNum() {
            return this.segmentIndex.getFirstSegmentNum() + this.segmentNumShift;
        }

        public RangedUri getSegmentUrl(int segmentNum) {
            return this.segmentIndex.getSegmentUrl(segmentNum - this.segmentNumShift);
        }
    }

    protected static final class PeriodHolder {
        private long availableEndTimeUs;
        private long availableStartTimeUs;
        /* access modifiers changed from: private */
        public DrmInitData drmInitData;
        private boolean indexIsExplicit;
        private boolean indexIsUnbounded;
        public final int localIndex;
        public final HashMap<String, RepresentationHolder> representationHolders;
        private final int[] representationIndices;
        public final long startTimeUs;

        public PeriodHolder(int localIndex2, MediaPresentationDescription manifest, int manifestIndex, ExposedTrack selectedTrack) {
            this.localIndex = localIndex2;
            Period period = manifest.getPeriod(manifestIndex);
            long periodDurationUs = getPeriodDurationUs(manifest, manifestIndex);
            AdaptationSet adaptationSet = period.adaptationSets.get(selectedTrack.adaptationSetIndex);
            List<Representation> representations = adaptationSet.representations;
            this.startTimeUs = period.startMs * 1000;
            this.drmInitData = getDrmInitData(adaptationSet);
            if (!selectedTrack.isAdaptive()) {
                this.representationIndices = new int[]{getRepresentationIndex(representations, selectedTrack.fixedFormat.f1192id)};
            } else {
                this.representationIndices = new int[selectedTrack.adaptiveFormats.length];
                for (int j = 0; j < selectedTrack.adaptiveFormats.length; j++) {
                    this.representationIndices[j] = getRepresentationIndex(representations, selectedTrack.adaptiveFormats[j].f1192id);
                }
            }
            this.representationHolders = new HashMap<>();
            for (int i : this.representationIndices) {
                Representation representation = representations.get(i);
                this.representationHolders.put(representation.format.f1192id, new RepresentationHolder(this.startTimeUs, periodDurationUs, representation));
            }
            updateRepresentationIndependentProperties(periodDurationUs, representations.get(this.representationIndices[0]));
        }

        public void updatePeriod(MediaPresentationDescription manifest, int manifestIndex, ExposedTrack selectedTrack) throws BehindLiveWindowException {
            Period period = manifest.getPeriod(manifestIndex);
            long periodDurationUs = getPeriodDurationUs(manifest, manifestIndex);
            List<Representation> representations = period.adaptationSets.get(selectedTrack.adaptationSetIndex).representations;
            for (int i : this.representationIndices) {
                Representation representation = representations.get(i);
                this.representationHolders.get(representation.format.f1192id).updateRepresentation(periodDurationUs, representation);
            }
            updateRepresentationIndependentProperties(periodDurationUs, representations.get(this.representationIndices[0]));
        }

        public long getAvailableStartTimeUs() {
            return this.availableStartTimeUs;
        }

        public long getAvailableEndTimeUs() {
            if (!isIndexUnbounded()) {
                return this.availableEndTimeUs;
            }
            throw new IllegalStateException("Period has unbounded index");
        }

        public boolean isIndexUnbounded() {
            return this.indexIsUnbounded;
        }

        public boolean isIndexExplicit() {
            return this.indexIsExplicit;
        }

        public DrmInitData getDrmInitData() {
            return this.drmInitData;
        }

        private void updateRepresentationIndependentProperties(long periodDurationUs, Representation arbitaryRepresentation) {
            boolean z = true;
            DashSegmentIndex segmentIndex = arbitaryRepresentation.getIndex();
            if (segmentIndex != null) {
                int firstSegmentNum = segmentIndex.getFirstSegmentNum();
                int lastSegmentNum = segmentIndex.getLastSegmentNum(periodDurationUs);
                if (lastSegmentNum != -1) {
                    z = false;
                }
                this.indexIsUnbounded = z;
                this.indexIsExplicit = segmentIndex.isExplicit();
                this.availableStartTimeUs = this.startTimeUs + segmentIndex.getTimeUs(firstSegmentNum);
                if (!this.indexIsUnbounded) {
                    this.availableEndTimeUs = this.startTimeUs + segmentIndex.getTimeUs(lastSegmentNum) + segmentIndex.getDurationUs(lastSegmentNum, periodDurationUs);
                    return;
                }
                return;
            }
            this.indexIsUnbounded = false;
            this.indexIsExplicit = true;
            this.availableStartTimeUs = this.startTimeUs;
            this.availableEndTimeUs = this.startTimeUs + periodDurationUs;
        }

        private static int getRepresentationIndex(List<Representation> representations, String formatId) {
            for (int i = 0; i < representations.size(); i++) {
                if (formatId.equals(representations.get(i).format.f1192id)) {
                    return i;
                }
            }
            throw new IllegalStateException("Missing format id: " + formatId);
        }

        private static DrmInitData getDrmInitData(AdaptationSet adaptationSet) {
            if (adaptationSet.contentProtections.isEmpty()) {
                return null;
            }
            DrmInitData.Mapped drmInitData2 = null;
            for (int i = 0; i < adaptationSet.contentProtections.size(); i++) {
                ContentProtection contentProtection = adaptationSet.contentProtections.get(i);
                if (!(contentProtection.uuid == null || contentProtection.data == null)) {
                    if (drmInitData2 == null) {
                        drmInitData2 = new DrmInitData.Mapped();
                    }
                    drmInitData2.put(contentProtection.uuid, contentProtection.data);
                }
            }
            return drmInitData2;
        }

        private static long getPeriodDurationUs(MediaPresentationDescription manifest, int index) {
            long durationMs = manifest.getPeriodDuration(index);
            if (durationMs == -1) {
                return -1;
            }
            return 1000 * durationMs;
        }
    }
}
