package com.bumptech.glide.request.target;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.bumptech.glide.request.Request;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class ViewTarget<T extends View, Z> extends BaseTarget<Z> {
    private static final String TAG = "ViewTarget";
    private static boolean isTagUsedAtLeastOnce = false;
    private static Integer tagId = null;
    private final SizeDeterminer sizeDeterminer;
    protected final T view;

    public static void setTagId(int tagId2) {
        if (tagId != null || isTagUsedAtLeastOnce) {
            throw new IllegalArgumentException("You cannot set the tag id more than once or change the tag id after the first request has been made");
        }
        tagId = Integer.valueOf(tagId2);
    }

    public ViewTarget(T view2) {
        if (view2 == null) {
            throw new NullPointerException("View must not be null!");
        }
        this.view = view2;
        this.sizeDeterminer = new SizeDeterminer(view2);
    }

    public T getView() {
        return this.view;
    }

    public void getSize(SizeReadyCallback cb) {
        this.sizeDeterminer.getSize(cb);
    }

    public void setRequest(Request request) {
        setTag(request);
    }

    public Request getRequest() {
        Object tag = getTag();
        if (tag == null) {
            return null;
        }
        if (tag instanceof Request) {
            return (Request) tag;
        }
        throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
    }

    private void setTag(Object tag) {
        if (tagId == null) {
            isTagUsedAtLeastOnce = true;
            this.view.setTag(tag);
            return;
        }
        this.view.setTag(tagId.intValue(), tag);
    }

    private Object getTag() {
        if (tagId == null) {
            return this.view.getTag();
        }
        return this.view.getTag(tagId.intValue());
    }

    public String toString() {
        return "Target for: " + this.view;
    }

    private static class SizeDeterminer {
        private static final int PENDING_SIZE = 0;
        private final List<SizeReadyCallback> cbs = new ArrayList();
        private Point displayDimens;
        private SizeDeterminerLayoutListener layoutListener;
        private final View view;

        public SizeDeterminer(View view2) {
            this.view = view2;
        }

        private void notifyCbs(int width, int height) {
            for (SizeReadyCallback cb : this.cbs) {
                cb.onSizeReady(width, height);
            }
            this.cbs.clear();
        }

        /* access modifiers changed from: private */
        public void checkCurrentDimens() {
            if (!this.cbs.isEmpty()) {
                int currentWidth = getViewWidthOrParam();
                int currentHeight = getViewHeightOrParam();
                if (isSizeValid(currentWidth) && isSizeValid(currentHeight)) {
                    notifyCbs(currentWidth, currentHeight);
                    ViewTreeObserver observer = this.view.getViewTreeObserver();
                    if (observer.isAlive()) {
                        observer.removeOnPreDrawListener(this.layoutListener);
                    }
                    this.layoutListener = null;
                }
            }
        }

        public void getSize(SizeReadyCallback cb) {
            int currentWidth = getViewWidthOrParam();
            int currentHeight = getViewHeightOrParam();
            if (!isSizeValid(currentWidth) || !isSizeValid(currentHeight)) {
                if (!this.cbs.contains(cb)) {
                    this.cbs.add(cb);
                }
                if (this.layoutListener == null) {
                    ViewTreeObserver observer = this.view.getViewTreeObserver();
                    this.layoutListener = new SizeDeterminerLayoutListener(this);
                    observer.addOnPreDrawListener(this.layoutListener);
                    return;
                }
                return;
            }
            cb.onSizeReady(currentWidth, currentHeight);
        }

        private int getViewHeightOrParam() {
            ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
            if (isSizeValid(this.view.getHeight())) {
                return this.view.getHeight();
            }
            if (layoutParams != null) {
                return getSizeForParam(layoutParams.height, true);
            }
            return 0;
        }

        private int getViewWidthOrParam() {
            ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
            if (isSizeValid(this.view.getWidth())) {
                return this.view.getWidth();
            }
            if (layoutParams != null) {
                return getSizeForParam(layoutParams.width, false);
            }
            return 0;
        }

        private int getSizeForParam(int param, boolean isHeight) {
            if (param != -2) {
                return param;
            }
            Point displayDimens2 = getDisplayDimens();
            return isHeight ? displayDimens2.y : displayDimens2.x;
        }

        @TargetApi(13)
        private Point getDisplayDimens() {
            if (this.displayDimens != null) {
                return this.displayDimens;
            }
            Display display = ((WindowManager) this.view.getContext().getSystemService("window")).getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= 13) {
                this.displayDimens = new Point();
                display.getSize(this.displayDimens);
            } else {
                this.displayDimens = new Point(display.getWidth(), display.getHeight());
            }
            return this.displayDimens;
        }

        private boolean isSizeValid(int size) {
            return size > 0 || size == -2;
        }

        private static class SizeDeterminerLayoutListener implements ViewTreeObserver.OnPreDrawListener {
            private final WeakReference<SizeDeterminer> sizeDeterminerRef;

            public SizeDeterminerLayoutListener(SizeDeterminer sizeDeterminer) {
                this.sizeDeterminerRef = new WeakReference<>(sizeDeterminer);
            }

            public boolean onPreDraw() {
                if (Log.isLoggable(ViewTarget.TAG, 2)) {
                    Log.v(ViewTarget.TAG, "OnGlobalLayoutListener called listener=" + this);
                }
                SizeDeterminer sizeDeterminer = (SizeDeterminer) this.sizeDeterminerRef.get();
                if (sizeDeterminer == null) {
                    return true;
                }
                sizeDeterminer.checkCurrentDimens();
                return true;
            }
        }
    }
}
