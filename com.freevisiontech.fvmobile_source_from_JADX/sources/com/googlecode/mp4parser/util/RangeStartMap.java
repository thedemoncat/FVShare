package com.googlecode.mp4parser.util;

import java.lang.Comparable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RangeStartMap<K extends Comparable, V> implements Map<K, V> {
    TreeMap<K, V> base = new TreeMap<>(new Comparator<K>() {
        public int compare(K o1, K o2) {
            return -o1.compareTo(o2);
        }
    });

    public RangeStartMap() {
    }

    public RangeStartMap(K k, V v) {
        put(k, v);
    }

    public int size() {
        return this.base.size();
    }

    public boolean isEmpty() {
        return this.base.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.base.get(key) != null;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public V get(Object k) {
        if (!(k instanceof Comparable)) {
            return null;
        }
        Comparable<K> key = (Comparable) k;
        if (isEmpty()) {
            return null;
        }
        Iterator<K> keys = this.base.keySet().iterator();
        K a = keys.next();
        while (true) {
            K a2 = (Comparable) a;
            if (!keys.hasNext()) {
                return this.base.get(a2);
            }
            if (key.compareTo(a2) >= 0) {
                return this.base.get(a2);
            }
            a = keys.next();
        }
    }

    public V put(K key, V value) {
        return this.base.put(key, value);
    }

    public V remove(Object k) {
        if (!(k instanceof Comparable)) {
            return null;
        }
        Comparable<K> key = (Comparable) k;
        if (isEmpty()) {
            return null;
        }
        Iterator<K> keys = this.base.keySet().iterator();
        K a = keys.next();
        while (true) {
            K a2 = (Comparable) a;
            if (!keys.hasNext()) {
                return this.base.remove(a2);
            }
            if (key.compareTo(a2) >= 0) {
                return this.base.remove(a2);
            }
            a = keys.next();
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        this.base.putAll(m);
    }

    public void clear() {
        this.base.clear();
    }

    public Set<K> keySet() {
        return this.base.keySet();
    }

    public Collection<V> values() {
        return this.base.values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return this.base.entrySet();
    }
}
