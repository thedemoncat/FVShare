package p012tv.danmaku.ijk.media.exo.demo.player;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.chunk.ChunkSampleSource;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.dash.DashChunkSource;
import com.google.android.exoplayer.dash.DefaultDashTrackSelector;
import com.google.android.exoplayer.dash.mpd.AdaptationSet;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;
import com.google.android.exoplayer.dash.mpd.Period;
import com.google.android.exoplayer.dash.mpd.UtcTimingElement;
import com.google.android.exoplayer.dash.mpd.UtcTimingElementResolver;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer.drm.MediaDrmCallback;
import com.google.android.exoplayer.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.text.TextRenderer;
import com.google.android.exoplayer.text.TextTrackRenderer;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.upstream.TransferListener;
import com.google.android.exoplayer.upstream.UriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.HashMap;
import p012tv.danmaku.ijk.media.exo.demo.player.DemoPlayer;

/* renamed from: tv.danmaku.ijk.media.exo.demo.player.DashRendererBuilder */
public class DashRendererBuilder implements DemoPlayer.RendererBuilder {
    private static final int AUDIO_BUFFER_SEGMENTS = 54;
    private static final int BUFFER_SEGMENT_SIZE = 65536;
    private static final int LIVE_EDGE_LATENCY_MS = 30000;
    private static final int SECURITY_LEVEL_1 = 1;
    private static final int SECURITY_LEVEL_3 = 3;
    private static final int SECURITY_LEVEL_UNKNOWN = -1;
    private static final String TAG = "DashRendererBuilder";
    private static final int TEXT_BUFFER_SEGMENTS = 2;
    private static final int VIDEO_BUFFER_SEGMENTS = 200;
    private final Context context;
    private AsyncRendererBuilder currentAsyncBuilder;
    private final MediaDrmCallback drmCallback;
    private final String url;
    private final String userAgent;

    public DashRendererBuilder(Context context2, String userAgent2, String url2, MediaDrmCallback drmCallback2) {
        this.context = context2;
        this.userAgent = userAgent2;
        this.url = url2;
        this.drmCallback = drmCallback2;
    }

    public void buildRenderers(DemoPlayer player) {
        this.currentAsyncBuilder = new AsyncRendererBuilder(this.context, this.userAgent, this.url, this.drmCallback, player);
        this.currentAsyncBuilder.init();
    }

    public void cancel() {
        if (this.currentAsyncBuilder != null) {
            this.currentAsyncBuilder.cancel();
            this.currentAsyncBuilder = null;
        }
    }

    /* renamed from: tv.danmaku.ijk.media.exo.demo.player.DashRendererBuilder$AsyncRendererBuilder */
    private static final class AsyncRendererBuilder implements ManifestFetcher.ManifestCallback<MediaPresentationDescription>, UtcTimingElementResolver.UtcTimingCallback {
        private boolean canceled;
        private final Context context;
        private final MediaDrmCallback drmCallback;
        private long elapsedRealtimeOffset;
        private MediaPresentationDescription manifest;
        private final UriDataSource manifestDataSource;
        private final ManifestFetcher<MediaPresentationDescription> manifestFetcher;
        private final DemoPlayer player;
        private final String userAgent;

        public AsyncRendererBuilder(Context context2, String userAgent2, String url, MediaDrmCallback drmCallback2, DemoPlayer player2) {
            this.context = context2;
            this.userAgent = userAgent2;
            this.drmCallback = drmCallback2;
            this.player = player2;
            MediaPresentationDescriptionParser parser = new MediaPresentationDescriptionParser();
            this.manifestDataSource = new DefaultUriDataSource(context2, userAgent2);
            this.manifestFetcher = new ManifestFetcher<>(url, this.manifestDataSource, parser);
        }

        public void init() {
            this.manifestFetcher.singleLoad(this.player.getMainHandler().getLooper(), this);
        }

        public void cancel() {
            this.canceled = true;
        }

        public void onSingleManifest(MediaPresentationDescription manifest2) {
            if (!this.canceled) {
                this.manifest = manifest2;
                if (!manifest2.dynamic || manifest2.utcTiming == null) {
                    buildRenderers();
                } else {
                    UtcTimingElementResolver.resolveTimingElement(this.manifestDataSource, manifest2.utcTiming, this.manifestFetcher.getManifestLoadCompleteTimestamp(), this);
                }
            }
        }

        public void onSingleManifestError(IOException e) {
            if (!this.canceled) {
                this.player.onRenderersError(e);
            }
        }

        public void onTimestampResolved(UtcTimingElement utcTiming, long elapsedRealtimeOffset2) {
            if (!this.canceled) {
                this.elapsedRealtimeOffset = elapsedRealtimeOffset2;
                buildRenderers();
            }
        }

        public void onTimestampError(UtcTimingElement utcTiming, IOException e) {
            if (!this.canceled) {
                Log.e(DashRendererBuilder.TAG, "Failed to resolve UtcTiming element [" + utcTiming + "]", e);
                buildRenderers();
            }
        }

        private void buildRenderers() {
            Period period = this.manifest.getPeriod(0);
            Handler mainHandler = this.player.getMainHandler();
            DefaultLoadControl defaultLoadControl = new DefaultLoadControl(new DefaultAllocator(65536));
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter(mainHandler, this.player);
            boolean hasContentProtection = false;
            for (int i = 0; i < period.adaptationSets.size(); i++) {
                AdaptationSet adaptationSet = period.adaptationSets.get(i);
                if (adaptationSet.type != -1) {
                    hasContentProtection |= adaptationSet.hasContentProtection();
                }
            }
            boolean filterHdContent = false;
            StreamingDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            if (hasContentProtection) {
                if (Util.SDK_INT < 18) {
                    this.player.onRenderersError(new UnsupportedDrmException(1));
                    return;
                }
                try {
                    drmSessionManager = StreamingDrmSessionManager.newWidevineInstance(this.player.getPlaybackLooper(), this.drmCallback, (HashMap<String, String>) null, this.player.getMainHandler(), this.player);
                    filterHdContent = getWidevineSecurityLevel(drmSessionManager) != 1;
                } catch (UnsupportedDrmException e) {
                    this.player.onRenderersError(e);
                    return;
                }
            }
            TrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(this.context, new ChunkSampleSource(new DashChunkSource(this.manifestFetcher, DefaultDashTrackSelector.newVideoInstance(this.context, true, filterHdContent), new DefaultUriDataSource(this.context, (TransferListener) defaultBandwidthMeter, this.userAgent), new FormatEvaluator.AdaptiveEvaluator(defaultBandwidthMeter), 30000, this.elapsedRealtimeOffset, mainHandler, this.player, 0), defaultLoadControl, 13107200, mainHandler, this.player, 0), MediaCodecSelector.DEFAULT, 1, 5000, drmSessionManager, true, mainHandler, this.player, 50);
            TrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer((SampleSource) new ChunkSampleSource(new DashChunkSource(this.manifestFetcher, DefaultDashTrackSelector.newAudioInstance(), new DefaultUriDataSource(this.context, (TransferListener) defaultBandwidthMeter, this.userAgent), (FormatEvaluator) null, 30000, this.elapsedRealtimeOffset, mainHandler, this.player, 1), defaultLoadControl, 3538944, mainHandler, this.player, 1), MediaCodecSelector.DEFAULT, (DrmSessionManager) drmSessionManager, true, mainHandler, (MediaCodecAudioTrackRenderer.EventListener) this.player, AudioCapabilities.getCapabilities(this.context), 3);
            TextTrackRenderer textTrackRenderer = new TextTrackRenderer((SampleSource) new ChunkSampleSource(new DashChunkSource(this.manifestFetcher, DefaultDashTrackSelector.newTextInstance(), new DefaultUriDataSource(this.context, (TransferListener) defaultBandwidthMeter, this.userAgent), (FormatEvaluator) null, 30000, this.elapsedRealtimeOffset, mainHandler, this.player, 2), defaultLoadControl, 131072, mainHandler, this.player, 2), (TextRenderer) this.player, mainHandler.getLooper(), new SubtitleParser[0]);
            TrackRenderer[] renderers = new TrackRenderer[4];
            renderers[0] = videoRenderer;
            renderers[1] = audioRenderer;
            renderers[2] = textTrackRenderer;
            this.player.onRenderers(renderers, defaultBandwidthMeter);
        }

        private static int getWidevineSecurityLevel(StreamingDrmSessionManager sessionManager) {
            String securityLevelProperty = sessionManager.getPropertyString("securityLevel");
            if (securityLevelProperty.equals("L1")) {
                return 1;
            }
            return securityLevelProperty.equals("L3") ? 3 : -1;
        }
    }
}
