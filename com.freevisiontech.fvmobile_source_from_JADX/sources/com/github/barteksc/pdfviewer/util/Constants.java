package com.github.barteksc.pdfviewer.util;

public class Constants {
    public static boolean DEBUG_MODE = false;
    public static float PART_SIZE = 256.0f;
    public static int PRELOAD_COUNT = 7;
    public static float THUMBNAIL_RATIO = 0.3f;

    public static class Cache {
        public static int CACHE_SIZE = 120;
        public static int THUMBNAILS_CACHE_SIZE = 6;
    }

    public static class Pinch {
        public static float MAXIMUM_ZOOM = 10.0f;
        public static float MINIMUM_ZOOM = 1.0f;
    }
}
