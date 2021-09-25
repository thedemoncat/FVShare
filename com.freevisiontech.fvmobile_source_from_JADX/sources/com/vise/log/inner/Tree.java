package com.vise.log.inner;

import android.text.TextUtils;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.vise.log.ViseLog;
import com.vise.log.common.LogConstant;
import com.vise.log.common.LogConvert;
import com.vise.log.config.LogDefaultConfig;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.MissingFormatArgumentException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Tree implements ITree {
    private final ThreadLocal<String> localTags = new ThreadLocal<>();
    private LogDefaultConfig mLogConfig = LogDefaultConfig.getInstance();

    /* access modifiers changed from: protected */
    public abstract void log(int i, String str, String str2);

    protected Tree() {
        this.mLogConfig.addParserClass(LogConstant.DEFAULT_PARSE_CLASS);
    }

    public ITree setTag(String tag) {
        if (!TextUtils.isEmpty(tag) && this.mLogConfig.isEnable()) {
            this.localTags.set(tag);
        }
        return this;
    }

    public void wtf(String message, Object... args) {
        logString(7, message, args);
    }

    public void wtf(Object object) {
        logObject(7, object);
    }

    /* renamed from: e */
    public void mo8869e(String message, Object... args) {
        logString(6, message, args);
    }

    /* renamed from: e */
    public void mo8868e(Object object) {
        logObject(6, object);
    }

    /* renamed from: w */
    public void mo8876w(String message, Object... args) {
        logString(5, message, args);
    }

    /* renamed from: w */
    public void mo8875w(Object object) {
        logObject(5, object);
    }

    /* renamed from: d */
    public void mo8867d(String message, Object... args) {
        logString(3, message, args);
    }

    /* renamed from: d */
    public void mo8866d(Object object) {
        logObject(3, object);
    }

    /* renamed from: i */
    public void mo8871i(String message, Object... args) {
        logString(4, message, args);
    }

    /* renamed from: i */
    public void mo8870i(Object object) {
        logObject(4, object);
    }

    /* renamed from: v */
    public void mo8874v(String message, Object... args) {
        logString(2, message, args);
    }

    /* renamed from: v */
    public void mo8873v(Object object) {
        logObject(2, object);
    }

    public void json(String json) {
        if (TextUtils.isEmpty(json)) {
            mo8866d("JSON{json is empty}");
            return;
        }
        try {
            if (json.startsWith("{")) {
                mo8866d(new JSONObject(json).toString(4));
            } else if (json.startsWith("[")) {
                mo8866d(new JSONArray(json).toString(4));
            }
        } catch (JSONException e) {
            mo8868e(e.toString() + "\n\njson = " + json);
        }
    }

    public void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            mo8866d("XML{xml is empty}");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", BleConstant.ISO);
            transformer.transform(xmlInput, xmlOutput);
            mo8866d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            mo8868e(e.toString() + "\n\nxml = " + xml);
        }
    }

    private void logObject(int type, Object object) {
        logString(type, LogConvert.objectToString(object), new Object[0]);
    }

    private synchronized void logString(int type, String msg, Object... args) {
        logString(type, msg, false, args);
    }

    private void logString(int type, String msg, boolean isPart, Object... args) {
        int i = 0;
        if (this.mLogConfig.isEnable() && type >= this.mLogConfig.getLogLevel()) {
            String tag = generateTag();
            if (msg.length() > 3072) {
                if (this.mLogConfig.isShowBorder()) {
                    printLog(type, tag, LogConvert.printDividingLine(1));
                    printLog(type, tag, LogConvert.printDividingLine(3) + getTopStackInfo());
                    printLog(type, tag, LogConvert.printDividingLine(4));
                }
                for (String subMsg : LogConvert.largeStringToList(msg)) {
                    logString(type, subMsg, true, args);
                }
                if (this.mLogConfig.isShowBorder()) {
                    printLog(type, tag, LogConvert.printDividingLine(2));
                    return;
                }
                return;
            }
            if (args.length > 0) {
                try {
                    msg = String.format(msg, args);
                } catch (MissingFormatArgumentException e) {
                    e.printStackTrace();
                }
            }
            if (!this.mLogConfig.isShowBorder()) {
                printLog(type, tag, msg);
            } else if (isPart) {
                String[] split = msg.split(LogConstant.f1064BR);
                int length = split.length;
                while (i < length) {
                    printLog(type, tag, LogConvert.printDividingLine(3) + split[i]);
                    i++;
                }
            } else {
                printLog(type, tag, LogConvert.printDividingLine(1));
                printLog(type, tag, LogConvert.printDividingLine(3) + getTopStackInfo());
                printLog(type, tag, LogConvert.printDividingLine(4));
                String[] split2 = msg.split(LogConstant.f1064BR);
                int length2 = split2.length;
                while (i < length2) {
                    printLog(type, tag, LogConvert.printDividingLine(3) + split2[i]);
                    i++;
                }
                printLog(type, tag, LogConvert.printDividingLine(2));
            }
        }
    }

    private String generateTag() {
        String tempTag = this.localTags.get();
        if (TextUtils.isEmpty(tempTag)) {
            return this.mLogConfig.getTagPrefix();
        }
        this.localTags.remove();
        return tempTag;
    }

    private StackTraceElement getCurrentStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace, ViseLog.class);
        if (stackOffset == -1) {
            return null;
        }
        return trace[stackOffset];
    }

    private String getTopStackInfo() {
        String customTag = this.mLogConfig.getFormatTag(getCurrentStackTrace());
        if (customTag != null) {
            return customTag;
        }
        StackTraceElement caller = getCurrentStackTrace();
        if (caller == null) {
            return "";
        }
        String stackTrace = caller.toString();
        String stackTrace2 = stackTrace.substring(stackTrace.lastIndexOf(40), stackTrace.length());
        String callerClazzName = caller.getClassName();
        return String.format("%s.%s%s", new Object[]{callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1), caller.getMethodName(), stackTrace2});
    }

    private int getStackOffset(StackTraceElement[] trace, Class cla) {
        for (int i = 5; i < trace.length; i++) {
            String name = trace[i].getClassName();
            if ((!cla.equals(ViseLog.class) || i >= trace.length - 1 || !trace[i + 1].getClassName().equals(ViseLog.class.getName())) && name.equals(cla.getName())) {
                return i + 1;
            }
        }
        return -1;
    }

    private void printLog(int type, String tag, String msg) {
        if (!this.mLogConfig.isShowBorder()) {
            msg = getTopStackInfo() + ": " + msg;
        }
        log(type, tag, msg);
    }
}
