package com.freevisiontech.fvmobile.bean;

public class FVPTZBean {
    private String deviceDisplayName;
    private String deviceServiceUUID;
    private String deviceTypeName;

    public String getDeviceTypeName() {
        return this.deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName2) {
        this.deviceTypeName = deviceTypeName2;
    }

    public String getDeviceServiceUUID() {
        return this.deviceServiceUUID;
    }

    public void setDeviceServiceUUID(String deviceUUID) {
        this.deviceServiceUUID = deviceUUID;
    }

    public String getDeviceDisplayName() {
        return this.deviceDisplayName;
    }

    public void setDeviceDisplayName(String deviceShowName) {
        this.deviceDisplayName = deviceShowName;
    }
}
