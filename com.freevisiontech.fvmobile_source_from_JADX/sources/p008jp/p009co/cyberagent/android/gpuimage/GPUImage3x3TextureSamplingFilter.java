package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter */
public class GPUImage3x3TextureSamplingFilter extends GPUImageFilter {
    public static final String THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform highp float texelWidth; \nuniform highp float texelHeight; \n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nvoid main()\n{\n    gl_Position = position;\n\n    vec2 widthStep = vec2(texelWidth, 0.0);\n    vec2 heightStep = vec2(0.0, texelHeight);\n    vec2 widthHeightStep = vec2(texelWidth, texelHeight);\n    vec2 widthNegativeHeightStep = vec2(texelWidth, -texelHeight);\n\n    textureCoordinate = inputTextureCoordinate.xy;\n    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n\n    topTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n    topLeftTextureCoordinate = inputTextureCoordinate.xy - widthHeightStep;\n    topRightTextureCoordinate = inputTextureCoordinate.xy + widthNegativeHeightStep;\n\n    bottomTextureCoordinate = inputTextureCoordinate.xy + heightStep;\n    bottomLeftTextureCoordinate = inputTextureCoordinate.xy - widthNegativeHeightStep;\n    bottomRightTextureCoordinate = inputTextureCoordinate.xy + widthHeightStep;\n}";
    private boolean mHasOverriddenImageSizeFactor;
    private float mLineSize;
    private float mTexelHeight;
    private float mTexelWidth;
    private int mUniformTexelHeightLocation;
    private int mUniformTexelWidthLocation;

    public GPUImage3x3TextureSamplingFilter() {
        this(GPUImageFilter.NO_FILTER_VERTEX_SHADER);
    }

    public GPUImage3x3TextureSamplingFilter(String fragmentShader) {
        super(THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER, fragmentShader);
        this.mHasOverriddenImageSizeFactor = false;
        this.mLineSize = 1.0f;
    }

    public void onInit() {
        super.onInit();
        this.mUniformTexelWidthLocation = GLES20.glGetUniformLocation(getProgram(), "texelWidth");
        this.mUniformTexelHeightLocation = GLES20.glGetUniformLocation(getProgram(), "texelHeight");
        if (this.mTexelWidth != 0.0f) {
            updateTexelValues();
        }
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        if (!this.mHasOverriddenImageSizeFactor) {
            setLineSize(this.mLineSize);
        }
    }

    public void setTexelWidth(float texelWidth) {
        this.mHasOverriddenImageSizeFactor = true;
        this.mTexelWidth = texelWidth;
        setFloat(this.mUniformTexelWidthLocation, texelWidth);
    }

    public void setTexelHeight(float texelHeight) {
        this.mHasOverriddenImageSizeFactor = true;
        this.mTexelHeight = texelHeight;
        setFloat(this.mUniformTexelHeightLocation, texelHeight);
    }

    public void setLineSize(float size) {
        this.mLineSize = size;
        this.mTexelWidth = size / ((float) getOutputWidth());
        this.mTexelHeight = size / ((float) getOutputHeight());
        updateTexelValues();
    }

    private void updateTexelValues() {
        setFloat(this.mUniformTexelWidthLocation, this.mTexelWidth);
        setFloat(this.mUniformTexelHeightLocation, this.mTexelHeight);
    }
}
