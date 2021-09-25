package com.freevisiontech.fvmobile.bean;

import android.graphics.Rect;
import java.util.List;

public class FVModeSelectEvent<T> {
    private List list;
    private T message;
    private int mode;
    private Rect rect;

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode2) {
        this.mode = mode2;
    }

    public T getMessage() {
        return this.message;
    }

    public void setMessage(T message2) {
        this.message = message2;
    }

    public Rect getRect() {
        return this.rect;
    }

    public void setRect(Rect rect2) {
        this.rect = rect2;
    }

    public List getList() {
        return this.list;
    }

    public void setList(List list2) {
        this.list = list2;
    }
}
