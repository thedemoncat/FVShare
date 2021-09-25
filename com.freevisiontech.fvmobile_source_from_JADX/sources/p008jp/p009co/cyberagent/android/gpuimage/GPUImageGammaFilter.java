package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter */
public class GPUImageGammaFilter extends GPUImageFilter {
    public static final String GAMMA_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float gamma;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);\n }";
    private float mGamma;
    private int mGammaLocation;

    public GPUImageGammaFilter() {
        this(1.2f);
    }

    public GPUImageGammaFilter(float gamma) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, GAMMA_FRAGMENT_SHADER);
        this.mGamma = gamma;
    }

    public void onInit() {
        super.onInit();
        this.mGammaLocation = GLES20.glGetUniformLocation(getProgram(), "gamma");
    }

    public void onInitialized() {
        super.onInitialized();
        setGamma(this.mGamma);
    }

    public void setGamma(float gamma) {
        this.mGamma = gamma;
        setFloat(this.mGammaLocation, this.mGamma);
    }
}
