package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;
import com.freevisiontech.fvmobile.C0853R;
import com.umeng.analytics.MobclickAgent;
import java.io.File;

public class VideoViewActivity extends Activity {
    private final String TAG = "main";
    private Button btn_pause;
    /* access modifiers changed from: private */
    public Button btn_play;
    private Button btn_replay;
    private Button btn_stop;
    private SeekBar.OnSeekBarChangeListener change = new SeekBar.OnSeekBarChangeListener() {
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            if (VideoViewActivity.this.vv_video != null && VideoViewActivity.this.vv_video.isPlaying()) {
                VideoViewActivity.this.vv_video.seekTo(10000);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }
    };
    private View.OnClickListener click = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case C0853R.C0855id.btn_play:
                    VideoViewActivity.this.play(0);
                    return;
                case C0853R.C0855id.btn_pause:
                    VideoViewActivity.this.pause();
                    return;
                case C0853R.C0855id.btn_replay:
                    VideoViewActivity.this.replay();
                    return;
                case C0853R.C0855id.btn_stop:
                    VideoViewActivity.this.stop();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean isPlaying;
    /* access modifiers changed from: private */
    public SeekBar seekBar;
    private String video_path;
    /* access modifiers changed from: private */
    public VideoView vv_video;

    public static Intent createIntent(Context context, String video_path2) {
        Intent intent = new Intent(context, VideoViewActivity.class);
        intent.putExtra("video_path", video_path2);
        return intent;
    }

    private void parseIntent() {
        this.video_path = getIntent().getStringExtra("video_path");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_videoview);
        parseIntent();
        this.seekBar = (SeekBar) findViewById(C0853R.C0855id.seekBar);
        this.vv_video = (VideoView) findViewById(C0853R.C0855id.vv_videoview);
        this.btn_play = (Button) findViewById(C0853R.C0855id.btn_play);
        this.btn_pause = (Button) findViewById(C0853R.C0855id.btn_pause);
        this.btn_replay = (Button) findViewById(C0853R.C0855id.btn_replay);
        this.btn_stop = (Button) findViewById(C0853R.C0855id.btn_stop);
        this.btn_play.setOnClickListener(this.click);
        this.btn_pause.setOnClickListener(this.click);
        this.btn_replay.setOnClickListener(this.click);
        this.btn_stop.setOnClickListener(this.click);
        this.seekBar.setOnSeekBarChangeListener(this.change);
        play(0);
    }

    /* access modifiers changed from: protected */
    public void play(int msec) {
        Log.i("main", " 获取视频文件地址");
        File file = new File(this.video_path);
        if (!file.exists()) {
            Toast.makeText(this, "视频文件路径错误", 0).show();
            return;
        }
        Log.i("main", "指定视频源路径");
        this.vv_video.setVideoPath(file.getAbsolutePath());
        Log.i("main", "开始播放");
        this.vv_video.start();
        this.vv_video.seekTo(msec);
        this.seekBar.setMax(this.vv_video.getDuration());
        new Thread() {
            public void run() {
                try {
                    boolean unused = VideoViewActivity.this.isPlaying = true;
                    while (VideoViewActivity.this.isPlaying) {
                        int current = VideoViewActivity.this.vv_video.getCurrentPosition();
                        Log.e("-----------------", "----------current-----" + current);
                        VideoViewActivity.this.seekBar.setProgress(current / 1000);
                        sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        this.btn_play.setEnabled(false);
        this.vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                VideoViewActivity.this.btn_play.setEnabled(true);
            }
        });
        this.vv_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                VideoViewActivity.this.play(0);
                boolean unused = VideoViewActivity.this.isPlaying = false;
                return false;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void replay() {
        if (this.vv_video == null || !this.vv_video.isPlaying()) {
            this.isPlaying = false;
            play(0);
            return;
        }
        this.vv_video.seekTo(0);
        Toast.makeText(this, "重新播放", 0).show();
        this.btn_pause.setText("暂停");
    }

    /* access modifiers changed from: protected */
    public void pause() {
        if (this.btn_pause.getText().toString().trim().equals("继续")) {
            this.btn_pause.setText("暂停");
            this.vv_video.start();
            Toast.makeText(this, "继续播放", 0).show();
        } else if (this.vv_video != null && this.vv_video.isPlaying()) {
            this.vv_video.pause();
            this.btn_pause.setText("继续");
            Toast.makeText(this, "暂停播放", 0).show();
        }
    }

    /* access modifiers changed from: protected */
    public void stop() {
        if (this.vv_video != null && this.vv_video.isPlaying()) {
            this.vv_video.stopPlayback();
            this.btn_play.setEnabled(true);
            this.isPlaying = false;
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
