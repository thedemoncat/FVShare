package com.freevisiontech.cameralib.impl.Camera2;

import android.graphics.Rect;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.hardware.camera2.params.RggbChannelVector;
import android.util.Range;

public class Camera2States {
    Integer aemode = null;
    Integer aestate = null;
    Integer afmode = null;
    Integer afstate = null;
    Integer awbmode = null;
    Integer awbstate = null;
    Integer colorcorrectmode = null;
    Integer controlmode = null;
    ColorSpaceTransform cst = null;
    Integer digitalvideostabmode = 0;

    /* renamed from: ev */
    Integer f1074ev = null;
    Long exposuretime = null;
    Integer flashmode = null;
    Integer flashstate = null;
    Float focalLen = null;
    Float focusdistance = null;
    Range<Integer> fpsranges = null;
    Long frameduration = null;
    RggbChannelVector gain = null;
    Integer iso = null;
    Integer lenstate = null;
    Integer opticalvideostabmode = 0;
    Integer scenemode = null;
    Rect zoom = null;

    public void clear() {
        this.awbmode = null;
        this.afmode = null;
        this.aemode = null;
        this.controlmode = null;
        this.flashmode = null;
        this.scenemode = null;
        this.afstate = null;
        this.aestate = null;
        this.awbstate = null;
        this.flashstate = null;
        this.lenstate = null;
        this.fpsranges = null;
        this.frameduration = null;
        this.iso = null;
        this.exposuretime = null;
        this.f1074ev = null;
        this.focusdistance = null;
        this.zoom = null;
        this.focalLen = null;
        this.gain = null;
        this.cst = null;
        this.colorcorrectmode = null;
        this.opticalvideostabmode = 0;
        this.digitalvideostabmode = 0;
    }
}
