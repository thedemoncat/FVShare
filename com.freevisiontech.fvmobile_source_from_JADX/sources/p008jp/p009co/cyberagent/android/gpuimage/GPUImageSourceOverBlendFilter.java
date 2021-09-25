package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageSourceOverBlendFilter */
public class GPUImageSourceOverBlendFilter extends GPUImageTwoInputFilter {
    public static final String SOURCE_OVER_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n \n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n   lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n   lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n   \n   gl_FragColor = mix(textureColor, textureColor2, textureColor2.a);\n }";

    public GPUImageSourceOverBlendFilter() {
        super(SOURCE_OVER_BLEND_FRAGMENT_SHADER);
    }
}
