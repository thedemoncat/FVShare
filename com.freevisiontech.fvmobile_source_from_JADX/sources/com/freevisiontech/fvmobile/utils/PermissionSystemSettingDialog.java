package com.freevisiontech.fvmobile.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;

public class PermissionSystemSettingDialog extends AlertDialog {
    private Button btn_dismiss;
    private Button btn_to_open;
    /* access modifiers changed from: private */
    public Context mContext;
    private TextView text_permission_system_setting;

    public PermissionSystemSettingDialog(Context context) {
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

    public PermissionSystemSettingDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public PermissionSystemSettingDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(C0853R.layout.dialog_permission_system_setting);
        getWindow().setBackgroundDrawableResource(17170445);
        this.text_permission_system_setting = (TextView) findViewById(C0853R.C0855id.text_permission_system_setting);
        this.text_permission_system_setting.setText(C0853R.string.label_permission_system_setting);
        this.btn_dismiss = (Button) findViewById(C0853R.C0855id.btn_dismiss);
        this.btn_to_open = (Button) findViewById(C0853R.C0855id.btn_to_open);
        this.btn_dismiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SPUtil.setParam(PermissionSystemSettingDialog.this.mContext, SharePrefConstant.PERMISSION_SYSTEM_SETTING, Integer.valueOf(Constants.PERMISSION_SYSTEM_SETTING_REMIND_CLOSE));
                PermissionSystemSettingDialog.this.dismiss();
            }
        });
        this.btn_to_open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SPUtil.setParam(PermissionSystemSettingDialog.this.mContext, SharePrefConstant.PERMISSION_SYSTEM_SETTING, Integer.valueOf(Constants.PERMISSION_SYSTEM_SETTING_REMIND_OPEN));
                PermissionSystemSettingDialog.this.dismiss();
                PermissionSystemSettingDialog.this.checkPermissionWrite((Activity) PermissionSystemSettingDialog.this.mContext);
            }
        });
    }

    /* access modifiers changed from: private */
    public void checkPermissionWrite(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(activity)) {
            activity.startActivity(new Intent("android.settings.action.MANAGE_WRITE_SETTINGS", Uri.parse("package:" + activity.getPackageName())));
        }
    }
}
