package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@TargetApi(19)
public class SizeConfigStrategy implements LruPoolStrategy {
    private static final Bitmap.Config[] ALPHA_8_IN_CONFIGS = {Bitmap.Config.ALPHA_8};
    private static final Bitmap.Config[] ARGB_4444_IN_CONFIGS = {Bitmap.Config.ARGB_4444};
    private static final Bitmap.Config[] ARGB_8888_IN_CONFIGS = {Bitmap.Config.ARGB_8888, null};
    private static final int MAX_SIZE_MULTIPLE = 8;
    private static final Bitmap.Config[] RGB_565_IN_CONFIGS = {Bitmap.Config.RGB_565};
    private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap<>();
    private final KeyPool keyPool = new KeyPool();
    private final Map<Bitmap.Config, NavigableMap<Integer, Integer>> sortedSizes = new HashMap();

    public void put(Bitmap bitmap) {
        Key key = this.keyPool.get(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
        this.groupedMap.put(key, bitmap);
        NavigableMap<Integer, Integer> sizes = getSizesForConfig(bitmap.getConfig());
        Integer current = (Integer) sizes.get(Integer.valueOf(key.size));
        sizes.put(Integer.valueOf(key.size), Integer.valueOf(current == null ? 1 : current.intValue() + 1));
    }

    public Bitmap get(int width, int height, Bitmap.Config config) {
        int size = Util.getBitmapByteSize(width, height, config);
        Bitmap result = this.groupedMap.get(findBestKey(this.keyPool.get(size, config), size, config));
        if (result != null) {
            decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(result)), result.getConfig());
            result.reconfigure(width, height, result.getConfig() != null ? result.getConfig() : Bitmap.Config.ARGB_8888);
        }
        return result;
    }

    private Key findBestKey(Key key, int size, Bitmap.Config config) {
        Key result = key;
        Bitmap.Config[] arr$ = getInConfigs(config);
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Bitmap.Config possibleConfig = arr$[i$];
            Integer possibleSize = getSizesForConfig(possibleConfig).ceilingKey(Integer.valueOf(size));
            if (possibleSize == null || possibleSize.intValue() > size * 8) {
                i$++;
            } else {
                if (possibleSize.intValue() == size) {
                    if (possibleConfig == null) {
                        if (config == null) {
                            return result;
                        }
                    } else if (possibleConfig.equals(config)) {
                        return result;
                    }
                }
                this.keyPool.offer(key);
                return this.keyPool.get(possibleSize.intValue(), possibleConfig);
            }
        }
        return result;
    }

    public Bitmap removeLast() {
        Bitmap removed = this.groupedMap.removeLast();
        if (removed != null) {
            decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(removed)), removed.getConfig());
        }
        return removed;
    }

    private void decrementBitmapOfSize(Integer size, Bitmap.Config config) {
        NavigableMap<Integer, Integer> sizes = getSizesForConfig(config);
        Integer current = (Integer) sizes.get(size);
        if (current.intValue() == 1) {
            sizes.remove(size);
        } else {
            sizes.put(size, Integer.valueOf(current.intValue() - 1));
        }
    }

    private NavigableMap<Integer, Integer> getSizesForConfig(Bitmap.Config config) {
        NavigableMap<Integer, Integer> sizes = this.sortedSizes.get(config);
        if (sizes != null) {
            return sizes;
        }
        NavigableMap<Integer, Integer> sizes2 = new TreeMap<>();
        this.sortedSizes.put(config, sizes2);
        return sizes2;
    }

    public String logBitmap(Bitmap bitmap) {
        return getBitmapString(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
    }

    public String logBitmap(int width, int height, Bitmap.Config config) {
        return getBitmapString(Util.getBitmapByteSize(width, height, config), config);
    }

    public int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append("SizeConfigStrategy{groupedMap=").append(this.groupedMap).append(", sortedSizes=(");
        for (Map.Entry<Bitmap.Config, NavigableMap<Integer, Integer>> entry : this.sortedSizes.entrySet()) {
            sb.append(entry.getKey()).append('[').append(entry.getValue()).append("], ");
        }
        if (!this.sortedSizes.isEmpty()) {
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        return sb.append(")}").toString();
    }

    static class KeyPool extends BaseKeyPool<Key> {
        KeyPool() {
        }

        public Key get(int size, Bitmap.Config config) {
            Key result = (Key) get();
            result.init(size, config);
            return result;
        }

        /* access modifiers changed from: protected */
        public Key create() {
            return new Key(this);
        }
    }

    static final class Key implements Poolable {
        private Bitmap.Config config;
        private final KeyPool pool;
        /* access modifiers changed from: private */
        public int size;

        public Key(KeyPool pool2) {
            this.pool = pool2;
        }

        Key(KeyPool pool2, int size2, Bitmap.Config config2) {
            this(pool2);
            init(size2, config2);
        }

        public void init(int size2, Bitmap.Config config2) {
            this.size = size2;
            this.config = config2;
        }

        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return SizeConfigStrategy.getBitmapString(this.size, this.config);
        }

        /* JADX WARNING: Removed duplicated region for block: B:8:0x0016 A[ORIG_RETURN, RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r5) {
            /*
                r4 = this;
                r1 = 0
                boolean r2 = r5 instanceof com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy.Key
                if (r2 == 0) goto L_0x0017
                r0 = r5
                com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy$Key r0 = (com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy.Key) r0
                int r2 = r4.size
                int r3 = r0.size
                if (r2 != r3) goto L_0x0017
                android.graphics.Bitmap$Config r2 = r4.config
                if (r2 != 0) goto L_0x0018
                android.graphics.Bitmap$Config r2 = r0.config
                if (r2 != 0) goto L_0x0017
            L_0x0016:
                r1 = 1
            L_0x0017:
                return r1
            L_0x0018:
                android.graphics.Bitmap$Config r2 = r4.config
                android.graphics.Bitmap$Config r3 = r0.config
                boolean r2 = r2.equals(r3)
                if (r2 == 0) goto L_0x0017
                goto L_0x0016
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy.Key.equals(java.lang.Object):boolean");
        }

        public int hashCode() {
            return (this.size * 31) + (this.config != null ? this.config.hashCode() : 0);
        }
    }

    /* access modifiers changed from: private */
    public static String getBitmapString(int size, Bitmap.Config config) {
        return "[" + size + "](" + config + ")";
    }

    /* renamed from: com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy$1 */
    static /* synthetic */ class C18841 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Bitmap.Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_8888.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.RGB_565.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ALPHA_8.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private static Bitmap.Config[] getInConfigs(Bitmap.Config requested) {
        switch (C18841.$SwitchMap$android$graphics$Bitmap$Config[requested.ordinal()]) {
            case 1:
                return ARGB_8888_IN_CONFIGS;
            case 2:
                return RGB_565_IN_CONFIGS;
            case 3:
                return ARGB_4444_IN_CONFIGS;
            case 4:
                return ALPHA_8_IN_CONFIGS;
            default:
                return new Bitmap.Config[]{requested};
        }
    }
}
