package p008jp.p009co.cyberagent.android.gpuimage;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageSmoothToonFilter */
public class GPUImageSmoothToonFilter extends GPUImageFilterGroup {
    GPUImageGaussianBlurFilter blurFilter = new GPUImageGaussianBlurFilter();
    GPUImageToonFilter toonFilter;

    public GPUImageSmoothToonFilter() {
        addFilter(this.blurFilter);
        this.toonFilter = new GPUImageToonFilter();
        addFilter(this.toonFilter);
        getFilters().add(this.blurFilter);
        setBlurSize(0.5f);
        setThreshold(0.2f);
        setQuantizationLevels(10.0f);
    }

    public void setTexelWidth(float value) {
        this.toonFilter.setTexelWidth(value);
    }

    public void setTexelHeight(float value) {
        this.toonFilter.setTexelHeight(value);
    }

    public void setBlurSize(float value) {
        this.blurFilter.setBlurSize(value);
    }

    public void setThreshold(float value) {
        this.toonFilter.setThreshold(value);
    }

    public void setQuantizationLevels(float value) {
        this.toonFilter.setQuantizationLevels(value);
    }
}
