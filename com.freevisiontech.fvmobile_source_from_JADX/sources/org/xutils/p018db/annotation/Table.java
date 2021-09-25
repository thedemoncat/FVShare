package org.xutils.p018db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* renamed from: org.xutils.db.annotation.Table */
public @interface Table {
    String name();

    String onCreated() default "";
}
