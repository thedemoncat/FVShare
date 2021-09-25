package com.freevisiontech.fvmobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;

public class ToastDialog extends Dialog {
    private int imageView;
    private Context mContext;
    private String messageStr;
    private TextView messageTv;
    private ImageView pictrueIv;
    private Button yes;
    /* access modifiers changed from: private */
    public onYesOnclickListener yesOnclickListener;
    private String yesStr;

    public interface onYesOnclickListener {
        void onYesClick();
    }

    public ToastDialog(Context context) {
        super(context, C0853R.style.Theme_Light_Dialog);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.toast_dialog);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        this.yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ToastDialog.this.yesOnclickListener != null) {
                    ToastDialog.this.yesOnclickListener.onYesClick();
                }
            }
        });
    }

    private void initView() {
        this.yes = (Button) findViewById(C0853R.C0855id.yes);
        this.messageTv = (TextView) findViewById(C0853R.C0855id.tv_desc);
        this.pictrueIv = (ImageView) findViewById(C0853R.C0855id.iv_pictrue);
    }

    private void initData() {
        if (this.messageStr != null) {
            this.messageTv.setText(this.messageStr);
        }
        if (this.yesStr != null) {
            this.yes.setText(this.yesStr);
        } else {
            this.yes.setVisibility(8);
        }
        if (this.imageView != 0) {
            this.pictrueIv.setImageResource(this.imageView);
        }
    }

    public void setMessage(String message) {
        this.messageStr = message;
    }

    public void setImage(int image) {
        this.imageView = image;
    }

    public void setDialogCancleable(boolean cancleable) {
        setCancelable(cancleable);
    }

    public void setDialogOutsideCancleable(boolean outsideCancleable) {
        setCanceledOnTouchOutside(outsideCancleable);
    }

    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener2) {
        if (str != null) {
            this.yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener2;
    }
}
