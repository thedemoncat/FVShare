package com.github.barteksc.pdfviewer.source;

import android.content.Context;
import android.net.Uri;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import java.io.IOException;

public class UriSource implements DocumentSource {
    private Uri uri;

    public UriSource(Uri uri2) {
        this.uri = uri2;
    }

    public PdfDocument createDocument(Context context, PdfiumCore core, String password) throws IOException {
        return core.newDocument(context.getContentResolver().openFileDescriptor(this.uri, "r"), password);
    }
}
