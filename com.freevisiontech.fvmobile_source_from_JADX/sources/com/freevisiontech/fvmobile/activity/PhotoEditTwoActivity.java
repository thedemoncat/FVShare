package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.filterlib.FVFilterManager;
import com.freevisiontech.filterlib.FilterType;
import com.freevisiontech.filterlib.myfilter.GPUImageBeautyFilter;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.PhotoEditUtils;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.freevisiontech.imgproclib.FVImageProcessor;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageView;
import p008jp.p009co.cyberagent.android.gpuimage.sample.GPUImageFilterTools;

public class PhotoEditTwoActivity extends Activity implements View.OnClickListener {
    @Bind({2131755466})
    ImageView act_edit_buttom_meiyan_dismiss;
    @Bind({2131755465})
    LinearLayout act_edit_buttom_meiyan_layout;
    @Bind({2131755467})
    ImageView act_edit_buttom_meiyan_ok;
    @Bind({2131755378})
    GPUImageView act_photo_edit_imageview;
    @Bind({2131755485})
    LinearLayout act_photo_edit_imageview_linear;
    @Bind({2131755384})
    TextView act_photo_edit_jiazai;
    private Activity activity;
    private Bitmap bitmap;
    private int currentItem;
    @Bind({2131755498})
    SeekBar edit_meiyan_seekbar;
    @Bind({2131755499})
    TextView edit_meiyan_seekbar_bili;
    /* access modifiers changed from: private */
    public int height;
    /* access modifiers changed from: private */
    public GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private LoadingView mProgressDialog;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) PhotoEditTwoActivity.this.act_photo_edit_imageview_linear.getLayoutParams();
                    linearParams2.width = PhotoEditTwoActivity.this.width;
                    linearParams2.height = PhotoEditTwoActivity.this.height;
                    PhotoEditTwoActivity.this.act_photo_edit_imageview_linear.setLayoutParams(linearParams2);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public int okProgress;
    private List<String> paths = new ArrayList();
    private String photo_edit_path;
    private List<String> strings;
    /* access modifiers changed from: private */
    public int width;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_photo_edit_two);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        this.act_edit_buttom_meiyan_layout.setVisibility(0);
        this.photo_edit_path = getIntent().getStringExtra(IntentKey.PHOTOEDITPATH);
        this.okProgress = 50;
        this.edit_meiyan_seekbar_bili.setText("50%");
        this.edit_meiyan_seekbar.setProgress(this.okProgress);
        this.bitmap = BitmapFactory.decodeFile(this.photo_edit_path);
        this.act_photo_edit_imageview.setImage(this.bitmap);
        this.mFilter = new GPUImageBeautyFilter(this);
        this.act_photo_edit_imageview.setFilter(this.mFilter);
        this.mFilter = FVFilterManager.adjustGPUImageFilter(this.mFilter, 50);
        setSeekBar();
        initview();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(this.photo_edit_path, options);
        final int drawableWidth = options.outWidth;
        final int drawableHeight = options.outHeight;
        this.act_photo_edit_imageview_linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PhotoEditTwoActivity.this.act_photo_edit_imageview_linear.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int unused = PhotoEditTwoActivity.this.width = PhotoEditTwoActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                int unused2 = PhotoEditTwoActivity.this.height = PhotoEditTwoActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                if (drawableWidth > drawableHeight) {
                    int unused3 = PhotoEditTwoActivity.this.width = PhotoEditTwoActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                    int unused4 = PhotoEditTwoActivity.this.height = (PhotoEditTwoActivity.this.width * drawableHeight) / drawableWidth;
                    PhotoEditTwoActivity.this.sendToHandler(11);
                    return;
                }
                int unused5 = PhotoEditTwoActivity.this.width = (PhotoEditTwoActivity.this.height * drawableWidth) / drawableHeight;
                int unused6 = PhotoEditTwoActivity.this.height = PhotoEditTwoActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                PhotoEditTwoActivity.this.sendToHandler(11);
            }
        });
    }

    public void sendToHandler(int what) {
        Message me = new Message();
        me.what = what;
        this.myHandler.sendMessage(me);
    }

    private void initview() {
        this.act_edit_buttom_meiyan_dismiss.setOnClickListener(this);
        this.act_edit_buttom_meiyan_ok.setOnClickListener(this);
    }

    private void setSeekBar() {
        this.edit_meiyan_seekbar.setMax(100);
        this.edit_meiyan_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int unused = PhotoEditTwoActivity.this.okProgress = progress;
                if (PhotoEditTwoActivity.this.mFilter != null) {
                    GPUImageFilter unused2 = PhotoEditTwoActivity.this.mFilter = FVFilterManager.adjustGPUImageFilter(PhotoEditTwoActivity.this.mFilter, progress);
                    PhotoEditTwoActivity.this.edit_meiyan_seekbar_bili.setText(progress + "%");
                }
                PhotoEditTwoActivity.this.act_photo_edit_imageview.requestRender();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void switchFilterTo(GPUImageFilter filter) {
        if (this.mFilter == null || (filter != null && !this.mFilter.getClass().equals(filter.getClass()))) {
            this.mFilter = filter;
            this.act_photo_edit_imageview.setFilter(this.mFilter);
            this.mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(this.mFilter);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.act_edit_buttom_meiyan_dismiss:
                finish();
                return;
            case C0853R.C0855id.act_edit_buttom_meiyan_ok:
                showProgress(getString(C0853R.string.label_produce_ing));
                String pathOne = PhotoEditUtils.saveBitmapToDrawable(FVImageProcessor.applyGPUFilter(this.bitmap, this.activity, FilterType.BEAUTY, this.okProgress));
                Intent intent = new Intent();
                intent.putExtra(IntentKey.PAINTPATH, pathOne);
                setResult(-1, intent);
                finish();
                return;
            default:
                return;
        }
    }

    public void showProgress(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new LoadingView(this.activity);
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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        this.myHandler.removeCallbacksAndMessages((Object) null);
        hideProgress();
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
