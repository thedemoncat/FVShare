package com.p007ny.ijk.upplayer.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.p003v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TextView;
import com.p007ny.ijk.upplayer.C1646R;
import com.p007ny.ijk.upplayer.application.Settings;
import com.p007ny.ijk.upplayer.media.IRenderView;
import com.p007ny.ijk.upplayer.services.MediaPlayerService;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import p012tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import p012tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import p012tv.danmaku.ijk.media.player.IMediaPlayer;
import p012tv.danmaku.ijk.media.player.IjkMediaPlayer;
import p012tv.danmaku.ijk.media.player.IjkTimedText;
import p012tv.danmaku.ijk.media.player.TextureMediaPlayer;
import p012tv.danmaku.ijk.media.player.misc.IMediaFormat;
import p012tv.danmaku.ijk.media.player.misc.ITrackInfo;
import p012tv.danmaku.ijk.media.player.misc.IjkMediaFormat;

/* renamed from: com.ny.ijk.upplayer.media.IjkVideoView */
public class IjkVideoView extends FrameLayout implements MediaController.MediaPlayerControl {
    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int[] s_allAspectRatio = {0, 1, 2, 4, 5};
    /* access modifiers changed from: private */
    public String TAG = "IjkVideoView";
    private List<Integer> mAllRenders = new ArrayList();
    /* access modifiers changed from: private */
    public Context mAppContext;
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            int unused = IjkVideoView.this.mCurrentBufferPercentage = percent;
        }
    };
    private boolean mCanPause = true;
    private boolean mCanSeekBack = true;
    private boolean mCanSeekForward = true;
    private IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {
        public void onCompletion(IMediaPlayer mp) {
            int unused = IjkVideoView.this.mCurrentState = 5;
            int unused2 = IjkVideoView.this.mTargetState = 5;
            if (IjkVideoView.this.mMediaController != null) {
                IjkVideoView.this.mMediaController.hide();
            }
            if (IjkVideoView.this.mOnCompletionListener != null) {
                IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this.mMediaPlayer);
            }
        }
    };
    private int mCurrentAspectRatio = s_allAspectRatio[0];
    private int mCurrentAspectRatioIndex = 0;
    /* access modifiers changed from: private */
    public int mCurrentBufferPercentage;
    private int mCurrentRender = 0;
    private int mCurrentRenderIndex = 0;
    /* access modifiers changed from: private */
    public int mCurrentState = 0;
    private boolean mEnableBackgroundPlay = false;
    private IMediaPlayer.OnErrorListener mErrorListener = new IMediaPlayer.OnErrorListener() {
        public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
            int messageId;
            Log.d(IjkVideoView.this.TAG, "Error: " + framework_err + "," + impl_err);
            int unused = IjkVideoView.this.mCurrentState = -1;
            int unused2 = IjkVideoView.this.mTargetState = -1;
            if (IjkVideoView.this.mMediaController != null) {
                IjkVideoView.this.mMediaController.hide();
            }
            if ((IjkVideoView.this.mOnErrorListener == null || !IjkVideoView.this.mOnErrorListener.onError(IjkVideoView.this.mMediaPlayer, framework_err, impl_err)) && IjkVideoView.this.getWindowToken() != null) {
                Resources resources = IjkVideoView.this.mAppContext.getResources();
                if (framework_err == 200) {
                    messageId = C1646R.string.VideoView_error_text_invalid_progressive_playback;
                } else {
                    messageId = C1646R.string.VideoView_error_text_unknown;
                }
                new AlertDialog.Builder(IjkVideoView.this.getContext()).setMessage(messageId).setPositiveButton(C1646R.string.VideoView_error_button, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (IjkVideoView.this.mOnCompletionListener != null) {
                            IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this.mMediaPlayer);
                        }
                    }
                }).setCancelable(false).show();
            }
            return true;
        }
    };
    private Map<String, String> mHeaders;
    /* access modifiers changed from: private */
    public InfoHudViewHolder mHudViewHolder;
    private IMediaPlayer.OnInfoListener mInfoListener = new IMediaPlayer.OnInfoListener() {
        public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
            if (IjkVideoView.this.mOnInfoListener != null) {
                IjkVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
            }
            switch (arg1) {
                case 3:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                case 700:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                    return true;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START /*701*/:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                    return true;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END /*702*/:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                    return true;
                case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH /*703*/:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + arg2);
                    return true;
                case 800:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                    return true;
                case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE /*801*/:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                    return true;
                case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE /*802*/:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                    return true;
                case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE /*901*/:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT /*902*/:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                case 10001:
                    int unused = IjkVideoView.this.mVideoRotationDegree = arg2;
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                    if (IjkVideoView.this.mRenderView == null) {
                        return true;
                    }
                    IjkVideoView.this.mRenderView.setVideoRotation(arg2);
                    return true;
                case 10002:
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                    return true;
                default:
                    return true;
            }
        }
    };
    /* access modifiers changed from: private */
    public IMediaController mMediaController;
    /* access modifiers changed from: private */
    public IMediaPlayer mMediaPlayer = null;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnCompletionListener mOnCompletionListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnErrorListener mOnErrorListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnInfoListener mOnInfoListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnTimedTextListener mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() {
        public void onTimedText(IMediaPlayer mp, IjkTimedText text) {
            if (text != null) {
                IjkVideoView.this.subtitleDisplay.setText(text.getText());
            }
        }
    };
    /* access modifiers changed from: private */
    public long mPrepareEndTime = 0;
    /* access modifiers changed from: private */
    public long mPrepareStartTime = 0;
    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            long unused = IjkVideoView.this.mPrepareEndTime = System.currentTimeMillis();
            if (IjkVideoView.this.mHudViewHolder != null) {
                IjkVideoView.this.mHudViewHolder.updateLoadCost(IjkVideoView.this.mPrepareEndTime - IjkVideoView.this.mPrepareStartTime);
            }
            int unused2 = IjkVideoView.this.mCurrentState = 2;
            if (IjkVideoView.this.mOnPreparedListener != null) {
                IjkVideoView.this.mOnPreparedListener.onPrepared(IjkVideoView.this.mMediaPlayer);
            }
            if (IjkVideoView.this.mMediaController != null) {
                IjkVideoView.this.mMediaController.setEnabled(true);
            }
            int unused3 = IjkVideoView.this.mVideoWidth = mp.getVideoWidth();
            int unused4 = IjkVideoView.this.mVideoHeight = mp.getVideoHeight();
            int seekToPosition = IjkVideoView.this.mSeekWhenPrepared;
            if (seekToPosition != 0) {
                IjkVideoView.this.seekTo(seekToPosition);
            }
            if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                if (IjkVideoView.this.mTargetState == 3) {
                    IjkVideoView.this.start();
                }
            } else if (IjkVideoView.this.mRenderView != null) {
                IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                if (IjkVideoView.this.mRenderView.shouldWaitForResize() && (IjkVideoView.this.mSurfaceWidth != IjkVideoView.this.mVideoWidth || IjkVideoView.this.mSurfaceHeight != IjkVideoView.this.mVideoHeight)) {
                    return;
                }
                if (IjkVideoView.this.mTargetState == 3) {
                    IjkVideoView.this.start();
                    if (IjkVideoView.this.mMediaController != null) {
                        IjkVideoView.this.mMediaController.show();
                    }
                } else if (IjkVideoView.this.isPlaying()) {
                } else {
                    if ((seekToPosition != 0 || IjkVideoView.this.getCurrentPosition() > 0) && IjkVideoView.this.mMediaController != null) {
                        IjkVideoView.this.mMediaController.show(0);
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public IRenderView mRenderView;
    IRenderView.IRenderCallback mSHCallback = new IRenderView.IRenderCallback() {
        public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int w, int h) {
            boolean isValidState;
            boolean hasValidSize;
            if (holder.getRenderView() != IjkVideoView.this.mRenderView) {
                Log.e(IjkVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                return;
            }
            int unused = IjkVideoView.this.mSurfaceWidth = w;
            int unused2 = IjkVideoView.this.mSurfaceHeight = h;
            if (IjkVideoView.this.mTargetState == 3) {
                isValidState = true;
            } else {
                isValidState = false;
            }
            if (!IjkVideoView.this.mRenderView.shouldWaitForResize() || (IjkVideoView.this.mVideoWidth == w && IjkVideoView.this.mVideoHeight == h)) {
                hasValidSize = true;
            } else {
                hasValidSize = false;
            }
            if (IjkVideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                if (IjkVideoView.this.mSeekWhenPrepared != 0) {
                    IjkVideoView.this.seekTo(IjkVideoView.this.mSeekWhenPrepared);
                }
                IjkVideoView.this.start();
            }
        }

        public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
            if (holder.getRenderView() != IjkVideoView.this.mRenderView) {
                Log.e(IjkVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                return;
            }
            IRenderView.ISurfaceHolder unused = IjkVideoView.this.mSurfaceHolder = holder;
            if (IjkVideoView.this.mMediaPlayer != null) {
                IjkVideoView.this.bindSurfaceHolder(IjkVideoView.this.mMediaPlayer, holder);
            } else {
                IjkVideoView.this.openVideo();
            }
        }

        public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
            if (holder.getRenderView() != IjkVideoView.this.mRenderView) {
                Log.e(IjkVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                return;
            }
            IRenderView.ISurfaceHolder unused = IjkVideoView.this.mSurfaceHolder = null;
            IjkVideoView.this.releaseWithoutStop();
        }
    };
    private IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        public void onSeekComplete(IMediaPlayer mp) {
        }
    };
    private long mSeekEndTime = 0;
    private long mSeekStartTime = 0;
    /* access modifiers changed from: private */
    public int mSeekWhenPrepared;
    private Settings mSettings;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
            int unused = IjkVideoView.this.mVideoWidth = mp.getVideoWidth();
            int unused2 = IjkVideoView.this.mVideoHeight = mp.getVideoHeight();
            int unused3 = IjkVideoView.this.mVideoSarNum = mp.getVideoSarNum();
            int unused4 = IjkVideoView.this.mVideoSarDen = mp.getVideoSarDen();
            if (IjkVideoView.this.mVideoWidth != 0 && IjkVideoView.this.mVideoHeight != 0) {
                if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                }
                IjkVideoView.this.requestLayout();
            }
        }
    };
    /* access modifiers changed from: private */
    public int mSurfaceHeight;
    /* access modifiers changed from: private */
    public IRenderView.ISurfaceHolder mSurfaceHolder = null;
    /* access modifiers changed from: private */
    public int mSurfaceWidth;
    /* access modifiers changed from: private */
    public int mTargetState = 0;
    private Uri mUri;
    /* access modifiers changed from: private */
    public int mVideoHeight;
    /* access modifiers changed from: private */
    public int mVideoRotationDegree;
    /* access modifiers changed from: private */
    public int mVideoSarDen;
    /* access modifiers changed from: private */
    public int mVideoSarNum;
    /* access modifiers changed from: private */
    public int mVideoWidth;
    /* access modifiers changed from: private */
    public TextView subtitleDisplay;

    public IjkVideoView(Context context) {
        super(context);
        initVideoView(context);
    }

    public IjkVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    @TargetApi(21)
    public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVideoView(context);
    }

    private void initVideoView(Context context) {
        this.mAppContext = context.getApplicationContext();
        this.mSettings = new Settings(this.mAppContext);
        initBackground();
        initRenders();
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.subtitleDisplay = new TextView(context);
        this.subtitleDisplay.setTextSize(24.0f);
        this.subtitleDisplay.setGravity(17);
        addView(this.subtitleDisplay, new FrameLayout.LayoutParams(-1, -2, 80));
    }

    public void setRenderView(IRenderView renderView) {
        if (this.mRenderView != null) {
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.setDisplay((SurfaceHolder) null);
            }
            View renderUIView = this.mRenderView.getView();
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
            removeView(renderUIView);
        }
        if (renderView != null) {
            this.mRenderView = renderView;
            renderView.setAspectRatio(this.mCurrentAspectRatio);
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                renderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            }
            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                renderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
            }
            View renderUIView2 = this.mRenderView.getView();
            renderUIView2.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
            addView(renderUIView2);
            this.mRenderView.addRenderCallback(this.mSHCallback);
            this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
        }
    }

    public void setAspectRatio(int arAspectFitParent) {
        if (this.mRenderView != null) {
            this.mRenderView.setAspectRatio(arAspectFitParent);
        }
    }

    public void setRender(int render) {
        switch (render) {
            case 0:
                setRenderView((IRenderView) null);
                return;
            case 1:
            case 2:
                TextureRenderView renderView = new TextureRenderView(getContext());
                if (this.mMediaPlayer != null) {
                    renderView.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
                    renderView.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight());
                    renderView.setVideoSampleAspectRatio(this.mMediaPlayer.getVideoSarNum(), this.mMediaPlayer.getVideoSarDen());
                    renderView.setAspectRatio(this.mCurrentAspectRatio);
                }
                setRenderView(renderView);
                return;
            default:
                Log.e(this.TAG, String.format(Locale.getDefault(), "invalid render %d\n", new Object[]{Integer.valueOf(render)}));
                return;
        }
    }

    public void setHudView(TableLayout tableLayout) {
        this.mHudViewHolder = new InfoHudViewHolder(getContext(), tableLayout);
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, (Map<String, String>) null);
    }

    private void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            if (this.mHudViewHolder != null) {
                this.mHudViewHolder.setMediaPlayer((IMediaPlayer) null);
            }
            this.mCurrentState = 0;
            this.mTargetState = 0;
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) null);
        }
    }

    /* access modifiers changed from: private */
    @TargetApi(23)
    public void openVideo() {
        if (this.mUri != null && this.mSurfaceHolder != null) {
            release(false);
            ((AudioManager) this.mAppContext.getSystemService("audio")).requestAudioFocus((AudioManager.OnAudioFocusChangeListener) null, 3, 1);
            try {
                this.mMediaPlayer = createPlayer(this.mSettings.getPlayer());
                Context context = getContext();
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
                this.mMediaPlayer.setOnTimedTextListener(this.mOnTimedTextListener);
                this.mCurrentBufferPercentage = 0;
                String scheme = this.mUri.getScheme();
                if (Build.VERSION.SDK_INT >= 23 && this.mSettings.getUsingMediaDataSource() && (TextUtils.isEmpty(scheme) || scheme.equalsIgnoreCase("file"))) {
                    this.mMediaPlayer.setDataSource(new FileMediaDataSource(new File(this.mUri.toString())));
                } else if (Build.VERSION.SDK_INT >= 14) {
                    this.mMediaPlayer.setDataSource(this.mAppContext, this.mUri, this.mHeaders);
                } else {
                    this.mMediaPlayer.setDataSource(this.mUri.toString());
                }
                bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mPrepareStartTime = System.currentTimeMillis();
                this.mMediaPlayer.prepareAsync();
                if (this.mHudViewHolder != null) {
                    this.mHudViewHolder.setMediaPlayer(this.mMediaPlayer);
                }
                this.mCurrentState = 1;
                attachMediaController();
            } catch (IOException ex) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } catch (IllegalArgumentException ex2) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex2);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }
        }
    }

    public void setMediaController(IMediaController controller) {
        if (this.mMediaController != null) {
            this.mMediaController.hide();
        }
        this.mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        View anchorView;
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            this.mMediaController.setMediaPlayer(this);
            if (getParent() instanceof View) {
                anchorView = (View) getParent();
            } else {
                anchorView = this;
            }
            this.mMediaController.setAnchorView(anchorView);
            this.mMediaController.setEnabled(isInPlaybackState());
        }
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    /* access modifiers changed from: private */
    public void bindSurfaceHolder(IMediaPlayer mp, IRenderView.ISurfaceHolder holder) {
        if (mp != null) {
            if (holder == null) {
                mp.setDisplay((SurfaceHolder) null);
            } else {
                holder.bindToMediaPlayer(mp);
            }
        }
    }

    public void releaseWithoutStop() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setDisplay((SurfaceHolder) null);
        }
    }

    public void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) null);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!isInPlaybackState() || this.mMediaController == null) {
            return false;
        }
        toggleMediaControlsVisiblity();
        return false;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        if (!isInPlaybackState() || this.mMediaController == null) {
            return false;
        }
        toggleMediaControlsVisiblity();
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 164 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && isKeyCodeSupported && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode == 126) {
                if (this.mMediaPlayer.isPlaying()) {
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode != 86 && keyCode != 127) {
                toggleMediaControlsVisiblity();
            } else if (!this.mMediaPlayer.isPlaying()) {
                return true;
            } else {
                pause();
                this.mMediaController.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    public void start() {
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public void suspend() {
        release(false);
    }

    public void resume() {
        openVideo();
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            this.mSeekStartTime = System.currentTimeMillis();
            this.mMediaPlayer.seekTo((long) msec);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = msec;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }

    public boolean canPause() {
        return this.mCanPause;
    }

    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public int toggleAspectRatio() {
        this.mCurrentAspectRatioIndex++;
        this.mCurrentAspectRatioIndex %= s_allAspectRatio.length;
        this.mCurrentAspectRatio = s_allAspectRatio[this.mCurrentAspectRatioIndex];
        if (this.mRenderView != null) {
            this.mRenderView.setAspectRatio(this.mCurrentAspectRatio);
        }
        return this.mCurrentAspectRatio;
    }

    private void initRenders() {
        this.mAllRenders.clear();
        if (this.mSettings.getEnableSurfaceView()) {
            this.mAllRenders.add(1);
        }
        if (this.mSettings.getEnableTextureView() && Build.VERSION.SDK_INT >= 14) {
            this.mAllRenders.add(2);
        }
        if (this.mSettings.getEnableNoView()) {
            this.mAllRenders.add(0);
        }
        if (this.mAllRenders.isEmpty()) {
            this.mAllRenders.add(1);
        }
        this.mCurrentRender = this.mAllRenders.get(this.mCurrentRenderIndex).intValue();
        setRender(this.mCurrentRender);
    }

    public int toggleRender() {
        this.mCurrentRenderIndex++;
        this.mCurrentRenderIndex %= this.mAllRenders.size();
        this.mCurrentRender = this.mAllRenders.get(this.mCurrentRenderIndex).intValue();
        setRender(this.mCurrentRender);
        return this.mCurrentRender;
    }

    @NonNull
    public static String getRenderText(Context context, int render) {
        switch (render) {
            case 0:
                return context.getString(C1646R.string.VideoView_render_none);
            case 1:
                return context.getString(C1646R.string.VideoView_render_surface_view);
            case 2:
                return context.getString(C1646R.string.VideoView_render_texture_view);
            default:
                return context.getString(C1646R.string.N_A);
        }
    }

    public int togglePlayer() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
        }
        if (this.mRenderView != null) {
            this.mRenderView.getView().invalidate();
        }
        openVideo();
        return this.mSettings.getPlayer();
    }

    @NonNull
    public static String getPlayerText(Context context, int player) {
        switch (player) {
            case 1:
                return context.getString(C1646R.string.VideoView_player_AndroidMediaPlayer);
            case 2:
                return context.getString(C1646R.string.VideoView_player_IjkMediaPlayer);
            case 3:
                return context.getString(C1646R.string.VideoView_player_IjkExoMediaPlayer);
            default:
                return context.getString(C1646R.string.N_A);
        }
    }

    public IMediaPlayer createPlayer(int playerType) {
        IMediaPlayer mediaPlayer;
        switch (playerType) {
            case 1:
                mediaPlayer = new AndroidMediaPlayer();
                break;
            case 3:
                Log.e("Player", "---IMediaPlayer createPlayer---3--PV_PLAYER__IjkExoMediaPlayer");
                mediaPlayer = new IjkExoMediaPlayer(this.mAppContext);
                break;
            default:
                Log.e("Player", "---IMediaPlayer createPlayer---2--PV_PLAYER__IjkMediaPlayer");
                IjkMediaPlayer ijkMediaPlayer = null;
                if (this.mUri != null) {
                    ijkMediaPlayer = new IjkMediaPlayer();
                    IjkMediaPlayer.native_setLogLevel(3);
                    if (this.mSettings.getUsingMediaCodec()) {
                        ijkMediaPlayer.setOption(4, "mediacodec", 1);
                        if (this.mSettings.getUsingMediaCodecAutoRotate()) {
                            ijkMediaPlayer.setOption(4, "mediacodec-auto-rotate", 1);
                        } else {
                            ijkMediaPlayer.setOption(4, "mediacodec-auto-rotate", 0);
                        }
                        if (this.mSettings.getMediaCodecHandleResolutionChange()) {
                            ijkMediaPlayer.setOption(4, "mediacodec-handle-resolution-change", 1);
                        } else {
                            ijkMediaPlayer.setOption(4, "mediacodec-handle-resolution-change", 0);
                        }
                    } else {
                        ijkMediaPlayer.setOption(4, "mediacodec", 0);
                    }
                    if (this.mSettings.getUsingOpenSLES()) {
                        ijkMediaPlayer.setOption(4, "opensles", 1);
                    } else {
                        ijkMediaPlayer.setOption(4, "opensles", 0);
                    }
                    String pixelFormat = this.mSettings.getPixelFormat();
                    if (TextUtils.isEmpty(pixelFormat)) {
                        ijkMediaPlayer.setOption(4, "overlay-format", 842225234);
                    } else {
                        ijkMediaPlayer.setOption(4, "overlay-format", pixelFormat);
                    }
                    ijkMediaPlayer.setOption(4, "framedrop", 1);
                    ijkMediaPlayer.setOption(4, "start-on-prepared", 0);
                    ijkMediaPlayer.setOption(1, "http-detect-range-support", 0);
                    ijkMediaPlayer.setOption(2, "skip_loop_filter", 48);
                }
                mediaPlayer = ijkMediaPlayer;
                break;
        }
        if (this.mSettings.getEnableDetachedSurfaceTextureView()) {
            return new TextureMediaPlayer(mediaPlayer);
        }
        return mediaPlayer;
    }

    private void initBackground() {
        this.mEnableBackgroundPlay = this.mSettings.getEnableBackgroundPlay();
        if (this.mEnableBackgroundPlay) {
            MediaPlayerService.intentToStart(getContext());
            this.mMediaPlayer = MediaPlayerService.getMediaPlayer();
            if (this.mHudViewHolder != null) {
                this.mHudViewHolder.setMediaPlayer(this.mMediaPlayer);
            }
        }
    }

    public boolean isBackgroundPlayEnabled() {
        return this.mEnableBackgroundPlay;
    }

    public void enterBackground() {
        MediaPlayerService.setMediaPlayer(this.mMediaPlayer);
    }

    public void stopBackgroundPlay() {
        MediaPlayerService.setMediaPlayer((IMediaPlayer) null);
    }

    public void showMediaInfo() {
        if (this.mMediaPlayer != null) {
            int selectedVideoTrack = MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, 1);
            int selectedAudioTrack = MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, 2);
            int selectedSubtitleTrack = MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, 3);
            TableLayoutBinder builder = new TableLayoutBinder(getContext());
            builder.appendSection(C1646R.string.mi_player);
            builder.appendRow2(C1646R.string.mi_player, MediaPlayerCompat.getName(this.mMediaPlayer));
            builder.appendSection(C1646R.string.mi_media);
            builder.appendRow2(C1646R.string.mi_resolution, buildResolution(this.mVideoWidth, this.mVideoHeight, this.mVideoSarNum, this.mVideoSarDen));
            builder.appendRow2(C1646R.string.mi_length, buildTimeMilli(this.mMediaPlayer.getDuration()));
            ITrackInfo[] trackInfos = this.mMediaPlayer.getTrackInfo();
            if (trackInfos != null) {
                int index = -1;
                for (ITrackInfo trackInfo : trackInfos) {
                    index++;
                    int trackType = trackInfo.getTrackType();
                    if (index == selectedVideoTrack) {
                        builder.appendSection(getContext().getString(C1646R.string.mi_stream_fmt1, new Object[]{Integer.valueOf(index)}) + " " + getContext().getString(C1646R.string.mi__selected_video_track));
                    } else if (index == selectedAudioTrack) {
                        builder.appendSection(getContext().getString(C1646R.string.mi_stream_fmt1, new Object[]{Integer.valueOf(index)}) + " " + getContext().getString(C1646R.string.mi__selected_audio_track));
                    } else if (index == selectedSubtitleTrack) {
                        builder.appendSection(getContext().getString(C1646R.string.mi_stream_fmt1, new Object[]{Integer.valueOf(index)}) + " " + getContext().getString(C1646R.string.mi__selected_subtitle_track));
                    } else {
                        builder.appendSection(getContext().getString(C1646R.string.mi_stream_fmt1, new Object[]{Integer.valueOf(index)}));
                    }
                    builder.appendRow2(C1646R.string.mi_type, buildTrackType(trackType));
                    builder.appendRow2(C1646R.string.mi_language, buildLanguage(trackInfo.getLanguage()));
                    IMediaFormat mediaFormat = trackInfo.getFormat();
                    if (mediaFormat != null && (mediaFormat instanceof IjkMediaFormat)) {
                        switch (trackType) {
                            case 1:
                                builder.appendRow2(C1646R.string.mi_codec, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_LONG_NAME_UI));
                                builder.appendRow2(C1646R.string.mi_profile_level, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_PROFILE_LEVEL_UI));
                                builder.appendRow2(C1646R.string.mi_pixel_format, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_PIXEL_FORMAT_UI));
                                builder.appendRow2(C1646R.string.mi_resolution, mediaFormat.getString(IjkMediaFormat.KEY_IJK_RESOLUTION_UI));
                                builder.appendRow2(C1646R.string.mi_frame_rate, mediaFormat.getString(IjkMediaFormat.KEY_IJK_FRAME_RATE_UI));
                                builder.appendRow2(C1646R.string.mi_bit_rate, mediaFormat.getString(IjkMediaFormat.KEY_IJK_BIT_RATE_UI));
                                break;
                            case 2:
                                builder.appendRow2(C1646R.string.mi_codec, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_LONG_NAME_UI));
                                builder.appendRow2(C1646R.string.mi_profile_level, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_PROFILE_LEVEL_UI));
                                builder.appendRow2(C1646R.string.mi_sample_rate, mediaFormat.getString(IjkMediaFormat.KEY_IJK_SAMPLE_RATE_UI));
                                builder.appendRow2(C1646R.string.mi_channels, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CHANNEL_UI));
                                builder.appendRow2(C1646R.string.mi_bit_rate, mediaFormat.getString(IjkMediaFormat.KEY_IJK_BIT_RATE_UI));
                                break;
                        }
                    }
                }
            }
            AlertDialog.Builder adBuilder = builder.buildAlertDialogBuilder();
            adBuilder.setTitle(C1646R.string.media_information);
            adBuilder.setNegativeButton(C1646R.string.close, (DialogInterface.OnClickListener) null);
            adBuilder.show();
        }
    }

    private String buildResolution(int width, int height, int sarNum, int sarDen) {
        StringBuilder sb = new StringBuilder();
        sb.append(width);
        sb.append(" x ");
        sb.append(height);
        if (sarNum > 1 || sarDen > 1) {
            sb.append("[");
            sb.append(sarNum);
            sb.append(":");
            sb.append(sarDen);
            sb.append("]");
        }
        return sb.toString();
    }

    private String buildTimeMilli(long duration) {
        long total_seconds = duration / 1000;
        long hours = total_seconds / 3600;
        long minutes = (total_seconds % 3600) / 60;
        long seconds = total_seconds % 60;
        if (duration <= 0) {
            return "--:--";
        }
        if (hours >= 100) {
            return String.format(Locale.US, "%d:%02d:%02d", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)});
        } else if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)});
        } else {
            return String.format(Locale.US, "%02d:%02d", new Object[]{Long.valueOf(minutes), Long.valueOf(seconds)});
        }
    }

    private String buildTrackType(int type) {
        Context context = getContext();
        switch (type) {
            case 1:
                return context.getString(C1646R.string.TrackType_video);
            case 2:
                return context.getString(C1646R.string.TrackType_audio);
            case 3:
                return context.getString(C1646R.string.TrackType_timedtext);
            case 4:
                return context.getString(C1646R.string.TrackType_subtitle);
            case 5:
                return context.getString(C1646R.string.TrackType_metadata);
            default:
                return context.getString(C1646R.string.TrackType_unknown);
        }
    }

    private String buildLanguage(String language) {
        if (TextUtils.isEmpty(language)) {
            return "und";
        }
        return language;
    }

    public ITrackInfo[] getTrackInfo() {
        if (this.mMediaPlayer == null) {
            return null;
        }
        return this.mMediaPlayer.getTrackInfo();
    }

    public void selectTrack(int stream) {
        MediaPlayerCompat.selectTrack(this.mMediaPlayer, stream);
    }

    public void deselectTrack(int stream) {
        MediaPlayerCompat.deselectTrack(this.mMediaPlayer, stream);
    }

    public int getSelectedTrack(int trackType) {
        return MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, trackType);
    }
}
