package com.google.android.exoplayer.smoothstreaming;

import android.net.Uri;
import android.os.SystemClock;
import android.util.Base64;
import android.util.SparseArray;
import com.google.android.exoplayer.BehindLiveWindowException;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.chunk.Chunk;
import com.google.android.exoplayer.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer.chunk.ChunkOperationHolder;
import com.google.android.exoplayer.chunk.ChunkSource;
import com.google.android.exoplayer.chunk.ContainerMediaChunk;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.chunk.MediaChunk;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer.extractor.mp4.Track;
import com.google.android.exoplayer.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer.smoothstreaming.SmoothStreamingManifest;
import com.google.android.exoplayer.smoothstreaming.SmoothStreamingTrackSelector;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.MimeTypes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SmoothStreamingChunkSource implements ChunkSource, SmoothStreamingTrackSelector.Output {
    private static final int INITIALIZATION_VECTOR_SIZE = 8;
    private static final int MINIMUM_MANIFEST_REFRESH_PERIOD_MS = 5000;
    private final FormatEvaluator adaptiveFormatEvaluator;
    private SmoothStreamingManifest currentManifest;
    private int currentManifestChunkOffset;
    private final DataSource dataSource;
    private final DrmInitData.Mapped drmInitData;
    private ExposedTrack enabledTrack;
    private final FormatEvaluator.Evaluation evaluation;
    private final SparseArray<ChunkExtractorWrapper> extractorWrappers;
    private IOException fatalError;
    private final boolean live;
    private final long liveEdgeLatencyUs;
    private final ManifestFetcher<SmoothStreamingManifest> manifestFetcher;
    private final SparseArray<MediaFormat> mediaFormats;
    private boolean needManifestRefresh;
    private boolean prepareCalled;
    private final TrackEncryptionBox[] trackEncryptionBoxes;
    private final SmoothStreamingTrackSelector trackSelector;
    private final ArrayList<ExposedTrack> tracks;

    public SmoothStreamingChunkSource(ManifestFetcher<SmoothStreamingManifest> manifestFetcher2, SmoothStreamingTrackSelector trackSelector2, DataSource dataSource2, FormatEvaluator adaptiveFormatEvaluator2, long liveEdgeLatencyMs) {
        this(manifestFetcher2, manifestFetcher2.getManifest(), trackSelector2, dataSource2, adaptiveFormatEvaluator2, liveEdgeLatencyMs);
    }

    public SmoothStreamingChunkSource(SmoothStreamingManifest manifest, SmoothStreamingTrackSelector trackSelector2, DataSource dataSource2, FormatEvaluator adaptiveFormatEvaluator2) {
        this((ManifestFetcher<SmoothStreamingManifest>) null, manifest, trackSelector2, dataSource2, adaptiveFormatEvaluator2, 0);
    }

    private SmoothStreamingChunkSource(ManifestFetcher<SmoothStreamingManifest> manifestFetcher2, SmoothStreamingManifest initialManifest, SmoothStreamingTrackSelector trackSelector2, DataSource dataSource2, FormatEvaluator adaptiveFormatEvaluator2, long liveEdgeLatencyMs) {
        this.manifestFetcher = manifestFetcher2;
        this.currentManifest = initialManifest;
        this.trackSelector = trackSelector2;
        this.dataSource = dataSource2;
        this.adaptiveFormatEvaluator = adaptiveFormatEvaluator2;
        this.liveEdgeLatencyUs = 1000 * liveEdgeLatencyMs;
        this.evaluation = new FormatEvaluator.Evaluation();
        this.tracks = new ArrayList<>();
        this.extractorWrappers = new SparseArray<>();
        this.mediaFormats = new SparseArray<>();
        this.live = initialManifest.isLive;
        SmoothStreamingManifest.ProtectionElement protectionElement = initialManifest.protectionElement;
        if (protectionElement != null) {
            byte[] keyId = getProtectionElementKeyId(protectionElement.data);
            this.trackEncryptionBoxes = new TrackEncryptionBox[1];
            this.trackEncryptionBoxes[0] = new TrackEncryptionBox(true, 8, keyId);
            this.drmInitData = new DrmInitData.Mapped();
            this.drmInitData.put(protectionElement.uuid, new DrmInitData.SchemeInitData(MimeTypes.VIDEO_MP4, protectionElement.data));
            return;
        }
        this.trackEncryptionBoxes = null;
        this.drmInitData = null;
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        }
        this.manifestFetcher.maybeThrowError();
    }

    public boolean prepare() {
        if (!this.prepareCalled) {
            this.prepareCalled = true;
            try {
                this.trackSelector.selectTracks(this.currentManifest, this);
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
        }
    }

    public void continueBuffering(long playbackPositionUs) {
        if (this.manifestFetcher != null && this.currentManifest.isLive && this.fatalError == null) {
            SmoothStreamingManifest newManifest = this.manifestFetcher.getManifest();
            if (!(this.currentManifest == newManifest || newManifest == null)) {
                SmoothStreamingManifest.StreamElement currentElement = this.currentManifest.streamElements[this.enabledTrack.elementIndex];
                int currentElementChunkCount = currentElement.chunkCount;
                SmoothStreamingManifest.StreamElement newElement = newManifest.streamElements[this.enabledTrack.elementIndex];
                if (currentElementChunkCount == 0 || newElement.chunkCount == 0) {
                    this.currentManifestChunkOffset += currentElementChunkCount;
                } else {
                    long newElementStartTimeUs = newElement.getStartTimeUs(0);
                    if (currentElement.getStartTimeUs(currentElementChunkCount - 1) + currentElement.getChunkDurationUs(currentElementChunkCount - 1) <= newElementStartTimeUs) {
                        this.currentManifestChunkOffset += currentElementChunkCount;
                    } else {
                        this.currentManifestChunkOffset += currentElement.getChunkIndex(newElementStartTimeUs);
                    }
                }
                this.currentManifest = newManifest;
                this.needManifestRefresh = false;
            }
            if (this.needManifestRefresh && SystemClock.elapsedRealtime() > this.manifestFetcher.getManifestLoadStartTimestamp() + 5000) {
                this.manifestFetcher.requestRefresh();
            }
        }
    }

    public final void getChunkOperation(List<? extends MediaChunk> queue, long playbackPositionUs, ChunkOperationHolder out) {
        int chunkIndex;
        long chunkEndTimeUs;
        if (this.fatalError != null) {
            out.chunk = null;
            return;
        }
        this.evaluation.queueSize = queue.size();
        if (this.enabledTrack.isAdaptive()) {
            this.adaptiveFormatEvaluator.evaluate(queue, playbackPositionUs, this.enabledTrack.adaptiveFormats, this.evaluation);
        } else {
            this.evaluation.format = this.enabledTrack.fixedFormat;
            this.evaluation.trigger = 2;
        }
        Format selectedFormat = this.evaluation.format;
        out.queueSize = this.evaluation.queueSize;
        if (selectedFormat == null) {
            out.chunk = null;
        } else if (out.queueSize != queue.size() || out.chunk == null || !out.chunk.format.equals(selectedFormat)) {
            out.chunk = null;
            SmoothStreamingManifest.StreamElement streamElement = this.currentManifest.streamElements[this.enabledTrack.elementIndex];
            if (streamElement.chunkCount != 0) {
                if (queue.isEmpty()) {
                    if (this.live) {
                        playbackPositionUs = getLiveSeekPosition(this.currentManifest, this.liveEdgeLatencyUs);
                    }
                    chunkIndex = streamElement.getChunkIndex(playbackPositionUs);
                } else {
                    chunkIndex = (((MediaChunk) queue.get(out.queueSize - 1)).chunkIndex + 1) - this.currentManifestChunkOffset;
                }
                if (!this.live || chunkIndex >= 0) {
                    if (this.currentManifest.isLive) {
                        if (chunkIndex >= streamElement.chunkCount) {
                            this.needManifestRefresh = true;
                            return;
                        } else if (chunkIndex == streamElement.chunkCount - 1) {
                            this.needManifestRefresh = true;
                        }
                    } else if (chunkIndex >= streamElement.chunkCount) {
                        out.endOfStream = true;
                        return;
                    }
                    boolean isLastChunk = !this.currentManifest.isLive && chunkIndex == streamElement.chunkCount + -1;
                    long chunkStartTimeUs = streamElement.getStartTimeUs(chunkIndex);
                    if (isLastChunk) {
                        chunkEndTimeUs = -1;
                    } else {
                        chunkEndTimeUs = chunkStartTimeUs + streamElement.getChunkDurationUs(chunkIndex);
                    }
                    int currentAbsoluteChunkIndex = chunkIndex + this.currentManifestChunkOffset;
                    int manifestTrackIndex = getManifestTrackIndex(streamElement, selectedFormat);
                    int manifestTrackKey = getManifestTrackKey(this.enabledTrack.elementIndex, manifestTrackIndex);
                    out.chunk = newMediaChunk(selectedFormat, streamElement.buildRequestUri(manifestTrackIndex, chunkIndex), (String) null, this.extractorWrappers.get(manifestTrackKey), this.drmInitData, this.dataSource, currentAbsoluteChunkIndex, chunkStartTimeUs, chunkEndTimeUs, this.evaluation.trigger, this.mediaFormats.get(manifestTrackKey), this.enabledTrack.adaptiveMaxWidth, this.enabledTrack.adaptiveMaxHeight);
                    return;
                }
                this.fatalError = new BehindLiveWindowException();
            } else if (this.currentManifest.isLive) {
                this.needManifestRefresh = true;
            } else {
                out.endOfStream = true;
            }
        }
    }

    public void onChunkLoadCompleted(Chunk chunk) {
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
        this.evaluation.format = null;
        this.fatalError = null;
    }

    public void adaptiveTrack(SmoothStreamingManifest manifest, int element, int[] trackIndices) {
        if (this.adaptiveFormatEvaluator != null) {
            MediaFormat maxHeightMediaFormat = null;
            SmoothStreamingManifest.StreamElement streamElement = manifest.streamElements[element];
            int maxWidth = -1;
            int maxHeight = -1;
            Format[] formats = new Format[trackIndices.length];
            for (int i = 0; i < formats.length; i++) {
                int manifestTrackIndex = trackIndices[i];
                formats[i] = streamElement.tracks[manifestTrackIndex].format;
                MediaFormat mediaFormat = initManifestTrack(manifest, element, manifestTrackIndex);
                if (maxHeightMediaFormat == null || mediaFormat.height > maxHeight) {
                    maxHeightMediaFormat = mediaFormat;
                }
                maxWidth = Math.max(maxWidth, mediaFormat.width);
                maxHeight = Math.max(maxHeight, mediaFormat.height);
            }
            Arrays.sort(formats, new Format.DecreasingBandwidthComparator());
            this.tracks.add(new ExposedTrack(maxHeightMediaFormat.copyAsAdaptive((String) null), element, formats, maxWidth, maxHeight));
        }
    }

    public void fixedTrack(SmoothStreamingManifest manifest, int element, int trackIndex) {
        this.tracks.add(new ExposedTrack(initManifestTrack(manifest, element, trackIndex), element, manifest.streamElements[element].tracks[trackIndex].format));
    }

    private MediaFormat initManifestTrack(SmoothStreamingManifest manifest, int elementIndex, int trackIndex) {
        MediaFormat mediaFormat;
        int mp4TrackType;
        List<byte[]> csd;
        int manifestTrackKey = getManifestTrackKey(elementIndex, trackIndex);
        MediaFormat mediaFormat2 = this.mediaFormats.get(manifestTrackKey);
        if (mediaFormat2 != null) {
            return mediaFormat2;
        }
        long durationUs = this.live ? -1 : manifest.durationUs;
        SmoothStreamingManifest.StreamElement element = manifest.streamElements[elementIndex];
        Format format = element.tracks[trackIndex].format;
        byte[][] csdArray = element.tracks[trackIndex].csd;
        switch (element.type) {
            case 0:
                if (csdArray != null) {
                    csd = Arrays.asList(csdArray);
                } else {
                    csd = Collections.singletonList(CodecSpecificDataUtil.buildAacAudioSpecificConfig(format.audioSamplingRate, format.audioChannels));
                }
                mediaFormat = MediaFormat.createAudioFormat(format.f1192id, format.mimeType, format.bitrate, -1, durationUs, format.audioChannels, format.audioSamplingRate, csd, format.language);
                mp4TrackType = Track.TYPE_soun;
                break;
            case 1:
                mediaFormat = MediaFormat.createVideoFormat(format.f1192id, format.mimeType, format.bitrate, -1, durationUs, format.width, format.height, Arrays.asList(csdArray));
                mp4TrackType = Track.TYPE_vide;
                break;
            case 2:
                mediaFormat = MediaFormat.createTextFormat(format.f1192id, format.mimeType, format.bitrate, durationUs, format.language);
                mp4TrackType = Track.TYPE_text;
                break;
            default:
                throw new IllegalStateException("Invalid type: " + element.type);
        }
        FragmentedMp4Extractor fragmentedMp4Extractor = new FragmentedMp4Extractor(3, new Track(trackIndex, mp4TrackType, element.timescale, -1, durationUs, mediaFormat, this.trackEncryptionBoxes, mp4TrackType == Track.TYPE_vide ? 4 : -1, (long[]) null, (long[]) null));
        this.mediaFormats.put(manifestTrackKey, mediaFormat);
        this.extractorWrappers.put(manifestTrackKey, new ChunkExtractorWrapper(fragmentedMp4Extractor));
        return mediaFormat;
    }

    private static long getLiveSeekPosition(SmoothStreamingManifest manifest, long liveEdgeLatencyUs2) {
        long liveEdgeTimestampUs = Long.MIN_VALUE;
        for (SmoothStreamingManifest.StreamElement streamElement : manifest.streamElements) {
            if (streamElement.chunkCount > 0) {
                liveEdgeTimestampUs = Math.max(liveEdgeTimestampUs, streamElement.getStartTimeUs(streamElement.chunkCount - 1) + streamElement.getChunkDurationUs(streamElement.chunkCount - 1));
            }
        }
        return liveEdgeTimestampUs - liveEdgeLatencyUs2;
    }

    private static int getManifestTrackIndex(SmoothStreamingManifest.StreamElement element, Format format) {
        SmoothStreamingManifest.TrackElement[] tracks2 = element.tracks;
        for (int i = 0; i < tracks2.length; i++) {
            if (tracks2[i].format.equals(format)) {
                return i;
            }
        }
        throw new IllegalStateException("Invalid format: " + format);
    }

    private static MediaChunk newMediaChunk(Format formatInfo, Uri uri, String cacheKey, ChunkExtractorWrapper extractorWrapper, DrmInitData drmInitData2, DataSource dataSource2, int chunkIndex, long chunkStartTimeUs, long chunkEndTimeUs, int trigger, MediaFormat mediaFormat, int adaptiveMaxWidth, int adaptiveMaxHeight) {
        return new ContainerMediaChunk(dataSource2, new DataSpec(uri, 0, -1, cacheKey), trigger, formatInfo, chunkStartTimeUs, chunkEndTimeUs, chunkIndex, chunkStartTimeUs, extractorWrapper, mediaFormat, adaptiveMaxWidth, adaptiveMaxHeight, drmInitData2, true, -1);
    }

    private static int getManifestTrackKey(int elementIndex, int trackIndex) {
        Assertions.checkState(elementIndex <= 65536 && trackIndex <= 65536);
        return (elementIndex << 16) | trackIndex;
    }

    private static byte[] getProtectionElementKeyId(byte[] initData) {
        StringBuilder initDataStringBuilder = new StringBuilder();
        for (int i = 0; i < initData.length; i += 2) {
            initDataStringBuilder.append((char) initData[i]);
        }
        String initDataString = initDataStringBuilder.toString();
        byte[] keyId = Base64.decode(initDataString.substring(initDataString.indexOf("<KID>") + 5, initDataString.indexOf("</KID>")), 0);
        swap(keyId, 0, 3);
        swap(keyId, 1, 2);
        swap(keyId, 4, 5);
        swap(keyId, 6, 7);
        return keyId;
    }

    private static void swap(byte[] data, int firstPosition, int secondPosition) {
        byte temp = data[firstPosition];
        data[firstPosition] = data[secondPosition];
        data[secondPosition] = temp;
    }

    private static final class ExposedTrack {
        /* access modifiers changed from: private */
        public final Format[] adaptiveFormats;
        /* access modifiers changed from: private */
        public final int adaptiveMaxHeight;
        /* access modifiers changed from: private */
        public final int adaptiveMaxWidth;
        /* access modifiers changed from: private */
        public final int elementIndex;
        /* access modifiers changed from: private */
        public final Format fixedFormat;
        public final MediaFormat trackFormat;

        public ExposedTrack(MediaFormat trackFormat2, int elementIndex2, Format fixedFormat2) {
            this.trackFormat = trackFormat2;
            this.elementIndex = elementIndex2;
            this.fixedFormat = fixedFormat2;
            this.adaptiveFormats = null;
            this.adaptiveMaxWidth = -1;
            this.adaptiveMaxHeight = -1;
        }

        public ExposedTrack(MediaFormat trackFormat2, int elementIndex2, Format[] adaptiveFormats2, int adaptiveMaxWidth2, int adaptiveMaxHeight2) {
            this.trackFormat = trackFormat2;
            this.elementIndex = elementIndex2;
            this.adaptiveFormats = adaptiveFormats2;
            this.adaptiveMaxWidth = adaptiveMaxWidth2;
            this.adaptiveMaxHeight = adaptiveMaxHeight2;
            this.fixedFormat = null;
        }

        public boolean isAdaptive() {
            return this.adaptiveFormats != null;
        }
    }
}
