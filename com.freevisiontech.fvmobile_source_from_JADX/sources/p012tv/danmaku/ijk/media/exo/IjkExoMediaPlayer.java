package p012tv.danmaku.ijk.media.exo;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.google.android.exoplayer.util.Util;
import java.io.FileDescriptor;
import java.util.Map;
import p012tv.danmaku.ijk.media.exo.demo.EventLogger;
import p012tv.danmaku.ijk.media.exo.demo.SmoothStreamingTestMediaDrmCallback;
import p012tv.danmaku.ijk.media.exo.demo.player.DemoPlayer;
import p012tv.danmaku.ijk.media.exo.demo.player.ExtractorRendererBuilder;
import p012tv.danmaku.ijk.media.exo.demo.player.HlsRendererBuilder;
import p012tv.danmaku.ijk.media.exo.demo.player.SmoothStreamingRendererBuilder;
import p012tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import p012tv.danmaku.ijk.media.player.IMediaPlayer;
import p012tv.danmaku.ijk.media.player.MediaInfo;
import p012tv.danmaku.ijk.media.player.misc.IjkTrackInfo;

/* renamed from: tv.danmaku.ijk.media.exo.IjkExoMediaPlayer */
public class IjkExoMediaPlayer extends AbstractMediaPlayer {
    private Context mAppContext;
    private String mDataSource;
    private DemoPlayerListener mDemoListener = new DemoPlayerListener();
    private EventLogger mEventLogger = new EventLogger();
    /* access modifiers changed from: private */
    public DemoPlayer mInternalPlayer;
    private DemoPlayer.RendererBuilder mRendererBuilder;
    private Surface mSurface;
    /* access modifiers changed from: private */
    public int mVideoHeight;
    /* access modifiers changed from: private */
    public int mVideoWidth;

    public IjkExoMediaPlayer(Context context) {
        this.mAppContext = context.getApplicationContext();
        this.mEventLogger.startSession();
    }

    public void setDisplay(SurfaceHolder sh) {
        if (sh == null) {
            setSurface((Surface) null);
        } else {
            setSurface(sh.getSurface());
        }
    }

    public void setSurface(Surface surface) {
        this.mSurface = surface;
        if (this.mInternalPlayer != null) {
            this.mInternalPlayer.setSurface(surface);
        }
    }

    public void setDataSource(Context context, Uri uri) {
        this.mDataSource = uri.toString();
        this.mRendererBuilder = getRendererBuilder();
    }

    public void setDataSource(Context context, Uri uri, Map<String, String> map) {
        setDataSource(context, uri);
    }

    public void setDataSource(String path) {
        setDataSource(this.mAppContext, Uri.parse(path));
    }

    public void setDataSource(FileDescriptor fd) {
        throw new UnsupportedOperationException("no support");
    }

    public String getDataSource() {
        return this.mDataSource;
    }

    public void prepareAsync() throws IllegalStateException {
        if (this.mInternalPlayer != null) {
            throw new IllegalStateException("can't prepare a prepared player");
        }
        this.mInternalPlayer = new DemoPlayer(this.mRendererBuilder);
        this.mInternalPlayer.addListener(this.mDemoListener);
        this.mInternalPlayer.addListener(this.mEventLogger);
        this.mInternalPlayer.setInfoListener(this.mEventLogger);
        this.mInternalPlayer.setInternalErrorListener(this.mEventLogger);
        if (this.mSurface != null) {
            this.mInternalPlayer.setSurface(this.mSurface);
        }
        this.mInternalPlayer.prepare();
        this.mInternalPlayer.setPlayWhenReady(false);
    }

    public void start() throws IllegalStateException {
        if (this.mInternalPlayer != null) {
            this.mInternalPlayer.setPlayWhenReady(true);
        }
    }

    public void stop() throws IllegalStateException {
        if (this.mInternalPlayer != null) {
            this.mInternalPlayer.release();
        }
    }

    public void pause() throws IllegalStateException {
        if (this.mInternalPlayer != null) {
            this.mInternalPlayer.setPlayWhenReady(false);
        }
    }

    public void setWakeMode(Context context, int mode) {
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
    }

    public IjkTrackInfo[] getTrackInfo() {
        return null;
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public boolean isPlaying() {
        if (this.mInternalPlayer == null) {
            return false;
        }
        switch (this.mInternalPlayer.getPlaybackState()) {
            case 3:
            case 4:
                return this.mInternalPlayer.getPlayWhenReady();
            default:
                return false;
        }
    }

    public void seekTo(long msec) throws IllegalStateException {
        if (this.mInternalPlayer != null) {
            this.mInternalPlayer.seekTo(msec);
        }
    }

    public long getCurrentPosition() {
        if (this.mInternalPlayer == null) {
            return 0;
        }
        return this.mInternalPlayer.getCurrentPosition();
    }

    public long getDuration() {
        if (this.mInternalPlayer == null) {
            return 0;
        }
        return this.mInternalPlayer.getDuration();
    }

    public int getVideoSarNum() {
        return 1;
    }

    public int getVideoSarDen() {
        return 1;
    }

    public void reset() {
        if (this.mInternalPlayer != null) {
            this.mInternalPlayer.release();
            this.mInternalPlayer.removeListener(this.mDemoListener);
            this.mInternalPlayer.removeListener(this.mEventLogger);
            this.mInternalPlayer.setInfoListener((DemoPlayer.InfoListener) null);
            this.mInternalPlayer.setInternalErrorListener((DemoPlayer.InternalErrorListener) null);
            this.mInternalPlayer = null;
        }
        this.mSurface = null;
        this.mDataSource = null;
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
    }

    public void setLooping(boolean looping) {
        throw new UnsupportedOperationException("no support");
    }

    public boolean isLooping() {
        return false;
    }

    public void setVolume(float leftVolume, float rightVolume) {
    }

    public int getAudioSessionId() {
        return 0;
    }

    public MediaInfo getMediaInfo() {
        return null;
    }

    public void setLogEnabled(boolean enable) {
    }

    public boolean isPlayable() {
        return true;
    }

    public void setAudioStreamType(int streamtype) {
    }

    public void setKeepInBackground(boolean keepInBackground) {
    }

    public void release() {
        if (this.mInternalPlayer != null) {
            reset();
            this.mDemoListener = null;
            this.mEventLogger.endSession();
            this.mEventLogger = null;
        }
    }

    public int getBufferedPercentage() {
        if (this.mInternalPlayer == null) {
            return 0;
        }
        return this.mInternalPlayer.getBufferedPercentage();
    }

    private DemoPlayer.RendererBuilder getRendererBuilder() {
        Uri contentUri = Uri.parse(this.mDataSource);
        String userAgent = Util.getUserAgent(this.mAppContext, "IjkExoMediaPlayer");
        switch (inferContentType(contentUri)) {
            case 1:
                return new SmoothStreamingRendererBuilder(this.mAppContext, userAgent, contentUri.toString(), new SmoothStreamingTestMediaDrmCallback());
            case 2:
                return new HlsRendererBuilder(this.mAppContext, userAgent, contentUri.toString());
            default:
                return new ExtractorRendererBuilder(this.mAppContext, userAgent, contentUri);
        }
    }

    private static int inferContentType(Uri uri) {
        return Util.inferContentType(uri.getLastPathSegment());
    }

    /* renamed from: tv.danmaku.ijk.media.exo.IjkExoMediaPlayer$DemoPlayerListener */
    private class DemoPlayerListener implements DemoPlayer.Listener {
        private boolean mDidPrepare;
        private boolean mIsBuffering;
        private boolean mIsPrepareing;

        private DemoPlayerListener() {
            this.mIsPrepareing = false;
            this.mDidPrepare = false;
            this.mIsBuffering = false;
        }

        public void onStateChanged(boolean playWhenReady, int playbackState) {
            if (this.mIsBuffering) {
                switch (playbackState) {
                    case 4:
                    case 5:
                        boolean unused = IjkExoMediaPlayer.this.notifyOnInfo(IMediaPlayer.MEDIA_INFO_BUFFERING_END, IjkExoMediaPlayer.this.mInternalPlayer.getBufferedPercentage());
                        this.mIsBuffering = false;
                        break;
                }
            }
            if (this.mIsPrepareing) {
                switch (playbackState) {
                    case 4:
                        IjkExoMediaPlayer.this.notifyOnPrepared();
                        this.mIsPrepareing = false;
                        this.mDidPrepare = false;
                        break;
                }
            }
            switch (playbackState) {
                case 1:
                    IjkExoMediaPlayer.this.notifyOnCompletion();
                    return;
                case 2:
                    this.mIsPrepareing = true;
                    return;
                case 3:
                    boolean unused2 = IjkExoMediaPlayer.this.notifyOnInfo(IMediaPlayer.MEDIA_INFO_BUFFERING_START, IjkExoMediaPlayer.this.mInternalPlayer.getBufferedPercentage());
                    this.mIsBuffering = true;
                    return;
                case 5:
                    IjkExoMediaPlayer.this.notifyOnCompletion();
                    return;
                default:
                    return;
            }
        }

        public void onError(Exception e) {
            boolean unused = IjkExoMediaPlayer.this.notifyOnError(1, 1);
        }

        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            int unused = IjkExoMediaPlayer.this.mVideoWidth = width;
            int unused2 = IjkExoMediaPlayer.this.mVideoHeight = height;
            IjkExoMediaPlayer.this.notifyOnVideoSizeChanged(width, height, 1, 1);
            if (unappliedRotationDegrees > 0) {
                boolean unused3 = IjkExoMediaPlayer.this.notifyOnInfo(10001, unappliedRotationDegrees);
            }
        }
    }
}
