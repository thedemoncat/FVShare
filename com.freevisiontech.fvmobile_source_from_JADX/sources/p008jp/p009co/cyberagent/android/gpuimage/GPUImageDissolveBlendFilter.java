package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter */
public class GPUImageDissolveBlendFilter extends GPUImageMixBlendFilter {
    public static final String DISSOLVE_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n uniform lowp float mixturePercent;\n \n void main()\n {\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n    \n    gl_FragColor = mix(textureColor, textureColor2, mixturePercent);\n }";

    public GPUImageDissolveBlendFilter() {
        super(DISSOLVE_BLEND_FRAGMENT_SHADER);
    }

    public GPUImageDissolveBlendFilter(float mix) {
        super(DISSOLVE_BLEND_FRAGMENT_SHADER, mix);
    }
}
