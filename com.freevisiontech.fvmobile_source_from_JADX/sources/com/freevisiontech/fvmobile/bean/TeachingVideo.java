package com.freevisiontech.fvmobile.bean;

public class TeachingVideo {
    private String videoContent;
    private String videoTitle;

    public TeachingVideo(String videoTitle2, String videoContent2) {
        this.videoTitle = videoTitle2;
        this.videoContent = videoContent2;
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public void setVideoTitle(String videoTitle2) {
        this.videoTitle = videoTitle2;
    }

    public String getVideoContent() {
        return this.videoContent;
    }

    public void setVideoContent(String videoContent2) {
        this.videoContent = videoContent2;
    }
}
