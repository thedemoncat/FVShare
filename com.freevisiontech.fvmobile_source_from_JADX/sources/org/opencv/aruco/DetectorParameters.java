package org.opencv.aruco;

public class DetectorParameters {
    protected final long nativeObj;

    private static native long create_0();

    private static native void delete(long j);

    private static native double get_adaptiveThreshConstant_0(long j);

    private static native int get_adaptiveThreshWinSizeMax_0(long j);

    private static native int get_adaptiveThreshWinSizeMin_0(long j);

    private static native int get_adaptiveThreshWinSizeStep_0(long j);

    private static native int get_cornerRefinementMaxIterations_0(long j);

    private static native double get_cornerRefinementMinAccuracy_0(long j);

    private static native int get_cornerRefinementWinSize_0(long j);

    private static native boolean get_doCornerRefinement_0(long j);

    private static native double get_errorCorrectionRate_0(long j);

    private static native int get_markerBorderBits_0(long j);

    private static native double get_maxErroneousBitsInBorderRate_0(long j);

    private static native double get_maxMarkerPerimeterRate_0(long j);

    private static native double get_minCornerDistanceRate_0(long j);

    private static native int get_minDistanceToBorder_0(long j);

    private static native double get_minMarkerDistanceRate_0(long j);

    private static native double get_minMarkerPerimeterRate_0(long j);

    private static native double get_minOtsuStdDev_0(long j);

    private static native double get_perspectiveRemoveIgnoredMarginPerCell_0(long j);

    private static native int get_perspectiveRemovePixelPerCell_0(long j);

    private static native double get_polygonalApproxAccuracyRate_0(long j);

    private static native void set_adaptiveThreshConstant_0(long j, double d);

    private static native void set_adaptiveThreshWinSizeMax_0(long j, int i);

    private static native void set_adaptiveThreshWinSizeMin_0(long j, int i);

    private static native void set_adaptiveThreshWinSizeStep_0(long j, int i);

    private static native void set_cornerRefinementMaxIterations_0(long j, int i);

    private static native void set_cornerRefinementMinAccuracy_0(long j, double d);

    private static native void set_cornerRefinementWinSize_0(long j, int i);

    private static native void set_doCornerRefinement_0(long j, boolean z);

    private static native void set_errorCorrectionRate_0(long j, double d);

    private static native void set_markerBorderBits_0(long j, int i);

    private static native void set_maxErroneousBitsInBorderRate_0(long j, double d);

    private static native void set_maxMarkerPerimeterRate_0(long j, double d);

    private static native void set_minCornerDistanceRate_0(long j, double d);

    private static native void set_minDistanceToBorder_0(long j, int i);

    private static native void set_minMarkerDistanceRate_0(long j, double d);

    private static native void set_minMarkerPerimeterRate_0(long j, double d);

    private static native void set_minOtsuStdDev_0(long j, double d);

    private static native void set_perspectiveRemoveIgnoredMarginPerCell_0(long j, double d);

    private static native void set_perspectiveRemovePixelPerCell_0(long j, int i);

    private static native void set_polygonalApproxAccuracyRate_0(long j, double d);

    protected DetectorParameters(long addr) {
        this.nativeObj = addr;
    }

    public static DetectorParameters create() {
        return new DetectorParameters(create_0());
    }

    public int get_adaptiveThreshWinSizeMin() {
        return get_adaptiveThreshWinSizeMin_0(this.nativeObj);
    }

    public void set_adaptiveThreshWinSizeMin(int adaptiveThreshWinSizeMin) {
        set_adaptiveThreshWinSizeMin_0(this.nativeObj, adaptiveThreshWinSizeMin);
    }

    public int get_adaptiveThreshWinSizeMax() {
        return get_adaptiveThreshWinSizeMax_0(this.nativeObj);
    }

    public void set_adaptiveThreshWinSizeMax(int adaptiveThreshWinSizeMax) {
        set_adaptiveThreshWinSizeMax_0(this.nativeObj, adaptiveThreshWinSizeMax);
    }

    public int get_adaptiveThreshWinSizeStep() {
        return get_adaptiveThreshWinSizeStep_0(this.nativeObj);
    }

    public void set_adaptiveThreshWinSizeStep(int adaptiveThreshWinSizeStep) {
        set_adaptiveThreshWinSizeStep_0(this.nativeObj, adaptiveThreshWinSizeStep);
    }

    public double get_adaptiveThreshConstant() {
        return get_adaptiveThreshConstant_0(this.nativeObj);
    }

    public void set_adaptiveThreshConstant(double adaptiveThreshConstant) {
        set_adaptiveThreshConstant_0(this.nativeObj, adaptiveThreshConstant);
    }

    public double get_minMarkerPerimeterRate() {
        return get_minMarkerPerimeterRate_0(this.nativeObj);
    }

    public void set_minMarkerPerimeterRate(double minMarkerPerimeterRate) {
        set_minMarkerPerimeterRate_0(this.nativeObj, minMarkerPerimeterRate);
    }

    public double get_maxMarkerPerimeterRate() {
        return get_maxMarkerPerimeterRate_0(this.nativeObj);
    }

    public void set_maxMarkerPerimeterRate(double maxMarkerPerimeterRate) {
        set_maxMarkerPerimeterRate_0(this.nativeObj, maxMarkerPerimeterRate);
    }

    public double get_polygonalApproxAccuracyRate() {
        return get_polygonalApproxAccuracyRate_0(this.nativeObj);
    }

    public void set_polygonalApproxAccuracyRate(double polygonalApproxAccuracyRate) {
        set_polygonalApproxAccuracyRate_0(this.nativeObj, polygonalApproxAccuracyRate);
    }

    public double get_minCornerDistanceRate() {
        return get_minCornerDistanceRate_0(this.nativeObj);
    }

    public void set_minCornerDistanceRate(double minCornerDistanceRate) {
        set_minCornerDistanceRate_0(this.nativeObj, minCornerDistanceRate);
    }

    public int get_minDistanceToBorder() {
        return get_minDistanceToBorder_0(this.nativeObj);
    }

    public void set_minDistanceToBorder(int minDistanceToBorder) {
        set_minDistanceToBorder_0(this.nativeObj, minDistanceToBorder);
    }

    public double get_minMarkerDistanceRate() {
        return get_minMarkerDistanceRate_0(this.nativeObj);
    }

    public void set_minMarkerDistanceRate(double minMarkerDistanceRate) {
        set_minMarkerDistanceRate_0(this.nativeObj, minMarkerDistanceRate);
    }

    public boolean get_doCornerRefinement() {
        return get_doCornerRefinement_0(this.nativeObj);
    }

    public void set_doCornerRefinement(boolean doCornerRefinement) {
        set_doCornerRefinement_0(this.nativeObj, doCornerRefinement);
    }

    public int get_cornerRefinementWinSize() {
        return get_cornerRefinementWinSize_0(this.nativeObj);
    }

    public void set_cornerRefinementWinSize(int cornerRefinementWinSize) {
        set_cornerRefinementWinSize_0(this.nativeObj, cornerRefinementWinSize);
    }

    public int get_cornerRefinementMaxIterations() {
        return get_cornerRefinementMaxIterations_0(this.nativeObj);
    }

    public void set_cornerRefinementMaxIterations(int cornerRefinementMaxIterations) {
        set_cornerRefinementMaxIterations_0(this.nativeObj, cornerRefinementMaxIterations);
    }

    public double get_cornerRefinementMinAccuracy() {
        return get_cornerRefinementMinAccuracy_0(this.nativeObj);
    }

    public void set_cornerRefinementMinAccuracy(double cornerRefinementMinAccuracy) {
        set_cornerRefinementMinAccuracy_0(this.nativeObj, cornerRefinementMinAccuracy);
    }

    public int get_markerBorderBits() {
        return get_markerBorderBits_0(this.nativeObj);
    }

    public void set_markerBorderBits(int markerBorderBits) {
        set_markerBorderBits_0(this.nativeObj, markerBorderBits);
    }

    public int get_perspectiveRemovePixelPerCell() {
        return get_perspectiveRemovePixelPerCell_0(this.nativeObj);
    }

    public void set_perspectiveRemovePixelPerCell(int perspectiveRemovePixelPerCell) {
        set_perspectiveRemovePixelPerCell_0(this.nativeObj, perspectiveRemovePixelPerCell);
    }

    public double get_perspectiveRemoveIgnoredMarginPerCell() {
        return get_perspectiveRemoveIgnoredMarginPerCell_0(this.nativeObj);
    }

    public void set_perspectiveRemoveIgnoredMarginPerCell(double perspectiveRemoveIgnoredMarginPerCell) {
        set_perspectiveRemoveIgnoredMarginPerCell_0(this.nativeObj, perspectiveRemoveIgnoredMarginPerCell);
    }

    public double get_maxErroneousBitsInBorderRate() {
        return get_maxErroneousBitsInBorderRate_0(this.nativeObj);
    }

    public void set_maxErroneousBitsInBorderRate(double maxErroneousBitsInBorderRate) {
        set_maxErroneousBitsInBorderRate_0(this.nativeObj, maxErroneousBitsInBorderRate);
    }

    public double get_minOtsuStdDev() {
        return get_minOtsuStdDev_0(this.nativeObj);
    }

    public void set_minOtsuStdDev(double minOtsuStdDev) {
        set_minOtsuStdDev_0(this.nativeObj, minOtsuStdDev);
    }

    public double get_errorCorrectionRate() {
        return get_errorCorrectionRate_0(this.nativeObj);
    }

    public void set_errorCorrectionRate(double errorCorrectionRate) {
        set_errorCorrectionRate_0(this.nativeObj, errorCorrectionRate);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
