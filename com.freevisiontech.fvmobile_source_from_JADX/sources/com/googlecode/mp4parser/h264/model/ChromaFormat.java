package com.googlecode.mp4parser.h264.model;

public class ChromaFormat {
    public static ChromaFormat MONOCHROME = new ChromaFormat(0, 0, 0);
    public static ChromaFormat YUV_420 = new ChromaFormat(1, 2, 2);
    public static ChromaFormat YUV_422 = new ChromaFormat(2, 2, 1);
    public static ChromaFormat YUV_444 = new ChromaFormat(3, 1, 1);

    /* renamed from: id */
    private int f1035id;
    private int subHeight;
    private int subWidth;

    public ChromaFormat(int id, int subWidth2, int subHeight2) {
        this.f1035id = id;
        this.subWidth = subWidth2;
        this.subHeight = subHeight2;
    }

    public static ChromaFormat fromId(int id) {
        if (id == MONOCHROME.f1035id) {
            return MONOCHROME;
        }
        if (id == YUV_420.f1035id) {
            return YUV_420;
        }
        if (id == YUV_422.f1035id) {
            return YUV_422;
        }
        if (id == YUV_444.f1035id) {
            return YUV_444;
        }
        return null;
    }

    public int getId() {
        return this.f1035id;
    }

    public int getSubWidth() {
        return this.subWidth;
    }

    public int getSubHeight() {
        return this.subHeight;
    }

    public String toString() {
        return "ChromaFormat{\nid=" + this.f1035id + ",\n" + " subWidth=" + this.subWidth + ",\n" + " subHeight=" + this.subHeight + '}';
    }
}
