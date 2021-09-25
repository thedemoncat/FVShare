package org.opencv.core;

public class DMatch {
    public float distance;
    public int imgIdx;
    public int queryIdx;
    public int trainIdx;

    public DMatch() {
        this(-1, -1, Float.MAX_VALUE);
    }

    public DMatch(int _queryIdx, int _trainIdx, float _distance) {
        this.queryIdx = _queryIdx;
        this.trainIdx = _trainIdx;
        this.imgIdx = -1;
        this.distance = _distance;
    }

    public DMatch(int _queryIdx, int _trainIdx, int _imgIdx, float _distance) {
        this.queryIdx = _queryIdx;
        this.trainIdx = _trainIdx;
        this.imgIdx = _imgIdx;
        this.distance = _distance;
    }

    public boolean lessThan(DMatch it) {
        return this.distance < it.distance;
    }

    public String toString() {
        return "DMatch [queryIdx=" + this.queryIdx + ", trainIdx=" + this.trainIdx + ", imgIdx=" + this.imgIdx + ", distance=" + this.distance + "]";
    }
}
