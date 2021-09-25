package com.yalantis.ucrop.model;

import android.graphics.Bitmap;

public class CropParameters {
    private Bitmap.CompressFormat mCompressFormat;
    private int mCompressQuality;
    private ExifInfo mExifInfo;
    private String mImageInputPath;
    private String mImageOutputPath;
    private int mMaxResultImageSizeX;
    private int mMaxResultImageSizeY;

    public CropParameters(int maxResultImageSizeX, int maxResultImageSizeY, Bitmap.CompressFormat compressFormat, int compressQuality, String imageInputPath, String imageOutputPath, ExifInfo exifInfo) {
        this.mMaxResultImageSizeX = maxResultImageSizeX;
        this.mMaxResultImageSizeY = maxResultImageSizeY;
        this.mCompressFormat = compressFormat;
        this.mCompressQuality = compressQuality;
        this.mImageInputPath = imageInputPath;
        this.mImageOutputPath = imageOutputPath;
        this.mExifInfo = exifInfo;
    }

    public int getMaxResultImageSizeX() {
        return this.mMaxResultImageSizeX;
    }

    public int getMaxResultImageSizeY() {
        return this.mMaxResultImageSizeY;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return this.mCompressFormat;
    }

    public int getCompressQuality() {
        return this.mCompressQuality;
    }

    public String getImageInputPath() {
        return this.mImageInputPath;
    }

    public String getImageOutputPath() {
        return this.mImageOutputPath;
    }

    public ExifInfo getExifInfo() {
        return this.mExifInfo;
    }
}
