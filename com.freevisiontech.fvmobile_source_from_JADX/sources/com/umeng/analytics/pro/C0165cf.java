package com.umeng.analytics.pro;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/* renamed from: com.umeng.analytics.pro.cf */
/* compiled from: TBaseHelper */
public final class C0165cf {

    /* renamed from: a */
    private static final Comparator f576a = new C0167a();

    private C0165cf() {
    }

    /* renamed from: a */
    public static int m950a(Object obj, Object obj2) {
        if (obj instanceof Comparable) {
            return m949a((Comparable) obj, (Comparable) obj2);
        }
        if (obj instanceof List) {
            return m953a((List) obj, (List) obj2);
        }
        if (obj instanceof Set) {
            return m955a((Set) obj, (Set) obj2);
        }
        if (obj instanceof Map) {
            return m954a((Map) obj, (Map) obj2);
        }
        if (obj instanceof byte[]) {
            return m958a((byte[]) obj, (byte[]) obj2);
        }
        throw new IllegalArgumentException("Cannot compare objects of type " + obj.getClass());
    }

    /* renamed from: a */
    public static int m957a(boolean z, boolean z2) {
        return Boolean.valueOf(z).compareTo(Boolean.valueOf(z2));
    }

    /* renamed from: a */
    public static int m945a(byte b, byte b2) {
        if (b < b2) {
            return -1;
        }
        if (b2 < b) {
            return 1;
        }
        return 0;
    }

    /* renamed from: a */
    public static int m956a(short s, short s2) {
        if (s < s2) {
            return -1;
        }
        if (s2 < s) {
            return 1;
        }
        return 0;
    }

    /* renamed from: a */
    public static int m947a(int i, int i2) {
        if (i < i2) {
            return -1;
        }
        if (i2 < i) {
            return 1;
        }
        return 0;
    }

    /* renamed from: a */
    public static int m948a(long j, long j2) {
        if (j < j2) {
            return -1;
        }
        if (j2 < j) {
            return 1;
        }
        return 0;
    }

    /* renamed from: a */
    public static int m946a(double d, double d2) {
        if (d < d2) {
            return -1;
        }
        if (d2 < d) {
            return 1;
        }
        return 0;
    }

    /* renamed from: a */
    public static int m951a(String str, String str2) {
        return str.compareTo(str2);
    }

    /* renamed from: a */
    public static int m958a(byte[] bArr, byte[] bArr2) {
        int a = m947a(bArr.length, bArr2.length);
        if (a != 0) {
            return a;
        }
        for (int i = 0; i < bArr.length; i++) {
            int a2 = m945a(bArr[i], bArr2[i]);
            if (a2 != 0) {
                return a2;
            }
        }
        return 0;
    }

    /* renamed from: a */
    public static int m949a(Comparable comparable, Comparable comparable2) {
        return comparable.compareTo(comparable2);
    }

    /* renamed from: a */
    public static int m953a(List list, List list2) {
        int a = m947a(list.size(), list2.size());
        if (a != 0) {
            return a;
        }
        for (int i = 0; i < list.size(); i++) {
            int compare = f576a.compare(list.get(i), list2.get(i));
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    /* renamed from: a */
    public static int m955a(Set set, Set set2) {
        int a = m947a(set.size(), set2.size());
        if (a != 0) {
            return a;
        }
        TreeSet treeSet = new TreeSet(f576a);
        treeSet.addAll(set);
        TreeSet treeSet2 = new TreeSet(f576a);
        treeSet2.addAll(set2);
        Iterator it = treeSet.iterator();
        Iterator it2 = treeSet2.iterator();
        while (it.hasNext() && it2.hasNext()) {
            int compare = f576a.compare(it.next(), it2.next());
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    /* renamed from: a */
    public static int m954a(Map map, Map map2) {
        int a = m947a(map.size(), map2.size());
        if (a != 0) {
            return a;
        }
        TreeMap treeMap = new TreeMap(f576a);
        treeMap.putAll(map);
        Iterator it = treeMap.entrySet().iterator();
        TreeMap treeMap2 = new TreeMap(f576a);
        treeMap2.putAll(map2);
        Iterator it2 = treeMap2.entrySet().iterator();
        while (it.hasNext() && it2.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Map.Entry entry2 = (Map.Entry) it2.next();
            int compare = f576a.compare(entry.getKey(), entry2.getKey());
            if (compare != 0) {
                return compare;
            }
            int compare2 = f576a.compare(entry.getValue(), entry2.getValue());
            if (compare2 != 0) {
                return compare2;
            }
        }
        return 0;
    }

    /* renamed from: com.umeng.analytics.pro.cf$a */
    /* compiled from: TBaseHelper */
    private static class C0167a implements Comparator {
        private C0167a() {
        }

        public int compare(Object obj, Object obj2) {
            if (obj == null && obj2 == null) {
                return 0;
            }
            if (obj == null) {
                return -1;
            }
            if (obj2 == null) {
                return 1;
            }
            if (obj instanceof List) {
                return C0165cf.m953a((List) obj, (List) obj2);
            }
            if (obj instanceof Set) {
                return C0165cf.m955a((Set) obj, (Set) obj2);
            }
            if (obj instanceof Map) {
                return C0165cf.m954a((Map) obj, (Map) obj2);
            }
            if (obj instanceof byte[]) {
                return C0165cf.m958a((byte[]) obj, (byte[]) obj2);
            }
            return C0165cf.m949a((Comparable) obj, (Comparable) obj2);
        }
    }

    /* renamed from: a */
    public static void m960a(ByteBuffer byteBuffer, StringBuilder sb) {
        int i;
        byte[] array = byteBuffer.array();
        int arrayOffset = byteBuffer.arrayOffset();
        int position = arrayOffset + byteBuffer.position();
        int limit = byteBuffer.limit() + arrayOffset;
        if (limit - position > 128) {
            i = position + 128;
        } else {
            i = limit;
        }
        for (int i2 = position; i2 < i; i2++) {
            if (i2 > position) {
                sb.append(" ");
            }
            sb.append(m959a(array[i2]));
        }
        if (limit != i) {
            sb.append("...");
        }
    }

    /* renamed from: a */
    public static String m959a(byte b) {
        return Integer.toHexString((b | C0217dk.f711a) & 511).toUpperCase().substring(1);
    }

    /* renamed from: a */
    public static byte[] m961a(ByteBuffer byteBuffer) {
        if (m963b(byteBuffer)) {
            return byteBuffer.array();
        }
        byte[] bArr = new byte[byteBuffer.remaining()];
        m952a(byteBuffer, bArr, 0);
        return bArr;
    }

    /* renamed from: b */
    public static boolean m963b(ByteBuffer byteBuffer) {
        return byteBuffer.hasArray() && byteBuffer.position() == 0 && byteBuffer.arrayOffset() == 0 && byteBuffer.remaining() == byteBuffer.capacity();
    }

    /* renamed from: a */
    public static int m952a(ByteBuffer byteBuffer, byte[] bArr, int i) {
        int remaining = byteBuffer.remaining();
        System.arraycopy(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), bArr, i, remaining);
        return remaining;
    }

    /* renamed from: c */
    public static ByteBuffer m964c(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return null;
        }
        return !m963b(byteBuffer) ? ByteBuffer.wrap(m961a(byteBuffer)) : byteBuffer;
    }

    /* renamed from: d */
    public static ByteBuffer m965d(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return null;
        }
        ByteBuffer wrap = ByteBuffer.wrap(new byte[byteBuffer.remaining()]);
        if (byteBuffer.hasArray()) {
            System.arraycopy(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), wrap.array(), 0, byteBuffer.remaining());
            return wrap;
        }
        byteBuffer.slice().get(wrap.array());
        return wrap;
    }

    /* renamed from: a */
    public static byte[] m962a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }
}
