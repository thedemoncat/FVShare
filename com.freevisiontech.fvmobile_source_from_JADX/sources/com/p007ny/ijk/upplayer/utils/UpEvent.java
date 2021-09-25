package com.p007ny.ijk.upplayer.utils;

/* renamed from: com.ny.ijk.upplayer.utils.UpEvent */
public class UpEvent<T> {
    private int code;
    private T data;

    public UpEvent(int code2) {
        this.code = code2;
    }

    public UpEvent(int code2, T data2) {
        this.code = code2;
        this.data = data2;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data2) {
        this.data = data2;
    }
}
