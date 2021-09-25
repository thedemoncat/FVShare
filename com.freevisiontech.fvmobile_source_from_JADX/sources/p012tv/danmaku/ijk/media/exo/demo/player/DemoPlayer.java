package p012tv.danmaku.ijk.media.exo.demo.player;

import android.media.MediaCodec;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import com.google.android.exoplayer.CodecCounters;
import com.google.android.exoplayer.DummyTrackRenderer;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.SingleSampleSource;
import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.chunk.ChunkSampleSource;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.dash.DashChunkSource;
import com.google.android.exoplayer.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.metadata.MetadataTrackRenderer;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.TextRenderer;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.util.DebugTextViewHelper;
import com.google.android.exoplayer.util.PlayerControl;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: tv.danmaku.ijk.media.exo.demo.player.DemoPlayer */
public class DemoPlayer implements ExoPlayer.Listener, ChunkSampleSource.EventListener, HlsSampleSource.EventListener, ExtractorSampleSource.EventListener, SingleSampleSource.EventListener, BandwidthMeter.EventListener, MediaCodecVideoTrackRenderer.EventListener, MediaCodecAudioTrackRenderer.EventListener, StreamingDrmSessionManager.EventListener, DashChunkSource.EventListener, TextRenderer, MetadataTrackRenderer.MetadataRenderer<List<Id3Frame>>, DebugTextViewHelper.Provider {
    private static final int RENDERER_BUILDING_STATE_BUILDING = 2;
    private static final int RENDERER_BUILDING_STATE_BUILT = 3;
    private static final int RENDERER_BUILDING_STATE_IDLE = 1;
    public static final int RENDERER_COUNT = 4;
    public static final int STATE_BUFFERING = 3;
    public static final int STATE_ENDED = 5;
    public static final int STATE_IDLE = 1;
    public static final int STATE_PREPARING = 2;
    public static final int STATE_READY = 4;
    public static final int TRACK_DEFAULT = 0;
    public static final int TRACK_DISABLED = -1;
    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_METADATA = 3;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_VIDEO = 0;
    private boolean backgrounded;
    private BandwidthMeter bandwidthMeter;
    private CaptionListener captionListener;
    private CodecCounters codecCounters;
    private Id3MetadataListener id3MetadataListener;
    private InfoListener infoListener;
    private InternalErrorListener internalErrorListener;
    private boolean lastReportedPlayWhenReady;
    private int lastReportedPlaybackState;
    private final CopyOnWriteArrayList<Listener> listeners;
    private final Handler mainHandler;
    private final ExoPlayer player = ExoPlayer.Factory.newInstance(4, 1000, 5000);
    private final PlayerControl playerControl;
    private final RendererBuilder rendererBuilder;
    private int rendererBuildingState;
    private Surface surface;
    private Format videoFormat;
    private TrackRenderer videoRenderer;
    private int videoTrackToRestore;

    /* renamed from: tv.danmaku.ijk.media.exo.demo.player.DemoPlayer$CaptionListener */
    public interface CaptionListener {
        void onCues(List<Cue> list);
    }

    /* renamed from: tv.danmaku.ijk.media.exo.demo.player.DemoPlayer$Id3MetadataListener */
    public interface Id3MetadataListener {
        void onId3Metadata(List<Id3Frame> list);
    }

    /* renamed from: tv.danmaku.ijk.media.exo.demo.player.DemoPlayer$InfoListener */
    public interface InfoListener {
        void onAudioFormatEnabled(Format format, int i, long j);

        void onAvailableRangeChanged(int i, TimeRange timeRange);

        void onBandwidthSample(int i, long j, long j2);

        void onDecoderInitialized(String str, long j, long j2);

        void onDroppedFrames(int i, long j);

        void onLoadCompleted(int i, long j, int i2, int i3, Format format, long j2, long j3, long j4, long j5);

        void onLoadStarted(int i, long j, int i2, int i3, Format format, long j2, long j3);

        void onVideoFormatEnabled(Format format, int i, long j);
    }

    /* renamed from: tv.danmaku.ijk.media.exo.demo.player.DemoPlayer$InternalErrorListener */
    public interface InternalErrorListener {
        void onAudioTrackInitializationError(AudioTrack.InitializationException initializationException);

        void onAudioTrackUnderrun(int i, long j, long j2);

        void onAudioTrackWriteError(AudioTrack.WriteException writeException);

        void onCryptoError(MediaCodec.CryptoException cryptoException);

        void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException decoderInitializationException);

        void onDrmSessionManagerError(Exception exc);

        void onLoadError(int i, IOException iOException);

        void onRendererInitializationError(Exception exc);
    }

    /* renamed from: tv.danmaku.ijk.media.exo.demo.player.DemoPlayer$Listener */
    public interface Listener {
        void onError(Exception exc);

        void onStateChanged(boolean z, int i);

        void onVideoSizeChanged(int i, int i2, int i3, float f);
    }

    /* renamed from: tv.danmaku.ijk.media.exo.demo.player.DemoPlayer$RendererBuilder */
    public interface RendererBuilder {
        void buildRenderers(DemoPlayer demoPlayer);

        void cancel();
    }

    public DemoPlayer(RendererBuilder rendererBuilder2) {
        this.rendererBuilder = rendererBuilder2;
        this.player.addListener(this);
        this.playerControl = new PlayerControl(this.player);
        this.mainHandler = new Handler();
        this.listeners = new CopyOnWriteArrayList<>();
        this.lastReportedPlaybackState = 1;
        this.rendererBuildingState = 1;
        this.player.setSelectedTrack(2, -1);
    }

    public PlayerControl getPlayerControl() {
        return this.playerControl;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public void setInternalErrorListener(InternalErrorListener listener) {
        this.internalErrorListener = listener;
    }

    public void setInfoListener(InfoListener listener) {
        this.infoListener = listener;
    }

    public void setCaptionListener(CaptionListener listener) {
        this.captionListener = listener;
    }

    public void setMetadataListener(Id3MetadataListener listener) {
        this.id3MetadataListener = listener;
    }

    public void setSurface(Surface surface2) {
        this.surface = surface2;
        pushSurface(false);
    }

    public Surface getSurface() {
        return this.surface;
    }

    public void blockingClearSurface() {
        this.surface = null;
        pushSurface(true);
    }

    public int getTrackCount(int type) {
        return this.player.getTrackCount(type);
    }

    public MediaFormat getTrackFormat(int type, int index) {
        return this.player.getTrackFormat(type, index);
    }

    public int getSelectedTrack(int type) {
        return this.player.getSelectedTrack(type);
    }

    public void setSelectedTrack(int type, int index) {
        this.player.setSelectedTrack(type, index);
        if (type == 2 && index < 0 && this.captionListener != null) {
            this.captionListener.onCues(Collections.emptyList());
        }
    }

    public boolean getBackgrounded() {
        return this.backgrounded;
    }

    public void setBackgrounded(boolean backgrounded2) {
        if (this.backgrounded != backgrounded2) {
            this.backgrounded = backgrounded2;
            if (backgrounded2) {
                this.videoTrackToRestore = getSelectedTrack(0);
                setSelectedTrack(0, -1);
                blockingClearSurface();
                return;
            }
            setSelectedTrack(0, this.videoTrackToRestore);
        }
    }

    public void prepare() {
        if (this.rendererBuildingState == 3) {
            this.player.stop();
        }
        this.rendererBuilder.cancel();
        this.videoFormat = null;
        this.videoRenderer = null;
        this.rendererBuildingState = 2;
        maybeReportPlayerState();
        this.rendererBuilder.buildRenderers(this);
    }

    /* access modifiers changed from: package-private */
    public void onRenderers(TrackRenderer[] renderers, BandwidthMeter bandwidthMeter2) {
        for (int i = 0; i < 4; i++) {
            if (renderers[i] == null) {
                renderers[i] = new DummyTrackRenderer();
            }
        }
        this.videoRenderer = renderers[0];
        this.codecCounters = this.videoRenderer instanceof MediaCodecTrackRenderer ? ((MediaCodecTrackRenderer) this.videoRenderer).codecCounters : renderers[1] instanceof MediaCodecTrackRenderer ? renderers[1].codecCounters : null;
        this.bandwidthMeter = bandwidthMeter2;
        pushSurface(false);
        this.player.prepare(renderers);
        this.rendererBuildingState = 3;
    }

    /* access modifiers changed from: package-private */
    public void onRenderersError(Exception e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onRendererInitializationError(e);
        }
        Iterator<Listener> it = this.listeners.iterator();
        while (it.hasNext()) {
            it.next().onError(e);
        }
        this.rendererBuildingState = 1;
        maybeReportPlayerState();
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.player.setPlayWhenReady(playWhenReady);
    }

    public void seekTo(long positionMs) {
        this.player.seekTo(positionMs);
    }

    public void release() {
        this.rendererBuilder.cancel();
        this.rendererBuildingState = 1;
        this.surface = null;
        this.player.release();
    }

    public int getPlaybackState() {
        if (this.rendererBuildingState == 2) {
            return 2;
        }
        int playerState = this.player.getPlaybackState();
        if (this.rendererBuildingState == 3 && playerState == 1) {
            return 2;
        }
        return playerState;
    }

    public Format getFormat() {
        return this.videoFormat;
    }

    public BandwidthMeter getBandwidthMeter() {
        return this.bandwidthMeter;
    }

    public CodecCounters getCodecCounters() {
        return this.codecCounters;
    }

    public long getCurrentPosition() {
        return this.player.getCurrentPosition();
    }

    public long getDuration() {
        return this.player.getDuration();
    }

    public int getBufferedPercentage() {
        return this.player.getBufferedPercentage();
    }

    public boolean getPlayWhenReady() {
        return this.player.getPlayWhenReady();
    }

    /* access modifiers changed from: package-private */
    public Looper getPlaybackLooper() {
        return this.player.getPlaybackLooper();
    }

    /* access modifiers changed from: package-private */
    public Handler getMainHandler() {
        return this.mainHandler;
    }

    public void onPlayerStateChanged(boolean playWhenReady, int state) {
        maybeReportPlayerState();
    }

    public void onPlayerError(ExoPlaybackException exception) {
        this.rendererBuildingState = 1;
        Iterator<Listener> it = this.listeners.iterator();
        while (it.hasNext()) {
            it.next().onError(exception);
        }
    }

    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Iterator<Listener> it = this.listeners.iterator();
        while (it.hasNext()) {
            it.next().onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    public void onDroppedFrames(int count, long elapsed) {
        if (this.infoListener != null) {
            this.infoListener.onDroppedFrames(count, elapsed);
        }
    }

    public void onBandwidthSample(int elapsedMs, long bytes, long bitrateEstimate) {
        if (this.infoListener != null) {
            this.infoListener.onBandwidthSample(elapsedMs, bytes, bitrateEstimate);
        }
    }

    public void onDownstreamFormatChanged(int sourceId, Format format, int trigger, long mediaTimeMs) {
        if (this.infoListener != null) {
            if (sourceId == 0) {
                this.videoFormat = format;
                this.infoListener.onVideoFormatEnabled(format, trigger, mediaTimeMs);
            } else if (sourceId == 1) {
                this.infoListener.onAudioFormatEnabled(format, trigger, mediaTimeMs);
            }
        }
    }

    public void onDrmKeysLoaded() {
    }

    public void onDrmSessionManagerError(Exception e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onDrmSessionManagerError(e);
        }
    }

    public void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onDecoderInitializationError(e);
        }
    }

    public void onAudioTrackInitializationError(AudioTrack.InitializationException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onAudioTrackInitializationError(e);
        }
    }

    public void onAudioTrackWriteError(AudioTrack.WriteException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onAudioTrackWriteError(e);
        }
    }

    public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onAudioTrackUnderrun(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
        }
    }

    public void onCryptoError(MediaCodec.CryptoException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onCryptoError(e);
        }
    }

    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
        if (this.infoListener != null) {
            this.infoListener.onDecoderInitialized(decoderName, elapsedRealtimeMs, initializationDurationMs);
        }
    }

    public void onLoadError(int sourceId, IOException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onLoadError(sourceId, e);
        }
    }

    public void onCues(List<Cue> cues) {
        if (this.captionListener != null && getSelectedTrack(2) != -1) {
            this.captionListener.onCues(cues);
        }
    }

    public void onMetadata(List<Id3Frame> id3Frames) {
        if (this.id3MetadataListener != null && getSelectedTrack(3) != -1) {
            this.id3MetadataListener.onId3Metadata(id3Frames);
        }
    }

    public void onAvailableRangeChanged(int sourceId, TimeRange availableRange) {
        if (this.infoListener != null) {
            this.infoListener.onAvailableRangeChanged(sourceId, availableRange);
        }
    }

    public void onPlayWhenReadyCommitted() {
    }

    public void onDrawnToSurface(Surface surface2) {
    }

    public void onLoadStarted(int sourceId, long length, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
        if (this.infoListener != null) {
            this.infoListener.onLoadStarted(sourceId, length, type, trigger, format, mediaStartTimeMs, mediaEndTimeMs);
        }
    }

    public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
        if (this.infoListener != null) {
            this.infoListener.onLoadCompleted(sourceId, bytesLoaded, type, trigger, format, mediaStartTimeMs, mediaEndTimeMs, elapsedRealtimeMs, loadDurationMs);
        }
    }

    public void onLoadCanceled(int sourceId, long bytesLoaded) {
    }

    public void onUpstreamDiscarded(int sourceId, long mediaStartTimeMs, long mediaEndTimeMs) {
    }

    private void maybeReportPlayerState() {
        boolean playWhenReady = this.player.getPlayWhenReady();
        int playbackState = getPlaybackState();
        if (this.lastReportedPlayWhenReady != playWhenReady || this.lastReportedPlaybackState != playbackState) {
            Iterator<Listener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().onStateChanged(playWhenReady, playbackState);
            }
            this.lastReportedPlayWhenReady = playWhenReady;
            this.lastReportedPlaybackState = playbackState;
        }
    }

    private void pushSurface(boolean blockForSurfacePush) {
        if (this.videoRenderer != null) {
            if (blockForSurfacePush) {
                this.player.blockingSendMessage(this.videoRenderer, 1, this.surface);
            } else {
                this.player.sendMessage(this.videoRenderer, 1, this.surface);
            }
        }
    }
}
