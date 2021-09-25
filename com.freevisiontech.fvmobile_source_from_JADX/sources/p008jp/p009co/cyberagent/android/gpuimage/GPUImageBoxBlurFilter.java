package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter */
public class GPUImageBoxBlurFilter extends GPUImageTwoPassTextureSamplingFilter {
    public static final String FRAGMENT_SHADER = "precision highp float;\n\nuniform sampler2D inputImageTexture;\n\nvarying vec2 centerTextureCoordinate;\nvarying vec2 oneStepLeftTextureCoordinate;\nvarying vec2 twoStepsLeftTextureCoordinate;\nvarying vec2 oneStepRightTextureCoordinate;\nvarying vec2 twoStepsRightTextureCoordinate;\n\nvoid main()\n{\nlowp vec4 fragmentColor = texture2D(inputImageTexture, centerTextureCoordinate) * 0.2;\nfragmentColor += texture2D(inputImageTexture, oneStepLeftTextureCoordinate) * 0.2;\nfragmentColor += texture2D(inputImageTexture, oneStepRightTextureCoordinate) * 0.2;\nfragmentColor += texture2D(inputImageTexture, twoStepsLeftTextureCoordinate) * 0.2;\nfragmentColor += texture2D(inputImageTexture, twoStepsRightTextureCoordinate) * 0.2;\n\ngl_FragColor = fragmentColor;\n}\n";
    public static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec2 inputTextureCoordinate;\n\nuniform float texelWidthOffset; \nuniform float texelHeightOffset; \n\nvarying vec2 centerTextureCoordinate;\nvarying vec2 oneStepLeftTextureCoordinate;\nvarying vec2 twoStepsLeftTextureCoordinate;\nvarying vec2 oneStepRightTextureCoordinate;\nvarying vec2 twoStepsRightTextureCoordinate;\n\nvoid main()\n{\ngl_Position = position;\n\nvec2 firstOffset = vec2(1.5 * texelWidthOffset, 1.5 * texelHeightOffset);\nvec2 secondOffset = vec2(3.5 * texelWidthOffset, 3.5 * texelHeightOffset);\n\ncenterTextureCoordinate = inputTextureCoordinate;\noneStepLeftTextureCoordinate = inputTextureCoordinate - firstOffset;\ntwoStepsLeftTextureCoordinate = inputTextureCoordinate - secondOffset;\noneStepRightTextureCoordinate = inputTextureCoordinate + firstOffset;\ntwoStepsRightTextureCoordinate = inputTextureCoordinate + secondOffset;\n}\n";
    private float blurSize;

    public GPUImageBoxBlurFilter() {
        this(1.0f);
    }

    public GPUImageBoxBlurFilter(float blurSize2) {
        super(VERTEX_SHADER, FRAGMENT_SHADER, VERTEX_SHADER, FRAGMENT_SHADER);
        this.blurSize = 1.0f;
        this.blurSize = blurSize2;
    }

    public void setBlurSize(float blurSize2) {
        this.blurSize = blurSize2;
        runOnDraw(new Runnable() {
            public void run() {
                GPUImageBoxBlurFilter.this.initTexelOffsets();
            }
        });
    }

    public float getVerticalTexelOffsetRatio() {
        return this.blurSize;
    }

    public float getHorizontalTexelOffsetRatio() {
        return this.blurSize;
    }
}
