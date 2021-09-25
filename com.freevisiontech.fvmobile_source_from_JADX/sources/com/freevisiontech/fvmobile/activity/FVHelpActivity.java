package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.LogUtils;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.LocalResourceManager;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVHelpActivity extends AppCompatActivity implements View.OnClickListener, OnPageChangeListener {
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    FVHelpActivity.this.tvPage.setVisibility(8);
                    return;
                default:
                    return;
            }
        }
    };
    @Bind({2131755249})
    ImageView imgBack;
    @Bind({2131756129})
    ImageView imgRight;
    private String mCurrentPtzType = "";
    @Bind({2131755279})
    PDFView pdfView;
    @Bind({2131756127})
    TextView tvCenterTitle;
    @Bind({2131755280})
    TextView tvPage;
    @Bind({2131756128})
    TextView tvRight;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_fvhelp);
        ButterKnife.bind((Activity) this);
        initTitle();
        initData();
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            CameraUtils.setFrameLayerNumber(27);
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    private void initData() {
        String name;
        if (CameraUtils.getCurrentPageIndex() == 0) {
            if (Util.isZh(this)) {
                if (Util.isZhFanTi(this)) {
                    name = "help_fan.pdf";
                } else {
                    name = "help_zh.pdf";
                }
            } else if (Util.isDe(this)) {
                name = "help_de.pdf";
            } else {
                name = "help_en.pdf";
            }
        } else if (CameraUtils.getCurrentPageIndex() == 2) {
            if (Util.isZh(this)) {
                name = "help_zh210.pdf";
            } else {
                name = "help_en210.pdf";
            }
        } else if (Util.isZh(this)) {
            if (Util.isZhFanTi(this)) {
                name = "help_fan300.pdf";
            } else {
                name = "help_zh300.pdf";
            }
        } else if (Util.isHan(this)) {
            name = "help_han300.pdf";
        } else {
            name = "help_en300.pdf";
        }
        this.pdfView.fromStream(LocalResourceManager.getLocalResourceStream(getApplicationContext(), name)).defaultPage(0).enableSwipe(true).onPageChange(this).load();
    }

    private void initTitle() {
        this.imgBack.setVisibility(0);
        this.imgBack.setOnClickListener(this);
        this.tvCenterTitle.setVisibility(0);
        this.tvCenterTitle.setText(getResources().getString(C0853R.string.help));
        this.tvRight.setVisibility(8);
        this.imgRight.setVisibility(8);
        this.mCurrentPtzType = getIntent().getStringExtra("help_ptz_type");
        if (CameraUtils.getCurrentPageIndex() == 1) {
            this.tvCenterTitle.setText("VILTA-S / SE　" + getResources().getString(C0853R.string.help_title));
        } else if (CameraUtils.getCurrentPageIndex() != 2) {
            this.tvCenterTitle.setText("VILTA-M　" + getResources().getString(C0853R.string.help_title));
        } else if (Util.isZh(this)) {
            this.tvCenterTitle.setText("VILTA-M Pro　" + getResources().getString(C0853R.string.help_title));
        } else {
            this.tvCenterTitle.setText("M Pro　" + getResources().getString(C0853R.string.help_title));
        }
    }

    public void onClick(View v) {
        if (v.getId() == this.imgBack.getId()) {
            finish();
        }
    }

    public void onPageChanged(int page, int pageCount) {
        LogUtils.m1525v("pdf", "page:" + page + "...pageCount:" + pageCount);
        this.tvPage.setText(page + "/" + (pageCount - 1));
        this.tvPage.setVisibility(0);
        if (this.handler != null) {
            this.handler.removeMessages(1);
            this.handler.sendEmptyMessageDelayed(1, 2000);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_SETTING_OK_TOP_BAR_UP_OR_DOWN_210 /*107708*/:
                if (CameraUtils.getFrameLayerNumber() == 27) {
                }
                return;
            case Constants.LABEL_SETTING_RETURN_KEY_210 /*107709*/:
                if (CameraUtils.getFrameLayerNumber() == 27) {
                    finish();
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_UP_210 /*107710*/:
                if (CameraUtils.getFrameLayerNumber() == 27) {
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_DOWN_210 /*107711*/:
                if (CameraUtils.getFrameLayerNumber() == 27) {
                }
                return;
            case Constants.LABEL_SETTING_LONG_RETURN_KEY_210 /*107718*/:
                if (CameraUtils.getFrameLayerNumber() == 27) {
                    finish();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages((Object) null);
        }
        ButterKnife.unbind(this);
        if (CameraUtils.getCurrentPageIndex() == 2) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            CameraUtils.setFrameLayerNumber(22);
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
