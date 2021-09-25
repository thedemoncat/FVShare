package org.xutils.view;

final class ViewInfo {
    public int parentId;
    public int value;

    ViewInfo() {
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ViewInfo viewInfo = (ViewInfo) o;
        if (this.value != viewInfo.value) {
            return false;
        }
        if (this.parentId != viewInfo.parentId) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.value * 31) + this.parentId;
    }
}
