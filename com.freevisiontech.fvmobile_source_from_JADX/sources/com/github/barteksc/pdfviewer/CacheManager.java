package com.github.barteksc.pdfviewer;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import com.github.barteksc.pdfviewer.model.PagePart;
import com.github.barteksc.pdfviewer.util.Constants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

class CacheManager {
    private final PriorityQueue<PagePart> activeCache = new PriorityQueue<>(Constants.Cache.CACHE_SIZE, this.comparator);
    private final PagePartComparator comparator = new PagePartComparator();
    private final Object passiveActiveLock = new Object();
    private final PriorityQueue<PagePart> passiveCache = new PriorityQueue<>(Constants.Cache.CACHE_SIZE, this.comparator);
    private final List<PagePart> thumbnails = new ArrayList();

    public void cachePart(PagePart part) {
        synchronized (this.passiveActiveLock) {
            makeAFreeSpace();
            this.activeCache.offer(part);
        }
    }

    public void makeANewSet() {
        synchronized (this.passiveActiveLock) {
            this.passiveCache.addAll(this.activeCache);
            this.activeCache.clear();
        }
    }

    private void makeAFreeSpace() {
        synchronized (this.passiveActiveLock) {
            while (this.activeCache.size() + this.passiveCache.size() >= Constants.Cache.CACHE_SIZE && !this.passiveCache.isEmpty()) {
                this.passiveCache.poll().getRenderedBitmap().recycle();
            }
            while (this.activeCache.size() + this.passiveCache.size() >= Constants.Cache.CACHE_SIZE && !this.activeCache.isEmpty()) {
                this.activeCache.poll().getRenderedBitmap().recycle();
            }
        }
    }

    public void cacheThumbnail(PagePart part) {
        synchronized (this.thumbnails) {
            if (this.thumbnails.size() >= Constants.Cache.THUMBNAILS_CACHE_SIZE) {
                this.thumbnails.remove(0).getRenderedBitmap().recycle();
            }
            this.thumbnails.add(part);
        }
    }

    public boolean upPartIfContained(int userPage, int page, float width, float height, RectF pageRelativeBounds, int toOrder) {
        boolean z;
        PagePart fakePart = new PagePart(userPage, page, (Bitmap) null, width, height, pageRelativeBounds, false, 0);
        synchronized (this.passiveActiveLock) {
            PagePart found = find(this.passiveCache, fakePart);
            if (found != null) {
                this.passiveCache.remove(found);
                found.setCacheOrder(toOrder);
                this.activeCache.offer(found);
                z = true;
            } else {
                z = find(this.activeCache, fakePart) != null;
            }
        }
        return z;
    }

    public boolean containsThumbnail(int userPage, int page, float width, float height, RectF pageRelativeBounds) {
        PagePart fakePart = new PagePart(userPage, page, (Bitmap) null, width, height, pageRelativeBounds, true, 0);
        synchronized (this.thumbnails) {
            for (PagePart part : this.thumbnails) {
                if (part.equals(fakePart)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Nullable
    private static PagePart find(PriorityQueue<PagePart> vector, PagePart fakePart) {
        Iterator<PagePart> it = vector.iterator();
        while (it.hasNext()) {
            PagePart part = it.next();
            if (part.equals(fakePart)) {
                return part;
            }
        }
        return null;
    }

    public List<PagePart> getPageParts() {
        List<PagePart> parts;
        synchronized (this.passiveActiveLock) {
            parts = new ArrayList<>(this.passiveCache);
            parts.addAll(this.activeCache);
        }
        return parts;
    }

    public List<PagePart> getThumbnails() {
        List<PagePart> list;
        synchronized (this.thumbnails) {
            list = this.thumbnails;
        }
        return list;
    }

    public void recycle() {
        synchronized (this.passiveActiveLock) {
            Iterator<PagePart> it = this.passiveCache.iterator();
            while (it.hasNext()) {
                it.next().getRenderedBitmap().recycle();
            }
            this.passiveCache.clear();
            Iterator<PagePart> it2 = this.activeCache.iterator();
            while (it2.hasNext()) {
                it2.next().getRenderedBitmap().recycle();
            }
            this.activeCache.clear();
        }
        synchronized (this.thumbnails) {
            for (PagePart part : this.thumbnails) {
                part.getRenderedBitmap().recycle();
            }
            this.thumbnails.clear();
        }
    }

    class PagePartComparator implements Comparator<PagePart> {
        PagePartComparator() {
        }

        public int compare(PagePart part1, PagePart part2) {
            if (part1.getCacheOrder() == part2.getCacheOrder()) {
                return 0;
            }
            return part1.getCacheOrder() > part2.getCacheOrder() ? 1 : -1;
        }
    }
}
