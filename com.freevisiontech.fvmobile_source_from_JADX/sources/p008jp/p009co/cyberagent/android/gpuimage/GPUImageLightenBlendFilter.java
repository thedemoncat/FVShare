package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageLightenBlendFilter */
public class GPUImageLightenBlendFilter extends GPUImageTwoInputFilter {
    public static final String LIGHTEN_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n void main()\n {\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n    \n    gl_FragColor = max(textureColor, textureColor2);\n }";

    public GPUImageLightenBlendFilter() {
        super(LIGHTEN_BLEND_FRAGMENT_SHADER);
    }
}
