package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.PointF;
import com.tuyenmonkey.mkloader.exception.InvalidNumberOfPulseException;
import com.tuyenmonkey.mkloader.model.Line;

public class Pulse extends LoaderView {
    private float lineDistance;
    private float lineWidth;
    private Line[] lines;
    private int numberOfLines;
    /* access modifiers changed from: private */
    public float[] scaleY;

    public Pulse(int numberOfLines2) throws InvalidNumberOfPulseException {
        if (numberOfLines2 < 3 || numberOfLines2 > 5) {
            throw new InvalidNumberOfPulseException();
        }
        this.numberOfLines = numberOfLines2;
        this.lines = new Line[numberOfLines2];
        this.scaleY = new float[numberOfLines2];
    }

    public void initializeObjects() {
        this.lineWidth = (float) (this.width / (this.numberOfLines * 2));
        this.lineDistance = this.lineWidth / 4.0f;
        float firstX = ((((float) this.width) - ((this.lineWidth * ((float) this.numberOfLines)) + (this.lineDistance * ((float) (this.numberOfLines - 1))))) / 2.0f) + (this.lineWidth / 2.0f);
        for (int i = 0; i < this.numberOfLines; i++) {
            this.lines[i] = new Line();
            this.lines[i].setColor(this.color);
            this.lines[i].setWidth(this.lineWidth);
            this.lines[i].setPoint1(new PointF(firstX, this.center.y - (((float) this.height) / 4.0f)));
            this.lines[i].setPoint2(new PointF(firstX, this.center.y + (((float) this.height) / 4.0f)));
        }
    }

    public void setUpAnimation() {
        for (int i = 0; i < this.numberOfLines; i++) {
            final int index = i;
            ValueAnimator scaleAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 1.5f, 1.0f});
            scaleAnimator.setDuration(1000);
            scaleAnimator.setStartDelay((long) (i * 120));
            scaleAnimator.setRepeatCount(-1);
            scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Pulse.this.scaleY[index] = ((Float) animation.getAnimatedValue()).floatValue();
                    if (Pulse.this.invalidateListener != null) {
                        Pulse.this.invalidateListener.reDraw();
                    }
                }
            });
            scaleAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfLines; i++) {
            canvas.save();
            canvas.translate(((float) i) * (this.lineWidth + this.lineDistance), 0.0f);
            canvas.scale(1.0f, this.scaleY[i], this.lines[i].getPoint1().x, this.center.y);
            this.lines[i].draw(canvas);
            canvas.restore();
        }
    }
}
