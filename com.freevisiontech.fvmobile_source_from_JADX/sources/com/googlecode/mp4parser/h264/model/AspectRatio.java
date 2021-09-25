package com.googlecode.mp4parser.h264.model;

public class AspectRatio {
    public static final AspectRatio Extended_SAR = new AspectRatio(255);
    private int value;

    private AspectRatio(int value2) {
        this.value = value2;
    }

    public static AspectRatio fromValue(int value2) {
        if (value2 == Extended_SAR.value) {
            return Extended_SAR;
        }
        return new AspectRatio(value2);
    }

    public int getValue() {
        return this.value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("AspectRatio{");
        sb.append("value=").append(this.value);
        sb.append('}');
        return sb.toString();
    }
}
