package android.support.p003v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.RequiresApi;
import android.support.p003v7.appcompat.C0777R;
import android.util.AttributeSet;
import android.widget.SeekBar;

/* renamed from: android.support.v7.widget.AppCompatSeekBar */
public class AppCompatSeekBar extends SeekBar {
    private final AppCompatSeekBarHelper mAppCompatSeekBarHelper;

    public AppCompatSeekBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public AppCompatSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, C0777R.attr.seekBarStyle);
    }

    public AppCompatSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mAppCompatSeekBarHelper = new AppCompatSeekBarHelper(this);
        this.mAppCompatSeekBarHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mAppCompatSeekBarHelper.drawTickMarks(canvas);
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        this.mAppCompatSeekBarHelper.drawableStateChanged();
    }

    @RequiresApi(11)
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.mAppCompatSeekBarHelper.jumpDrawablesToCurrentState();
    }
}
