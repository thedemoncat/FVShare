package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageExclusionBlendFilter */
public class GPUImageExclusionBlendFilter extends GPUImageTwoInputFilter {
    public static final String EXCLUSION_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n     mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n     \n     //     Dca = (Sca.Da + Dca.Sa - 2.Sca.Dca) + Sca.(1 - Da) + Dca.(1 - Sa)\n     \n     gl_FragColor = vec4((overlay.rgb * base.a + base.rgb * overlay.a - 2.0 * overlay.rgb * base.rgb) + overlay.rgb * (1.0 - base.a) + base.rgb * (1.0 - overlay.a), base.a);\n }";

    public GPUImageExclusionBlendFilter() {
        super(EXCLUSION_BLEND_FRAGMENT_SHADER);
    }
}
