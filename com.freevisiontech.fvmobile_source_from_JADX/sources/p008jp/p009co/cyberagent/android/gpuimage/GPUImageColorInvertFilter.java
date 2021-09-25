package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter */
public class GPUImageColorInvertFilter extends GPUImageFilter {
    public static final String COLOR_INVERT_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nvoid main()\n{\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    \n    gl_FragColor = vec4((1.0 - textureColor.rgb), textureColor.w);\n}";

    public GPUImageColorInvertFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, COLOR_INVERT_FRAGMENT_SHADER);
    }
}
