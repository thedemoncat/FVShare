package com.freevisiontech.fvmobile.bean.network;

public class Advertisement {
    private String code;
    private String endTime;
    private String imageUrl;
    private String startTime;
    private String title;
    private String url;
    private String version;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime2) {
        this.startTime = startTime2;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl2) {
        this.imageUrl = imageUrl2;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version2) {
        this.version = version2;
    }
}
