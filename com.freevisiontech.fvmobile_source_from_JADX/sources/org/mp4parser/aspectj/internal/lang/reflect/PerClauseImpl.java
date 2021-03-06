package org.mp4parser.aspectj.internal.lang.reflect;

import org.mp4parser.aspectj.lang.reflect.PerClause;
import org.mp4parser.aspectj.lang.reflect.PerClauseKind;

public class PerClauseImpl implements PerClause {
    private final PerClauseKind kind;

    protected PerClauseImpl(PerClauseKind kind2) {
        this.kind = kind2;
    }

    public PerClauseKind getKind() {
        return this.kind;
    }

    public String toString() {
        return "issingleton()";
    }
}
