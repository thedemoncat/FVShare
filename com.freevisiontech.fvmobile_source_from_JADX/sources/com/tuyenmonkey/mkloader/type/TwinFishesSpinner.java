package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import com.tuyenmonkey.mkloader.model.Circle;

public class TwinFishesSpinner extends LoaderView {
    private Circle[] circles;
    private int numberOfCircle = 10;
    /* access modifiers changed from: private */
    public float[] rotates = new float[this.numberOfCircle];

    public void initializeObjects() {
        float size = (float) Math.min(this.width, this.height);
        float circleRadius = size / 10.0f;
        this.circles = new Circle[this.numberOfCircle];
        for (int i = 0; i < this.numberOfCircle / 2; i++) {
            this.circles[i] = new Circle();
            this.circles[i].setCenter(this.center.x, circleRadius);
            this.circles[i].setColor(this.color);
            this.circles[i].setRadius(circleRadius - ((((float) i) * circleRadius) / 6.0f));
        }
        for (int i2 = this.numberOfCircle / 2; i2 < this.numberOfCircle; i2++) {
            this.circles[i2] = new Circle();
            this.circles[i2].setCenter(this.center.x, size - circleRadius);
            this.circles[i2].setColor(this.color);
            this.circles[i2].setRadius(circleRadius - ((((float) (i2 - 5)) * circleRadius) / 6.0f));
        }
    }

    public void setUpAnimation() {
        int i;
        for (int i2 = 0; i2 < this.numberOfCircle; i2++) {
            final int index = i2;
            ValueAnimator fadeAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 360.0f});
            fadeAnimator.setRepeatCount(-1);
            fadeAnimator.setDuration(1700);
            if (index >= 5) {
                i = index - 5;
            } else {
                i = index;
            }
            fadeAnimator.setStartDelay((long) (i * 100));
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    TwinFishesSpinner.this.rotates[index] = ((Float) animation.getAnimatedValue()).floatValue();
                    if (TwinFishesSpinner.this.invalidateListener != null) {
                        TwinFishesSpinner.this.invalidateListener.reDraw();
                    }
                }
            });
            fadeAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfCircle; i++) {
            canvas.save();
            canvas.rotate(this.rotates[i], this.center.x, this.center.y);
            this.circles[i].draw(canvas);
            canvas.restore();
        }
    }
}
