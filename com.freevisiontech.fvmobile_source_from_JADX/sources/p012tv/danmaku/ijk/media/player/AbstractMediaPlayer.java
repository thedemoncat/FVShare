package p012tv.danmaku.ijk.media.player;

import p012tv.danmaku.ijk.media.player.IMediaPlayer;
import p012tv.danmaku.ijk.media.player.misc.IMediaDataSource;

/* renamed from: tv.danmaku.ijk.media.player.AbstractMediaPlayer */
public abstract class AbstractMediaPlayer implements IMediaPlayer {
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private IMediaPlayer.OnTimedTextListener mOnTimedTextListener;
    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    public final void setOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public final void setOnCompletionListener(IMediaPlayer.OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public final void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener listener) {
        this.mOnBufferingUpdateListener = listener;
    }

    public final void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener listener) {
        this.mOnSeekCompleteListener = listener;
    }

    public final void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    public final void setOnErrorListener(IMediaPlayer.OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public final void setOnInfoListener(IMediaPlayer.OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    public final void setOnTimedTextListener(IMediaPlayer.OnTimedTextListener listener) {
        this.mOnTimedTextListener = listener;
    }

    public void resetListeners() {
        this.mOnPreparedListener = null;
        this.mOnBufferingUpdateListener = null;
        this.mOnCompletionListener = null;
        this.mOnSeekCompleteListener = null;
        this.mOnVideoSizeChangedListener = null;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
        this.mOnTimedTextListener = null;
    }

    /* access modifiers changed from: protected */
    public final void notifyOnPrepared() {
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onPrepared(this);
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyOnCompletion() {
        if (this.mOnCompletionListener != null) {
            this.mOnCompletionListener.onCompletion(this);
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyOnBufferingUpdate(int percent) {
        if (this.mOnBufferingUpdateListener != null) {
            this.mOnBufferingUpdateListener.onBufferingUpdate(this, percent);
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyOnSeekComplete() {
        if (this.mOnSeekCompleteListener != null) {
            this.mOnSeekCompleteListener.onSeekComplete(this);
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyOnVideoSizeChanged(int width, int height, int sarNum, int sarDen) {
        if (this.mOnVideoSizeChangedListener != null) {
            this.mOnVideoSizeChangedListener.onVideoSizeChanged(this, width, height, sarNum, sarDen);
        }
    }

    /* access modifiers changed from: protected */
    public final boolean notifyOnError(int what, int extra) {
        return this.mOnErrorListener != null && this.mOnErrorListener.onError(this, what, extra);
    }

    /* access modifiers changed from: protected */
    public final boolean notifyOnInfo(int what, int extra) {
        return this.mOnInfoListener != null && this.mOnInfoListener.onInfo(this, what, extra);
    }

    /* access modifiers changed from: protected */
    public final void notifyOnTimedText(IjkTimedText text) {
        if (this.mOnTimedTextListener != null) {
            this.mOnTimedTextListener.onTimedText(this, text);
        }
    }

    public void setDataSource(IMediaDataSource mediaDataSource) {
        throw new UnsupportedOperationException();
    }
}
