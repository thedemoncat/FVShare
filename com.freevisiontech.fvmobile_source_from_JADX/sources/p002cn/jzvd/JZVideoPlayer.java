package p002cn.jzvd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.p003v7.app.ActionBar;
import android.support.p003v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.exoplayer.C1907C;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: cn.jzvd.JZVideoPlayer */
public abstract class JZVideoPlayer extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {
    public static boolean ACTION_BAR_EXIST = true;
    public static long CLICK_QUIT_FULLSCREEN_TIME = 0;
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6;
    public static final int CURRENT_STATE_ERROR = 7;
    public static final int CURRENT_STATE_NORMAL = 0;
    public static final int CURRENT_STATE_PAUSE = 5;
    public static final int CURRENT_STATE_PLAYING = 3;
    public static final int CURRENT_STATE_PREPARING = 1;
    public static final int CURRENT_STATE_PREPARING_CHANGING_URL = 2;
    public static int FULLSCREEN_ORIENTATION = 4;
    public static final int FULL_SCREEN_NORMAL_DELAY = 300;
    protected static JZUserAction JZ_USER_EVENT = null;
    public static int NORMAL_ORIENTATION = 1;
    public static boolean SAVE_PROGRESS = true;
    public static final int SCREEN_WINDOW_FULLSCREEN = 2;
    public static final int SCREEN_WINDOW_LIST = 1;
    public static final int SCREEN_WINDOW_NORMAL = 0;
    public static final int SCREEN_WINDOW_TINY = 3;
    public static final String TAG = "JiaoZiVideoPlayer";
    public static final int THRESHOLD = 80;
    public static boolean TOOL_BAR_EXIST = true;
    protected static Timer UPDATE_PROGRESS_TIMER = null;
    public static final String URL_KEY_DEFAULT = "URL_KEY_DEFAULT";
    public static int VIDEO_IMAGE_DISPLAY_TYPE = 0;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER = 0;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT = 1;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP = 2;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL = 3;
    public static boolean WIFI_TIP_DIALOG_SHOWED = false;
    public static long lastAutoFullscreenTime = 0;
    private static Class mClass = null;
    private static Context mContext = null;
    private static int mCurrentPosition = 0;
    private static List<String> mVideoTitles;
    private static List<String> mVideoUris;
    public static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case -2:
                    try {
                        JZVideoPlayer player = JZVideoPlayerManager.getCurrentJzvd();
                        if (player != null && player.currentState == 3) {
                            player.startButton.performClick();
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    Log.d("JiaoZiVideoPlayer", "AUDIOFOCUS_LOSS_TRANSIENT [" + hashCode() + "]");
                    return;
                case -1:
                    JZVideoPlayer.releaseAllVideos();
                    Log.d("JiaoZiVideoPlayer", "AUDIOFOCUS_LOSS [" + hashCode() + "]");
                    return;
                default:
                    return;
            }
        }
    };
    public ViewGroup bottomContainer;
    public int currentScreen = -1;
    public int currentState = -1;
    public TextView currentTimeTextView;
    public int currentUrlMapIndex = 0;
    public Object[] dataSourceObjects;
    public ImageView fullscreenButton;
    public int heightRatio = 0;
    public ImageView lastBtn;
    protected AudioManager mAudioManager;
    protected boolean mChangeBrightness;
    protected boolean mChangePosition;
    protected boolean mChangeVolume;
    protected float mDownX;
    protected float mDownY;
    protected float mGestureDownBrightness;
    protected long mGestureDownPosition;
    protected int mGestureDownVolume;
    protected ProgressTimerTask mProgressTimerTask;
    protected int mScreenHeight;
    protected int mScreenWidth;
    protected long mSeekTimePosition;
    protected boolean mTouchingProgressBar;
    public ImageView nextBtn;
    public Object[] objects = null;
    public ImageView playPauseBtn;
    public int positionInList = -1;
    public SeekBar progressBar;
    public long seekToInAdvance = 0;
    public ImageView startButton;
    public ViewGroup textureViewContainer;
    boolean tmp_test_back = false;
    public ViewGroup topContainer;
    public TextView totalTimeTextView;
    public int videoRotation = 0;
    public int widthRatio = 0;

    public abstract int getLayoutId();

    public JZVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public JZVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public static void releaseAllVideos() {
        if (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME > 300) {
            Log.d("JiaoZiVideoPlayer", "releaseAllVideos");
            JZVideoPlayerManager.completeAll();
            JZMediaManager.instance().positionInList = -1;
            JZMediaManager.instance().releaseMediaPlayer();
        }
    }

    public static void startFullscreen(Context context, Class _class, String url, List<String> videoUris, int defaultPosition) {
        mVideoUris = videoUris;
        mContext = context;
        mClass = _class;
        mCurrentPosition = defaultPosition;
        LinkedHashMap map = new LinkedHashMap();
        map.put(URL_KEY_DEFAULT, url);
        startFullscreen(context, _class, new Object[]{map}, 0, "");
    }

    public static void startFullscreen(Context context, Class _class, String url, Object... objects2) {
        LinkedHashMap map = new LinkedHashMap();
        map.put(URL_KEY_DEFAULT, url);
        startFullscreen(context, _class, new Object[]{map}, 0, objects2);
    }

    public static void startFullscreen(Context context, Class _class, Object[] dataSourceObjects2, int defaultUrlMapIndex, Object... objects2) {
        hideSupportActionBar(context);
        JZUtils.setRequestedOrientation(context, FULLSCREEN_ORIENTATION);
        ViewGroup vp = (ViewGroup) JZUtils.scanForActivity(context).findViewById(16908290);
        View old = vp.findViewById(C0783R.C0785id.jz_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        try {
            JZVideoPlayer jzVideoPlayer = _class.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            jzVideoPlayer.setId(C0783R.C0785id.jz_fullscreen_id);
            vp.addView(jzVideoPlayer, new FrameLayout.LayoutParams(-1, -1));
            jzVideoPlayer.setUp(dataSourceObjects2, defaultUrlMapIndex, 2, objects2);
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            jzVideoPlayer.startButton.performClick();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean backPress() {
        Log.i("JiaoZiVideoPlayer", "backPress");
        if (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME < 300) {
            return false;
        }
        if (JZVideoPlayerManager.getSecondFloor() != null) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            if (JZUtils.dataSourceObjectsContainsUri(JZVideoPlayerManager.getFirstFloor().dataSourceObjects, JZMediaManager.getCurrentDataSource())) {
                JZVideoPlayer jzVideoPlayer = JZVideoPlayerManager.getSecondFloor();
                jzVideoPlayer.onEvent(jzVideoPlayer.currentScreen == 2 ? 8 : 10);
                JZVideoPlayerManager.getFirstFloor().playOnThisJzvd();
            } else {
                quitFullscreenOrTinyWindow();
            }
            return true;
        } else if (JZVideoPlayerManager.getFirstFloor() == null) {
            return false;
        } else {
            if (JZVideoPlayerManager.getFirstFloor().currentScreen != 2 && JZVideoPlayerManager.getFirstFloor().currentScreen != 3) {
                return false;
            }
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            quitFullscreenOrTinyWindow();
            return true;
        }
    }

    public static void quitFullscreenOrTinyWindow() {
        JZVideoPlayerManager.getFirstFloor().clearFloatScreen();
        JZMediaManager.instance().releaseMediaPlayer();
        JZVideoPlayerManager.completeAll();
    }

    @SuppressLint({"RestrictedApi"})
    public static void showSupportActionBar(Context context) {
        ActionBar ab;
        if (!(!ACTION_BAR_EXIST || JZUtils.getAppCompActivity(context) == null || (ab = JZUtils.getAppCompActivity(context).getSupportActionBar()) == null)) {
            ab.setShowHideAnimationEnabled(false);
            ab.show();
        }
        if (TOOL_BAR_EXIST) {
            JZUtils.getWindow(context).clearFlags(1024);
        }
    }

    @SuppressLint({"RestrictedApi"})
    public static void hideSupportActionBar(Context context) {
        ActionBar ab;
        if (!(!ACTION_BAR_EXIST || JZUtils.getAppCompActivity(context) == null || (ab = JZUtils.getAppCompActivity(context).getSupportActionBar()) == null)) {
            ab.setShowHideAnimationEnabled(false);
            ab.hide();
        }
        if (TOOL_BAR_EXIST) {
            JZUtils.getWindow(context).setFlags(1024, 1024);
        }
    }

    public static void clearSavedProgress(Context context, String url) {
        JZUtils.clearSavedProgress(context, url);
    }

    public static void setJzUserAction(JZUserAction jzUserEvent) {
        JZ_USER_EVENT = jzUserEvent;
    }

    public static void goOnPlayOnResume() {
        if (JZVideoPlayerManager.getCurrentJzvd() != null) {
            JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState == 5) {
                jzvd.onStatePlaying();
                JZMediaManager.start();
            }
        }
    }

    public static void goOnPlayOnPause() {
        if (JZVideoPlayerManager.getCurrentJzvd() != null) {
            JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState != 6 && jzvd.currentState != 0 && jzvd.currentState != 7) {
                jzvd.onStatePause();
                JZMediaManager.pause();
            }
        }
    }

    public static void onScrollAutoTiny(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = JZMediaManager.instance().positionInList;
        if (currentPlayPosition < 0) {
            return;
        }
        if (currentPlayPosition < firstVisibleItem || currentPlayPosition > lastVisibleItem - 1) {
            if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentScreen != 3 && JZVideoPlayerManager.getCurrentJzvd().currentScreen != 2) {
                if (JZVideoPlayerManager.getCurrentJzvd().currentState == 5) {
                    releaseAllVideos();
                    return;
                }
                Log.e("JiaoZiVideoPlayer", "onScroll: out screen");
                JZVideoPlayerManager.getCurrentJzvd().startWindowTiny();
            }
        } else if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentScreen == 3) {
            Log.e("JiaoZiVideoPlayer", "onScroll: into screen");
            backPress();
        }
    }

    public static void onScrollReleaseAllVideos(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = JZMediaManager.instance().positionInList;
        Log.e("JiaoZiVideoPlayer", "onScrollReleaseAllVideos: " + currentPlayPosition + " " + firstVisibleItem + " " + currentPlayPosition + " " + lastVisibleItem);
        if (currentPlayPosition < 0) {
            return;
        }
        if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > lastVisibleItem - 1) && JZVideoPlayerManager.getCurrentJzvd().currentScreen != 2) {
            releaseAllVideos();
        }
    }

    public static void onChildViewAttachedToWindow(View view, int jzvdId) {
        JZVideoPlayer videoPlayer;
        if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentScreen == 3 && (videoPlayer = (JZVideoPlayer) view.findViewById(jzvdId)) != null && JZUtils.getCurrentFromDataSource(videoPlayer.dataSourceObjects, videoPlayer.currentUrlMapIndex).equals(JZMediaManager.getCurrentDataSource())) {
            backPress();
        }
    }

    public static void onChildViewDetachedFromWindow(View view) {
        if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentScreen != 3) {
            JZVideoPlayer videoPlayer = JZVideoPlayerManager.getCurrentJzvd();
            if (((ViewGroup) view).indexOfChild(videoPlayer) == -1) {
                return;
            }
            if (videoPlayer.currentState == 5) {
                releaseAllVideos();
            } else {
                videoPlayer.startWindowTiny();
            }
        }
    }

    public static void setTextureViewRotation(int rotation) {
        if (JZMediaManager.textureView != null) {
            JZMediaManager.textureView.setRotation((float) rotation);
        }
    }

    public static void setVideoImageDisplayType(int type) {
        VIDEO_IMAGE_DISPLAY_TYPE = type;
        if (JZMediaManager.textureView != null) {
            JZMediaManager.textureView.requestLayout();
        }
    }

    public Object getCurrentUrl() {
        return JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex);
    }

    public void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        this.startButton = (ImageView) findViewById(C0783R.C0785id.start);
        this.fullscreenButton = (ImageView) findViewById(C0783R.C0785id.fullscreen);
        this.progressBar = (SeekBar) findViewById(C0783R.C0785id.bottom_seek_progress);
        this.currentTimeTextView = (TextView) findViewById(C0783R.C0785id.current);
        this.totalTimeTextView = (TextView) findViewById(C0783R.C0785id.total);
        this.bottomContainer = (ViewGroup) findViewById(C0783R.C0785id.layout_bottom);
        this.textureViewContainer = (ViewGroup) findViewById(C0783R.C0785id.surface_container);
        this.topContainer = (ViewGroup) findViewById(C0783R.C0785id.layout_top);
        this.lastBtn = (ImageView) findViewById(C0783R.C0785id.last);
        this.nextBtn = (ImageView) findViewById(C0783R.C0785id.next);
        this.playPauseBtn = (ImageView) findViewById(C0783R.C0785id.play_pause);
        this.startButton.setOnClickListener(this);
        this.fullscreenButton.setOnClickListener(this);
        this.progressBar.setOnSeekBarChangeListener(this);
        this.bottomContainer.setOnClickListener(this);
        this.textureViewContainer.setOnClickListener(this);
        this.textureViewContainer.setOnTouchListener(this);
        this.lastBtn.setOnClickListener(this);
        this.nextBtn.setOnClickListener(this);
        this.playPauseBtn.setOnClickListener(this);
        this.mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        this.mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        this.mAudioManager = (AudioManager) getContext().getSystemService("audio");
        try {
            if (isCurrentPlay()) {
                NORMAL_ORIENTATION = ((AppCompatActivity) context).getRequestedOrientation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUp(String url, int screen, Object... objects2) {
        LinkedHashMap map = new LinkedHashMap();
        map.put(URL_KEY_DEFAULT, url);
        setUp(new Object[]{map}, 0, screen, objects2);
    }

    public void setUp(Object[] dataSourceObjects2, int defaultUrlMapIndex, int screen, Object... objects2) {
        if (this.dataSourceObjects == null || JZUtils.getCurrentFromDataSource(dataSourceObjects2, this.currentUrlMapIndex) == null || !JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).equals(JZUtils.getCurrentFromDataSource(dataSourceObjects2, this.currentUrlMapIndex))) {
            if (isCurrentJZVD() && JZUtils.dataSourceObjectsContainsUri(dataSourceObjects2, JZMediaManager.getCurrentDataSource())) {
                long position = 0;
                try {
                    position = JZMediaManager.getCurrentPosition();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                if (position != 0) {
                    JZUtils.saveProgress(getContext(), JZMediaManager.getCurrentDataSource(), position);
                }
                JZMediaManager.instance().releaseMediaPlayer();
            } else if (isCurrentJZVD() && !JZUtils.dataSourceObjectsContainsUri(dataSourceObjects2, JZMediaManager.getCurrentDataSource())) {
                startWindowTiny();
            } else if (isCurrentJZVD() || !JZUtils.dataSourceObjectsContainsUri(dataSourceObjects2, JZMediaManager.getCurrentDataSource())) {
                if (isCurrentJZVD() || JZUtils.dataSourceObjectsContainsUri(dataSourceObjects2, JZMediaManager.getCurrentDataSource())) {
                }
            } else if (JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd().currentScreen == 3) {
                this.tmp_test_back = true;
            }
            this.dataSourceObjects = dataSourceObjects2;
            this.currentUrlMapIndex = defaultUrlMapIndex;
            this.currentScreen = screen;
            this.objects = objects2;
            onStateNormal();
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == C0783R.C0785id.start) {
            Log.i("JiaoZiVideoPlayer", "onClick start [" + hashCode() + "] ");
            if (this.dataSourceObjects == null || JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex) == null) {
                Toast.makeText(getContext(), getResources().getString(C0783R.string.no_url), 0).show();
                return;
            } else if (this.currentState == 0) {
                if (JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("file") || JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("/") || JZUtils.isWifiConnected(getContext()) || WIFI_TIP_DIALOG_SHOWED) {
                    startVideo();
                    onEvent(0);
                } else {
                    showWifiDialog();
                    return;
                }
            } else if (this.currentState == 3) {
                onEvent(3);
                Log.d("JiaoZiVideoPlayer", "pauseVideo [" + hashCode() + "] ");
                JZMediaManager.pause();
                onStatePause();
            } else if (this.currentState == 5) {
                onEvent(4);
                JZMediaManager.start();
                onStatePlaying();
            } else if (this.currentState == 6) {
                onEvent(2);
                startVideo();
            }
        } else if (i == C0783R.C0785id.fullscreen) {
            Log.i("JiaoZiVideoPlayer", "onClick fullscreen [" + hashCode() + "] ");
            if (this.currentState == 6) {
                return;
            }
            if (this.currentScreen == 2) {
                backPress();
            } else {
                Log.d("JiaoZiVideoPlayer", "toFullscreenActivity [" + hashCode() + "] ");
                onEvent(7);
                startWindowFullscreen();
            }
        }
        if (mVideoUris != null && mVideoUris.size() > 0) {
            if (i == C0783R.C0785id.last) {
                if (mCurrentPosition >= 1) {
                    mCurrentPosition--;
                    releaseAllVideos();
                    startFullscreen(mContext, mClass, mVideoUris.get(mCurrentPosition), "");
                } else if (mCurrentPosition == 0) {
                    Toast.makeText(mContext, getResources().getString(C0783R.string.toast_at_first_video), 1).show();
                }
            } else if (i == C0783R.C0785id.next) {
                if (mCurrentPosition < mVideoUris.size() - 1) {
                    mCurrentPosition++;
                    releaseAllVideos();
                    startFullscreen(mContext, mClass, mVideoUris.get(mCurrentPosition), "");
                } else if (mCurrentPosition == mVideoUris.size() - 1) {
                    Toast.makeText(mContext, getResources().getString(C0783R.string.toast_at_last_video), 1).show();
                }
            } else if (i == C0783R.C0785id.play_pause) {
                JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
                if (jzvd.currentState == 3) {
                    this.playPauseBtn.setImageResource(C0783R.C0784drawable.jz_play);
                    goOnPlayOnPause();
                } else if (jzvd.currentState == 5) {
                    this.playPauseBtn.setImageResource(C0783R.C0784drawable.jz_pause);
                    goOnPlayOnResume();
                }
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (v.getId() != C0783R.C0785id.surface_container) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                Log.i("JiaoZiVideoPlayer", "onTouch surfaceContainer actionDown [" + hashCode() + "] ");
                this.mTouchingProgressBar = true;
                this.mDownX = x;
                this.mDownY = y;
                this.mChangeVolume = false;
                this.mChangePosition = false;
                this.mChangeBrightness = false;
                return false;
            case 1:
                Log.i("JiaoZiVideoPlayer", "onTouch surfaceContainer actionUp [" + hashCode() + "] ");
                this.mTouchingProgressBar = false;
                dismissProgressDialog();
                dismissVolumeDialog();
                dismissBrightnessDialog();
                if (this.mChangePosition) {
                }
                if (this.mChangeVolume) {
                    onEvent(11);
                }
                startProgressTimer();
                return false;
            case 2:
                Log.i("JiaoZiVideoPlayer", "onTouch surfaceContainer actionMove [" + hashCode() + "] ");
                float deltaX = x - this.mDownX;
                float deltaY = y - this.mDownY;
                float absDeltaX = Math.abs(deltaX);
                float absDeltaY = Math.abs(deltaY);
                if (this.currentScreen == 2 && !this.mChangePosition && !this.mChangeVolume && !this.mChangeBrightness && (absDeltaX > 80.0f || absDeltaY > 80.0f)) {
                    cancelProgressTimer();
                    if (absDeltaX >= 80.0f) {
                        if (this.currentState != 7) {
                            this.mChangePosition = true;
                            this.mGestureDownPosition = getCurrentPositionWhenPlaying();
                        }
                    } else if (this.mDownX < ((float) this.mScreenWidth) * 0.5f) {
                        this.mChangeBrightness = true;
                        WindowManager.LayoutParams lp = JZUtils.getWindow(getContext()).getAttributes();
                        if (lp.screenBrightness < 0.0f) {
                            try {
                                this.mGestureDownBrightness = (float) Settings.System.getInt(getContext().getContentResolver(), "screen_brightness");
                                Log.i("JiaoZiVideoPlayer", "current system brightness: " + this.mGestureDownBrightness);
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            this.mGestureDownBrightness = lp.screenBrightness * 255.0f;
                            Log.i("JiaoZiVideoPlayer", "current activity brightness: " + this.mGestureDownBrightness);
                        }
                    } else {
                        this.mChangeVolume = true;
                        this.mGestureDownVolume = this.mAudioManager.getStreamVolume(3);
                    }
                }
                if (this.mChangePosition) {
                    long totalTimeDuration = getDuration();
                    this.mSeekTimePosition = (long) ((int) (((float) this.mGestureDownPosition) + ((((float) totalTimeDuration) * deltaX) / ((float) this.mScreenWidth))));
                    if (this.mSeekTimePosition > totalTimeDuration) {
                        this.mSeekTimePosition = totalTimeDuration;
                    }
                    showProgressDialog(deltaX, JZUtils.stringForTime(this.mSeekTimePosition), this.mSeekTimePosition, JZUtils.stringForTime(totalTimeDuration), totalTimeDuration);
                    JZMediaManager.seekTo(this.mSeekTimePosition);
                    long duration = getDuration();
                    long j = this.mSeekTimePosition * 100;
                    if (duration == 0) {
                        duration = 1;
                    }
                    this.progressBar.setProgress((int) (j / duration));
                }
                if (this.mChangeVolume) {
                    deltaY = -deltaY;
                    int max = this.mAudioManager.getStreamMaxVolume(3);
                    this.mAudioManager.setStreamVolume(3, this.mGestureDownVolume + ((int) (((((float) max) * deltaY) * 3.0f) / ((float) this.mScreenHeight))), 0);
                    float f = -deltaY;
                    showVolumeDialog(f, (int) (((float) ((this.mGestureDownVolume * 100) / max)) + (((3.0f * deltaY) * 100.0f) / ((float) this.mScreenHeight))));
                }
                if (!this.mChangeBrightness) {
                    return false;
                }
                float deltaY2 = -deltaY;
                int deltaV = (int) (((255.0f * deltaY2) * 3.0f) / ((float) this.mScreenHeight));
                WindowManager.LayoutParams params = JZUtils.getWindow(getContext()).getAttributes();
                if ((this.mGestureDownBrightness + ((float) deltaV)) / 255.0f >= 1.0f) {
                    params.screenBrightness = 1.0f;
                } else if ((this.mGestureDownBrightness + ((float) deltaV)) / 255.0f <= 0.0f) {
                    params.screenBrightness = 0.01f;
                } else {
                    params.screenBrightness = (this.mGestureDownBrightness + ((float) deltaV)) / 255.0f;
                }
                JZUtils.getWindow(getContext()).setAttributes(params);
                showBrightnessDialog((int) (((this.mGestureDownBrightness * 100.0f) / 255.0f) + (((3.0f * deltaY2) * 100.0f) / ((float) this.mScreenHeight))));
                return false;
            default:
                return false;
        }
    }

    public void startVideo() {
        JZVideoPlayerManager.completeAll();
        Log.d("JiaoZiVideoPlayer", "startVideo [" + hashCode() + "] ");
        initTextureView();
        addTextureView();
        ((AudioManager) getContext().getSystemService("audio")).requestAudioFocus(onAudioFocusChangeListener, 3, 2);
        JZUtils.scanForActivity(getContext()).getWindow().addFlags(128);
        JZMediaManager.setDataSource(this.dataSourceObjects);
        JZMediaManager.setCurrentDataSource(JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
        JZMediaManager.instance().positionInList = this.positionInList;
        onStatePreparing();
        JZVideoPlayerManager.setFirstFloor(this);
    }

    public void onPrepared() {
        Log.i("JiaoZiVideoPlayer", "onPrepared  [" + hashCode() + "] ");
        onStatePrepared();
        onStatePlaying();
    }

    public void setState(int state) {
        setState(state, 0, 0);
    }

    public void setState(int state, int urlMapIndex, int seekToInAdvance2) {
        switch (state) {
            case 0:
                onStateNormal();
                return;
            case 1:
                onStatePreparing();
                return;
            case 2:
                onStatePreparingChangingUrl(urlMapIndex, (long) seekToInAdvance2);
                return;
            case 3:
                onStatePlaying();
                return;
            case 5:
                onStatePause();
                return;
            case 6:
                onStateAutoComplete();
                return;
            case 7:
                onStateError();
                return;
            default:
                return;
        }
    }

    public void onStateNormal() {
        Log.i("JiaoZiVideoPlayer", "onStateNormal  [" + hashCode() + "] ");
        this.currentState = 0;
        cancelProgressTimer();
    }

    public void onStatePreparing() {
        Log.i("JiaoZiVideoPlayer", "onStatePreparing  [" + hashCode() + "] ");
        this.currentState = 1;
        resetProgressAndTime();
    }

    public void onStatePreparingChangingUrl(int urlMapIndex, long seekToInAdvance2) {
        this.currentState = 2;
        this.currentUrlMapIndex = urlMapIndex;
        this.seekToInAdvance = seekToInAdvance2;
        JZMediaManager.setDataSource(this.dataSourceObjects);
        JZMediaManager.setCurrentDataSource(JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
        JZMediaManager.instance().prepare();
    }

    public void onStatePrepared() {
        if (this.seekToInAdvance != 0) {
            JZMediaManager.seekTo(this.seekToInAdvance);
            this.seekToInAdvance = 0;
            return;
        }
        long position = JZUtils.getSavedProgress(getContext(), JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
        if (position != 0) {
            JZMediaManager.seekTo(position);
        }
    }

    public void onStatePlaying() {
        Log.i("JiaoZiVideoPlayer", "onStatePlaying  [" + hashCode() + "] ");
        this.currentState = 3;
        startProgressTimer();
    }

    public void onStatePause() {
        Log.i("JiaoZiVideoPlayer", "onStatePause  [" + hashCode() + "] ");
        this.currentState = 5;
        startProgressTimer();
    }

    public void onStateError() {
        Log.i("JiaoZiVideoPlayer", "onStateError  [" + hashCode() + "] ");
        this.currentState = 7;
        cancelProgressTimer();
    }

    public void onStateAutoComplete() {
        Log.i("JiaoZiVideoPlayer", "onStateAutoComplete  [" + hashCode() + "] ");
        this.currentState = 6;
        cancelProgressTimer();
        this.progressBar.setProgress(100);
        this.currentTimeTextView.setText(this.totalTimeTextView.getText());
    }

    public void onInfo(int what, int extra) {
        Log.d("JiaoZiVideoPlayer", "onInfo what - " + what + " extra - " + extra);
    }

    public void onError(int what, int extra) {
        Log.e("JiaoZiVideoPlayer", "onError " + what + " - " + extra + " [" + hashCode() + "] ");
        if (what != 38 && extra != -38 && what != -38 && extra != 38 && extra != -19) {
            onStateError();
            if (isCurrentPlay()) {
                JZMediaManager.instance().releaseMediaPlayer();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.currentScreen == 2 || this.currentScreen == 3) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (this.widthRatio == 0 || this.heightRatio == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int specWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            int specHeight = (int) ((((float) specWidth) * ((float) this.heightRatio)) / ((float) this.widthRatio));
            setMeasuredDimension(specWidth, specHeight);
            getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(specWidth, C1907C.ENCODING_PCM_32BIT), View.MeasureSpec.makeMeasureSpec(specHeight, C1907C.ENCODING_PCM_32BIT));
        }
    }

    public void onAutoCompletion() {
        Runtime.getRuntime().gc();
        Log.i("JiaoZiVideoPlayer", "onAutoCompletion  [" + hashCode() + "] ");
        onEvent(6);
        dismissVolumeDialog();
        dismissProgressDialog();
        dismissBrightnessDialog();
        onStateAutoComplete();
        if (this.currentScreen == 2 || this.currentScreen == 3) {
            backPress();
        }
        JZMediaManager.instance().releaseMediaPlayer();
        JZUtils.saveProgress(getContext(), JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex), 0);
    }

    public void onCompletion() {
        Log.i("JiaoZiVideoPlayer", "onCompletion  [" + hashCode() + "] ");
        if (this.currentState == 3 || this.currentState == 5) {
            JZUtils.saveProgress(getContext(), JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex), getCurrentPositionWhenPlaying());
        }
        cancelProgressTimer();
        dismissBrightnessDialog();
        dismissProgressDialog();
        dismissVolumeDialog();
        onStateNormal();
        this.textureViewContainer.removeView(JZMediaManager.textureView);
        JZMediaManager.instance().currentVideoWidth = 0;
        JZMediaManager.instance().currentVideoHeight = 0;
        ((AudioManager) getContext().getSystemService("audio")).abandonAudioFocus(onAudioFocusChangeListener);
        JZUtils.scanForActivity(getContext()).getWindow().clearFlags(128);
        clearFullscreenLayout();
        JZUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        if (JZMediaManager.surface != null) {
            JZMediaManager.surface.release();
        }
        if (JZMediaManager.savedSurfaceTexture != null) {
            JZMediaManager.savedSurfaceTexture.release();
        }
        JZMediaManager.textureView = null;
        JZMediaManager.savedSurfaceTexture = null;
    }

    public void release() {
        if (JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).equals(JZMediaManager.getCurrentDataSource()) && System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME > 300) {
            if (JZVideoPlayerManager.getSecondFloor() != null && JZVideoPlayerManager.getSecondFloor().currentScreen == 2) {
                return;
            }
            if (JZVideoPlayerManager.getSecondFloor() != null || JZVideoPlayerManager.getFirstFloor() == null || JZVideoPlayerManager.getFirstFloor().currentScreen != 2) {
                Log.d("JiaoZiVideoPlayer", "releaseMediaPlayer [" + hashCode() + "]");
                releaseAllVideos();
            }
        }
    }

    public void initTextureView() {
        removeTextureView();
        JZMediaManager.textureView = new JZResizeTextureView(getContext());
        JZMediaManager.textureView.setSurfaceTextureListener(JZMediaManager.instance());
    }

    public void addTextureView() {
        Log.d("JiaoZiVideoPlayer", "addTextureView [" + hashCode() + "] ");
        this.textureViewContainer.addView(JZMediaManager.textureView, new FrameLayout.LayoutParams(-1, -1, 17));
    }

    public void removeTextureView() {
        JZMediaManager.savedSurfaceTexture = null;
        if (JZMediaManager.textureView != null && JZMediaManager.textureView.getParent() != null) {
            ((ViewGroup) JZMediaManager.textureView.getParent()).removeView(JZMediaManager.textureView);
        }
    }

    public void clearFullscreenLayout() {
        ViewGroup vp = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(16908290);
        View oldF = vp.findViewById(C0783R.C0785id.jz_fullscreen_id);
        View oldT = vp.findViewById(C0783R.C0785id.jz_tiny_id);
        if (oldF != null) {
            vp.removeView(oldF);
        }
        if (oldT != null) {
            vp.removeView(oldT);
        }
        showSupportActionBar(getContext());
    }

    public void clearFloatScreen() {
        JZUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        showSupportActionBar(getContext());
        ViewGroup vp = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(16908290);
        JZVideoPlayer fullJzvd = (JZVideoPlayer) vp.findViewById(C0783R.C0785id.jz_fullscreen_id);
        JZVideoPlayer tinyJzvd = (JZVideoPlayer) vp.findViewById(C0783R.C0785id.jz_tiny_id);
        if (fullJzvd != null) {
            vp.removeView(fullJzvd);
            if (fullJzvd.textureViewContainer != null) {
                fullJzvd.textureViewContainer.removeView(JZMediaManager.textureView);
            }
        }
        if (tinyJzvd != null) {
            vp.removeView(tinyJzvd);
            if (tinyJzvd.textureViewContainer != null) {
                tinyJzvd.textureViewContainer.removeView(JZMediaManager.textureView);
            }
        }
        JZVideoPlayerManager.setSecondFloor((JZVideoPlayer) null);
    }

    public void onVideoSizeChanged() {
        Log.i("JiaoZiVideoPlayer", "onVideoSizeChanged  [" + hashCode() + "] ");
        if (JZMediaManager.textureView != null) {
            if (this.videoRotation != 0) {
                JZMediaManager.textureView.setRotation((float) this.videoRotation);
            }
            JZMediaManager.textureView.setVideoSize(JZMediaManager.instance().currentVideoWidth, JZMediaManager.instance().currentVideoHeight);
        }
    }

    public void startProgressTimer() {
        Log.i("JiaoZiVideoPlayer", "startProgressTimer:  [" + hashCode() + "] ");
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        this.mProgressTimerTask = new ProgressTimerTask();
        UPDATE_PROGRESS_TIMER.schedule(this.mProgressTimerTask, 0, 300);
    }

    public void cancelProgressTimer() {
        if (UPDATE_PROGRESS_TIMER != null) {
            UPDATE_PROGRESS_TIMER.cancel();
        }
        if (this.mProgressTimerTask != null) {
            this.mProgressTimerTask.cancel();
        }
    }

    public void setProgressAndText(int progress, long position, long duration) {
        if (!this.mTouchingProgressBar && progress != 0) {
            this.progressBar.setProgress(progress);
        }
        if (position != 0) {
            this.currentTimeTextView.setText(JZUtils.stringForTime(position));
        }
        this.totalTimeTextView.setText(JZUtils.stringForTime(duration));
    }

    public void setBufferProgress(int bufferProgress) {
        if (bufferProgress != 0) {
            this.progressBar.setSecondaryProgress(bufferProgress);
        }
    }

    public void resetProgressAndTime() {
        this.progressBar.setProgress(0);
        this.progressBar.setSecondaryProgress(0);
        this.currentTimeTextView.setText(JZUtils.stringForTime(0));
        this.totalTimeTextView.setText(JZUtils.stringForTime(0));
    }

    public long getCurrentPositionWhenPlaying() {
        long position = 0;
        if (this.currentState == 3 || this.currentState == 5) {
            try {
                position = JZMediaManager.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return position;
    }

    public long getDuration() {
        try {
            return JZMediaManager.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i("JiaoZiVideoPlayer", "bottomProgress onStartTrackingTouch [" + hashCode() + "] ");
        cancelProgressTimer();
        for (ViewParent vpdown = getParent(); vpdown != null; vpdown = vpdown.getParent()) {
            vpdown.requestDisallowInterceptTouchEvent(true);
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i("JiaoZiVideoPlayer", "bottomProgress onStopTrackingTouch [" + hashCode() + "] ");
        onEvent(5);
        startProgressTimer();
        for (ViewParent vpup = getParent(); vpup != null; vpup = vpup.getParent()) {
            vpup.requestDisallowInterceptTouchEvent(false);
        }
        if (this.currentState == 3 || this.currentState == 5) {
            long time = (((long) seekBar.getProgress()) * getDuration()) / 100;
            JZMediaManager.seekTo(time);
            Log.i("JiaoZiVideoPlayer", "seekTo " + time + " [" + hashCode() + "] ");
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            long duration = getDuration();
            this.currentTimeTextView.setText(JZUtils.stringForTime((((long) progress) * duration) / 100));
            this.mSeekTimePosition = (((long) progress) * duration) / 100;
            JZMediaManager.seekTo(this.mSeekTimePosition);
        }
    }

    public void startWindowFullscreen() {
        Log.i("JiaoZiVideoPlayer", "startWindowFullscreen  [" + hashCode() + "] ");
        hideSupportActionBar(getContext());
        ViewGroup vp = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(16908290);
        View old = vp.findViewById(C0783R.C0785id.jz_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        this.textureViewContainer.removeView(JZMediaManager.textureView);
        try {
            JZVideoPlayer jzVideoPlayer = (JZVideoPlayer) getClass().getConstructor(new Class[]{Context.class}).newInstance(new Object[]{getContext()});
            jzVideoPlayer.setId(C0783R.C0785id.jz_fullscreen_id);
            vp.addView(jzVideoPlayer, new FrameLayout.LayoutParams(-1, -1));
            jzVideoPlayer.setSystemUiVisibility(4102);
            jzVideoPlayer.setUp(this.dataSourceObjects, this.currentUrlMapIndex, 2, this.objects);
            jzVideoPlayer.setState(this.currentState);
            jzVideoPlayer.addTextureView();
            JZVideoPlayerManager.setSecondFloor(jzVideoPlayer);
            JZUtils.setRequestedOrientation(getContext(), FULLSCREEN_ORIENTATION);
            onStateNormal();
            jzVideoPlayer.progressBar.setSecondaryProgress(this.progressBar.getSecondaryProgress());
            jzVideoPlayer.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startWindowTiny() {
        Log.i("JiaoZiVideoPlayer", "startWindowTiny  [" + hashCode() + "] ");
        onEvent(9);
        if (this.currentState != 0 && this.currentState != 7 && this.currentState != 6) {
            ViewGroup vp = (ViewGroup) JZUtils.scanForActivity(getContext()).findViewById(16908290);
            View old = vp.findViewById(C0783R.C0785id.jz_tiny_id);
            if (old != null) {
                vp.removeView(old);
            }
            this.textureViewContainer.removeView(JZMediaManager.textureView);
            try {
                JZVideoPlayer jzVideoPlayer = (JZVideoPlayer) getClass().getConstructor(new Class[]{Context.class}).newInstance(new Object[]{getContext()});
                jzVideoPlayer.setId(C0783R.C0785id.jz_tiny_id);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(400, 400);
                lp.gravity = 85;
                vp.addView(jzVideoPlayer, lp);
                jzVideoPlayer.setUp(this.dataSourceObjects, this.currentUrlMapIndex, 3, this.objects);
                jzVideoPlayer.setState(this.currentState);
                jzVideoPlayer.addTextureView();
                JZVideoPlayerManager.setSecondFloor(jzVideoPlayer);
                onStateNormal();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public boolean isCurrentPlay() {
        return isCurrentJZVD() && JZUtils.dataSourceObjectsContainsUri(this.dataSourceObjects, JZMediaManager.getCurrentDataSource());
    }

    public boolean isCurrentJZVD() {
        return JZVideoPlayerManager.getCurrentJzvd() != null && JZVideoPlayerManager.getCurrentJzvd() == this;
    }

    public void playOnThisJzvd() {
        Log.i("JiaoZiVideoPlayer", "playOnThisJzvd  [" + hashCode() + "] ");
        this.currentState = JZVideoPlayerManager.getSecondFloor().currentState;
        this.currentUrlMapIndex = JZVideoPlayerManager.getSecondFloor().currentUrlMapIndex;
        clearFloatScreen();
        setState(this.currentState);
        addTextureView();
    }

    public void autoFullscreen(float x) {
        if (!isCurrentPlay()) {
            return;
        }
        if ((this.currentState == 3 || this.currentState == 5) && this.currentScreen != 2 && this.currentScreen != 3) {
            if (x > 0.0f) {
                JZUtils.setRequestedOrientation(getContext(), 0);
            } else {
                JZUtils.setRequestedOrientation(getContext(), 8);
            }
            onEvent(7);
            startWindowFullscreen();
        }
    }

    public void autoQuitFullscreen() {
        if (System.currentTimeMillis() - lastAutoFullscreenTime > 2000 && isCurrentPlay() && this.currentState == 3 && this.currentScreen == 2) {
            lastAutoFullscreenTime = System.currentTimeMillis();
            backPress();
        }
    }

    public void onEvent(int type) {
        if (JZ_USER_EVENT != null && isCurrentPlay() && this.dataSourceObjects != null) {
            JZ_USER_EVENT.onEvent(type, JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex), this.currentScreen, this.objects);
        }
    }

    public static void setMediaInterface(JZMediaInterface mediaInterface) {
        JZMediaManager.instance().jzMediaInterface = mediaInterface;
    }

    public void onSeekComplete() {
    }

    public void showWifiDialog() {
    }

    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
    }

    public void dismissProgressDialog() {
    }

    public void showVolumeDialog(float deltaY, int volumePercent) {
    }

    public void dismissVolumeDialog() {
    }

    public void showBrightnessDialog(int brightnessPercent) {
    }

    public void dismissBrightnessDialog() {
    }

    /* renamed from: cn.jzvd.JZVideoPlayer$JZAutoFullscreenListener */
    public static class JZAutoFullscreenListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float f = event.values[1];
            float f2 = event.values[2];
            if ((x < -12.0f || x > 12.0f) && System.currentTimeMillis() - JZVideoPlayer.lastAutoFullscreenTime > 2000) {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().autoFullscreen(x);
                }
                JZVideoPlayer.lastAutoFullscreenTime = System.currentTimeMillis();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    /* renamed from: cn.jzvd.JZVideoPlayer$ProgressTimerTask */
    public class ProgressTimerTask extends TimerTask {
        public ProgressTimerTask() {
        }

        public void run() {
            if (JZVideoPlayer.this.currentState == 3 || JZVideoPlayer.this.currentState == 5) {
                JZVideoPlayer.this.post(new Runnable() {
                    public void run() {
                        long j;
                        long position = JZVideoPlayer.this.getCurrentPositionWhenPlaying();
                        long duration = JZVideoPlayer.this.getDuration();
                        long j2 = position * 100;
                        if (duration == 0) {
                            j = 1;
                        } else {
                            j = duration;
                        }
                        JZVideoPlayer.this.setProgressAndText((int) (j2 / j), position, duration);
                    }
                });
            }
        }
    }
}
