package p012tv.danmaku.ijk.media.player.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
/* renamed from: tv.danmaku.ijk.media.player.annotations.CalledByNative */
public @interface CalledByNative {
    String value() default "";
}
