package org.opencv.xfeatures2d;

import java.util.List;
import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint2f;
import org.opencv.utils.Converters;

public class PCTSignatures extends Algorithm {
    public static final int GAUSSIAN = 1;
    public static final int HEURISTIC = 2;
    public static final int L0_25 = 0;
    public static final int L0_5 = 1;

    /* renamed from: L1 */
    public static final int f1135L1 = 2;

    /* renamed from: L2 */
    public static final int f1136L2 = 3;
    public static final int L2SQUARED = 4;

    /* renamed from: L5 */
    public static final int f1137L5 = 5;
    public static final int L_INFINITY = 6;
    public static final int MINUS = 0;
    public static final int NORMAL = 2;
    public static final int REGULAR = 1;
    public static final int UNIFORM = 0;

    private static native void computeSignature_0(long j, long j2, long j3);

    private static native void computeSignatures_0(long j, long j2, long j3);

    private static native long create_0(int i, int i2, int i3);

    private static native long create_1();

    private static native long create_2(long j, int i);

    private static native long create_3(long j, long j2);

    private static native void delete(long j);

    private static native void drawSignature_0(long j, long j2, long j3, float f, int i);

    private static native void drawSignature_1(long j, long j2, long j3);

    private static native void generateInitPoints_0(long j, int i, int i2);

    private static native int getClusterMinSize_0(long j);

    private static native int getDistanceFunction_0(long j);

    private static native float getDropThreshold_0(long j);

    private static native int getGrayscaleBits_0(long j);

    private static native int getInitSeedCount_0(long j);

    private static native long getInitSeedIndexes_0(long j);

    private static native int getIterationCount_0(long j);

    private static native float getJoiningDistance_0(long j);

    private static native int getMaxClustersCount_0(long j);

    private static native int getSampleCount_0(long j);

    private static native long getSamplingPoints_0(long j);

    private static native float getWeightA_0(long j);

    private static native float getWeightB_0(long j);

    private static native float getWeightConstrast_0(long j);

    private static native float getWeightEntropy_0(long j);

    private static native float getWeightL_0(long j);

    private static native float getWeightX_0(long j);

    private static native float getWeightY_0(long j);

    private static native int getWindowRadius_0(long j);

    private static native void setClusterMinSize_0(long j, int i);

    private static native void setDistanceFunction_0(long j, int i);

    private static native void setDropThreshold_0(long j, float f);

    private static native void setGrayscaleBits_0(long j, int i);

    private static native void setInitSeedIndexes_0(long j, long j2);

    private static native void setIterationCount_0(long j, int i);

    private static native void setJoiningDistance_0(long j, float f);

    private static native void setMaxClustersCount_0(long j, int i);

    private static native void setSamplingPoints_0(long j, long j2);

    private static native void setTranslation_0(long j, int i, float f);

    private static native void setTranslations_0(long j, long j2);

    private static native void setWeightA_0(long j, float f);

    private static native void setWeightB_0(long j, float f);

    private static native void setWeightContrast_0(long j, float f);

    private static native void setWeightEntropy_0(long j, float f);

    private static native void setWeightL_0(long j, float f);

    private static native void setWeightX_0(long j, float f);

    private static native void setWeightY_0(long j, float f);

    private static native void setWeight_0(long j, int i, float f);

    private static native void setWeights_0(long j, long j2);

    private static native void setWindowRadius_0(long j, int i);

    protected PCTSignatures(long addr) {
        super(addr);
    }

    public static PCTSignatures create(int initSampleCount, int initSeedCount, int pointDistribution) {
        return new PCTSignatures(create_0(initSampleCount, initSeedCount, pointDistribution));
    }

    public static PCTSignatures create() {
        return new PCTSignatures(create_1());
    }

    public static PCTSignatures create(MatOfPoint2f initSamplingPoints, int initSeedCount) {
        return new PCTSignatures(create_2(initSamplingPoints.nativeObj, initSeedCount));
    }

    public static PCTSignatures create(MatOfPoint2f initSamplingPoints, MatOfInt initClusterSeedIndexes) {
        return new PCTSignatures(create_3(initSamplingPoints.nativeObj, initClusterSeedIndexes.nativeObj));
    }

    public float getDropThreshold() {
        return getDropThreshold_0(this.nativeObj);
    }

    public float getJoiningDistance() {
        return getJoiningDistance_0(this.nativeObj);
    }

    public float getWeightA() {
        return getWeightA_0(this.nativeObj);
    }

    public float getWeightB() {
        return getWeightB_0(this.nativeObj);
    }

    public float getWeightConstrast() {
        return getWeightConstrast_0(this.nativeObj);
    }

    public float getWeightEntropy() {
        return getWeightEntropy_0(this.nativeObj);
    }

    public float getWeightL() {
        return getWeightL_0(this.nativeObj);
    }

    public float getWeightX() {
        return getWeightX_0(this.nativeObj);
    }

    public float getWeightY() {
        return getWeightY_0(this.nativeObj);
    }

    public int getClusterMinSize() {
        return getClusterMinSize_0(this.nativeObj);
    }

    public int getDistanceFunction() {
        return getDistanceFunction_0(this.nativeObj);
    }

    public int getGrayscaleBits() {
        return getGrayscaleBits_0(this.nativeObj);
    }

    public int getInitSeedCount() {
        return getInitSeedCount_0(this.nativeObj);
    }

    public int getIterationCount() {
        return getIterationCount_0(this.nativeObj);
    }

    public int getMaxClustersCount() {
        return getMaxClustersCount_0(this.nativeObj);
    }

    public int getSampleCount() {
        return getSampleCount_0(this.nativeObj);
    }

    public int getWindowRadius() {
        return getWindowRadius_0(this.nativeObj);
    }

    public MatOfPoint2f getSamplingPoints() {
        return MatOfPoint2f.fromNativeAddr(getSamplingPoints_0(this.nativeObj));
    }

    public MatOfInt getInitSeedIndexes() {
        return MatOfInt.fromNativeAddr(getInitSeedIndexes_0(this.nativeObj));
    }

    public void computeSignature(Mat image, Mat signature) {
        computeSignature_0(this.nativeObj, image.nativeObj, signature.nativeObj);
    }

    public void computeSignatures(List<Mat> images, List<Mat> signatures) {
        computeSignatures_0(this.nativeObj, Converters.vector_Mat_to_Mat(images).nativeObj, Converters.vector_Mat_to_Mat(signatures).nativeObj);
    }

    public static void drawSignature(Mat source, Mat signature, Mat result, float radiusToShorterSideRatio, int borderThickness) {
        drawSignature_0(source.nativeObj, signature.nativeObj, result.nativeObj, radiusToShorterSideRatio, borderThickness);
    }

    public static void drawSignature(Mat source, Mat signature, Mat result) {
        drawSignature_1(source.nativeObj, signature.nativeObj, result.nativeObj);
    }

    public static void generateInitPoints(MatOfPoint2f initPoints, int count, int pointDistribution) {
        generateInitPoints_0(initPoints.nativeObj, count, pointDistribution);
    }

    public void setClusterMinSize(int clusterMinSize) {
        setClusterMinSize_0(this.nativeObj, clusterMinSize);
    }

    public void setDistanceFunction(int distanceFunction) {
        setDistanceFunction_0(this.nativeObj, distanceFunction);
    }

    public void setDropThreshold(float dropThreshold) {
        setDropThreshold_0(this.nativeObj, dropThreshold);
    }

    public void setGrayscaleBits(int grayscaleBits) {
        setGrayscaleBits_0(this.nativeObj, grayscaleBits);
    }

    public void setInitSeedIndexes(MatOfInt initSeedIndexes) {
        setInitSeedIndexes_0(this.nativeObj, initSeedIndexes.nativeObj);
    }

    public void setIterationCount(int iterationCount) {
        setIterationCount_0(this.nativeObj, iterationCount);
    }

    public void setJoiningDistance(float joiningDistance) {
        setJoiningDistance_0(this.nativeObj, joiningDistance);
    }

    public void setMaxClustersCount(int maxClustersCount) {
        setMaxClustersCount_0(this.nativeObj, maxClustersCount);
    }

    public void setSamplingPoints(MatOfPoint2f samplingPoints) {
        setSamplingPoints_0(this.nativeObj, samplingPoints.nativeObj);
    }

    public void setTranslation(int idx, float value) {
        setTranslation_0(this.nativeObj, idx, value);
    }

    public void setTranslations(MatOfFloat translations) {
        setTranslations_0(this.nativeObj, translations.nativeObj);
    }

    public void setWeight(int idx, float value) {
        setWeight_0(this.nativeObj, idx, value);
    }

    public void setWeightA(float weight) {
        setWeightA_0(this.nativeObj, weight);
    }

    public void setWeightB(float weight) {
        setWeightB_0(this.nativeObj, weight);
    }

    public void setWeightContrast(float weight) {
        setWeightContrast_0(this.nativeObj, weight);
    }

    public void setWeightEntropy(float weight) {
        setWeightEntropy_0(this.nativeObj, weight);
    }

    public void setWeightL(float weight) {
        setWeightL_0(this.nativeObj, weight);
    }

    public void setWeightX(float weight) {
        setWeightX_0(this.nativeObj, weight);
    }

    public void setWeightY(float weight) {
        setWeightY_0(this.nativeObj, weight);
    }

    public void setWeights(MatOfFloat weights) {
        setWeights_0(this.nativeObj, weights.nativeObj);
    }

    public void setWindowRadius(int radius) {
        setWindowRadius_0(this.nativeObj, radius);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
