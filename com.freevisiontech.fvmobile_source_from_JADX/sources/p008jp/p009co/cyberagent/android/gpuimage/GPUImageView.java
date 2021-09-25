package p008jp.p009co.cyberagent.android.gpuimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.google.android.exoplayer.C1907C;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.IntBuffer;
import java.util.concurrent.Semaphore;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageView */
public class GPUImageView extends FrameLayout {
    private GPUImageFilter mFilter;
    public Size mForceSize = null;
    /* access modifiers changed from: private */
    public GLSurfaceView mGLSurfaceView;
    private GPUImage mGPUImage;
    private float mRatio = 0.0f;
    private SurfaceView mSurfaceView;

    /* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageView$OnPictureSavedListener */
    public interface OnPictureSavedListener {
        void onPictureSaved(Uri uri);
    }

    public GPUImageView(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public GPUImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mGLSurfaceView = new GPUImageGLSurfaceView(context, attrs);
        addView(this.mGLSurfaceView);
        this.mGPUImage = new GPUImage(getContext());
        this.mGPUImage.setGLSurfaceView(this.mGLSurfaceView);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeight;
        int newWidth;
        if (this.mRatio != 0.0f) {
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            int height = View.MeasureSpec.getSize(heightMeasureSpec);
            if (((float) width) / this.mRatio < ((float) height)) {
                newWidth = width;
                newHeight = Math.round(((float) width) / this.mRatio);
            } else {
                newHeight = height;
                newWidth = Math.round(((float) height) * this.mRatio);
            }
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(newWidth, C1907C.ENCODING_PCM_32BIT), View.MeasureSpec.makeMeasureSpec(newHeight, C1907C.ENCODING_PCM_32BIT));
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public GPUImage getGPUImage() {
        return this.mGPUImage;
    }

    public void setBackgroundColor(float red, float green, float blue) {
        this.mGPUImage.setBackgroundColor(red, green, blue);
    }

    public void setRatio(float ratio) {
        this.mRatio = ratio;
        this.mGLSurfaceView.requestLayout();
        this.mGPUImage.deleteImage();
    }

    public void setScaleType(GPUImage.ScaleType scaleType) {
        this.mGPUImage.setScaleType(scaleType);
    }

    public void setRotation(Rotation rotation) {
        this.mGPUImage.setRotation(rotation);
        requestRender();
    }

    public void setFilter(GPUImageFilter filter) {
        this.mFilter = filter;
        this.mGPUImage.setFilter(filter);
        requestRender();
    }

    public GLSurfaceView getGLSurfaceView() {
        return this.mGLSurfaceView;
    }

    public GPUImageFilter getFilter() {
        return this.mFilter;
    }

    public void setImage(Bitmap bitmap) {
        this.mGPUImage.setImage(bitmap);
    }

    public void setImage(Uri uri) {
        this.mGPUImage.setImage(uri);
    }

    public void setImage(File file) {
        this.mGPUImage.setImage(file);
    }

    public void requestRender() {
        this.mGLSurfaceView.requestRender();
    }

    public void saveToPictures(String folderName, String fileName, OnPictureSavedListener listener) {
        new SaveTask(this, folderName, fileName, listener).execute(new Void[0]);
    }

    public void saveToPictures(String folderName, String fileName, int width, int height, OnPictureSavedListener listener) {
        new SaveTask(folderName, fileName, width, height, listener).execute(new Void[0]);
    }

    public Bitmap capture(int width, int height) throws InterruptedException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Do not call this method from the UI thread!");
        }
        this.mForceSize = new Size(width, height);
        final Semaphore waiter = new Semaphore(0);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    GPUImageView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    GPUImageView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                waiter.release();
            }
        });
        post(new Runnable() {
            public void run() {
                GPUImageView.this.addView(new LoadingView(GPUImageView.this.getContext()));
                GPUImageView.this.mGLSurfaceView.requestLayout();
            }
        });
        waiter.acquire();
        this.mGPUImage.runOnGLThread(new Runnable() {
            public void run() {
                waiter.release();
            }
        });
        requestRender();
        waiter.acquire();
        Bitmap bitmap = capture();
        this.mForceSize = null;
        post(new Runnable() {
            public void run() {
                GPUImageView.this.mGLSurfaceView.requestLayout();
            }
        });
        requestRender();
        postDelayed(new Runnable() {
            public void run() {
                GPUImageView.this.removeViewAt(1);
            }
        }, 300);
        return bitmap;
    }

    public Bitmap capture() throws InterruptedException {
        final Semaphore waiter = new Semaphore(0);
        final int width = this.mGLSurfaceView.getMeasuredWidth();
        final int height = this.mGLSurfaceView.getMeasuredHeight();
        final int[] pixelMirroredArray = new int[(width * height)];
        this.mGPUImage.runOnGLThread(new Runnable() {
            public void run() {
                IntBuffer pixelBuffer = IntBuffer.allocate(width * height);
                GLES20.glReadPixels(0, 0, width, height, 6408, 5121, pixelBuffer);
                int[] pixelArray = pixelBuffer.array();
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        pixelMirroredArray[(((height - i) - 1) * width) + j] = pixelArray[(width * i) + j];
                    }
                }
                waiter.release();
            }
        });
        requestRender();
        waiter.acquire();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(pixelMirroredArray));
        return bitmap;
    }

    public void onPause() {
        this.mGLSurfaceView.onPause();
    }

    public void onResume() {
        this.mGLSurfaceView.onResume();
    }

    /* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageView$Size */
    public static class Size {
        int height;
        int width;

        public Size(int width2, int height2) {
            this.width = width2;
            this.height = height2;
        }
    }

    /* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageView$GPUImageGLSurfaceView */
    private class GPUImageGLSurfaceView extends GLSurfaceView {
        public GPUImageGLSurfaceView(Context context) {
            super(context);
        }

        public GPUImageGLSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (GPUImageView.this.mForceSize != null) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(GPUImageView.this.mForceSize.width, C1907C.ENCODING_PCM_32BIT), View.MeasureSpec.makeMeasureSpec(GPUImageView.this.mForceSize.height, C1907C.ENCODING_PCM_32BIT));
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    /* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageView$LoadingView */
    private class LoadingView extends FrameLayout {
        public LoadingView(Context context) {
            super(context);
            init();
        }

        public LoadingView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public LoadingView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        private void init() {
            ProgressBar view = new ProgressBar(getContext());
            view.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
            addView(view);
            setBackgroundColor(-16777216);
        }
    }

    /* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageView$SaveTask */
    private class SaveTask extends AsyncTask<Void, Void, Void> {
        private final String mFileName;
        private final String mFolderName;
        /* access modifiers changed from: private */
        public final Handler mHandler;
        private final int mHeight;
        /* access modifiers changed from: private */
        public final OnPictureSavedListener mListener;
        private final int mWidth;

        public SaveTask(GPUImageView gPUImageView, String folderName, String fileName, OnPictureSavedListener listener) {
            this(folderName, fileName, 0, 0, listener);
        }

        public SaveTask(String folderName, String fileName, int width, int height, OnPictureSavedListener listener) {
            this.mFolderName = folderName;
            this.mFileName = fileName;
            this.mWidth = width;
            this.mHeight = height;
            this.mListener = listener;
            this.mHandler = new Handler();
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... params) {
            try {
                saveImage(this.mFolderName, this.mFileName, this.mWidth != 0 ? GPUImageView.this.capture(this.mWidth, this.mHeight) : GPUImageView.this.capture());
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void saveImage(String folderName, String fileName, Bitmap image) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName + "/" + fileName);
            try {
                file.getParentFile().mkdirs();
                image.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
                MediaScannerConnection.scanFile(GPUImageView.this.getContext(), new String[]{file.toString()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, final Uri uri) {
                        if (SaveTask.this.mListener != null) {
                            SaveTask.this.mHandler.post(new Runnable() {
                                public void run() {
                                    SaveTask.this.mListener.onPictureSaved(uri);
                                }
                            });
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
