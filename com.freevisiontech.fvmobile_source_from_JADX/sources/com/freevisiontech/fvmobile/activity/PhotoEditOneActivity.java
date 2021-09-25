package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
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

public class PhotoEditOneActivity extends Activity implements View.OnClickListener {
    @Bind({2131755445})
    ImageView act_edit_buttom_lvjing_dismiss;
    @Bind({2131755423})
    LinearLayout act_edit_buttom_lvjing_layout;
    @Bind({2131755446})
    ImageView act_edit_buttom_lvjing_ok;
    @Bind({2131755485})
    LinearLayout act_photo_edit_imageview_linear;
    @Bind({2131755384})
    TextView act_photo_edit_jiazai;
    @Bind({2131755487})
    GPUImageView act_photo_edit_one_imageview;
    private Activity activity;
    private Bitmap bitmap;
    private int currentItem;
    @Bind({2131755490})
    SeekBar edit_lvjing_seekbar;
    @Bind({2131755491})
    TextView edit_lvjing_seekbar_bili;
    @Bind({2131755488})
    RelativeLayout edit_lvjing_seekbar_rl_layout;
    private GPUImageFilter filter;
    /* access modifiers changed from: private */
    public int height;
    private GPUImageFilter mFilter;
    /* access modifiers changed from: private */
    public GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private LoadingView mProgressDialog;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) PhotoEditOneActivity.this.act_photo_edit_imageview_linear.getLayoutParams();
                    linearParams2.width = PhotoEditOneActivity.this.width;
                    linearParams2.height = PhotoEditOneActivity.this.height;
                    PhotoEditOneActivity.this.act_photo_edit_imageview_linear.setLayoutParams(linearParams2);
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
    public int pro0 = 0;
    /* access modifiers changed from: private */
    public int pro1 = 0;
    /* access modifiers changed from: private */
    public int pro2 = 0;
    /* access modifiers changed from: private */
    public int pro3 = 0;
    /* access modifiers changed from: private */
    public int pro4 = 0;
    /* access modifiers changed from: private */
    public int pro5 = 0;
    /* access modifiers changed from: private */
    public int pro6 = 0;
    /* access modifiers changed from: private */
    public int pro7 = 0;
    /* access modifiers changed from: private */
    public int pro8 = 0;
    @Bind({2131755425})
    GPUImageView recycle_item_photo_edit_one_image0;
    @Bind({2131755428})
    GPUImageView recycle_item_photo_edit_one_image1;
    @Bind({2131755431})
    GPUImageView recycle_item_photo_edit_one_image2;
    @Bind({2131755434})
    GPUImageView recycle_item_photo_edit_one_image3;
    @Bind({2131755437})
    GPUImageView recycle_item_photo_edit_one_image4;
    @Bind({2131755440})
    GPUImageView recycle_item_photo_edit_one_image5;
    @Bind({2131755443})
    GPUImageView recycle_item_photo_edit_one_image6;
    @Bind({2131755424})
    ImageView recycle_item_photo_edit_one_kuang0;
    @Bind({2131755427})
    ImageView recycle_item_photo_edit_one_kuang1;
    @Bind({2131755430})
    ImageView recycle_item_photo_edit_one_kuang2;
    @Bind({2131755433})
    ImageView recycle_item_photo_edit_one_kuang3;
    @Bind({2131755436})
    ImageView recycle_item_photo_edit_one_kuang4;
    @Bind({2131755439})
    ImageView recycle_item_photo_edit_one_kuang5;
    @Bind({2131755442})
    ImageView recycle_item_photo_edit_one_kuang6;
    /* access modifiers changed from: private */
    public int selectPosition = 0;
    private List<String> strings;
    /* access modifiers changed from: private */
    public int width;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_photo_edit_one);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        this.act_edit_buttom_lvjing_layout.setVisibility(0);
        this.photo_edit_path = getIntent().getStringExtra(IntentKey.PHOTOEDITPATH);
        this.edit_lvjing_seekbar_bili.setText("0%");
        this.bitmap = BitmapFactory.decodeFile(this.photo_edit_path);
        this.act_photo_edit_one_imageview.setImage(this.bitmap);
        this.recycle_item_photo_edit_one_image0.setImage(this.bitmap);
        this.recycle_item_photo_edit_one_image1.setImage(this.bitmap);
        this.recycle_item_photo_edit_one_image2.setImage(this.bitmap);
        this.recycle_item_photo_edit_one_image3.setImage(this.bitmap);
        this.recycle_item_photo_edit_one_image4.setImage(this.bitmap);
        this.recycle_item_photo_edit_one_image5.setImage(this.bitmap);
        this.recycle_item_photo_edit_one_image6.setImage(this.bitmap);
        this.recycle_item_photo_edit_one_image0.setFilter(FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.NOFILTER));
        this.recycle_item_photo_edit_one_image1.setFilter(FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.SEPIA));
        this.recycle_item_photo_edit_one_image2.setFilter(FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.GRAYSCALE));
        this.recycle_item_photo_edit_one_image3.setFilter(FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.SKETCH));
        this.recycle_item_photo_edit_one_image4.setFilter(FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.LOOKUP_AMATORKA));
        this.recycle_item_photo_edit_one_image5.setFilter(FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.TONE_CURVE));
        this.recycle_item_photo_edit_one_image6.setFilter(FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.EMBOSS));
        setSeekBar();
        initview();
        setLvJingKuang(0);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(this.photo_edit_path, options);
        final int drawableWidth = options.outWidth;
        final int drawableHeight = options.outHeight;
        this.act_photo_edit_imageview_linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PhotoEditOneActivity.this.act_photo_edit_imageview_linear.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int unused = PhotoEditOneActivity.this.width = PhotoEditOneActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                int unused2 = PhotoEditOneActivity.this.height = PhotoEditOneActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                if (drawableWidth > drawableHeight) {
                    int unused3 = PhotoEditOneActivity.this.width = PhotoEditOneActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                    int unused4 = PhotoEditOneActivity.this.height = (PhotoEditOneActivity.this.width * drawableHeight) / drawableWidth;
                    PhotoEditOneActivity.this.sendToHandler(11);
                    return;
                }
                int unused5 = PhotoEditOneActivity.this.width = (PhotoEditOneActivity.this.height * drawableWidth) / drawableHeight;
                int unused6 = PhotoEditOneActivity.this.height = PhotoEditOneActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                PhotoEditOneActivity.this.sendToHandler(11);
            }
        });
    }

    public void sendToHandler(int what) {
        Message me = new Message();
        me.what = what;
        this.myHandler.sendMessage(me);
    }

    private void setLvJingKuang(int i) {
        this.recycle_item_photo_edit_one_kuang0.setVisibility(8);
        this.recycle_item_photo_edit_one_kuang1.setVisibility(8);
        this.recycle_item_photo_edit_one_kuang2.setVisibility(8);
        this.recycle_item_photo_edit_one_kuang3.setVisibility(8);
        this.recycle_item_photo_edit_one_kuang4.setVisibility(8);
        this.recycle_item_photo_edit_one_kuang5.setVisibility(8);
        this.recycle_item_photo_edit_one_kuang6.setVisibility(8);
        switch (i) {
            case 0:
                this.recycle_item_photo_edit_one_kuang0.setVisibility(0);
                this.edit_lvjing_seekbar_rl_layout.setVisibility(8);
                return;
            case 1:
                this.recycle_item_photo_edit_one_kuang1.setVisibility(0);
                this.edit_lvjing_seekbar_rl_layout.setVisibility(0);
                return;
            case 2:
                this.recycle_item_photo_edit_one_kuang2.setVisibility(0);
                this.edit_lvjing_seekbar_rl_layout.setVisibility(8);
                return;
            case 3:
                this.recycle_item_photo_edit_one_kuang3.setVisibility(0);
                this.edit_lvjing_seekbar_rl_layout.setVisibility(8);
                return;
            case 4:
                this.recycle_item_photo_edit_one_kuang4.setVisibility(0);
                this.edit_lvjing_seekbar_rl_layout.setVisibility(8);
                return;
            case 5:
                this.recycle_item_photo_edit_one_kuang5.setVisibility(0);
                this.edit_lvjing_seekbar_rl_layout.setVisibility(8);
                return;
            case 6:
                this.recycle_item_photo_edit_one_kuang6.setVisibility(0);
                this.edit_lvjing_seekbar_rl_layout.setVisibility(0);
                return;
            default:
                return;
        }
    }

    private void setSeekBar() {
        this.edit_lvjing_seekbar.setMax(100);
        this.edit_lvjing_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int unused = PhotoEditOneActivity.this.okProgress = progress;
                if (PhotoEditOneActivity.this.selectPosition == 0) {
                    int unused2 = PhotoEditOneActivity.this.pro0 = progress;
                } else if (PhotoEditOneActivity.this.selectPosition == 1) {
                    int unused3 = PhotoEditOneActivity.this.pro1 = progress;
                } else if (PhotoEditOneActivity.this.selectPosition == 2) {
                    int unused4 = PhotoEditOneActivity.this.pro2 = progress;
                } else if (PhotoEditOneActivity.this.selectPosition == 3) {
                    int unused5 = PhotoEditOneActivity.this.pro3 = progress;
                } else if (PhotoEditOneActivity.this.selectPosition == 4) {
                    int unused6 = PhotoEditOneActivity.this.pro4 = progress;
                } else if (PhotoEditOneActivity.this.selectPosition == 5) {
                    int unused7 = PhotoEditOneActivity.this.pro5 = progress;
                } else if (PhotoEditOneActivity.this.selectPosition == 6) {
                    int unused8 = PhotoEditOneActivity.this.pro6 = progress;
                } else if (PhotoEditOneActivity.this.selectPosition == 7) {
                    int unused9 = PhotoEditOneActivity.this.pro7 = progress;
                } else if (PhotoEditOneActivity.this.selectPosition == 8) {
                    int unused10 = PhotoEditOneActivity.this.pro8 = progress;
                }
                if (PhotoEditOneActivity.this.mFilterAdjuster != null) {
                    PhotoEditOneActivity.this.mFilterAdjuster.adjust(progress);
                    Log.e("-------------", "------------progress----" + progress);
                    PhotoEditOneActivity.this.edit_lvjing_seekbar_bili.setText(progress + "%");
                }
                PhotoEditOneActivity.this.act_photo_edit_one_imageview.requestRender();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void switchFilterTo(GPUImageFilter filter2) {
        if (this.mFilter == null || (filter2 != null && !this.mFilter.getClass().equals(filter2.getClass()))) {
            this.mFilter = filter2;
            this.act_photo_edit_one_imageview.setFilter(this.mFilter);
            this.mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(this.mFilter);
        }
    }

    private void initview() {
        this.act_edit_buttom_lvjing_dismiss.setOnClickListener(this);
        this.recycle_item_photo_edit_one_image0.setOnClickListener(this);
        this.recycle_item_photo_edit_one_image1.setOnClickListener(this);
        this.recycle_item_photo_edit_one_image2.setOnClickListener(this);
        this.recycle_item_photo_edit_one_image3.setOnClickListener(this);
        this.recycle_item_photo_edit_one_image4.setOnClickListener(this);
        this.recycle_item_photo_edit_one_image5.setOnClickListener(this);
        this.recycle_item_photo_edit_one_image6.setOnClickListener(this);
        this.act_edit_buttom_lvjing_ok.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.recycle_item_photo_edit_one_image0:
                this.selectPosition = 0;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.NOFILTER);
                switchFilterTo(this.filter);
                if (this.pro0 == 0) {
                    this.edit_lvjing_seekbar.setProgress(50);
                } else {
                    this.edit_lvjing_seekbar.setProgress(this.pro0);
                }
                setLvJingKuang(0);
                return;
            case C0853R.C0855id.recycle_item_photo_edit_one_image1:
                this.selectPosition = 1;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.SEPIA);
                switchFilterTo(this.filter);
                if (this.pro1 == 0) {
                    this.edit_lvjing_seekbar.setProgress(50);
                } else {
                    this.edit_lvjing_seekbar.setProgress(this.pro1);
                }
                setLvJingKuang(1);
                return;
            case C0853R.C0855id.recycle_item_photo_edit_one_image2:
                this.selectPosition = 2;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.GRAYSCALE);
                switchFilterTo(this.filter);
                if (this.pro2 == 0) {
                    this.edit_lvjing_seekbar.setProgress(50);
                } else {
                    this.edit_lvjing_seekbar.setProgress(this.pro2);
                }
                setLvJingKuang(2);
                return;
            case C0853R.C0855id.recycle_item_photo_edit_one_image3:
                this.selectPosition = 3;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.SKETCH);
                switchFilterTo(this.filter);
                if (this.pro3 == 0) {
                    this.edit_lvjing_seekbar.setProgress(50);
                } else {
                    this.edit_lvjing_seekbar.setProgress(this.pro3);
                }
                setLvJingKuang(3);
                return;
            case C0853R.C0855id.recycle_item_photo_edit_one_image4:
                this.selectPosition = 4;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.LOOKUP_AMATORKA);
                switchFilterTo(this.filter);
                if (this.pro4 == 0) {
                    this.edit_lvjing_seekbar.setProgress(50);
                } else {
                    this.edit_lvjing_seekbar.setProgress(this.pro4);
                }
                setLvJingKuang(4);
                return;
            case C0853R.C0855id.recycle_item_photo_edit_one_image5:
                this.selectPosition = 5;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.TONE_CURVE);
                switchFilterTo(this.filter);
                if (this.pro5 == 0) {
                    this.edit_lvjing_seekbar.setProgress(50);
                } else {
                    this.edit_lvjing_seekbar.setProgress(this.pro5);
                }
                setLvJingKuang(5);
                return;
            case C0853R.C0855id.recycle_item_photo_edit_one_image6:
                this.selectPosition = 6;
                this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.EMBOSS);
                switchFilterTo(this.filter);
                if (this.pro6 == 0) {
                    this.edit_lvjing_seekbar.setProgress(50);
                } else {
                    this.edit_lvjing_seekbar.setProgress(this.pro6);
                }
                setLvJingKuang(6);
                return;
            case C0853R.C0855id.act_edit_buttom_lvjing_dismiss:
                finish();
                return;
            case C0853R.C0855id.act_edit_buttom_lvjing_ok:
                showProgress(getString(C0853R.string.label_produce_ing));
                if (this.selectPosition == 0) {
                    finish();
                    return;
                }
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

    private void setPositionZero() {
        this.selectPosition = 0;
        this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.NOFILTER);
        switchFilterTo(this.filter);
        if (this.pro0 == 0) {
        }
    }

    private void setPositionOne() {
        this.selectPosition = 1;
        this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.SEPIA);
        switchFilterTo(this.filter);
        if (this.pro1 == 0) {
            this.edit_lvjing_seekbar.setProgress(50);
        } else {
            this.edit_lvjing_seekbar.setProgress(this.pro1);
        }
    }

    private void setPositionTwo() {
        this.selectPosition = 2;
        this.filter = FVFilterManager.createGPUImageFilterForType(this.activity, FilterType.GRAYSCALE);
        switchFilterTo(this.filter);
        if (this.pro2 == 0) {
            this.edit_lvjing_seekbar.setProgress(50);
        } else {
            this.edit_lvjing_seekbar.setProgress(this.pro2);
        }
    }

    private Bitmap getSelectFilterBitmap(int selectPosition2) {
        Bitmap bitmapNew = this.bitmap;
        switch (selectPosition2) {
            case 0:
                return FVImageProcessor.applyGPUFilter(this.bitmap, (Context) this.activity, FilterType.NOFILTER);
            case 1:
                return FVImageProcessor.applyGPUFilter(this.bitmap, this.activity, FilterType.SEPIA, this.okProgress);
            case 2:
                return FVImageProcessor.applyGPUFilter(this.bitmap, (Context) this.activity, FilterType.GRAYSCALE);
            case 3:
                return FVImageProcessor.applyGPUFilter(this.bitmap, (Context) this.activity, FilterType.SKETCH);
            case 4:
                return FVImageProcessor.applyGPUFilter(this.bitmap, (Context) this.activity, FilterType.LOOKUP_AMATORKA);
            case 5:
                return FVImageProcessor.applyGPUFilter(this.bitmap, (Context) this.activity, FilterType.TONE_CURVE);
            case 6:
                return FVImageProcessor.applyGPUFilter(this.bitmap, this.activity, FilterType.EMBOSS, this.okProgress);
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
