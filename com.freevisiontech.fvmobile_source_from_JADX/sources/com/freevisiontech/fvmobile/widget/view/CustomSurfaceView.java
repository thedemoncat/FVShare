package com.freevisiontech.fvmobile.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static int state = 0;
    Bitmap bitmap;
    private Bitmap bmm;
    private Canvas canvas;
    private Context context;
    private int height;
    private Path mPath = new Path();
    private SurfaceHolder mSurfaceHolder;
    private Paint mpaint = new Paint();
    private Bitmap newBitmap;
    private int radius;
    private Bitmap saveBitmap;
    private boolean startBooleanDraw = true;
    private boolean startDraw;
    private boolean startMove = false;
    int startX;
    int startY;
    int stopX;
    int stopY;
    private int viewHeight;
    private int viewWidth;
    private int width;

    public CustomSurfaceView(Context context2) {
        super(context2);
        this.context = context2;
        try {
            this.saveBitmap = Bitmap.createBitmap(context2.getResources().getDisplayMetrics().widthPixels, context2.getResources().getDisplayMetrics().heightPixels, Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
        }
        this.startBooleanDraw = true;
        this.startMove = false;
        this.width = context2.getResources().getDisplayMetrics().widthPixels;
        this.height = context2.getResources().getDisplayMetrics().heightPixels;
        this.canvas = new Canvas(this.saveBitmap);
        this.canvas.setBitmap(this.saveBitmap);
        initView();
    }

    public CustomSurfaceView(Context context2, AttributeSet attrs) {
        super(context2, attrs);
    }

    private void initView() {
        setMeasuredDimension(720, 1000);
        this.mSurfaceHolder = getHolder();
        this.mSurfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        this.mSurfaceHolder.setFormat(-2);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.startDraw = true;
        this.canvas = this.mSurfaceHolder.lockCanvas();
        this.canvas.setBitmap(this.saveBitmap);
        this.mSurfaceHolder.unlockCanvasAndPost(this.canvas);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.viewWidth = getWidth();
        this.viewHeight = getHeight();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width2, int height2) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.startDraw = false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                if (this.startBooleanDraw) {
                    this.mPath = new Path();
                    this.mpaint = new Paint();
                    this.startX = 0;
                    this.startY = 0;
                    this.startX = (int) event.getX();
                    this.startY = (int) event.getY();
                    this.mPath.moveTo((float) this.startX, (float) this.startY);
                    break;
                }
                break;
            case 1:
                if (this.startBooleanDraw) {
                    this.mPath.moveTo((float) this.startX, (float) this.startY);
                    this.mPath.lineTo((float) this.startX, (float) this.stopY);
                    this.mPath.lineTo((float) this.stopX, (float) this.stopY);
                    this.mPath.lineTo((float) this.stopX, (float) this.startY);
                    this.mPath.lineTo((float) this.startX, (float) this.startY);
                    this.mPath.close();
                    if (!this.startMove) {
                        if (!(this.startX == 0 || this.startY == 0)) {
                            SPUtils.put(this.context, SharePrefConstant.CAMERA_FOLLOW, Integer.valueOf(Constants.CAMERA_FOLLOW_DROP));
                            Util.sendIntEventMessge((int) Constants.START_KCF_TO_BOTTOMBAR, new Rect(this.startX, this.startY, this.startX, this.startY));
                            drawsTwo(0, 0, 0, 0);
                            break;
                        }
                    } else {
                        if (this.stopX == 0 || this.stopY == 0) {
                            SPUtils.put(this.context, SharePrefConstant.CAMERA_FOLLOW, Integer.valueOf(Constants.CAMERA_FOLLOW_DROP));
                            Util.sendIntEventMessge((int) Constants.START_KCF_TO_BOTTOMBAR, new Rect(this.startX, this.startY, this.startX, this.startY));
                            drawsTwo(0, 0, 0, 0);
                        } else if (Math.abs(this.stopX - this.startX) >= 150 && Math.abs(this.stopY - this.startY) >= 150) {
                            SPUtils.put(this.context, SharePrefConstant.CAMERA_FOLLOW, Integer.valueOf(Constants.CAMERA_FOLLOW_FRAME));
                            Util.sendIntEventMessge((int) Constants.START_KCF_TO_BOTTOMBAR, new Rect(this.startX, this.startY, this.stopX, this.stopY));
                        } else if (Math.abs(this.stopX - this.startX) >= 20 || Math.abs(this.stopY - this.startY) >= 20) {
                            drawsTwo(0, 0, 0, 0);
                            Toast.makeText(getContext(), C0853R.string.small_kcf_box, 0).show();
                        } else {
                            SPUtils.put(this.context, SharePrefConstant.CAMERA_FOLLOW, Integer.valueOf(Constants.CAMERA_FOLLOW_DROP));
                            Util.sendIntEventMessge((int) Constants.START_KCF_TO_BOTTOMBAR, new Rect(this.startX, this.startY, this.startX, this.startY));
                            drawsTwo(0, 0, 0, 0);
                        }
                        this.startMove = false;
                        break;
                    }
                }
                break;
            case 2:
                if (this.startBooleanDraw) {
                    this.stopX = (int) event.getX();
                    this.stopY = (int) event.getY();
                    if (Math.abs(this.stopX - this.startX) >= 20 || Math.abs(this.stopY - this.startY) >= 20) {
                        draws();
                    }
                    this.startMove = true;
                    break;
                }
                break;
        }
        return true;
    }

    public void setUpStartDraw() {
        this.startBooleanDraw = false;
    }

    public void setUpStartDrawTwo() {
        this.startBooleanDraw = true;
    }

    public void draws() {
        int x1New;
        int x2New;
        int y1New;
        int y2New;
        if (this.canvas != null) {
            this.canvas.drawColor(ViewCompat.MEASURED_SIZE_MASK);
        }
        this.canvas = this.mSurfaceHolder.lockCanvas();
        this.mpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        if (this.mpaint != null) {
            this.canvas.drawPaint(this.mpaint);
        }
        this.mpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.canvas.drawBitmap(this.saveBitmap, (Rect) null, new Rect(0, 0, getWidth(), getHeight()), (Paint) null);
        this.mpaint.setAntiAlias(true);
        if (Math.abs(this.startX - this.stopX) < 50 || Math.abs(this.startY - this.stopY) < 50) {
            this.mpaint.setColor(-1);
            this.mpaint.setStyle(Paint.Style.STROKE);
            this.mpaint.setStrokeWidth((float) dip2px(this.context, 3.0f));
            this.canvas.drawRect((float) this.startX, (float) this.startY, (float) this.stopX, (float) this.stopY, this.mpaint);
        } else {
            this.mpaint.setColor(getResources().getColor(C0853R.color.f1084fa));
            this.mpaint.setStyle(Paint.Style.STROKE);
            this.mpaint.setStrokeWidth((float) dip2px(this.context, 1.0f));
            this.canvas.drawRect((float) this.startX, (float) this.startY, (float) this.stopX, (float) this.stopY, this.mpaint);
        }
        if (Math.abs(this.startX - this.stopX) > 50 && Math.abs(this.startY - this.stopY) > 50) {
            if (this.startX >= this.stopX) {
                x1New = this.stopX;
                x2New = this.startX;
            } else {
                x1New = this.startX;
                x2New = this.stopX;
            }
            if (this.startY >= this.stopY) {
                y1New = this.stopY;
                y2New = this.startY;
            } else {
                y1New = this.startY;
                y2New = this.stopY;
            }
            this.mpaint.setColor(-1);
            this.mpaint.setStrokeWidth((float) dip2px(this.context, 3.0f));
            this.canvas.drawLine((float) x1New, (float) y1New, (float) x1New, (float) (y1New + 50), this.mpaint);
            this.canvas.drawLine((float) x1New, (float) y1New, (float) (x1New + 50), (float) y1New, this.mpaint);
            this.canvas.drawLine((float) (x2New - 50), (float) y2New, (float) x2New, (float) y2New, this.mpaint);
            this.canvas.drawLine((float) x2New, (float) (y2New - 50), (float) x2New, (float) y2New, this.mpaint);
            this.canvas.drawLine((float) x1New, (float) (y2New - 50), (float) x1New, (float) y2New, this.mpaint);
            this.canvas.drawLine((float) x1New, (float) y2New, (float) (x1New + 50), (float) y2New, this.mpaint);
            this.canvas.drawLine((float) (x2New - 50), (float) y1New, (float) x2New, (float) y1New, this.mpaint);
            this.canvas.drawLine((float) x2New, (float) (y1New + 50), (float) x2New, (float) y1New, this.mpaint);
            this.mpaint.setStyle(Paint.Style.FILL);
            this.canvas.drawCircle((float) x1New, (float) y1New, (float) dip2px(this.context, 1.0f), this.mpaint);
            this.canvas.drawCircle((float) x2New, (float) y2New, (float) dip2px(this.context, 1.0f), this.mpaint);
            this.canvas.drawCircle((float) x1New, (float) y2New, (float) dip2px(this.context, 1.0f), this.mpaint);
            this.canvas.drawCircle((float) x2New, (float) y1New, (float) dip2px(this.context, 1.0f), this.mpaint);
        }
        this.mSurfaceHolder.unlockCanvasAndPost(this.canvas);
    }

    public void drawsTwo(int startX2, int startY2, int stopX2, int stopY2) {
        int x1New;
        int x2New;
        int y1New;
        int y2New;
        this.canvas = this.mSurfaceHolder.lockCanvas();
        if (this.canvas != null) {
            this.canvas.drawColor(ViewCompat.MEASURED_SIZE_MASK);
        }
        this.mpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        if (!(this.mpaint == null || this.canvas == null)) {
            this.canvas.drawPaint(this.mpaint);
        }
        this.mpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        new Rect(0, 0, getWidth(), getHeight());
        this.mpaint.setAntiAlias(true);
        this.mpaint.setColor(getResources().getColor(C0853R.color.f1084fa));
        this.mpaint.setStyle(Paint.Style.STROKE);
        this.mpaint.setStrokeWidth((float) dip2px(this.context, 1.0f));
        if (this.canvas != null) {
            this.canvas.drawRect((float) startX2, (float) startY2, (float) stopX2, (float) stopY2, this.mpaint);
        }
        if (Math.abs(startX2 - stopX2) > 50 && Math.abs(startY2 - stopY2) > 50) {
            if (startX2 >= stopX2) {
                x1New = stopX2;
                x2New = startX2;
            } else {
                x1New = startX2;
                x2New = stopX2;
            }
            if (startY2 >= stopY2) {
                y1New = stopY2;
                y2New = startY2;
            } else {
                y1New = startY2;
                y2New = stopY2;
            }
            this.mpaint.setColor(-1);
            this.mpaint.setStrokeWidth((float) dip2px(this.context, 3.0f));
            if (this.canvas != null) {
                this.canvas.drawLine((float) x1New, (float) y1New, (float) x1New, (float) (y1New + 50), this.mpaint);
                this.canvas.drawLine((float) x1New, (float) y1New, (float) (x1New + 50), (float) y1New, this.mpaint);
                this.canvas.drawLine((float) (x2New - 50), (float) y2New, (float) x2New, (float) y2New, this.mpaint);
                this.canvas.drawLine((float) x2New, (float) (y2New - 50), (float) x2New, (float) y2New, this.mpaint);
                this.canvas.drawLine((float) x1New, (float) (y2New - 50), (float) x1New, (float) y2New, this.mpaint);
                this.canvas.drawLine((float) x1New, (float) y2New, (float) (x1New + 50), (float) y2New, this.mpaint);
                this.canvas.drawLine((float) (x2New - 50), (float) y1New, (float) x2New, (float) y1New, this.mpaint);
                this.canvas.drawLine((float) x2New, (float) (y1New + 50), (float) x2New, (float) y1New, this.mpaint);
                this.mpaint.setStyle(Paint.Style.FILL);
                this.canvas.drawCircle((float) x1New, (float) y1New, (float) dip2px(this.context, 1.0f), this.mpaint);
                this.canvas.drawCircle((float) x2New, (float) y2New, (float) dip2px(this.context, 1.0f), this.mpaint);
                this.canvas.drawCircle((float) x1New, (float) y2New, (float) dip2px(this.context, 1.0f), this.mpaint);
                this.canvas.drawCircle((float) x2New, (float) y1New, (float) dip2px(this.context, 1.0f), this.mpaint);
            }
        }
        if (this.canvas != null) {
            this.mSurfaceHolder.unlockCanvasAndPost(this.canvas);
        }
    }

    public static int dip2px(Context context2, float dipValue) {
        return (int) ((dipValue * context2.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
