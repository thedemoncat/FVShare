package com.bumptech.glide.load.model;

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenericLoaderFactory {
    private static final ModelLoader NULL_MODEL_LOADER = new ModelLoader() {
        public DataFetcher getResourceFetcher(Object model, int width, int height) {
            throw new NoSuchMethodError("This should never be called!");
        }

        public String toString() {
            return "NULL_MODEL_LOADER";
        }
    };
    private final Map<Class, Map<Class, ModelLoader>> cachedModelLoaders = new HashMap();
    private final Context context;
    private final Map<Class, Map<Class, ModelLoaderFactory>> modelClassToResourceFactories = new HashMap();

    public GenericLoaderFactory(Context context2) {
        this.context = context2.getApplicationContext();
    }

    public synchronized <T, Y> ModelLoaderFactory<T, Y> unregister(Class<T> modelClass, Class<Y> resourceClass) {
        ModelLoaderFactory result;
        this.cachedModelLoaders.clear();
        result = null;
        Map<Class, ModelLoaderFactory> resourceToFactories = this.modelClassToResourceFactories.get(modelClass);
        if (resourceToFactories != null) {
            result = resourceToFactories.remove(resourceClass);
        }
        return result;
    }

    public synchronized <T, Y> ModelLoaderFactory<T, Y> register(Class<T> modelClass, Class<Y> resourceClass, ModelLoaderFactory<T, Y> factory) {
        ModelLoaderFactory previous;
        this.cachedModelLoaders.clear();
        Map<Class, ModelLoaderFactory> resourceToFactories = this.modelClassToResourceFactories.get(modelClass);
        if (resourceToFactories == null) {
            resourceToFactories = new HashMap<>();
            this.modelClassToResourceFactories.put(modelClass, resourceToFactories);
        }
        previous = resourceToFactories.put(resourceClass, factory);
        if (previous != null) {
            Iterator i$ = this.modelClassToResourceFactories.values().iterator();
            while (true) {
                if (i$.hasNext()) {
                    if (i$.next().containsValue(previous)) {
                        previous = null;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return previous;
    }

    @Deprecated
    public synchronized <T, Y> ModelLoader<T, Y> buildModelLoader(Class<T> modelClass, Class<Y> resourceClass, Context context2) {
        return buildModelLoader(modelClass, resourceClass);
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [java.lang.Class<Y>, java.lang.Class] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized <T, Y> com.bumptech.glide.load.model.ModelLoader<T, Y> buildModelLoader(java.lang.Class<T> r4, java.lang.Class<Y> r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            com.bumptech.glide.load.model.ModelLoader r1 = r3.getCachedLoader(r4, r5)     // Catch:{ all -> 0x0029 }
            if (r1 == 0) goto L_0x0014
            com.bumptech.glide.load.model.ModelLoader r2 = NULL_MODEL_LOADER     // Catch:{ all -> 0x0029 }
            boolean r2 = r2.equals(r1)     // Catch:{ all -> 0x0029 }
            if (r2 == 0) goto L_0x0012
            r2 = 0
        L_0x0010:
            monitor-exit(r3)
            return r2
        L_0x0012:
            r2 = r1
            goto L_0x0010
        L_0x0014:
            com.bumptech.glide.load.model.ModelLoaderFactory r0 = r3.getFactory(r4, r5)     // Catch:{ all -> 0x0029 }
            if (r0 == 0) goto L_0x0025
            android.content.Context r2 = r3.context     // Catch:{ all -> 0x0029 }
            com.bumptech.glide.load.model.ModelLoader r1 = r0.build(r2, r3)     // Catch:{ all -> 0x0029 }
            r3.cacheModelLoader(r4, r5, r1)     // Catch:{ all -> 0x0029 }
        L_0x0023:
            r2 = r1
            goto L_0x0010
        L_0x0025:
            r3.cacheNullLoader(r4, r5)     // Catch:{ all -> 0x0029 }
            goto L_0x0023
        L_0x0029:
            r2 = move-exception
            monitor-exit(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.model.GenericLoaderFactory.buildModelLoader(java.lang.Class, java.lang.Class):com.bumptech.glide.load.model.ModelLoader");
    }

    private <T, Y> void cacheNullLoader(Class<T> modelClass, Class<Y> resourceClass) {
        cacheModelLoader(modelClass, resourceClass, NULL_MODEL_LOADER);
    }

    private <T, Y> void cacheModelLoader(Class<T> modelClass, Class<Y> resourceClass, ModelLoader<T, Y> modelLoader) {
        Map<Class, ModelLoader> resourceToLoaders = this.cachedModelLoaders.get(modelClass);
        if (resourceToLoaders == null) {
            resourceToLoaders = new HashMap<>();
            this.cachedModelLoaders.put(modelClass, resourceToLoaders);
        }
        resourceToLoaders.put(resourceClass, modelLoader);
    }

    private <T, Y> ModelLoader<T, Y> getCachedLoader(Class<T> modelClass, Class<Y> resourceClass) {
        Map<Class, ModelLoader> resourceToLoaders = this.cachedModelLoaders.get(modelClass);
        if (resourceToLoaders != null) {
            return resourceToLoaders.get(resourceClass);
        }
        return null;
    }

    private <T, Y> ModelLoaderFactory<T, Y> getFactory(Class<T> modelClass, Class<Y> resourceClass) {
        Map<Class, ModelLoaderFactory> currentResourceToFactories;
        Map<Class, ModelLoaderFactory> resourceToFactories = this.modelClassToResourceFactories.get(modelClass);
        ModelLoaderFactory result = null;
        if (resourceToFactories != null) {
            result = resourceToFactories.get(resourceClass);
        }
        if (result == null) {
            for (Class<? super T> registeredModelClass : this.modelClassToResourceFactories.keySet()) {
                if (registeredModelClass.isAssignableFrom(modelClass) && (currentResourceToFactories = this.modelClassToResourceFactories.get(registeredModelClass)) != null && (result = currentResourceToFactories.get(resourceClass)) != null) {
                    break;
                }
            }
        }
        return result;
    }
}
