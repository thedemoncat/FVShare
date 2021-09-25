package com.github.barteksc.pdfviewer.source;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.github.barteksc.pdfviewer.util.FileUtils;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import java.io.IOException;

public class AssetSource implements DocumentSource {
    private final String assetName;

    public AssetSource(String assetName2) {
        this.assetName = assetName2;
    }

    public PdfDocument createDocument(Context context, PdfiumCore core, String password) throws IOException {
        return core.newDocument(ParcelFileDescriptor.open(FileUtils.fileFromAsset(context, this.assetName), 268435456), password);
    }
}
