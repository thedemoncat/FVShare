package org.opencv.imgproc;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat4;
import org.opencv.core.MatOfFloat6;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.utils.Converters;

public class Subdiv2D {
    public static final int NEXT_AROUND_DST = 34;
    public static final int NEXT_AROUND_LEFT = 19;
    public static final int NEXT_AROUND_ORG = 0;
    public static final int NEXT_AROUND_RIGHT = 49;
    public static final int PREV_AROUND_DST = 51;
    public static final int PREV_AROUND_LEFT = 32;
    public static final int PREV_AROUND_ORG = 17;
    public static final int PREV_AROUND_RIGHT = 2;
    public static final int PTLOC_ERROR = -2;
    public static final int PTLOC_INSIDE = 0;
    public static final int PTLOC_ON_EDGE = 2;
    public static final int PTLOC_OUTSIDE_RECT = -1;
    public static final int PTLOC_VERTEX = 1;
    protected final long nativeObj;

    private static native long Subdiv2D_0(int i, int i2, int i3, int i4);

    private static native long Subdiv2D_1();

    private static native void delete(long j);

    private static native int edgeDst_0(long j, int i, double[] dArr);

    private static native int edgeDst_1(long j, int i);

    private static native int edgeOrg_0(long j, int i, double[] dArr);

    private static native int edgeOrg_1(long j, int i);

    private static native int findNearest_0(long j, double d, double d2, double[] dArr);

    private static native int findNearest_1(long j, double d, double d2);

    private static native void getEdgeList_0(long j, long j2);

    private static native int getEdge_0(long j, int i, int i2);

    private static native void getLeadingEdgeList_0(long j, long j2);

    private static native void getTriangleList_0(long j, long j2);

    private static native double[] getVertex_0(long j, int i, double[] dArr);

    private static native double[] getVertex_1(long j, int i);

    private static native void getVoronoiFacetList_0(long j, long j2, long j3, long j4);

    private static native void initDelaunay_0(long j, int i, int i2, int i3, int i4);

    private static native int insert_0(long j, double d, double d2);

    private static native void insert_1(long j, long j2);

    private static native int locate_0(long j, double d, double d2, double[] dArr, double[] dArr2);

    private static native int nextEdge_0(long j, int i);

    private static native int rotateEdge_0(long j, int i, int i2);

    private static native int symEdge_0(long j, int i);

    protected Subdiv2D(long addr) {
        this.nativeObj = addr;
    }

    public Subdiv2D(Rect rect) {
        this.nativeObj = Subdiv2D_0(rect.f1130x, rect.f1131y, rect.width, rect.height);
    }

    public Subdiv2D() {
        this.nativeObj = Subdiv2D_1();
    }

    public Point getVertex(int vertex, int[] firstEdge) {
        double[] firstEdge_out = new double[1];
        Point retVal = new Point(getVertex_0(this.nativeObj, vertex, firstEdge_out));
        if (firstEdge != null) {
            firstEdge[0] = (int) firstEdge_out[0];
        }
        return retVal;
    }

    public Point getVertex(int vertex) {
        return new Point(getVertex_1(this.nativeObj, vertex));
    }

    public int edgeDst(int edge, Point dstpt) {
        double[] dstpt_out = new double[2];
        int retVal = edgeDst_0(this.nativeObj, edge, dstpt_out);
        if (dstpt != null) {
            dstpt.f1125x = dstpt_out[0];
            dstpt.f1126y = dstpt_out[1];
        }
        return retVal;
    }

    public int edgeDst(int edge) {
        return edgeDst_1(this.nativeObj, edge);
    }

    public int edgeOrg(int edge, Point orgpt) {
        double[] orgpt_out = new double[2];
        int retVal = edgeOrg_0(this.nativeObj, edge, orgpt_out);
        if (orgpt != null) {
            orgpt.f1125x = orgpt_out[0];
            orgpt.f1126y = orgpt_out[1];
        }
        return retVal;
    }

    public int edgeOrg(int edge) {
        return edgeOrg_1(this.nativeObj, edge);
    }

    public int findNearest(Point pt, Point nearestPt) {
        double[] nearestPt_out = new double[2];
        int retVal = findNearest_0(this.nativeObj, pt.f1125x, pt.f1126y, nearestPt_out);
        if (nearestPt != null) {
            nearestPt.f1125x = nearestPt_out[0];
            nearestPt.f1126y = nearestPt_out[1];
        }
        return retVal;
    }

    public int findNearest(Point pt) {
        return findNearest_1(this.nativeObj, pt.f1125x, pt.f1126y);
    }

    public int getEdge(int edge, int nextEdgeType) {
        return getEdge_0(this.nativeObj, edge, nextEdgeType);
    }

    public int insert(Point pt) {
        return insert_0(this.nativeObj, pt.f1125x, pt.f1126y);
    }

    public int locate(Point pt, int[] edge, int[] vertex) {
        double[] edge_out = new double[1];
        double[] vertex_out = new double[1];
        int retVal = locate_0(this.nativeObj, pt.f1125x, pt.f1126y, edge_out, vertex_out);
        if (edge != null) {
            edge[0] = (int) edge_out[0];
        }
        if (vertex != null) {
            vertex[0] = (int) vertex_out[0];
        }
        return retVal;
    }

    public int nextEdge(int edge) {
        return nextEdge_0(this.nativeObj, edge);
    }

    public int rotateEdge(int edge, int rotate) {
        return rotateEdge_0(this.nativeObj, edge, rotate);
    }

    public int symEdge(int edge) {
        return symEdge_0(this.nativeObj, edge);
    }

    public void getEdgeList(MatOfFloat4 edgeList) {
        getEdgeList_0(this.nativeObj, edgeList.nativeObj);
    }

    public void getLeadingEdgeList(MatOfInt leadingEdgeList) {
        getLeadingEdgeList_0(this.nativeObj, leadingEdgeList.nativeObj);
    }

    public void getTriangleList(MatOfFloat6 triangleList) {
        getTriangleList_0(this.nativeObj, triangleList.nativeObj);
    }

    public void getVoronoiFacetList(MatOfInt idx, List<MatOfPoint2f> facetList, MatOfPoint2f facetCenters) {
        Mat facetList_mat = new Mat();
        getVoronoiFacetList_0(this.nativeObj, idx.nativeObj, facetList_mat.nativeObj, facetCenters.nativeObj);
        Converters.Mat_to_vector_vector_Point2f(facetList_mat, facetList);
        facetList_mat.release();
    }

    public void initDelaunay(Rect rect) {
        initDelaunay_0(this.nativeObj, rect.f1130x, rect.f1131y, rect.width, rect.height);
    }

    public void insert(MatOfPoint2f ptvec) {
        insert_1(this.nativeObj, ptvec.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
