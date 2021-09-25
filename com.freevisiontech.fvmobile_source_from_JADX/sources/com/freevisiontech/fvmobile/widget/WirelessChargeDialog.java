package com.freevisiontech.fvmobile.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;

public class WirelessChargeDialog extends AlertDialog implements View.OnClickListener {
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SPUtils.put(WirelessChargeDialog.this.context, SharePrefConstant.WIRELESS_CHARGE_DIALOG_DISPLAY_AGAIN, Boolean.valueOf(isChecked));
        }
    };
    /* access modifiers changed from: private */
    public Context context;
    private Button mNoBtn;
    private CheckBox mNotDisplayAgainCb;
    private Button mYesBtn;

    public WirelessChargeDialog(Context context2) {
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
        setContentView(C0853R.layout.dialog_wireless_charge);
        getWindow().setBackgroundDrawableResource(17170445);
        this.mYesBtn = (Button) findViewById(C0853R.C0855id.wireless_dialog_yes_btn);
        this.mNoBtn = (Button) findViewById(C0853R.C0855id.wireless_dialog_no_btn);
        this.mYesBtn.setOnClickListener(this);
        this.mNoBtn.setOnClickListener(this);
        this.mNotDisplayAgainCb = (CheckBox) findViewById(C0853R.C0855id.not_display_again_cb);
        this.mNotDisplayAgainCb.setOnCheckedChangeListener(this.checkedChangeListener);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.wireless_dialog_yes_btn:
                EventBusUtil.sendEvent(new Event(54));
                break;
            case C0853R.C0855id.wireless_dialog_no_btn:
                EventBusUtil.sendEvent(new Event(55));
                break;
        }
        dismiss();
    }
}
