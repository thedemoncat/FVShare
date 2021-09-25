package com.freevisiontech.fvmobile.bean;

public class MoveTimeLapseBean {
    private Integer XPoint;
    private Integer YPoint;
    private Integer ZPoint;

    public Integer getXPoint() {
        return this.XPoint;
    }

    public void setXPoint(int XPoint2) {
        this.XPoint = Integer.valueOf(XPoint2);
    }

    public Integer getYPoint() {
        return this.YPoint;
    }

    public void setYPoint(int YPoint2) {
        this.YPoint = Integer.valueOf(YPoint2);
    }

    public Integer getZPoint() {
        return this.ZPoint;
    }

    public void setZPoint(Integer ZPoint2) {
        this.ZPoint = ZPoint2;
    }
}
