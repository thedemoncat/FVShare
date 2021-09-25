package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter */
public class GPUImageChromaKeyBlendFilter extends GPUImageTwoInputFilter {
    public static final String CHROMA_KEY_BLEND_FRAGMENT_SHADER = " precision highp float;\n \n varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform float thresholdSensitivity;\n uniform float smoothing;\n uniform vec3 colorToReplace;\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n     vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n     \n     float maskY = 0.2989 * colorToReplace.r + 0.5866 * colorToReplace.g + 0.1145 * colorToReplace.b;\n     float maskCr = 0.7132 * (colorToReplace.r - maskY);\n     float maskCb = 0.5647 * (colorToReplace.b - maskY);\n     \n     float Y = 0.2989 * textureColor.r + 0.5866 * textureColor.g + 0.1145 * textureColor.b;\n     float Cr = 0.7132 * (textureColor.r - Y);\n     float Cb = 0.5647 * (textureColor.b - Y);\n     \n     float blendValue = 1.0 - smoothstep(thresholdSensitivity, thresholdSensitivity + smoothing, distance(vec2(Cr, Cb), vec2(maskCr, maskCb)));\n     gl_FragColor = mix(textureColor, textureColor2, blendValue);\n }";
    private float[] mColorToReplace = {0.0f, 1.0f, 0.0f};
    private int mColorToReplaceLocation;
    private float mSmoothing = 0.1f;
    private int mSmoothingLocation;
    private float mThresholdSensitivity = 0.3f;
    private int mThresholdSensitivityLocation;

    public GPUImageChromaKeyBlendFilter() {
        super(CHROMA_KEY_BLEND_FRAGMENT_SHADER);
    }

    public void onInit() {
        super.onInit();
        this.mThresholdSensitivityLocation = GLES20.glGetUniformLocation(getProgram(), "thresholdSensitivity");
        this.mSmoothingLocation = GLES20.glGetUniformLocation(getProgram(), "smoothing");
        this.mColorToReplaceLocation = GLES20.glGetUniformLocation(getProgram(), "colorToReplace");
    }

    public void onInitialized() {
        super.onInitialized();
        setSmoothing(this.mSmoothing);
        setThresholdSensitivity(this.mThresholdSensitivity);
        setColorToReplace(this.mColorToReplace[0], this.mColorToReplace[1], this.mColorToReplace[2]);
    }

    public void setSmoothing(float smoothing) {
        this.mSmoothing = smoothing;
        setFloat(this.mSmoothingLocation, this.mSmoothing);
    }

    public void setThresholdSensitivity(float thresholdSensitivity) {
        this.mThresholdSensitivity = thresholdSensitivity;
        setFloat(this.mThresholdSensitivityLocation, this.mThresholdSensitivity);
    }

    public void setColorToReplace(float redComponent, float greenComponent, float blueComponent) {
        this.mColorToReplace = new float[]{redComponent, greenComponent, blueComponent};
        setFloatVec3(this.mColorToReplaceLocation, this.mColorToReplace);
    }
}
