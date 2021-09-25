package com.freevisiontech.fvmobile.bean;

public class VideoEditingBean {
    private double endTime;
    private double startTime;
    private String videoEndPath;
    private String videoStartPath;

    public double getStartTime() {
        return this.startTime;
    }

    public void setStartTime(double startTime2) {
        this.startTime = startTime2;
    }

    public double getEndTime() {
        return this.endTime;
    }

    public void setEndTime(double endTime2) {
        this.endTime = endTime2;
    }

    public String getVideoStartPath() {
        return this.videoStartPath;
    }

    public void setVideoStartPath(String videoStartPath2) {
        this.videoStartPath = videoStartPath2;
    }

    public String getVideoEndPath() {
        return this.videoEndPath;
    }

    public void setVideoEndPath(String videoEndPath2) {
        this.videoEndPath = videoEndPath2;
    }

    public String toString() {
        return "VideoEditingBean{startTime=" + this.startTime + ", endTime=" + this.endTime + ", videoStartPath='" + this.videoStartPath + '\'' + ", videoEndPath='" + this.videoEndPath + '\'' + '}';
    }
}
