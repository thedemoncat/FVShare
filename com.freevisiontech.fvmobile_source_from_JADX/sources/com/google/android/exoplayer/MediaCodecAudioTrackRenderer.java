package com.google.android.exoplayer;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.PlaybackParams;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Surface;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer.util.MimeTypes;
import java.nio.ByteBuffer;
import p012tv.danmaku.ijk.media.player.misc.IMediaFormat;

@TargetApi(16)
public class MediaCodecAudioTrackRenderer extends MediaCodecTrackRenderer implements MediaClock {
    public static final int MSG_SET_PLAYBACK_PARAMS = 2;
    public static final int MSG_SET_VOLUME = 1;
    private boolean allowPositionDiscontinuity;
    private int audioSessionId;
    private final AudioTrack audioTrack;
    private boolean audioTrackHasData;
    private long currentPositionUs;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    private long lastFeedElapsedRealtimeMs;
    private boolean passthroughEnabled;
    private MediaFormat passthroughMediaFormat;
    private int pcmEncoding;

    public interface EventListener extends MediaCodecTrackRenderer.EventListener {
        void onAudioTrackInitializationError(AudioTrack.InitializationException initializationException);

        void onAudioTrackUnderrun(int i, long j, long j2);

        void onAudioTrackWriteError(AudioTrack.WriteException writeException);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector) {
        this(source, mediaCodecSelector, (DrmSessionManager) null, true);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys) {
        this(source, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, (Handler) null, (EventListener) null);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector, Handler eventHandler, EventListener eventListener2) {
        this(source, mediaCodecSelector, (DrmSessionManager) null, true, eventHandler, eventListener2);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener2) {
        this(source, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener2, (AudioCapabilities) null, 3);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, MediaCodecSelector mediaCodecSelector, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener2, AudioCapabilities audioCapabilities, int streamType) {
        this(new SampleSource[]{source}, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener2, audioCapabilities, streamType);
    }

    public MediaCodecAudioTrackRenderer(SampleSource[] sources, MediaCodecSelector mediaCodecSelector, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener2, AudioCapabilities audioCapabilities, int streamType) {
        super(sources, mediaCodecSelector, (DrmSessionManager<FrameworkMediaCrypto>) drmSessionManager, playClearSamplesWithoutKeys, eventHandler, (MediaCodecTrackRenderer.EventListener) eventListener2);
        this.eventListener = eventListener2;
        this.audioSessionId = 0;
        this.audioTrack = new AudioTrack(audioCapabilities, streamType);
    }

    /* access modifiers changed from: protected */
    public boolean handlesTrack(MediaCodecSelector mediaCodecSelector, MediaFormat mediaFormat) throws MediaCodecUtil.DecoderQueryException {
        String mimeType = mediaFormat.mimeType;
        if (!MimeTypes.isAudio(mimeType)) {
            return false;
        }
        if (MimeTypes.AUDIO_UNKNOWN.equals(mimeType) || ((allowPassthrough(mimeType) && mediaCodecSelector.getPassthroughDecoderInfo() != null) || mediaCodecSelector.getDecoderInfo(mimeType, false) != null)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public DecoderInfo getDecoderInfo(MediaCodecSelector mediaCodecSelector, String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        DecoderInfo passthroughDecoderInfo;
        if (!allowPassthrough(mimeType) || (passthroughDecoderInfo = mediaCodecSelector.getPassthroughDecoderInfo()) == null) {
            this.passthroughEnabled = false;
            return super.getDecoderInfo(mediaCodecSelector, mimeType, requiresSecureDecoder);
        }
        this.passthroughEnabled = true;
        return passthroughDecoderInfo;
    }

    /* access modifiers changed from: protected */
    public boolean allowPassthrough(String mimeType) {
        return this.audioTrack.isPassthroughSupported(mimeType);
    }

    /* access modifiers changed from: protected */
    public void configureCodec(MediaCodec codec, boolean codecIsAdaptive, MediaFormat format, MediaCrypto crypto) {
        String mimeType = format.getString(IMediaFormat.KEY_MIME);
        if (this.passthroughEnabled) {
            format.setString(IMediaFormat.KEY_MIME, MimeTypes.AUDIO_RAW);
            codec.configure(format, (Surface) null, crypto, 0);
            format.setString(IMediaFormat.KEY_MIME, mimeType);
            this.passthroughMediaFormat = format;
            return;
        }
        codec.configure(format, (Surface) null, crypto, 0);
        this.passthroughMediaFormat = null;
    }

    /* access modifiers changed from: protected */
    public MediaClock getMediaClock() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void onInputFormatChanged(MediaFormatHolder holder) throws ExoPlaybackException {
        super.onInputFormatChanged(holder);
        this.pcmEncoding = MimeTypes.AUDIO_RAW.equals(holder.format.mimeType) ? holder.format.pcmEncoding : 2;
    }

    /* access modifiers changed from: protected */
    public void onOutputFormatChanged(MediaCodec codec, MediaFormat outputFormat) {
        MediaFormat format;
        boolean passthrough = this.passthroughMediaFormat != null;
        String mimeType = passthrough ? this.passthroughMediaFormat.getString(IMediaFormat.KEY_MIME) : MimeTypes.AUDIO_RAW;
        if (passthrough) {
            format = this.passthroughMediaFormat;
        } else {
            format = outputFormat;
        }
        this.audioTrack.configure(mimeType, format.getInteger("channel-count"), format.getInteger("sample-rate"), this.pcmEncoding);
    }

    /* access modifiers changed from: protected */
    public void onAudioSessionId(int audioSessionId2) {
    }

    /* access modifiers changed from: protected */
    public void onStarted() {
        super.onStarted();
        this.audioTrack.play();
    }

    /* access modifiers changed from: protected */
    public void onStopped() {
        this.audioTrack.pause();
        super.onStopped();
    }

    /* access modifiers changed from: protected */
    public boolean isEnded() {
        return super.isEnded() && !this.audioTrack.hasPendingData();
    }

    /* access modifiers changed from: protected */
    public boolean isReady() {
        return this.audioTrack.hasPendingData() || super.isReady();
    }

    public long getPositionUs() {
        long newCurrentPositionUs = this.audioTrack.getCurrentPositionUs(isEnded());
        if (newCurrentPositionUs != Long.MIN_VALUE) {
            if (!this.allowPositionDiscontinuity) {
                newCurrentPositionUs = Math.max(this.currentPositionUs, newCurrentPositionUs);
            }
            this.currentPositionUs = newCurrentPositionUs;
            this.allowPositionDiscontinuity = false;
        }
        return this.currentPositionUs;
    }

    /* access modifiers changed from: protected */
    public void onDisabled() throws ExoPlaybackException {
        this.audioSessionId = 0;
        try {
            this.audioTrack.release();
        } finally {
            super.onDisabled();
        }
    }

    /* access modifiers changed from: protected */
    public void onDiscontinuity(long positionUs) throws ExoPlaybackException {
        super.onDiscontinuity(positionUs);
        this.audioTrack.reset();
        this.currentPositionUs = positionUs;
        this.allowPositionDiscontinuity = true;
    }

    /* access modifiers changed from: protected */
    public boolean processOutputBuffer(long positionUs, long elapsedRealtimeUs, MediaCodec codec, ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo, int bufferIndex, boolean shouldSkip) throws ExoPlaybackException {
        if (this.passthroughEnabled && (bufferInfo.flags & 2) != 0) {
            codec.releaseOutputBuffer(bufferIndex, false);
            return true;
        } else if (shouldSkip) {
            codec.releaseOutputBuffer(bufferIndex, false);
            this.codecCounters.skippedOutputBufferCount++;
            this.audioTrack.handleDiscontinuity();
            return true;
        } else {
            if (!this.audioTrack.isInitialized()) {
                try {
                    if (this.audioSessionId != 0) {
                        this.audioTrack.initialize(this.audioSessionId);
                    } else {
                        this.audioSessionId = this.audioTrack.initialize();
                        onAudioSessionId(this.audioSessionId);
                    }
                    this.audioTrackHasData = false;
                    if (getState() == 3) {
                        this.audioTrack.play();
                    }
                } catch (AudioTrack.InitializationException e) {
                    notifyAudioTrackInitializationError(e);
                    throw new ExoPlaybackException((Throwable) e);
                }
            } else {
                boolean audioTrackHadData = this.audioTrackHasData;
                this.audioTrackHasData = this.audioTrack.hasPendingData();
                if (audioTrackHadData && !this.audioTrackHasData && getState() == 3) {
                    long elapsedSinceLastFeedMs = SystemClock.elapsedRealtime() - this.lastFeedElapsedRealtimeMs;
                    long bufferSizeUs = this.audioTrack.getBufferSizeUs();
                    notifyAudioTrackUnderrun(this.audioTrack.getBufferSize(), bufferSizeUs == -1 ? -1 : bufferSizeUs / 1000, elapsedSinceLastFeedMs);
                }
            }
            try {
                int handleBufferResult = this.audioTrack.handleBuffer(buffer, bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs);
                this.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
                if ((handleBufferResult & 1) != 0) {
                    handleAudioTrackDiscontinuity();
                    this.allowPositionDiscontinuity = true;
                }
                if ((handleBufferResult & 2) == 0) {
                    return false;
                }
                codec.releaseOutputBuffer(bufferIndex, false);
                this.codecCounters.renderedOutputBufferCount++;
                return true;
            } catch (AudioTrack.WriteException e2) {
                notifyAudioTrackWriteError(e2);
                throw new ExoPlaybackException((Throwable) e2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onOutputStreamEnded() {
        this.audioTrack.handleEndOfStream();
    }

    /* access modifiers changed from: protected */
    public void handleAudioTrackDiscontinuity() {
    }

    public void handleMessage(int messageType, Object message) throws ExoPlaybackException {
        switch (messageType) {
            case 1:
                this.audioTrack.setVolume(((Float) message).floatValue());
                return;
            case 2:
                this.audioTrack.setPlaybackParams((PlaybackParams) message);
                return;
            default:
                super.handleMessage(messageType, message);
                return;
        }
    }

    private void notifyAudioTrackInitializationError(final AudioTrack.InitializationException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackInitializationError(e);
                }
            });
        }
    }

    private void notifyAudioTrackWriteError(final AudioTrack.WriteException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackWriteError(e);
                }
            });
        }
    }

    private void notifyAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        if (this.eventHandler != null && this.eventListener != null) {
            final int i = bufferSize;
            final long j = bufferSizeMs;
            final long j2 = elapsedSinceLastFeedMs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackUnderrun(i, j, j2);
                }
            });
        }
    }
}
