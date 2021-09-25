package com.freevisiontech.fvmobile.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.freevisiontech.fvmobile.C0853R;

public class TransparentPop extends PopupWindow {
    /* access modifiers changed from: private */
    public View contentView;
    private boolean defineHeight;
    private boolean defineWidth;
    /* access modifiers changed from: private */
    public DismissListener dismissListener;
    /* access modifiers changed from: private */
    public boolean hasAnimation;
    private Animation inAnimation;
    private Context mContext;
    private int mHeight;
    private InvokeListener mInvokeListener;
    private int mWidth;
    private Animation outAnimation;
    private LinearLayout parent;

    public interface DismissListener {
        void dismiss();
    }

    public interface InvokeListener {
        void invokeView(LinearLayout linearLayout);
    }

    public TransparentPop(Context context, InvokeListener listener) {
        this(context, -1, -1, listener);
    }

    public TransparentPop(Context context, int width, int height, InvokeListener listener) {
        super(context);
        this.defineWidth = false;
        this.defineHeight = false;
        this.hasAnimation = true;
        this.mContext = context;
        this.mInvokeListener = listener;
        if (width > 0) {
            this.defineWidth = true;
            this.mWidth = width;
        }
        if (height > 0) {
            this.defineHeight = true;
            this.mHeight = height;
        }
        init();
    }

    private void init() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(C0853R.layout.layout_transparent_pop, new LinearLayout(this.mContext), false);
        this.parent = (LinearLayout) this.contentView.findViewById(C0853R.C0855id.content);
        if (this.mInvokeListener != null) {
            this.mInvokeListener.invokeView(this.parent);
        }
        if (this.defineWidth) {
            setWidth(this.mWidth);
        } else {
            setWidth(-1);
        }
        if (this.defineHeight) {
            setHeight(this.mHeight);
        } else {
            setHeight(-1);
        }
        setContentView(this.contentView);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        this.inAnimation = AnimationUtils.loadAnimation(this.mContext, C0853R.anim.fade_scale_in);
        this.outAnimation = AnimationUtils.loadAnimation(this.mContext, C0853R.anim.fade_scale_out);
        initListener();
    }

    private void initListener() {
        initOutAnimationListener();
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                if (TransparentPop.this.dismissListener != null) {
                    TransparentPop.this.dismissListener.dismiss();
                }
            }
        });
    }

    private void initOutAnimationListener() {
        this.outAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                TransparentPop.this.contentView.post(new Runnable() {
                    public void run() {
                        TransparentPop.this.dismiss();
                    }
                });
            }
        });
    }

    public void dismissFromOut() {
        this.contentView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TransparentPop.this.dismiss(TransparentPop.this.hasAnimation);
            }
        });
    }

    public void show(View reference) {
        show(reference, true);
    }

    public void show(View reference, boolean hasAnimation2) {
        show(reference, 0, 0, hasAnimation2);
    }

    public void show(View reference, int width, int height, boolean hasAnimation2) {
        if (hasAnimation2) {
            this.parent.startAnimation(this.inAnimation);
        }
        showAtLocation(reference, 17, width, height);
    }

    public void dismiss(boolean hasAnimation2) {
        if (hasAnimation2) {
            this.parent.startAnimation(this.outAnimation);
            dismiss();
            return;
        }
        dismiss();
    }

    public void setInAnimation(Animation inAnimation2) {
        this.inAnimation = inAnimation2;
    }

    public void setOutAnimation(Animation outAnimation2) {
        this.outAnimation = outAnimation2;
        initOutAnimationListener();
    }

    public void setBackGroundResource(int id) {
        if (this.contentView != null) {
            this.contentView.setBackgroundResource(id);
        }
    }

    public void setDismissListener(DismissListener dismissListener2) {
        this.dismissListener = dismissListener2;
    }
}
