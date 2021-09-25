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
import android.view.SurfaceView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.p007ny.ijk.upplayer.media.IRenderView;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import p012tv.danmaku.ijk.media.player.IMediaPlayer;
import p012tv.danmaku.ijk.media.player.ISurfaceTextureHolder;

/* renamed from: com.ny.ijk.upplayer.media.SurfaceRenderView */
public class SurfaceRenderView extends SurfaceView implements IRenderView {
    private MeasureHelper mMeasureHelper;
    private SurfaceCallback mSurfaceCallback;

    public SurfaceRenderView(Context context) {
        super(context);
        initView(context);
    }

    public SurfaceRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SurfaceRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(21)
    public SurfaceRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.mMeasureHelper = new MeasureHelper(this);
        this.mSurfaceCallback = new SurfaceCallback(this);
        getHolder().addCallback(this.mSurfaceCallback);
        getHolder().setType(0);
    }

    public View getView() {
        return this;
    }

    public boolean shouldWaitForResize() {
        return true;
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            getHolder().setFixedSize(videoWidth, videoHeight);
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
        Log.e("", "SurfaceView doesn't support rotation (" + degree + ")!\n");
    }

    public void setAspectRatio(int aspectRatio) {
        this.mMeasureHelper.setAspectRatio(aspectRatio);
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getWidth() != 0) {
            setMeasuredDimension(getWidth(), getHeight());
        } else {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        }
    }

    /* renamed from: com.ny.ijk.upplayer.media.SurfaceRenderView$InternalSurfaceHolder */
    private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder {
        private SurfaceHolder mSurfaceHolder;
        private SurfaceRenderView mSurfaceView;

        public InternalSurfaceHolder(@NonNull SurfaceRenderView surfaceView, @Nullable SurfaceHolder surfaceHolder) {
            this.mSurfaceView = surfaceView;
            this.mSurfaceHolder = surfaceHolder;
        }

        public void bindToMediaPlayer(IMediaPlayer mp) {
            if (mp != null) {
                if (Build.VERSION.SDK_INT >= 16 && (mp instanceof ISurfaceTextureHolder)) {
                    ((ISurfaceTextureHolder) mp).setSurfaceTexture((SurfaceTexture) null);
                }
                mp.setDisplay(this.mSurfaceHolder);
            }
        }

        @NonNull
        public IRenderView getRenderView() {
            return this.mSurfaceView;
        }

        @Nullable
        public SurfaceHolder getSurfaceHolder() {
            return this.mSurfaceHolder;
        }

        @Nullable
        public SurfaceTexture getSurfaceTexture() {
            return null;
        }

        @Nullable
        public Surface openSurface() {
            if (this.mSurfaceHolder == null) {
                return null;
            }
            return this.mSurfaceHolder.getSurface();
        }
    }

    public void addRenderCallback(IRenderView.IRenderCallback callback) {
        this.mSurfaceCallback.addRenderCallback(callback);
    }

    public void removeRenderCallback(IRenderView.IRenderCallback callback) {
        this.mSurfaceCallback.removeRenderCallback(callback);
    }

    /* renamed from: com.ny.ijk.upplayer.media.SurfaceRenderView$SurfaceCallback */
    private static final class SurfaceCallback implements SurfaceHolder.Callback {
        private int mFormat;
        private int mHeight;
        private boolean mIsFormatChanged;
        private Map<IRenderView.IRenderCallback, Object> mRenderCallbackMap = new ConcurrentHashMap();
        private SurfaceHolder mSurfaceHolder;
        private WeakReference<SurfaceRenderView> mWeakSurfaceView;
        private int mWidth;

        public SurfaceCallback(@NonNull SurfaceRenderView surfaceView) {
            this.mWeakSurfaceView = new WeakReference<>(surfaceView);
        }

        public void addRenderCallback(@NonNull IRenderView.IRenderCallback callback) {
            this.mRenderCallbackMap.put(callback, callback);
            IRenderView.ISurfaceHolder surfaceHolder = null;
            if (this.mSurfaceHolder != null) {
                if (0 == 0) {
                    surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
                }
                callback.onSurfaceCreated(surfaceHolder, this.mWidth, this.mHeight);
            }
            if (this.mIsFormatChanged) {
                if (surfaceHolder == null) {
                    surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
                }
                callback.onSurfaceChanged(surfaceHolder, this.mFormat, this.mWidth, this.mHeight);
            }
        }

        public void removeRenderCallback(@NonNull IRenderView.IRenderCallback callback) {
            this.mRenderCallbackMap.remove(callback);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            this.mSurfaceHolder = holder;
            this.mIsFormatChanged = false;
            this.mFormat = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            IRenderView.ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
            for (IRenderView.IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0);
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            this.mSurfaceHolder = null;
            this.mIsFormatChanged = false;
            this.mFormat = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            IRenderView.ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
            for (IRenderView.IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceDestroyed(surfaceHolder);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.mSurfaceHolder = holder;
            this.mIsFormatChanged = true;
            this.mFormat = format;
            this.mWidth = width;
            this.mHeight = height;
            IRenderView.ISurfaceHolder surfaceHolder = new InternalSurfaceHolder((SurfaceRenderView) this.mWeakSurfaceView.get(), this.mSurfaceHolder);
            for (IRenderView.IRenderCallback renderCallback : this.mRenderCallbackMap.keySet()) {
                renderCallback.onSurfaceChanged(surfaceHolder, format, width, height);
            }
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(SurfaceRenderView.class.getName());
    }

    @TargetApi(14)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (Build.VERSION.SDK_INT >= 14) {
            info.setClassName(SurfaceRenderView.class.getName());
        }
    }
}
