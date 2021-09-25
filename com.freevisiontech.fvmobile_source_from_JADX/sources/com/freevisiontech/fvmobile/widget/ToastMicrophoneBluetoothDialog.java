package com.freevisiontech.fvmobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;

public class ToastMicrophoneBluetoothDialog extends Dialog {
    private TextView bodyTv;
    private Context mContext;
    private Button yes;
    /* access modifiers changed from: private */
    public onYesOnclickListener yesOnclickListener;
    private String yesStr;

    public interface onYesOnclickListener {
        void onYesClick();
    }

    public ToastMicrophoneBluetoothDialog(Context context) {
        super(context, C0853R.style.Theme_Light_Dialog);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.dialog_microphone_bouetooth_no_find);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        this.yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ToastMicrophoneBluetoothDialog.this.yesOnclickListener != null) {
                    ToastMicrophoneBluetoothDialog.this.yesOnclickListener.onYesClick();
                }
            }
        });
    }

    private void initView() {
        this.yes = (Button) findViewById(C0853R.C0855id.yes);
        this.bodyTv = (TextView) findViewById(C0853R.C0855id.tv_text_body);
    }

    private void initData() {
    }

    public void setTitleAndBodyText(String strTitle, String strBody) {
        this.bodyTv.setText(strBody);
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
