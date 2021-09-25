package com.freevisiontech.cameralib;

import android.support.p001v4.util.ArrayMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SizeMap {
    private final ArrayMap<AspectRatio, SortedSet<Size>> mRatios = new ArrayMap<>();

    public boolean add(Size size) {
        try {
            for (AspectRatio ratio : this.mRatios.keySet()) {
                if (ratio.matches(size)) {
                    SortedSet<Size> sizes = this.mRatios.get(ratio);
                    if (sizes.contains(size)) {
                        return false;
                    }
                    sizes.add(size);
                    return true;
                }
            }
            SortedSet<Size> sizes2 = new TreeSet<>();
            sizes2.add(size);
            this.mRatios.put(AspectRatio.m1507of(size.getWidth(), size.getHeight()), sizes2);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void remove(AspectRatio ratio) {
        this.mRatios.remove(ratio);
    }

    public Set<AspectRatio> ratios() {
        return this.mRatios.keySet();
    }

    public SortedSet<Size> sizes(AspectRatio ratio) {
        return this.mRatios.get(ratio);
    }

    public List<Size> allSizes() {
        List<Size> ls = new ArrayList<>();
        for (SortedSet<Size> ss : this.mRatios.values()) {
            for (Size s : ss) {
                ls.add(s);
            }
        }
        return ls;
    }

    public AspectRatio ratioBySize(Size size) {
        for (AspectRatio ratio : this.mRatios.keySet()) {
            if (this.mRatios.get(ratio).contains(size)) {
                return ratio;
            }
        }
        return null;
    }

    public void clear() {
        this.mRatios.clear();
    }

    public boolean isEmpty() {
        return this.mRatios.isEmpty();
    }

    public SizeMap clone() {
        SizeMap map = new SizeMap();
        for (AspectRatio ratio : this.mRatios.keySet()) {
            for (Size size : this.mRatios.get(ratio)) {
                map.add(size);
            }
        }
        return map;
    }

    public boolean contains(Size size) {
        for (AspectRatio ratio : this.mRatios.keySet()) {
            if (ratio.matches(size) && this.mRatios.get(ratio).contains(size)) {
                return true;
            }
        }
        return false;
    }
}
