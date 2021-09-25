package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter */
public class GPUImageMultiplyBlendFilter extends GPUImageTwoInputFilter {
    public static final String MULTIPLY_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n     lowp vec4 base = texture2D(inputImageTexture, textureCoordinate);\n     lowp vec4 overlayer = texture2D(inputImageTexture2, textureCoordinate2);\n          \n     gl_FragColor = overlayer * base + overlayer * (1.0 - base.a) + base * (1.0 - overlayer.a);\n }";

    public GPUImageMultiplyBlendFilter() {
        super(MULTIPLY_BLEND_FRAGMENT_SHADER);
    }
}
