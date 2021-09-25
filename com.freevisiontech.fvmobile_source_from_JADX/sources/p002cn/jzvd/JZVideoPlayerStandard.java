package p002cn.jzvd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: cn.jzvd.JZVideoPlayerStandard */
public class JZVideoPlayerStandard extends JZVideoPlayer {
    protected static Timer DISMISS_CONTROL_VIEW_TIMER;
    public static int LAST_GET_BATTERYLEVEL_PERCENT = 70;
    public static long LAST_GET_BATTERYLEVEL_TIME = 0;
    public ImageView backButton;
    /* access modifiers changed from: private */
    public BroadcastReceiver battertReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                JZVideoPlayerStandard.LAST_GET_BATTERYLEVEL_PERCENT = (intent.getIntExtra("level", 0) * 100) / intent.getIntExtra("scale", 100);
                JZVideoPlayerStandard.this.setBatteryLevel();
                JZVideoPlayerStandard.this.getContext().unregisterReceiver(JZVideoPlayerStandard.this.battertReceiver);
            }
        }
    };
    public ImageView batteryLevel;
    public TextView clarity;
    public PopupWindow clarityPopWindow;
    public ProgressBar loadingProgressBar;
    protected Dialog mBrightnessDialog;
    protected ProgressBar mDialogBrightnessProgressBar;
    protected TextView mDialogBrightnessTextView;
    protected ImageView mDialogIcon;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogVolumeImageView;
    protected ProgressBar mDialogVolumeProgressBar;
    protected TextView mDialogVolumeTextView;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected Dialog mProgressDialog;
    public TextView mRetryBtn;
    public LinearLayout mRetryLayout;
    protected Dialog mVolumeDialog;
    public TextView replayTextView;
    public ImageView thumbImageView;
    public ImageView tinyBackImageView;
    public TextView titleTextView;
    public TextView videoCurrentTime;

    public JZVideoPlayerStandard(Context context) {
        super(context);
    }

    public JZVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context) {
        super.init(context);
        this.titleTextView = (TextView) findViewById(C0783R.C0785id.title);
        this.backButton = (ImageView) findViewById(C0783R.C0785id.back);
        this.thumbImageView = (ImageView) findViewById(C0783R.C0785id.thumb);
        this.loadingProgressBar = (ProgressBar) findViewById(C0783R.C0785id.loading);
        this.tinyBackImageView = (ImageView) findViewById(C0783R.C0785id.back_tiny);
        this.batteryLevel = (ImageView) findViewById(C0783R.C0785id.battery_level);
        this.videoCurrentTime = (TextView) findViewById(C0783R.C0785id.video_current_time);
        this.replayTextView = (TextView) findViewById(C0783R.C0785id.replay_text);
        this.clarity = (TextView) findViewById(C0783R.C0785id.clarity);
        this.mRetryBtn = (TextView) findViewById(C0783R.C0785id.retry_btn);
        this.mRetryLayout = (LinearLayout) findViewById(C0783R.C0785id.retry_layout);
        this.thumbImageView.setOnClickListener(this);
        this.backButton.setOnClickListener(this);
        this.tinyBackImageView.setOnClickListener(this);
        this.clarity.setOnClickListener(this);
        this.mRetryBtn.setOnClickListener(this);
    }

    public void setUp(Object[] dataSourceObjects, int defaultUrlMapIndex, int screen, Object... objects) {
        super.setUp(dataSourceObjects, defaultUrlMapIndex, screen, objects);
        if (objects.length != 0) {
            this.titleTextView.setText(objects[0].toString());
        }
        if (this.currentScreen == 2) {
            this.fullscreenButton.setImageResource(C0783R.C0784drawable.jz_shrink);
            this.backButton.setVisibility(0);
            this.tinyBackImageView.setVisibility(4);
            if (dataSourceObjects[0].size() == 1) {
                this.clarity.setVisibility(8);
            } else {
                this.clarity.setText(JZUtils.getKeyFromDataSource(dataSourceObjects, this.currentUrlMapIndex));
                this.clarity.setVisibility(0);
            }
            changeStartButtonSize((int) getResources().getDimension(C0783R.dimen.jz_start_button_w_h_fullscreen));
        } else if (this.currentScreen == 0 || this.currentScreen == 1) {
            this.fullscreenButton.setImageResource(C0783R.C0784drawable.jz_enlarge);
            this.backButton.setVisibility(8);
            this.tinyBackImageView.setVisibility(4);
            changeStartButtonSize((int) getResources().getDimension(C0783R.dimen.jz_start_button_w_h_normal));
            this.clarity.setVisibility(8);
        } else if (this.currentScreen == 3) {
            this.tinyBackImageView.setVisibility(0);
            setAllControlsVisiblity(4, 4, 4, 4, 4, 4, 4);
            this.clarity.setVisibility(8);
        }
        setSystemTimeAndBattery();
        if (this.tmp_test_back) {
            this.tmp_test_back = false;
            JZVideoPlayerManager.setFirstFloor(this);
            backPress();
        }
    }

    public void changeStartButtonSize(int size) {
        ViewGroup.LayoutParams lp = this.startButton.getLayoutParams();
        lp.height = size;
        lp.width = size;
        ViewGroup.LayoutParams lp2 = this.loadingProgressBar.getLayoutParams();
        lp2.height = size;
        lp2.width = size;
    }

    public int getLayoutId() {
        return C0783R.layout.jz_layout_standard;
    }

    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
    }

    public void onStatePreparingChangingUrl(int urlMapIndex, long seekToInAdvance) {
        super.onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);
        this.loadingProgressBar.setVisibility(0);
        this.startButton.setVisibility(4);
    }

    public void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingClear();
    }

    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
    }

    public void onStateError() {
        super.onStateError();
        changeUiToError();
    }

    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        changeUiToComplete();
        cancelDismissControlViewTimer();
    }

    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id != C0783R.C0785id.surface_container) {
            if (id == C0783R.C0785id.bottom_seek_progress) {
                switch (event.getAction()) {
                    case 0:
                        cancelDismissControlViewTimer();
                        break;
                    case 1:
                        startDismissControlViewTimer();
                        break;
                }
            }
        } else {
            switch (event.getAction()) {
                case 1:
                    startDismissControlViewTimer();
                    if (this.mChangePosition) {
                        long duration = getDuration();
                        long j = this.mSeekTimePosition * 100;
                        if (duration == 0) {
                            duration = 1;
                        }
                        int i = (int) (j / duration);
                    }
                    if (!this.mChangePosition && !this.mChangeVolume) {
                        onEvent(102);
                        onClickUiToggle();
                        break;
                    }
            }
        }
        return super.onTouch(v, event);
    }

    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == C0783R.C0785id.thumb) {
            if (this.dataSourceObjects == null || JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex) == null) {
                Toast.makeText(getContext(), getResources().getString(C0783R.string.no_url), 0).show();
            } else if (this.currentState == 0) {
                if (JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("file") || JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("/") || JZUtils.isWifiConnected(getContext()) || WIFI_TIP_DIALOG_SHOWED) {
                    startVideo();
                    onEvent(101);
                    return;
                }
                showWifiDialog();
            } else if (this.currentState == 6) {
                onClickUiToggle();
            }
        } else if (i == C0783R.C0785id.surface_container) {
            startDismissControlViewTimer();
        } else if (i == C0783R.C0785id.back) {
            backPress();
        } else if (i == C0783R.C0785id.back_tiny) {
            if (JZVideoPlayerManager.getFirstFloor().currentScreen == 1) {
                quitFullscreenOrTinyWindow();
            } else {
                backPress();
            }
        } else if (i == C0783R.C0785id.clarity) {
            final LinearLayout layout = (LinearLayout) ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(C0783R.layout.jz_layout_clarity, (ViewGroup) null);
            View.OnClickListener mQualityListener = new View.OnClickListener() {
                public void onClick(View v) {
                    JZVideoPlayerStandard.this.onStatePreparingChangingUrl(((Integer) v.getTag()).intValue(), JZVideoPlayerStandard.this.getCurrentPositionWhenPlaying());
                    JZVideoPlayerStandard.this.clarity.setText(JZUtils.getKeyFromDataSource(JZVideoPlayerStandard.this.dataSourceObjects, JZVideoPlayerStandard.this.currentUrlMapIndex));
                    for (int j = 0; j < layout.getChildCount(); j++) {
                        if (j == JZVideoPlayerStandard.this.currentUrlMapIndex) {
                            ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#fff85959"));
                        } else {
                            ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                    if (JZVideoPlayerStandard.this.clarityPopWindow != null) {
                        JZVideoPlayerStandard.this.clarityPopWindow.dismiss();
                    }
                }
            };
            for (int j = 0; j < ((LinkedHashMap) this.dataSourceObjects[0]).size(); j++) {
                String key = JZUtils.getKeyFromDataSource(this.dataSourceObjects, j);
                TextView clarityItem = (TextView) View.inflate(getContext(), C0783R.layout.jz_layout_clarity_item, (ViewGroup) null);
                clarityItem.setText(key);
                clarityItem.setTag(Integer.valueOf(j));
                layout.addView(clarityItem, j);
                clarityItem.setOnClickListener(mQualityListener);
                if (j == this.currentUrlMapIndex) {
                    clarityItem.setTextColor(Color.parseColor("#fff85959"));
                }
            }
            this.clarityPopWindow = new PopupWindow(layout, -2, -2, true);
            this.clarityPopWindow.setContentView(layout);
            this.clarityPopWindow.showAsDropDown(this.clarity);
            layout.measure(0, 0);
            this.clarityPopWindow.update(this.clarity, -(this.clarity.getMeasuredWidth() / 3), -(this.clarity.getMeasuredHeight() / 3), Math.round((float) (layout.getMeasuredWidth() * 2)), layout.getMeasuredHeight());
        } else if (i != C0783R.C0785id.retry_btn) {
        } else {
            if (this.dataSourceObjects == null || JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex) == null) {
                Toast.makeText(getContext(), getResources().getString(C0783R.string.no_url), 0).show();
            } else if (JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("file") || JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex).toString().startsWith("/") || JZUtils.isWifiConnected(getContext()) || WIFI_TIP_DIALOG_SHOWED) {
                initTextureView();
                addTextureView();
                JZMediaManager.setDataSource(this.dataSourceObjects);
                JZMediaManager.setCurrentDataSource(JZUtils.getCurrentFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
                onStatePreparing();
                onEvent(1);
            } else {
                showWifiDialog();
            }
        }
    }

    public void showWifiDialog() {
        super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(C0783R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(C0783R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                JZVideoPlayerStandard.this.onEvent(103);
                JZVideoPlayerStandard.this.startVideo();
                JZVideoPlayer.WIFI_TIP_DIALOG_SHOWED = true;
            }
        });
        builder.setNegativeButton(getResources().getString(C0783R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                JZVideoPlayerStandard.this.clearFloatScreen();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        if (this.currentState == 3) {
            dissmissControlView();
        } else {
            startDismissControlViewTimer();
        }
    }

    public void onClickUiToggle() {
        if (this.bottomContainer.getVisibility() != 0) {
            setSystemTimeAndBattery();
            this.clarity.setText(JZUtils.getKeyFromDataSource(this.dataSourceObjects, this.currentUrlMapIndex));
        }
        if (this.currentState == 1) {
            changeUiToPreparing();
            if (this.bottomContainer.getVisibility() != 0) {
                setSystemTimeAndBattery();
            }
        } else if (this.currentState == 3) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (this.currentState != 5) {
        } else {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        }
    }

    public void setSystemTimeAndBattery() {
        this.videoCurrentTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
        if (System.currentTimeMillis() - LAST_GET_BATTERYLEVEL_TIME > 30000) {
            LAST_GET_BATTERYLEVEL_TIME = System.currentTimeMillis();
            getContext().registerReceiver(this.battertReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            return;
        }
        setBatteryLevel();
    }

    public void setBatteryLevel() {
        int percent = LAST_GET_BATTERYLEVEL_PERCENT;
        if (percent < 15) {
            this.batteryLevel.setBackgroundResource(C0783R.C0784drawable.jz_battery_level_10);
        } else if (percent >= 15 && percent < 40) {
            this.batteryLevel.setBackgroundResource(C0783R.C0784drawable.jz_battery_level_30);
        } else if (percent >= 40 && percent < 60) {
            this.batteryLevel.setBackgroundResource(C0783R.C0784drawable.jz_battery_level_50);
        } else if (percent >= 60 && percent < 80) {
            this.batteryLevel.setBackgroundResource(C0783R.C0784drawable.jz_battery_level_70);
        } else if (percent >= 80 && percent < 95) {
            this.batteryLevel.setBackgroundResource(C0783R.C0784drawable.jz_battery_level_90);
        } else if (percent >= 95 && percent <= 100) {
            this.batteryLevel.setBackgroundResource(C0783R.C0784drawable.jz_battery_level_100);
        }
    }

    public void onCLickUiToggleToClear() {
        if (this.currentState == 1) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPreparing();
            }
        } else if (this.currentState == 3) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPlayingClear();
            }
        } else if (this.currentState == 5) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPauseClear();
            }
        } else if (this.currentState == 6 && this.bottomContainer.getVisibility() == 0) {
            changeUiToComplete();
        }
    }

    public void setProgressAndText(int progress, long position, long duration) {
        super.setProgressAndText(progress, position, duration);
    }

    public void setBufferProgress(int bufferProgress) {
        super.setBufferProgress(bufferProgress);
    }

    public void resetProgressAndTime() {
        super.resetProgressAndTime();
    }

    public void changeUiToNormal() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisiblity(0, 4, 0, 4, 0, 4, 4);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisiblity(0, 4, 0, 4, 0, 4, 4);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void changeUiToPreparing() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisiblity(4, 4, 4, 0, 0, 4, 4);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisiblity(4, 4, 4, 0, 0, 4, 4);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void changeUiToPlayingShow() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void changeUiToPlayingClear() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisiblity(4, 4, 4, 4, 4, 0, 4);
                return;
            case 2:
                setAllControlsVisiblity(4, 4, 4, 4, 4, 0, 4);
                return;
            default:
                return;
        }
    }

    public void changeUiToPauseShow() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void changeUiToPauseClear() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisiblity(4, 4, 4, 4, 4, 0, 4);
                return;
            case 2:
                setAllControlsVisiblity(4, 4, 4, 4, 4, 0, 4);
                return;
            default:
                return;
        }
    }

    public void changeUiToComplete() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisiblity(0, 4, 0, 4, 0, 4, 4);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisiblity(0, 4, 0, 4, 0, 4, 4);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void changeUiToError() {
        switch (this.currentScreen) {
            case 0:
            case 1:
                setAllControlsVisiblity(4, 4, 0, 4, 4, 4, 0);
                updateStartImage();
                return;
            case 2:
                setAllControlsVisiblity(0, 4, 0, 4, 4, 4, 0);
                updateStartImage();
                return;
            default:
                return;
        }
    }

    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        this.bottomContainer.setVisibility(bottomCon);
        this.startButton.setVisibility(startBtn);
        this.loadingProgressBar.setVisibility(loadingPro);
        this.thumbImageView.setVisibility(thumbImg);
        this.mRetryLayout.setVisibility(retryLayout);
    }

    public void updateStartImage() {
        if (this.currentState == 3) {
            this.startButton.setVisibility(0);
            this.startButton.setImageResource(C0783R.C0784drawable.jz_pause_mid);
            this.replayTextView.setVisibility(4);
        } else if (this.currentState == 7) {
            this.startButton.setVisibility(4);
            this.replayTextView.setVisibility(4);
        } else if (this.currentState == 6) {
            this.startButton.setVisibility(0);
            this.startButton.setImageResource(C0783R.C0784drawable.jz_click_replay_selector);
            this.replayTextView.setVisibility(0);
        } else {
            this.startButton.setImageResource(C0783R.C0784drawable.jz_play_mid);
            this.replayTextView.setVisibility(4);
        }
    }

    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
        if (this.mProgressDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(C0783R.layout.jz_dialog_progress, (ViewGroup) null);
            this.mDialogProgressBar = (ProgressBar) localView.findViewById(C0783R.C0785id.duration_progressbar);
            this.mDialogSeekTime = (TextView) localView.findViewById(C0783R.C0785id.tv_current);
            this.mDialogTotalTime = (TextView) localView.findViewById(C0783R.C0785id.tv_duration);
            this.mDialogIcon = (ImageView) localView.findViewById(C0783R.C0785id.duration_image_tip);
            this.mProgressDialog = createDialogWithView(localView);
        }
        if (!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }
        this.mDialogSeekTime.setText(seekTime);
        this.mDialogTotalTime.setText(totalTime);
        this.mDialogProgressBar.setProgress(totalTimeDuration <= 0 ? 0 : (int) ((100 * seekTimePosition) / totalTimeDuration));
        if (deltaX > 0.0f) {
            this.mDialogIcon.setBackgroundResource(C0783R.C0784drawable.jz_forward_icon);
        } else {
            this.mDialogIcon.setBackgroundResource(C0783R.C0784drawable.jz_backward_icon);
        }
        onCLickUiToggleToClear();
    }

    public void dismissProgressDialog() {
        super.dismissProgressDialog();
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }

    public void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        if (this.mVolumeDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(C0783R.layout.jz_dialog_volume, (ViewGroup) null);
            this.mDialogVolumeImageView = (ImageView) localView.findViewById(C0783R.C0785id.volume_image_tip);
            this.mDialogVolumeTextView = (TextView) localView.findViewById(C0783R.C0785id.tv_volume);
            this.mDialogVolumeProgressBar = (ProgressBar) localView.findViewById(C0783R.C0785id.volume_progressbar);
            this.mVolumeDialog = createDialogWithView(localView);
        }
        if (!this.mVolumeDialog.isShowing()) {
            this.mVolumeDialog.show();
        }
        if (volumePercent <= 0) {
            this.mDialogVolumeImageView.setBackgroundResource(C0783R.C0784drawable.jz_close_volume);
        } else {
            this.mDialogVolumeImageView.setBackgroundResource(C0783R.C0784drawable.jz_add_volume);
        }
        if (volumePercent > 100) {
            volumePercent = 100;
        } else if (volumePercent < 0) {
            volumePercent = 0;
        }
        this.mDialogVolumeTextView.setText(volumePercent + "%");
        this.mDialogVolumeProgressBar.setProgress(volumePercent);
        onCLickUiToggleToClear();
    }

    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (this.mVolumeDialog != null) {
            this.mVolumeDialog.dismiss();
        }
    }

    public void showBrightnessDialog(int brightnessPercent) {
        super.showBrightnessDialog(brightnessPercent);
        if (this.mBrightnessDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(C0783R.layout.jz_dialog_brightness, (ViewGroup) null);
            this.mDialogBrightnessTextView = (TextView) localView.findViewById(C0783R.C0785id.tv_brightness);
            this.mDialogBrightnessProgressBar = (ProgressBar) localView.findViewById(C0783R.C0785id.brightness_progressbar);
            this.mBrightnessDialog = createDialogWithView(localView);
        }
        if (!this.mBrightnessDialog.isShowing()) {
            this.mBrightnessDialog.show();
        }
        if (brightnessPercent > 100) {
            brightnessPercent = 100;
        } else if (brightnessPercent < 0) {
            brightnessPercent = 0;
        }
        this.mDialogBrightnessTextView.setText(brightnessPercent + "%");
        this.mDialogBrightnessProgressBar.setProgress(brightnessPercent);
        onCLickUiToggleToClear();
    }

    public void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
        if (this.mBrightnessDialog != null) {
            this.mBrightnessDialog.dismiss();
        }
    }

    public Dialog createDialogWithView(View localView) {
        Dialog dialog = new Dialog(getContext(), C0783R.style.jz_style_dialog_progress);
        dialog.setContentView(localView);
        Window window = dialog.getWindow();
        window.addFlags(8);
        window.addFlags(32);
        window.addFlags(16);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.gravity = 17;
        window.setAttributes(localLayoutParams);
        return dialog;
    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        this.mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(this.mDismissControlViewTimerTask, 2500);
    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
        }
        if (this.mDismissControlViewTimerTask != null) {
            this.mDismissControlViewTimerTask.cancel();
        }
    }

    public void onAutoCompletion() {
        super.onAutoCompletion();
        cancelDismissControlViewTimer();
    }

    public void onCompletion() {
        super.onCompletion();
        cancelDismissControlViewTimer();
        if (this.clarityPopWindow != null) {
            this.clarityPopWindow.dismiss();
        }
    }

    public void dissmissControlView() {
        if (this.currentState != 0 && this.currentState != 7 && this.currentState != 6) {
            post(new Runnable() {
                public void run() {
                    JZVideoPlayerStandard.this.bottomContainer.setVisibility(4);
                    JZVideoPlayerStandard.this.startButton.setVisibility(4);
                    if (JZVideoPlayerStandard.this.clarityPopWindow != null) {
                        JZVideoPlayerStandard.this.clarityPopWindow.dismiss();
                    }
                    if (JZVideoPlayerStandard.this.currentScreen != 3) {
                    }
                }
            });
        }
    }

    /* renamed from: cn.jzvd.JZVideoPlayerStandard$DismissControlViewTimerTask */
    public class DismissControlViewTimerTask extends TimerTask {
        public DismissControlViewTimerTask() {
        }

        public void run() {
            JZVideoPlayerStandard.this.dissmissControlView();
        }
    }
}
