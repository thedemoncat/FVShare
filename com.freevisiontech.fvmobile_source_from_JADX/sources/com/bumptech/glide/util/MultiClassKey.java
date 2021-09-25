package com.bumptech.glide.util;

public class MultiClassKey {
    private Class<?> first;
    private Class<?> second;

    public MultiClassKey() {
    }

    public MultiClassKey(Class<?> first2, Class<?> second2) {
        set(first2, second2);
    }

    public void set(Class<?> first2, Class<?> second2) {
        this.first = first2;
        this.second = second2;
    }

    public String toString() {
        return "MultiClassKey{first=" + this.first + ", second=" + this.second + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiClassKey that = (MultiClassKey) o;
        if (!this.first.equals(that.first)) {
            return false;
        }
        if (!this.second.equals(that.second)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.first.hashCode() * 31) + this.second.hashCode();
    }
}
