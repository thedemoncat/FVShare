package com.vise.log.parser;

import com.vise.log.common.LogConvert;
import java.lang.ref.Reference;

public class ReferenceParse implements Parser<Reference> {
    public Class<Reference> parseClassType() {
        return Reference.class;
    }

    public String parseString(Reference reference) {
        Object actual = reference.get();
        StringBuilder builder = new StringBuilder(reference.getClass().getSimpleName() + "<" + actual.getClass().getSimpleName() + "> {");
        builder.append("→").append(LogConvert.objectToString(actual));
        return builder.toString() + "}";
    }
}
