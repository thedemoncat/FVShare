package com.freevisiontech.fvmobile.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;

public class LoadingView extends AlertDialog {
    private Animation animation;
    private ImageView iv_loading;
    private TextView tv_load_dialog;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, int theme) {
        super(context, theme);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(C0853R.layout.loading);
        getWindow().setBackgroundDrawableResource(17170445);
        this.iv_loading = (ImageView) findViewById(C0853R.C0855id.iv_loading);
        this.tv_load_dialog = (TextView) findViewById(C0853R.C0855id.tv_load_dialog);
        this.animation = AnimationUtils.loadAnimation(getContext(), C0853R.anim.progress_rotate);
        this.animation.setFillAfter(true);
    }

    public void setMessage(CharSequence message) {
        if (this.tv_load_dialog != null) {
            this.tv_load_dialog.setText(message);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (this.animation != null) {
            this.iv_loading.startAnimation(this.animation);
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.iv_loading.clearAnimation();
    }

    public void show() {
        super.show();
    }

    public void dismiss() {
        super.dismiss();
    }
}
