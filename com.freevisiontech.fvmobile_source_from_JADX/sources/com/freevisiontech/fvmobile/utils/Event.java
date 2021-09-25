package com.freevisiontech.fvmobile.utils;

public class Event<T> {
    private int code;
    private T data;

    public Event(int code2) {
        this.code = code2;
    }

    public Event(int code2, T data2) {
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
