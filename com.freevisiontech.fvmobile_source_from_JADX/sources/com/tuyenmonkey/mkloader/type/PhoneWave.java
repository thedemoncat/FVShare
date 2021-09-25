package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.tuyenmonkey.mkloader.model.Arc;

public class PhoneWave extends LoaderView {
    /* access modifiers changed from: private */
    public Arc[] arcs;
    private int numberOfArc = 3;

    public void initializeObjects() {
        float r = ((float) Math.min(this.width, this.height)) / 2.0f;
        this.arcs = new Arc[this.numberOfArc];
        for (int i = 0; i < this.numberOfArc; i++) {
            float d = (r / 4.0f) + ((((float) i) * r) / 4.0f);
            this.arcs[i] = new Arc();
            this.arcs[i].setColor(this.color);
            this.arcs[i].setAlpha(126);
            this.arcs[i].setOval(new RectF(this.center.x - d, (this.center.y - d) + (r / 3.0f), this.center.x + d, this.center.y + d + (r / 3.0f)));
            this.arcs[i].setStartAngle(225.0f);
            this.arcs[i].setSweepAngle(90.0f);
            this.arcs[i].setStyle(Paint.Style.STROKE);
            this.arcs[i].setWidth(r / 10.0f);
        }
    }

    public void setUpAnimation() {
        for (int i = 0; i < this.numberOfArc; i++) {
            final int index = i;
            ValueAnimator fadeAnimator = ValueAnimator.ofInt(new int[]{126, 255, 126});
            fadeAnimator.setRepeatCount(-1);
            fadeAnimator.setDuration(1000);
            fadeAnimator.setStartDelay((long) (i * 120));
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    PhoneWave.this.arcs[index].setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                    if (PhoneWave.this.invalidateListener != null) {
                        PhoneWave.this.invalidateListener.reDraw();
                    }
                }
            });
            fadeAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfArc; i++) {
            this.arcs[i].draw(canvas);
        }
    }
}
