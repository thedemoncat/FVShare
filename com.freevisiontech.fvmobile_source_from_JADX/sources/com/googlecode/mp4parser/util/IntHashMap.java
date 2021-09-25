package com.googlecode.mp4parser.util;

public class IntHashMap {
    private transient int count;
    private float loadFactor;
    private transient Entry[] table;
    private int threshold;

    private static class Entry {
        int hash;
        int key;
        Entry next;
        Object value;

        protected Entry(int hash2, int key2, Object value2, Entry next2) {
            this.hash = hash2;
            this.key = key2;
            this.value = value2;
            this.next = next2;
        }
    }

    public IntHashMap() {
        this(20, 0.75f);
    }

    public IntHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public IntHashMap(int initialCapacity, float loadFactor2) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        } else if (loadFactor2 <= 0.0f) {
            throw new IllegalArgumentException("Illegal Load: " + loadFactor2);
        } else {
            initialCapacity = initialCapacity == 0 ? 1 : initialCapacity;
            this.loadFactor = loadFactor2;
            this.table = new Entry[initialCapacity];
            this.threshold = (int) (((float) initialCapacity) * loadFactor2);
        }
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public boolean contains(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Entry[] tab = this.table;
        int i = tab.length;
        while (true) {
            int i2 = i;
            i = i2 - 1;
            if (i2 <= 0) {
                return false;
            }
            for (Entry e = tab[i]; e != null; e = e.next) {
                if (e.value.equals(value)) {
                    return true;
                }
            }
        }
    }

    public boolean containsValue(Object value) {
        return contains(value);
    }

    public boolean containsKey(int key) {
        Entry[] tab = this.table;
        int hash = key;
        for (Entry e = tab[(Integer.MAX_VALUE & hash) % tab.length]; e != null; e = e.next) {
            if (e.hash == hash) {
                return true;
            }
        }
        return false;
    }

    public Object get(int key) {
        Entry[] tab = this.table;
        int hash = key;
        for (Entry e = tab[(Integer.MAX_VALUE & hash) % tab.length]; e != null; e = e.next) {
            if (e.hash == hash) {
                return e.value;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void rehash() {
        int oldCapacity = this.table.length;
        Entry[] oldMap = this.table;
        int newCapacity = (oldCapacity * 2) + 1;
        Entry[] newMap = new Entry[newCapacity];
        this.threshold = (int) (((float) newCapacity) * this.loadFactor);
        this.table = newMap;
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i = i2 - 1;
            if (i2 > 0) {
                Entry old = oldMap[i];
                while (old != null) {
                    Entry e = old;
                    old = old.next;
                    int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
                    e.next = newMap[index];
                    newMap[index] = e;
                }
            } else {
                return;
            }
        }
    }

    public Object put(int key, Object value) {
        Entry[] tab = this.table;
        int hash = key;
        int index = (hash & Integer.MAX_VALUE) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                Object obj = e.value;
                e.value = value;
                return obj;
            }
        }
        if (this.count >= this.threshold) {
            rehash();
            tab = this.table;
            index = (hash & Integer.MAX_VALUE) % tab.length;
        }
        tab[index] = new Entry(hash, key, value, tab[index]);
        this.count++;
        return null;
    }

    public Object remove(int key) {
        Entry[] tab = this.table;
        int hash = key;
        int index = (Integer.MAX_VALUE & hash) % tab.length;
        Entry prev = null;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }
                this.count--;
                Object obj = e.value;
                e.value = null;
                return obj;
            }
            prev = e;
        }
        return null;
    }

    public synchronized void clear() {
        Entry[] tab = this.table;
        int index = tab.length;
        while (true) {
            index--;
            if (index < 0) {
                this.count = 0;
            } else {
                tab[index] = null;
            }
        }
    }
}
