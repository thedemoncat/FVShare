package com.freevisiontech.fvmobile.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.Util;

public class FVPhotoVideoDeleteDialog extends Dialog {
    /* access modifiers changed from: private */
    public CheckButtonOnclick buttonListener;
    /* access modifiers changed from: private */
    public CheckButtonSureOnclick buttonSureListener;
    /* access modifiers changed from: private */
    public Context context;

    public interface CheckButtonOnclick {
        void onClick(View view);
    }

    public interface CheckButtonSureOnclick {
        void onClick(View view);
    }

    public FVPhotoVideoDeleteDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(C0853R.layout.dialog_delete_video_yes_or_no);
        getWindow().setBackgroundDrawableResource(17170445);
        ((TextView) findViewById(C0853R.C0855id.dialog_fvkcf_free_reset)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVPhotoVideoDeleteDialog.this.buttonListener.onClick(v);
            }
        });
        ((TextView) findViewById(C0853R.C0855id.dialog_fvkcf_free_sure)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVPhotoVideoDeleteDialog.this.buttonSureListener.onClick(v);
            }
        });
        setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                Util.hideBottomUIMenu((Activity) FVPhotoVideoDeleteDialog.this.context);
            }
        });
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
