package com.vise.log.config;

import com.vise.log.parser.Parser;

public interface LogConfig {
    LogConfig addParserClass(Class<? extends Parser>... clsArr);

    LogConfig configAllowLog(boolean z);

    LogConfig configFormatTag(String str);

    LogConfig configLevel(int i);

    LogConfig configShowBorders(boolean z);

    LogConfig configTagPrefix(String str);
}
