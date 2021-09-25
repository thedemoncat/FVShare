package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.p003v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.widget.FVCalibrationExitPop;
import com.freevisiontech.fvmobile.widget.FVCalibrationMessagePop;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;

public class FVCalibrationActivity3 extends BaseActivity {
    @Bind({2131755249})
    ImageView btnBack;
    @Bind({2131756127})
    TextView centerTitle;
    /* access modifiers changed from: private */
    public boolean destroyed = false;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    private boolean moveMode = false;
    /* access modifiers changed from: private */
    public boolean moveTimeout = false;
    private FVCalibrationMessagePop msgPop;
    @Bind({2131755272})
    ProgressBar progressBar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_fvcalibration3);
        ButterKnife.bind((Activity) this);
        initTitleAndListener();
        countDownTimer();
    }

    private void initTitleAndListener() {
        this.btnBack.setVisibility(0);
        this.centerTitle.setVisibility(0);
        this.centerTitle.setText(C0853R.string.label_ptz_calibration);
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean unused = FVCalibrationActivity3.this.moveTimeout = true;
                boolean unused2 = FVCalibrationActivity3.this.destroyed = true;
                FVCalibrationActivity3.this.showExitPop();
            }
        });
    }

    private void countDownTimer() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (!FVCalibrationActivity3.this.destroyed) {
                    boolean unused = FVCalibrationActivity3.this.moveTimeout = true;
                    boolean unused2 = FVCalibrationActivity3.this.isConnected = ViseBluetooth.getInstance().isConnected();
                    if (FVCalibrationActivity3.this.isConnected) {
                        FVCalibrationActivity3.this.showPop(true);
                    } else {
                        FVCalibrationActivity3.this.showPtzDisConnectedDialog();
                    }
                }
            }
        }, 30000);
    }

    /* access modifiers changed from: private */
    public void showPtzDisConnectedDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage((int) C0853R.string.lable_bluetooth_is_disconnected);
        dialog.setCancelable(false);
        dialog.setPositiveButton((int) C0853R.string.label_sure, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FVCalibrationActivity3.this.finish();
            }
        });
        dialog.show();
    }

    private void gotoActivity(boolean success) {
        this.destroyed = true;
        Intent intent = new Intent(this, FVCalibrationActivity4.class);
        intent.putExtra("success", success);
        startActivity(intent);
        overridePendingTransition(C0853R.anim.vilta_slide_in_right, C0853R.anim.vilta_slide_out_right);
        finish();
    }

    /* access modifiers changed from: private */
    public void showPop(boolean exit) {
        this.moveMode = exit;
        this.msgPop = new FVCalibrationMessagePop(this, exit);
        this.msgPop.show(this.progressBar);
    }

    /* access modifiers changed from: private */
    public void showExitPop() {
        new FVCalibrationExitPop(this).show(this.btnBack);
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
                ViseLog.m1466e("FVCalibrationActivity3:command" + value[0] + "subcommand" + value[1]);
                if ((value[0] & 255) == 90) {
                    if ((value[1] & 255) == 23 && (value[2] & 255) != 0 && (value[2] & 255) != 1) {
                        if ((value[2] & 255) == 2) {
                            if (this.msgPop != null && !this.moveMode) {
                                this.msgPop.dismiss();
                                return;
                            }
                            return;
                        } else if ((value[2] & 255) == 3) {
                            BleByteUtil.actPTZSettingChange(ClosedCaptionCtrl.TAB_OFFSET_CHAN_1);
                            gotoActivity(true);
                            return;
                        } else if ((value[2] & 255) == 4) {
                            BleByteUtil.actPTZSettingChange(ClosedCaptionCtrl.TAB_OFFSET_CHAN_1);
                            gotoActivity(false);
                            return;
                        } else if ((value[2] & 255) == 5) {
                            BleByteUtil.actPTZSettingChange(ClosedCaptionCtrl.TAB_OFFSET_CHAN_1);
                            if (!this.moveTimeout) {
                                showPop(false);
                                return;
                            }
                            return;
                        } else if ((value[2] & 255) == 6) {
                            finish();
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if ((value[0] & 255) == 165 && (value[1] & 255) == 23 && (value[2] & 255) == 6) {
                    ViseLog.m1466e("界面3退出");
                    if (this.msgPop != null) {
                        this.msgPop.dismiss();
                    }
                    finish();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void onBackPressed() {
        this.destroyed = true;
        showExitPop();
    }

    public void onDestroy() {
        this.destroyed = true;
        EventBusUtil.unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
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
