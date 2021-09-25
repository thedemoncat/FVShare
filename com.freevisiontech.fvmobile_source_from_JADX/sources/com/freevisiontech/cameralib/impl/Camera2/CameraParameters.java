package com.freevisiontech.cameralib.impl.Camera2;

import com.freevisiontech.cameralib.AspectRatio;
import com.freevisiontech.cameralib.Size;
import java.util.HashMap;

public class CameraParameters {
    public static HashMap<Size, String> Camera2_Record_Trace_Model_Resoultuion_ExcludeMap_Back = new HashMap<>();
    public static HashMap<Size, String> Camera2_Record_Trace_Model_Resoultuion_ExcludeMap_Front = new HashMap<>();
    private BackCamera backCamera = new BackCamera();
    public int facing = 1;
    private FrontCamera frontCamera = new FrontCamera();
    public int imageFormat = 256;

    static {
        Camera2_Record_Trace_Model_Resoultuion_ExcludeMap_Back.put(new Size(3840, 2160), "clt-al00");
        Camera2_Record_Trace_Model_Resoultuion_ExcludeMap_Front.put(new Size(1920, 1080), "clt-al00");
        Camera2_Record_Trace_Model_Resoultuion_ExcludeMap_Front.put(new Size(1280, 720), "clt-al00");
        Camera2_Record_Trace_Model_Resoultuion_ExcludeMap_Front.put(new Size(720, 480), "clt-al00");
    }

    public class BackCamera {
        public int afmode = 4;
        public int awbmode = 1;
        public int controlmode = 1;
        public int flashmode = 3;
        public int fps = 30;
        public AspectRatio mAspectRatio = null;
        public Size previewframeSize = new Size(1280, 720);
        public Size previewsize = new Size(1920, 1080);
        public int scenemode = 0;
        public Size[] supportedPictureSizes = null;
        public Size[] supportedPreviewSizes = null;
        public Size[] supportedRawPictureSizes = null;
        public Size[] supportedVideoSizes = null;

        public BackCamera() {
        }
    }

    public class FrontCamera {
        public int afmode = 4;
        public int awbmode = 1;
        public int controlmode = 1;
        public int flashmode = 3;
        public int fps = 30;
        public AspectRatio mAspectRatio = null;
        public Size previewframeSize = new Size(1280, 720);
        public Size previewsize = new Size(1280, 720);
        public int scenemode = 0;
        public Size[] supportedPictureSizes = null;
        public Size[] supportedPreviewSizes = null;
        public Size[] supportedRawPictureSizes = null;
        public Size[] supportedVideoSizes = null;

        public FrontCamera() {
        }
    }

    public Size getPreviewSize(int face) {
        if (face != 0) {
            return this.backCamera.previewsize;
        }
        return this.frontCamera.previewsize;
    }

    public void setPreviewSize(int face, Size size) {
        if (face != 0) {
            this.backCamera.previewsize = size;
        } else {
            this.frontCamera.previewsize = size;
        }
    }

    public Size getPreviewFrameSize(int face) {
        if (face != 0) {
            return this.backCamera.previewframeSize;
        }
        return this.frontCamera.previewframeSize;
    }

    public void setPreviewFrameSize(int face, Size size) {
        if (face != 0) {
            this.backCamera.previewframeSize = size;
        } else {
            this.frontCamera.previewframeSize = size;
        }
    }

    public int getFps(int face) {
        if (face != 0) {
            return this.backCamera.fps;
        }
        return this.frontCamera.fps;
    }

    public void setFps(int face, int fps) {
        if (face != 0) {
            this.backCamera.fps = fps;
        } else {
            this.frontCamera.fps = fps;
        }
    }

    public int getAwbMode(int face) {
        if (face != 0) {
            return this.backCamera.awbmode;
        }
        return this.frontCamera.awbmode;
    }

    public void setAwbMode(int face, int value) {
        if (face != 0) {
            this.backCamera.awbmode = value;
        } else {
            this.frontCamera.awbmode = value;
        }
    }

    public int getAfMode(int face) {
        if (face != 0) {
            return this.backCamera.afmode;
        }
        return this.frontCamera.afmode;
    }

    public void setAfMode(int face, int value) {
        if (face != 0) {
            this.backCamera.afmode = value;
        } else {
            this.frontCamera.afmode = value;
        }
    }

    public int getControlMode(int face) {
        if (face != 0) {
            return this.backCamera.controlmode;
        }
        return this.frontCamera.controlmode;
    }

    public void setControlMode(int face, int value) {
        if (face != 0) {
            this.backCamera.controlmode = value;
        } else {
            this.frontCamera.controlmode = value;
        }
    }

    public int getFlashMode(int face) {
        if (face != 0) {
            return this.backCamera.flashmode;
        }
        return this.frontCamera.flashmode;
    }

    public void setFlashMode(int face, int value) {
        if (face != 0) {
            this.backCamera.flashmode = value;
        } else {
            this.frontCamera.flashmode = value;
        }
    }

    public int getSceneMode(int face) {
        if (face == 0) {
            return this.backCamera.scenemode;
        }
        return this.frontCamera.scenemode;
    }

    public void setSceneMode(int face, int value) {
        if (face != 0) {
            this.backCamera.scenemode = value;
        } else {
            this.frontCamera.scenemode = value;
        }
    }

    public Size[] getSupportedVideoSizes(int face) {
        if (face != 0) {
            return this.backCamera.supportedVideoSizes;
        }
        return this.frontCamera.supportedVideoSizes;
    }

    public void setSupportedVideoSizes(int face, Size[] sizes) {
        if (face != 0) {
            this.backCamera.supportedVideoSizes = sizes;
        } else {
            this.frontCamera.supportedVideoSizes = sizes;
        }
    }

    public Size[] getSupportedPictureSizes(int face) {
        if (face != 0) {
            return this.backCamera.supportedPictureSizes;
        }
        return this.frontCamera.supportedPictureSizes;
    }

    public void setSupportedPictureSizes(int face, Size[] sizes) {
        if (face != 0) {
            this.backCamera.supportedPictureSizes = sizes;
        } else {
            this.frontCamera.supportedPictureSizes = sizes;
        }
    }

    public Size[] getSupportedRawPictureSizes(int face) {
        if (face != 0) {
            return this.backCamera.supportedRawPictureSizes;
        }
        return this.frontCamera.supportedRawPictureSizes;
    }

    public void setSupportedRawPictureSizes(int face, Size[] sizes) {
        if (face != 0) {
            this.backCamera.supportedRawPictureSizes = sizes;
        } else {
            this.frontCamera.supportedRawPictureSizes = sizes;
        }
    }

    public Size[] getSupportedPreviewSizes(int face) {
        if (face != 0) {
            return this.backCamera.supportedPreviewSizes;
        }
        return this.frontCamera.supportedPreviewSizes;
    }

    public void setSupportedPrevewSizes(int face, Size[] sizes) {
        if (face != 0) {
            this.backCamera.supportedPreviewSizes = sizes;
        } else {
            this.frontCamera.supportedPreviewSizes = sizes;
        }
    }

    public void setPrevewRatio(int face, AspectRatio ratio) {
        if (face != 0) {
            this.backCamera.mAspectRatio = ratio;
        } else {
            this.frontCamera.mAspectRatio = ratio;
        }
    }

    public AspectRatio getPrevewRatio(int face) {
        if (face != 0) {
            return this.backCamera.mAspectRatio;
        }
        return this.frontCamera.mAspectRatio;
    }
}
