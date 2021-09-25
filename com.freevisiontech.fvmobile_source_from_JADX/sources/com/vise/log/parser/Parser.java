package com.vise.log.parser;

import com.vise.log.common.LogConstant;

public interface Parser<T> {
    public static final String LINE_SEPARATOR = LogConstant.f1064BR;

    Class<T> parseClassType();

    String parseString(T t);
}
