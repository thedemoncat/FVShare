package com.github.barteksc.pdfviewer.source;

import android.content.Context;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import java.io.IOException;

public class ByteArraySource implements DocumentSource {
    private byte[] data;

    public ByteArraySource(byte[] data2) {
        this.data = data2;
    }

    public PdfDocument createDocument(Context context, PdfiumCore core, String password) throws IOException {
        return core.newDocument(this.data, password);
    }
}
