package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageColorMatrixFilter */
public class GPUImageColorMatrixFilter extends GPUImageFilter {
    public static final String COLOR_MATRIX_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform lowp mat4 colorMatrix;\nuniform lowp float intensity;\n\nvoid main()\n{\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp vec4 outputColor = textureColor * colorMatrix;\n    \n    gl_FragColor = (intensity * outputColor) + ((1.0 - intensity) * textureColor);\n}";
    private float[] mColorMatrix;
    private int mColorMatrixLocation;
    private float mIntensity;
    private int mIntensityLocation;

    public GPUImageColorMatrixFilter() {
        this(1.0f, new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f});
    }

    public GPUImageColorMatrixFilter(float intensity, float[] colorMatrix) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, COLOR_MATRIX_FRAGMENT_SHADER);
        this.mIntensity = intensity;
        this.mColorMatrix = colorMatrix;
    }

    public void onInit() {
        super.onInit();
        this.mColorMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "colorMatrix");
        this.mIntensityLocation = GLES20.glGetUniformLocation(getProgram(), "intensity");
    }

    public void onInitialized() {
        super.onInitialized();
        setIntensity(this.mIntensity);
        setColorMatrix(this.mColorMatrix);
    }

    public void setIntensity(float intensity) {
        this.mIntensity = intensity;
        setFloat(this.mIntensityLocation, intensity);
    }

    public void setColorMatrix(float[] colorMatrix) {
        this.mColorMatrix = colorMatrix;
        setUniformMatrix4f(this.mColorMatrixLocation, colorMatrix);
    }
}
