package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import com.tuyenmonkey.mkloader.model.Circle;

public class ClassicSpinner extends LoaderView {
    /* access modifiers changed from: private */
    public Circle[] circles;
    private int circlesSize = 8;

    public void initializeObjects() {
        float circleRadius = ((float) Math.min(this.width, this.height)) / 10.0f;
        this.circles = new Circle[this.circlesSize];
        for (int i = 0; i < this.circlesSize; i++) {
            this.circles[i] = new Circle();
            this.circles[i].setCenter(this.center.x, circleRadius);
            this.circles[i].setColor(this.color);
            this.circles[i].setAlpha(126);
            this.circles[i].setRadius(circleRadius);
        }
    }

    public void setUpAnimation() {
        for (int i = 0; i < this.circlesSize; i++) {
            final int index = i;
            ValueAnimator fadeAnimator = ValueAnimator.ofInt(new int[]{126, 255, 126});
            fadeAnimator.setRepeatCount(-1);
            fadeAnimator.setDuration(1000);
            fadeAnimator.setStartDelay((long) (index * 120));
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    ClassicSpinner.this.circles[index].setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                    if (ClassicSpinner.this.invalidateListener != null) {
                        ClassicSpinner.this.invalidateListener.reDraw();
                    }
                }
            });
            fadeAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < this.circlesSize; i++) {
            canvas.save();
            canvas.rotate((float) (i * 45), this.center.x, this.center.y);
            this.circles[i].draw(canvas);
            canvas.restore();
        }
    }
}
