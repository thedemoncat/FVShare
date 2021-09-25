package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;

public class PhotoEditFiveActivity extends Activity implements View.OnClickListener {
    @Bind({2131755421})
    TextView act_edit_buttom_caijian_btCai;
    @Bind({2131755422})
    TextView act_edit_buttom_caijian_btXuan;
    @Bind({2131755420})
    ImageView act_edit_buttom_caijian_dismiss;
    @Bind({2131755410})
    LinearLayout act_edit_buttom_caijian_layout;
    @Bind({2131755411})
    LinearLayout act_edit_buttom_caijian_layout_caijian;
    @Bind({2131755419})
    LinearLayout act_edit_buttom_xuanzhuan_layout;
    @Bind({2131755378})
    ImageView act_photo_edit_imageview;
    @Bind({2131755384})
    TextView act_photo_edit_jiazai;
    private Activity activity;
    private int currentItem;
    private ProgressDialog mProgressDialog;
    private List<String> paths = new ArrayList();
    private String photo_edit_path;
    private List<String> strings;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_photo_edit_five);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        this.act_edit_buttom_caijian_layout.setVisibility(0);
        this.photo_edit_path = getIntent().getStringExtra(IntentKey.PHOTOEDITPATH);
        getGlideImage(this.photo_edit_path);
        initview();
    }

    private void getGlideImage(String photo_edit_path2) {
        Glide.with(getApplicationContext()).load(photo_edit_path2).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new GlideDrawableImageViewTarget(this.act_photo_edit_imageview) {
            public void onResourceReady(GlideDrawable resource, GlideAnimation animation) {
                super.onResourceReady(resource, (GlideAnimation<? super GlideDrawable>) animation);
                PhotoEditFiveActivity.this.act_photo_edit_jiazai.setVisibility(8);
            }
        });
    }

    private void initview() {
        this.act_edit_buttom_caijian_dismiss.setOnClickListener(this);
        this.act_edit_buttom_caijian_btXuan.setOnClickListener(this);
        this.act_edit_buttom_caijian_btCai.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.act_edit_buttom_caijian_dismiss:
                finish();
                return;
            case C0853R.C0855id.act_edit_buttom_caijian_btCai:
                this.act_edit_buttom_caijian_layout_caijian.setVisibility(0);
                this.act_edit_buttom_xuanzhuan_layout.setVisibility(8);
                return;
            case C0853R.C0855id.act_edit_buttom_caijian_btXuan:
                this.act_edit_buttom_xuanzhuan_layout.setVisibility(0);
                this.act_edit_buttom_caijian_layout_caijian.setVisibility(8);
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

    public void showProgress(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(this.activity);
            this.mProgressDialog.setCancelable(true);
            this.mProgressDialog.setCanceledOnTouchOutside(false);
        }
        this.mProgressDialog.setMessage(msg);
        this.mProgressDialog.show();
    }

    public void hideProgress() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
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
