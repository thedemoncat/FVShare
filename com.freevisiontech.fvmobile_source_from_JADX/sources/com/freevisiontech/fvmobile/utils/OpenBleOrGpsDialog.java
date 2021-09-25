package com.freevisiontech.fvmobile.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;

public class OpenBleOrGpsDialog extends AlertDialog {
    private Button btn_ok;
    private Button btn_to_open;
    /* access modifiers changed from: private */
    public Context mContext;
    private TextView open_ble_or_gps_hint;

    public OpenBleOrGpsDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public OpenBleOrGpsDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public OpenBleOrGpsDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(C0853R.layout.dialog_open_ble_or_gps);
        getWindow().setBackgroundDrawableResource(17170445);
        this.open_ble_or_gps_hint = (TextView) findViewById(C0853R.C0855id.open_ble_or_gps_hint);
        this.open_ble_or_gps_hint.setText(C0853R.string.label_open_ble_or_open_gps);
        this.btn_to_open = (Button) findViewById(C0853R.C0855id.btn_to_open);
        this.btn_ok = (Button) findViewById(C0853R.C0855id.btn_ok);
        this.btn_to_open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenBleOrGpsDialog.this.dismiss();
                ((Activity) OpenBleOrGpsDialog.this.mContext).startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 0);
            }
        });
        this.btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenBleOrGpsDialog.this.dismiss();
            }
        });
    }
}
