package com.shockwave.pdfium;

import android.os.ParcelFileDescriptor;
import android.support.p001v4.util.ArrayMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfDocument {
    long mNativeDocPtr;
    final Map<Integer, Long> mNativePagesPtr = new ArrayMap();
    ParcelFileDescriptor parcelFileDescriptor;

    public static class Meta {
        String author;
        String creationDate;
        String creator;
        String keywords;
        String modDate;
        String producer;
        String subject;
        String title;

        public String getTitle() {
            return this.title;
        }

        public String getAuthor() {
            return this.author;
        }

        public String getSubject() {
            return this.subject;
        }

        public String getKeywords() {
            return this.keywords;
        }

        public String getCreator() {
            return this.creator;
        }

        public String getProducer() {
            return this.producer;
        }

        public String getCreationDate() {
            return this.creationDate;
        }

        public String getModDate() {
            return this.modDate;
        }
    }

    public static class Bookmark {
        private List<Bookmark> children = new ArrayList();
        long mNativePtr;
        long pageIdx;
        String title;

        public List<Bookmark> getChildren() {
            return this.children;
        }

        public boolean hasChildren() {
            return !this.children.isEmpty();
        }

        public String getTitle() {
            return this.title;
        }

        public long getPageIdx() {
            return this.pageIdx;
        }
    }

    PdfDocument() {
    }

    public boolean hasPage(int index) {
        return this.mNativePagesPtr.containsKey(Integer.valueOf(index));
    }
}
