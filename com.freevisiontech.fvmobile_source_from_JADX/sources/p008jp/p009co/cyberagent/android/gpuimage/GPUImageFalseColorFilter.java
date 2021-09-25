package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageFalseColorFilter */
public class GPUImageFalseColorFilter extends GPUImageFilter {
    public static final String FALSECOLOR_FRAGMENT_SHADER = "precision lowp float;\n\nvarying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform float intensity;\nuniform vec3 firstColor;\nuniform vec3 secondColor;\n\nconst mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\nlowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\nfloat luminance = dot(textureColor.rgb, luminanceWeighting);\n\ngl_FragColor = vec4( mix(firstColor.rgb, secondColor.rgb, luminance), textureColor.a);\n}\n";
    private float[] mFirstColor;
    private int mFirstColorLocation;
    private float[] mSecondColor;
    private int mSecondColorLocation;

    public GPUImageFalseColorFilter() {
        this(0.0f, 0.0f, 0.5f, 1.0f, 0.0f, 0.0f);
    }

    public GPUImageFalseColorFilter(float firstRed, float firstGreen, float firstBlue, float secondRed, float secondGreen, float secondBlue) {
        this(new float[]{firstRed, firstGreen, firstBlue}, new float[]{secondRed, secondGreen, secondBlue});
    }

    public GPUImageFalseColorFilter(float[] firstColor, float[] secondColor) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, FALSECOLOR_FRAGMENT_SHADER);
        this.mFirstColor = firstColor;
        this.mSecondColor = secondColor;
    }

    public void onInit() {
        super.onInit();
        this.mFirstColorLocation = GLES20.glGetUniformLocation(getProgram(), "firstColor");
        this.mSecondColorLocation = GLES20.glGetUniformLocation(getProgram(), "secondColor");
    }

    public void onInitialized() {
        super.onInitialized();
        setFirstColor(this.mFirstColor);
        setSecondColor(this.mSecondColor);
    }

    public void setFirstColor(float[] firstColor) {
        this.mFirstColor = firstColor;
        setFloatVec3(this.mFirstColorLocation, firstColor);
    }

    public void setSecondColor(float[] secondColor) {
        this.mSecondColor = secondColor;
        setFloatVec3(this.mSecondColorLocation, secondColor);
    }
}
