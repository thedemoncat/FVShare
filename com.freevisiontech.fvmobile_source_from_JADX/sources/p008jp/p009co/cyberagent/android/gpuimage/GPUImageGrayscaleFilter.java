package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter */
public class GPUImageGrayscaleFilter extends GPUImageFilter {
    public static final String GRAYSCALE_FRAGMENT_SHADER = "precision highp float;\n\nvarying vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\n  lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n  float luminance = dot(textureColor.rgb, W);\n\n  gl_FragColor = vec4(vec3(luminance), textureColor.a);\n}";

    public GPUImageGrayscaleFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, GRAYSCALE_FRAGMENT_SHADER);
    }
}
