package com.p007ny.ijk.upplayer.media;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.p003v7.app.ActionBar;
import android.support.p003v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.freevisiontech.cameralib.Constants;
import com.google.android.exoplayer.C1907C;
import com.p007ny.ijk.upplayer.C1646R;
import com.p007ny.ijk.upplayer.utils.UpEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Marker;
import p010me.iwf.photopicker.utils.PpEventConstant;
import p012tv.danmaku.ijk.media.player.IMediaPlayer;
import p012tv.danmaku.ijk.media.player.IjkLibLoader;
import p012tv.danmaku.ijk.media.player.IjkMediaPlayer;
import p012tv.danmaku.ijk.media.player.misc.IMediaFormat;
import p012tv.danmaku.ijk.media.player.pragma.DebugLog;

/* renamed from: com.ny.ijk.upplayer.media.PlayerManager */
public class PlayerManager implements View.OnClickListener {
    public static final String SCALETYPE_16_9 = "16:9";
    public static final String SCALETYPE_4_3 = "4:3";
    public static final String SCALETYPE_FILLPARENT = "fillparent";
    public static final String SCALETYPE_FITPARENT = "fitparent";
    public static final String SCALETYPE_FITXY = "fitXY";
    public static final String SCALETYPE_WRAPCONTENT = "wrapContent";
    protected static Timer UPDATE_PROGRESS_TIMER = null;
    private static final int UPDATE_TIME_AND_PROGRESS = 1;
    private final int STATUS_COMPLETED = 4;
    private final int STATUS_ERROR = -1;
    private final int STATUS_IDLE = 0;
    private final int STATUS_LOADING = 1;
    private final int STATUS_PAUSE = 3;
    private final int STATUS_PLAYING = 2;
    private final int STATUS_PTZ_BACK = 6;
    private final int STATUS_PTZ_PLAY_PAUSE = 5;
    private final int STATUS_PTZ_SEEK_AFTER = 7;
    private final int STATUS_PTZ_SEEK_BEFORE = 8;
    private final Activity activity;
    private final AudioManager audioManager;
    /* access modifiers changed from: private */
    public ImageView backIv;
    private LinearLayout bottomLl;
    private SeekBar bottomSeekBar;
    private float brightness = -1.0f;
    /* access modifiers changed from: private */
    public CountDownTimer controlViewTimer = new CountDownTimer(5000, 1000) {
        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            PlayerManager.this.setControlViewVisiable(false);
        }
    };
    private int currentPosition;
    /* access modifiers changed from: private */
    public TextView currentTv;
    private long defaultRetryTime = 5000;
    private boolean fullScreenOnly;
    public GestureDetector gestureDetector;
    /* access modifiers changed from: private */
    public boolean isLive = false;
    private ImageView lastIv;
    private ProgressBar loadingProgressBar;
    private Dialog mBrightnessDialog;
    /* access modifiers changed from: private */
    public boolean mControlVisiable = true;
    /* access modifiers changed from: private */
    public int mCurrentPosition = 0;
    private ProgressBar mDialogBrightnessProgressBar;
    private TextView mDialogBrightnessTv;
    private ImageView mDialogIconIv;
    private ProgressBar mDialogProgressBar;
    private TextView mDialogSeekTimeTv;
    private TextView mDialogTotalTimeTv;
    private ProgressBar mDialogVolumeProgressBar;
    private TextView mDialogVolumeTv;
    private Map<String, Object> mMap;
    private final int mMaxVolume;
    /* access modifiers changed from: private */
    public String mPositionStr = "00:00";
    /* access modifiers changed from: private */
    public int mProgress = 0;
    private Dialog mProgressDialog;
    protected ProgressTimerTask mProgressTimerTask;
    SharedPreferences mSp;
    /* access modifiers changed from: private */
    public Handler mTimeHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    PlayerManager.this.changeTimeAndProgress();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public String mTotalStr = "00:00";
    private Handler mUiHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 5:
                    PlayerManager.this.playPauseIv.performClick();
                    return false;
                case 6:
                    PlayerManager.this.backIv.performClick();
                    return false;
                case 7:
                    PlayerManager.this.onProgressSlide(0.1f);
                    if (((float) PlayerManager.this.mCurrentPosition) + 0.1f >= ((float) PlayerManager.this.videoView.getDuration())) {
                        return false;
                    }
                    PlayerManager.this.progressDismissTimer.start();
                    return false;
                case 8:
                    PlayerManager.this.onProgressSlide(-0.1f);
                    PlayerManager.this.progressDismissTimer.start();
                    return false;
                default:
                    return false;
            }
        }
    });
    private Dialog mVolumeDialog;
    private long newPosition = -1;
    private ImageView nextIv;
    /* access modifiers changed from: private */
    public OnCompleteListener onCompleteListener = new OnCompleteListener() {
        public void onComplete() {
            PlayerManager.this.releaseProgressTimer();
            PlayerManager.this.startIv.setImageResource(C1646R.mipmap.up_play_mid);
            PlayerManager.this.playPauseIv.setImageResource(C1646R.mipmap.up_play);
            PlayerManager.this.statusChange(4);
            PlayerManager.this.stop();
            PlayerManager.this.releaseProgressTimer();
        }
    };
    private OnControlPanelVisibilityChangeListener onControlPanelVisibilityChangeListener = new OnControlPanelVisibilityChangeListener() {
        public void change(boolean isShowing) {
        }
    };
    /* access modifiers changed from: private */
    public OnErrorListener onErrorListener = new OnErrorListener() {
        public void onError(int what, int extra) {
        }
    };
    /* access modifiers changed from: private */
    public OnInfoListener onInfoListener = new OnInfoListener() {
        public void onInfo(int what, int extra) {
        }
    };
    /* access modifiers changed from: private */
    public OrientationEventListener orientationEventListener;
    private long pauseTime;
    /* access modifiers changed from: private */
    public ImageView playPauseIv;
    private View playerContent;
    private PlayerStateListener playerStateListener;
    private boolean playerSupport;
    private boolean portrait;
    /* access modifiers changed from: private */
    public CountDownTimer progressDismissTimer = new CountDownTimer(1000, 100) {
        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            if (PlayerManager.this.videoView.isPlaying()) {
                PlayerManager.this.dismissProgressDialog();
            }
        }
    };
    /* access modifiers changed from: private */
    public int screenWidthPixels;
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            PlayerManager.this.currentTv.setText(PlayerManager.this.generateTime((long) ((seekBar.getProgress() * PlayerManager.this.getDuration()) / 100)));
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            PlayerManager.this.videoView.seekTo((seekBar.getProgress() * PlayerManager.this.getDuration()) / 100);
        }
    };
    /* access modifiers changed from: private */
    public ImageView startIv;
    private LinearLayout startLl;
    /* access modifiers changed from: private */
    public int status = 0;
    private RelativeLayout topRl;
    private TextView totalTv;
    private String url;
    private List<String> urls;
    /* access modifiers changed from: private */
    public final IjkVideoView videoView;
    private int volume = -1;

    /* renamed from: com.ny.ijk.upplayer.media.PlayerManager$OnCompleteListener */
    public interface OnCompleteListener {
        void onComplete();
    }

    /* renamed from: com.ny.ijk.upplayer.media.PlayerManager$OnControlPanelVisibilityChangeListener */
    public interface OnControlPanelVisibilityChangeListener {
        void change(boolean z);
    }

    /* renamed from: com.ny.ijk.upplayer.media.PlayerManager$OnErrorListener */
    public interface OnErrorListener {
        void onError(int i, int i2);
    }

    /* renamed from: com.ny.ijk.upplayer.media.PlayerManager$OnInfoListener */
    public interface OnInfoListener {
        void onInfo(int i, int i2);
    }

    /* renamed from: com.ny.ijk.upplayer.media.PlayerManager$PlayerStateListener */
    public interface PlayerStateListener {
        void onComplete();

        void onError();

        void onLoading();

        void onPlay();
    }

    public void setPlayerStateListener(PlayerStateListener playerStateListener2) {
        this.playerStateListener = playerStateListener2;
    }

    public void setDefaultRetryTime(long defaultRetryTime2) {
        this.defaultRetryTime = defaultRetryTime2;
    }

    public PlayerManager(final Activity activity2) {
        try {
            IjkMediaPlayer.loadLibrariesOnce((IjkLibLoader) null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            this.playerSupport = true;
        } catch (Throwable e) {
            Log.e("UpPlayer", "loadLibraries error", e);
        }
        this.activity = activity2;
        activity2.getWindow().addFlags(128);
        this.screenWidthPixels = activity2.getResources().getDisplayMetrics().widthPixels;
        this.videoView = (IjkVideoView) activity2.findViewById(C1646R.C1648id.up_player_view);
        this.videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                PlayerManager.this.statusChange(4);
                PlayerManager.this.onCompleteListener.onComplete();
                activity2.finish();
            }
        });
        this.videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
                PlayerManager.this.statusChange(-1);
                PlayerManager.this.onErrorListener.onError(what, extra);
                return true;
            }
        });
        this.videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case 3:
                        PlayerManager.this.statusChange(2);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START /*701*/:
                        PlayerManager.this.statusChange(1);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END /*702*/:
                        PlayerManager.this.statusChange(2);
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH /*703*/:
                        Toast.makeText(activity2, "download rate:" + extra, 1).show();
                        break;
                }
                PlayerManager.this.onInfoListener.onInfo(what, extra);
                return false;
            }
        });
        this.audioManager = (AudioManager) activity2.getSystemService("audio");
        this.mMaxVolume = this.audioManager.getStreamMaxVolume(3);
        this.gestureDetector = new GestureDetector(activity2, new PlayGestureListener());
        if (this.fullScreenOnly) {
            activity2.setRequestedOrientation(0);
        }
        this.portrait = getScreenOrientation() == 1;
        if (!this.playerSupport) {
            DebugLog.m1552e("", "播放器不支持此设备");
        }
        initControlView();
        this.playerContent = activity2.findViewById(C1646R.C1648id.player_content);
        this.playerContent.setClickable(true);
        this.playerContent.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (PlayerManager.this.gestureDetector.onTouchEvent(motionEvent)) {
                    return true;
                }
                switch (motionEvent.getAction() & 255) {
                    case 1:
                        PlayerManager.this.dismissmBrightnessDialog();
                        PlayerManager.this.dismissVolumeDialog();
                        PlayerManager.this.dismissProgressDialog();
                        break;
                }
                return false;
            }
        });
        final boolean isPortrait = getScreenOrientation() == 1;
        this.orientationEventListener = new OrientationEventListener(activity2) {
            public void onOrientationChanged(int orientation) {
                if ((orientation < 0 || orientation > 30) && orientation < 330 && (orientation < 150 || orientation > 210)) {
                    if (((orientation >= 90 && orientation <= 120) || (orientation >= 240 && orientation <= 300)) && !isPortrait) {
                        activity2.setRequestedOrientation(4);
                        PlayerManager.this.orientationEventListener.disable();
                    }
                } else if (isPortrait) {
                    activity2.setRequestedOrientation(4);
                    PlayerManager.this.orientationEventListener.disable();
                }
            }
        };
        this.orientationEventListener.enable();
        if (Build.VERSION.SDK_INT >= 19) {
            activity2.getWindow().getDecorView().setSystemUiVisibility(5894);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.mSp = PreferenceManager.getDefaultSharedPreferences(activity2);
        this.mMap = new HashMap();
    }

    private void initControlView() {
        this.topRl = (RelativeLayout) this.activity.findViewById(C1646R.C1648id.top_layout);
        this.startLl = (LinearLayout) this.activity.findViewById(C1646R.C1648id.start_layout);
        this.bottomLl = (LinearLayout) this.activity.findViewById(C1646R.C1648id.bottom_layout);
        this.loadingProgressBar = (ProgressBar) this.activity.findViewById(C1646R.C1648id.loading);
        this.currentTv = (TextView) this.activity.findViewById(C1646R.C1648id.current);
        this.totalTv = (TextView) this.activity.findViewById(C1646R.C1648id.total);
        this.bottomSeekBar = (SeekBar) this.activity.findViewById(C1646R.C1648id.bottom_seek_progress);
        this.bottomSeekBar.setOnSeekBarChangeListener(this.seekBarChangeListener);
        this.backIv = (ImageView) this.activity.findViewById(C1646R.C1648id.back);
        this.startIv = (ImageView) this.activity.findViewById(C1646R.C1648id.start);
        this.lastIv = (ImageView) this.activity.findViewById(C1646R.C1648id.last);
        this.playPauseIv = (ImageView) this.activity.findViewById(C1646R.C1648id.play_pause);
        this.nextIv = (ImageView) this.activity.findViewById(C1646R.C1648id.next);
        this.backIv.setOnClickListener(this);
        this.startIv.setOnClickListener(this);
        this.lastIv.setOnClickListener(this);
        this.playPauseIv.setOnClickListener(this);
        this.nextIv.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBusCome(UpEvent event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(UpEvent event) {
        switch (event.getCode()) {
            case PpEventConstant.STATUS_PTZ_SEEK_BEFORE /*1365*/:
                Log.e("ViltaX", "--0x777--" + getClass().toString());
                this.mUiHandler.sendEmptyMessage(8);
                return;
            case PpEventConstant.FM210_VIDEO_PLAY_OR_PAUSE /*1638*/:
                Log.e("ViltaX", "--0x666--" + getClass().toString());
                this.mUiHandler.sendEmptyMessage(5);
                return;
            case PpEventConstant.STATUS_PTZ_SEEK_AFTER /*1911*/:
                Log.e("ViltaX", "--0x888--" + getClass().toString());
                this.mUiHandler.sendEmptyMessage(7);
                return;
            case PpEventConstant.FM210_VIDEO_BACK /*2457*/:
                Log.e("ViltaX", "--0x999--" + getClass().toString());
                this.mUiHandler.sendEmptyMessage(6);
                return;
            default:
                return;
        }
    }

    private int getScreenOrientation() {
        int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (((rotation == 0 || rotation == 2) && height > width) || ((rotation == 1 || rotation == 3) && width > height)) {
            switch (rotation) {
                case 0:
                    return 1;
                case 1:
                    return 0;
                case 2:
                    return 9;
                case 3:
                    return 8;
                default:
                    return 1;
            }
        } else {
            switch (rotation) {
                case 0:
                    return 0;
                case 1:
                    return 1;
                case 2:
                    return 8;
                case 3:
                    return 9;
                default:
                    return 0;
            }
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == C1646R.C1648id.back) {
            this.activity.finish();
        } else if (i == C1646R.C1648id.play_pause || i == C1646R.C1648id.start) {
            Log.e("upplayer", "---status: " + this.status);
            if (this.status == 2) {
                this.startIv.setImageResource(C1646R.mipmap.up_play_mid);
                this.playPauseIv.setImageResource(C1646R.mipmap.up_play);
                statusChange(3);
                this.videoView.pause();
            } else if (this.status == 3) {
                this.startIv.setImageResource(C1646R.mipmap.up_pause_mid);
                this.playPauseIv.setImageResource(C1646R.mipmap.up_pause);
                statusChange(2);
                this.videoView.start();
            } else if (this.status == 4) {
                this.startIv.setImageResource(C1646R.mipmap.up_pause_mid);
                this.playPauseIv.setImageResource(C1646R.mipmap.up_pause);
                statusChange(2);
                this.videoView.release(false);
                IjkVideoView ijkVideoView = this.videoView;
                IjkVideoView ijkVideoView2 = this.videoView;
                ijkVideoView.setRender(2);
                play(this.urls.get(this.mCurrentPosition));
            }
        } else if (i == C1646R.C1648id.last) {
            if (this.mCurrentPosition >= 1) {
                this.mCurrentPosition--;
                statusChange(4);
                this.videoView.release(false);
                IjkVideoView ijkVideoView3 = this.videoView;
                IjkVideoView ijkVideoView4 = this.videoView;
                ijkVideoView3.setRender(2);
                releaseProgressTimer();
                play(this.urls.get(this.mCurrentPosition));
                this.startIv.setImageResource(C1646R.mipmap.up_pause_mid);
                this.playPauseIv.setImageResource(C1646R.mipmap.up_pause);
            } else if (this.mCurrentPosition == 0) {
                Toast.makeText(this.activity, this.activity.getString(C1646R.string.toast_at_first_video), 1).show();
            }
        } else if (i != C1646R.C1648id.next) {
        } else {
            if (this.mCurrentPosition < this.urls.size() - 1) {
                this.mCurrentPosition++;
                statusChange(4);
                this.videoView.release(false);
                IjkVideoView ijkVideoView5 = this.videoView;
                IjkVideoView ijkVideoView6 = this.videoView;
                ijkVideoView5.setRender(2);
                releaseProgressTimer();
                play(this.urls.get(this.mCurrentPosition));
                this.startIv.setImageResource(C1646R.mipmap.up_pause_mid);
                this.playPauseIv.setImageResource(C1646R.mipmap.up_pause);
            } else if (this.mCurrentPosition == this.urls.size() - 1) {
                Toast.makeText(this.activity, this.activity.getString(C1646R.string.toast_at_last_video), 1).show();
            }
        }
    }

    public void play(List<String> urls2, int defaultUrl) {
        this.urls = urls2;
        this.mCurrentPosition = defaultUrl;
        play(urls2.get(defaultUrl));
    }

    private void selectRightPlayer(String url2) {
        int playerType;
        MediaExtractor extractor = new MediaExtractor();
        int frameRate = 30;
        int rotation = 0;
        try {
            extractor.setDataSource(url2);
            int numTracks = extractor.getTrackCount();
            for (int i = 0; i < numTracks; i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                if (format.getString(IMediaFormat.KEY_MIME).startsWith("video/") && format.containsKey("frame-rate") && this.mMap.get("frame-rate") != null && this.mMap.get("rotation-degrees") != null) {
                    frameRate = ((Integer) this.mMap.get("frame-rate")).intValue();
                    rotation = ((Integer) this.mMap.get("rotation-degrees")).intValue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            extractor.release();
        }
        Log.e("Player", "--frameRate--" + frameRate + " fps--rotation:" + rotation);
        if (frameRate > 60) {
            playerType = 2;
        } else {
            playerType = 3;
        }
        SharedPreferences.Editor editor = this.mSp.edit();
        editor.putInt(this.activity.getString(C1646R.string.pref_key_player), playerType);
        editor.commit();
    }

    /* renamed from: com.ny.ijk.upplayer.media.PlayerManager$PlayGestureListener */
    public class PlayGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean toSeek;
        private boolean volumeControl;

        public PlayGestureListener() {
        }

        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        public boolean onDown(MotionEvent e) {
            this.firstTouch = true;
            return super.onDown(e);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean z = true;
            float mOldX = e1.getX();
            float mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            Log.e("upplayer", "mOldX: " + mOldX + "--mOldY: " + mOldY);
            Log.e("upplayer", "deltaX: " + deltaX + "--deltaY: " + deltaY);
            if (this.firstTouch) {
                this.toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                if (mOldX <= ((float) PlayerManager.this.screenWidthPixels) * 0.5f) {
                    z = false;
                }
                this.volumeControl = z;
                this.firstTouch = false;
            }
            Log.e("upplayer", "toSeek--" + this.toSeek);
            if (!this.toSeek) {
                float percent = deltaY / ((float) PlayerManager.this.videoView.getHeight());
                if (this.volumeControl) {
                    PlayerManager.this.onVolumeSlide(percent);
                } else {
                    PlayerManager.this.onBrightnessSlide(percent);
                }
            } else if (!PlayerManager.this.isLive) {
                PlayerManager.this.onProgressSlide((-deltaX) / ((float) PlayerManager.this.videoView.getWidth()));
                Log.e("upplayer", "percent: " + ((-deltaX) / ((float) PlayerManager.this.videoView.getWidth())));
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        public boolean onSingleTapUp(MotionEvent e) {
            PlayerManager.this.controlViewTimer.start();
            if (PlayerManager.this.mControlVisiable) {
                PlayerManager.this.setControlViewVisiable(false);
                PlayerManager.this.controlViewTimer.cancel();
            } else {
                PlayerManager.this.setControlViewVisiable(true);
                PlayerManager.this.controlViewTimer.start();
            }
            return true;
        }
    }

    public boolean isPlayerSupport() {
        return this.playerSupport;
    }

    public boolean isPlaying() {
        if (this.videoView != null) {
            return this.videoView.isPlaying();
        }
        return false;
    }

    public void stop() {
        this.videoView.stopPlayback();
    }

    public int getCurrentPosition() {
        return this.videoView.getCurrentPosition();
    }

    public IjkVideoView getVideoView() {
        if (this.videoView != null) {
            return this.videoView;
        }
        return null;
    }

    public int getDuration() {
        return this.videoView.getDuration();
    }

    public PlayerManager playerInFullScreen(boolean fullScreen) {
        if (fullScreen) {
            this.activity.setRequestedOrientation(0);
        }
        return this;
    }

    /* access modifiers changed from: private */
    public void statusChange(int newStatus) {
        this.status = newStatus;
        if (!this.isLive && newStatus == 4) {
            Log.e("upplayer", "statusChange STATUS_COMPLETED...");
            if (this.playerStateListener != null) {
                this.playerStateListener.onComplete();
                releaseProgressTimer();
            }
        } else if (newStatus == -1) {
            Log.e("upplayer", "statusChange STATUS_ERROR...");
            if (this.playerStateListener != null) {
                this.playerStateListener.onError();
                releaseProgressTimer();
            }
        } else if (newStatus == 1) {
            Log.e("upplayer", "statusChange STATUS_LOADING...");
            if (this.playerStateListener != null) {
                this.playerStateListener.onLoading();
            }
        } else if (newStatus == 2) {
            Log.e("upplayer", "statusChange STATUS_PLAYING...");
            if (this.playerStateListener != null) {
                this.playerStateListener.onPlay();
            }
        }
    }

    private void onPause() {
        this.pauseTime = System.currentTimeMillis();
        if (this.status == 2) {
            this.videoView.pause();
            if (!this.isLive) {
                this.currentPosition = this.videoView.getCurrentPosition();
            }
        }
    }

    public void onResume() {
        this.pauseTime = 0;
        if (this.status == 2) {
            if (this.isLive) {
                this.videoView.seekTo(0);
            } else if (this.currentPosition > 0) {
                this.videoView.seekTo(this.currentPosition);
            }
            this.videoView.start();
        }
    }

    /* access modifiers changed from: private */
    public void setControlViewVisiable(boolean isVisiable) {
        int visibility;
        if (isVisiable) {
            visibility = 0;
            this.mControlVisiable = true;
        } else {
            visibility = 8;
            this.mControlVisiable = false;
        }
        this.startLl.setVisibility(visibility);
        this.bottomLl.setVisibility(visibility);
    }

    public void onDestroy() {
        this.orientationEventListener.disable();
        this.videoView.stopPlayback();
        releaseProgressTimer();
        EventBus.getDefault().unregister(this);
    }

    public void play(String url2) {
        this.url = url2;
        if (this.playerSupport) {
            selectRightPlayer(this.urls.get(this.mCurrentPosition));
            this.videoView.setVideoPath(url2);
            this.videoView.start();
            this.status = 2;
            startProgressTimer();
            this.controlViewTimer.start();
        }
    }

    /* access modifiers changed from: private */
    public String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)});
        }
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
    }

    /* access modifiers changed from: private */
    public void onVolumeSlide(float percent) {
        setControlViewVisiable(false);
        if (this.volume == -1) {
            this.volume = this.audioManager.getStreamVolume(3);
            if (this.volume < 0) {
                this.volume = 0;
            }
        }
        int index = (int) ((((float) this.mMaxVolume) * percent) + ((float) this.volume));
        if (index > this.mMaxVolume) {
            index = this.mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        this.audioManager.setStreamVolume(3, index, 0);
        int i = (int) (((((double) index) * 1.0d) / ((double) this.mMaxVolume)) * 100.0d);
        showVolumeDialog(i);
        String s = i + "%";
        if (i == 0) {
            s = Constants.FOCUS_MODE_OFF;
        }
        DebugLog.m1550d("", "onVolumeSlide:" + s);
    }

    /* access modifiers changed from: private */
    public void onProgressSlide(float percent) {
        setControlViewVisiable(false);
        long position = (long) this.videoView.getCurrentPosition();
        long duration = (long) this.videoView.getDuration();
        long deltaMax = Math.min(100000, duration - position);
        long delta = (long) (((float) deltaMax) * percent);
        this.newPosition = delta + position;
        Log.e("upplayer", "position: " + position + "--duration:  " + duration + "--deltaMax:  " + deltaMax + "--delta:  " + delta + "--newPosition: " + this.newPosition);
        if (this.newPosition > duration) {
            this.newPosition = duration;
        } else if (this.newPosition < 0) {
            this.newPosition = 0;
            delta = -position;
        }
        int showDelta = (int) (delta / 100);
        showProgressDialog(showDelta, (int) ((100 * position) / duration));
        Log.e("upplayer", "showDelta: " + showDelta);
        if (showDelta != 0) {
            String text = showDelta > 0 ? Marker.ANY_NON_NULL_MARKER + showDelta : "" + showDelta;
            this.videoView.seekTo((int) this.newPosition);
            Log.e("upplayer", "onProgressSlide:" + text);
        }
    }

    private void showmBrightnessDialog(int brightnessPercent) {
        if (this.mBrightnessDialog == null) {
            View localview = LayoutInflater.from(this.activity).inflate(C1646R.layout.up_dialog_brightness, (ViewGroup) null);
            this.mDialogBrightnessProgressBar = (ProgressBar) localview.findViewById(C1646R.C1648id.brightness_progressbar);
            this.mDialogBrightnessTv = (TextView) localview.findViewById(C1646R.C1648id.tv_brightness);
            this.mBrightnessDialog = createDialogWithView(localview);
        }
        if (!this.mBrightnessDialog.isShowing()) {
            this.mBrightnessDialog.show();
        }
        if (brightnessPercent > 100) {
            brightnessPercent = 100;
        } else if (brightnessPercent < 0) {
            brightnessPercent = 0;
        }
        this.mDialogBrightnessTv.setText(brightnessPercent + "%");
        this.mDialogBrightnessProgressBar.setProgress(brightnessPercent);
    }

    /* access modifiers changed from: private */
    public void dismissmBrightnessDialog() {
        if (this.mBrightnessDialog != null) {
            this.mBrightnessDialog.dismiss();
        }
    }

    private void showVolumeDialog(int volumePercent) {
        if (this.mVolumeDialog == null) {
            View localview = LayoutInflater.from(this.activity).inflate(C1646R.layout.up_dialog_volume, (ViewGroup) null);
            this.mDialogVolumeProgressBar = (ProgressBar) localview.findViewById(C1646R.C1648id.volume_progressbar);
            this.mDialogVolumeTv = (TextView) localview.findViewById(C1646R.C1648id.tv_volume);
            this.mVolumeDialog = createDialogWithView(localview);
        }
        if (!this.mVolumeDialog.isShowing()) {
            this.mVolumeDialog.show();
        }
        if (volumePercent > 100) {
            volumePercent = 100;
        } else if (volumePercent < 0) {
            volumePercent = 0;
        }
        this.mDialogVolumeTv.setText(volumePercent + "%");
        this.mDialogVolumeProgressBar.setProgress(volumePercent);
    }

    /* access modifiers changed from: private */
    public void dismissVolumeDialog() {
        if (this.mVolumeDialog != null) {
            this.mVolumeDialog.dismiss();
        }
    }

    private void showProgressDialog(int showDelta, int progress) {
        if (this.mProgressDialog == null) {
            View localview = LayoutInflater.from(this.activity).inflate(C1646R.layout.up_dialog_progress, (ViewGroup) null);
            this.mDialogProgressBar = (ProgressBar) localview.findViewById(C1646R.C1648id.duration_progressbar);
            this.mDialogSeekTimeTv = (TextView) localview.findViewById(C1646R.C1648id.tv_current);
            this.mDialogTotalTimeTv = (TextView) localview.findViewById(C1646R.C1648id.tv_duration);
            this.mDialogIconIv = (ImageView) localview.findViewById(C1646R.C1648id.duration_image_tip);
            this.mProgressDialog = createDialogWithView(localview);
        }
        if (!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }
        this.mDialogSeekTimeTv.setText(generateTime((long) ((getDuration() * progress) / 100)));
        this.mDialogTotalTimeTv.setText(this.totalTv.getText());
        this.mDialogProgressBar.setProgress(progress);
        if (showDelta > 0) {
            this.mDialogIconIv.setBackgroundResource(C1646R.mipmap.up_forward_icon);
        } else {
            this.mDialogIconIv.setBackgroundResource(C1646R.mipmap.up_backward_icon);
        }
    }

    /* access modifiers changed from: private */
    public void dismissProgressDialog() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }

    private Dialog createDialogWithView(View localview) {
        Dialog dialog = new Dialog(this.activity, C1646R.style.up_style_dialog_progress);
        dialog.setContentView(localview);
        Window window = dialog.getWindow();
        window.addFlags(8);
        window.addFlags(32);
        window.addFlags(16);
        window.setLayout(-2, -2);
        window.setAttributes(window.getAttributes());
        return dialog;
    }

    /* access modifiers changed from: private */
    public void onBrightnessSlide(float percent) {
        setControlViewVisiable(false);
        if (this.brightness < 0.0f) {
            this.brightness = this.activity.getWindow().getAttributes().screenBrightness;
            if (this.brightness <= 0.0f) {
                this.brightness = 0.5f;
            } else if (this.brightness < 0.01f) {
                this.brightness = 0.01f;
            }
        }
        DebugLog.m1550d("", "brightness:" + this.brightness + ",percent:" + percent);
        WindowManager.LayoutParams lpa = this.activity.getWindow().getAttributes();
        lpa.screenBrightness = this.brightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        this.activity.getWindow().setAttributes(lpa);
        showmBrightnessDialog((int) ((this.brightness + percent) * 100.0f));
    }

    public void setFullScreenOnly(boolean fullScreenOnly2) {
        this.fullScreenOnly = fullScreenOnly2;
        tryFullScreen(fullScreenOnly2);
        if (fullScreenOnly2) {
            this.activity.setRequestedOrientation(0);
        } else {
            this.activity.setRequestedOrientation(4);
        }
    }

    private void tryFullScreen(boolean fullScreen) {
        ActionBar supportActionBar;
        if ((this.activity instanceof AppCompatActivity) && (supportActionBar = ((AppCompatActivity) this.activity).getSupportActionBar()) != null) {
            if (fullScreen) {
                supportActionBar.hide();
            } else {
                supportActionBar.show();
            }
            setFullScreen(fullScreen);
        }
    }

    private void setFullScreen(boolean fullScreen) {
        if (this.activity != null) {
            WindowManager.LayoutParams attrs = this.activity.getWindow().getAttributes();
            if (fullScreen) {
                attrs.flags |= 1024;
                this.activity.getWindow().setAttributes(attrs);
                this.activity.getWindow().addFlags(512);
                this.activity.getWindow().addFlags(C1907C.SAMPLE_FLAG_DECODE_ONLY);
                return;
            }
            attrs.flags &= -1025;
            this.activity.getWindow().setAttributes(attrs);
            this.activity.getWindow().clearFlags(512);
        }
    }

    public void setScaleType(String scaleType) {
        if (SCALETYPE_FITPARENT.equals(scaleType)) {
            this.videoView.setAspectRatio(0);
        } else if (SCALETYPE_FILLPARENT.equals(scaleType)) {
            this.videoView.setAspectRatio(1);
        } else if (SCALETYPE_WRAPCONTENT.equals(scaleType)) {
            this.videoView.setAspectRatio(2);
        } else if (SCALETYPE_FITXY.equals(scaleType)) {
            this.videoView.setAspectRatio(3);
        } else if (SCALETYPE_16_9.equals(scaleType)) {
            this.videoView.setAspectRatio(4);
        } else if (SCALETYPE_4_3.equals(scaleType)) {
            this.videoView.setAspectRatio(5);
        }
    }

    public void start() {
        this.videoView.start();
    }

    public void pause() {
        this.videoView.pause();
    }

    public boolean onBackPressed() {
        if (this.fullScreenOnly || getScreenOrientation() != 0) {
            return false;
        }
        this.activity.setRequestedOrientation(1);
        return true;
    }

    /* renamed from: com.ny.ijk.upplayer.media.PlayerManager$ProgressTimerTask */
    public class ProgressTimerTask extends TimerTask {
        public ProgressTimerTask() {
        }

        public void run() {
            if (PlayerManager.this.status == 2 || PlayerManager.this.status == 3) {
                String total = PlayerManager.this.generateTime((long) PlayerManager.this.getDuration());
                String current = PlayerManager.this.generateTime((long) PlayerManager.this.getCurrentPosition());
                int position = 0;
                int duration = 0;
                if (!total.equals("00:00")) {
                    String unused = PlayerManager.this.mTotalStr = total;
                    duration = PlayerManager.this.getDuration();
                }
                if (!current.equals("00:00")) {
                    String unused2 = PlayerManager.this.mPositionStr = current;
                    position = PlayerManager.this.getCurrentPosition();
                }
                if (!(duration == 0 || position == 0)) {
                    int unused3 = PlayerManager.this.mProgress = (PlayerManager.this.getCurrentPosition() * 100) / PlayerManager.this.getDuration();
                }
                PlayerManager.this.mTimeHandler.sendEmptyMessage(1);
            }
        }
    }

    public void startProgressTimer() {
        UPDATE_PROGRESS_TIMER = new Timer();
        this.mProgressTimerTask = new ProgressTimerTask();
        UPDATE_PROGRESS_TIMER.schedule(this.mProgressTimerTask, 0, 300);
    }

    public void releaseProgressTimer() {
        UPDATE_PROGRESS_TIMER = null;
        this.mProgressTimerTask = null;
        this.mTotalStr = "00:00";
        this.mPositionStr = "00:00";
        this.mProgress = 0;
        this.mTimeHandler.removeMessages(1);
    }

    /* access modifiers changed from: private */
    public void changeTimeAndProgress() {
        this.totalTv.setText(this.mTotalStr);
        this.currentTv.setText(this.mPositionStr);
        this.bottomSeekBar.setProgress(this.mProgress);
    }

    /* renamed from: com.ny.ijk.upplayer.media.PlayerManager$Query */
    class Query {
        private final Activity activity;
        private View view;

        Query(Activity activity2) {
            this.activity = activity2;
        }

        /* renamed from: id */
        public Query mo16649id(int id) {
            this.view = this.activity.findViewById(id);
            return this;
        }

        public Query image(int resId) {
            if (this.view instanceof ImageView) {
                ((ImageView) this.view).setImageResource(resId);
            }
            return this;
        }

        public Query visible() {
            if (this.view != null) {
                this.view.setVisibility(0);
            }
            return this;
        }

        public Query gone() {
            if (this.view != null) {
                this.view.setVisibility(8);
            }
            return this;
        }

        public Query invisible() {
            if (this.view != null) {
                this.view.setVisibility(4);
            }
            return this;
        }

        public Query clicked(View.OnClickListener handler) {
            if (this.view != null) {
                this.view.setOnClickListener(handler);
            }
            return this;
        }

        public Query text(CharSequence text) {
            if (this.view != null && (this.view instanceof TextView)) {
                ((TextView) this.view).setText(text);
            }
            return this;
        }

        public Query visibility(int visible) {
            if (this.view != null) {
                this.view.setVisibility(visible);
            }
            return this;
        }

        private void size(boolean width, int n, boolean dip) {
            if (this.view != null) {
                ViewGroup.LayoutParams lp = this.view.getLayoutParams();
                if (n > 0 && dip) {
                    n = dip2pixel(this.activity, (float) n);
                }
                if (width) {
                    lp.width = n;
                } else {
                    lp.height = n;
                }
                this.view.setLayoutParams(lp);
            }
        }

        public void height(int height, boolean dip) {
            size(false, height, dip);
        }

        private int dip2pixel(Context context, float n) {
            return (int) TypedValue.applyDimension(1, n, context.getResources().getDisplayMetrics());
        }

        private float pixel2dip(Context context, float n) {
            return n / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
        }
    }

    public PlayerManager onError(OnErrorListener onErrorListener2) {
        this.onErrorListener = onErrorListener2;
        return this;
    }

    public PlayerManager onComplete(OnCompleteListener onCompleteListener2) {
        this.onCompleteListener = onCompleteListener2;
        return this;
    }

    public PlayerManager onInfo(OnInfoListener onInfoListener2) {
        this.onInfoListener = onInfoListener2;
        return this;
    }

    public PlayerManager onControlPanelVisiblityChange(OnControlPanelVisibilityChangeListener onControlPanelVisibilityChangeListener2) {
        this.onControlPanelVisibilityChangeListener = onControlPanelVisibilityChangeListener2;
        return this;
    }

    public PlayerManager live(boolean isLive2) {
        this.isLive = isLive2;
        return this;
    }

    public PlayerManager toggleAspectRatio() {
        if (this.videoView != null) {
            this.videoView.toggleAspectRatio();
        }
        return this;
    }
}
