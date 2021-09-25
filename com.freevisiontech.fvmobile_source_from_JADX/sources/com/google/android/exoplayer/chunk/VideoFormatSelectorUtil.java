package com.google.android.exoplayer.chunk;

import android.content.Context;
import android.graphics.Point;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.Util;
import java.util.ArrayList;
import java.util.List;

public final class VideoFormatSelectorUtil {
    private static final float FRACTION_TO_CONSIDER_FULLSCREEN = 0.98f;

    public static int[] selectVideoFormatsForDefaultDisplay(Context context, List<? extends FormatWrapper> formatWrappers, String[] allowedContainerMimeTypes, boolean filterHdFormats) throws MediaCodecUtil.DecoderQueryException {
        Point viewportSize = Util.getPhysicalDisplaySize(context);
        return selectVideoFormats(formatWrappers, allowedContainerMimeTypes, filterHdFormats, true, false, viewportSize.x, viewportSize.y);
    }

    public static int[] selectVideoFormats(List<? extends FormatWrapper> formatWrappers, String[] allowedContainerMimeTypes, boolean filterHdFormats, boolean orientationMayChange, boolean secureDecoder, int viewportWidth, int viewportHeight) throws MediaCodecUtil.DecoderQueryException {
        int maxVideoPixelsToRetain = Integer.MAX_VALUE;
        ArrayList<Integer> selectedIndexList = new ArrayList<>();
        int formatWrapperCount = formatWrappers.size();
        for (int i = 0; i < formatWrapperCount; i++) {
            Format format = ((FormatWrapper) formatWrappers.get(i)).getFormat();
            if (isFormatPlayable(format, allowedContainerMimeTypes, filterHdFormats, secureDecoder)) {
                selectedIndexList.add(Integer.valueOf(i));
                if (format.width > 0 && format.height > 0 && viewportWidth > 0 && viewportHeight > 0) {
                    Point maxVideoSizeInViewport = getMaxVideoSizeInViewport(orientationMayChange, viewportWidth, viewportHeight, format.width, format.height);
                    int videoPixels = format.width * format.height;
                    if (format.width >= ((int) (((float) maxVideoSizeInViewport.x) * FRACTION_TO_CONSIDER_FULLSCREEN)) && format.height >= ((int) (((float) maxVideoSizeInViewport.y) * FRACTION_TO_CONSIDER_FULLSCREEN)) && videoPixels < maxVideoPixelsToRetain) {
                        maxVideoPixelsToRetain = videoPixels;
                    }
                }
            }
        }
        if (maxVideoPixelsToRetain != Integer.MAX_VALUE) {
            for (int i2 = selectedIndexList.size() - 1; i2 >= 0; i2--) {
                Format format2 = ((FormatWrapper) formatWrappers.get(selectedIndexList.get(i2).intValue())).getFormat();
                if (format2.width > 0 && format2.height > 0 && format2.width * format2.height > maxVideoPixelsToRetain) {
                    selectedIndexList.remove(i2);
                }
            }
        }
        return Util.toArray(selectedIndexList);
    }

    private static boolean isFormatPlayable(Format format, String[] allowedContainerMimeTypes, boolean filterHdFormats, boolean secureDecoder) throws MediaCodecUtil.DecoderQueryException {
        if (allowedContainerMimeTypes != null && !Util.contains(allowedContainerMimeTypes, format.mimeType)) {
            return false;
        }
        if (filterHdFormats && (format.width >= 1280 || format.height >= 720)) {
            return false;
        }
        if (format.width > 0 && format.height > 0) {
            if (Util.SDK_INT >= 21) {
                String videoMediaMimeType = MimeTypes.getVideoMediaMimeType(format.codecs);
                if (MimeTypes.VIDEO_UNKNOWN.equals(videoMediaMimeType)) {
                    videoMediaMimeType = MimeTypes.VIDEO_H264;
                }
                if (format.frameRate <= 0.0f) {
                    return MediaCodecUtil.isSizeSupportedV21(videoMediaMimeType, secureDecoder, format.width, format.height);
                }
                return MediaCodecUtil.isSizeAndRateSupportedV21(videoMediaMimeType, secureDecoder, format.width, format.height, (double) format.frameRate);
            } else if (format.width * format.height <= MediaCodecUtil.maxH264DecodableFrameSize()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private static Point getMaxVideoSizeInViewport(boolean orientationMayChange, int viewportWidth, int viewportHeight, int videoWidth, int videoHeight) {
        boolean z = true;
        if (orientationMayChange) {
            boolean z2 = videoWidth > videoHeight;
            if (viewportWidth <= viewportHeight) {
                z = false;
            }
            if (z2 != z) {
                int tempViewportWidth = viewportWidth;
                viewportWidth = viewportHeight;
                viewportHeight = tempViewportWidth;
            }
        }
        if (videoWidth * viewportHeight >= videoHeight * viewportWidth) {
            return new Point(viewportWidth, Util.ceilDivide(viewportWidth * videoHeight, videoWidth));
        }
        return new Point(Util.ceilDivide(viewportHeight * videoWidth, videoHeight), viewportHeight);
    }

    private VideoFormatSelectorUtil() {
    }
}
