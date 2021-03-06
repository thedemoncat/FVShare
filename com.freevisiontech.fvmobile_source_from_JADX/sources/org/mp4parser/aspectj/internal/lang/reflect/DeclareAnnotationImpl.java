package org.mp4parser.aspectj.internal.lang.reflect;

import java.lang.annotation.Annotation;
import org.mp4parser.aspectj.lang.reflect.AjType;
import org.mp4parser.aspectj.lang.reflect.DeclareAnnotation;
import org.mp4parser.aspectj.lang.reflect.SignaturePattern;
import org.mp4parser.aspectj.lang.reflect.TypePattern;

public class DeclareAnnotationImpl implements DeclareAnnotation {
    private String annText;
    private AjType<?> declaringType;
    private DeclareAnnotation.Kind kind;
    private SignaturePattern signaturePattern;
    private Annotation theAnnotation;
    private TypePattern typePattern;

    public DeclareAnnotationImpl(AjType<?> declaring, String kindString, String pattern, Annotation ann, String annText2) {
        this.declaringType = declaring;
        if (kindString.equals("at_type")) {
            this.kind = DeclareAnnotation.Kind.Type;
        } else if (kindString.equals("at_field")) {
            this.kind = DeclareAnnotation.Kind.Field;
        } else if (kindString.equals("at_method")) {
            this.kind = DeclareAnnotation.Kind.Method;
        } else if (kindString.equals("at_constructor")) {
            this.kind = DeclareAnnotation.Kind.Constructor;
        } else {
            throw new IllegalStateException("Unknown declare annotation kind: " + kindString);
        }
        if (this.kind == DeclareAnnotation.Kind.Type) {
            this.typePattern = new TypePatternImpl(pattern);
        } else {
            this.signaturePattern = new SignaturePatternImpl(pattern);
        }
        this.theAnnotation = ann;
        this.annText = annText2;
    }

    public AjType<?> getDeclaringType() {
        return this.declaringType;
    }

    public DeclareAnnotation.Kind getKind() {
        return this.kind;
    }

    public SignaturePattern getSignaturePattern() {
        return this.signaturePattern;
    }

    public TypePattern getTypePattern() {
        return this.typePattern;
    }

    public Annotation getAnnotation() {
        return this.theAnnotation;
    }

    public String getAnnotationAsText() {
        return this.annText;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("declare @");
        switch (getKind()) {
            case Type:
                sb.append("type : ");
                sb.append(getTypePattern().asString());
                break;
            case Method:
                sb.append("method : ");
                sb.append(getSignaturePattern().asString());
                break;
            case Field:
                sb.append("field : ");
                sb.append(getSignaturePattern().asString());
                break;
            case Constructor:
                sb.append("constructor : ");
                sb.append(getSignaturePattern().asString());
                break;
        }
        sb.append(" : ");
        sb.append(getAnnotationAsText());
        return sb.toString();
    }
}
