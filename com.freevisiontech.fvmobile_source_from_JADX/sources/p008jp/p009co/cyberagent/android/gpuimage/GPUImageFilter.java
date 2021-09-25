package p008jp.p009co.cyberagent.android.gpuimage;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Scanner;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageFilter */
public class GPUImageFilter {
    public static final String NO_FILTER_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
    public static final String NO_FILTER_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
    private final String mFragmentShader;
    protected int mGLAttribPosition;
    protected int mGLAttribTextureCoordinate;
    protected int mGLProgId;
    protected int mGLUniformTexture;
    private boolean mIsInitialized;
    protected int mOutputHeight;
    protected int mOutputWidth;
    private final LinkedList<Runnable> mRunOnDraw;
    private final String mVertexShader;

    public GPUImageFilter() {
        this(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
    }

    public GPUImageFilter(String vertexShader, String fragmentShader) {
        this.mRunOnDraw = new LinkedList<>();
        this.mVertexShader = vertexShader;
        this.mFragmentShader = fragmentShader;
    }

    public final void init() {
        onInit();
        this.mIsInitialized = true;
        onInitialized();
    }

    public void onInit() {
        this.mGLProgId = OpenGlUtils.loadProgram(this.mVertexShader, this.mFragmentShader);
        this.mGLAttribPosition = GLES20.glGetAttribLocation(this.mGLProgId, "position");
        this.mGLUniformTexture = GLES20.glGetUniformLocation(this.mGLProgId, "inputImageTexture");
        this.mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(this.mGLProgId, "inputTextureCoordinate");
        this.mIsInitialized = true;
    }

    public void onInitialized() {
    }

    public final void destroy() {
        this.mIsInitialized = false;
        GLES20.glDeleteProgram(this.mGLProgId);
        onDestroy();
    }

    public void onDestroy() {
    }

    public void onOutputSizeChanged(int width, int height) {
        this.mOutputWidth = width;
        this.mOutputHeight = height;
    }

    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        GLES20.glUseProgram(this.mGLProgId);
        runPendingOnDrawTasks();
        if (this.mIsInitialized) {
            cubeBuffer.position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, cubeBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
            textureBuffer.position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, textureBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
            if (textureId != -1) {
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, textureId);
                GLES20.glUniform1i(this.mGLUniformTexture, 0);
            }
            onDrawArraysPre();
            GLES20.glDrawArrays(5, 0, 4);
            GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
            GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
            GLES20.glBindTexture(3553, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onDrawArraysPre() {
    }

    /* access modifiers changed from: protected */
    public void runPendingOnDrawTasks() {
        while (!this.mRunOnDraw.isEmpty()) {
            this.mRunOnDraw.removeFirst().run();
        }
    }

    public boolean isInitialized() {
        return this.mIsInitialized;
    }

    public int getOutputWidth() {
        return this.mOutputWidth;
    }

    public int getOutputHeight() {
        return this.mOutputHeight;
    }

    public int getProgram() {
        return this.mGLProgId;
    }

    public int getAttribPosition() {
        return this.mGLAttribPosition;
    }

    public int getAttribTextureCoordinate() {
        return this.mGLAttribTextureCoordinate;
    }

    public int getUniformTexture() {
        return this.mGLUniformTexture;
    }

    /* access modifiers changed from: protected */
    public void setInteger(final int location, final int intValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1i(location, intValue);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloat(final int location, final float floatValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1f(location, floatValue);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloatVec2(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloatVec3(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloatVec4(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setFloatArray(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1fv(location, arrayValue.length, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setPoint(final int location, final PointF point) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform2fv(location, 1, new float[]{point.x, point.y}, 0);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setUniformMatrix3f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniformMatrix3fv(location, 1, false, matrix, 0);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setUniformMatrix4f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void runOnDraw(Runnable runnable) {
        synchronized (this.mRunOnDraw) {
            this.mRunOnDraw.addLast(runnable);
        }
    }

    public static String loadShader(String file, Context context) {
        try {
            InputStream ims = context.getAssets().open(file);
            String re = convertStreamToString(ims);
            ims.close();
            return re;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
