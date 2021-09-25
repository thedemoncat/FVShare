package com.freevisiontech.fvmobile.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.LoadingView;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity {
    protected Dialog dialog;
    private LoadingView mProgressDialog;
    private Toast toast;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_main);
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
    }

    public void MyToast(String s) {
        if (this.toast == null) {
            this.toast = Toast.makeText(this, s, 0);
        } else {
            this.toast.setText(s);
        }
        this.toast.show();
    }

    /* access modifiers changed from: protected */
    public int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            return getResources().getDimensionPixelSize(Integer.parseInt(c.getField("status_bar_height").get(c.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void MyToast(int s) {
        if (this.toast == null) {
            this.toast = Toast.makeText(this, s, 0);
        } else {
            this.toast.setText(s);
        }
        this.toast.show();
    }

    public void showProgress(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new LoadingView(this);
            this.mProgressDialog.setCancelable(true);
            this.mProgressDialog.setCanceledOnTouchOutside(false);
        }
        this.mProgressDialog.setMessage(msg);
        this.mProgressDialog.show();
    }

    public void hideProgress() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }

    public void showDialogv7(String title, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) title);
        builder.setNegativeButton((int) C0853R.string.label_cancel, (DialogInterface.OnClickListener) null);
        builder.setPositiveButton((int) C0853R.string.label_sure, clickListener);
        builder.show();
    }

    public void showDialogv7(int title, String negative, String positive, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(title);
        builder.setNegativeButton((CharSequence) negative, (DialogInterface.OnClickListener) null);
        builder.setPositiveButton((CharSequence) positive, clickListener);
        builder.show();
    }

    public void showDialogv7(int title, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(title);
        builder.setNegativeButton((int) C0853R.string.label_cancel, (DialogInterface.OnClickListener) null);
        builder.setPositiveButton((int) C0853R.string.label_sure, clickListener);
        builder.show();
    }

    public void closrKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showDialog() {
    }

    public void closeDialog() {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
    }

    public void finish() {
        closrKeyboard();
        super.finish();
    }

    /* access modifiers changed from: protected */
    public boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStickyEventBusCome(Event event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
    }

    /* access modifiers changed from: protected */
    public void receiveStickyEvent(Event event) {
    }

    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }
}
