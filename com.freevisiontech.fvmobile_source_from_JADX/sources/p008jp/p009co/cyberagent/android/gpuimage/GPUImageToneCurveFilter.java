package p008jp.p009co.cyberagent.android.gpuimage;

import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLES20;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter */
public class GPUImageToneCurveFilter extends GPUImageFilter {
    public static final String TONE_CURVE_FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n uniform sampler2D toneCurveTexture;\n\n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     lowp float redCurveValue = texture2D(toneCurveTexture, vec2(textureColor.r, 0.0)).r;\n     lowp float greenCurveValue = texture2D(toneCurveTexture, vec2(textureColor.g, 0.0)).g;\n     lowp float blueCurveValue = texture2D(toneCurveTexture, vec2(textureColor.b, 0.0)).b;\n\n     gl_FragColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, textureColor.a);\n }";
    private PointF[] mBlueControlPoints;
    /* access modifiers changed from: private */
    public ArrayList<Float> mBlueCurve;
    private PointF[] mGreenControlPoints;
    /* access modifiers changed from: private */
    public ArrayList<Float> mGreenCurve;
    private PointF[] mRedControlPoints;
    /* access modifiers changed from: private */
    public ArrayList<Float> mRedCurve;
    private PointF[] mRgbCompositeControlPoints;
    /* access modifiers changed from: private */
    public ArrayList<Float> mRgbCompositeCurve;
    /* access modifiers changed from: private */
    public int[] mToneCurveTexture = {-1};
    private int mToneCurveTextureUniformLocation;

    public GPUImageToneCurveFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, TONE_CURVE_FRAGMENT_SHADER);
        PointF[] defaultCurvePoints = {new PointF(0.0f, 0.0f), new PointF(0.5f, 0.5f), new PointF(1.0f, 1.0f)};
        this.mRgbCompositeControlPoints = defaultCurvePoints;
        this.mRedControlPoints = defaultCurvePoints;
        this.mGreenControlPoints = defaultCurvePoints;
        this.mBlueControlPoints = defaultCurvePoints;
    }

    public void onInit() {
        super.onInit();
        this.mToneCurveTextureUniformLocation = GLES20.glGetUniformLocation(getProgram(), "toneCurveTexture");
        GLES20.glActiveTexture(33987);
        GLES20.glGenTextures(1, this.mToneCurveTexture, 0);
        GLES20.glBindTexture(3553, this.mToneCurveTexture[0]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
    }

    public void onInitialized() {
        super.onInitialized();
        setRgbCompositeControlPoints(this.mRgbCompositeControlPoints);
        setRedControlPoints(this.mRedControlPoints);
        setGreenControlPoints(this.mGreenControlPoints);
        setBlueControlPoints(this.mBlueControlPoints);
    }

    /* access modifiers changed from: protected */
    public void onDrawArraysPre() {
        if (this.mToneCurveTexture[0] != -1) {
            GLES20.glActiveTexture(33987);
            GLES20.glBindTexture(3553, this.mToneCurveTexture[0]);
            GLES20.glUniform1i(this.mToneCurveTextureUniformLocation, 3);
        }
    }

    public void setFromCurveFileInputStream(InputStream input) {
        try {
            short readShort = readShort(input);
            int totalCurves = readShort(input);
            ArrayList<PointF[]> curves = new ArrayList<>(totalCurves);
            for (int i = 0; i < totalCurves; i++) {
                int pointCount = readShort(input);
                PointF[] points = new PointF[pointCount];
                for (int j = 0; j < pointCount; j++) {
                    points[j] = new PointF(((float) readShort(input)) * 0.003921569f, ((float) readShort(input)) * 0.003921569f);
                }
                curves.add(points);
            }
            input.close();
            this.mRgbCompositeControlPoints = curves.get(0);
            this.mRedControlPoints = curves.get(1);
            this.mGreenControlPoints = curves.get(2);
            this.mBlueControlPoints = curves.get(3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private short readShort(InputStream input) throws IOException {
        return (short) ((input.read() << 8) | input.read());
    }

    public void setRgbCompositeControlPoints(PointF[] points) {
        this.mRgbCompositeControlPoints = points;
        this.mRgbCompositeCurve = createSplineCurve(this.mRgbCompositeControlPoints);
        updateToneCurveTexture();
    }

    public void setRedControlPoints(PointF[] points) {
        this.mRedControlPoints = points;
        this.mRedCurve = createSplineCurve(this.mRedControlPoints);
        updateToneCurveTexture();
    }

    public void setGreenControlPoints(PointF[] points) {
        this.mGreenControlPoints = points;
        this.mGreenCurve = createSplineCurve(this.mGreenControlPoints);
        updateToneCurveTexture();
    }

    public void setBlueControlPoints(PointF[] points) {
        this.mBlueControlPoints = points;
        this.mBlueCurve = createSplineCurve(this.mBlueControlPoints);
        updateToneCurveTexture();
    }

    private void updateToneCurveTexture() {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glActiveTexture(33987);
                GLES20.glBindTexture(3553, GPUImageToneCurveFilter.this.mToneCurveTexture[0]);
                if (GPUImageToneCurveFilter.this.mRedCurve.size() >= 256 && GPUImageToneCurveFilter.this.mGreenCurve.size() >= 256 && GPUImageToneCurveFilter.this.mBlueCurve.size() >= 256 && GPUImageToneCurveFilter.this.mRgbCompositeCurve.size() >= 256) {
                    byte[] toneCurveByteArray = new byte[1024];
                    for (int currentCurveIndex = 0; currentCurveIndex < 256; currentCurveIndex++) {
                        toneCurveByteArray[(currentCurveIndex * 4) + 2] = (byte) (((int) Math.min(Math.max(((Float) GPUImageToneCurveFilter.this.mRgbCompositeCurve.get(currentCurveIndex)).floatValue() + ((float) currentCurveIndex) + ((Float) GPUImageToneCurveFilter.this.mBlueCurve.get(currentCurveIndex)).floatValue(), 0.0f), 255.0f)) & 255);
                        toneCurveByteArray[(currentCurveIndex * 4) + 1] = (byte) (((int) Math.min(Math.max(((Float) GPUImageToneCurveFilter.this.mRgbCompositeCurve.get(currentCurveIndex)).floatValue() + ((float) currentCurveIndex) + ((Float) GPUImageToneCurveFilter.this.mGreenCurve.get(currentCurveIndex)).floatValue(), 0.0f), 255.0f)) & 255);
                        toneCurveByteArray[currentCurveIndex * 4] = (byte) (((int) Math.min(Math.max(((Float) GPUImageToneCurveFilter.this.mRgbCompositeCurve.get(currentCurveIndex)).floatValue() + ((float) currentCurveIndex) + ((Float) GPUImageToneCurveFilter.this.mRedCurve.get(currentCurveIndex)).floatValue(), 0.0f), 255.0f)) & 255);
                        toneCurveByteArray[(currentCurveIndex * 4) + 3] = -1;
                    }
                    GLES20.glTexImage2D(3553, 0, 6408, 256, 1, 0, 6408, 5121, ByteBuffer.wrap(toneCurveByteArray));
                }
            }
        });
    }

    private ArrayList<Float> createSplineCurve(PointF[] points) {
        if (points == null || points.length <= 0) {
            return null;
        }
        PointF[] pointsSorted = (PointF[]) points.clone();
        Arrays.sort(pointsSorted, new Comparator<PointF>() {
            public int compare(PointF point1, PointF point2) {
                if (point1.x < point2.x) {
                    return -1;
                }
                if (point1.x > point2.x) {
                    return 1;
                }
                return 0;
            }
        });
        Point[] convertedPoints = new Point[pointsSorted.length];
        for (int i = 0; i < points.length; i++) {
            PointF point = pointsSorted[i];
            convertedPoints[i] = new Point((int) (point.x * 255.0f), (int) (point.y * 255.0f));
        }
        ArrayList<Point> splinePoints = createSplineCurve2(convertedPoints);
        Point firstSplinePoint = splinePoints.get(0);
        if (firstSplinePoint.x > 0) {
            for (int i2 = firstSplinePoint.x; i2 >= 0; i2--) {
                splinePoints.add(0, new Point(i2, 0));
            }
        }
        Point lastSplinePoint = splinePoints.get(splinePoints.size() - 1);
        if (lastSplinePoint.x < 255) {
            for (int i3 = lastSplinePoint.x + 1; i3 <= 255; i3++) {
                splinePoints.add(new Point(i3, 255));
            }
        }
        ArrayList<Float> preparedSplinePoints = new ArrayList<>(splinePoints.size());
        Iterator<Point> it = splinePoints.iterator();
        while (it.hasNext()) {
            Point newPoint = it.next();
            Point origPoint = new Point(newPoint.x, newPoint.x);
            float distance = (float) Math.sqrt(Math.pow((double) (origPoint.x - newPoint.x), 2.0d) + Math.pow((double) (origPoint.y - newPoint.y), 2.0d));
            if (origPoint.y > newPoint.y) {
                distance = -distance;
            }
            preparedSplinePoints.add(Float.valueOf(distance));
        }
        return preparedSplinePoints;
    }

    private ArrayList<Point> createSplineCurve2(Point[] points) {
        ArrayList<Double> sdA = createSecondDerivative(points);
        int n = sdA.size();
        if (n < 1) {
            return null;
        }
        double[] sd = new double[n];
        for (int i = 0; i < n; i++) {
            sd[i] = sdA.get(i).doubleValue();
        }
        ArrayList<Point> output = new ArrayList<>(n + 1);
        for (int i2 = 0; i2 < n - 1; i2++) {
            Point cur = points[i2];
            Point next = points[i2 + 1];
            for (int x = cur.x; x < next.x; x++) {
                double t = ((double) (x - cur.x)) / ((double) (next.x - cur.x));
                double a = 1.0d - t;
                double b = t;
                double h = (double) (next.x - cur.x);
                double y = (((double) cur.y) * a) + (((double) next.y) * b) + (((h * h) / 6.0d) * (((((a * a) * a) - a) * sd[i2]) + ((((b * b) * b) - b) * sd[i2 + 1])));
                if (y > 255.0d) {
                    y = 255.0d;
                } else if (y < 0.0d) {
                    y = 0.0d;
                }
                output.add(new Point(x, (int) Math.round(y)));
            }
        }
        if (output.size() != 255) {
            return output;
        }
        output.add(points[points.length - 1]);
        return output;
    }

    private ArrayList<Double> createSecondDerivative(Point[] points) {
        int n = points.length;
        if (n <= 1) {
            return null;
        }
        double[][] matrix = (double[][]) Array.newInstance(Double.TYPE, new int[]{n, 3});
        double[] result = new double[n];
        matrix[0][1] = 1.0d;
        matrix[0][0] = 0.0d;
        matrix[0][2] = 0.0d;
        for (int i = 1; i < n - 1; i++) {
            Point P1 = points[i - 1];
            Point P2 = points[i];
            Point P3 = points[i + 1];
            matrix[i][0] = ((double) (P2.x - P1.x)) / 6.0d;
            matrix[i][1] = ((double) (P3.x - P1.x)) / 3.0d;
            matrix[i][2] = ((double) (P3.x - P2.x)) / 6.0d;
            result[i] = (((double) (P3.y - P2.y)) / ((double) (P3.x - P2.x))) - (((double) (P2.y - P1.y)) / ((double) (P2.x - P1.x)));
        }
        result[0] = 0.0d;
        result[n - 1] = 0.0d;
        matrix[n - 1][1] = 1.0d;
        matrix[n - 1][0] = 0.0d;
        matrix[n - 1][2] = 0.0d;
        for (int i2 = 1; i2 < n; i2++) {
            double k = matrix[i2][0] / matrix[i2 - 1][1];
            double[] dArr = matrix[i2];
            dArr[1] = dArr[1] - (matrix[i2 - 1][2] * k);
            matrix[i2][0] = 0.0d;
            result[i2] = result[i2] - (result[i2 - 1] * k);
        }
        for (int i3 = n - 2; i3 >= 0; i3--) {
            double k2 = matrix[i3][2] / matrix[i3 + 1][1];
            double[] dArr2 = matrix[i3];
            dArr2[1] = dArr2[1] - (matrix[i3 + 1][0] * k2);
            matrix[i3][2] = 0.0d;
            result[i3] = result[i3] - (result[i3 + 1] * k2);
        }
        ArrayList<Double> output = new ArrayList<>(n);
        for (int i4 = 0; i4 < n; i4++) {
            output.add(Double.valueOf(result[i4] / matrix[i4][1]));
        }
        return output;
    }
}
