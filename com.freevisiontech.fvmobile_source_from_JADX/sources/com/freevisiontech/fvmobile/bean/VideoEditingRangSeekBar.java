package com.freevisiontech.fvmobile.bean;

import java.util.List;

public class VideoEditingRangSeekBar {
    private float max;
    private float min;
    private int position;
    private List rangSeekList;

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position2) {
        this.position = position2;
    }

    public float getMin() {
        return this.min;
    }

    public void setMin(float min2) {
        this.min = min2;
    }

    public float getMax() {
        return this.max;
    }

    public void setMax(float max2) {
        this.max = max2;
    }

    public List getRangSeekList() {
        return this.rangSeekList;
    }

    public void setRangSeekList(List rangSeekList2) {
        this.rangSeekList = rangSeekList2;
    }

    public String toString() {
        return "VideoEditingRangSeekBar{position=" + this.position + ", min=" + this.min + ", max=" + this.max + ", rangSeekList=" + this.rangSeekList + '}';
    }
}
