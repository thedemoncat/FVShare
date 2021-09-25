package com.google.android.exoplayer.upstream.cache;

import android.os.ConditionVariable;
import com.google.android.exoplayer.upstream.cache.Cache;
import com.google.android.exoplayer.util.Assertions;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public final class SimpleCache implements Cache {
    private final File cacheDir;
    private final HashMap<String, TreeSet<CacheSpan>> cachedSpans;
    private final CacheEvictor evictor;
    private final HashMap<String, ArrayList<Cache.Listener>> listeners;
    private final HashMap<String, CacheSpan> lockedSpans;
    private long totalSpace = 0;

    public SimpleCache(File cacheDir2, CacheEvictor evictor2) {
        this.cacheDir = cacheDir2;
        this.evictor = evictor2;
        this.lockedSpans = new HashMap<>();
        this.cachedSpans = new HashMap<>();
        this.listeners = new HashMap<>();
        final ConditionVariable conditionVariable = new ConditionVariable();
        new Thread("SimpleCache.initialize()") {
            public void run() {
                synchronized (SimpleCache.this) {
                    conditionVariable.open();
                    SimpleCache.this.initialize();
                }
            }
        }.start();
        conditionVariable.block();
    }

    public synchronized NavigableSet<CacheSpan> addListener(String key, Cache.Listener listener) {
        ArrayList<Cache.Listener> listenersForKey = this.listeners.get(key);
        if (listenersForKey == null) {
            listenersForKey = new ArrayList<>();
            this.listeners.put(key, listenersForKey);
        }
        listenersForKey.add(listener);
        return getCachedSpans(key);
    }

    public synchronized void removeListener(String key, Cache.Listener listener) {
        ArrayList<Cache.Listener> listenersForKey = this.listeners.get(key);
        if (listenersForKey != null) {
            listenersForKey.remove(listener);
            if (listenersForKey.isEmpty()) {
                this.listeners.remove(key);
            }
        }
    }

    public synchronized NavigableSet<CacheSpan> getCachedSpans(String key) {
        TreeSet<CacheSpan> spansForKey;
        spansForKey = this.cachedSpans.get(key);
        return spansForKey == null ? null : new TreeSet(spansForKey);
    }

    public synchronized Set<String> getKeys() {
        return new HashSet(this.cachedSpans.keySet());
    }

    public synchronized long getCacheSpace() {
        return this.totalSpace;
    }

    public synchronized CacheSpan startReadWrite(String key, long position) throws InterruptedException {
        CacheSpan span;
        CacheSpan lookupSpan = CacheSpan.createLookup(key, position);
        while (true) {
            span = startReadWriteNonBlocking(lookupSpan);
            if (span == null) {
                wait();
            }
        }
        return span;
    }

    public synchronized CacheSpan startReadWriteNonBlocking(String key, long position) {
        return startReadWriteNonBlocking(CacheSpan.createLookup(key, position));
    }

    private synchronized CacheSpan startReadWriteNonBlocking(CacheSpan lookupSpan) {
        CacheSpan cacheSpan;
        CacheSpan spanningRegion = getSpan(lookupSpan);
        if (spanningRegion.isCached) {
            CacheSpan oldCacheSpan = spanningRegion;
            TreeSet<CacheSpan> spansForKey = this.cachedSpans.get(oldCacheSpan.key);
            Assertions.checkState(spansForKey.remove(oldCacheSpan));
            CacheSpan spanningRegion2 = oldCacheSpan.touch();
            spansForKey.add(spanningRegion2);
            notifySpanTouched(oldCacheSpan, spanningRegion2);
            cacheSpan = spanningRegion2;
        } else if (!this.lockedSpans.containsKey(lookupSpan.key)) {
            this.lockedSpans.put(lookupSpan.key, spanningRegion);
            cacheSpan = spanningRegion;
        } else {
            cacheSpan = null;
        }
        return cacheSpan;
    }

    public synchronized File startFile(String key, long position, long length) {
        Assertions.checkState(this.lockedSpans.containsKey(key));
        if (!this.cacheDir.exists()) {
            removeStaleSpans();
            this.cacheDir.mkdirs();
        }
        this.evictor.onStartFile(this, key, position, length);
        return CacheSpan.getCacheFileName(this.cacheDir, key, position, System.currentTimeMillis());
    }

    public synchronized void commitFile(File file) {
        CacheSpan span = CacheSpan.createCacheEntry(file);
        Assertions.checkState(span != null);
        Assertions.checkState(this.lockedSpans.containsKey(span.key));
        if (file.exists()) {
            if (file.length() == 0) {
                file.delete();
            } else {
                addSpan(span);
                notifyAll();
            }
        }
    }

    public synchronized void releaseHoleSpan(CacheSpan holeSpan) {
        Assertions.checkState(holeSpan == this.lockedSpans.remove(holeSpan.key));
        notifyAll();
    }

    private CacheSpan getSpan(CacheSpan lookupSpan) {
        CacheSpan floorSpan;
        String key = lookupSpan.key;
        long offset = lookupSpan.position;
        TreeSet<CacheSpan> entries = this.cachedSpans.get(key);
        if (entries == null) {
            return CacheSpan.createOpenHole(key, lookupSpan.position);
        }
        CacheSpan floorSpan2 = entries.floor(lookupSpan);
        if (floorSpan2 == null || floorSpan2.position > offset || offset >= floorSpan2.position + floorSpan2.length) {
            CacheSpan ceilEntry = entries.ceiling(lookupSpan);
            if (ceilEntry == null) {
                floorSpan = CacheSpan.createOpenHole(key, lookupSpan.position);
            } else {
                floorSpan = CacheSpan.createClosedHole(key, lookupSpan.position, ceilEntry.position - lookupSpan.position);
            }
            return floorSpan;
        } else if (floorSpan2.file.exists()) {
            return floorSpan2;
        } else {
            removeStaleSpans();
            return getSpan(lookupSpan);
        }
    }

    /* access modifiers changed from: private */
    public void initialize() {
        if (!this.cacheDir.exists()) {
            this.cacheDir.mkdirs();
        }
        File[] files = this.cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.length() == 0) {
                    file.delete();
                } else {
                    File file2 = CacheSpan.upgradeIfNeeded(file);
                    CacheSpan span = CacheSpan.createCacheEntry(file2);
                    if (span == null) {
                        file2.delete();
                    } else {
                        addSpan(span);
                    }
                }
            }
            this.evictor.onCacheInitialized();
        }
    }

    private void addSpan(CacheSpan span) {
        TreeSet<CacheSpan> spansForKey = this.cachedSpans.get(span.key);
        if (spansForKey == null) {
            spansForKey = new TreeSet<>();
            this.cachedSpans.put(span.key, spansForKey);
        }
        spansForKey.add(span);
        this.totalSpace += span.length;
        notifySpanAdded(span);
    }

    public synchronized void removeSpan(CacheSpan span) {
        TreeSet<CacheSpan> spansForKey = this.cachedSpans.get(span.key);
        this.totalSpace -= span.length;
        Assertions.checkState(spansForKey.remove(span));
        span.file.delete();
        if (spansForKey.isEmpty()) {
            this.cachedSpans.remove(span.key);
        }
        notifySpanRemoved(span);
    }

    private void removeStaleSpans() {
        Iterator<Map.Entry<String, TreeSet<CacheSpan>>> iterator = this.cachedSpans.entrySet().iterator();
        while (iterator.hasNext()) {
            Iterator<CacheSpan> spanIterator = iterator.next().getValue().iterator();
            boolean isEmpty = true;
            while (spanIterator.hasNext()) {
                CacheSpan span = spanIterator.next();
                if (!span.file.exists()) {
                    spanIterator.remove();
                    if (span.isCached) {
                        this.totalSpace -= span.length;
                    }
                    notifySpanRemoved(span);
                } else {
                    isEmpty = false;
                }
            }
            if (isEmpty) {
                iterator.remove();
            }
        }
    }

    private void notifySpanRemoved(CacheSpan span) {
        ArrayList<Cache.Listener> keyListeners = this.listeners.get(span.key);
        if (keyListeners != null) {
            for (int i = keyListeners.size() - 1; i >= 0; i--) {
                keyListeners.get(i).onSpanRemoved(this, span);
            }
        }
        this.evictor.onSpanRemoved(this, span);
    }

    private void notifySpanAdded(CacheSpan span) {
        ArrayList<Cache.Listener> keyListeners = this.listeners.get(span.key);
        if (keyListeners != null) {
            for (int i = keyListeners.size() - 1; i >= 0; i--) {
                keyListeners.get(i).onSpanAdded(this, span);
            }
        }
        this.evictor.onSpanAdded(this, span);
    }

    private void notifySpanTouched(CacheSpan oldSpan, CacheSpan newSpan) {
        ArrayList<Cache.Listener> keyListeners = this.listeners.get(oldSpan.key);
        if (keyListeners != null) {
            for (int i = keyListeners.size() - 1; i >= 0; i--) {
                keyListeners.get(i).onSpanTouched(this, oldSpan, newSpan);
            }
        }
        this.evictor.onSpanTouched(this, oldSpan, newSpan);
    }

    public synchronized boolean isCached(String key, long position, long length) {
        boolean z;
        TreeSet<CacheSpan> entries = this.cachedSpans.get(key);
        if (entries != null) {
            CacheSpan floorSpan = entries.floor(CacheSpan.createLookup(key, position));
            if (floorSpan != null && floorSpan.position + floorSpan.length > position) {
                long queryEndPosition = position + length;
                long currentEndPosition = floorSpan.position + floorSpan.length;
                if (currentEndPosition < queryEndPosition) {
                    Iterator<CacheSpan> iterator = entries.tailSet(floorSpan, false).iterator();
                    while (true) {
                        if (!iterator.hasNext()) {
                            z = false;
                            break;
                        }
                        CacheSpan next = iterator.next();
                        if (next.position <= currentEndPosition) {
                            currentEndPosition = Math.max(currentEndPosition, next.position + next.length);
                            if (currentEndPosition >= queryEndPosition) {
                                z = true;
                                break;
                            }
                        } else {
                            z = false;
                            break;
                        }
                    }
                } else {
                    z = true;
                }
            } else {
                z = false;
            }
        } else {
            z = false;
        }
        return z;
    }
}
