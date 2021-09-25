package com.googlecode.mp4parser;

import com.googlecode.mp4parser.util.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class FileDataSourceImpl implements DataSource {
    private static Logger LOG = Logger.getLogger(FileDataSourceImpl.class);

    /* renamed from: fc */
    FileChannel f1022fc;
    String filename;

    public FileDataSourceImpl(File f) throws FileNotFoundException {
        this.f1022fc = new FileInputStream(f).getChannel();
        this.filename = f.getName();
    }

    public FileDataSourceImpl(String f) throws FileNotFoundException {
        File file = new File(f);
        this.f1022fc = new FileInputStream(file).getChannel();
        this.filename = file.getName();
    }

    public FileDataSourceImpl(FileChannel fc) {
        this.f1022fc = fc;
        this.filename = "unknown";
    }

    public FileDataSourceImpl(FileChannel fc, String filename2) {
        this.f1022fc = fc;
        this.filename = filename2;
    }

    public synchronized int read(ByteBuffer byteBuffer) throws IOException {
        return this.f1022fc.read(byteBuffer);
    }

    public synchronized long size() throws IOException {
        return this.f1022fc.size();
    }

    public synchronized long position() throws IOException {
        return this.f1022fc.position();
    }

    public synchronized void position(long nuPos) throws IOException {
        this.f1022fc.position(nuPos);
    }

    public synchronized long transferTo(long startPosition, long count, WritableByteChannel sink) throws IOException {
        return this.f1022fc.transferTo(startPosition, count, sink);
    }

    public synchronized ByteBuffer map(long startPosition, long size) throws IOException {
        LOG.logDebug(String.valueOf(startPosition) + " " + size);
        return this.f1022fc.map(FileChannel.MapMode.READ_ONLY, startPosition, size);
    }

    public void close() throws IOException {
        this.f1022fc.close();
    }

    public String toString() {
        return this.filename;
    }
}
