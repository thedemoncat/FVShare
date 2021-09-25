package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageHalftoneFilter */
public class GPUImageHalftoneFilter extends GPUImageFilter {
    public static final String HALFTONE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform highp float fractionalWidthOfPixel;\nuniform highp float aspectRatio;\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\nvoid main()\n{\n  highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);\n  highp vec2 samplePos = textureCoordinate - mod(textureCoordinate, sampleDivisor) + 0.5 * sampleDivisor;\n  highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n  highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n  highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);\n  lowp vec3 sampledColor = texture2D(inputImageTexture, samplePos).rgb;\n  highp float dotScaling = 1.0 - dot(sampledColor, W);\n  lowp float checkForPresenceWithinDot = 1.0 - step(distanceFromSamplePoint, (fractionalWidthOfPixel * 0.5) * dotScaling);\n  gl_FragColor = vec4(vec3(checkForPresenceWithinDot), 1.0);\n}";
    private float mAspectRatio;
    private int mAspectRatioLocation;
    private float mFractionalWidthOfAPixel;
    private int mFractionalWidthOfPixelLocation;

    public GPUImageHalftoneFilter() {
        this(0.01f);
    }

    public GPUImageHalftoneFilter(float fractionalWidthOfAPixel) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, HALFTONE_FRAGMENT_SHADER);
        this.mFractionalWidthOfAPixel = fractionalWidthOfAPixel;
    }

    public void onInit() {
        super.onInit();
        this.mFractionalWidthOfPixelLocation = GLES20.glGetUniformLocation(getProgram(), "fractionalWidthOfPixel");
        this.mAspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
        setFractionalWidthOfAPixel(this.mFractionalWidthOfAPixel);
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setAspectRatio(((float) height) / ((float) width));
    }

    public void setFractionalWidthOfAPixel(float fractionalWidthOfAPixel) {
        this.mFractionalWidthOfAPixel = fractionalWidthOfAPixel;
        setFloat(this.mFractionalWidthOfPixelLocation, this.mFractionalWidthOfAPixel);
    }

    public void setAspectRatio(float aspectRatio) {
        this.mAspectRatio = aspectRatio;
        setFloat(this.mAspectRatioLocation, this.mAspectRatio);
    }
}
