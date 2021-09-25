package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;

public class FVCalibrationActivity4 extends BaseActivity {
    private static final int OVER_CALIBRATION_TIMEOUT = 1001;
    @Bind({2131755249})
    ImageView btnBack;
    @Bind({2131755268})
    Button btnNext;
    @Bind({2131756127})
    TextView centerTitle;
    @Bind({2131755269})
    ImageView iconGuide;
    @Bind({2131755273})
    ImageView iconGuide2;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    ViseLog.m1466e("退出校准通讯超时");
                    FVCalibrationActivity4.access$008(FVCalibrationActivity4.this);
                    if (FVCalibrationActivity4.this.startFailCount >= 3) {
                        int unused = FVCalibrationActivity4.this.startFailCount = 0;
                        FVCalibrationActivity4.this.finish();
                        return;
                    } else if (FVCalibrationActivity4.this.isConnected) {
                        FVCalibrationActivity4.this.sendpTZMessage();
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    @Bind({2131755275})
    RelativeLayout rlMsg2;
    /* access modifiers changed from: private */
    public int startFailCount = 0;
    @Bind({2131755271})
    TextView tvDescription;
    @Bind({2131755274})
    TextView tvDescription2;

    static /* synthetic */ int access$008(FVCalibrationActivity4 x0) {
        int i = x0.startFailCount;
        x0.startFailCount = i + 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_fvcalibration4);
        ButterKnife.bind((Activity) this);
        initView();
        initTitleAndListener();
    }

    private void initView() {
        if (getIntent().getBooleanExtra("success", false)) {
            this.iconGuide.setVisibility(0);
            this.tvDescription.setVisibility(0);
            this.btnNext.setText(C0853R.string.label_ptz_calibration_over);
            this.iconGuide2.setVisibility(4);
            this.tvDescription2.setVisibility(4);
            this.rlMsg2.setVisibility(4);
            return;
        }
        this.iconGuide2.setVisibility(0);
        this.tvDescription2.setVisibility(0);
        this.rlMsg2.setVisibility(0);
        this.btnNext.setText(C0853R.string.label_ptz_calibration_exit);
        this.iconGuide.setVisibility(4);
        this.tvDescription.setVisibility(4);
    }

    private void initTitleAndListener() {
        this.btnBack.setVisibility(0);
        this.centerTitle.setVisibility(0);
        this.centerTitle.setText(C0853R.string.label_ptz_calibration);
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCalibrationActivity4.this.ptzConnectedSendMsgOrClose();
            }
        });
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCalibrationActivity4.this.ptzConnectedSendMsgOrClose();
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendpTZMessage() {
        BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.TAB_OFFSET_CHAN_1, (byte) 6);
        this.mHandler.sendEmptyMessageDelayed(1001, 500);
    }

    /* access modifiers changed from: private */
    public void ptzConnectedSendMsgOrClose() {
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        if (this.isConnected) {
            sendpTZMessage();
        } else {
            finish();
        }
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
                return;
            case 119:
                byte[] value = (byte[]) event.getData();
                ViseLog.m1466e("receive data :FVCalibrationActivity:command" + value[0] + "subcommand" + value[1]);
                if ((value[0] & 255) == 165) {
                    if ((value[0] & 255) == 23 && (value[2] & 255) != 0 && (value[2] & 255) != 1 && (value[2] & 255) != 2 && (value[2] & 255) != 3 && (value[2] & 255) != 4 && (value[2] & 255) != 5) {
                        this.startFailCount = 0;
                        finish();
                        return;
                    }
                    return;
                } else if ((value[0] & 255) == 165 && (value[1] & 255) == 23 && (value[2] & 255) == 6) {
                    if (this.mHandler != null) {
                        this.mHandler.removeMessages(1001);
                    }
                    ViseLog.m1466e("界面4退出");
                    finish();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        ptzConnectedSendMsgOrClose();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        EventBusUtil.unregister(this);
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
