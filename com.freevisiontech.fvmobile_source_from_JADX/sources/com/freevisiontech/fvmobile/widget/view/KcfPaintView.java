package com.freevisiontech.fvmobile.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.freevisiontech.fvmobile.bean.MoveTimeLapsePaintBean;
import com.freevisiontech.fvmobile.bean.MoveTimeScalePaintBean;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.Util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class KcfPaintView extends View {
    private static final float TOUCH_TOLERANCE = 4.0f;
    private static List<DrawPath> savePath;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    private Context context;
    private Float dXNewEnd;
    private Float dYNewEnd;

    /* renamed from: dp */
    private DrawPath f1113dp;
    private List listA;
    private List listB;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;

    /* renamed from: mX */
    private float f1114mX;

    /* renamed from: mY */
    private float f1115mY;
    /* access modifiers changed from: private */
    public boolean okStart;
    private MoveTimeLapsePaintBean paintBean;
    private MoveTimeScalePaintBean paintScaleBean;
    private List pointList;
    private List pointScaleList;
    InputStreamReader reader;
    private int screenHeight;
    private int screenWidth;

    private class DrawPath {
        public Paint paint;
        public Path path;

        private DrawPath() {
        }
    }

    public KcfPaintView(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        this.context = context2;
    }

    public void setWidthHight(Context context2, int width, int hight) {
        this.context = context2;
        this.screenWidth = width;
        this.screenHeight = hight;
        this.mBitmap = Bitmap.createBitmap(this.screenWidth, this.screenHeight, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mBitmap);
        this.mCanvas.setBitmap(this.mBitmap);
        this.okStart = true;
        this.mBitmapPaint = new Paint(4);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setStrokeWidth(5.0f);
        this.mPaint.setColor(-7099154);
        savePath = new ArrayList();
        this.pointList = new ArrayList();
        this.pointScaleList = new ArrayList();
        this.listA = new ArrayList();
        this.listB = new ArrayList();
    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(ViewCompat.MEASURED_SIZE_MASK);
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mBitmapPaint);
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
        if (this.mPath != null) {
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                if (!this.okStart) {
                    return true;
                }
                this.listA.clear();
                this.pointList.clear();
                this.pointScaleList.clear();
                this.mPath = new Path();
                this.f1113dp = new DrawPath();
                this.f1113dp.path = this.mPath;
                this.f1113dp.paint = this.mPaint;
                touch_start(x, y);
                return true;
            case 1:
                if (!this.okStart) {
                    return true;
                }
                if (this.pointList.size() > 100) {
                    this.okStart = false;
                    Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO_PAINT, this.pointList);
                    return true;
                }
                this.okStart = false;
                clear();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        boolean unused = KcfPaintView.this.okStart = true;
                    }
                }, 500);
                return true;
            case 2:
                if (!this.okStart) {
                    return true;
                }
                if (this.pointList.size() < 5000) {
                    touch_move(x, y);
                }
                if (this.pointScaleList.size() >= 5000) {
                    return true;
                }
                this.paintScaleBean = new MoveTimeScalePaintBean();
                this.paintScaleBean.setXPhonePoint(x);
                this.paintScaleBean.setYPhonePoint(y);
                if (this.pointScaleList.size() < 1) {
                    this.pointScaleList.add(this.paintScaleBean);
                    return true;
                }
                MoveTimeScalePaintBean moveTimeLapsePaintBeanS0 = (MoveTimeScalePaintBean) this.pointScaleList.get(this.pointScaleList.size() - 1);
                float P00 = moveTimeLapsePaintBeanS0.getXPhonePoint();
                float PY00 = moveTimeLapsePaintBeanS0.getYPhonePoint();
                float dXSpaceOne0 = Math.abs(x - P00);
                float dYSpaceOne0 = Math.abs(y - PY00);
                float Xk0 = 0.021972656f * CameraUtils.getkXPerScale();
                float Yk0 = 0.0038146973f * CameraUtils.getkYPerScale();
                float dXYSpaceNew0 = (float) Math.sqrt((double) ((dXSpaceOne0 * dXSpaceOne0 * Xk0 * Xk0) + (dYSpaceOne0 * dYSpaceOne0 * Yk0 * Yk0)));
                if (dXYSpaceNew0 <= 0.2f || dXYSpaceNew0 <= 0.2f) {
                    return true;
                }
                this.pointScaleList.add(this.paintScaleBean);
                if (this.pointScaleList.size() < 3) {
                    return true;
                }
                if (this.pointScaleList.size() == 3) {
                    int size = this.pointScaleList.size() - 1;
                    MoveTimeScalePaintBean moveTimeLapsePaintBeanSSS = (MoveTimeScalePaintBean) this.pointScaleList.get(this.pointScaleList.size() - 1);
                    float P2 = moveTimeLapsePaintBeanSSS.getXPhonePoint();
                    float PY2 = moveTimeLapsePaintBeanSSS.getYPhonePoint();
                    int size2 = this.pointScaleList.size() - 2;
                    MoveTimeScalePaintBean moveTimeLapsePaintBeanSS = (MoveTimeScalePaintBean) this.pointScaleList.get(this.pointScaleList.size() - 2);
                    float P1 = moveTimeLapsePaintBeanSS.getXPhonePoint();
                    float PY1 = moveTimeLapsePaintBeanSS.getYPhonePoint();
                    int size3 = this.pointScaleList.size() - 3;
                    MoveTimeScalePaintBean moveTimeLapsePaintBeanS = (MoveTimeScalePaintBean) this.pointScaleList.get(this.pointScaleList.size() - 3);
                    float P0 = moveTimeLapsePaintBeanS.getXPhonePoint();
                    float PY0 = moveTimeLapsePaintBeanS.getYPhonePoint();
                    float dXSpaceOne = Math.abs(P1 - P0);
                    float dYSpaceOne = Math.abs(PY1 - PY0);
                    float dXSpaceTwo = Math.abs(P2 - P1);
                    float dYSpaceTwo = Math.abs(PY2 - PY1);
                    float Xk = 0.021972656f * CameraUtils.getkXPerScale();
                    float Yk = 0.0038146973f * CameraUtils.getkYPerScale();
                    float ddd = ((float) Math.sqrt((double) ((((dXSpaceOne * dXSpaceOne) * Xk) * Xk) + (((dYSpaceOne * dYSpaceOne) * Yk) * Yk)))) / 0.2f;
                    float fff = ((float) Math.sqrt((double) ((((dXSpaceTwo * dXSpaceTwo) * Xk) * Xk) + (((dYSpaceTwo * dYSpaceTwo) * Yk) * Yk)))) / 0.2f;
                    if (ddd <= 1.0f) {
                        return true;
                    }
                    int allNums = ((int) ddd) + ((int) fff) + 2;
                    for (int e = 0; e < allNums; e++) {
                        if (((float) e) < 1.0f + ddd) {
                            Float dXNew = Float.valueOf(shengChengBeiSai(P0, P1, P2, (float) e, allNums));
                            Float dYNew = Float.valueOf(shengChengBeiSai(PY0, PY1, PY2, (float) e, allNums));
                            if (this.pointList.size() < 5000) {
                                if (this.pointList.size() > 0) {
                                    MoveTimeLapsePaintBean moveTimeLapsePaintBean = (MoveTimeLapsePaintBean) this.pointList.get(this.pointList.size() - 1);
                                    float xxx = moveTimeLapsePaintBean.getXPhonePoint();
                                    float yyy = moveTimeLapsePaintBean.getYPhonePoint();
                                    Float xNew = Float.valueOf((0.1f * dXNew.floatValue()) + (0.9f * xxx));
                                    Float yNew = Float.valueOf((0.1f * dYNew.floatValue()) + (0.9f * yyy));
                                    this.paintBean = new MoveTimeLapsePaintBean();
                                    this.paintBean.setXPhonePoint(xNew.floatValue());
                                    this.paintBean.setYPhonePoint(yNew.floatValue());
                                    this.pointList.add(this.paintBean);
                                    if (this.pointList.size() > 2) {
                                        this.mCanvas.drawPoint(xNew.floatValue(), yNew.floatValue(), this.mPaint);
                                        invalidate();
                                    }
                                } else {
                                    this.paintBean = new MoveTimeLapsePaintBean();
                                    this.paintBean.setXPhonePoint(dXNew.floatValue());
                                    this.paintBean.setYPhonePoint(dYNew.floatValue());
                                    this.pointList.add(this.paintBean);
                                }
                            }
                        }
                    }
                    this.dXNewEnd = Float.valueOf(shengChengBeiSai(P0, P1, P2, ddd, allNums));
                    this.dYNewEnd = Float.valueOf(shengChengBeiSai(PY0, PY1, PY2, ddd, allNums));
                    return true;
                }
                int size4 = this.pointScaleList.size() - 1;
                MoveTimeScalePaintBean moveTimeLapsePaintBeanSSS2 = (MoveTimeScalePaintBean) this.pointScaleList.get(this.pointScaleList.size() - 1);
                float P22 = moveTimeLapsePaintBeanSSS2.getXPhonePoint();
                float PY22 = moveTimeLapsePaintBeanSSS2.getYPhonePoint();
                int size5 = this.pointScaleList.size() - 2;
                MoveTimeScalePaintBean moveTimeLapsePaintBeanSS2 = (MoveTimeScalePaintBean) this.pointScaleList.get(this.pointScaleList.size() - 2);
                float P12 = moveTimeLapsePaintBeanSS2.getXPhonePoint();
                float PY12 = moveTimeLapsePaintBeanSS2.getYPhonePoint();
                float P02 = this.dXNewEnd.floatValue();
                float PY02 = this.dYNewEnd.floatValue();
                float dXSpaceOne2 = Math.abs(P12 - P02);
                float dYSpaceOne2 = Math.abs(PY12 - PY02);
                float dXSpaceTwo2 = Math.abs(P22 - P12);
                float dYSpaceTwo2 = Math.abs(PY22 - PY12);
                float Xk2 = 0.021972656f * CameraUtils.getkXPerScale();
                float Yk2 = 0.0038146973f * CameraUtils.getkYPerScale();
                float ddd2 = ((float) Math.sqrt((double) ((((dXSpaceOne2 * dXSpaceOne2) * Xk2) * Xk2) + (((dYSpaceOne2 * dYSpaceOne2) * Yk2) * Yk2)))) / 0.2f;
                float fff2 = ((float) Math.sqrt((double) ((((dXSpaceTwo2 * dXSpaceTwo2) * Xk2) * Xk2) + (((dYSpaceTwo2 * dYSpaceTwo2) * Yk2) * Yk2)))) / 0.2f;
                if (ddd2 <= 1.0f) {
                    return true;
                }
                int allNums2 = ((int) ddd2) + ((int) fff2) + 2;
                for (int e2 = 0; e2 < allNums2; e2++) {
                    if (((float) e2) < 1.0f + ddd2) {
                        Float dXNew2 = Float.valueOf(shengChengBeiSai(P02, P12, P22, (float) e2, allNums2));
                        Float dYNew2 = Float.valueOf(shengChengBeiSai(PY02, PY12, PY22, (float) e2, allNums2));
                        MoveTimeLapsePaintBean moveTimeLapsePaintBean2 = (MoveTimeLapsePaintBean) this.pointList.get(this.pointList.size() - 1);
                        float xxx2 = moveTimeLapsePaintBean2.getXPhonePoint();
                        float yyy2 = moveTimeLapsePaintBean2.getYPhonePoint();
                        Float xNew2 = Float.valueOf((0.1f * dXNew2.floatValue()) + (0.9f * xxx2));
                        Float yNew2 = Float.valueOf((0.1f * dYNew2.floatValue()) + (0.9f * yyy2));
                        if (this.pointList.size() < 5000) {
                            this.paintBean = new MoveTimeLapsePaintBean();
                            this.paintBean.setXPhonePoint(xNew2.floatValue());
                            this.paintBean.setYPhonePoint(yNew2.floatValue());
                            this.pointList.add(this.paintBean);
                            if (this.pointList.size() > 2) {
                                this.mCanvas.drawPoint(xNew2.floatValue(), yNew2.floatValue(), this.mPaint);
                                invalidate();
                            }
                        }
                    }
                }
                this.dXNewEnd = Float.valueOf(shengChengBeiSai(P02, P12, P22, ddd2, allNums2));
                this.dYNewEnd = Float.valueOf(shengChengBeiSai(PY02, PY12, PY22, ddd2, allNums2));
                return true;
            default:
                return true;
        }
    }

    public boolean string2File(String content, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            this.bufferedReader = new BufferedReader(new StringReader(content));
            this.bufferedWriter = new BufferedWriter(new FileWriter(file));
            char[] buffer = new char[1024];
            while (true) {
                int len = this.bufferedReader.read(buffer);
                if (len == -1) {
                    break;
                }
                this.bufferedWriter.write(buffer, 0, len);
            }
            this.bufferedWriter.flush();
            this.bufferedReader.close();
            this.bufferedWriter.close();
            if (this.bufferedReader != null) {
                try {
                    this.bufferedReader.close();
                } catch (IOException e) {
                }
            }
            return true;
        } catch (IOException e2) {
            if (this.bufferedReader != null) {
                try {
                    this.bufferedReader.close();
                } catch (IOException e3) {
                }
            }
            return false;
        } catch (Throwable th) {
            if (this.bufferedReader != null) {
                try {
                    this.bufferedReader.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    private float shengChengBeiSai(float p0, float p1, float p2, float tt, int all) {
        float t = tt / ((float) all);
        return ((1.0f - t) * (1.0f - t) * p0) + (2.0f * t * (1.0f - t) * p1) + (t * t * p2);
    }

    private float shengChengBeiSaiOne(float p0, float p1, float tt, int all) {
        float t = tt / ((float) all);
        return ((1.0f - t) * p0) + (t * p1);
    }

    private void touch_start(float x, float y) {
        this.f1114mX = x;
        this.f1115mY = y;
    }

    public void ok_start() {
        this.okStart = true;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - this.f1114mX);
        float dy = Math.abs(this.f1115mY - y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            this.f1114mX = x;
            this.f1115mY = y;
        }
    }

    private void touch_up() {
        this.mPath.lineTo(this.f1114mX, this.f1115mY);
        this.mCanvas.drawPath(this.mPath, this.mPaint);
        savePath.add(this.f1113dp);
        this.mPath = null;
    }

    public void touch_anew_start(float x, float y) {
        this.mPath = new Path();
        this.f1113dp = new DrawPath();
        this.f1113dp.path = this.mPath;
        this.f1113dp.paint = this.mPaint;
        this.mPath.moveTo(x, y);
        this.f1114mX = x;
        this.f1115mY = y;
    }

    public void touch_anew_move(float x, float y) {
        float dx = Math.abs(x - this.f1114mX);
        float dy = Math.abs(this.f1115mY - y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            this.mPath.quadTo(this.f1114mX, this.f1115mY, (this.f1114mX + x) / 2.0f, (this.f1115mY + y) / 2.0f);
            this.f1114mX = x;
            this.f1115mY = y;
        }
        invalidate();
    }

    public void touch_anew_move_two(float x, float y) {
        float dx = Math.abs(x - this.f1114mX);
        float dy = Math.abs(this.f1115mY - y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            this.mPath.quadTo(this.f1114mX, this.f1115mY, this.f1114mX, this.f1115mY);
            this.f1114mX = x;
            this.f1115mY = y;
        }
        invalidate();
    }

    public void clear() {
        this.mPath = new Path();
        this.f1113dp = new DrawPath();
        this.f1113dp.path = this.mPath;
        this.f1113dp.paint = this.mPaint;
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mCanvas.drawPaint(this.mPaint);
        touch_start(0.0f, 0.0f);
        invalidate();
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        invalidate();
    }
}
