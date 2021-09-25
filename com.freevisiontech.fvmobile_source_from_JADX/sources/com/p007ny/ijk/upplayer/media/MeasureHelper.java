package com.p007ny.ijk.upplayer.media;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import com.p007ny.ijk.upplayer.C1646R;
import java.lang.ref.WeakReference;

/* renamed from: com.ny.ijk.upplayer.media.MeasureHelper */
public final class MeasureHelper {
    private int mCurrentAspectRatio = 0;
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private WeakReference<View> mWeakView;

    public MeasureHelper(View view) {
        this.mWeakView = new WeakReference<>(view);
    }

    public View getView() {
        if (this.mWeakView == null) {
            return null;
        }
        return (View) this.mWeakView.get();
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        this.mVideoSarNum = videoSarNum;
        this.mVideoSarDen = videoSarDen;
    }

    public void setVideoRotation(int videoRotationDegree) {
        this.mVideoRotationDegree = videoRotationDegree;
    }

    public void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float displayAspectRatio;
        if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
            int tempSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempSpec;
        }
        int width = View.getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = View.getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if (this.mCurrentAspectRatio != 3) {
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
                int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
                int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
                int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
                if (widthSpecMode == Integer.MIN_VALUE && heightSpecMode == Integer.MIN_VALUE) {
                    float specAspectRatio = ((float) widthSpecSize) / ((float) heightSpecSize);
                    switch (this.mCurrentAspectRatio) {
                        case 4:
                            displayAspectRatio = 1.7777778f;
                            if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
                                displayAspectRatio = 1.0f / 1.7777778f;
                                break;
                            }
                        case 5:
                            displayAspectRatio = 1.3333334f;
                            if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
                                displayAspectRatio = 1.0f / 1.3333334f;
                                break;
                            }
                        default:
                            displayAspectRatio = ((float) this.mVideoWidth) / ((float) this.mVideoHeight);
                            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                                displayAspectRatio = (((float) this.mVideoSarNum) * displayAspectRatio) / ((float) this.mVideoSarDen);
                                break;
                            }
                    }
                    boolean shouldBeWider = displayAspectRatio > specAspectRatio;
                    switch (this.mCurrentAspectRatio) {
                        case 0:
                        case 4:
                        case 5:
                            if (!shouldBeWider) {
                                height = heightSpecSize;
                                width = (int) (((float) height) * displayAspectRatio);
                                break;
                            } else {
                                width = widthSpecSize;
                                height = (int) (((float) width) / displayAspectRatio);
                                break;
                            }
                        case 1:
                            if (!shouldBeWider) {
                                width = widthSpecSize;
                                height = (int) (((float) width) / displayAspectRatio);
                                break;
                            } else {
                                height = heightSpecSize;
                                width = (int) (((float) height) * displayAspectRatio);
                                break;
                            }
                        default:
                            if (!shouldBeWider) {
                                height = Math.min(this.mVideoHeight, heightSpecSize);
                                width = (int) (((float) height) * displayAspectRatio);
                                break;
                            } else {
                                width = Math.min(this.mVideoWidth, widthSpecSize);
                                height = (int) (((float) width) / displayAspectRatio);
                                break;
                            }
                    }
                } else if (widthSpecMode == 1073741824 && heightSpecMode == 1073741824) {
                    width = widthSpecSize;
                    height = heightSpecSize;
                    if (this.mVideoWidth * height < this.mVideoHeight * width) {
                        width = (this.mVideoWidth * height) / this.mVideoHeight;
                    } else if (this.mVideoWidth * height > this.mVideoHeight * width) {
                        height = (this.mVideoHeight * width) / this.mVideoWidth;
                    }
                } else if (widthSpecMode == 1073741824) {
                    width = widthSpecSize;
                    height = (this.mVideoHeight * width) / this.mVideoWidth;
                    if (heightSpecMode == Integer.MIN_VALUE && height > heightSpecSize) {
                        height = heightSpecSize;
                    }
                } else if (heightSpecMode == 1073741824) {
                    height = heightSpecSize;
                    width = (this.mVideoWidth * height) / this.mVideoHeight;
                    if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                        width = widthSpecSize;
                    }
                } else {
                    width = this.mVideoWidth;
                    height = this.mVideoHeight;
                    if (heightSpecMode == Integer.MIN_VALUE && height > heightSpecSize) {
                        height = heightSpecSize;
                        width = (this.mVideoWidth * height) / this.mVideoHeight;
                    }
                    if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                        width = widthSpecSize;
                        height = (this.mVideoHeight * width) / this.mVideoWidth;
                    }
                }
            }
        } else {
            width = widthMeasureSpec;
            height = heightMeasureSpec;
        }
        this.mMeasuredWidth = width;
        this.mMeasuredHeight = height;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public void setAspectRatio(int aspectRatio) {
        this.mCurrentAspectRatio = aspectRatio;
    }

    @NonNull
    public static String getAspectRatioText(Context context, int aspectRatio) {
        switch (aspectRatio) {
            case 0:
                return context.getString(C1646R.string.VideoView_ar_aspect_fit_parent);
            case 1:
                return context.getString(C1646R.string.VideoView_ar_aspect_fill_parent);
            case 2:
                return context.getString(C1646R.string.VideoView_ar_aspect_wrap_content);
            case 3:
                return context.getString(C1646R.string.VideoView_ar_match_parent);
            case 4:
                return context.getString(C1646R.string.VideoView_ar_16_9_fit_parent);
            case 5:
                return context.getString(C1646R.string.VideoView_ar_4_3_fit_parent);
            default:
                return context.getString(C1646R.string.N_A);
        }
    }
}
