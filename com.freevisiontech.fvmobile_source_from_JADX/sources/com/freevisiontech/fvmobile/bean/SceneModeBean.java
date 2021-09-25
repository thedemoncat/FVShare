package com.freevisiontech.fvmobile.bean;

public class SceneModeBean {
    private boolean isChecked = false;
    private int resourceId;
    private String sceneMode;
    private String sceneName;

    public SceneModeBean(String sceneName2, int resourceId2, String sceneMode2) {
        this.sceneName = sceneName2;
        this.resourceId = resourceId2;
        this.sceneMode = sceneMode2;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public void setText(String sceneName2) {
        this.sceneName = sceneName2;
    }

    public int getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(int resourceId2) {
        this.resourceId = resourceId2;
    }

    public String getSceneMode() {
        return this.sceneMode;
    }

    public void setSceneMode(String sceneMode2) {
        this.sceneMode = sceneMode2;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }
}
