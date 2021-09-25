package p010me.iwf.photopicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.exoplayer.C1907C;

/* renamed from: me.iwf.photopicker.widget.SquareItemLayout */
public class SquareItemLayout extends RelativeLayout {
    public SquareItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareItemLayout(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int widthMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), C1907C.ENCODING_PCM_32BIT);
        super.onMeasure(widthMeasureSpec2, widthMeasureSpec2);
    }
}
