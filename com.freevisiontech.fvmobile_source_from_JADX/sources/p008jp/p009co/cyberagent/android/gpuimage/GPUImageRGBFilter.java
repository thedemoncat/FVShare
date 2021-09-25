package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter */
public class GPUImageRGBFilter extends GPUImageFilter {
    public static final String RGB_FRAGMENT_SHADER = "  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform highp float red;\n  uniform highp float green;\n  uniform highp float blue;\n  \n  void main()\n  {\n      highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n      \n      gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);\n  }\n";
    private float mBlue;
    private int mBlueLocation;
    private float mGreen;
    private int mGreenLocation;
    private boolean mIsInitialized;
    private float mRed;
    private int mRedLocation;

    public GPUImageRGBFilter() {
        this(1.0f, 1.0f, 1.0f);
    }

    public GPUImageRGBFilter(float red, float green, float blue) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, RGB_FRAGMENT_SHADER);
        this.mIsInitialized = false;
        this.mRed = red;
        this.mGreen = green;
        this.mBlue = blue;
    }

    public void onInit() {
        super.onInit();
        this.mRedLocation = GLES20.glGetUniformLocation(getProgram(), "red");
        this.mGreenLocation = GLES20.glGetUniformLocation(getProgram(), "green");
        this.mBlueLocation = GLES20.glGetUniformLocation(getProgram(), "blue");
        this.mIsInitialized = true;
        setRed(this.mRed);
        setGreen(this.mGreen);
        setBlue(this.mBlue);
    }

    public void setRed(float red) {
        this.mRed = red;
        if (this.mIsInitialized) {
            setFloat(this.mRedLocation, this.mRed);
        }
    }

    public void setGreen(float green) {
        this.mGreen = green;
        if (this.mIsInitialized) {
            setFloat(this.mGreenLocation, this.mGreen);
        }
    }

    public void setBlue(float blue) {
        this.mBlue = blue;
        if (this.mIsInitialized) {
            setFloat(this.mBlueLocation, this.mBlue);
        }
    }
}
