package p008jp.p009co.cyberagent.android.gpuimage;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import p008jp.p009co.cyberagent.android.gpuimage.util.TextureRotationUtil;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup */
public class GPUImageFilterGroup extends GPUImageFilter {
    protected List<GPUImageFilter> mFilters;
    private int[] mFrameBufferTextures;
    private int[] mFrameBuffers;
    private final FloatBuffer mGLCubeBuffer;
    private final FloatBuffer mGLTextureBuffer;
    private final FloatBuffer mGLTextureFlipBuffer;
    protected List<GPUImageFilter> mMergedFilters;

    public GPUImageFilterGroup() {
        this((List<GPUImageFilter>) null);
    }

    public GPUImageFilterGroup(List<GPUImageFilter> filters) {
        this.mFilters = filters;
        if (this.mFilters == null) {
            this.mFilters = new ArrayList();
        } else {
            updateMergedFilters();
        }
        this.mGLCubeBuffer = ByteBuffer.allocateDirect(GPUImageRenderer.CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLCubeBuffer.put(GPUImageRenderer.CUBE).position(0);
        this.mGLTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLTextureBuffer.put(TextureRotationUtil.TEXTURE_NO_ROTATION).position(0);
        float[] flipTexture = TextureRotationUtil.getRotation(Rotation.NORMAL, false, true);
        this.mGLTextureFlipBuffer = ByteBuffer.allocateDirect(flipTexture.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLTextureFlipBuffer.put(flipTexture).position(0);
    }

    public void addFilter(GPUImageFilter aFilter) {
        if (aFilter != null) {
            this.mFilters.add(aFilter);
            updateMergedFilters();
        }
    }

    public void onInit() {
        super.onInit();
        for (GPUImageFilter filter : this.mFilters) {
            filter.init();
        }
    }

    public void onDestroy() {
        destroyFramebuffers();
        for (GPUImageFilter filter : this.mFilters) {
            filter.destroy();
        }
        super.onDestroy();
    }

    private void destroyFramebuffers() {
        if (this.mFrameBufferTextures != null) {
            GLES20.glDeleteTextures(this.mFrameBufferTextures.length, this.mFrameBufferTextures, 0);
            this.mFrameBufferTextures = null;
        }
        if (this.mFrameBuffers != null) {
            GLES20.glDeleteFramebuffers(this.mFrameBuffers.length, this.mFrameBuffers, 0);
            this.mFrameBuffers = null;
        }
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        if (this.mFrameBuffers != null) {
            destroyFramebuffers();
        }
        int size = this.mFilters.size();
        for (int i = 0; i < size; i++) {
            this.mFilters.get(i).onOutputSizeChanged(width, height);
        }
        if (this.mMergedFilters != null && this.mMergedFilters.size() > 0) {
            int size2 = this.mMergedFilters.size();
            this.mFrameBuffers = new int[(size2 - 1)];
            this.mFrameBufferTextures = new int[(size2 - 1)];
            for (int i2 = 0; i2 < size2 - 1; i2++) {
                GLES20.glGenFramebuffers(1, this.mFrameBuffers, i2);
                GLES20.glGenTextures(1, this.mFrameBufferTextures, i2);
                GLES20.glBindTexture(3553, this.mFrameBufferTextures[i2]);
                GLES20.glTexImage2D(3553, 0, 6408, width, height, 0, 6408, 5121, (Buffer) null);
                GLES20.glTexParameterf(3553, 10240, 9729.0f);
                GLES20.glTexParameterf(3553, 10241, 9729.0f);
                GLES20.glTexParameterf(3553, 10242, 33071.0f);
                GLES20.glTexParameterf(3553, 10243, 33071.0f);
                GLES20.glBindFramebuffer(36160, this.mFrameBuffers[i2]);
                GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.mFrameBufferTextures[i2], 0);
                GLES20.glBindTexture(3553, 0);
                GLES20.glBindFramebuffer(36160, 0);
            }
        }
    }

    @SuppressLint({"WrongCall"})
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        boolean isNotLast;
        runPendingOnDrawTasks();
        if (isInitialized() && this.mFrameBuffers != null && this.mFrameBufferTextures != null && this.mMergedFilters != null) {
            int size = this.mMergedFilters.size();
            int previousTexture = textureId;
            for (int i = 0; i < size; i++) {
                GPUImageFilter filter = this.mMergedFilters.get(i);
                if (i < size - 1) {
                    isNotLast = true;
                } else {
                    isNotLast = false;
                }
                if (isNotLast) {
                    GLES20.glBindFramebuffer(36160, this.mFrameBuffers[i]);
                    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                }
                if (i == 0) {
                    filter.onDraw(previousTexture, cubeBuffer, textureBuffer);
                } else if (i == size - 1) {
                    filter.onDraw(previousTexture, this.mGLCubeBuffer, size % 2 == 0 ? this.mGLTextureFlipBuffer : this.mGLTextureBuffer);
                } else {
                    filter.onDraw(previousTexture, this.mGLCubeBuffer, this.mGLTextureBuffer);
                }
                if (isNotLast) {
                    GLES20.glBindFramebuffer(36160, 0);
                    previousTexture = this.mFrameBufferTextures[i];
                }
            }
        }
    }

    public List<GPUImageFilter> getFilters() {
        return this.mFilters;
    }

    public List<GPUImageFilter> getMergedFilters() {
        return this.mMergedFilters;
    }

    public void updateMergedFilters() {
        if (this.mFilters != null) {
            if (this.mMergedFilters == null) {
                this.mMergedFilters = new ArrayList();
            } else {
                this.mMergedFilters.clear();
            }
            for (GPUImageFilter filter : this.mFilters) {
                if (filter instanceof GPUImageFilterGroup) {
                    ((GPUImageFilterGroup) filter).updateMergedFilters();
                    List<GPUImageFilter> filters = ((GPUImageFilterGroup) filter).getMergedFilters();
                    if (filters != null && !filters.isEmpty()) {
                        this.mMergedFilters.addAll(filters);
                    }
                } else {
                    this.mMergedFilters.add(filter);
                }
            }
        }
    }
}
