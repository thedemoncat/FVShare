package com.freevisiontech.cameralib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.RggbChannelVector;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import com.freevisiontech.cameralib.AspectRatio;
import com.freevisiontech.cameralib.Constants;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.cameralib.SizeMap;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Constants;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Manager;
import com.freevisiontech.cameralib.impl.Camera2.CameraParameters;
import com.umeng.analytics.C0015a;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class CameraUtils {
    public static final int COLOR_FormatI420 = 1;
    public static final int COLOR_FormatNV21 = 2;
    public static boolean ShowDebugInfo = true;

    public static int Clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        return x < min ? min : x;
    }

    public static int GetRotateDegree(CameraCharacteristics characteristics, Activity activity) {
        int degrees = 0;
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
                break;
        }
        return ((((Integer) characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue() - degrees) + C0015a.f29p) % C0015a.f29p;
    }

    public static List<Size> SizeArray2SizeList(Size[] sizes) {
        if (sizes == null) {
            return null;
        }
        List<Size> list = new ArrayList<>();
        for (Size add : sizes) {
            list.add(add);
        }
        return list;
    }

    public static Size[] SizeList2SizeArray(List<Size> sizeList) {
        if (sizeList == null) {
            return null;
        }
        int[] b = new int[sizeList.size()];
        for (int s = 0; s < b.length; s++) {
            b[s] = sizeList.get(s).getWidth();
        }
        int[] d = sort(b);
        Size[] sizes = new Size[sizeList.size()];
        for (int e = 0; e < d.length; e++) {
            for (int i = 0; i < sizes.length; i++) {
                if (sizeList.get(i).getWidth() == d[e]) {
                    sizes[e] = sizeList.get(i);
                }
            }
        }
        return sizes;
    }

    public static int[] sort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < (a.length - 1) - i; j++) {
                if (a[j] < a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
        return a;
    }

    public static Size ChooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {
        List<Size> bigEnough = new ArrayList<>();
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight && option.getHeight() == (option.getWidth() * h) / w) {
                if (option.getWidth() < textureViewWidth || option.getHeight() < textureViewHeight) {
                    notBigEnough.add(option);
                } else {
                    bigEnough.add(option);
                }
            }
        }
        if (bigEnough.size() > 0) {
            return (Size) Collections.min(bigEnough, new Camera2Manager.CompareSizesByArea());
        }
        if (notBigEnough.size() > 0) {
            return (Size) Collections.max(notBigEnough, new Camera2Manager.CompareSizesByArea());
        }
        return choices[0];
    }

    public static Size ChooseOptimalSize(Size[] choices, int textureWidth, int textureHeight) {
        return ChooseOptimalSize(choices, AspectRatio.m1507of(textureWidth, textureHeight));
    }

    public static Size ChooseOptimalSize(Size[] choices, AspectRatio aspectRatio) {
        AspectRatio real;
        SizeMap sizeMap = Sizes2SizeMap(choices);
        Set<AspectRatio> ratios = sizeMap.ratios();
        AspectRatio big = null;
        AspectRatio less = null;
        float asp = aspectRatio.toFloat();
        for (AspectRatio as : ratios) {
            float p = as.toFloat();
            if (p >= asp) {
                if (big == null) {
                    big = as;
                } else if (p < big.toFloat()) {
                    big = as;
                }
            } else if (less == null) {
                less = as;
            } else if (p > less.toFloat()) {
                less = as;
            }
        }
        if (big == null) {
            real = less;
        } else if (less == null) {
            real = big;
        } else if (Math.abs(big.toFloat() - asp) > Math.abs(asp - less.toFloat())) {
            real = less;
        } else {
            real = big;
        }
        float d = 1.0E9f;
        Size sd = null;
        for (Size s : sizeMap.sizes(real)) {
            float dd = SizeAbsDistance(s, new Size(1920, 1080));
            if (dd < d) {
                d = dd;
                sd = s;
            }
        }
        return sd;
    }

    public static float SizeAbsDistance(Size s1, Size s2) {
        return (float) (Math.abs(s1.getWidth() - s2.getWidth()) + Math.abs(s1.getHeight() - s2.getHeight()));
    }

    public static SizeMap Sizes2SizeMap(Size[] sizes) {
        SizeMap sm = new SizeMap();
        if (sizes != null) {
            for (Size s : sizes) {
                sm.add(s);
            }
        }
        return sm;
    }

    public static String FocusModeId2FocusModeStr(int mode) {
        switch (mode) {
            case 0:
                return Constants.FOCUS_MODE_OFF;
            case 1:
                return com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO;
            case 2:
                return "macro";
            case 3:
                return "continuous-video";
            case 4:
                return "continuous-picture";
            case 5:
                return "edof";
            default:
                return "";
        }
    }

    public static int FocusModeStr2FocusModeId(String mode) {
        if (mode.equals(Constants.FOCUS_MODE_OFF)) {
            return 0;
        }
        if (mode.equals(com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO)) {
            return 1;
        }
        if (mode.equals("macro")) {
            return 2;
        }
        if (mode.equals("continuous-video")) {
            return 3;
        }
        if (mode.equals("continuous-picture")) {
            return 4;
        }
        if (mode.equals("edof")) {
            return 5;
        }
        return -1;
    }

    public static SizeFps Quality2Size(int quality) {
        Size size = null;
        int fps = 30;
        switch (quality) {
            case 4:
            case 1004:
                size = new Size(720, 480);
                fps = 30;
                break;
            case 5:
            case 1005:
                size = new Size(1280, 720);
                fps = 30;
                break;
            case 6:
            case 1006:
                size = new Size(1920, 1080);
                fps = 30;
                break;
            case 8:
            case 1008:
                size = new Size(3840, 2160);
                fps = 30;
                break;
        }
        if (size != null) {
            return new SizeFps(size, fps);
        }
        return null;
    }

    public static class SizeFps {
        public int fps;
        public Size size;

        public SizeFps(Size s, int f) {
            this.size = s;
            this.fps = f;
        }
    }

    public static String WhiteBalanceMode2WhiteBalanceStr(int mode) {
        switch (mode) {
            case 0:
                return "";
            case 1:
                return com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO;
            case 2:
                return "incandescent";
            case 3:
                return "fluorescent";
            case 4:
                return "warm-fluorescent";
            case 5:
                return "daylight";
            case 6:
                return "cloudy-daylight";
            case 7:
                return "twilight";
            case 8:
                return "shade";
            default:
                return "";
        }
    }

    public static int WhiteBalanceStr2WhiteBalanceMode(String mode) {
        int wbmode = -1;
        if (mode == null || mode.equals("")) {
            wbmode = 0;
        }
        if (mode.equals(com.freevisiontech.fvmobile.utility.Constants.SCENE_MODE_AUTO)) {
            wbmode = 1;
        }
        if (mode.equals("incandescent")) {
            wbmode = 2;
        }
        if (mode.equals("fluorescent")) {
            wbmode = 3;
        }
        if (mode.equals("warm-fluorescent")) {
            wbmode = 4;
        }
        if (mode.equals("daylight")) {
            wbmode = 5;
        }
        if (mode.equals("cloudy-daylight")) {
            wbmode = 6;
        }
        if (mode.equals("twilight")) {
            wbmode = 7;
        }
        if (mode.equals("shade")) {
            return 8;
        }
        return wbmode;
    }

    public static Size[] SizeArrayFromSizeArray(android.util.Size[] sizes) {
        if (sizes == null) {
            return null;
        }
        Size[] ss = new Size[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            ss[i] = Size.FromSize(sizes[i]);
        }
        return ss;
    }

    public static int Intege2int(Integer integer) {
        if (integer == null) {
            return -1;
        }
        try {
            return integer.intValue();
        } catch (Exception e) {
            return -1;
        }
    }

    public static float Float2float(Float f) {
        if (f == null) {
            return -1.0f;
        }
        try {
            return f.floatValue();
        } catch (Exception e) {
            return -1.0f;
        }
    }

    public static long Long2long(Long l) {
        if (l == null) {
            return -1;
        }
        try {
            return l.longValue();
        } catch (Exception e) {
            return -1;
        }
    }

    public static double Double2double(Double d) {
        if (d == null) {
            return -1.0d;
        }
        try {
            return d.doubleValue();
        } catch (Exception e) {
            return -1.0d;
        }
    }

    public static boolean Boolean2bool(Boolean b) {
        if (b == null) {
            return false;
        }
        try {
            return b.booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    public static Mat ImageToMat(Image image) {
        int offset;
        int width = image.getWidth();
        int height = image.getHeight();
        int offset2 = 0;
        Image.Plane[] planes = image.getPlanes();
        byte[] data = new byte[(((image.getWidth() * image.getHeight()) * ImageFormat.getBitsPerPixel(35)) / 8)];
        byte[] rowData = new byte[planes[0].getRowStride()];
        int i = 0;
        while (i < planes.length) {
            ByteBuffer buffer = planes[i].getBuffer();
            int rowStride = planes[i].getRowStride();
            int pixelStride = planes[i].getPixelStride();
            int w = i == 0 ? width : width / 2;
            int h = i == 0 ? height : height / 2;
            for (int row = 0; row < h; row++) {
                int bytesPerPixel = ImageFormat.getBitsPerPixel(35) / 8;
                if (pixelStride == bytesPerPixel) {
                    int length = w * bytesPerPixel;
                    buffer.get(data, offset2, length);
                    if (h - row != 1) {
                        buffer.position((buffer.position() + rowStride) - length);
                    }
                    offset2 += length;
                } else {
                    if (h - row == 1) {
                        buffer.get(rowData, 0, (width - pixelStride) + 1);
                    } else {
                        buffer.get(rowData, 0, rowStride);
                    }
                    int col = 0;
                    while (true) {
                        offset = offset2;
                        if (col >= w) {
                            break;
                        }
                        offset2 = offset + 1;
                        data[offset] = rowData[col * pixelStride];
                        col++;
                    }
                    offset2 = offset;
                }
            }
            i++;
        }
        Mat mat = new Mat((height / 2) + height, width, CvType.CV_8UC1);
        mat.put(0, 0, data);
        return mat;
    }

    public static byte[] ImageToByte(Image image) {
        int offset;
        int width = image.getWidth();
        int height = image.getHeight();
        int offset2 = 0;
        Image.Plane[] planes = image.getPlanes();
        byte[] data = new byte[(((image.getWidth() * image.getHeight()) * ImageFormat.getBitsPerPixel(35)) / 8)];
        byte[] rowData = new byte[planes[0].getRowStride()];
        int i = 0;
        while (i < planes.length) {
            ByteBuffer buffer = planes[i].getBuffer();
            int rowStride = planes[i].getRowStride();
            int pixelStride = planes[i].getPixelStride();
            int w = i == 0 ? width : width / 2;
            int h = i == 0 ? height : height / 2;
            for (int row = 0; row < h; row++) {
                int bytesPerPixel = ImageFormat.getBitsPerPixel(35) / 8;
                if (pixelStride == bytesPerPixel) {
                    int length = w * bytesPerPixel;
                    buffer.get(data, offset2, length);
                    if (h - row != 1) {
                        buffer.position((buffer.position() + rowStride) - length);
                    }
                    offset2 += length;
                } else {
                    if (h - row == 1) {
                        buffer.get(rowData, 0, (width - pixelStride) + 1);
                    } else {
                        buffer.get(rowData, 0, rowStride);
                    }
                    int col = 0;
                    while (true) {
                        offset = offset2;
                        if (col >= w) {
                            break;
                        }
                        offset2 = offset + 1;
                        data[offset] = rowData[col * pixelStride];
                        col++;
                    }
                    offset2 = offset;
                }
            }
            i++;
        }
        return data;
    }

    public static byte[] ImageToByteEx(Image image) {
        Image.Plane yPlane = image.getPlanes()[0];
        int ySize = yPlane.getBuffer().remaining();
        Image.Plane uPlane = image.getPlanes()[1];
        Image.Plane vPlane = image.getPlanes()[2];
        int uSize = uPlane.getBuffer().remaining();
        int vSize = vPlane.getBuffer().remaining();
        byte[] data = new byte[((ySize / 2) + ySize)];
        yPlane.getBuffer().get(data, 0, ySize);
        ByteBuffer ub = uPlane.getBuffer();
        ByteBuffer vb = vPlane.getBuffer();
        if (uPlane.getPixelStride() == 1) {
            ub.get(data, ySize, uSize);
            vb.get(data, ySize + uSize, vSize);
        } else {
            vb.get(data, ySize, vSize);
        }
        return data;
    }

    public static byte[] GetDataFromImage(Image image, int colorFormat) {
        int length;
        Rect crop = image.getCropRect();
        int format = image.getFormat();
        int width = crop.width();
        int height = crop.height();
        Image.Plane[] planes = image.getPlanes();
        byte[] data = new byte[(((width * height) * ImageFormat.getBitsPerPixel(format)) / 8)];
        byte[] rowData = new byte[planes[0].getRowStride()];
        int channelOffset = 0;
        int outputStride = 1;
        int i = 0;
        while (i < planes.length) {
            switch (i) {
                case 0:
                    channelOffset = 0;
                    outputStride = 1;
                    break;
                case 1:
                    if (colorFormat != 1) {
                        if (colorFormat == 2) {
                            channelOffset = (width * height) + 1;
                            outputStride = 2;
                            break;
                        }
                    } else {
                        channelOffset = width * height;
                        outputStride = 1;
                        break;
                    }
                    break;
                case 2:
                    if (colorFormat != 1) {
                        if (colorFormat == 2) {
                            channelOffset = width * height;
                            outputStride = 2;
                            break;
                        }
                    } else {
                        channelOffset = (int) (((double) (width * height)) * 1.25d);
                        outputStride = 1;
                        break;
                    }
                    break;
            }
            ByteBuffer buffer = planes[i].getBuffer();
            int rowStride = planes[i].getRowStride();
            int pixelStride = planes[i].getPixelStride();
            int shift = i == 0 ? 0 : 1;
            int w = width >> shift;
            int h = height >> shift;
            buffer.position(((crop.top >> shift) * rowStride) + ((crop.left >> shift) * pixelStride));
            for (int row = 0; row < h; row++) {
                if (pixelStride == 1 && outputStride == 1) {
                    length = w;
                    buffer.get(data, channelOffset, length);
                    channelOffset += length;
                } else {
                    length = ((w - 1) * pixelStride) + 1;
                    buffer.get(rowData, 0, length);
                    for (int col = 0; col < w; col++) {
                        data[channelOffset] = rowData[col * pixelStride];
                        channelOffset += outputStride;
                    }
                }
                if (row < h - 1) {
                    buffer.position((buffer.position() + rowStride) - length);
                }
            }
            i++;
        }
        return data;
    }

    public static Size GetPreferSize(Size[] sizes, Size prefersize, boolean ismax, boolean isqualitysupported) {
        Size s = null;
        if (sizes == null) {
            return null;
        }
        int length = sizes.length;
        for (int i = 0; i < length; i++) {
            Size ss = sizes[i];
            if (ss.getHeight() == prefersize.getHeight() && ss.getWidth() == prefersize.getWidth()) {
                if (isqualitysupported) {
                    return ss;
                }
            } else if (ss.getHeight() != prefersize.getHeight()) {
                continue;
            } else if (ss.getHeight() * 16 == ss.getWidth() * 9 || ss.getHeight() * 4 == ss.getWidth() * 3) {
                return ss;
            } else {
                if (s == null) {
                    s = ss;
                } else if (ismax) {
                    if (s.getWidth() <= ss.getWidth()) {
                        s = ss;
                    }
                } else if (s.getWidth() >= ss.getWidth()) {
                    s = ss;
                }
            }
        }
        return s;
    }

    public static int Size2Quality(Size size) {
        int w = size.getWidth();
        int h = size.getHeight();
        if (w == 3840 && h == 2160) {
            return 8;
        }
        if (w == 1920 && h == 1080) {
            return 6;
        }
        if (w == 1280 && h == 720) {
            return 5;
        }
        if (w == 720 && h == 480) {
            return 4;
        }
        return -1;
    }

    public static String ShowSizesStrings(List<Camera.Size> sizes, String TAG, String prefix) {
        StringBuffer sb = new StringBuffer();
        for (Camera.Size size : sizes) {
            sb.append(size.width).append("x").append(size.height).append(" ; ");
        }
        Log.v(TAG, prefix + sb.toString());
        return sb.toString();
    }

    public static String ShowSizeArrayStrings(Size[] sizes, String TAG, String prefix) {
        StringBuffer sb = new StringBuffer();
        for (Size size : sizes) {
            sb.append(size.getWidth()).append("x").append(size.getHeight()).append(" ; ");
        }
        Log.v(TAG, prefix + sb.toString());
        return sb.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0042 A[SYNTHETIC, Splitter:B:25:0x0042] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0047 A[SYNTHETIC, Splitter:B:28:0x0047] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0058 A[SYNTHETIC, Splitter:B:36:0x0058] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x005d A[SYNTHETIC, Splitter:B:39:0x005d] */
    /* JADX WARNING: Removed duplicated region for block: B:57:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void SaveBytes2File(byte[] r8, java.lang.String r9) {
        /*
            java.io.File r4 = new java.io.File
            r4.<init>(r9)
            r5 = 0
            r0 = 0
            boolean r7 = r4.exists()     // Catch:{ Exception -> 0x003c }
            if (r7 == 0) goto L_0x0010
            r4.delete()     // Catch:{ Exception -> 0x003c }
        L_0x0010:
            r4.createNewFile()     // Catch:{ Exception -> 0x003c }
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x003c }
            r6.<init>(r4)     // Catch:{ Exception -> 0x003c }
            java.io.BufferedOutputStream r1 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0072, all -> 0x006b }
            r1.<init>(r6)     // Catch:{ Exception -> 0x0072, all -> 0x006b }
            r1.write(r8)     // Catch:{ Exception -> 0x0075, all -> 0x006e }
            r1.flush()     // Catch:{ Exception -> 0x0075, all -> 0x006e }
            if (r6 == 0) goto L_0x0028
            r6.close()     // Catch:{ IOException -> 0x0030 }
        L_0x0028:
            if (r1 == 0) goto L_0x0079
            r1.close()     // Catch:{ Exception -> 0x0035 }
            r0 = r1
            r5 = r6
        L_0x002f:
            return
        L_0x0030:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0028
        L_0x0035:
            r3 = move-exception
            r3.printStackTrace()
            r0 = r1
            r5 = r6
            goto L_0x002f
        L_0x003c:
            r2 = move-exception
        L_0x003d:
            r2.printStackTrace()     // Catch:{ all -> 0x0055 }
            if (r5 == 0) goto L_0x0045
            r5.close()     // Catch:{ IOException -> 0x0050 }
        L_0x0045:
            if (r0 == 0) goto L_0x002f
            r0.close()     // Catch:{ Exception -> 0x004b }
            goto L_0x002f
        L_0x004b:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x002f
        L_0x0050:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0045
        L_0x0055:
            r7 = move-exception
        L_0x0056:
            if (r5 == 0) goto L_0x005b
            r5.close()     // Catch:{ IOException -> 0x0061 }
        L_0x005b:
            if (r0 == 0) goto L_0x0060
            r0.close()     // Catch:{ Exception -> 0x0066 }
        L_0x0060:
            throw r7
        L_0x0061:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x005b
        L_0x0066:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0060
        L_0x006b:
            r7 = move-exception
            r5 = r6
            goto L_0x0056
        L_0x006e:
            r7 = move-exception
            r0 = r1
            r5 = r6
            goto L_0x0056
        L_0x0072:
            r2 = move-exception
            r5 = r6
            goto L_0x003d
        L_0x0075:
            r2 = move-exception
            r0 = r1
            r5 = r6
            goto L_0x003d
        L_0x0079:
            r0 = r1
            r5 = r6
            goto L_0x002f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.cameralib.utils.CameraUtils.SaveBytes2File(byte[], java.lang.String):void");
    }

    public static void LogV(String tag, String info) {
        if (ShowDebugInfo) {
            Log.v(tag, info);
        }
    }

    public static boolean isApkDebugable(Context context) {
        try {
            if ((context.getApplicationInfo().flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static RggbChannelVector WBTemperatureFromFactor(int factor) {
        return new RggbChannelVector(0.635f + (0.0208333f * ((float) factor)), 1.0f, 1.0f, 3.7420394f + (-0.0287829f * ((float) factor)));
    }

    public static int FactorFromWbTemperature(RggbChannelVector vector) {
        return new Float((((vector.getRed() - 0.635f) / 0.0208333f) + ((vector.getBlue() - 3.7420394f) / -0.0287829f)) / 2.0f).intValue();
    }

    public static RggbChannelVector ColorTemperatureFromValue(int whiteBalance) {
        float red;
        float green;
        float blue;
        float temperature = (float) (whiteBalance / 100);
        if (temperature <= 66.0f) {
            red = 255.0f;
        } else {
            red = (float) (329.698727446d * Math.pow((double) (temperature - 60.0f), -0.1332047592d));
            if (red < 0.0f) {
                red = 0.0f;
            }
            if (red > 255.0f) {
                red = 255.0f;
            }
        }
        if (temperature <= 66.0f) {
            green = (float) ((99.4708025861d * Math.log((double) temperature)) - 161.1195681661d);
            if (green < 0.0f) {
                green = 0.0f;
            }
            if (green > 255.0f) {
                green = 255.0f;
            }
        } else {
            float green2 = (float) (288.1221695283d * Math.pow((double) (temperature - 60.0f), -0.0755148492d));
            if (green2 < 0.0f) {
                green2 = 0.0f;
            }
            if (green > 255.0f) {
                green = 255.0f;
            }
        }
        if (temperature >= 66.0f) {
            blue = 255.0f;
        } else if (temperature <= 19.0f) {
            blue = 0.0f;
        } else {
            blue = (float) ((138.5177312231d * Math.log((double) (temperature - 10.0f))) - 305.0447927307d);
            if (blue < 0.0f) {
                blue = 0.0f;
            }
            if (blue > 255.0f) {
                blue = 255.0f;
            }
        }
        return new RggbChannelVector((red / 255.0f) * 2.0f, green / 255.0f, green / 255.0f, (blue / 255.0f) * 2.0f);
    }

    public static boolean isSizeModelPairInRecordTraceExludeMap(Size size, int face) {
        String models;
        if (face == 0) {
            models = CameraParameters.Camera2_Record_Trace_Model_Resoultuion_ExcludeMap_Front.get(size);
        } else {
            models = CameraParameters.Camera2_Record_Trace_Model_Resoultuion_ExcludeMap_Back.get(size);
        }
        if (models == null || models.isEmpty() || !models.contains(Build.MODEL.trim().toLowerCase())) {
            return false;
        }
        return true;
    }

    public static boolean isQualityModelPairInRecordTraceExludeMap(int quality, int face) {
        SizeFps sizef = Quality2Size(quality);
        if (sizef == null) {
            return false;
        }
        return isSizeModelPairInRecordTraceExludeMap(sizef.size, face);
    }

    public static int VideoBitRateFromQuality(int quality) {
        if (quality == 8) {
            return Camera2Constants.VideoBitRate_2160;
        }
        if (quality == 6) {
            return Camera2Constants.VideoBitRate_1080;
        }
        if (quality == 5) {
            return Camera2Constants.VideoBitRate_720;
        }
        if (quality == 4) {
            return Camera2Constants.VideoBitRate_480;
        }
        return Camera2Constants.VideoBitRate_High;
    }

    public static int VideoBitRateFromVideoHeightFrame(int videoheight, int frame) {
        if (videoheight == 2160 && frame <= 30) {
            return Camera2Constants.VideoBitRate_2160;
        }
        if (videoheight == 1080 && frame <= 30) {
            return Camera2Constants.VideoBitRate_1080;
        }
        if (videoheight == 720 && frame <= 30) {
            return Camera2Constants.VideoBitRate_720;
        }
        if (videoheight == 480 && frame <= 30) {
            return Camera2Constants.VideoBitRate_480;
        }
        if (videoheight == 1080 && frame <= 120) {
            return 35000000;
        }
        if (videoheight == 720 && frame <= 120) {
            return 25000000;
        }
        if (videoheight == 1080 && frame <= 240) {
            return 35000000;
        }
        if (videoheight != 720 || frame > 240) {
            return Camera2Constants.VideoBitRate_High;
        }
        return 25000000;
    }
}
