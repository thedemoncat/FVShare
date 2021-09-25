package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.LazyList;
import com.googlecode.mp4parser.util.Logger;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BasicContainer implements Container, Iterator<Box>, Closeable {
    private static final Box EOF = new AbstractBox("eof ") {
        /* access modifiers changed from: protected */
        public long getContentSize() {
            return 0;
        }

        /* access modifiers changed from: protected */
        public void getContent(ByteBuffer byteBuffer) {
        }

        /* access modifiers changed from: protected */
        public void _parseDetails(ByteBuffer content) {
        }
    };
    private static Logger LOG = Logger.getLogger(BasicContainer.class);
    protected BoxParser boxParser;
    private List<Box> boxes = new ArrayList();
    protected DataSource dataSource;
    long endPosition = 0;
    Box lookahead = null;
    long parsePosition = 0;
    long startPosition = 0;

    public List<Box> getBoxes() {
        if (this.dataSource == null || this.lookahead == EOF) {
            return this.boxes;
        }
        return new LazyList(this.boxes, this);
    }

    public void setBoxes(List<Box> boxes2) {
        this.boxes = new ArrayList(boxes2);
        this.lookahead = EOF;
        this.dataSource = null;
    }

    /* access modifiers changed from: protected */
    public long getContainerSize() {
        long contentSize = 0;
        for (int i = 0; i < getBoxes().size(); i++) {
            contentSize += this.boxes.get(i).getSize();
        }
        return contentSize;
    }

    public <T extends Box> List<T> getBoxes(Class<T> clazz) {
        List<T> boxesToBeReturned = null;
        T oneBox = null;
        List<Box> boxes2 = getBoxes();
        for (int i = 0; i < boxes2.size(); i++) {
            T boxe = (Box) boxes2.get(i);
            if (clazz.isInstance(boxe)) {
                if (oneBox == null) {
                    oneBox = boxe;
                } else {
                    if (boxesToBeReturned == null) {
                        boxesToBeReturned = new ArrayList<>(2);
                        boxesToBeReturned.add(oneBox);
                    }
                    boxesToBeReturned.add(boxe);
                }
            }
        }
        if (boxesToBeReturned != null) {
            return boxesToBeReturned;
        }
        if (oneBox != null) {
            return Collections.singletonList(oneBox);
        }
        return Collections.emptyList();
    }

    public <T extends Box> List<T> getBoxes(Class<T> clazz, boolean recursive) {
        List<T> boxesToBeReturned = new ArrayList<>(2);
        List<Box> boxes2 = getBoxes();
        for (int i = 0; i < boxes2.size(); i++) {
            Box boxe = boxes2.get(i);
            if (clazz.isInstance(boxe)) {
                boxesToBeReturned.add(boxe);
            }
            if (recursive && (boxe instanceof Container)) {
                boxesToBeReturned.addAll(((Container) boxe).getBoxes(clazz, recursive));
            }
        }
        return boxesToBeReturned;
    }

    public void addBox(Box box) {
        if (box != null) {
            this.boxes = new ArrayList(getBoxes());
            box.setParent(this);
            this.boxes.add(box);
        }
    }

    public void initContainer(DataSource dataSource2, long containerSize, BoxParser boxParser2) throws IOException {
        this.dataSource = dataSource2;
        long position = dataSource2.position();
        this.startPosition = position;
        this.parsePosition = position;
        dataSource2.position(dataSource2.position() + containerSize);
        this.endPosition = dataSource2.position();
        this.boxParser = boxParser2;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        if (this.lookahead == EOF) {
            return false;
        }
        if (this.lookahead != null) {
            return true;
        }
        try {
            this.lookahead = next();
            return true;
        } catch (NoSuchElementException e) {
            this.lookahead = EOF;
            return false;
        }
    }

    public Box next() {
        Box b;
        if (this.lookahead != null && this.lookahead != EOF) {
            Box b2 = this.lookahead;
            this.lookahead = null;
            return b2;
        } else if (this.dataSource == null || this.parsePosition >= this.endPosition) {
            this.lookahead = EOF;
            throw new NoSuchElementException();
        } else {
            try {
                synchronized (this.dataSource) {
                    this.dataSource.position(this.parsePosition);
                    b = this.boxParser.parseBox(this.dataSource, this);
                    this.parsePosition = this.dataSource.position();
                }
                return b;
            } catch (EOFException e) {
                throw new NoSuchElementException();
            } catch (IOException e2) {
                throw new NoSuchElementException();
            }
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getClass().getSimpleName()).append("[");
        for (int i = 0; i < this.boxes.size(); i++) {
            if (i > 0) {
                buffer.append(";");
            }
            buffer.append(this.boxes.get(i).toString());
        }
        buffer.append("]");
        return buffer.toString();
    }

    public final void writeContainer(WritableByteChannel bb) throws IOException {
        for (Box box : getBoxes()) {
            box.getBox(bb);
        }
    }

    public ByteBuffer getByteBuffer(long rangeStart, long size) throws IOException {
        ByteBuffer map;
        if (this.dataSource != null) {
            synchronized (this.dataSource) {
                map = this.dataSource.map(this.startPosition + rangeStart, size);
            }
            return map;
        }
        ByteBuffer out = ByteBuffer.allocate(CastUtils.l2i(size));
        long rangeEnd = rangeStart + size;
        long boxEnd = 0;
        for (Box box : this.boxes) {
            long boxStart = boxEnd;
            boxEnd = boxStart + box.getSize();
            if (boxEnd > rangeStart && boxStart < rangeEnd) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                WritableByteChannel wbc = Channels.newChannel(baos);
                box.getBox(wbc);
                wbc.close();
                if (boxStart >= rangeStart && boxEnd <= rangeEnd) {
                    out.put(baos.toByteArray());
                } else if (boxStart < rangeStart && boxEnd > rangeEnd) {
                    int length = CastUtils.l2i((box.getSize() - (rangeStart - boxStart)) - (boxEnd - rangeEnd));
                    out.put(baos.toByteArray(), CastUtils.l2i(rangeStart - boxStart), length);
                } else if (boxStart < rangeStart && boxEnd <= rangeEnd) {
                    int length2 = CastUtils.l2i(box.getSize() - (rangeStart - boxStart));
                    out.put(baos.toByteArray(), CastUtils.l2i(rangeStart - boxStart), length2);
                } else if (boxStart >= rangeStart && boxEnd > rangeEnd) {
                    out.put(baos.toByteArray(), 0, CastUtils.l2i(box.getSize() - (boxEnd - rangeEnd)));
                }
            }
        }
        return (ByteBuffer) out.rewind();
    }

    public void close() throws IOException {
        this.dataSource.close();
    }
}
