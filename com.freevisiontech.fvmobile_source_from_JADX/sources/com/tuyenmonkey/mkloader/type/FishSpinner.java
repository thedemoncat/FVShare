package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import com.tuyenmonkey.mkloader.model.Circle;

public class FishSpinner extends LoaderView {
    private Circle[] circles;
    private int numberOfCircle = 5;
    /* access modifiers changed from: private */
    public float[] rotates = new float[this.numberOfCircle];

    public void initializeObjects() {
        float circleRadius = ((float) Math.min(this.width, this.height)) / 10.0f;
        this.circles = new Circle[this.numberOfCircle];
        for (int i = 0; i < this.numberOfCircle; i++) {
            this.circles[i] = new Circle();
            this.circles[i].setCenter(this.center.x, circleRadius);
            this.circles[i].setColor(this.color);
            this.circles[i].setRadius(circleRadius - ((((float) i) * circleRadius) / 6.0f));
        }
    }

    public void setUpAnimation() {
        for (int i = 0; i < this.numberOfCircle; i++) {
            final int index = i;
            ValueAnimator fadeAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 360.0f});
            fadeAnimator.setRepeatCount(-1);
            fadeAnimator.setDuration(1700);
            fadeAnimator.setStartDelay((long) (index * 100));
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    FishSpinner.this.rotates[index] = ((Float) animation.getAnimatedValue()).floatValue();
                    if (FishSpinner.this.invalidateListener != null) {
                        FishSpinner.this.invalidateListener.reDraw();
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
