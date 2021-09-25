package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageToonFilter */
public class GPUImageToonFilter extends GPUImage3x3TextureSamplingFilter {
    public static final String TOON_FRAGMENT_SHADER = "precision highp float;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp float intensity;\nuniform highp float threshold;\nuniform highp float quantizationLevels;\n\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\nvec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n\nfloat bottomLeftIntensity = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\nfloat topRightIntensity = texture2D(inputImageTexture, topRightTextureCoordinate).r;\nfloat topLeftIntensity = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\nfloat bottomRightIntensity = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\nfloat leftIntensity = texture2D(inputImageTexture, leftTextureCoordinate).r;\nfloat rightIntensity = texture2D(inputImageTexture, rightTextureCoordinate).r;\nfloat bottomIntensity = texture2D(inputImageTexture, bottomTextureCoordinate).r;\nfloat topIntensity = texture2D(inputImageTexture, topTextureCoordinate).r;\nfloat h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\nfloat v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n\nfloat mag = length(vec2(h, v));\n\nvec3 posterizedImageColor = floor((textureColor.rgb * quantizationLevels) + 0.5) / quantizationLevels;\n\nfloat thresholdTest = 1.0 - step(threshold, mag);\n\ngl_FragColor = vec4(posterizedImageColor * thresholdTest, textureColor.a);\n}\n";
    float mQuantizationLevels;
    int mQuantizationLevelsLocation;
    float mThreshold;
    int mThresholdLocation;

    public GPUImageToonFilter() {
        this(0.2f, 10.0f);
    }

    public GPUImageToonFilter(float threshold, float quantizationLevels) {
        super(TOON_FRAGMENT_SHADER);
        this.mThreshold = threshold;
        this.mQuantizationLevels = quantizationLevels;
    }

    public void onInit() {
        super.onInit();
        this.mThresholdLocation = GLES20.glGetUniformLocation(getProgram(), "threshold");
        this.mQuantizationLevelsLocation = GLES20.glGetUniformLocation(getProgram(), "quantizationLevels");
    }

    public void onInitialized() {
        super.onInitialized();
        setThreshold(this.mThreshold);
        setQuantizationLevels(this.mQuantizationLevels);
    }

    public void setThreshold(float threshold) {
        this.mThreshold = threshold;
        setFloat(this.mThresholdLocation, threshold);
    }

    public void setQuantizationLevels(float quantizationLevels) {
        this.mQuantizationLevels = quantizationLevels;
        setFloat(this.mQuantizationLevelsLocation, quantizationLevels);
    }
}
