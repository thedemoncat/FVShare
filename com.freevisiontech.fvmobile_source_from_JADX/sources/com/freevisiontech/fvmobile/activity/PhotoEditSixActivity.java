package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.PhotoPaintView;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;

public class PhotoEditSixActivity extends Activity implements View.OnClickListener {
    @Bind({2131755477})
    ImageView act_edit_buttom_paint_dismiss;
    @Bind({2131755468})
    LinearLayout act_edit_buttom_paint_layout;
    @Bind({2131755478})
    ImageView act_edit_buttom_paint_ok;
    @Bind({2131755476})
    RecyclerView act_edit_buttom_paint_recycler;
    @Bind({2131755378})
    ImageView act_photo_edit_imageview;
    @Bind({2131755485})
    LinearLayout act_photo_edit_imageview_linear;
    @Bind({2131755384})
    TextView act_photo_edit_jiazai;
    /* access modifiers changed from: private */
    public Activity activity;
    /* access modifiers changed from: private */
    public BaseRVAdapter adapterPaint;
    private Context context;
    private int currentItem;

    /* renamed from: dm */
    private DisplayMetrics f1092dm;
    /* access modifiers changed from: private */
    public int height;
    /* access modifiers changed from: private */
    public List listPaint;
    private LoadingView mProgressDialog;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    PhotoEditSixActivity.this.photo_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            PhotoEditSixActivity.this.photo_view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            PhotoEditSixActivity.this.photo_view.setWidthHight(PhotoEditSixActivity.this, PhotoEditSixActivity.this.width + 100, PhotoEditSixActivity.this.height);
                        }
                    });
                    RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) PhotoEditSixActivity.this.photo_edit_paint_view_linear.getLayoutParams();
                    linearParams2.width = PhotoEditSixActivity.this.width;
                    linearParams2.height = PhotoEditSixActivity.this.height;
                    PhotoEditSixActivity.this.photo_edit_paint_view_linear.setLayoutParams(linearParams2);
                    return;
                default:
                    return;
            }
        }
    };
    private List<String> paths = new ArrayList();
    @Bind({2131755492})
    LinearLayout photo_edit_paint_view_linear;
    private String photo_edit_path;
    /* access modifiers changed from: private */
    public PhotoPaintView photo_view;
    int pos = 1;
    private List<String> strings;
    /* access modifiers changed from: private */
    public int width;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_photo_edit_six);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        this.context = this;
        this.act_edit_buttom_paint_layout.setVisibility(0);
        this.photo_edit_path = getIntent().getStringExtra(IntentKey.PHOTOEDITPATH);
        initview();
        this.photo_view = (PhotoPaintView) findViewById(C0853R.C0855id.photo_edit_paint_view);
        getRecyclerView();
        Glide.with(this.activity).load(this.photo_edit_path).into(new GlideDrawableImageViewTarget(this.act_photo_edit_imageview) {
            public void onResourceReady(GlideDrawable resource, GlideAnimation animation) {
                super.onResourceReady(resource, (GlideAnimation<? super GlideDrawable>) animation);
                PhotoEditSixActivity.this.act_photo_edit_imageview_linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        PhotoEditSixActivity.this.act_photo_edit_imageview_linear.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        int unused = PhotoEditSixActivity.this.width = PhotoEditSixActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                        int unused2 = PhotoEditSixActivity.this.height = PhotoEditSixActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                        Log.e("----------", "-------------kongJianWidth: " + PhotoEditSixActivity.this.width + " kongJianHeight: " + PhotoEditSixActivity.this.height);
                        int drawableWidth = PhotoEditSixActivity.this.act_photo_edit_imageview.getDrawable().getBounds().width();
                        int drawableHeight = PhotoEditSixActivity.this.act_photo_edit_imageview.getDrawable().getBounds().height();
                        Log.e("-------------", "---------------- drawableWidth: " + drawableWidth + " drawableHeight: " + drawableHeight);
                        if (drawableWidth > drawableHeight) {
                            int unused3 = PhotoEditSixActivity.this.width = PhotoEditSixActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                            int unused4 = PhotoEditSixActivity.this.height = (PhotoEditSixActivity.this.width * drawableHeight) / drawableWidth;
                            Log.e("--------------", "------------------ widthNew: " + PhotoEditSixActivity.this.width + " heightNew: " + PhotoEditSixActivity.this.height);
                            PhotoEditSixActivity.this.sendToHandler(11);
                            return;
                        }
                        int unused5 = PhotoEditSixActivity.this.height = PhotoEditSixActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                        int unused6 = PhotoEditSixActivity.this.width = (PhotoEditSixActivity.this.height * drawableWidth) / drawableHeight;
                        Log.e("--------------", "------------------ widthNew: " + PhotoEditSixActivity.this.width + " heightNew: " + PhotoEditSixActivity.this.height);
                        PhotoEditSixActivity.this.sendToHandler(11);
                    }
                });
            }
        });
        this.photo_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PhotoEditSixActivity.this.photo_view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                PhotoEditSixActivity.this.photo_view.setWidthHight(PhotoEditSixActivity.this, 100, 100);
            }
        });
    }

    public void sendToHandler(int what) {
        Message me = new Message();
        me.what = what;
        this.myHandler.sendMessage(me);
    }

    private void getRecyclerView() {
        this.listPaint = new ArrayList();
        this.listPaint.add(-43230);
        this.listPaint.add(-43230);
        this.listPaint.add(-6381922);
        this.listPaint.add(-10011977);
        this.listPaint.add(-11110404);
        this.listPaint.add(-33280);
        this.listPaint.add(-5317);
        this.listPaint.add(-14312668);
        List<String> test = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            test.add("测试" + i);
        }
        this.act_edit_buttom_paint_recycler.setLayoutManager(new LinearLayoutManager(this.activity, 0, false));
        this.adapterPaint = new BaseRVAdapter(this.activity, test) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.recycle_item_photo_paint_color;
            }

            public void onBind(BaseViewHolder holder, final int position) {
                final ImageView paint_color20 = (ImageView) holder.getView(C0853R.C0855id.recycle_item_photo_paint_color20);
                ImageView paint_color14 = (ImageView) holder.getView(C0853R.C0855id.recycle_item_photo_paint_color14);
                RelativeLayout paint_color_all = (RelativeLayout) holder.getView(C0853R.C0855id.recycle_item_photo_paint_color_all);
                final ImageView paint_xiangpi = (ImageView) holder.getView(C0853R.C0855id.act_edit_buttom_paint_xiangpi);
                PhotoEditSixActivity.this.setColorBack(paint_color20, position);
                PhotoEditSixActivity.this.setColorBack(paint_color14, position);
                final ImageView photo_paint_xiangxia = (ImageView) holder.getView(C0853R.C0855id.recycle_item_photo_paint_xiangxia);
                photo_paint_xiangxia.setVisibility(8);
                paint_xiangpi.setBackgroundResource(C0853R.mipmap.ic_edit_photo_xiangpi);
                paint_color20.setVisibility(8);
                if (PhotoEditSixActivity.this.pos == position) {
                    paint_color20.setVisibility(0);
                    photo_paint_xiangxia.setVisibility(0);
                    if (PhotoEditSixActivity.this.pos == 0) {
                        paint_xiangpi.setBackgroundResource(C0853R.mipmap.ic_edit_photo_xiangpi_check);
                    }
                }
                if (position == 0) {
                    paint_xiangpi.setVisibility(0);
                    paint_color_all.setVisibility(8);
                } else {
                    paint_xiangpi.setVisibility(8);
                    paint_color_all.setVisibility(0);
                }
                paint_color14.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        paint_color20.setVisibility(0);
                        photo_paint_xiangxia.setVisibility(0);
                        PhotoEditSixActivity.this.pos = position;
                        PhotoEditSixActivity.this.photo_view.setPaintColorSize(PhotoEditSixActivity.this.activity, ((Integer) PhotoEditSixActivity.this.listPaint.get(position)).intValue(), 12);
                        PhotoEditSixActivity.this.adapterPaint.notifyDataSetChanged();
                    }
                });
                paint_xiangpi.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        paint_xiangpi.setBackgroundResource(C0853R.mipmap.ic_edit_photo_xiangpi_check);
                        PhotoEditSixActivity.this.pos = position;
                        PhotoEditSixActivity.this.photo_view.setPaintColorSizeCha(PhotoEditSixActivity.this.activity, 30);
                        PhotoEditSixActivity.this.adapterPaint.notifyDataSetChanged();
                    }
                });
            }
        };
        this.act_edit_buttom_paint_recycler.setAdapter(this.adapterPaint);
    }

    /* access modifiers changed from: private */
    public void setColorBack(ImageView paint_color20, int position) {
        switch (position) {
            case 0:
                paint_color20.setBackgroundResource(C0853R.C0854drawable.edit_photo_paint_round_bg_f2_check);
                return;
            case 1:
                paint_color20.setBackgroundResource(C0853R.C0854drawable.edit_photo_paint_round_bg_f2_check);
                return;
            case 2:
                paint_color20.setBackgroundResource(C0853R.C0854drawable.edit_photo_paint_round_bg_9e_check);
                return;
            case 3:
                paint_color20.setBackgroundResource(C0853R.C0854drawable.edit_photo_paint_round_bg_b7_check);
                return;
            case 4:
                paint_color20.setBackgroundResource(C0853R.C0854drawable.edit_photo_paint_round_bg_c7_check);
                return;
            case 5:
                paint_color20.setBackgroundResource(C0853R.C0854drawable.edit_photo_paint_round_bg_f0_check);
                return;
            case 6:
                paint_color20.setBackgroundResource(C0853R.C0854drawable.edit_photo_paint_round_bg_fb_check);
                return;
            case 7:
                paint_color20.setBackgroundResource(C0853R.C0854drawable.edit_photo_paint_round_bg_g25_check);
                return;
            default:
                return;
        }
    }

    private void getGlideImage(int currentItem2) {
        Glide.with(getApplicationContext()).load(this.paths.get(currentItem2)).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new GlideDrawableImageViewTarget(this.act_photo_edit_imageview) {
            public void onResourceReady(GlideDrawable resource, GlideAnimation animation) {
                super.onResourceReady(resource, (GlideAnimation<? super GlideDrawable>) animation);
                PhotoEditSixActivity.this.act_photo_edit_jiazai.setVisibility(8);
            }
        });
    }

    private void initview() {
        this.act_edit_buttom_paint_dismiss.setOnClickListener(this);
        this.act_edit_buttom_paint_ok.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.act_edit_buttom_paint_dismiss:
                finish();
                return;
            case C0853R.C0855id.act_edit_buttom_paint_ok:
                showProgress(getString(C0853R.string.file_picture_generation_being));
                String path = this.photo_view.saveMyBitmap(BitmapFactory.decodeFile(this.photo_edit_path));
                Intent intent = new Intent();
                intent.putExtra(IntentKey.PAINTPATH, path);
                setResult(-1, intent);
                finish();
                return;
            default:
                return;
        }
    }

    public void showProgress(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new LoadingView(this);
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
