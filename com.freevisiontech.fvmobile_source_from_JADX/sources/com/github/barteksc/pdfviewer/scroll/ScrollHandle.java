package com.github.barteksc.pdfviewer.scroll;

import com.github.barteksc.pdfviewer.PDFView;

public interface ScrollHandle {
    void destroyLayout();

    void hide();

    void hideDelayed();

    void setPageNum(int i);

    void setScroll(float f);

    void setupLayout(PDFView pDFView);

    void show();

    boolean shown();
}
