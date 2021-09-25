package org.mp4parser.aspectj.runtime.internal;

import java.util.Stack;
import org.mp4parser.aspectj.lang.NoAspectBoundException;
import org.mp4parser.aspectj.runtime.CFlow;
import org.mp4parser.aspectj.runtime.internal.cflowstack.ThreadStack;
import org.mp4parser.aspectj.runtime.internal.cflowstack.ThreadStackFactory;
import org.mp4parser.aspectj.runtime.internal.cflowstack.ThreadStackFactoryImpl;
import org.mp4parser.aspectj.runtime.internal.cflowstack.ThreadStackFactoryImpl11;

public class CFlowStack {
    private static ThreadStackFactory tsFactory;
    private ThreadStack stackProxy = tsFactory.getNewThreadStack();

    static {
        selectFactoryForVMVersion();
    }

    private Stack getThreadStack() {
        return this.stackProxy.getThreadStack();
    }

    public void push(Object obj) {
        getThreadStack().push(obj);
    }

    public void pushInstance(Object obj) {
        getThreadStack().push(new CFlow(obj));
    }

    public void push(Object[] obj) {
        getThreadStack().push(new CFlowPlusState(obj));
    }

    public void pop() {
        Stack s = getThreadStack();
        s.pop();
        if (s.isEmpty()) {
            this.stackProxy.removeThreadStack();
        }
    }

    public Object peek() {
        Stack stack = getThreadStack();
        if (!stack.isEmpty()) {
            return stack.peek();
        }
        throw new NoAspectBoundException();
    }

    public Object get(int index) {
        CFlow cf = peekCFlow();
        if (cf == null) {
            return null;
        }
        return cf.get(index);
    }

    public Object peekInstance() {
        CFlow cf = peekCFlow();
        if (cf != null) {
            return cf.getAspect();
        }
        throw new NoAspectBoundException();
    }

    public CFlow peekCFlow() {
        Stack stack = getThreadStack();
        if (stack.isEmpty()) {
            return null;
        }
        return (CFlow) stack.peek();
    }

    public CFlow peekTopCFlow() {
        Stack stack = getThreadStack();
        if (stack.isEmpty()) {
            return null;
        }
        return (CFlow) stack.elementAt(0);
    }

    public boolean isValid() {
        return !getThreadStack().isEmpty();
    }

    private static ThreadStackFactory getThreadLocalStackFactory() {
        return new ThreadStackFactoryImpl();
    }

    private static ThreadStackFactory getThreadLocalStackFactoryFor11() {
        return new ThreadStackFactoryImpl11();
    }

    private static void selectFactoryForVMVersion() {
        boolean useThreadLocalImplementation;
        String override = getSystemPropertyWithoutSecurityException("aspectj.runtime.cflowstack.usethreadlocal", "unspecified");
        if (override.equals("unspecified")) {
            useThreadLocalImplementation = System.getProperty("java.class.version", "0.0").compareTo("46.0") >= 0;
        } else {
            useThreadLocalImplementation = override.equals("yes") || override.equals("true");
        }
        if (useThreadLocalImplementation) {
            tsFactory = getThreadLocalStackFactory();
        } else {
            tsFactory = getThreadLocalStackFactoryFor11();
        }
    }

    private static String getSystemPropertyWithoutSecurityException(String aPropertyName, String aDefaultValue) {
        try {
            return System.getProperty(aPropertyName, aDefaultValue);
        } catch (SecurityException e) {
            return aDefaultValue;
        }
    }

    public static String getThreadStackFactoryClassName() {
        return tsFactory.getClass().getName();
    }
}
