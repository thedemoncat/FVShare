package com.google.android.exoplayer.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.ExoPlayerLibraryInfo;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.vending.expansion.downloader.Constants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Util {
    private static final int[] CRC32_BYTES_MSBF = {0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013, 1276238704, 1221641927, 1167319070, 1095957929, 1595256236, 1540665371, 1452775106, 1381403509, 1780037320, 1859660671, 1671105958, 1733955601, 2031960084, 2111593891, 1889500026, 1952343757, -1742489888, -1662866601, -1851683442, -1788833735, -1960329156, -1880695413, -2103051438, -2040207643, -1104454824, -1159051537, -1213636554, -1284997759, -1389417084, -1444007885, -1532160278, -1603531939, -734892656, -789352409, -575645954, -646886583, -952755380, -1007220997, -827056094, -898286187, -231047128, -151282273, -71779514, -8804623, -515967244, -436212925, -390279782, -327299027, 881225847, 809987520, 1023691545, 969234094, 662832811, 591600412, 771767749, 717299826, 311336399, 374308984, 453813921, 533576470, 25881363, 88864420, 134795389, 214552010, 2023205639, 2086057648, 1897238633, 1976864222, 1804852699, 1867694188, 1645340341, 1724971778, 1587496639, 1516133128, 1461550545, 1406951526, 1302016099, 1230646740, 1142491917, 1087903418, -1398421865, -1469785312, -1524105735, -1578704818, -1079922613, -1151291908, -1239184603, -1293773166, -1968362705, -1905510760, -2094067647, -2014441994, -1716953613, -1654112188, -1876203875, -1796572374, -525066777, -462094256, -382327159, -302564546, -206542021, -143559028, -97365931, -17609246, -960696225, -1031934488, -817968335, -872425850, -709327229, -780559564, -600130067, -654598054, 1762451694, 1842216281, 1619975040, 1682949687, 2047383090, 2127137669, 1938468188, 2001449195, 1325665622, 1271206113, 1183200824, 1111960463, 1543535498, 1489069629, 1434599652, 1363369299, 622672798, 568075817, 748617968, 677256519, 907627842, 853037301, 1067152940, 995781531, 51762726, 131386257, 177728840, 240578815, 269590778, 349224269, 429104020, 491947555, -248556018, -168932423, -122852000, -60002089, -500490030, -420856475, -341238852, -278395381, -685261898, -739858943, -559578920, -630940305, -1004286614, -1058877219, -845023740, -916395085, -1119974018, -1174433591, -1262701040, -1333941337, -1371866206, -1426332139, -1481064244, -1552294533, -1690935098, -1611170447, -1833673816, -1770699233, -2009983462, -1930228819, -2119160460, -2056179517, 1569362073, 1498123566, 1409854455, 1355396672, 1317987909, 1246755826, 1192025387, 1137557660, 2072149281, 2135122070, 1912620623, 1992383480, 1753615357, 1816598090, 1627664531, 1707420964, 295390185, 358241886, 404320391, 483945776, 43990325, 106832002, 186451547, 266083308, 932423249, 861060070, 1041341759, 986742920, 613929101, 542559546, 756411363, 701822548, -978770311, -1050133554, -869589737, -924188512, -693284699, -764654318, -550540341, -605129092, -475935807, -413084042, -366743377, -287118056, -257573603, -194731862, -114850189, -35218492, -1984365303, -1921392450, -2143631769, -2063868976, -1698919467, -1635936670, -1824608069, -1744851700, -1347415887, -1418654458, -1506661409, -1561119128, -1129027987, -1200260134, -1254728445, -1309196108};
    public static final String DEVICE = Build.DEVICE;
    private static final Pattern ESCAPED_CHARACTER_PATTERN = Pattern.compile("%([A-Fa-f0-9]{2})");
    public static final String MANUFACTURER = Build.MANUFACTURER;
    private static final long MAX_BYTES_TO_DRAIN = 2048;
    public static final String MODEL = Build.MODEL;
    public static final int SDK_INT = ((Build.VERSION.SDK_INT == 23 && Build.VERSION.CODENAME.charAt(0) == 'N') ? 24 : Build.VERSION.SDK_INT);
    private static final String TAG = "Util";
    public static final int TYPE_DASH = 0;
    public static final int TYPE_HLS = 2;
    public static final int TYPE_OTHER = 3;
    public static final int TYPE_SS = 1;
    private static final Pattern XS_DATE_TIME_PATTERN = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)(\\.(\\d+))?([Zz]|((\\+|\\-)(\\d\\d):(\\d\\d)))?");
    private static final Pattern XS_DURATION_PATTERN = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");

    private Util() {
    }

    @SuppressLint({"InlinedApi"})
    public static boolean isAndroidTv(Context context) {
        return context.getPackageManager().hasSystemFeature("android.software.leanback");
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while (true) {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) {
                return outputStream.toByteArray();
            }
            outputStream.write(buffer, 0, bytesRead);
        }
    }

    public static boolean isLocalFileUri(Uri uri) {
        String scheme = uri.getScheme();
        return TextUtils.isEmpty(scheme) || scheme.equals("file");
    }

    public static boolean areEqual(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        return o1.equals(o2);
    }

    public static boolean contains(Object[] items, Object item) {
        for (Object areEqual : items) {
            if (areEqual(areEqual, item)) {
                return true;
            }
        }
        return false;
    }

    public static ExecutorService newSingleThreadExecutor(final String threadName) {
        return Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, threadName);
            }
        });
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(final String threadName) {
        return Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, threadName);
            }
        });
    }

    public static void closeQuietly(DataSource dataSource) {
        try {
            dataSource.close();
        } catch (IOException e) {
        }
    }

    public static void closeQuietly(OutputStream outputStream) {
        try {
            outputStream.close();
        } catch (IOException e) {
        }
    }

    public static String toLowerInvariant(String text) {
        if (text == null) {
            return null;
        }
        return text.toLowerCase(Locale.US);
    }

    public static int ceilDivide(int numerator, int denominator) {
        return ((numerator + denominator) - 1) / denominator;
    }

    public static long ceilDivide(long numerator, long denominator) {
        return ((numerator + denominator) - 1) / denominator;
    }

    public static int binarySearchFloor(long[] a, long key, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(a, key);
        if (index < 0) {
            index = -(index + 2);
        } else if (!inclusive) {
            index--;
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static int binarySearchCeil(long[] a, long key, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(a, key);
        if (index < 0) {
            index ^= -1;
        } else if (!inclusive) {
            index++;
        }
        return stayInBounds ? Math.min(a.length - 1, index) : index;
    }

    public static <T> int binarySearchFloor(List<? extends Comparable<? super T>> list, T key, boolean inclusive, boolean stayInBounds) {
        int index = Collections.binarySearch(list, key);
        if (index < 0) {
            index = -(index + 2);
        } else if (!inclusive) {
            index--;
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static <T> int binarySearchCeil(List<? extends Comparable<? super T>> list, T key, boolean inclusive, boolean stayInBounds) {
        int index = Collections.binarySearch(list, key);
        if (index < 0) {
            index ^= -1;
        } else if (!inclusive) {
            index++;
        }
        return stayInBounds ? Math.min(list.size() - 1, index) : index;
    }

    public static int[] firstIntegersArray(int length) {
        int[] firstIntegers = new int[length];
        for (int i = 0; i < length; i++) {
            firstIntegers[i] = i;
        }
        return firstIntegers;
    }

    public static long parseXsDuration(String value) {
        Matcher matcher = XS_DURATION_PATTERN.matcher(value);
        if (!matcher.matches()) {
            return (long) (Double.parseDouble(value) * 3600.0d * 1000.0d);
        }
        boolean negated = !TextUtils.isEmpty(matcher.group(1));
        String years = matcher.group(3);
        double durationSeconds = years != null ? Double.parseDouble(years) * 3.1556908E7d : 0.0d;
        String months = matcher.group(5);
        double durationSeconds2 = durationSeconds + (months != null ? Double.parseDouble(months) * 2629739.0d : 0.0d);
        String days = matcher.group(7);
        double durationSeconds3 = durationSeconds2 + (days != null ? Double.parseDouble(days) * 86400.0d : 0.0d);
        String hours = matcher.group(10);
        double durationSeconds4 = durationSeconds3 + (hours != null ? Double.parseDouble(hours) * 3600.0d : 0.0d);
        String minutes = matcher.group(12);
        double durationSeconds5 = durationSeconds4 + (minutes != null ? Double.parseDouble(minutes) * 60.0d : 0.0d);
        String seconds = matcher.group(14);
        long durationMillis = (long) (1000.0d * (durationSeconds5 + (seconds != null ? Double.parseDouble(seconds) : 0.0d)));
        if (negated) {
            return -durationMillis;
        }
        return durationMillis;
    }

    public static long parseXsDateTime(String value) throws ParseException {
        int timezoneShift;
        Matcher matcher = XS_DATE_TIME_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new ParseException("Invalid date/time format: " + value, 0);
        }
        if (matcher.group(9) == null) {
            timezoneShift = 0;
        } else if (matcher.group(9).equalsIgnoreCase("Z")) {
            timezoneShift = 0;
        } else {
            timezoneShift = (Integer.parseInt(matcher.group(12)) * 60) + Integer.parseInt(matcher.group(13));
            if (matcher.group(11).equals(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                timezoneShift *= -1;
            }
        }
        Calendar dateTime = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        dateTime.clear();
        dateTime.set(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
        if (!TextUtils.isEmpty(matcher.group(8))) {
            dateTime.set(14, new BigDecimal("0." + matcher.group(8)).movePointRight(3).intValue());
        }
        long time = dateTime.getTimeInMillis();
        if (timezoneShift != 0) {
            return time - ((long) (60000 * timezoneShift));
        }
        return time;
    }

    public static long scaleLargeTimestamp(long timestamp, long multiplier, long divisor) {
        if (divisor >= multiplier && divisor % multiplier == 0) {
            return timestamp / (divisor / multiplier);
        }
        if (divisor < multiplier && multiplier % divisor == 0) {
            return timestamp * (multiplier / divisor);
        }
        return (long) (((double) timestamp) * (((double) multiplier) / ((double) divisor)));
    }

    public static long[] scaleLargeTimestamps(List<Long> timestamps, long multiplier, long divisor) {
        long[] scaledTimestamps = new long[timestamps.size()];
        if (divisor >= multiplier && divisor % multiplier == 0) {
            long divisionFactor = divisor / multiplier;
            for (int i = 0; i < scaledTimestamps.length; i++) {
                scaledTimestamps[i] = timestamps.get(i).longValue() / divisionFactor;
            }
        } else if (divisor >= multiplier || multiplier % divisor != 0) {
            double multiplicationFactor = ((double) multiplier) / ((double) divisor);
            for (int i2 = 0; i2 < scaledTimestamps.length; i2++) {
                scaledTimestamps[i2] = (long) (((double) timestamps.get(i2).longValue()) * multiplicationFactor);
            }
        } else {
            long multiplicationFactor2 = multiplier / divisor;
            for (int i3 = 0; i3 < scaledTimestamps.length; i3++) {
                scaledTimestamps[i3] = timestamps.get(i3).longValue() * multiplicationFactor2;
            }
        }
        return scaledTimestamps;
    }

    public static void scaleLargeTimestampsInPlace(long[] timestamps, long multiplier, long divisor) {
        if (divisor >= multiplier && divisor % multiplier == 0) {
            long divisionFactor = divisor / multiplier;
            for (int i = 0; i < timestamps.length; i++) {
                timestamps[i] = timestamps[i] / divisionFactor;
            }
        } else if (divisor >= multiplier || multiplier % divisor != 0) {
            double multiplicationFactor = ((double) multiplier) / ((double) divisor);
            for (int i2 = 0; i2 < timestamps.length; i2++) {
                timestamps[i2] = (long) (((double) timestamps[i2]) * multiplicationFactor);
            }
        } else {
            long multiplicationFactor2 = multiplier / divisor;
            for (int i3 = 0; i3 < timestamps.length; i3++) {
                timestamps[i3] = timestamps[i3] * multiplicationFactor2;
            }
        }
    }

    public static int[] toArray(List<Integer> list) {
        if (list == null) {
            return null;
        }
        int length = list.size();
        int[] intArray = new int[length];
        for (int i = 0; i < length; i++) {
            intArray[i] = list.get(i).intValue();
        }
        return intArray;
    }

    public static void maybeTerminateInputStream(HttpURLConnection connection, long bytesRemaining) {
        if (SDK_INT == 19 || SDK_INT == 20) {
            try {
                InputStream inputStream = connection.getInputStream();
                if (bytesRemaining == -1) {
                    if (inputStream.read() == -1) {
                        return;
                    }
                } else if (bytesRemaining <= 2048) {
                    return;
                }
                String className = inputStream.getClass().getName();
                if (className.equals("com.android.okhttp.internal.http.HttpTransport$ChunkedInputStream") || className.equals("com.android.okhttp.internal.http.HttpTransport$FixedLengthInputStream")) {
                    Method unexpectedEndOfInput = inputStream.getClass().getSuperclass().getDeclaredMethod("unexpectedEndOfInput", new Class[0]);
                    unexpectedEndOfInput.setAccessible(true);
                    unexpectedEndOfInput.invoke(inputStream, new Object[0]);
                }
            } catch (IOException | Exception e) {
            }
        }
    }

    public static DataSpec getRemainderDataSpec(DataSpec dataSpec, int bytesLoaded) {
        long remainingLength = -1;
        if (bytesLoaded == 0) {
            return dataSpec;
        }
        if (dataSpec.length != -1) {
            remainingLength = dataSpec.length - ((long) bytesLoaded);
        }
        return new DataSpec(dataSpec.uri, dataSpec.position + ((long) bytesLoaded), remainingLength, dataSpec.key, dataSpec.flags);
    }

    public static int getIntegerCodeForString(String string) {
        int length = string.length();
        Assertions.checkArgument(length <= 4);
        int result = 0;
        for (int i = 0; i < length; i++) {
            result = (result << 8) | string.charAt(i);
        }
        return result;
    }

    public static int getTopInt(long value) {
        return (int) (value >>> 32);
    }

    public static int getBottomInt(long value) {
        return (int) value;
    }

    public static long getLong(int topInteger, int bottomInteger) {
        return (((long) topInteger) << 32) | (((long) bottomInteger) & 4294967295L);
    }

    public static String getHexStringFromBytes(byte[] data, int beginIndex, int endIndex) {
        StringBuilder dataStringBuilder = new StringBuilder(endIndex - beginIndex);
        for (int i = beginIndex; i < endIndex; i++) {
            dataStringBuilder.append(String.format(Locale.US, "%02X", new Object[]{Byte.valueOf(data[i])}));
        }
        return dataStringBuilder.toString();
    }

    public static byte[] getBytesFromHexString(String hexString) {
        byte[] data = new byte[(hexString.length() / 2)];
        for (int i = 0; i < data.length; i++) {
            int stringOffset = i * 2;
            data[i] = (byte) ((Character.digit(hexString.charAt(stringOffset), 16) << 4) + Character.digit(hexString.charAt(stringOffset + 1), 16));
        }
        return data;
    }

    public static <T> String getCommaDelimitedSimpleClassNames(T[] objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            stringBuilder.append(objects[i].getClass().getSimpleName());
            if (i < objects.length - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static String getUserAgent(Context context, String applicationName) {
        String versionName;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "?";
        }
        return applicationName + "/" + versionName + " (Linux;Android " + Build.VERSION.RELEASE + ") ExoPlayerLib/" + ExoPlayerLibraryInfo.VERSION;
    }

    /* JADX WARNING: type inference failed for: r5v2, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] executePost(java.lang.String r8, byte[] r9, java.util.Map<java.lang.String, java.lang.String> r10) throws java.io.IOException {
        /*
            r6 = 1
            r4 = 0
            java.net.URL r5 = new java.net.URL     // Catch:{ all -> 0x0045 }
            r5.<init>(r8)     // Catch:{ all -> 0x0045 }
            java.net.URLConnection r5 = r5.openConnection()     // Catch:{ all -> 0x0045 }
            r0 = r5
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ all -> 0x0045 }
            r4 = r0
            java.lang.String r5 = "POST"
            r4.setRequestMethod(r5)     // Catch:{ all -> 0x0045 }
            if (r9 == 0) goto L_0x004c
            r5 = r6
        L_0x0018:
            r4.setDoOutput(r5)     // Catch:{ all -> 0x0045 }
            r5 = 1
            r4.setDoInput(r5)     // Catch:{ all -> 0x0045 }
            if (r10 == 0) goto L_0x004e
            java.util.Set r5 = r10.entrySet()     // Catch:{ all -> 0x0045 }
            java.util.Iterator r7 = r5.iterator()     // Catch:{ all -> 0x0045 }
        L_0x0029:
            boolean r5 = r7.hasNext()     // Catch:{ all -> 0x0045 }
            if (r5 == 0) goto L_0x004e
            java.lang.Object r3 = r7.next()     // Catch:{ all -> 0x0045 }
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ all -> 0x0045 }
            java.lang.Object r5 = r3.getKey()     // Catch:{ all -> 0x0045 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ all -> 0x0045 }
            java.lang.Object r6 = r3.getValue()     // Catch:{ all -> 0x0045 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ all -> 0x0045 }
            r4.setRequestProperty(r5, r6)     // Catch:{ all -> 0x0045 }
            goto L_0x0029
        L_0x0045:
            r5 = move-exception
            if (r4 == 0) goto L_0x004b
            r4.disconnect()
        L_0x004b:
            throw r5
        L_0x004c:
            r5 = 0
            goto L_0x0018
        L_0x004e:
            if (r9 == 0) goto L_0x005a
            java.io.OutputStream r2 = r4.getOutputStream()     // Catch:{ all -> 0x0045 }
            r2.write(r9)     // Catch:{ all -> 0x006b }
            r2.close()     // Catch:{ all -> 0x0045 }
        L_0x005a:
            java.io.InputStream r1 = r4.getInputStream()     // Catch:{ all -> 0x0045 }
            byte[] r5 = toByteArray(r1)     // Catch:{ all -> 0x0070 }
            r1.close()     // Catch:{ all -> 0x0045 }
            if (r4 == 0) goto L_0x006a
            r4.disconnect()
        L_0x006a:
            return r5
        L_0x006b:
            r5 = move-exception
            r2.close()     // Catch:{ all -> 0x0045 }
            throw r5     // Catch:{ all -> 0x0045 }
        L_0x0070:
            r5 = move-exception
            r1.close()     // Catch:{ all -> 0x0045 }
            throw r5     // Catch:{ all -> 0x0045 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.util.Util.executePost(java.lang.String, byte[], java.util.Map):byte[]");
    }

    public static int getPcmEncoding(int bitDepth) {
        switch (bitDepth) {
            case 8:
                return 3;
            case 16:
                return 2;
            case 24:
                return Integer.MIN_VALUE;
            case 32:
                return C1907C.ENCODING_PCM_32BIT;
            default:
                return 0;
        }
    }

    public static int inferContentType(String fileName) {
        if (fileName == null) {
            return 3;
        }
        if (fileName.endsWith(".mpd")) {
            return 0;
        }
        if (fileName.endsWith(".ism")) {
            return 1;
        }
        if (fileName.endsWith(".m3u8")) {
            return 2;
        }
        return 3;
    }

    public static String escapeFileName(String fileName) {
        int length = fileName.length();
        int charactersToEscapeCount = 0;
        for (int i = 0; i < length; i++) {
            if (shouldEscapeCharacter(fileName.charAt(i))) {
                charactersToEscapeCount++;
            }
        }
        if (charactersToEscapeCount == 0) {
            return fileName;
        }
        StringBuilder builder = new StringBuilder((charactersToEscapeCount * 2) + length);
        int i2 = 0;
        while (charactersToEscapeCount > 0) {
            int i3 = i2 + 1;
            char c = fileName.charAt(i2);
            if (shouldEscapeCharacter(c)) {
                builder.append('%').append(Integer.toHexString(c));
                charactersToEscapeCount--;
            } else {
                builder.append(c);
            }
            i2 = i3;
        }
        if (i2 < length) {
            builder.append(fileName, i2, length);
        }
        int i4 = i2;
        return builder.toString();
    }

    private static boolean shouldEscapeCharacter(char c) {
        switch (c) {
            case '\"':
            case '%':
            case '*':
            case '/':
            case ':':
            case '<':
            case '>':
            case '?':
            case '\\':
            case '|':
                return true;
            default:
                return false;
        }
    }

    public static String unescapeFileName(String fileName) {
        int length = fileName.length();
        int percentCharacterCount = 0;
        for (int i = 0; i < length; i++) {
            if (fileName.charAt(i) == '%') {
                percentCharacterCount++;
            }
        }
        if (percentCharacterCount == 0) {
            return fileName;
        }
        int expectedLength = length - (percentCharacterCount * 2);
        StringBuilder builder = new StringBuilder(expectedLength);
        Matcher matcher = ESCAPED_CHARACTER_PATTERN.matcher(fileName);
        int endOfLastMatch = 0;
        while (percentCharacterCount > 0 && matcher.find()) {
            builder.append(fileName, endOfLastMatch, matcher.start()).append((char) Integer.parseInt(matcher.group(1), 16));
            endOfLastMatch = matcher.end();
            percentCharacterCount--;
        }
        if (endOfLastMatch < length) {
            builder.append(fileName, endOfLastMatch, length);
        }
        if (builder.length() != expectedLength) {
            return null;
        }
        return builder.toString();
    }

    public static int crc(byte[] bytes, int start, int end, int initialValue) {
        for (int i = start; i < end; i++) {
            initialValue = (initialValue << 8) ^ CRC32_BYTES_MSBF[((initialValue >>> 24) ^ (bytes[i] & 255)) & 255];
        }
        return initialValue;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v26, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Point getPhysicalDisplaySize(android.content.Context r15) {
        /*
            int r11 = SDK_INT
            r12 = 25
            if (r11 >= r12) goto L_0x00c1
            java.lang.String r11 = "Sony"
            java.lang.String r12 = MANUFACTURER
            boolean r11 = r11.equals(r12)
            if (r11 == 0) goto L_0x0037
            java.lang.String r11 = MODEL
            if (r11 == 0) goto L_0x0037
            java.lang.String r11 = MODEL
            java.lang.String r12 = "BRAVIA"
            boolean r11 = r11.startsWith(r12)
            if (r11 == 0) goto L_0x0037
            android.content.pm.PackageManager r11 = r15.getPackageManager()
            java.lang.String r12 = "com.sony.dtv.hardware.panel.qfhd"
            boolean r11 = r11.hasSystemFeature(r12)
            if (r11 == 0) goto L_0x0037
            android.graphics.Point r2 = new android.graphics.Point
            r11 = 3840(0xf00, float:5.381E-42)
            r12 = 2160(0x870, float:3.027E-42)
            r2.<init>(r11, r12)
        L_0x0036:
            return r2
        L_0x0037:
            java.lang.String r11 = "NVIDIA"
            java.lang.String r12 = MANUFACTURER
            boolean r11 = r11.equals(r12)
            if (r11 == 0) goto L_0x00c1
            java.lang.String r11 = MODEL
            if (r11 == 0) goto L_0x00c1
            java.lang.String r11 = MODEL
            java.lang.String r12 = "SHIELD"
            boolean r11 = r11.contains(r12)
            if (r11 == 0) goto L_0x00c1
            r6 = 0
            java.lang.String r11 = "android.os.SystemProperties"
            java.lang.Class r8 = java.lang.Class.forName(r11)     // Catch:{ Exception -> 0x00de }
            java.lang.String r11 = "get"
            r12 = 1
            java.lang.Class[] r12 = new java.lang.Class[r12]     // Catch:{ Exception -> 0x00de }
            r13 = 0
            java.lang.Class<java.lang.String> r14 = java.lang.String.class
            r12[r13] = r14     // Catch:{ Exception -> 0x00de }
            java.lang.reflect.Method r4 = r8.getMethod(r11, r12)     // Catch:{ Exception -> 0x00de }
            r11 = 1
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ Exception -> 0x00de }
            r12 = 0
            java.lang.String r13 = "sys.display-size"
            r11[r12] = r13     // Catch:{ Exception -> 0x00de }
            java.lang.Object r11 = r4.invoke(r8, r11)     // Catch:{ Exception -> 0x00de }
            r0 = r11
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x00de }
            r6 = r0
        L_0x0079:
            boolean r11 = android.text.TextUtils.isEmpty(r6)
            if (r11 != 0) goto L_0x00c1
            java.lang.String r11 = r6.trim()     // Catch:{ NumberFormatException -> 0x00a6 }
            java.lang.String r12 = "x"
            java.lang.String[] r7 = r11.split(r12)     // Catch:{ NumberFormatException -> 0x00a6 }
            int r11 = r7.length     // Catch:{ NumberFormatException -> 0x00a6 }
            r12 = 2
            if (r11 != r12) goto L_0x00a7
            r11 = 0
            r11 = r7[r11]     // Catch:{ NumberFormatException -> 0x00a6 }
            int r9 = java.lang.Integer.parseInt(r11)     // Catch:{ NumberFormatException -> 0x00a6 }
            r11 = 1
            r11 = r7[r11]     // Catch:{ NumberFormatException -> 0x00a6 }
            int r5 = java.lang.Integer.parseInt(r11)     // Catch:{ NumberFormatException -> 0x00a6 }
            if (r9 <= 0) goto L_0x00a7
            if (r5 <= 0) goto L_0x00a7
            android.graphics.Point r2 = new android.graphics.Point     // Catch:{ NumberFormatException -> 0x00a6 }
            r2.<init>(r9, r5)     // Catch:{ NumberFormatException -> 0x00a6 }
            goto L_0x0036
        L_0x00a6:
            r11 = move-exception
        L_0x00a7:
            java.lang.String r11 = "Util"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "Invalid sys.display-size: "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.StringBuilder r12 = r12.append(r6)
            java.lang.String r12 = r12.toString()
            android.util.Log.e(r11, r12)
        L_0x00c1:
            java.lang.String r11 = "window"
            java.lang.Object r10 = r15.getSystemService(r11)
            android.view.WindowManager r10 = (android.view.WindowManager) r10
            android.view.Display r1 = r10.getDefaultDisplay()
            android.graphics.Point r2 = new android.graphics.Point
            r2.<init>()
            int r11 = SDK_INT
            r12 = 23
            if (r11 < r12) goto L_0x00e9
            getDisplaySizeV23(r1, r2)
            goto L_0x0036
        L_0x00de:
            r3 = move-exception
            java.lang.String r11 = "Util"
            java.lang.String r12 = "Failed to read sys.display-size"
            android.util.Log.e(r11, r12, r3)
            goto L_0x0079
        L_0x00e9:
            int r11 = SDK_INT
            r12 = 17
            if (r11 < r12) goto L_0x00f4
            getDisplaySizeV17(r1, r2)
            goto L_0x0036
        L_0x00f4:
            int r11 = SDK_INT
            r12 = 16
            if (r11 < r12) goto L_0x00ff
            getDisplaySizeV16(r1, r2)
            goto L_0x0036
        L_0x00ff:
            getDisplaySizeV9(r1, r2)
            goto L_0x0036
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.util.Util.getPhysicalDisplaySize(android.content.Context):android.graphics.Point");
    }

    @TargetApi(23)
    private static void getDisplaySizeV23(Display display, Point outSize) {
        Display.Mode mode = display.getMode();
        outSize.x = mode.getPhysicalWidth();
        outSize.y = mode.getPhysicalHeight();
    }

    @TargetApi(17)
    private static void getDisplaySizeV17(Display display, Point outSize) {
        display.getRealSize(outSize);
    }

    @TargetApi(16)
    private static void getDisplaySizeV16(Display display, Point outSize) {
        display.getSize(outSize);
    }

    private static void getDisplaySizeV9(Display display, Point outSize) {
        outSize.x = display.getWidth();
        outSize.y = display.getHeight();
    }
}
