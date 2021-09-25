package com.freevisiontech.fvmobile.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.utils.BleByteUtil;

public class ControlDefaultCamDialog extends AlertDialog implements View.OnClickListener {
    private Context context;
    private Button mCancelBtn;
    private Button mSettringBtn;

    public ControlDefaultCamDialog(Context context2) {
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
        setContentView(C0853R.layout.dialog_control_default_camera);
        getWindow().setBackgroundDrawableResource(17170445);
        this.mCancelBtn = (Button) findViewById(C0853R.C0855id.control_default_camera_dialog_cancel_btn);
        this.mSettringBtn = (Button) findViewById(C0853R.C0855id.control_default_camera_dialog_setting_btn);
        this.mCancelBtn.setOnClickListener(this);
        this.mSettringBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.control_default_camera_dialog_setting_btn:
                if (ViseBluetooth.getInstance().isConnected()) {
                    BleByteUtil.controlDefaultCamSwitch((byte) 87, 1);
                }
                ViseBluetooth.getInstance().disconnect();
                this.context.startActivity(new Intent("android.settings.BLUETOOTH_SETTINGS"));
                break;
        }
        dismiss();
    }
}
