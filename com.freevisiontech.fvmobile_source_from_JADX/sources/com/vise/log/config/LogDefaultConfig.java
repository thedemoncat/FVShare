package com.vise.log.config;

import android.text.TextUtils;
import com.vise.log.common.LogPattern;
import com.vise.log.parser.Parser;
import java.util.ArrayList;
import java.util.List;

public class LogDefaultConfig implements LogConfig {
    private static LogDefaultConfig singleton;
    private boolean enable = true;
    private String formatTag;
    private int logLevel = 2;
    private List<Parser> parseList = new ArrayList();
    private boolean showBorder = true;
    private String tagPrefix;

    private LogDefaultConfig() {
    }

    public static LogDefaultConfig getInstance() {
        if (singleton == null) {
            synchronized (LogDefaultConfig.class) {
                if (singleton == null) {
                    singleton = new LogDefaultConfig();
                }
            }
        }
        return singleton;
    }

    public LogConfig configAllowLog(boolean allowLog) {
        this.enable = allowLog;
        return this;
    }

    public LogConfig configTagPrefix(String prefix) {
        this.tagPrefix = prefix;
        return this;
    }

    public LogConfig configFormatTag(String formatTag2) {
        this.formatTag = formatTag2;
        return this;
    }

    public String getFormatTag(StackTraceElement caller) {
        LogPattern logPattern;
        if (TextUtils.isEmpty(this.formatTag) || caller == null || (logPattern = LogPattern.compile(this.formatTag)) == null) {
            return null;
        }
        return logPattern.apply(caller);
    }

    public LogConfig configShowBorders(boolean showBorder2) {
        this.showBorder = showBorder2;
        return this;
    }

    public LogConfig configLevel(int logLevel2) {
        this.logLevel = logLevel2;
        return this;
    }

    public LogConfig addParserClass(Class<? extends Parser>... classes) {
        for (Class<? extends Parser> cla : classes) {
            try {
                this.parseList.add(0, cla.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public String getTagPrefix() {
        if (TextUtils.isEmpty(this.tagPrefix)) {
            return "ViseLog";
        }
        return this.tagPrefix;
    }

    public boolean isShowBorder() {
        return this.showBorder;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    public List<Parser> getParseList() {
        return this.parseList;
    }
}
