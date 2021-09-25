package com.vise.log.inner;

public class SoulsTree extends Tree {
    private volatile Tree[] forestAsArray = new Tree[0];

    public void wtf(String message, Object... args) {
        for (Tree wtf : this.forestAsArray) {
            wtf.wtf(message, args);
        }
    }

    public void wtf(Object object) {
        for (Tree wtf : this.forestAsArray) {
            wtf.wtf(object);
        }
    }

    /* renamed from: e */
    public void mo8869e(String message, Object... args) {
        for (Tree e : this.forestAsArray) {
            e.mo8869e(message, args);
        }
    }

    /* renamed from: e */
    public void mo8868e(Object object) {
        for (Tree e : this.forestAsArray) {
            e.mo8868e(object);
        }
    }

    /* renamed from: w */
    public void mo8876w(String message, Object... args) {
        for (Tree w : this.forestAsArray) {
            w.mo8876w(message, args);
        }
    }

    /* renamed from: w */
    public void mo8875w(Object object) {
        for (Tree w : this.forestAsArray) {
            w.mo8875w(object);
        }
    }

    /* renamed from: d */
    public void mo8867d(String message, Object... args) {
        for (Tree d : this.forestAsArray) {
            d.mo8867d(message, args);
        }
    }

    /* renamed from: d */
    public void mo8866d(Object object) {
        for (Tree d : this.forestAsArray) {
            d.mo8866d(object);
        }
    }

    /* renamed from: i */
    public void mo8871i(String message, Object... args) {
        for (Tree i : this.forestAsArray) {
            i.mo8871i(message, args);
        }
    }

    /* renamed from: i */
    public void mo8870i(Object object) {
        for (Tree i : this.forestAsArray) {
            i.mo8870i(object);
        }
    }

    /* renamed from: v */
    public void mo8874v(String message, Object... args) {
        for (Tree v : this.forestAsArray) {
            v.mo8874v(message, args);
        }
    }

    /* renamed from: v */
    public void mo8873v(Object object) {
        for (Tree v : this.forestAsArray) {
            v.mo8873v(object);
        }
    }

    public void json(String json) {
        for (Tree json2 : this.forestAsArray) {
            json2.json(json);
        }
    }

    public void xml(String xml) {
        for (Tree xml2 : this.forestAsArray) {
            xml2.xml(xml);
        }
    }

    /* access modifiers changed from: protected */
    public void log(int type, String tag, String message) {
        throw new AssertionError("Missing override for log method.");
    }

    public Tree[] getForestAsArray() {
        return this.forestAsArray;
    }

    public void setForestAsArray(Tree[] forestAsArray2) {
        this.forestAsArray = forestAsArray2;
    }
}
