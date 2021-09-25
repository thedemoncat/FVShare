package com.freevisiontech.fvmobile.widget.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.freevisiontech.fvmobile.C0853R;
import java.util.ArrayList;
import java.util.List;

public abstract class ScrollPickerView<T> extends View {
    /* access modifiers changed from: private */
    public static final SlotInterpolator sAutoScrollInterpolator = new SlotInterpolator();
    private ValueAnimator mAutoScrollAnimator;
    /* access modifiers changed from: private */
    public boolean mCanTap;
    private Drawable mCenterItemBackground;
    /* access modifiers changed from: private */
    public int mCenterPoint;
    private int mCenterPosition;
    /* access modifiers changed from: private */
    public int mCenterX;
    /* access modifiers changed from: private */
    public int mCenterY;
    private List<T> mData;
    /* access modifiers changed from: private */
    public boolean mDisallowInterceptTouch;
    /* access modifiers changed from: private */
    public boolean mDisallowTouch;
    private boolean mDrawAllItem;
    private GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    public boolean mIsAutoScrolling;
    private boolean mIsCirculation;
    private boolean mIsFling;
    /* access modifiers changed from: private */
    public boolean mIsHorizontal;
    /* access modifiers changed from: private */
    public boolean mIsInertiaScroll;
    private boolean mIsMovingCenter;
    private int mItemHeight;
    /* access modifiers changed from: private */
    public int mItemSize;
    private int mItemWidth;
    /* access modifiers changed from: private */
    public float mLastMoveX;
    /* access modifiers changed from: private */
    public float mLastMoveY;
    private int mLastScrollX;
    private int mLastScrollY;
    /* access modifiers changed from: private */
    public OnSelectedListener mListener;
    /* access modifiers changed from: private */
    public float mMoveLength;
    private Paint mPaint;
    private Scroller mScroller;
    /* access modifiers changed from: private */
    public int mSelected;
    private int mVisibleItemCount;

    public interface OnSelectedListener {
        void onSelected(ScrollPickerView scrollPickerView, int i);
    }

    public abstract void drawItem(Canvas canvas, List<T> list, int i, int i2, float f, float f2);

    public ScrollPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mVisibleItemCount = 3;
        this.mIsInertiaScroll = true;
        this.mIsCirculation = true;
        this.mDisallowInterceptTouch = false;
        this.mItemHeight = 0;
        this.mItemWidth = 0;
        this.mCenterPosition = -1;
        this.mMoveLength = 0.0f;
        this.mLastScrollY = 0;
        this.mLastScrollX = 0;
        this.mDisallowTouch = false;
        this.mCenterItemBackground = null;
        this.mCanTap = true;
        this.mIsHorizontal = false;
        this.mDrawAllItem = false;
        this.mIsAutoScrolling = false;
        this.mGestureDetector = new GestureDetector(getContext(), new FlingOnGestureListener());
        this.mScroller = new Scroller(getContext());
        this.mAutoScrollAnimator = ValueAnimator.ofInt(new int[]{0, 0});
        this.mPaint = new Paint(1);
        this.mPaint.setStyle(Paint.Style.FILL);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int i = 2;
        boolean z = true;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, C0853R.styleable.ScrollPickerView);
            if (typedArray.hasValue(0)) {
                setCenterItemBackground(typedArray.getDrawable(0));
            }
            setVisibleItemCount(typedArray.getInt(1, getVisibleItemCount()));
            setCenterPosition(typedArray.getInt(2, getCenterPosition()));
            setIsCirculation(typedArray.getBoolean(3, isIsCirculation()));
            setDisallowInterceptTouch(typedArray.getBoolean(4, isDisallowInterceptTouch()));
            if (this.mIsHorizontal) {
                i = 1;
            }
            if (typedArray.getInt(5, i) != 1) {
                z = false;
            }
            setHorizontal(z);
            typedArray.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mData != null && this.mData.size() > 0) {
            if (this.mCenterItemBackground != null) {
                this.mCenterItemBackground.draw(canvas);
            }
            int start = Math.min(Math.max(this.mCenterPosition + 1, this.mVisibleItemCount - this.mCenterPosition), this.mData.size());
            if (this.mDrawAllItem) {
                start = this.mData.size();
            }
            for (int i = start; i >= 1; i--) {
                if (this.mDrawAllItem || i <= this.mCenterPosition + 1) {
                    int position = this.mSelected - i < 0 ? (this.mData.size() + this.mSelected) - i : this.mSelected - i;
                    if (this.mIsCirculation) {
                        drawItem(canvas, this.mData, position, -i, this.mMoveLength, (((float) this.mCenterPoint) + this.mMoveLength) - ((float) (this.mItemSize * i)));
                    } else if (this.mSelected - i >= 0) {
                        drawItem(canvas, this.mData, position, -i, this.mMoveLength, (((float) this.mCenterPoint) + this.mMoveLength) - ((float) (this.mItemSize * i)));
                    }
                }
                if (this.mDrawAllItem || i <= this.mVisibleItemCount - this.mCenterPosition) {
                    int position2 = this.mSelected + i >= this.mData.size() ? (this.mSelected + i) - this.mData.size() : this.mSelected + i;
                    if (this.mIsCirculation) {
                        drawItem(canvas, this.mData, position2, i, this.mMoveLength, ((float) this.mCenterPoint) + this.mMoveLength + ((float) (this.mItemSize * i)));
                    } else if (this.mSelected + i < this.mData.size()) {
                        drawItem(canvas, this.mData, position2, i, this.mMoveLength, ((float) this.mCenterPoint) + this.mMoveLength + ((float) (this.mItemSize * i)));
                    }
                }
            }
            drawItem(canvas, this.mData, this.mSelected, 0, this.mMoveLength, ((float) this.mCenterPoint) + this.mMoveLength);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        reset();
    }

    private void reset() {
        if (this.mCenterPosition < 0) {
            this.mCenterPosition = this.mVisibleItemCount / 2;
        }
        if (this.mIsHorizontal) {
            this.mItemHeight = getMeasuredHeight();
            this.mItemWidth = getMeasuredWidth() / this.mVisibleItemCount;
            this.mCenterY = 0;
            this.mCenterX = this.mCenterPosition * this.mItemWidth;
            this.mItemSize = this.mItemWidth;
            this.mCenterPoint = this.mCenterX;
        } else {
            this.mItemHeight = getMeasuredHeight() / this.mVisibleItemCount;
            this.mItemWidth = getMeasuredWidth();
            this.mCenterY = this.mCenterPosition * this.mItemHeight;
            this.mCenterX = 0;
            this.mItemSize = this.mItemHeight;
            this.mCenterPoint = this.mCenterY;
        }
        if (this.mCenterItemBackground != null) {
            this.mCenterItemBackground.setBounds(this.mCenterX, this.mCenterY, this.mCenterX + this.mItemWidth, this.mCenterY + this.mItemHeight);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mDisallowTouch && !this.mGestureDetector.onTouchEvent(event)) {
            switch (event.getActionMasked()) {
                case 1:
                    this.mLastMoveY = event.getY();
                    this.mLastMoveX = event.getX();
                    moveToCenter();
                    break;
                case 2:
                    if (this.mIsHorizontal) {
                        if (Math.abs(event.getX() - this.mLastMoveX) >= 0.1f) {
                            this.mMoveLength += event.getX() - this.mLastMoveX;
                        }
                    } else if (Math.abs(event.getY() - this.mLastMoveY) >= 0.1f) {
                        this.mMoveLength += event.getY() - this.mLastMoveY;
                    }
                    this.mLastMoveY = event.getY();
                    this.mLastMoveX = event.getX();
                    checkCirculation();
                    invalidate();
                    break;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void computeScroll(int curr, int end, float rate) {
        if (rate < 1.0f) {
            if (this.mIsHorizontal) {
                this.mMoveLength = (this.mMoveLength + ((float) curr)) - ((float) this.mLastScrollX);
                this.mLastScrollX = curr;
            } else {
                this.mMoveLength = (this.mMoveLength + ((float) curr)) - ((float) this.mLastScrollY);
                this.mLastScrollY = curr;
            }
            checkCirculation();
            invalidate();
            return;
        }
        this.mIsMovingCenter = false;
        this.mLastScrollY = 0;
        this.mLastScrollX = 0;
        if (this.mMoveLength > 0.0f) {
            if (this.mMoveLength < ((float) (this.mItemSize / 2))) {
                this.mMoveLength = 0.0f;
            } else {
                this.mMoveLength = (float) this.mItemSize;
            }
        } else if ((-this.mMoveLength) < ((float) (this.mItemSize / 2))) {
            this.mMoveLength = 0.0f;
        } else {
            this.mMoveLength = (float) (-this.mItemSize);
        }
        checkCirculation();
        this.mMoveLength = 0.0f;
        this.mLastScrollY = 0;
        this.mLastScrollX = 0;
        notifySelected();
        invalidate();
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            if (this.mIsHorizontal) {
                this.mMoveLength = (this.mMoveLength + ((float) this.mScroller.getCurrX())) - ((float) this.mLastScrollX);
            } else {
                this.mMoveLength = (this.mMoveLength + ((float) this.mScroller.getCurrY())) - ((float) this.mLastScrollY);
            }
            this.mLastScrollY = this.mScroller.getCurrY();
            this.mLastScrollX = this.mScroller.getCurrX();
            checkCirculation();
            invalidate();
        } else if (this.mIsFling) {
            this.mIsFling = false;
            moveToCenter();
        } else if (this.mIsMovingCenter) {
            this.mMoveLength = 0.0f;
            this.mIsMovingCenter = false;
            this.mLastScrollY = 0;
            this.mLastScrollX = 0;
            notifySelected();
        }
    }

    public void cancelScroll() {
        this.mLastScrollY = 0;
        this.mLastScrollX = 0;
        this.mIsMovingCenter = false;
        this.mIsFling = false;
        this.mScroller.abortAnimation();
        stopAutoScroll();
    }

    private void checkCirculation() {
        if (this.mMoveLength >= ((float) this.mItemSize)) {
            this.mSelected -= (int) (this.mMoveLength / ((float) this.mItemSize));
            if (this.mSelected >= 0) {
                this.mMoveLength = (this.mMoveLength - ((float) this.mItemSize)) % ((float) this.mItemSize);
            } else if (this.mIsCirculation) {
                do {
                    this.mSelected = this.mData.size() + this.mSelected;
                } while (this.mSelected < 0);
                this.mMoveLength = (this.mMoveLength - ((float) this.mItemSize)) % ((float) this.mItemSize);
            } else {
                this.mSelected = 0;
                this.mMoveLength = (float) this.mItemSize;
                if (this.mIsFling) {
                    this.mScroller.forceFinished(true);
                }
                if (this.mIsMovingCenter) {
                    scroll(this.mMoveLength, 0);
                }
            }
        } else if (this.mMoveLength <= ((float) (-this.mItemSize))) {
            this.mSelected += (int) ((-this.mMoveLength) / ((float) this.mItemSize));
            if (this.mSelected < this.mData.size()) {
                this.mMoveLength = (this.mMoveLength + ((float) this.mItemSize)) % ((float) this.mItemSize);
            } else if (this.mIsCirculation) {
                do {
                    this.mSelected -= this.mData.size();
                } while (this.mSelected >= this.mData.size());
                this.mMoveLength = (this.mMoveLength + ((float) this.mItemSize)) % ((float) this.mItemSize);
            } else {
                this.mSelected = this.mData.size() - 1;
                this.mMoveLength = (float) (-this.mItemSize);
                if (this.mIsFling) {
                    this.mScroller.forceFinished(true);
                }
                if (this.mIsMovingCenter) {
                    scroll(this.mMoveLength, 0);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void moveToCenter() {
        if (this.mScroller.isFinished() && !this.mIsFling && this.mMoveLength != 0.0f) {
            cancelScroll();
            if (this.mMoveLength > 0.0f) {
                if (this.mIsHorizontal) {
                    if (this.mMoveLength < ((float) (this.mItemWidth / 2))) {
                        scroll(this.mMoveLength, 0);
                    } else {
                        scroll(this.mMoveLength, this.mItemWidth);
                    }
                } else if (this.mMoveLength < ((float) (this.mItemHeight / 2))) {
                    scroll(this.mMoveLength, 0);
                } else {
                    scroll(this.mMoveLength, this.mItemHeight);
                }
            } else if (this.mIsHorizontal) {
                if ((-this.mMoveLength) < ((float) (this.mItemWidth / 2))) {
                    scroll(this.mMoveLength, 0);
                } else {
                    scroll(this.mMoveLength, -this.mItemWidth);
                }
            } else if ((-this.mMoveLength) < ((float) (this.mItemHeight / 2))) {
                scroll(this.mMoveLength, 0);
            } else {
                scroll(this.mMoveLength, -this.mItemHeight);
            }
        }
    }

    private void scroll(float from, int to) {
        if (this.mIsHorizontal) {
            this.mLastScrollX = (int) from;
            this.mIsMovingCenter = true;
            this.mScroller.startScroll((int) from, 0, 0, 0);
            this.mScroller.setFinalX(to);
        } else {
            this.mLastScrollY = (int) from;
            this.mIsMovingCenter = true;
            this.mScroller.startScroll(0, (int) from, 0, 0);
            this.mScroller.setFinalY(to);
        }
        invalidate();
    }

    /* access modifiers changed from: private */
    public void fling(float from, float vel) {
        if (this.mIsHorizontal) {
            this.mLastScrollX = (int) from;
            this.mIsFling = true;
            this.mScroller.fling((int) from, 0, (int) vel, 0, this.mItemWidth * -10, this.mItemWidth * 10, 0, 0);
        } else {
            this.mLastScrollY = (int) from;
            this.mIsFling = true;
            this.mScroller.fling(0, (int) from, 0, (int) vel, 0, 0, this.mItemHeight * -10, this.mItemHeight * 10);
        }
        invalidate();
    }

    private void notifySelected() {
        if (this.mListener != null) {
            post(new Runnable() {
                public void run() {
                    ScrollPickerView.this.mListener.onSelected(ScrollPickerView.this, ScrollPickerView.this.mSelected);
                }
            });
        }
    }

    public void autoScrollFast(int position, long duration, float speed, Interpolator interpolator) {
        final int end;
        if (!this.mIsAutoScrolling && this.mIsCirculation) {
            cancelScroll();
            this.mIsAutoScrolling = true;
            int length = (int) (((float) duration) * speed);
            int circle = (int) (((((float) length) * 1.0f) / ((float) (this.mData.size() * this.mItemSize))) + 0.5f);
            if (circle <= 0) {
                circle = 1;
            }
            int aPlan = (this.mData.size() * circle * this.mItemSize) + ((this.mSelected - position) * this.mItemSize);
            int bPlan = aPlan + (this.mData.size() * this.mItemSize);
            if (Math.abs(length - aPlan) < Math.abs(length - bPlan)) {
                end = aPlan;
            } else {
                end = bPlan;
            }
            this.mAutoScrollAnimator.cancel();
            this.mAutoScrollAnimator.setIntValues(new int[]{0, end});
            this.mAutoScrollAnimator.setInterpolator(interpolator);
            this.mAutoScrollAnimator.setDuration(duration);
            this.mAutoScrollAnimator.removeAllUpdateListeners();
            if (end != 0) {
                this.mAutoScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ScrollPickerView.this.computeScroll(((Integer) animation.getAnimatedValue()).intValue(), end, (((float) animation.getCurrentPlayTime()) * 1.0f) / ((float) animation.getDuration()));
                    }
                });
                this.mAutoScrollAnimator.removeAllListeners();
                this.mAutoScrollAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        boolean unused = ScrollPickerView.this.mIsAutoScrolling = false;
                    }
                });
                this.mAutoScrollAnimator.start();
                return;
            }
            computeScroll(end, end, 1.0f);
            this.mIsAutoScrolling = false;
        }
    }

    public void autoScrollFast(int position, long duration) {
        autoScrollFast(position, duration, (float) dip2px(0.6f), sAutoScrollInterpolator);
    }

    public void autoScrollFast(int position, long duration, float speed) {
        autoScrollFast(position, duration, speed, sAutoScrollInterpolator);
    }

    public void autoScrollToPosition(int toPosition, long duration, Interpolator interpolator) {
        autoScrollTo((this.mSelected - (toPosition % this.mData.size())) * this.mItemHeight, duration, interpolator, false);
    }

    public void autoScrollTo(final int endY, long duration, Interpolator interpolator, boolean canIntercept) {
        if (!this.mIsAutoScrolling) {
            final boolean temp = this.mDisallowTouch;
            this.mDisallowTouch = !canIntercept;
            this.mIsAutoScrolling = true;
            this.mAutoScrollAnimator.cancel();
            this.mAutoScrollAnimator.setIntValues(new int[]{0, endY});
            this.mAutoScrollAnimator.setInterpolator(interpolator);
            this.mAutoScrollAnimator.setDuration(duration);
            this.mAutoScrollAnimator.removeAllUpdateListeners();
            this.mAutoScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    ScrollPickerView.this.computeScroll(((Integer) animation.getAnimatedValue()).intValue(), endY, (((float) animation.getCurrentPlayTime()) * 1.0f) / ((float) animation.getDuration()));
                }
            });
            this.mAutoScrollAnimator.removeAllListeners();
            this.mAutoScrollAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    boolean unused = ScrollPickerView.this.mIsAutoScrolling = false;
                    boolean unused2 = ScrollPickerView.this.mDisallowTouch = temp;
                }
            });
            this.mAutoScrollAnimator.start();
        }
    }

    public void stopAutoScroll() {
        this.mIsAutoScrolling = false;
        this.mAutoScrollAnimator.cancel();
    }

    private static class SlotInterpolator implements Interpolator {
        private SlotInterpolator() {
        }

        public float getInterpolation(float input) {
            return ((float) (Math.cos(((double) (1.0f + input)) * 3.141592653589793d) / 2.0d)) + 0.5f;
        }
    }

    private class FlingOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean mIsScrollingLastTime;

        private FlingOnGestureListener() {
            this.mIsScrollingLastTime = false;
        }

        public boolean onDown(MotionEvent e) {
            ViewParent parent;
            if (ScrollPickerView.this.mDisallowInterceptTouch && (parent = ScrollPickerView.this.getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            this.mIsScrollingLastTime = ScrollPickerView.this.isScrolling();
            ScrollPickerView.this.cancelScroll();
            float unused = ScrollPickerView.this.mLastMoveY = e.getY();
            float unused2 = ScrollPickerView.this.mLastMoveX = e.getX();
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!ScrollPickerView.this.mIsInertiaScroll) {
                return true;
            }
            ScrollPickerView.this.cancelScroll();
            if (ScrollPickerView.this.mIsHorizontal) {
                ScrollPickerView.this.fling(ScrollPickerView.this.mMoveLength, velocityX);
                return true;
            }
            ScrollPickerView.this.fling(ScrollPickerView.this.mMoveLength, velocityY);
            return true;
        }

        public boolean onSingleTapUp(MotionEvent e) {
            float lastMove;
            float unused = ScrollPickerView.this.mLastMoveY = e.getY();
            float unused2 = ScrollPickerView.this.mLastMoveX = e.getX();
            if (ScrollPickerView.this.isHorizontal()) {
                int unused3 = ScrollPickerView.this.mCenterPoint = ScrollPickerView.this.mCenterX;
                lastMove = ScrollPickerView.this.mLastMoveX;
            } else {
                int unused4 = ScrollPickerView.this.mCenterPoint = ScrollPickerView.this.mCenterY;
                lastMove = ScrollPickerView.this.mLastMoveY;
            }
            if (!ScrollPickerView.this.mCanTap || ScrollPickerView.this.isScrolling() || this.mIsScrollingLastTime) {
                ScrollPickerView.this.moveToCenter();
                return true;
            } else if (lastMove >= ((float) ScrollPickerView.this.mCenterPoint) && lastMove <= ((float) (ScrollPickerView.this.mCenterPoint + ScrollPickerView.this.mItemSize))) {
                ScrollPickerView.this.performClick();
                return true;
            } else if (lastMove < ((float) ScrollPickerView.this.mCenterPoint)) {
                ScrollPickerView.this.autoScrollTo(ScrollPickerView.this.mItemSize, 150, ScrollPickerView.sAutoScrollInterpolator, false);
                return true;
            } else if (lastMove > ((float) (ScrollPickerView.this.mCenterPoint + ScrollPickerView.this.mItemSize))) {
                ScrollPickerView.this.autoScrollTo(-ScrollPickerView.this.mItemSize, 150, ScrollPickerView.sAutoScrollInterpolator, false);
                return true;
            } else {
                ScrollPickerView.this.moveToCenter();
                return true;
            }
        }
    }

    public List<T> getData() {
        return this.mData;
    }

    public void setData(List<T> data) {
        if (data == null) {
            this.mData = new ArrayList();
        } else {
            this.mData = data;
        }
        this.mSelected = 0;
        invalidate();
    }

    public T getSelectedItem() {
        return this.mData.get(this.mSelected);
    }

    public int getSelectedPosition() {
        return this.mSelected;
    }

    public void setSelectedPosition(int position) {
        if (position >= 0 && position <= this.mData.size() - 1 && position != this.mSelected) {
            this.mSelected = position;
            invalidate();
            if (this.mListener != null) {
                notifySelected();
            }
        }
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.mListener = listener;
    }

    public OnSelectedListener getListener() {
        return this.mListener;
    }

    public boolean isInertiaScroll() {
        return this.mIsInertiaScroll;
    }

    public void setInertiaScroll(boolean inertiaScroll) {
        this.mIsInertiaScroll = inertiaScroll;
    }

    public boolean isIsCirculation() {
        return this.mIsCirculation;
    }

    public void setIsCirculation(boolean isCirculation) {
        this.mIsCirculation = isCirculation;
    }

    public boolean isDisallowInterceptTouch() {
        return this.mDisallowInterceptTouch;
    }

    public int getVisibleItemCount() {
        return this.mVisibleItemCount;
    }

    public void setVisibleItemCount(int visibleItemCount) {
        this.mVisibleItemCount = visibleItemCount;
        reset();
        invalidate();
    }

    public void setDisallowInterceptTouch(boolean disallowInterceptTouch) {
        this.mDisallowInterceptTouch = disallowInterceptTouch;
    }

    public int getItemHeight() {
        return this.mItemHeight;
    }

    public int getItemWidth() {
        return this.mItemWidth;
    }

    public int getItemSize() {
        return this.mItemSize;
    }

    public int getCenterX() {
        return this.mCenterX;
    }

    public int getCenterY() {
        return this.mCenterY;
    }

    public int getCenterPoint() {
        return this.mCenterPoint;
    }

    public boolean isDisallowTouch() {
        return this.mDisallowTouch;
    }

    public void setDisallowTouch(boolean disallowTouch) {
        this.mDisallowTouch = disallowTouch;
    }

    public void setCenterPosition(int centerPosition) {
        if (centerPosition < 0) {
            this.mCenterPosition = 0;
        } else if (centerPosition >= this.mVisibleItemCount) {
            this.mCenterPosition = this.mVisibleItemCount - 1;
        } else {
            this.mCenterPosition = centerPosition;
        }
        this.mCenterY = this.mCenterPosition * this.mItemHeight;
        invalidate();
    }

    public int getCenterPosition() {
        return this.mCenterPosition;
    }

    public void setCenterItemBackground(Drawable centerItemBackground) {
        this.mCenterItemBackground = centerItemBackground;
        this.mCenterItemBackground.setBounds(this.mCenterX, this.mCenterY, this.mCenterX + this.mItemWidth, this.mCenterY + this.mItemHeight);
        invalidate();
    }

    public void setCenterItemBackground(int centerItemBackgroundColor) {
        this.mCenterItemBackground = new ColorDrawable(centerItemBackgroundColor);
        this.mCenterItemBackground.setBounds(this.mCenterX, this.mCenterY, this.mCenterX + this.mItemWidth, this.mCenterY + this.mItemHeight);
        invalidate();
    }

    public Drawable getCenterItemBackground() {
        return this.mCenterItemBackground;
    }

    public boolean isScrolling() {
        return this.mIsFling || this.mIsMovingCenter || this.mIsAutoScrolling;
    }

    public boolean isFling() {
        return this.mIsFling;
    }

    public boolean isMovingCenter() {
        return this.mIsMovingCenter;
    }

    public boolean isAutoScrolling() {
        return this.mIsAutoScrolling;
    }

    public boolean isCanTap() {
        return this.mCanTap;
    }

    public void setCanTap(boolean canTap) {
        this.mCanTap = canTap;
    }

    public boolean isHorizontal() {
        return this.mIsHorizontal;
    }

    public boolean isVertical() {
        return !this.mIsHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        if (this.mIsHorizontal != horizontal) {
            this.mIsHorizontal = horizontal;
            reset();
            if (this.mIsHorizontal) {
                this.mItemSize = this.mItemWidth;
            } else {
                this.mItemSize = this.mItemHeight;
            }
            invalidate();
        }
    }

    public void setVertical(boolean vertical) {
        boolean z;
        boolean z2 = true;
        boolean z3 = this.mIsHorizontal;
        if (!vertical) {
            z = true;
        } else {
            z = false;
        }
        if (z3 != z) {
            if (vertical) {
                z2 = false;
            }
            this.mIsHorizontal = z2;
            reset();
            if (this.mIsHorizontal) {
                this.mItemSize = this.mItemWidth;
            } else {
                this.mItemSize = this.mItemHeight;
            }
            invalidate();
        }
    }

    public boolean isDrawAllItem() {
        return this.mDrawAllItem;
    }

    public void setDrawAllItem(boolean drawAllItem) {
        this.mDrawAllItem = drawAllItem;
    }

    public int dip2px(float dipVlue) {
        return (int) ((dipVlue * getContext().getResources().getDisplayMetrics().density) + 0.5f);
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == 0) {
            moveToCenter();
        }
    }
}
