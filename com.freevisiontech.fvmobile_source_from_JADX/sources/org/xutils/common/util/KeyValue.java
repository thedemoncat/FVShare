package org.xutils.common.util;

public class KeyValue {
    public final String key;
    public final Object value;

    public KeyValue(String key2, Object value2) {
        this.key = key2;
        this.value = value2;
    }

    public String getValueStr() {
        if (this.value == null) {
            return null;
        }
        return this.value.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyValue keyValue = (KeyValue) o;
        if (this.key != null) {
            return this.key.equals(keyValue.key);
        }
        if (keyValue.key != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.key != null) {
            return this.key.hashCode();
        }
        return 0;
    }

    public String toString() {
        return "KeyValue{key='" + this.key + '\'' + ", value=" + this.value + '}';
    }
}
