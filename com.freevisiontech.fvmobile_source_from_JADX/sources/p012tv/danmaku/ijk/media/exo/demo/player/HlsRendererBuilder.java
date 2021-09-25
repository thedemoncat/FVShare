package p012tv.danmaku.ijk.media.exo.demo.player;

import android.content.Context;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import java.io.IOException;
import p012tv.danmaku.ijk.media.exo.demo.player.DemoPlayer;

/* renamed from: tv.danmaku.ijk.media.exo.demo.player.HlsRendererBuilder */
public class HlsRendererBuilder implements DemoPlayer.RendererBuilder {
    private static final int AUDIO_BUFFER_SEGMENTS = 54;
    private static final int BUFFER_SEGMENT_SIZE = 65536;
    private static final int MAIN_BUFFER_SEGMENTS = 254;
    private static final int TEXT_BUFFER_SEGMENTS = 2;
    private final Context context;
    private AsyncRendererBuilder currentAsyncBuilder;
    private final String url;
    private final String userAgent;

    public HlsRendererBuilder(Context context2, String userAgent2, String url2) {
        this.context = context2;
        this.userAgent = userAgent2;
        this.url = url2;
    }

    public void buildRenderers(DemoPlayer player) {
        this.currentAsyncBuilder = new AsyncRendererBuilder(this.context, this.userAgent, this.url, player);
        this.currentAsyncBuilder.init();
    }

    public void cancel() {
        if (this.currentAsyncBuilder != null) {
            this.currentAsyncBuilder.cancel();
            this.currentAsyncBuilder = null;
        }
    }

    /* renamed from: tv.danmaku.ijk.media.exo.demo.player.HlsRendererBuilder$AsyncRendererBuilder */
    private static final class AsyncRendererBuilder implements ManifestFetcher.ManifestCallback<HlsPlaylist> {
        private boolean canceled;
        private final Context context;
        private final DemoPlayer player;
        private final ManifestFetcher<HlsPlaylist> playlistFetcher;
        private final String userAgent;

        public AsyncRendererBuilder(Context context2, String userAgent2, String url, DemoPlayer player2) {
            this.context = context2;
            this.userAgent = userAgent2;
            this.player = player2;
            this.playlistFetcher = new ManifestFetcher<>(url, new DefaultUriDataSource(context2, userAgent2), new HlsPlaylistParser());
        }

        public void init() {
            this.playlistFetcher.singleLoad(this.player.getMainHandler().getLooper(), this);
        }

        public void cancel() {
            this.canceled = true;
        }

        public void onSingleManifestError(IOException e) {
            if (!this.canceled) {
                this.player.onRenderersError(e);
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r35v0, resolved type: com.google.android.exoplayer.TrackRenderer[]} */
        /* JADX WARNING: type inference failed for: r36v1 */
        /* JADX WARNING: type inference failed for: r0v41, types: [com.google.android.exoplayer.text.eia608.Eia608TrackRenderer] */
        /* JADX WARNING: type inference failed for: r0v42, types: [com.google.android.exoplayer.text.TextTrackRenderer] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onSingleManifest(com.google.android.exoplayer.hls.HlsPlaylist r38) {
            /*
                r37 = this;
                r0 = r37
                boolean r3 = r0.canceled
                if (r3 == 0) goto L_0x0007
            L_0x0006:
                return
            L_0x0007:
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r3 = r0.player
                android.os.Handler r13 = r3.getMainHandler()
                com.google.android.exoplayer.DefaultLoadControl r11 = new com.google.android.exoplayer.DefaultLoadControl
                com.google.android.exoplayer.upstream.DefaultAllocator r3 = new com.google.android.exoplayer.upstream.DefaultAllocator
                r5 = 65536(0x10000, float:9.18355E-41)
                r3.<init>(r5)
                r11.<init>(r3)
                com.google.android.exoplayer.upstream.DefaultBandwidthMeter r7 = new com.google.android.exoplayer.upstream.DefaultBandwidthMeter
                r7.<init>()
                com.google.android.exoplayer.hls.PtsTimestampAdjusterProvider r8 = new com.google.android.exoplayer.hls.PtsTimestampAdjusterProvider
                r8.<init>()
                r32 = 0
                r31 = 0
                r0 = r38
                boolean r3 = r0 instanceof com.google.android.exoplayer.hls.HlsMasterPlaylist
                if (r3 == 0) goto L_0x004b
                r34 = r38
                com.google.android.exoplayer.hls.HlsMasterPlaylist r34 = (com.google.android.exoplayer.hls.HlsMasterPlaylist) r34
                r0 = r34
                java.util.List<com.google.android.exoplayer.hls.Variant> r3 = r0.subtitles
                boolean r3 = r3.isEmpty()
                if (r3 != 0) goto L_0x0172
                r32 = 1
            L_0x003f:
                r0 = r34
                java.util.List<com.google.android.exoplayer.hls.Variant> r3 = r0.audios
                boolean r3 = r3.isEmpty()
                if (r3 != 0) goto L_0x0176
                r31 = 1
            L_0x004b:
                com.google.android.exoplayer.upstream.DefaultUriDataSource r4 = new com.google.android.exoplayer.upstream.DefaultUriDataSource
                r0 = r37
                android.content.Context r3 = r0.context
                r0 = r37
                java.lang.String r5 = r0.userAgent
                r4.<init>((android.content.Context) r3, (com.google.android.exoplayer.upstream.TransferListener) r7, (java.lang.String) r5)
                com.google.android.exoplayer.hls.HlsChunkSource r2 = new com.google.android.exoplayer.hls.HlsChunkSource
                r3 = 1
                r0 = r37
                android.content.Context r5 = r0.context
                com.google.android.exoplayer.hls.DefaultHlsTrackSelector r6 = com.google.android.exoplayer.hls.DefaultHlsTrackSelector.newDefaultInstance(r5)
                r5 = r38
                r2.<init>(r3, r4, r5, r6, r7, r8)
                com.google.android.exoplayer.hls.HlsSampleSource r9 = new com.google.android.exoplayer.hls.HlsSampleSource
                r12 = 16646144(0xfe0000, float:2.3326216E-38)
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r14 = r0.player
                r15 = 0
                r10 = r2
                r9.<init>(r10, r11, r12, r13, r14, r15)
                com.google.android.exoplayer.MediaCodecVideoTrackRenderer r15 = new com.google.android.exoplayer.MediaCodecVideoTrackRenderer
                r0 = r37
                android.content.Context r0 = r0.context
                r16 = r0
                com.google.android.exoplayer.MediaCodecSelector r18 = com.google.android.exoplayer.MediaCodecSelector.DEFAULT
                r19 = 1
                r20 = 5000(0x1388, double:2.4703E-320)
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r0 = r0.player
                r23 = r0
                r24 = 50
                r17 = r9
                r22 = r13
                r15.<init>(r16, r17, r18, r19, r20, r22, r23, r24)
                com.google.android.exoplayer.metadata.MetadataTrackRenderer r33 = new com.google.android.exoplayer.metadata.MetadataTrackRenderer
                com.google.android.exoplayer.metadata.id3.Id3Parser r3 = new com.google.android.exoplayer.metadata.id3.Id3Parser
                r3.<init>()
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r5 = r0.player
                android.os.Looper r6 = r13.getLooper()
                r0 = r33
                r0.<init>(r9, r3, r5, r6)
                if (r31 == 0) goto L_0x017a
                com.google.android.exoplayer.upstream.DefaultUriDataSource r18 = new com.google.android.exoplayer.upstream.DefaultUriDataSource
                r0 = r37
                android.content.Context r3 = r0.context
                r0 = r37
                java.lang.String r5 = r0.userAgent
                r0 = r18
                r0.<init>((android.content.Context) r3, (com.google.android.exoplayer.upstream.TransferListener) r7, (java.lang.String) r5)
                com.google.android.exoplayer.hls.HlsChunkSource r16 = new com.google.android.exoplayer.hls.HlsChunkSource
                r17 = 0
                com.google.android.exoplayer.hls.DefaultHlsTrackSelector r20 = com.google.android.exoplayer.hls.DefaultHlsTrackSelector.newAudioInstance()
                r19 = r38
                r21 = r7
                r22 = r8
                r16.<init>(r17, r18, r19, r20, r21, r22)
                com.google.android.exoplayer.hls.HlsSampleSource r19 = new com.google.android.exoplayer.hls.HlsSampleSource
                r22 = 3538944(0x360000, float:4.959117E-39)
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r0 = r0.player
                r24 = r0
                r25 = 1
                r20 = r16
                r21 = r11
                r23 = r13
                r19.<init>(r20, r21, r22, r23, r24, r25)
                com.google.android.exoplayer.MediaCodecAudioTrackRenderer r20 = new com.google.android.exoplayer.MediaCodecAudioTrackRenderer
                r3 = 2
                com.google.android.exoplayer.SampleSource[] r0 = new com.google.android.exoplayer.SampleSource[r3]
                r21 = r0
                r3 = 0
                r21[r3] = r9
                r3 = 1
                r21[r3] = r19
                com.google.android.exoplayer.MediaCodecSelector r22 = com.google.android.exoplayer.MediaCodecSelector.DEFAULT
                r23 = 0
                r24 = 1
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r3 = r0.player
                android.os.Handler r25 = r3.getMainHandler()
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r0 = r0.player
                r26 = r0
                r0 = r37
                android.content.Context r3 = r0.context
                com.google.android.exoplayer.audio.AudioCapabilities r27 = com.google.android.exoplayer.audio.AudioCapabilities.getCapabilities((android.content.Context) r3)
                r28 = 3
                r20.<init>((com.google.android.exoplayer.SampleSource[]) r21, (com.google.android.exoplayer.MediaCodecSelector) r22, (com.google.android.exoplayer.drm.DrmSessionManager) r23, (boolean) r24, (android.os.Handler) r25, (com.google.android.exoplayer.MediaCodecAudioTrackRenderer.EventListener) r26, (com.google.android.exoplayer.audio.AudioCapabilities) r27, (int) r28)
            L_0x010b:
                if (r32 == 0) goto L_0x01a1
                com.google.android.exoplayer.upstream.DefaultUriDataSource r23 = new com.google.android.exoplayer.upstream.DefaultUriDataSource
                r0 = r37
                android.content.Context r3 = r0.context
                r0 = r37
                java.lang.String r5 = r0.userAgent
                r0 = r23
                r0.<init>((android.content.Context) r3, (com.google.android.exoplayer.upstream.TransferListener) r7, (java.lang.String) r5)
                com.google.android.exoplayer.hls.HlsChunkSource r21 = new com.google.android.exoplayer.hls.HlsChunkSource
                r22 = 0
                com.google.android.exoplayer.hls.DefaultHlsTrackSelector r25 = com.google.android.exoplayer.hls.DefaultHlsTrackSelector.newSubtitleInstance()
                r24 = r38
                r26 = r7
                r27 = r8
                r21.<init>(r22, r23, r24, r25, r26, r27)
                com.google.android.exoplayer.hls.HlsSampleSource r24 = new com.google.android.exoplayer.hls.HlsSampleSource
                r27 = 131072(0x20000, float:1.83671E-40)
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r0 = r0.player
                r29 = r0
                r30 = 2
                r25 = r21
                r26 = r11
                r28 = r13
                r24.<init>(r25, r26, r27, r28, r29, r30)
                com.google.android.exoplayer.text.TextTrackRenderer r36 = new com.google.android.exoplayer.text.TextTrackRenderer
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r3 = r0.player
                android.os.Looper r5 = r13.getLooper()
                r6 = 0
                com.google.android.exoplayer.text.SubtitleParser[] r6 = new com.google.android.exoplayer.text.SubtitleParser[r6]
                r0 = r36
                r1 = r24
                r0.<init>((com.google.android.exoplayer.SampleSource) r1, (com.google.android.exoplayer.text.TextRenderer) r3, (android.os.Looper) r5, (com.google.android.exoplayer.text.SubtitleParser[]) r6)
            L_0x0156:
                r3 = 4
                com.google.android.exoplayer.TrackRenderer[] r0 = new com.google.android.exoplayer.TrackRenderer[r3]
                r35 = r0
                r3 = 0
                r35[r3] = r15
                r3 = 1
                r35[r3] = r20
                r3 = 3
                r35[r3] = r33
                r3 = 2
                r35[r3] = r36
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r3 = r0.player
                r0 = r35
                r3.onRenderers(r0, r7)
                goto L_0x0006
            L_0x0172:
                r32 = 0
                goto L_0x003f
            L_0x0176:
                r31 = 0
                goto L_0x004b
            L_0x017a:
                com.google.android.exoplayer.MediaCodecAudioTrackRenderer r20 = new com.google.android.exoplayer.MediaCodecAudioTrackRenderer
                com.google.android.exoplayer.MediaCodecSelector r22 = com.google.android.exoplayer.MediaCodecSelector.DEFAULT
                r23 = 0
                r24 = 1
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r3 = r0.player
                android.os.Handler r25 = r3.getMainHandler()
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r0 = r0.player
                r26 = r0
                r0 = r37
                android.content.Context r3 = r0.context
                com.google.android.exoplayer.audio.AudioCapabilities r27 = com.google.android.exoplayer.audio.AudioCapabilities.getCapabilities((android.content.Context) r3)
                r28 = 3
                r21 = r9
                r20.<init>((com.google.android.exoplayer.SampleSource) r21, (com.google.android.exoplayer.MediaCodecSelector) r22, (com.google.android.exoplayer.drm.DrmSessionManager) r23, (boolean) r24, (android.os.Handler) r25, (com.google.android.exoplayer.MediaCodecAudioTrackRenderer.EventListener) r26, (com.google.android.exoplayer.audio.AudioCapabilities) r27, (int) r28)
                goto L_0x010b
            L_0x01a1:
                com.google.android.exoplayer.text.eia608.Eia608TrackRenderer r36 = new com.google.android.exoplayer.text.eia608.Eia608TrackRenderer
                r0 = r37
                tv.danmaku.ijk.media.exo.demo.player.DemoPlayer r3 = r0.player
                android.os.Looper r5 = r13.getLooper()
                r0 = r36
                r0.<init>(r9, r3, r5)
                goto L_0x0156
            */
            throw new UnsupportedOperationException("Method not decompiled: p012tv.danmaku.ijk.media.exo.demo.player.HlsRendererBuilder.AsyncRendererBuilder.onSingleManifest(com.google.android.exoplayer.hls.HlsPlaylist):void");
        }
    }
}
