package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter */
public class GPUImageHazeFilter extends GPUImageFilter {
    public static final String HAZE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform lowp float distance;\nuniform highp float slope;\n\nvoid main()\n{\n\t//todo reconsider precision modifiers\t \n\t highp vec4 color = vec4(1.0);//todo reimplement as a parameter\n\n\t highp float  d = textureCoordinate.y * slope  +  distance; \n\n\t highp vec4 c = texture2D(inputImageTexture, textureCoordinate) ; // consider using unpremultiply\n\n\t c = (c - d * color) / (1.0 -d);\n\n\t gl_FragColor = c; //consider using premultiply(c);\n}\n";
    private float mDistance;
    private int mDistanceLocation;
    private float mSlope;
    private int mSlopeLocation;

    public GPUImageHazeFilter() {
        this(0.2f, 0.0f);
    }

    public GPUImageHazeFilter(float distance, float slope) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, HAZE_FRAGMENT_SHADER);
        this.mDistance = distance;
        this.mSlope = slope;
    }

    public void onInit() {
        super.onInit();
        this.mDistanceLocation = GLES20.glGetUniformLocation(getProgram(), "distance");
        this.mSlopeLocation = GLES20.glGetUniformLocation(getProgram(), "slope");
    }

    public void onInitialized() {
        super.onInitialized();
        setDistance(this.mDistance);
        setSlope(this.mSlope);
    }

    public void setDistance(float distance) {
        this.mDistance = distance;
        setFloat(this.mDistanceLocation, distance);
    }

    public void setSlope(float slope) {
        this.mSlope = slope;
        setFloat(this.mSlopeLocation, slope);
    }
}
