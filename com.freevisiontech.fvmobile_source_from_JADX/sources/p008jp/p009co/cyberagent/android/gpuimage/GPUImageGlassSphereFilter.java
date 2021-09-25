package p008jp.p009co.cyberagent.android.gpuimage;

import android.graphics.PointF;
import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter */
public class GPUImageGlassSphereFilter extends GPUImageFilter {
    public static final String SPHERE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float aspectRatio;\nuniform highp float refractiveIndex;\n// uniform vec3 lightPosition;\nconst highp vec3 lightPosition = vec3(-0.5, 0.5, 1.0);\nconst highp vec3 ambientLightPosition = vec3(0.0, 0.0, 1.0);\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\nhighp float distanceFromCenter = distance(center, textureCoordinateToUse);\nlowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);\n\ndistanceFromCenter = distanceFromCenter / radius;\n\nhighp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);\nhighp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));\n\nhighp vec3 refractedVector = 2.0 * refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);\nrefractedVector.xy = -refractedVector.xy;\n\nhighp vec3 finalSphereColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5).rgb;\n\n// Grazing angle lighting\nhighp float lightingIntensity = 2.5 * (1.0 - pow(clamp(dot(ambientLightPosition, sphereNormal), 0.0, 1.0), 0.25));\nfinalSphereColor += lightingIntensity;\n\n// Specular lighting\nlightingIntensity  = clamp(dot(normalize(lightPosition), sphereNormal), 0.0, 1.0);\nlightingIntensity  = pow(lightingIntensity, 15.0);\nfinalSphereColor += vec3(0.8, 0.8, 0.8) * lightingIntensity;\n\ngl_FragColor = vec4(finalSphereColor, 1.0) * checkForPresenceWithinSphere;\n}\n";
    private float mAspectRatio;
    private int mAspectRatioLocation;
    private PointF mCenter;
    private int mCenterLocation;
    private float mRadius;
    private int mRadiusLocation;
    private float mRefractiveIndex;
    private int mRefractiveIndexLocation;

    public GPUImageGlassSphereFilter() {
        this(new PointF(0.5f, 0.5f), 0.25f, 0.71f);
    }

    public GPUImageGlassSphereFilter(PointF center, float radius, float refractiveIndex) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, SPHERE_FRAGMENT_SHADER);
        this.mCenter = center;
        this.mRadius = radius;
        this.mRefractiveIndex = refractiveIndex;
    }

    public void onInit() {
        super.onInit();
        this.mCenterLocation = GLES20.glGetUniformLocation(getProgram(), TtmlNode.CENTER);
        this.mRadiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        this.mAspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
        this.mRefractiveIndexLocation = GLES20.glGetUniformLocation(getProgram(), "refractiveIndex");
    }

    public void onInitialized() {
        super.onInitialized();
        setRadius(this.mRadius);
        setCenter(this.mCenter);
        setRefractiveIndex(this.mRefractiveIndex);
    }

    public void onOutputSizeChanged(int width, int height) {
        this.mAspectRatio = ((float) height) / ((float) width);
        setAspectRatio(this.mAspectRatio);
        super.onOutputSizeChanged(width, height);
    }

    private void setAspectRatio(float aspectRatio) {
        this.mAspectRatio = aspectRatio;
        setFloat(this.mAspectRatioLocation, aspectRatio);
    }

    public void setRefractiveIndex(float refractiveIndex) {
        this.mRefractiveIndex = refractiveIndex;
        setFloat(this.mRefractiveIndexLocation, refractiveIndex);
    }

    public void setCenter(PointF center) {
        this.mCenter = center;
        setPoint(this.mCenterLocation, center);
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        setFloat(this.mRadiusLocation, radius);
    }
}
