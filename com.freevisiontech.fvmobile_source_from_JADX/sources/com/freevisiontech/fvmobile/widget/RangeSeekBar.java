package com.freevisiontech.fvmobile.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.freevisiontech.fvmobile.C0853R;

public class RangeSeekBar extends View {
    private OnRangeChangedListener callback;
    private int cellsCount;
    private float cellsPercent;
    private int colorLineEdge;
    private int colorLineSelected;
    private SeekBar currTouch;
    private SeekBar leftSB;
    private RectF line;
    private int lineBottom;
    private int lineCorners;
    private final float lineHeight;
    private int lineLeft;
    private int lineRight;
    private int lineTop;
    private int lineWidth;
    private float maxValue;
    private float minValue;
    private float offsetValue;
    private final float padding;
    private Paint paint;
    private int reserveCount;
    private float reservePercent;
    private float reserveValue;
    private SeekBar rightSB;
    private int seekBarResId;
    private final float thumbHalfHeight;
    private final float thumbHalfWidth;
    private final Bitmap thumbImage;
    private final Bitmap thumbPressedImage;
    private final float thumbWidth;

    public interface OnRangeChangedListener {
        void onRangeChanged(RangeSeekBar rangeSeekBar, float f, float f2);
    }

    private class SeekBar {
        ValueAnimator anim;
        Bitmap bmp;
        int bottom;
        float currPercent;
        Paint defaultPaint;
        int heightSize;
        int left;
        int lineWidth;
        float material;
        int right;
        RadialGradient shadowGradient;

        /* renamed from: te */
        final TypeEvaluator<Integer> f1109te;
        int top;
        int widthSize;

        private SeekBar() {
            this.material = 0.0f;
            this.f1109te = new TypeEvaluator<Integer>() {
                public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                    return Integer.valueOf(Color.argb((int) (((float) Color.alpha(startValue.intValue())) + (((float) (Color.alpha(endValue.intValue()) - Color.alpha(startValue.intValue()))) * fraction)), (int) (((float) Color.red(startValue.intValue())) + (((float) (Color.red(endValue.intValue()) - Color.red(startValue.intValue()))) * fraction)), (int) (((float) Color.green(startValue.intValue())) + (((float) (Color.green(endValue.intValue()) - Color.green(startValue.intValue()))) * fraction)), (int) (((float) Color.blue(startValue.intValue())) + (((float) (Color.blue(endValue.intValue()) - Color.blue(startValue.intValue()))) * fraction))));
                }
            };
        }

        /* access modifiers changed from: package-private */
        public void onSizeChanged(int centerX, int centerY, int hSize, int parentLineWidth, boolean cellsMode, int bmpResId, Context context) {
            this.heightSize = hSize;
            this.widthSize = (int) (((float) this.heightSize) * 0.4f);
            this.left = centerX - (this.widthSize / 2);
            this.right = (this.widthSize / 2) + centerX;
            this.top = centerY - (this.heightSize / 2);
            this.bottom = (this.heightSize / 2) + centerY;
            if (cellsMode) {
                this.lineWidth = parentLineWidth;
            } else {
                this.lineWidth = parentLineWidth - this.widthSize;
            }
            if (bmpResId > 0) {
                Bitmap original = BitmapFactory.decodeResource(context.getResources(), bmpResId);
                Matrix matrix = new Matrix();
                matrix.postScale(((float) this.widthSize) / ((float) original.getWidth()), ((float) this.heightSize) / ((float) original.getHeight()));
                this.bmp = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
                return;
            }
            this.defaultPaint = new Paint(1);
            int barShadowRadius = (int) (((float) ((int) (((float) this.widthSize) * 0.5f))) * 0.95f);
            this.shadowGradient = new RadialGradient((float) (this.widthSize / 2), (float) (this.heightSize / 2), (float) barShadowRadius, -16777216, 0, Shader.TileMode.CLAMP);
        }

        /* access modifiers changed from: package-private */
        public boolean collide(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            int offset = (int) (((float) this.lineWidth) * this.currPercent);
            return x > ((float) (this.left + offset)) && x < ((float) (this.right + offset)) && y > ((float) this.top) && y < ((float) this.bottom);
        }

        /* access modifiers changed from: package-private */
        public void slide(float percent) {
            if (percent < 0.0f) {
                percent = 0.0f;
            } else if (percent > 1.0f) {
                percent = 1.0f;
            }
            this.currPercent = percent;
        }

        /* access modifiers changed from: package-private */
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.translate((float) ((int) (((float) this.lineWidth) * this.currPercent)), 0.0f);
            if (this.bmp != null) {
                canvas.drawBitmap(this.bmp, (float) this.left, (float) this.top, (Paint) null);
            } else {
                canvas.translate((float) this.left, 0.0f);
                drawDefault(canvas);
            }
            canvas.restore();
        }

        private void drawDefault(Canvas canvas) {
            int centerX = this.widthSize / 2;
            int centerY = this.heightSize / 2;
            int radius = (int) (((float) this.widthSize) * 0.5f);
            this.defaultPaint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.translate(0.0f, ((float) radius) * 0.25f);
            canvas.scale((this.material * 0.1f) + 1.0f, (this.material * 0.1f) + 1.0f, (float) centerX, (float) centerY);
            this.defaultPaint.setShader(this.shadowGradient);
            canvas.drawCircle((float) centerX, (float) centerY, (float) radius, this.defaultPaint);
            this.defaultPaint.setShader((Shader) null);
            canvas.restore();
            this.defaultPaint.setStyle(Paint.Style.FILL);
            this.defaultPaint.setColor(this.f1109te.evaluate(this.material, -1, -1579033).intValue());
            canvas.drawCircle((float) centerX, (float) centerY, (float) radius, this.defaultPaint);
            this.defaultPaint.setStyle(Paint.Style.STROKE);
            this.defaultPaint.setColor(-2631721);
            canvas.drawCircle((float) centerX, (float) centerY, (float) radius, this.defaultPaint);
        }

        /* access modifiers changed from: private */
        public void materialRestore() {
            if (this.anim != null) {
                this.anim.cancel();
            }
            this.anim = ValueAnimator.ofFloat(new float[]{this.material, 0.0f});
            this.anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    SeekBar.this.material = ((Float) animation.getAnimatedValue()).floatValue();
                    RangeSeekBar.this.invalidate();
                }
            });
            this.anim.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    SeekBar.this.material = 0.0f;
                    RangeSeekBar.this.invalidate();
                }
            });
            this.anim.start();
        }
    }

    public RangeSeekBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        this.line = new RectF();
        this.leftSB = new SeekBar();
        this.rightSB = new SeekBar();
        this.cellsCount = 1;
        this.thumbImage = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_video_editing_huakuai_left);
        this.thumbPressedImage = BitmapFactory.decodeResource(getResources(), C0853R.mipmap.ic_video_editing_huakuai_right);
        this.lineHeight = (float) this.thumbImage.getHeight();
        this.thumbWidth = (float) this.thumbImage.getWidth();
        this.thumbHalfWidth = this.thumbWidth * 0.5f;
        this.padding = this.thumbHalfWidth;
        this.thumbHalfHeight = ((float) this.thumbImage.getHeight()) * 0.5f;
        TypedArray t = context.obtainStyledAttributes(attrs, C0853R.styleable.RangeSeekBar);
        this.seekBarResId = t.getResourceId(6, 0);
        this.colorLineSelected = t.getColor(4, -11806366);
        this.colorLineEdge = t.getColor(5, -2631721);
        setRules(t.getFloat(1, 0.0f), t.getFloat(0, 1.0f), t.getFloat(2, 0.0f), t.getInt(3, 1));
        t.recycle();
    }

    public void setOnRangeChangedListener(OnRangeChangedListener listener) {
        this.callback = listener;
    }

    public void setValue(float min, float max) {
        float min2 = min + this.offsetValue;
        float max2 = max + this.offsetValue;
        if (min2 < this.minValue) {
            throw new IllegalArgumentException("setValue() min < (preset min - offsetValue) . #min:" + min2 + " #preset min:" + this.minValue + " #offsetValue:" + this.offsetValue);
        } else if (max2 > this.maxValue) {
            throw new IllegalArgumentException("setValue() max > (preset max - offsetValue) . #max:" + max2 + " #preset max:" + this.maxValue + " #offsetValue:" + this.offsetValue);
        } else {
            if (this.reserveCount <= 1) {
                this.leftSB.currPercent = (min2 - this.minValue) / (this.maxValue - this.minValue);
                this.rightSB.currPercent = (max2 - this.minValue) / (this.maxValue - this.minValue);
            } else if ((min2 - this.minValue) % ((float) this.reserveCount) != 0.0f) {
                throw new IllegalArgumentException("setValue() (min - preset min) % reserveCount != 0 . #min:" + min2 + " #preset min:" + this.minValue + "#reserveCount:" + this.reserveCount + "#reserve:" + this.reserveValue);
            } else if ((max2 - this.minValue) % ((float) this.reserveCount) != 0.0f) {
                throw new IllegalArgumentException("setValue() (max - preset min) % reserveCount != 0 . #max:" + max2 + " #preset min:" + this.minValue + "#reserveCount:" + this.reserveCount + "#reserve:" + this.reserveValue);
            } else {
                this.leftSB.currPercent = ((min2 - this.minValue) / ((float) this.reserveCount)) * this.cellsPercent;
                this.rightSB.currPercent = ((max2 - this.minValue) / ((float) this.reserveCount)) * this.cellsPercent;
            }
            invalidate();
        }
    }

    public void setRules(float min, float max) {
        setRules(min, max, (float) this.reserveCount, this.cellsCount);
    }

    public void setRules(float min, float max, float reserve, int cells) {
        if (max <= min) {
            throw new IllegalArgumentException("setRules() max must be greater than min ! #max:" + max + " #min:" + min);
        }
        if (min < 0.0f) {
            this.offsetValue = 0.0f - min;
            min += this.offsetValue;
            max += this.offsetValue;
        }
        this.minValue = min;
        this.maxValue = max;
        if (reserve < 0.0f) {
            throw new IllegalArgumentException("setRules() reserve must be greater than zero ! #reserve:" + reserve);
        } else if (reserve >= max - min) {
            throw new IllegalArgumentException("setRules() reserve must be less than (max - min) ! #reserve:" + reserve + " #max - min:" + (max - min));
        } else if (cells < 1) {
            throw new IllegalArgumentException("setRules() cells must be greater than 1 ! #cells:" + cells);
        } else {
            this.cellsCount = cells;
            this.cellsPercent = 1.0f / ((float) this.cellsCount);
            this.reserveValue = reserve;
            this.reservePercent = reserve / (max - min);
            this.reserveCount = (int) (((float) (this.reservePercent % this.cellsPercent != 0.0f ? 1 : 0)) + (this.reservePercent / this.cellsPercent));
            if (this.cellsCount > 1) {
                if (this.leftSB.currPercent + (this.cellsPercent * ((float) this.reserveCount)) <= 1.0f && this.leftSB.currPercent + (this.cellsPercent * ((float) this.reserveCount)) > this.rightSB.currPercent) {
                    this.rightSB.currPercent = this.leftSB.currPercent + (this.cellsPercent * ((float) this.reserveCount));
                } else if (this.rightSB.currPercent - (this.cellsPercent * ((float) this.reserveCount)) >= 0.0f && this.rightSB.currPercent - (this.cellsPercent * ((float) this.reserveCount)) < this.leftSB.currPercent) {
                    this.leftSB.currPercent = this.rightSB.currPercent - (this.cellsPercent * ((float) this.reserveCount));
                }
            } else if (this.leftSB.currPercent + this.reservePercent <= 1.0f && this.leftSB.currPercent + this.reservePercent > this.rightSB.currPercent) {
                this.rightSB.currPercent = this.leftSB.currPercent + this.reservePercent;
            } else if (this.rightSB.currPercent - this.reservePercent >= 0.0f && this.rightSB.currPercent - this.reservePercent < this.leftSB.currPercent) {
                this.leftSB.currPercent = this.rightSB.currPercent - this.reservePercent;
            }
            invalidate();
        }
    }

    public float[] getCurrentRange() {
        float range = this.maxValue - this.minValue;
        return new float[]{(-this.offsetValue) + this.minValue + (this.leftSB.currPercent * range), (-this.offsetValue) + this.minValue + (this.rightSB.currPercent * range)};
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        if (((float) View.MeasureSpec.getSize(heightMeasureSpec)) * 1.8f > ((float) widthSize)) {
            setMeasuredDimension(widthSize, (int) (((float) widthSize) / 1.8f));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        boolean z;
        super.onSizeChanged(w, h, oldw, oldh);
        int seekBarRadius = h / 2;
        this.lineLeft = seekBarRadius;
        this.lineRight = w - seekBarRadius;
        this.lineTop = seekBarRadius - (seekBarRadius / 4);
        this.lineBottom = (seekBarRadius / 4) + seekBarRadius;
        this.lineWidth = this.lineRight - this.lineLeft;
        this.line.set((float) this.lineLeft, (float) this.lineTop, (float) this.lineRight, (float) this.lineBottom);
        this.lineCorners = (int) (((float) (this.lineBottom - this.lineTop)) * 0.45f);
        this.leftSB.onSizeChanged(seekBarRadius, seekBarRadius, h, this.lineWidth, this.cellsCount > 1, this.seekBarResId, getContext());
        SeekBar seekBar = this.rightSB;
        int i = this.lineWidth;
        if (this.cellsCount > 1) {
            z = true;
        } else {
            z = false;
        }
        seekBar.onSizeChanged(seekBarRadius, seekBarRadius, h, i, z, this.seekBarResId, getContext());
        if (this.cellsCount == 1) {
            this.rightSB.left += this.leftSB.widthSize;
            this.rightSB.right += this.leftSB.widthSize;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(this.colorLineEdge);
        if (this.cellsPercent > 0.0f) {
            this.paint.setStrokeWidth(((float) this.lineCorners) * 0.2f);
            for (int i = 1; i < this.cellsCount; i++) {
                Canvas canvas2 = canvas;
                canvas2.drawLine((((float) i) * this.cellsPercent * ((float) this.lineWidth)) + ((float) this.lineLeft), (float) (this.lineTop - this.lineCorners), (((float) i) * this.cellsPercent * ((float) this.lineWidth)) + ((float) this.lineLeft), (float) (this.lineBottom + this.lineCorners), this.paint);
            }
        }
        canvas.drawRoundRect(this.line, (float) this.lineCorners, (float) this.lineCorners, this.paint);
        this.paint.setColor(this.colorLineSelected);
        Canvas canvas3 = canvas;
        canvas3.drawRect((((float) this.leftSB.lineWidth) * this.leftSB.currPercent) + ((float) (this.leftSB.left + (this.leftSB.widthSize / 2))), 0.5f * (((float) getHeight()) - this.lineHeight), (((float) this.rightSB.lineWidth) * this.rightSB.currPercent) + ((float) (this.rightSB.left + (this.rightSB.widthSize / 2))), 0.5f * (((float) getHeight()) + this.lineHeight), this.paint);
        this.leftSB.draw(canvas);
        this.rightSB.draw(canvas);
    }

    private void drawThumb(float screenCoord, boolean pressed, Canvas canvas) {
        canvas.drawBitmap(pressed ? this.thumbPressedImage : this.thumbImage, screenCoord - this.thumbHalfWidth, (0.5f * ((float) getHeight())) - this.thumbHalfHeight, this.paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float percent;
        float percent2;
        float percent3;
        float percent4;
        switch (event.getAction()) {
            case 0:
                if (this.rightSB.currPercent >= 1.0f && this.leftSB.collide(event)) {
                    this.currTouch = this.leftSB;
                    return true;
                } else if (this.rightSB.collide(event)) {
                    this.currTouch = this.rightSB;
                    return true;
                } else if (!this.leftSB.collide(event)) {
                    return false;
                } else {
                    this.currTouch = this.leftSB;
                    return true;
                }
            case 1:
            case 3:
                this.currTouch.materialRestore();
                if (this.callback != null) {
                    float[] result = getCurrentRange();
                    this.callback.onRangeChanged(this, result[0], result[1]);
                    break;
                }
                break;
            case 2:
                float x = event.getX();
                this.currTouch.material = this.currTouch.material >= 1.0f ? 1.0f : this.currTouch.material + 0.1f;
                if (this.currTouch == this.leftSB) {
                    if (this.cellsCount > 1) {
                        if (x < ((float) this.lineLeft)) {
                            percent4 = 0.0f;
                        } else {
                            percent4 = ((x - ((float) this.lineLeft)) * 1.0f) / ((float) this.lineWidth);
                        }
                        int touchLeftCellsValue = Math.round(percent4 / this.cellsPercent);
                        int currRightCellsValue = Math.round(this.rightSB.currPercent / this.cellsPercent);
                        float f = (float) touchLeftCellsValue;
                        float f2 = this.cellsPercent;
                        while (true) {
                            percent3 = f * f2;
                            if (touchLeftCellsValue > currRightCellsValue - this.reserveCount && touchLeftCellsValue - 1 >= 0) {
                                f = (float) touchLeftCellsValue;
                                f2 = this.cellsPercent;
                            }
                        }
                    } else {
                        if (x < ((float) this.lineLeft)) {
                            percent3 = 0.0f;
                        } else {
                            percent3 = ((x - ((float) this.lineLeft)) * 1.0f) / ((float) (this.lineWidth - this.rightSB.widthSize));
                        }
                        if (percent3 > this.rightSB.currPercent - this.reservePercent) {
                            percent3 = this.rightSB.currPercent - this.reservePercent;
                        }
                    }
                    this.leftSB.slide(percent3);
                } else if (this.currTouch == this.rightSB) {
                    if (this.cellsCount > 1) {
                        if (x > ((float) this.lineRight)) {
                            percent2 = 1.0f;
                        } else {
                            percent2 = ((x - ((float) this.lineLeft)) * 1.0f) / ((float) this.lineWidth);
                        }
                        int touchRightCellsValue = Math.round(percent2 / this.cellsPercent);
                        int currLeftCellsValue = Math.round(this.leftSB.currPercent / this.cellsPercent);
                        float f3 = (float) touchRightCellsValue;
                        float f4 = this.cellsPercent;
                        while (true) {
                            percent = f3 * f4;
                            if (touchRightCellsValue < this.reserveCount + currLeftCellsValue) {
                                touchRightCellsValue++;
                                if (((float) touchRightCellsValue) <= this.maxValue - this.minValue) {
                                    f3 = (float) touchRightCellsValue;
                                    f4 = this.cellsPercent;
                                }
                            }
                        }
                    } else {
                        if (x > ((float) this.lineRight)) {
                            percent = 1.0f;
                        } else {
                            percent = (((x - ((float) this.lineLeft)) - ((float) this.leftSB.widthSize)) * 1.0f) / ((float) (this.lineWidth - this.leftSB.widthSize));
                        }
                        if (percent < this.leftSB.currPercent + this.reservePercent) {
                            percent = this.leftSB.currPercent + this.reservePercent;
                        }
                    }
                    this.rightSB.slide(percent);
                }
                if (this.callback != null) {
                    getCurrentRange();
                }
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        float unused = ss.minValue = this.minValue - this.offsetValue;
        float unused2 = ss.maxValue = this.maxValue - this.offsetValue;
        float unused3 = ss.reserveValue = this.reserveValue;
        int unused4 = ss.cellsCount = this.cellsCount;
        float[] results = getCurrentRange();
        float unused5 = ss.currSelectedMin = results[0];
        float unused6 = ss.currSelectedMax = results[1];
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setRules(ss.minValue, ss.maxValue, ss.reserveValue, ss.cellsCount);
        setValue(ss.currSelectedMin, ss.currSelectedMax);
    }

    private class SavedState extends View.BaseSavedState {
        /* access modifiers changed from: private */
        public int cellsCount;
        /* access modifiers changed from: private */
        public float currSelectedMax;
        /* access modifiers changed from: private */
        public float currSelectedMin;
        /* access modifiers changed from: private */
        public float maxValue;
        /* access modifiers changed from: private */
        public float minValue;
        /* access modifiers changed from: private */
        public float reserveValue;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.minValue = in.readFloat();
            this.maxValue = in.readFloat();
            this.reserveValue = in.readFloat();
            this.cellsCount = in.readInt();
            this.currSelectedMin = in.readFloat();
            this.currSelectedMax = in.readFloat();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.minValue);
            out.writeFloat(this.maxValue);
            out.writeFloat(this.reserveValue);
            out.writeInt(this.cellsCount);
            out.writeFloat(this.currSelectedMin);
            out.writeFloat(this.currSelectedMax);
        }
    }
}
