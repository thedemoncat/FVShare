package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.google.android.vending.expansion.downloader.Constants;
import com.makeramen.roundedimageview.RoundedImageView;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.widget.control.DataChangeNotification;
import p010me.iwf.photopicker.widget.control.IssueKey;

public class VideoEditingEndActivity extends Activity implements View.OnClickListener {
    @Bind({2131755539})
    RoundedImageView act_video_editing_end_image;
    @Bind({2131755540})
    ImageView act_video_editing_end_to_play_video;
    private Activity activity;
    @Bind({2131756085})
    ImageView img_file_back;
    private List paths;
    @Bind({2131756090})
    TextView tv_file_right_select;
    private String videoHeChengPath;

    public static Intent createIntent(Context context, String videoHeChengPath2) {
        Intent intent = new Intent(context, VideoEditingEndActivity.class);
        intent.putExtra("videoHeChengPath", videoHeChengPath2);
        return intent;
    }

    private void parseIntent() {
        this.videoHeChengPath = getIntent().getStringExtra("videoHeChengPath");
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_video_editing_end);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        parseIntent();
        initView();
        this.paths = new ArrayList();
        this.paths.add(getBasePath() + this.videoHeChengPath);
        Util.updateGalleryForVideo(this, getBasePath() + this.videoHeChengPath);
    }

    private String getBasePath() {
        if (!((String) SPUtils.get(this.activity, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
            return Util.getParentPath(this.activity);
        }
        return IntentKey.FILE_PATH;
    }

    private void initView() {
        this.img_file_back.setVisibility(0);
        this.tv_file_right_select.setText(C0853R.string.home_fragment_bottom_file);
        this.img_file_back.setOnClickListener(this);
        this.tv_file_right_select.setOnClickListener(this);
        this.act_video_editing_end_to_play_video.setOnClickListener(this);
        Glide.with(this.activity).load(getBasePath() + this.videoHeChengPath).asBitmap().centerCrop().into(this.act_video_editing_end_image);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.act_video_editing_end_to_play_video:
                startActivity(ControllerActivity.createIntent(this, this.paths, String.valueOf(0)));
                return;
            case C0853R.C0855id.img_file_back:
                finish();
                return;
            case C0853R.C0855id.tv_file_right_select:
                BaseActivityManager.getActivityManager().popActivityOne(PlayVideoActivity.class);
                BaseActivityManager.getActivityManager().popActivityOne(FileWriteActivity.class);
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.VIDEO_SELECT_CHECK);
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
        DataChangeNotification.getInstance().notifyDataChanged(IssueKey.PHOTO_VIDEO_DEIETE);
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
