package com.makeramen.roundedimageview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final float DEFAULT_BORDER_WIDTH = 0.0f;
    public static final float DEFAULT_RADIUS = 0.0f;
    public static final Shader.TileMode DEFAULT_TILE_MODE = Shader.TileMode.CLAMP;
    private static final ImageView.ScaleType[] SCALE_TYPES = {ImageView.ScaleType.MATRIX, ImageView.ScaleType.FIT_XY, ImageView.ScaleType.FIT_START, ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.FIT_END, ImageView.ScaleType.CENTER, ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_INSIDE};
    public static final String TAG = "RoundedImageView";
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_UNDEFINED = -2;
    private Drawable mBackgroundDrawable;
    private int mBackgroundResource;
    private ColorStateList mBorderColor;
    private float mBorderWidth;
    private ColorFilter mColorFilter;
    private boolean mColorMod;
    private final float[] mCornerRadii;
    private Drawable mDrawable;
    private boolean mHasColorFilter;
    private boolean mIsOval;
    private boolean mMutateBackground;
    private int mResource;
    private ImageView.ScaleType mScaleType;
    private Shader.TileMode mTileModeX;
    private Shader.TileMode mTileModeY;

    static {
        boolean z;
        if (!RoundedImageView.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    public RoundedImageView(Context context) {
        super(context);
        this.mCornerRadii = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        this.mBorderColor = ColorStateList.valueOf(-16777216);
        this.mBorderWidth = 0.0f;
        this.mColorFilter = null;
        this.mColorMod = false;
        this.mHasColorFilter = false;
        this.mIsOval = false;
        this.mMutateBackground = false;
        this.mTileModeX = DEFAULT_TILE_MODE;
        this.mTileModeY = DEFAULT_TILE_MODE;
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCornerRadii = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        this.mBorderColor = ColorStateList.valueOf(-16777216);
        this.mBorderWidth = 0.0f;
        this.mColorFilter = null;
        this.mColorMod = false;
        this.mHasColorFilter = false;
        this.mIsOval = false;
        this.mMutateBackground = false;
        this.mTileModeX = DEFAULT_TILE_MODE;
        this.mTileModeY = DEFAULT_TILE_MODE;
        TypedArray a = context.obtainStyledAttributes(attrs, C1644R.styleable.RoundedImageView, defStyle, 0);
        int index = a.getInt(C1644R.styleable.RoundedImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(SCALE_TYPES[index]);
        } else {
            setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        float cornerRadiusOverride = (float) a.getDimensionPixelSize(C1644R.styleable.RoundedImageView_riv_corner_radius, -1);
        this.mCornerRadii[0] = (float) a.getDimensionPixelSize(C1644R.styleable.RoundedImageView_riv_corner_radius_top_left, -1);
        this.mCornerRadii[1] = (float) a.getDimensionPixelSize(C1644R.styleable.RoundedImageView_riv_corner_radius_top_right, -1);
        this.mCornerRadii[2] = (float) a.getDimensionPixelSize(C1644R.styleable.RoundedImageView_riv_corner_radius_bottom_right, -1);
        this.mCornerRadii[3] = (float) a.getDimensionPixelSize(C1644R.styleable.RoundedImageView_riv_corner_radius_bottom_left, -1);
        boolean any = false;
        int len = this.mCornerRadii.length;
        for (int i = 0; i < len; i++) {
            if (this.mCornerRadii[i] < 0.0f) {
                this.mCornerRadii[i] = 0.0f;
            } else {
                any = true;
            }
        }
        if (!any) {
            cornerRadiusOverride = cornerRadiusOverride < 0.0f ? 0.0f : cornerRadiusOverride;
            int len2 = this.mCornerRadii.length;
            for (int i2 = 0; i2 < len2; i2++) {
                this.mCornerRadii[i2] = cornerRadiusOverride;
            }
        }
        this.mBorderWidth = (float) a.getDimensionPixelSize(C1644R.styleable.RoundedImageView_riv_border_width, -1);
        if (this.mBorderWidth < 0.0f) {
            this.mBorderWidth = 0.0f;
        }
        this.mBorderColor = a.getColorStateList(C1644R.styleable.RoundedImageView_riv_border_color);
        if (this.mBorderColor == null) {
            this.mBorderColor = ColorStateList.valueOf(-16777216);
        }
        this.mMutateBackground = a.getBoolean(C1644R.styleable.RoundedImageView_riv_mutate_background, false);
        this.mIsOval = a.getBoolean(C1644R.styleable.RoundedImageView_riv_oval, false);
        int tileMode = a.getInt(C1644R.styleable.RoundedImageView_riv_tile_mode, -2);
        if (tileMode != -2) {
            setTileModeX(parseTileMode(tileMode));
            setTileModeY(parseTileMode(tileMode));
        }
        int tileModeX = a.getInt(C1644R.styleable.RoundedImageView_riv_tile_mode_x, -2);
        if (tileModeX != -2) {
            setTileModeX(parseTileMode(tileModeX));
        }
        int tileModeY = a.getInt(C1644R.styleable.RoundedImageView_riv_tile_mode_y, -2);
        if (tileModeY != -2) {
            setTileModeY(parseTileMode(tileModeY));
        }
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(true);
        if (this.mMutateBackground) {
            super.setBackgroundDrawable(this.mBackgroundDrawable);
        }
        a.recycle();
    }

    private static Shader.TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case 0:
                return Shader.TileMode.CLAMP;
            case 1:
                return Shader.TileMode.REPEAT;
            case 2:
                return Shader.TileMode.MIRROR;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    public ImageView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (!$assertionsDisabled && scaleType == null) {
            throw new AssertionError();
        } else if (this.mScaleType != scaleType) {
            this.mScaleType = scaleType;
            switch (C20321.$SwitchMap$android$widget$ImageView$ScaleType[scaleType.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    super.setScaleType(ImageView.ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    /* renamed from: com.makeramen.roundedimageview.RoundedImageView$1 */
    static /* synthetic */ class C20321 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ImageView.ScaleType.values().length];

        static {
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_START.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_END.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    public void setImageDrawable(Drawable drawable) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageBitmap(Bitmap bm) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromBitmap(bm);
        updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageResource(@DrawableRes int resId) {
        if (this.mResource != resId) {
            this.mResource = resId;
            this.mDrawable = resolveResource();
            updateDrawableAttrs();
            super.setImageDrawable(this.mDrawable);
        }
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    private Drawable resolveResource() {
        Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }
        Drawable d = null;
        if (this.mResource != 0) {
            try {
                d = rsrc.getDrawable(this.mResource);
            } catch (Exception e) {
                Log.w(TAG, "Unable to find resource: " + this.mResource, e);
                this.mResource = 0;
            }
        }
        return RoundedDrawable.fromDrawable(d);
    }

    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    public void setBackgroundResource(@DrawableRes int resId) {
        if (this.mBackgroundResource != resId) {
            this.mBackgroundResource = resId;
            this.mBackgroundDrawable = resolveBackgroundResource();
            setBackgroundDrawable(this.mBackgroundDrawable);
        }
    }

    public void setBackgroundColor(int color) {
        this.mBackgroundDrawable = new ColorDrawable(color);
        setBackgroundDrawable(this.mBackgroundDrawable);
    }

    private Drawable resolveBackgroundResource() {
        Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }
        Drawable d = null;
        if (this.mBackgroundResource != 0) {
            try {
                d = rsrc.getDrawable(this.mBackgroundResource);
            } catch (Exception e) {
                Log.w(TAG, "Unable to find resource: " + this.mBackgroundResource, e);
                this.mBackgroundResource = 0;
            }
        }
        return RoundedDrawable.fromDrawable(d);
    }

    private void updateDrawableAttrs() {
        updateAttrs(this.mDrawable, this.mScaleType);
    }

    private void updateBackgroundDrawableAttrs(boolean convert) {
        if (this.mMutateBackground) {
            if (convert) {
                this.mBackgroundDrawable = RoundedDrawable.fromDrawable(this.mBackgroundDrawable);
            }
            updateAttrs(this.mBackgroundDrawable, ImageView.ScaleType.FIT_XY);
        }
    }

    public void setColorFilter(ColorFilter cf) {
        if (this.mColorFilter != cf) {
            this.mColorFilter = cf;
            this.mHasColorFilter = true;
            this.mColorMod = true;
            applyColorMod();
            invalidate();
        }
    }

    private void applyColorMod() {
        if (this.mDrawable != null && this.mColorMod) {
            this.mDrawable = this.mDrawable.mutate();
            if (this.mHasColorFilter) {
                this.mDrawable.setColorFilter(this.mColorFilter);
            }
        }
    }

    private void updateAttrs(Drawable drawable, ImageView.ScaleType scaleType) {
        if (drawable != null) {
            if (drawable instanceof RoundedDrawable) {
                ((RoundedDrawable) drawable).setScaleType(scaleType).setBorderWidth(this.mBorderWidth).setBorderColor(this.mBorderColor).setOval(this.mIsOval).setTileModeX(this.mTileModeX).setTileModeY(this.mTileModeY);
                if (this.mCornerRadii != null) {
                    ((RoundedDrawable) drawable).setCornerRadius(this.mCornerRadii[0], this.mCornerRadii[1], this.mCornerRadii[2], this.mCornerRadii[3]);
                }
                applyColorMod();
            } else if (drawable instanceof LayerDrawable) {
                LayerDrawable ld = (LayerDrawable) drawable;
                int layers = ld.getNumberOfLayers();
                for (int i = 0; i < layers; i++) {
                    updateAttrs(ld.getDrawable(i), scaleType);
                }
            }
        }
    }

    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        this.mBackgroundDrawable = background;
        updateBackgroundDrawableAttrs(true);
        super.setBackgroundDrawable(this.mBackgroundDrawable);
    }

    public float getCornerRadius() {
        return getMaxCornerRadius();
    }

    public float getMaxCornerRadius() {
        float maxRadius = 0.0f;
        for (float r : this.mCornerRadii) {
            maxRadius = Math.max(r, maxRadius);
        }
        return maxRadius;
    }

    public float getCornerRadius(int corner) {
        return this.mCornerRadii[corner];
    }

    public void setCornerRadiusDimen(@DimenRes int resId) {
        float radius = getResources().getDimension(resId);
        setCornerRadius(radius, radius, radius, radius);
    }

    public void setCornerRadiusDimen(int corner, @DimenRes int resId) {
        setCornerRadius(corner, (float) getResources().getDimensionPixelSize(resId));
    }

    public void setCornerRadius(float radius) {
        setCornerRadius(radius, radius, radius, radius);
    }

    public void setCornerRadius(int corner, float radius) {
        if (this.mCornerRadii[corner] != radius) {
            this.mCornerRadii[corner] = radius;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public void setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (this.mCornerRadii[0] != topLeft || this.mCornerRadii[1] != topRight || this.mCornerRadii[2] != bottomRight || this.mCornerRadii[3] != bottomLeft) {
            this.mCornerRadii[0] = topLeft;
            this.mCornerRadii[1] = topRight;
            this.mCornerRadii[3] = bottomLeft;
            this.mCornerRadii[2] = bottomRight;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    public void setBorderWidth(@DimenRes int resId) {
        setBorderWidth(getResources().getDimension(resId));
    }

    public void setBorderWidth(float width) {
        if (this.mBorderWidth != width) {
            this.mBorderWidth = width;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    @ColorInt
    public int getBorderColor() {
        return this.mBorderColor.getDefaultColor();
    }

    public void setBorderColor(@ColorInt int color) {
        setBorderColor(ColorStateList.valueOf(color));
    }

    public ColorStateList getBorderColors() {
        return this.mBorderColor;
    }

    public void setBorderColor(ColorStateList colors) {
        if (!this.mBorderColor.equals(colors)) {
            if (colors == null) {
                colors = ColorStateList.valueOf(-16777216);
            }
            this.mBorderColor = colors;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            if (this.mBorderWidth > 0.0f) {
                invalidate();
            }
        }
    }

    public boolean isOval() {
        return this.mIsOval;
    }

    public void setOval(boolean oval) {
        this.mIsOval = oval;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public Shader.TileMode getTileModeX() {
        return this.mTileModeX;
    }

    public void setTileModeX(Shader.TileMode tileModeX) {
        if (this.mTileModeX != tileModeX) {
            this.mTileModeX = tileModeX;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public Shader.TileMode getTileModeY() {
        return this.mTileModeY;
    }

    public void setTileModeY(Shader.TileMode tileModeY) {
        if (this.mTileModeY != tileModeY) {
            this.mTileModeY = tileModeY;
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public boolean mutatesBackground() {
        return this.mMutateBackground;
    }

    public void mutateBackground(boolean mutate) {
        if (this.mMutateBackground != mutate) {
            this.mMutateBackground = mutate;
            updateBackgroundDrawableAttrs(true);
            invalidate();
        }
    }
}
