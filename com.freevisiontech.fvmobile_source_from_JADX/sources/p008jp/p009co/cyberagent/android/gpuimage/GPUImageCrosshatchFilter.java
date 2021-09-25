package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter */
public class GPUImageCrosshatchFilter extends GPUImageFilter {
    public static final String CROSSHATCH_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform highp float crossHatchSpacing;\nuniform highp float lineWidth;\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\nvoid main()\n{\nhighp float luminance = dot(texture2D(inputImageTexture, textureCoordinate).rgb, W);\nlowp vec4 colorToDisplay = vec4(1.0, 1.0, 1.0, 1.0);\nif (luminance < 1.00)\n{\nif (mod(textureCoordinate.x + textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.75)\n{\nif (mod(textureCoordinate.x - textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.50)\n{\nif (mod(textureCoordinate.x + textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.3)\n{\nif (mod(textureCoordinate.x - textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\ngl_FragColor = colorToDisplay;\n}\n";
    private float mCrossHatchSpacing;
    private int mCrossHatchSpacingLocation;
    private float mLineWidth;
    private int mLineWidthLocation;

    public GPUImageCrosshatchFilter() {
        this(0.03f, 0.003f);
    }

    public GPUImageCrosshatchFilter(float crossHatchSpacing, float lineWidth) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, CROSSHATCH_FRAGMENT_SHADER);
        this.mCrossHatchSpacing = crossHatchSpacing;
        this.mLineWidth = lineWidth;
    }

    public void onInit() {
        super.onInit();
        this.mCrossHatchSpacingLocation = GLES20.glGetUniformLocation(getProgram(), "crossHatchSpacing");
        this.mLineWidthLocation = GLES20.glGetUniformLocation(getProgram(), "lineWidth");
    }

    public void onInitialized() {
        super.onInitialized();
        setCrossHatchSpacing(this.mCrossHatchSpacing);
        setLineWidth(this.mLineWidth);
    }

    public void setCrossHatchSpacing(float crossHatchSpacing) {
        float singlePixelSpacing;
        if (getOutputWidth() != 0) {
            singlePixelSpacing = 1.0f / ((float) getOutputWidth());
        } else {
            singlePixelSpacing = 4.8828125E-4f;
        }
        if (crossHatchSpacing < singlePixelSpacing) {
            this.mCrossHatchSpacing = singlePixelSpacing;
        } else {
            this.mCrossHatchSpacing = crossHatchSpacing;
        }
        setFloat(this.mCrossHatchSpacingLocation, this.mCrossHatchSpacing);
    }

    public void setLineWidth(float lineWidth) {
        this.mLineWidth = lineWidth;
        setFloat(this.mLineWidthLocation, this.mLineWidth);
    }
}
