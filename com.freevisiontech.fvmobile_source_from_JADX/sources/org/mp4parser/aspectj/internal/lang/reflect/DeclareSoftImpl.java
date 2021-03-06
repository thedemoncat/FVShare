package org.mp4parser.aspectj.internal.lang.reflect;

import org.mp4parser.aspectj.lang.reflect.AjType;
import org.mp4parser.aspectj.lang.reflect.AjTypeSystem;
import org.mp4parser.aspectj.lang.reflect.DeclareSoft;
import org.mp4parser.aspectj.lang.reflect.PointcutExpression;

public class DeclareSoftImpl implements DeclareSoft {
    private AjType<?> declaringType;
    private AjType<?> exceptionType;
    private String missingTypeName;
    private PointcutExpression pointcut;

    public DeclareSoftImpl(AjType<?> declaringType2, String pcut, String exceptionTypeName) {
        this.declaringType = declaringType2;
        this.pointcut = new PointcutExpressionImpl(pcut);
        try {
            this.exceptionType = AjTypeSystem.getAjType(Class.forName(exceptionTypeName, false, declaringType2.getJavaClass().getClassLoader()));
        } catch (ClassNotFoundException e) {
            this.missingTypeName = exceptionTypeName;
        }
    }

    public AjType getDeclaringType() {
        return this.declaringType;
    }

    public AjType getSoftenedExceptionType() throws ClassNotFoundException {
        if (this.missingTypeName == null) {
            return this.exceptionType;
        }
        throw new ClassNotFoundException(this.missingTypeName);
    }

    public PointcutExpression getPointcutExpression() {
        return this.pointcut;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("declare soft : ");
        if (this.missingTypeName != null) {
            sb.append(this.exceptionType.getName());
        } else {
            sb.append(this.missingTypeName);
        }
        sb.append(" : ");
        sb.append(getPointcutExpression().asString());
        return sb.toString();
    }
}
