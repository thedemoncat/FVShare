package com.freevisiontech.fvmobile.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;

public class StorageChangeDialog extends AlertDialog implements View.OnClickListener {
    private Context context;
    private Button mKnownBtn;
    private TextView mMessageTv;

    public StorageChangeDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(C0853R.layout.dialog_storage_change);
        getWindow().setBackgroundDrawableResource(17170445);
        this.mMessageTv = (TextView) findViewById(C0853R.C0855id.storage_change_message_tv);
        this.mKnownBtn = (Button) findViewById(C0853R.C0855id.storage_change_known_btn);
        this.mKnownBtn.setOnClickListener(this);
    }

    public void setMessage(String msg) {
        this.mMessageTv.setText(msg);
    }

    public void onClick(View v) {
        v.getId();
        dismiss();
    }
}
