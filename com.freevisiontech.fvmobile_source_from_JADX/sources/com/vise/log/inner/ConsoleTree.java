package com.vise.log.inner;

public class ConsoleTree extends Tree {
    /* access modifiers changed from: protected */
    public void log(int type, String tag, String message) {
        System.out.println(tag + "\t" + message);
    }
}
