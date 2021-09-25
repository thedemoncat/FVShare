package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageTwoPassTextureSamplingFilter */
public class GPUImageTwoPassTextureSamplingFilter extends GPUImageTwoPassFilter {
    public GPUImageTwoPassTextureSamplingFilter(String firstVertexShader, String firstFragmentShader, String secondVertexShader, String secondFragmentShader) {
        super(firstVertexShader, firstFragmentShader, secondVertexShader, secondFragmentShader);
    }

    public void onInit() {
        super.onInit();
        initTexelOffsets();
    }

    /* access modifiers changed from: protected */
    public void initTexelOffsets() {
        float ratio = getHorizontalTexelOffsetRatio();
        GPUImageFilter filter = (GPUImageFilter) this.mFilters.get(0);
        int texelWidthOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelWidthOffset");
        int texelHeightOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelHeightOffset");
        filter.setFloat(texelWidthOffsetLocation, ratio / ((float) this.mOutputWidth));
        filter.setFloat(texelHeightOffsetLocation, 0.0f);
        float ratio2 = getVerticalTexelOffsetRatio();
        GPUImageFilter filter2 = (GPUImageFilter) this.mFilters.get(1);
        int texelWidthOffsetLocation2 = GLES20.glGetUniformLocation(filter2.getProgram(), "texelWidthOffset");
        int texelHeightOffsetLocation2 = GLES20.glGetUniformLocation(filter2.getProgram(), "texelHeightOffset");
        filter2.setFloat(texelWidthOffsetLocation2, 0.0f);
        filter2.setFloat(texelHeightOffsetLocation2, ratio2 / ((float) this.mOutputHeight));
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        initTexelOffsets();
    }

    public float getVerticalTexelOffsetRatio() {
        return 1.0f;
    }

    public float getHorizontalTexelOffsetRatio() {
        return 1.0f;
    }
}
