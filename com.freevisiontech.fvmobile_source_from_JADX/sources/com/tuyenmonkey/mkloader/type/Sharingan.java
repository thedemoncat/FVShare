package com.tuyenmonkey.mkloader.type;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.tuyenmonkey.mkloader.model.Circle;

public class Sharingan extends LoaderView {
    private Circle eye;
    private Circle eyeBound;
    private float eyeBoundRadius;
    private float eyeBoundRadiusScale;
    private int numberOfSharingan = 3;
    /* access modifiers changed from: private */
    public float rotate;
    /* access modifiers changed from: private */
    public float scale;
    private Circle[] sharingans;

    public void initializeObjects() {
        float r = ((float) Math.min(this.width, this.height)) / 2.0f;
        this.eyeBoundRadius = r / 1.5f;
        this.eye = new Circle();
        this.eye.setCenter(this.center.x, this.center.y);
        this.eye.setColor(this.color);
        this.eye.setRadius(r / 4.0f);
        this.eyeBound = new Circle();
        this.eyeBound.setCenter(this.center.x, this.center.y);
        this.eyeBound.setColor(this.color);
        this.eyeBound.setRadius(this.eyeBoundRadius);
        this.eyeBound.setStyle(Paint.Style.STROKE);
        this.eyeBound.setWidth(r / 20.0f);
        this.sharingans = new Circle[this.numberOfSharingan];
        for (int i = 0; i < this.numberOfSharingan; i++) {
            this.sharingans[i] = new Circle();
            this.sharingans[i].setCenter(this.center.x, this.center.y - this.eyeBoundRadius);
            this.sharingans[i].setColor(this.color);
            this.sharingans[i].setRadius(r / 6.0f);
        }
    }

    public void setUpAnimation() {
        ValueAnimator rotateAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 360.0f});
        rotateAnimator.setDuration(1500);
        rotateAnimator.setRepeatCount(-1);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float unused = Sharingan.this.rotate = ((Float) animation.getAnimatedValue()).floatValue();
                if (Sharingan.this.invalidateListener != null) {
                    Sharingan.this.invalidateListener.reDraw();
                }
            }
        });
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.8f, 1.0f});
        scaleAnimator.setDuration(1000);
        scaleAnimator.setRepeatCount(-1);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float unused = Sharingan.this.scale = ((Float) animation.getAnimatedValue()).floatValue();
                if (Sharingan.this.invalidateListener != null) {
                    Sharingan.this.invalidateListener.reDraw();
                }
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotateAnimator).with(scaleAnimator);
        animatorSet.start();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.scale(this.scale, this.scale, this.center.x, this.center.y);
        canvas.rotate(this.rotate, this.center.x, this.center.y);
        this.eye.draw(canvas);
        this.eyeBound.draw(canvas);
        for (int i = 0; i < this.numberOfSharingan; i++) {
            canvas.save();
            canvas.rotate((float) (i * 120), this.center.x, this.center.y);
            this.sharingans[i].draw(canvas);
            canvas.restore();
        }
        canvas.restore();
    }
}
