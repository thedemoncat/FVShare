package com.freevisiontech.fvmobile.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.SPUtil;

public class WirelessChargeSupportDialog extends AlertDialog implements View.OnClickListener {
    private Context context;
    private TextView mMsgTv;
    private Button mNoBtn;
    private int mStep = 0;
    private Button mYesBtn;

    public WirelessChargeSupportDialog(Context context2) {
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
        setContentView(C0853R.layout.dialog_wireless_charge_support);
        getWindow().setBackgroundDrawableResource(17170445);
        this.mMsgTv = (TextView) findViewById(C0853R.C0855id.wireless_charging_support_message_tv);
        this.mYesBtn = (Button) findViewById(C0853R.C0855id.wireless_charging_support_yes_btn);
        this.mNoBtn = (Button) findViewById(C0853R.C0855id.wireless_charging_support_no_btn);
        this.mYesBtn.setOnClickListener(this);
        this.mNoBtn.setOnClickListener(this);
    }

    private void confirmClick() {
        if (this.mStep == 0) {
            SPUtil.setParam(this.context, "save_storage_path", 1);
            this.mMsgTv.setText(this.context.getString(C0853R.string.is_open_wireless_charging));
            this.mStep++;
        } else if (this.mStep == 1) {
            BleByteUtil.setPTZParameters((byte) 24, (byte) 1);
            dismiss();
        }
    }

    private void cancelClick() {
        if (this.mStep == 0) {
            SPUtil.setParam(this.context, "save_storage_path", 2);
            BleByteUtil.setPTZParameters((byte) 24, (byte) 0);
        } else if (this.mStep == 1) {
            BleByteUtil.setPTZParameters((byte) 24, (byte) 0);
        }
        dismiss();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.wireless_charging_support_no_btn:
                cancelClick();
                return;
            case C0853R.C0855id.wireless_charging_support_yes_btn:
                confirmClick();
                return;
            default:
                return;
        }
    }
}
