package com.github.barteksc.pdfviewer.listener;

import android.graphics.Canvas;

public interface OnDrawListener {
    void onLayerDrawn(Canvas canvas, float f, float f2, int i);
}
