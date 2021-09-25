package com.googlecode.mp4parser.authoring;

import com.googlecode.mp4parser.util.Matrix;
import java.util.Date;

public class TrackMetaData implements Cloneable {
    private Date creationTime = new Date();
    private int group = 0;
    private double height;
    private String language = "eng";
    int layer;
    private Matrix matrix = Matrix.ROTATE_0;
    private Date modificationTime = new Date();
    private long timescale;
    private long trackId = 1;
    private float volume;
    private double width;

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language2) {
        this.language = language2;
    }

    public long getTimescale() {
        return this.timescale;
    }

    public void setTimescale(long timescale2) {
        this.timescale = timescale2;
    }

    public Date getModificationTime() {
        return this.modificationTime;
    }

    public void setModificationTime(Date modificationTime2) {
        this.modificationTime = modificationTime2;
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(Date creationTime2) {
        this.creationTime = creationTime2;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width2) {
        this.width = width2;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height2) {
        this.height = height2;
    }

    public long getTrackId() {
        return this.trackId;
    }

    public void setTrackId(long trackId2) {
        this.trackId = trackId2;
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLayer(int layer2) {
        this.layer = layer2;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume2) {
        this.volume = volume2;
    }

    public int getGroup() {
        return this.group;
    }

    public void setGroup(int group2) {
        this.group = group2;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public void setMatrix(Matrix matrix2) {
        this.matrix = matrix2;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
