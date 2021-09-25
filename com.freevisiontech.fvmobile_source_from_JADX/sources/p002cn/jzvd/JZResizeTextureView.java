package p002cn.jzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

/* renamed from: cn.jzvd.JZResizeTextureView */
public class JZResizeTextureView extends TextureView {
    protected static final String TAG = "JZResizeTextureView";
    public int currentVideoHeight;
    public int currentVideoWidth;

    public JZResizeTextureView(Context context) {
        super(context);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    public JZResizeTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    public void setVideoSize(int currentVideoWidth2, int currentVideoHeight2) {
        if (this.currentVideoWidth != currentVideoWidth2 || this.currentVideoHeight != currentVideoHeight2) {
            this.currentVideoWidth = currentVideoWidth2;
            this.currentVideoHeight = currentVideoHeight2;
            requestLayout();
        }
    }

    public void setRotation(float rotation) {
        if (rotation != getRotation()) {
            super.setRotation(rotation);
            requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure  [" + hashCode() + "] ");
        int viewRotation = (int) getRotation();
        int videoWidth = this.currentVideoWidth;
        int videoHeight = this.currentVideoHeight;
        int parentHeight = ((View) getParent()).getMeasuredHeight();
        int parentWidth = ((View) getParent()).getMeasuredWidth();
        if (!(parentWidth == 0 || parentHeight == 0 || videoWidth == 0 || videoHeight == 0 || JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE != 1)) {
            if (viewRotation == 90 || viewRotation == 270) {
                int tempSize = parentWidth;
                parentWidth = parentHeight;
                parentHeight = tempSize;
            }
            videoHeight = (videoWidth * parentHeight) / parentWidth;
        }
        if (viewRotation == 90 || viewRotation == 270) {
            int tempMeasureSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempMeasureSpec;
        }
        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);
        if (videoWidth > 0 && videoHeight > 0) {
            int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
            Log.i(TAG, "widthMeasureSpec  [" + View.MeasureSpec.toString(widthMeasureSpec) + "]");
            Log.i(TAG, "heightMeasureSpec [" + View.MeasureSpec.toString(heightMeasureSpec) + "]");
            if (widthSpecMode == 1073741824 && heightSpecMode == 1073741824) {
                width = widthSpecSize;
                height = heightSpecSize;
                if (videoWidth * height < width * videoHeight) {
                    width = (height * videoWidth) / videoHeight;
                } else if (videoWidth * height > width * videoHeight) {
                    height = (width * videoHeight) / videoWidth;
                }
            } else if (widthSpecMode == 1073741824) {
                width = widthSpecSize;
                height = (width * videoHeight) / videoWidth;
                if (heightSpecMode == Integer.MIN_VALUE && height > heightSpecSize) {
                    height = heightSpecSize;
                    width = (height * videoWidth) / videoHeight;
                }
            } else if (heightSpecMode == 1073741824) {
                height = heightSpecSize;
                width = (height * videoWidth) / videoHeight;
                if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                    width = widthSpecSize;
                    height = (width * videoHeight) / videoWidth;
                }
            } else {
                width = videoWidth;
                height = videoHeight;
                if (heightSpecMode == Integer.MIN_VALUE && height > heightSpecSize) {
                    height = heightSpecSize;
                    width = (height * videoWidth) / videoHeight;
                }
                if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                    width = widthSpecSize;
                    height = (width * videoHeight) / videoWidth;
                }
            }
        }
        if (!(parentWidth == 0 || parentHeight == 0 || videoWidth == 0 || videoHeight == 0)) {
            if (JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE == 3) {
                height = videoHeight;
                width = videoWidth;
            } else if (JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE == 2) {
                if (viewRotation == 90 || viewRotation == 270) {
                    int tempSize2 = parentWidth;
                    parentWidth = parentHeight;
                    parentHeight = tempSize2;
                }
                if (((double) videoHeight) / ((double) videoWidth) > ((double) parentHeight) / ((double) parentWidth)) {
                    height = (int) ((((double) parentWidth) / ((double) width)) * ((double) height));
                    width = parentWidth;
                } else if (((double) videoHeight) / ((double) videoWidth) < ((double) parentHeight) / ((double) parentWidth)) {
                    width = (int) ((((double) parentHeight) / ((double) height)) * ((double) width));
                    height = parentHeight;
                }
            }
        }
        setMeasuredDimension(width, height);
    }
}
