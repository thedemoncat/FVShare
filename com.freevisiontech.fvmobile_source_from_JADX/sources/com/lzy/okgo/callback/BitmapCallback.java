package com.lzy.okgo.callback;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.lzy.okgo.convert.BitmapConvert;
import okhttp3.Response;

public abstract class BitmapCallback extends AbsCallback<Bitmap> {
    private BitmapConvert convert;

    public BitmapCallback() {
        this.convert = new BitmapConvert();
    }

    public BitmapCallback(int maxWidth, int maxHeight) {
        this.convert = new BitmapConvert(maxWidth, maxHeight);
    }

    public BitmapCallback(int maxWidth, int maxHeight, Bitmap.Config decodeConfig, ImageView.ScaleType scaleType) {
        this.convert = new BitmapConvert(maxWidth, maxHeight, decodeConfig, scaleType);
    }

    public Bitmap convertResponse(Response response) throws Throwable {
        Bitmap bitmap = this.convert.convertResponse(response);
        response.close();
        return bitmap;
    }
}
