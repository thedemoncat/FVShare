package com.freevisiontech.cameralib;

import android.support.annotation.NonNull;

public class Size implements Comparable<Size> {
    private final int mHeight;
    private final int mWidth;

    public static Size FromSize(android.util.Size size) {
        return new Size(size.getWidth(), size.getHeight());
    }

    public Size(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Size)) {
            return false;
        }
        Size size = (Size) o;
        if (!(this.mWidth == size.mWidth && this.mHeight == size.mHeight)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return this.mWidth + "x" + this.mHeight;
    }

    public int hashCode() {
        return this.mHeight ^ ((this.mWidth << 16) | (this.mWidth >>> 16));
    }

    public int compareTo(@NonNull Size another) {
        return (this.mWidth * this.mHeight) - (another.mWidth * another.mHeight);
    }

    public android.util.Size toSize() {
        return new android.util.Size(this.mWidth, this.mHeight);
    }
}
