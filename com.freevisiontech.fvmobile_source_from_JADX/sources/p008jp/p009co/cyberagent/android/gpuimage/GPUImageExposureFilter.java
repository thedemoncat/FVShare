package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter */
public class GPUImageExposureFilter extends GPUImageFilter {
    public static final String EXPOSURE_FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform highp float exposure;\n \n void main()\n {\n     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);\n } ";
    private float mExposure;
    private int mExposureLocation;

    public GPUImageExposureFilter() {
        this(1.0f);
    }

    public GPUImageExposureFilter(float exposure) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, EXPOSURE_FRAGMENT_SHADER);
        this.mExposure = exposure;
    }

    public void onInit() {
        super.onInit();
        this.mExposureLocation = GLES20.glGetUniformLocation(getProgram(), "exposure");
    }

    public void onInitialized() {
        super.onInitialized();
        setExposure(this.mExposure);
    }

    public void setExposure(float exposure) {
        this.mExposure = exposure;
        setFloat(this.mExposureLocation, this.mExposure);
    }
}
