package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.freevisiontech.fvmobile.utility.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoPaintView extends View {
    private static final float TOUCH_TOLERANCE = 4.0f;
    private static List<DrawPath> savePath;
    private Context context;

    /* renamed from: dp */
    private DrawPath f1102dp;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;

    /* renamed from: mX */
    private float f1103mX;

    /* renamed from: mY */
    private float f1104mY;
    private int screenHeight;
    private int screenWidth;

    private class DrawPath {
        public Paint paint;
        public Path path;

        private DrawPath() {
        }
    }

    public PhotoPaintView(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        this.context = context2;
    }

    public PhotoPaintView(Context context2, int w, int h) {
        super(context2);
        this.mBitmap = Bitmap.createBitmap(context2.getResources().getDisplayMetrics().widthPixels, context2.getResources().getDisplayMetrics().heightPixels, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mBitmap);
        this.mCanvas.setBitmap(this.mBitmap);
        this.mBitmapPaint = new Paint(4);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
        this.mPaint.setStrokeWidth(12.0f);
        this.mPaint.setColor(-14596609);
        savePath = new ArrayList();
    }

    public void setWidthHight(Context context2, int width, int hight) {
        this.context = context2;
        this.screenWidth = width;
        this.screenHeight = hight;
        this.mBitmap = Bitmap.createBitmap(this.screenWidth, this.screenHeight, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mBitmap);
        this.mCanvas.setBitmap(this.mBitmap);
        this.mBitmapPaint = new Paint(4);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setStrokeWidth(12.0f);
        this.mPaint.setColor(-43230);
        savePath = new ArrayList();
    }

    public void setPaintColorSize(Context context2, int color, int size) {
        this.context = context2;
        this.mBitmapPaint = new Paint(4);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setStrokeWidth((float) size);
        this.mPaint.setColor(color);
    }

    public void setPaintColorSizeCha(Context context2, int size) {
        this.context = context2;
        this.mBitmapPaint = new Paint(4);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        this.mPaint.setColor(0);
        this.mPaint.setStrokeWidth((float) size);
        setLayerType(1, (Paint) null);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(ViewCompat.MEASURED_SIZE_MASK);
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mBitmapPaint);
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
        if (this.mPath != null) {
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }

    private void touch_start(float x, float y) {
        this.mPath.moveTo(x, y);
        this.f1103mX = x;
        this.f1104mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - this.f1103mX);
        float dy = Math.abs(this.f1104mY - y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            this.mPath.quadTo(this.f1103mX, this.f1104mY, (this.f1103mX + x) / 2.0f, (this.f1104mY + y) / 2.0f);
            this.f1103mX = x;
            this.f1104mY = y;
        }
    }

    private void touch_up() {
        this.mPath.lineTo(this.f1103mX, this.f1104mY);
        this.mCanvas.drawPath(this.mPath, this.mPaint);
        savePath.add(this.f1102dp);
        this.mPath = null;
    }

    public void photoEditPaintReturn(Context context2) {
        this.context = context2;
        this.mBitmap = Bitmap.createBitmap(context2.getResources().getDisplayMetrics().widthPixels, context2.getResources().getDisplayMetrics().heightPixels, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mBitmap);
        this.mCanvas.setBitmap(this.mBitmap);
        if (savePath != null && savePath.size() > 0) {
            savePath.remove(savePath.size() - 1);
            for (DrawPath drawPath : savePath) {
                this.mCanvas.drawPath(drawPath.path, drawPath.paint);
            }
            invalidate();
            String fileUrl = Environment.getExternalStorageDirectory().toString() + "/android/data/test.png";
            Log.e("---------------", "-----------fileUrl--" + fileUrl);
            try {
                FileOutputStream fos = new FileOutputStream(new File(fileUrl));
                this.mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public String saveMyBitmap(Bitmap bmp) {
        long currentTimeMillis = System.currentTimeMillis();
        String paintPath = Util.getOutputPhotoFile(this.context);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(new File(paintPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mergeBitmap(bmp, this.mBitmap).compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return paintPath;
    }

    public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap) {
        if (backBitmap == null || backBitmap.isRecycled() || frontBitmap == null || frontBitmap.isRecycled()) {
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Rect baseRect = new Rect(0, 0, backBitmap.getWidth(), backBitmap.getHeight());
        Rect frontRect = new Rect(0, 0, frontBitmap.getWidth() - 100, frontBitmap.getHeight());
        Log.e("---------------", "--------------width1--" + backBitmap.getWidth() + "----height1---" + backBitmap.getHeight() + "---------width2----" + frontBitmap.getWidth() + "--------height2---" + frontBitmap.getHeight());
        canvas.drawBitmap(frontBitmap, frontRect, baseRect, (Paint) null);
        return bitmap;
    }

    public void redo() {
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                this.mPath = new Path();
                this.f1102dp = new DrawPath();
                this.f1102dp.path = this.mPath;
                this.f1102dp.paint = this.mPaint;
                touch_start(x, y);
                invalidate();
                return true;
            case 1:
                touch_up();
                invalidate();
                return true;
            case 2:
                touch_move(x, y);
                invalidate();
                return true;
            default:
                return true;
        }
    }
}
