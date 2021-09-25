package com.googlecode.mp4parser;

import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class FileDataSourceViaHeapImpl implements DataSource {
    private static Logger LOG = Logger.getLogger(FileDataSourceViaHeapImpl.class);

    /* renamed from: fc */
    FileChannel f1023fc;
    String filename;

    public FileDataSourceViaHeapImpl(File f) throws FileNotFoundException {
        this.f1023fc = new FileInputStream(f).getChannel();
        this.filename = f.getName();
    }

    public FileDataSourceViaHeapImpl(String f) throws FileNotFoundException {
        File file = new File(f);
        this.f1023fc = new FileInputStream(file).getChannel();
        this.filename = file.getName();
    }

    public FileDataSourceViaHeapImpl(FileChannel fc) {
        this.f1023fc = fc;
        this.filename = "unknown";
    }

    public FileDataSourceViaHeapImpl(FileChannel fc, String filename2) {
        this.f1023fc = fc;
        this.filename = filename2;
    }

    public synchronized int read(ByteBuffer byteBuffer) throws IOException {
        return this.f1023fc.read(byteBuffer);
    }

    public synchronized long size() throws IOException {
        return this.f1023fc.size();
    }

    public synchronized long position() throws IOException {
        return this.f1023fc.position();
    }

    public synchronized void position(long nuPos) throws IOException {
        this.f1023fc.position(nuPos);
    }

    public synchronized long transferTo(long startPosition, long count, WritableByteChannel sink) throws IOException {
        return this.f1023fc.transferTo(startPosition, count, sink);
    }

    public synchronized ByteBuffer map(long startPosition, long size) throws IOException {
        ByteBuffer bb;
        bb = ByteBuffer.allocate(CastUtils.l2i(size));
        this.f1023fc.read(bb, startPosition);
        return (ByteBuffer) bb.rewind();
    }

    public void close() throws IOException {
        this.f1023fc.close();
    }

    public String toString() {
        return this.filename;
    }
}
