package com.vise.log.common;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LogPattern {
    private final int count;
    private final int length;

    /* access modifiers changed from: protected */
    public abstract String doApply(StackTraceElement stackTraceElement);

    private LogPattern(int count2, int length2) {
        this.count = count2;
        this.length = length2;
    }

    public final String apply(StackTraceElement caller) {
        return LogConvert.shorten(doApply(caller), this.count, this.length);
    }

    /* access modifiers changed from: protected */
    public boolean isCallerNeeded() {
        return false;
    }

    public static LogPattern compile(String pattern) {
        if (pattern == null) {
            return null;
        }
        try {
            return new Compiler().compile(pattern);
        } catch (Exception e) {
            return new PlainLogPattern(0, 0, pattern);
        }
    }

    public static class PlainLogPattern extends LogPattern {
        private final String string;

        public PlainLogPattern(int count, int length, String string2) {
            super(count, length);
            this.string = string2;
        }

        /* access modifiers changed from: protected */
        public String doApply(StackTraceElement caller) {
            return this.string;
        }
    }

    public static class DateLogPattern extends LogPattern {
        private final SimpleDateFormat dateFormat;

        @SuppressLint({"SimpleDateFormat"})
        public DateLogPattern(int count, int length, String dateFormat2) {
            super(count, length);
            if (dateFormat2 != null) {
                this.dateFormat = new SimpleDateFormat(dateFormat2);
            } else {
                this.dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
            }
        }

        /* access modifiers changed from: protected */
        public String doApply(StackTraceElement caller) {
            return this.dateFormat.format(new Date());
        }
    }

    public static class CallerLogPattern extends LogPattern {
        private int callerCount;
        private int callerLength;

        public CallerLogPattern(int count, int length, int callerCount2, int callerLength2) {
            super(count, length);
            this.callerCount = callerCount2;
            this.callerLength = callerLength2;
        }

        /* access modifiers changed from: protected */
        public String doApply(StackTraceElement caller) {
            String callerString;
            if (caller == null) {
                throw new IllegalArgumentException("Caller not found");
            }
            if (caller.getLineNumber() < 0) {
                callerString = String.format("%s#%s", new Object[]{caller.getClassName(), caller.getMethodName()});
            } else {
                String stackTrace = caller.toString();
                callerString = String.format("%s.%s%s", new Object[]{caller.getClassName(), caller.getMethodName(), stackTrace.substring(stackTrace.lastIndexOf(40), stackTrace.length())});
            }
            try {
                return LogConvert.shortenClassName(callerString, this.callerCount, this.callerLength);
            } catch (Exception e) {
                return e.getMessage();
            }
        }

        /* access modifiers changed from: protected */
        public boolean isCallerNeeded() {
            return true;
        }
    }

    public static class ConcatenateLogPattern extends LogPattern {
        private final List<LogPattern> patternList;

        public ConcatenateLogPattern(int count, int length, List<LogPattern> patternList2) {
            super(count, length);
            this.patternList = new ArrayList(patternList2);
        }

        public void addPattern(LogPattern pattern) {
            this.patternList.add(pattern);
        }

        /* access modifiers changed from: protected */
        public String doApply(StackTraceElement caller) {
            StringBuilder builder = new StringBuilder();
            for (LogPattern pattern : this.patternList) {
                builder.append(pattern.apply(caller));
            }
            return builder.toString();
        }

        /* access modifiers changed from: protected */
        public boolean isCallerNeeded() {
            for (LogPattern pattern : this.patternList) {
                if (pattern.isCallerNeeded()) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class ThreadNameLogPattern extends LogPattern {
        public ThreadNameLogPattern(int count, int length) {
            super(count, length);
        }

        /* access modifiers changed from: protected */
        public String doApply(StackTraceElement caller) {
            return Thread.currentThread().getName();
        }
    }

    public static class Compiler {
        private final Pattern CALLER_PATTERN = Pattern.compile("%([+-]?\\d+)?(\\.([+-]?\\d+))?caller(\\{([+-]?\\d+)?(\\.([+-]?\\d+))?\\})?");
        private final Pattern CALLER_PATTERN_SHORT = Pattern.compile("%([+-]?\\d+)?(\\.([+-]?\\d+))?c(\\{([+-]?\\d+)?(\\.([+-]?\\d+))?\\})?");
        private final Pattern CONCATENATE_PATTERN = Pattern.compile("%([+-]?\\d+)?(\\.([+-]?\\d+))?\\(");
        private final Pattern DATE_PATTERN = Pattern.compile("%date(\\{(.*?)\\})?");
        private final Pattern DATE_PATTERN_SHORT = Pattern.compile("%d(\\{(.*?)\\})?");
        private final Pattern NEWLINE_PATTERN = Pattern.compile("%n");
        private final Pattern PERCENT_PATTERN = Pattern.compile("%%");
        private final Pattern THREAD_NAME_PATTERN = Pattern.compile("%([+-]?\\d+)?(\\.([+-]?\\d+))?thread");
        private final Pattern THREAD_NAME_PATTERN_SHORT = Pattern.compile("%([+-]?\\d+)?(\\.([+-]?\\d+))?t");
        private String patternString;
        private int position;
        private List<ConcatenateLogPattern> queue;

        public LogPattern compile(String string) {
            if (string == null) {
                return null;
            }
            this.position = 0;
            this.patternString = string;
            this.queue = new ArrayList();
            this.queue.add(new ConcatenateLogPattern(0, 0, new ArrayList()));
            while (true) {
                if (string.length() <= this.position) {
                    break;
                }
                int index = string.indexOf("%", this.position);
                int bracketIndex = string.indexOf(")", this.position);
                if (this.queue.size() > 1 && bracketIndex < index) {
                    this.queue.get(this.queue.size() - 1).addPattern(new PlainLogPattern(0, 0, string.substring(this.position, bracketIndex)));
                    this.queue.get(this.queue.size() - 2).addPattern(this.queue.remove(this.queue.size() - 1));
                    this.position = bracketIndex + 1;
                }
                if (index == -1) {
                    this.queue.get(this.queue.size() - 1).addPattern(new PlainLogPattern(0, 0, string.substring(this.position)));
                    break;
                }
                this.queue.get(this.queue.size() - 1).addPattern(new PlainLogPattern(0, 0, string.substring(this.position, index)));
                this.position = index;
                parse();
            }
            return this.queue.get(0);
        }

        private void parse() {
            String group;
            String group2;
            Matcher matcher = findPattern(this.PERCENT_PATTERN);
            if (matcher != null) {
                this.queue.get(this.queue.size() - 1).addPattern(new PlainLogPattern(0, 0, "%"));
                this.position = matcher.end();
                return;
            }
            Matcher matcher2 = findPattern(this.NEWLINE_PATTERN);
            if (matcher2 != null) {
                this.queue.get(this.queue.size() - 1).addPattern(new PlainLogPattern(0, 0, "\n"));
                this.position = matcher2.end();
                return;
            }
            Matcher matcher3 = findPattern(this.CALLER_PATTERN);
            if (matcher3 == null && (matcher3 = findPattern(this.CALLER_PATTERN_SHORT)) == null) {
                Matcher matcher4 = findPattern(this.DATE_PATTERN);
                if (matcher4 == null && (matcher4 = findPattern(this.DATE_PATTERN_SHORT)) == null) {
                    Matcher matcher5 = findPattern(this.THREAD_NAME_PATTERN);
                    if (matcher5 == null && (matcher5 = findPattern(this.THREAD_NAME_PATTERN_SHORT)) == null) {
                        Matcher matcher6 = findPattern(this.CONCATENATE_PATTERN);
                        if (matcher6 != null) {
                            this.queue.add(new ConcatenateLogPattern(Integer.parseInt(matcher6.group(1) == null ? "0" : matcher6.group(1)), Integer.parseInt(matcher6.group(3) == null ? "0" : matcher6.group(3)), new ArrayList()));
                            this.position = matcher6.end();
                            return;
                        }
                        throw new IllegalArgumentException();
                    }
                    this.queue.get(this.queue.size() - 1).addPattern(new ThreadNameLogPattern(Integer.parseInt(matcher5.group(1) == null ? "0" : matcher5.group(1)), Integer.parseInt(matcher5.group(3) == null ? "0" : matcher5.group(3))));
                    this.position = matcher5.end();
                    return;
                }
                this.queue.get(this.queue.size() - 1).addPattern(new DateLogPattern(0, 0, matcher4.group(2)));
                this.position = matcher4.end();
                return;
            }
            int count = Integer.parseInt(matcher3.group(1) == null ? "0" : matcher3.group(1));
            int length = Integer.parseInt(matcher3.group(3) == null ? "0" : matcher3.group(3));
            if (matcher3.group(5) == null) {
                group = "0";
            } else {
                group = matcher3.group(5);
            }
            int countCaller = Integer.parseInt(group);
            if (matcher3.group(7) == null) {
                group2 = "0";
            } else {
                group2 = matcher3.group(7);
            }
            this.queue.get(this.queue.size() - 1).addPattern(new CallerLogPattern(count, length, countCaller, Integer.parseInt(group2)));
            this.position = matcher3.end();
        }

        private Matcher findPattern(Pattern pattern) {
            Matcher matcher = pattern.matcher(this.patternString);
            if (!matcher.find(this.position) || matcher.start() != this.position) {
                return null;
            }
            return matcher;
        }
    }
}
