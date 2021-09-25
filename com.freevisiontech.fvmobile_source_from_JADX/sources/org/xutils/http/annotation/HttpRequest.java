package org.xutils.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.xutils.http.app.DefaultParamsBuilder;
import org.xutils.http.app.ParamsBuilder;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRequest {
    Class<? extends ParamsBuilder> builder() default DefaultParamsBuilder.class;

    String[] cacheKeys() default {""};

    String host() default "";

    String path();

    String[] signs() default {""};
}
