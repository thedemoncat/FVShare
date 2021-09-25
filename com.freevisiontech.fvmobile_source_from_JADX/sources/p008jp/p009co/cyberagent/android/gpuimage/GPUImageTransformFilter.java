package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageTransformFilter */
public class GPUImageTransformFilter extends GPUImageFilter {
    public static final String TRANSFORM_VERTEX_SHADER = "attribute vec4 position;\n attribute vec4 inputTextureCoordinate;\n \n uniform mat4 transformMatrix;\n uniform mat4 orthographicMatrix;\n \n varying vec2 textureCoordinate;\n \n void main()\n {\n     gl_Position = transformMatrix * vec4(position.xyz, 1.0) * orthographicMatrix;\n     textureCoordinate = inputTextureCoordinate.xy;\n }";
    private boolean anchorTopLeft;
    private boolean ignoreAspectRatio;
    private float[] orthographicMatrix = new float[16];
    private int orthographicMatrixUniform;
    private float[] transform3D;
    private int transformMatrixUniform;

    public GPUImageTransformFilter() {
        super(TRANSFORM_VERTEX_SHADER, GPUImageFilter.NO_FILTER_FRAGMENT_SHADER);
        Matrix.orthoM(this.orthographicMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
        this.transform3D = new float[16];
        Matrix.setIdentityM(this.transform3D, 0);
    }

    public void onInit() {
        super.onInit();
        this.transformMatrixUniform = GLES20.glGetUniformLocation(getProgram(), "transformMatrix");
        this.orthographicMatrixUniform = GLES20.glGetUniformLocation(getProgram(), "orthographicMatrix");
        setUniformMatrix4f(this.transformMatrixUniform, this.transform3D);
        setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
    }

    public void onInitialized() {
        super.onInitialized();
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        if (!this.ignoreAspectRatio) {
            Matrix.orthoM(this.orthographicMatrix, 0, -1.0f, 1.0f, (((float) height) * -1.0f) / ((float) width), (((float) height) * 1.0f) / ((float) width), -1.0f, 1.0f);
            setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
        }
    }

    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        FloatBuffer vertBuffer = cubeBuffer;
        if (!this.ignoreAspectRatio) {
            float[] adjustedVertices = new float[8];
            cubeBuffer.position(0);
            cubeBuffer.get(adjustedVertices);
            float normalizedHeight = ((float) getOutputHeight()) / ((float) getOutputWidth());
            adjustedVertices[1] = adjustedVertices[1] * normalizedHeight;
            adjustedVertices[3] = adjustedVertices[3] * normalizedHeight;
            adjustedVertices[5] = adjustedVertices[5] * normalizedHeight;
            adjustedVertices[7] = adjustedVertices[7] * normalizedHeight;
            vertBuffer = ByteBuffer.allocateDirect(adjustedVertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            vertBuffer.put(adjustedVertices).position(0);
        }
        super.onDraw(textureId, vertBuffer, textureBuffer);
    }

    public void setTransform3D(float[] transform3D2) {
        this.transform3D = transform3D2;
        setUniformMatrix4f(this.transformMatrixUniform, transform3D2);
    }

    public float[] getTransform3D() {
        return this.transform3D;
    }

    public void setIgnoreAspectRatio(boolean ignoreAspectRatio2) {
        this.ignoreAspectRatio = ignoreAspectRatio2;
        if (ignoreAspectRatio2) {
            Matrix.orthoM(this.orthographicMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
            setUniformMatrix4f(this.orthographicMatrixUniform, this.orthographicMatrix);
            return;
        }
        onOutputSizeChanged(getOutputWidth(), getOutputHeight());
    }

    public boolean ignoreAspectRatio() {
        return this.ignoreAspectRatio;
    }

    public void setAnchorTopLeft(boolean anchorTopLeft2) {
        this.anchorTopLeft = anchorTopLeft2;
        setIgnoreAspectRatio(this.ignoreAspectRatio);
    }

    public boolean anchorTopLeft() {
        return this.anchorTopLeft;
    }
}
