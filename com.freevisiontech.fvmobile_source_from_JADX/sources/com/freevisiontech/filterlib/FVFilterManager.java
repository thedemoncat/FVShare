package com.freevisiontech.filterlib;

import android.content.Context;
import com.freevisiontech.filterlib.impl.FVGPUFilterManager;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;

public class FVFilterManager {
    public static GPUImageFilter createGPUImageFilterForType(Context context, FilterType type) {
        return FVGPUFilterManager.createFilterForType(context, type);
    }

    public static GPUImageFilter adjustGPUImageFilter(GPUImageFilter filter, int percent) {
        return FVGPUFilterManager.adjustFilter(filter, percent);
    }

    public static GPUImageFilter createGPUImageFilterForType(Context context, FilterType type, int percent) {
        return FVGPUFilterManager.createFilterForType(context, type, percent);
    }
}
