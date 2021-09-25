package com.github.barteksc.pdfviewer.source;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import java.io.File;
import java.io.IOException;

public class FileSource implements DocumentSource {
    private File file;

    public FileSource(File file2) {
        this.file = file2;
    }

    public PdfDocument createDocument(Context context, PdfiumCore core, String password) throws IOException {
        return core.newDocument(ParcelFileDescriptor.open(this.file, 268435456), password);
    }
}
