package p008jp.p009co.cyberagent.android.gpuimage;

import java.util.List;

/* renamed from: jp.co.cyberagent.android.gpuimage.GPUImageTwoPassFilter */
public class GPUImageTwoPassFilter extends GPUImageFilterGroup {
    public GPUImageTwoPassFilter(String firstVertexShader, String firstFragmentShader, String secondVertexShader, String secondFragmentShader) {
        super((List<GPUImageFilter>) null);
        addFilter(new GPUImageFilter(firstVertexShader, firstFragmentShader));
        addFilter(new GPUImageFilter(secondVertexShader, secondFragmentShader));
    }
}
