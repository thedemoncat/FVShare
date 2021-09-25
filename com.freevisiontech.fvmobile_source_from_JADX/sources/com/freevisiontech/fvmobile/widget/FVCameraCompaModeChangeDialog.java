package com.freevisiontech.fvmobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;

public class FVCameraCompaModeChangeDialog extends Dialog {
    /* access modifiers changed from: private */
    public CheckButtonOnclick buttonListener;
    /* access modifiers changed from: private */
    public CheckButtonSureOnclick buttonSureListener;
    private Context context;
    private RelativeLayout mLayout;

    public interface CheckButtonOnclick {
        void onClick(View view);
    }

    public interface CheckButtonSureOnclick {
        void onClick(View view);
    }

    public FVCameraCompaModeChangeDialog(Context context2) {
        super(context2, C0853R.style.Theme_Light_Dialog);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.dialog_fv_camera_compa_mode_change);
        ((TextView) findViewById(C0853R.C0855id.dialog_fvkcf_free_reset)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCameraCompaModeChangeDialog.this.buttonListener.onClick(v);
            }
        });
        ((TextView) findViewById(C0853R.C0855id.dialog_fvkcf_free_sure)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCameraCompaModeChangeDialog.this.buttonSureListener.onClick(v);
            }
        });
    }

    public void setButtonOnClick(CheckButtonOnclick buttonListener2) {
        this.buttonListener = buttonListener2;
    }

    public void setButtonSureOnClick(CheckButtonSureOnclick buttonSureListener2) {
        this.buttonSureListener = buttonSureListener2;
    }

    public void setDialogCancleable(boolean cancleable) {
        setCancelable(cancleable);
    }

    public void setDialogOutsideCancleable(boolean outsideCancleable) {
        setCanceledOnTouchOutside(outsideCancleable);
    }
}
