package com.vise.log;

import com.vise.log.config.LogConfig;
import com.vise.log.config.LogDefaultConfig;
import com.vise.log.inner.SoulsTree;
import com.vise.log.inner.Tree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViseLog {
    private static final List<Tree> FOREST = new ArrayList();
    private static final LogDefaultConfig LOG_DEFAULT_CONFIG = LogDefaultConfig.getInstance();
    private static final Tree TREE_OF_SOULS = new SoulsTree();

    private ViseLog() {
        throw new AssertionError("No instances.");
    }

    public static void wtf(String message, Object... args) {
        TREE_OF_SOULS.wtf(message, args);
    }

    public static void wtf(Object object) {
        TREE_OF_SOULS.wtf(object);
    }

    /* renamed from: e */
    public static void m1467e(String message, Object... args) {
        TREE_OF_SOULS.mo8869e(message, args);
    }

    /* renamed from: e */
    public static void m1466e(Object object) {
        TREE_OF_SOULS.mo8868e(object);
    }

    /* renamed from: w */
    public static void m1473w(String message, Object... args) {
        TREE_OF_SOULS.mo8876w(message, args);
    }

    /* renamed from: w */
    public static void m1472w(Object object) {
        TREE_OF_SOULS.mo8875w(object);
    }

    /* renamed from: d */
    public static void m1465d(String message, Object... args) {
        TREE_OF_SOULS.mo8867d(message, args);
    }

    /* renamed from: d */
    public static void m1464d(Object object) {
        TREE_OF_SOULS.mo8866d(object);
    }

    /* renamed from: i */
    public static void m1469i(String message, Object... args) {
        TREE_OF_SOULS.mo8871i(message, args);
    }

    /* renamed from: i */
    public static void m1468i(Object object) {
        TREE_OF_SOULS.mo8870i(object);
    }

    /* renamed from: v */
    public static void m1471v(String message, Object... args) {
        TREE_OF_SOULS.mo8874v(message, args);
    }

    /* renamed from: v */
    public static void m1470v(Object object) {
        TREE_OF_SOULS.mo8873v(object);
    }

    public static void json(String json) {
        TREE_OF_SOULS.json(json);
    }

    public static void xml(String xml) {
        TREE_OF_SOULS.xml(xml);
    }

    public static Tree asTree() {
        return TREE_OF_SOULS;
    }

    public static LogConfig getLogConfig() {
        return LOG_DEFAULT_CONFIG;
    }

    public static Tree setTag(String tag) {
        for (Tree tag2 : ((SoulsTree) TREE_OF_SOULS).getForestAsArray()) {
            tag2.setTag(tag);
        }
        return TREE_OF_SOULS;
    }

    public static void plant(Tree tree) {
        if (tree == null) {
            throw new NullPointerException("tree == null");
        } else if (tree == TREE_OF_SOULS) {
            throw new IllegalArgumentException("Cannot plant Timber into itself.");
        } else {
            synchronized (FOREST) {
                FOREST.add(tree);
                ((SoulsTree) TREE_OF_SOULS).setForestAsArray((Tree[]) FOREST.toArray(new Tree[FOREST.size()]));
            }
        }
    }

    public static void plant(Tree... trees) {
        if (trees == null) {
            throw new NullPointerException("trees == null");
        }
        int length = trees.length;
        int i = 0;
        while (i < length) {
            Tree tree = trees[i];
            if (tree == null) {
                throw new NullPointerException("trees contains null");
            } else if (tree == TREE_OF_SOULS) {
                throw new IllegalArgumentException("Cannot plant Timber into itself.");
            } else {
                i++;
            }
        }
        synchronized (FOREST) {
            Collections.addAll(FOREST, trees);
            ((SoulsTree) TREE_OF_SOULS).setForestAsArray((Tree[]) FOREST.toArray(new Tree[FOREST.size()]));
        }
    }

    public static void uproot(Tree tree) {
        synchronized (FOREST) {
            if (!FOREST.remove(tree)) {
                throw new IllegalArgumentException("Cannot uproot tree which is not planted: " + tree);
            }
            ((SoulsTree) TREE_OF_SOULS).setForestAsArray((Tree[]) FOREST.toArray(new Tree[FOREST.size()]));
        }
    }

    public static void uprootAll() {
        synchronized (FOREST) {
            FOREST.clear();
            ((SoulsTree) TREE_OF_SOULS).setForestAsArray(new Tree[0]);
        }
    }

    public static List<Tree> forest() {
        List<Tree> unmodifiableList;
        synchronized (FOREST) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(FOREST));
        }
        return unmodifiableList;
    }

    public static int treeCount() {
        int size;
        synchronized (FOREST) {
            size = FOREST.size();
        }
        return size;
    }
}
