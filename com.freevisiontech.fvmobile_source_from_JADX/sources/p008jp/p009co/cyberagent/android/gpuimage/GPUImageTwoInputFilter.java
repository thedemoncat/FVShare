package p008jp.p009co.cyberagent.android.gpuimage;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import p008jp.p009co.cyberagent.android.gpuimage.util.TextureRotationUtil;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageTwoInputFilter */
public class GPUImageTwoInputFilter extends GPUImageFilter {
    private static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\n \nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n}";
    private Bitmap mBitmap;
    public int mFilterInputTextureUniform2;
    public int mFilterSecondTextureCoordinateAttribute;
    public int mFilterSourceTexture2;
    private ByteBuffer mTexture2CoordinatesBuffer;

    public GPUImageTwoInputFilter(String fragmentShader) {
        this(VERTEX_SHADER, fragmentShader);
    }

    public GPUImageTwoInputFilter(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
        this.mFilterSourceTexture2 = -1;
        setRotation(Rotation.NORMAL, false, false);
    }

    public void onInit() {
        super.onInit();
        this.mFilterSecondTextureCoordinateAttribute = GLES20.glGetAttribLocation(getProgram(), "inputTextureCoordinate2");
        this.mFilterInputTextureUniform2 = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
        GLES20.glEnableVertexAttribArray(this.mFilterSecondTextureCoordinateAttribute);
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            setBitmap(this.mBitmap);
        }
    }

    public void setBitmap(final Bitmap bitmap) {
        if (bitmap == null || !bitmap.isRecycled()) {
            this.mBitmap = bitmap;
            if (this.mBitmap != null) {
                runOnDraw(new Runnable() {
                    public void run() {
                        if (GPUImageTwoInputFilter.this.mFilterSourceTexture2 == -1 && bitmap != null && !bitmap.isRecycled()) {
                            GLES20.glActiveTexture(33987);
                            GPUImageTwoInputFilter.this.mFilterSourceTexture2 = OpenGlUtils.loadTexture(bitmap, -1, false);
                        }
                    }
                });
            }
        }
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void recycleBitmap() {
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(1, new int[]{this.mFilterSourceTexture2}, 0);
        this.mFilterSourceTexture2 = -1;
    }

    /* access modifiers changed from: protected */
    public void onDrawArraysPre() {
        GLES20.glEnableVertexAttribArray(this.mFilterSecondTextureCoordinateAttribute);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.mFilterSourceTexture2);
        GLES20.glUniform1i(this.mFilterInputTextureUniform2, 3);
        this.mTexture2CoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, 2, 5126, false, 0, this.mTexture2CoordinatesBuffer);
    }

    public void setRotation(Rotation rotation, boolean flipHorizontal, boolean flipVertical) {
        float[] buffer = TextureRotationUtil.getRotation(rotation, flipHorizontal, flipVertical);
        ByteBuffer bBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder());
        FloatBuffer fBuffer = bBuffer.asFloatBuffer();
        fBuffer.put(buffer);
        fBuffer.flip();
        this.mTexture2CoordinatesBuffer = bBuffer;
    }
}
