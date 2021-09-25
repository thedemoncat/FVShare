package android.support.p003v7.widget.helper;

import android.graphics.Canvas;
import android.support.p001v4.view.ViewCompat;
import android.support.p003v7.recyclerview.C0780R;
import android.support.p003v7.widget.RecyclerView;
import android.view.View;

/* renamed from: android.support.v7.widget.helper.ItemTouchUIUtilImpl */
class ItemTouchUIUtilImpl {

    /* renamed from: android.support.v7.widget.helper.ItemTouchUIUtilImpl$Api21Impl */
    static class Api21Impl extends BaseImpl {
        Api21Impl() {
        }

        public void onDraw(Canvas c, RecyclerView recyclerView, View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (isCurrentlyActive && view.getTag(C0780R.C0782id.item_touch_helper_previous_elevation) == null) {
                Object originalElevation = Float.valueOf(ViewCompat.getElevation(view));
                ViewCompat.setElevation(view, 1.0f + findMaxElevation(recyclerView, view));
                view.setTag(C0780R.C0782id.item_touch_helper_previous_elevation, originalElevation);
            }
            super.onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);
        }

        private float findMaxElevation(RecyclerView recyclerView, View itemView) {
            int childCount = recyclerView.getChildCount();
            float max = 0.0f;
            for (int i = 0; i < childCount; i++) {
                View child = recyclerView.getChildAt(i);
                if (child != itemView) {
                    float elevation = ViewCompat.getElevation(child);
                    if (elevation > max) {
                        max = elevation;
                    }
                }
            }
            return max;
        }

        public void clearView(View view) {
            Object tag = view.getTag(C0780R.C0782id.item_touch_helper_previous_elevation);
            if (tag != null && (tag instanceof Float)) {
                ViewCompat.setElevation(view, ((Float) tag).floatValue());
            }
            view.setTag(C0780R.C0782id.item_touch_helper_previous_elevation, (Object) null);
            super.clearView(view);
        }
    }

    ItemTouchUIUtilImpl() {
    }

    /* renamed from: android.support.v7.widget.helper.ItemTouchUIUtilImpl$BaseImpl */
    static class BaseImpl implements ItemTouchUIUtil {
        BaseImpl() {
        }

        public void clearView(View view) {
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
        }

        public void onSelected(View view) {
        }

        public void onDraw(Canvas c, RecyclerView recyclerView, View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            view.setTranslationX(dX);
            view.setTranslationY(dY);
        }

        public void onDrawOver(Canvas c, RecyclerView recyclerView, View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        }
    }
}
