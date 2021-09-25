package p004pl.droidsonroids.gif;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;
import java.io.IOException;

/* renamed from: pl.droidsonroids.gif.GifTextView */
public class GifTextView extends TextView {
    private boolean mFreezesAnimation;

    public GifTextView(Context context) {
        super(context);
    }

    public GifTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public GifTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle, 0);
    }

    @RequiresApi(21)
    public GifTextView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        init(attrs, defStyle, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle, int defStyleRes) {
        if (attrs != null) {
            Drawable left = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableLeft", 0));
            Drawable top = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableTop", 0));
            Drawable right = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableRight", 0));
            Drawable bottom = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableBottom", 0));
            Drawable start = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableStart", 0));
            Drawable end = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableEnd", 0));
            if (Build.VERSION.SDK_INT >= 17) {
                if (getLayoutDirection() == 0) {
                    if (start == null) {
                        start = left;
                    }
                    if (end == null) {
                        end = right;
                    }
                } else {
                    if (start == null) {
                        start = right;
                    }
                    if (end == null) {
                        end = left;
                    }
                }
                setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
                setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            }
            setBackgroundInternal(getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", 0)));
        }
        this.mFreezesAnimation = GifViewUtils.isFreezingAnimation(this, attrs, defStyle, defStyleRes);
    }

    private void setBackgroundInternal(Drawable bg) {
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(bg);
        } else {
            setBackgroundDrawable(bg);
        }
    }

    public void setBackgroundResource(int resId) {
        setBackgroundInternal(getGifOrDefaultDrawable(resId));
    }

    private Drawable getGifOrDefaultDrawable(int resId) {
        if (resId == 0) {
            return null;
        }
        Resources resources = getResources();
        if (!isInEditMode() && "drawable".equals(resources.getResourceTypeName(resId))) {
            try {
                return new GifDrawable(resources, resId);
            } catch (Resources.NotFoundException | IOException e) {
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(resId, getContext().getTheme());
        }
        return resources.getDrawable(resId);
    }

    @RequiresApi(17)
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(getGifOrDefaultDrawable(start), getGifOrDefaultDrawable(top), getGifOrDefaultDrawable(end), getGifOrDefaultDrawable(bottom));
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        setCompoundDrawablesWithIntrinsicBounds(getGifOrDefaultDrawable(left), getGifOrDefaultDrawable(top), getGifOrDefaultDrawable(right), getGifOrDefaultDrawable(bottom));
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hideCompoundDrawables(getCompoundDrawables());
        if (Build.VERSION.SDK_INT >= 17) {
            hideCompoundDrawables(getCompoundDrawablesRelative());
        }
    }

    private void hideCompoundDrawables(Drawable[] drawables) {
        for (Drawable d : drawables) {
            if (d != null) {
                d.setVisible(false, false);
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        Drawable[] savedDrawables = new Drawable[7];
        if (this.mFreezesAnimation) {
            Drawable[] compoundDrawables = getCompoundDrawables();
            System.arraycopy(compoundDrawables, 0, savedDrawables, 0, compoundDrawables.length);
            if (Build.VERSION.SDK_INT >= 17) {
                Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
                savedDrawables[4] = compoundDrawablesRelative[0];
                savedDrawables[5] = compoundDrawablesRelative[2];
            }
            savedDrawables[6] = getBackground();
        }
        return new GifViewSavedState(super.onSaveInstanceState(), savedDrawables);
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof GifViewSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        GifViewSavedState ss = (GifViewSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        Drawable[] compoundDrawables = getCompoundDrawables();
        ss.restoreState(compoundDrawables[0], 0);
        ss.restoreState(compoundDrawables[1], 1);
        ss.restoreState(compoundDrawables[2], 2);
        ss.restoreState(compoundDrawables[3], 3);
        if (Build.VERSION.SDK_INT >= 17) {
            Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
            ss.restoreState(compoundDrawablesRelative[0], 4);
            ss.restoreState(compoundDrawablesRelative[2], 5);
        }
        ss.restoreState(getBackground(), 6);
    }

    public void setFreezesAnimation(boolean freezesAnimation) {
        this.mFreezesAnimation = freezesAnimation;
    }
}
