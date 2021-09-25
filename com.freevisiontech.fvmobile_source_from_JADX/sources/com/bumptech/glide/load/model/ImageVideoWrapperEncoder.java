package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Encoder;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageVideoWrapperEncoder implements Encoder<ImageVideoWrapper> {
    private final Encoder<ParcelFileDescriptor> fileDescriptorEncoder;

    /* renamed from: id */
    private String f1184id;
    private final Encoder<InputStream> streamEncoder;

    public ImageVideoWrapperEncoder(Encoder<InputStream> streamEncoder2, Encoder<ParcelFileDescriptor> fileDescriptorEncoder2) {
        this.streamEncoder = streamEncoder2;
        this.fileDescriptorEncoder = fileDescriptorEncoder2;
    }

    public boolean encode(ImageVideoWrapper data, OutputStream os) {
        if (data.getStream() != null) {
            return this.streamEncoder.encode(data.getStream(), os);
        }
        return this.fileDescriptorEncoder.encode(data.getFileDescriptor(), os);
    }

    public String getId() {
        if (this.f1184id == null) {
            this.f1184id = this.streamEncoder.getId() + this.fileDescriptorEncoder.getId();
        }
        return this.f1184id;
    }
}
