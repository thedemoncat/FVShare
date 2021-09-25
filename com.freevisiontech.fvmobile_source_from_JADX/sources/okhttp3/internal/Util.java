package okhttp3.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.IDN;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;

public final class Util {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final RequestBody EMPTY_REQUEST = RequestBody.create((MediaType) null, EMPTY_BYTE_ARRAY);
    public static final ResponseBody EMPTY_RESPONSE = ResponseBody.create((MediaType) null, EMPTY_BYTE_ARRAY);
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final Comparator<String> NATURAL_ORDER = new Comparator<String>() {
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    };
    public static final TimeZone UTC = TimeZone.getTimeZone("GMT");
    private static final Charset UTF_16_BE = Charset.forName("UTF-16BE");
    private static final ByteString UTF_16_BE_BOM = ByteString.decodeHex("feff");
    private static final Charset UTF_16_LE = Charset.forName("UTF-16LE");
    private static final ByteString UTF_16_LE_BOM = ByteString.decodeHex("fffe");
    private static final Charset UTF_32_BE = Charset.forName("UTF-32BE");
    private static final ByteString UTF_32_BE_BOM = ByteString.decodeHex("0000ffff");
    private static final Charset UTF_32_LE = Charset.forName("UTF-32LE");
    private static final ByteString UTF_32_LE_BOM = ByteString.decodeHex("ffff0000");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final ByteString UTF_8_BOM = ByteString.decodeHex("efbbbf");
    private static final Pattern VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");

    private Util() {
    }

    public static void checkOffsetAndCount(long arrayLength, long offset, long count) {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (AssertionError e) {
                if (!isAndroidGetsocknameError(e)) {
                    throw e;
                }
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e2) {
            }
        }
    }

    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    public static boolean discard(Source source, int timeout, TimeUnit timeUnit) {
        try {
            return skipAll(source, timeout, timeUnit);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean skipAll(Source source, int duration, TimeUnit timeUnit) throws IOException {
        long originalDuration;
        long now = System.nanoTime();
        if (source.timeout().hasDeadline()) {
            originalDuration = source.timeout().deadlineNanoTime() - now;
        } else {
            originalDuration = Long.MAX_VALUE;
        }
        source.timeout().deadlineNanoTime(Math.min(originalDuration, timeUnit.toNanos((long) duration)) + now);
        try {
            Buffer skipBuffer = new Buffer();
            while (source.read(skipBuffer, 8192) != -1) {
                skipBuffer.clear();
            }
            if (originalDuration == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
            } else {
                source.timeout().deadlineNanoTime(now + originalDuration);
            }
            return true;
        } catch (InterruptedIOException e) {
            if (originalDuration == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
            } else {
                source.timeout().deadlineNanoTime(now + originalDuration);
            }
            return false;
        } catch (Throwable th) {
            if (originalDuration == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
            } else {
                source.timeout().deadlineNanoTime(now + originalDuration);
            }
            throw th;
        }
    }

    public static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList(new ArrayList(list));
    }

    public static <T> List<T> immutableList(T... elements) {
        return Collections.unmodifiableList(Arrays.asList((Object[]) elements.clone()));
    }

    public static ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    public static String[] intersect(Comparator<? super String> comparator, String[] first, String[] second) {
        List<String> result = new ArrayList<>();
        for (String a : first) {
            int length = second.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (comparator.compare(a, second[i]) == 0) {
                    result.add(a);
                    break;
                } else {
                    i++;
                }
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    public static boolean nonEmptyIntersection(Comparator<String> comparator, String[] first, String[] second) {
        if (first == null || second == null || first.length == 0 || second.length == 0) {
            return false;
        }
        for (String a : first) {
            for (String b : second) {
                if (comparator.compare(a, b) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String hostHeader(HttpUrl url, boolean includeDefaultPort) {
        String host;
        if (url.host().contains(":")) {
            host = "[" + url.host() + "]";
        } else {
            host = url.host();
        }
        if (includeDefaultPort || url.port() != HttpUrl.defaultPort(url.scheme())) {
            return host + ":" + url.port();
        }
        return host;
    }

    public static String toHumanReadableAscii(String s) {
        int i = 0;
        int length = s.length();
        while (i < length) {
            int c = s.codePointAt(i);
            if (c <= 31 || c >= 127) {
                Buffer buffer = new Buffer();
                buffer.writeUtf8(s, 0, i);
                int j = i;
                while (j < length) {
                    int c2 = s.codePointAt(j);
                    buffer.writeUtf8CodePoint((c2 <= 31 || c2 >= 127) ? 63 : c2);
                    j += Character.charCount(c2);
                }
                return buffer.readUtf8();
            }
            i += Character.charCount(c);
        }
        return s;
    }

    public static boolean isAndroidGetsocknameError(AssertionError e) {
        return (e.getCause() == null || e.getMessage() == null || !e.getMessage().contains("getsockname failed")) ? false : true;
    }

    public static int indexOf(Comparator<String> comparator, String[] array, String value) {
        int size = array.length;
        for (int i = 0; i < size; i++) {
            if (comparator.compare(array[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    public static String[] concat(String[] array, String value) {
        String[] result = new String[(array.length + 1)];
        System.arraycopy(array, 0, result, 0, array.length);
        result[result.length - 1] = value;
        return result;
    }

    public static int skipLeadingAsciiWhitespace(String input, int pos, int limit) {
        int i = pos;
        while (i < limit) {
            switch (input.charAt(i)) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    i++;
                default:
                    return i;
            }
        }
        return limit;
    }

    public static int skipTrailingAsciiWhitespace(String input, int pos, int limit) {
        int i = limit - 1;
        while (i >= pos) {
            switch (input.charAt(i)) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    i--;
                default:
                    return i + 1;
            }
        }
        return pos;
    }

    public static String trimSubstring(String string, int pos, int limit) {
        int start = skipLeadingAsciiWhitespace(string, pos, limit);
        return string.substring(start, skipTrailingAsciiWhitespace(string, start, limit));
    }

    public static int delimiterOffset(String input, int pos, int limit, String delimiters) {
        for (int i = pos; i < limit; i++) {
            if (delimiters.indexOf(input.charAt(i)) != -1) {
                return i;
            }
        }
        return limit;
    }

    public static int delimiterOffset(String input, int pos, int limit, char delimiter) {
        for (int i = pos; i < limit; i++) {
            if (input.charAt(i) == delimiter) {
                return i;
            }
        }
        return limit;
    }

    public static String domainToAscii(String input) {
        try {
            String result = IDN.toASCII(input).toLowerCase(Locale.US);
            if (result.isEmpty()) {
                return null;
            }
            if (containsInvalidHostnameAsciiCodes(result)) {
                return null;
            }
            return result;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static boolean containsInvalidHostnameAsciiCodes(String hostnameAscii) {
        for (int i = 0; i < hostnameAscii.length(); i++) {
            char c = hostnameAscii.charAt(i);
            if (c <= 31 || c >= 127 || " #%/:?@[\\]".indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    public static int indexOfControlOrNonAscii(String input) {
        int i = 0;
        int length = input.length();
        while (i < length) {
            char c = input.charAt(i);
            if (c <= 31 || c >= 127) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static boolean verifyAsIpAddress(String host) {
        return VERIFY_AS_IP_ADDRESS.matcher(host).matches();
    }

    public static String format(String format, Object... args) {
        return String.format(Locale.US, format, args);
    }

    public static Charset bomAwareCharset(BufferedSource source, Charset charset) throws IOException {
        if (source.rangeEquals(0, UTF_8_BOM)) {
            source.skip((long) UTF_8_BOM.size());
            return UTF_8;
        } else if (source.rangeEquals(0, UTF_16_BE_BOM)) {
            source.skip((long) UTF_16_BE_BOM.size());
            return UTF_16_BE;
        } else if (source.rangeEquals(0, UTF_16_LE_BOM)) {
            source.skip((long) UTF_16_LE_BOM.size());
            return UTF_16_LE;
        } else if (source.rangeEquals(0, UTF_32_BE_BOM)) {
            source.skip((long) UTF_32_BE_BOM.size());
            return UTF_32_BE;
        } else if (!source.rangeEquals(0, UTF_32_LE_BOM)) {
            return charset;
        } else {
            source.skip((long) UTF_32_LE_BOM.size());
            return UTF_32_LE;
        }
    }
}
