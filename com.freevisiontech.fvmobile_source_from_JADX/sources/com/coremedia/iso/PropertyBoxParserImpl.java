package com.coremedia.iso;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.UserBox;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class PropertyBoxParserImpl extends AbstractBoxParser {
    static String[] EMPTY_STRING_ARRAY = new String[0];
    StringBuilder buildLookupStrings = new StringBuilder();
    ThreadLocal<String> clazzName = new ThreadLocal<>();
    Pattern constuctorPattern = Pattern.compile("(.*)\\((.*?)\\)");
    Properties mapping;
    ThreadLocal<String[]> param = new ThreadLocal<>();

    public PropertyBoxParserImpl(String... customProperties) {
        InputStream customIS;
        InputStream is = getClass().getResourceAsStream("/isoparser-default.properties");
        try {
            this.mapping = new Properties();
            this.mapping.load(is);
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> enumeration = (cl == null ? ClassLoader.getSystemClassLoader() : cl).getResources("isoparser-custom.properties");
            while (enumeration.hasMoreElements()) {
                customIS = enumeration.nextElement().openStream();
                this.mapping.load(customIS);
                customIS.close();
            }
            for (String customProperty : customProperties) {
                this.mapping.load(getClass().getResourceAsStream(customProperty));
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        } catch (Throwable th) {
            try {
                is.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            throw th;
        }
    }

    public PropertyBoxParserImpl(Properties mapping2) {
        this.mapping = mapping2;
    }

    public Box createBox(String type, byte[] userType, String parent) {
        invoke(type, userType, parent);
        String[] param2 = this.param.get();
        try {
            Class<?> cls = Class.forName(this.clazzName.get());
            if (param2.length <= 0) {
                return (Box) cls.newInstance();
            }
            Class[] constructorArgsClazz = new Class[param2.length];
            Object[] constructorArgs = new Object[param2.length];
            for (int i = 0; i < param2.length; i++) {
                if ("userType".equals(param2[i])) {
                    constructorArgs[i] = userType;
                    constructorArgsClazz[i] = byte[].class;
                } else if (IjkMediaMeta.IJKM_KEY_TYPE.equals(param2[i])) {
                    constructorArgs[i] = type;
                    constructorArgsClazz[i] = String.class;
                } else if ("parent".equals(param2[i])) {
                    constructorArgs[i] = parent;
                    constructorArgsClazz[i] = String.class;
                } else {
                    throw new InternalError("No such param: " + param2[i]);
                }
            }
            return (Box) cls.getConstructor(constructorArgsClazz).newInstance(constructorArgs);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3);
        } catch (InvocationTargetException e4) {
            throw new RuntimeException(e4);
        } catch (NoSuchMethodException e5) {
            throw new RuntimeException(e5);
        }
    }

    public void invoke(String type, byte[] userType, String parent) {
        String constructor;
        if (userType == null) {
            constructor = this.mapping.getProperty(type);
            if (constructor == null) {
                String lookup = this.buildLookupStrings.append(parent).append('-').append(type).toString();
                this.buildLookupStrings.setLength(0);
                constructor = this.mapping.getProperty(lookup);
            }
        } else if (!UserBox.TYPE.equals(type)) {
            throw new RuntimeException("we have a userType but no uuid box type. Something's wrong");
        } else {
            constructor = this.mapping.getProperty("uuid[" + Hex.encodeHex(userType).toUpperCase() + "]");
            if (constructor == null) {
                constructor = this.mapping.getProperty(String.valueOf(parent) + "-uuid[" + Hex.encodeHex(userType).toUpperCase() + "]");
            }
            if (constructor == null) {
                constructor = this.mapping.getProperty(UserBox.TYPE);
            }
        }
        if (constructor == null) {
            constructor = this.mapping.getProperty("default");
        }
        if (constructor == null) {
            throw new RuntimeException("No box object found for " + type);
        } else if (!constructor.endsWith(")")) {
            this.param.set(EMPTY_STRING_ARRAY);
            this.clazzName.set(constructor);
        } else {
            Matcher m = this.constuctorPattern.matcher(constructor);
            if (!m.matches()) {
                throw new RuntimeException("Cannot work with that constructor: " + constructor);
            }
            this.clazzName.set(m.group(1));
            if (m.group(2).length() == 0) {
                this.param.set(EMPTY_STRING_ARRAY);
            } else {
                this.param.set(m.group(2).length() > 0 ? m.group(2).split(",") : new String[0]);
            }
        }
    }
}
