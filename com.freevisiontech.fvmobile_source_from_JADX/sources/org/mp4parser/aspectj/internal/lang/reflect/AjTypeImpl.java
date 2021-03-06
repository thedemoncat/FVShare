package org.mp4parser.aspectj.internal.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.mp4parser.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.mp4parser.aspectj.internal.lang.annotation.ajcDeclareEoW;
import org.mp4parser.aspectj.internal.lang.annotation.ajcDeclareParents;
import org.mp4parser.aspectj.internal.lang.annotation.ajcDeclarePrecedence;
import org.mp4parser.aspectj.internal.lang.annotation.ajcDeclareSoft;
import org.mp4parser.aspectj.internal.lang.annotation.ajcITD;
import org.mp4parser.aspectj.internal.lang.annotation.ajcPrivileged;
import org.mp4parser.aspectj.lang.annotation.After;
import org.mp4parser.aspectj.lang.annotation.AfterReturning;
import org.mp4parser.aspectj.lang.annotation.AfterThrowing;
import org.mp4parser.aspectj.lang.annotation.Around;
import org.mp4parser.aspectj.lang.annotation.Aspect;
import org.mp4parser.aspectj.lang.annotation.Before;
import org.mp4parser.aspectj.lang.annotation.DeclareError;
import org.mp4parser.aspectj.lang.annotation.DeclareParents;
import org.mp4parser.aspectj.lang.annotation.DeclareWarning;
import org.mp4parser.aspectj.lang.reflect.Advice;
import org.mp4parser.aspectj.lang.reflect.AdviceKind;
import org.mp4parser.aspectj.lang.reflect.AjType;
import org.mp4parser.aspectj.lang.reflect.AjTypeSystem;
import org.mp4parser.aspectj.lang.reflect.DeclareAnnotation;
import org.mp4parser.aspectj.lang.reflect.DeclareErrorOrWarning;
import org.mp4parser.aspectj.lang.reflect.DeclarePrecedence;
import org.mp4parser.aspectj.lang.reflect.DeclareSoft;
import org.mp4parser.aspectj.lang.reflect.InterTypeConstructorDeclaration;
import org.mp4parser.aspectj.lang.reflect.InterTypeFieldDeclaration;
import org.mp4parser.aspectj.lang.reflect.InterTypeMethodDeclaration;
import org.mp4parser.aspectj.lang.reflect.NoSuchAdviceException;
import org.mp4parser.aspectj.lang.reflect.NoSuchPointcutException;
import org.mp4parser.aspectj.lang.reflect.PerClause;
import org.mp4parser.aspectj.lang.reflect.PerClauseKind;
import org.mp4parser.aspectj.lang.reflect.Pointcut;

public class AjTypeImpl<T> implements AjType<T> {
    private static final String ajcMagic = "ajc$";
    private Advice[] advice = null;
    private Class<T> clazz;
    private Advice[] declaredAdvice = null;
    private InterTypeConstructorDeclaration[] declaredITDCons = null;
    private InterTypeFieldDeclaration[] declaredITDFields = null;
    private InterTypeMethodDeclaration[] declaredITDMethods = null;
    private Pointcut[] declaredPointcuts = null;
    private InterTypeConstructorDeclaration[] itdCons = null;
    private InterTypeFieldDeclaration[] itdFields = null;
    private InterTypeMethodDeclaration[] itdMethods = null;
    private Pointcut[] pointcuts = null;

    public AjTypeImpl(Class<T> fromClass) {
        this.clazz = fromClass;
    }

    public String getName() {
        return this.clazz.getName();
    }

    public Package getPackage() {
        return this.clazz.getPackage();
    }

    public AjType<?>[] getInterfaces() {
        return toAjTypeArray(this.clazz.getInterfaces());
    }

    public int getModifiers() {
        return this.clazz.getModifiers();
    }

    public Class<T> getJavaClass() {
        return this.clazz;
    }

    public AjType<? super T> getSupertype() {
        Class<? super T> superclass = this.clazz.getSuperclass();
        if (superclass == null) {
            return null;
        }
        return new AjTypeImpl(superclass);
    }

    public Type getGenericSupertype() {
        return this.clazz.getGenericSuperclass();
    }

    public Method getEnclosingMethod() {
        return this.clazz.getEnclosingMethod();
    }

    public Constructor getEnclosingConstructor() {
        return this.clazz.getEnclosingConstructor();
    }

    public AjType<?> getEnclosingType() {
        Class<?> enc = this.clazz.getEnclosingClass();
        if (enc != null) {
            return new AjTypeImpl(enc);
        }
        return null;
    }

    public AjType<?> getDeclaringType() {
        Class dec = this.clazz.getDeclaringClass();
        if (dec != null) {
            return new AjTypeImpl(dec);
        }
        return null;
    }

    public PerClause getPerClause() {
        if (!isAspect()) {
            return null;
        }
        String perClause = ((Aspect) this.clazz.getAnnotation(Aspect.class)).value();
        if (perClause.equals("")) {
            if (getSupertype().isAspect()) {
                return getSupertype().getPerClause();
            }
            return new PerClauseImpl(PerClauseKind.SINGLETON);
        } else if (perClause.startsWith("perthis(")) {
            return new PointcutBasedPerClauseImpl(PerClauseKind.PERTHIS, perClause.substring("perthis(".length(), perClause.length() - 1));
        } else {
            if (perClause.startsWith("pertarget(")) {
                return new PointcutBasedPerClauseImpl(PerClauseKind.PERTARGET, perClause.substring("pertarget(".length(), perClause.length() - 1));
            }
            if (perClause.startsWith("percflow(")) {
                return new PointcutBasedPerClauseImpl(PerClauseKind.PERCFLOW, perClause.substring("percflow(".length(), perClause.length() - 1));
            }
            if (perClause.startsWith("percflowbelow(")) {
                return new PointcutBasedPerClauseImpl(PerClauseKind.PERCFLOWBELOW, perClause.substring("percflowbelow(".length(), perClause.length() - 1));
            }
            if (perClause.startsWith("pertypewithin")) {
                return new TypePatternBasedPerClauseImpl(PerClauseKind.PERTYPEWITHIN, perClause.substring("pertypewithin(".length(), perClause.length() - 1));
            }
            throw new IllegalStateException("Per-clause not recognized: " + perClause);
        }
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return this.clazz.isAnnotationPresent(annotationType);
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return this.clazz.getAnnotation(annotationType);
    }

    public Annotation[] getAnnotations() {
        return this.clazz.getAnnotations();
    }

    public Annotation[] getDeclaredAnnotations() {
        return this.clazz.getDeclaredAnnotations();
    }

    public AjType<?>[] getAjTypes() {
        return toAjTypeArray(this.clazz.getClasses());
    }

    public AjType<?>[] getDeclaredAjTypes() {
        return toAjTypeArray(this.clazz.getDeclaredClasses());
    }

    public Constructor getConstructor(AjType<?>... parameterTypes) throws NoSuchMethodException {
        return this.clazz.getConstructor(toClassArray(parameterTypes));
    }

    public Constructor[] getConstructors() {
        return this.clazz.getConstructors();
    }

    public Constructor getDeclaredConstructor(AjType<?>... parameterTypes) throws NoSuchMethodException {
        return this.clazz.getDeclaredConstructor(toClassArray(parameterTypes));
    }

    public Constructor[] getDeclaredConstructors() {
        return this.clazz.getDeclaredConstructors();
    }

    public Field getDeclaredField(String name) throws NoSuchFieldException {
        Field f = this.clazz.getDeclaredField(name);
        if (!f.getName().startsWith(ajcMagic)) {
            return f;
        }
        throw new NoSuchFieldException(name);
    }

    public Field[] getDeclaredFields() {
        Field[] fields = this.clazz.getDeclaredFields();
        List<Field> filteredFields = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getName().startsWith(ajcMagic) && !field.isAnnotationPresent(DeclareWarning.class) && !field.isAnnotationPresent(DeclareError.class)) {
                filteredFields.add(field);
            }
        }
        Field[] ret = new Field[filteredFields.size()];
        filteredFields.toArray(ret);
        return ret;
    }

    public Field getField(String name) throws NoSuchFieldException {
        Field f = this.clazz.getField(name);
        if (!f.getName().startsWith(ajcMagic)) {
            return f;
        }
        throw new NoSuchFieldException(name);
    }

    public Field[] getFields() {
        Field[] fields = this.clazz.getFields();
        List<Field> filteredFields = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getName().startsWith(ajcMagic) && !field.isAnnotationPresent(DeclareWarning.class) && !field.isAnnotationPresent(DeclareError.class)) {
                filteredFields.add(field);
            }
        }
        Field[] ret = new Field[filteredFields.size()];
        filteredFields.toArray(ret);
        return ret;
    }

    public Method getDeclaredMethod(String name, AjType<?>... parameterTypes) throws NoSuchMethodException {
        Method m = this.clazz.getDeclaredMethod(name, toClassArray(parameterTypes));
        if (isReallyAMethod(m)) {
            return m;
        }
        throw new NoSuchMethodException(name);
    }

    public Method getMethod(String name, AjType<?>... parameterTypes) throws NoSuchMethodException {
        Method m = this.clazz.getMethod(name, toClassArray(parameterTypes));
        if (isReallyAMethod(m)) {
            return m;
        }
        throw new NoSuchMethodException(name);
    }

    public Method[] getDeclaredMethods() {
        Method[] methods = this.clazz.getDeclaredMethods();
        List<Method> filteredMethods = new ArrayList<>();
        for (Method method : methods) {
            if (isReallyAMethod(method)) {
                filteredMethods.add(method);
            }
        }
        Method[] ret = new Method[filteredMethods.size()];
        filteredMethods.toArray(ret);
        return ret;
    }

    public Method[] getMethods() {
        Method[] methods = this.clazz.getMethods();
        List<Method> filteredMethods = new ArrayList<>();
        for (Method method : methods) {
            if (isReallyAMethod(method)) {
                filteredMethods.add(method);
            }
        }
        Method[] ret = new Method[filteredMethods.size()];
        filteredMethods.toArray(ret);
        return ret;
    }

    private boolean isReallyAMethod(Method method) {
        if (method.getName().startsWith(ajcMagic)) {
            return false;
        }
        if (method.getAnnotations().length == 0) {
            return true;
        }
        if (method.isAnnotationPresent(org.mp4parser.aspectj.lang.annotation.Pointcut.class) || method.isAnnotationPresent(Before.class) || method.isAnnotationPresent(After.class) || method.isAnnotationPresent(AfterReturning.class) || method.isAnnotationPresent(AfterThrowing.class) || method.isAnnotationPresent(Around.class)) {
            return false;
        }
        return true;
    }

    public Pointcut getDeclaredPointcut(String name) throws NoSuchPointcutException {
        for (Pointcut pc : getDeclaredPointcuts()) {
            if (pc.getName().equals(name)) {
                return pc;
            }
        }
        throw new NoSuchPointcutException(name);
    }

    public Pointcut getPointcut(String name) throws NoSuchPointcutException {
        for (Pointcut pc : getPointcuts()) {
            if (pc.getName().equals(name)) {
                return pc;
            }
        }
        throw new NoSuchPointcutException(name);
    }

    public Pointcut[] getDeclaredPointcuts() {
        if (this.declaredPointcuts != null) {
            return this.declaredPointcuts;
        }
        List<Pointcut> pointcuts2 = new ArrayList<>();
        for (Method method : this.clazz.getDeclaredMethods()) {
            Pointcut pc = asPointcut(method);
            if (pc != null) {
                pointcuts2.add(pc);
            }
        }
        Pointcut[] ret = new Pointcut[pointcuts2.size()];
        pointcuts2.toArray(ret);
        this.declaredPointcuts = ret;
        return ret;
    }

    public Pointcut[] getPointcuts() {
        if (this.pointcuts != null) {
            return this.pointcuts;
        }
        List<Pointcut> pcuts = new ArrayList<>();
        for (Method method : this.clazz.getMethods()) {
            Pointcut pc = asPointcut(method);
            if (pc != null) {
                pcuts.add(pc);
            }
        }
        Pointcut[] ret = new Pointcut[pcuts.size()];
        pcuts.toArray(ret);
        this.pointcuts = ret;
        return ret;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0017, code lost:
        r1 = r1.substring(r1.indexOf("$$") + 2, r1.length());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.mp4parser.aspectj.lang.reflect.Pointcut asPointcut(java.lang.reflect.Method r10) {
        /*
            r9 = this;
            java.lang.Class<org.mp4parser.aspectj.lang.annotation.Pointcut> r0 = org.mp4parser.aspectj.lang.annotation.Pointcut.class
            java.lang.annotation.Annotation r8 = r10.getAnnotation(r0)
            org.mp4parser.aspectj.lang.annotation.Pointcut r8 = (org.mp4parser.aspectj.lang.annotation.Pointcut) r8
            if (r8 == 0) goto L_0x004e
            java.lang.String r1 = r10.getName()
            java.lang.String r0 = "ajc$"
            boolean r0 = r1.startsWith(r0)
            if (r0 == 0) goto L_0x0037
            java.lang.String r0 = "$$"
            int r6 = r1.indexOf(r0)
            int r0 = r6 + 2
            int r2 = r1.length()
            java.lang.String r1 = r1.substring(r0, r2)
            java.lang.String r0 = "$"
            int r7 = r1.indexOf(r0)
            r0 = -1
            if (r7 == r0) goto L_0x0037
            r0 = 0
            java.lang.String r1 = r1.substring(r0, r7)
        L_0x0037:
            org.mp4parser.aspectj.internal.lang.reflect.PointcutImpl r0 = new org.mp4parser.aspectj.internal.lang.reflect.PointcutImpl
            java.lang.String r2 = r8.value()
            java.lang.Class r3 = r10.getDeclaringClass()
            org.mp4parser.aspectj.lang.reflect.AjType r4 = org.mp4parser.aspectj.lang.reflect.AjTypeSystem.getAjType(r3)
            java.lang.String r5 = r8.argNames()
            r3 = r10
            r0.<init>(r1, r2, r3, r4, r5)
        L_0x004d:
            return r0
        L_0x004e:
            r0 = 0
            goto L_0x004d
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mp4parser.aspectj.internal.lang.reflect.AjTypeImpl.asPointcut(java.lang.reflect.Method):org.mp4parser.aspectj.lang.reflect.Pointcut");
    }

    public Advice[] getDeclaredAdvice(AdviceKind... ofType) {
        Set<AdviceKind> types;
        if (ofType.length == 0) {
            types = EnumSet.allOf(AdviceKind.class);
        } else {
            types = EnumSet.noneOf(AdviceKind.class);
            types.addAll(Arrays.asList(ofType));
        }
        return getDeclaredAdvice((Set) types);
    }

    public Advice[] getAdvice(AdviceKind... ofType) {
        Set<AdviceKind> types;
        if (ofType.length == 0) {
            types = EnumSet.allOf(AdviceKind.class);
        } else {
            types = EnumSet.noneOf(AdviceKind.class);
            types.addAll(Arrays.asList(ofType));
        }
        return getAdvice((Set) types);
    }

    private Advice[] getDeclaredAdvice(Set ofAdviceTypes) {
        if (this.declaredAdvice == null) {
            initDeclaredAdvice();
        }
        List<Advice> adviceList = new ArrayList<>();
        for (Advice a : this.declaredAdvice) {
            if (ofAdviceTypes.contains(a.getKind())) {
                adviceList.add(a);
            }
        }
        Advice[] ret = new Advice[adviceList.size()];
        adviceList.toArray(ret);
        return ret;
    }

    private void initDeclaredAdvice() {
        Method[] methods = this.clazz.getDeclaredMethods();
        List<Advice> adviceList = new ArrayList<>();
        for (Method method : methods) {
            Advice advice2 = asAdvice(method);
            if (advice2 != null) {
                adviceList.add(advice2);
            }
        }
        this.declaredAdvice = new Advice[adviceList.size()];
        adviceList.toArray(this.declaredAdvice);
    }

    private Advice[] getAdvice(Set ofAdviceTypes) {
        if (this.advice == null) {
            initAdvice();
        }
        List<Advice> adviceList = new ArrayList<>();
        for (Advice a : this.advice) {
            if (ofAdviceTypes.contains(a.getKind())) {
                adviceList.add(a);
            }
        }
        Advice[] ret = new Advice[adviceList.size()];
        adviceList.toArray(ret);
        return ret;
    }

    private void initAdvice() {
        Method[] methods = this.clazz.getMethods();
        List<Advice> adviceList = new ArrayList<>();
        for (Method method : methods) {
            Advice advice2 = asAdvice(method);
            if (advice2 != null) {
                adviceList.add(advice2);
            }
        }
        this.advice = new Advice[adviceList.size()];
        adviceList.toArray(this.advice);
    }

    public Advice getAdvice(String name) throws NoSuchAdviceException {
        if (name.equals("")) {
            throw new IllegalArgumentException("use getAdvice(AdviceType...) instead for un-named advice");
        }
        if (this.advice == null) {
            initAdvice();
        }
        for (Advice a : this.advice) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        throw new NoSuchAdviceException(name);
    }

    public Advice getDeclaredAdvice(String name) throws NoSuchAdviceException {
        if (name.equals("")) {
            throw new IllegalArgumentException("use getAdvice(AdviceType...) instead for un-named advice");
        }
        if (this.declaredAdvice == null) {
            initDeclaredAdvice();
        }
        for (Advice a : this.declaredAdvice) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        throw new NoSuchAdviceException(name);
    }

    private Advice asAdvice(Method method) {
        if (method.getAnnotations().length == 0) {
            return null;
        }
        Before beforeAnn = (Before) method.getAnnotation(Before.class);
        if (beforeAnn != null) {
            return new AdviceImpl(method, beforeAnn.value(), AdviceKind.BEFORE);
        }
        After afterAnn = (After) method.getAnnotation(After.class);
        if (afterAnn != null) {
            return new AdviceImpl(method, afterAnn.value(), AdviceKind.AFTER);
        }
        AfterReturning afterReturningAnn = (AfterReturning) method.getAnnotation(AfterReturning.class);
        if (afterReturningAnn != null) {
            String pcExpr = afterReturningAnn.pointcut();
            if (pcExpr.equals("")) {
                pcExpr = afterReturningAnn.value();
            }
            return new AdviceImpl(method, pcExpr, AdviceKind.AFTER_RETURNING, afterReturningAnn.returning());
        }
        AfterThrowing afterThrowingAnn = (AfterThrowing) method.getAnnotation(AfterThrowing.class);
        if (afterThrowingAnn != null) {
            String pcExpr2 = afterThrowingAnn.pointcut();
            if (pcExpr2 == null) {
                pcExpr2 = afterThrowingAnn.value();
            }
            return new AdviceImpl(method, pcExpr2, AdviceKind.AFTER_THROWING, afterThrowingAnn.throwing());
        }
        Around aroundAnn = (Around) method.getAnnotation(Around.class);
        if (aroundAnn != null) {
            return new AdviceImpl(method, aroundAnn.value(), AdviceKind.AROUND);
        }
        return null;
    }

    public InterTypeMethodDeclaration getDeclaredITDMethod(String name, AjType<?> target, AjType<?>... parameterTypes) throws NoSuchMethodException {
        for (InterTypeMethodDeclaration itdm : getDeclaredITDMethods()) {
            try {
                if (itdm.getName().equals(name) && itdm.getTargetType().equals(target)) {
                    AjType<?>[] ptypes = itdm.getParameterTypes();
                    if (ptypes.length == parameterTypes.length) {
                        int i = 0;
                        while (i < ptypes.length) {
                            if (ptypes[i].equals(parameterTypes[i])) {
                                i++;
                            }
                        }
                        return itdm;
                    }
                    continue;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        throw new NoSuchMethodException(name);
    }

    public InterTypeMethodDeclaration[] getDeclaredITDMethods() {
        if (this.declaredITDMethods == null) {
            List<InterTypeMethodDeclaration> itdms = new ArrayList<>();
            for (Method m : this.clazz.getDeclaredMethods()) {
                if (m.getName().contains("ajc$interMethodDispatch1$") && m.isAnnotationPresent(ajcITD.class)) {
                    ajcITD ann = (ajcITD) m.getAnnotation(ajcITD.class);
                    itdms.add(new InterTypeMethodDeclarationImpl(this, ann.targetType(), ann.modifiers(), ann.name(), m));
                }
            }
            addAnnotationStyleITDMethods(itdms, false);
            this.declaredITDMethods = new InterTypeMethodDeclaration[itdms.size()];
            itdms.toArray(this.declaredITDMethods);
        }
        return this.declaredITDMethods;
    }

    public InterTypeMethodDeclaration getITDMethod(String name, AjType<?> target, AjType<?>... parameterTypes) throws NoSuchMethodException {
        for (InterTypeMethodDeclaration itdm : getITDMethods()) {
            try {
                if (itdm.getName().equals(name) && itdm.getTargetType().equals(target)) {
                    AjType<?>[] ptypes = itdm.getParameterTypes();
                    if (ptypes.length == parameterTypes.length) {
                        int i = 0;
                        while (i < ptypes.length) {
                            if (ptypes[i].equals(parameterTypes[i])) {
                                i++;
                            }
                        }
                        return itdm;
                    }
                    continue;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        throw new NoSuchMethodException(name);
    }

    public InterTypeMethodDeclaration[] getITDMethods() {
        if (this.itdMethods == null) {
            List<InterTypeMethodDeclaration> itdms = new ArrayList<>();
            for (Method m : this.clazz.getDeclaredMethods()) {
                if (m.getName().contains("ajc$interMethod$") && m.isAnnotationPresent(ajcITD.class)) {
                    ajcITD ann = (ajcITD) m.getAnnotation(ajcITD.class);
                    if (Modifier.isPublic(ann.modifiers())) {
                        itdms.add(new InterTypeMethodDeclarationImpl(this, ann.targetType(), ann.modifiers(), ann.name(), m));
                    }
                }
            }
            addAnnotationStyleITDMethods(itdms, true);
            this.itdMethods = new InterTypeMethodDeclaration[itdms.size()];
            itdms.toArray(this.itdMethods);
        }
        return this.itdMethods;
    }

    private void addAnnotationStyleITDMethods(List<InterTypeMethodDeclaration> toList, boolean publicOnly) {
        if (isAspect()) {
            for (Field f : this.clazz.getDeclaredFields()) {
                if (f.getType().isInterface() && f.isAnnotationPresent(DeclareParents.class)) {
                    Class cls = DeclareParents.class;
                    if (((DeclareParents) f.getAnnotation(cls)).defaultImpl() != cls) {
                        for (Method itdM : f.getType().getDeclaredMethods()) {
                            if (Modifier.isPublic(itdM.getModifiers()) || !publicOnly) {
                                toList.add(new InterTypeMethodDeclarationImpl(this, AjTypeSystem.getAjType(f.getType()), itdM, 1));
                            }
                        }
                    }
                }
            }
        }
    }

    private void addAnnotationStyleITDFields(List<InterTypeFieldDeclaration> list, boolean publicOnly) {
    }

    public InterTypeConstructorDeclaration getDeclaredITDConstructor(AjType<?> target, AjType<?>... parameterTypes) throws NoSuchMethodException {
        for (InterTypeConstructorDeclaration itdc : getDeclaredITDConstructors()) {
            try {
                if (itdc.getTargetType().equals(target)) {
                    AjType<?>[] ptypes = itdc.getParameterTypes();
                    if (ptypes.length == parameterTypes.length) {
                        int i = 0;
                        while (i < ptypes.length) {
                            if (ptypes[i].equals(parameterTypes[i])) {
                                i++;
                            }
                        }
                        return itdc;
                    }
                    continue;
                } else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        throw new NoSuchMethodException();
    }

    public InterTypeConstructorDeclaration[] getDeclaredITDConstructors() {
        if (this.declaredITDCons == null) {
            List<InterTypeConstructorDeclaration> itdcs = new ArrayList<>();
            for (Method m : this.clazz.getDeclaredMethods()) {
                if (m.getName().contains("ajc$postInterConstructor") && m.isAnnotationPresent(ajcITD.class)) {
                    ajcITD ann = (ajcITD) m.getAnnotation(ajcITD.class);
                    itdcs.add(new InterTypeConstructorDeclarationImpl(this, ann.targetType(), ann.modifiers(), m));
                }
            }
            this.declaredITDCons = new InterTypeConstructorDeclaration[itdcs.size()];
            itdcs.toArray(this.declaredITDCons);
        }
        return this.declaredITDCons;
    }

    public InterTypeConstructorDeclaration getITDConstructor(AjType<?> target, AjType<?>... parameterTypes) throws NoSuchMethodException {
        for (InterTypeConstructorDeclaration itdc : getITDConstructors()) {
            try {
                if (itdc.getTargetType().equals(target)) {
                    AjType<?>[] ptypes = itdc.getParameterTypes();
                    if (ptypes.length == parameterTypes.length) {
                        int i = 0;
                        while (i < ptypes.length) {
                            if (ptypes[i].equals(parameterTypes[i])) {
                                i++;
                            }
                        }
                        return itdc;
                    }
                    continue;
                } else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        throw new NoSuchMethodException();
    }

    public InterTypeConstructorDeclaration[] getITDConstructors() {
        if (this.itdCons == null) {
            List<InterTypeConstructorDeclaration> itdcs = new ArrayList<>();
            for (Method m : this.clazz.getMethods()) {
                if (m.getName().contains("ajc$postInterConstructor") && m.isAnnotationPresent(ajcITD.class)) {
                    ajcITD ann = (ajcITD) m.getAnnotation(ajcITD.class);
                    if (Modifier.isPublic(ann.modifiers())) {
                        itdcs.add(new InterTypeConstructorDeclarationImpl(this, ann.targetType(), ann.modifiers(), m));
                    }
                }
            }
            this.itdCons = new InterTypeConstructorDeclaration[itdcs.size()];
            itdcs.toArray(this.itdCons);
        }
        return this.itdCons;
    }

    public InterTypeFieldDeclaration getDeclaredITDField(String name, AjType<?> target) throws NoSuchFieldException {
        for (InterTypeFieldDeclaration itdf : getDeclaredITDFields()) {
            if (itdf.getName().equals(name)) {
                try {
                    if (itdf.getTargetType().equals(target)) {
                        return itdf;
                    }
                } catch (ClassNotFoundException e) {
                }
            }
        }
        throw new NoSuchFieldException(name);
    }

    public InterTypeFieldDeclaration[] getDeclaredITDFields() {
        List<InterTypeFieldDeclaration> itdfs = new ArrayList<>();
        if (this.declaredITDFields == null) {
            for (Method m : this.clazz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(ajcITD.class) && m.getName().contains("ajc$interFieldInit")) {
                    ajcITD ann = (ajcITD) m.getAnnotation(ajcITD.class);
                    try {
                        Method dispatch = this.clazz.getDeclaredMethod(m.getName().replace("FieldInit", "FieldGetDispatch"), m.getParameterTypes());
                        itdfs.add(new InterTypeFieldDeclarationImpl(this, ann.targetType(), ann.modifiers(), ann.name(), AjTypeSystem.getAjType(dispatch.getReturnType()), dispatch.getGenericReturnType()));
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException("Can't find field get dispatch method for " + m.getName());
                    }
                }
            }
            addAnnotationStyleITDFields(itdfs, false);
            this.declaredITDFields = new InterTypeFieldDeclaration[itdfs.size()];
            itdfs.toArray(this.declaredITDFields);
        }
        return this.declaredITDFields;
    }

    public InterTypeFieldDeclaration getITDField(String name, AjType<?> target) throws NoSuchFieldException {
        for (InterTypeFieldDeclaration itdf : getITDFields()) {
            if (itdf.getName().equals(name)) {
                try {
                    if (itdf.getTargetType().equals(target)) {
                        return itdf;
                    }
                } catch (ClassNotFoundException e) {
                }
            }
        }
        throw new NoSuchFieldException(name);
    }

    public InterTypeFieldDeclaration[] getITDFields() {
        List<InterTypeFieldDeclaration> itdfs = new ArrayList<>();
        if (this.itdFields == null) {
            for (Method m : this.clazz.getMethods()) {
                if (m.isAnnotationPresent(ajcITD.class)) {
                    ajcITD ann = (ajcITD) m.getAnnotation(ajcITD.class);
                    if (m.getName().contains("ajc$interFieldInit") && Modifier.isPublic(ann.modifiers())) {
                        try {
                            Method dispatch = m.getDeclaringClass().getDeclaredMethod(m.getName().replace("FieldInit", "FieldGetDispatch"), m.getParameterTypes());
                            itdfs.add(new InterTypeFieldDeclarationImpl(this, ann.targetType(), ann.modifiers(), ann.name(), AjTypeSystem.getAjType(dispatch.getReturnType()), dispatch.getGenericReturnType()));
                        } catch (NoSuchMethodException e) {
                            throw new IllegalStateException("Can't find field get dispatch method for " + m.getName());
                        }
                    }
                }
            }
            addAnnotationStyleITDFields(itdfs, true);
            this.itdFields = new InterTypeFieldDeclaration[itdfs.size()];
            itdfs.toArray(this.itdFields);
        }
        return this.itdFields;
    }

    public DeclareErrorOrWarning[] getDeclareErrorOrWarnings() {
        List<DeclareErrorOrWarning> deows = new ArrayList<>();
        for (Field field : this.clazz.getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(DeclareWarning.class)) {
                    DeclareWarning dw = (DeclareWarning) field.getAnnotation(DeclareWarning.class);
                    if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                        deows.add(new DeclareErrorOrWarningImpl(dw.value(), (String) field.get((Object) null), false, this));
                    }
                } else if (field.isAnnotationPresent(DeclareError.class)) {
                    DeclareError de = (DeclareError) field.getAnnotation(DeclareError.class);
                    if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                        deows.add(new DeclareErrorOrWarningImpl(de.value(), (String) field.get((Object) null), true, this));
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
            }
        }
        for (Method method : this.clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ajcDeclareEoW.class)) {
                ajcDeclareEoW deowAnn = (ajcDeclareEoW) method.getAnnotation(ajcDeclareEoW.class);
                deows.add(new DeclareErrorOrWarningImpl(deowAnn.pointcut(), deowAnn.message(), deowAnn.isError(), this));
            }
        }
        DeclareErrorOrWarning[] ret = new DeclareErrorOrWarning[deows.size()];
        deows.toArray(ret);
        return ret;
    }

    public org.mp4parser.aspectj.lang.reflect.DeclareParents[] getDeclareParents() {
        List<org.mp4parser.aspectj.lang.reflect.DeclareParents> decps = new ArrayList<>();
        for (Method method : this.clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ajcDeclareParents.class)) {
                ajcDeclareParents decPAnn = (ajcDeclareParents) method.getAnnotation(ajcDeclareParents.class);
                decps.add(new DeclareParentsImpl(decPAnn.targetTypePattern(), decPAnn.parentTypes(), decPAnn.isExtends(), this));
            }
        }
        addAnnotationStyleDeclareParents(decps);
        if (getSupertype().isAspect()) {
            decps.addAll(Arrays.asList(getSupertype().getDeclareParents()));
        }
        org.mp4parser.aspectj.lang.reflect.DeclareParents[] ret = new org.mp4parser.aspectj.lang.reflect.DeclareParents[decps.size()];
        decps.toArray(ret);
        return ret;
    }

    private void addAnnotationStyleDeclareParents(List<org.mp4parser.aspectj.lang.reflect.DeclareParents> toList) {
        for (Field f : this.clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(DeclareParents.class) && f.getType().isInterface()) {
                toList.add(new DeclareParentsImpl(((DeclareParents) f.getAnnotation(DeclareParents.class)).value(), f.getType().getName(), false, this));
            }
        }
    }

    public DeclareSoft[] getDeclareSofts() {
        List<DeclareSoft> decs = new ArrayList<>();
        for (Method method : this.clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ajcDeclareSoft.class)) {
                ajcDeclareSoft decSAnn = (ajcDeclareSoft) method.getAnnotation(ajcDeclareSoft.class);
                decs.add(new DeclareSoftImpl(this, decSAnn.pointcut(), decSAnn.exceptionType()));
            }
        }
        if (getSupertype().isAspect()) {
            decs.addAll(Arrays.asList(getSupertype().getDeclareSofts()));
        }
        DeclareSoft[] ret = new DeclareSoft[decs.size()];
        decs.toArray(ret);
        return ret;
    }

    public DeclareAnnotation[] getDeclareAnnotations() {
        List<DeclareAnnotation> decAs = new ArrayList<>();
        for (Method method : this.clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ajcDeclareAnnotation.class)) {
                ajcDeclareAnnotation decAnn = (ajcDeclareAnnotation) method.getAnnotation(ajcDeclareAnnotation.class);
                Annotation targetAnnotation = null;
                Annotation[] arr$ = method.getAnnotations();
                int len$ = arr$.length;
                int i$ = 0;
                while (true) {
                    if (i$ >= len$) {
                        break;
                    }
                    Annotation ann = arr$[i$];
                    if (ann.annotationType() != ajcDeclareAnnotation.class) {
                        targetAnnotation = ann;
                        break;
                    }
                    i$++;
                }
                decAs.add(new DeclareAnnotationImpl(this, decAnn.kind(), decAnn.pattern(), targetAnnotation, decAnn.annotation()));
            }
        }
        if (getSupertype().isAspect()) {
            decAs.addAll(Arrays.asList(getSupertype().getDeclareAnnotations()));
        }
        DeclareAnnotation[] ret = new DeclareAnnotation[decAs.size()];
        decAs.toArray(ret);
        return ret;
    }

    public DeclarePrecedence[] getDeclarePrecedence() {
        List<DeclarePrecedence> decps = new ArrayList<>();
        if (this.clazz.isAnnotationPresent(org.mp4parser.aspectj.lang.annotation.DeclarePrecedence.class)) {
            decps.add(new DeclarePrecedenceImpl(((org.mp4parser.aspectj.lang.annotation.DeclarePrecedence) this.clazz.getAnnotation(org.mp4parser.aspectj.lang.annotation.DeclarePrecedence.class)).value(), this));
        }
        for (Method method : this.clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ajcDeclarePrecedence.class)) {
                decps.add(new DeclarePrecedenceImpl(((ajcDeclarePrecedence) method.getAnnotation(ajcDeclarePrecedence.class)).value(), this));
            }
        }
        if (getSupertype().isAspect()) {
            decps.addAll(Arrays.asList(getSupertype().getDeclarePrecedence()));
        }
        DeclarePrecedence[] ret = new DeclarePrecedence[decps.size()];
        decps.toArray(ret);
        return ret;
    }

    public T[] getEnumConstants() {
        return this.clazz.getEnumConstants();
    }

    public TypeVariable<Class<T>>[] getTypeParameters() {
        return this.clazz.getTypeParameters();
    }

    public boolean isEnum() {
        return this.clazz.isEnum();
    }

    public boolean isInstance(Object o) {
        return this.clazz.isInstance(o);
    }

    public boolean isInterface() {
        return this.clazz.isInterface();
    }

    public boolean isLocalClass() {
        return this.clazz.isLocalClass() && !isAspect();
    }

    public boolean isMemberClass() {
        return this.clazz.isMemberClass() && !isAspect();
    }

    public boolean isArray() {
        return this.clazz.isArray();
    }

    public boolean isPrimitive() {
        return this.clazz.isPrimitive();
    }

    public boolean isAspect() {
        return this.clazz.getAnnotation(Aspect.class) != null;
    }

    public boolean isMemberAspect() {
        return this.clazz.isMemberClass() && isAspect();
    }

    public boolean isPrivileged() {
        return isAspect() && this.clazz.isAnnotationPresent(ajcPrivileged.class);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AjTypeImpl)) {
            return false;
        }
        return ((AjTypeImpl) obj).clazz.equals(this.clazz);
    }

    public int hashCode() {
        return this.clazz.hashCode();
    }

    private AjType<?>[] toAjTypeArray(Class<?>[] classes) {
        AjType<?>[] ajtypes = new AjType[classes.length];
        for (int i = 0; i < ajtypes.length; i++) {
            ajtypes[i] = AjTypeSystem.getAjType(classes[i]);
        }
        return ajtypes;
    }

    private Class<?>[] toClassArray(AjType<?>[] ajTypes) {
        Class<?>[] classes = new Class[ajTypes.length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = ajTypes[i].getJavaClass();
        }
        return classes;
    }

    public String toString() {
        return getName();
    }
}
