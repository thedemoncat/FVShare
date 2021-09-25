package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.Util;
import com.google.android.exoplayer.C1907C;
import com.umeng.analytics.MobclickAgent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ControllerActivity extends Activity implements View.OnClickListener {
    @Bind({2131755249})
    ImageView img_back;
    private MediaController mController;
    /* access modifiers changed from: private */
    public MyCountDownTimer mMyCountDownTimer;
    /* access modifiers changed from: private */
    public List<String> paths;
    private String photoPosition;
    /* access modifiers changed from: private */
    public int pos;
    private int position;
    /* access modifiers changed from: private */
    public VideoView vv_video;

    public static Intent createIntent(Context context, List<String> paths2, String photoPosition2) {
        Intent intent = new Intent(context, ControllerActivity.class);
        intent.putStringArrayListExtra("paths", (ArrayList) paths2);
        intent.putExtra("photoPosition", photoPosition2);
        return intent;
    }

    private void parseIntent() {
        Intent intent = getIntent();
        this.paths = intent.getStringArrayListExtra("paths");
        this.photoPosition = intent.getStringExtra("photoPosition");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_controller);
        ButterKnife.bind((Activity) this);
        parseIntent();
        this.position = Integer.valueOf(this.photoPosition).intValue();
        this.vv_video = (VideoView) findViewById(C0853R.C0855id.vv_video);
        playControllerVideo(this.position);
        this.img_back.setVisibility(0);
        this.img_back.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(1024);
        }
        hideBottomUIMenu();
    }

    /* access modifiers changed from: protected */
    public void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(3846);
            getWindow().addFlags(C1907C.SAMPLE_FLAG_DECODE_ONLY);
        }
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        playControllerVideo(this.position);
    }

    public static int getBottomStatusHeight(Context context) {
        return getDpi(context) - getScreenHeight(context);
    }

    public static int getDpi(Context context) {
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class.forName("android.view.Display").getMethod("getRealMetrics", new Class[]{DisplayMetrics.class}).invoke(display, new Object[]{displayMetrics});
            return displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /* access modifiers changed from: private */
    public void playControllerVideo(final int position2) {
        String videoPath = this.paths.get(position2).toString();
        this.mController = new MediaController(this);
        File file = new File(videoPath);
        if (file.exists()) {
            this.vv_video.setVideoPath(file.getAbsolutePath());
            this.vv_video.setMediaController(this.mController);
            if (Util.getPhoneVirtualKey(this)) {
                this.mController.setPadding(0, 0, 0, getBottomStatusHeight(this));
            }
            this.mController.setMediaPlayer(this.vv_video);
            this.vv_video.start();
            this.vv_video.seekTo(0);
            this.vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    ControllerActivity.this.vv_video.seekTo(ControllerActivity.this.vv_video.getDuration());
                    if (ControllerActivity.this.mMyCountDownTimer != null) {
                        ControllerActivity.this.mMyCountDownTimer.cancel();
                        MyCountDownTimer unused = ControllerActivity.this.mMyCountDownTimer = new MyCountDownTimer(1000, 1000);
                        ControllerActivity.this.mMyCountDownTimer.start();
                        return;
                    }
                    MyCountDownTimer unused2 = ControllerActivity.this.mMyCountDownTimer = new MyCountDownTimer(1000, 1000);
                    ControllerActivity.this.mMyCountDownTimer.start();
                }
            });
            this.mController.setPrevNextListeners(new View.OnClickListener() {
                public void onClick(View v) {
                    if (position2 == ControllerActivity.this.paths.size() - 1) {
                        Toast.makeText(ControllerActivity.this, C0853R.string.file_video_play_end, 0).show();
                        return;
                    }
                    if (position2 >= 0 && position2 < ControllerActivity.this.paths.size()) {
                        int unused = ControllerActivity.this.pos = position2 + 1;
                        ControllerActivity.this.vv_video.stopPlayback();
                        ControllerActivity.this.playControllerVideo(ControllerActivity.this.pos);
                    }
                    Toast.makeText(ControllerActivity.this, C0853R.string.file_video_play_the_next, 0).show();
                }
            }, new View.OnClickListener() {
                public void onClick(View v) {
                    if (position2 == 0) {
                        Toast.makeText(ControllerActivity.this, C0853R.string.file_video_play_first, 0).show();
                        return;
                    }
                    if (position2 >= 0 && position2 < ControllerActivity.this.paths.size()) {
                        int unused = ControllerActivity.this.pos = position2 - 1;
                        ControllerActivity.this.vv_video.stopPlayback();
                        ControllerActivity.this.playControllerVideo(ControllerActivity.this.pos);
                    }
                    Toast.makeText(ControllerActivity.this, C0853R.string.file_video_play_the_one, 0).show();
                }
            });
        }
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.img_back:
                finish();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
        }
        this.vv_video.stopPlayback();
        this.vv_video = null;
        ButterKnife.unbind(this);
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            ControllerActivity.this.finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
