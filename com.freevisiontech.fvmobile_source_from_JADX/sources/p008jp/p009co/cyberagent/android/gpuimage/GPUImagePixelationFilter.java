package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter */
public class GPUImagePixelationFilter extends GPUImageFilter {
    public static final String PIXELATION_FRAGMENT_SHADER = "precision highp float;\nvarying vec2 textureCoordinate;\nuniform float imageWidthFactor;\nuniform float imageHeightFactor;\nuniform sampler2D inputImageTexture;\nuniform float pixel;\nvoid main()\n{\n  vec2 uv  = textureCoordinate.xy;\n  float dx = pixel * imageWidthFactor;\n  float dy = pixel * imageHeightFactor;\n  vec2 coord = vec2(dx * floor(uv.x / dx), dy * floor(uv.y / dy));\n  vec3 tc = texture2D(inputImageTexture, coord).xyz;\n  gl_FragColor = vec4(tc, 1.0);\n}";
    private int mImageHeightFactorLocation;
    private int mImageWidthFactorLocation;
    private float mPixel = 1.0f;
    private int mPixelLocation;

    public GPUImagePixelationFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, PIXELATION_FRAGMENT_SHADER);
    }

    public void onInit() {
        super.onInit();
        this.mImageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
        this.mImageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
        this.mPixelLocation = GLES20.glGetUniformLocation(getProgram(), "pixel");
        setPixel(this.mPixel);
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setFloat(this.mImageWidthFactorLocation, 1.0f / ((float) width));
        setFloat(this.mImageHeightFactorLocation, 1.0f / ((float) height));
    }

    public void setPixel(float pixel) {
        this.mPixel = pixel;
        setFloat(this.mPixelLocation, this.mPixel);
    }
}
