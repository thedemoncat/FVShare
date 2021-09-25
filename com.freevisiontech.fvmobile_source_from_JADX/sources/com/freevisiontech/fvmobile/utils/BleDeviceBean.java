package com.freevisiontech.fvmobile.utils;

public class BleDeviceBean {
    private String bleName;
    private String ptzType;
    private long time;

    public String getPtzType() {
        return this.ptzType;
    }

    public void setPtzType(String ptzType2) {
        this.ptzType = ptzType2;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time2) {
        this.time = time2;
    }

    public String getBleName() {
        return this.bleName;
    }

    public void setBleName(String bleName2) {
        this.bleName = bleName2;
    }

    public String toString() {
        return "DateBean{time=" + this.time + ", bleName='" + this.bleName + '\'' + ", ptzTypeName=:" + this.ptzType + '\'' + '}';
    }
}
