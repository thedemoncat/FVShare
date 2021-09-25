package com.daimajia.easing;

import com.daimajia.easing.back.BackEaseIn;
import com.daimajia.easing.back.BackEaseInOut;
import com.daimajia.easing.back.BackEaseOut;
import com.daimajia.easing.bounce.BounceEaseIn;
import com.daimajia.easing.bounce.BounceEaseInOut;
import com.daimajia.easing.bounce.BounceEaseOut;
import com.daimajia.easing.circ.CircEaseIn;
import com.daimajia.easing.circ.CircEaseInOut;
import com.daimajia.easing.circ.CircEaseOut;
import com.daimajia.easing.cubic.CubicEaseIn;
import com.daimajia.easing.cubic.CubicEaseInOut;
import com.daimajia.easing.cubic.CubicEaseOut;
import com.daimajia.easing.elastic.ElasticEaseIn;
import com.daimajia.easing.elastic.ElasticEaseOut;
import com.daimajia.easing.expo.ExpoEaseIn;
import com.daimajia.easing.expo.ExpoEaseInOut;
import com.daimajia.easing.expo.ExpoEaseOut;
import com.daimajia.easing.linear.Linear;
import com.daimajia.easing.quad.QuadEaseIn;
import com.daimajia.easing.quad.QuadEaseInOut;
import com.daimajia.easing.quad.QuadEaseOut;
import com.daimajia.easing.quint.QuintEaseIn;
import com.daimajia.easing.quint.QuintEaseInOut;
import com.daimajia.easing.quint.QuintEaseOut;
import com.daimajia.easing.sine.SineEaseIn;
import com.daimajia.easing.sine.SineEaseInOut;
import com.daimajia.easing.sine.SineEaseOut;

public enum Skill {
    BackEaseIn(BackEaseIn.class),
    BackEaseOut(BackEaseOut.class),
    BackEaseInOut(BackEaseInOut.class),
    BounceEaseIn(BounceEaseIn.class),
    BounceEaseOut(BounceEaseOut.class),
    BounceEaseInOut(BounceEaseInOut.class),
    CircEaseIn(CircEaseIn.class),
    CircEaseOut(CircEaseOut.class),
    CircEaseInOut(CircEaseInOut.class),
    CubicEaseIn(CubicEaseIn.class),
    CubicEaseOut(CubicEaseOut.class),
    CubicEaseInOut(CubicEaseInOut.class),
    ElasticEaseIn(ElasticEaseIn.class),
    ElasticEaseOut(ElasticEaseOut.class),
    ExpoEaseIn(ExpoEaseIn.class),
    ExpoEaseOut(ExpoEaseOut.class),
    ExpoEaseInOut(ExpoEaseInOut.class),
    QuadEaseIn(QuadEaseIn.class),
    QuadEaseOut(QuadEaseOut.class),
    QuadEaseInOut(QuadEaseInOut.class),
    QuintEaseIn(QuintEaseIn.class),
    QuintEaseOut(QuintEaseOut.class),
    QuintEaseInOut(QuintEaseInOut.class),
    SineEaseIn(SineEaseIn.class),
    SineEaseOut(SineEaseOut.class),
    SineEaseInOut(SineEaseInOut.class),
    Linear(Linear.class);
    
    private Class easingMethod;

    private Skill(Class clazz) {
        this.easingMethod = clazz;
    }

    public BaseEasingMethod getMethod(float duration) {
        try {
            return (BaseEasingMethod) this.easingMethod.getConstructor(new Class[]{Float.TYPE}).newInstance(new Object[]{Float.valueOf(duration)});
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Can not init easingMethod instance");
        }
    }
}
