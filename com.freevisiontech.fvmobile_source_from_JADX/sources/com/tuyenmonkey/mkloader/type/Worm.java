package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import com.tuyenmonkey.mkloader.model.Circle;

public class Worm extends LoaderView {
    /* access modifiers changed from: private */
    public Circle[] circles;
    private int circlesSize = 5;
    private float radius;
    private int[] transformations = {-2, -1, 0, 1, 2};

    public void initializeObjects() {
        this.circles = new Circle[this.circlesSize];
        this.radius = (((float) this.width) / 10.0f) - (((float) this.width) / 100.0f);
        for (int i = 0; i < this.circlesSize; i++) {
            this.circles[i] = new Circle();
            this.circles[i].setColor(this.color);
            this.circles[i].setRadius(this.radius);
            this.circles[i].setCenter(this.center.x, this.center.y);
        }
    }

    public void setUpAnimation() {
        for (int i = 0; i < this.circlesSize; i++) {
            final int index = i;
            ValueAnimator translateAnimator = ValueAnimator.ofFloat(new float[]{this.center.y, ((float) this.height) / 4.0f, ((float) (this.height * 3)) / 4.0f, this.center.y});
            translateAnimator.setDuration(1000);
            translateAnimator.setStartDelay((long) (index * 120));
            translateAnimator.setRepeatCount(-1);
            translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Worm.this.circles[index].setCenter(Worm.this.center.x, ((Float) animation.getAnimatedValue()).floatValue());
                    if (Worm.this.invalidateListener != null) {
                        Worm.this.invalidateListener.reDraw();
                    }
                }
            });
            translateAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < this.circlesSize; i++) {
            canvas.save();
            canvas.translate(2.0f * this.radius * ((float) this.transformations[i]), 0.0f);
            this.circles[i].draw(canvas);
            canvas.restore();
        }
    }
}
