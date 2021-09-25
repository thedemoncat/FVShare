package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.umeng.analytics.MobclickAgent;
import java.io.File;
import java.util.List;

public class VideoEditingItemEachActivity extends Activity implements View.OnClickListener {
    private final int PHOTOPATHSRESULT = 20;
    @Bind({2131755523})
    ImageView act_video_editing_bofang;
    @Bind({2131755542})
    ImageView act_video_editing_item_each_imageview;
    @Bind({2131755543})
    ImageView act_video_editing_item_each_imageview1;
    @Bind({2131755544})
    ImageView act_video_editing_item_each_imageview2;
    @Bind({2131755522})
    ImageView act_video_editing_zhanting;
    private Activity activity;
    private BaseRVAdapter adapter;
    @Bind({2131756214})
    ImageView img_video_editing_back;
    private MediaController mController;
    int pos = 1000;
    @Bind({2131756216})
    TextView tv_video_editing_all_baocun;
    @Bind({2131756217})
    TextView tv_video_editing_right_share;
    private List videoList;
    private String videoPaths;
    @Bind({2131755541})
    VideoView vv_video;

    public static Intent createIntent(Context context, String listVideo) {
        Intent intent = new Intent(context, VideoEditingItemEachActivity.class);
        intent.putExtra(IntentKey.VIDEOS_PATH, listVideo);
        return intent;
    }

    private void parseIntent() {
        this.videoPaths = getIntent().getStringExtra(IntentKey.VIDEOS_PATH);
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_video_editing_item_each);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        parseIntent();
        Log.e("-----------------", "-------------videosPath-----" + this.videoPaths);
        initView();
        this.mController = new MediaController(this);
        playEditingVideoFirst();
    }

    private void playEditingVideoFirst() {
        File file = new File(this.videoPaths);
        if (file.exists()) {
            this.vv_video.setVideoPath(file.getAbsolutePath());
            this.mController.setMediaPlayer(this.vv_video);
            this.vv_video.start();
            this.vv_video.seekTo(0);
        }
    }

    private void playEditingVideo() {
        File file = new File(this.videoPaths);
        if (file.exists()) {
            this.vv_video.setVideoPath(file.getAbsolutePath());
            this.mController.setMediaPlayer(this.vv_video);
            this.vv_video.start();
            this.vv_video.seekTo(0);
        }
    }

    private void initView() {
        Glide.with(this.activity).load(this.videoPaths).into(this.act_video_editing_item_each_imageview);
        Glide.with(this.activity).load(this.videoPaths).into(this.act_video_editing_item_each_imageview1);
        Glide.with(this.activity).load(this.videoPaths).into(this.act_video_editing_item_each_imageview2);
        this.tv_video_editing_right_share.setVisibility(8);
        this.tv_video_editing_all_baocun.setVisibility(8);
        this.img_video_editing_back.setOnClickListener(this);
        this.act_video_editing_zhanting.setOnClickListener(this);
        this.act_video_editing_bofang.setOnClickListener(this);
        this.vv_video.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.act_video_editing_zhanting:
                this.act_video_editing_zhanting.setVisibility(8);
                this.act_video_editing_bofang.setVisibility(0);
                this.vv_video.stopPlayback();
                return;
            case C0853R.C0855id.act_video_editing_bofang:
                this.act_video_editing_zhanting.setVisibility(0);
                this.act_video_editing_bofang.setVisibility(8);
                this.vv_video.stopPlayback();
                playEditingVideo();
                return;
            case C0853R.C0855id.img_video_editing_back:
                finish();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
