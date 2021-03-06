package com.google.android.exoplayer;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.TraceUtil;
import com.google.android.exoplayer.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@TargetApi(16)
public abstract class MediaCodecTrackRenderer extends SampleSourceTrackRenderer {
    private static final byte[] ADAPTATION_WORKAROUND_BUFFER = Util.getBytesFromHexString("0000016742C00BDA259000000168CE0F13200000016588840DCE7118A0002FBF1C31C3275D78");
    private static final int ADAPTATION_WORKAROUND_SLICE_WIDTH_HEIGHT = 32;
    private static final long MAX_CODEC_HOTSWAP_TIME_MS = 1000;
    private static final int RECONFIGURATION_STATE_NONE = 0;
    private static final int RECONFIGURATION_STATE_QUEUE_PENDING = 2;
    private static final int RECONFIGURATION_STATE_WRITE_PENDING = 1;
    private static final int REINITIALIZATION_STATE_NONE = 0;
    private static final int REINITIALIZATION_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REINITIALIZATION_STATE_WAIT_END_OF_STREAM = 2;
    protected static final int SOURCE_STATE_NOT_READY = 0;
    protected static final int SOURCE_STATE_READY = 1;
    protected static final int SOURCE_STATE_READY_READ_MAY_FAIL = 2;
    private MediaCodec codec;
    public final CodecCounters codecCounters;
    private long codecHotswapTimeMs;
    private boolean codecIsAdaptive;
    private boolean codecNeedsAdaptationWorkaround;
    private boolean codecNeedsAdaptationWorkaroundBuffer;
    private boolean codecNeedsDiscardToSpsWorkaround;
    private boolean codecNeedsEosFlushWorkaround;
    private boolean codecNeedsEosPropagationWorkaround;
    private boolean codecNeedsFlushWorkaround;
    private boolean codecNeedsMonoChannelCountWorkaround;
    private boolean codecReceivedBuffers;
    private boolean codecReceivedEos;
    private int codecReconfigurationState;
    private boolean codecReconfigured;
    private int codecReinitializationState;
    private final List<Long> decodeOnlyPresentationTimestamps;
    private final boolean deviceNeedsAutoFrcWorkaround;
    private DrmInitData drmInitData;
    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    protected final Handler eventHandler;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    private MediaFormat format;
    private final MediaFormatHolder formatHolder;
    private ByteBuffer[] inputBuffers;
    private int inputIndex;
    private boolean inputStreamEnded;
    private final MediaCodecSelector mediaCodecSelector;
    private boolean openedDrmSession;
    private final MediaCodec.BufferInfo outputBufferInfo;
    private ByteBuffer[] outputBuffers;
    private int outputIndex;
    private boolean outputStreamEnded;
    private final boolean playClearSamplesWithoutKeys;
    private final SampleHolder sampleHolder;
    private boolean shouldSkipAdaptationWorkaroundOutputBuffer;
    private int sourceState;
    private boolean waitingForFirstSyncFrame;
    private boolean waitingForKeys;

    public interface EventListener {
        void onCryptoError(MediaCodec.CryptoException cryptoException);

        void onDecoderInitializationError(DecoderInitializationException decoderInitializationException);

        void onDecoderInitialized(String str, long j, long j2);
    }

    /* access modifiers changed from: protected */
    public abstract void configureCodec(MediaCodec mediaCodec, boolean z, MediaFormat mediaFormat, MediaCrypto mediaCrypto);

    /* access modifiers changed from: protected */
    public abstract boolean handlesTrack(MediaCodecSelector mediaCodecSelector2, MediaFormat mediaFormat) throws MediaCodecUtil.DecoderQueryException;

    /* access modifiers changed from: protected */
    public abstract boolean processOutputBuffer(long j, long j2, MediaCodec mediaCodec, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, int i, boolean z) throws ExoPlaybackException;

    public static class DecoderInitializationException extends Exception {
        private static final int CUSTOM_ERROR_CODE_BASE = -50000;
        private static final int DECODER_QUERY_ERROR = -49998;
        private static final int NO_SUITABLE_DECODER_ERROR = -49999;
        public final String decoderName;
        public final String diagnosticInfo;
        public final String mimeType;
        public final boolean secureDecoderRequired;

        public DecoderInitializationException(MediaFormat mediaFormat, Throwable cause, boolean secureDecoderRequired2, int errorCode) {
            super("Decoder init failed: [" + errorCode + "], " + mediaFormat, cause);
            this.mimeType = mediaFormat.mimeType;
            this.secureDecoderRequired = secureDecoderRequired2;
            this.decoderName = null;
            this.diagnosticInfo = buildCustomDiagnosticInfo(errorCode);
        }

        public DecoderInitializationException(MediaFormat mediaFormat, Throwable cause, boolean secureDecoderRequired2, String decoderName2) {
            super("Decoder init failed: " + decoderName2 + ", " + mediaFormat, cause);
            this.mimeType = mediaFormat.mimeType;
            this.secureDecoderRequired = secureDecoderRequired2;
            this.decoderName = decoderName2;
            this.diagnosticInfo = Util.SDK_INT >= 21 ? getDiagnosticInfoV21(cause) : null;
        }

        @TargetApi(21)
        private static String getDiagnosticInfoV21(Throwable cause) {
            if (cause instanceof MediaCodec.CodecException) {
                return ((MediaCodec.CodecException) cause).getDiagnosticInfo();
            }
            return null;
        }

        private static String buildCustomDiagnosticInfo(int errorCode) {
            return "com.google.android.exoplayer.MediaCodecTrackRenderer_" + (errorCode < 0 ? "neg_" : "") + Math.abs(errorCode);
        }
    }

    public MediaCodecTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector2, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager2, boolean playClearSamplesWithoutKeys2, Handler eventHandler2, EventListener eventListener2) {
        this(new SampleSource[]{source}, mediaCodecSelector2, drmSessionManager2, playClearSamplesWithoutKeys2, eventHandler2, eventListener2);
    }

    public MediaCodecTrackRenderer(SampleSource[] sources, MediaCodecSelector mediaCodecSelector2, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager2, boolean playClearSamplesWithoutKeys2, Handler eventHandler2, EventListener eventListener2) {
        super(sources);
        Assertions.checkState(Util.SDK_INT >= 16);
        this.mediaCodecSelector = (MediaCodecSelector) Assertions.checkNotNull(mediaCodecSelector2);
        this.drmSessionManager = drmSessionManager2;
        this.playClearSamplesWithoutKeys = playClearSamplesWithoutKeys2;
        this.eventHandler = eventHandler2;
        this.eventListener = eventListener2;
        this.deviceNeedsAutoFrcWorkaround = deviceNeedsAutoFrcWorkaround();
        this.codecCounters = new CodecCounters();
        this.sampleHolder = new SampleHolder(0);
        this.formatHolder = new MediaFormatHolder();
        this.decodeOnlyPresentationTimestamps = new ArrayList();
        this.outputBufferInfo = new MediaCodec.BufferInfo();
        this.codecReconfigurationState = 0;
        this.codecReinitializationState = 0;
    }

    /* access modifiers changed from: protected */
    public final boolean handlesTrack(MediaFormat mediaFormat) throws MediaCodecUtil.DecoderQueryException {
        return handlesTrack(this.mediaCodecSelector, mediaFormat);
    }

    /* access modifiers changed from: protected */
    public DecoderInfo getDecoderInfo(MediaCodecSelector mediaCodecSelector2, String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        return mediaCodecSelector2.getDecoderInfo(mimeType, requiresSecureDecoder);
    }

    /* access modifiers changed from: protected */
    public final void maybeInitCodec() throws ExoPlaybackException {
        if (shouldInitCodec()) {
            String mimeType = this.format.mimeType;
            MediaCrypto mediaCrypto = null;
            boolean requiresSecureDecoder = false;
            if (this.drmInitData != null) {
                if (this.drmSessionManager == null) {
                    throw new ExoPlaybackException("Media requires a DrmSessionManager");
                }
                if (!this.openedDrmSession) {
                    this.drmSessionManager.open(this.drmInitData);
                    this.openedDrmSession = true;
                }
                int drmSessionState = this.drmSessionManager.getState();
                if (drmSessionState == 0) {
                    throw new ExoPlaybackException((Throwable) this.drmSessionManager.getError());
                } else if (drmSessionState == 3 || drmSessionState == 4) {
                    mediaCrypto = this.drmSessionManager.getMediaCrypto().getWrappedMediaCrypto();
                    requiresSecureDecoder = this.drmSessionManager.requiresSecureDecoderComponent(mimeType);
                } else {
                    return;
                }
            }
            DecoderInfo decoderInfo = null;
            try {
                decoderInfo = getDecoderInfo(this.mediaCodecSelector, mimeType, requiresSecureDecoder);
            } catch (MediaCodecUtil.DecoderQueryException e) {
                notifyAndThrowDecoderInitError(new DecoderInitializationException(this.format, (Throwable) e, requiresSecureDecoder, -49998));
            }
            if (decoderInfo == null) {
                notifyAndThrowDecoderInitError(new DecoderInitializationException(this.format, (Throwable) null, requiresSecureDecoder, -49999));
            }
            String codecName = decoderInfo.name;
            this.codecIsAdaptive = decoderInfo.adaptive;
            this.codecNeedsDiscardToSpsWorkaround = codecNeedsDiscardToSpsWorkaround(codecName, this.format);
            this.codecNeedsFlushWorkaround = codecNeedsFlushWorkaround(codecName);
            this.codecNeedsAdaptationWorkaround = codecNeedsAdaptationWorkaround(codecName);
            this.codecNeedsEosPropagationWorkaround = codecNeedsEosPropagationWorkaround(codecName);
            this.codecNeedsEosFlushWorkaround = codecNeedsEosFlushWorkaround(codecName);
            this.codecNeedsMonoChannelCountWorkaround = codecNeedsMonoChannelCountWorkaround(codecName, this.format);
            try {
                long codecInitializingTimestamp = SystemClock.elapsedRealtime();
                TraceUtil.beginSection("createByCodecName(" + codecName + ")");
                this.codec = MediaCodec.createByCodecName(codecName);
                TraceUtil.endSection();
                TraceUtil.beginSection("configureCodec");
                configureCodec(this.codec, decoderInfo.adaptive, getFrameworkMediaFormat(this.format), mediaCrypto);
                TraceUtil.endSection();
                TraceUtil.beginSection("codec.start()");
                this.codec.start();
                TraceUtil.endSection();
                long codecInitializedTimestamp = SystemClock.elapsedRealtime();
                notifyDecoderInitialized(codecName, codecInitializedTimestamp, codecInitializedTimestamp - codecInitializingTimestamp);
                this.inputBuffers = this.codec.getInputBuffers();
                this.outputBuffers = this.codec.getOutputBuffers();
            } catch (Exception e2) {
                notifyAndThrowDecoderInitError(new DecoderInitializationException(this.format, (Throwable) e2, requiresSecureDecoder, codecName));
            }
            this.codecHotswapTimeMs = getState() == 3 ? SystemClock.elapsedRealtime() : -1;
            this.inputIndex = -1;
            this.outputIndex = -1;
            this.waitingForFirstSyncFrame = true;
            this.codecCounters.codecInitCount++;
        }
    }

    private void notifyAndThrowDecoderInitError(DecoderInitializationException e) throws ExoPlaybackException {
        notifyDecoderInitializationError(e);
        throw new ExoPlaybackException((Throwable) e);
    }

    /* access modifiers changed from: protected */
    public boolean shouldInitCodec() {
        return this.codec == null && this.format != null;
    }

    /* access modifiers changed from: protected */
    public final boolean codecInitialized() {
        return this.codec != null;
    }

    /* access modifiers changed from: protected */
    public final boolean haveFormat() {
        return this.format != null;
    }

    /* access modifiers changed from: protected */
    public void onDisabled() throws ExoPlaybackException {
        this.format = null;
        this.drmInitData = null;
        try {
            releaseCodec();
            try {
                if (this.openedDrmSession) {
                    this.drmSessionManager.close();
                    this.openedDrmSession = false;
                }
            } finally {
                super.onDisabled();
            }
        } catch (Throwable th) {
            if (this.openedDrmSession) {
                this.drmSessionManager.close();
                this.openedDrmSession = false;
            }
            throw th;
        } finally {
            super.onDisabled();
        }
    }

    /* access modifiers changed from: protected */
    public void releaseCodec() {
        if (this.codec != null) {
            this.codecHotswapTimeMs = -1;
            this.inputIndex = -1;
            this.outputIndex = -1;
            this.waitingForKeys = false;
            this.decodeOnlyPresentationTimestamps.clear();
            this.inputBuffers = null;
            this.outputBuffers = null;
            this.codecReconfigured = false;
            this.codecReceivedBuffers = false;
            this.codecIsAdaptive = false;
            this.codecNeedsDiscardToSpsWorkaround = false;
            this.codecNeedsFlushWorkaround = false;
            this.codecNeedsAdaptationWorkaround = false;
            this.codecNeedsEosPropagationWorkaround = false;
            this.codecNeedsEosFlushWorkaround = false;
            this.codecNeedsMonoChannelCountWorkaround = false;
            this.codecNeedsAdaptationWorkaroundBuffer = false;
            this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
            this.codecReceivedEos = false;
            this.codecReconfigurationState = 0;
            this.codecReinitializationState = 0;
            this.codecCounters.codecReleaseCount++;
            try {
                this.codec.stop();
                try {
                    this.codec.release();
                } finally {
                    this.codec = null;
                }
            } catch (Throwable th) {
                this.codec.release();
                throw th;
            } finally {
                this.codec = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDiscontinuity(long positionUs) throws ExoPlaybackException {
        this.sourceState = 0;
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.codec != null) {
            flushCodec();
        }
    }

    /* access modifiers changed from: protected */
    public void onStarted() {
    }

    /* access modifiers changed from: protected */
    public void onStopped() {
    }

    /* access modifiers changed from: protected */
    public void doSomeWork(long positionUs, long elapsedRealtimeUs, boolean sourceIsReady) throws ExoPlaybackException {
        this.sourceState = sourceIsReady ? this.sourceState == 0 ? 1 : this.sourceState : 0;
        if (this.format == null) {
            readFormat(positionUs);
        }
        maybeInitCodec();
        if (this.codec != null) {
            TraceUtil.beginSection("drainAndFeed");
            do {
            } while (drainOutputBuffer(positionUs, elapsedRealtimeUs));
            if (feedInputBuffer(positionUs, true)) {
                do {
                } while (feedInputBuffer(positionUs, false));
            }
            TraceUtil.endSection();
        }
        this.codecCounters.ensureUpdated();
    }

    private void readFormat(long positionUs) throws ExoPlaybackException {
        if (readSource(positionUs, this.formatHolder, (SampleHolder) null) == -4) {
            onInputFormatChanged(this.formatHolder);
        }
    }

    /* access modifiers changed from: protected */
    public void flushCodec() throws ExoPlaybackException {
        this.codecHotswapTimeMs = -1;
        this.inputIndex = -1;
        this.outputIndex = -1;
        this.waitingForFirstSyncFrame = true;
        this.waitingForKeys = false;
        this.decodeOnlyPresentationTimestamps.clear();
        this.codecNeedsAdaptationWorkaroundBuffer = false;
        this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
        if (this.codecNeedsFlushWorkaround || (this.codecNeedsEosFlushWorkaround && this.codecReceivedEos)) {
            releaseCodec();
            maybeInitCodec();
        } else if (this.codecReinitializationState != 0) {
            releaseCodec();
            maybeInitCodec();
        } else {
            this.codec.flush();
            this.codecReceivedBuffers = false;
        }
        if (this.codecReconfigured && this.format != null) {
            this.codecReconfigurationState = 1;
        }
    }

    private boolean feedInputBuffer(long positionUs, boolean firstFeed) throws ExoPlaybackException {
        int result;
        if (this.inputStreamEnded || this.codecReinitializationState == 2) {
            return false;
        }
        if (this.inputIndex < 0) {
            this.inputIndex = this.codec.dequeueInputBuffer(0);
            if (this.inputIndex < 0) {
                return false;
            }
            this.sampleHolder.data = this.inputBuffers[this.inputIndex];
            this.sampleHolder.clearData();
        }
        if (this.codecReinitializationState == 1) {
            if (!this.codecNeedsEosPropagationWorkaround) {
                this.codecReceivedEos = true;
                this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0, 4);
                this.inputIndex = -1;
            }
            this.codecReinitializationState = 2;
            return false;
        } else if (this.codecNeedsAdaptationWorkaroundBuffer) {
            this.codecNeedsAdaptationWorkaroundBuffer = false;
            this.sampleHolder.data.put(ADAPTATION_WORKAROUND_BUFFER);
            this.codec.queueInputBuffer(this.inputIndex, 0, ADAPTATION_WORKAROUND_BUFFER.length, 0, 0);
            this.inputIndex = -1;
            this.codecReceivedBuffers = true;
            return true;
        } else {
            if (this.waitingForKeys) {
                result = -3;
            } else {
                if (this.codecReconfigurationState == 1) {
                    for (int i = 0; i < this.format.initializationData.size(); i++) {
                        this.sampleHolder.data.put(this.format.initializationData.get(i));
                    }
                    this.codecReconfigurationState = 2;
                }
                result = readSource(positionUs, this.formatHolder, this.sampleHolder);
                if (firstFeed && this.sourceState == 1 && result == -2) {
                    this.sourceState = 2;
                }
            }
            if (result == -2) {
                return false;
            }
            if (result == -4) {
                if (this.codecReconfigurationState == 2) {
                    this.sampleHolder.clearData();
                    this.codecReconfigurationState = 1;
                }
                onInputFormatChanged(this.formatHolder);
                return true;
            } else if (result == -1) {
                if (this.codecReconfigurationState == 2) {
                    this.sampleHolder.clearData();
                    this.codecReconfigurationState = 1;
                }
                this.inputStreamEnded = true;
                if (!this.codecReceivedBuffers) {
                    processEndOfStream();
                    return false;
                }
                try {
                    if (!this.codecNeedsEosPropagationWorkaround) {
                        this.codecReceivedEos = true;
                        this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0, 4);
                        this.inputIndex = -1;
                    }
                    return false;
                } catch (MediaCodec.CryptoException e) {
                    notifyCryptoError(e);
                    throw new ExoPlaybackException((Throwable) e);
                }
            } else {
                if (this.waitingForFirstSyncFrame) {
                    if (!this.sampleHolder.isSyncFrame()) {
                        this.sampleHolder.clearData();
                        if (this.codecReconfigurationState == 2) {
                            this.codecReconfigurationState = 1;
                        }
                        return true;
                    }
                    this.waitingForFirstSyncFrame = false;
                }
                boolean sampleEncrypted = this.sampleHolder.isEncrypted();
                this.waitingForKeys = shouldWaitForKeys(sampleEncrypted);
                if (this.waitingForKeys) {
                    return false;
                }
                if (this.codecNeedsDiscardToSpsWorkaround && !sampleEncrypted) {
                    NalUnitUtil.discardToSps(this.sampleHolder.data);
                    if (this.sampleHolder.data.position() == 0) {
                        return true;
                    }
                    this.codecNeedsDiscardToSpsWorkaround = false;
                }
                try {
                    int bufferSize = this.sampleHolder.data.position();
                    int adaptiveReconfigurationBytes = bufferSize - this.sampleHolder.size;
                    long presentationTimeUs = this.sampleHolder.timeUs;
                    if (this.sampleHolder.isDecodeOnly()) {
                        this.decodeOnlyPresentationTimestamps.add(Long.valueOf(presentationTimeUs));
                    }
                    onQueuedInputBuffer(presentationTimeUs, this.sampleHolder.data, bufferSize, sampleEncrypted);
                    if (sampleEncrypted) {
                        this.codec.queueSecureInputBuffer(this.inputIndex, 0, getFrameworkCryptoInfo(this.sampleHolder, adaptiveReconfigurationBytes), presentationTimeUs, 0);
                    } else {
                        this.codec.queueInputBuffer(this.inputIndex, 0, bufferSize, presentationTimeUs, 0);
                    }
                    this.inputIndex = -1;
                    this.codecReceivedBuffers = true;
                    this.codecReconfigurationState = 0;
                    this.codecCounters.inputBufferCount++;
                    return true;
                } catch (MediaCodec.CryptoException e2) {
                    notifyCryptoError(e2);
                    throw new ExoPlaybackException((Throwable) e2);
                }
            }
        }
    }

    private static MediaCodec.CryptoInfo getFrameworkCryptoInfo(SampleHolder sampleHolder2, int adaptiveReconfigurationBytes) {
        MediaCodec.CryptoInfo cryptoInfo = sampleHolder2.cryptoInfo.getFrameworkCryptoInfoV16();
        if (adaptiveReconfigurationBytes != 0) {
            if (cryptoInfo.numBytesOfClearData == null) {
                cryptoInfo.numBytesOfClearData = new int[1];
            }
            int[] iArr = cryptoInfo.numBytesOfClearData;
            iArr[0] = iArr[0] + adaptiveReconfigurationBytes;
        }
        return cryptoInfo;
    }

    private MediaFormat getFrameworkMediaFormat(MediaFormat format2) {
        MediaFormat mediaFormat = format2.getFrameworkMediaFormatV16();
        if (this.deviceNeedsAutoFrcWorkaround) {
            mediaFormat.setInteger("auto-frc", 0);
        }
        return mediaFormat;
    }

    private boolean shouldWaitForKeys(boolean sampleEncrypted) throws ExoPlaybackException {
        if (!this.openedDrmSession) {
            return false;
        }
        int drmManagerState = this.drmSessionManager.getState();
        if (drmManagerState == 0) {
            throw new ExoPlaybackException((Throwable) this.drmSessionManager.getError());
        } else if (drmManagerState == 4) {
            return false;
        } else {
            if (sampleEncrypted || !this.playClearSamplesWithoutKeys) {
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onInputFormatChanged(MediaFormatHolder formatHolder2) throws ExoPlaybackException {
        boolean z = true;
        MediaFormat oldFormat = this.format;
        this.format = formatHolder2.format;
        this.drmInitData = formatHolder2.drmInitData;
        if (!Util.areEqual(this.format, oldFormat)) {
            if (this.codec != null && canReconfigureCodec(this.codec, this.codecIsAdaptive, oldFormat, this.format)) {
                this.codecReconfigured = true;
                this.codecReconfigurationState = 1;
                if (!(this.codecNeedsAdaptationWorkaround && this.format.width == oldFormat.width && this.format.height == oldFormat.height)) {
                    z = false;
                }
                this.codecNeedsAdaptationWorkaroundBuffer = z;
            } else if (this.codecReceivedBuffers) {
                this.codecReinitializationState = 1;
            } else {
                releaseCodec();
                maybeInitCodec();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onOutputFormatChanged(MediaCodec codec2, MediaFormat outputFormat) throws ExoPlaybackException {
    }

    /* access modifiers changed from: protected */
    public void onOutputStreamEnded() {
    }

    /* access modifiers changed from: protected */
    public void onQueuedInputBuffer(long presentationTimeUs, ByteBuffer buffer, int bufferSize, boolean sampleEncrypted) {
    }

    /* access modifiers changed from: protected */
    public void onProcessedOutputBuffer(long presentationTimeUs) {
    }

    /* access modifiers changed from: protected */
    public boolean canReconfigureCodec(MediaCodec codec2, boolean codecIsAdaptive2, MediaFormat oldFormat, MediaFormat newFormat) {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    /* access modifiers changed from: protected */
    public boolean isReady() {
        return this.format != null && !this.waitingForKeys && (this.sourceState != 0 || this.outputIndex >= 0 || isWithinHotswapPeriod());
    }

    /* access modifiers changed from: protected */
    public final int getSourceState() {
        return this.sourceState;
    }

    private boolean isWithinHotswapPeriod() {
        return SystemClock.elapsedRealtime() < this.codecHotswapTimeMs + 1000;
    }

    /* access modifiers changed from: protected */
    public long getDequeueOutputBufferTimeoutUs() {
        return 0;
    }

    private boolean drainOutputBuffer(long positionUs, long elapsedRealtimeUs) throws ExoPlaybackException {
        if (this.outputStreamEnded) {
            return false;
        }
        if (this.outputIndex < 0) {
            this.outputIndex = this.codec.dequeueOutputBuffer(this.outputBufferInfo, getDequeueOutputBufferTimeoutUs());
        }
        if (this.outputIndex == -2) {
            processOutputFormat();
            return true;
        } else if (this.outputIndex == -3) {
            this.outputBuffers = this.codec.getOutputBuffers();
            this.codecCounters.outputBuffersChangedCount++;
            return true;
        } else if (this.outputIndex < 0) {
            if (!this.codecNeedsEosPropagationWorkaround || (!this.inputStreamEnded && this.codecReinitializationState != 2)) {
                return false;
            }
            processEndOfStream();
            return true;
        } else if (this.shouldSkipAdaptationWorkaroundOutputBuffer) {
            this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
            this.codec.releaseOutputBuffer(this.outputIndex, false);
            this.outputIndex = -1;
            return true;
        } else if ((this.outputBufferInfo.flags & 4) != 0) {
            processEndOfStream();
            return false;
        } else {
            int decodeOnlyIndex = getDecodeOnlyIndex(this.outputBufferInfo.presentationTimeUs);
            if (!processOutputBuffer(positionUs, elapsedRealtimeUs, this.codec, this.outputBuffers[this.outputIndex], this.outputBufferInfo, this.outputIndex, decodeOnlyIndex != -1)) {
                return false;
            }
            onProcessedOutputBuffer(this.outputBufferInfo.presentationTimeUs);
            if (decodeOnlyIndex != -1) {
                this.decodeOnlyPresentationTimestamps.remove(decodeOnlyIndex);
            }
            this.outputIndex = -1;
            return true;
        }
    }

    private void processOutputFormat() throws ExoPlaybackException {
        MediaFormat format2 = this.codec.getOutputFormat();
        if (this.codecNeedsAdaptationWorkaround && format2.getInteger("width") == 32 && format2.getInteger("height") == 32) {
            this.shouldSkipAdaptationWorkaroundOutputBuffer = true;
            return;
        }
        if (this.codecNeedsMonoChannelCountWorkaround) {
            format2.setInteger("channel-count", 1);
        }
        onOutputFormatChanged(this.codec, format2);
        this.codecCounters.outputFormatChangedCount++;
    }

    private void processEndOfStream() throws ExoPlaybackException {
        if (this.codecReinitializationState == 2) {
            releaseCodec();
            maybeInitCodec();
            return;
        }
        this.outputStreamEnded = true;
        onOutputStreamEnded();
    }

    private void notifyDecoderInitializationError(final DecoderInitializationException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecTrackRenderer.this.eventListener.onDecoderInitializationError(e);
                }
            });
        }
    }

    private void notifyCryptoError(final MediaCodec.CryptoException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecTrackRenderer.this.eventListener.onCryptoError(e);
                }
            });
        }
    }

    private void notifyDecoderInitialized(String decoderName, long initializedTimestamp, long initializationDuration) {
        if (this.eventHandler != null && this.eventListener != null) {
            final String str = decoderName;
            final long j = initializedTimestamp;
            final long j2 = initializationDuration;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecTrackRenderer.this.eventListener.onDecoderInitialized(str, j, j2);
                }
            });
        }
    }

    private int getDecodeOnlyIndex(long presentationTimeUs) {
        int size = this.decodeOnlyPresentationTimestamps.size();
        for (int i = 0; i < size; i++) {
            if (this.decodeOnlyPresentationTimestamps.get(i).longValue() == presentationTimeUs) {
                return i;
            }
        }
        return -1;
    }

    private static boolean codecNeedsFlushWorkaround(String name) {
        return Util.SDK_INT < 18 || (Util.SDK_INT == 18 && ("OMX.SEC.avc.dec".equals(name) || "OMX.SEC.avc.dec.secure".equals(name))) || (Util.SDK_INT == 19 && Util.MODEL.startsWith("SM-G800") && ("OMX.Exynos.avc.dec".equals(name) || "OMX.Exynos.avc.dec.secure".equals(name)));
    }

    private static boolean codecNeedsAdaptationWorkaround(String name) {
        return Util.SDK_INT < 24 && ("OMX.Nvidia.h264.decode".equals(name) || "OMX.Nvidia.h264.decode.secure".equals(name)) && (Util.DEVICE.equals("flounder") || Util.DEVICE.equals("flounder_lte") || Util.DEVICE.equals("grouper") || Util.DEVICE.equals("tilapia"));
    }

    private static boolean codecNeedsDiscardToSpsWorkaround(String name, MediaFormat format2) {
        return Util.SDK_INT < 21 && format2.initializationData.isEmpty() && "OMX.MTK.VIDEO.DECODER.AVC".equals(name);
    }

    private static boolean codecNeedsEosPropagationWorkaround(String name) {
        return Util.SDK_INT <= 17 && ("OMX.rk.video_decoder.avc".equals(name) || "OMX.allwinner.video.decoder.avc".equals(name));
    }

    private static boolean codecNeedsEosFlushWorkaround(String name) {
        return Util.SDK_INT <= 23 && "OMX.google.vorbis.decoder".equals(name);
    }

    private static boolean codecNeedsMonoChannelCountWorkaround(String name, MediaFormat format2) {
        if (Util.SDK_INT > 18 || format2.channelCount != 1 || !"OMX.MTK.AUDIO.DECODER.MP3".equals(name)) {
            return false;
        }
        return true;
    }

    private static boolean deviceNeedsAutoFrcWorkaround() {
        return Util.SDK_INT <= 22 && "foster".equals(Util.DEVICE) && "NVIDIA".equals(Util.MANUFACTURER);
    }
}
