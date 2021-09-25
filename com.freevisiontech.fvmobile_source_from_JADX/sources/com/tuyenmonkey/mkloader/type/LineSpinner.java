package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.PointF;
import com.tuyenmonkey.mkloader.model.Line;

public class LineSpinner extends LoaderView {
    /* access modifiers changed from: private */
    public Line[] lines;
    private int numberOfLine = 8;

    public void initializeObjects() {
        int size = Math.min(this.width, this.height);
        float lineWidth = ((float) size) / 10.0f;
        this.lines = new Line[this.numberOfLine];
        for (int i = 0; i < this.numberOfLine; i++) {
            this.lines[i] = new Line();
            this.lines[i].setColor(this.color);
            this.lines[i].setAlpha(126);
            this.lines[i].setWidth(lineWidth);
            this.lines[i].setPoint1(new PointF(this.center.x, (this.center.y - (((float) size) / 2.0f)) + lineWidth));
            this.lines[i].setPoint2(new PointF(this.center.x, this.lines[i].getPoint1().y + (2.0f * lineWidth)));
        }
    }

    public void setUpAnimation() {
        for (int i = 0; i < this.numberOfLine; i++) {
            final int index = i;
            ValueAnimator fadeAnimator = ValueAnimator.ofInt(new int[]{126, 255, 126});
            fadeAnimator.setRepeatCount(-1);
            fadeAnimator.setDuration(1000);
            fadeAnimator.setStartDelay((long) (index * 120));
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    LineSpinner.this.lines[index].setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                    if (LineSpinner.this.invalidateListener != null) {
                        LineSpinner.this.invalidateListener.reDraw();
                    }
                }
            });
            fadeAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfLine; i++) {
            canvas.save();
            canvas.rotate((float) (i * 45), this.center.x, this.center.y);
            this.lines[i].draw(canvas);
            canvas.restore();
        }
    }
}
