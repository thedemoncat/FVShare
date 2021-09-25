package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.bean.MoveTimeLapseBean;
import com.freevisiontech.fvmobile.bean.MoveTimeLapsePaintBean;
import com.freevisiontech.fvmobile.bean.MoveTimeScalePaintBean;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.widget.FVKcfFreeStyleDialog;
import com.freevisiontech.fvmobile.widget.FVMoveTimelapsePop;
import com.freevisiontech.fvmobile.widget.FVTimeLapseFreestylePop;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.freevisiontech.fvmobile.widget.view.KcfPaintView;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVKcfFreestyleActivity extends BaseActivity implements View.OnClickListener {
    float Mbia;
    float Mmax;
    float Mmin;
    @Bind({2131755290})
    LinearLayout act_fv_kcf_free_recycler_linear_scale_xxx;
    @Bind({2131755286})
    LinearLayout act_fv_kcf_free_recycler_linear_scale_yyy;
    @Bind({2131755288})
    LinearLayout act_fv_kcf_free_recycler_linear_xxx;
    @Bind({2131755284})
    LinearLayout act_fv_kcf_free_recycler_linear_yyy;
    @Bind({2131755291})
    RecyclerView act_fv_kcf_free_recycler_scale_xxx;
    @Bind({2131755287})
    RecyclerView act_fv_kcf_free_recycler_scale_yyy;
    @Bind({2131755289})
    RecyclerView act_fv_kcf_free_recycler_xxx;
    @Bind({2131755285})
    RecyclerView act_fv_kcf_free_recycler_yyy;
    /* access modifiers changed from: private */
    public KcfPaintView act_yanshi_paint;
    /* access modifiers changed from: private */
    public Activity activity;
    @Bind({2131755344})
    TextView btnExit;
    /* access modifiers changed from: private */
    public Boolean checkDialog;
    private float dertM1;
    float dertML;
    /* access modifiers changed from: private */
    public List<Integer> device2Phone;
    /* access modifiers changed from: private */
    public FVKcfFreeStyleDialog fVKcfFreeStyleDialog;
    @Bind({2131755296})
    ImageView icon1;
    @Bind({2131755341})
    ImageView icon10;
    @Bind({2131755343})
    ImageView icon10_end;
    @Bind({2131755298})
    ImageView icon1_end;
    @Bind({2131755301})
    ImageView icon2;
    @Bind({2131755303})
    ImageView icon2_end;
    @Bind({2131755306})
    ImageView icon3;
    @Bind({2131755308})
    ImageView icon3_end;
    @Bind({2131755311})
    ImageView icon4;
    @Bind({2131755313})
    ImageView icon4_end;
    @Bind({2131755316})
    ImageView icon5;
    @Bind({2131755318})
    ImageView icon5_end;
    @Bind({2131755321})
    ImageView icon6;
    @Bind({2131755323})
    ImageView icon6_end;
    @Bind({2131755326})
    ImageView icon7;
    @Bind({2131755328})
    ImageView icon7_end;
    @Bind({2131755331})
    ImageView icon8;
    @Bind({2131755333})
    ImageView icon8_end;
    @Bind({2131755336})
    ImageView icon9;
    @Bind({2131755338})
    ImageView icon9_end;
    private boolean isConnected = false;
    private float kXPer = 0.0f;
    private float kYPer = 0.0f;
    /* access modifiers changed from: private */
    public float kx2a;
    /* access modifiers changed from: private */
    public float ky2b;
    @Bind({2131755293})
    RelativeLayout ll_layout1;
    @Bind({2131755339})
    RelativeLayout ll_layout10;
    @Bind({2131755342})
    LinearLayout ll_layout10_end;
    @Bind({2131755340})
    LinearLayout ll_layout10_start;
    @Bind({2131755297})
    LinearLayout ll_layout1_end;
    @Bind({2131755294})
    LinearLayout ll_layout1_start;
    @Bind({2131755299})
    RelativeLayout ll_layout2;
    @Bind({2131755302})
    LinearLayout ll_layout2_end;
    @Bind({2131755300})
    LinearLayout ll_layout2_start;
    @Bind({2131755304})
    RelativeLayout ll_layout3;
    @Bind({2131755307})
    LinearLayout ll_layout3_end;
    @Bind({2131755305})
    LinearLayout ll_layout3_start;
    @Bind({2131755309})
    RelativeLayout ll_layout4;
    @Bind({2131755312})
    LinearLayout ll_layout4_end;
    @Bind({2131755310})
    LinearLayout ll_layout4_start;
    @Bind({2131755314})
    RelativeLayout ll_layout5;
    @Bind({2131755317})
    LinearLayout ll_layout5_end;
    @Bind({2131755315})
    LinearLayout ll_layout5_start;
    @Bind({2131755319})
    RelativeLayout ll_layout6;
    @Bind({2131755322})
    LinearLayout ll_layout6_end;
    @Bind({2131755320})
    LinearLayout ll_layout6_start;
    @Bind({2131755324})
    RelativeLayout ll_layout7;
    @Bind({2131755327})
    LinearLayout ll_layout7_end;
    @Bind({2131755325})
    LinearLayout ll_layout7_start;
    @Bind({2131755329})
    RelativeLayout ll_layout8;
    @Bind({2131755332})
    LinearLayout ll_layout8_end;
    @Bind({2131755330})
    LinearLayout ll_layout8_start;
    @Bind({2131755334})
    RelativeLayout ll_layout9;
    @Bind({2131755337})
    LinearLayout ll_layout9_end;
    @Bind({2131755335})
    LinearLayout ll_layout9_start;
    /* access modifiers changed from: private */
    public int ll_layout_height;
    /* access modifiers changed from: private */
    public int ll_layout_width;
    /* access modifiers changed from: private */
    public List<MoveTimeLapsePaintBean> pathList;
    private List<MoveTimeScalePaintBean> pathScaleList;
    /* access modifiers changed from: private */
    public float phoneHight;
    /* access modifiers changed from: private */
    public float phoneWidth;
    /* access modifiers changed from: private */
    public List<String> photoPaths;
    private PopupWindow pop1;
    private PopupWindow pop2;
    /* access modifiers changed from: private */
    public LoadingView progressDialog;
    @Bind({2131755283})
    TextView tvHint;
    @Bind({2131755295})
    TextView tvNumber;
    private int x0Point = 0;
    private float xMin = 0.0f;
    private float yMin = 0.0f;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.activity_fvkcf_freestyle);
        ButterKnife.bind((Activity) this);
        Util.setFullScreen(this);
        BaseActivityManager.getActivityManager().pushActivity(this);
        this.activity = this;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initStatus();
        Intent intent = getIntent();
        int enterorback = intent.getIntExtra("enterorback", 1);
        this.photoPaths = intent.getStringArrayListExtra(IntentKey.VIDEOS_PATH);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        this.phoneWidth = (float) dm.widthPixels;
        this.phoneHight = (float) dm.heightPixels;
        this.device2Phone = new ArrayList();
        if (CameraUtils.getCurrentPageIndex() == 1) {
            this.device2Phone = transitionPointDevice2PhoneList300();
        } else {
            this.device2Phone = transitionPointDevice2PhoneList();
        }
        if (enterorback == 1) {
        }
        this.act_yanshi_paint = (KcfPaintView) findViewById(C0853R.C0855id.act_yanshi_paint);
        this.act_yanshi_paint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                FVKcfFreestyleActivity.this.act_yanshi_paint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FVKcfFreestyleActivity.this.act_yanshi_paint.setWidthHight(FVKcfFreestyleActivity.this.activity, (int) FVKcfFreestyleActivity.this.phoneWidth, (int) FVKcfFreestyleActivity.this.phoneHight);
            }
        });
        setOnClick();
        if (this.photoPaths.size() == 1) {
            this.ll_layout1.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
        } else if (this.photoPaths.size() == 2) {
            this.ll_layout1.setVisibility(0);
            this.ll_layout2.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
            Glide.with(this.activity).load(this.photoPaths.get(1)).asBitmap().centerCrop().into(this.icon2);
        } else if (this.photoPaths.size() == 3) {
            this.ll_layout1.setVisibility(0);
            this.ll_layout2.setVisibility(0);
            this.ll_layout3.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
            Glide.with(this.activity).load(this.photoPaths.get(1)).asBitmap().centerCrop().into(this.icon2);
            Glide.with(this.activity).load(this.photoPaths.get(2)).asBitmap().centerCrop().into(this.icon3);
        } else if (this.photoPaths.size() == 4) {
            this.ll_layout1.setVisibility(0);
            this.ll_layout2.setVisibility(0);
            this.ll_layout3.setVisibility(0);
            this.ll_layout4.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
            Glide.with(this.activity).load(this.photoPaths.get(1)).asBitmap().centerCrop().into(this.icon2);
            Glide.with(this.activity).load(this.photoPaths.get(2)).asBitmap().centerCrop().into(this.icon3);
            Glide.with(this.activity).load(this.photoPaths.get(3)).asBitmap().centerCrop().into(this.icon4);
        } else if (this.photoPaths.size() == 5) {
            this.ll_layout1.setVisibility(0);
            this.ll_layout2.setVisibility(0);
            this.ll_layout3.setVisibility(0);
            this.ll_layout4.setVisibility(0);
            this.ll_layout5.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
            Glide.with(this.activity).load(this.photoPaths.get(1)).asBitmap().centerCrop().into(this.icon2);
            Glide.with(this.activity).load(this.photoPaths.get(2)).asBitmap().centerCrop().into(this.icon3);
            Glide.with(this.activity).load(this.photoPaths.get(3)).asBitmap().centerCrop().into(this.icon4);
            Glide.with(this.activity).load(this.photoPaths.get(4)).asBitmap().centerCrop().into(this.icon5);
        } else if (this.photoPaths.size() == 6) {
            this.ll_layout1.setVisibility(0);
            this.ll_layout2.setVisibility(0);
            this.ll_layout3.setVisibility(0);
            this.ll_layout4.setVisibility(0);
            this.ll_layout5.setVisibility(0);
            this.ll_layout6.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
            Glide.with(this.activity).load(this.photoPaths.get(1)).asBitmap().centerCrop().into(this.icon2);
            Glide.with(this.activity).load(this.photoPaths.get(2)).asBitmap().centerCrop().into(this.icon3);
            Glide.with(this.activity).load(this.photoPaths.get(3)).asBitmap().centerCrop().into(this.icon4);
            Glide.with(this.activity).load(this.photoPaths.get(4)).asBitmap().centerCrop().into(this.icon5);
            Glide.with(this.activity).load(this.photoPaths.get(5)).asBitmap().centerCrop().into(this.icon6);
        } else if (this.photoPaths.size() == 7) {
            this.ll_layout1.setVisibility(0);
            this.ll_layout2.setVisibility(0);
            this.ll_layout3.setVisibility(0);
            this.ll_layout4.setVisibility(0);
            this.ll_layout5.setVisibility(0);
            this.ll_layout6.setVisibility(0);
            this.ll_layout7.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
            Glide.with(this.activity).load(this.photoPaths.get(1)).asBitmap().centerCrop().into(this.icon2);
            Glide.with(this.activity).load(this.photoPaths.get(2)).asBitmap().centerCrop().into(this.icon3);
            Glide.with(this.activity).load(this.photoPaths.get(3)).asBitmap().centerCrop().into(this.icon4);
            Glide.with(this.activity).load(this.photoPaths.get(4)).asBitmap().centerCrop().into(this.icon5);
            Glide.with(this.activity).load(this.photoPaths.get(5)).asBitmap().centerCrop().into(this.icon6);
            Glide.with(this.activity).load(this.photoPaths.get(6)).asBitmap().centerCrop().into(this.icon7);
        } else if (this.photoPaths.size() == 8) {
            this.ll_layout1.setVisibility(0);
            this.ll_layout2.setVisibility(0);
            this.ll_layout3.setVisibility(0);
            this.ll_layout4.setVisibility(0);
            this.ll_layout5.setVisibility(0);
            this.ll_layout6.setVisibility(0);
            this.ll_layout7.setVisibility(0);
            this.ll_layout8.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
            Glide.with(this.activity).load(this.photoPaths.get(1)).asBitmap().centerCrop().into(this.icon2);
            Glide.with(this.activity).load(this.photoPaths.get(2)).asBitmap().centerCrop().into(this.icon3);
            Glide.with(this.activity).load(this.photoPaths.get(3)).asBitmap().centerCrop().into(this.icon4);
            Glide.with(this.activity).load(this.photoPaths.get(4)).asBitmap().centerCrop().into(this.icon5);
            Glide.with(this.activity).load(this.photoPaths.get(5)).asBitmap().centerCrop().into(this.icon6);
            Glide.with(this.activity).load(this.photoPaths.get(6)).asBitmap().centerCrop().into(this.icon7);
            Glide.with(this.activity).load(this.photoPaths.get(7)).asBitmap().centerCrop().into(this.icon8);
        } else if (this.photoPaths.size() == 9) {
            this.ll_layout1.setVisibility(0);
            this.ll_layout2.setVisibility(0);
            this.ll_layout3.setVisibility(0);
            this.ll_layout4.setVisibility(0);
            this.ll_layout5.setVisibility(0);
            this.ll_layout6.setVisibility(0);
            this.ll_layout7.setVisibility(0);
            this.ll_layout8.setVisibility(0);
            this.ll_layout9.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
            Glide.with(this.activity).load(this.photoPaths.get(1)).asBitmap().centerCrop().into(this.icon2);
            Glide.with(this.activity).load(this.photoPaths.get(2)).asBitmap().centerCrop().into(this.icon3);
            Glide.with(this.activity).load(this.photoPaths.get(3)).asBitmap().centerCrop().into(this.icon4);
            Glide.with(this.activity).load(this.photoPaths.get(4)).asBitmap().centerCrop().into(this.icon5);
            Glide.with(this.activity).load(this.photoPaths.get(5)).asBitmap().centerCrop().into(this.icon6);
            Glide.with(this.activity).load(this.photoPaths.get(6)).asBitmap().centerCrop().into(this.icon7);
            Glide.with(this.activity).load(this.photoPaths.get(7)).asBitmap().centerCrop().into(this.icon8);
            Glide.with(this.activity).load(this.photoPaths.get(8)).asBitmap().centerCrop().into(this.icon9);
        } else if (this.photoPaths.size() == 10) {
            this.ll_layout1.setVisibility(0);
            this.ll_layout2.setVisibility(0);
            this.ll_layout3.setVisibility(0);
            this.ll_layout4.setVisibility(0);
            this.ll_layout5.setVisibility(0);
            this.ll_layout6.setVisibility(0);
            this.ll_layout7.setVisibility(0);
            this.ll_layout8.setVisibility(0);
            this.ll_layout9.setVisibility(0);
            this.ll_layout10.setVisibility(0);
            Glide.with(this.activity).load(this.photoPaths.get(0)).asBitmap().centerCrop().into(this.icon1);
            Glide.with(this.activity).load(this.photoPaths.get(1)).asBitmap().centerCrop().into(this.icon2);
            Glide.with(this.activity).load(this.photoPaths.get(2)).asBitmap().centerCrop().into(this.icon3);
            Glide.with(this.activity).load(this.photoPaths.get(3)).asBitmap().centerCrop().into(this.icon4);
            Glide.with(this.activity).load(this.photoPaths.get(4)).asBitmap().centerCrop().into(this.icon5);
            Glide.with(this.activity).load(this.photoPaths.get(5)).asBitmap().centerCrop().into(this.icon6);
            Glide.with(this.activity).load(this.photoPaths.get(6)).asBitmap().centerCrop().into(this.icon7);
            Glide.with(this.activity).load(this.photoPaths.get(7)).asBitmap().centerCrop().into(this.icon8);
            Glide.with(this.activity).load(this.photoPaths.get(8)).asBitmap().centerCrop().into(this.icon9);
            Glide.with(this.activity).load(this.photoPaths.get(9)).asBitmap().centerCrop().into(this.icon10);
        }
        this.ll_layout1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                FVKcfFreestyleActivity.this.ll_layout1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int unused = FVKcfFreestyleActivity.this.ll_layout_width = FVKcfFreestyleActivity.this.ll_layout1.getMeasuredWidth();
                int unused2 = FVKcfFreestyleActivity.this.ll_layout_height = FVKcfFreestyleActivity.this.ll_layout1.getMeasuredHeight();
            }
        });
        if (this.device2Phone != null && this.device2Phone.size() > 0) {
            if (this.device2Phone.size() > 0 && this.device2Phone.get(0) != null) {
                RelativeLayout.LayoutParams margin1 = (RelativeLayout.LayoutParams) this.ll_layout1.getLayoutParams();
                margin1.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(0).intValue()) - Util.dip2px(this.activity, 5.0f), setWidthHeightBroder((int) this.phoneHight, 102, this.device2Phone.get(1).intValue() - 51), 0, 0);
                this.ll_layout1.setLayoutParams(margin1);
            }
            if (this.device2Phone.size() > 2 && this.device2Phone.get(2) != null) {
                this.ll_layout2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        FVKcfFreestyleActivity.this.ll_layout2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        RelativeLayout.LayoutParams margin2 = (RelativeLayout.LayoutParams) FVKcfFreestyleActivity.this.ll_layout2.getLayoutParams();
                        margin2.setMargins(FVKcfFreestyleActivity.this.setWidthHeightBroder((int) FVKcfFreestyleActivity.this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, ((Integer) FVKcfFreestyleActivity.this.device2Phone.get(2)).intValue()) - Util.dip2px(FVKcfFreestyleActivity.this.activity, 5.0f), FVKcfFreestyleActivity.this.setWidthHeightBroder((int) FVKcfFreestyleActivity.this.phoneHight, 102, ((Integer) FVKcfFreestyleActivity.this.device2Phone.get(3)).intValue() - 51), 0, 0);
                        FVKcfFreestyleActivity.this.ll_layout2.setLayoutParams(margin2);
                        if (!Boolean.valueOf(FVKcfFreestyleActivity.this.setWidthBroderDisplay((int) FVKcfFreestyleActivity.this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, ((Integer) FVKcfFreestyleActivity.this.device2Phone.get(2)).intValue())).booleanValue()) {
                            FVKcfFreestyleActivity.this.ll_layout2_start.setVisibility(8);
                            FVKcfFreestyleActivity.this.ll_layout2_end.setVisibility(0);
                            Glide.with(FVKcfFreestyleActivity.this.activity).load((String) FVKcfFreestyleActivity.this.photoPaths.get(1)).asBitmap().centerCrop().into(FVKcfFreestyleActivity.this.icon2_end);
                        }
                    }
                });
            }
            if (this.device2Phone.size() > 4 && this.device2Phone.get(4) != null) {
                RelativeLayout.LayoutParams margin3 = (RelativeLayout.LayoutParams) this.ll_layout3.getLayoutParams();
                margin3.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(4).intValue()) - Util.dip2px(this.activity, 5.0f), setWidthHeightBroder((int) this.phoneHight, 102, this.device2Phone.get(5).intValue() - 51), 0, 0);
                this.ll_layout3.setLayoutParams(margin3);
                if (!Boolean.valueOf(setWidthBroderDisplay((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(4).intValue())).booleanValue()) {
                    this.ll_layout3_start.setVisibility(8);
                    this.ll_layout3_end.setVisibility(0);
                    Glide.with(this.activity).load(this.photoPaths.get(2)).asBitmap().centerCrop().into(this.icon3_end);
                }
            }
            if (this.device2Phone.size() > 6 && this.device2Phone.get(6) != null) {
                RelativeLayout.LayoutParams margin4 = (RelativeLayout.LayoutParams) this.ll_layout4.getLayoutParams();
                margin4.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(6).intValue()) - Util.dip2px(this.activity, 5.0f), setWidthHeightBroder((int) this.phoneHight, 102, this.device2Phone.get(7).intValue() - 51), 0, 0);
                this.ll_layout4.setLayoutParams(margin4);
                if (!Boolean.valueOf(setWidthBroderDisplay((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(6).intValue())).booleanValue()) {
                    this.ll_layout4_start.setVisibility(8);
                    this.ll_layout4_end.setVisibility(0);
                    Glide.with(this.activity).load(this.photoPaths.get(3)).asBitmap().centerCrop().into(this.icon4_end);
                }
            }
            if (this.device2Phone.size() > 8 && this.device2Phone.get(8) != null) {
                RelativeLayout.LayoutParams margin5 = (RelativeLayout.LayoutParams) this.ll_layout5.getLayoutParams();
                margin5.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(8).intValue()) - Util.dip2px(this.activity, 5.0f), setWidthHeightBroder((int) this.phoneHight, 102, this.device2Phone.get(9).intValue() - 51), 0, 0);
                this.ll_layout5.setLayoutParams(margin5);
                if (!Boolean.valueOf(setWidthBroderDisplay((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(8).intValue())).booleanValue()) {
                    this.ll_layout5_start.setVisibility(8);
                    this.ll_layout5_end.setVisibility(0);
                    Glide.with(this.activity).load(this.photoPaths.get(4)).asBitmap().centerCrop().into(this.icon5_end);
                }
            }
            if (this.device2Phone.size() > 10 && this.device2Phone.get(10) != null) {
                RelativeLayout.LayoutParams margin6 = (RelativeLayout.LayoutParams) this.ll_layout6.getLayoutParams();
                margin6.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(10).intValue()) - Util.dip2px(this.activity, 5.0f), setWidthHeightBroder((int) this.phoneHight, 102, this.device2Phone.get(11).intValue() - 51), 0, 0);
                this.ll_layout6.setLayoutParams(margin6);
                if (!Boolean.valueOf(setWidthBroderDisplay((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(10).intValue())).booleanValue()) {
                    this.ll_layout6_start.setVisibility(8);
                    this.ll_layout6_end.setVisibility(0);
                    Glide.with(this.activity).load(this.photoPaths.get(5)).asBitmap().centerCrop().into(this.icon6_end);
                }
            }
            if (this.device2Phone.size() > 12 && this.device2Phone.get(12) != null) {
                RelativeLayout.LayoutParams margin7 = (RelativeLayout.LayoutParams) this.ll_layout7.getLayoutParams();
                margin7.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(12).intValue()) - Util.dip2px(this.activity, 5.0f), setWidthHeightBroder((int) this.phoneHight, 102, this.device2Phone.get(13).intValue() - 51), 0, 0);
                this.ll_layout7.setLayoutParams(margin7);
                if (!Boolean.valueOf(setWidthBroderDisplay((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(12).intValue())).booleanValue()) {
                    this.ll_layout7_start.setVisibility(8);
                    this.ll_layout7_end.setVisibility(0);
                    Glide.with(this.activity).load(this.photoPaths.get(6)).asBitmap().centerCrop().into(this.icon7_end);
                }
            }
            if (this.device2Phone.size() > 14 && this.device2Phone.get(14) != null) {
                RelativeLayout.LayoutParams margin8 = (RelativeLayout.LayoutParams) this.ll_layout8.getLayoutParams();
                margin8.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(14).intValue()) - Util.dip2px(this.activity, 5.0f), setWidthHeightBroder((int) this.phoneHight, 102, this.device2Phone.get(15).intValue() - 51), 0, 0);
                this.ll_layout8.setLayoutParams(margin8);
                if (!Boolean.valueOf(setWidthBroderDisplay((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(14).intValue())).booleanValue()) {
                    this.ll_layout8_start.setVisibility(8);
                    this.ll_layout8_end.setVisibility(0);
                    Glide.with(this.activity).load(this.photoPaths.get(7)).asBitmap().centerCrop().into(this.icon8_end);
                }
            }
            if (this.device2Phone.size() > 16 && this.device2Phone.get(16) != null) {
                RelativeLayout.LayoutParams margin9 = (RelativeLayout.LayoutParams) this.ll_layout9.getLayoutParams();
                margin9.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(16).intValue()) - Util.dip2px(this.activity, 5.0f), setWidthHeightBroder((int) this.phoneHight, 102, this.device2Phone.get(17).intValue() - 51), 0, 0);
                this.ll_layout9.setLayoutParams(margin9);
                if (!Boolean.valueOf(setWidthBroderDisplay((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(16).intValue())).booleanValue()) {
                    this.ll_layout9_start.setVisibility(8);
                    this.ll_layout9_end.setVisibility(0);
                    Glide.with(this.activity).load(this.photoPaths.get(8)).asBitmap().centerCrop().into(this.icon9_end);
                }
            }
            if (this.device2Phone.size() > 18 && this.device2Phone.get(18) != null) {
                RelativeLayout.LayoutParams margin10 = (RelativeLayout.LayoutParams) this.ll_layout10.getLayoutParams();
                margin10.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(18).intValue()) - Util.dip2px(this.activity, 5.0f), setWidthHeightBroder((int) this.phoneHight, 102, this.device2Phone.get(19).intValue() - 51), 0, 0);
                this.ll_layout10.setLayoutParams(margin10);
                if (!Boolean.valueOf(setWidthBroderDisplay((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(18).intValue())).booleanValue()) {
                    this.ll_layout10_start.setVisibility(8);
                    this.ll_layout10_end.setVisibility(0);
                    Glide.with(this.activity).load(this.photoPaths.get(9)).asBitmap().centerCrop().into(this.icon10_end);
                }
            }
        }
        if (enterorback == 2) {
            List<Integer> pathList2 = MoveTimelapseUtil.getInstance().getPointLinePingMu();
            this.act_yanshi_paint.touch_anew_start(Float.valueOf(pathList2.get(0).toString()).floatValue(), Float.valueOf(pathList2.get(1).toString()).floatValue());
            for (int i = 0; i < pathList2.size(); i++) {
                if (i % 2 == 0) {
                    this.act_yanshi_paint.touch_anew_move(Float.valueOf(pathList2.get(i).toString()).floatValue(), Float.valueOf(pathList2.get(i + 1).toString()).floatValue());
                }
            }
        }
        if (this.device2Phone != null && this.device2Phone.size() > 0) {
            setDividingRuleRecyclerYYY();
            setDividingRuleRecyclerXXX();
            setDividingRuleRecyclerScaleXXX();
            setDividingRuleRecyclerScaleYYY();
        }
        MoveTimelapseUtil.setMoveVideoAndMoveTimeVideo(1);
        SPUtils.put(this, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, 1);
    }

    private void setDividingRuleRecyclerScaleYYY() {
        final int yuanDianTop = (int) (this.phoneHight / 2.0f);
        RelativeLayout.LayoutParams margin1 = (RelativeLayout.LayoutParams) this.act_fv_kcf_free_recycler_linear_scale_yyy.getLayoutParams();
        margin1.setMargins((setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(0).intValue()) - Util.dip2px(this.activity, 30.0f)) - Util.dip2px(this.activity, 6.0f), 0, 0, 0);
        this.act_fv_kcf_free_recycler_linear_scale_yyy.setLayoutParams(margin1);
        List<String> test = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            test.add("测试" + i);
        }
        this.act_fv_kcf_free_recycler_scale_yyy.setLayoutManager(new LinearLayoutManager(this.activity, 1, false));
        this.act_fv_kcf_free_recycler_scale_yyy.setAdapter(new BaseRVAdapter(this.activity, test) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.activity_fvkcf_freestyle_recycler_scale_yyy;
            }

            public void onBind(BaseViewHolder holder, int position) {
                RelativeLayout act_fv_kcf_free_recycler_linear_xxx_scale = (RelativeLayout) holder.getView(C0853R.C0855id.act_fv_kcf_free_recycler_linear_xxx_scale);
                TextView act_fv_kcf_free_recycler_text_xxx_scale = (TextView) holder.getView(C0853R.C0855id.act_fv_kcf_free_recycler_text_xxx_scale);
                if (position == 0) {
                    int aaa = yuanDianTop % Util.dip2px(FVKcfFreestyleActivity.this.activity, 130.0f);
                    LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) act_fv_kcf_free_recycler_linear_xxx_scale.getLayoutParams();
                    linearParams2.height = Util.dip2px(FVKcfFreestyleActivity.this.activity, 10.0f) + aaa;
                    act_fv_kcf_free_recycler_linear_xxx_scale.setLayoutParams(linearParams2);
                }
                act_fv_kcf_free_recycler_text_xxx_scale.setText(((int) (((float) (yuanDianTop - ((yuanDianTop % Util.dip2px(FVKcfFreestyleActivity.this.activity, 130.0f)) + (Util.dip2px(FVKcfFreestyleActivity.this.activity, 130.0f) * position)))) * Math.abs(FVKcfFreestyleActivity.this.ky2b))) + "°");
            }
        });
    }

    private void setDividingRuleRecyclerScaleXXX() {
        final int yuanDianLeft = setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(0).intValue());
        RelativeLayout.LayoutParams margin1 = (RelativeLayout.LayoutParams) this.act_fv_kcf_free_recycler_linear_scale_xxx.getLayoutParams();
        margin1.setMargins(0, ((int) (this.phoneHight / 2.0f)) + Util.dip2px(this.activity, 6.0f), 0, 0);
        this.act_fv_kcf_free_recycler_linear_scale_xxx.setLayoutParams(margin1);
        List<String> test = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            test.add("测试" + i);
        }
        this.act_fv_kcf_free_recycler_scale_xxx.setLayoutManager(new LinearLayoutManager(this.activity, 0, false));
        this.act_fv_kcf_free_recycler_scale_xxx.setAdapter(new BaseRVAdapter(this.activity, test) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.activity_fvkcf_freestyle_recycler_scale_xxx;
            }

            public void onBind(BaseViewHolder holder, int position) {
                RelativeLayout act_fv_kcf_free_recycler_linear_xxx_scale = (RelativeLayout) holder.getView(C0853R.C0855id.act_fv_kcf_free_recycler_linear_xxx_scale);
                TextView act_fv_kcf_free_recycler_text_xxx_scale = (TextView) holder.getView(C0853R.C0855id.act_fv_kcf_free_recycler_text_xxx_scale);
                if (position == 0) {
                    int aaa = yuanDianLeft % Util.dip2px(FVKcfFreestyleActivity.this.activity, 130.0f);
                    LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) act_fv_kcf_free_recycler_linear_xxx_scale.getLayoutParams();
                    linearParams2.width = Util.dip2px(FVKcfFreestyleActivity.this.activity, 10.0f) + aaa;
                    act_fv_kcf_free_recycler_linear_xxx_scale.setLayoutParams(linearParams2);
                }
                act_fv_kcf_free_recycler_text_xxx_scale.setText(((int) (((float) (((yuanDianLeft % Util.dip2px(FVKcfFreestyleActivity.this.activity, 130.0f)) + (Util.dip2px(FVKcfFreestyleActivity.this.activity, 130.0f) * position)) - yuanDianLeft)) * Math.abs(FVKcfFreestyleActivity.this.kx2a))) + "°");
            }
        });
    }

    private void setDividingRuleRecyclerYYY() {
        RelativeLayout.LayoutParams margin1 = (RelativeLayout.LayoutParams) this.act_fv_kcf_free_recycler_linear_yyy.getLayoutParams();
        margin1.setMargins(setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(0).intValue()), 0, 0, 0);
        this.act_fv_kcf_free_recycler_linear_yyy.setLayoutParams(margin1);
        final int aaa = ((int) (this.phoneHight / 2.0f)) % Util.dip2px(this.activity, 26.0f);
        List<String> test = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            test.add("测试" + i);
        }
        this.act_fv_kcf_free_recycler_yyy.setLayoutManager(new LinearLayoutManager(this.activity, 1, false));
        this.act_fv_kcf_free_recycler_yyy.setAdapter(new BaseRVAdapter(this.activity, test) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.activity_fvkcf_freestyle_recycler_yyy;
            }

            public void onBind(BaseViewHolder holder, int position) {
                LinearLayout act_fv_kcf_free_recycler_linear_yyy_scale = (LinearLayout) holder.getView(C0853R.C0855id.act_fv_kcf_free_recycler_linear_yyy_scale);
                TextView textView = (TextView) holder.getView(C0853R.C0855id.act_fv_kcf_free_recycler_text_yyy_scale);
                if (position == 0) {
                    LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) act_fv_kcf_free_recycler_linear_yyy_scale.getLayoutParams();
                    linearParams2.height = aaa;
                    act_fv_kcf_free_recycler_linear_yyy_scale.setLayoutParams(linearParams2);
                }
            }
        });
    }

    private void setDividingRuleRecyclerXXX() {
        final int yuanDianLeft = setWidthHeightBroder((int) this.phoneWidth, CompanyIdentifierResolver.SALUTICA_ALLIED_SOLUTIONS, this.device2Phone.get(0).intValue());
        RelativeLayout.LayoutParams margin1 = (RelativeLayout.LayoutParams) this.act_fv_kcf_free_recycler_linear_xxx.getLayoutParams();
        margin1.setMargins(0, ((int) (this.phoneHight / 2.0f)) - Util.dip2px(this.activity, 6.0f), 0, 0);
        this.act_fv_kcf_free_recycler_linear_xxx.setLayoutParams(margin1);
        List<String> test = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            test.add("测试" + i);
        }
        this.act_fv_kcf_free_recycler_xxx.setLayoutManager(new LinearLayoutManager(this.activity, 0, false));
        this.act_fv_kcf_free_recycler_xxx.setAdapter(new BaseRVAdapter(this.activity, test) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.activity_fvkcf_freestyle_recycler_xxx;
            }

            public void onBind(BaseViewHolder holder, int position) {
                LinearLayout act_fv_kcf_free_recycler_linear_xxx_scale = (LinearLayout) holder.getView(C0853R.C0855id.act_fv_kcf_free_recycler_linear_xxx_scale);
                TextView act_fv_kcf_free_recycler_text_xxx_scale = (TextView) holder.getView(C0853R.C0855id.act_fv_kcf_free_recycler_text_xxx_scale);
                if (position == 0) {
                    act_fv_kcf_free_recycler_text_xxx_scale.setVisibility(8);
                    LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) act_fv_kcf_free_recycler_linear_xxx_scale.getLayoutParams();
                    linearParams2.width = yuanDianLeft % Util.dip2px(FVKcfFreestyleActivity.this.activity, 26.0f);
                    act_fv_kcf_free_recycler_linear_xxx_scale.setLayoutParams(linearParams2);
                    return;
                }
                act_fv_kcf_free_recycler_text_xxx_scale.setVisibility(0);
            }
        });
    }

    /* access modifiers changed from: private */
    public int setWidthHeightBroder(int ping, int viewLength, int shuRu) {
        if (shuRu + viewLength > ping) {
            int newShrRu = ping - viewLength;
            if (newShrRu < 0) {
                newShrRu = 0;
            }
            return newShrRu;
        }
        int newShrRu2 = shuRu;
        if (newShrRu2 < 0) {
            newShrRu2 = 0;
        }
        return newShrRu2;
    }

    /* access modifiers changed from: private */
    public boolean setWidthBroderDisplay(int ping, int viewLength, int shuRu) {
        if (shuRu + viewLength > ping) {
            return false;
        }
        return true;
    }

    private void initStatus() {
        int gridingMode = ((Integer) SPUtils.get(this.activity, SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_NONE))).intValue();
        FVModeSelectEvent fvModeSelectEvent = new FVModeSelectEvent();
        fvModeSelectEvent.setMode(gridingMode);
        onModeSwitch(fvModeSelectEvent);
    }

    private void setOnClick() {
        this.btnExit.setOnClickListener(this);
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        this.progressDialog = new LoadingView(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_Exit:
                showMoveTimeLapsePop();
                CameraUtils.setBosIsResume(true);
                finish();
                return;
            default:
                return;
        }
    }

    public void hideUI() {
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions = 1798 | 4096;
                } else {
                    uiOptions = 1798 | 1;
                }
                FVKcfFreestyleActivity.this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.MOVE_TIME_LAPSE_TAKE_PHOTO_PAINT /*106304*/:
                this.pathList = fvModeSelectEvent.getList();
                if (this.fVKcfFreeStyleDialog != null) {
                    this.fVKcfFreeStyleDialog.finish();
                }
                this.checkDialog = true;
                this.fVKcfFreeStyleDialog = new FVKcfFreeStyleDialog(this);
                this.fVKcfFreeStyleDialog.show();
                this.fVKcfFreeStyleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        Util.hideBottomUIMenu(FVKcfFreestyleActivity.this.activity);
                    }
                });
                this.fVKcfFreeStyleDialog.setButtonOnClick(new FVKcfFreeStyleDialog.CheckButtonOnclick() {
                    public void onClick(View view) {
                        FVKcfFreestyleActivity.this.act_yanshi_paint.ok_start();
                        FVKcfFreestyleActivity.this.fVKcfFreeStyleDialog.finish();
                        FVKcfFreestyleActivity.this.act_yanshi_paint.clear();
                    }
                });
                this.fVKcfFreeStyleDialog.setButtonSureOnClick(new FVKcfFreeStyleDialog.CheckButtonSureOnclick() {
                    public void onClick(View view) {
                        if (FVKcfFreestyleActivity.this.checkDialog.booleanValue()) {
                            Boolean unused = FVKcfFreestyleActivity.this.checkDialog = false;
                            FVKcfFreestyleActivity.this.act_yanshi_paint.ok_start();
                            FVKcfFreestyleActivity.this.progressDialog.show();
                            FVKcfFreestyleActivity.this.progressDialog.setMessage(FVKcfFreestyleActivity.this.getString(C0853R.string.label_please_wait));
                            MoveTimelapseUtil.getInstance().setPointLine(FVKcfFreestyleActivity.this.transitionPointPhone2DeviceList(FVKcfFreestyleActivity.this.pathList));
                            MoveTimelapseUtil.getInstance().setPointLinePingMu(FVKcfFreestyleActivity.this.transitionPointPhone2DeviceListTwo(FVKcfFreestyleActivity.this.pathList));
                            FVKcfFreestyleActivity.this.progressDialog.dismiss();
                            FVKcfFreestyleActivity.this.showTimeLapsePop();
                            FVKcfFreestyleActivity.this.finish();
                        }
                    }
                });
                return;
            case Constants.MOVE_TIME_SCALE_TAKE_PHOTO_PAINT /*106305*/:
                this.pathScaleList = fvModeSelectEvent.getList();
                return;
            case Constants.FPV_SEND_PHOTO_OR_VIDEO_DISMISS_POP /*106980*/:
                Log.e("--------------", "-------- 退出移动摄影, 返回上一级弹框 自由模式 activity -----");
                Util.sendIntEventMessge(10008);
                CameraUtils.setMoveTimelapseIng(false);
                SPUtils.put(this, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                finish();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void showTimeLapsePop() {
        Util.sendIntEventMessge(10009);
        int height = Util.getDeviceSize(this).y - Util.dip2px(this, 10.0f);
        FVTimeLapseFreestylePop timeLapsePop = new FVTimeLapseFreestylePop();
        timeLapsePop.init(MoveTimelapseUtil.getInstance().getContext(), MoveTimelapseUtil.getInstance().getParentView());
        this.pop1 = new PopupWindow(timeLapsePop.getView(), height, height);
        this.pop1.setBackgroundDrawable(new ColorDrawable(0));
        this.pop1.setAnimationStyle(C0853R.style.popAnimation2);
        this.pop1.setOutsideTouchable(false);
        timeLapsePop.setPop(this.pop1, timeLapsePop);
        int right = MoveTimelapseUtil.getInstance().getParentView().getRight() + Util.dip2px(this, 2.0f);
        this.pop1.showAtLocation(MoveTimelapseUtil.getInstance().getParentView().findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
        MoveTimelapseUtil.setMoveVideoAndMoveTimeVideo(1);
    }

    private void showMoveTimeLapsePop() {
        Util.sendIntEventMessge(10009);
        int height = Util.getDeviceSize(this).y - Util.dip2px(this, 10.0f);
        FVMoveTimelapsePop moveTimeLapsePop = new FVMoveTimelapsePop();
        moveTimeLapsePop.init(MoveTimelapseUtil.getInstance().getContext(), MoveTimelapseUtil.getInstance().getParentView(), true);
        this.pop2 = new PopupWindow(moveTimeLapsePop.getView(), height, height);
        this.pop2.setBackgroundDrawable(new ColorDrawable(0));
        this.pop2.setAnimationStyle(C0853R.style.popAnimation2);
        this.pop2.setOutsideTouchable(false);
        moveTimeLapsePop.setPop(this.pop2, moveTimeLapsePop);
        int right = MoveTimelapseUtil.getInstance().getParentView().getRight() + Util.dip2px(this, 2.0f);
        this.pop2.showAtLocation(MoveTimelapseUtil.getInstance().getParentView().findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
        MoveTimelapseUtil.setMoveVideoAndMoveTimeVideo(1);
    }

    /* access modifiers changed from: protected */
    public boolean isRegisterEventBus() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 34:
                this.isConnected = false;
                ViseLog.m1466e("发送点数据失败,设备已断开");
                MoveTimelapseUtil.getInstance().detroy();
                Toast.makeText(this, getResources().getString(C0853R.string.lable_bluetooth_is_disconnected), 0).show();
                finish();
                return;
            default:
                return;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        BaseActivityManager.getActivityManager().popActivityOne(FVKcfFreestyleActivity.class);
    }

    public void onBackPressed() {
        showMoveTimeLapsePop();
        super.onBackPressed();
    }

    public List<Integer> transitionPointDevice2PhoneList300() {
        float dertN;
        float dertMR;
        float dertMR2;
        ViseLog.m1466e("设备坐标向手机坐标转化:" + this.phoneWidth + "===" + this.phoneHight);
        ArrayList<MoveTimeLapseBean> moveTimeLapseList = MoveTimelapseUtil.getInstance().getMoveTimeLapseList();
        if (moveTimeLapseList == null || moveTimeLapseList.size() <= 0) {
            return null;
        }
        this.x0Point = moveTimeLapseList.get(0).getXPoint().intValue();
        ArrayList<MoveTimeLapseBean> mapValuesList_X = new ArrayList<>();
        ArrayList<MoveTimeLapseBean> mapValuesList_Y = new ArrayList<>();
        ArrayList<MoveTimeLapseBean> mapValuesList_Z = new ArrayList<>();
        mapValuesList_X.addAll(moveTimeLapseList);
        mapValuesList_Y.addAll(moveTimeLapseList);
        mapValuesList_Z.addAll(moveTimeLapseList);
        for (int i = 0; i < mapValuesList_X.size(); i++) {
            ViseLog.m1466e("x1排序前:" + mapValuesList_X.get(i).getXPoint() + "=========y排序前:" + mapValuesList_X.get(i).getYPoint() + "=========z排序前:" + mapValuesList_Z.get(i).getZPoint());
        }
        Collections.sort(mapValuesList_X, new Comparator<MoveTimeLapseBean>() {
            public int compare(MoveTimeLapseBean moveTimeLapseBean, MoveTimeLapseBean moveTimeLapseBean1) {
                return moveTimeLapseBean.getXPoint().compareTo(moveTimeLapseBean1.getXPoint());
            }
        });
        for (int i2 = 0; i2 < mapValuesList_X.size(); i2++) {
            ViseLog.m1466e("x1排序后:" + mapValuesList_X.get(i2).getXPoint() + "=========y排序后:" + mapValuesList_X.get(i2).getYPoint() + "=========z排序后:" + mapValuesList_Z.get(i2).getZPoint());
        }
        this.xMin = (float) mapValuesList_X.get(0).getXPoint().intValue();
        float xMax = (float) mapValuesList_X.get(mapValuesList_X.size() - 1).getXPoint().intValue();
        ViseLog.m1466e("xMin:" + this.xMin + "===xMax:" + xMax);
        float kX = 0.0f;
        int M0 = moveTimeLapseList.get(0).getXPoint().intValue();
        int intValue = moveTimeLapseList.get(0).getYPoint().intValue();
        int Q0 = moveTimeLapseList.get(0).getZPoint().intValue();
        int Mend = moveTimeLapseList.get(moveTimeLapseList.size() - 1).getXPoint().intValue();
        int Qend = moveTimeLapseList.get(moveTimeLapseList.size() - 1).getZPoint().intValue();
        try {
            if (mapValuesList_X.size() == 1) {
                float ML = (((float) M0) + (((float) 135) * 45.0f)) - ((float) Q0);
                float MR = (((float) M0) + (((float) -135) * 45.0f)) - ((float) Q0);
                this.Mbia = 0.0f;
                if (ML - xMax < 45.0f * 45.0f) {
                    this.dertML = ML - xMax;
                } else {
                    this.dertML = 45.0f * 45.0f;
                }
                if (this.xMin - MR < 45.0f * 45.0f) {
                    dertMR2 = this.xMin - MR;
                } else {
                    dertMR2 = 45.0f * 45.0f;
                }
                kX = (-1.0f * this.phoneWidth) / (((xMax - this.xMin) + this.dertML) + dertMR2);
            } else {
                float ML2 = (((float) M0) + (((float) 135) * 45.0f)) - ((float) Q0);
                float MR2 = (((float) M0) + (((float) -135) * 45.0f)) - ((float) Q0);
                this.Mbia = (float) ((Mend - Qend) - (M0 - Q0));
                if (xMax < ML2) {
                    this.Mmax = xMax;
                } else {
                    this.Mmax = ML2;
                }
                if (this.xMin > MR2) {
                    this.Mmin = this.xMin;
                } else {
                    this.Mmin = MR2;
                }
                if (ML2 - this.Mmax < 15.0f * 45.0f) {
                    this.dertML = ML2 - this.Mmax;
                } else {
                    this.dertML = 15.0f * 45.0f;
                }
                if (this.Mmin - MR2 < 15.0f * 45.0f) {
                    dertMR = this.Mmin - MR2;
                } else {
                    dertMR = 15.0f * 45.0f;
                }
                kX = (-1.0f * this.phoneWidth) / (((xMax - this.xMin) + this.dertML) + dertMR);
            }
            this.kXPer = 1.0f / kX;
            CameraUtils.setkXPerScale(this.kXPer);
        } catch (Exception e) {
            Log.e("Device2PhoneList", ": " + e.getMessage());
        }
        ViseLog.m1466e("kX:" + kX + "===kXPer:" + this.kXPer);
        Collections.sort(mapValuesList_Y, new Comparator<MoveTimeLapseBean>() {
            public int compare(MoveTimeLapseBean moveTimeLapseBean, MoveTimeLapseBean moveTimeLapseBean1) {
                return moveTimeLapseBean.getYPoint().compareTo(moveTimeLapseBean1.getYPoint());
            }
        });
        for (int i3 = 0; i3 < mapValuesList_Y.size(); i3++) {
            ViseLog.m1466e("x2排序后:" + mapValuesList_Y.get(i3).getXPoint() + "=========y排序后:" + mapValuesList_Y.get(i3).getYPoint());
        }
        this.yMin = (float) mapValuesList_Y.get(0).getYPoint().intValue();
        ViseLog.m1466e("yMin:" + this.yMin + "===yMax:" + ((float) mapValuesList_Y.get(mapValuesList_Y.size() - 1).getYPoint().intValue()));
        float kY = 0.0f;
        try {
            ArrayList arrayList = new ArrayList();
            for (int i4 = 0; i4 < mapValuesList_Y.size(); i4++) {
                arrayList.add(Float.valueOf(Math.abs((float) mapValuesList_Y.get(i4).getYPoint().intValue())));
            }
            Collections.sort(arrayList);
            float YMmax = ((Float) arrayList.get(arrayList.size() - 1)).floatValue();
            if (YMmax <= ((float) 75) * 257.0f) {
                dertN = 30.0f * 257.0f;
            } else {
                dertN = 2.0f * ((((float) 90) * 257.0f) - YMmax);
            }
            kY = (-1.0f * this.phoneHight) / ((2.0f * YMmax) + dertN);
            this.kYPer = 1.0f / kY;
            CameraUtils.setkYPerScale(this.kYPer);
        } catch (Exception e2) {
            Log.e("Device2PhoneList", ": " + e2.getMessage());
        }
        ViseLog.m1466e("kY:" + kY + "===kYPer:" + this.kYPer);
        ArrayList arrayList2 = new ArrayList();
        for (int p = 0; p < moveTimeLapseList.size(); p++) {
            MoveTimeLapseBean moveTimeLapseBean = moveTimeLapseList.get(p);
            int xPhone = (int) (((((((float) moveTimeLapseBean.getXPoint().intValue()) - this.Mmin) + this.dertML) - this.Mbia) * kX) + this.phoneWidth);
            arrayList2.add(Integer.valueOf(xPhone));
            int yPhone = (int) ((((float) (moveTimeLapseBean.getYPoint().intValue() + 0)) * kY) + (this.phoneHight / 2.0f));
            arrayList2.add(Integer.valueOf(yPhone));
            ViseLog.m1466e("手机x" + xPhone + "手机y" + yPhone);
        }
        return arrayList2;
    }

    public List<Integer> transitionPointDevice2PhoneList() {
        ViseLog.m1466e("设备坐标向手机坐标转化:" + this.phoneWidth + "===" + this.phoneHight);
        ArrayList<MoveTimeLapseBean> moveTimeLapseList = MoveTimelapseUtil.getInstance().getMoveTimeLapseList();
        if (moveTimeLapseList == null || moveTimeLapseList.size() <= 0) {
            return null;
        }
        this.x0Point = moveTimeLapseList.get(0).getXPoint().intValue();
        ArrayList<MoveTimeLapseBean> mapValuesList_X = new ArrayList<>();
        ArrayList<MoveTimeLapseBean> mapValuesList_Y = new ArrayList<>();
        mapValuesList_X.addAll(moveTimeLapseList);
        mapValuesList_Y.addAll(moveTimeLapseList);
        for (int i = 0; i < mapValuesList_X.size(); i++) {
            ViseLog.m1466e("x1排序前:" + mapValuesList_X.get(i).getXPoint() + "=========y排序前:" + mapValuesList_X.get(i).getYPoint());
        }
        Collections.sort(mapValuesList_X, new Comparator<MoveTimeLapseBean>() {
            public int compare(MoveTimeLapseBean moveTimeLapseBean, MoveTimeLapseBean moveTimeLapseBean1) {
                return moveTimeLapseBean.getXPoint().compareTo(moveTimeLapseBean1.getXPoint());
            }
        });
        for (int i2 = 0; i2 < mapValuesList_X.size(); i2++) {
            ViseLog.m1466e("x1排序后:" + mapValuesList_X.get(i2).getXPoint() + "=========y排序后:" + mapValuesList_X.get(i2).getYPoint());
        }
        this.xMin = (float) mapValuesList_X.get(0).getXPoint().intValue();
        float xMax = (float) mapValuesList_X.get(mapValuesList_X.size() - 1).getXPoint().intValue();
        ViseLog.m1466e("xMin:" + this.xMin + "===xMax:" + xMax);
        float kX = 0.0f;
        try {
            if (mapValuesList_X.size() == 1) {
                this.dertM1 = 4096.0f;
                kX = (-1.0f * this.phoneWidth) / ((xMax - this.xMin) + this.dertM1);
            } else {
                if (xMax - this.xMin <= 62805.0f) {
                    this.dertM1 = 2730.0f;
                } else {
                    this.dertM1 = 65536.0f - (xMax - this.xMin);
                }
                kX = (-1.0f * this.phoneWidth) / ((xMax - this.xMin) + this.dertM1);
            }
            this.kXPer = 1.0f / kX;
            CameraUtils.setkXPerScale(this.kXPer);
        } catch (Exception e) {
            Log.e("Device2PhoneList", ": " + e.getMessage());
        }
        ViseLog.m1466e("kX:" + kX + "===kXPer:" + this.kXPer);
        Collections.sort(mapValuesList_Y, new Comparator<MoveTimeLapseBean>() {
            public int compare(MoveTimeLapseBean moveTimeLapseBean, MoveTimeLapseBean moveTimeLapseBean1) {
                return moveTimeLapseBean.getYPoint().compareTo(moveTimeLapseBean1.getYPoint());
            }
        });
        for (int i3 = 0; i3 < mapValuesList_Y.size(); i3++) {
            ViseLog.m1466e("x2排序后:" + mapValuesList_Y.get(i3).getXPoint() + "=========y排序后:" + mapValuesList_Y.get(i3).getYPoint());
        }
        this.yMin = (float) mapValuesList_Y.get(0).getYPoint().intValue();
        ViseLog.m1466e("yMin:" + this.yMin + "===yMax:" + ((float) mapValuesList_Y.get(mapValuesList_Y.size() - 1).getYPoint().intValue()));
        float kY = 0.0f;
        try {
            List<Float> listYNew = new ArrayList<>();
            for (int i4 = 0; i4 < mapValuesList_Y.size(); i4++) {
                listYNew.add(Float.valueOf(Math.abs((float) mapValuesList_Y.get(i4).getYPoint().intValue())));
            }
            Collections.sort(listYNew);
            float YMmax = listYNew.get(listYNew.size() - 1).floatValue();
            if (YMmax <= 22282.0f) {
                kY = (-1.0f * this.phoneHight) / ((2.0f * YMmax) + 7864.0f);
            } else {
                kY = (-1.0f * this.phoneHight) / ((2.0f * YMmax) + (2.0f * (26215.0f - YMmax)));
            }
            this.kYPer = 1.0f / kY;
            CameraUtils.setkYPerScale(this.kYPer);
        } catch (Exception e2) {
            Log.e("Device2PhoneList", ": " + e2.getMessage());
        }
        ViseLog.m1466e("kY:" + kY + "===kYPer:" + this.kYPer);
        List<Integer> list = new ArrayList<>();
        for (int p = 0; p < moveTimeLapseList.size(); p++) {
            MoveTimeLapseBean moveTimeLapseBean = moveTimeLapseList.get(p);
            int xPhone = (int) ((((((float) moveTimeLapseBean.getXPoint().intValue()) - this.xMin) + (this.dertM1 / 2.0f)) * kX) + this.phoneWidth);
            list.add(Integer.valueOf(xPhone));
            int yPhone = (int) ((((float) (moveTimeLapseBean.getYPoint().intValue() + 0)) * kY) + (this.phoneHight / 2.0f));
            list.add(Integer.valueOf(yPhone));
            ViseLog.m1466e("手机x" + xPhone + "手机y" + yPhone);
        }
        return list;
    }

    public List<Integer> transitionPointPhone2DeviceList(List<MoveTimeLapsePaintBean> list) {
        float xPhonePoint;
        float f;
        ViseLog.m1466e("手机坐标向设备坐标转化1===" + list.size());
        if (list == null || list.size() <= 0) {
            return null;
        }
        List<Integer> mList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MoveTimeLapsePaintBean paintBean = list.get(i);
            if (CameraUtils.getCurrentPageIndex() == 1) {
                xPhonePoint = ((((paintBean.getXPhonePoint() - this.phoneWidth) * this.kXPer) + this.Mmin) - this.dertML) + this.Mbia;
                f = (float) this.x0Point;
            } else {
                xPhonePoint = (((paintBean.getXPhonePoint() - this.phoneWidth) * this.kXPer) + this.xMin) - ((float) this.x0Point);
                f = this.dertM1 / 2.0f;
            }
            int xDevice = (int) (xPhonePoint - f);
            mList.add(Integer.valueOf(xDevice));
            int yDevice = (int) ((paintBean.getYPhonePoint() - (this.phoneHight / 2.0f)) * this.kYPer);
            mList.add(Integer.valueOf(yDevice));
            ViseLog.m1466e("x轴" + xDevice + "y轴" + yDevice);
        }
        ViseLog.m1466e("手机坐标向设备坐标转化2===" + mList.size());
        return mList;
    }

    public List<Integer> transitionPointPhone2DeviceListTwo(List<MoveTimeLapsePaintBean> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        List<Integer> mList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MoveTimeLapsePaintBean paintBean = list.get(i);
            mList.add(Integer.valueOf((int) paintBean.getXPhonePoint()));
            mList.add(Integer.valueOf((int) paintBean.getYPhonePoint()));
        }
        return mList;
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
