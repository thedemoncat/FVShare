package com.bumptech.glide.gifdecoder;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;

public class GifDecoder {
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int DISPOSAL_BACKGROUND = 2;
    private static final int DISPOSAL_NONE = 1;
    private static final int DISPOSAL_PREVIOUS = 3;
    private static final int DISPOSAL_UNSPECIFIED = 0;
    private static final int INITIAL_FRAME_POINTER = -1;
    private static final int MAX_STACK_SIZE = 4096;
    private static final int NULL_CODE = -1;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OPEN_ERROR = 2;
    public static final int STATUS_PARTIAL_DECODE = 3;
    private static final String TAG = GifDecoder.class.getSimpleName();
    private int[] act;
    private BitmapProvider bitmapProvider;
    private final byte[] block = new byte[256];
    private byte[] data;
    private int framePointer;
    private GifHeader header;
    private byte[] mainPixels;
    private int[] mainScratch;
    private GifHeaderParser parser;
    private byte[] pixelStack;
    private short[] prefix;
    private Bitmap previousImage;
    private ByteBuffer rawData;
    private boolean savePrevious;
    private int status;
    private byte[] suffix;

    public interface BitmapProvider {
        Bitmap obtain(int i, int i2, Bitmap.Config config);

        void release(Bitmap bitmap);
    }

    public GifDecoder(BitmapProvider provider) {
        this.bitmapProvider = provider;
        this.header = new GifHeader();
    }

    public int getWidth() {
        return this.header.width;
    }

    public int getHeight() {
        return this.header.height;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getStatus() {
        return this.status;
    }

    public void advance() {
        this.framePointer = (this.framePointer + 1) % this.header.frameCount;
    }

    public int getDelay(int n) {
        if (n < 0 || n >= this.header.frameCount) {
            return -1;
        }
        return this.header.frames.get(n).delay;
    }

    public int getNextDelay() {
        if (this.header.frameCount <= 0 || this.framePointer < 0) {
            return -1;
        }
        return getDelay(this.framePointer);
    }

    public int getFrameCount() {
        return this.header.frameCount;
    }

    public int getCurrentFrameIndex() {
        return this.framePointer;
    }

    public void resetFrameIndex() {
        this.framePointer = -1;
    }

    public int getLoopCount() {
        return this.header.loopCount;
    }

    public synchronized Bitmap getNextFrame() {
        Bitmap bitmap = null;
        synchronized (this) {
            if (this.header.frameCount <= 0 || this.framePointer < 0) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "unable to decode frame, frameCount=" + this.header.frameCount + " framePointer=" + this.framePointer);
                }
                this.status = 1;
            }
            if (this.status != 1 && this.status != 2) {
                this.status = 0;
                GifFrame currentFrame = this.header.frames.get(this.framePointer);
                GifFrame previousFrame = null;
                int previousIndex = this.framePointer - 1;
                if (previousIndex >= 0) {
                    previousFrame = this.header.frames.get(previousIndex);
                }
                if (currentFrame.lct == null) {
                    this.act = this.header.gct;
                } else {
                    this.act = currentFrame.lct;
                    if (this.header.bgIndex == currentFrame.transIndex) {
                        this.header.bgColor = 0;
                    }
                }
                int save = 0;
                if (currentFrame.transparency) {
                    save = this.act[currentFrame.transIndex];
                    this.act[currentFrame.transIndex] = 0;
                }
                if (this.act == null) {
                    if (Log.isLoggable(TAG, 3)) {
                        Log.d(TAG, "No Valid Color Table");
                    }
                    this.status = 1;
                } else {
                    bitmap = setPixels(currentFrame, previousFrame);
                    if (currentFrame.transparency) {
                        this.act[currentFrame.transIndex] = save;
                    }
                }
            } else if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Unable to decode frame, status=" + this.status);
            }
        }
        return bitmap;
    }

    public int read(InputStream is, int contentLength) {
        int capacity = 16384;
        if (is != null) {
            if (contentLength > 0) {
                capacity = contentLength + 4096;
            }
            try {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream(capacity);
                byte[] data2 = new byte[16384];
                while (true) {
                    int nRead = is.read(data2, 0, data2.length);
                    if (nRead == -1) {
                        break;
                    }
                    buffer.write(data2, 0, nRead);
                }
                buffer.flush();
                read(buffer.toByteArray());
            } catch (IOException e) {
                Log.w(TAG, "Error reading data from stream", e);
            }
        } else {
            this.status = 2;
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e2) {
                Log.w(TAG, "Error closing stream", e2);
            }
        }
        return this.status;
    }

    public void clear() {
        this.header = null;
        this.data = null;
        this.mainPixels = null;
        this.mainScratch = null;
        if (this.previousImage != null) {
            this.bitmapProvider.release(this.previousImage);
        }
        this.previousImage = null;
        this.rawData = null;
    }

    public void setData(GifHeader header2, byte[] data2) {
        this.header = header2;
        this.data = data2;
        this.status = 0;
        this.framePointer = -1;
        this.rawData = ByteBuffer.wrap(data2);
        this.rawData.rewind();
        this.rawData.order(ByteOrder.LITTLE_ENDIAN);
        this.savePrevious = false;
        Iterator i$ = header2.frames.iterator();
        while (true) {
            if (i$.hasNext()) {
                if (i$.next().dispose == 3) {
                    this.savePrevious = true;
                    break;
                }
            } else {
                break;
            }
        }
        this.mainPixels = new byte[(header2.width * header2.height)];
        this.mainScratch = new int[(header2.width * header2.height)];
    }

    private GifHeaderParser getHeaderParser() {
        if (this.parser == null) {
            this.parser = new GifHeaderParser();
        }
        return this.parser;
    }

    public int read(byte[] data2) {
        this.data = data2;
        this.header = getHeaderParser().setData(data2).parseHeader();
        if (data2 != null) {
            this.rawData = ByteBuffer.wrap(data2);
            this.rawData.rewind();
            this.rawData.order(ByteOrder.LITTLE_ENDIAN);
            this.mainPixels = new byte[(this.header.width * this.header.height)];
            this.mainScratch = new int[(this.header.width * this.header.height)];
            this.savePrevious = false;
            Iterator i$ = this.header.frames.iterator();
            while (true) {
                if (i$.hasNext()) {
                    if (i$.next().dispose == 3) {
                        this.savePrevious = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return this.status;
    }

    private Bitmap setPixels(GifFrame currentFrame, GifFrame previousFrame) {
        int width = this.header.width;
        int height = this.header.height;
        int[] dest = this.mainScratch;
        if (previousFrame != null && previousFrame.dispose > 0) {
            if (previousFrame.dispose == 2) {
                int c = 0;
                if (!currentFrame.transparency) {
                    c = this.header.bgColor;
                }
                Arrays.fill(dest, c);
            } else if (previousFrame.dispose == 3 && this.previousImage != null) {
                this.previousImage.getPixels(dest, 0, width, 0, 0, width, height);
            }
        }
        decodeBitmapData(currentFrame);
        int pass = 1;
        int inc = 8;
        int iline = 0;
        for (int i = 0; i < currentFrame.f1175ih; i++) {
            int line = i;
            if (currentFrame.interlace) {
                if (iline >= currentFrame.f1175ih) {
                    pass++;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            inc = 4;
                            break;
                        case 4:
                            iline = 1;
                            inc = 2;
                            break;
                    }
                }
                line = iline;
                iline += inc;
            }
            int line2 = line + currentFrame.f1178iy;
            if (line2 < this.header.height) {
                int k = line2 * this.header.width;
                int dx = k + currentFrame.f1177ix;
                int dlim = dx + currentFrame.f1176iw;
                if (this.header.width + k < dlim) {
                    dlim = k + this.header.width;
                }
                int sx = i * currentFrame.f1176iw;
                while (dx < dlim) {
                    int sx2 = sx + 1;
                    int c2 = this.act[this.mainPixels[sx] & 255];
                    if (c2 != 0) {
                        dest[dx] = c2;
                    }
                    dx++;
                    sx = sx2;
                }
            }
        }
        if (this.savePrevious && (currentFrame.dispose == 0 || currentFrame.dispose == 1)) {
            if (this.previousImage == null) {
                this.previousImage = getNextBitmap();
            }
            this.previousImage.setPixels(dest, 0, width, 0, 0, width, height);
        }
        Bitmap result = getNextBitmap();
        result.setPixels(dest, 0, width, 0, 0, width, height);
        return result;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v0, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: byte} */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=int, for r6v5, types: [short] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void decodeBitmapData(com.bumptech.glide.gifdecoder.GifFrame r25) {
        /*
            r24 = this;
            if (r25 == 0) goto L_0x0011
            r0 = r24
            java.nio.ByteBuffer r0 = r0.rawData
            r22 = r0
            r0 = r25
            int r0 = r0.bufferFrameStart
            r23 = r0
            r22.position(r23)
        L_0x0011:
            if (r25 != 0) goto L_0x00c5
            r0 = r24
            com.bumptech.glide.gifdecoder.GifHeader r0 = r0.header
            r22 = r0
            r0 = r22
            int r0 = r0.width
            r22 = r0
            r0 = r24
            com.bumptech.glide.gifdecoder.GifHeader r0 = r0.header
            r23 = r0
            r0 = r23
            int r0 = r0.height
            r23 = r0
            int r16 = r22 * r23
        L_0x002d:
            r0 = r24
            byte[] r0 = r0.mainPixels
            r22 = r0
            if (r22 == 0) goto L_0x0046
            r0 = r24
            byte[] r0 = r0.mainPixels
            r22 = r0
            r0 = r22
            int r0 = r0.length
            r22 = r0
            r0 = r22
            r1 = r16
            if (r0 >= r1) goto L_0x0052
        L_0x0046:
            r0 = r16
            byte[] r0 = new byte[r0]
            r22 = r0
            r0 = r22
            r1 = r24
            r1.mainPixels = r0
        L_0x0052:
            r0 = r24
            short[] r0 = r0.prefix
            r22 = r0
            if (r22 != 0) goto L_0x0068
            r22 = 4096(0x1000, float:5.74E-42)
            r0 = r22
            short[] r0 = new short[r0]
            r22 = r0
            r0 = r22
            r1 = r24
            r1.prefix = r0
        L_0x0068:
            r0 = r24
            byte[] r0 = r0.suffix
            r22 = r0
            if (r22 != 0) goto L_0x007e
            r22 = 4096(0x1000, float:5.74E-42)
            r0 = r22
            byte[] r0 = new byte[r0]
            r22 = r0
            r0 = r22
            r1 = r24
            r1.suffix = r0
        L_0x007e:
            r0 = r24
            byte[] r0 = r0.pixelStack
            r22 = r0
            if (r22 != 0) goto L_0x0094
            r22 = 4097(0x1001, float:5.741E-42)
            r0 = r22
            byte[] r0 = new byte[r0]
            r22 = r0
            r0 = r22
            r1 = r24
            r1.pixelStack = r0
        L_0x0094:
            int r10 = r24.read()
            r22 = 1
            int r5 = r22 << r10
            int r12 = r5 + 1
            int r2 = r5 + 2
            r17 = -1
            int r8 = r10 + 1
            r22 = 1
            int r22 = r22 << r8
            int r7 = r22 + -1
            r6 = 0
        L_0x00ab:
            if (r6 >= r5) goto L_0x00d5
            r0 = r24
            short[] r0 = r0.prefix
            r22 = r0
            r23 = 0
            r22[r6] = r23
            r0 = r24
            byte[] r0 = r0.suffix
            r22 = r0
            byte r0 = (byte) r6
            r23 = r0
            r22[r6] = r23
            int r6 = r6 + 1
            goto L_0x00ab
        L_0x00c5:
            r0 = r25
            int r0 = r0.f1176iw
            r22 = r0
            r0 = r25
            int r0 = r0.f1175ih
            r23 = r0
            int r16 = r22 * r23
            goto L_0x002d
        L_0x00d5:
            r3 = 0
            r18 = r3
            r20 = r3
            r13 = r3
            r9 = r3
            r4 = r3
            r11 = r3
            r14 = 0
        L_0x00df:
            r0 = r16
            if (r14 >= r0) goto L_0x00f3
            if (r9 != 0) goto L_0x0107
            int r9 = r24.readBlock()
            if (r9 > 0) goto L_0x0106
            r22 = 3
            r0 = r22
            r1 = r24
            r1.status = r0
        L_0x00f3:
            r14 = r18
        L_0x00f5:
            r0 = r16
            if (r14 >= r0) goto L_0x0200
            r0 = r24
            byte[] r0 = r0.mainPixels
            r22 = r0
            r23 = 0
            r22[r14] = r23
            int r14 = r14 + 1
            goto L_0x00f5
        L_0x0106:
            r3 = 0
        L_0x0107:
            r0 = r24
            byte[] r0 = r0.block
            r22 = r0
            byte r22 = r22[r3]
            r0 = r22
            r0 = r0 & 255(0xff, float:3.57E-43)
            r22 = r0
            int r22 = r22 << r4
            int r11 = r11 + r22
            int r4 = r4 + 8
            int r3 = r3 + 1
            int r9 = r9 + -1
            r21 = r20
        L_0x0121:
            if (r4 < r8) goto L_0x0207
            r6 = r11 & r7
            int r11 = r11 >> r8
            int r4 = r4 - r8
            if (r6 != r5) goto L_0x0136
            int r8 = r10 + 1
            r22 = 1
            int r22 = r22 << r8
            int r7 = r22 + -1
            int r2 = r5 + 2
            r17 = -1
            goto L_0x0121
        L_0x0136:
            if (r6 <= r2) goto L_0x0143
            r22 = 3
            r0 = r22
            r1 = r24
            r1.status = r0
            r20 = r21
            goto L_0x00df
        L_0x0143:
            if (r6 != r12) goto L_0x0148
            r20 = r21
            goto L_0x00df
        L_0x0148:
            r22 = -1
            r0 = r17
            r1 = r22
            if (r0 != r1) goto L_0x0168
            r0 = r24
            byte[] r0 = r0.pixelStack
            r22 = r0
            int r20 = r21 + 1
            r0 = r24
            byte[] r0 = r0.suffix
            r23 = r0
            byte r23 = r23[r6]
            r22[r21] = r23
            r17 = r6
            r13 = r6
            r21 = r20
            goto L_0x0121
        L_0x0168:
            r15 = r6
            if (r6 < r2) goto L_0x017c
            r0 = r24
            byte[] r0 = r0.pixelStack
            r22 = r0
            int r20 = r21 + 1
            byte r0 = (byte) r13
            r23 = r0
            r22[r21] = r23
            r6 = r17
            r21 = r20
        L_0x017c:
            if (r6 < r5) goto L_0x019b
            r0 = r24
            byte[] r0 = r0.pixelStack
            r22 = r0
            int r20 = r21 + 1
            r0 = r24
            byte[] r0 = r0.suffix
            r23 = r0
            byte r23 = r23[r6]
            r22[r21] = r23
            r0 = r24
            short[] r0 = r0.prefix
            r22 = r0
            short r6 = r22[r6]
            r21 = r20
            goto L_0x017c
        L_0x019b:
            r0 = r24
            byte[] r0 = r0.suffix
            r22 = r0
            byte r22 = r22[r6]
            r0 = r22
            r13 = r0 & 255(0xff, float:3.57E-43)
            r0 = r24
            byte[] r0 = r0.pixelStack
            r22 = r0
            int r20 = r21 + 1
            byte r0 = (byte) r13
            r23 = r0
            r22[r21] = r23
            r22 = 4096(0x1000, float:5.74E-42)
            r0 = r22
            if (r2 >= r0) goto L_0x01e1
            r0 = r24
            short[] r0 = r0.prefix
            r22 = r0
            r0 = r17
            short r0 = (short) r0
            r23 = r0
            r22[r2] = r23
            r0 = r24
            byte[] r0 = r0.suffix
            r22 = r0
            byte r0 = (byte) r13
            r23 = r0
            r22[r2] = r23
            int r2 = r2 + 1
            r22 = r2 & r7
            if (r22 != 0) goto L_0x01e1
            r22 = 4096(0x1000, float:5.74E-42)
            r0 = r22
            if (r2 >= r0) goto L_0x01e1
            int r8 = r8 + 1
            int r7 = r7 + r2
        L_0x01e1:
            r17 = r15
            r19 = r18
        L_0x01e5:
            if (r20 <= 0) goto L_0x0201
            int r20 = r20 + -1
            r0 = r24
            byte[] r0 = r0.mainPixels
            r22 = r0
            int r18 = r19 + 1
            r0 = r24
            byte[] r0 = r0.pixelStack
            r23 = r0
            byte r23 = r23[r20]
            r22[r19] = r23
            int r14 = r14 + 1
            r19 = r18
            goto L_0x01e5
        L_0x0200:
            return
        L_0x0201:
            r18 = r19
            r21 = r20
            goto L_0x0121
        L_0x0207:
            r20 = r21
            goto L_0x00df
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.gifdecoder.GifDecoder.decodeBitmapData(com.bumptech.glide.gifdecoder.GifFrame):void");
    }

    private int read() {
        try {
            return this.rawData.get() & 255;
        } catch (Exception e) {
            this.status = 1;
            return 0;
        }
    }

    private int readBlock() {
        int blockSize = read();
        int n = 0;
        if (blockSize > 0) {
            while (n < blockSize) {
                int count = blockSize - n;
                try {
                    this.rawData.get(this.block, n, count);
                    n += count;
                } catch (Exception e) {
                    Log.w(TAG, "Error Reading Block", e);
                    this.status = 1;
                }
            }
        }
        return n;
    }

    private Bitmap getNextBitmap() {
        Bitmap result = this.bitmapProvider.obtain(this.header.width, this.header.height, BITMAP_CONFIG);
        if (result == null) {
            result = Bitmap.createBitmap(this.header.width, this.header.height, BITMAP_CONFIG);
        }
        setAlpha(result);
        return result;
    }

    @TargetApi(12)
    private static void setAlpha(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 12) {
            bitmap.setHasAlpha(true);
        }
    }
}
