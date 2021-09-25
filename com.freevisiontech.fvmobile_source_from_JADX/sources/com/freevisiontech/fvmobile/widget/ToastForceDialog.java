package com.freevisiontech.fvmobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;

public class ToastForceDialog extends Dialog {
    private TextView bodyTv;
    private Context mContext;
    private TextView titleTv;
    private Button yes;
    /* access modifiers changed from: private */
    public onYesOnclickListener yesOnclickListener;
    private String yesStr;

    public interface onYesOnclickListener {
        void onYesClick();
    }

    public ToastForceDialog(Context context) {
        super(context, C0853R.style.Theme_Light_Dialog);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.toast_force_dialog);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        this.yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ToastForceDialog.this.yesOnclickListener != null) {
                    ToastForceDialog.this.yesOnclickListener.onYesClick();
                }
            }
        });
    }

    private void initView() {
        this.yes = (Button) findViewById(C0853R.C0855id.yes);
        this.titleTv = (TextView) findViewById(C0853R.C0855id.tv_text_title);
        this.bodyTv = (TextView) findViewById(C0853R.C0855id.tv_text_body);
    }

    private void initData() {
    }

    public void setTitleAndBodyText(String strTitle, String strBody) {
        this.titleTv.setText(strTitle);
        this.bodyTv.setText(strBody);
    }

    public void setMessageGravity(int gravity) {
        this.bodyTv.setGravity(gravity);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.bodyTv.getLayoutParams();
        params.setMargins(0, 0, 0, 40);
        this.bodyTv.setLayoutParams(params);
    }

    public void setDialogCancleable(boolean cancleable) {
        setCancelable(cancleable);
    }

    public void setDialogOutsideCancleable(boolean outsideCancleable) {
        setCanceledOnTouchOutside(outsideCancleable);
    }

    public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener2) {
        this.yesOnclickListener = onYesOnclickListener2;
    }
}
