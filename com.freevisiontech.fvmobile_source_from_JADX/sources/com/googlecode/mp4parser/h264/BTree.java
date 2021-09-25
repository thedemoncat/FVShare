package com.googlecode.mp4parser.h264;

public class BTree {
    private BTree one;
    private Object value;
    private BTree zero;

    public void addString(String path, Object value2) {
        BTree branch;
        if (path.length() == 0) {
            this.value = value2;
            return;
        }
        if (path.charAt(0) == '0') {
            if (this.zero == null) {
                this.zero = new BTree();
            }
            branch = this.zero;
        } else {
            if (this.one == null) {
                this.one = new BTree();
            }
            branch = this.one;
        }
        branch.addString(path.substring(1), value2);
    }

    public BTree down(int b) {
        if (b == 0) {
            return this.zero;
        }
        return this.one;
    }

    public Object getValue() {
        return this.value;
    }
}
