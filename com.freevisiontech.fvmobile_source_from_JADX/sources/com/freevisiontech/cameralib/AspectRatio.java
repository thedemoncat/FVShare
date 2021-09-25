package com.freevisiontech.cameralib;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.p001v4.util.SparseArrayCompat;

public class AspectRatio implements Comparable<AspectRatio>, Parcelable {
    public static final Parcelable.Creator<AspectRatio> CREATOR = new Parcelable.Creator<AspectRatio>() {
        public AspectRatio createFromParcel(Parcel source) {
            return AspectRatio.m1507of(source.readInt(), source.readInt());
        }

        public AspectRatio[] newArray(int size) {
            return new AspectRatio[size];
        }
    };
    private static final SparseArrayCompat<SparseArrayCompat<AspectRatio>> sCache = new SparseArrayCompat<>(16);

    /* renamed from: mX */
    private final int f1072mX;

    /* renamed from: mY */
    private final int f1073mY;

    /* renamed from: of */
    public static AspectRatio m1507of(int x, int y) {
        int gcd = gcd(x, y);
        int x2 = x / gcd;
        int y2 = y / gcd;
        SparseArrayCompat<AspectRatio> arrayX = sCache.get(x2);
        if (arrayX == null) {
            AspectRatio ratio = new AspectRatio(x2, y2);
            SparseArrayCompat<AspectRatio> arrayX2 = new SparseArrayCompat<>();
            arrayX2.put(y2, ratio);
            sCache.put(x2, arrayX2);
            return ratio;
        }
        AspectRatio ratio2 = arrayX.get(y2);
        if (ratio2 == null) {
            ratio2 = new AspectRatio(x2, y2);
            arrayX.put(y2, ratio2);
        }
        return ratio2;
    }

    public static AspectRatio parse(String s) {
        int position = s.indexOf(58);
        if (position == -1) {
            throw new IllegalArgumentException("Malformed aspect ratio: " + s);
        }
        try {
            return m1507of(Integer.parseInt(s.substring(0, position)), Integer.parseInt(s.substring(position + 1)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Malformed aspect ratio: " + s, e);
        }
    }

    private AspectRatio(int x, int y) {
        this.f1072mX = x;
        this.f1073mY = y;
    }

    public int getX() {
        return this.f1072mX;
    }

    public int getY() {
        return this.f1073mY;
    }

    public boolean matches(Size size) {
        int gcd = gcd(size.getWidth(), size.getHeight());
        return this.f1072mX == size.getWidth() / gcd && this.f1073mY == size.getHeight() / gcd;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof AspectRatio)) {
            return false;
        }
        AspectRatio ratio = (AspectRatio) o;
        if (!(this.f1072mX == ratio.f1072mX && this.f1073mY == ratio.f1073mY)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return this.f1072mX + ":" + this.f1073mY;
    }

    public float toFloat() {
        return ((float) this.f1072mX) / ((float) this.f1073mY);
    }

    public int hashCode() {
        return this.f1073mY ^ ((this.f1072mX << 16) | (this.f1072mX >>> 16));
    }

    public int compareTo(@NonNull AspectRatio another) {
        if (equals(another)) {
            return 0;
        }
        if (toFloat() - another.toFloat() > 0.0f) {
            return 1;
        }
        return -1;
    }

    public AspectRatio inverse() {
        return m1507of(this.f1073mY, this.f1072mX);
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int c = b;
            b = a % b;
            a = c;
        }
        return a;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.f1072mX);
        dest.writeInt(this.f1073mY);
    }
}
