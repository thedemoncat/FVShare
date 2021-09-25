package com.freevisiontech.imgproclib;

import android.content.Context;
import android.graphics.Bitmap;
import com.freevisiontech.filterlib.FilterType;
import com.freevisiontech.imgproclib.impl.FVGPUImageProcessor;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;

public class FVImageProcessor {
    public static Bitmap applyGPUFilter(Bitmap image, Context ctx, GPUImageFilter filter) {
        return FVGPUImageProcessor.applyGPUFilter(image, ctx, filter);
    }

    public static Bitmap applyGPUFilter(Bitmap image, Context ctx, FilterType filtertype, int percent) {
        return FVGPUImageProcessor.applyGPUFilter(image, ctx, filtertype, percent);
    }

    public static Bitmap applyGPUFilter(Bitmap image, Context ctx, FilterType filtertype) {
        return FVGPUImageProcessor.applyGPUFilter(image, ctx, filtertype, -1);
    }
}
