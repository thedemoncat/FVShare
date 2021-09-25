package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter */
public class GPUImageSharpenFilter extends GPUImageFilter {
    public static final String SHARPEN_FRAGMENT_SHADER = "precision highp float;\n\nvarying highp vec2 textureCoordinate;\nvarying highp vec2 leftTextureCoordinate;\nvarying highp vec2 rightTextureCoordinate; \nvarying highp vec2 topTextureCoordinate;\nvarying highp vec2 bottomTextureCoordinate;\n\nvarying highp float centerMultiplier;\nvarying highp float edgeMultiplier;\n\nuniform sampler2D inputImageTexture;\n\nvoid main()\n{\n    mediump vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;\n    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n\n    gl_FragColor = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(inputImageTexture, bottomTextureCoordinate).w);\n}";
    public static final String SHARPEN_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform float imageWidthFactor; \nuniform float imageHeightFactor; \nuniform float sharpness;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate; \nvarying vec2 topTextureCoordinate;\nvarying vec2 bottomTextureCoordinate;\n\nvarying float centerMultiplier;\nvarying float edgeMultiplier;\n\nvoid main()\n{\n    gl_Position = position;\n    \n    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n    \n    textureCoordinate = inputTextureCoordinate.xy;\n    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n    topTextureCoordinate = inputTextureCoordinate.xy + heightStep;     \n    bottomTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n    \n    centerMultiplier = 1.0 + 4.0 * sharpness;\n    edgeMultiplier = sharpness;\n}";
    private int mImageHeightFactorLocation;
    private int mImageWidthFactorLocation;
    private float mSharpness;
    private int mSharpnessLocation;

    public GPUImageSharpenFilter() {
        this(0.0f);
    }

    public GPUImageSharpenFilter(float sharpness) {
        super(SHARPEN_VERTEX_SHADER, SHARPEN_FRAGMENT_SHADER);
        this.mSharpness = sharpness;
    }

    public void onInit() {
        super.onInit();
        this.mSharpnessLocation = GLES20.glGetUniformLocation(getProgram(), "sharpness");
        this.mImageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
        this.mImageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
        setSharpness(this.mSharpness);
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setFloat(this.mImageWidthFactorLocation, 1.0f / ((float) width));
        setFloat(this.mImageHeightFactorLocation, 1.0f / ((float) height));
    }

    public void setSharpness(float sharpness) {
        this.mSharpness = sharpness;
        setFloat(this.mSharpnessLocation, this.mSharpness);
    }
}
