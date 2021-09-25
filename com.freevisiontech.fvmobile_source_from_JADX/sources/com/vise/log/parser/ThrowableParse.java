package com.vise.log.parser;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableParse implements Parser<Throwable> {
    public Class<Throwable> parseClassType() {
        return Throwable.class;
    }

    public String parseString(Throwable throwable) {
        return getStackTraceString(throwable);
    }

    private String getStackTraceString(Throwable t) {
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
