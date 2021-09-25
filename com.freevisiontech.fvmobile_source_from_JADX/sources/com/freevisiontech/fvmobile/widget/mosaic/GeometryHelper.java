package com.freevisiontech.fvmobile.widget.mosaic;

import android.graphics.Rect;

public class GeometryHelper {
    public static int PointAtLineLeftRight(Point start, Point end, Point test) {
        start.f1111x -= test.f1111x;
        start.f1112y -= test.f1112y;
        end.f1111x -= test.f1111x;
        end.f1112y -= test.f1112y;
        float ret = (start.f1111x * end.f1112y) - (start.f1112y * end.f1111x);
        if (ret == 0.0f) {
            return 0;
        }
        if (ret > 0.0f) {
            return 1;
        }
        if (ret < 0.0f) {
            return -1;
        }
        return 0;
    }

    public static Boolean IsTwoLineIntersect(Point start1, Point end1, Point start2, Point end2) {
        if (PointAtLineLeftRight(start2.clone(), end2.clone(), start1.clone()) * PointAtLineLeftRight(start2.clone(), end2.clone(), end1.clone()) > 0) {
            return false;
        }
        if (PointAtLineLeftRight(start1.clone(), end1.clone(), start2.clone()) * PointAtLineLeftRight(start1.clone(), end1.clone(), end2.clone()) > 0) {
            return false;
        }
        return true;
    }

    public static Boolean IsLineIntersectRect(Point start, Point end, Rect rect) {
        if (IsPointInRect(rect, start).booleanValue() || IsPointInRect(rect, end).booleanValue()) {
            return true;
        }
        if (IsTwoLineIntersect(start, end, new Point((float) rect.left, (float) rect.top), new Point((float) rect.left, (float) rect.bottom)).booleanValue()) {
            return true;
        }
        if (IsTwoLineIntersect(start, end, new Point((float) rect.left, (float) rect.bottom), new Point((float) rect.right, (float) rect.bottom)).booleanValue()) {
            return true;
        }
        if (IsTwoLineIntersect(start, end, new Point((float) rect.right, (float) rect.bottom), new Point((float) rect.right, (float) rect.top)).booleanValue()) {
            return true;
        }
        if (IsTwoLineIntersect(start, end, new Point((float) rect.left, (float) rect.top), new Point((float) rect.right, (float) rect.top)).booleanValue()) {
            return true;
        }
        return false;
    }

    private static Boolean IsPointInRect(Rect rect, Point test) {
        if (test.f1111x < ((float) rect.left) || test.f1111x > ((float) rect.right) || test.f1112y < ((float) rect.top) || test.f1112y > ((float) rect.bottom)) {
            return false;
        }
        return true;
    }
}
