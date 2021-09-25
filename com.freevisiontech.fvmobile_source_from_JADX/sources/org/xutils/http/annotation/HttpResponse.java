package org.xutils.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.xutils.http.app.ResponseParser;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpResponse {
    Class<? extends ResponseParser> parser();
}
