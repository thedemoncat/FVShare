package p008jp.p009co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageMixBlendFilter */
public class GPUImageMixBlendFilter extends GPUImageTwoInputFilter {
    private float mMix;
    private int mMixLocation;

    public GPUImageMixBlendFilter(String fragmentShader) {
        this(fragmentShader, 0.5f);
    }

    public GPUImageMixBlendFilter(String fragmentShader, float mix) {
        super(fragmentShader);
        this.mMix = mix;
    }

    public void onInit() {
        super.onInit();
        this.mMixLocation = GLES20.glGetUniformLocation(getProgram(), "mixturePercent");
    }

    public void onInitialized() {
        super.onInitialized();
        setMix(this.mMix);
    }

    public void setMix(float mix) {
        this.mMix = mix;
        setFloat(this.mMixLocation, this.mMix);
    }
}
