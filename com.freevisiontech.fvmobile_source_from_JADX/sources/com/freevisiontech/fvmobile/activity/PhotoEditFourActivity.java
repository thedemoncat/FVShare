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
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.widget.mosaic.MosaicView;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;

public class PhotoEditFourActivity extends Activity implements View.OnClickListener {
    @Bind({2131755463})
    ImageView act_edit_buttom_masaike_dismiss;
    @Bind({2131755447})
    LinearLayout act_edit_buttom_masaike_layout;
    @Bind({2131755464})
    ImageView act_edit_buttom_masaike_ok;
    @Bind({2131755485})
    LinearLayout act_photo_edit_imageview_linear;
    @Bind({2131755384})
    TextView act_photo_edit_jiazai;
    private Activity activity;
    private int currentItem;
    /* access modifiers changed from: private */
    public int height;
    private int height1;
    @Bind({2131755455})
    LinearLayout id_edit_buttom_masaike_linear_mosaic1;
    @Bind({2131755457})
    LinearLayout id_edit_buttom_masaike_linear_mosaic2;
    @Bind({2131755459})
    LinearLayout id_edit_buttom_masaike_linear_mosaic3;
    @Bind({2131755461})
    LinearLayout id_edit_buttom_masaike_linear_mosaic4;
    @Bind({2131755456})
    ImageView id_edit_buttom_masaike_mosaic1;
    @Bind({2131755458})
    ImageView id_edit_buttom_masaike_mosaic2;
    @Bind({2131755460})
    ImageView id_edit_buttom_masaike_mosaic3;
    @Bind({2131755462})
    ImageView id_edit_buttom_masaike_mosaic4;
    private LoadingView mProgressDialog;
    /* access modifiers changed from: private */
    public MosaicView mosaicView;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    PhotoEditFourActivity.this.mosaicView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            PhotoEditFourActivity.this.mosaicView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            PhotoEditFourActivity.this.mosaicView.init(PhotoEditFourActivity.this.width, PhotoEditFourActivity.this.height, PhotoEditFourActivity.this.photo_edit_path);
                        }
                    });
                    RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) PhotoEditFourActivity.this.act_photo_edit_imageview_linear.getLayoutParams();
                    linearParams2.width = PhotoEditFourActivity.this.width;
                    linearParams2.height = PhotoEditFourActivity.this.height;
                    PhotoEditFourActivity.this.act_photo_edit_imageview_linear.setLayoutParams(linearParams2);
                    return;
                default:
                    return;
            }
        }
    };
    private List<String> paths = new ArrayList();
    /* access modifiers changed from: private */
    public String photo_edit_path;
    private List<String> strings;
    /* access modifiers changed from: private */
    public int width;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_photo_edit_four);
        ButterKnife.bind((Activity) this);
        this.activity = this;
        this.act_edit_buttom_masaike_layout.setVisibility(0);
        this.photo_edit_path = getIntent().getStringExtra(IntentKey.PHOTOEDITPATH);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(this.photo_edit_path, options);
        final int drawableWidth = options.outWidth;
        final int drawableHeight = options.outHeight;
        this.mosaicView = (MosaicView) findViewById(C0853R.C0855id.act_photo_edit_mosaic_imageview);
        initview();
        this.mosaicView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PhotoEditFourActivity.this.mosaicView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                PhotoEditFourActivity.this.mosaicView.init(1080, 1080, PhotoEditFourActivity.this.photo_edit_path);
            }
        });
        this.act_photo_edit_imageview_linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PhotoEditFourActivity.this.act_photo_edit_imageview_linear.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int unused = PhotoEditFourActivity.this.width = PhotoEditFourActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                int unused2 = PhotoEditFourActivity.this.height = PhotoEditFourActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                if (drawableWidth > drawableHeight) {
                    int unused3 = PhotoEditFourActivity.this.width = PhotoEditFourActivity.this.act_photo_edit_imageview_linear.getMeasuredWidth();
                    int unused4 = PhotoEditFourActivity.this.height = (PhotoEditFourActivity.this.width * drawableHeight) / drawableWidth;
                    PhotoEditFourActivity.this.sendToHandler(11);
                    return;
                }
                int unused5 = PhotoEditFourActivity.this.width = (PhotoEditFourActivity.this.height * drawableWidth) / drawableHeight;
                int unused6 = PhotoEditFourActivity.this.height = PhotoEditFourActivity.this.act_photo_edit_imageview_linear.getMeasuredHeight();
                PhotoEditFourActivity.this.sendToHandler(11);
            }
        });
    }

    public void sendToHandler(int what) {
        Message me = new Message();
        me.what = what;
        this.myHandler.sendMessage(me);
    }

    private void initview() {
        this.act_edit_buttom_masaike_dismiss.setOnClickListener(this);
        this.id_edit_buttom_masaike_linear_mosaic1.setOnClickListener(this);
        this.id_edit_buttom_masaike_linear_mosaic2.setOnClickListener(this);
        this.id_edit_buttom_masaike_linear_mosaic3.setOnClickListener(this);
        this.id_edit_buttom_masaike_linear_mosaic4.setOnClickListener(this);
        this.act_edit_buttom_masaike_ok.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.id_edit_buttom_masaike_linear_mosaic1:
                showProgress(getString(C0853R.string.file_show_pro_title));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PhotoEditFourActivity.this.mosaicView.mosaicSize(30);
                        PhotoEditFourActivity.this.setAllBackgroundColor();
                        PhotoEditFourActivity.this.id_edit_buttom_masaike_mosaic1.setBackgroundResource(C0853R.C0854drawable.edit_photo_black_round_bg);
                        PhotoEditFourActivity.this.hideProgress();
                    }
                }, 100);
                return;
            case C0853R.C0855id.id_edit_buttom_masaike_linear_mosaic2:
                showProgress(getString(C0853R.string.file_show_pro_title));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PhotoEditFourActivity.this.mosaicView.mosaicSize(50);
                        PhotoEditFourActivity.this.setAllBackgroundColor();
                        PhotoEditFourActivity.this.id_edit_buttom_masaike_mosaic2.setBackgroundResource(C0853R.C0854drawable.edit_photo_black_round_bg);
                        PhotoEditFourActivity.this.hideProgress();
                    }
                }, 100);
                return;
            case C0853R.C0855id.id_edit_buttom_masaike_linear_mosaic3:
                showProgress(getString(C0853R.string.file_show_pro_title));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PhotoEditFourActivity.this.mosaicView.mosaicSize(70);
                        PhotoEditFourActivity.this.setAllBackgroundColor();
                        PhotoEditFourActivity.this.id_edit_buttom_masaike_mosaic3.setBackgroundResource(C0853R.C0854drawable.edit_photo_black_round_bg);
                        PhotoEditFourActivity.this.hideProgress();
                    }
                }, 100);
                return;
            case C0853R.C0855id.id_edit_buttom_masaike_linear_mosaic4:
                showProgress(getString(C0853R.string.file_show_pro_title));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PhotoEditFourActivity.this.mosaicView.mosaicSize(100);
                        PhotoEditFourActivity.this.setAllBackgroundColor();
                        PhotoEditFourActivity.this.id_edit_buttom_masaike_mosaic4.setBackgroundResource(C0853R.C0854drawable.edit_photo_black_round_bg);
                        PhotoEditFourActivity.this.hideProgress();
                    }
                }, 100);
                return;
            case C0853R.C0855id.act_edit_buttom_masaike_dismiss:
                finish();
                return;
            case C0853R.C0855id.act_edit_buttom_masaike_ok:
                showProgress(getString(C0853R.string.label_produce_ing));
                String path = this.mosaicView.setMosaicBitmapPath();
                Intent intent = new Intent();
                intent.putExtra(IntentKey.PAINTPATH, path);
                setResult(-1, intent);
                finish();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void setAllBackgroundColor() {
        this.id_edit_buttom_masaike_mosaic1.setBackgroundResource(C0853R.C0854drawable.edit_photo_gray_round_bg);
        this.id_edit_buttom_masaike_mosaic2.setBackgroundResource(C0853R.C0854drawable.edit_photo_gray_round_bg);
        this.id_edit_buttom_masaike_mosaic3.setBackgroundResource(C0853R.C0854drawable.edit_photo_gray_round_bg);
        this.id_edit_buttom_masaike_mosaic4.setBackgroundResource(C0853R.C0854drawable.edit_photo_gray_round_bg);
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
