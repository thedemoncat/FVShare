package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageLaplacianFilter */
public class GPUImageLaplacianFilter extends GPUImage3x3TextureSamplingFilter {
    public static final String LAPLACIAN_FRAGMENT_SHADER = "precision highp float;\n\nuniform sampler2D inputImageTexture;\n\nuniform mediump mat3 convolutionMatrix;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nvoid main()\n{\nmediump vec3 bottomColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\nmediump vec3 bottomLeftColor = texture2D(inputImageTexture, bottomLeftTextureCoordinate).rgb;\nmediump vec3 bottomRightColor = texture2D(inputImageTexture, bottomRightTextureCoordinate).rgb;\nmediump vec4 centerColor = texture2D(inputImageTexture, textureCoordinate);\nmediump vec3 leftColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\nmediump vec3 rightColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\nmediump vec3 topColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\nmediump vec3 topRightColor = texture2D(inputImageTexture, topRightTextureCoordinate).rgb;\nmediump vec3 topLeftColor = texture2D(inputImageTexture, topLeftTextureCoordinate).rgb;\n\nmediump vec3 resultColor = topLeftColor * convolutionMatrix[0][0] + topColor * convolutionMatrix[0][1] + topRightColor * convolutionMatrix[0][2];\nresultColor += leftColor * convolutionMatrix[1][0] + centerColor.rgb * convolutionMatrix[1][1] + rightColor * convolutionMatrix[1][2];\nresultColor += bottomLeftColor * convolutionMatrix[2][0] + bottomColor * convolutionMatrix[2][1] + bottomRightColor * convolutionMatrix[2][2];\n\n// Normalize the results to allow for negative gradients in the 0.0-1.0 colorspace\nresultColor = resultColor + 0.5;\n\ngl_FragColor = vec4(resultColor, centerColor.a);\n}\n";
    private float[] mConvolutionKernel;
    private int mUniformConvolutionMatrix;

    public GPUImageLaplacianFilter() {
        this(new float[]{0.5f, 1.0f, 0.5f, 1.0f, -6.0f, 1.0f, 0.5f, 1.0f, 0.5f});
    }

    private GPUImageLaplacianFilter(float[] convolutionKernel) {
        super(LAPLACIAN_FRAGMENT_SHADER);
        this.mConvolutionKernel = convolutionKernel;
    }

    public void onInit() {
        super.onInit();
        this.mUniformConvolutionMatrix = GLES20.glGetUniformLocation(getProgram(), "convolutionMatrix");
        setConvolutionKernel(this.mConvolutionKernel);
    }

    private void setConvolutionKernel(float[] convolutionKernel) {
        this.mConvolutionKernel = convolutionKernel;
        setUniformMatrix3f(this.mUniformConvolutionMatrix, this.mConvolutionKernel);
    }
}
