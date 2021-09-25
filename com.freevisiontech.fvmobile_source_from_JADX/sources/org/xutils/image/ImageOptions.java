package org.xutils.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import java.lang.reflect.Field;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

public class ImageOptions {
    public static final ImageOptions DEFAULT = new ImageOptions();
    /* access modifiers changed from: private */
    public Animation animation = null;
    /* access modifiers changed from: private */
    public boolean autoRotate = false;
    /* access modifiers changed from: private */
    public boolean circular = false;
    private boolean compress = true;
    /* access modifiers changed from: private */
    public Bitmap.Config config = Bitmap.Config.RGB_565;
    /* access modifiers changed from: private */
    public boolean crop = false;
    /* access modifiers changed from: private */
    public boolean fadeIn = false;
    /* access modifiers changed from: private */
    public Drawable failureDrawable = null;
    /* access modifiers changed from: private */
    public int failureDrawableId = 0;
    /* access modifiers changed from: private */
    public boolean forceLoadingDrawable = true;
    /* access modifiers changed from: private */
    public int height = 0;
    /* access modifiers changed from: private */
    public boolean ignoreGif = true;
    /* access modifiers changed from: private */
    public ImageView.ScaleType imageScaleType = ImageView.ScaleType.CENTER_CROP;
    /* access modifiers changed from: private */
    public Drawable loadingDrawable = null;
    /* access modifiers changed from: private */
    public int loadingDrawableId = 0;
    private int maxHeight = 0;
    private int maxWidth = 0;
    /* access modifiers changed from: private */
    public ParamsBuilder paramsBuilder;
    /* access modifiers changed from: private */
    public ImageView.ScaleType placeholderScaleType = ImageView.ScaleType.CENTER_INSIDE;
    /* access modifiers changed from: private */
    public int radius = 0;
    /* access modifiers changed from: private */
    public boolean square = false;
    /* access modifiers changed from: private */
    public boolean useMemCache = true;
    /* access modifiers changed from: private */
    public int width = 0;

    public interface ParamsBuilder {
        RequestParams buildParams(RequestParams requestParams, ImageOptions imageOptions);
    }

    protected ImageOptions() {
    }

    /* access modifiers changed from: package-private */
    public final void optimizeMaxSize(ImageView view) {
        if (this.width <= 0 || this.height <= 0) {
            int screenWidth = DensityUtil.getScreenWidth();
            int screenHeight = DensityUtil.getScreenHeight();
            if (this.width < 0) {
                this.maxWidth = (screenWidth * 3) / 2;
                this.compress = false;
            }
            if (this.height < 0) {
                this.maxHeight = (screenHeight * 3) / 2;
                this.compress = false;
            }
            if (view != null || this.maxWidth > 0 || this.maxHeight > 0) {
                int tempWidth = this.maxWidth;
                int tempHeight = this.maxHeight;
                if (view != null) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    if (params != null) {
                        if (tempWidth <= 0) {
                            if (params.width > 0) {
                                tempWidth = params.width;
                                if (this.width <= 0) {
                                    this.width = tempWidth;
                                }
                            } else if (params.width != -2) {
                                tempWidth = view.getWidth();
                            }
                        }
                        if (tempHeight <= 0) {
                            if (params.height > 0) {
                                tempHeight = params.height;
                                if (this.height <= 0) {
                                    this.height = tempHeight;
                                }
                            } else if (params.height != -2) {
                                tempHeight = view.getHeight();
                            }
                        }
                    }
                    if (tempWidth <= 0) {
                        tempWidth = getImageViewFieldValue(view, "mMaxWidth");
                    }
                    if (tempHeight <= 0) {
                        tempHeight = getImageViewFieldValue(view, "mMaxHeight");
                    }
                }
                if (tempWidth <= 0) {
                    tempWidth = screenWidth;
                }
                if (tempHeight <= 0) {
                    tempHeight = screenHeight;
                }
                this.maxWidth = tempWidth;
                this.maxHeight = tempHeight;
                return;
            }
            this.maxWidth = screenWidth;
            this.maxHeight = screenHeight;
            return;
        }
        this.maxWidth = this.width;
        this.maxHeight = this.height;
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public int getMaxHeight() {
        return this.maxHeight;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isCrop() {
        return this.crop;
    }

    public int getRadius() {
        return this.radius;
    }

    public boolean isSquare() {
        return this.square;
    }

    public boolean isCircular() {
        return this.circular;
    }

    public boolean isIgnoreGif() {
        return this.ignoreGif;
    }

    public boolean isAutoRotate() {
        return this.autoRotate;
    }

    public boolean isCompress() {
        return this.compress;
    }

    public Bitmap.Config getConfig() {
        return this.config;
    }

    public Drawable getLoadingDrawable(ImageView view) {
        if (this.loadingDrawable == null && this.loadingDrawableId > 0 && view != null) {
            try {
                this.loadingDrawable = view.getResources().getDrawable(this.loadingDrawableId);
            } catch (Throwable ex) {
                LogUtil.m1565e(ex.getMessage(), ex);
            }
        }
        return this.loadingDrawable;
    }

    public Drawable getFailureDrawable(ImageView view) {
        if (this.failureDrawable == null && this.failureDrawableId > 0 && view != null) {
            try {
                this.failureDrawable = view.getResources().getDrawable(this.failureDrawableId);
            } catch (Throwable ex) {
                LogUtil.m1565e(ex.getMessage(), ex);
            }
        }
        return this.failureDrawable;
    }

    public boolean isFadeIn() {
        return this.fadeIn;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public ImageView.ScaleType getPlaceholderScaleType() {
        return this.placeholderScaleType;
    }

    public ImageView.ScaleType getImageScaleType() {
        return this.imageScaleType;
    }

    public boolean isForceLoadingDrawable() {
        return this.forceLoadingDrawable;
    }

    public boolean isUseMemCache() {
        return this.useMemCache;
    }

    public ParamsBuilder getParamsBuilder() {
        return this.paramsBuilder;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageOptions options = (ImageOptions) o;
        if (this.maxWidth != options.maxWidth || this.maxHeight != options.maxHeight || this.width != options.width || this.height != options.height || this.crop != options.crop || this.radius != options.radius || this.square != options.square || this.circular != options.circular || this.autoRotate != options.autoRotate || this.compress != options.compress) {
            return false;
        }
        if (this.config != options.config) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 1;
        int i5 = 0;
        int i6 = ((((((((((this.maxWidth * 31) + this.maxHeight) * 31) + this.width) * 31) + this.height) * 31) + (this.crop ? 1 : 0)) * 31) + this.radius) * 31;
        if (this.square) {
            i = 1;
        } else {
            i = 0;
        }
        int i7 = (i6 + i) * 31;
        if (this.circular) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i8 = (i7 + i2) * 31;
        if (this.autoRotate) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i9 = (i8 + i3) * 31;
        if (!this.compress) {
            i4 = 0;
        }
        int i10 = (i9 + i4) * 31;
        if (this.config != null) {
            i5 = this.config.hashCode();
        }
        return i10 + i5;
    }

    public String toString() {
        int i;
        int i2;
        int i3 = 1;
        StringBuilder sb = new StringBuilder("_");
        sb.append(this.maxWidth).append("_");
        sb.append(this.maxHeight).append("_");
        sb.append(this.width).append("_");
        sb.append(this.height).append("_");
        sb.append(this.radius).append("_");
        sb.append(this.config).append("_");
        if (this.crop) {
            i = 1;
        } else {
            i = 0;
        }
        sb.append(i).append(this.square ? 1 : 0).append(this.circular ? 1 : 0);
        if (this.autoRotate) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        StringBuilder append = sb.append(i2);
        if (!this.compress) {
            i3 = 0;
        }
        append.append(i3);
        return sb.toString();
    }

    private static int getImageViewFieldValue(ImageView view, String fieldName) {
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = ((Integer) field.get(view)).intValue();
            if (fieldValue <= 0 || fieldValue >= Integer.MAX_VALUE) {
                return 0;
            }
            return fieldValue;
        } catch (Throwable th) {
            return 0;
        }
    }

    public static class Builder {
        protected ImageOptions options;

        public Builder() {
            newImageOptions();
        }

        /* access modifiers changed from: protected */
        public void newImageOptions() {
            this.options = new ImageOptions();
        }

        public ImageOptions build() {
            return this.options;
        }

        public Builder setSize(int width, int height) {
            int unused = this.options.width = width;
            int unused2 = this.options.height = height;
            return this;
        }

        public Builder setCrop(boolean crop) {
            boolean unused = this.options.crop = crop;
            return this;
        }

        public Builder setRadius(int radius) {
            int unused = this.options.radius = radius;
            return this;
        }

        public Builder setSquare(boolean square) {
            boolean unused = this.options.square = square;
            return this;
        }

        public Builder setCircular(boolean circular) {
            boolean unused = this.options.circular = circular;
            return this;
        }

        public Builder setAutoRotate(boolean autoRotate) {
            boolean unused = this.options.autoRotate = autoRotate;
            return this;
        }

        public Builder setConfig(Bitmap.Config config) {
            Bitmap.Config unused = this.options.config = config;
            return this;
        }

        public Builder setIgnoreGif(boolean ignoreGif) {
            boolean unused = this.options.ignoreGif = ignoreGif;
            return this;
        }

        public Builder setLoadingDrawableId(int loadingDrawableId) {
            int unused = this.options.loadingDrawableId = loadingDrawableId;
            return this;
        }

        public Builder setLoadingDrawable(Drawable loadingDrawable) {
            Drawable unused = this.options.loadingDrawable = loadingDrawable;
            return this;
        }

        public Builder setFailureDrawableId(int failureDrawableId) {
            int unused = this.options.failureDrawableId = failureDrawableId;
            return this;
        }

        public Builder setFailureDrawable(Drawable failureDrawable) {
            Drawable unused = this.options.failureDrawable = failureDrawable;
            return this;
        }

        public Builder setFadeIn(boolean fadeIn) {
            boolean unused = this.options.fadeIn = fadeIn;
            return this;
        }

        public Builder setAnimation(Animation animation) {
            Animation unused = this.options.animation = animation;
            return this;
        }

        public Builder setPlaceholderScaleType(ImageView.ScaleType placeholderScaleType) {
            ImageView.ScaleType unused = this.options.placeholderScaleType = placeholderScaleType;
            return this;
        }

        public Builder setImageScaleType(ImageView.ScaleType imageScaleType) {
            ImageView.ScaleType unused = this.options.imageScaleType = imageScaleType;
            return this;
        }

        public Builder setForceLoadingDrawable(boolean forceLoadingDrawable) {
            boolean unused = this.options.forceLoadingDrawable = forceLoadingDrawable;
            return this;
        }

        public Builder setUseMemCache(boolean useMemCache) {
            boolean unused = this.options.useMemCache = useMemCache;
            return this;
        }

        public Builder setParamsBuilder(ParamsBuilder paramsBuilder) {
            ParamsBuilder unused = this.options.paramsBuilder = paramsBuilder;
            return this;
        }
    }
}
