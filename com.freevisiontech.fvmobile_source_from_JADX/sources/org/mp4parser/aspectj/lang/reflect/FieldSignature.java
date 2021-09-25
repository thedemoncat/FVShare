package org.mp4parser.aspectj.lang.reflect;

import java.lang.reflect.Field;

public interface FieldSignature extends MemberSignature {
    Field getField();

    Class getFieldType();
}
