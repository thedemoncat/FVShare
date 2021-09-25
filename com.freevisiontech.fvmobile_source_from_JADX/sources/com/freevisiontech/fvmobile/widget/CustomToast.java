package com.freevisiontech.fvmobile.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.Util;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomToast {
    public static final int LENGTH_ALWAYS = 0;
    public static final int LENGTH_LONG = 4;
    public static final int LENGTH_SHORT = 2;
    private Handler handler = new Handler();
    private Method hide;
    private Runnable hideRunnable = new Runnable() {
        public void run() {
            CustomToast.this.hide();
        }
    };
    private boolean isShow = false;
    private TextView mBt;
    private Context mContext;
    private int mDuration = 2;
    private int mImg;
    private ImageView mIv;
    private String mMessage;
    private int mStyle;
    private Object mTN;
    private Boolean needToast = false;
    private Method show;
    private Toast toastStart;

    public CustomToast(Context context) {
        this.mContext = context;
    }

    public void customToast(String message, int img, int style, int duration) {
        this.mMessage = message;
        this.mImg = img;
        this.mStyle = style;
        this.mDuration = duration;
        if (this.toastStart != null) {
            this.toastStart.cancel();
        }
        View inflate = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0853R.layout.custom_toast, (ViewGroup) null);
        RelativeLayout linearLayout = (RelativeLayout) inflate.findViewById(C0853R.C0855id.ll_show);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Util.getDisplayMetrics((Activity) this.mContext).widthPixels - Util.dip2px(this.mContext, 16.0f), Util.dip2px(this.mContext, 50.0f));
        layoutParams.leftMargin = Util.dip2px(this.mContext, 8.0f);
        linearLayout.setLayoutParams(layoutParams);
        Util.fullScreenImmersive(linearLayout);
        this.mIv = (ImageView) inflate.findViewById(C0853R.C0855id.iv_show);
        this.mBt = (TextView) inflate.findViewById(C0853R.C0855id.tv_show);
        this.toastStart = new Toast(this.mContext);
        this.toastStart.setGravity(55, 0, Util.dip2px(this.mContext, 15.0f));
        this.toastStart.setDuration(1);
        this.toastStart.setView(inflate);
        this.mBt.setText(this.mMessage);
        this.mIv.setImageResource(this.mImg);
        this.toastStart.show();
    }

    public static float dp2px(Context context, int dp) {
        return ((float) dp) * context.getResources().getDisplayMetrics().density;
    }

    public void hide() {
        if (this.isShow) {
            try {
                this.hide.invoke(this.mTN, new Object[0]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
            }
            this.isShow = false;
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        this.toastStart.setGravity(gravity, xOffset, yOffset);
    }

    public void cancel() {
        if (this.toastStart != null) {
            this.toastStart.cancel();
        }
    }

    public void setNeedToast(boolean needToast2) {
        this.needToast = Boolean.valueOf(needToast2);
    }

    public void initTN(int animations) {
        try {
            Field tnField = this.toastStart.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            this.mTN = tnField.get(this.toastStart);
            this.show = this.mTN.getClass().getMethod("show", new Class[0]);
            this.hide = this.mTN.getClass().getMethod("hide", new Class[0]);
            if (animations != -1) {
                Field tnParamsField = this.mTN.getClass().getDeclaredField("mParams");
                tnParamsField.setAccessible(true);
                ((WindowManager.LayoutParams) tnParamsField.get(this.mTN)).windowAnimations = animations;
            }
            Field tnNextViewField = this.mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(this.mTN, this.toastStart.getView());
            try {
                this.show.invoke(this.mTN, new Object[0]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
            }
            this.isShow = true;
            if (this.mDuration > 0) {
                this.handler.postDelayed(this.hideRunnable, (long) (this.mDuration * 1000));
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }
}
