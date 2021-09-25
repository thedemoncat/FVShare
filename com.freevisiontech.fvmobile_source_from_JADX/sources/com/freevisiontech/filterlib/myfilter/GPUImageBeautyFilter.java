package com.freevisiontech.filterlib.myfilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import com.freevisiontech.fvmobile.C0853R;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;
import p008jp.p009co.cyberagent.android.gpuimage.OpenGlUtils;

public class GPUImageBeautyFilter extends GPUImageFilter {
    private final String TAG = "GPUImageBeautyFilter";
    private int beautyLevel = 5;
    private int mParamsLocation;
    private int mSingleStepOffsetLocation;

    public GPUImageBeautyFilter(Context context) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(context, C0853R.raw.beautify_fragment));
    }

    public void onInit() {
        super.onInit();
        this.mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
        this.mParamsLocation = GLES20.glGetUniformLocation(getProgram(), "params");
        setBeautyLevel(this.beautyLevel);
    }

    private void setTexelSize(float w, float h) {
        setFloatVec2(this.mSingleStepOffsetLocation, new float[]{2.0f / w, 2.0f / h});
        Log.v("GPUImageBeautyFilter", "setTexelSize:" + w + ":" + h);
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setTexelSize((float) width, (float) height);
    }

    public void setBeautyLevel(int level) {
        this.beautyLevel = level;
        switch (level) {
            case 1:
                setFloatVec4(this.mParamsLocation, new float[]{1.0f, 1.0f, 0.15f, 0.15f});
                break;
            case 2:
                setFloatVec4(this.mParamsLocation, new float[]{0.8f, 0.9f, 0.2f, 0.2f});
                break;
            case 3:
                setFloatVec4(this.mParamsLocation, new float[]{0.6f, 0.8f, 0.25f, 0.25f});
                break;
            case 4:
                setFloatVec4(this.mParamsLocation, new float[]{0.4f, 0.7f, 0.38f, 0.3f});
                break;
            case 5:
                setFloatVec4(this.mParamsLocation, new float[]{0.33f, 0.63f, 0.4f, 0.35f});
                break;
            default:
                this.beautyLevel = 5;
                break;
        }
        Log.v("GPUImageBeautyFilter", "" + this.beautyLevel);
    }
}
