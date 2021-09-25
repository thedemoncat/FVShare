package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.p003v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;

public class FVCalibrationActivity extends BaseActivity {
    private static final int PREPARE_CALIBRATION_TIMEOUT = 1001;
    @Bind({2131755249})
    ImageView btnBack;
    @Bind({2131755268})
    Button btnNext;
    @Bind({2131756127})
    TextView centerTitle;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    ViseLog.m1466e("准备校准通讯超时");
                    FVCalibrationActivity.access$008(FVCalibrationActivity.this);
                    if (FVCalibrationActivity.this.startFailCount >= 3) {
                        int unused = FVCalibrationActivity.this.startFailCount = 0;
                        Toast.makeText(FVCalibrationActivity.this, FVCalibrationActivity.this.getString(C0853R.string.device_communication_error), 1).show();
                        return;
                    } else if (FVCalibrationActivity.this.isConnected) {
                        FVCalibrationActivity.this.sendPtzMessage();
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public int startFailCount = 0;

    static /* synthetic */ int access$008(FVCalibrationActivity x0) {
        int i = x0.startFailCount;
        x0.startFailCount = i + 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_fvcalibration);
        ButterKnife.bind((Activity) this);
        initTitleAndListener();
    }

    private void initTitleAndListener() {
        this.btnBack.setVisibility(0);
        this.centerTitle.setVisibility(0);
        this.centerTitle.setText(C0853R.string.label_ptz_calibration);
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCalibrationActivity.this.finish();
            }
        });
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCalibrationActivity.this.checkConnectedAndSendMessage();
            }
        });
    }

    private void gotoActivity() {
        startActivity(new Intent(this, FVCalibrationActivity2.class));
        overridePendingTransition(C0853R.anim.vilta_slide_in_right, C0853R.anim.vilta_slide_out_right);
        finish();
    }

    /* access modifiers changed from: private */
    public void checkConnectedAndSendMessage() {
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        if (this.isConnected) {
            sendPtzMessage();
        } else {
            showPtzDisConnectedDialog();
        }
    }

    private void showPtzDisConnectedDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage((int) C0853R.string.lable_bluetooth_is_disconnected);
        dialog.setCancelable(false);
        dialog.setPositiveButton((int) C0853R.string.label_sure, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FVCalibrationActivity.this.finish();
            }
        });
        dialog.show();
    }

    /* access modifiers changed from: private */
    public void sendPtzMessage() {
        BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.TAB_OFFSET_CHAN_1, (byte) 1);
        this.mHandler.sendEmptyMessageDelayed(1001, 1000);
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
                if ((value[0] & 255) != 165 || (value[1] & 255) != 23 || (value[2] & 255) == 0) {
                    return;
                }
                if ((value[2] & 255) == 1) {
                    if (this.mHandler != null) {
                        this.mHandler.removeMessages(1001);
                    }
                    this.startFailCount = 0;
                    gotoActivity();
                    return;
                }
                if ((value[2] & 255) == 2 || (value[2] & 255) == 3 || (value[2] & 255) == 4 || (value[2] & 255) != 5) {
                }
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1001);
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
