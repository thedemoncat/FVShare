package org.mp4parser.aspectj.internal.lang.reflect;

import org.mp4parser.aspectj.lang.reflect.AjType;
import org.mp4parser.aspectj.lang.reflect.DeclareErrorOrWarning;
import org.mp4parser.aspectj.lang.reflect.PointcutExpression;

public class DeclareErrorOrWarningImpl implements DeclareErrorOrWarning {
    private AjType declaringType;
    private boolean isError;
    private String msg;

    /* renamed from: pc */
    private PointcutExpression f1050pc;

    public DeclareErrorOrWarningImpl(String pointcut, String message, boolean isError2, AjType decType) {
        this.f1050pc = new PointcutExpressionImpl(pointcut);
        this.msg = message;
        this.isError = isError2;
        this.declaringType = decType;
    }

    public AjType getDeclaringType() {
        return this.declaringType;
    }

    public PointcutExpression getPointcutExpression() {
        return this.f1050pc;
    }

    public String getMessage() {
        return this.msg;
    }

    public boolean isError() {
        return this.isError;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("declare ");
        sb.append(isError() ? "error : " : "warning : ");
        sb.append(getPointcutExpression().asString());
        sb.append(" : ");
        sb.append("\"");
        sb.append(getMessage());
        sb.append("\"");
        return sb.toString();
    }
}
