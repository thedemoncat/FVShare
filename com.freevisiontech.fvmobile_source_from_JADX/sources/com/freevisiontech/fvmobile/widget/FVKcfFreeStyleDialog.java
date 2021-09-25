package com.freevisiontech.fvmobile.widget;

import android.content.Context;
import android.os.Build;
import android.support.p003v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;

public class FVKcfFreeStyleDialog extends BaseDialog {
    /* access modifiers changed from: private */
    public CheckButtonOnclick buttonListener;
    /* access modifiers changed from: private */
    public CheckButtonSureOnclick buttonSureListener;
    private Context context;
    private Context mContext;
    private AlertDialog mDialog;

    public interface CheckButtonOnclick {
        void onClick(View view);
    }

    public interface CheckButtonSureOnclick {
        void onClick(View view);
    }

    public FVKcfFreeStyleDialog(Context context2) {
        super(context2, C0853R.layout.dialog_fv_kcf_free_style);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions = 1798 | 4096;
                } else {
                    uiOptions = 1798 | 1;
                }
                FVKcfFreeStyleDialog.this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        this.mContext = context2;
        getWindow().setLayout(-1, -1);
        setDialogAttributes(context2);
        LinearLayout linearLayout = (LinearLayout) findViewById(C0853R.C0855id.dialog_fvkcf_free_linear);
        ((TextView) findViewById(C0853R.C0855id.dialog_fvkcf_free_reset)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVKcfFreeStyleDialog.this.buttonListener.onClick(v);
            }
        });
        ((TextView) findViewById(C0853R.C0855id.dialog_fvkcf_free_sure)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVKcfFreeStyleDialog.this.buttonSureListener.onClick(v);
            }
        });
    }

    private void setDialogAttributes(Context context2) {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = -1;
        lp.height = -1;
        dialogWindow.setGravity(80);
        dialogWindow.setAttributes(lp);
    }

    public void setButtonOnClick(CheckButtonOnclick buttonListener2) {
        this.buttonListener = buttonListener2;
    }

    public void setButtonSureOnClick(CheckButtonSureOnclick buttonSureListener2) {
        this.buttonSureListener = buttonSureListener2;
    }

    public void finish() {
        dismiss();
    }
}
