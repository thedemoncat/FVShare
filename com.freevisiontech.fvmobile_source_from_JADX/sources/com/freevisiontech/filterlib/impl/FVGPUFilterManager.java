package com.freevisiontech.filterlib.impl;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import com.freevisiontech.filterlib.FilterType;
import com.freevisiontech.filterlib.myfilter.GPUImageBeautyFilter;
import com.freevisiontech.fvmobile.C0853R;
import java.util.LinkedList;
import java.util.List;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageCGAColorspaceFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageColorBurnBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageColorDodgeBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageDarkenBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageDifferenceBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageDilationFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageDivideBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageExclusionBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFalseColorFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageHalftoneFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageHardLightBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageHueBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageHueFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageLaplacianFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageLightenBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageLinearBurnBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageLuminosityBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageNonMaximumSuppressionFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageNormalBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageRGBDilationFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSaturationBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageScreenBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSmoothToonFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSoftLightBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSourceOverBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSphereRefractionFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSubtractBlendFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageToonFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageTransformFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageTwoInputFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageWeakPixelInclusionFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

public class FVGPUFilterManager {
    public static GPUImageFilter createFilterForType(Context context, FilterType type) {
        switch (type) {
            case NOFILTER:
                return new GPUImageFilter();
            case CONTRAST:
                return new GPUImageContrastFilter(2.0f);
            case GAMMA:
                return new GPUImageGammaFilter(2.0f);
            case INVERT:
                return new GPUImageColorInvertFilter();
            case PIXELATION:
                return new GPUImagePixelationFilter();
            case HUE:
                return new GPUImageHueFilter(90.0f);
            case BRIGHTNESS:
                return new GPUImageBrightnessFilter(1.5f);
            case GRAYSCALE:
                return new GPUImageGrayscaleFilter();
            case SEPIA:
                return new GPUImageSepiaFilter();
            case SHARPEN:
                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(2.0f);
                return sharpness;
            case SOBEL_EDGE_DETECTION:
                return new GPUImageSobelEdgeDetection();
            case THREE_X_THREE_CONVOLUTION:
                GPUImage3x3ConvolutionFilter convolution = new GPUImage3x3ConvolutionFilter();
                convolution.setConvolutionKernel(new float[]{-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f});
                return convolution;
            case EMBOSS:
                return new GPUImageEmbossFilter();
            case POSTERIZE:
                return new GPUImagePosterizeFilter();
            case FILTER_GROUP:
                List<GPUImageFilter> filters = new LinkedList<>();
                filters.add(new GPUImageContrastFilter());
                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                filters.add(new GPUImageGrayscaleFilter());
                return new GPUImageFilterGroup(filters);
            case SATURATION:
                return new GPUImageSaturationFilter(1.0f);
            case EXPOSURE:
                return new GPUImageExposureFilter(0.0f);
            case HIGHLIGHT_SHADOW:
                return new GPUImageHighlightShadowFilter(0.0f, 1.0f);
            case MONOCHROME:
                return new GPUImageMonochromeFilter(1.0f, new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            case OPACITY:
                return new GPUImageOpacityFilter(1.0f);
            case RGB:
                return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
            case WHITE_BALANCE:
                return new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
            case VIGNETTE:
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case TONE_CURVE:
                GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
                toneCurveFilter.setFromCurveFileInputStream(context.getResources().openRawResource(C0853R.raw.tone_cuver_sample));
                return toneCurveFilter;
            case BLEND_DIFFERENCE:
                return createBlendFilter(context, GPUImageDifferenceBlendFilter.class);
            case BLEND_SOURCE_OVER:
                return createBlendFilter(context, GPUImageSourceOverBlendFilter.class);
            case BLEND_COLOR_BURN:
                return createBlendFilter(context, GPUImageColorBurnBlendFilter.class);
            case BLEND_COLOR_DODGE:
                return createBlendFilter(context, GPUImageColorDodgeBlendFilter.class);
            case BLEND_DARKEN:
                return createBlendFilter(context, GPUImageDarkenBlendFilter.class);
            case BLEND_DISSOLVE:
                return createBlendFilter(context, GPUImageDissolveBlendFilter.class);
            case BLEND_EXCLUSION:
                return createBlendFilter(context, GPUImageExclusionBlendFilter.class);
            case BLEND_HARD_LIGHT:
                return createBlendFilter(context, GPUImageHardLightBlendFilter.class);
            case BLEND_LIGHTEN:
                return createBlendFilter(context, GPUImageLightenBlendFilter.class);
            case BLEND_ADD:
                return createBlendFilter(context, GPUImageAddBlendFilter.class);
            case BLEND_DIVIDE:
                return createBlendFilter(context, GPUImageDivideBlendFilter.class);
            case BLEND_MULTIPLY:
                return createBlendFilter(context, GPUImageMultiplyBlendFilter.class);
            case BLEND_OVERLAY:
                return createBlendFilter(context, GPUImageOverlayBlendFilter.class);
            case BLEND_SCREEN:
                return createBlendFilter(context, GPUImageScreenBlendFilter.class);
            case BLEND_ALPHA:
                return createBlendFilter(context, GPUImageAlphaBlendFilter.class);
            case BLEND_COLOR:
                return createBlendFilter(context, GPUImageColorBlendFilter.class);
            case BLEND_HUE:
                return createBlendFilter(context, GPUImageHueBlendFilter.class);
            case BLEND_SATURATION:
                return createBlendFilter(context, GPUImageSaturationBlendFilter.class);
            case BLEND_LUMINOSITY:
                return createBlendFilter(context, GPUImageLuminosityBlendFilter.class);
            case BLEND_LINEAR_BURN:
                return createBlendFilter(context, GPUImageLinearBurnBlendFilter.class);
            case BLEND_SOFT_LIGHT:
                return createBlendFilter(context, GPUImageSoftLightBlendFilter.class);
            case BLEND_SUBTRACT:
                return createBlendFilter(context, GPUImageSubtractBlendFilter.class);
            case BLEND_CHROMA_KEY:
                return createBlendFilter(context, GPUImageChromaKeyBlendFilter.class);
            case BLEND_NORMAL:
                return createBlendFilter(context, GPUImageNormalBlendFilter.class);
            case LOOKUP_AMATORKA:
                GPUImageLookupFilter amatorka = new GPUImageLookupFilter();
                amatorka.setBitmap(BitmapFactory.decodeResource(context.getResources(), C0853R.C0854drawable.lookup_amatorka));
                return amatorka;
            case GAUSSIAN_BLUR:
                return new GPUImageGaussianBlurFilter();
            case CROSSHATCH:
                return new GPUImageCrosshatchFilter();
            case BOX_BLUR:
                return new GPUImageBoxBlurFilter();
            case CGA_COLORSPACE:
                return new GPUImageCGAColorspaceFilter();
            case DILATION:
                return new GPUImageDilationFilter();
            case KUWAHARA:
                return new GPUImageKuwaharaFilter();
            case RGB_DILATION:
                return new GPUImageRGBDilationFilter();
            case SKETCH:
                return new GPUImageSketchFilter();
            case TOON:
                return new GPUImageToonFilter();
            case SMOOTH_TOON:
                return new GPUImageSmoothToonFilter();
            case BULGE_DISTORTION:
                return new GPUImageBulgeDistortionFilter();
            case GLASS_SPHERE:
                return new GPUImageGlassSphereFilter();
            case HAZE:
                return new GPUImageHazeFilter();
            case LAPLACIAN:
                return new GPUImageLaplacianFilter();
            case NON_MAXIMUM_SUPPRESSION:
                return new GPUImageNonMaximumSuppressionFilter();
            case SPHERE_REFRACTION:
                return new GPUImageSphereRefractionFilter();
            case SWIRL:
                return new GPUImageSwirlFilter();
            case WEAK_PIXEL_INCLUSION:
                return new GPUImageWeakPixelInclusionFilter();
            case FALSE_COLOR:
                return new GPUImageFalseColorFilter();
            case COLOR_BALANCE:
                return new GPUImageColorBalanceFilter();
            case LEVELS_FILTER_MIN:
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                return levelsFilter;
            case HALFTONE:
                return new GPUImageHalftoneFilter();
            case BILATERAL_BLUR:
                return new GPUImageBilateralFilter();
            case TRANSFORM2D:
                return new GPUImageTransformFilter();
            case BEAUTY:
                return new GPUImageBeautyFilter(context);
            default:
                throw new IllegalStateException("No filter of that type!");
        }
    }

    public static GPUImageFilter adjustFilter(GPUImageFilter filter, int percent) {
        FilterAdjuster fa = new FilterAdjuster(filter);
        if (!fa.canAdjust()) {
            return null;
        }
        fa.adjust(percent);
        return fa.getAdjuster().getFilter();
    }

    public static GPUImageFilter createFilterForType(Context context, FilterType type, int percent) {
        GPUImageFilter filter = createFilterForType(context, type);
        return percent == -1 ? filter : adjustFilter(filter, percent);
    }

    private static GPUImageFilter createBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> filterClass) {
        try {
            return (GPUImageTwoInputFilter) filterClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
