package com.bumptech.glide.load.model.file_descriptor;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.StringLoader;

public class FileDescriptorStringLoader extends StringLoader<ParcelFileDescriptor> implements FileDescriptorModelLoader<String> {

    public static class Factory implements ModelLoaderFactory<String, ParcelFileDescriptor> {
        public ModelLoader<String, ParcelFileDescriptor> build(Context context, GenericLoaderFactory factories) {
            return new FileDescriptorStringLoader((ModelLoader<Uri, ParcelFileDescriptor>) factories.buildModelLoader(Uri.class, ParcelFileDescriptor.class));
        }

        public void teardown() {
        }
    }

    public FileDescriptorStringLoader(Context context) {
        this(Glide.buildFileDescriptorModelLoader(Uri.class, context));
    }

    public FileDescriptorStringLoader(ModelLoader<Uri, ParcelFileDescriptor> uriLoader) {
        super(uriLoader);
    }
}
