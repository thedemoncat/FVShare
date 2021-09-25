package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter */
public class GPUImageOpacityFilter extends GPUImageFilter {
    public static final String OPACITY_FRAGMENT_SHADER = "  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform lowp float opacity;\n  \n  void main()\n  {\n      lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n      \n      gl_FragColor = vec4(textureColor.rgb, textureColor.a * opacity);\n  }\n";
    private float mOpacity;
    private int mOpacityLocation;

    public GPUImageOpacityFilter() {
        this(1.0f);
    }

    public GPUImageOpacityFilter(float opacity) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, OPACITY_FRAGMENT_SHADER);
        this.mOpacity = opacity;
    }

    public void onInit() {
        super.onInit();
        this.mOpacityLocation = GLES20.glGetUniformLocation(getProgram(), "opacity");
    }

    public void onInitialized() {
        super.onInitialized();
        setOpacity(this.mOpacity);
    }

    public void setOpacity(float opacity) {
        this.mOpacity = opacity;
        setFloat(this.mOpacityLocation, this.mOpacity);
    }
}
