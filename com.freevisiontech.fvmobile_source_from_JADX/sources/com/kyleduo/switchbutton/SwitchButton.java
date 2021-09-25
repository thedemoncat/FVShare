package com.kyleduo.switchbutton;

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
import android.support.p001v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;

public class SwitchButton extends CompoundButton {
    private static int[] CHECKED_PRESSED_STATE = {16842912, 16842910, 16842919};
    public static final int DEFAULT_ANIMATION_DURATION = 250;
    public static final int DEFAULT_THUMB_MARGIN_DP = 2;
    public static final float DEFAULT_THUMB_RANGE_RATIO = 1.8f;
    public static final int DEFAULT_THUMB_SIZE_DP = 20;
    public static final int DEFAULT_TINT_COLOR = 3309506;
    private static int[] UNCHECKED_PRESSED_STATE = {-16842912, 16842910, 16842919};
    private long mAnimationDuration;
    private ColorStateList mBackColor;
    private Drawable mBackDrawable;
    private int mBackHeight;
    private float mBackRadius;
    private RectF mBackRectF;
    private int mBackWidth;
    private boolean mCatch = false;
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
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
    private float mProgress;
    private ObjectAnimator mProgressAnimator;
    private boolean mReady = false;
    private Paint mRectPaint;
    private boolean mRestoring = false;
    private RectF mSafeRectF;
    private float mStartX;
    private float mStartY;
    private int mTextAdjust;
    private int mTextExtra;
    private float mTextHeight;
    private CharSequence mTextOff;
    private RectF mTextOffRectF;
    private CharSequence mTextOn;
    private RectF mTextOnRectF;
    private TextPaint mTextPaint;
    private int mTextThumbInset;
    private float mTextWidth;
    private ColorStateList mThumbColor;
    private Drawable mThumbDrawable;
    private int mThumbHeight;
    private RectF mThumbMargin;
    private float mThumbRadius;
    private float mThumbRangeRatio;
    private RectF mThumbRectF;
    private int mThumbWidth;
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
        this.mThumbMargin = new RectF();
        this.mTextOnRectF = new RectF();
        this.mTextOffRectF = new RectF();
        this.mProgressAnimator = ObjectAnimator.ofFloat(this, "progress", new float[]{0.0f, 0.0f}).setDuration(250);
        this.mProgressAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mPresentThumbRectF = new RectF();
        Drawable thumbDrawable = null;
        ColorStateList thumbColor = null;
        float margin = getResources().getDisplayMetrics().density * 2.0f;
        float marginLeft = 0.0f;
        float marginRight = 0.0f;
        float marginTop = 0.0f;
        float marginBottom = 0.0f;
        float thumbWidth = 0.0f;
        float thumbHeight = 0.0f;
        float thumbRadius = -1.0f;
        float backRadius = -1.0f;
        Drawable backDrawable = null;
        ColorStateList backColor = null;
        float thumbRangeRatio = 1.8f;
        int animationDuration = 250;
        boolean fadeBack = true;
        int tintColor = 0;
        String textOn = null;
        String textOff = null;
        int textThumbInset = 0;
        int textExtra = 0;
        int textAdjust = 0;
        if (attrs == null) {
            ta = null;
        } else {
            ta = getContext().obtainStyledAttributes(attrs, C1640R.styleable.SwitchButton);
        }
        if (ta != null) {
            thumbDrawable = ta.getDrawable(C1640R.styleable.SwitchButton_kswThumbDrawable);
            thumbColor = ta.getColorStateList(C1640R.styleable.SwitchButton_kswThumbColor);
            float margin2 = ta.getDimension(C1640R.styleable.SwitchButton_kswThumbMargin, margin);
            marginLeft = ta.getDimension(C1640R.styleable.SwitchButton_kswThumbMarginLeft, margin2);
            marginRight = ta.getDimension(C1640R.styleable.SwitchButton_kswThumbMarginRight, margin2);
            marginTop = ta.getDimension(C1640R.styleable.SwitchButton_kswThumbMarginTop, margin2);
            marginBottom = ta.getDimension(C1640R.styleable.SwitchButton_kswThumbMarginBottom, margin2);
            thumbWidth = ta.getDimension(C1640R.styleable.SwitchButton_kswThumbWidth, 0.0f);
            thumbHeight = ta.getDimension(C1640R.styleable.SwitchButton_kswThumbHeight, 0.0f);
            thumbRadius = ta.getDimension(C1640R.styleable.SwitchButton_kswThumbRadius, -1.0f);
            backRadius = ta.getDimension(C1640R.styleable.SwitchButton_kswBackRadius, -1.0f);
            backDrawable = ta.getDrawable(C1640R.styleable.SwitchButton_kswBackDrawable);
            backColor = ta.getColorStateList(C1640R.styleable.SwitchButton_kswBackColor);
            thumbRangeRatio = ta.getFloat(C1640R.styleable.SwitchButton_kswThumbRangeRatio, 1.8f);
            animationDuration = ta.getInteger(C1640R.styleable.SwitchButton_kswAnimationDuration, 250);
            fadeBack = ta.getBoolean(C1640R.styleable.SwitchButton_kswFadeBack, true);
            tintColor = ta.getColor(C1640R.styleable.SwitchButton_kswTintColor, 0);
            textOn = ta.getString(C1640R.styleable.SwitchButton_kswTextOn);
            textOff = ta.getString(C1640R.styleable.SwitchButton_kswTextOff);
            textThumbInset = ta.getDimensionPixelSize(C1640R.styleable.SwitchButton_kswTextThumbInset, 0);
            textExtra = ta.getDimensionPixelSize(C1640R.styleable.SwitchButton_kswTextExtra, 0);
            textAdjust = ta.getDimensionPixelSize(C1640R.styleable.SwitchButton_kswTextAdjust, 0);
            ta.recycle();
        }
        TypedArray ta2 = attrs == null ? null : getContext().obtainStyledAttributes(attrs, new int[]{16842970, 16842981});
        if (ta2 != null) {
            boolean focusable = ta2.getBoolean(0, true);
            boolean clickable = ta2.getBoolean(1, focusable);
            setFocusable(focusable);
            setClickable(clickable);
            ta2.recycle();
        } else {
            setFocusable(true);
            setClickable(true);
        }
        this.mTextOn = textOn;
        this.mTextOff = textOff;
        this.mTextThumbInset = textThumbInset;
        this.mTextExtra = textExtra;
        this.mTextAdjust = textAdjust;
        this.mThumbDrawable = thumbDrawable;
        this.mThumbColor = thumbColor;
        this.mIsThumbUseDrawable = this.mThumbDrawable != null;
        this.mTintColor = tintColor;
        if (this.mTintColor == 0) {
            TypedValue typedValue = new TypedValue();
            if (getContext().getTheme().resolveAttribute(C1640R.attr.colorAccent, typedValue, true)) {
                this.mTintColor = typedValue.data;
            } else {
                this.mTintColor = 3309506;
            }
        }
        if (!this.mIsThumbUseDrawable && this.mThumbColor == null) {
            this.mThumbColor = ColorUtils.generateThumbColorWithTintColor(this.mTintColor);
            this.mCurrThumbColor = this.mThumbColor.getDefaultColor();
        }
        this.mThumbWidth = ceil((double) thumbWidth);
        this.mThumbHeight = ceil((double) thumbHeight);
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
            thumbRangeRatio = Math.max(thumbRangeRatio, 1.0f);
        }
        this.mThumbRangeRatio = thumbRangeRatio;
        this.mThumbRadius = thumbRadius;
        this.mBackRadius = backRadius;
        this.mAnimationDuration = (long) animationDuration;
        this.mFadeBack = fadeBack;
        this.mProgressAnimator.setDuration(this.mAnimationDuration);
        if (isChecked()) {
            setProgress(1.0f);
        }
    }

    private Layout makeLayout(CharSequence text) {
        return new StaticLayout(text, this.mTextPaint, (int) Math.ceil((double) Layout.getDesiredWidth(text, this.mTextPaint)), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float onWidth;
        float offWidth;
        float onHeight;
        float offHeight;
        if (this.mOnLayout == null && !TextUtils.isEmpty(this.mTextOn)) {
            this.mOnLayout = makeLayout(this.mTextOn);
        }
        if (this.mOffLayout == null && !TextUtils.isEmpty(this.mTextOff)) {
            this.mOffLayout = makeLayout(this.mTextOff);
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
        if (onWidth == 0.0f && offWidth == 0.0f) {
            this.mTextWidth = 0.0f;
        } else {
            this.mTextWidth = Math.max(onWidth, offWidth);
        }
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
        if (onHeight == 0.0f && offHeight == 0.0f) {
            this.mTextHeight = 0.0f;
        } else {
            this.mTextHeight = Math.max(onHeight, offHeight);
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int measuredWidth = widthSize;
        if (this.mThumbWidth == 0 && this.mIsThumbUseDrawable) {
            this.mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
        }
        int textWidth = ceil((double) this.mTextWidth);
        if (this.mThumbRangeRatio == 0.0f) {
            this.mThumbRangeRatio = 1.8f;
        }
        if (widthMode == 1073741824) {
            int contentSize = (widthSize - getPaddingLeft()) - getPaddingRight();
            if (this.mThumbWidth != 0) {
                int moveRange = ceil((double) (((float) this.mThumbWidth) * this.mThumbRangeRatio));
                int textExtraSpace = (this.mTextExtra + textWidth) - ((moveRange - this.mThumbWidth) + ceil((double) Math.max(this.mThumbMargin.left, this.mThumbMargin.right)));
                this.mBackWidth = ceil((double) (((float) moveRange) + this.mThumbMargin.left + this.mThumbMargin.right + ((float) Math.max(textExtraSpace, 0))));
                if (this.mBackWidth < 0) {
                    this.mThumbWidth = 0;
                }
                if (((float) moveRange) + Math.max(this.mThumbMargin.left, 0.0f) + Math.max(this.mThumbMargin.right, 0.0f) + ((float) Math.max(textExtraSpace, 0)) > ((float) contentSize)) {
                    this.mThumbWidth = 0;
                }
            }
            if (this.mThumbWidth == 0) {
                int moveRange2 = ceil((double) ((((float) ((widthSize - getPaddingLeft()) - getPaddingRight())) - Math.max(this.mThumbMargin.left, 0.0f)) - Math.max(this.mThumbMargin.right, 0.0f)));
                if (moveRange2 < 0) {
                    this.mThumbWidth = 0;
                    this.mBackWidth = 0;
                    return measuredWidth;
                }
                this.mThumbWidth = ceil((double) (((float) moveRange2) / this.mThumbRangeRatio));
                this.mBackWidth = ceil((double) (((float) moveRange2) + this.mThumbMargin.left + this.mThumbMargin.right));
                if (this.mBackWidth < 0) {
                    this.mThumbWidth = 0;
                    this.mBackWidth = 0;
                    return measuredWidth;
                }
                int textExtraSpace2 = (this.mTextExtra + textWidth) - ((moveRange2 - this.mThumbWidth) + ceil((double) Math.max(this.mThumbMargin.left, this.mThumbMargin.right)));
                if (textExtraSpace2 > 0) {
                    this.mThumbWidth -= textExtraSpace2;
                }
                if (this.mThumbWidth < 0) {
                    this.mThumbWidth = 0;
                    this.mBackWidth = 0;
                    return measuredWidth;
                }
            }
        } else {
            if (this.mThumbWidth == 0) {
                this.mThumbWidth = ceil((double) (getResources().getDisplayMetrics().density * 20.0f));
            }
            if (this.mThumbRangeRatio == 0.0f) {
                this.mThumbRangeRatio = 1.8f;
            }
            int moveRange3 = ceil((double) (((float) this.mThumbWidth) * this.mThumbRangeRatio));
            int textExtraSpace3 = ceil((double) (((float) (this.mTextExtra + textWidth)) - ((((float) (moveRange3 - this.mThumbWidth)) + Math.max(this.mThumbMargin.left, this.mThumbMargin.right)) + ((float) this.mTextThumbInset))));
            this.mBackWidth = ceil((double) (((float) moveRange3) + this.mThumbMargin.left + this.mThumbMargin.right + ((float) Math.max(0, textExtraSpace3))));
            if (this.mBackWidth < 0) {
                this.mThumbWidth = 0;
                this.mBackWidth = 0;
                return measuredWidth;
            }
            int contentSize2 = ceil((double) (((float) moveRange3) + Math.max(0.0f, this.mThumbMargin.left) + Math.max(0.0f, this.mThumbMargin.right) + ((float) Math.max(0, textExtraSpace3))));
            measuredWidth = Math.max(contentSize2, getPaddingLeft() + contentSize2 + getPaddingRight());
        }
        return measuredWidth;
    }

    private int measureHeight(int heightMeasureSpec) {
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int measuredHeight = heightSize;
        if (this.mThumbHeight == 0 && this.mIsThumbUseDrawable) {
            this.mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        }
        if (heightMode == 1073741824) {
            if (this.mThumbHeight != 0) {
                this.mBackHeight = ceil((double) (((float) this.mThumbHeight) + this.mThumbMargin.top + this.mThumbMargin.bottom));
                this.mBackHeight = ceil((double) Math.max((float) this.mBackHeight, this.mTextHeight));
                if ((((float) ((this.mBackHeight + getPaddingTop()) + getPaddingBottom())) - Math.min(0.0f, this.mThumbMargin.top)) - Math.min(0.0f, this.mThumbMargin.bottom) > ((float) heightSize)) {
                    this.mThumbHeight = 0;
                }
            }
            if (this.mThumbHeight == 0) {
                this.mBackHeight = ceil((double) (((float) ((heightSize - getPaddingTop()) - getPaddingBottom())) + Math.min(0.0f, this.mThumbMargin.top) + Math.min(0.0f, this.mThumbMargin.bottom)));
                if (this.mBackHeight < 0) {
                    this.mBackHeight = 0;
                    this.mThumbHeight = 0;
                    return measuredHeight;
                }
                this.mThumbHeight = ceil((double) ((((float) this.mBackHeight) - this.mThumbMargin.top) - this.mThumbMargin.bottom));
            }
            if (this.mThumbHeight < 0) {
                this.mBackHeight = 0;
                this.mThumbHeight = 0;
                return measuredHeight;
            }
        } else {
            if (this.mThumbHeight == 0) {
                this.mThumbHeight = ceil((double) (getResources().getDisplayMetrics().density * 20.0f));
            }
            this.mBackHeight = ceil((double) (((float) this.mThumbHeight) + this.mThumbMargin.top + this.mThumbMargin.bottom));
            if (this.mBackHeight < 0) {
                this.mBackHeight = 0;
                this.mThumbHeight = 0;
                return measuredHeight;
            }
            int textExtraSpace = ceil((double) (this.mTextHeight - ((float) this.mBackHeight)));
            if (textExtraSpace > 0) {
                this.mBackHeight += textExtraSpace;
                this.mThumbHeight += textExtraSpace;
            }
            int contentSize = Math.max(this.mThumbHeight, this.mBackHeight);
            measuredHeight = Math.max(Math.max(contentSize, getPaddingTop() + contentSize + getPaddingBottom()), getSuggestedMinimumHeight());
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

    private int ceil(double dimen) {
        return (int) Math.ceil(dimen);
    }

    private void setup() {
        float thumbTop;
        float thumbLeft;
        if (this.mThumbWidth != 0 && this.mThumbHeight != 0 && this.mBackWidth != 0 && this.mBackHeight != 0) {
            if (this.mThumbRadius == -1.0f) {
                this.mThumbRadius = (float) (Math.min(this.mThumbWidth, this.mThumbHeight) / 2);
            }
            if (this.mBackRadius == -1.0f) {
                this.mBackRadius = (float) (Math.min(this.mBackWidth, this.mBackHeight) / 2);
            }
            int contentWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
            int contentHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
            int drawingWidth = ceil((double) ((((float) this.mBackWidth) - Math.min(0.0f, this.mThumbMargin.left)) - Math.min(0.0f, this.mThumbMargin.right)));
            int drawingHeight = ceil((double) ((((float) this.mBackHeight) - Math.min(0.0f, this.mThumbMargin.top)) - Math.min(0.0f, this.mThumbMargin.bottom)));
            if (contentHeight <= drawingHeight) {
                thumbTop = ((float) getPaddingTop()) + Math.max(0.0f, this.mThumbMargin.top);
            } else {
                thumbTop = ((float) getPaddingTop()) + Math.max(0.0f, this.mThumbMargin.top) + ((float) (((contentHeight - drawingHeight) + 1) / 2));
            }
            if (contentWidth <= this.mBackWidth) {
                thumbLeft = ((float) getPaddingLeft()) + Math.max(0.0f, this.mThumbMargin.left);
            } else {
                thumbLeft = ((float) getPaddingLeft()) + Math.max(0.0f, this.mThumbMargin.left) + ((float) (((contentWidth - drawingWidth) + 1) / 2));
            }
            this.mThumbRectF.set(thumbLeft, thumbTop, ((float) this.mThumbWidth) + thumbLeft, ((float) this.mThumbHeight) + thumbTop);
            float backLeft = this.mThumbRectF.left - this.mThumbMargin.left;
            this.mBackRectF.set(backLeft, this.mThumbRectF.top - this.mThumbMargin.top, ((float) this.mBackWidth) + backLeft, (this.mThumbRectF.top - this.mThumbMargin.top) + ((float) this.mBackHeight));
            this.mSafeRectF.set(this.mThumbRectF.left, 0.0f, (this.mBackRectF.right - this.mThumbMargin.right) - this.mThumbRectF.width(), 0.0f);
            this.mBackRadius = Math.min(Math.min(this.mBackRectF.width(), this.mBackRectF.height()) / 2.0f, this.mBackRadius);
            if (this.mBackDrawable != null) {
                this.mBackDrawable.setBounds((int) this.mBackRectF.left, (int) this.mBackRectF.top, ceil((double) this.mBackRectF.right), ceil((double) this.mBackRectF.bottom));
            }
            if (this.mOnLayout != null) {
                float onLeft = (this.mBackRectF.left + (((((this.mBackRectF.width() + ((float) this.mTextThumbInset)) - ((float) this.mThumbWidth)) - this.mThumbMargin.right) - ((float) this.mOnLayout.getWidth())) / 2.0f)) - ((float) this.mTextAdjust);
                float onTop = this.mBackRectF.top + ((this.mBackRectF.height() - ((float) this.mOnLayout.getHeight())) / 2.0f);
                this.mTextOnRectF.set(onLeft, onTop, ((float) this.mOnLayout.getWidth()) + onLeft, ((float) this.mOnLayout.getHeight()) + onTop);
            }
            if (this.mOffLayout != null) {
                float offLeft = ((this.mBackRectF.right - (((((this.mBackRectF.width() + ((float) this.mTextThumbInset)) - ((float) this.mThumbWidth)) - this.mThumbMargin.left) - ((float) this.mOffLayout.getWidth())) / 2.0f)) - ((float) this.mOffLayout.getWidth())) + ((float) this.mTextAdjust);
                float offTop = this.mBackRectF.top + ((this.mBackRectF.height() - ((float) this.mOffLayout.getHeight())) / 2.0f);
                this.mTextOffRectF.set(offLeft, offTop, ((float) this.mOffLayout.getWidth()) + offLeft, ((float) this.mOffLayout.getHeight()) + offTop);
            }
            this.mReady = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int aboveColor;
        RectF rectF;
        int textColor;
        super.onDraw(canvas);
        if (!this.mReady) {
            setup();
        }
        if (this.mReady) {
            if (this.mIsBackUseDrawable) {
                if (!this.mFadeBack || this.mCurrentBackDrawable == null || this.mNextBackDrawable == null) {
                    this.mBackDrawable.setAlpha(255);
                    this.mBackDrawable.draw(canvas);
                } else {
                    Drawable below = isChecked() ? this.mCurrentBackDrawable : this.mNextBackDrawable;
                    Drawable above = isChecked() ? this.mNextBackDrawable : this.mCurrentBackDrawable;
                    int alpha = (int) (255.0f * getProgress());
                    below.setAlpha(alpha);
                    below.draw(canvas);
                    above.setAlpha(255 - alpha);
                    above.draw(canvas);
                }
            } else if (this.mFadeBack) {
                int belowColor = isChecked() ? this.mCurrBackColor : this.mNextBackColor;
                if (isChecked()) {
                    aboveColor = this.mNextBackColor;
                } else {
                    aboveColor = this.mCurrBackColor;
                }
                int alpha2 = (int) (255.0f * getProgress());
                this.mPaint.setARGB((Color.alpha(belowColor) * alpha2) / 255, Color.red(belowColor), Color.green(belowColor), Color.blue(belowColor));
                canvas.drawRoundRect(this.mBackRectF, this.mBackRadius, this.mBackRadius, this.mPaint);
                this.mPaint.setARGB((Color.alpha(aboveColor) * (255 - alpha2)) / 255, Color.red(aboveColor), Color.green(aboveColor), Color.blue(aboveColor));
                canvas.drawRoundRect(this.mBackRectF, this.mBackRadius, this.mBackRadius, this.mPaint);
                this.mPaint.setAlpha(255);
            } else {
                this.mPaint.setColor(this.mCurrBackColor);
                canvas.drawRoundRect(this.mBackRectF, this.mBackRadius, this.mBackRadius, this.mPaint);
            }
            Layout switchText = ((double) getProgress()) > 0.5d ? this.mOnLayout : this.mOffLayout;
            RectF textRectF = ((double) getProgress()) > 0.5d ? this.mTextOnRectF : this.mTextOffRectF;
            if (!(switchText == null || textRectF == null)) {
                int alpha3 = (int) ((((double) getProgress()) >= 0.75d ? (getProgress() * 4.0f) - 3.0f : ((double) getProgress()) < 0.25d ? 1.0f - (getProgress() * 4.0f) : 0.0f) * 255.0f);
                if (((double) getProgress()) > 0.5d) {
                    textColor = this.mOnTextColor;
                } else {
                    textColor = this.mOffTextColor;
                }
                switchText.getPaint().setARGB((Color.alpha(textColor) * alpha3) / 255, Color.red(textColor), Color.green(textColor), Color.blue(textColor));
                canvas.save();
                canvas.translate(textRectF.left, textRectF.top);
                switchText.draw(canvas);
                canvas.restore();
            }
            this.mPresentThumbRectF.set(this.mThumbRectF);
            this.mPresentThumbRectF.offset(this.mProgress * this.mSafeRectF.width(), 0.0f);
            if (this.mIsThumbUseDrawable) {
                this.mThumbDrawable.setBounds((int) this.mPresentThumbRectF.left, (int) this.mPresentThumbRectF.top, ceil((double) this.mPresentThumbRectF.right), ceil((double) this.mPresentThumbRectF.bottom));
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
                this.mRectPaint.setColor(Color.parseColor("#000000"));
                canvas.drawLine(this.mSafeRectF.left, this.mThumbRectF.top, this.mSafeRectF.right, this.mThumbRectF.top, this.mRectPaint);
                this.mRectPaint.setColor(Color.parseColor("#00CC00"));
                if (((double) getProgress()) > 0.5d) {
                    rectF = this.mTextOnRectF;
                } else {
                    rectF = this.mTextOffRectF;
                }
                canvas.drawRect(rectF, this.mRectPaint);
            }
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
        if (!isEnabled() || !isClickable() || !isFocusable() || !this.mReady) {
            return false;
        }
        int action = event.getAction();
        float deltaX = event.getX() - this.mStartX;
        float deltaY = event.getY() - this.mStartY;
        switch (action) {
            case 0:
                this.mStartX = event.getX();
                this.mStartY = event.getY();
                this.mLastX = this.mStartX;
                setPressed(true);
                break;
            case 1:
            case 3:
                this.mCatch = false;
                setPressed(false);
                float time = (float) (event.getEventTime() - event.getDownTime());
                if (Math.abs(deltaX) < ((float) this.mTouchSlop) && Math.abs(deltaY) < ((float) this.mTouchSlop) && time < ((float) this.mClickTimeout)) {
                    performClick();
                    break;
                } else {
                    boolean nextStatus = getStatusBasedOnPos();
                    if (nextStatus == isChecked()) {
                        animateToState(nextStatus);
                        break;
                    } else {
                        playSoundEffect(0);
                        setChecked(nextStatus);
                        break;
                    }
                }
                break;
            case 2:
                float x = event.getX();
                setProgress(getProgress() + ((x - this.mLastX) / this.mSafeRectF.width()));
                if (!this.mCatch && (Math.abs(deltaX) > ((float) (this.mTouchSlop / 2)) || Math.abs(deltaY) > ((float) (this.mTouchSlop / 2)))) {
                    if (deltaY == 0.0f || Math.abs(deltaX) > Math.abs(deltaY)) {
                        catchView();
                    } else if (Math.abs(deltaY) > Math.abs(deltaX)) {
                        return false;
                    }
                }
                this.mLastX = x;
                break;
        }
        return true;
    }

    private boolean getStatusBasedOnPos() {
        return getProgress() > 0.5f;
    }

    private float getProgress() {
        return this.mProgress;
    }

    private void setProgress(float progress) {
        float tp = progress;
        if (tp > 1.0f) {
            tp = 1.0f;
        } else if (tp < 0.0f) {
            tp = 0.0f;
        }
        this.mProgress = tp;
        invalidate();
    }

    public boolean performClick() {
        return super.performClick();
    }

    /* access modifiers changed from: protected */
    public void animateToState(boolean checked) {
        if (this.mProgressAnimator != null) {
            if (this.mProgressAnimator.isRunning()) {
                this.mProgressAnimator.cancel();
            }
            this.mProgressAnimator.setDuration(this.mAnimationDuration);
            if (checked) {
                this.mProgressAnimator.setFloatValues(new float[]{this.mProgress, 1.0f});
            } else {
                this.mProgressAnimator.setFloatValues(new float[]{this.mProgress, 0.0f});
            }
            this.mProgressAnimator.start();
        }
    }

    private void catchView() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        this.mCatch = true;
    }

    public void setChecked(boolean checked) {
        if (isChecked() != checked) {
            animateToState(checked);
        }
        if (this.mRestoring) {
            setCheckedImmediatelyNoEvent(checked);
        } else {
            super.setChecked(checked);
        }
    }

    public void setCheckedNoEvent(boolean checked) {
        if (this.mChildOnCheckedChangeListener == null) {
            setChecked(checked);
            return;
        }
        super.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        setChecked(checked);
        super.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void setCheckedImmediatelyNoEvent(boolean checked) {
        if (this.mChildOnCheckedChangeListener == null) {
            setCheckedImmediately(checked);
            return;
        }
        super.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        setCheckedImmediately(checked);
        super.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void toggleNoEvent() {
        if (this.mChildOnCheckedChangeListener == null) {
            toggle();
            return;
        }
        super.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        toggle();
        super.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void toggleImmediatelyNoEvent() {
        if (this.mChildOnCheckedChangeListener == null) {
            toggleImmediately();
            return;
        }
        super.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        toggleImmediately();
        super.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        super.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mChildOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void setCheckedImmediately(boolean checked) {
        super.setChecked(checked);
        if (this.mProgressAnimator != null && this.mProgressAnimator.isRunning()) {
            this.mProgressAnimator.cancel();
        }
        setProgress(checked ? 1.0f : 0.0f);
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
        refreshDrawableState();
        this.mReady = false;
        requestLayout();
        invalidate();
    }

    public void setThumbDrawableRes(int thumbDrawableRes) {
        setThumbDrawable(ContextCompat.getDrawable(getContext(), thumbDrawableRes));
    }

    public Drawable getBackDrawable() {
        return this.mBackDrawable;
    }

    public void setBackDrawable(Drawable backDrawable) {
        this.mBackDrawable = backDrawable;
        this.mIsBackUseDrawable = this.mBackDrawable != null;
        refreshDrawableState();
        this.mReady = false;
        requestLayout();
        invalidate();
    }

    public void setBackDrawableRes(int backDrawableRes) {
        setBackDrawable(ContextCompat.getDrawable(getContext(), backDrawableRes));
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
        setBackColor(ContextCompat.getColorStateList(getContext(), backColorRes));
    }

    public ColorStateList getThumbColor() {
        return this.mThumbColor;
    }

    public void setThumbColor(ColorStateList thumbColor) {
        this.mThumbColor = thumbColor;
        if (this.mThumbColor != null) {
            setThumbDrawable((Drawable) null);
        }
        invalidate();
    }

    public void setThumbColorRes(int thumbColorRes) {
        setThumbColor(ContextCompat.getColorStateList(getContext(), thumbColorRes));
    }

    public float getThumbRangeRatio() {
        return this.mThumbRangeRatio;
    }

    public void setThumbRangeRatio(float thumbRangeRatio) {
        this.mThumbRangeRatio = thumbRangeRatio;
        this.mReady = false;
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
        this.mReady = false;
        requestLayout();
    }

    public void setThumbSize(int width, int height) {
        this.mThumbWidth = width;
        this.mThumbHeight = height;
        this.mReady = false;
        requestLayout();
    }

    public float getThumbWidth() {
        return (float) this.mThumbWidth;
    }

    public float getThumbHeight() {
        return (float) this.mThumbHeight;
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
        this.mReady = false;
        requestLayout();
        invalidate();
    }

    public CharSequence getTextOn() {
        return this.mTextOn;
    }

    public CharSequence getTextOff() {
        return this.mTextOff;
    }

    public void setTextThumbInset(int textThumbInset) {
        this.mTextThumbInset = textThumbInset;
        this.mReady = false;
        requestLayout();
        invalidate();
    }

    public void setTextExtra(int textExtra) {
        this.mTextExtra = textExtra;
        this.mReady = false;
        requestLayout();
        invalidate();
    }

    public void setTextAdjust(int textAdjust) {
        this.mTextAdjust = textAdjust;
        this.mReady = false;
        requestLayout();
        invalidate();
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
        this.mRestoring = true;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mRestoring = false;
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
