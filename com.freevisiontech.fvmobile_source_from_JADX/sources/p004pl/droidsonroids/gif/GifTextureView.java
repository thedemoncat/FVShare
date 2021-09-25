package p004pl.droidsonroids.gif;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;
import java.io.IOException;
import java.lang.ref.WeakReference;
import p004pl.droidsonroids.gif.InputSource;
import p004pl.droidsonroids.gif.annotations.Beta;

@RequiresApi(14)
/* renamed from: pl.droidsonroids.gif.GifTextureView */
public class GifTextureView extends TextureView {
    private static final ImageView.ScaleType[] sScaleTypeArray = {ImageView.ScaleType.MATRIX, ImageView.ScaleType.FIT_XY, ImageView.ScaleType.FIT_START, ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.FIT_END, ImageView.ScaleType.CENTER, ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_INSIDE};
    private boolean mFreezesAnimation;
    /* access modifiers changed from: private */
    public InputSource mInputSource;
    private RenderThread mRenderThread;
    private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;
    /* access modifiers changed from: private */
    public float mSpeedFactor = 1.0f;
    private final Matrix mTransform = new Matrix();

    @Beta
    /* renamed from: pl.droidsonroids.gif.GifTextureView$PlaceholderDrawListener */
    public interface PlaceholderDrawListener {
        void onDrawPlaceholder(Canvas canvas);
    }

    public GifTextureView(Context context) {
        super(context);
        init((AttributeSet) null, 0, 0);
    }

    public GifTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public GifTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(21)
    public GifTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs != null) {
            int scaleTypeIndex = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "scaleType", -1);
            if (scaleTypeIndex >= 0 && scaleTypeIndex < sScaleTypeArray.length) {
                this.mScaleType = sScaleTypeArray[scaleTypeIndex];
            }
            TypedArray textureViewAttributes = getContext().obtainStyledAttributes(attrs, C1685R.styleable.GifTextureView, defStyleAttr, defStyleRes);
            this.mInputSource = findSource(textureViewAttributes);
            super.setOpaque(textureViewAttributes.getBoolean(C1685R.styleable.GifTextureView_isOpaque, false));
            textureViewAttributes.recycle();
            this.mFreezesAnimation = GifViewUtils.isFreezingAnimation(this, attrs, defStyleAttr, defStyleRes);
        } else {
            super.setOpaque(false);
        }
        if (!isInEditMode()) {
            this.mRenderThread = new RenderThread(this);
            if (this.mInputSource != null) {
                this.mRenderThread.start();
            }
        }
    }

    public void setSurfaceTextureListener(TextureView.SurfaceTextureListener listener) {
        throw new UnsupportedOperationException("Changing SurfaceTextureListener is not supported");
    }

    public TextureView.SurfaceTextureListener getSurfaceTextureListener() {
        return null;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        throw new UnsupportedOperationException("Changing SurfaceTexture is not supported");
    }

    private static InputSource findSource(TypedArray textureViewAttributes) {
        TypedValue value = new TypedValue();
        if (!textureViewAttributes.getValue(C1685R.styleable.GifTextureView_gifSource, value)) {
            return null;
        }
        if (value.resourceId != 0) {
            String resourceTypeName = textureViewAttributes.getResources().getResourceTypeName(value.resourceId);
            if (GifViewUtils.SUPPORTED_RESOURCE_TYPE_NAMES.contains(resourceTypeName)) {
                return new InputSource.ResourcesSource(textureViewAttributes.getResources(), value.resourceId);
            }
            if (!"string".equals(resourceTypeName)) {
                throw new IllegalArgumentException("Expected string, drawable, mipmap or raw resource type. '" + resourceTypeName + "' is not supported");
            }
        }
        return new InputSource.AssetSource(textureViewAttributes.getResources().getAssets(), value.string.toString());
    }

    /* renamed from: pl.droidsonroids.gif.GifTextureView$RenderThread */
    private static class RenderThread extends Thread implements TextureView.SurfaceTextureListener {
        final ConditionVariable isSurfaceValid = new ConditionVariable();
        /* access modifiers changed from: private */
        public GifInfoHandle mGifInfoHandle = new GifInfoHandle();
        private final WeakReference<GifTextureView> mGifTextureViewReference;
        /* access modifiers changed from: private */
        public IOException mIOException;
        long[] mSavedState;

        RenderThread(GifTextureView gifTextureView) {
            super("GifRenderThread");
            this.mGifTextureViewReference = new WeakReference<>(gifTextureView);
        }

        public void run() {
            try {
                GifTextureView gifTextureView = (GifTextureView) this.mGifTextureViewReference.get();
                if (gifTextureView != null) {
                    this.mGifInfoHandle = gifTextureView.mInputSource.open();
                    this.mGifInfoHandle.setOptions(1, gifTextureView.isOpaque());
                    final GifTextureView gifTextureView2 = (GifTextureView) this.mGifTextureViewReference.get();
                    if (gifTextureView2 == null) {
                        this.mGifInfoHandle.recycle();
                        return;
                    }
                    gifTextureView2.setSuperSurfaceTextureListener(this);
                    boolean isSurfaceAvailable = gifTextureView2.isAvailable();
                    this.isSurfaceValid.set(isSurfaceAvailable);
                    if (isSurfaceAvailable) {
                        gifTextureView2.post(new Runnable() {
                            public void run() {
                                gifTextureView2.updateTextureViewSize(RenderThread.this.mGifInfoHandle);
                            }
                        });
                    }
                    this.mGifInfoHandle.setSpeedFactor(gifTextureView2.mSpeedFactor);
                    while (!isInterrupted()) {
                        try {
                            this.isSurfaceValid.block();
                            SurfaceTexture surfaceTexture = gifTextureView2.getSurfaceTexture();
                            if (surfaceTexture != null) {
                                Surface surface = new Surface(surfaceTexture);
                                try {
                                    this.mGifInfoHandle.bindSurface(surface, this.mSavedState);
                                } finally {
                                    surface.release();
                                    surfaceTexture.release();
                                }
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    this.mGifInfoHandle.recycle();
                    this.mGifInfoHandle = new GifInfoHandle();
                }
            } catch (IOException ex) {
                this.mIOException = ex;
            }
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            GifTextureView gifTextureView = (GifTextureView) this.mGifTextureViewReference.get();
            if (gifTextureView != null) {
                gifTextureView.updateTextureViewSize(this.mGifInfoHandle);
            }
            this.isSurfaceValid.open();
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            this.isSurfaceValid.close();
            this.mGifInfoHandle.postUnbindSurface();
            return false;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }

        /* access modifiers changed from: package-private */
        public void dispose(@NonNull GifTextureView gifTextureView, @Nullable PlaceholderDrawListener drawer) {
            this.isSurfaceValid.close();
            gifTextureView.setSuperSurfaceTextureListener(drawer != null ? new PlaceholderDrawingSurfaceTextureListener(drawer) : null);
            this.mGifInfoHandle.postUnbindSurface();
            interrupt();
        }
    }

    /* access modifiers changed from: private */
    public void setSuperSurfaceTextureListener(TextureView.SurfaceTextureListener listener) {
        super.setSurfaceTextureListener(listener);
    }

    public void setOpaque(boolean opaque) {
        if (opaque != isOpaque()) {
            super.setOpaque(opaque);
            setInputSource(this.mInputSource);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mRenderThread.dispose(this, (PlaceholderDrawListener) null);
        super.onDetachedFromWindow();
        SurfaceTexture surfaceTexture = getSurfaceTexture();
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
    }

    public synchronized void setInputSource(@Nullable InputSource inputSource) {
        setInputSource(inputSource, (PlaceholderDrawListener) null);
    }

    @Beta
    public synchronized void setInputSource(@Nullable InputSource inputSource, @Nullable PlaceholderDrawListener placeholderDrawListener) {
        this.mRenderThread.dispose(this, placeholderDrawListener);
        this.mInputSource = inputSource;
        this.mRenderThread = new RenderThread(this);
        if (inputSource != null) {
            this.mRenderThread.start();
        }
    }

    public void setSpeed(@FloatRange(from = 0.0d, fromInclusive = false) float factor) {
        this.mSpeedFactor = factor;
        this.mRenderThread.mGifInfoHandle.setSpeedFactor(factor);
    }

    @Nullable
    public IOException getIOException() {
        if (this.mRenderThread.mIOException != null) {
            return this.mRenderThread.mIOException;
        }
        return GifIOException.fromCode(this.mRenderThread.mGifInfoHandle.getNativeErrorCode());
    }

    public void setScaleType(@NonNull ImageView.ScaleType scaleType) {
        this.mScaleType = scaleType;
        updateTextureViewSize(this.mRenderThread.mGifInfoHandle);
    }

    public ImageView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    /* access modifiers changed from: private */
    public void updateTextureViewSize(GifInfoHandle gifInfoHandle) {
        float scaleRef;
        Matrix transform = new Matrix();
        float viewWidth = (float) getWidth();
        float viewHeight = (float) getHeight();
        float scaleX = ((float) gifInfoHandle.getWidth()) / viewWidth;
        float scaleY = ((float) gifInfoHandle.getHeight()) / viewHeight;
        RectF src = new RectF(0.0f, 0.0f, (float) gifInfoHandle.getWidth(), (float) gifInfoHandle.getHeight());
        RectF dst = new RectF(0.0f, 0.0f, viewWidth, viewHeight);
        switch (C06881.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
            case 1:
                transform.setScale(scaleX, scaleY, viewWidth / 2.0f, viewHeight / 2.0f);
                break;
            case 2:
                float scaleRef2 = 1.0f / Math.min(scaleX, scaleY);
                transform.setScale(scaleRef2 * scaleX, scaleRef2 * scaleY, viewWidth / 2.0f, viewHeight / 2.0f);
                break;
            case 3:
                if (((float) gifInfoHandle.getWidth()) > viewWidth || ((float) gifInfoHandle.getHeight()) > viewHeight) {
                    scaleRef = Math.min(1.0f / scaleX, 1.0f / scaleY);
                } else {
                    scaleRef = 1.0f;
                }
                transform.setScale(scaleRef * scaleX, scaleRef * scaleY, viewWidth / 2.0f, viewHeight / 2.0f);
                break;
            case 4:
                transform.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
                transform.preScale(scaleX, scaleY);
                break;
            case 5:
                transform.setRectToRect(src, dst, Matrix.ScaleToFit.END);
                transform.preScale(scaleX, scaleY);
                break;
            case 6:
                transform.setRectToRect(src, dst, Matrix.ScaleToFit.START);
                transform.preScale(scaleX, scaleY);
                break;
            case 7:
                return;
            case 8:
                transform.set(this.mTransform);
                transform.preScale(scaleX, scaleY);
                break;
        }
        super.setTransform(transform);
    }

    /* renamed from: pl.droidsonroids.gif.GifTextureView$1 */
    static /* synthetic */ class C06881 {
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
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_END.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_START.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public void setImageMatrix(Matrix matrix) {
        setTransform(matrix);
    }

    public void setTransform(Matrix transform) {
        this.mTransform.set(transform);
        updateTextureViewSize(this.mRenderThread.mGifInfoHandle);
    }

    public Matrix getTransform(Matrix transform) {
        if (transform == null) {
            transform = new Matrix();
        }
        transform.set(this.mTransform);
        return transform;
    }

    public Parcelable onSaveInstanceState() {
        this.mRenderThread.mSavedState = this.mRenderThread.mGifInfoHandle.getSavedState();
        return new GifViewSavedState(super.onSaveInstanceState(), this.mFreezesAnimation ? this.mRenderThread.mSavedState : null);
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof GifViewSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        GifViewSavedState ss = (GifViewSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mRenderThread.mSavedState = ss.mStates[0];
    }

    public void setFreezesAnimation(boolean freezesAnimation) {
        this.mFreezesAnimation = freezesAnimation;
    }
}
