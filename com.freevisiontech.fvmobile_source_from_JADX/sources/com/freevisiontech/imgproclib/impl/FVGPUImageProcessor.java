package com.freevisiontech.imgproclib.impl;

import android.content.Context;
import android.graphics.Bitmap;
import com.freevisiontech.filterlib.FVFilterManager;
import com.freevisiontech.filterlib.FilterType;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImage;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;

public class FVGPUImageProcessor {
    public static Bitmap applyGPUFilter(Bitmap image, Context ctx, GPUImageFilter filter) {
        GPUImage gpuImage = new GPUImage(ctx);
        gpuImage.setImage(image);
        gpuImage.setFilter(filter);
        return gpuImage.getBitmapWithFilterApplied();
    }

    public static Bitmap applyGPUFilter(Bitmap image, Context ctx, FilterType filtertype, int percent) {
        GPUImageFilter filter = FVFilterManager.createGPUImageFilterForType(ctx, filtertype, percent);
        GPUImage gpuImage = new GPUImage(ctx);
        gpuImage.setImage(image);
        gpuImage.setFilter(filter);
        return gpuImage.getBitmapWithFilterApplied();
    }
}
