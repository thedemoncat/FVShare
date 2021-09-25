package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.tuyenmonkey.mkloader.model.Arc;
import com.umeng.analytics.C0015a;
import com.yalantis.ucrop.view.CropImageView;

public class Whirlpool extends LoaderView {
    private Arc[] arcs;
    private int numberOfArc = 3;
    /* access modifiers changed from: private */
    public float[] rotates;

    public void initializeObjects() {
        float r = ((float) Math.min(this.width, this.height)) / 2.0f;
        this.arcs = new Arc[this.numberOfArc];
        this.rotates = new float[this.numberOfArc];
        for (int i = 0; i < this.numberOfArc; i++) {
            float d = (r / 4.0f) + ((((float) i) * r) / 4.0f);
            this.arcs[i] = new Arc();
            this.arcs[i].setColor(this.color);
            this.arcs[i].setOval(new RectF(this.center.x - d, this.center.y - d, this.center.x + d, this.center.y + d));
            this.arcs[i].setStartAngle((float) (i * 45));
            this.arcs[i].setSweepAngle((float) ((i * 45) + 90));
            this.arcs[i].setStyle(Paint.Style.STROKE);
            this.arcs[i].setWidth(r / 10.0f);
        }
    }

    public void setUpAnimation() {
        int i;
        for (int i2 = this.numberOfArc - 1; i2 >= 0; i2--) {
            final int index = i2;
            float[] fArr = new float[2];
            fArr[0] = this.arcs[i2].getStartAngle();
            float startAngle = this.arcs[i2].getStartAngle();
            if (i2 % 2 == 0) {
                i = -1;
            } else {
                i = 1;
            }
            fArr[1] = ((float) (i * C0015a.f29p)) + startAngle;
            ValueAnimator fadeAnimator = ValueAnimator.ofFloat(fArr);
            fadeAnimator.setRepeatCount(-1);
            fadeAnimator.setDuration((long) ((i2 + 1) * CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION));
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Whirlpool.this.rotates[index] = ((Float) animation.getAnimatedValue()).floatValue();
                    if (Whirlpool.this.invalidateListener != null) {
                        Whirlpool.this.invalidateListener.reDraw();
                    }
                }
            });
            fadeAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfArc; i++) {
            canvas.save();
            canvas.rotate(this.rotates[i], this.center.x, this.center.y);
            this.arcs[i].draw(canvas);
            canvas.restore();
        }
    }
}
