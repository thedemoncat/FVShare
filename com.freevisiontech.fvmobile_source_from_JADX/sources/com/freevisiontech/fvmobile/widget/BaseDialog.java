package com.freevisiontech.fvmobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import com.freevisiontech.fvmobile.C0853R;

public class BaseDialog extends Dialog {
    public BaseDialog(Context context, int layoutResID) {
        this(context, layoutResID, -2, -2);
    }

    public BaseDialog(Context context, int layoutResID, int width, int height) {
        this(context, layoutResID, width, height, 17);
    }

    public BaseDialog(Context context, int layoutResID, int width, int height, int gravity) {
        super(context, getTheme(context));
        setContentView(layoutResID);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setWindowAnimations(16973826);
        window.setBackgroundDrawableResource(17170445);
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        window.setAttributes(params);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    private static int getTheme(Context context) {
        return C0853R.style.Theme_Light_Dialog;
    }
}
