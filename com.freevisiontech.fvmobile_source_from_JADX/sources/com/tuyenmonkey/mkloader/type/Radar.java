package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.PointF;
import com.tuyenmonkey.mkloader.model.Line;

public class Radar extends LoaderView {
    /* access modifiers changed from: private */
    public float degree;
    private Line line;

    public void initializeObjects() {
        float size = (float) Math.min(this.width, this.height);
        this.line = new Line();
        this.line.setPoint1(this.center);
        this.line.setPoint2(new PointF(0.0f, size / 2.0f));
        this.line.setColor(this.color);
        this.line.setWidth(5.0f);
    }

    public void setUpAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0f, 359.0f});
        animator.setDuration(1000);
        animator.setRepeatCount(-1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float unused = Radar.this.degree = ((Float) animation.getAnimatedValue()).floatValue();
                if (Radar.this.invalidateListener != null) {
                    Radar.this.invalidateListener.reDraw();
                }
            }
        });
        animator.start();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(this.degree, this.center.x, this.center.y);
        this.line.draw(canvas);
        canvas.restore();
    }
}
