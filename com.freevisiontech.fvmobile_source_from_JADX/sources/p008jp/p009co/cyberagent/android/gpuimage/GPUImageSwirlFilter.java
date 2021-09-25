package p008jp.p009co.cyberagent.android.gpuimage;

import android.graphics.PointF;
import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter */
public class GPUImageSwirlFilter extends GPUImageFilter {
    public static final String SWIRL_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float angle;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = textureCoordinate;\nhighp float dist = distance(center, textureCoordinate);\nif (dist < radius)\n{\ntextureCoordinateToUse -= center;\nhighp float percent = (radius - dist) / radius;\nhighp float theta = percent * percent * angle * 8.0;\nhighp float s = sin(theta);\nhighp float c = cos(theta);\ntextureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));\ntextureCoordinateToUse += center;\n}\n\ngl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );\n\n}\n";
    private float mAngle;
    private int mAngleLocation;
    private PointF mCenter;
    private int mCenterLocation;
    private float mRadius;
    private int mRadiusLocation;

    public GPUImageSwirlFilter() {
        this(0.5f, 1.0f, new PointF(0.5f, 0.5f));
    }

    public GPUImageSwirlFilter(float radius, float angle, PointF center) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, SWIRL_FRAGMENT_SHADER);
        this.mRadius = radius;
        this.mAngle = angle;
        this.mCenter = center;
    }

    public void onInit() {
        super.onInit();
        this.mAngleLocation = GLES20.glGetUniformLocation(getProgram(), "angle");
        this.mRadiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        this.mCenterLocation = GLES20.glGetUniformLocation(getProgram(), TtmlNode.CENTER);
    }

    public void onInitialized() {
        super.onInitialized();
        setRadius(this.mRadius);
        setAngle(this.mAngle);
        setCenter(this.mCenter);
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        setFloat(this.mRadiusLocation, radius);
    }

    public void setAngle(float angle) {
        this.mAngle = angle;
        setFloat(this.mAngleLocation, angle);
    }

    public void setCenter(PointF center) {
        this.mCenter = center;
        setPoint(this.mCenterLocation, center);
    }
}
