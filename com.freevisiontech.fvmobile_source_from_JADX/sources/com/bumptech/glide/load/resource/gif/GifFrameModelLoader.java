package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.Priority;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;

class GifFrameModelLoader implements ModelLoader<GifDecoder, GifDecoder> {
    GifFrameModelLoader() {
    }

    public DataFetcher<GifDecoder> getResourceFetcher(GifDecoder model, int width, int height) {
        return new GifFrameDataFetcher(model);
    }

    private static class GifFrameDataFetcher implements DataFetcher<GifDecoder> {
        private final GifDecoder decoder;

        public GifFrameDataFetcher(GifDecoder decoder2) {
            this.decoder = decoder2;
        }

        public GifDecoder loadData(Priority priority) {
            return this.decoder;
        }

        public void cleanup() {
        }

        public String getId() {
            return String.valueOf(this.decoder.getCurrentFrameIndex());
        }

        public void cancel() {
        }
    }
}
