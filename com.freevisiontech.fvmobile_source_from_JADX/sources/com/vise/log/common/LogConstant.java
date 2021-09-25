package com.vise.log.common;

import com.vise.log.config.LogDefaultConfig;
import com.vise.log.parser.BundleParse;
import com.vise.log.parser.CollectionParse;
import com.vise.log.parser.IntentParse;
import com.vise.log.parser.MapParse;
import com.vise.log.parser.Parser;
import com.vise.log.parser.ReferenceParse;
import com.vise.log.parser.ThrowableParse;
import java.util.List;

public class LogConstant {

    /* renamed from: BR */
    public static final String f1064BR = System.getProperty("line.separator");
    public static final Class<? extends Parser>[] DEFAULT_PARSE_CLASS = {BundleParse.class, IntentParse.class, CollectionParse.class, MapParse.class, ThrowableParse.class, ReferenceParse.class};
    public static final int DIVIDER_BOTTOM = 2;
    public static final int DIVIDER_CENTER = 4;
    public static final int DIVIDER_NORMAL = 3;
    public static final int DIVIDER_TOP = 1;
    public static final int LINE_MAX = 3072;
    public static final int MAX_CHILD_LEVEL = 2;
    public static final int MIN_STACK_OFFSET = 5;
    public static final String STRING_OBJECT_NULL = "Object[object is null]";

    public static List<Parser> getParsers() {
        return LogDefaultConfig.getInstance().getParseList();
    }
}
