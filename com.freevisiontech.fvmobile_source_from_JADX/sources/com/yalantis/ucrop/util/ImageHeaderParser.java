package com.yalantis.ucrop.util;

import android.media.ExifInterface;
import android.support.p001v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.Log;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class ImageHeaderParser {
    private static final int[] BYTES_PER_FORMAT = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
    private static final int EXIF_MAGIC_NUMBER = 65496;
    private static final int EXIF_SEGMENT_TYPE = 225;
    private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
    private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\u0000\u0000";
    private static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = JPEG_EXIF_SEGMENT_PREAMBLE.getBytes(Charset.forName("UTF-8"));
    private static final int MARKER_EOI = 217;
    private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
    private static final int ORIENTATION_TAG_TYPE = 274;
    private static final int SEGMENT_SOS = 218;
    private static final int SEGMENT_START_ID = 255;
    private static final String TAG = "ImageHeaderParser";
    public static final int UNKNOWN_ORIENTATION = -1;
    private final Reader reader;

    private interface Reader {
        int getUInt16() throws IOException;

        short getUInt8() throws IOException;

        int read(byte[] bArr, int i) throws IOException;

        long skip(long j) throws IOException;
    }

    public ImageHeaderParser(InputStream is) {
        this.reader = new StreamReader(is);
    }

    public int getOrientation() throws IOException {
        int magicNumber = this.reader.getUInt16();
        if (handles(magicNumber)) {
            int exifSegmentLength = moveToExifSegmentAndGetLength();
            if (exifSegmentLength != -1) {
                return parseExifSegment(new byte[exifSegmentLength], exifSegmentLength);
            }
            if (!Log.isLoggable(TAG, 3)) {
                return -1;
            }
            Log.d(TAG, "Failed to parse exif segment length, or exif segment not found");
            return -1;
        } else if (!Log.isLoggable(TAG, 3)) {
            return -1;
        } else {
            Log.d(TAG, "Parser doesn't handle magic number: " + magicNumber);
            return -1;
        }
    }

    private int parseExifSegment(byte[] tempArray, int exifSegmentLength) throws IOException {
        int read = this.reader.read(tempArray, exifSegmentLength);
        if (read != exifSegmentLength) {
            if (!Log.isLoggable(TAG, 3)) {
                return -1;
            }
            Log.d(TAG, "Unable to read exif segment data, length: " + exifSegmentLength + ", actually read: " + read);
            return -1;
        } else if (hasJpegExifPreamble(tempArray, exifSegmentLength)) {
            return parseExifSegment(new RandomAccessReader(tempArray, exifSegmentLength));
        } else {
            if (!Log.isLoggable(TAG, 3)) {
                return -1;
            }
            Log.d(TAG, "Missing jpeg exif preamble");
            return -1;
        }
    }

    private boolean hasJpegExifPreamble(byte[] exifData, int exifSegmentLength) {
        boolean result = exifData != null && exifSegmentLength > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length;
        if (!result) {
            return result;
        }
        for (int i = 0; i < JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length; i++) {
            if (exifData[i] != JPEG_EXIF_SEGMENT_PREAMBLE_BYTES[i]) {
                return false;
            }
        }
        return result;
    }

    private int moveToExifSegmentAndGetLength() throws IOException {
        short segmentType;
        int segmentLength;
        long skipped;
        do {
            short segmentId = this.reader.getUInt8();
            if (segmentId != 255) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Unknown segmentId=" + segmentId);
                }
                return -1;
            }
            segmentType = this.reader.getUInt8();
            if (segmentType == 218) {
                return -1;
            }
            if (segmentType == 217) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Found MARKER_EOI in exif segment");
                }
                return -1;
            }
            segmentLength = this.reader.getUInt16() - 2;
            if (segmentType == 225) {
                return segmentLength;
            }
            skipped = this.reader.skip((long) segmentLength);
        } while (skipped == ((long) segmentLength));
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Unable to skip enough data, type: " + segmentType + ", wanted to skip: " + segmentLength + ", but actually skipped: " + skipped);
        }
        return -1;
    }

    private static int parseExifSegment(RandomAccessReader segmentData) {
        ByteOrder byteOrder;
        int headerOffsetSize = JPEG_EXIF_SEGMENT_PREAMBLE.length();
        short byteOrderIdentifier = segmentData.getInt16(headerOffsetSize);
        if (byteOrderIdentifier == MOTOROLA_TIFF_MAGIC_NUMBER) {
            byteOrder = ByteOrder.BIG_ENDIAN;
        } else if (byteOrderIdentifier == INTEL_TIFF_MAGIC_NUMBER) {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        } else {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Unknown endianness = " + byteOrderIdentifier);
            }
            byteOrder = ByteOrder.BIG_ENDIAN;
        }
        segmentData.order(byteOrder);
        int firstIfdOffset = segmentData.getInt32(headerOffsetSize + 4) + headerOffsetSize;
        int tagCount = segmentData.getInt16(firstIfdOffset);
        for (int i = 0; i < tagCount; i++) {
            int tagOffset = calcTagOffset(firstIfdOffset, i);
            int tagType = segmentData.getInt16(tagOffset);
            if (tagType == 274) {
                int formatCode = segmentData.getInt16(tagOffset + 2);
                if (formatCode >= 1 && formatCode <= 12) {
                    int componentCount = segmentData.getInt32(tagOffset + 4);
                    if (componentCount >= 0) {
                        if (Log.isLoggable(TAG, 3)) {
                            Log.d(TAG, "Got tagIndex=" + i + " tagType=" + tagType + " formatCode=" + formatCode + " componentCount=" + componentCount);
                        }
                        int byteCount = componentCount + BYTES_PER_FORMAT[formatCode];
                        if (byteCount <= 4) {
                            int tagValueOffset = tagOffset + 8;
                            if (tagValueOffset < 0 || tagValueOffset > segmentData.length()) {
                                if (Log.isLoggable(TAG, 3)) {
                                    Log.d(TAG, "Illegal tagValueOffset=" + tagValueOffset + " tagType=" + tagType);
                                }
                            } else if (byteCount >= 0 && tagValueOffset + byteCount <= segmentData.length()) {
                                return segmentData.getInt16(tagValueOffset);
                            } else {
                                if (Log.isLoggable(TAG, 3)) {
                                    Log.d(TAG, "Illegal number of bytes for TI tag data tagType=" + tagType);
                                }
                            }
                        } else if (Log.isLoggable(TAG, 3)) {
                            Log.d(TAG, "Got byte count > 4, not orientation, continuing, formatCode=" + formatCode);
                        }
                    } else if (Log.isLoggable(TAG, 3)) {
                        Log.d(TAG, "Negative tiff component count");
                    }
                } else if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Got invalid format code = " + formatCode);
                }
            }
        }
        return -1;
    }

    private static int calcTagOffset(int ifdOffset, int tagIndex) {
        return ifdOffset + 2 + (tagIndex * 12);
    }

    private static boolean handles(int imageMagicNumber) {
        return (imageMagicNumber & EXIF_MAGIC_NUMBER) == EXIF_MAGIC_NUMBER || imageMagicNumber == MOTOROLA_TIFF_MAGIC_NUMBER || imageMagicNumber == INTEL_TIFF_MAGIC_NUMBER;
    }

    private static class RandomAccessReader {
        private final ByteBuffer data;

        public RandomAccessReader(byte[] data2, int length) {
            this.data = (ByteBuffer) ByteBuffer.wrap(data2).order(ByteOrder.BIG_ENDIAN).limit(length);
        }

        public void order(ByteOrder byteOrder) {
            this.data.order(byteOrder);
        }

        public int length() {
            return this.data.remaining();
        }

        public int getInt32(int offset) {
            return this.data.getInt(offset);
        }

        public short getInt16(int offset) {
            return this.data.getShort(offset);
        }
    }

    private static class StreamReader implements Reader {

        /* renamed from: is */
        private final InputStream f1157is;

        public StreamReader(InputStream is) {
            this.f1157is = is;
        }

        public int getUInt16() throws IOException {
            return ((this.f1157is.read() << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (this.f1157is.read() & 255);
        }

        public short getUInt8() throws IOException {
            return (short) (this.f1157is.read() & 255);
        }

        public long skip(long total) throws IOException {
            if (total < 0) {
                return 0;
            }
            long toSkip = total;
            while (toSkip > 0) {
                long skipped = this.f1157is.skip(toSkip);
                if (skipped > 0) {
                    toSkip -= skipped;
                } else if (this.f1157is.read() == -1) {
                    break;
                } else {
                    toSkip--;
                }
            }
            return total - toSkip;
        }

        public int read(byte[] buffer, int byteCount) throws IOException {
            int toRead = byteCount;
            while (toRead > 0) {
                int read = this.f1157is.read(buffer, byteCount - toRead, toRead);
                if (read == -1) {
                    break;
                }
                toRead -= read;
            }
            return byteCount - toRead;
        }
    }

    public static void copyExif(ExifInterface originalExif, int width, int height, String imageOutputPath) {
        String[] attributes = {"FNumber", "DateTime", "DateTimeDigitized", "ExposureTime", "Flash", "FocalLength", "GPSAltitude", "GPSAltitudeRef", "GPSDateStamp", "GPSLatitude", "GPSLatitudeRef", "GPSLongitude", "GPSLongitudeRef", "GPSProcessingMethod", "GPSTimeStamp", "ISOSpeedRatings", "Make", "Model", "SubSecTime", "SubSecTimeDigitized", "SubSecTimeOriginal", "WhiteBalance"};
        try {
            ExifInterface newExif = new ExifInterface(imageOutputPath);
            for (String attribute : attributes) {
                String value = originalExif.getAttribute(attribute);
                if (!TextUtils.isEmpty(value)) {
                    newExif.setAttribute(attribute, value);
                }
            }
            newExif.setAttribute("ImageWidth", String.valueOf(width));
            newExif.setAttribute("ImageLength", String.valueOf(height));
            newExif.setAttribute(ScreenOrientationUtil.BC_OrientationChangedKey, "0");
            newExif.saveAttributes();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
