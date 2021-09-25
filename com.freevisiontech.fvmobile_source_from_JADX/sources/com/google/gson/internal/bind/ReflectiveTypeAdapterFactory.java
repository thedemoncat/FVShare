package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor2, FieldNamingStrategy fieldNamingPolicy2, Excluder excluder2, JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory2) {
        this.constructorConstructor = constructorConstructor2;
        this.fieldNamingPolicy = fieldNamingPolicy2;
        this.excluder = excluder2;
        this.jsonAdapterFactory = jsonAdapterFactory2;
    }

    public boolean excludeField(Field f, boolean serialize) {
        return excludeField(f, serialize, this.excluder);
    }

    static boolean excludeField(Field f, boolean serialize, Excluder excluder2) {
        return !excluder2.excludeClass(f.getType(), serialize) && !excluder2.excludeField(f, serialize);
    }

    private List<String> getFieldNames(Field f) {
        SerializedName annotation = (SerializedName) f.getAnnotation(SerializedName.class);
        if (annotation == null) {
            return Collections.singletonList(this.fieldNamingPolicy.translateName(f));
        }
        String serializedName = annotation.value();
        String[] alternates = annotation.alternate();
        if (alternates.length == 0) {
            return Collections.singletonList(serializedName);
        }
        List<String> fieldNames = new ArrayList<>(alternates.length + 1);
        fieldNames.add(serializedName);
        for (String alternate : alternates) {
            fieldNames.add(alternate);
        }
        return fieldNames;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }
        return new Adapter(this.constructorConstructor.get(type), getBoundFields(gson, type, raw));
    }

    private BoundField createBoundField(Gson context, Field field, String name, TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
        final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
        JsonAdapter annotation = (JsonAdapter) field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> mapped = null;
        if (annotation != null) {
            mapped = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, context, fieldType, annotation);
        }
        final boolean jsonAdapterPresent = mapped != null;
        if (mapped == null) {
            mapped = context.getAdapter(fieldType);
        }
        final TypeAdapter<?> typeAdapter = mapped;
        final Field field2 = field;
        final Gson gson = context;
        final TypeToken<?> typeToken = fieldType;
        return new BoundField(name, serialize, deserialize) {
            /* access modifiers changed from: package-private */
            public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                TypeAdapter t;
                Object fieldValue = field2.get(value);
                if (jsonAdapterPresent) {
                    t = typeAdapter;
                } else {
                    t = new TypeAdapterRuntimeTypeWrapper(gson, typeAdapter, typeToken.getType());
                }
                t.write(writer, fieldValue);
            }

            /* access modifiers changed from: package-private */
            public void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
                Object fieldValue = typeAdapter.read(reader);
                if (fieldValue != null || !isPrimitive) {
                    field2.set(value, fieldValue);
                }
            }

            public boolean writeField(Object value) throws IOException, IllegalAccessException {
                if (this.serialized && field2.get(value) != value) {
                    return true;
                }
                return false;
            }
        };
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.Class<java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v2, resolved type: com.google.gson.reflect.TypeToken<?>} */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00a5, code lost:
        r22 = com.google.gson.reflect.TypeToken.get(com.google.gson.internal.C$Gson$Types.resolve(r22.getType(), r23, r23.getGenericSuperclass()));
        r23 = r22.getRawType();
        r22 = r22;
     */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Class<?>, code=java.lang.Class, for r23v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Map<java.lang.String, com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField> getBoundFields(com.google.gson.Gson r21, com.google.gson.reflect.TypeToken<?> r22, java.lang.Class r23) {
        /*
            r20 = this;
            java.util.LinkedHashMap r17 = new java.util.LinkedHashMap
            r17.<init>()
            boolean r2 = r23.isInterface()
            if (r2 == 0) goto L_0x000c
        L_0x000b:
            return r17
        L_0x000c:
            java.lang.reflect.Type r10 = r22.getType()
        L_0x0010:
            java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
            r0 = r23
            if (r0 == r2) goto L_0x000b
            java.lang.reflect.Field[] r13 = r23.getDeclaredFields()
            int r0 = r13.length
            r19 = r0
            r2 = 0
            r18 = r2
        L_0x0020:
            r0 = r18
            r1 = r19
            if (r0 >= r1) goto L_0x00a5
            r4 = r13[r18]
            r2 = 1
            r0 = r20
            boolean r7 = r0.excludeField(r4, r2)
            r2 = 0
            r0 = r20
            boolean r8 = r0.excludeField(r4, r2)
            if (r7 != 0) goto L_0x003f
            if (r8 != 0) goto L_0x003f
        L_0x003a:
            int r2 = r18 + 1
            r18 = r2
            goto L_0x0020
        L_0x003f:
            r2 = 1
            r4.setAccessible(r2)
            java.lang.reflect.Type r2 = r22.getType()
            java.lang.reflect.Type r3 = r4.getGenericType()
            r0 = r23
            java.lang.reflect.Type r12 = com.google.gson.internal.C$Gson$Types.resolve(r2, r0, r3)
            r0 = r20
            java.util.List r11 = r0.getFieldNames(r4)
            r15 = 0
            r14 = 0
        L_0x0059:
            int r2 = r11.size()
            if (r14 >= r2) goto L_0x0083
            java.lang.Object r5 = r11.get(r14)
            java.lang.String r5 = (java.lang.String) r5
            if (r14 == 0) goto L_0x0068
            r7 = 0
        L_0x0068:
            com.google.gson.reflect.TypeToken r6 = com.google.gson.reflect.TypeToken.get((java.lang.reflect.Type) r12)
            r2 = r20
            r3 = r21
            com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$BoundField r9 = r2.createBoundField(r3, r4, r5, r6, r7, r8)
            r0 = r17
            java.lang.Object r16 = r0.put(r5, r9)
            com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$BoundField r16 = (com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField) r16
            if (r15 != 0) goto L_0x0080
            r15 = r16
        L_0x0080:
            int r14 = r14 + 1
            goto L_0x0059
        L_0x0083:
            if (r15 == 0) goto L_0x003a
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.StringBuilder r3 = r3.append(r10)
            java.lang.String r6 = " declares multiple JSON fields named "
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r6 = r15.name
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        L_0x00a5:
            java.lang.reflect.Type r2 = r22.getType()
            java.lang.reflect.Type r3 = r23.getGenericSuperclass()
            r0 = r23
            java.lang.reflect.Type r2 = com.google.gson.internal.C$Gson$Types.resolve(r2, r0, r3)
            com.google.gson.reflect.TypeToken r22 = com.google.gson.reflect.TypeToken.get((java.lang.reflect.Type) r2)
            java.lang.Class r23 = r22.getRawType()
            goto L_0x0010
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getBoundFields(com.google.gson.Gson, com.google.gson.reflect.TypeToken, java.lang.Class):java.util.Map");
    }

    static abstract class BoundField {
        final boolean deserialized;
        final String name;
        final boolean serialized;

        /* access modifiers changed from: package-private */
        public abstract void read(JsonReader jsonReader, Object obj) throws IOException, IllegalAccessException;

        /* access modifiers changed from: package-private */
        public abstract void write(JsonWriter jsonWriter, Object obj) throws IOException, IllegalAccessException;

        /* access modifiers changed from: package-private */
        public abstract boolean writeField(Object obj) throws IOException, IllegalAccessException;

        protected BoundField(String name2, boolean serialized2, boolean deserialized2) {
            this.name = name2;
            this.serialized = serialized2;
            this.deserialized = deserialized2;
        }
    }

    public static final class Adapter<T> extends TypeAdapter<T> {
        private final Map<String, BoundField> boundFields;
        private final ObjectConstructor<T> constructor;

        Adapter(ObjectConstructor<T> constructor2, Map<String, BoundField> boundFields2) {
            this.constructor = constructor2;
            this.boundFields = boundFields2;
        }

        public T read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            T instance = this.constructor.construct();
            try {
                in.beginObject();
                while (in.hasNext()) {
                    BoundField field = this.boundFields.get(in.nextName());
                    if (field == null || !field.deserialized) {
                        in.skipValue();
                    } else {
                        field.read(in, instance);
                    }
                }
                in.endObject();
                return instance;
            } catch (IllegalStateException e) {
                throw new JsonSyntaxException((Throwable) e);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            }
        }

        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    if (boundField.writeField(value)) {
                        out.name(boundField.name);
                        boundField.write(out, value);
                    }
                }
                out.endObject();
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }
}
