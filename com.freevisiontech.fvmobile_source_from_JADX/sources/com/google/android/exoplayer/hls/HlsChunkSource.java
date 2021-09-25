package com.google.android.exoplayer.hls;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer.BehindLiveWindowException;
import com.google.android.exoplayer.chunk.Chunk;
import com.google.android.exoplayer.chunk.ChunkOperationHolder;
import com.google.android.exoplayer.chunk.DataChunk;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.extractor.p016ts.AdtsExtractor;
import com.google.android.exoplayer.extractor.p016ts.PtsTimestampAdjuster;
import com.google.android.exoplayer.extractor.p016ts.TsExtractor;
import com.google.android.exoplayer.hls.HlsMediaPlaylist;
import com.google.android.exoplayer.hls.HlsTrackSelector;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.UriUtil;
import com.google.android.exoplayer.util.Util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HlsChunkSource implements HlsTrackSelector.Output {
    private static final String AAC_FILE_EXTENSION = ".aac";
    private static final float BANDWIDTH_FRACTION = 0.8f;
    public static final long DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS = 20000;
    public static final long DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS = 5000;
    public static final long DEFAULT_PLAYLIST_BLACKLIST_MS = 60000;
    private static final double LIVE_VARIANT_SWITCH_SAFETY_EXTRA_SECS = 2.0d;
    private static final String MP3_FILE_EXTENSION = ".mp3";
    private static final String TAG = "HlsChunkSource";
    private static final String VTT_FILE_EXTENSION = ".vtt";
    private static final String WEBVTT_FILE_EXTENSION = ".webvtt";
    private final BandwidthMeter bandwidthMeter;
    private final String baseUri;
    private final DataSource dataSource;
    private long durationUs;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private final Handler eventHandler;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    private IOException fatalError;
    private final boolean isMaster;
    private boolean live;
    private final HlsMasterPlaylist masterPlaylist;
    private final long maxBufferDurationToSwitchDownUs;
    private final long minBufferDurationToSwitchUpUs;
    private final HlsPlaylistParser playlistParser;
    private boolean prepareCalled;
    private byte[] scratchSpace;
    private int selectedTrackIndex;
    private int selectedVariantIndex;
    private final PtsTimestampAdjusterProvider timestampAdjusterProvider;
    private final HlsTrackSelector trackSelector;
    private final ArrayList<ExposedTrack> tracks;
    private long[] variantBlacklistTimes;
    private long[] variantLastPlaylistLoadTimesMs;
    private HlsMediaPlaylist[] variantPlaylists;
    private Variant[] variants;

    public interface EventListener {
        void onMediaPlaylistLoadCompleted(byte[] bArr);
    }

    public HlsChunkSource(boolean isMaster2, DataSource dataSource2, HlsPlaylist playlist, HlsTrackSelector trackSelector2, BandwidthMeter bandwidthMeter2, PtsTimestampAdjusterProvider timestampAdjusterProvider2) {
        this(isMaster2, dataSource2, playlist, trackSelector2, bandwidthMeter2, timestampAdjusterProvider2, 5000, DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS, (Handler) null, (EventListener) null);
    }

    public HlsChunkSource(boolean isMaster2, DataSource dataSource2, HlsPlaylist playlist, HlsTrackSelector trackSelector2, BandwidthMeter bandwidthMeter2, PtsTimestampAdjusterProvider timestampAdjusterProvider2, long minBufferDurationToSwitchUpMs, long maxBufferDurationToSwitchDownMs) {
        this(isMaster2, dataSource2, playlist, trackSelector2, bandwidthMeter2, timestampAdjusterProvider2, minBufferDurationToSwitchUpMs, maxBufferDurationToSwitchDownMs, (Handler) null, (EventListener) null);
    }

    public HlsChunkSource(boolean isMaster2, DataSource dataSource2, HlsPlaylist playlist, HlsTrackSelector trackSelector2, BandwidthMeter bandwidthMeter2, PtsTimestampAdjusterProvider timestampAdjusterProvider2, long minBufferDurationToSwitchUpMs, long maxBufferDurationToSwitchDownMs, Handler eventHandler2, EventListener eventListener2) {
        this.isMaster = isMaster2;
        this.dataSource = dataSource2;
        this.trackSelector = trackSelector2;
        this.bandwidthMeter = bandwidthMeter2;
        this.timestampAdjusterProvider = timestampAdjusterProvider2;
        this.eventListener = eventListener2;
        this.eventHandler = eventHandler2;
        this.minBufferDurationToSwitchUpUs = 1000 * minBufferDurationToSwitchUpMs;
        this.maxBufferDurationToSwitchDownUs = 1000 * maxBufferDurationToSwitchDownMs;
        this.baseUri = playlist.baseUri;
        this.playlistParser = new HlsPlaylistParser();
        this.tracks = new ArrayList<>();
        if (playlist.type == 0) {
            this.masterPlaylist = (HlsMasterPlaylist) playlist;
            return;
        }
        Format format = new Format("0", MimeTypes.APPLICATION_M3U8, -1, -1, -1.0f, -1, -1, -1, (String) null, (String) null);
        List<Variant> variants2 = new ArrayList<>();
        variants2.add(new Variant(this.baseUri, format));
        this.masterPlaylist = new HlsMasterPlaylist(this.baseUri, variants2, Collections.emptyList(), Collections.emptyList(), (String) null, (String) null);
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        }
    }

    public boolean prepare() {
        if (!this.prepareCalled) {
            this.prepareCalled = true;
            try {
                this.trackSelector.selectTracks(this.masterPlaylist, this);
                selectTrack(0);
            } catch (IOException e) {
                this.fatalError = e;
            }
        }
        if (this.fatalError == null) {
            return true;
        }
        return false;
    }

    public boolean isLive() {
        return this.live;
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public int getTrackCount() {
        return this.tracks.size();
    }

    public Variant getFixedTrackVariant(int index) {
        Variant[] variants2 = this.tracks.get(index).variants;
        if (variants2.length == 1) {
            return variants2[0];
        }
        return null;
    }

    public String getMuxedAudioLanguage() {
        return this.masterPlaylist.muxedAudioLanguage;
    }

    public String getMuxedCaptionLanguage() {
        return this.masterPlaylist.muxedCaptionLanguage;
    }

    public int getSelectedTrackIndex() {
        return this.selectedTrackIndex;
    }

    public void selectTrack(int index) {
        this.selectedTrackIndex = index;
        ExposedTrack selectedTrack = this.tracks.get(this.selectedTrackIndex);
        this.selectedVariantIndex = selectedTrack.defaultVariantIndex;
        this.variants = selectedTrack.variants;
        this.variantPlaylists = new HlsMediaPlaylist[this.variants.length];
        this.variantLastPlaylistLoadTimesMs = new long[this.variants.length];
        this.variantBlacklistTimes = new long[this.variants.length];
    }

    public void seek() {
        if (this.isMaster) {
            this.timestampAdjusterProvider.reset();
        }
    }

    public void reset() {
        this.fatalError = null;
    }

    public void getChunkOperation(TsChunk previousTsChunk, long playbackPositionUs, ChunkOperationHolder out) {
        int previousChunkVariantIndex;
        int chunkMediaSequence;
        long startTimeUs;
        HlsExtractorWrapper extractorWrapper;
        if (previousTsChunk == null) {
            previousChunkVariantIndex = -1;
        } else {
            previousChunkVariantIndex = getVariantIndex(previousTsChunk.format);
        }
        int nextVariantIndex = getNextVariantIndex(previousTsChunk, playbackPositionUs);
        boolean switchingVariant = (previousTsChunk == null || previousChunkVariantIndex == nextVariantIndex) ? false : true;
        HlsMediaPlaylist mediaPlaylist = this.variantPlaylists[nextVariantIndex];
        if (mediaPlaylist == null) {
            out.chunk = newMediaPlaylistChunk(nextVariantIndex);
            return;
        }
        this.selectedVariantIndex = nextVariantIndex;
        if (this.live) {
            if (previousTsChunk == null) {
                chunkMediaSequence = getLiveStartChunkSequenceNumber(this.selectedVariantIndex);
            } else {
                chunkMediaSequence = getLiveNextChunkSequenceNumber(previousTsChunk.chunkIndex, previousChunkVariantIndex, this.selectedVariantIndex);
                if (chunkMediaSequence < mediaPlaylist.mediaSequence) {
                    this.fatalError = new BehindLiveWindowException();
                    return;
                }
            }
        } else if (previousTsChunk == null) {
            chunkMediaSequence = Util.binarySearchFloor(mediaPlaylist.segments, Long.valueOf(playbackPositionUs), true, true) + mediaPlaylist.mediaSequence;
        } else if (switchingVariant) {
            chunkMediaSequence = Util.binarySearchFloor(mediaPlaylist.segments, Long.valueOf(previousTsChunk.startTimeUs), true, true) + mediaPlaylist.mediaSequence;
        } else {
            chunkMediaSequence = previousTsChunk.getNextChunkIndex();
        }
        int chunkIndex = chunkMediaSequence - mediaPlaylist.mediaSequence;
        if (chunkIndex < mediaPlaylist.segments.size()) {
            HlsMediaPlaylist.Segment segment = mediaPlaylist.segments.get(chunkIndex);
            Uri chunkUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.url);
            if (segment.isEncrypted) {
                Uri keyUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.encryptionKeyUri);
                if (!keyUri.equals(this.encryptionKeyUri)) {
                    out.chunk = newEncryptionKeyChunk(keyUri, segment.encryptionIV, this.selectedVariantIndex);
                    return;
                } else if (!Util.areEqual(segment.encryptionIV, this.encryptionIvString)) {
                    setEncryptionData(keyUri, segment.encryptionIV, this.encryptionKey);
                }
            } else {
                clearEncryptionData();
            }
            DataSpec dataSpec = new DataSpec(chunkUri, segment.byterangeOffset, segment.byterangeLength, (String) null);
            if (!this.live) {
                startTimeUs = segment.startTimeUs;
            } else if (previousTsChunk == null) {
                startTimeUs = 0;
            } else {
                startTimeUs = previousTsChunk.getAdjustedEndTimeUs() - (switchingVariant ? previousTsChunk.getDurationUs() : 0);
            }
            long endTimeUs = startTimeUs + ((long) (segment.durationSecs * 1000000.0d));
            Format format = this.variants[this.selectedVariantIndex].format;
            String lastPathSegment = chunkUri.getLastPathSegment();
            if (lastPathSegment.endsWith(AAC_FILE_EXTENSION)) {
                extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, new AdtsExtractor(startTimeUs), switchingVariant, -1, -1);
            } else if (lastPathSegment.endsWith(MP3_FILE_EXTENSION)) {
                extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, new Mp3Extractor(startTimeUs), switchingVariant, -1, -1);
            } else if (lastPathSegment.endsWith(WEBVTT_FILE_EXTENSION) || lastPathSegment.endsWith(VTT_FILE_EXTENSION)) {
                PtsTimestampAdjuster timestampAdjuster = this.timestampAdjusterProvider.getAdjuster(this.isMaster, segment.discontinuitySequenceNumber, startTimeUs);
                if (timestampAdjuster != null) {
                    extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, new WebvttExtractor(timestampAdjuster), switchingVariant, -1, -1);
                } else {
                    return;
                }
            } else if (previousTsChunk == null || previousTsChunk.discontinuitySequenceNumber != segment.discontinuitySequenceNumber || !format.equals(previousTsChunk.format)) {
                PtsTimestampAdjuster timestampAdjuster2 = this.timestampAdjusterProvider.getAdjuster(this.isMaster, segment.discontinuitySequenceNumber, startTimeUs);
                if (timestampAdjuster2 != null) {
                    int workaroundFlags = 16;
                    String codecs = format.codecs;
                    if (!TextUtils.isEmpty(codecs)) {
                        if (MimeTypes.getAudioMediaMimeType(codecs) != MimeTypes.AUDIO_AAC) {
                            workaroundFlags = 16 | 2;
                        }
                        if (MimeTypes.getVideoMediaMimeType(codecs) != MimeTypes.VIDEO_H264) {
                            workaroundFlags |= 4;
                        }
                    }
                    Extractor extractor = new TsExtractor(timestampAdjuster2, workaroundFlags);
                    ExposedTrack selectedTrack = this.tracks.get(this.selectedTrackIndex);
                    extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, extractor, switchingVariant, selectedTrack.adaptiveMaxWidth, selectedTrack.adaptiveMaxHeight);
                } else {
                    return;
                }
            } else {
                extractorWrapper = previousTsChunk.extractorWrapper;
            }
            out.chunk = new TsChunk(this.dataSource, dataSpec, 0, format, startTimeUs, endTimeUs, chunkMediaSequence, segment.discontinuitySequenceNumber, extractorWrapper, this.encryptionKey, this.encryptionIv);
        } else if (!mediaPlaylist.live) {
            out.endOfStream = true;
        } else {
            if (shouldRerequestLiveMediaPlaylist(this.selectedVariantIndex)) {
                out.chunk = newMediaPlaylistChunk(this.selectedVariantIndex);
            }
        }
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof MediaPlaylistChunk) {
            MediaPlaylistChunk mediaPlaylistChunk = (MediaPlaylistChunk) chunk;
            this.scratchSpace = mediaPlaylistChunk.getDataHolder();
            setMediaPlaylist(mediaPlaylistChunk.variantIndex, mediaPlaylistChunk.getResult());
            if (this.eventHandler != null && this.eventListener != null) {
                final byte[] rawResponse = mediaPlaylistChunk.getRawResponse();
                this.eventHandler.post(new Runnable() {
                    public void run() {
                        HlsChunkSource.this.eventListener.onMediaPlaylistLoadCompleted(rawResponse);
                    }
                });
            }
        } else if (chunk instanceof EncryptionKeyChunk) {
            EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk) chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.f1198iv, encryptionKeyChunk.getResult());
        }
    }

    public boolean onChunkLoadError(Chunk chunk, IOException e) {
        int responseCode;
        int variantIndex;
        if (chunk.bytesLoaded() != 0 || ((!(chunk instanceof TsChunk) && !(chunk instanceof MediaPlaylistChunk) && !(chunk instanceof EncryptionKeyChunk)) || !(e instanceof HttpDataSource.InvalidResponseCodeException) || ((responseCode = ((HttpDataSource.InvalidResponseCodeException) e).responseCode) != 404 && responseCode != 410))) {
            return false;
        }
        if (chunk instanceof TsChunk) {
            variantIndex = getVariantIndex(((TsChunk) chunk).format);
        } else if (chunk instanceof MediaPlaylistChunk) {
            variantIndex = ((MediaPlaylistChunk) chunk).variantIndex;
        } else {
            variantIndex = ((EncryptionKeyChunk) chunk).variantIndex;
        }
        boolean alreadyBlacklisted = this.variantBlacklistTimes[variantIndex] != 0;
        this.variantBlacklistTimes[variantIndex] = SystemClock.elapsedRealtime();
        if (alreadyBlacklisted) {
            Log.w(TAG, "Already blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
            return false;
        } else if (!allVariantsBlacklisted()) {
            Log.w(TAG, "Blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
            return true;
        } else {
            Log.w(TAG, "Final variant not blacklisted (" + responseCode + "): " + chunk.dataSpec.uri);
            this.variantBlacklistTimes[variantIndex] = 0;
            return false;
        }
    }

    public void adaptiveTrack(HlsMasterPlaylist playlist, Variant[] variants2) {
        Arrays.sort(variants2, new Comparator<Variant>() {
            private final Comparator<Format> formatComparator = new Format.DecreasingBandwidthComparator();

            public int compare(Variant first, Variant second) {
                return this.formatComparator.compare(first.format, second.format);
            }
        });
        int defaultVariantIndex = computeDefaultVariantIndex(playlist, variants2, this.bandwidthMeter);
        int maxWidth = -1;
        int maxHeight = -1;
        for (Variant variant : variants2) {
            Format variantFormat = variant.format;
            maxWidth = Math.max(variantFormat.width, maxWidth);
            maxHeight = Math.max(variantFormat.height, maxHeight);
        }
        if (maxWidth <= 0) {
            maxWidth = 1920;
        }
        if (maxHeight <= 0) {
            maxHeight = 1080;
        }
        this.tracks.add(new ExposedTrack(variants2, defaultVariantIndex, maxWidth, maxHeight));
    }

    public void fixedTrack(HlsMasterPlaylist playlist, Variant variant) {
        this.tracks.add(new ExposedTrack(variant));
    }

    /* access modifiers changed from: protected */
    public int computeDefaultVariantIndex(HlsMasterPlaylist playlist, Variant[] variants2, BandwidthMeter bandwidthMeter2) {
        int defaultVariantIndex = 0;
        int minOriginalVariantIndex = Integer.MAX_VALUE;
        for (int i = 0; i < variants2.length; i++) {
            int originalVariantIndex = playlist.variants.indexOf(variants2[i]);
            if (originalVariantIndex < minOriginalVariantIndex) {
                minOriginalVariantIndex = originalVariantIndex;
                defaultVariantIndex = i;
            }
        }
        return defaultVariantIndex;
    }

    private int getLiveStartChunkSequenceNumber(int variantIndex) {
        HlsMediaPlaylist mediaPlaylist = this.variantPlaylists[variantIndex];
        return mediaPlaylist.mediaSequence + (mediaPlaylist.segments.size() > 3 ? mediaPlaylist.segments.size() - 3 : 0);
    }

    private int getLiveNextChunkSequenceNumber(int previousChunkIndex, int oldVariantIndex, int newVariantIndex) {
        if (oldVariantIndex == newVariantIndex) {
            return previousChunkIndex + 1;
        }
        HlsMediaPlaylist oldMediaPlaylist = this.variantPlaylists[oldVariantIndex];
        HlsMediaPlaylist newMediaPlaylist = this.variantPlaylists[newVariantIndex];
        double offsetToLiveInstantSecs = 0.0d;
        for (int i = previousChunkIndex - oldMediaPlaylist.mediaSequence; i < oldMediaPlaylist.segments.size(); i++) {
            offsetToLiveInstantSecs += oldMediaPlaylist.segments.get(i).durationSecs;
        }
        long currentTimeMs = SystemClock.elapsedRealtime();
        double offsetToLiveInstantSecs2 = ((offsetToLiveInstantSecs + (((double) (currentTimeMs - this.variantLastPlaylistLoadTimesMs[oldVariantIndex])) / 1000.0d)) + LIVE_VARIANT_SWITCH_SAFETY_EXTRA_SECS) - (((double) (currentTimeMs - this.variantLastPlaylistLoadTimesMs[newVariantIndex])) / 1000.0d);
        if (offsetToLiveInstantSecs2 < 0.0d) {
            return newMediaPlaylist.mediaSequence + newMediaPlaylist.segments.size() + 1;
        }
        for (int i2 = newMediaPlaylist.segments.size() - 1; i2 >= 0; i2--) {
            offsetToLiveInstantSecs2 -= newMediaPlaylist.segments.get(i2).durationSecs;
            if (offsetToLiveInstantSecs2 < 0.0d) {
                return newMediaPlaylist.mediaSequence + i2;
            }
        }
        return newMediaPlaylist.mediaSequence - 1;
    }

    private int getNextVariantIndex(TsChunk previousTsChunk, long playbackPositionUs) {
        clearStaleBlacklistedVariants();
        long bitrateEstimate = this.bandwidthMeter.getBitrateEstimate();
        if (this.variantBlacklistTimes[this.selectedVariantIndex] != 0) {
            return getVariantIndexForBandwidth(bitrateEstimate);
        }
        if (previousTsChunk == null) {
            return this.selectedVariantIndex;
        }
        if (bitrateEstimate == -1) {
            return this.selectedVariantIndex;
        }
        int idealIndex = getVariantIndexForBandwidth(bitrateEstimate);
        if (idealIndex == this.selectedVariantIndex) {
            return this.selectedVariantIndex;
        }
        long bufferedUs = (previousTsChunk.getAdjustedEndTimeUs() - previousTsChunk.getDurationUs()) - playbackPositionUs;
        if (this.variantBlacklistTimes[this.selectedVariantIndex] != 0) {
            return idealIndex;
        }
        if (idealIndex <= this.selectedVariantIndex || bufferedUs >= this.maxBufferDurationToSwitchDownUs) {
            return (idealIndex >= this.selectedVariantIndex || bufferedUs <= this.minBufferDurationToSwitchUpUs) ? this.selectedVariantIndex : idealIndex;
        }
        return idealIndex;
    }

    private int getVariantIndexForBandwidth(long bitrateEstimate) {
        if (bitrateEstimate == -1) {
            bitrateEstimate = 0;
        }
        int effectiveBitrate = (int) (((float) bitrateEstimate) * 0.8f);
        int lowestQualityEnabledVariantIndex = -1;
        for (int i = 0; i < this.variants.length; i++) {
            if (this.variantBlacklistTimes[i] == 0) {
                if (this.variants[i].format.bitrate <= effectiveBitrate) {
                    return i;
                }
                lowestQualityEnabledVariantIndex = i;
            }
        }
        Assertions.checkState(lowestQualityEnabledVariantIndex != -1);
        return lowestQualityEnabledVariantIndex;
    }

    private boolean shouldRerequestLiveMediaPlaylist(int nextVariantIndex) {
        return SystemClock.elapsedRealtime() - this.variantLastPlaylistLoadTimesMs[nextVariantIndex] >= ((long) ((this.variantPlaylists[nextVariantIndex].targetDurationSecs * 1000) / 2));
    }

    private MediaPlaylistChunk newMediaPlaylistChunk(int variantIndex) {
        Uri mediaPlaylistUri = UriUtil.resolveToUri(this.baseUri, this.variants[variantIndex].url);
        return new MediaPlaylistChunk(this.dataSource, new DataSpec(mediaPlaylistUri, 0, -1, (String) null, 1), this.scratchSpace, this.playlistParser, variantIndex, mediaPlaylistUri.toString());
    }

    private EncryptionKeyChunk newEncryptionKeyChunk(Uri keyUri, String iv, int variantIndex) {
        return new EncryptionKeyChunk(this.dataSource, new DataSpec(keyUri, 0, -1, (String) null, 1), this.scratchSpace, iv, variantIndex);
    }

    private void setEncryptionData(Uri keyUri, String iv, byte[] secretKey) {
        String trimmedIv;
        if (iv.toLowerCase(Locale.getDefault()).startsWith("0x")) {
            trimmedIv = iv.substring(2);
        } else {
            trimmedIv = iv;
        }
        byte[] ivData = new BigInteger(trimmedIv, 16).toByteArray();
        byte[] ivDataWithPadding = new byte[16];
        int offset = ivData.length > 16 ? ivData.length - 16 : 0;
        System.arraycopy(ivData, offset, ivDataWithPadding, (ivDataWithPadding.length - ivData.length) + offset, ivData.length - offset);
        this.encryptionKeyUri = keyUri;
        this.encryptionKey = secretKey;
        this.encryptionIvString = iv;
        this.encryptionIv = ivDataWithPadding;
    }

    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }

    private void setMediaPlaylist(int variantIndex, HlsMediaPlaylist mediaPlaylist) {
        this.variantLastPlaylistLoadTimesMs[variantIndex] = SystemClock.elapsedRealtime();
        this.variantPlaylists[variantIndex] = mediaPlaylist;
        this.live |= mediaPlaylist.live;
        this.durationUs = this.live ? -1 : mediaPlaylist.durationUs;
    }

    private boolean allVariantsBlacklisted() {
        for (long variantBlacklistTime : this.variantBlacklistTimes) {
            if (variantBlacklistTime == 0) {
                return false;
            }
        }
        return true;
    }

    private void clearStaleBlacklistedVariants() {
        long currentTime = SystemClock.elapsedRealtime();
        for (int i = 0; i < this.variantBlacklistTimes.length; i++) {
            if (this.variantBlacklistTimes[i] != 0 && currentTime - this.variantBlacklistTimes[i] > 60000) {
                this.variantBlacklistTimes[i] = 0;
            }
        }
    }

    private int getVariantIndex(Format format) {
        for (int i = 0; i < this.variants.length; i++) {
            if (this.variants[i].format.equals(format)) {
                return i;
            }
        }
        throw new IllegalStateException("Invalid format: " + format);
    }

    private static final class ExposedTrack {
        /* access modifiers changed from: private */
        public final int adaptiveMaxHeight;
        /* access modifiers changed from: private */
        public final int adaptiveMaxWidth;
        /* access modifiers changed from: private */
        public final int defaultVariantIndex;
        /* access modifiers changed from: private */
        public final Variant[] variants;

        public ExposedTrack(Variant fixedVariant) {
            this.variants = new Variant[]{fixedVariant};
            this.defaultVariantIndex = 0;
            this.adaptiveMaxWidth = -1;
            this.adaptiveMaxHeight = -1;
        }

        public ExposedTrack(Variant[] adaptiveVariants, int defaultVariantIndex2, int maxWidth, int maxHeight) {
            this.variants = adaptiveVariants;
            this.defaultVariantIndex = defaultVariantIndex2;
            this.adaptiveMaxWidth = maxWidth;
            this.adaptiveMaxHeight = maxHeight;
        }
    }

    private static final class MediaPlaylistChunk extends DataChunk {
        private final HlsPlaylistParser playlistParser;
        private final String playlistUrl;
        private byte[] rawResponse;
        private HlsMediaPlaylist result;
        public final int variantIndex;

        public MediaPlaylistChunk(DataSource dataSource, DataSpec dataSpec, byte[] scratchSpace, HlsPlaylistParser playlistParser2, int variantIndex2, String playlistUrl2) {
            super(dataSource, dataSpec, 4, 0, (Format) null, -1, scratchSpace);
            this.variantIndex = variantIndex2;
            this.playlistParser = playlistParser2;
            this.playlistUrl = playlistUrl2;
        }

        /* access modifiers changed from: protected */
        public void consume(byte[] data, int limit) throws IOException {
            this.rawResponse = Arrays.copyOf(data, limit);
            this.result = (HlsMediaPlaylist) this.playlistParser.parse(this.playlistUrl, (InputStream) new ByteArrayInputStream(this.rawResponse));
        }

        public byte[] getRawResponse() {
            return this.rawResponse;
        }

        public HlsMediaPlaylist getResult() {
            return this.result;
        }
    }

    private static final class EncryptionKeyChunk extends DataChunk {

        /* renamed from: iv */
        public final String f1198iv;
        private byte[] result;
        public final int variantIndex;

        public EncryptionKeyChunk(DataSource dataSource, DataSpec dataSpec, byte[] scratchSpace, String iv, int variantIndex2) {
            super(dataSource, dataSpec, 3, 0, (Format) null, -1, scratchSpace);
            this.f1198iv = iv;
            this.variantIndex = variantIndex2;
        }

        /* access modifiers changed from: protected */
        public void consume(byte[] data, int limit) throws IOException {
            this.result = Arrays.copyOf(data, limit);
        }

        public byte[] getResult() {
            return this.result;
        }
    }
}
