package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageDifferenceBlendFilter */
public class GPUImageDifferenceBlendFilter extends GPUImageTwoInputFilter {
    public static final String DIFFERENCE_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n     gl_FragColor = vec4(abs(textureColor2.rgb - textureColor.rgb), textureColor.a);\n }";

    public GPUImageDifferenceBlendFilter() {
        super(DIFFERENCE_BLEND_FRAGMENT_SHADER);
    }
}
