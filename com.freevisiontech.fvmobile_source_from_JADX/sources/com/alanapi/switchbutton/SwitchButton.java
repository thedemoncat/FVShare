package com.alanapi.switchbutton;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;

public class SwitchButton extends CompoundButton {
    private static int[] CHECKED_PRESSED_STATE = {16842912, 16842910, 16842919};
    public static final int DEFAULT_ANIMATION_DURATION = 250;
    public static final float DEFAULT_BACK_MEASURE_RATIO = 1.8f;
    public static final int DEFAULT_TEXT_MARGIN_DP = 2;
    public static final int DEFAULT_THUMB_MARGIN_DP = 2;
    public static final int DEFAULT_THUMB_SIZE_DP = 20;
    public static final int DEFAULT_TINT_COLOR = 3309506;
    private static int[] UNCHECKED_PRESSED_STATE = {-16842912, 16842910, 16842919};
    private long mAnimationDuration;
    private ColorStateList mBackColor;
    private Drawable mBackDrawable;
    private float mBackMeasureRatio;
    private float mBackRadius;
    private RectF mBackRectF;
    private int mClickTimeout;
    private int mCurrBackColor;
    private int mCurrThumbColor;
    private Drawable mCurrentBackDrawable;
    private boolean mDrawDebugRect = false;
    private boolean mFadeBack;
    private boolean mIsBackUseDrawable;
    private boolean mIsThumbUseDrawable;
    private float mLastX;
    private int mNextBackColor;
    private Drawable mNextBackDrawable;
    private Layout mOffLayout;
    private int mOffTextColor;
    private Layout mOnLayout;
    private int mOnTextColor;
    private Paint mPaint;
    private RectF mPresentThumbRectF;
    private float mProcess;
    private ObjectAnimator mProcessAnimator;
    private Paint mRectPaint;
    private RectF mSafeRectF;
    private float mStartX;
    private float mStartY;
    private float mTextHeight;
    private float mTextMarginH;
    private CharSequence mTextOff;
    private RectF mTextOffRectF;
    private CharSequence mTextOn;
    private RectF mTextOnRectF;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private ColorStateList mThumbColor;
    private Drawable mThumbDrawable;
    private RectF mThumbMargin;
    private float mThumbRadius;
    private RectF mThumbRectF;
    private PointF mThumbSizeF;
    private int mTintColor;
    private int mTouchSlop;

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SwitchButton(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta;
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
        this.mPaint = new Paint(1);
        this.mRectPaint = new Paint(1);
        this.mRectPaint.setStyle(Paint.Style.STROKE);
        this.mRectPaint.setStrokeWidth(getResources().getDisplayMetrics().density);
        this.mTextPaint = getPaint();
        this.mThumbRectF = new RectF();
        this.mBackRectF = new RectF();
        this.mSafeRectF = new RectF();
        this.mThumbSizeF = new PointF();
        this.mThumbMargin = new RectF();
        this.mTextOnRectF = new RectF();
        this.mTextOffRectF = new RectF();
        this.mProcessAnimator = ObjectAnimator.ofFloat(this, "process", new float[]{0.0f, 0.0f}).setDuration(250);
        this.mProcessAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mPresentThumbRectF = new RectF();
        float density = getResources().getDisplayMetrics().density;
        Drawable thumbDrawable = null;
        ColorStateList thumbColor = null;
        float margin = density * 2.0f;
        float marginLeft = 0.0f;
        float marginRight = 0.0f;
        float marginTop = 0.0f;
        float marginBottom = 0.0f;
        float thumbWidth = density * 20.0f;
        float thumbHeight = density * 20.0f;
        float thumbRadius = (20.0f * density) / 2.0f;
        float backRadius = thumbRadius;
        Drawable backDrawable = null;
        ColorStateList backColor = null;
        float backMeasureRatio = 1.8f;
        int animationDuration = 250;
        boolean fadeBack = true;
        int tintColor = Integer.MIN_VALUE;
        String textOn = null;
        String textOff = null;
        float textMarginH = density * 2.0f;
        if (attrs == null) {
            ta = null;
        } else {
            ta = getContext().obtainStyledAttributes(attrs, C0786R.styleable.SwitchButton);
        }
        if (ta != null) {
            thumbDrawable = ta.getDrawable(C0786R.styleable.SwitchButton_swThumbDrawable);
            thumbColor = ta.getColorStateList(C0786R.styleable.SwitchButton_swThumbColor);
            float margin2 = ta.getDimension(C0786R.styleable.SwitchButton_swThumbMargin, margin);
            marginLeft = ta.getDimension(C0786R.styleable.SwitchButton_swThumbMarginLeft, margin2);
            marginRight = ta.getDimension(C0786R.styleable.SwitchButton_swThumbMarginRight, margin2);
            marginTop = ta.getDimension(C0786R.styleable.SwitchButton_swThumbMarginTop, margin2);
            marginBottom = ta.getDimension(C0786R.styleable.SwitchButton_swThumbMarginBottom, margin2);
            thumbWidth = ta.getDimension(C0786R.styleable.SwitchButton_swThumbWidth, thumbWidth);
            thumbHeight = ta.getDimension(C0786R.styleable.SwitchButton_swThumbHeight, thumbHeight);
            thumbRadius = ta.getDimension(C0786R.styleable.SwitchButton_swThumbRadius, Math.min(thumbWidth, thumbHeight) / 2.0f);
            backRadius = ta.getDimension(C0786R.styleable.SwitchButton_swBackRadius, (2.0f * density) + thumbRadius);
            backDrawable = ta.getDrawable(C0786R.styleable.SwitchButton_swBackDrawable);
            backColor = ta.getColorStateList(C0786R.styleable.SwitchButton_swBackColor);
            backMeasureRatio = ta.getFloat(C0786R.styleable.SwitchButton_swBackMeasureRatio, 1.8f);
            animationDuration = ta.getInteger(C0786R.styleable.SwitchButton_swAnimationDuration, 250);
            fadeBack = ta.getBoolean(C0786R.styleable.SwitchButton_swFadeBack, true);
            tintColor = ta.getColor(C0786R.styleable.SwitchButton_swTintColor, Integer.MIN_VALUE);
            textOn = ta.getString(C0786R.styleable.SwitchButton_swTextOn);
            textOff = ta.getString(C0786R.styleable.SwitchButton_swTextOff);
            textMarginH = ta.getDimension(C0786R.styleable.SwitchButton_swTextMarginH, textMarginH);
            ta.recycle();
        }
        this.mTextOn = textOn;
        this.mTextOff = textOff;
        this.mTextMarginH = textMarginH;
        this.mThumbDrawable = thumbDrawable;
        this.mThumbColor = thumbColor;
        this.mIsThumbUseDrawable = this.mThumbDrawable != null;
        this.mTintColor = tintColor;
        if (this.mTintColor == Integer.MIN_VALUE) {
            this.mTintColor = 3309506;
        }
        if (!this.mIsThumbUseDrawable && this.mThumbColor == null) {
            this.mThumbColor = ColorUtils.generateThumbColorWithTintColor(this.mTintColor);
            this.mCurrThumbColor = this.mThumbColor.getDefaultColor();
        }
        if (this.mIsThumbUseDrawable) {
            thumbWidth = Math.max(thumbWidth, (float) this.mThumbDrawable.getMinimumWidth());
            thumbHeight = Math.max(thumbHeight, (float) this.mThumbDrawable.getMinimumHeight());
        }
        this.mThumbSizeF.set(thumbWidth, thumbHeight);
        this.mBackDrawable = backDrawable;
        this.mBackColor = backColor;
        this.mIsBackUseDrawable = this.mBackDrawable != null;
        if (!this.mIsBackUseDrawable && this.mBackColor == null) {
            this.mBackColor = ColorUtils.generateBackColorWithTintColor(this.mTintColor);
            this.mCurrBackColor = this.mBackColor.getDefaultColor();
            this.mNextBackColor = this.mBackColor.getColorForState(CHECKED_PRESSED_STATE, this.mCurrBackColor);
        }
        this.mThumbMargin.set(marginLeft, marginTop, marginRight, marginBottom);
        if (this.mThumbMargin.width() >= 0.0f) {
            backMeasureRatio = Math.max(backMeasureRatio, 1.0f);
        }
        this.mBackMeasureRatio = backMeasureRatio;
        this.mThumbRadius = thumbRadius;
        this.mBackRadius = backRadius;
        this.mAnimationDuration = (long) animationDuration;
        this.mFadeBack = fadeBack;
        this.mProcessAnimator.setDuration(this.mAnimationDuration);
        setFocusable(true);
        setClickable(true);
        if (isChecked()) {
            setProcess(1.0f);
        }
    }

    private Layout makeLayout(CharSequence text) {
        return new StaticLayout(text, this.mTextPaint, (int) Math.ceil((double) Layout.getDesiredWidth(text, this.mTextPaint)), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mOnLayout == null && this.mTextOn != null) {
            this.mOnLayout = makeLayout(this.mTextOn);
        }
        if (this.mOffLayout == null && this.mTextOff != null) {
            this.mOffLayout = makeLayout(this.mTextOff);
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        float onWidth;
        float offWidth;
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int minWidth = (int) (this.mThumbSizeF.x * this.mBackMeasureRatio);
        if (this.mIsBackUseDrawable) {
            minWidth = Math.max(minWidth, this.mBackDrawable.getMinimumWidth());
        }
        if (this.mOnLayout != null) {
            onWidth = (float) this.mOnLayout.getWidth();
        } else {
            onWidth = 0.0f;
        }
        if (this.mOffLayout != null) {
            offWidth = (float) this.mOffLayout.getWidth();
        } else {
            offWidth = 0.0f;
        }
        if (!(onWidth == 0.0f && offWidth == 0.0f)) {
            this.mTextWidth = Math.max(onWidth, offWidth) + (this.mTextMarginH * 2.0f);
            float left = ((float) minWidth) - this.mThumbSizeF.x;
            if (left < this.mTextWidth) {
                minWidth = (int) (((float) minWidth) + (this.mTextWidth - left));
            }
        }
        int minWidth2 = Math.max(minWidth, (int) (((float) minWidth) + this.mThumbMargin.left + this.mThumbMargin.right));
        int minWidth3 = Math.max(Math.max(minWidth2, getPaddingLeft() + minWidth2 + getPaddingRight()), getSuggestedMinimumWidth());
        if (widthMode == 1073741824) {
            return Math.max(minWidth3, widthSize);
        }
        int measuredWidth = minWidth3;
        if (widthMode == Integer.MIN_VALUE) {
            return Math.min(measuredWidth, widthSize);
        }
        return measuredWidth;
    }

    private int measureHeight(int heightMeasureSpec) {
        float onHeight;
        float offHeight;
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int minHeight = (int) Math.max(this.mThumbSizeF.y, this.mThumbSizeF.y + this.mThumbMargin.top + this.mThumbMargin.right);
        if (this.mOnLayout != null) {
            onHeight = (float) this.mOnLayout.getHeight();
        } else {
            onHeight = 0.0f;
        }
        if (this.mOffLayout != null) {
            offHeight = (float) this.mOffLayout.getHeight();
        } else {
            offHeight = 0.0f;
        }
        if (!(onHeight == 0.0f && offHeight == 0.0f)) {
            this.mTextHeight = Math.max(onHeight, offHeight);
            minHeight = (int) Math.max((float) minHeight, this.mTextHeight);
        }
        int minHeight2 = Math.max(minHeight, getSuggestedMinimumHeight());
        int minHeight3 = Math.max(minHeight2, getPaddingTop() + minHeight2 + getPaddingBottom());
        if (heightMode == 1073741824) {
            return Math.max(minHeight3, heightSize);
        }
        int measuredHeight = minHeight3;
        if (heightMode == Integer.MIN_VALUE) {
            return Math.min(measuredHeight, heightSize);
        }
        return measuredHeight;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            setup();
        }
    }

    private void setup() {
        float thumbTop = ((float) getPaddingTop()) + Math.max(0.0f, this.mThumbMargin.top);
        float thumbLeft = ((float) getPaddingLeft()) + Math.max(0.0f, this.mThumbMargin.left);
        if (!(this.mOnLayout == null || this.mOffLayout == null || this.mThumbMargin.top + this.mThumbMargin.bottom <= 0.0f)) {
            thumbTop += (((((float) ((getMeasuredHeight() - getPaddingBottom()) - getPaddingTop())) - this.mThumbSizeF.y) - this.mThumbMargin.top) - this.mThumbMargin.bottom) / 2.0f;
        }
        if (this.mIsThumbUseDrawable) {
            this.mThumbSizeF.x = Math.max(this.mThumbSizeF.x, (float) this.mThumbDrawable.getMinimumWidth());
            this.mThumbSizeF.y = Math.max(this.mThumbSizeF.y, (float) this.mThumbDrawable.getMinimumHeight());
        }
        this.mThumbRectF.set(thumbLeft, thumbTop, this.mThumbSizeF.x + thumbLeft, this.mThumbSizeF.y + thumbTop);
        float backLeft = this.mThumbRectF.left - this.mThumbMargin.left;
        float textDiffWidth = Math.min(0.0f, ((Math.max(this.mThumbSizeF.x * this.mBackMeasureRatio, this.mThumbSizeF.x + this.mTextWidth) - this.mThumbRectF.width()) - this.mTextWidth) / 2.0f);
        float textDiffHeight = Math.min(0.0f, (((this.mThumbRectF.height() + this.mThumbMargin.top) + this.mThumbMargin.bottom) - this.mTextHeight) / 2.0f);
        this.mBackRectF.set(backLeft + textDiffWidth, (this.mThumbRectF.top - this.mThumbMargin.top) + textDiffHeight, (((this.mThumbMargin.left + backLeft) + Math.max(this.mThumbSizeF.x * this.mBackMeasureRatio, this.mThumbSizeF.x + this.mTextWidth)) + this.mThumbMargin.right) - textDiffWidth, (this.mThumbRectF.bottom + this.mThumbMargin.bottom) - textDiffHeight);
        this.mSafeRectF.set(this.mThumbRectF.left, 0.0f, (this.mBackRectF.right - this.mThumbMargin.right) - this.mThumbRectF.width(), 0.0f);
        this.mBackRadius = Math.min(Math.min(this.mBackRectF.width(), this.mBackRectF.height()) / 2.0f, this.mBackRadius);
        if (this.mBackDrawable != null) {
            this.mBackDrawable.setBounds((int) this.mBackRectF.left, (int) this.mBackRectF.top, (int) this.mBackRectF.right, (int) this.mBackRectF.bottom);
        }
        if (this.mOnLayout != null) {
            float marginOnX = ((this.mBackRectF.left + (((this.mBackRectF.width() - this.mThumbRectF.width()) - ((float) this.mOnLayout.getWidth())) / 2.0f)) - this.mThumbMargin.left) + (((float) (this.mThumbMargin.left > 0.0f ? 1 : -1)) * this.mTextMarginH);
            float marginOnY = this.mBackRectF.top + ((this.mBackRectF.height() - ((float) this.mOnLayout.getHeight())) / 2.0f);
            this.mTextOnRectF.set(marginOnX, marginOnY, ((float) this.mOnLayout.getWidth()) + marginOnX, ((float) this.mOnLayout.getHeight()) + marginOnY);
        }
        if (this.mOffLayout != null) {
            float marginOffX = (((this.mBackRectF.right - (((this.mBackRectF.width() - this.mThumbRectF.width()) - ((float) this.mOffLayout.getWidth())) / 2.0f)) + this.mThumbMargin.right) - ((float) this.mOffLayout.getWidth())) - (((float) (this.mThumbMargin.right > 0.0f ? 1 : -1)) * this.mTextMarginH);
            float marginOffY = this.mBackRectF.top + ((this.mBackRectF.height() - ((float) this.mOffLayout.getHeight())) / 2.0f);
            this.mTextOffRectF.set(marginOffX, marginOffY, ((float) this.mOffLayout.getWidth()) + marginOffX, ((float) this.mOffLayout.getHeight()) + marginOffY);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float process;
        super.onDraw(canvas);
        if (this.mIsBackUseDrawable) {
            if (!this.mFadeBack || this.mCurrentBackDrawable == null || this.mNextBackDrawable == null) {
                this.mBackDrawable.setAlpha(255);
                this.mBackDrawable.draw(canvas);
            } else {
                int alpha = (int) ((isChecked() ? getProcess() : 1.0f - getProcess()) * 255.0f);
                this.mCurrentBackDrawable.setAlpha(alpha);
                this.mCurrentBackDrawable.draw(canvas);
                this.mNextBackDrawable.setAlpha(255 - alpha);
                this.mNextBackDrawable.draw(canvas);
            }
        } else if (this.mFadeBack) {
            if (isChecked()) {
                process = getProcess();
            } else {
                process = 1.0f - getProcess();
            }
            int alpha2 = (int) (process * 255.0f);
            this.mPaint.setARGB((Color.alpha(this.mCurrBackColor) * alpha2) / 255, Color.red(this.mCurrBackColor), Color.green(this.mCurrBackColor), Color.blue(this.mCurrBackColor));
            canvas.drawRoundRect(this.mBackRectF, this.mBackRadius, this.mBackRadius, this.mPaint);
            this.mPaint.setARGB((Color.alpha(this.mNextBackColor) * (255 - alpha2)) / 255, Color.red(this.mNextBackColor), Color.green(this.mNextBackColor), Color.blue(this.mNextBackColor));
            canvas.drawRoundRect(this.mBackRectF, this.mBackRadius, this.mBackRadius, this.mPaint);
            this.mPaint.setAlpha(255);
        } else {
            this.mPaint.setColor(this.mCurrBackColor);
            canvas.drawRoundRect(this.mBackRectF, this.mBackRadius, this.mBackRadius, this.mPaint);
        }
        Layout switchText = ((double) getProcess()) > 0.5d ? this.mOnLayout : this.mOffLayout;
        RectF textRectF = ((double) getProcess()) > 0.5d ? this.mTextOnRectF : this.mTextOffRectF;
        if (!(switchText == null || textRectF == null)) {
            int alpha3 = (int) ((((double) getProcess()) >= 0.75d ? (getProcess() * 4.0f) - 3.0f : ((double) getProcess()) < 0.25d ? 1.0f - (getProcess() * 4.0f) : 0.0f) * 255.0f);
            int textColor = ((double) getProcess()) > 0.5d ? this.mOnTextColor : this.mOffTextColor;
            switchText.getPaint().setARGB((Color.alpha(textColor) * alpha3) / 255, Color.red(textColor), Color.green(textColor), Color.blue(textColor));
            canvas.save();
            canvas.translate(textRectF.left, textRectF.top);
            switchText.draw(canvas);
            canvas.restore();
        }
        this.mPresentThumbRectF.set(this.mThumbRectF);
        this.mPresentThumbRectF.offset(this.mProcess * this.mSafeRectF.width(), 0.0f);
        if (this.mIsThumbUseDrawable) {
            this.mThumbDrawable.setBounds((int) this.mPresentThumbRectF.left, (int) this.mPresentThumbRectF.top, (int) this.mPresentThumbRectF.right, (int) this.mPresentThumbRectF.bottom);
            this.mThumbDrawable.draw(canvas);
        } else {
            this.mPaint.setColor(this.mCurrThumbColor);
            canvas.drawRoundRect(this.mPresentThumbRectF, this.mThumbRadius, this.mThumbRadius, this.mPaint);
        }
        if (this.mDrawDebugRect) {
            this.mRectPaint.setColor(Color.parseColor("#AA0000"));
            canvas.drawRect(this.mBackRectF, this.mRectPaint);
            this.mRectPaint.setColor(Color.parseColor("#0000FF"));
            canvas.drawRect(this.mPresentThumbRectF, this.mRectPaint);
            this.mRectPaint.setColor(Color.parseColor("#00CC00"));
            canvas.drawRect(((double) getProcess()) > 0.5d ? this.mTextOnRectF : this.mTextOffRectF, this.mRectPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mIsThumbUseDrawable || this.mThumbColor == null) {
            setDrawableState(this.mThumbDrawable);
        } else {
            this.mCurrThumbColor = this.mThumbColor.getColorForState(getDrawableState(), this.mCurrThumbColor);
        }
        int[] nextState = isChecked() ? UNCHECKED_PRESSED_STATE : CHECKED_PRESSED_STATE;
        ColorStateList textColors = getTextColors();
        if (textColors != null) {
            int defaultTextColor = textColors.getDefaultColor();
            this.mOnTextColor = textColors.getColorForState(CHECKED_PRESSED_STATE, defaultTextColor);
            this.mOffTextColor = textColors.getColorForState(UNCHECKED_PRESSED_STATE, defaultTextColor);
        }
        if (this.mIsBackUseDrawable || this.mBackColor == null) {
            if (!(this.mBackDrawable instanceof StateListDrawable) || !this.mFadeBack) {
                this.mNextBackDrawable = null;
            } else {
                this.mBackDrawable.setState(nextState);
                this.mNextBackDrawable = this.mBackDrawable.getCurrent().mutate();
            }
            setDrawableState(this.mBackDrawable);
            if (this.mBackDrawable != null) {
                this.mCurrentBackDrawable = this.mBackDrawable.getCurrent().mutate();
                return;
            }
            return;
        }
        this.mCurrBackColor = this.mBackColor.getColorForState(getDrawableState(), this.mCurrBackColor);
        this.mNextBackColor = this.mBackColor.getColorForState(nextState, this.mCurrBackColor);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || !isClickable()) {
            return false;
        }
        int action = event.getAction();
        float deltaX = event.getX() - this.mStartX;
        float deltaY = event.getY() - this.mStartY;
        switch (action) {
            case 0:
                catchView();
                this.mStartX = event.getX();
                this.mStartY = event.getY();
                this.mLastX = this.mStartX;
                setPressed(true);
                return true;
            case 1:
            case 3:
                setPressed(false);
                boolean nextStatus = getStatusBasedOnPos();
                float time = (float) (event.getEventTime() - event.getDownTime());
                if (deltaX < ((float) this.mTouchSlop) && deltaY < ((float) this.mTouchSlop) && time < ((float) this.mClickTimeout)) {
                    performClick();
                    return true;
                } else if (nextStatus != isChecked()) {
                    playSoundEffect(0);
                    setChecked(nextStatus);
                    return true;
                } else {
                    animateToState(nextStatus);
                    return true;
                }
            case 2:
                float x = event.getX();
                setProcess(getProcess() + ((x - this.mLastX) / this.mSafeRectF.width()));
                this.mLastX = x;
                return true;
            default:
                return true;
        }
    }

    private boolean getStatusBasedOnPos() {
        return getProcess() > 0.5f;
    }

    public final float getProcess() {
        return this.mProcess;
    }

    public final void setProcess(float process) {
        float tp = process;
        if (tp > 1.0f) {
            tp = 1.0f;
        } else if (tp < 0.0f) {
            tp = 0.0f;
        }
        this.mProcess = tp;
        invalidate();
    }

    public boolean performClick() {
        return super.performClick();
    }

    /* access modifiers changed from: protected */
    public void animateToState(boolean checked) {
        if (this.mProcessAnimator != null) {
            if (this.mProcessAnimator.isRunning()) {
                this.mProcessAnimator.cancel();
            }
            this.mProcessAnimator.setDuration(this.mAnimationDuration);
            if (checked) {
                this.mProcessAnimator.setFloatValues(new float[]{this.mProcess, 1.0f});
            } else {
                this.mProcessAnimator.setFloatValues(new float[]{this.mProcess, 0.0f});
            }
            this.mProcessAnimator.start();
        }
    }

    private void catchView() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    public void setChecked(boolean checked) {
        if (isChecked() != checked) {
            animateToState(checked);
        }
        super.setChecked(checked);
    }

    public void setCheckedImmediately(boolean checked) {
        super.setChecked(checked);
        if (this.mProcessAnimator != null && this.mProcessAnimator.isRunning()) {
            this.mProcessAnimator.cancel();
        }
        setProcess(checked ? 1.0f : 0.0f);
        invalidate();
    }

    public void toggleImmediately() {
        setCheckedImmediately(!isChecked());
    }

    private void setDrawableState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(getDrawableState());
            invalidate();
        }
    }

    public boolean isDrawDebugRect() {
        return this.mDrawDebugRect;
    }

    public void setDrawDebugRect(boolean drawDebugRect) {
        this.mDrawDebugRect = drawDebugRect;
        invalidate();
    }

    public long getAnimationDuration() {
        return this.mAnimationDuration;
    }

    public void setAnimationDuration(long animationDuration) {
        this.mAnimationDuration = animationDuration;
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public void setThumbDrawable(Drawable thumbDrawable) {
        this.mThumbDrawable = thumbDrawable;
        this.mIsThumbUseDrawable = this.mThumbDrawable != null;
        setup();
        refreshDrawableState();
        requestLayout();
        invalidate();
    }

    public void setThumbDrawableRes(int thumbDrawableRes) {
        setThumbDrawable(getResources().getDrawable(thumbDrawableRes));
    }

    public Drawable getBackDrawable() {
        return this.mBackDrawable;
    }

    public void setBackDrawable(Drawable backDrawable) {
        this.mBackDrawable = backDrawable;
        this.mIsBackUseDrawable = this.mBackDrawable != null;
        setup();
        refreshDrawableState();
        requestLayout();
        invalidate();
    }

    public void setBackDrawableRes(int backDrawableRes) {
        setBackDrawable(getResources().getDrawable(backDrawableRes));
    }

    public ColorStateList getBackColor() {
        return this.mBackColor;
    }

    public void setBackColor(ColorStateList backColor) {
        this.mBackColor = backColor;
        if (this.mBackColor != null) {
            setBackDrawable((Drawable) null);
        }
        invalidate();
    }

    public void setBackColorRes(int backColorRes) {
        setBackColor(getResources().getColorStateList(backColorRes));
    }

    public ColorStateList getThumbColor() {
        return this.mThumbColor;
    }

    public void setThumbColor(ColorStateList thumbColor) {
        this.mThumbColor = thumbColor;
        if (this.mThumbColor != null) {
            setThumbDrawable((Drawable) null);
        }
    }

    public void setThumbColorRes(int thumbColorRes) {
        setThumbColor(getResources().getColorStateList(thumbColorRes));
    }

    public float getBackMeasureRatio() {
        return this.mBackMeasureRatio;
    }

    public void setBackMeasureRatio(float backMeasureRatio) {
        this.mBackMeasureRatio = backMeasureRatio;
        requestLayout();
    }

    public RectF getThumbMargin() {
        return this.mThumbMargin;
    }

    public void setThumbMargin(RectF thumbMargin) {
        if (thumbMargin == null) {
            setThumbMargin(0.0f, 0.0f, 0.0f, 0.0f);
        } else {
            setThumbMargin(thumbMargin.left, thumbMargin.top, thumbMargin.right, thumbMargin.bottom);
        }
    }

    public void setThumbMargin(float left, float top, float right, float bottom) {
        this.mThumbMargin.set(left, top, right, bottom);
        requestLayout();
    }

    public void setThumbSize(float width, float height) {
        this.mThumbSizeF.set(width, height);
        setup();
        requestLayout();
    }

    public float getThumbWidth() {
        return this.mThumbSizeF.x;
    }

    public float getThumbHeight() {
        return this.mThumbSizeF.y;
    }

    public void setThumbSize(PointF size) {
        if (size == null) {
            float defaultSize = getResources().getDisplayMetrics().density * 20.0f;
            setThumbSize(defaultSize, defaultSize);
            return;
        }
        setThumbSize(size.x, size.y);
    }

    public PointF getThumbSizeF() {
        return this.mThumbSizeF;
    }

    public float getThumbRadius() {
        return this.mThumbRadius;
    }

    public void setThumbRadius(float thumbRadius) {
        this.mThumbRadius = thumbRadius;
        if (!this.mIsThumbUseDrawable) {
            invalidate();
        }
    }

    public PointF getBackSizeF() {
        return new PointF(this.mBackRectF.width(), this.mBackRectF.height());
    }

    public float getBackRadius() {
        return this.mBackRadius;
    }

    public void setBackRadius(float backRadius) {
        this.mBackRadius = backRadius;
        if (!this.mIsBackUseDrawable) {
            invalidate();
        }
    }

    public boolean isFadeBack() {
        return this.mFadeBack;
    }

    public void setFadeBack(boolean fadeBack) {
        this.mFadeBack = fadeBack;
    }

    public int getTintColor() {
        return this.mTintColor;
    }

    public void setTintColor(int tintColor) {
        this.mTintColor = tintColor;
        this.mThumbColor = ColorUtils.generateThumbColorWithTintColor(this.mTintColor);
        this.mBackColor = ColorUtils.generateBackColorWithTintColor(this.mTintColor);
        this.mIsBackUseDrawable = false;
        this.mIsThumbUseDrawable = false;
        refreshDrawableState();
        invalidate();
    }

    public void setText(CharSequence onText, CharSequence offText) {
        this.mTextOn = onText;
        this.mTextOff = offText;
        this.mOnLayout = null;
        this.mOffLayout = null;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.onText = this.mTextOn;
        ss.offText = this.mTextOff;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        setText(ss.onText, ss.offText);
        super.onRestoreInstanceState(ss.getSuperState());
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        CharSequence offText;
        CharSequence onText;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.onText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.offText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            TextUtils.writeToParcel(this.onText, out, flags);
            TextUtils.writeToParcel(this.offText, out, flags);
        }
    }
}
