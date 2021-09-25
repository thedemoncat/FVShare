package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter */
public class GPUImageLevelsFilter extends GPUImageFilter {
    public static final String LEVELS_FRAGMET_SHADER = " varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform mediump vec3 levelMinimum;\n uniform mediump vec3 levelMiddle;\n uniform mediump vec3 levelMaximum;\n uniform mediump vec3 minOutput;\n uniform mediump vec3 maxOutput;\n \n void main()\n {\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4( mix(minOutput, maxOutput, pow(min(max(textureColor.rgb -levelMinimum, vec3(0.0)) / (levelMaximum - levelMinimum  ), vec3(1.0)), 1.0 /levelMiddle)) , textureColor.a);\n }\n";
    private static final String LOGTAG = GPUImageLevelsFilter.class.getSimpleName();
    private float[] mMax;
    private int mMaxLocation;
    private float[] mMaxOutput;
    private int mMaxOutputLocation;
    private float[] mMid;
    private int mMidLocation;
    private float[] mMin;
    private int mMinLocation;
    private float[] mMinOutput;
    private int mMinOutputLocation;

    public GPUImageLevelsFilter() {
        this(new float[]{0.0f, 0.0f, 0.0f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{0.0f, 0.0f, 0.0f}, new float[]{1.0f, 1.0f, 1.0f});
    }

    private GPUImageLevelsFilter(float[] min, float[] mid, float[] max, float[] minOUt, float[] maxOut) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, LEVELS_FRAGMET_SHADER);
        this.mMin = min;
        this.mMid = mid;
        this.mMax = max;
        this.mMinOutput = minOUt;
        this.mMaxOutput = maxOut;
        setMin(0.0f, 1.0f, 1.0f, 0.0f, 1.0f);
    }

    public void onInit() {
        super.onInit();
        this.mMinLocation = GLES20.glGetUniformLocation(getProgram(), "levelMinimum");
        this.mMidLocation = GLES20.glGetUniformLocation(getProgram(), "levelMiddle");
        this.mMaxLocation = GLES20.glGetUniformLocation(getProgram(), "levelMaximum");
        this.mMinOutputLocation = GLES20.glGetUniformLocation(getProgram(), "minOutput");
        this.mMaxOutputLocation = GLES20.glGetUniformLocation(getProgram(), "maxOutput");
    }

    public void onInitialized() {
        super.onInitialized();
        updateUniforms();
    }

    public void updateUniforms() {
        setFloatVec3(this.mMinLocation, this.mMin);
        setFloatVec3(this.mMidLocation, this.mMid);
        setFloatVec3(this.mMaxLocation, this.mMax);
        setFloatVec3(this.mMinOutputLocation, this.mMinOutput);
        setFloatVec3(this.mMaxOutputLocation, this.mMaxOutput);
    }

    public void setMin(float min, float mid, float max, float minOut, float maxOut) {
        setRedMin(min, mid, max, minOut, maxOut);
        setGreenMin(min, mid, max, minOut, maxOut);
        setBlueMin(min, mid, max, minOut, maxOut);
    }

    public void setMin(float min, float mid, float max) {
        setMin(min, mid, max, 0.0f, 1.0f);
    }

    public void setRedMin(float min, float mid, float max, float minOut, float maxOut) {
        this.mMin[0] = min;
        this.mMid[0] = mid;
        this.mMax[0] = max;
        this.mMinOutput[0] = minOut;
        this.mMaxOutput[0] = maxOut;
        updateUniforms();
    }

    public void setRedMin(float min, float mid, float max) {
        setRedMin(min, mid, max, 0.0f, 1.0f);
    }

    public void setGreenMin(float min, float mid, float max, float minOut, float maxOut) {
        this.mMin[1] = min;
        this.mMid[1] = mid;
        this.mMax[1] = max;
        this.mMinOutput[1] = minOut;
        this.mMaxOutput[1] = maxOut;
        updateUniforms();
    }

    public void setGreenMin(float min, float mid, float max) {
        setGreenMin(min, mid, max, 0.0f, 1.0f);
    }

    public void setBlueMin(float min, float mid, float max, float minOut, float maxOut) {
        this.mMin[2] = min;
        this.mMid[2] = mid;
        this.mMax[2] = max;
        this.mMinOutput[2] = minOut;
        this.mMaxOutput[2] = maxOut;
        updateUniforms();
    }

    public void setBlueMin(float min, float mid, float max) {
        setBlueMin(min, mid, max, 0.0f, 1.0f);
    }
}
