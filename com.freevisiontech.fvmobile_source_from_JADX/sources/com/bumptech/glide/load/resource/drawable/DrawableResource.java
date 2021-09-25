package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.Resource;

public abstract class DrawableResource<T extends Drawable> implements Resource<T> {
    protected final T drawable;

    public DrawableResource(T drawable2) {
        if (drawable2 == null) {
            throw new NullPointerException("Drawable must not be null!");
        }
        this.drawable = drawable2;
    }

    public final T get() {
        return this.drawable.getConstantState().newDrawable();
    }
}
