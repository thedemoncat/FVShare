package p008jp.p009co.cyberagent.android.gpuimage;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImage;
import p008jp.p009co.cyberagent.android.gpuimage.util.TextureRotationUtil;

@TargetApi(11)
/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageRenderer */
public class GPUImageRenderer implements GLSurfaceView.Renderer, Camera.PreviewCallback {
    static final float[] CUBE = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    public static final int NO_IMAGE = -1;
    /* access modifiers changed from: private */
    public int mAddedPadding;
    private float mBackgroundBlue = 0.0f;
    private float mBackgroundGreen = 0.0f;
    private float mBackgroundRed = 0.0f;
    /* access modifiers changed from: private */
    public GPUImageFilter mFilter;
    private boolean mFlipHorizontal;
    private boolean mFlipVertical;
    private final FloatBuffer mGLCubeBuffer;
    /* access modifiers changed from: private */
    public IntBuffer mGLRgbBuffer;
    private final FloatBuffer mGLTextureBuffer;
    /* access modifiers changed from: private */
    public int mGLTextureId = -1;
    /* access modifiers changed from: private */
    public int mImageHeight;
    /* access modifiers changed from: private */
    public int mImageWidth;
    /* access modifiers changed from: private */
    public int mOutputHeight;
    /* access modifiers changed from: private */
    public int mOutputWidth;
    private Rotation mRotation;
    private final Queue<Runnable> mRunOnDraw;
    private final Queue<Runnable> mRunOnDrawEnd;
    private GPUImage.ScaleType mScaleType = GPUImage.ScaleType.CENTER_CROP;
    public final Object mSurfaceChangedWaiter = new Object();
    public SurfaceTexture mSurfaceTexture = null;

    public GPUImageRenderer(GPUImageFilter filter) {
        this.mFilter = filter;
        this.mRunOnDraw = new LinkedList();
        this.mRunOnDrawEnd = new LinkedList();
        this.mGLCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLCubeBuffer.put(CUBE).position(0);
        this.mGLTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        setRotation(Rotation.NORMAL, false, false);
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(this.mBackgroundRed, this.mBackgroundGreen, this.mBackgroundBlue, 1.0f);
        GLES20.glDisable(2929);
        this.mFilter.init();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.mOutputWidth = width;
        this.mOutputHeight = height;
        GLES20.glViewport(0, 0, width, height);
        GLES20.glUseProgram(this.mFilter.getProgram());
        this.mFilter.onOutputSizeChanged(width, height);
        adjustImageScaling();
        synchronized (this.mSurfaceChangedWaiter) {
            this.mSurfaceChangedWaiter.notifyAll();
        }
    }

    public void onDrawFrame(GL10 gl) {
        Log.v("Huang", "onDrawFrame");
        GLES20.glClear(16640);
        runAll(this.mRunOnDraw);
        this.mFilter.onDraw(this.mGLTextureId, this.mGLCubeBuffer, this.mGLTextureBuffer);
        runAll(this.mRunOnDrawEnd);
        if (this.mSurfaceTexture != null) {
            this.mSurfaceTexture.updateTexImage();
        }
    }

    public void setBackgroundColor(float red, float green, float blue) {
        this.mBackgroundRed = red;
        this.mBackgroundGreen = green;
        this.mBackgroundBlue = blue;
    }

    private void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    public void onPreviewFrame(final byte[] data, final Camera camera) {
        try {
            final Camera.Size previewSize = camera.getParameters().getPreviewSize();
            Log.v("Huang", "onPreviewFrame:width=" + previewSize.width + ":height=" + previewSize.height);
            if (this.mGLRgbBuffer == null) {
                this.mGLRgbBuffer = IntBuffer.allocate(previewSize.width * previewSize.height);
            }
            if (this.mRunOnDraw.isEmpty()) {
                runOnDraw(new Runnable() {
                    public void run() {
                        GPUImageNativeLibrary.YUVtoRBGA(data, previewSize.width, previewSize.height, GPUImageRenderer.this.mGLRgbBuffer.array());
                        int unused = GPUImageRenderer.this.mGLTextureId = OpenGlUtils.loadTexture(GPUImageRenderer.this.mGLRgbBuffer, previewSize, GPUImageRenderer.this.mGLTextureId);
                        camera.addCallbackBuffer(data);
                        if (GPUImageRenderer.this.mImageWidth != previewSize.width) {
                            int unused2 = GPUImageRenderer.this.mImageWidth = previewSize.width;
                            int unused3 = GPUImageRenderer.this.mImageHeight = previewSize.height;
                            GPUImageRenderer.this.adjustImageScaling();
                        }
                    }
                });
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void setUpSurfaceTexture(final Camera camera) {
        runOnDraw(new Runnable() {
            public void run() {
                Log.v("Huang", "setUpSurfaceTexture");
                int[] textures = new int[1];
                GLES20.glGenTextures(1, textures, 0);
                GPUImageRenderer.this.mSurfaceTexture = new SurfaceTexture(textures[0]);
                try {
                    camera.setPreviewTexture(GPUImageRenderer.this.mSurfaceTexture);
                    camera.setPreviewCallback(GPUImageRenderer.this);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setFilter(final GPUImageFilter filter) {
        runOnDraw(new Runnable() {
            public void run() {
                GPUImageFilter oldFilter = GPUImageRenderer.this.mFilter;
                GPUImageFilter unused = GPUImageRenderer.this.mFilter = filter;
                if (oldFilter != null) {
                    oldFilter.destroy();
                }
                GPUImageRenderer.this.mFilter.init();
                GLES20.glUseProgram(GPUImageRenderer.this.mFilter.getProgram());
                GPUImageRenderer.this.mFilter.onOutputSizeChanged(GPUImageRenderer.this.mOutputWidth, GPUImageRenderer.this.mOutputHeight);
            }
        });
    }

    public void deleteImage() {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glDeleteTextures(1, new int[]{GPUImageRenderer.this.mGLTextureId}, 0);
                int unused = GPUImageRenderer.this.mGLTextureId = -1;
            }
        });
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap, true);
    }

    public void setImageBitmap(final Bitmap bitmap, final boolean recycle) {
        if (bitmap != null) {
            runOnDraw(new Runnable() {
                public void run() {
                    Bitmap resizedBitmap = null;
                    if (bitmap.getWidth() % 2 == 1) {
                        resizedBitmap = Bitmap.createBitmap(bitmap.getWidth() + 1, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas can = new Canvas(resizedBitmap);
                        can.drawARGB(0, 0, 0, 0);
                        can.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                        int unused = GPUImageRenderer.this.mAddedPadding = 1;
                    } else {
                        int unused2 = GPUImageRenderer.this.mAddedPadding = 0;
                    }
                    int unused3 = GPUImageRenderer.this.mGLTextureId = OpenGlUtils.loadTexture(resizedBitmap != null ? resizedBitmap : bitmap, GPUImageRenderer.this.mGLTextureId, recycle);
                    if (resizedBitmap != null) {
                        resizedBitmap.recycle();
                    }
                    int unused4 = GPUImageRenderer.this.mImageWidth = bitmap.getWidth();
                    int unused5 = GPUImageRenderer.this.mImageHeight = bitmap.getHeight();
                    GPUImageRenderer.this.adjustImageScaling();
                }
            });
        }
    }

    public void setScaleType(GPUImage.ScaleType scaleType) {
        this.mScaleType = scaleType;
    }

    /* access modifiers changed from: protected */
    public int getFrameWidth() {
        return this.mOutputWidth;
    }

    /* access modifiers changed from: protected */
    public int getFrameHeight() {
        return this.mOutputHeight;
    }

    /* access modifiers changed from: private */
    public void adjustImageScaling() {
        float outputWidth = (float) this.mOutputWidth;
        float outputHeight = (float) this.mOutputHeight;
        if (this.mRotation == Rotation.ROTATION_270 || this.mRotation == Rotation.ROTATION_90) {
            outputWidth = (float) this.mOutputHeight;
            outputHeight = (float) this.mOutputWidth;
        }
        float ratioMax = Math.max(outputWidth / ((float) this.mImageWidth), outputHeight / ((float) this.mImageHeight));
        int imageWidthNew = Math.round(((float) this.mImageWidth) * ratioMax);
        float ratioWidth = ((float) imageWidthNew) / outputWidth;
        float ratioHeight = ((float) Math.round(((float) this.mImageHeight) * ratioMax)) / outputHeight;
        float[] cube = CUBE;
        float[] textureCords = TextureRotationUtil.getRotation(this.mRotation, this.mFlipHorizontal, this.mFlipVertical);
        if (this.mScaleType == GPUImage.ScaleType.CENTER_CROP) {
            float distHorizontal = (1.0f - (1.0f / ratioWidth)) / 2.0f;
            float distVertical = (1.0f - (1.0f / ratioHeight)) / 2.0f;
            textureCords = new float[]{addDistance(textureCords[0], distHorizontal), addDistance(textureCords[1], distVertical), addDistance(textureCords[2], distHorizontal), addDistance(textureCords[3], distVertical), addDistance(textureCords[4], distHorizontal), addDistance(textureCords[5], distVertical), addDistance(textureCords[6], distHorizontal), addDistance(textureCords[7], distVertical)};
        } else {
            cube = new float[]{CUBE[0] / ratioHeight, CUBE[1] / ratioWidth, CUBE[2] / ratioHeight, CUBE[3] / ratioWidth, CUBE[4] / ratioHeight, CUBE[5] / ratioWidth, CUBE[6] / ratioHeight, CUBE[7] / ratioWidth};
        }
        this.mGLCubeBuffer.clear();
        this.mGLCubeBuffer.put(cube).position(0);
        this.mGLTextureBuffer.clear();
        this.mGLTextureBuffer.put(textureCords).position(0);
    }

    private float addDistance(float coordinate, float distance) {
        return coordinate == 0.0f ? distance : 1.0f - distance;
    }

    public void setRotationCamera(Rotation rotation, boolean flipHorizontal, boolean flipVertical) {
        setRotation(rotation, flipVertical, flipHorizontal);
    }

    public void setRotation(Rotation rotation) {
        this.mRotation = rotation;
        adjustImageScaling();
    }

    public void setRotation(Rotation rotation, boolean flipHorizontal, boolean flipVertical) {
        this.mFlipHorizontal = flipHorizontal;
        this.mFlipVertical = flipVertical;
        setRotation(rotation);
    }

    public Rotation getRotation() {
        return this.mRotation;
    }

    public boolean isFlippedHorizontally() {
        return this.mFlipHorizontal;
    }

    public boolean isFlippedVertically() {
        return this.mFlipVertical;
    }

    /* access modifiers changed from: protected */
    public void runOnDraw(Runnable runnable) {
        synchronized (this.mRunOnDraw) {
            this.mRunOnDraw.add(runnable);
        }
    }

    /* access modifiers changed from: protected */
    public void runOnDrawEnd(Runnable runnable) {
        synchronized (this.mRunOnDrawEnd) {
            this.mRunOnDrawEnd.add(runnable);
        }
    }
}
