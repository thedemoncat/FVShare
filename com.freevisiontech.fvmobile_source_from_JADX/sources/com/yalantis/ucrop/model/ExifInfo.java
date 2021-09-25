package com.yalantis.ucrop.model;

public class ExifInfo {
    private int mExifDegrees;
    private int mExifOrientation;
    private int mExifTranslation;

    public ExifInfo(int exifOrientation, int exifDegrees, int exifTranslation) {
        this.mExifOrientation = exifOrientation;
        this.mExifDegrees = exifDegrees;
        this.mExifTranslation = exifTranslation;
    }

    public int getExifOrientation() {
        return this.mExifOrientation;
    }

    public int getExifDegrees() {
        return this.mExifDegrees;
    }

    public int getExifTranslation() {
        return this.mExifTranslation;
    }

    public void setExifOrientation(int exifOrientation) {
        this.mExifOrientation = exifOrientation;
    }

    public void setExifDegrees(int exifDegrees) {
        this.mExifDegrees = exifDegrees;
    }

    public void setExifTranslation(int exifTranslation) {
        this.mExifTranslation = exifTranslation;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExifInfo exifInfo = (ExifInfo) o;
        if (this.mExifOrientation != exifInfo.mExifOrientation || this.mExifDegrees != exifInfo.mExifDegrees) {
            return false;
        }
        if (this.mExifTranslation != exifInfo.mExifTranslation) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((this.mExifOrientation * 31) + this.mExifDegrees) * 31) + this.mExifTranslation;
    }
}
