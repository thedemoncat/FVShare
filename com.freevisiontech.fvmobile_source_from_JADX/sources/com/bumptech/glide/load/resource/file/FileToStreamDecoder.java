package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileToStreamDecoder<T> implements ResourceDecoder<File, T> {
    private static final FileOpener DEFAULT_FILE_OPENER = new FileOpener();
    private final FileOpener fileOpener;
    private ResourceDecoder<InputStream, T> streamDecoder;

    public FileToStreamDecoder(ResourceDecoder<InputStream, T> streamDecoder2) {
        this(streamDecoder2, DEFAULT_FILE_OPENER);
    }

    FileToStreamDecoder(ResourceDecoder<InputStream, T> streamDecoder2, FileOpener fileOpener2) {
        this.streamDecoder = streamDecoder2;
        this.fileOpener = fileOpener2;
    }

    public Resource<T> decode(File source, int width, int height) throws IOException {
        InputStream is = null;
        try {
            is = this.fileOpener.open(source);
            return this.streamDecoder.decode(is, width, height);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public String getId() {
        return "";
    }

    static class FileOpener {
        FileOpener() {
        }

        public InputStream open(File file) throws FileNotFoundException {
            return new FileInputStream(file);
        }
    }
}
