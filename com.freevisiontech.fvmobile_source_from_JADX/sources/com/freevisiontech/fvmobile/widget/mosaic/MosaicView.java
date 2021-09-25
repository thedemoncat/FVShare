package com.freevisiontech.fvmobile.widget.mosaic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.freevisiontech.fvmobile.utility.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MosaicView extends View {
    private static final String ERROR_INFO = "bad bitmap to add mosaic";
    private int BLOCK_SIZE = 50;
    private final int VALID_DISTANCE = 4;
    private Bitmap mBitmap;
    private int mBitmapHeight;
    private int mBitmapWidth;
    private int mColumnCount;
    private Context mContext;
    private float mLastX;
    private float mLastY;
    private Paint mPaint;
    private int mRowCount;
    private int[] mSampleColors;
    private int[] mSrcBitmapPixs;
    private int[] mTempBitmapPixs;

    public MosaicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
    }

    public void init(int width, int height, String photo_edit_path) {
        this.mBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(photo_edit_path), width, height, true);
        Bitmap bitmap = this.mBitmap;
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0 || bitmap.isRecycled()) {
            throw new RuntimeException(ERROR_INFO);
        }
        this.mBitmapWidth = bitmap.getWidth();
        this.mBitmapHeight = bitmap.getHeight();
        this.mRowCount = (int) Math.ceil((double) (((float) this.mBitmapHeight) / ((float) this.BLOCK_SIZE)));
        this.mColumnCount = (int) Math.ceil((double) (((float) this.mBitmapWidth) / ((float) this.BLOCK_SIZE)));
        this.mSampleColors = new int[(this.mRowCount * this.mColumnCount)];
        int maxX = this.mBitmapWidth - 1;
        int maxY = this.mBitmapHeight - 1;
        this.mSrcBitmapPixs = new int[(this.mBitmapWidth * this.mBitmapHeight)];
        this.mTempBitmapPixs = new int[(this.mBitmapWidth * this.mBitmapHeight)];
        bitmap.getPixels(this.mSrcBitmapPixs, 0, this.mBitmapWidth, 0, 0, this.mBitmapWidth, this.mBitmapHeight);
        bitmap.getPixels(this.mTempBitmapPixs, 0, this.mBitmapWidth, 0, 0, this.mBitmapWidth, this.mBitmapHeight);
        for (int row = 0; row < this.mRowCount; row++) {
            for (int column = 0; column < this.mColumnCount; column++) {
                this.mSampleColors[(this.mColumnCount * row) + column] = sampleBlock(this.mSrcBitmapPixs, column * this.BLOCK_SIZE, row * this.BLOCK_SIZE, this.BLOCK_SIZE, maxX, maxY);
            }
        }
        bitmap.setPixels(this.mSrcBitmapPixs, 0, this.mBitmapWidth, 0, 0, this.mBitmapWidth, this.mBitmapHeight);
    }

    private int sampleBlock(int[] pxs, int startX, int startY, int blockSize, int maxX, int maxY) {
        int stopX = Math.min((startX + blockSize) - 1, maxX);
        int stopY = Math.min((startY + blockSize) - 1, maxY);
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int y = startY; y <= stopY; y++) {
            int p = y * this.mBitmapWidth;
            for (int x = startX; x <= stopX; x++) {
                int color = pxs[p + x];
                red += Color.red(color);
                green += Color.green(color);
                blue += Color.blue(color);
            }
        }
        int sampleCount = ((stopY - startY) + 1) * ((stopX - startX) + 1);
        return Color.rgb(red / sampleCount, green / sampleCount, blue / sampleCount);
    }

    private void touchStart(float x, float y) {
        this.mLastX = x;
        this.mLastY = y;
    }

    private void touchMove(float x, float y) {
        if (Math.abs(x - this.mLastX) >= 4.0f || Math.abs(y - this.mLastY) >= 4.0f) {
            mosaic(new Point(this.mLastX, this.mLastY), new Point(x, y));
        }
        this.mLastX = x;
        this.mLastY = y;
    }

    public void mosaicSize(int size) {
        this.BLOCK_SIZE = size;
        Bitmap bitmap = this.mBitmap;
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0 || bitmap.isRecycled()) {
            throw new RuntimeException(ERROR_INFO);
        }
        this.mBitmapWidth = bitmap.getWidth();
        this.mBitmapHeight = bitmap.getHeight();
        this.mRowCount = (int) Math.ceil((double) (((float) this.mBitmapHeight) / ((float) this.BLOCK_SIZE)));
        this.mColumnCount = (int) Math.ceil((double) (((float) this.mBitmapWidth) / ((float) this.BLOCK_SIZE)));
        this.mSampleColors = new int[(this.mRowCount * this.mColumnCount)];
        int maxX = this.mBitmapWidth - 1;
        int maxY = this.mBitmapHeight - 1;
        this.mSrcBitmapPixs = new int[(this.mBitmapWidth * this.mBitmapHeight)];
        this.mTempBitmapPixs = new int[(this.mBitmapWidth * this.mBitmapHeight)];
        bitmap.getPixels(this.mSrcBitmapPixs, 0, this.mBitmapWidth, 0, 0, this.mBitmapWidth, this.mBitmapHeight);
        bitmap.getPixels(this.mTempBitmapPixs, 0, this.mBitmapWidth, 0, 0, this.mBitmapWidth, this.mBitmapHeight);
        for (int row = 0; row < this.mRowCount; row++) {
            for (int column = 0; column < this.mColumnCount; column++) {
                this.mSampleColors[(this.mColumnCount * row) + column] = sampleBlock(this.mSrcBitmapPixs, column * this.BLOCK_SIZE, row * this.BLOCK_SIZE, this.BLOCK_SIZE, maxX, maxY);
            }
        }
        bitmap.setPixels(this.mSrcBitmapPixs, 0, this.mBitmapWidth, 0, 0, this.mBitmapWidth, this.mBitmapHeight);
    }

    private void mosaic(Point startPoint, Point endPoint) {
        float startTouchX = startPoint.f1111x;
        float startTouchY = startPoint.f1112y;
        float endTouchX = endPoint.f1111x;
        float endTouchY = endPoint.f1112y;
        float minX = Math.min(startTouchX, endTouchX);
        float maxX = Math.max(startTouchX, endTouchX);
        int startIndexX = ((int) minX) / this.BLOCK_SIZE;
        int endIndexX = ((int) maxX) / this.BLOCK_SIZE;
        float minY = Math.min(startTouchY, endTouchY);
        float maxY = Math.max(startTouchY, endTouchY);
        int startIndexY = ((int) minY) / this.BLOCK_SIZE;
        int endIndexY = ((int) maxY) / this.BLOCK_SIZE;
        for (int row = startIndexY; row <= endIndexY; row++) {
            for (int colunm = startIndexX; colunm <= endIndexX; colunm++) {
                if (GeometryHelper.IsLineIntersectRect(startPoint.clone(), endPoint.clone(), new Rect(this.BLOCK_SIZE * colunm, this.BLOCK_SIZE * row, (colunm + 1) * this.BLOCK_SIZE, (row + 1) * this.BLOCK_SIZE)).booleanValue()) {
                    int rowMax = Math.min((row + 1) * this.BLOCK_SIZE, this.mBitmapHeight);
                    int colunmMax = Math.min((colunm + 1) * this.BLOCK_SIZE, this.mBitmapWidth);
                    for (int i = row * this.BLOCK_SIZE; i < rowMax; i++) {
                        for (int j = colunm * this.BLOCK_SIZE; j < colunmMax; j++) {
                            this.mTempBitmapPixs[(this.mBitmapWidth * i) + j] = this.mSampleColors[(this.mColumnCount * row) + colunm];
                        }
                    }
                }
            }
        }
        this.mBitmap.setPixels(this.mTempBitmapPixs, 0, this.mBitmapWidth, 0, 0, this.mBitmapWidth, this.mBitmapHeight);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                touchStart(Math.abs(x), Math.abs(y));
                invalidate();
                return true;
            case 1:
                invalidate();
                return true;
            case 2:
                touchMove(Math.abs(x), Math.abs(y));
                invalidate();
                return true;
            default:
                return true;
        }
    }

    public String setMosaicBitmapPath() {
        long currentTimeMillis = System.currentTimeMillis();
        String paintPath = Util.getOutputPhotoMosaicFileCatch(this.mContext);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(new File(paintPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
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
}
