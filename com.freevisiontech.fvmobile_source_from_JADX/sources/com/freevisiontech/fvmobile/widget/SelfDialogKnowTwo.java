package com.freevisiontech.fvmobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.freevisiontech.fvmobile.C0853R;

public class SelfDialogKnowTwo extends Dialog {
    /* access modifiers changed from: private */
    public CheckButtonOnclick buttonListener;
    private Button finish;

    public interface CheckButtonOnclick {
        void onClick(View view);
    }

    public SelfDialogKnowTwo(Context context) {
        super(context, C0853R.style.Theme_Light_Dialog);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.upgrade_dialog_i_know_two);
        initView();
    }

    private void initView() {
        this.finish = (Button) findViewById(C0853R.C0855id.finish);
        this.finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelfDialogKnowTwo.this.buttonListener.onClick(v);
            }
        });
    }

    public void setButtonOnClick(CheckButtonOnclick buttonListener2) {
        this.buttonListener = buttonListener2;
    }
}
