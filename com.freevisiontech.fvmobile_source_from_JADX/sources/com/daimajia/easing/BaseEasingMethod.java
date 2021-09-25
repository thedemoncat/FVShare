package com.daimajia.easing;

import android.animation.TypeEvaluator;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class BaseEasingMethod implements TypeEvaluator<Number> {
    protected float mDuration;
    private ArrayList<EasingListener> mListeners = new ArrayList<>();

    public interface EasingListener {
        /* renamed from: on */
        void mo16250on(float f, float f2, float f3, float f4, float f5);
    }

    public abstract Float calculate(float f, float f2, float f3, float f4);

    public void addEasingListener(EasingListener l) {
        this.mListeners.add(l);
    }

    public void addEasingListeners(EasingListener... ls) {
        for (EasingListener l : ls) {
            this.mListeners.add(l);
        }
    }

    public void removeEasingListener(EasingListener l) {
        this.mListeners.remove(l);
    }

    public void clearEasingListeners() {
        this.mListeners.clear();
    }

    public BaseEasingMethod(float duration) {
        this.mDuration = duration;
    }

    public void setDuration(float duration) {
        this.mDuration = duration;
    }

    public final Float evaluate(float fraction, Number startValue, Number endValue) {
        float t = this.mDuration * fraction;
        float b = startValue.floatValue();
        float c = endValue.floatValue() - startValue.floatValue();
        float d = this.mDuration;
        float result = calculate(t, b, c, d).floatValue();
        Iterator<EasingListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().mo16250on(t, result, b, c, d);
        }
        return Float.valueOf(result);
    }
}
