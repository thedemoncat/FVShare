package com.freevisiontech.fvmobile.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.bean.network.ActivateVerify;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.google.android.vending.expansion.downloader.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class ActivateDialog extends AlertDialog implements View.OnClickListener {
    public static final String URL_ACTIVATE_STEP_ONE = "http://app.freevisiontech.com:8080/oss/activation/UserActivationQueryJsonForDB.jspx";
    public static final String URL_ACTIVATE_STEP_TWO = "http://app.freevisiontech.com:8080/oss/activation/ActivationDoneQueryJsonForDB.jspx";
    private static final String URL_AGREEMENT = "http://www.freevisiontech.com/fvprivatepolicy.htm";
    /* access modifiers changed from: private */
    public LoadingView activateProgressDialog;
    ActivateVerify activateVerify;
    /* access modifiers changed from: private */
    public Context context;
    private TextView mAgreementTv;

    public ActivateDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(C0853R.layout.dialog_activate);
        findViewById(C0853R.C0855id.dismiss_activate_dialog_iv).setOnClickListener(this);
        findViewById(C0853R.C0855id.activate_btn).setOnClickListener(this);
        this.mAgreementTv = (TextView) findViewById(C0853R.C0855id.activate_user_privacy_agreement);
        this.mAgreementTv.setOnClickListener(this);
        getWindow().setBackgroundDrawableResource(17170445);
        SpannableString ss = new SpannableString(this.context.getString(C0853R.string.activate_user_privacy_agreement));
        ss.setSpan(new URLSpanNoUnderline(URL_AGREEMENT), 0, 14, 33);
        this.mAgreementTv.setText(ss);
        this.mAgreementTv.setMovementMethod(LinkMovementMethod.getInstance());
        this.activateProgressDialog = new LoadingView(this.context);
        this.activateProgressDialog.setCancelable(false);
        this.activateProgressDialog.setCanceledOnTouchOutside(false);
        EventBusUtil.register(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.activate_btn:
                startActivate();
                return;
            case C0853R.C0855id.dismiss_activate_dialog_iv:
                dismiss();
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    private void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 49:
                if (this.activateVerify != null) {
                    Toast.makeText(this.context, this.context.getString(C0853R.string.activate_success), 1).show();
                    dismiss();
                    affirmActivate("10");
                    return;
                }
                return;
            case 50:
            case 53:
                if (this.activateVerify != null) {
                    Toast.makeText(this.context, this.context.getString(C0853R.string.activate_fail_code) + "1101)", 1).show();
                    if (this.activateProgressDialog.isShowing()) {
                        this.activateProgressDialog.dismiss();
                    }
                    affirmActivate("30");
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void affirmActivate(String result) {
        ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) OkGo.post(URL_ACTIVATE_STEP_TWO).tag(this.context)).headers("charset", "UTF-8")).params("activation_id", this.activateVerify.getActivationId(), new boolean[0])).params("results", result, new boolean[0])).execute(new StringCallback() {
            public void onSuccess(Response<String> response) {
                if (ActivateDialog.this.activateProgressDialog.isShowing()) {
                    BleByteUtil.getPTZSingleParameters((byte) 26);
                    ActivateDialog.this.activateProgressDialog.dismiss();
                }
            }

            public void onError(Response<String> response) {
                super.onError(response);
                Toast.makeText(ActivateDialog.this.context, ActivateDialog.this.context.getString(C0853R.string.activate_fail), 1).show();
                if (ActivateDialog.this.activateProgressDialog.isShowing()) {
                    ActivateDialog.this.activateProgressDialog.dismiss();
                }
            }
        });
    }

    private void startActivate() {
        this.activateProgressDialog.show();
        this.activateProgressDialog.setMessage(this.context.getString(C0853R.string.activating));
        String ptzSnCode = BlePtzParasConstant.GET_PTZ_SN_CODE;
        int activateStatus = BlePtzParasConstant.GET_PTZ_ACTIVATE_STATUS;
        String ptzMac = (String) SPUtils.get(this.context, SharePrefConstant.CURRENT_PTZ_MAC, "");
        String phoneType = Build.BRAND + Constants.FILENAME_SEQUENCE_SEPARATOR + Build.MODEL;
        String type = "";
        if (CameraUtils.getCurrentPageIndex() == 0) {
            type = BleConstant.FM_200_DISPLAY_NAME;
        } else if (CameraUtils.getCurrentPageIndex() == 1) {
            type = BleConstant.FM_300_DISPLAY_NAME;
        }
        if (CameraUtils.getCurrentPageIndex() == 2) {
            type = BleConstant.FM_210_DISPLAY_NAME;
        }
        if (ptzSnCode != null && !ptzSnCode.equals("") && ptzMac != null && ptzMac != "" && phoneType != null && type != null) {
            ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) OkGo.post(URL_ACTIVATE_STEP_ONE).tag(this.context)).headers("charset", "UTF-8")).params("sn_code", ptzSnCode, new boolean[0])).params("mac_address", ptzMac.replace(":", ""), new boolean[0])).params("phone_type", phoneType, new boolean[0])).params(IjkMediaMeta.IJKM_KEY_TYPE, type, new boolean[0])).params("activation_by", activateStatus, new boolean[0])).execute(new StringCallback() {
                public void onSuccess(Response<String> response) {
                    try {
                        String jsonStr = new JsonParser().parse(response.body()).getAsString();
                        Gson gson = new Gson();
                        ActivateDialog.this.activateVerify = (ActivateVerify) gson.fromJson(jsonStr, ActivateVerify.class);
                        if (!ActivateDialog.this.activateVerify.getCode().equals("1000")) {
                            if (ActivateDialog.this.activateProgressDialog.isShowing()) {
                                ActivateDialog.this.activateProgressDialog.dismiss();
                            }
                            Toast.makeText(ActivateDialog.this.context, ActivateDialog.this.context.getString(C0853R.string.activate_fail_code) + ActivateDialog.this.activateVerify.getCode() + ")", 1).show();
                        } else if (ActivateDialog.this.activateVerify != null) {
                            BleByteUtil.setPTZParameters((byte) 58, ActivateDialog.this.formatCmd(ActivateDialog.this.activateVerify));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (ActivateDialog.this.activateProgressDialog.isShowing()) {
                            ActivateDialog.this.activateProgressDialog.dismiss();
                        }
                        Toast.makeText(ActivateDialog.this.context, ActivateDialog.this.context.getString(C0853R.string.activate_fail_code) + "1098)", 1).show();
                    }
                }

                public void onError(Response<String> response) {
                    super.onError(response);
                    Toast.makeText(ActivateDialog.this.context, ActivateDialog.this.context.getString(C0853R.string.activate_fail_code) + "1099)", 1).show();
                    if (ActivateDialog.this.activateProgressDialog.isShowing()) {
                        ActivateDialog.this.activateProgressDialog.dismiss();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public byte[] formatCmd(ActivateVerify activateVerify2) {
        byte[] activateCmd = new byte[20];
        activateCmd[0] = 1;
        int sum = 0;
        byte[] randomCmdList = HexUtil.StringToBytes(activateVerify2.getRandom());
        byte[] encodeCmdList = HexUtil.StringToBytes(activateVerify2.getResult());
        for (int i = 0; i < randomCmdList.length; i++) {
            activateCmd[i + 1] = randomCmdList[i];
            sum = i + 2;
        }
        for (int j = 0; j < encodeCmdList.length; j++) {
            activateCmd[sum + j] = encodeCmdList[j];
        }
        return activateCmd;
    }

    public class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.bgColor = -1;
            ds.setColor(Color.parseColor("#4ba0ff"));
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        EventBusUtil.unregister(this);
    }
}
