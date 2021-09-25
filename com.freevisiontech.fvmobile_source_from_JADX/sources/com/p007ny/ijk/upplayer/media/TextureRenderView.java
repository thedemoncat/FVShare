package com.p007ny.ijk.upplayer.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.p007ny.ijk.upplayer.media.IRenderView;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import p012tv.danmaku.ijk.media.player.IMediaPlayer;
import p012tv.danmaku.ijk.media.player.ISurfaceTextureHolder;
import p012tv.danmaku.ijk.media.player.ISurfaceTextureHost;

@TargetApi(14)
/* renamed from: com.ny.ijk.upplayer.media.TextureRenderView */
public class TextureRenderView extends TextureView implements IRenderView {
    private static final String TAG = "TextureRenderView";
    private MeasureHelper mMeasureHelper;
    /* access modifiers changed from: private */
    public SurfaceCallback mSurfaceCallback;

    public TextureRenderView(Context context) {
        super(context);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(21)
    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.mMeasureHelper = new MeasureHelper(this);
        this.mSurfaceCallback = new SurfaceCallback(this);
        setSurfaceTextureListener(this.mSurfaceCallback);
    }

    public View getView() {
        return this;
    }

    public boolean shouldWaitForResize() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mSurfaceCallback.willDetachFromWindow();
        super.onDetachedFromWindow();
        this.mSurfaceCallback.didDetachFromWindow();
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            requestLayout();
        }
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            this.mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    public void setVideoRotation(int degree) {
        this.mMeasureHelper.setVideoRotation(degree);
        setRotation((float) degree);
    }

    public void setAspectRatio(int aspectRatio) {
        this.mMeasureHelper.setAspectRatio(aspectRatio);
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(this.mMeasureHelper.getMeasuredWidth(), this.mMeasureHelper.getMeasuredHeight());
    }

    public IRenderView.ISurfaceHolder getSurfaceHolder() {
        return new InternalSurfaceHolder(this, this.mSurfaceCallback.mSurfaceTexture, this.mSurfaceCallback);
    }

    /* renamed from: com.ny.ijk.upplayer.media.TextureRenderView$InternalSurfaceHolder */
    private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder {
        private SurfaceTexture mSurfaceTexture;
        private ISurfaceTextureHost mSurfaceTextureHost;
        private TextureRenderView mTextureView;

        public InternalSurfaceHolder(@NonNull TextureRenderView textureView, @Nullable SurfaceTexture surfaceTexture, @NonNull ISurfaceTextureHost surfaceTextureHost) {
            this.mTextureView = textureView;
            this.mSurfaceTexture = surfaceTexture;
            this.mSurfaceTextureHost = surfaceTextureHost;
        }

        @TargetApi(16)
        public void bindToMediaPlayer(IMediaPlayer mp) {
            if (mp != null) {
                if (Build.VERSION.SDK_INT < 16 || !(mp instanceof ISurfaceTextureHolder)) {
                    mp.setSurface(openSurface());
                    return;
                }
                ISurfaceTextureHolder textureHolder = (ISurfaceTextureHolder) mp;
                this.mTextureView.mSurfaceCallback.setOwnSurfaceTexture(false);
                SurfaceTexture surfaceTexture = textureHolder.getSurfaceTexture();
                if (surfaceTexture != null) {
                    this.mTextureView.setSurfaceTexture(surfaceTexture);
                    return;
                }
                textureHolder.setSurfaceTexture(this.mSurfaceTexture);
                textureHolder.setSurfaceTextureHost(this.mTextureView.mSurfaceCallback);
            }
        }

        @NonNull
        public IRenderView getRenderView() {
            return this.mTextureView;
        }

        @Nullable
        public SurfaceHolder getSurfaceHolder() {
            return null;
        }

        @Nullable
        public SurfaceTexture getSurfaceTexture() {
            return this.mSurfaceTexture;
        }

        @Nullable
        public Surface openSurface() {
            if (this.mSurfaceTexture == null) {
                return null;
            }
            return new Surface(this.mSurfaceTexture);
        }
    }

    public void addRenderCallback(IRenderView.IRenderCallback callback) {
        this.mSurfaceCallback.addRenderCallback(callback);
    }

    public void removeRenderCallback(IRenderView.IRenderCallback callback) {
        this.mSurfaceCallback.removeRenderCallback(callback);
    }

    /* renamed from: com.ny.ijk.upplayer.media.TextureRenderView$SurfaceCallback */
    private static final class SurfaceCallback implements TextureView.SurfaceTextureListener, ISurfaceTextureHost {
        private boolean mDidDetachFromWindow = false;
        private int mHeight;
        private boolean mIsFormatChanged;
        private boolean mOwnSurfaceTexture = true;
        private Map<IRenderView.IRenderCallback, Object> mRenderCallbackMap = new ConcurrentHashMap();
        /* access modifiers changed from: private */
        public SurfaceTexture mSurfaceTexture;
        private WeakReference<TextureRenderView> mWeakRenderView;
        private int mWidth;
        private boolean mWillDetachFromWindow = false;

        public SurfaceCallback(@NonNull TextureRenderView renderView) {
            this.mWeakRenderView = new WeakReference<>(renderView);
        }

        public void setOwnSurfaceTexture(boolean ownSurfaceTexture) {
            this.mOwnSurfaceTexture = ownSurfaceTexture;
        }

        public void addRenderCallback(@NonNull IRenderView.IRenderCallback callback) {
            this.mRenderCallbackMap.put(callback, callback);
            IRenderView.ISurfaceHolder surfaceHolder = null;
            if (this.mSurfaceTexture != null) {
                if (0 == 0) {
                    surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), this.mSurfaceTexture, this);
                }
                callback.onSurfaceCreated(surfaceHolder, this.mWidth, this.mHeight);
            }
            if (this.mIsFormatChanged) {
                if (surfaceHolder == null) {
                    surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), this.mSurfaceTexture, this);
                }
                callback.onSurfaceChanged(surfaceHolder, 0, this.mWidth, this.mHeight);
            }
        }

        public void removeRenderCallback(@NonNull IRenderView.IRenderCallback callback) {
            this.mRenderCallbackMap.remove(callback);
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            this.mSurfaceTexture = surface;
            this.mIsFormatChanged = false;
            this.mWidth = 0;
            this.mHeight = 0;
            IRenderView.ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), surface, this);
            for (IRenderView.IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0);
            }
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            this.mSurfaceTexture = surface;
            this.mIsFormatChanged = true;
            this.mWidth = width;
            this.mHeight = height;
            IRenderView.ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), surface, this);
            for (IRenderView.IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceChanged(surfaceHolder, 0, width, height);
            }
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            this.mSurfaceTexture = surface;
            this.mIsFormatChanged = false;
            this.mWidth = 0;
            this.mHeight = 0;
            IRenderView.ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((TextureRenderView) this.mWeakRenderView.get(), surface, this);
            for (IRenderView.IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceDestroyed(surfaceHolder);
            }
            Log.d(TextureRenderView.TAG, "onSurfaceTextureDestroyed: destroy: " + this.mOwnSurfaceTexture);
            return this.mOwnSurfaceTexture;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }

        public void releaseSurfaceTexture(SurfaceTexture surfaceTexture) {
            if (surfaceTexture == null) {
                Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: null");
            } else if (this.mDidDetachFromWindow) {
                if (surfaceTexture != this.mSurfaceTexture) {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: didDetachFromWindow(): release different SurfaceTexture");
                    surfaceTexture.release();
                } else if (!this.mOwnSurfaceTexture) {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: didDetachFromWindow(): release detached SurfaceTexture");
                    surfaceTexture.release();
                } else {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: didDetachFromWindow(): already released by TextureView");
                }
            } else if (this.mWillDetachFromWindow) {
                if (surfaceTexture != this.mSurfaceTexture) {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: willDetachFromWindow(): release different SurfaceTexture");
                    surfaceTexture.release();
                } else if (!this.mOwnSurfaceTexture) {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: willDetachFromWindow(): re-attach SurfaceTexture to TextureView");
                    setOwnSurfaceTexture(true);
                } else {
                    Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: willDetachFromWindow(): will released by TextureView");
                }
            } else if (surfaceTexture != this.mSurfaceTexture) {
                Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: alive: release different SurfaceTexture");
                surfaceTexture.release();
            } else if (!this.mOwnSurfaceTexture) {
                Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: alive: re-attach SurfaceTexture to TextureView");
                setOwnSurfaceTexture(true);
            } else {
                Log.d(TextureRenderView.TAG, "releaseSurfaceTexture: alive: will released by TextureView");
            }
        }

        public void willDetachFromWindow() {
            Log.d(TextureRenderView.TAG, "willDetachFromWindow()");
            this.mWillDetachFromWindow = true;
        }

        public void didDetachFromWindow() {
            Log.d(TextureRenderView.TAG, "didDetachFromWindow()");
            this.mDidDetachFromWindow = true;
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(TextureRenderView.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(TextureRenderView.class.getName());
    }
}
