package com.google.android.exoplayer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Surface;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.TraceUtil;
import com.google.android.exoplayer.util.Util;
import java.nio.ByteBuffer;
import p012tv.danmaku.ijk.media.player.misc.IMediaFormat;

@TargetApi(16)
public class MediaCodecVideoTrackRenderer extends MediaCodecTrackRenderer {
    private static final String KEY_CROP_BOTTOM = "crop-bottom";
    private static final String KEY_CROP_LEFT = "crop-left";
    private static final String KEY_CROP_RIGHT = "crop-right";
    private static final String KEY_CROP_TOP = "crop-top";
    public static final int MSG_SET_SURFACE = 1;
    private final long allowedJoiningTimeUs;
    private int consecutiveDroppedFrameCount;
    private int currentHeight;
    private float currentPixelWidthHeightRatio;
    private int currentUnappliedRotationDegrees;
    private int currentWidth;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrameCount;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    private final VideoFrameReleaseTimeHelper frameReleaseTimeHelper;
    private long joiningDeadlineUs;
    private int lastReportedHeight;
    private float lastReportedPixelWidthHeightRatio;
    private int lastReportedUnappliedRotationDegrees;
    private int lastReportedWidth;
    private final int maxDroppedFrameCountToNotify;
    private float pendingPixelWidthHeightRatio;
    private int pendingRotationDegrees;
    private boolean renderedFirstFrame;
    private boolean reportedDrawnToSurface;
    private Surface surface;
    private final int videoScalingMode;

    public interface EventListener extends MediaCodecTrackRenderer.EventListener {
        void onDrawnToSurface(Surface surface);

        void onDroppedFrames(int i, long j);

        void onVideoSizeChanged(int i, int i2, int i3, float f);
    }

    public MediaCodecVideoTrackRenderer(Context context, SampleSource source, MediaCodecSelector mediaCodecSelector, int videoScalingMode2) {
        this(context, source, mediaCodecSelector, videoScalingMode2, 0);
    }

    public MediaCodecVideoTrackRenderer(Context context, SampleSource source, MediaCodecSelector mediaCodecSelector, int videoScalingMode2, long allowedJoiningTimeMs) {
        this(context, source, mediaCodecSelector, videoScalingMode2, allowedJoiningTimeMs, (Handler) null, (EventListener) null, -1);
    }

    public MediaCodecVideoTrackRenderer(Context context, SampleSource source, MediaCodecSelector mediaCodecSelector, int videoScalingMode2, long allowedJoiningTimeMs, Handler eventHandler, EventListener eventListener2, int maxDroppedFrameCountToNotify2) {
        this(context, source, mediaCodecSelector, videoScalingMode2, allowedJoiningTimeMs, (DrmSessionManager<FrameworkMediaCrypto>) null, false, eventHandler, eventListener2, maxDroppedFrameCountToNotify2);
    }

    public MediaCodecVideoTrackRenderer(Context context, SampleSource source, MediaCodecSelector mediaCodecSelector, int videoScalingMode2, long allowedJoiningTimeMs, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener2, int maxDroppedFrameCountToNotify2) {
        super(source, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, (MediaCodecTrackRenderer.EventListener) eventListener2);
        this.frameReleaseTimeHelper = new VideoFrameReleaseTimeHelper(context);
        this.videoScalingMode = videoScalingMode2;
        this.allowedJoiningTimeUs = 1000 * allowedJoiningTimeMs;
        this.eventListener = eventListener2;
        this.maxDroppedFrameCountToNotify = maxDroppedFrameCountToNotify2;
        this.joiningDeadlineUs = -1;
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.lastReportedWidth = -1;
        this.lastReportedHeight = -1;
        this.lastReportedPixelWidthHeightRatio = -1.0f;
    }

    /* access modifiers changed from: protected */
    public boolean handlesTrack(MediaCodecSelector mediaCodecSelector, MediaFormat mediaFormat) throws MediaCodecUtil.DecoderQueryException {
        String mimeType = mediaFormat.mimeType;
        if (!MimeTypes.isVideo(mimeType)) {
            return false;
        }
        if (MimeTypes.VIDEO_UNKNOWN.equals(mimeType) || mediaCodecSelector.getDecoderInfo(mimeType, false) != null) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onEnabled(int track, long positionUs, boolean joining) throws ExoPlaybackException {
        super.onEnabled(track, positionUs, joining);
        if (joining && this.allowedJoiningTimeUs > 0) {
            this.joiningDeadlineUs = (SystemClock.elapsedRealtime() * 1000) + this.allowedJoiningTimeUs;
        }
        this.frameReleaseTimeHelper.enable();
    }

    /* access modifiers changed from: protected */
    public void onDiscontinuity(long positionUs) throws ExoPlaybackException {
        super.onDiscontinuity(positionUs);
        this.renderedFirstFrame = false;
        this.consecutiveDroppedFrameCount = 0;
        this.joiningDeadlineUs = -1;
    }

    /* access modifiers changed from: protected */
    public boolean isReady() {
        if (super.isReady() && (this.renderedFirstFrame || !codecInitialized() || getSourceState() == 2)) {
            this.joiningDeadlineUs = -1;
            return true;
        } else if (this.joiningDeadlineUs == -1) {
            return false;
        } else {
            if (SystemClock.elapsedRealtime() * 1000 < this.joiningDeadlineUs) {
                return true;
            }
            this.joiningDeadlineUs = -1;
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onStarted() {
        super.onStarted();
        this.droppedFrameCount = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
    }

    /* access modifiers changed from: protected */
    public void onStopped() {
        this.joiningDeadlineUs = -1;
        maybeNotifyDroppedFrameCount();
        super.onStopped();
    }

    /* access modifiers changed from: protected */
    public void onDisabled() throws ExoPlaybackException {
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.lastReportedWidth = -1;
        this.lastReportedHeight = -1;
        this.lastReportedPixelWidthHeightRatio = -1.0f;
        this.frameReleaseTimeHelper.disable();
        super.onDisabled();
    }

    public void handleMessage(int messageType, Object message) throws ExoPlaybackException {
        if (messageType == 1) {
            setSurface((Surface) message);
        } else {
            super.handleMessage(messageType, message);
        }
    }

    private void setSurface(Surface surface2) throws ExoPlaybackException {
        if (this.surface != surface2) {
            this.surface = surface2;
            this.reportedDrawnToSurface = false;
            int state = getState();
            if (state == 2 || state == 3) {
                releaseCodec();
                maybeInitCodec();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldInitCodec() {
        return super.shouldInitCodec() && this.surface != null && this.surface.isValid();
    }

    /* access modifiers changed from: protected */
    public void configureCodec(MediaCodec codec, boolean codecIsAdaptive, MediaFormat format, MediaCrypto crypto) {
        maybeSetMaxInputSize(format, codecIsAdaptive);
        codec.configure(format, this.surface, crypto, 0);
    }

    /* access modifiers changed from: protected */
    public void onInputFormatChanged(MediaFormatHolder holder) throws ExoPlaybackException {
        super.onInputFormatChanged(holder);
        this.pendingPixelWidthHeightRatio = holder.format.pixelWidthHeightRatio == -1.0f ? 1.0f : holder.format.pixelWidthHeightRatio;
        this.pendingRotationDegrees = holder.format.rotationDegrees == -1 ? 0 : holder.format.rotationDegrees;
    }

    /* access modifiers changed from: protected */
    public final boolean haveRenderedFirstFrame() {
        return this.renderedFirstFrame;
    }

    /* access modifiers changed from: protected */
    public void onOutputFormatChanged(MediaCodec codec, MediaFormat outputFormat) {
        int integer;
        int integer2;
        boolean hasCrop = outputFormat.containsKey(KEY_CROP_RIGHT) && outputFormat.containsKey(KEY_CROP_LEFT) && outputFormat.containsKey(KEY_CROP_BOTTOM) && outputFormat.containsKey(KEY_CROP_TOP);
        if (hasCrop) {
            integer = (outputFormat.getInteger(KEY_CROP_RIGHT) - outputFormat.getInteger(KEY_CROP_LEFT)) + 1;
        } else {
            integer = outputFormat.getInteger("width");
        }
        this.currentWidth = integer;
        if (hasCrop) {
            integer2 = (outputFormat.getInteger(KEY_CROP_BOTTOM) - outputFormat.getInteger(KEY_CROP_TOP)) + 1;
        } else {
            integer2 = outputFormat.getInteger("height");
        }
        this.currentHeight = integer2;
        this.currentPixelWidthHeightRatio = this.pendingPixelWidthHeightRatio;
        if (Util.SDK_INT < 21) {
            this.currentUnappliedRotationDegrees = this.pendingRotationDegrees;
        } else if (this.pendingRotationDegrees == 90 || this.pendingRotationDegrees == 270) {
            int rotatedHeight = this.currentWidth;
            this.currentWidth = this.currentHeight;
            this.currentHeight = rotatedHeight;
            this.currentPixelWidthHeightRatio = 1.0f / this.currentPixelWidthHeightRatio;
        }
        codec.setVideoScalingMode(this.videoScalingMode);
    }

    /* access modifiers changed from: protected */
    public boolean canReconfigureCodec(MediaCodec codec, boolean codecIsAdaptive, MediaFormat oldFormat, MediaFormat newFormat) {
        return newFormat.mimeType.equals(oldFormat.mimeType) && (codecIsAdaptive || (oldFormat.width == newFormat.width && oldFormat.height == newFormat.height));
    }

    /* access modifiers changed from: protected */
    public boolean processOutputBuffer(long positionUs, long elapsedRealtimeUs, MediaCodec codec, ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo, int bufferIndex, boolean shouldSkip) {
        if (shouldSkip) {
            skipOutputBuffer(codec, bufferIndex);
            this.consecutiveDroppedFrameCount = 0;
            return true;
        } else if (!this.renderedFirstFrame) {
            if (Util.SDK_INT >= 21) {
                renderOutputBufferV21(codec, bufferIndex, System.nanoTime());
            } else {
                renderOutputBuffer(codec, bufferIndex);
            }
            this.consecutiveDroppedFrameCount = 0;
            return true;
        } else if (getState() != 3) {
            return false;
        } else {
            long elapsedSinceStartOfLoopUs = (SystemClock.elapsedRealtime() * 1000) - elapsedRealtimeUs;
            long systemTimeNs = System.nanoTime();
            long adjustedReleaseTimeNs = this.frameReleaseTimeHelper.adjustReleaseTime(bufferInfo.presentationTimeUs, systemTimeNs + (1000 * ((bufferInfo.presentationTimeUs - positionUs) - elapsedSinceStartOfLoopUs)));
            long earlyUs = (adjustedReleaseTimeNs - systemTimeNs) / 1000;
            if (earlyUs < -30000) {
                dropOutputBuffer(codec, bufferIndex);
                return true;
            }
            if (Util.SDK_INT >= 21) {
                if (earlyUs < 50000) {
                    renderOutputBufferV21(codec, bufferIndex, adjustedReleaseTimeNs);
                    this.consecutiveDroppedFrameCount = 0;
                    return true;
                }
            } else if (earlyUs < 30000) {
                if (earlyUs > 11000) {
                    try {
                        Thread.sleep((earlyUs - 10000) / 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                renderOutputBuffer(codec, bufferIndex);
                this.consecutiveDroppedFrameCount = 0;
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void skipOutputBuffer(MediaCodec codec, int bufferIndex) {
        TraceUtil.beginSection("skipVideoBuffer");
        codec.releaseOutputBuffer(bufferIndex, false);
        TraceUtil.endSection();
        this.codecCounters.skippedOutputBufferCount++;
    }

    /* access modifiers changed from: protected */
    public void dropOutputBuffer(MediaCodec codec, int bufferIndex) {
        TraceUtil.beginSection("dropVideoBuffer");
        codec.releaseOutputBuffer(bufferIndex, false);
        TraceUtil.endSection();
        this.codecCounters.droppedOutputBufferCount++;
        this.droppedFrameCount++;
        this.consecutiveDroppedFrameCount++;
        this.codecCounters.maxConsecutiveDroppedOutputBufferCount = Math.max(this.consecutiveDroppedFrameCount, this.codecCounters.maxConsecutiveDroppedOutputBufferCount);
        if (this.droppedFrameCount == this.maxDroppedFrameCountToNotify) {
            maybeNotifyDroppedFrameCount();
        }
    }

    /* access modifiers changed from: protected */
    public void renderOutputBuffer(MediaCodec codec, int bufferIndex) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        codec.releaseOutputBuffer(bufferIndex, true);
        TraceUtil.endSection();
        this.codecCounters.renderedOutputBufferCount++;
        this.renderedFirstFrame = true;
        maybeNotifyDrawnToSurface();
    }

    /* access modifiers changed from: protected */
    @TargetApi(21)
    public void renderOutputBufferV21(MediaCodec codec, int bufferIndex, long releaseTimeNs) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        codec.releaseOutputBuffer(bufferIndex, releaseTimeNs);
        TraceUtil.endSection();
        this.codecCounters.renderedOutputBufferCount++;
        this.renderedFirstFrame = true;
        maybeNotifyDrawnToSurface();
    }

    @SuppressLint({"InlinedApi"})
    private void maybeSetMaxInputSize(MediaFormat format, boolean codecIsAdaptive) {
        int maxPixels;
        int minCompressionRatio;
        if (!format.containsKey("max-input-size")) {
            int maxHeight = format.getInteger("height");
            if (codecIsAdaptive && format.containsKey("max-height")) {
                maxHeight = Math.max(maxHeight, format.getInteger("max-height"));
            }
            int maxWidth = format.getInteger("width");
            if (codecIsAdaptive && format.containsKey("max-width")) {
                maxWidth = Math.max(maxHeight, format.getInteger("max-width"));
            }
            String string = format.getString(IMediaFormat.KEY_MIME);
            char c = 65535;
            switch (string.hashCode()) {
                case -1664118616:
                    if (string.equals(MimeTypes.VIDEO_H263)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1662541442:
                    if (string.equals(MimeTypes.VIDEO_H265)) {
                        c = 4;
                        break;
                    }
                    break;
                case 1187890754:
                    if (string.equals(MimeTypes.VIDEO_MP4V)) {
                        c = 1;
                        break;
                    }
                    break;
                case 1331836730:
                    if (string.equals(MimeTypes.VIDEO_H264)) {
                        c = 2;
                        break;
                    }
                    break;
                case 1599127256:
                    if (string.equals(MimeTypes.VIDEO_VP8)) {
                        c = 3;
                        break;
                    }
                    break;
                case 1599127257:
                    if (string.equals(MimeTypes.VIDEO_VP9)) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                    maxPixels = maxWidth * maxHeight;
                    minCompressionRatio = 2;
                    break;
                case 2:
                    if (!"BRAVIA 4K 2015".equals(Util.MODEL)) {
                        maxPixels = ((maxWidth + 15) / 16) * ((maxHeight + 15) / 16) * 16 * 16;
                        minCompressionRatio = 2;
                        break;
                    } else {
                        return;
                    }
                case 3:
                    maxPixels = maxWidth * maxHeight;
                    minCompressionRatio = 2;
                    break;
                case 4:
                case 5:
                    maxPixels = maxWidth * maxHeight;
                    minCompressionRatio = 4;
                    break;
                default:
                    return;
            }
            format.setInteger("max-input-size", (maxPixels * 3) / (minCompressionRatio * 2));
        }
    }

    private void maybeNotifyVideoSizeChanged() {
        if (this.eventHandler != null && this.eventListener != null) {
            if (this.lastReportedWidth != this.currentWidth || this.lastReportedHeight != this.currentHeight || this.lastReportedUnappliedRotationDegrees != this.currentUnappliedRotationDegrees || this.lastReportedPixelWidthHeightRatio != this.currentPixelWidthHeightRatio) {
                final int currentWidth2 = this.currentWidth;
                final int currentHeight2 = this.currentHeight;
                final int currentUnappliedRotationDegrees2 = this.currentUnappliedRotationDegrees;
                final float currentPixelWidthHeightRatio2 = this.currentPixelWidthHeightRatio;
                this.eventHandler.post(new Runnable() {
                    public void run() {
                        MediaCodecVideoTrackRenderer.this.eventListener.onVideoSizeChanged(currentWidth2, currentHeight2, currentUnappliedRotationDegrees2, currentPixelWidthHeightRatio2);
                    }
                });
                this.lastReportedWidth = currentWidth2;
                this.lastReportedHeight = currentHeight2;
                this.lastReportedUnappliedRotationDegrees = currentUnappliedRotationDegrees2;
                this.lastReportedPixelWidthHeightRatio = currentPixelWidthHeightRatio2;
            }
        }
    }

    private void maybeNotifyDrawnToSurface() {
        if (this.eventHandler != null && this.eventListener != null && !this.reportedDrawnToSurface) {
            final Surface surface2 = this.surface;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecVideoTrackRenderer.this.eventListener.onDrawnToSurface(surface2);
                }
            });
            this.reportedDrawnToSurface = true;
        }
    }

    private void maybeNotifyDroppedFrameCount() {
        if (this.eventHandler != null && this.eventListener != null && this.droppedFrameCount != 0) {
            long now = SystemClock.elapsedRealtime();
            final int countToNotify = this.droppedFrameCount;
            final long elapsedToNotify = now - this.droppedFrameAccumulationStartTimeMs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecVideoTrackRenderer.this.eventListener.onDroppedFrames(countToNotify, elapsedToNotify);
                }
            });
            this.droppedFrameCount = 0;
            this.droppedFrameAccumulationStartTimeMs = now;
        }
    }
}
