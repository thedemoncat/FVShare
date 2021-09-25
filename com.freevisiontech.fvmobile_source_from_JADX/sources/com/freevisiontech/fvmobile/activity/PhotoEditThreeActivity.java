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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.filterlib.FVFilterManager;
import com.freevisiontech.filterlib.FilterType;
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

public class PhotoEditThreeActivity extends Activity implements View.OnClickListener {
    @Bind({2131755483})
    ImageView act_edit_buttom_tiaojie_dismiss;
    @Bind({2131755479})
    LinearLayout act_edit_buttom_tiaojie_layout;
    @Bind({2131755484})
    ImageView act_edit_buttom_tiaojie_ok;
    @Bind({2131755378})
    GPUImageView act_photo_edit_imageview;
    @Bind({2131755485})
    LinearLayout act_photo_edit_imageview_linear;
    @Bind({2131755384})
    TextView act_photo_edit_jiazai;
    private Activity activity;
    private Bitmap bitmap;
    private int currentItem;
    @Bind({2131755495})
    SeekBar edit_tiaojie_seekbar;
    @Bind({2131755496})
    TextView edit_tiaojie_seekbar_bili;
    private GPUImageFilter filter;
    /* access modifiers changed from: private */
    public int height;
    @Bind({2131755482})
    RadioButton id_edit_buttom_tiaojie_bao;
    @Bind({2131755480})
    RadioButton id_edit_buttom_tiaojie_duibi;
    @Bind({2131755481})
    RadioButton id_edit_buttom_tiaojie_liang;
    private GPUImageFilter mFilter;
    /* access modifiers changed from: private */
    public GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private LoadingView mProgressDialog;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) PhotoEditThreeActivity.this.act_photo_edit_imageview_linear.getLayoutParams();
                    linearParams2.width = PhotoEditThreeActivity.this.width;
                    linearParams2.height = PhotoEditThreeActivity.this.height;
                    PhotoEditThreeActivity.this.act_photo_edit_imageview_linear.setLayoutParams(linearParams2);
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
    /* access modifiers changed from: private */
    public int pro1 = 0;
    /* access modifiers changed from: private */
    public int pro2 = 0;
    /* access modifiers changed from: private */
    public int pro3 = 0;
    /* access modifiers changed from: private */
    public int selectPosition = 1;
    private List<String> strings;
    /* access modifiers changed from: private */
    public int width;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_photo_edit_three);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        this.act_edit_buttom_tiaojie_layout.setVisibility(0);
        this.photo_edit_path = getIntent().getStringExtra(IntentKey.PHOTOEDITPATH);
        this.edit_tiaojie_seekbar_bili.setText("0%");
        this.bitmap = BitmapFactory.decodeFile(this.photo_edit_path);
        this.act_photo_edit_imageview.setImage(this.bitmap);
        switchFilterTo(FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.CONTRAST));
        setSeekBar();
        initview();
        this.edit_tiaojie_seekbar.setProgress(50);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(this.photo_edit_path, options);
        final int drawableWidth = options.outWidth;
        final int drawableHeight = options.outHeight;
        this.act_photo_edit_imageview_linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PhotoEditThreeActivity.this.act_photo_edit_imageview_linear.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int unused = PhotoEditThreeActivity.this.width = PhotoEditThreeActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                int unused2 = PhotoEditThreeActivity.this.height = PhotoEditThreeActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                if (drawableWidth > drawableHeight) {
                    int unused3 = PhotoEditThreeActivity.this.width = PhotoEditThreeActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                    int unused4 = PhotoEditThreeActivity.this.height = (PhotoEditThreeActivity.this.width * drawableHeight) / drawableWidth;
                    PhotoEditThreeActivity.this.sendToHandler(11);
                    return;
                }
                int unused5 = PhotoEditThreeActivity.this.width = (PhotoEditThreeActivity.this.height * drawableWidth) / drawableHeight;
                int unused6 = PhotoEditThreeActivity.this.height = PhotoEditThreeActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                PhotoEditThreeActivity.this.sendToHandler(11);
            }
        });
    }

    public void sendToHandler(int what) {
        Message me = new Message();
        me.what = what;
        this.myHandler.sendMessage(me);
    }

    private void setSeekBar() {
        this.edit_tiaojie_seekbar.setMax(100);
        this.edit_tiaojie_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int unused = PhotoEditThreeActivity.this.okProgress = progress;
                if (PhotoEditThreeActivity.this.selectPosition == 1) {
                    int unused2 = PhotoEditThreeActivity.this.pro1 = progress;
                } else if (PhotoEditThreeActivity.this.selectPosition == 2) {
                    int unused3 = PhotoEditThreeActivity.this.pro2 = progress;
                } else if (PhotoEditThreeActivity.this.selectPosition == 3) {
                    int unused4 = PhotoEditThreeActivity.this.pro3 = progress;
                }
                if (PhotoEditThreeActivity.this.mFilterAdjuster != null) {
                    PhotoEditThreeActivity.this.mFilterAdjuster.adjust(progress);
                    PhotoEditThreeActivity.this.edit_tiaojie_seekbar_bili.setText(progress + "%");
                }
                PhotoEditThreeActivity.this.act_photo_edit_imageview.requestRender();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initview() {
        this.act_edit_buttom_tiaojie_dismiss.setOnClickListener(this);
        this.id_edit_buttom_tiaojie_duibi.setOnClickListener(this);
        this.id_edit_buttom_tiaojie_liang.setOnClickListener(this);
        this.id_edit_buttom_tiaojie_bao.setOnClickListener(this);
        this.act_edit_buttom_tiaojie_ok.setOnClickListener(this);
    }

    private void switchFilterTo(GPUImageFilter filter2) {
        if (this.mFilter == null || (filter2 != null && !this.mFilter.getClass().equals(filter2.getClass()))) {
            this.mFilter = filter2;
            this.act_photo_edit_imageview.setFilter(this.mFilter);
            this.mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(this.mFilter);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.id_edit_buttom_tiaojie_duibi:
                this.selectPosition = 1;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.CONTRAST);
                switchFilterTo(this.filter);
                if (this.pro1 == 0) {
                    this.edit_tiaojie_seekbar.setProgress(50);
                    return;
                } else {
                    this.edit_tiaojie_seekbar.setProgress(this.pro1);
                    return;
                }
            case C0853R.C0855id.id_edit_buttom_tiaojie_liang:
                this.selectPosition = 2;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.EXPOSURE);
                switchFilterTo(this.filter);
                if (this.pro2 == 0) {
                    this.edit_tiaojie_seekbar.setProgress(50);
                    return;
                } else {
                    this.edit_tiaojie_seekbar.setProgress(this.pro2);
                    return;
                }
            case C0853R.C0855id.id_edit_buttom_tiaojie_bao:
                this.selectPosition = 3;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.SATURATION);
                switchFilterTo(this.filter);
                if (this.pro3 == 0) {
                    this.edit_tiaojie_seekbar.setProgress(50);
                    return;
                } else {
                    this.edit_tiaojie_seekbar.setProgress(this.pro3);
                    return;
                }
            case C0853R.C0855id.act_edit_buttom_tiaojie_dismiss:
                finish();
                return;
            case C0853R.C0855id.act_edit_buttom_tiaojie_ok:
                showProgress(getString(C0853R.string.label_produce_ing));
                String pathOne = PhotoEditUtils.saveBitmapToDrawable(getSelectFilterBitmap(this.selectPosition));
                Intent intent = new Intent();
                intent.putExtra(IntentKey.PAINTPATH, pathOne);
                setResult(-1, intent);
                finish();
                return;
            default:
                return;
        }
    }

    private Bitmap getSelectFilterBitmap(int selectPosition2) {
        Bitmap bitmapNew = this.bitmap;
        switch (selectPosition2) {
            case 1:
                return FVImageProcessor.applyGPUFilter(this.bitmap, this.activity, FilterType.CONTRAST, this.okProgress);
            case 2:
                return FVImageProcessor.applyGPUFilter(this.bitmap, this.activity, FilterType.EXPOSURE, this.okProgress);
            case 3:
                return FVImageProcessor.applyGPUFilter(this.bitmap, this.activity, FilterType.SATURATION, this.okProgress);
            default:
                return bitmapNew;
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
